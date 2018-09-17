---
type: post100
title:  Space Filters
categories: XAP100
weight: 800
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}} {{% /ssummary %}}

Space Filters are interceptors inside the XAP Space engine that enable integration with external systems and/or implementation of user-defined logic based once space operations are executed.


![Space Filter.JPG](/attachment_files/Space Filter.JPG)


# Space Filters

The `UrlSpaceFactoryBean` allows you to configure [Space Filters](./the-space-filters-metadata.html). It uses the space support for a `FilterProvider`, which is a wrapper for an `ISpaceFilter` implementation and its characteristics (such as priority, `activeWhenBackup`). This allows you to provide space filters without changing the space schema.

{{% note %}}
Space Filters can only be used with embedded spaces.
{{%/note%}}

## ISpaceFilter

An actual implementation of the `ISpaceFilter` interface can be provided using the `SpaceFilterProviderFactory` class. Here is a very simple example of an `ISpaceFilter` implementation:


```java
public class SimpleFilter implements ISpaceFilter {

    public void init(IJSpace space, String filterId, String url, int priority)
                throws RuntimeException {
        // perform operations on init
    }

    public void process(SpaceContext context, ISpaceFilterEntry entry, int operationCode)
                throws RuntimeException {
        // process single entry filter operations
    }

    public void process(SpaceContext context, ISpaceFilterEntry[] entries, int operationCode)
                throws RuntimeException {
        // process multiple entries filter operation (such as update)
    }

    public void close() throws RuntimeException {
        // perform operation when filter closes
    }
}
```

The following Spring configuration registers this filter for before write (`0`), before read (`2`), and before take (`3`) operations:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<bean id="simpleFilter" class="eg.SimpleFilter" />

<os-core:embedded-space id="space" name="mySpace">
    <os-core:space-filter priority="2">
        <os-core:filter ref="simpleFilter" />
        <os-core:operation code="0" />
        <os-core:operation code="2" />
        <os-core:operation code="3" />
    </os-core:space-filter>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="simpleFilter" class="eg.SimpleFilter" />

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="filterProviders">
        <list>
            <bean class="org.openspaces.core.space.filter.SpaceFilterProviderFactory">
                <property name="filter" ref="simpleFilter" />
                <property name="priority" value="2" />
                <property name="operationCodes">
                    <list>
                        <value>0</value>
                        <value>2</value>
                        <value>3</value>
                    </list>
                </property>
            </bean>
        </list>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Delegate Filters

OpenSpaces comes with delegate implementations of `ISpaceFilter`, allowing you to use either annotations or explicit method listings in order to use POJOs as space filters.

Here is an example of a simple POJO filter using annotations:


```java
public class SimpleFilter {

    @OnFilterInit
    void init() {
    }

    @OnFilterClose
    void close() {
    }

    @BeforeWrite
    public void beforeWrite(Message entry) {
        // ...
    }

    @AfterWrite
    public void afterWrite(Echo echo) {
        // ...
    }

    @BeforeRead
    public void beforeRead(ISpaceFilterEntry entry) {
        // ...
    }

    @BeforeTake
    public void beforeTake(Message entry, int operationCode) {
        // ...
    }

    @AfterRead
    public void afterRead(Echo echo) {
        // ...
    }

    // called for each matching object
    @AfterReadMultiple
    public void afterReadMultiple(Echo echo) {
        // ...
    }

    // called for each matching object
    @AfterTakeMultiple
    public void afterTakeMultiple(Echo echo) {
        // ...
    }
}
```

This example (which also applies to explicit method listings, just without the annotations) demonstrates different options to mark methods as filter operation callbacks or filter lifecycle callbacks.

First, note the `beforeRead(ISpaceFilterEntry entry)` method (the method name can be any name of your choice). The method accepts the same `ISpaceFilterEntry` that the `ISpaceFilter` process method uses (which is usually used for extracting the actual template or Entry). With the `beforeWrite(Message entry)` method, the delegate automatically detects that the first parameter is not an `ISpaceFilterEntry`, and uses it to extract the actual Entry, which is used to invoke the method with (in our case) `Message`. When using Entry-type classes in the filter callback, other types that are not assignable to the Entry parameter type, do not cause the filter method callback to be invoked. (In our case, `beforeWrite` is not invoked for the echo object.)

{{% note %}}
When either annotations or explicit method listings are used, only a single method per operation can be defined.
{{%/note%}}

The delegate filter shown above, can be configured in Spring using the following XML:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<bean id="simpleFilter" class="test.SimpleFilter" />

<os-core:embedded-space id="space" name="mySpace">
    <os-core:annotation-adapter-filter priority="2">

        <os-core:filter ref="simpleFilter" />
    </os-core:annotation-adapter-filter>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="simpleFilter" class="test.SimpleFilter" />

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="filterProviders">
    	<bean class="org.openspaces.core.space.filter.AnnotationFilterFactoryBean">
    	    <property name="filter" ref="simpleFilter" />
    	    <property name="priority" value="2" />
    	</bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

The following Spring configuration XML shows how the filter can be configured, using explicit method listings. (In this case, annotations are not required.)

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<bean id="simpleFilter" class="test.SimpleFilter" />

<os-core:embedded-space id="space" name="spaceAdapterSimpleFilterMethod">
    <os-core:method-adapter-filter priority="2"
                                   filter-init="init" filter-close="close"
                                   before-write="beforeWrite" after-write="afterWrite"
                                   before-read="beforeRead" before-take="beforeTake">
        <os-core:filter ref="simpleFilter"/>
    </os-core:method-adapter-filter>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="simpleFilter" class="test.SimpleFilter" />

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="filterProviders">
    	<bean class="org.openspaces.core.space.filter.MethodFilterFactoryBean">
    	    <property name="filter" ref="simpleFilter" />
    	    <proeprty name="priority" value="2" />
    	    <property name="filterInit" value="init" />
    	    <property name="filterClose" value="close" />
    	    <property name="beforeWrite" value="beforeWrite" />
    	    <property name="afterWrite" value="afterWrite" />
    	    <property name="beforeRead" value="beforeRead" />
    	    <property name="beforeTake" value="beforeTake" />
    	</bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}

# Accessing a Space within a Space Filter

Accessing a space within a space filter can cause a cycle construction exception, since the space can not be injected to the filter (because the space was not constructed yet). There are options to solve this with pure Spring, but OpenSpaces provides a simpler option by using the [GigaSpacesLateContext](./pojo-grid-annotations.html) annotation.

# Space Replication Filters

The `UrlSpaceFactoryBean` allows you to configure [Cluster Replication Filters]({{%currentadmurl%}}/cluster-replication-filters.html). It uses the space support for a `ReplicationFilterProvider` which is a wrapper for an `IReplicationFilter` implementation and its characteristics (such as `activeWhenBackup`). This allows you to provide space replication filters without changing the space schema.

{{% note %}}
Space replication filters can only be used with embedded spaces.
{{%/note%}}

A simple implementation of `IReplicationFilter` is shown below:


```java
public class SimpleReplicationFilter implements IReplicationFilter {

    public void init(IJSpace space, String paramUrl, ReplicationPolicy replicationPolicy) {
        // init logic here
    }

    public void process(int direction, IReplicationFilterEntry replicationFilterEntry, String remoteSpaceMemberName) {
        // process logic here
    }

    public void close() {
        // close logic here
    }
}
```

The following configuration shows how it can be injected:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<bean id="simpleReplicationFilter" class="eg.SimpleReplicationFilter" />

<os-core:embedded-space id="space" name="mySpace">
    <os-core:space-replication-filter>
        <os-core:input-filter ref="simpleReplicationFilter" />
        <os-core:output-filter ref="simpleReplicationFilter" />
    </os-core:space-replication-filter>
</os-core:embedded-space>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="simpleReplicationFilter" class="eg.SimpleReplicationFilter" />

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="replicationFilterProvider">
        <bean class="org.openspaces.core.space.filter.replication.DefaultReplicationFilterProviderFactory">
            <property name="inputFilter" ref="simpleReplicationFilter" />
            <property name="outputFilter" ref="simpleReplicationFilter" />
        </bean>
    </property>
</bean>
```

{{% /tab %}}
{{% /tabs %}}
