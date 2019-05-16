---
type: post101
title:  Event Driven Remoting
categories: XAP101
parent: space-based-remoting-overview.html
weight: 300
canonical: auto
---

{{% ssummary%}}{{%/ssummary%}}


Event Driven remoting is characterized by the nature of how the client communicates with the server. Under the wires, event driven remoting uses the space write and take capabilities. The client writes an internal invocation Entry, that holds the different invocation information (such as service name, method name, and arguments) and then waits for a response (by using blocking take on an expected response). The server, meanwhile, waits (by using OpenSpaces event containers) for internal invocation entries, and once one arrives, it executes the requested service and writes back a response.

{{% refer %}}
You may find an Event Driven Remoting example in the [Event Driven Remoting Example best practice](/sbp/event-driven-remoting-example.html) section.
{{% /refer %}}

# Defining the Contract

In order to support remoting, the first step is to define the contract between the client and the server. In our case, the contract is a simple Java interface. Here is an example:


```java
public interface IDataProcessor {

    /**
     * Process a given Data object, returning the processed Data object.
     */
    Data processData(Data data);
}
```

{{% note %}}
The `Data` object should be `Serializable`, or better yet, `Externalizable` (for better performance).
{{%/note%}}

# Implementing the Contract

Next, an implementation of this contract needs to be provided. This implementation will "live" on the server side. Here is a sample implementation:

{{%tabs%}}
{{%tab "  Annotation "%}}


```java
@RemotingService
public class DataProcessor implements IDataProcessor {

    public Data processData(Data data) {
    	data.setProcessor(true);
    	return data;
    }
}
```

{{% /tab %}}
{{%tab "  XML "%}}


```java
public class DataProcessor implements IDataProcessor {

    public Data processData(Data data) {
    	data.setProcessor(true);
    	return data;
    }
}
```

{{% /tab %}}
{{% /tabs %}}

The XML tab corresponds to exporting the service using xml configuration. The Annotation tab corresponds to exporting the service using annotations.

# Exporting the Service Over the Space

The next step is exporting the service over the space. Exporting the service is done on the server side. As with other Spring remoting support, exporting the service and the method of exporting the service is a configuration time decision. Here is an example of a Spring XML configuration:

{{%tabs%}}
{{%tab "  Annotation "%}}


```xml

<!-- Support @RemotingService component scanning -->
<context:component-scan base-package="com.mycompany"/>

<!-- Support the @RemotingService annotation on a service-->
<os-remoting:annotation-support />

<os-core:embedded-space id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:service-exporter id="serviceExporter" />

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-events:listener ref="serviceExporter"/>

</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="dataProcessor" class="DataProcessor" />

<os-remoting:service-exporter id="serviceExporter">
     <os-remoting:service ref="dataProcessor"/>
</os-remoting:service-exporter>

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-events:listener ref="serviceExporter"/>

</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="DataProcessor" />

<bean id="serviceExporter" class="org.openspaces.remoting.SpaceRemotingServiceExporter">
    <property name="services">
        <list>
            <ref bean="dataProcessor" />
        </list>
    </property>
</bean>

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="eventListener" ref="serviceExporter">
</bean>
```

{{% /tab %}}
{{% /tabs %}}

Exporting the service as a remote space service uses the [event polling container](./polling-container.html). The polling container automatically listens to internal `EventDrivenSpaceRemotingEntry` objects, and writes back an updated `EventDrivenSpaceRemotingEntry` to the space. All polling event container features, such as transactions and different receive handlers can be used with Space remoting.

## Exporting a Service Non-Collocated with the Space

With the above example the Service is collocated with the space. There might be scenarios where the service might be running as part of a separate PU where the space and the service running in different PUs. In this case the Service configuration should use a polling container configured in non-blocking mode:


```xml
   <os-core:space-proxy  id="space" name="mySpace"/>

   <os-core:giga-space id="gigaspace" space="space" tx-manager="transactionManager"/>

   <bean id="SimpleServiceImpl" class="example.eventremoting.service.SimpleServiceImpl"/>

   <os-remoting:service-exporter id="ServiceExporter" >
	<os-remoting:service ref="SimpleServiceImpl"/>
   </os-remoting:service-exporter>

    <os-core:distributed-tx-manager id="transactionManager" />

    <os-events:polling-container id="simpleServiceAsyncContainer" giga-space="gigaspace">
	    <os-events:listener ref="ServiceExporter" />
    	<os-events:tx-support tx-manager="transactionManager" tx-timeout="30000" />

  	<os-events:receive-operation-handler>
        <bean class="org.openspaces.events.polling.receive.SingleTakeReceiveOperationHandler">
            <property name="nonBlocking" value="true" />
            <property name="nonBlockingFactor" value="10" />
        </bean>
    </os-events:receive-operation-handler>

    </os-events:polling-container>
```

{{% info %}}
Usually, exporting services is done on a Processing Unit (or Spring application context) that starts an embedded space.
{{%/info%}}

# Using the Service on the Client-Side

In order to use the exported `IDataProcessor` on the client side, beans should use the `IDataProcessor` interface directly:


```java
public class DataRemoting {

    private IDataProcessor dataProcessor;

    public void setDataProcessor(IDataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }
}
```

Configuring the `IDataProcessor` proxy can be done in the following manner:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy  id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:event-driven-proxy id="dataProcessor" giga-space="gigaSpace"
                   interface="org.openspaces.example.data.common.IDataProcessor" timeout="15000">
</os-remoting:event-driven-proxy>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="org.openspaces.remoting.EventDrivenSpaceRemotingProxyFactoryBean">
    <property name="gigaSpace" ref="gigaSpace" />
    <property name="serviceInterface" value="org.openspaces.example.data.common.IDataProcessor" />
    <property name="timeout" value="15000" />
</bean>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();

IDataProcessor dataProcessor = new EventDrivenRemotingProxyConfigurer<IDataProcessor>(gigaSpace, IDataProcessor.class)
                               .timeout(15000)
                               .proxy();

DataRemoting dataRemoting = new DataRemoting();

dataRemoting.setDataProcessor(dataProcessor);
```

{{% /tab %}}
{{% /tabs %}}

The above example uses the `event-driven-proxy` bean in order to create the remoting proxy, which can then be injected into the `DataRemoting` object. OpenSpaces remoting also allows you to inject the remoting proxy directly into the remoting service property using annotations. Here is an example using the `DataRemoting` class:


```java
public class DataRemoting {

    @EventDrivenProxy(timeout=15000)
    private IDataProcessor dataProcessor;

    // ...
}
```

If there are more than one `GigaSpace` beans defined within the application context, the ID of the `giga-space` bean needs to be defined on the annotations.

In order to enable this feature, the following element needs to be added to the application context XML:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-remoting:annotation-support />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.remoting.RemotingAnnotationBeanPostProcessor" />
```

{{% /tab %}}
{{% /tabs %}}

## Remote Routing Handler

Many times, space remoting is done by exporting services in a space with a partitioned cluster topology. The service is exported when working directly with a cluster member (and not against the whole space cluster). When working with such a topology, the client side remoting automatically generates a random routing index value (using the newly created remote invocation object hashcode). In order to control the routing index, the following interface can be implemented:


```java
public interface RemoteRoutingHandler<T> {

    /**
     * Returns the routing field value based on the remoting invocation. If <code>null</code>
     * is returned, will use internal calcualtion of the routing index.
     */
    T computeRouting(SpaceRemotingInvocation remotingEntry);
}
```

Here is a sample implementation which uses the first parameter `Data` object type as the routing index.


```java
public class DataRemoteRoutingHandler implements RemoteRoutingHandler<Long> {

    public Long computeRouting(SpaceRemotingInvocation remotingEntry) {
        if (remotingEntry.getMethodName().equals("processData")) {
            Data data = (Data) remotingEntry.getArguments()[0];
            return data.getType();
        }
        return null;
    }
}
```

Finally, the wiring is done in the following manner:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:event-driven-proxy id="dataProcessor" giga-space="gigaSpace"
                   interface="org.openspaces.example.data.common.IDataProcessor" timeout="15000">
    <os-remoting:routing-handler>
        <bean class="org.openspaces.example.data.feeder.support.DataRemoteRoutingHandler"/>
    </os-remoting:routing-handler>
</os-remoting:event-driven-proxy>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="org.openspaces.remoting.EventDrivenSpaceRemotingProxyFactoryBean">
    <property name="gigaSpace" ref="gigaSpace" />
    <property name="serviceInterface" value="org.openspaces.example.data.common.IDataProcessor" />
    <property name="timeout" value="15000" />
    <property name="remoteRoutingHandler">
        <bean class="org.openspaces.example.data.feeder.support.DataRemoteRoutingHandler"/>
    </property>
</bean>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();

IDataProcessor dataProcessor = new EventDrivenRemotingProxyConfigurer<IDataProcessor>(gigaSpace, IDataProcessor.class)
                               .timeout(15000)
                               .remoteRoutingHandler(new DataRemoteRoutingHandler())
                               .proxy();

DataRemoting dataRemoting = new DataRemoting();

dataRemoting.setDataProcessor(dataProcessor);
```

{{% /tab %}}
{{% /tabs %}}

## Routing Annotation

The above option of using the remote routing handler is very handy when not using annotations. OpenSpaces remoting supports the `Routing` annotation in order to define which of the parameters controls the routing. Here is an example:


```java
public interface MyService {

    void doSomething(@Routing int param1, int param2);
}
```

In the above example, routing is done using the `param1` value. When using complex objects, a method name can be specified, which is invoked on the parameter in order to get the actual routing index. Here is an example:


```java
public interface MyService {

    void doSomething(@Routing("getProperty") Value param1, int param2);
}
```

In the above example, the `getProperty` method is called on the `Value` object and its return value is used to extract the routing index.

{{% note %}}
**Note:** Using a [SpaceDocument](./document-api.html) as the annotated routing argument will cause an exception since the `SpaceDocument` class does not define a getter method for each property, but rather a generic getter method to get a property value by its name. The solution is either to extend the `SpaceDocument` class as described in [Extending Space Documents](./document-extending.html), and define the relevant `getProperty` method in the extension class, or use the `RemoteRoutingHandler` mentioned above.
{{%/note%}}

# Server Side Services Injection

Each parameter of a remote service can be dynamically injected as if it were a bean defined within the server side (where the service is actually executed) context. This allows you to utilize server side resources and state, within the actual invocation. In order to enable such injection the service interface should either be annotated with `AutowireArguments` annotation, or implement the marker interface `AutowireArgumentsMarker`. Let's see an example:


```java
@AutowireArguments
public interface MyService {

    void doSomething(Value param1);
}
```

The above service exposes a service which accepts a `Value` as a parameter. The `Value` parameter, if needed, can be injected dynamically with server side resources and state. For example, if the server side execution needs to be aware of the `ClusterInfo`, the `Value` parameter can implement the `ClusterInfoAware` interface. Here is what it looks like:


```java
public class Value implements ClusterInfoAware {

    private transient ClusterInfo clusterInfo;

    public void setClusterInfo(ClusterInfo clusterInfo) {
        this.clusterInfo = clusterInfo;
    }
}
```

{{% info %}}
Note the fact that `clusterInfo` is transient, so it won't be marshaled after injection back to the client.
{{%/info%}}

Another example is injecting another service that is defined in the server side directly into the parameter, for example, using Spring `@Autowired` annotation.


```java
public class Value implements ClusterInfoAware {

    @Autowired
    private transient AnotherService anotherService;
}
```

# Execution Aspects

Space based remoting allows you to inject "aspects" that can wrap the invocation of a remote method on the client side, as well as wrapping the execution of an invocation on the server side.

## The Client Invocation Aspect

The client invocation aspect interface is shown below. You should implement this interface and wire and instance of the implementing class to the client side remote proxy, as explained below:


```java
public interface RemoteInvocationAspect<T> {

    /**
     * The aspect is called instead of the actual remote invocation. The methodInvocation can
     * be used to access any method related information. The remoting invoker passed can be used
     * to actually invoke the remote invocaiton.
     */
    T invoke(MethodInvocation methodInvocation, RemotingInvoker remotingInvoker) throws Throwable;
}
```

An implementation of such an aspect can be configured as follows:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy  id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:event-driven-proxy id="dataProcessor" giga-space="gigaSpace"
                   interface="org.openspaces.example.data.common.IDataProcessor">
    <os-remoting:aspect>
    	<bean class="eg.MyRemoteInvocationAspect" />
    </os-remoting:aspect>
</os-remoting:event-driven-proxy>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="org.openspaces.remoting.EventDrivenSpaceRemotingProxyFactoryBean">
    <property name="gigaSpace" ref="gigaSpace" />
    <property name="serviceInterface" value="org.openspaces.example.data.common.IDataProcessor" />
    <property name="remoteInvocationAspect">
    	<bean class="eg.MyRemoteInvocationAspect" />
    </property>
</bean>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();

IDataProcessor dataProcessor = new EventDrivenRemotingProxyConfigurer<IDataProcessor>(gigaSpace, IDataProcessor.class)
                               .timeout(15000)
                               .remoteInvocationAspect(new MyRemoteInvocationAspect())
                               .proxy();

DataRemoting dataRemoting = new DataRemoting();

dataRemoting.setDataProcessor(dataProcessor);
```

{{% /tab %}}
{{% /tabs %}}

The instance of the class that implements the `RemoteInvocationAspect` interface will be called by the framework prior to the invocation on the client side. Note that it is up to the aspect implementation to decide whether or not the call should proceed.
If the aspect implementation decides to proceed with the call it should call `method.invoke()` on the provided `method argument`. Otherwise the call to the server will not be performed.

{{% anchor serverExecutionApect %}}

## The Server Invocation Aspect

The server side invocation aspect interface is shown below. You should implement this interface and wire and instance of the implementing class to the server side service exporter (this is the component that is responsible for exposing your service bean to remote clients):


```java
public interface ServiceExecutionAspect {

    /**
     * A service execution callback allows you to wrap the execution of "server side" service. If
     * actual execution of the service is needed, the <code>invoke</code> method will need to
     * be called on the passed <code>Method</code>, using the service as the actual service to
     * invoke it on, and {@link SpaceRemotingInvocation#getArguments()} as the method arguments.
     *
     * <p>As an example: <code>method.invoke(service, invocation.getArguments())</code>.
     */
    Object invoke(SpaceRemotingInvocation invocation, Method method, Object service)
                                  throws InvocationTargetException, IllegalAccessException;
}
```

An implementation of such an aspect can be configured as follows:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="dataProcessor" class="DataProcessor" />

<os-remoting:service-exporter id="serviceExporter">
     <os-remoting:aspect>
         <bean class="eg.MyServiceExecutionAspect" />
     </os-remoting:aspect>
     <os-remoting:service ref="dataProcessor"/>
</os-remoting:service-exporter>

<os-events:polling-container id="eventContainer" giga-space="gigaSpace">

    <os-events:listener ref="serviceExporter"/>

</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="DataProcessor" />

<bean id="serviceExporter" class="org.openspaces.remoting.SpaceRemotingServiceExporter">
    <property name="serviceExecutionAspect">
         <bean class="eg.MyServiceExecutionAspect" />
    </property>
    <property name="services">
        <list>
            <ref bean="dataProcessor" />
        </list>
    </property>
</bean>

<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="eventListener" ref="serviceExporter">
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The instance of the class that implements the `ServiceExecutionAspect` interface will be called by the framework prior to the invocation of the service bean on the server side. Note that it is up to the aspect implementation to decide whether or not the call should proceed.
If the aspect implementation decides to proceed with the call it should call `method.invoke()` on the provided `method` argument. Otherwise the call to the service bean will not be performed.

# The Metadata Handler

When executing a service using Space based remoting, a set of one or more metadata arguments (in the form of a `java.lang.Object` array) can be passed to the server with the remote invocation. This feature is very handy when you want to pass certain metadata with every remote call that is not part of the arguments of the method you call. A prime example for using meta arguments is security -- i.e. passing security credentials as meta arguments, and using a server side aspect to authorize the execution or to log who actually called the method.

To create the meta arguments on the client side you should implement the following interface and inject an instance of the implementing class to the client side proxy:


```java
public interface MetaArgumentsHandler {

    /**
     * Meta argument handler can control the meta data objects that will be used
     * for the remote invocation.
     */
    Object[] obtainMetaArguments(SpaceRemotingInvocation remotingEntry);
}
```

The following snippets show how to plug a custom meta arguments handler to the client side remote proxy. The `Object` array returned by the implementation of the `MetaArgumentsHandler` interface will be sent along with the invocation to server side.

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy  id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:event-driven-proxy id="dataProcessor" giga-space="gigaSpace"
                   interface="org.openspaces.example.data.common.IDataProcessor">
    <os-remoting:meta-arguments-handler>
    	<bean class="eg.MyMetaArgumentsHandler" />
    </os-remoting:meta-arguments-handler>
</os-remoting:event-driven-proxy>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<bean id="dataProcessor" class="org.openspaces.remoting.EventDrivenSpaceRemotingProxyFactoryBean">
    <property name="gigaSpace" ref="gigaSpace" />
    <property name="serviceInterface" value="org.openspaces.example.data.common.IDataProcessor" />
    <property name="metaArgumentsHandler">
    	<bean class="eg.MyMetaArgumentsHandler" />
    </property>
</bean>

<bean id="dataRemoting" class="DataRemoting">
    <property name="dataProcessor" ref="dataProcessor" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();

IDataProcessor dataProcessor = new EventDrivenRemotingProxyConfigurer<IDataProcessor>(gigaSpace, IDataProcessor.class)
                               .timeout(15000)
                               .metaArgumentsHandler(new MyMetaArgumentsHandler())
                               .proxy();

DataRemoting dataRemoting = new DataRemoting();

dataRemoting.setDataProcessor(dataProcessor);
```

{{% /tab %}}
{{% /tabs %}}

The way to access the meta arguments on the server side is to configure a [server side execution aspect](#serverExecutionApect) by implementing the `ServiceExecutionAspect` and wiring it on the server side as shown [above](#serverExecutionApect). To access the meta arguments, you should call `SpaceRemotingInvocation.getMetaArguments()` on the `invocation` argument provided to the server side aspect.

# Asynchronous Execution

OpenSpaces event driven remoting supports asynchronous execution of remote services using the `java.util.concurrent` `Future` interface. Since the remoting implementation is asynchronous by nature, a `Future` can be used to execute remote services asynchronously.

Since the definition of the interface acts as the contract between the client and the server, asynchronous invocation requires the interface to also support the same business method, returning a `Future` instead of its actual return value. The asynchronous nature is only relevant on the client side, provided by the asynchronous nature of OpenSpaces remoting. There are two options to implement such behavior:

## Different Service Interfaces

The client and the server can have a different service interface definition under the same package and under the same name. The server includes the actual implementation:


```java
public interface SimpleService {

    String say(String message);

    int calc(int value1, int value2);
}
```

The client has the same method except it returns a future:


```java
public interface SimpleService {

    Future<String> say(String message);

    int calc(int value1, int value2);
}
```

In the above example, the `say()` method uses a `Future` in order to receive the result, while the `calc` method remains synchronous.

{{% info %}}
When using different services, it is important to include the interface definition in the processing unit `lib` directory (or the compiled classes root directory), so that both client and server use their own definitions and don't share a single one.
{{%/info%}}

## Single Service Interface

Both the client and server share the same interface, with the interface holding both synchronous and asynchronous services. Here is an example:


```java
public interface SimpleService {

    String say(String message);

    Future<String> asyncSay(String message);
}
```

In the above case, the server implementation does not need to implement the `asyncSay` method (simply return `null` for the `asyncSay` method). The client side can choose which service to invoke.
You should note the naming convention and the signature of the asynchronous method in this case.
If the regular method looks as follows:
`T doSomething(String arg)`, T being the return value type, then the asynchronous method should have the following signature:
`Future<T> asyncDoSomething(String arg)`.
In other words, the regular method should be prefixed with `async` and the return value should be a `Future` of the original return value.

# Failover

In case of a Primary Backup clusters and Partitioned Sync2Backup clusters, when there is failure of primary space partition, remoting request is transparently forwarded to the backup.

Event Driven Remoting proxy is aware of each partition in the cluster and connection with the failed primary partition will get interrupted/disconnected in case of a failure. Proxy recognizes failure and redirects the request to the backup partition. When the backup becomes ready (any warmup code that needs to run and is ready to accept operations), it will accept this request and process the request. All of this failover behavior is done within the GigaSpaces proxy code and client does not need any extra logic. For more information refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).

If the backup space does not become ready to accept requests within the configured number of retries, execute request will fail with an Exception.


# Considerations

If the remote method is called frequently or large complex objects are used as return types, it is recommended to implement optimized serialization such as `Externalizable` for the returned value object or use libraries such as [kryo](https://github.com/EsotericSoftware/kryo).

{{% refer %}}
For more information see [Custom Serialization](./custom-serialization.html).
{{% /refer %}}
