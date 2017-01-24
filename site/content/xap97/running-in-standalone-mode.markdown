---
type: post97
title:  Standalone Mode
categories: XAP97
parent: deploying-and-running-overview.html
weight: 200
---


{{% ssummary %}}  {{% /ssummary %}}



The standalone processing unit container allows you to run a processing unit in standalone mode, which means that the processing unit constructs its own dedicated classloader, and automatically includes it its classpath all of the jar files located under the processing unit's `lib` directory.
It's implementation class is [StandaloneProcessingUnitContainer]({{% api-javadoc %}}/org/openspaces/pu/container/standalone/StandaloneProcessingUnitContainer.html).

The standalone processing unit container is built around Spring's `ApplicationContext` with several extensions relevant to GigaSpaces, such as [ClusterInfo](./obtaining-cluster-information.html).
It contains a `main()` method and can be started from an external script or programmatically by using the `ProcessingUnitContainerProvider` abstraction.

It should be used when you would like to run your processing unit in a non-managed environment (outside of the service grid) or from within your own code, and still benefit from the automatic classpath creation as with the managed mode.

# Executable StandaloneProcessingUnitContainer

The `StandaloneProcessingUnitContainer` provides an executable `main()` method that allows it to be run directly. The `main()` method uses the `StandaloneProcessingUnitContainerProvider` and command-line conventions in order to create the `StandaloneProcessingUnitContainer`. A required parameter is the processing unit location, pointing to the file-system location of the processing unit directory. The following is a list of all the possible command-line parameters available:

The `StandaloneProcessingUnitContainer` class provides an executable `main()` method, allowing you to run it directly via a shell script for example. The `main()` method uses the `StandaloneProcessingUnitContainerProvider` class and program arguments in order to create the `StandaloneProcessingUnitContainer`. The following is a list of all the possible program arguments that can be specified to the `StandaloneProcessingUnitContainer`:


| Option | Description |
|:-------|:------------|
|`-config [configLocation]` | Allows you to set/add a processing unit deployment descriptor location.{{<wbr>}}Follows the Spring [Resource Loader](http://static.springframework.org/spring/docs/2.5.x/reference/resources.html#resources-resourceloader) including [ant style patterns](http://static.springframework.org/spring/docs/2.5.x/reference/resources.html#resources-app-ctx-wildcards-in-resource-paths). This parameter can be specified multiple times.{{<wbr>}}The default is `classpath*:/META-INF/spring/pu.xml`. |
|`-cluster [cluster options]` | Allows you to control the `ClusterInfo` injected into the container and the runtime topology{{<wbr>}}of the processing unit.{{<wbr>}}The following options are available (they are used automatically by any embedded space included{{<wbr>}}in the processing unit):{{<wbr>}}- `schema` - the cluster schema used by the processing unit. Possible values are `sync-replicated`, `async-replicated` and `partitioned-sync2backup`{{<wbr>}}- `total_members` - Determines the total members in the emulated cluster. Format is `numberOfInstances[,numberOfBackups]`, e.g. `total_members 2,1`{{<wbr>}}- `id` -- Mandatory. Determines the id of the processing unit instance the this container will run.{{<wbr>}}- `backup_id` -- If you want the container to run a backup instance, use this parameter in conjunction with the `id` parameter. It will force the container to run the instance and will determines its backup ID. |
|`-properties [property file location]` | Allows you to [inject properties](./deployment-properties.html) to the processing unit at deployment time. |
|`-properties embed://[property1 name]=[property1 value];` {{<wbr>}} `[property2 name]=[property2 value]` | Allows you to [directly inject properties](./deployment-properties.html) to the processing unit at startup time. |

# Starting the Standalone Processing Unit Container via the `puInstance` Shell Script

GigaSpaces comes with the `puInstance` shell script, which uses the `StandaloneProcessingUnitContainer` in order to run a processing unit directly from the command line.

Here are some examples of using the `puInstance` script in order to run a processing unit:

{{%tabs%}}
{{%tab "  Unix "%}}


```java
puInstance.sh -cluster schema=partitioned total_members=2 id=1 data-processor.jar
```

{{% /tab %}}
{{%tab "  Windows "%}}


```java
puInstance.bat -cluster schema=partitioned total_members=2 id=1 data-processor.jar
```

{{% /tab %}}
{{% /tabs %}}

The above example starts a processing unit (which includes an embedded space) in a partitioned cluster schema, with two members and `id=1`. In order to run the full cluster, another `puInstance` has to be started with `id=2`.

{{%tabs%}}
{{%tab "  Unix "%}}


```java
puInstance.sh -cluster schema=partitioned-sync2backup total_members=1,1 id=1 backup_id=1
-properties runtime.properties data-processor.jar
```

{{% /tab %}}
{{%tab "  Windows "%}}


```java
puInstance.bat -cluster schema=partitioned-sync2backup total_members=1,1 id=1 backup_id=1
-properties runtime.properties data-processor.jar
```

{{% /tab %}}
{{% /tabs %}}

The above example starts a processing unit instance (with an embedded space) in a partitioned-sync2backup cluster schema, with one primary and one backup. It also uses an external properties file to inject property values at startup time.

# Starting a StandaloneProcessingUnitContainer Programmatically

Here is an example of using a `ProcessingUnitContainerProvider` in order to create a standalone processing unit container programmatically with two partitions:


```java
StandaloneProcessingUnitContainerProvider provider = new StandaloneProcessingUnitContainerProvider("/usr/gigaspaces/data-processor.jar");
// provide cluster information for the specific PU instance
ClusterInfo clusterInfo = new ClusterInfo();
clusterInfo.setSchema("partitioned-sync2backup");
clusterInfo.setNumberOfInstances(2);
clusterInfo.setNumberOfBackups(1);
clusterInfo.setInstanceId(1);
provider.setClusterInfo(clusterInfo);

// set the config location (override the default one - classpath:/META-INF/spring/pu.xml)
provider.addConfigLocation("classpath:/test/my-pu.xml");

// Build the Spring application context and "start" it
ProcessingUnitContainer container = provider.createContainer();

// ...

container.close();
```

The `StandaloneProcessingUnitContainerProvider` is constructed with a file-system path to the processing unit jar file. It constructs a new class loader and adds all the jar files in the processing unit's `lib` directory to it automatically.

# Disabling Embedded Lookup Service

The StandaloneProcessingUnitContainer automatically starts an embedded Lookup service. If you intend to use a separate Lookup service you can disable the embedded Lookup service by passing the setting the `com.j_spaces.core.container.directory_services.jini_lus.enabled` system property to false. This property can also be set within the Space definition:

```xml
<os-core:embedded-space id="space" name="mySpace">
    <os-core:properties>
      <props>
        <prop key="com.j_spaces.core.container.directory_services.jini_lus.start-embedded-lus">false</prop>
      </props>
    </os-core:properties>
</os-core:embedded-space>
```