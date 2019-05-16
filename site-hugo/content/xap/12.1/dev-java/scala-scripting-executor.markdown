---
type: post121
title:  Scripting Executor
categories: XAP121
parent: scala.html
weight: 400
canonical: auto
---


{{% ssummary  %}}{{% /ssummary %}}



[Dynamic Language Tasks](./task-dynamic-language.html) feature has been extended and now supports Scala based script execution.

# Configuration

Here is how you would configure a processing unit to run a scripting executor with scala support and use it from a client proxy. For detailed information on the `Scripting Executor` framework, see [Dynamic Language Tasks](./task-dynamic-language.html).

## Processing Unit Configuration


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<bean id="scriptingExecutorImpl" class="org.openspaces.remoting.scripting.DefaultScriptingExecutor">
  <property name="executors">
    <map>
      <entry key="scala">
	<bean class="org.openspaces.remoting.scripting.ScalaLocalScriptExecutor">
	</bean>
      </entry>
    </map>
  </property>
</bean>

<os-remoting:service-exporter id="serviceExporter">
  <os-remoting:service ref="scriptingExecutorImpl"/>
</os-remoting:service-exporter>

<os-events:polling-container id="remotingContainer" giga-space="gigaSpace">
  <os-events:listener ref="serviceExporter"/>
</os-events:polling-container>
```

## Client Side Configuration


```xml
<os-core:space-proxy  id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>

<os-remoting:executor-proxy id="executorScriptingExecutor" giga-space="gigaSpace"
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
</os-remoting:executor-proxy>
```

# Usage

3 new [Script]({{% api-javadoc %}}/index.html?org/openspaces/remoting/scripting/Script.html) implementations have been added to support compilation and caching of compiled scala scripts. These provide the ability to explicitly set the static type for script parameters which is required when the runtime type is not public. In most cases, there is no need to define these as they can be deduced to use the parameter runtime type.

- `org.openspaces.remoting.scripting.ScalaTypedStaticScript` which extends [StaticScript]({{% api-javadoc %}}/index.html?org/openspaces/remoting/scripting/StaticScript.html).
- `org.openspaces.remoting.scripting.ScalaTypedStaticResourceScript` which extends [StaticResourceScript]({{% api-javadoc %}}/index.html?org/openspaces/remoting/scripting/StaticResourceScript.html).
- `org.openspaces.remoting.scripting.ScalaTypedResourceLazyLoadingScript` which extends [ResourceLazyLoadingScript]({{% api-javadoc %}}/index.html?org/openspaces/remoting/scripting/ResourceLazyLoadingScript.html).

# Example

Import org.openspaces.remoting.scripting.ScalaTypedStaticScript into scope to use the methods demonstrated below.


```scala
val code = """
val readData: Any = gigaSpace.read(null)
val numberAsString = someNumber.toString
val setAsString = someSet.toString
numberAsString + " " + someString + " " + setAsString + " " + readData
"""

val script = new ScalaTypedStaticScript("myScript", "scala", code)
  .parameter("someNumber", 1)
  .parameter("someString", "str")
  // explicit type is requierd because the runtime type of the generated
  // set is not public
  .parameter("someSet", Set(1,2,3), classOf[Set[_]])

val result = executor.execute(script)

println("Script execution result: " + result)
```
