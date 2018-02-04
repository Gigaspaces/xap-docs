---
type: post
title:  Space and PU
weight: 100
parent: admin-tools-overview.html
---

................
 
#  Deploying a Space 
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
<XAP-HOME>/bin/xap space deploy 
```
{{%/tab%}}


{{%tab "Web Console"%}}
[Deploying a Space](/xap/12.3/admin/web-management-deploy-space.html)
{{%/tab%}} 

{{%tab "Admin Console"%}}

```bash
<XAP-HOME>/bin/xap space deploy 
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