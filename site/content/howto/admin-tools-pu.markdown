---
type: post
title:  Processing Unit
weight: 400
parent: admin-tools-overview.html
---
 
 
# Deploy

Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit to deploy   | Yes |
| \[file\] | Path to processing unit file (.jar or .zip) |Yes  |
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---backups=\<backups\>   |  Number of backups per partition   |  |
| ---instances=\<instances\>   | Number of instances|  |
| ---max-instances-per-machine=\<maxInstancesPerMachine\>| Determines maximum number of instances in same machine |  |
| ---max-instances-per-vm=\<maxInstancesPerVM\>   |Determines maximum number of instances in same VM |  |
| ---partitions=\<partitions\>   | Number of partitions|  |
| ---property=\<String,String>\>   | Context properties|  |
| ---schema=\<schema\> | Cluster schema ||
| ---version  |Display version information | | 
| ---zones=\<zones\> |Which zones can host this processing unit| |

 
## Example

```bash
<XAP-HOME>/bin/xap pu deploy --partitions=2  myPu  mypu.jar
```
 
 

# Un-Deploy

Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit to deploy   | Yes |
 
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --keep-file    |  Keep the deployment file for future use    |  |
 
## Example

```bash
<XAP-HOME>/bin/xap pu undeploy --keep-file myPu  
```
 
 


# List
 
## Example

```bash
<XAP-HOME>/bin/xap pu list
```
 
 
 
# List instances
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit    | Yes |

  
## Example

```bash
<XAP-HOME>/bin/xap pu list myPu
```
 
  
# Info
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit    | Yes |

  
## Example

```bash
<XAP-HOME>/bin/xap pu info myPu
```
 
 
# Info Instance
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit    | Yes |

  
## Example

```bash
<XAP-HOME>/bin/xap pu info-instance myPu
```
 
 
# Increment
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit    | Yes |

  
## Example

```bash
<XAP-HOME>/bin/xap pu increment myPu
```
 
  
# Decrement
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of the processing unit    | Yes |

  
## Example

```bash
<XAP-HOME>/bin/xap pu decrement myPu
```
 
    

# Relocate
 
## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| id    | Id of Processing  unit instance to terminate |  Yes|
| targetContainerId    |  Target container to relocate to   | Yes |
  
  
## Example

```bash
<XAP-HOME>/bin/xap pu relocate myPuId container~22
```
 
 
# Quiesce  

## Description

Sends a quiesce request to the GSM for the provided processing unit’s name.

## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of Processing  Unit |  Yes|
 


 
## Example

```bash
<XAP-HOME>/bin/xap pu quiesce myPu
```
 

# Unquiesce 

## Parameters
  
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name   | Name of Processing  Unit |  Yes|
 

##  Example
  
 
```bash
<XAP-HOME>/bin/xap pu unquiesce myPu
``` 


# Run

Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| path  | The relative /absolute path of the processing unit directory or jar | Yes |
 
 
 
## Options
 
|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---ha  | Should the processing unit include backups for high availability|  |
| ---instances=\<instances\>   | Number of instances|  |
| ---partitions=\<partitions\>   | Number of partitions|  |
 
## Example
```bash
<XAP-HOME>/bin/xap pu run --partitions=2  ../mypu.jar
```
 
