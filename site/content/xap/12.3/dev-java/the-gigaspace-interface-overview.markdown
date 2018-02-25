---
type: post123
title:  The Space Interface
categories: XAP123, OSS
weight: 700
parent: none
---


A Space is a logical in-memory service, which can store entries of information. An entry is a domain object; In Java, an entry can be as simple a POJO or a SpaceDocument.

When a client connects to a Space, a proxy is created that holds a connection, which implements the space API. All client interaction is performed through this proxy.

The Space is accessed via a programmatic interface that supports the following main functions:

- Write - the semantics of writing a new entry of information to the Space.
- Read - read the contents of a stored entry into the client side.
- Take - get the value from the Space and delete its content.
- Notify - alert when the contents of an entry of interest have registered changes.

{{%refer%}}
For more information about Spaces, refer to [The Space Operations](./the-space-operations.html).
{{%/refer%}}

# Embedded Space

A client communicating with a an embedded Space performs all its operations via local connection. There is no network overhead when using this approach.

{{%align center%}}
![embedded-space.jpg](/attachment_files/embedded-space.jpg)
{{%/align%}}

The following is an example of how to create an embedded Space. The `EmbeddedSpaceConfigurer` is used to configure the Space URL:

{{%tabs%}}
{{%tab "Code"%}}


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("mySpace")).gigaSpace();
```

{{% /tab %}}

{{%tab "Namespace"%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
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
```
{{% /tab %}}
{{% /tabs %}}

The Embedded space can be used in a distributed architecture such as the replicated or partitioned clustered space:

{{%align center%}}
![replicated-space1.jpg](/attachment_files/replicated-space1.jpg)
{{%/align%}}

A simple way to use the embedded Space in a clustered architecture is by deploying a [clustered Space]({{%currentadmurl%}}/data-grid-clustering.html), or packaging your application as a [Processing Unit](./the-processing-unit-overview.html) and deploying it using the relevant SLA.

# Remote Space

A client communicating with a remote Space performs all its operations via a remote connection. The remote Space can be partitioned (with or without backups) or replicated (based on synchronous or asynchronous replication).

{{% align center %}}
![remote-space.jpg](/attachment_files/remote-space.jpg)
{{% /align %}}

The following is an example of how a client application can create a proxy for interacting with a remote Space:

{{%tabs%}}
{{%tab "  Code "%}}


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("mySpace")).gigaSpace();
```

{{% /tab %}}
{{%tab "  Namespace "%}}


```xml
<os-core:space-proxy  id="space" space-name="mySpace"/>
<os-core:giga-space id="gigaSpace" space="space"/>
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
```

{{% /tab %}}

{{% /tabs %}}

{{%refer%}}
A full description of the Space URL Properties is available [here.](./the-space-configuration.html).
{{%/refer%}}

## Reconnection

When working with a **remote Space**, the Space may become unavailable (due to network problems, Processing Unit relocation, etc.). For information on how such disruptions are handled and configured, refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).

# Local Cache

XAP supports a [local cache](./local-cache.html) (near cache) configuration. This provides a front-end, client-side cache that is used with the `read` operations implicitly. The local cache is loaded on demand or when you perform a `read` operation, and is updated implicitly by the Space.

{{%align center%}}
![local_cache.jpg](/attachment_files/local_cache.jpg)
{{%/align%}}

The following is an example of a `GigaSpace` construct with a local cache:

{{%tabs%}}
{{%tab "  Code "%}}


```java
// Initialize remote space configurer:
SpaceProxyConfigurer urlConfigurer = new SpaceProxyConfigurer("space");
// Initialize local cache configurer
LocalCacheSpaceConfigurer localCacheConfigurer = new LocalCacheSpaceConfigurer(urlConfigurer);

// Create local cache:
GigaSpace localCache = new GigaSpaceConfigurer(localCacheConfigurer).gigaSpace();
```

{{% /tab %}}
{{%tab "    Namespace   "%}}


```xml
<os-core:space-proxy  id="space" space-name="mySpace"/>
<os-core:local-cache id="localCacheSpace" space="space"/>
<os-core:giga-space id="localCache" space="localCacheSpace"/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="localCacheSpace"
    class="org.openspaces.core.space.cache.LocalCacheSpaceFactoryBean">
    <property name="space" ref="space" />
</bean>
```

{{% /tab %}}

{{% /tabs %}}

# Local View

XAP supports a [local view](./local-view.html) configuration. This provides a front-end, client-side cache that is used with any `read` or `readMultiple` operations implicitly. The local view is loaded on start, and is updated implicitly by the Space.

{{% align center %}}
![local_view.jpg](/attachment_files/local_view.jpg)
{{% /align %}}

Here is an example for a `GigaSpace` construct with a local cache:

{{%tabs%}}
{{%tab "  Code "%}}


```java
// Initialize remote space configurer:
SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("space");

// Initialize local view configurer
LocalViewSpaceConfigurer localViewConfigurer = new LocalViewSpaceConfigurer(configurer)
 		    .addViewQuery(new SQLQuery<Message>(Message.class, "processed = true"))
 		    .addViewQuery(new SQLQuery<Message>(Message.class, "priority > 3"));

// Create local view:
GigaSpace localView = new GigaSpaceConfigurer(localViewConfigurer).gigaSpace();
```

{{% /tab %}}
{{%tab "    Namespace   "%}}


```xml
<os-core:space-proxy id="space" space-name="mySpace" />

<os-core:local-view id="localViewSpace" space="space">
    <os-core:view-query class="Message" where="processed = true"/>
    <os-core:view-query class="Message" where="priority > 3"/>
</os-core:local-view>

<os-core:giga-space id="localView" space="localViewSpace"/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="viewSpace" class="org.openspaces.core.space.cache.LocalViewSpaceFactoryBean">
    <property name="space" ref="space" />
    <property name="localViews">
        <list>
            <bean class="com.j_spaces.core.client.view.View">
                <constructor-arg index="0" value="com.example.Message1" />
                <constructor-arg index="1" value="processed = true" />
            </bean>
            <bean class="com.j_spaces.core.client.view.View">
                <constructor-arg index="0" value="com.example.Message2" />
                <constructor-arg index="1" value="priority > 3" />
            </bean>
        </list>
    </property>
</bean>
```
{{% /tab %}}
{{% /tabs %}}

# Resource Cleanup

There are two types of resources associated with Space instances and Space clients that have to be released before shutting down your application.

## Thread and Memory Resources

If your Space client or embedded Space are running within a Spring-enabled environment (such as the XAP service grid or a standalone Spring application), and are configured in a Spring application context, these resources will be cleaned up automatically when the Spring application context is destroyed. However, if you start the Space client or Space instance programmatically, you must call the `UrlSpaceConfigurer destroy()` method when your application no longer uses the Space instance/Space client.

Example:


```java
SpaceProxyConfigurer urlConfigurer = new SpaceProxyConfigurer("space");
 //....
urlConfigurer.destroy();

// Local cache
LocalCacheSpaceConfigurer localCacheConfigurer = new LocalCacheSpaceConfigurer(urlConfigurer);
localCacheConfigurer.destroy();

// Local view
LocalViewSpaceConfigurer localViewConfigurer = new LocalViewSpaceConfigurer(urlConfigurer);
localViewConfigurer.destroy();
```

{{%note "Local View and Cache"%}}
When using LocalCache and LocalView. you need to call the `destroy()` method on their respective configurer.
{{%/note%}}

## Communication Resources

All communication-related resources in XAP are shared between all the XAP components at the Java classloader level. If you're using the [XAP service grid]({{%currentadmurl%}}/the-runtime-environment.html) to run your XAP application, you do not need to handle the cleanup of communication resources explicitly.

If your application runs in a standalone environment or another hosted environment (such as a JEE application server), you must explicitly clean up those resources. You have to shut down these resources explicitly when your application no longer uses the XAP components (for example, when it is undeployed from the application server). This is done by calling the static [shutdown()]({{% api-javadoc %}}/com/gigaspaces/lrmi/LRMIManager.html) method on the LRMIManager.

If the JVM process is shut down anyway, you don't have to explicitly shut down the communication resources.

Example:


```java
SpaceProxyConfigurer urlConfigurer = new SpaceProxyConfigurer("space");
 //....
urlConfigurer.destroy();

LRMIManager.shutdown();
```

# Security

A secured Space should be configured with a security context so that it can be accessed (when connecting to it remotely). The following is an example of how this can be configured:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:space-proxy id="space" space-name="mySpace">
    <os-core:security username="sa" password="adaw@##$" />
</os-core:space-proxy>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.SpaceProxyFactoryBean">
    <property name="name" value="space" />
    <property name="securityConfig">
        <bean class="org.openspaces.core.space.SecurityConfig">
            <property name="username" value="sa" />
            <property name="password" value="adaw@##$" />
        </bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The following is an example of how to define security with an embedded Space. In this case, we enable security and specify the username and password.

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:space-proxy  id="space" space-name="mySpace">
    <os-core:security username="sa" password="adaw@##$" />
</os-core:space-proxy>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="securityConfig">
        <bean class="org.openspaces.core.space.SecurityConfig">
            <property name="username" value="sa" />
            <property name="password" value="adaw@##$" />
        </bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

You can configure the Space to be secured using deploy-time properties (bean-level properties), without declaring the security element. The `security.username` and `security.password` can be provided, and the Spaces defined within the Processing Unit are automatically secured.

{{%refer%}}
For more information about how to secure XAP components, refer to the [Security](../security/) section of the Administrator guide.
{{%/refer%}}

# Persistency

When constructing a Space, you can provide [Space Persistency](./space-persistency-overview.html) extensions using Spring-based configuration instead of using the Space schema. The following is an example of how it can be defined:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
    <property name="url" value="jdbc:hsqldb:hsql://localhost:9001"/>
    <property name="username" value="sa"/>
    <property name="password" value=""/>
</bean>

<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mappingResources">
        <list>
            <value>Person.hbm.xml</value>
        </list>
    </property>
    <property name="hibernateProperties">
        <props>
            <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
            <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
            <prop key="hibernate.cache.use_second_level_cache">false</prop>
            <prop key="hibernate.cache.use_query_cache">false</prop>
            <prop key="hibernate.hbm2ddl.auto">update</prop>
        </props>
    </property>
</bean>

<bean id="hibernateSpaceDataSource" class="com.gigaspaces.datasource.hibernate.DefaultHibernateSpaceDataSource">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>

<os-core:embedded-space id="space" space-name="mySpace" schema="persistent" space-data-source="hibernateSpaceDataSource" />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
    <property name="url" value="jdbc:hsqldb:hsql://localhost:9001"/>
    <property name="username" value="sa"/>
    <property name="password" value=""/>
</bean>

<bean id="sessionFactory" class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mappingResources">
        <list>
            <value>Person.hbm.xml</value>
        </list>
    </property>
    <property name="hibernateProperties">

        <props>
            <prop key="hibernate.dialect">org.hibernate.dialect.HSQLDialect</prop>
            <prop key="hibernate.cache.provider_class">org.hibernate.cache.NoCacheProvider</prop>
            <prop key="hibernate.cache.use_second_level_cache">false</prop>
            <prop key="hibernate.cache.use_query_cache">false</prop>
            <prop key="hibernate.hbm2ddl.auto">update</prop>
        </props>
    </property>
</bean>

<bean id="hibernateSpaceDataSource" class="com.gigaspaces.datasource.hibernate.DefaultHibernateSpaceDataSource">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="schema" value="persistent" />
    <property name="spaceDataSource" ref="hibernateSpaceDataSource" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The above example uses Spring built-in support for configuring both a custom JDBC `DataSource` and a Hibernate `SessionFactory` to define and use the XAP built-in `HibernateSpaceDataSource`. The XAP data source is then injected into the 
Space construction (note the specific schema change), and causes the Space to use it.

{{% note "Note" %}}
This configuration can also be used with the XAP [Mirror Service](./asynchronous-persistency-with-the-mirror.html) deployed as a Processing Unit.
{{%/note%}}

# Additional Resources


{{%section%}}
{{%column width="30%"  %}}
{{%youtube "k2zGdUi_ntU"  "Custom Change API"%}}
{{%/column%}}

{{%column width="30%"  %}}
{{%youtube "H22qPux8Fh8"  "Aggregations"%}}
{{%/column%}}

{{%column width="30%"  %}}
{{%/column%}}
{{%/section%}}




