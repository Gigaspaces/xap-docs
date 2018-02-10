---
type: post
title:  Space 
weight: 200
parent: admin-tools-overview.html
---

................
 


# Deploying a Space 
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
| --partitions=\<partitions\>    |  Number of partitions    |  |
| --requires-isolation   | if each instance should be provisioned in an isolated container|  |


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap space deploy mySpace
```
{{%/tab%}}

{{%tab "REST"%}}
```bash 
```
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

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| --help    |  Display help information for deploy    |  |


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
| --help    |  Display help information for deploy    |  |
|--operation-stats | Get space operation statistics | |
|--type-stats      | Get space types statistics | |


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
| --help    |  Display help information for deploy    |  |
|--operation-stats | Get space operation statistics | |
|--type-stats      | Get space types statistics | |
|--replication-stats | Get replication statistics | |


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
| --help    |  Display help information for deploy    |  |
|--ha | Should the space include backups for high availability | |
|--instances=\<instances\>      | Which instances should be run (default is all) | |
|--partitions=\<partitions\>      | Number of partitions in space | |
|--version | DIsplay version information | |


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

