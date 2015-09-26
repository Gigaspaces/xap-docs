---
type: post100
title:  Configuration
categories: XAP100
weight: 200
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}}{{%/ssummary%}}

When a client connects to a space, a proxy is created that holds a connection to the space. All client interaction is performed through this proxy.
The proxy provides a simpler space API using the [GigaSpace](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html) interface.
It is created with a Space URL and optional parameters.


# The Space URL

In order to locate a space you need to specify its URL. The SpaceURL is used as part of the space proxy and is set using the UrlSpaceFactoryBean.

The general format of the space URL is:


```xml
<protocol>://<lookup service hostname>:<port>/<space container name>/<space name>?<properties>
```

The following parameters can be defined:


| Name | Description |
|:-----|:------------|
| <nobr>Lookup Service Host name/IP</nobr> | The machine host name/IP running the lookup service. May be \* when Jini is used as a protocol. In this case the space is located using multicast or unicast with search path. |
| Port | The Jini lookup port. If no port is specified the default port (10098) will be used |
| Space Container Name | The name of the space container that holds the space. May be \* when Jini is used as a protocol. In this case the container name will be ignored when performing lookup and the space will be searched regardless of the container that holds it. |
| Space Name | The space name to search. The same name defined when space has been created via the Space browser or the `createSpace` utility. |
| [Properties String](#url properties) | (Optional) named value list of special properties. |

{{% refer %}}
Make sure your network and machines running GigaSpaces are configured to have multicast enabled. See the [How to Configure Multicast]({{%currentadmurl%}}/network-multicast.html) section for details on how to enable multicast.
{{%/refer%}}

### Examples

**Accessing Remote Space Using Jini Lookup Service - Unicast Discovery**

```bash
jini://LookupServiceHostname/*/mySpace
```


**Accessing Remote Space Using the Jini Lookup Service - Multicast Discovery**

```bash
jini://*/*/mySpace
```

**Starting Embedded Space Using the Java Protocol**

```bash
/./mySpace (which translates to java://localhost:10098/containerName/mySpace?schema=default)
/./mySpace?schema=cache (which translates to java://localhost:10098/containerName/mySpace?schema=cache)
java://LookupServiceHostName:port/myContainerName/spaceName
```


**Distributed Unicast-Based Lookup Service Support**{{%wbr%}}
In environments that do not support multicast, you can use the `locators` space URL property to instruct the started space or a client to locate the Jini Lookup Service on specific host names and ports.The locators can have a comma-delimited lookup hosts list.

The following URL formats are supported:{{%wbr%}}


```bash
jini://*/*/space_name?locators=h1:port,h2:port,h3:port
jini://LookupServiceHostName1:port1,....LookupServiceHostName n:port n/*/space_name
jini://LookupServiceHostName1:port1,....LookupServiceHostName n:port n/*/space_name?locators=LookupServiceHostName1:port,LookupServiceHostName2:port,LookupServiceHostName3:port
jini://LookupServiceHostName1:port1/*/space name?locators=LookupServiceHostName1:port,LookupServiceHostName2:port,LookupServiceHostName3:port
```


###  Space Container Notation

The Space URL uses the following notation to start a space: `/./<Space Name>`. For example: `/./mySpace`

When using that space URL the system will instantiate (create) a space instance named `mySpace` using the default schema configuration. The default schema is set to transient space configuration and it is equivalent to using the following URL:

```bash
java://localhost:10098/mySpace_container/mySpace?schema=default
```

{{% tip %}}
You can use "." as the container name in the space URL. A value of "." as the container name will be translated to `<space name>_container` name. In the above example the container name is explicitly defined as `mySpace_container`.
{{% /tip %}}

When a URL is provided without the protocol (java) and host name (localhost), the following URL is created /./mySpace as:

```bash
java://localhost:10098/mySpace_container/mySpace?schema=default
```

{{%anchor url properties%}}

# URL Properties

The following are optional property string values:


|Property String | Description | Optional values |
|:--------------|:----------------|:------------|
|`create` | Creates a new space using the container's default parameters. New spaces use the default space configuration file. Example: `java://localhost:10098/containerName`{{% wbr %}}`/mySpaces?create=true` | |
|`fifo` | Indicates that all take/write operations be conducted in FIFO mode. Default is false. Example: `jini://localhost:10098/containerName`{{% wbr %}}`/mySpaces?fifo=true` | `false` |
|`groups` | The Jini Lookup Service group to find container or space using multicast. Example: `jini://*/containerName/spaceName?groups=grid`{{% wbr %}}{{% infosign %}} The default value of the `LOOKUPGROUPS` variable is the GigaSpaces version number, preceded by `XAP`. For example, in GigaSpaces XAP 6.0 the default lookup group is `XAP6.0`. This is the lookup group which the space and Jini Transaction Manager register with, and which clients use by default to connect to the space.{{% wbr %}}{{% exclamation %}} Jini groups are irrelevant when using unicast lookup discovery -- they are relevant only when using multicast lookup discovery. If you have multiple spaces with the same name and you are using unicast lookup discovery, you might end up getting the wrong proxy. In such a case, make sure you have a different lookup for each space, where each space is configured to use a specific lookup. A good practice is to have different space names. | `Group name` |
|`locators` | Instructs the started space or a client to locate the Jini Lookup Service on specific host name and port. For more details please refer to [How to Configure Unicast Discovery]({{%currentadmurl%}}/network-unicast-discovery.html#HowtoConfigureUnicastDiscovery-Configuringthelookuplocatorsproperty) page. | |
|`updateMode` | Push or pull update mode. Example: {{%wbr%}}`jini://localhost:10098/containerName /mySpaces?useLocalCache&updateMode=1` | `UPDATE_`{{% wbr %}} `MODE`{{% wbr %}} `_PULL`{{% wbr %}} `= 1` {{% wbr %}} `UPDATE_`{{% wbr %}} `MODE`{{% wbr %}} `_PUSH`{{% wbr %}} `= 2` |
|`security`{{% wbr %}} `Manager` | When false, `SpaceFinder` will not initialize RMISecurityManager. Default is `true`. Example: `jini://localhost:10098/containerName`{{% wbr %}} `/mySpaces?securityManager=false` | |
|`useLocalCache` | Turn Master-Local Space mode.By default Master-Local mode is turned off. To enable master local have the `useLocalCache` as part of the URL |  |
|`versioned` | When false, optimistic lock is disabled. In a local cache and views the default is `true`, otherwise the default value is `false`. Example: `jini://localhost:10098/containerName/mySpaces?versioned=false` | |
|`clustername` | The cluster name to lookup using multicast. The returned object is a clustered proxy. | |
|`clustergroup` | The cluster group to lookup using multicast. The returned object is a clustered proxy. | |
|`cluster_schema` | The cluster schema XSL file name to be used to setup a cluster config on the fly in memory. If the `?cluster_schema option` is passed e.g. `?cluster_schema=sync_replication`, the system will use the `sync_replication-cluster-schema.xsl` together with a cluster Dom which will be built using user's inputs on regards # of members, # of backup members etc. | |
|`schema` | Using the schema flag, the requested space schema name will be loaded/parsed while creating an embedded space. If the space already has configuration file then the requested schema will not be applied and the that file exist, it will overwrite the default configuration defined by the schema. Note that when using the option ?create with java:// protocol, the system will create a container, space and use the default space configuration schema file (default-space-schema.xml) | |
|`total_members` | The `total_members` attribute in the space URL stands for the total number of cache members within the cache cluster.{{% wbr %}}The number is used to create the list of members participating in the cluster on the fly based on the cache name convention. This pattern is used to avoid the need for creating a cluster topology file. {{% wbr %}}The number of actual running cache instances can vary dynamically between `1<=total_members`.{{% wbr %}}The format of the `total_members` = number of primary instances, number of backup instances per primary. In this example the value is 4,2 which means that this cluster contains up to 4 primary instances each containing 2 backup instances. The backup_id is used to define whether the instance is a backup instance or not.{{% wbr %}}If this attribute is not defined the instance will be considered a primary instance. The container name will be translated in this case to \[cache name\]\_container\[id\]\[backup_id\].{{% wbr %}}In this case it will be expanded to mySpace_container1\_1 | |
|`backup_id` | Used in case of Partitioned Cache (when adding backup to each partition). The backup_id is used to define whether the instance is a backup instance or not. If this attribute is not defined the instance will be considered a primary cache instance.{{% wbr %}}The container name will be translated in this case to \[cache name\]_container\[id\]_\[backup_id\].{{% wbr %}} In this case it will be expanded to mySpace_container1_1. | |
|`id` | The id attribute is used to distinguish between cache instances in this cluster. | |
|`properties` | if properties property is used as part of the URL space, space and container schema will be loaded and the properties listed as part of the properties file (`[prop-file-name].properties`) which contains the values to override the schema space/container/cluster configuration values that are defined in the schema files.{{% wbr %}}Another benefit of using the ?properties option is when we want to load system properties while VM starts or set SpaceURL attributes. See /config/gs.properties file as a reference. | |
|`mirror` | When setting this URL property it will allow the space to connect to the Mirror service to push its data and operations for asynchronous persistency.{{% wbr %}}Example:{{% wbr %}}`/./mySpace?cluster_schema=sync_replicated&mirror`{{% wbr %}} Default: no mirror connection | |

Example for space URL using options:


```java
jini://*/*/mySpace?useLocalCache&versioned=false

/./mySpace?cluster_schema=partitioned&total_members=4&id=1
```


The `UrlSpaceFactoryBean` allows you to set different URL properties, either explicitly using explicit properties, or using a custom `Properties` object. All of the current URL properties are exposed using explicit properties, in order to simplify the configuration.

Here is an example of a space working in FIFO mode, using specific lookup groups:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:embedded-space id="space" name="space" lookup-groups="test" lookup-timeout="10000"  lookup-locators="myHost" versioned="true" />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="versioned" value="true" />
    <property name="lookupGroups" value="test" />
    <property name="lookupTimeout" value="20000" />
    
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

   // Create the url
   UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer("/./space").lookupGroups("test").lookupTimeout(20000);

   // Create the url with arguments
   UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer("/./space?groups=test&timeout=20000");

   // Create the url with properties
   UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer("/./space")
       .addProperty("fifo","true")
       .addProperty("lookupGroups","test");
   // .....
   // Create the Proxy
   GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();

   // shutting down / closing the Space
   spaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

### Overriding Default Configuration Using General Properties

The space allows you to override specific schema configuration element values using the `Properties` object, that uses an XPath-like navigation as the name value. The `UrlSpaceFactoryBean` allows you to set the `Properties` object, specifying it within the Spring configuration.

{{% tip title="Which component's configuration can be overridden? "%}}
The general properties are used to override various components such as the space, space container, cluster schema properties, space proxy/client configuration, space URL attributes and other system and environmental properties.
{{% /tip %}}

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" name="space">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.cache_policy">0</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="properties">
        <props>
            <prop key="space-config.engine.cache_policy">0</prop>
        </props>
    </property>
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

   UrlSpaceConfigurer spaceConfigurer =
    new UrlSpaceConfigurer("/./space").addProperty("space-config.engine.cache_policy", "0");

   // Create the proxy
   GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
   .......
   // shutting down / closing the Space
   spaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

Popular overrides:

- [Memory Manager Settings]({{%currentadmurl%}}/memory-management-facilities.html#Memory Manager Parameters)
- [Replication Settings]({{%currentadmurl%}}/replication-parameters.html)
- [Replication Redo-log Settings]({{%currentadmurl%}}/controlling-the-replication-redo-log.html#Redo Log Capacity Configuration)
- [Proxy Connectivity Settings]({{%currentadmurl%}}/tuning-proxy-connectivity.html#Configuration)
- [Persistency Settings](./space-persistency-advanced-topics.html#Properties)


{{%anchor proxy%}}

# The Space Proxy - The GigaSpace Bean

The `GigaSpace` Spring Bean provides a simple way to configure a proxy to be injected into the relevant Bean.

Here is an example on how to create the proxy:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:embedded-space id="space" name="space" />
  </os-core:giga-space id="mySpace" space="space" />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<os-core:embedded-space id="space" name="space">
<bean id="mySpace" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

   // Create the URL
   UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer("/./mySpace");

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


# Proxy Properties

The `GigaSpace` Bean can have the following elements:


|Element|Description|Required|Default Value|
|:------|:----------|:-------|:------------|
|space|This can be an embedded space , remote space , local view or local cache. |YES| |
|clustered|Boolean. [Cluster flag]({{%currentadmurl%}}/clustered-vs-non-clustered-proxies.html). Controlling if this GigaSpace will work with a clustered view of the space or directly with a cluster member. By default if this flag is not set it will be set automatically by this factory. It will be set to true if the space is an embedded one AND the space is not a local cache proxy. It will be set to false otherwise (i.e. the space is not an embedded space OR the space is a local cache proxy)| NO | true for remote proxy , false for embedded proxy|
|default-read-timeout|Numerical Value. Sets the default read timeout for `read(Object)` and `readIfExists(Object)` operations.|NO| 0 (NO_WAIT). TimeUnit:millsec|
|<nobr>default-take-timeout</nobr>|Numerical Value. Sets the default take timeout for `take(Object)` and `takeIfExists(Object)` operations.|NO| 0 (NO_WAIT). TimeUnit:millsec|
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
{{%tab "  Namespace "%}}


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



### Examples

Here are some examples on how to configure the Space URL and the proxy:

Declaring a remote space with a transaction manager:


```xml
<tx:annotation-driven transaction-manager="transactionManager"/>

<os-core:space-proxy id="space" name="space" />
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```


Declaring a remote space with a transaction manager and creating an embedded space:


```xml
<os-core:space-proxy id="spaceRemote" name="space" />
<os-core:giga-space id="gigaSpaceRemote" space=" spaceRemote"  tx-manager="transactionManager1"/>

<os-core:space id="spaceEmbed" name="space" />
<os-core:giga-space id="gigaSpaceEmbed" space="spaceEmbed"  tx-manager="transactionManager2"/>
```

Declaring a remote space creating a local view:


```xml
<os-core:space-proxy id="spaceRemote" name="space" />
<os-core:local-view id="localViewSpace" space="spaceRemote">
	<os-core:view-query class="com.example.Message1" where="processed = true"/>
</os-core:local-view>
<os-core:giga-space id="gigaSpaceLocalView" space="localViewSpace"/>
```

Declaring a remote space with a local view , a regular remote space (without a view) and an embedded space:


```xml
<os-core:space-proxy id="spaceRemote" name="space" />
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
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" name="space" />
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
{{%tab "  Plain XML "%}}


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
{{%tab "  Code "%}}


```java

  UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer("/./space");

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

{{%javadoc "- [ReadModifiers]( com/gigaspaces/client/ReadModifiers )"%}}
{{%javadoc "- [WriteModifiers]( com/gigaspaces/client/WriteModifiers)"%}}
{{%javadoc "- [TakeModifiers]( com/gigaspaces/client/TakeModifiers)"%}}
{{%javadoc "- [CountModifiers]( com/gigaspaces/client/CountModifiers)"%}}
{{%javadoc "- [ClearModifiers]( com/gigaspaces/client/ClearModifiers)"%}}
{{%javadoc "- [ChangeModifiers]( com/gigaspaces/client/ChangeModifiers)"%}}

# Exception Hierarchy

OpenSpaces is built on top of the Spring [consistent exception hierarchy](http://static.springframework.org/spring/docs/2.0.x/reference/dao.html#dao-exceptions) by translating all of the different JavaSpaces exceptions and GigaSpaces exceptions into runtime exceptions, consistent with the Spring exception hierarchy. All the different exceptions exist in the `org.openspaces.core` package.

OpenSpaces provides a pluggable exception translator using the following interface:


```java
public interface ExceptionTranslator {

    DataAccessException translate(Throwable e);
}
```

A default implementation of the exception translator is automatically used, which translates most of the relevant exceptions into either Spring data access exceptions, or concrete OpenSpaces runtime exceptions (in the `org.openspaces.core` package).

### Exception handling for Batch Operations

Batch operations many throw the following Exceptions. Make sure you catch these and act appropriately:

- [WriteMultiplePartialFailureException](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/core/WriteMultiplePartialFailureException.html)
- [WriteMultipleException](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/core/WriteMultipleException.html)
- [ReadMultipleException](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/core/ReadMultipleException.html)
- [TakeMultipleException](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/core/TakeMultipleException.html)
- [ClearException](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/core/ClearException.html)


# Basic Guidelines

- The variable represents a remote or embedded space proxy (for a single space or clustered) and **should be constructed only** once throughout the lifetime of the application process.
- You should treat the variable as a singleton to be shared across multiple different threads within your application.
- The interface is thread safe and there is no need to create a GigaSpace variable per application thread.
- In case the space has been fully terminated (no backup or primary instances running any more) the client space proxy will try to reconnect to the space up to a predefined timeout based on the [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html) settings. If it fails to reconnect, an error will be displayed.
- Within a single Processing Unit (or Spring application context), several GigaSpace instances can be defined, each with different characteristics, all will be interacting with the same remote space.
