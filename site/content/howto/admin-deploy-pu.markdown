---
type: post
title:  Deploying a Processing Unit
weight: 400
parent: admin-spaces-pu.html
---
 
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}
<br> 

# To deploy a PU

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- name : Name of the processing unit to deploy<br>
- \[file\]:Path to processing unit file (.jar or .zip)  
 

_Options:_<br>

- ---backups=\<backups\>:Number of backups per partition.<br>
- ---instances=\<instances\>:Number of instances.<br>
- ---max-instances-per-machine=\<maxInstancesPerMachine\> : Determines maximum number of instances in same machine.<br>
- ---max-instances-per-vm=\<maxInstancesPerVM\> : Determines maximum number of instances in same VM.<br>
- ---partitions=\<partitions\>:Number of partitions.<br>
- ---property=\<String,String>\>:Context properties.<br>
- ---schema=\<schema\>:Cluster schema.<br>
- ---version:Display version information.<br>
- ---zones=\<zones\>:Which zones can host this processing unit.

 
*Example:*<br>

This example deploys a PU named **myPu** with 2 partitions using the mypu.jar file.

```bash
<XAP-HOME>/bin/xap pu deploy --partitions=2  myPu  mypu.jar
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name: name of the processing unit.<br>
- resource: File/Path to processing unit file (.jar or .zip)

_Options:_<br>

- topology <br>
    schema:partitioned <br> 
	instances: <br> 
	partitions: <br>
	backupsPerPartition: <br> 
	
- sla<br>
	requiresIsolation: <br> 
	zones: <br>
	
- contextProperties 
 
*Example:*<br>
 

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
    "name": "string", \ 
    "resource": "string", \ 
    "topology": { \ 
      "schema": "partitioned", \ 
      "instances": 0, \ 
      "partitions": 0, \ 
      "backupsPerPartition": 0 \ 
    }, \ 
    "sla": { \ 
      "requiresIsolation": true, \ 
      "zones": [ \ 
        "string" \ 
      ], \ 
      "maxInstancesPerVM": 0, \ 
      "maxInstancesPerMachine": 0 \ 
    }, \ 
    "contextProperties": {} \ 
  }' 'http://localhost:8090/v1/deployments'
```
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
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}

