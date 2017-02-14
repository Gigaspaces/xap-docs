---
type: post121
title:  Overview
categories: XAP121
weight: 100
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}}{{%/ssummary%}}


A Space is a logical in-memory service, which can store entries of information. An entry is a domain object; In Java, an entry can be as simple a POJO or a SpaceDocument.

When a client connects to a space, a proxy is created that holds a connection which implements the space API. All client interaction is performed through this proxy.

The space is accessed via a programmatic interface which supports the following main functions:

- Write - the semantics of writing a new entry of information into the space.
- Read - read the contents of a stored entry into the client side.
- Take - get the value from the space and delete its content.
- Notify - alert when the contents of an entry of interest have registered changes.


{{%refer%}}
[The Space Operations](./the-space-operations.html)
{{%/refer%}}



# Embedded Space


A client communicating with a an embedded space performs all its operation via local connection. There is no network overhead when using this approach.

{{%align center%}}
![embedded-space.jpg](/attachment_files/embedded-space.jpg)
{{%/align%}}

Here is an example how to create an embedded space. The `EmbeddedSpaceConfigurer` is used to configure the space url:

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

A simple way to use the embedded space in a clustered architecture would be by deploying a [clustered space]({{%currentadmurl%}}/data-grid-clustering.html) or packaging your application as a [Processing Unit](./the-processing-unit-overview.html) and deploy it using the relevant SLA.



# Remote Space

A client communicating with a remote space performs all its operation via a remote connection. The remote space can be partitioned (with or without backups) or replicated (sync or async replication based).

{{% align center %}}
![remote-space.jpg](/attachment_files/remote-space.jpg)
{{% /align %}}

Here is an example how a client application can create a proxy to interacting with a remote space:

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
A full description of the Space URL Properties can be found [here.](./the-space-configuration.html)
{{%/refer%}}


## Reconnection
When working with a **remote space**, the space may become unavailable (network problems, processing unit relocation, etc.). For information on how such disruptions are handled and configured refer to [Proxy Connectivity]({{%currentadmurl%}}/tuning-proxy-connectivity.html).



# Local Cache

XAP supports a [Local Cache](./local-cache.html) (near cache) configuration. This provides a front-end client side cache that will be used with the `read` operations implicitly . The local cache will be loaded on demand or when you perform a `read` operation and will be updated implicitly by the space.

{{%align center%}}
![local_cache.jpg](/attachment_files/local_cache.jpg)
{{%/align%}}

Here is an example for a `GigaSpace` construct with a local cache:

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



{{%refer%}}
[Local Cache](./local-cache.html)
{{%/refer%}}



# Local View

XAP supports a [Local View](./local-view.html) configuration. This provides a front-end client side cache that will be used with any `read` or `readMultiple` operations implicitly. The local view will be loaded on start and will be updated implicitly by the space.

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


{{%refer%}}
[Local View](./local-view.html)
{{%/refer%}}



# Resource cleanup

There are two types of resources associated with Space instances and Space clients that need to be freed up before shutting down your application.

### Thread and memory resources
If your space client or embedded space are running within a Spring-enabled environment (e.g. the XAP service grid or a standalone Spring application), and are configured in a Spring application context, these resources will be cleaned up automatically when the Spring application context is destroyed. <br/>
However, if you start the space client or space instance programmatically, you must call the `UrlSpaceConfigurer destroy()` method when your application no longer uses the space instance / space client.

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
When using LocalCache and LocalView you need to call the `destroy()` method on their respective configurer.
{{%/note%}}


### Communication resources
All communication related resources in XAP are shared between all the XAP components
at the Java classloader level. If you're using the [GigasSpaces service grid]({{%currentadmurl%}}/the-runtime-environment.html) to run your XAP application you do not need to handle communication resources cleanup explicitly.
If your application runs in a standalone environment or another hosted environment (e.g. a JEE application server) you will need to explicitly clean up those resources.
You need to shutdown these resources explicitly when your application no longer uses the XAP components (e.g. when it's un deployed from the application server). This is done by calling the static [shutdown()]({{% api-javadoc %}}/com/gigaspaces/lrmi/LRMIManager.html) method on the LRMIManager.
Note that if the JVM process is shut down anyway, you do not need to do explicitly shut down the communication resources.


Example:


```java
SpaceProxyConfigurer urlConfigurer = new SpaceProxyConfigurer("space");
 //....
urlConfigurer.destroy();

LRMIManager.shutdown();
```


# Security

A secured space should be configured with a security context so that it can be accessed (when connecting to it remotely). Here is an example of how this can be configured:

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

Here is an example of how to define security with an embedded space. In this case, we enable security and specify the username and password.

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

It is possible to configure the space to be secured using deploy time properties (bean level properties), without declaring the security element. The `security.username` and `security.password` can be provided, and the spaces defined within the processing unit are automatically secured.


{{%refer%}}
[Security]({{%currentsecurl%}}/)
{{%/refer%}}


# Persistency

When constructing a space, it is possible to provide [Space Persistency](./space-persistency.html) extensions using Spring-based configuration (instead of using the space schema). Here is an example of how it can be defined:

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

The above example uses Spring built-in support for configuring both a custom JDBC `DataSource` and a Hibernate `SessionFactory` to define and use the GigaSpaces built-in `HibernateSpaceDataSource`. The GigaSpaces data source is then injected into the space construction (note the specific schema change), and causes the space to use it.



{{% refer %}}
This configuration can also be used with the XAP [Mirror Service](./asynchronous-persistency-with-the-mirror.html) deployed as a Processing Unit.
{{%/refer%}}


