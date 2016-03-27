---
type: post110
title:  Data Example
categories: XAP110TUT
weight: 5000
parent: none
---


{{% ssummary %}}{{% /ssummary %}}

This example gives an extensive overview of the OpenSpaces APIs, the Processing Unit concept and the configuration options in the `sla.xml` and `pu.xml` files. The example contains two Processing Units; one that feeds data objects into the system, and another that reads those objects and processes them.


{{% info %}}
The example is located under `<XAP Root>/examples/data`
{{% /info %}}

The purpose of this example is to show many of the capabilities of GigaSpaces XAP and its mainstream APi, namely OpenSpaces. The example uses the following features:

- POJO Annotations
- The `GigaSpace` class
- Declarative transactions
- Event containers (polling and notify containers)
- OpenSpaces remoting
- OpenSpaces clustering and SLA
- Using the JMS [MessageConverter]({{%currentjavaurl%}}/jms-space-interoperability.html) feature that writes POJOs to the space using the JMS API

This example shows a PU that contains several services that are independent of each other and serve different purposes within one application. Moreover, the example show how different processing units use the space to share data.

# Architecture

This example includes two modules that are deployed to the service grid, and a domain model that consists of the `Data` class (shared between two modules). Each module runs within a processing unit, one runs the [DataFeeder](#datafeeder) bean, the [JMSDataFeeder](#jms) bean and the [DataRemoting](#remoting) bean, all write `Data` instances with raw data into the remote space. The space is actually embedded within the second processing unit, which runs the [DataProcessor](#dataprocessor) bean. The `DataProcessor` service takes the new `Data` objects, processes the raw data and writes them back to the space. Both processing units run additional services that interact with the space or with the other services, either in via a polling container, a notify container or via GigaSpaces remoting support.

The entire application looks like this:

![diag.jpg](/attachment_files/diag.jpg)

## Application Workflow

1. The [DataFeeder](#datafeeder) writes unprocessed `Data` objects into the space every second.
1. The [JMSDataFeeder](#jms) uses Spring's `JmsTemplate` over GigaSpaces JMS to write unprocessed `Data` objects into the space every second.
1. The [ViewDataCounter](#view) performs a count operation on the space every second to count all of the `Data` instances (processed and unprocessed).
1. the [DataRemoting](#remoting) performs direct remote calls on the `DataProcessor` service.
1. The [DataProcessor](#dataprocessor) takes unprocessed `Data` objects, processes them and writes them back to the space.
1. The [ProcessedDataCount](#processedata) receives notifications of processed `Data` objects that are written or updated in the space. Notifications are configured to arrive in batches of 10 objects or every 5 seconds.

{{% exclamation %}} The services described above are independent of each other. They are merely explained here to show the various capabilities of GigaSpaces XAP and the OpenSpaces API.

# The POJO Domain Model

The only object in our model is the `Data` object.


```java
@SpaceClass
public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long type;

    private String rawData;

    private String data;

    private Boolean processed;

[..]
    @SpaceId
    public String getId() {
        return id;
    }

    /**
     * The type of the data object. Used as the routing field when working with
     * a partitioned space.
     */
    @SpaceRouting
    public Long getType() {
        return type;
    }
[..]
```

Note the annotations that are used in this object:

- `@SpaceClass` -- the marked object can be written to a space.
- `@SpaceId` -- marks the `id` attribute of the `Data` class as the unique identifier of this class. Each instance of `Data` should have a unique ID value.
- `@SpaceRouting` -- when using a partitioned cluster topology, `Data` objects are routed to the appropriate partitions according to the specified attribute, in this case `type` (this means that two instances with the same value for this field will end up in the same partition).

Basically, every `Data` object is written to the space by the `DataFeeder` or by the `JMSDataFeeder` with the `processed` value set to `false`, and this value is later set to `true` by the `DataProcessor`.

{{% info %}}
Even though our object implements `Serializable`, it doesn't have to in all cases. This is relevant only when the `Data` object is used as a parameter in remote calls.
{{%/info%}}

# POJO Services with Spring

According to the diagram above, there are 5 different services in our application, each is independent of the others and performs different actions as detailed below.

{{% anchor dataprocessor %}}

## DataProcessor

The data processor is implemented in the class `rg.openspaces.example.data.processor.DataProcessor`. It is configured in the `pu.xml` file of the processor module.

The most interesting part of the `DataProcessor` class is the `processData()` method. Note that it is marked with the `@SpaceDataEvent` annotation. This annotation marks the method to be called whenever a `Data` object that matches our criteria is available for processing (in our case, change the `processed` attribute to `true`) and write the object back to the space.

You might also have noticed that the `DataProcessor` implements `IDataProcessor`. This simple interface is used to expose the `DataProcessor` to remote clients via space based remoting. In our example, as you can see in the diagram above, the `DataRemoting` service (part of the feeder processing unit) makes a remote call to the `DataProcessor`, located in the processor processing unit (which runs in another process, or even another machine). This is exactly why our `Data` object is `Serializable`.

The event processing aspect of the `DataProcessor` is enabled using GigaSpaces polling container.

The polling container is created since the `DataProcessor` class is annotated with `@Polling` annotation. The value returned from the method annotated with the `@EventTemplate` annotation will be used as the _template_ for the polling container, i.e. the polling container will use it in the take operations it does periodically. This means that it will determine the criteria of the `Data` objects to be processed (in this case with the `processed` attribute set to `false`.

Note the the `pu.xml` file of the processor module contains the `<context:annotation-config/>` and `<os-events:annotation-support/>` tags to instruct the infrastructure to look for classes annotated with `@EventDriven` and `@Polling` and create polling container for them. Also note that `@TransactionalEvent` annotation which means that event will automatically be processed under a new transaction which will commit unless an exception is thrown.

{{% anchor processedata %}}

## DataProcessedCounter

The `DataProcessedCounter` is a service that is also part of the processor module. It also contains a method that is marked with the `@SpaceDataEvent` annotation. However, there are two differences between the `DataProcessedCounter` and the `DataProcessor`. The minor difference is that the `DataProcessedCounter` processes `Data` objects with the `processed` attribute set to `true` (meaning objects that were already processed by the `DataProcessor` and written back to the space).

The bigger difference is that unlike the `DataProcessor`, which **takes** objects from the space to the processes them, the `DataProcessedCounter` is **notified** whenever these objects are written to the space or updated in the space. Moreover, instead of receiving the events one by one, the `DataProcessedCounter` receives batches of events that can be based on the number of objects, interval in milliseconds, or both:
This is expressed in the following annotations:


```java
@EventDriven
//Event notification mechanism
@Notify
//batching behavior
@NotifyBatch(size = 10, time = 5000)
//notify for write and update operations
@NotifyType(write = true, update = true)
```

Similar to the polling container, the `@EventTemplate` annotation is used to annotate the method which returns the template for the notify registration.
Unlike the `DataProcessor`, which defines the template in the form of a `Data` instance, the `DataProcessedCounter` defines a `SQLQuery`, using the class type and and the `WHERE` clause of the query. The SQL query is equivalent to the way we defined the template, and either can be used (although the `SQLQuery` allows more flexibility since it support `OR`, _greater than_ and _less than_ semantics.
As with the polling container, the `pu.xml` should include the `<context:annotation-config/>` and `<os-events:annotation-support/>` tags (it's enough to specify them once for the entire processing unit, regardless of the number of actual event containers in your processing unit)

{{% info %}}
Note that code only contains the implementation of the counter. Everything else is configured through annotation (or alternatively can be configured via XML), which means you can change the event-handling mode without changing the handling code.
{{%/info%}}

{{% anchor datafeeder %}}

## DataFeeder

The `DataFeeder` class belongs to the feeder module, and is configured as a Spring bean that repetitively (every one second) creates new `Data` objects with one of the four types (1, 2, 3 or 4), sets the `processed` value to `false`, and writes them to the space:

{{%tabs%}}
{{%tab "  Code "%}}


```java
public class DataFeeder {
[..]
Data data = new Data((counter++ % numberOfTypes), "FEEDER " + Long.toString(time));
data.setId(startIdFrom + counter);
data.setProcessed(false);
gigaSpace.write(data);
[..]
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="dataFeeder" class="org.openspaces.example.data.feeder.DataFeeder"/>
```

{{% /tab %}}
{{% /tabs %}}

The `gigaSpace` member is an instance of `GigaSpace` -- which represent the client side proxy to the space. Note that the instance is injected to the `DataFeeder` in a declarative manner:

{{%tabs%}}
{{%tab "  Code "%}}


```java
@GigaSpaceContext(name = "gigaSpace")
private GigaSpace gigaSpace;
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<!--
    Enables the usage of @GigaSpaceContext annotation based injection.
-->
<os-core:giga-space-context/>

<!--
    A bean representing a space (an IJSpace implementation).
    Note, we perform a lookup on the space since we are working against a remote space.
-->
<os-core:space-proxy  id="space" name="space" />

<!--
    OpenSpaces simplified space API built on top of IJSpace/JavaSpace.
-->
<os-core:giga-space id="gigaSpace" space="space"/>
```

{{% /tab %}}
{{% /tabs %}}

In the Configuration tab above, we show how to define the space with its URL, and then define the `GigaSpace` instance which is based on that space. In this case, the space URL points to a remote space location (this is because the space is located within the `DataProcessor` Processing Unit, which is remote to the Processing Unit in which `DataFeeder` is running). This approach not only allows you to easily configure the application, but also to completely change the `GigaSpace` functionality implementation, without changing the code of your original application.

Because the `DataFeeder` includes JSR-250's `@PostConstruct` and `@PreDestroy` annotations, its annotated methods (`construct()` and `destroy()`} are called when it is created and destroyed, respectively.

{{% anchor jms %}}

## JMSDataFeeder

The `JMSDataFeeder` is similar to the `DataFeeder`. The difference between the beans is that the `JMSDataFeeder` uses Spring's `JmsTemplate` on top of the GigaSpaces JMS implementation to write the `Data` objects to the space; no space API is used directly. This is possible due to the usage of a `MessageConverter` that converts JMS messages into any required POJO type, in this case, `Data`. In this example, we configure the `ConnectionFactory` to use the `ObjectMessage2ObjectConverter` that comes with the GigaSpaces JMS implementation. The `ObjectMessage2ObjectConverter` receives a JMS `ObjectMessage` and returns the message's content (body) as the object to write to the space. The JMS `ObjectMessage` itself, including headers, properties etc., is not written. The `JMSDataFeeder` uses Spring's `JmsTemplate` and `MessageCreator` to send `ObjectMessages` that contain the `Data` objects, and the converter makes sure that only the contained `Data` objects are written.

{{%tabs%}}
{{%tab "  Code "%}}


```java
public class JMSDataFeeder {
    [..]
    Data data = new Data(Data.TYPES[counter++ % Data.TYPES.length], "FEEDER " + Long.toString(time));
    data.setProcessed(false);
    jmsTemplate.send(new MessageCreator() {
        public Message createMessage(Session session) throws JMSException {
            return session.createObjectMessage(data);
        }
    });
    [..]
}
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="jmsDataFeeder" class="org.openspaces.example.data.feeder.JMSDataFeeder"/>
```

{{% /tab %}}
{{% /tabs %}}

The `JMSDataFeeder` is injected with a Spring `JmsTemplate`. The `JmsTemplate` is injected with a JMS `ConnectionFactory` and a destination of type `Queue`. Unlike the `DataFeeder`, the `JMSDataFeeder` does not declare an instance of `GigaSpace`. `GigaSpace` is injected into the `ConnectionFactory` bean and is used behind the scenes by the JMS layer. In addition, the `ConnectionFactory` is injected with a `MessageConverter` of type `ObjectMessage2ObjectConverter`.

{{%tabs%}}
{{%tab "  Code "%}}


```java
public class JMSDataFeeder {
    [..]
    /** Sets the JmsTemplate */
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }
    [..]
}
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="jmsDataFeeder" class="org.openspaces.example.data.feeder.JMSDataFeeder" depends-on="gigaSpace">
    <property name="instanceId" value="${clusterInfo.instanceId}" />
    <property name="numberOfTypes" value="${numberOfTypes}" />
    <property name="jmsTemplate">
        <bean class="org.springframework.jms.core.JmsTemplate">
            <property name="connectionFactory" ref="connectionFactory"/>
            <property name="defaultDestination" ref="destination" />
        </bean>
    </property>
</bean>

<!--
    The JMS ConnectionFactory used by the JMS feeder
-->
<os-jms:connection-factory id="connectionFactory" giga-space="gigaSpace"
                           message-converter="messageConverter" />

<!--
    The MessageConverter used by the JMS layer to convert JMS message to any POJO/s before sending.
-->
<bean id="messageConverter" class="com.j_spaces.jms.utils.ObjectMessage2ObjectConverter" />

<!--
    A JMS destination used when sending messages
-->
<os-jms:queue id="destination" name="MyQueue" />
```

{{% /tab %}}
{{% /tabs %}}

Because the `JMSDataFeeder` includes JSR-250's `@PostConstruct` and `@PreDestroy` annotations, its annotated methods (`construct()` and `destroy()`} are called when it is created and destroyed, respectively.

{{% anchor view %}}

## ViewDataCounter

The `ViewDataCounter` is a simple service that performs a count operation on the space every second.

{{%tabs%}}
{{%tab "  Code "%}}


```java
int count = gigaSpace.count(new Data());
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<bean id="viewDataCounter" class="org.openspaces.example.data.feeder.ViewDataCounter"/>
```

{{% /tab %}}
{{% /tabs %}}

Note that the `ViewDataCounter` is configured in the same `pu.xml` file as the `DataFeeder`, however, there is one major difference in how the two services connect to the space. The `DataFeeder` uses a `GigaSpace` instance to access the space remotely, while the `ViewDataCounter` uses a different instance -- a local view of the space. A local view is like a local filter defined in the client side that registers for specific templates. Every object that is written to the space or updated in it that matches the template is immediately sent to the local view of that client. This way, different clients can keep different local caches according to their interests, and gain in memory read speeds.

In this example, the `ViewDataCounter` is interested only in processed `Data` objects, so it defines a local view with a template and uses a `GigaSpace` instance that uses the local view.

{{%tabs%}}
{{%tab "  Code "%}}


```java
@GigaSpaceContext(name = "processedViewGigaSpace")
private GigaSpace gigaSpace;
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<os-core:local-view id="processedViewSpace" space="space">
	<os-core:view-query where="processed = true" class="org.openspaces.example.data.common.Data"/>
</os-core:local-view>
```

{{% /tab %}}
{{% /tabs %}}

Because the `ViewDataCounter` includes JSR-250's `@PostConstruct` and `@PreDestroy` annotations, its annotated methods (`construct()` and `destroy()`} are called when it is created and destroyed, respectively.

{{% anchor remoting %}}

## DataRemoting

`DataRemoting` demonstrates the remoting capabilities of OpenSpaces, using the space as the transport layer for the remote calls.

Implementing a remote service is quite straightforward, you just need to expose your remote interface and add the proper configuration. In our example, we want to be able to access the `DataProcessor` remotely, so we added the `IDataProcessor` interface. `DataRemoting` only uses `IDataProcessor`, and is completely unaware to the underlying space.

{{%tabs%}}
{{%tab "  Code "%}}


```java
[..]
@EventDrivenProxy(gigaSpace = "gigaSpace", timeout = 30000)
private IDataProcessor dataProcessor;
[..]
    Data data = new Data((counter++ % numberOfTypes), "FEEDER " + Long.toString(time));
    data.setProcessed(false);
    System.out.println("--- REMOTING PARAMETER " + data);
    dataProcessor.sayData(data);
    data = dataProcessor.processData(data);
    System.out.println("--- REMOTING RESULT   " + data);
[..]
```

{{% /tab %}}
{{%tab "  Configuration "%}}


```xml
<os-remoting:annotation-support/>
<!--
	The DataRemoting bean, uses the proxied dataProcessor without any knowledge of the remoting invocation.
-->
<bean id="dataRemoting" class="org.openspaces.example.data.feeder.DataRemoting">
	<property name="dataProcessor" ref="dataProcessor"/>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

**Steps to run the application inside IntelliJ IDE:**

{{% anchor JImporting Project to the IDE %}}

**Importing the project into IntelliJ**

1. Import the hello world project with 3 modules **common**, **pnrocessor**, and **feeder** located under the `<XAP Root>/examples/data` folder.

{{%accordion%}}
{{%accord title=" How do I do that..."%}}
{{% panel %}}


**Importing the sample projects into the IDE**

* Select **File** > **New** > **Project from Existing Sources...**.
* Browse to `<XAP Root>/examples/data` folder and choose the file `pom.xml` and click **OK**.



![intellij-ide-data-1.png](/attachment_files/intellij-ide-data-1.png)



* Don't change the default settings of this page and click **Next**.
&nbsp;

* Enable the **IDE** profile and disable the **Default** profile then click **Next**.



![intellij-ide-2.png](/attachment_files/intellij-ide-2.png)


* Click **Next**.
&nbsp;

* Select project SDK and click **Next**.
&nbsp;

* Enter Project name and location then click **Finish**.

{{% /panel %}}
{{%/accord%}}
{{%/accordion%}}

{{% anchor Create Run Configurations in IDE %}} **Create Run Configurations**
&nbsp;

1. Execute the following command from the project root directory `<XAP Root>/examples/helloworld`:

```bash
build.(sh/bat) intellij
```
{{% anchor Create Run Configurations in IDE %}}



{{% anchor Run Processor in IDE %}} **Running the Processor**


1. From the toolbar at the top of the screen, select **Run > Run... > Processor**.

{{% anchor JRun Feeder in IDE %}}

**Waiting for the Processor to instantiate**

1. Before running the feeder, you should wait for the following output to appear in the **Run tab** at the bottom of the screen:
    Processor instantiated, waiting for messages feed...
This indicates the Processor is up and running.

{{% anchor JRun Feeder in IDE2 %}}

**Running the Feeder**


1. From the toolbar at the top of the screen, select **Run > Run... > Feeder**.

{{% anchor JView Output %}}

You can use the Management Console to view the Object count and statistics for the different operations:

![ide-gs-ui-stats.jpg](/attachment_files/ide-gs-ui-stats.jpg)

#### Expected output

Running the processor and the feeder results in the following output, which can be viewed in the **Run tab** at the bottom of the screen.
Press on Processor or Feeder to switch between the output consoles.

**Feeder expected output**
	    
The feeder starts, writes unprocessed data objects both by directly writing them to the space
and by using OpenSpaces Remoting. The JMS feeder uses JMS API to feed unprocessed data objects. It
does so by using a MessageConverter that converts JMS ObjectMessages to the data objects.

```bash
  --- STARTING FEEDER WITH CYCLE [1000]
  --- STARTING FEEDER WITH CYCLE [1000]
  --- STARTING REMOTING WITH CYCLE [1000]
  --- REMOTING PARAMETER id[null] type[0] rawData[FEEDER 1459064046345] data[null] processed[false]
  --- STARTING BROADCAST REMOTING COUNTER WITH CYCLE [1000]
  --- STARTING VIEW COUNTER WITH CYCLE [1000]
  2016-03-27 10:34:06,364  INFO [org.openspaces.pu.container.integrated.IntegratedProcessingUnitContainer] - Processing unit(s) started successfully
  --- REMOTING RESULT   id[null] type[0] rawData[FEEDER 1459064046345] data[PROCESSED : FEEDER 1459064046345] processed[true]
  --- FEEDER WROTE id[2] type[1] rawData[FEEDER 1459064047265] data[null] processed[false]
  --- JMS WROTE id[-2] type[1] rawData[JMS_FEEDER 1459064047307] data[null] processed[false]
  --- REMOTING PARAMETER id[null] type[1] rawData[FEEDER 1459064047337] data[null] processed[false]
  ---- VIEW COUNT IS [0]
  **** BROADCAST REMOTING COUNT IS [0]
    .
    .
    .
  ---- VIEW COUNT IS [2]
  --- REMOTING RESULT   id[null] type[2] rawData[FEEDER 1459064048338] data[PROCESSED : FEEDER 1459064048338] processed[true]
  --- FEEDER WROTE id[4] type[3] rawData[FEEDER 1459064049265] data[null] processed[false]
  --- JMS WROTE id[-4] type[3] rawData[JMS_FEEDER 1459064049307] data[null] processed[false]
  --- REMOTING PARAMETER id[null] type[3] rawData[FEEDER 1459064049338] data[null] processed[false]
  **** BROADCAST REMOTING COUNT IS [4]
  ---- VIEW COUNT IS [4]
  .
  .
  .
```


**Processor expected output**

The processor prints the _id_, _type_, _rawData_ and _info_ attributes for each messages it takes for processing:


```bash
 ++++ SAYING : id[null] type[0] rawData[FEEDER 1459064046345] data[null] processed[false]
 ------ PROCESSED : id[null] type[0] rawData[FEEDER 1459064046345] data[PROCESSED : FEEDER 1459064046345] processed[true]
 ++++ SAYING : id[null] type[1] rawData[FEEDER 1459064047337] data[null] processed[false]
 ------ PROCESSED : id[2] type[1] rawData[FEEDER 1459064047265] data[PROCESSED : FEEDER 1459064047265] processed[true]
 ***** COUNT DATA PROCESSED CALLED : 0
 ------ PROCESSED : id[null] type[1] rawData[FEEDER 1459064047337] data[PROCESSED : FEEDER 1459064047337] processed[true]
 ------ PROCESSED : id[-2] type[1] rawData[JMS_FEEDER 1459064047307] data[PROCESSED : JMS_FEEDER 1459064047307] processed[true]
 ++++ SAYING : id[null] type[2] rawData[FEEDER 1459064048338] data[null] processed[false]
 ***** COUNT DATA PROCESSED CALLED : 2
    .
    .
    .
 ------ PROCESSED : id[3] type[2] rawData[FEEDER 1459064048265] data[PROCESSED : FEEDER 1459064048265] processed[true]
 ------ PROCESSED : id[null] type[2] rawData[FEEDER 1459064048338] data[PROCESSED : FEEDER 1459064048338] processed[true]
 ------ PROCESSED : id[-3] type[2] rawData[JMS_FEEDER 1459064048307] data[PROCESSED : JMS_FEEDER 1459064048307] processed[true]
 ++++ SAYING : id[null] type[3] rawData[FEEDER 1459064049338] data[null] processed[false]
 ***** COUNT DATA PROCESSED CALLED : 4
    .
    .
    .
```

**Running the example from IntelliJ IDE with replicated space**

A replicated topology consists of a master Space (primay) and a slave Space (backup). All entries are replicated from one to the other.
In the case of failover, the backup Space resume the primary role.
&nbsp;

After importing the project to IntelliJ IDE and creating run configuration as explained before.
&nbsp;

Run the **Processor Primary** configurations - from the toolbar at the top of the screen, select **Run > Run... > Processor Primary**.
&nbsp;

Run the **Processor Backup** configurations - from the toolbar at the top of the screen, select **Run > Run... > Processor Backup**.
&nbsp;

Run the **Feeder** configurations - from the toolbar at the top of the screen, select **Run > Run... > Feeder**.
&nbsp;

Now all the data is processed only by the Processor Primary. (As is can be seen at the console's output). 
&nbsp;

Next, to simulate failover, stop the Processor Primary process by clicking the stop button as seen in the picture below.

![intellij-ide-data-stop-processor.png](/attachment_files/intellij-ide-data-stop-processor.png)


Then you will see the data is no longer processed by "Processor Primary" but it is processed by "Processor Backup" while the "Feeder" continues to write unprocessed data. 

# Building and Packaging

This example includes a `build.(sh/bat)` script to invoke `Maven` (there is no need to pre-install `Maven`, if `Maven` is not installed on your machine, then the script will use `Maven` installation folder that located at `<XAP Root>/tools/maven/apache-maven-3.2.5`)

From the `<Example Root>` directory (`<XAP Root>/examples/data`) call:


```bash
build.bat/sh compile
```

This compiles the code to a `target` directory and copies the Processing Unit Deployment Descriptors, namely the `pu.xml` and `sla.xml`.

{{% note %}}
The Deployment Descriptor should always reside under the `META-INF\spring` directory of your application.
{{%/note%}}

# Deployment

The example uses maven as its build tool. It comes with a build script that runs Maven automatically. Running the build script with no parameters within the current directory will list all the relevant tasks that can be run with this example.

Running `build.(sh/bat) compile` will compile all the different modules. In case of the Processor and Feeder modules, it will compile the classes directly into their respective PU structure.

Running `build.(sh/bat) package` will finalize the processing unit structure of both the Processor and the Feeder by copying the Common module jar file into the `lib` directory within the processing unit structure. In case of the processor module, it will copy the jar file to `processor/target/data-processor/lib`, and will make `processor/target/data-processor.jar` a ready to use processing unit.

Running `build.(sh/bat) intellij` will create run configuration for IntelliJ IDE, allowing you to run the Processor and the Feeder using IntelliJ run (or degub) targets.

In order to deploy the data example onto the Service Grid, simply run gs-agent which will start a GSM and **two** GSCs (note, we need two GSCs because of the SLA defined within the processor module). Next, `build.(sh/bat) deploy` will need to be executed. The task will deploy the `processor.jar` and the `feeder.jar` onto the running GSM.
This will cause the feeder to be deployed into one of the GSC and start feeding unprocessed data into the two processing units.
Run the GS-UI in order to see the 4 PU instances deployed (two partitions, each with one backup).

Running 'build.(sh/bat) undeploy' will remove all of the processing units of this example from the service grid.

Another option to deploy the example can be using the GS CLI using the deploy option. An interesting example of externally providing the SLA that applies to the deployed processing unit can be running:


```bash
'gs.(sh/bat) deploy -sla ../examples/data/partitioned-sla.xml ../examples/data/processor/target/data-processor.jar'.
```

This allows to deploy the data-processor example using a partitioned space (and not a partitioned-sync2backup) which is defined in the `sla.xml`.

In order to run the feeder using the GS CLI please execute `gs.(sh/bat) deploy ../examples/data/feeder/target/data-feeder.jar`.

Some ways to play with the examples:

1. Start another GSC and relocate (click and drag on GS-UI) the feeder to the other GSC. This will simplify the output since on the GSC that used to run both a processor and a feeder, you will only have the processor now. And on the new GSC, you will see the feeder.
1. Kill one of the GSC that runs the Processor processing unit. Thanks to the SLA, each partition (primary and backup) of an instance will run on a different GSC. This means that the feeder should keep on running,
and the active GSC should have its backup partition space turn into a primary one. While this is happening, the two other instances of the processor PU will get relocated to the GSC that is running the Feeder PU.

