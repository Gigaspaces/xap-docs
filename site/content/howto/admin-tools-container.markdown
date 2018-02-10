---
type: post
title:  Container
weight: 100
parent: admin-tools-overview.html
---

................
 
 
# Create 

**Parameters**

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| host   |  Host to create container on | Yes | | 

**Options** 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --help      |  Display help information for this command    |  |
| --memory=\<memory\>   |  Max JVM memory for the container    |  |
| --zone=\<zone\>     |  Zone where the container should be deployed   |  |
| --property=\<String=String\>  | Additional System properties|  |
| --version   | Display version information | |


{{%tabs%}}
{{%tab "CLI"%}}

```bash
<XAP-HOME>/bin/xap container create myHost
```
{{%/tab%}}
{{%tab "REST"%}} 
{{%/tab%}}
{{%/tabs%}}
 
# Kill 

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container kill admin~4300
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 
# Restart

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container restart admin~2712
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}

## List 

{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container list
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}



## Info

**Parameters**
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container info admin~2712
```
{{%/tab%}}
{{%tab "REST"%}}
{{%/tab%}}
{{%/tabs%}}
 