---
type: post97
title:  CPP Processing Unit
categories: XAP97
parent: xap-cpp.html
weight: 60
---

{{% ssummary%}}{{% /ssummary %}}


GigaSpaces XAP is a universal grid based runtime environment for real-time Java, c++ and .Net applications. The real-time environment includes business logic Service Level agreement based container allowing you to deploy your libraries and their statefull data to have self-healing, dynamic scalability and continuous high-availability capabilities out of the box.

The c++ code that is running within the SLA container should implement simple interface, compiled and deployed. c++ Libraries deployment can be done by placing the libraries on all the machines running GigaSpaces SLA container or by placing these on shared location accessible to all GigaSpaces SLA containers.

Once the c++ libraries have been deployed, GigaSpaces runtime environment will load these, initialize these and run these. You may pass into the business logic external parameters and get the status of the business logic instance via watchers or space entries.

Since the runtime SLA container and its manager (GSM) includes sensors that allows it to monitor the behavior of the processing units hosted within it, it may instantiate additional business logic c++ instances on other machines having empty containers to cope with the additional incoming requests or to enlarge the capacity of the running spaces.

The Processing Unit configuration should follow the basic standards that comprise any processing unit. These includes space settings , transaction settings , cluster settings , notify and polling container and the actual c++ business logic library.

The c++ business logic is managed by c++ runtime that is hosted within Java service that allows it to interact with the Java runtime. This runtime layer maintains the temporary sessions required for the c++ runtime (such as transaction, space proxy handles) and serialize c++ objects (aka POCOs) into the space and backup into form the space into the c++ business logic.

You may have as part of the processing unit:

- Pure c++ business logic
- Space instance(s) running in different cluster topology(replicated , partitioned or partitioned-replicated)
- Collocated business logic and Space instance(s) where the c++ business logic accessing only the collocated spaces or remote space instances running within another processing units.

Once the c++ business logic deployed with collocated spaces, there are no remote calls involved when the c++ business logic interacts with the space. There would be remote calls involved in case the business logic access remote spaces explicitly (space proxy configured in clustered mode) and when the spaces configured to have replica space. In this case (replica spaces) every destructive operation would trigger replication event that would transport the local space changes to the replica space(s). Data replication can be done in synchronous or Asynchronous manner.

When the business logic deployed with a collocated space it can inherit the space active mode (Primary or backup). This means that you may have your business logic running in stand-by mode, none initialized as long as it's collocated space running in backup mode. Backup mode space would get its operations only from the existing primary space and is not accessible for client for direct interaction. Once the primary space and its collocated business logic fails (normally or abnormally), the collocated c++ business logic is initialized and started.

{{% tip %}}
You can find a fully running [C++ PU example](./cpp-processing-unit-example.html) with source code, build scripts and running instructions.
{{% /tip %}}

# Scalability - How Can I Get More Horsepower?

Historically software systems scaled by adding more hardware running more software instances.

You can always add more CPUs and more memory to the same machine to be able to process more data per unit of time, but at some point you will reach the physical limit of what a single machine can actually run.

The concept of OpenSpaces processing unit was designed around this fact.  Your unit of scale is the processing unit.  In order to scale, you need to run more processing units.  You can scale your application by:

- running multiple threads concurrently within the same process
- running multiple processes concurrently within the same machine
- deploying multiple processes across multiple machines that are running concurrently and utilizing in an optimized manner your networked computer resources (aka grid)

Nevertheless, often we cannot fully take advantage of the available horsepower, because we get stuck at the data access layer; i.e. we cannot feed the processes with the relevant data fast enough, for these to fully leverage their full CPU, network, and memory resources and complete a given job in the quickest manner, moving to the next one efficiently.

To solve this bottleneck, the processing unit allows you to collocate the business logic and data; i.e. both are running within the same process sharing the same memory address. In fact, the POCO c++ framework allows you to build your business logic without taking into consideration the final deployment topology.  The code can be designed, implemented and unit-tested using a single, embedded space collocated with the business logic on your development machine.  The same code can then be deployed across a system involving hundreds of machines having hundreds of spaces (collocated or not) with the c++ business logic, ultimately processing millions of data items per second with sub-millisecond latency.

# Collocated or not Collocated?

Before actually collocating the required state and business logic, you should take the following into consideration:

1. Is your business logic designed to process incoming data events without accessing remote data located in other partitions?
2. Is your data model designed to support stickiness - to be routed to the same logical partition based on its content?
3. Is your c++ process designed to cope with a self-healing mechanism that will restart the failed c++ instance somewhere else on the network, allowing the system to continue and function without disruption as long as there are machines available to run the application?
4. What is the amount of work involved to process the incoming events? Does it involve lots of IO operations accessing many different resources?
All the above considerations are also relevant for Java and .Net business logic, since both have the ability to collocate the required state.

To help you make the right decision when deploying your application below are some guidelines that correlate to the above considerations:

1. The c++ business logic may access only its collocated space or the entire cluster members.
    - If the collocated space can store both the data required for the processing and the consumed data there is a good chance you can use the collocated mode.
    - If the business logic needs data stored within other partitions you might have 2 space proxies used - one that access only the collocated space and consumes the incoming "tasks" that need to be processed, and one that accesses all cluster members and fetches data using space SQL queries needed for the processing.
    - Advanced implementations would use the Map Reduce technique (at GigaSpaces, we call this the "Service Virtualization Framework" or "Remoting").  This popular technique invokes business logic at the relevant partitions that produce intermediate results.  These results are then delivered to the client that aggregates these and returns the final result to the original caller.

2. In order that incoming data will land at the correct partition, associated objects should have the same routing field value.  A client accessing a clustered space has, by default, a proxy running a simple algorithm that calculates the target partition for each space operation.  The calculation uses by default the hash code value of a field declared as the routing field.  Each POCO class should have one routing field declared where the actual field value can be assigned by getting data from possibly several other fields.
Here is an example for the POCO decoration xml config:


```xml
<class name="myPOCO">
    <property name="routingField" type="int" index="true"/>
    <routing  name="routingField"/>
    <property name="myData" type="string" index="true"/>
    <property name="uid" type="string"/>
    <id name="uid" auto-generate="true" />
  </class>
```

The **routingField** value hash code will be used to rout write/read operations to the correct partition.

Note: for fail-safe operations, a partition may have one or more dedicated backup spaces running in standby mode and holding identical data.

3. With the SBA model business logic state must be stored within the space; i.e. the space is a shared memory resource at the application level.  To ensure data consistency and coherency you should conduct destructive operations using a transaction - i.e. have these as one atomic operation.  Since a primary space may have in-memory backup space(s) running in different machine(s) you would never lose your required state.  Once a primary fails, the existing backup spaces conduct an election voting process where only one of them becomes the primary one.  As a result, the collocated c++ business logic associated with the space also moves into active mode.  At the same time, the GigaSpaces Grid Service Manager (GSM) looks for an available grid container to launch the "missing" backup to, obeying and maintaining the defined SLA for that service.  This constructs a self-healing system allowing your application to continue and function as long as you have machines running the GigaSpaces-provided SLA driven containers.When a processing unit hosts your c++ business logic but accesses a remote space running as a separate process within the same machine as the c++ business logic or in a different remote machine, there is some cost involved which varies depending on the topology.  The remote call overhead depends on the network speed, network bandwidth, data complexity (serialization involved) and its size.  The larger the size of serialized and transported data is, the longer it takes for the remote operation to be completed.  This applies both to write and read operations.

4. When a processing unit hosting your c++ business logic has the space collocated as well, no remote calls are involved when the c++ business logic accessing the space.  Some memory allocation is conducted - this is a result of the c++ runtime passing data into the space crossing the JNI boundary\- this is done via a very efficient protocol (I will have a separate post on this).  If the time spent performing the business logic (worker calculation time) is much longer than the time it takes for the worker to: retrieve the task from the space, write back the result or read required data from the space, it might be logical to run the c++ worker as a stand-alone processing unit, separately from the space.  As a rule of thumb, a good ratio for using remote stand alone workers would be 1:10 or more - i.e. if the average time of performing the 3 basic space remote calls (take, read, write) is 1ms and the time it takes to perform relevant worker calculation (unrelated to the space) is 10 ms, it would be wise to run the c++ worker as a stand alone processing unit.  If the ratio is less than 1:10, go for the embedded space deployment topology.

# c++ Processing Unit Interface

The c++ business logic deployed into the SLA-driven container should inherit from a c++ base class called ICppWorker.  The ICppWorker includes a few methods you must implement:


```java
class CommandObject;
typedef boost::shared_ptr<CommandObject> CommandObjectPtr;
class IWorkerPeer;
class LocalCache;
typedef boost::shared_ptr<LocalCache> CachePtr;
class Serializer;

class ICppWorker
{
public:
	virtual ~ICppWorker() {}
	/**
	* @brief well known entry point cppType()
	*
	* The implementaion of this method must return the fullname of the worker
	*
	* virtual const char* cppType() { return typeid(ExampleWorker).name(); }
	*/
	virtual const char* cppType() = 0;
	/**
	* @brief well known entry point className()
	*
	* The implementor must return the name of this worker in order to catolog
	* and cache the worker in the worker cache.
	*
	* virtual const char* className() { return "ExampleWorker"; }
	*/
	virtual const char* className() = 0;
	/**
	* @brief Initialize
	*
	* This entry point is called upon successful load of the users worker via the
	* SBA container.
	*
	* @param IWorkerPeer* Host. The reference back to the Host Container
	*/
	virtual bool  Initialize(IWorkerPeer* Host) = 0;
	/**
	* @brief run
	*
	* This entry point is called by the host container when work is available for the worker.
	* The worker performs the work described within the CommandObject, the worker is also obliged
	* to return a CommandObject back to the caller which indicates the sucessfullness of the work performed.
	* @see CommandObject
	*
	* @param CommandObjectPtr work. The Command that should be processed.
	*/
	virtual CommandObjectPtr run(CommandObjectPtr work) = 0;
	/**
	* @brief Destroy.
	* This entry point is called prior to the worker becoming elligible for unload.
	*/
	virtual bool  Destroy() = 0;
};
```

The Initialize will be called once the object is instantiated by the SLA container.

The Destroy method will be called once the SLA-driven container is shutdown.

The most important method is the run method.  This is where you place the code that will be running in a continuous manner.  This code would have very simple flow:

1. Getting some data from the space
2. Performing some work - might involve reading additional data from the space
3. Writing back into the space some resulting information

Here is what each phase should include:

1. The first phase would call the take or takeMultiple operations.  In some cases these would be transactional operations.  FIFO take mode might be very relevant here.
2. The second phase might include some calculations.  You might call here the read or readMultiple (in some cases space iterator) to feed into the calculations' required data.  These can read data from the same space the data retrieved from in phase one, or from another space(s).
3. The third phase might include write or writeMutiple calls writing calculated results back into the space. These will be collected by another worker responsible for aggregating results and delegating these to the end clients.  The write or writeMutiple would use the same transaction as used with the first phase.  This phase would commit the transaction.

Here is a simple example:


```java
CommandObjectPtr CppService::run(CommandObjectPtr Object)
{
      genericVector     replyParams = Object->getParameters();
      SpaceProxyPtr     proxy;
      SpaceFinder       finder;
      long long proxyId = any_cast<long long>(replyParams[0]);
      proxy = finder.attach( proxyId, false, m_callback);
      Task taskTemplate;

      while(true)
      {
            Task task = proxy->take(&taskTemplate, NULL_TX, Lease::FOREVER);
            Result result = task->execute();
            proxy->write(result, NULL_TX, Lease::FOREVER);
       }
}
```

The above code injects the space proxy (the proxy could be remote or embedded, single or clustered), performs blocking take using the taskTemplate object, executes the Task::execute() method, and returns back into the space the Result object of the execution.  Once this cycle is completed, another one starts all over again.

# The Processing Unit Declaration

The processing unit is declared using a simple xml file that follows the Spring Framework standard.  It comprises of the:

- Space identity you want to inject into the c++ worker and its scope (local or clustered)
- c++ worker(s) you wish to deploy

See below an example:


```xml
<beans>
    <os-core:space id="space" url="/./space" />
    <os-core:giga-space id="gigaSpace" space="space" />
    <bean id="cpp" class="com.gigaspaces.javacpp.openspaces.CXXBean">
        <property name="gigaSpace" ref="gigaSpace" />
               <property name="workerName" value="CppService" />
    </bean>
</beans>
```

The url="/./space" instructs the GigaSpaces runtime to start the c++ worker with a collocated space instance running within the same process.  This will allow the c++ worker to perform space operations in-memory without any remote calls involved.  If a cluster SLA declared, accessing other remote cluster members will involve remote calls.

Having
    url="jini://*/*/space"
means the c++ worker will access remote space(s).  These spaces may span multiple machines and may have any clustered topology (replicated or partitioned).
See more details about the [Space URL](./the-space-configuration.html).

Optional settings you may include as part of the processing unit declaration:

- Space properties settings
- Transaction Manager settings
- SLA settings such as cluster topology
- Local cache/view settings
- External Data Source settings
- Security settings
- Space Filter settings
- Replication Filter settings
- Space Mode Context Loader settings

Here is an example for a processing unit with a c++ worker deployed, using a clustered SLA-driven container that is running in a partitioned topology with one backup:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{%currentversion%}}/sla/openspaces-sla.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd">
    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer"/>
    <os-core:space id="space" url="/./space" />
    <os-core:giga-space id="gigaSpace" space="space" />
    <bean id="cpp" class="com.gigaspaces.javacpp.openspaces.CXXBean">
        <property name="gigaSpace" ref="gigaSpace" />
        <property name="workerName" value="CppService" />
    </bean>
    <os-sla:sla cluster-schema="partitioned-sync2backup" number-of-instances="2" number-of-backups="1"
            max-instances-per-vm="1"/>
</beans>
```

# Multiple c++ Workers within the Same Processing Unit

In some cases your business logic may involve multiple c++ workers running within the same processing unit and dealing simultaneously with different aspects of the processing.  These workers may perform different parts of the processing independently or may depend on each other (workflow), exchanging data via the space.

Here is an example for a processing unit declaration that introduces a processing unit with two c++ workers:


```xml
<bean id="cppWorker1" class="com.gigaspaces.javacpp.openspaces.CXXBean">
        <property name="gigaSpace" ref="gigaSpace" />
        <property name="workerName" value="CppService1" />
</bean>

<bean id="cppWorker2" class="com.gigaspaces.javacpp.openspaces.CXXBean">
        <property name="gigaSpace" ref="gigaSpace" />
        <property name="workerName" value="CppService2" />
</bean>
```

# Deploying the c++ Processing Unit

Deploying your c++ processing unit requires 2 steps:

    - Placing the processing unit declaration at the deployment folder. The default deployment folder located at <GigaSpaces Root>\deploy folder. You can change this using the gsm settings.

    - Placing the processing unit libraries at the native libraries folder. The default location for these located at <GigaSpaces Root>\lib\ServiceGrid\native. You can change this using the <GigaSpaces Root>\bin\setenv.bat/sh script.

Here is how your processing unit deployed folder should look like:


```java
<GigaSpaces Root>deploy\cppPUexample

- META-INF
       spring <-- Here you should place your processing unit declaration - called pu.xml

- lib
```

Run `<GigaSpaces Root>\bin\gsm` And few `<GigaSpaces Root>\bin\gsc` and deploy your processing unit using the following command:


```java
<GigaSpaces Root>\bin\gs pudeploy cppPUexample
```

