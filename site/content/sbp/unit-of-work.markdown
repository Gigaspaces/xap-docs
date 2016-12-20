---
type: post
title:  Unit Of Work
categories: SBP
parent: processing.html
weight: 1000
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Shay Hassidim| 8.0 | Feb 2011  |           |          |


# Overview
The Unit of work Pattern - Parallel atomic ordered data processing for associated data objects



# GigaSpaces Unit of Work
GigaSpaces Unit of Work (UOW) enables a stand-alone message producer to group messages into a single unit such that those messages can be handled in order - similar to a FIFO queue localized within a transaction. This single unit is called a Unit-of-work and requires that all messages from that unit be processed **sequentially in the order** they were created (within the unit of work). Other units can be processes in parallel. This approach maximize the system performance and its scalability and allows it to processes vast amount of data consuming memory and CPU resources in a very optimal manner.

The UOW can be used with financial systems to process **trade orders** , in healthcare systems to processes **patient medical data** , with transportation systems to process **reservations** , with airlines systems to process **flight schedule** , with billing system to processes **payments**, etc.

{{% tip %}}
Starting with XAP 9 you may use the **FIFO Grouping** to implement the Unit of Work model. See the [FIFO Grouping]({{%latestjavaurl%}}/fifo-grouping.html) for details.
{{% /tip %}}

# GigaSpaces FIFO and UOW
While the [FIFO]({{%latestjavaurl%}}/fifo-support.html) mode provides ordered object consumption, it does so in a very strict sense. It defines an order between space objects based on the time they were written into the space. FIFO does not take into account consuming associated objects as one atomic operation. UOW allows a polling container to process a group of associated objects in the order they have been written in parallel to other processing groups. Multiple polling containers handle different groups concurrently, each group items processed in a FIFO fashion.

# When can the GigaSpaces Unit of Work be used?
GigaSpaces UOW can be used in the following cases:

- When having **many consumers**, each should handle a different group (number of groups may be unlimited) where the processing of the items within the group should be done in an ordered fashion as **one atomic operation**.
- When having **multiple producers**, where data from each producer may be associated with different groups (number of groups may be unlimited) where the processing of the items within the group should be done in an ordered fashion as **one atomic operation**.

# Example use case
Here is a simple scenario illustrates the Unit of Work usage:

1. Client A starts an Order ID 1 and submits a request to buy $1000 worth of shares of IBM
2. Client A starts an Order ID 2 and submits a request to buy $1000 worth of shares of MSFT
3. Client A resumes Order ID 1 and submits a request to increase the purchase of IBM request by $500
4. Client A resumes Order ID 1 and submits a request to cancel the purchase of IBM shares
5. Client A cancels Order ID 2

With the above scenario requests 1, 3 and 4 should be processed as one atomic operation where requests 2 and 5 can be processed in parallel but also as one atomic operation.

# How is the GigaSpaces Unit of Work configured?

- Multiple polling containers running in the following mode are started:
-- Using `SingleTakeReceiveOperationHandler`.
-- Using one concurrent consumer thread.
-- Consumed objects in a FIFO mode.
-- Template set with a different `bucketId` for each polling container - This ensures **no contention** or **race conditions** will be generated.
-- Using Local Transaction Manager.
- The polling container `SpaceDataEvent` implementation flow:
		1. Transaction started and an object at the top of the FIFO chain is taken.
		2. To consume the entire group, a `takeMultiple` is called using a template with the group identity set. The objects are retrieved in FIFO fashion (in order).
		3. Group is processed.
		4. Transaction is committed.
		5. Other groups are processes in-parallel by other polling containers.

{{% align center %}}
![uow_1.jpg](/attachment_files/sbp/uow_1.jpg)
{{% /align %}}

# UOW Example

##  Running the Example

{{% tip %}}
You can [download](/attachment_files/sbp/uow.zip) eclipse project with example source code, running scripts and configuration.
{{% /tip %}}

{{%tabs%}}

{{%tab "  Running the UOWProcessor within your IDE "%}}
You can run the UOW Data-Grid with the collocated `UOWProcessor` within your IDE using the following configuration:

{{% align center %}}
![uow_3.jpg](/attachment_files/sbp/uow_3.jpg)
{{% /align %}}

Here is a configuration for a UOW Data-Grid with 2 partitions:

{{% align center %}}
![uow_2.jpg](/attachment_files/sbp/uow_2.jpg)
{{% /align %}}

{{% /tab %}}

{{%tab "  Deploying the UOWProcessor into the Service Grid "%}}
Instead of running the UOWProcessor within your IDE, you can deploy it into the Service Grid.

1. Edit the `setExampleEnv.bat` to include correct values for the `NIC_ADDR` variable as your machine IP and the `GS_HOME` variable as the GigaSpaces root folder.
2. Start the Service-Grid


```java
runAgent.bat
```

3. Deploy the UOWProcessor PU


```java
deployUOW.bat
```

This will deploy the UOW Data-Grid with 2 partitions and a backup.
{{% /tab %}}

{{%tab "  Running the UOWFeeder "%}}
You can run the `UOWFeeder` within your IDE using the following configuration:

{{% align center %}}
![uow_4.jpg](/attachment_files/sbp/uow_4.jpg)
{{% /align %}}

or using the following:


```java
runClient.bat
```

{{% /tab %}}

{{% /tabs %}}

## Example Code and Configuration

{{% tip %}}
The bucket count configured via the UOW Data-Grid pu.xml using the BucketConfiguration Bean
{{% /tip %}}

{{%tabs%}}

{{%tab "  The UOWMessage Class "%}}


```java
package com.giagspaces.patterns.uow;

@SpaceClass(fifoSupport = FifoSupport.ALL)
public class UOWMessage {

	private Integer id;
	private String data;
	private String group;
	private Integer buketId;

	public UOWMessage() {
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@SpaceIndex(type = SpaceIndexType.BASIC)
	@SpaceRouting
	public String getGroup() {
		return group;
	}

	public void setGroup(String subject) {
		this.group = subject;
	}

	public Integer getBuketId() {
		return buketId;
	}

	@SpaceIndex(type = SpaceIndexType.BASIC)
	public void setBuketId(Integer buketId) {
		this.buketId = buketId;
	}
}
```

{{% /tab %}}

{{%tab "  The UOWFeeder "%}}

The `buketId` is calculated using the following:


```java
group % bucketsCount
```


```java
package com.giagspaces.patterns.uow;
public class UOWFeederMain {

	static String locators = System.getProperty("locators", "127.0.0.1");
	static String groups = System.getProperty("groups", "gigaspaces-8.0.3-XAPPremium-ga");

	public static void main(String[] args) throws Exception {
		int burstSize = 100;
		int iterations = 20;
		int groupsCount = 20;

		if (args.length > 0) {
			burstSize = Integer.valueOf(args[0]).intValue();
		}

		GigaSpace space = new GigaSpaceConfigurer(new UrlSpaceConfigurer(
				"jini://*/*/space").lookupGroups(groups).lookupLocators(locators)).gigaSpace();

		UOWProcessorService uowService = new ExecutorRemotingProxyConfigurer<UOWProcessorService>(
				space, UOWProcessorService.class).proxy();

		int bucketsCount = uowService.getBucketsCount();
		System.out.println("There are "+ bucketsCount+ " buckets with "+groupsCount + " groups");

		int i = 0;

		UOWMessage messageArry[] = new UOWMessage[burstSize];
		for (int count = 0; count < iterations; count++) {
			for (int j = 0; j < burstSize; j++) {
				UOWMessage m = new UOWMessage();
				m.setData("AA");
				m.setId(i);
				int group = i % groupsCount;
				m.setGroup(group + "");
				m.setBuketId(group % bucketsCount);
				messageArry[j] = m;
				i++;
			}
			space.writeMultiple(messageArry);
			System.out.println("Feeder wrote " + burstSize + " objects:"+ (i-burstSize) + "-"+ (i-1));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
```

{{% /tab %}}

{{%tab "  The UOWProcessorService "%}}


```java
package com.giagspaces.patterns.uow;

public interface UOWProcessorService {
	int getBucketsCount();
}
```

{{% /tab %}}

{{%tab "  The UOWProcessor "%}}


```java
package com.giagspaces.patterns.uow;
public class UOWProcessor {
	int bucketID;
	int partitionID;

	public UOWProcessor(int _bucketID , int _partitionID) {
		this.bucketID = _bucketID;
		this.partitionID = _partitionID;
	}

	@ExceptionHandler
	public EventExceptionHandler exceptionHandler() {
		return new EventExceptionHandler() {

			@Override
			public void onException(ListenerExecutionFailedException exception,
					Object data, GigaSpace gigaSpace,
					TransactionStatus txStatus, Object source)
					throws RuntimeException {
				System.out.println(exception);
				throw exception;
			}

			@Override
			public void onSuccess(Object data, GigaSpace gigaSpace,
					TransactionStatus txStatus, Object source)
					throws RuntimeException {
			}
		};
	}

	@SpaceDataEvent
	public void eventListener(UOWMessage message, GigaSpace space,
			TransactionStatus txStatus) {
		UOWMessage associatedMessagesTemplate = new UOWMessage();
		associatedMessagesTemplate.setGroup(message.getGroup());

		// consume associated Messages
		UOWMessage associatedMessages[] = space.takeMultiple(
				associatedMessagesTemplate, 1000);
		if (associatedMessages.length == 0) {
			System.out.println("No associated messages to process.");
		}

		// Consume the incoming message + associatedMessages
		StringBuffer buf = new StringBuffer("Consumed:Group ").append(
				message.getGroup()).append(" :").append(" ").append(
				message.getId()).append(" ");

		// check if really FIFO
		if (associatedMessages.length != 1) {
			for (int i = 0; i < associatedMessages.length; i++) {
				buf.append(associatedMessages[i].getId()).append(" ");

				// not end of the array
				if (i != associatedMessages.length - 1) {
					if (associatedMessages[i + 1].getId()
							- associatedMessages[i].getId() != 20) {
						System.err.println("Error IN FIFO!!! Group "
								+ associatedMessages[i].getGroup()
								+ " Current:"
								+ associatedMessages[i + 1].getId() + " Prev:"
								+ associatedMessages[i].getId());
						// Something is wrong - rollback
						txStatus.setRollbackOnly();
						return;

					}
				}
			}
		}

		// Simulate processing time
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Partition ID:" + partitionID + " TID:" + Thread.currentThread().getId() + " bucket ID:"+ bucketID + " " + buf.toString());
	}
}
```

{{% /tab %}}

{{%tab "  The UOWProcessorFactory "%}}


```java
@RemotingService
public class UOWProcessorFactory implements UOWProcessorService{

	@Autowired
	@Resource(name = "bucketConfiguration")
	BucketConfiguration bucketConfig = new BucketConfiguration ();

	@GigaSpaceContext
	GigaSpace space;

	@Autowired
	@Resource(name = "transactionManager")
	PlatformTransactionManager transactionManager;

	@ClusterInfoContext
	ClusterInfo clusterInfo;

	int partitionID = 1;

	List<SimplePollingEventListenerContainer> pcList = new LinkedList<SimplePollingEventListenerContainer>();

	void createProcessor(int bucket) {
    	System.out.println(">>>>> starting polling container for bucket #" + bucket);
		UOWMessage templ = new UOWMessage();
		templ.setBuketId(bucket);

		SimplePollingEventListenerContainer pc = new SimplePollingContainerConfigurer(space)
			.eventListenerAnnotation(new UOWProcessor(bucket,partitionID))
			.transactionManager(transactionManager).template(templ)
			.pollingContainer();

		pcList.add(pc);
	}

    @PostPrimary
	public void init() throws Exception {
    	if (clusterInfo!=null) partitionID=clusterInfo.getInstanceId();
    	System.out.println(">>>>> Partition ID:"+ partitionID + " - starting "+ bucketConfig.getBucketsCount() + " polling containers <<<<<<");
		for (int i = 0; i < bucketConfig.getBucketsCount(); i++) {
			createProcessor(i);
		}
	}

    @PreDestroy
    public void shutdown() {
        for (SimplePollingEventListenerContainer simplePollingEventListenerContainer : pcList) {
            simplePollingEventListenerContainer.shutdown();
        }
    }

	public int getBucketsCount() {
		return bucketConfig.getBucketsCount();
	}
}
```

{{% /tab %}}

{{%tab "  The UOW Data-Grid pu.xml  "%}}


```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xmlns:os-core="http://www.openspaces.org/schema/core"
      xmlns:os-events="http://www.openspaces.org/schema/events"
      xmlns:tx="http://www.springframework.org/schema/tx"
      xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
      xmlns:os-sla="http://www.openspaces.org/schema/sla"
      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
      http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
      http://www.openspaces.org/schema/core http://www.openspaces.org/schema/core/openspaces-core.xsd
      http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
      http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd
      http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/sla/openspaces-sla.xsd">

 	<os-core:annotation-support />

	<bean id="bucketConfiguration" class="com.giagspaces.patterns.uow.BucketConfiguration" >
		 <property name="bucketsCount"> <value>8</value>  </property>
	</bean>

	<context:component-scan base-package="com.giagspaces.patterns.uow"/>
	<os-remoting:annotation-support />
	<tx:annotation-driven transaction-manager="transactionManager"/>
	<os-core:giga-space-context/>
	<os-events:annotation-support />
	<os-core:embedded-space id="space" name="space"/>
	<os-core:local-tx-manager id="transactionManager" space="space" default-timeout="5000" />
	<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
	<os-remoting:service-exporter id="serviceExporter" />

</beans>
```

{{% /tab %}}

{{%tab "  The BucketConfiguration "%}}


```java
package com.giagspaces.patterns.uow;

public class BucketConfiguration {
	int bucketsCount = 4;

	public int getBucketsCount() {
		return bucketsCount;
	}

	public void setBucketsCount(int bucketsCount) {
		this.bucketsCount = bucketsCount;
	}
}
```

{{% /tab %}}

{{% /tabs %}}

