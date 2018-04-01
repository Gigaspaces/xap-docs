---
type: post123
title:  Deploying a Processing Unit
categories: XAP123ADM,PRM
weight: 400
parent: admin-spaces-pu.html
---
 
 
 
**Deploying a Processing Unit onto the Service Grid.**
 

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu deploy`

*Description:*

Deploys a Processing Unit to the Service Grid.

*Parameters and Options:*

|Item | Name| Description |
|:----|:----|:------------|
|Parameter | name |Name of the processing unit to deploy|
|Parameter | file |Path to processing unit file (.jar or .zip) |
|Option    | backups=\<backups\> |Number of backups per partition.|
|Option    | instances=\<instances\> |Number of instances.|
|Option    | max-instances-per-machine=\<maxInstancesPerMachine\>|Determines maximum number of instances in same machine (from each partition).|
|Option    | max-instances-per-vm=\<maxInstancesPerVM\>| Determines maximum number of instances in same VM (from each partition). |
|Option    |partitions=\<partitions\> | Number of partitions. |
|Option    |property=\<String,String\>|Context properties.|
|Option    |schema=\<schema\>|Cluster schema (partitioned, sync_replicated, async_replicated).|
|Option    |version | Display version information.|
|Option    |zones=\<zones\>| Which zones can host this processing unit.|

 
*Input Example:*

This example deploys a PU named **myPu** with 2 partitions using the mypu.jar file.

```bash
xap pu deploy --partitions=2  myPu  mypu.jar
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

*Path*

`POST /pus`


*Description:*

Deploys a Processing Unit onto the Service Grid.

*Example:*
 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
    "name": "myPu", \ 
    "resource": "myPu.jar", \ 
    "sla": { \ 
      "requiresIsolation": true, \ 
      "zones": [ \ 
        "string" \ 
      ], \ 
      "maxInstancesPerVM": 1, \ 
      "maxInstancesPerMachine": 1 \ 
    }, \ 
    "contextProperties": {} \ 
  }' 'http://localhost:8090/v1/deployments'
```

*Options:*

| Option | Description | Required |
|:-------|:------------|:---------|
|name | Name of the processing unit.| Yes |
|resource|File/Path to processing unit file (.jar or .zip) | Yes|
|schema | Type of schema to use | No| 
|instances | Number of instances to deploy | No|
|partitions| Number of partitions | No|
|backupsPerPartition|Number of backups per partition. | No| 
|requiresIsolation| Requires isolation | No|
|zones|Which zones can host this processing unit. | No|
|contextProperties  |Context properties. | No|
 

{{%/tab%}}


{{%tab "Web Management Console"%}}

1. From the Deploy menu on the menu bar, select **Processing Unit**.
1. In the Processing Unit Deployment dialog box, do the following:

	<ol type="a">
		<li>To search an external repository for the relevant resource file, select the <b>Upload jar/war/zip file</b> option, click the Browse button next to the <b>Upload file</b> box, and select the required file.</li>
		<li>To use a local resource file, select the <b>Select from list</b> option and select the required file from the dropdown list.</li>
		<li>(Optional) If you want this Processing Unit to have a different name from the one specified in the resource file, type the new name in the <b>Override Processing Unit name</b> box.</li>
		<li>(Optional) If you want this Processing Unit to be secure, do the following In the <b>User Login Details</b> area:
		<ul>
			<li>Select <b>Secured Space</b>.</li>
			<li>Provide the user credentials in the <b>User Name</b> and <b>Password</b> boxes.
		</ul>
		</ol>
1.	In the **Cluster Info** area, apply the required configuration details:
	<ol type="a">
		<li>In the <b>Cluster schema</b> box, specify the SLA definitions (cluster topology):</li>
		<ul>
			<li><b>None</b> - A standalone Processing Unit with an embedded Space.</li>
			<li><b>Partitioned</b> - A cluster that is partitioned across the instances that are specified.</li>
			<li><b>Sync_replicated</b> - A cluster with synchronous replication across the instances that are specified.</li>
			<li><b>Async_replicated</b> - A cluster with asynchronous replication across the instances that are specified.</li>
		</ul>
		<li>In the <b>Number of Instances</b> box,  specify the number of primary Processing Unit instances to deploy in the cluster.</li>
		<li>(For partitioned clusters) In the <b>Number of Backups</b> box, define the number of backup Processing Units for each primary Processing Unit.</li>
		<li>In the <b>Max Inst. per VM</b> box, define the maximum number of Processing Unit instances each virtual host may contain (the default is 1).</li>
		<li>In the <b>Max Inst. per VM</b> box, define the maximum number of Processing Unit instances each physical host may contain.</li>
		<li>If you have more than one host, you can specify on which host to deploy the primary Processing Unit instances.</li>
		</li>
	</ol>	
1. Click **Deploy**.
 
{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}

