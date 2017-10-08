---
type: post102
title:  The Processing Unit
categories: XAP102TUT
weight: 900
parent: none
---



The PU is the fundamental unit of deployment in XAP. The PU itself runs within a Processing Unit Container and is deployed onto the Service Grid. Once a PU is deployed, a PU instance is the actual runtime entity.





There are two types of Processing Unit Containers:

- Integrated Processing Unit Container
A container that runs the PU inside an IDE (e.g. IntelliJ IDEA, Eclipse). The integrated processing unit container enables to run the PU inside your IDE for testing and debugging purposes.

- Service Grid Processing Unit Container
A Processing Unit Container which runs within a Grid Service Container. It enables running the PU within a service grid, which provides self-healing and SLA capabilities to components deployed on it.

# Processing Unit (PU)
The PU is a deployable, independent, scalable unit, which is the building block for the Space Based Architecture (SBA). The PU is a combination of service beans and/or an embedded space instance. The artifacts that belong to a PU are packaged as a JAR or WAR file.

There are several types of PU's; data only, business-logic only, mixed PU's (which contain both data and business logic) and special purpose PU's.

#### Data only PU
This type of PU does not include any business logic, only a Space. The PU simply defines the runtime characteristics of the space, i.e. its runtime topology, the number of space replicas/partitions, etc.

#### Business-logic only PU
The Business-logic Only PU implements your application code, and does not include any data. Typically, your code interacts with a remote Space which is defined by another PU. By defining the PU as business logic only, you create an application server which is hosted and monitored by the XAP Service Grid. The application can be a typical *Spring* application deployed to an XAP PU.

#### Mixed PU
This type of PU's includes both business logic and a space. Typically, the business logic interacts with a local space instance (i.e. a data grid instance running within the same PU instance) to achieve lowest possible latency and best performance.

#### Elastic Processing Unit (EPU)
An Elastic Processing Unit (EPU) is a Processing Unit with additional capabilities that simplify its deployment across multiple machines. Containers and machine resources such as Memory and CPU are automatically provisioned based on Memory and CPU requirements. When a machine failure occurs, or when scale requirements change, new machines are provisioned and the Processing Unit deployment distribution is balanced automatically. The PU scale is triggered by modifying the requirements through an API call. From that point in time the EPU continuously maintains the specified capacity (indefinitely, or until the next scale trigger).

{{%learn "/xap102/elastic-processing-unit.html"%}}


#### Web PU
XAP allows you to deploy web applications (packaged as a WAR file) onto the Service Grid. The integration is built on top of the Service Grid Processing Unit Container. The web application itself is a pure, JEE based, web application. The application can be the most generic web application, and automatically make use of the Service Grid features. The web application can define a Space (either embedded or remote) very easily (either using Spring or not).The web container used behind the scenes is Jetty.

{{%learn "/xap102/web-application-support.html"%}}

#### Mule PU
XAP's Mule integration allows you to run a pure Mule application (with or without XAP special extension points and transports) as a PU.

{{%learn "/xap102/mule-processing-unit.html"%}}




# The PU Jar File
Much like a JEE web application or an OSGi bundle, The PU is packaged as a .jar file and follows a certain directory structure which enables the XAP runtime environment to easily locate the deployment descriptor and load its classes and the libraries it depends on. A typical PU looks as follows:


```bash
|----META-INF
|--------spring
|------------pu.xml
|------------pu.properties
|------------sla.xml
|--------MANIFEST.MF
|----xap
|--------tutorial
|------------model
|----------------Payment.class
|----------------User.class
|----lib
|--------hibernate3.jar
|--------....
|--------commons-math.jar
```

The PU jar file is composed of several key elements:

- META-INF/spring/pu.xml (mandatory): This is the PU's deployment descriptor, which is in fact a Spring context XML configuration with a number of XAP-specific namespace bindings. These bindings include XAP specific components (such as the space for example). The pu.xml file typically contains definitions of XAP components (space, event containers, remote service exporters) and user defined beans.

- META-INF/spring/sla.xml (not mandatory): This file contains SLA definitions for the PU (i.e. number of instances, number of backup and deployment requirements). Note that this is optional, and can be replaced with an `<os:sla>` definition in the pu.xml file. If neither is present, the default SLA will be applied. SLA definitions can also be specified at the deploy time via command line arguments.

- META-INF/spring/pu.properties (not mandatory): Enables you to externalize properties included in the pu.xml file (e.g. database connection username and password), and also set system-level deployment properties and overrides, such as JEE related deployment properties.

- User class files: Your processing unit's classes (here under the xap.tutorial package)

- lib: Other jars on which your PU depends.

- META-INF/MANIFEST.MF (not mandatory): This file could be used for adding additional jars to the PU classpath, using the standard MANIFEST.MF Class-Path property.


# The pu.xml file
This file is a Spring framework XML configuration file. It leverages the Spring framework IoC container and extends it by using the Spring custom namespace mechanism.

The definitions in the pu.xml file are divided into 2 major categories:

- GigaSpaces specific components, such as space, event containers or remote service exporters.
- User defined beans, which define instances of user classes to be used by the PU. For example, user defined event handlers to which the event containers delegate events as those are received.


Here is an example of a pu.xml file:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
    top level element of the Spring configuration. Note the multiple namespace definition for both GigaSpaces and Spring.
-->
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:context="http://www.springframework.org/schema/context"
   xmlns:os-core="http://www.openspaces.org/schema/core"
   xmlns:os-events="http://www.openspaces.org/schema/events"
   xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
   xmlns:os-sla="http://www.openspaces.org/schema/sla"
   xsi:schemaLocation="
   http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
   http://www.openspaces.org/schema/core http://www.openspaces.org/schema/10.2/core/openspaces-core.xsd
   http://www.openspaces.org/schema/events http://www.openspaces.org/schema/10.2/events/openspaces-events.xsd
   http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/10.2/remoting/openspaces-remoting.xsd
   http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/10.2/sla/openspaces-sla.xsd">

    <!-- Enables to configure Spring beans through annotations-->
    <context:annotation-config />

    <!-- Enable @PostPrimary and others annotation support. -->
    <os-core:annotation-support />

    <!-- Enables using @Polling and @Notify annotations to creating polling and notify containers  -->
    <os-events:annotation-support />

    <!-- Enables using @RemotingService and other remoting related annotations   -->
    <os-remoting:annotation-support />

    <!--
        A bean representing a space. Here we configure an embedded space (note the url element which does
        not contain any remote protocol prefix. Also note that we do not specify here the cluster topology
        of the space. It is declared by the `os-sla:sla` element of this pu.xml file.
    -->
    <os-core:embedded-space id="space" name="eventSpace" />

    <!-- Define the GigaSpace instance that the application will use to access the space  -->
    <os-core:giga-space id="eventSpace" space="space"/>

</beans>
```

{{%learn "/xap102/configuring-processing-unit-elements.html"%}}




# Service Level Agreement (SLA)
The SLA definitions can be provided as part of the PU package or during the PU's deployment process. They define the number of PU instances that should be running and deploy-time requirements such as clustering topology for PU's which contain a space. The GSM reads the SLA definition, and deploys the PU onto the available GSCs according to it.
A sample SLA definition is shown below:


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	   http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/10.2/sla/openspaces-sla.xsd">

      <os-sla:sla cluster-schema="partitioned-sync2backup"
            number-of-instances="2" number-of-backups="1"
            max-instances-per-vm="1">
       </os-sla:sla>
</beans>
```

{{%learn "/xap/10.2/admin/the-sla-overview.html"%}}





# Deployment
When deploying the PU to the XAP Service Grid, the PU jar file is uploaded to the XAP Manager (GSM) and extracted to the deploy directory of the local XAP installation (located by default under <XAP Root>/deploy).
Once extracted, the GSM processes the deployment descriptor and based on that provisions PU instances to the running XAP containers.

Each GSC to which a certain instance was provisioned, downloads the PU jar file from the GSM, extracts it to its local working directory (located by default under <XAP Root>/work/deployed-processing-units) and starts the PU instance.


# Example
Our Online Payment system is expected to handle a large amount of concurrent users performing transactions. The system also needs to be highly available. This is where XAP's PU comes into play. We will create a polling container that takes a payment event as input and processes it. Then, we will deploy this code as a PU onto the IMDG. Payment events are being written into a space and the polling container will pick up the events and process them. We will use the pu.xml file to define the deployment and add an SLA configuration to it to provide failover and scalability.

### Polling Container
First we define a polling container that will handle the business logic upon receiving a payment event. In our example we define a polling container that will receive events when a new payment is created:

```java
@EventDriven
@Polling
@NotifyType(write = true)
public class PaymentProcessor {

	// Define the event we are interested in
	@EventTemplate
	Payment unprocessedData() {
		Payment template = new Payment();
		template.setStatus(ETransactionStatus.NEW);
		return template;
	}

	@SpaceDataEvent
	public Payment eventListener(Payment event) {
		System.out.println("Payment received; processing .....");

		// set the status on the event and write it back into the space
		event.setStatus(ETransactionStatus.PROCESSED);
		return event;
	}
}
```

#### Create pu.xml
In this step will create the configuration file for the PU deployment


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
	xsi:schemaLocation="
   http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
   http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
   http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
   http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
   http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd">

	<!-- Scan the packages for annotations / -->
	<context:component-scan base-package="xap" />

	<!-- Enables to configure Spring beans through annotations -->
	<context:annotation-config />

	<!-- Enable @PostPrimary and others annotation support. -->
	<os-core:annotation-support />

	<!-- Enables using @Polling and @Notify annotations to creating polling and notify containers -->
	<os-events:annotation-support />

	<!-- Enables using @RemotingService and other remoting related annotations -->
	<os-remoting:annotation-support />

	<!-- A bean representing a space (an IJSpace implementation) -->
	<os-core:embedded-space id="space" name="eventSpace" />

	<!-- Define the GigaSpace instance that the application will use to access the space -->
	<os-core:giga-space id="eventSpace" space="space"/>

</beans>
```

#### Deployment
Now we have all the pieces that are necessary to create the jar file for the PU. After we have created the jar file its time to deploy the PU onto the data grid. Again, you can do this in three ways; by script, Java code or via the admin UI. In our example will use the scripts to deploy the PU.

First we start the GigaSpace Agent (GSA) that will create our IMDG on this machine:

{{%tabs%}}
{{%tab "  Windows "%}}

```bash
GS_HOME\bin\gs-agent.bat
```
{{% /tab %}}

{{%tab "  Linux "%}}

```bash
GS_HOME/bin/gs-agent.sh
```
{{% /tab %}}

{{% /tabs %}}

And now we deploy the PU onto the IMDG:

```bash
GS_HOME\bin\gs.sh deploy  eventPU.jar
```
We assume that the jar we created is named eventPU.jar

If you startup the Admin UI you will be able to see that through the deployment a space called eventSpace was created and a PU named with the jar name.



#### Client interface
Now its time to create a client that creates events and writes them into the space. We will attach a listener on the client side to the space that will receive events when the payment is processed.

{{%tabs%}}
{{%tab "  Client Listener "%}}

```java
@EventDriven
@Polling
@NotifyType(write = true)
public class ClientListener {

	// Define the event we are interested in
	@EventTemplate
	Payment unprocessedData() {
		Payment template = new Payment();
		template.setStatus(ETransactionStatus.PROCESSED);
		return template;
	}

	@SpaceDataEvent
	public Payment eventListener(Payment event) {
		System.out.println("Processed Payment received ");

		return null;
	}
}
```
{{% /tab %}}

{{%tab "  Client "%}}

```java
public void postPayment() {
	// Register the event handler on the Space
	this.registerPollingListener();

	// Create a payment
	Payment payment = new Payment();
	payment.setCreatedDate(new Date(System.currentTimeMillis()));
	payment.setPayingAccountId(new Integer(1));
	payment.setPaymentAmount(new Double(120.70));

	// write the payment into the spaceO
	space.write(payment);
}
public void registerPollingListener() {
     Payment payment = new Payment();
     payment.setStatus(ETransactionStatus.PROCESSED);

     SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(
         space).eventListenerAnnotation(new ClientListener())
         .pollingContainer();
     pollingEventListenerContainer.start();
}
```
{{% /tab %}}
{{% /tabs %}}

When you run this code you should see that the PU deployed onto the IMDG is processing the event, changes the status of the payment to PROCESSED and writes the event back into the space. The client then will receive an event because it has registered a listener that listens for processed payment events.

#### Deploy a PU with the WEB Admin UI
There is complete example available of  a PU on GitHub. You can download, build and deploy this example. Here is an example how you deploy a PU with the WEB admin UI:

{{%section%}}
{{%column width="25%"%}}
Deploy PU

{{%popup   "/attachment_files/qsg/EventPU1.png"%}}

{{%/column%}}

{{%column width="25%"%}}
Applications deployed

{{%popup   "/attachment_files/qsg/EventPU2.png"%}}

{{%/column%}}

{{%column width="25%"%}}
Data Grid

{{%popup   "/attachment_files/qsg/EventPU3.png"%}}

{{%/column%}}

{{%column width="25%"%}}
Statistics

{{%popup   "/attachment_files/qsg/EventPU4.png"%}}

{{%/column%}}
{{%/section%}}

{{%tryit "https://github.com/Gigaspaces/xap-tutorial" %}}

#### Failover and Scalability
One of our non functional requirements for our online payment system is that it is highly available and it can handle a large amount of concurrent transactions. This can be accomplish in a couple of ways. We can deploy the PU with multiple concurrent threads and or multiple PU instances on top of the grid.

#### Multi threaded PU
By default the PU is single threaded. With a simple annotation you can tell XAP how many threads the PU should run with.

```java
@EventDriven
@Polling @Polling(concurrentConsumers = 3, maxConcurrentConsumers = 10)
@NotifyType(write = true)
public class PaymentProcessor {
}
```

#### Multiple PU's
Lets assume that we have two machines available for our deployment. We want to deploy 4 instances of our PU, two on each machine.

The deployment script for this scenario looks like this:

```bash

With a statefull PU, embedded space
./gs.sh deploy -cluster schema=partitioned total_members=4,0 -max-instances-per-machine 2 eventPU.jar

With a stateless PU
./gs.sh deploy -cluster total_members=4 -max-instances-per-machine 2 eventPU.jar
```

{{%learn "/xap/10.2/admin/deploy-command-line-interface.html"%}}


