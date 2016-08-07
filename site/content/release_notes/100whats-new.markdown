---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap100.html
weight: 100
---

This page lists the main new features in XAP 10.0 (Java and .Net editions). It's not an exhaustive list of all new features. For a full change log for 10.0 please refer to the [new features](./100new-features.html) and [fixed issues](./100fixed-issues.html) pages.


{{%panel%}}

- [Interactive Online Tutorial](#0)

- [Native Integration with Solid State Drives](#1)

- [Global HTTP Session Sharing](#2)

- [Query Aggregations](#3)

- [Optimized Initial Data Load](#4)


- [Custom Change Operation](#6)

- [Performance improvement for reg ex query](#7)

- [Jetty 9 support](#8)

- [Web UI Enhancements](#9)

- [Create & configure a Space without a url](#10)

- [Support for Mule 3.5](#11)

- [Compressed Storage Mode in .NET](#12)

- [Externalizable End of Life](#13)



{{%/panel%}}



{{%anchor 0%}}

# Interactive Online Tutorial

This is a tutorial that will setup a XAP Cluster on a real server on demand. The tutorial include an interactive shell that will allow you to execute the various XAP API for writing and reading data as well as open a groovy shell to write your own code and experience the full XAP API.

{{%learn "/xap100/tutorials.html"%}}

{{%anchor 1%}}

# Native Integration with Solid State Drives

XAP 10 introduces a new Storage interface allowing an external storage mechanism (one that does not reside on the JVM heap) to act as storage medium for the data in the IMDG. This storage model allows the IMDG to interact with the storage medium for storing IMDG data.

{{%learn "/xap100adm/blobstore-cache-policy.html"%}}

{{%anchor 2%}}

# Global HTTP Session Sharing

XAP 10 Global HTTP Session Sharing includes the following new features:

- Delta update support - changes identified at the session attribute level.
- Better serialization (Kryo instead of xstream)
- Compression support
- Principle / Session ID based session management. Allows session sharing across different apps with same SSO
- Role based SSO Support
- Better logging

{{%learn "/xap100/global-http-session-sharing.html"%}}


{{%anchor 3%}}

# Query Aggregations

XAP lets you perform aggregations across the Space. There is no need to retrieve the entire data set from the Space to the client side , iterate the result set and perform the aggregation. The Aggregators allow you to perform the entire aggregation activity at the Space side avoiding any data retrieval back to the client side. Only the result of each aggregation activity performed with each partition is returned back to the client side where all the results are reduced and returned to the client application.

Example:

{{%tabs%}}
{{%tab " Java"%}}

```java
import static org.openspaces.extensions.QueryExtension.*;
...
SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,"country=? OR country=? ");
query.setParameter(1, "UK");
query.setParameter(2, "U.S.A");

// retrieve the maximum value stored in the field "age"
Number maxAgeInSpace = max(space, query, "age");
/// retrieve the minimum value stored in the field "age"
Number minAgeInSpace = min(space, query, "age");
// Sum the "age" field on all space objects.
Number combinedAgeInSpace = sum(space, query, "age");
// Sum's the "age" field on all space objects then divides by the number of space objects.
Double averageAge = average(space, query, "age");
// Retrieve the space object with the highest value for the field "age".
Person oldestPersonInSpace = maxEntry(space, query, "age");
/// Retrieve the space object with the lowest value for the field "age".
Person youngestPersonInSpace = minEntry(space, query, "age");
```
{{%/tab%}}
{{%tab " .Net"%}}

```c#
using GigaSpaces.Core.Linq;
...
var queryable = from p in spaceProxy.Query<Person>("Country='UK' OR Country='U.S.A'") select p;
// retrieve the maximum value stored in the field "Age"
int maxAgeInSpace = queryable.Max(p => p.Age);
// retrieve the minimum value stored in the field "Age"
int minAgeInSpace = queryable.Min(p => p.Age);
// Sum the "Age" field on all space objects.
int combinedAgeInSpace = queryable.Sum(p => p.Age);
// Sum's the "Age" field on all space objects then divides by the number of space objects.
double averageAge = queryable.Average(p => p.Age);
// Retrieve the space object with the highest value for the field "Age".
Person oldestPersonInSpace = queryable.MaxEntry(p => p.Age);
// Retrieve the space object with the lowest value for the field "Age".
Person youngestPersonInSpace = queryable.MinEntry(p => p.Age);
```
{{%/tab%}}
{{%/tabs%}}


{{%section%}}
{{%column width="50%" %}}
{{%/column%}}
{{%column width="10%" %}}
{{%learn "/xap100/aggregators.html"%}}
{{%/column%}}
{{%column width="10%" %}}
{{%learn "/xap100net/aggregators.html"%}}
{{%/column%}}
{{%/section%}}

{{%anchor 4%}}

# Optimized Initial Data Load

You can now control the initial load with metadata. Using the `@SpaceInitialLoadQuery` annotation lets you define a method that implements the query for a Space class that should be pre loaded.

Example:


```java
public class InitialLoadQueryExample {

    @SpaceInitialLoadQuery(type="com.example.domain.MyClass")
    public String initialLoad(ClusterInfo clusterInfo) {
        Integer num = clusterInfo.getNumberOfInstances(), instanceId = clusterInfo.getInstanceId();
        return "propertyA > 50 AND routingProperty % " + num + " = " + instanceId;
    }
}
```

{{%learn "/xap100/space-persistency-initial-load.html"%}}


{{%anchor 5%}}



{{%anchor 6%}}

# Custom Change Operation

The change API now includes an option for custom change operation. The user can implement his own change operation in case the built-in operations (increment, add, remove, set, etc.) do not suffice.

{{%learn "/xap100/change-api-custom-operation.html"%}}


{{%anchor 7%}}

# Performance improvement for reg ex query

Free text search and Regular Expression queries can use indexes now, which will improve performance.

{{%section%}}
{{%column width="50%" %}}
{{%/column%}}
{{%column width="10%" %}}
{{%learn "/xap100/query-free-text-search.html"%}}
{{%/column%}}
{{%column width="10%" %}}
{{%learn "/xap100net/query-free-text-search.html"%}}
{{%/column%}}
{{%/section%}}


{{%anchor 8 %}}

# Jetty 9 support

With this new version it is possible to use Jetty version 9.

{{%learn "/xap100/web-jetty-processing-unit-container.html#jetty-version"%}}


{{%anchor 9%}}

# Web UI Enhancements

A number of additional features were added to the web ui, specifically the ability to view jmx urls, specify zones, deployment properties and the capability of viewing the full data grid configuration.


{{%learn "/xap100adm/web-management-data-grid-view.html"%}}



{{%anchor 10%}}

# Create & configure a Space without a url

XAP 10.0 lets you create, connect and configure a Space without declaring a url.


#### Embedded Space:

For an embedded Space the `embedded-space` tag is used in the name space configuration and in the plain XML configuration the `EmbeddedSpaceFactoryBean`.

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" name="mySpace"/>
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

#### Remote Space:

For a remote Space the `space-proxy` tag is used in the name space configuration and in the plain XML configuration the `SpaceProxyFactoryBean`.


{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space-proxy  id="space" name="mySpace"/>
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


{{%anchor 11%}}

# XAP Support for Mule 3.5

{{%learn "/xap100/mule-esb.html"%}}


{{%anchor 12%}}

# Compressed Storage Mode .Net

{{%learn "/xap100net/poco-storage-type.html"%}}



{{%anchor 13%}}

# Externalizable End of Life

End-Of-Life: starting with XAP 10.0, the default behaviour is to ignore Externalizable and serialize Externalizable POJOs the same way as plain POJOs.

{{%learn "/release_notes/100upgrading.html"%}}
