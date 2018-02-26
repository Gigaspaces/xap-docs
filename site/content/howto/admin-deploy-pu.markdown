---
type: post
title:  Deploying a PU
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
TBD 
{{%/tab%}}

{{%tab "GigaSpaces Management Console"%}}
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}

