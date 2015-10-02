---
type: post97net
title:  Configuration
categories: XAP97NET
weight: 200
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}}{{%/ssummary%}}

When a client connects to a space, a proxy is created that holds a connection to the space. All client interaction is performed through this proxy.
The proxy provides a simpler space API using the [ISpaceProxy](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_ISpaceProxy.htm) interface.
It is created with a Space URL and optional parameters.


# The Space URL

In order to locate a space you need to specify its URL. The SpaceURL is used as part of the space proxy.

The general format of the space URL is:


```bash
<protocol>://<lookup service hostname>:<port>/<space container name>/<space name>?<properties>
```

The following parameters can be defined:


| Name | Description |
|:-----|:------------|
| Lookup Service Host name/IP | The machine host name/IP running the lookup service. May be \* when Jini is used as a protocol. In this case the space is located using multicast or unicast with search path. |
| Port | The Jini lookup port. If no port is specified the default port (10098) will be used |
| Space Container Name | The name of the space container that holds the space. May be \* when Jini is used as a protocol. In this case the container name will be ignored when performing lookup and the space will be searched regardless of the container that holds it. |
| Space Name | The space name to search. The same name defined when space has been created via the Space browser or the `createSpace` utility. |
| [Properties String](#url properties) | (Optional) named value list of special properties. |

{{% note %}}
Make sure your network and machines running GigaSpaces are configured to have multicast enabled. See the [How to Configure Multicast]({{%currentadmurl%}}/network-multicast.html) section for details on how to enable multicast.
{{%/note%}}

### Examples

**Accessing Remote Space Using Jini Lookup Service - Unicast Discovery**{{%wbr%}}

```bash
jini://LookupServiceHostname/*/mySpace{{%wbr%}}
```


**Accessing Remote Space Using the Jini Lookup Service - Multicast Discovery**{{%wbr%}}

```bash
jini://*/*/mySpace{{%wbr%}}
```


```bash
/./mySpace (which translates to java://localhost:10098/containerName/mySpace?schema=default){{%wbr%}}
/./mySpace?schema=cache (which translates to java://localhost:10098/containerName/mySpace?schema=cache)
java://LookupServiceHostName:port/myContainerName/spaceName{{%wbr%}}
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
java://localhost:10098/mySpace_container/mySpace?schema=default

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
|`total_members` | The `total_members` attribute in the space URL stands for the total number of cache members within the cache cluster.{{% wbr %}}The number is used to create the list of members participating in the cluster on the fly based on the cache name convention. This pattern is used to avoid the need for creating a cluster topology file. {{% wbr %}}The number of actual running cache instances can vary dynamically between `1<=total_members`.{{% wbr %}}The format of the `total_members` = number of primary instances, number of backup instances per primary. In this example the value is 4,2 which means that this cluster contains up to 4 primary instances each containing 2 backup instances. The backup\_id is used to define whether the instance is a backup instance or not.{{% wbr %}}If this attribute is not defined the instance will be considered a primary instance. The container name will be translated in this case to \[cache name\]\_container\[id\]\[backup_id\].{{% wbr %}}In this case it will be expanded to mySpace_container1\_1 | |
|`backup_id` | Used in case of Partitioned Cache (when adding backup to each partition). The backup_id is used to define whether the instance is a backup instance or not. If this attribute is not defined the instance will be considered a primary cache instance.{{% wbr %}}The container name will be translated in this case to \[cache name\]_container\[id\]_\[backup_id\].{{% wbr %}} In this case it will be expanded to mySpace_container1_1. | |
|`NOWriteLease` | If true - Lease object would not return from the write/writeMultiple operations. Default: false | |
|`id` | The id attribute is used to distinguish between cache instances in this cluster. | |
|`properties` | if properties property is used as part of the URL space, space and container schema will be loaded and the properties listed as part of the properties file (`[prop-file-name].properties`) which contains the values to override the schema space/container/cluster configuration values that are defined in the schema files.{{% wbr %}}Another benefit of using the ?properties option is when we want to load system properties while VM starts or set SpaceURL attributes. See /config/gs.properties file as a reference. | |
|`mirror` | When setting this URL property it will allow the space to connect to the Mirror service to push its data and operations for asynchronous persistency.{{% wbr %}}Example:{{% wbr %}}`/./mySpace?cluster_schema=sync_replicated&mirror`{{% wbr %}} Default: no mirror connection | |

Example for space URL using options:


```bash
jini://*/*/mySpace?useLocalCache&versioned=false
/./mySpace?cluster_schema=partitioned&total_members=4&id=1
```


The [SpaceConfig](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_SpaceConfig.htm) class allows you to set different URL properties using a `Dictionary` object.

Here is an example of a space working in FIFO mode, using specific lookup groups:


```csharp
//define the space configuration object
SpaceConfig config = new SpaceConfig();

Dictionary<String,String> properties =  new Dictionary<String,String> ();

properties.Add ("fifo", "true");
properties.Add("lookupGroups","test");

config.CustomProperties = properties;

// Cluster info settings
ClusterInfo cinfo = new ClusterInfo ();
cinfo.NumberOfBackups = 1;
cinfo.Schema = "sync_replication";

config.ClusterInfo = cinfo;

//create the ISpaceProxy
ISpaceProxy proxy = GigaSpacesFactory.FindSpace("/./mySpace", config);

proxy.Dispose ();
```





