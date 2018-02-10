---
type: post
title:  PU
weight: 300
parent: admin-tools-overview.html
---
 
 
# Deploy

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit to deploy   | Yes | | 
| \[file\] | Path to processing unit file (.jar or .zip) |Yes  | |
 
 
**Options**
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --help    |  Display help information for deploy    |  |
| --backups=\<backups\>   |  Number of backups per partition   |  |
| --instances=\<instances\>   | Number of instances|  |
| --max-instances-per-machine=\<maxInstancesPerMachine\>| Determines maximum number of instances in same machine |  |
| --max-instances-per-vm=\<maxInstancesPerVM\>   |Determines maximum number of instances in same VM |  |
| --partitions=\<partitions\>   | Number of partitions|  |
| --property=\<String,String>\>   | Context properties|  |
| --schema=\<schema\> | Cluster schema ||
| --version  |Display version information | | 
| --zones=\<zones\> |Which zones can host this processing unit| |

 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu deploy --partitions=2  myPu  mypu.jar
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
 