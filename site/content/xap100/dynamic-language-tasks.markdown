---
type: post100
title:  Dynamic Language Tasks
categories: XAP100
parent: task-execution-overview.html
weight: 300
---

{{%ssummary%}}{{%/ssummary%}}

[Event Driven Remoting](./event-driven-remoting.html) and [Executor Based Remoting](./executor-based-remoting.html) allow you to simply expose and execute custom services using the Space as the transport layer. The dynamic tasks feature (also known as "scripting support") allows you to utilize a built remote service, exposed using these two remoting mechanisms, that can execute dynamic language scripts in the space.
A major benefit of using scripting or dynamic languages is the ability to simply change the behavior of the executed task without the need to recompile your application. As per the SBA paradigm, dynamic language scripts can be executed collocated with the Space, allowing you to execute the task as close as possible to the data it needs to access, and then return a result.

The following section goes through the steps needed in order to execute dynamic language tasks. The other sections in this page dive more deeply into different dynamic language support features.

The first step in using scripting is exposing the built in scripting service over the Space. Here is a simple example:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<!-- The scripting built in service executor -->
<bean id="scriptingExecutor" class="org.openspaces.remoting.scripting.DefaultScriptingExecutor" />

<!-- The service exporter exposing the scripting service -->
<os-remoting:service-exporter id="serviceExporter">
     <os-remoting:service ref="scriptingExecutor"/>
</os-remoting:service-exporter>

<os-core:embedded-space id="space" name="mySpace"

<os-core:giga-space id="gigaSpace" space="space"/>

<!-- Polling container exposing the service exporter as async remoting -->
<os-events:polling-container id="eventContainer" giga-space="gigaSpace">
    <os-events:listener ref="serviceExporter">
</os-events:polling-container>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<!-- The scripting built in service executor -->
<bean id="scriptingExecutor" class="org.openspaces.remoting.scripting.DefaultScriptingExecutor" />

<!-- The service exporter exposing the scripting service -->
<bean id="serviceExporter" class="org.openspaces.remoting.SpaceRemotingServiceExporter">
    <property name="services">
        <list>
            <ref bean="scriptingExecutor" />
        </list>
    </property>
</bean>

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
</bean>

<!-- Polling container exposing the service exporter as async remoting -->
<bean id="eventContainer" class="org.openspaces.events.polling.SimplePollingEventListenerContainer">
    <property name="eventListener" ref="serviceExporter">
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The next simple step is using the exposed scripting service on the client side, here is a groovy example :


```java
public class MyRemoting {

    @EventDrivenScriptingExecutor
    private ScriptingExecutor asyncScriptingExecutor;

    @ExecutorScriptingExecutor
    private ScriptingExecutor execScriptingExecutor;

    public void doSomeScripting() {
        asyncScriptingExecutor.execute(new StaticScript("myScriptAsync", "groovy",
                                                                "println 'Async Hello Groovy'"));

        execScriptingExecutor.execute(new StaticScript("myScriptExec", "groovy",
                                                               "println 'Sync Hello Groovy'"));
    }
}
```

On the client side Spring XML configuration, the following needs to be defined (mainly to define the connection to the Space as the transport layer, and define the scripting annotation processor):

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy id="space" name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:annotation-support />

<bean id="myRemoting" class="MyRemoting" />
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

<bean id="remotingAnnotationSupport" class="org.openspaces.remoting.RemotingAnnotationBeanPostProcessor" />

<bean id="myRemoting" class="MyRemoting" />
```

{{% /tab %}}
{{% /tabs %}}

# What is a Script

`Script` is a simple interface that is defined within the OpenSpaces scripting over remoting support. It has the following basic methods:


```java
public interface Script extends BroadcastIndicator {

    /**
     * The name of the script. Should uniquely identify scripts.
     */
    String getName();

    /**
     * The type of the script (or actually, language). For example, <code>groovy</code> or <code>ruby</code>.
     */
    String getType();

    /**
     * The script source as a string.
     */
    String getScriptAsString();

    /**
     * One or more parameters that will be passed to the script.
     */
    Map<String, Object> getParameters();

    /**
     * The routing index for the script. Can be <code>null</code>. Defaults
     * to a random routing.
     */
    Object getRouting();

    /**
     * Should this script be cached or not. Defaults to <code>true</code>.
     */
    boolean shouldCache();

    /**
     * Returns a set of meta arguments that are used for remote invocation.
     *
     * @see org.openspaces.remoting.SpaceRemotingInvocation#getMetaArguments()
     */
    Object[] getMetaArguments();
}
```

There are several implementations of this class which is covered more in the client section. OpenSpaces comes with three different implementations of the `Script` interface, they are:

- `StaticScript`: A script that holds the actual script as a string. The name, type, and script must be provided.
- `StaticResourceScript`: A static script that uses Spring `Resource` and `ResourceLoader` to load a given script (for example, from the classpath).
- `ResourceLazyLoadingScript`: A lazy loading script that uses Spring abstraction on top of a resource. The script resource location uses Spring notations for location (similar to a URL, with the addition of the `classpath:` prefix).

# Exposing JavaScript Scripting Service

OpenSpaces scripting over remoting support has a built in service that allows to expose the execution of scripting code. The service is implemented using the `DefaultScriptingExecutor` class. The executor supports several scripting languages out of the box, allows to cache compiled scripts, and automatically expose common beans as script parameters.

## Local Script Executor

The `DefaultScriptingExecutor` holds one or more `LocalScriptExecutor`. A `LocalScriptExecutor` implementation is an abstraction on top of an actual scripting language (such as groovy or jruby). OpenSpaces scripting support comes with built in local executors for groovy, jruby, and Java 6 `ScriptEngine` abstraction.

By default, the default scripting executor identifies if groovy, jruby, or Java 6 scripting support exist within the classpath, and automatically registers then as possible local executors. The relevant local executors are `GroovyLocalScriptExecutor` for groovy, `JRubyLocalScriptExecutor` for jruby, and `Jsr223LocalScriptExecutor` for Java 6. The local scripting executor is chosen based on the `Script#getType()`.

The Java 6 local script executor is a special case as in-itself it allows to execute several scripting engines types. The `DefaultScriptingExecutor` handles that by trying to offload the execution of an unidentified type to the `Jsr223LocalScriptExecutor`. Unidentified type is one that has no specific `LocalScriptExecutor` configured for it within the `DefaultScriptingExecutor`. This means that if one wishes to execute a JavaScript script that comes built in with Java 6, the type for the script should be `JavaScript` (and not Jsr223).

## Script Execution Parameters

The `Script` interface allows to define specific parameters per script. The `DefaultScriptingExecutor` can be configured with a set of parameters that is passed to each script execution. A `setParameters` that accepts a Map can be passed to each script.

By default, the `DefaultScriptingExecutor` passes beans of type `GigaSpace`, keyed by their bean id. The Spring `ApplicationContext` is passed to the script under the `applicationContext` key, and the `ClusterInfo` object is passed under the `clusterInfo` key.

As an example, if my server side Spring application context has a `GigaSpace` bean with id of `myGigaSpace`, a simple groovy script to use it (without configuring anything within the default executor) can be: `myGigaSpace.write(new Object())`. Note, we use the id of the bean to access the `GigaSpace` and write a new Object.

{{% info %}}
A quick note regarding usage of `GigaSpace` beans. The clustered flag plays an important role when using it. This basically allows to control if the script execution of `GigaSpace` operations works only against its clustered member space, or against the whole cluster.
{{%/info%}}

## Script Compilation Caching

If a scripting engine support compilation of scripting, the `DefaultScriptingExecutor` allows caching of compiled scripts for faster execution. The actual caching of scripts is abstracted using `CompiledScriptCache` interface, and has default LRU type cache built in.

The compilation is done based on a flag on the `Script` interface (which defaults to true), and if cached, the "key" of the cache is the script name. This means that if a script was changed, it needs to be executed with a different script name in order for it to recompile and take affect.

# Scripting on the Client Side

Using scripting on the client side is very similar to how similar remoting services are used. There are two types of proxies, `executor-proxy` and `event-driven-proxy`, and there are specific scripting annotations that allow to simplify the injection of scripting proxies.

Here is the xml definition of an async scripting proxy (the sync proxy is exactly the same except for the tag/class name of the proxy):

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-remoting:event-driven-proxy id="eventDrivenScriptingExecutor" giga-space="gigaSpace"
                         interface="org.openspaces.remoting.scripting.ScriptingExecutor">
    <os-remoting:aspect>
        <bean class="org.openspaces.remoting.scripting.LazyLoadingRemoteInvocationAspect" />
    </os-remoting:aspect>
    <os-remoting:routing-handler>
        <bean class="org.openspaces.remoting.scripting.ScriptingRemoteRoutingHandler" />
    </os-remoting:routing-handler>
    <os-remoting:meta-arguments-handler>
        <bean class="org.openspaces.remoting.scripting.ScriptingMetaArgumentsHandler" />
    </os-remoting:meta-arguments-handler>
</os-remoting:event-driven-proxy>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="eventDrivenScriptingExecutor" class="org.openspaces.remoting.EventDrivenSpaceRemotingProxyFactoryBean">
    <property name="gigaSpace" ref="gigaSpace" />
    <property name="serviceInterface" value="org.openspaces.remoting.scripting.ScriptingExecutor" />
    <property name="remoteInvocationAspect">
        <bean class="org.openspaces.remoting.scripting.LazyLoadingRemoteInvocationAspect" />
    </property>
    <property name="remoteRoutingHandler">
        <bean class="org.openspaces.remoting.scripting.ScriptingRemoteRoutingHandler" />
    </property>
    <property name="metaArgumentsHandler">
        <bean class="org.openspaces.remoting.scripting.ScriptingMetaArgumentsHandler" />
    </property>
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}
Asynchronous scripting executor:


```java

GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/mySpace")).gigaSpace();
ScriptingExecutor  executor = new EventDrivenScriptingProxyConfigurer (gigaSpace).scriptingExecutor();
```

Executor based scripting executor:


```java

GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/mySpace")).gigaSpace();
ScriptingExecutor  executor = new ExecutorScriptingProxyConfigurer (gigaSpace).scriptingExecutor();
```

{{% /tab %}}
{{% /tabs %}}

The different components implemented by scripting basically enables all the different scripting client side features which we will cover in the next sections. The annotation based injection is a much simplified way to use scripting on the client side and is shown in the Overview section.

## Routing Index

The `Script` interface allows to set a routing index which controls, both in async and sync mode (not in broadcast mode), where the execution of the script is directed. Here is a simple example of it:


```java
asyncScriptingExecutor.execute(new StaticScript("myScript", "groovy","println 'Hello Groovy'").routing(1));
```

This causes the routing to go the the second member (assuming hash based routing is configured). Naturally, most often a business level routing will be used, for example, direct a script that executed on a certain share to a partition based on the share symbol.

## Lazy Loading of Scripts

The `StaticResourceScript` holds the actual script contents and it gets passed for each execution of the script. If script supports caching, there is no need to pass the actual contents of the script on each execution, since once the script was compiled, only its parameters and other values are required, not the actual script. The `LazyLoadingRemoteInvocationAspect` aspect (on the client side) with the `ResourceLazyLoadingScript` script type allow for such behavior. As an example:


```java
asyncScriptingExecutor.execute(new ResourceLazyLoadingScript("test4", "groovy", "classpath:test.groovy").cache(true));
```

The above code only loads the `test.groovy` file if it is required on the server side. By default it is not sent, and if the server has not compiled it, it returns to the client and request the actual script content. The script is then loaded and sent again to the server. This logic is implemented by the `LazyLoadingRemoteInvocationAspect`.
