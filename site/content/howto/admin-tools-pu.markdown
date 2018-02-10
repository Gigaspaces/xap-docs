---
type: post
title:  PU
weight: 400
parent: admin-tools-overview.html
---
 
 
# Deploy

Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit to deploy   | Yes | | 
| \[file\] | Path to processing unit file (.jar or .zip) |Yes  | |
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| help    |  Display help information for deploy    |  |
| backups=\<backups\>   |  Number of backups per partition   |  |
| instances=\<instances\>   | Number of instances|  |
| max-instances-per-machine=\<maxInstancesPerMachine\>| Determines maximum number of instances in same machine |  |
| max-instances-per-vm=\<maxInstancesPerVM\>   |Determines maximum number of instances in same VM |  |
| partitions=\<partitions\>   | Number of partitions|  |
| property=\<String,String>\>   | Context properties|  |
| schema=\<schema\> | Cluster schema ||
| version  |Display version information | | 
| zones=\<zones\> |Which zones can host this processing unit| |

 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu deploy --partitions=2  myPu  mypu.jar
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 

# Un-Deploy

Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit to deploy   | Yes | | 
 
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| keep-file    |  Keep the deployment file for future use    |  |
 

 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu undeploy --keep-file myPu  
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 


# List
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu list
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
 
# List instances
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit    | Yes | | 

  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu list myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
  
# Info
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit    | Yes | | 

  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu info myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
# Info Instance
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit    | Yes | | 

  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu info-instance myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
# Increment
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit    | Yes | | 

  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu increment myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
  
# Decrement
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of the processing unit    | Yes | | 

  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu decrement myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
    

# Relocate
 
## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| id    | Id of Processing  unit instance to terminate |  Yes| |
| targetContainerId    |  Target container to relocate to   | Yes | |
  
  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu relocate myPuId container~22
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
# Quiesce 

## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of Processing  Unit |  Yes| |
 
  
  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu quiesce myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}

# Unquiesce 

## Parameters
  
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name   | Name of Processing  Unit |  Yes| |
 
  
  
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu unquiesce myPu
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}


# Run

Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| path  | The relative /absolute path of the processing unit directory or jar | Yes | | 
 
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ha  | Should the processing unit include backups for high availability|  |
| instances=\<instances\>   | Number of instances|  |
| partitions=\<partitions\>   | Number of partitions|  |
 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap pu run --partitions=2  ../mypu.jar
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
