---
type: post121
title:  The Space Bean
categories: XAP121

---

{{%warning%}}
This page is under construction.
{{%/warning%}}

When a client connects to a space, a proxy is created that holds a connection to the space. All client interaction is performed through this proxy.
The proxy provides a simpler space API using the [GigaSpace]({{% api-javadoc %}}/index.html) interface.
It can be created with a Space Bean and optional parameters.


# Embedded Space

Example:

```xml
<os-core:embedded-space id="space" space-name="xapSpace" 
   versioned="true" lookup-groups="uat" lookup-locators="someHost:4174" />
```

Here are the available parameters for an embedded space:

|   Name | Type |  Description |  Default | Required |  See Also |
|--------|------|--------------|-----------|---------|-----------|
| id                                    |String|  Used to create the GigaSpace Bean| |Yes| [GigaSpace Bean](#the-space-proxy-the-gigaspace-bean)  |
| space-name                            |String| The space name to use| |Yes ||
| lookup-groups                         |String|The Jini Lookup Service group to find the space using multicast        || No |  |
| lookup-locators                       |String| Instructs the started space to locate the Jini Lookup Service on specific host name and port        ||  No  | |
| lookup-timeout                        |Number| Lookup timeout in milli seconds            | 5000ms |No| |
| versioned                             |Boolean|  When false, optimistic lock is disabled. In a local cache and views the default is true, otherwise the default value is false.|False |No| [Annotation](./pojo-attribute-annotations.html#spaceversion)<br>[Optimistic Locking](./transaction-optimistic-locking.html) |
| schema                                |String|  Schema options: mirror, persistent           | |No ||
| mirrored                              |Boolean|Asynchronous Persistency - Write Behind          |False| No |[Mirror Service](./asynchronous-persistency-with-the-mirror.html) |          
| register-for-space-mode-notifications |Boolean| Register for Space mode notifications |False |No| [Space Notifications](./the-space-notifications.html) |
| external-data-source                  |Reference|| |No ||
| space-data-source                     |Reference|Data Source for the Space||No| [Space Persistency](./space-persistency-overview.html)|
| space-sync-endpoint                   |Reference|Space Synchronization Endpoint| |No| [Asynchronous Persistence](./asynchronous-persistency-with-the-mirror.html)|
| enable-member-alive-indicator         || | |No ||
| gateway-targets                       |String|Multi Site WAN Replication | |No |[Multi Site WAN Replication](./multi-site-replication-over-the-wan.html#configuring-a-space-with-gateway-targets)|

## Bean Properties

|   Property | Description |  See Also |
|--------|-------------|-----------|
|all-in-cache-policy | | |
|lru-cache-policy |||
|custom-cache-policy||[Custom eviction policy]({{%currentadmurl%}}/custom-eviction-policy.html)|
|blob-store-data-policy|| [MemoryXtend]({{%currentadmurl%}}/memoryxtend.html)|
|attribute-store|||
|leader-selection|||
|properties|||
|space-filter||[Space Filters](./the-space-filters.html)|
|space-sql-function||[SQL Functions](./query-sql-function.html)|
|annotation-adapter-filter||[Space Filters](./the-space-filters.html)|
|method-adapter-filter||[Space Filters](./the-space-filters.html)|
|filter-provider|||
|query-extension-provider|||
|replication-filter-provider|||
|space-replication-filter||[Multi Site WAN Replication](./replication-gateway-filtering.html)|
|security |Accessing a secured space with username and password |[Secured Space]({{%currentsecurl%}}/securing-your-data.html)|

# Remote Space

Example:

```xml
<os-core:space-proxy id="space" space-name="remoteSpace"
    lookup-groups="uat" lookup-locators="someHost:4174"/>
```

Here are the available parameters for a remote space:

|   Name | Type |  Description |  Default | Required |  See Also |
|--------|------|--------------|-----------|---------|-----------|
| id                                    |String|  Used to create the GigaSpace Bean| |Yes| [GigaSpace Bean](#the-space-proxy-the-gigaspace-bean)  |
| space-name                            |String| The space name to connect to the remote space| |Yes ||
| instance-id                           | Number     |  The id of the space instance within a cluster to connect to | |  No  ||
| lookup-groups                         |String|The Jini Lookup Service group to find the space using multicast        || No |  |
| <nobr>lookup-locators<nober>          |String| Instructs the started space to locate the Jini Lookup Service on specific host name and port        ||  No  | |
| lookup-timeout                        |MSeconds| Lookup timeout in milli seconds            |5000ms |No| |
| versioned                             |Boolean|  When false, optimistic lock is disabled. In a local cache and views the default is true, otherwise the default value is false.|False |No| [Annotation](./pojo-attribute-annotations.html#spaceversion)<br>[Optimistic Locking](./transaction-optimistic-locking.html) |
 
 
## Properties

|   Property | Description |  See Also |
|--------|-------------|-----------|
|security | Accessing a secured space with username and password  |[Secured Space]({{%currentsecurl%}}/securing-your-data.html#processing-unit)|
|properties | | |
 

# GigaSpace Bean

The `GigaSpace` Spring Bean provides a simple way to configure a proxy to be injected into the relevant Bean.

Here is an example on how to create the proxy:

{{%tabs%}}
{{%tab "  Namespace "%}}
```xml
<os-core:embedded-space id="space" space-name="space" />
</os-core:giga-space id="mySpace" space="space" />
```
{{% /tab %}}
{{%tab "Plain XML"%}}

```xml
<os-core:embedded-space id="space" space-name="space">
<bean id="mySpace" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>
```
{{% /tab %}}
{{%tab "Code"%}}
```java

   // Create the Space Bean
   EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("mySpace");

   // Create the Proxy
   GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();

   // shutting down -- closing the Space
   spaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

{{% note %}}
The application is always injected with `os-core:giga-space` bean that wraps always a `os-core:space`.
{{% /note %}}


## Properties

The `GigaSpace` Bean can have the following elements:


|Element|Description|Required|Default Value|
|:------|:----------|:-------|:------------|
|space|This can be an embedded space , remote space , local view or local cache. |YES| |
|clustered|Boolean. [Cluster flag]({{%currentadmurl%}}/clustered-vs-non-clustered-proxies.html). Controlling if this GigaSpace will work with a clustered view of the space or directly with a cluster member. By default if this flag is not set it will be set automatically by this factory. It will be set to true if the space is an embedded one AND the space is not a local cache proxy. It will be set to false otherwise (i.e. the space is not an embedded space OR the space is a local cache proxy)| NO | true for remote proxy , false for embedded proxy|
|<nobr>default-read-timeout<nobr>|Numerical Value. Sets the default read timeout for `read(Object)` and `readIfExists(Object)` operations.|NO| 0 (NO_WAIT). TimeUnit:millsec|
|default-take-timeout|Numerical Value. Sets the default take timeout for `take(Object)` and `takeIfExists(Object)` operations.|NO| 0 (NO_WAIT). TimeUnit:millsec|
|default-write-lease| Numerical Value. Sets the default [space object lease](./leases-automatic-expiration.html) (TTL) for `write(Object)` operation. |NO| FOREVER. TimeUnit:millsec|
|default-isolation| Options: DEFAULT , READ_UNCOMMITTED, READ_COMMITTED , REPEATABLE_READ|NO| DEFAULT|
|tx-manager|Set the transaction manager to enable transactional operations. Can be null if transactional support is not required or the default space is used as a transactional context. |NO| |
|write-modifier|Defines a single default write modifier for the space proxy. Options: NONE, WRITE_ONLY, UPDATE_ONLY, UPDATE_OR_WRITE, RETURN_PREV_ON_UPDATE, ONE_WAY, MEMORY_ONLY_SEARCH, PARTIAL_UPDATE|NO| UPDATE_OR_WRITE |
|read-modifier|The modifier constant name as defined in ReadModifiers. Options:NONE, REPEATABLE_READ, READ_COMMITTED, DIRTY_READ, EXCLUSIVE_READ_LOCK, IGNORE_PARTIAL_FAILURE, FIFO, FIFO_GROUPING_POLL, MEMORY_ONLY_SEARCH|NO|READ_COMMITTED|
|take-modifier|Defines a single default take modifier for the space proxy. Options:NONE, EVICT_ONLY, IGNORE_PARTIAL_FAILURE, FIFO, FIFO_GROUPING_POLL, MEMORY_ONLY_SEARCH|NO| NONE|
|change-modifier|Defines a single default change modifier for the space proxy. Options:NONE, ONE_WAY, MEMORY_ONLY_SEARCH, RETURN_DETAILED_RESULTS|NO| NONE|
|clear-modifier|Defines a single default count modifier for the space proxy. Options:NONE, EVICT_ONLY, MEMORY_ONLY_SEARCH|NO| NONE|
|count-modifier|Defines a single default count modifier for the space proxy. Options:NONE, REPEATABLE_READ, READ_COMMITTED, DIRTY_READ, EXCLUSIVE_READ_LOCK, MEMORY_ONLY_SEARCH|NO| NONE|

Here is an example of the `GigaSpace` Bean:

{{%tabs%}}
{{%tab "Namespace"%}}


```xml
 <os-core:space id="mySpace" name="space"/>

 <os-core:giga-space id="gigaSpaceClustered" space="mySpace" clustered="true"
  	 default-read-timeout="10000"
  	 default-take-timeout="10000"
  	 default-write-lease="100000">
  	 <os-core:read-modifier value="FIFO"/>
  	 <os-core:change-modifier value="RETURN_DETAILED_RESULTS"/>
  	 <os-core:clear-modifier value="EVICT_ONLY"/>
  	 <os-core:count-modifier value="READ_COMMITTED"/>
  	 <os-core:take-modifier value="FIFO"/>

  	 <!-- to add more than one modifier, simply include all desired modifiers -->
  	 <os-core:write-modifier value="PARTIAL_UPDATE"/>
  	 <os-core:write-modifier value="UPDATE_ONLY"/>
  	</os-core:giga-space>
```
{{% /tab %}}
{{%tab "  Plain XML "%}}

```xml
<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
 	 <property name="space" ref="space" />
 	 <property name="clustered" value="true" />
 	 <property name="defaultReadTimeout" value="10000" />
 	 <property name="defaultTakeTimeout" value="100000" />
 	 <property name="defaultWriteLease" value="100000" />
 	 <property name="defaultWriteModifiers">
 	 <array>
 	 <bean id="updateOnly"
 	 class="org.openspaces.core.config.modifiers.WriteModifierFactoryBean">
 	 <property name="modifierName" value="UPDATE_ONLY" />
 	 </bean>
 	 <bean id="partialUpdate"
 	 class="org.openspaces.core.config.modifiers.WriteModifierFactoryBean">
 	 <property name="modifierName" value="PARTIAL_UPDATE" />
 	 </bean>
 	 </array>
 	 </property>
</bean>
```
{{% /tab %}}
{{% /tabs %}}



## Examples

Here are some examples on how to configure the Space Bean and the proxy:

Declaring a remote space with a transaction manager:


```xml
<tx:annotation-driven transaction-manager="transactionManager"/>

<os-core:space-proxy id="space" space-name="space" />
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```



Declaring a remote space with a transaction manager and creating an embedded space:


```xml
<os-core:space-proxy id="spaceRemote" space-name="space" />
<os-core:giga-space id="gigaSpaceRemote" space=" spaceRemote"  tx-manager="transactionManager1"/>

<os-core:space id="spaceEmbed" name="space" />
<os-core:giga-space id="gigaSpaceEmbed" space="spaceEmbed"  tx-manager="transactionManager2"/>
```

{{%refer%}}
[Transaction management](./transaction-management.html)
{{%/refer%}}



Declaring a remote space creating a local view:


```xml
<os-core:space-proxy id="spaceRemote" space-name="space" />
<os-core:local-view id="localViewSpace" space="spaceRemote">
	<os-core:view-query class="com.example.Message1" where="processed = true"/>
</os-core:local-view>
<os-core:giga-space id="gigaSpaceLocalView" space="localViewSpace"/>
```


{{%refer%}}
[Local View](./local-view.html)
{{%/refer%}}


Declaring a remote space with a local view , a regular remote space (without a view) and an embedded space:


```xml
<os-core:space-proxy id="spaceRemote" space-name="space" />
	<os-core:local-view id="localViewSpace" space="spaceRemote">
	<os-core:view-query class="com.example.Message1" where="processed = true"/>
</os-core:local-view>

<os-core:giga-space id="gigaSpaceLocalView" space="localViewSpace"/>
<os-core:giga-space id="gigaSpaceRemote" space="spaceRemote"  tx-manager="transactionManager1"/>

<os-core:space id="spaceEmbed" name="space" />
<os-core:giga-space id="gigaSpaceEmbed" space="spaceEmbed"  tx-manager="transactionManager2"/>
```

{{% note %}}
The application is always injected with `os-core:giga-space` bean that wraps always a `os-core:space`.
{{% /note %}}



# Default Operation Modifiers

You may configure default modifiers for the different operations in the `GigaSpace` interface. The default modifiers can be configured in the following manner:

{{%tabs%}}
{{%tab "Namespace"%}}

```xml
<os-core:embedded-space id="space" space-name="space" />
<os-core:giga-space id="gigaSpace" space="space">
  <os-core:read-modifier value="FIFO"/>
  <os-core:change-modifier value="RETURN_DETAILED_RESULTS"/>
  <os-core:clear-modifier value="EVICT_ONLY"/>
  <os-core:count-modifier value="READ_COMMITTED"/>
  <os-core:take-modifier value="FIFO"/>

  <!-- to add more than one modifier, simply include all desired modifiers -->
  <os-core:write-modifier value="PARTIAL_UPDATE"/>
  <os-core:write-modifier value="UPDATE_ONLY"/>
</<os-core:giga-space>
```

{{% /tab %}}
{{%tab "Plain XML"%}}
```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
  <property name="name" value="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
  <property name="space" ref="space" />
  <property name="defaultWriteModifiers">
    <array>
      <bean id="updateOnly"
        class="org.openspaces.core.config.modifiers.WriteModifierFactoryBean">
        <property name="modifierName" value="UPDATE_ONLY" />
      </bean>
      <bean id="partialUpdate"
        class="org.openspaces.core.config.modifiers.WriteModifierFactoryBean">
        <property name="modifierName" value="PARTIAL_UPDATE" />
      </bean>
    </array>
  </property>
</bean>
```

{{% /tab %}}
{{%tab "Code"%}}
```java
  EmbeddedSpaceConfigurer spaceConfigurer = new EmbeddedSpaceConfigurer("space");

  GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer)
  .defaultWriteModifiers(WriteModifiers.PARTIAL_UPDATE.add(WriteModifiers.UPDATE_ONLY))
  .defaultReadModifiers(ReadModifiers.FIFO)
  .defaultChangeModifiers(ChangeModifiers.RETURN_DETAILED_RESULTS)
  .defaultClearModifiers(ClearModifiers.EVICT_ONLY)
  .defaultCountModifiers(CountModifiers.READ_COMMITTED)
  .defaultTakeModifiers(TakeModifiers.FIFO)
  .gigaSpace();
```
{{% /tab %}}
{{% /tabs %}}

Any operation on the configured proxy will be treated as if the default modifiers were explicitly passed. If a certain operation requires passing an explicit modifier and also wishes to merge the existing default modifiers, the following  pattern should be used:


```java
GigaSpace gigaSpace = ...
gigaSpace.write(someObject, gigaSpace.getDefaultWriteModifiers().add(WriteModifiers.WRITE_ONLY));
```

For further details on each of the available modifiers see:

- [ReadModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ReadModifiers.html)
- [WriteModifiers]({{% api-javadoc %}}/com/gigaspaces/client/WriteModifiers.html)
- [TakeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/TakeModifiers.html)
- [CountModifiers]({{% api-javadoc %}}/com/gigaspaces/client/CountModifiers.html)
- [ClearModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ClearModifiers.html)
- [ChangeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ChangeModifiers.html)

# Exception Hierarchy

XAP is built on top of the Spring {{%exurl "consistent exception hierarchy" "http://static.springframework.org/spring/docs/2.0.x/reference/dao.html#dao-exceptions"%}} by translating all of the different JavaSpaces exceptions and GigaSpaces exceptions into runtime exceptions, consistent with the Spring exception hierarchy. All the different exceptions exist in the `org.openspaces.core` package.

XAP provides a pluggable exception translator using the following interface:


```java
public interface ExceptionTranslator {

    DataAccessException translate(Throwable e);
}
```

A default implementation of the exception translator is automatically used, which translates most of the relevant exceptions into either Spring data access exceptions, or concrete OpenSpaces runtime exceptions (in the `org.openspaces.core` package).

## Exception handling for Batch Operations

Batch operations many throw the following Exceptions. Make sure you catch these and act appropriately:

- [WriteMultiplePartialFailureException]({{% api-javadoc %}}/index.html?org/openspaces/core/WriteMultiplePartialFailureException.html)
- [WriteMultipleException]({{% api-javadoc %}}/index.html?org/openspaces/core/WriteMultipleException.html)
- [ReadMultipleException]({{% api-javadoc %}}/index.html?org/openspaces/core/ReadMultipleException.html)
- [TakeMultipleException]({{% api-javadoc %}}/index.html?org/openspaces/core/TakeMultipleException.html)
- [ClearException]({{% api-javadoc %}}/index.html?org/openspaces/core/ClearException.html)


# Basic Guidelines

- The variable represents a remote or embedded space proxy (for a single space or clustered) and **should be constructed only** once throughout the lifetime of the application process.
- You should treat the variable as a singleton to be shared across multiple different threads within your application.
- The interface is thread safe and there is no need to create a GigaSpace variable per application thread.
- In case the space has been fully terminated (no backup or primary instances running any more) the client space proxy will try to reconnect to the space up to a predefined timeout based on the [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html) settings. If it fails to reconnect, an error will be displayed.
- Within a single Processing Unit (or Spring application context), several GigaSpace instances can be defined, each with different characteristics, all will be interacting with the same remote space.
