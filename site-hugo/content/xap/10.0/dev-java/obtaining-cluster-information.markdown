---
type: post100
title:  Cluster Information
categories: XAP100
parent: the-processing-unit-overview.html
weight: 300
---

{{% ssummary %}}  {{% /ssummary %}}



One of the core concepts of GigaSpaces processing units is the fact that the clustering topology of the processing unit is determined at deployment time. Therefore, when building a processing unit, there is almost no need to be aware of the actual clustering topology with which the processing unit is deployed.
However, in some cases you may want your processing unit to be aware of it.
This mechanism is also used internally by the platform to maintain the clustering information when deploying a space for example.

# Available Cluster Information

As mentioned above the cluster information is passed to the components of each processing unit instance via an instance of the `org.openspaces.core.cluster.ClusterInfo` class, which holds the following information:


| Attribute Name | Description | Method |
|:---------------|:------------|:-------|
| Cluster schema | If the processing unit contains an embedded space, the cluster schema of that space | `getSchema()`|
| <nobr>Number of instances</nobr> | The number of instances of the processing unit. If the processing unit contains an embedded space, denotes the number of primary instances | `getNumberOfInstances()` |
| Number of backups |If the processing unit contains an embedded space with backups, denotes the number of backups per primary instance | `getNumberOfBackups()` |
| Instance ID | A value between 1 to `numberOfInstances`, denoting the instance ID of the processing unit instance | `getInstanceId()` |
| Backup ID | If the processing unit contains an embedded space with backups, a value between on 1 to `numberOfBackups`, denoting the backup ID of the processing instance. The primary instance ID which the processing unit instance is backing up is denoted by `instanceId`. If the processing unit instance is not a backup instance, `null` is returned | `getBackupId()` |
| Running number | A running number of the processing unit instance instance. Takes into account different topologies and provides a unique identifier (starting from `0`) of the processing unit instance within the cluster | `getRunningNumber()` |

{{% info %}}
`null` value for one of these properties means that they are not applicable for the processing unit instance at hand or the used deployment topology.
{{%/info%}}

# Obtaining the Cluster Information

A `ClusterInfo` instance can be injected to the processing unit instances upon their creation.
There are several ways to enable the injection:

- Implementing the `ClusterInfoAware` interface. Any component (a bean in the Spring configuration) within the processing unit that implements this interface will be automatically injected with the `ClusterInfo` instance
- Using the `@ClusterInfoContext` annotation. Fields of the processing unit components (beans in the Spring configuration) annotated with this annotation will be automatically injected with the `ClusterInfo` instance

Once injected, the processing unit components can access all the information within the `ClusterInfo` instance and cache it locally for later usage (by saving it as an instance variable for example).

# ClusterInfoAware

GigaSpaces provides the `ClusterInfoAware` interface, which allows beans to be injected with the `ClusterInfo` instance. This is similar to Spring's `ApplicationContextAware` interface, that allows beans to be injected with Spring's `ApplicationContext` by implementing the interface.
Here's the `ClusterInfoAware` interface:


```java
public interface ClusterInfoAware {

    /**
     * Sets the cluster information.
     *
     * <p>Note, the cluster information is obtained externally from the application context which means
     * that this feature need to be supported by specific containers (and is not supported by plain
     * Spring application context). This means that beans that implement {@link ClusterInfoAware}
     * should take into account the fact that the cluster info provided might be null.
     *
     * @param clusterInfo
     *            The cluster information to be injected
     */
    void setClusterInfo(ClusterInfo clusterInfo);
}
```

All of the processing unit [runtime modes](./deploying-and-running-the-processing-unit.html) provide support for the `ClusterInfo` and `ClusterInfoAware` interfaces by default. Built-in GigaSpaces components make use of this feature. User-defined beans can make use of this information as well (for example, to connect to a specific database based on the ID of the processing unit instance).

{{% anchor ClusterInfoContext %}}

# @ClusterInfoContext

Similar to the the `ClusterInfoAware` interface, this field level annotation allows beans to be injected with the `ClusterInfo` instance. Here's an example:


```java
public class MyBean {

    @ClusterInfoContext
    private ClusterInfo clusterInfo;

    ...
}
```

# Space Construction and ClusterInfo

The [Space component](./the-space-configuration.html) implements the `ClusterInfoAware`, and uses it in order to construct an embedded space by mapping `ClusterInfo` properties to Space URL properties (if provided). Here is a mapping from `ClusterInfo` to Space URL properties:


|ClusterInfo|Space URL|
|:----------|:--------|
| ClusterInfo#getSchema | cluster_schema |
| ClusterInfo#getNumberOfInstances | total_members first value (before the comma) |
| ClusterInfo#getNumberOfBackups | total_members second value (after the comma) |
| ClusterInfo#getInstanceId | id |
| ClusterInfo#getBackupId | backup_id |

# ClusterInfo XML Injection

When running the processing unit in any of the [runtime modes](./deploying-and-running-the-processing-unit.html), `ClusterInfo` can also be used directly within the Spring XML configuration. In a similar manner, properties can be injected. Here is an example of how this can be used:


```xml

<bean id="myBean" class="MyBean">
    <property name="connectionUrl" value="testconnection_${clusterInfo.runningNumber}" />
</bean>
```

In the above example, the value of the `connectionUrl` property of `myBean` is  built based on the `runningNumber` provided by the `ClusterInfo` instance. Here is a list mapping the `ClusterInfo` properties to their equivalent `${clusterInfo.*`} syntax:


|ClusterInfo|Space URL|
|:----------|:--------|
| ClusterInfo#getSchema | clusterInfo.schema |
| ClusterInfo#getNumberOfInstances | clusterInfo.numberOfInstances |
| ClusterInfo#getNumberOfBackups | clusterInfo.numberOfBackups |
| ClusterInfo#getInstanceId | clusterInfo.instanceId |
| ClusterInfo#getBackupId | clusterInfo.backupId |
| ClusterInfo#getRunningNumber | clusterInfo.runningNumber |
| ClusterInfo#getRunningNumberOffest1 | clusterInfo.runningNumberOffest1 |

# Processing Unit Container Injection

Each of the [runtime modes](./deploying-and-running-the-processing-unit.html) supports the injection of `ClusterInfo`. However, when running in [standalone mode](./running-in-standalone-mode.html) or [within your IDE](./running-and-debugging-within-your-ide.html), the `-cluster` parameter controls the clustering aspects of the processing unit instance(s). Below is a list of the parameters you can specify for the standalone and IDE runtime modes, and how they are mapped the `ClusterInfo` properties:

- `schema clusterSchemaName`: Maps to `ClusterInfo#setSchema`.
- `total_members numberOfInstances[,numberOfBackups]`: Maps to `ClusterInfo#setNumberOfInstances`, and optionally `ClusterInfo#setNumberOfBackups`.
- `id instanceId`: Maps to `ClusterInfo#setInstanceId`.
- `backup_id backupId`: Maps to `ClusterInfo#setBackupId`.

{{% refer %}}For more details on how to use the `-cluster` option, see the [processing unit runtime modes section](./deploying-and-running-the-processing-unit.html){{% /refer %}}

