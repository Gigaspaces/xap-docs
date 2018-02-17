---
type: post
title:  Container
weight: 200
parent: admin-tools-overview.html
---

 
 
 
# Create 

## Parameters

|  Parameter  |  Description                 | Required |
|:------------|:-----------------------------|:---------|
| host        |  Host to create the container on | Yes |

##  Options

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---memory=\<memory\>   |  Max JVM memory for the container    |  |
| ---zone=\<zone\>     |  Zone where the container should be deployed   | [Zones](/xap/12.3/admin/the-sla-zones.html) |
| ---property=\<String=String\>  | Additional System properties|  |
 
 
## Examples

```bash
<XAP-HOME>/bin/xap container create myHost
<XAP-HOME>/bin/xap container create --zone=green myHost
```
 
 
# Kill 

## Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| containerId   |  The id of the container to be killed.  | Yes |
 
 

## Example
```bash
<XAP-HOME>/bin/xap container kill container1
```
 
 
# Restart

## Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| containerId   | The id of the container that will be restarted   | Yes |
 

## Example 

```bash
<XAP-HOME>/bin/xap container restart container1
```
 

# List 

## Example
 
```bash
<XAP-HOME>/bin/xap container list
```
 


# Info

## Parameters
 
|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| containerId   | The id of the container that the info shhould be displayed   | Yes |

## Example
 
```bash
<XAP-HOME>/bin/xap container info container1
```
 