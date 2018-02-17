---
type: post
title:  Space 
weight: 300
parent: admin-tools-overview.html
---


# Deploying a Space 
(automatically deploys a PU that contains only a Space)

##  Parameters

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name    |  The name of the Space | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ha       |  Should backups be used for high availability | |
| partitions=\<partitions\>    |  Number of partitions    |  |
| requires-isolation   | if each instance should be provisioned in an isolated container|  |


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space deploy mySpace
```
{{%/tab%}}

{{%tab "REST"%}}
```rest
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces?name=mySpace&backups=true&requiresIsolation=true'
```
{{%/tab%}}
 

{{%tab "Web Admin UI"%}}
 
1. From the Deploy menu on the menu bar, select Space.<br>
2. In the Space Deployment dialog box, do the following:<br>
    a. In the Space name box, type a name for the space.<br>
    b. (Optional) If you want this Space to be secure, do the following In the User Login Details area:<br> 
        Select Secured Space.<br>
        Provide the user credentials in the User Name and Password boxes.<br>
    In the Cluster Info area, apply the required configuration details:<br>
    In the Cluster schema box, specify the SLA definitions (cluster topology):<br>
        None - A standalone Space.<br>
        Partitioned - A cluster that is partitioned across the instances that are specified.<br>
        Sync_replicated - A cluster with synchronous replication across the instances that are specified.<br>
        Async_replicated - A cluster with asynchronous replication across the instances that are specified.<br>
-- In the Number of Instances box, specify the number of primary Space instances to deploy in the cluster. 
-- (For partitioned clusters) In the Number of Backups box, define the number of backup Spaces for each primary Space.
-- In the Max Inst. per VM, define the maximum number of Space instances each virtual host may contain (the default is 1).
-- In the Max Inst. per VM, define the maximum number of Space instances each physical host may contain.
-- If you have more than one host, you can specify on which host to deploy the primary Space instances.
3. (Optional) If you want to use a configuration file to specify the SLA definitions, or if you want to override your defined SLA definitions in specific scenarios, click Next. 
4. Click the Browse button next to the SLA override box and select the sla.xml file that you want to use.


5. If your environment contains zones, you can do one of the following:
-- select the Select Zone option and:
--- From the list on the left, select which zone to use for the -zone deployment parameter. 
--- In the Max. Instances (partitions) number area on the right, define the maximum instances per zone (-max-instances-per-zone deployment parameter).
-- If you dont want to specify a zone, select Any Zone.
7. Click Deploy.

 
{{%/tab%}}
{{%/tabs%}}

# List 

Lists all spaces

{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space list
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
{{%/tab%}}
{{%/tabs%}}

# List Instances

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name    |  The name of the Space | Yes | | 

## Options 


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
{{%/tab%}}
{{%/tabs%}}

# Space Info

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name    |  The name of the Space | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| operation-stats | Get space operation statistics | |
| type-stats      | Get space types statistics | |


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space info --operation-stats mySpace
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
{{%/tab%}}
{{%/tabs%}}


# Space Instance Info

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| instanceId    |  Space instance Id | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| operation-stats | Get space operation statistics | |
| type-stats      | Get space types statistics | |
| replication-stats | Get replication statistics | |


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space info-instance --operation-stats mySpace~1
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
{{%/tab%}}
{{%/tabs%}}


# Run 

Under construction 

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name    |  The name of the Space | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ha | Should the space include backups for high availability | |
| instances=\<instances\>      | Which instances should be run (default is all) | |
| partitions=\<partitions\>      | Number of partitions in space | |
| version | DIsplay version information | |


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space run --partitions=2 mySpace
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
{{%/tab%}}
{{%/tabs%}}

