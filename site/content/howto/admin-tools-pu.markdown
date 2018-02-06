---
type: post
title:  Space and PU
weight: 100
parent: admin-tools-overview.html
---

................
 
 
#  Container 

## Create 

**Parameters**

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| host   |  Host to create container on | Yes | | 

**Options** 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --help      |  Display help information for this command    |  |
| --memory=   |  Max JVM memory for the container    |  |
| --zone=     |  Zone where the container should be deployed   |  |
| --property=  | Additional System properties|  |


{{%tabs%}}
{{%tab "CLI"%}}

```bash
<XAP-HOME>/bin/xap container create myHost
```
{{%/tab%}}

{{%tab "REST"%}}

 
[REST Manager localhost](http://localhost:8090/v1/index.html#!/Containers/post_containers)
 
{{%/tab%}}


{{%tab "Web Console"%}}
[Web Management Center](/xap/12.3/admin/web-management-common-view.html)
{{%/tab%}} 

{{%tab "Admin Console"%}}

```bash
 
```
{{%/tab%}}

{{%/tabs%}}
 
## Kill 

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container kill containerId
```
{{%/tab%}}
{{%tab "REST"%}}
[REST Manager localhost](http://localhost:8090/v1/index.html#!/Containers/post_containers)
{{%/tab%}}
{{%tab "Web Console"%}}
[Web Management Center](/xap/12.3/admin/web-management-common-view.html)
{{%/tab%}} 
{{%tab "Admin Console"%}}

```bash
```
{{%/tab%}}
{{%/tabs%}}
 
## Restart

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container restart containerId
```
{{%/tab%}}
{{%tab "REST"%}}
[REST Manager localhost](http://localhost:8090/v1/index.html#!/Containers/post_containers)
{{%/tab%}}
{{%tab "Web Console"%}}
[Web Management Center](/xap/12.3/admin/web-management-common-view.html)
{{%/tab%}} 
{{%tab "Admin Console"%}}

```bash
```
{{%/tab%}}
{{%/tabs%}}

## List 


## Info

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 

 
# Space

## Deploying a Space 
(automatically deploys a PU that contains only a Space)

##  Parameters

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| name    |  The name of the Space | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --ha       |  Should backups be used for high availability | |
| --help    |  Display help information for deploy    |  |
| --partitions=partitions    |  Number of partitions    |  |
| --requires-isolation   | if each instance should be provisioned in an isolated container|  |


{{%tabs%}}
{{%tab "CLI"%}}

```bash
<XAP-HOME>/bin/xap space deploy 
```
{{%/tab%}}

{{%tab "REST"%}}

```bash
 
```
{{%/tab%}}


{{%tab "Web Console"%}}
[Deploying a Space](/xap/12.3/admin/web-management-deploy-space.html)
{{%/tab%}} 

{{%tab "Admin Console"%}}

```bash
 
```
{{%/tab%}}

{{%/tabs%}}




Viewing General Space Details<br>
Viewing Space Information<br>

Deploying a Processing Unit<br>
Viewing the Deployed Processing Units (ability to drill through to all contents, including the Space)<br>
Viewing Processing Unit Configuration Information<br>

Undeploying a Processing Unit


Querying a Space

Viewing Data Types<br>
Viewing Space Statistics<br>
Viewing Client-Side Cache Details<br>
Viewing Event Container Details<br>
Viewing Network Details<br>
Viewing Transaction Details<br>

Quiescing a Processing Unit (advanced topic)<br>
Unquiescing a Processing Unit (advanced topic)

Restarting a Processing Unit Instance (advanced)<br>
Relocating a Processing Unit (advanced) 