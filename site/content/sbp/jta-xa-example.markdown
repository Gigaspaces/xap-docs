---
type: post
title:  JTA-XA Example
categories: SBP
parent: processing.html
weight: 300
---


{{% tip %}}
**Summary:**  Integrating GigaSpaces with External JMS Server using JTA/XA<br/>
**Author**: Shravan (Sean) Kumar, Solutions Architect, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0.1<br/>
**Last Update**: April 2011<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview

Integrating GigaSpaces with an external JMS Servers is demonstrated in this page. This example shows how GigaSpaces event container can process events and send JMS messages to a external JMS server all under one distributed transaction.

{{% note %}}
 Use of distributed transactions is done as a demonstration. Use this with caution, in production applications this can be expensive and have a performance penalty. Well known patterns like [Idempotent Receiver](http://www.eaipatterns.com/IdempotentReceiver.html) are potential alternatives to distributed transactions.
{{% /note %}}

This example includes:

- GigaSpaces updates and JMS message creation are done under transactions so as to avoid duplicate processing/data loss.
- Apache ActiveMQ is used as a the JMS provider.
- Atomikos is used as the JTA Transaction provider and uses XA protocol.
- Example is based on the GigaSpaces helloworld example included with the product.
- To demonstrate the XA transaction it rollbacks messages with 100 modulo. You will notice that these messages will never appear in the JMS queue and are rolled back on GigaSpaces server.

## Source Code

{{%tabs%}}

{{%tab "  Atomikos Configuration "%}}


```xml
<!-- Construct Atomikos UserTransactionManager, needed to configure Spring -->
<bean id="atomikosTransactionManager" class="com.atomikos.icatch.jta.UserTransactionManager"
	init-method="init" destroy-method="close">
	<property name="forceShutdown" value="false" />
</bean>

<!-- Also use Atomikos UserTransactionImp, needed to configure Spring -->
<bean id="atomikosUserTransaction" class="com.atomikos.icatch.jta.UserTransactionImp">
	<property name="transactionTimeout" value="300" />
</bean>

<bean id="transactionManager"
	class="org.springframework.transaction.jta.JtaTransactionManager">
	<property name="transactionManager">
		<ref local="atomikosTransactionManager" />
	</property>
	<property name="userTransaction">
		<ref local="atomikosUserTransaction" />
	</property>
</bean>
```

{{% /tab %}}

{{%tab "  JMS Configuration "%}}


```xml
<!-- creates an activemq xa connection factory using the amq namespace -->
<bean id="amqConnectionFactory" class="org.apache.activemq.ActiveMQXAConnectionFactory">
        <property name="brokerURL"><value>tcp://localhost:61616</value></property>
</bean>

<bean id="queue" class="org.apache.activemq.command.ActiveMQQueue">
	<property name="physicalName"><value>gigaspaces.helloworld.jms.exampleQueue</value></property>
</bean>

<!-- Configure the JMS connector -->
<bean id="connectionFactory" class="com.atomikos.jms.AtomikosConnectionFactoryBean"
    init-method="init" destroy-method="close">
    <property name="uniqueResourceName" value="MY_QUEUE"/>
    <property name="xaConnectionFactory" ref="amqConnectionFactory" />
    <property name="localTransactionMode" value="false"></property>
</bean>

<!-- JmsTemplate Definition -->
<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
	<property name="connectionFactory" ref="connectionFactory" />
	<property name="sessionTransacted" value="true" />
</bean>
```

{{% /tab %}}

{{%tab "  GigaSpaces Configuration "%}}


```xml
<!-- A bean representing a space (an IJSpace implementation). -->
<os-core:embedded-space id="space" name="processorSpace" />

<!-- A wrapper bean to the space to provide OpenSpaces simplified space
	API (built on top of IJSpace/JavaSpace). -->
<os-core:giga-space id="gigaSpace" space="space"
	tx-manager="transactionManager" />

<!-- A Polling Container bean that performs repeated take operations from
	the space of objects matching a defined template. (The take operations are
	by default blocking, which means a single take operation is waiting until
	a match is found) The template here instructs the polling container to take
	objects of type Message with their "info" attribute set to "Hello ". When
	a match is found, the object is taken and passed to a listener bean - here
	the listener is the previously defined Processor bean. This bean has the
	method processMessage(), which is invoked on the taken object, retuning a
	processed object. After the object is processed, it is written back to the
	space by the Polling Container. -->
<os-events:polling-container id="helloProcessorPollingEventContainer"
	giga-space="gigaSpace">
	<os-events:tx-support tx-manager="transactionManager" />
	<os-core:template>
		<bean class="org.openspaces.example.helloworld.common.Message">
			<property name="info" value="Hello " />
		</bean>
	</os-core:template>
	<os-events:listener>
		<os-events:annotation-adapter>
			<os-events:delegate ref="helloProcessor" />
		</os-events:annotation-adapter>
	</os-events:listener>
</os-events:polling-container>

<!-- The processor bean -->
<bean id="helloProcessor" class="org.openspaces.example.helloworld.processor.Processor">
	<property name="jmsTemplate" ref="jmsTemplate" />
	<property name="queue" ref="queue"/>
</bean>
```

{{% /tab %}}

{{% /tabs %}}

**Processor Bean and Message Bean definitions**

{{%tabs%}}

{{%tab "  Processor bean definition "%}}


```java
package org.openspaces.example.helloworld.processor;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.example.helloworld.common.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * The processor is passed interesting Objects from its associated
 * PollingContainer
 * <p>
 * The PollingContainer removes objects from the GigaSpace that match the
 * criteria specified for it.
 * <p>
 * Once the Processor receives each Object, it modifies its state and returns it
 * to the PollingContainer which writes them back to the GigaSpace
 * <p/>
 * <p>
 * The PollingContainer is configured in the pu.xml file of this project
 */
public class Processor implements InitializingBean {
	Logger logger = Logger.getLogger(this.getClass().getName());
	private JmsTemplate jmsTemplate;
	private Queue queue;

	private int msgCtr;
	private int rollbacks;

	/**
	 * Process the given Message and return it to the caller. This method is
	 * invoked using OpenSpaces Events when a matching event occurs.
	 *
	 * @throws JMSException
	 * @throws Exception
	 */
	@SpaceDataEvent
	public Message processMessage(Message msg) throws Exception {
		logger.info("Processor PROCESSING: " + msg + " MessageCtr: " + ++msgCtr);
		myBusinessLogic(msg);

		try {
			sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		if (msg.getId() % 100 == 0) {
			logger.info("Rolling back: " + msg.getId() + " Rollback Counter: "
					+ ++rollbacks);
			throw new Exception("Rollback");
		}
		return msg;
	}

	private void myBusinessLogic(Message msg) {
		msg.setInfo(msg.getInfo() + "World !! Message id: " + msg.getId());
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public Processor() {
		logger.info("Processor instantiated, waiting for messages feed...");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("In the Processor Bean");
	}

	/**
	 * Send to the queue
	 */
	private void sendMessage(final Message msg) throws IOException,
			JMSException {

		this.jmsTemplate.send(this.queue, new MessageCreator() {
			public javax.jms.Message createMessage(Session session)
					throws JMSException {
				return session.createTextMessage(msg.getInfo());
			}
		});

	}
}
```

{{% /tab %}}

{{%tab "  Message bean definition "%}}


```java
package org.openspaces.example.helloworld.common;

import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * A simple object used to work with the Space.
 */
public class Message  {

    private Integer id;
    private String info;

    /**
     * Necessary Default constructor
     */
    public Message() {
    }

    /**
     * Constructs a new Message with the given id and info
     * and info.
     */
    public Message(Integer id, String info) {
        this.id = id;
        this.info = info;
    }

    /**
     * The id of this message.
     * We will use this attribute to route the message objects when
     * they are written to the space, defined in the Message.gs.xml file.
     */
	@SpaceRouting
    public Integer getId() {
        return id;
    }

    /**
     * The id of this message.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * The information this object holds.
     */
    public String getInfo() {
        return info;
    }

    /**
     * The information this object holds.
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
	 * A simple toString to print out the state.
     */
    public String toString() {
        return "id[" + id + "] info[" + info +"]";
    }
}
```

{{% /tab %}}

{{% /tabs %}}

## Running the example

Step 1. Download Apache ActiveMQ from [here](http://activemq.apache.org/download.html).

Step 2. Download Atomikos TransactionEssentials from [here](http://www.atomikos.com/Main/TransactionsEssentialsDownloadForm).

Step 3. Extract the [example](/attachment_files/sbp/helloworld-jta.zip) archive into a folder (calling it <helloworld-jta>). Modify the setDevEnv.bat and build.properties files to have proper paths for GigaSpaces home, Java home, ActiveMQ home and Atomikos home. Also modify the NIC_ADDR and locators variable to have proper IP address.

Step 4. Open a command shell and navigate to <helloworld-jta> folder.

Step 5. Run `setDevEnv.bat` script in <helloworld-jta> folder, to set the environment variables.

Step 6. Copy the required jars to the <helloworld-jta>\lib folder using the `copy-libs` ant task provided.


```java
 build copy-libs
```

{{% note %}}
 Example was tested using following product versions,

1. GigaSpaces - **8.0.1**

2. Apache ActiveMQ - **5.5**

3. Atomikos TransactionEssentials - **3.7.0**

If you are using different versions please make sure all the equivalent jars are reflected in `copy-libs` ant task
{{% /note %}}

Step 7. Start a gs-ui instance using `gs-ui.bat` script in <helloworld-jta> folder.

Step 8. Run `gs-agent.bat`  <helloworld-jta> folder, to start the GigaSpaces components (GSA,GSM, LUS, GSM).

Step 9. Start the ActiveMQ process using <ActiveMQHome>`\bin\activemq.bat` script.

{{% note %}}
If ActiveMQ is running on another server, please remember to update the brokerURL in `pu.xml`
{{% /note %}}

Step 10. Deploy the processorSpace cluster by running `deploy-processor` ant task.


```java
build deploy-processor
```
Step 11. Run the feeder process using `run-feeder` ant task.

```java
 build run-feeder
```
Step 12. If you check GigaSpaces logs, you will notice that the Message-0 (id=0) is Rolled back and all other messages are processed successfully and sent to JMS server.

{{% note %}}
 Message-0 (id=0) will keep going back to Polling container logic because the space update and JMS message both are rolled back. This is intentionally done, to demonstrate XA.
{{% /note %}}

Step 13. You can validate the JMS messages received by the Queue using a test JMS client included. You can run the client using `jms-client` ant task.

```java
 build jms-client
```

## References

- JTA/XA support information, [Transaction Management]({{%latestjavaurl%}}/transaction-management.html).
- XA transactions using Spring, http://www.javaworld.com/javaworld/jw-04-2007/jw-04-xa.html.
- Distributed transactions in Spring, with and without XA, http://www.javaworld.com/javaworld/jw-01-2009/jw-01-spring-transactions.html.
- Atomikos TransactionEssentials Spring Integration information, http://www.atomikos.com/Documentation/SpringIntegration.
