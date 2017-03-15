---
type: post
title:  Distributed Mirror
categories: SBP
parent: production.html
weight: 50
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|:----------:|
| Shay Hassidim| 12.0 | March 2017| [Asynchronous Persistence]({{%latestjavaurl%}}/asynchronous-persistency-with-the-mirror.html)| {{%download "/download_files/sbp/distributed-mirror.zip"%}}|

# Overview 

By default a single mirror instance used to perform write-behind activity, persisting data from the space to the enterprise backend database. For most systems this will provide sufficient throughput to address the activity a single clustered space generating. There are cases with large space clusters or with systems that produce large volume of activity where the amount of activity performed by a clustered space would require a distributed (multi-instance) mirror setup. The solution described here allows each partition (primary and backup instances) to perform write-behind activity via a dedicated mirror. This forms an architecture where each partition using a separate mirror instance that may run on a different machine, utilizing its full CPU and network bandwidth. This approach reducing the potential for redolog accumulation generating a lag between the space data state and the persistency backend state as a result of a single mirror.
 
# Architecture

With the example below we will run a partitioned space (2 partitions) and 2 mirror instances, both writing into a single database.  

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-1.png)
{{%/align%}}

#  The Example
Download {{%download "/download_files/sbp/distributed-mirror.zip"%}} the example and extract its content into an empty folder. You will find 3 zip files:

- multi-mirror.zip – A Mirror PU configured to support multiple mirror instances
- space.zip - A Space PU configured to support multiple mirror instances
- feeder.zip – A PU writing objects into the space

##  Start the Database and its UI

Run SQLDB:
```bash
java -cp hsqldb-2.3.2.jar org.hsqldb.Server -database.0 file:testDB -dbname.0 testDB
```
 
Run SQLDB Manager:
```bash
java -cp hsqldb-2.3.2.jar org.hsqldb.util.DatabaseManager
```
 
##  Connect to the database:

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-2.jpg)
{{%/align%}}

You can find the hsqldb-2.3.2.jar within the multi-mirror.zip lib folder.

##  Start Agent
Start an agent with 7 GSCs. This will allow the GSM to deploy each instance into a dedicated GSC. It will allow you to track the activity easily by monitoring the GSC log file each PU instance using.
```bash
gs-agent gsa.gsc 7
```

##  Deploying Multiple Mirror Instances
To scale the mirror activity deploy multiple mirror instances , one per partition. You should deploy each separately. Each can use the same PU library with different mirror-service_id value – see below example:

```bash
gs deploy -properties embed://mirror-service_id=1 -override-name mirror1 multi-mirror.zip

gs deploy -properties embed://mirror-service_id=2 -override-name mirror2 multi-mirror.zip
```

You can create a script that will loop and deploy multiple mirror instances each with its own unique ID.

##  Deploying the Space
To deploy the space:

```bash
gs deploy -cluster schema=partitioned total_members=2,1 -override-name space space.zip
```

##  Deploying the Feeder
Deploy the feeder via the following:

```bash
gs deploy feeder.zip
```

This will write objects into the space. The mirrors will persist these into the database.

##  View the Space and the Mirrors Status

Start the GS-Web UI. You should see the space and the different mirror instances:

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-3.jpg)
{{%/align%}}

Or you can start the Management console and view the space and the different mirror instances:

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-4.jpg)
{{%/align%}}

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-5.png)
{{%/align%}}

You may access the database and check the amount of rows inserted – this should match the amount of objects within the space:

{{%align center%}}
![image](/attachment_files/sbp/mirror/distributed-mirror-6.jpg)
{{%/align%}}

##  Space pu.xml configuration

The cluster-config.mirror-service.url should include the ${clusterInfo.instanceId} as demonstrated below:

```xml
<os-core:space id="space" url="/./space" mirror="true" >
        <os-core:properties>
            <props>
                <prop key="cluster-config.mirror-service.url">jini://*/mirror-service${clusterInfo.instanceId}_container/mirror-service${clusterInfo.instanceId}</prop>
            </props>
        </os-core:properties>
    </os-core:space>
```

##  Mirror pu.xml configuration

The url mirror bean property should include the mirror service id together with the mirror name as demonstrated below:

```bash
<os-core:mirror id="mirror" url="/./mirror-service${mirror-service_id}" space-sync-endpoint="hibernateSpaceSynchronizationEndpoint" operation-grouping="group-by-space-transaction">
        <os-core:source-space name="space" partitions="2" backups="1"/>
    </os-core:mirror>
```


#  Limitations
- Distributed transactions are not supported. Local transactions are supported.