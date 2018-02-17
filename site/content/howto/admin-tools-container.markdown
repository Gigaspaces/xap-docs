---
type: post
title:  Container
weight: 200
parent: admin-tools-overview.html
---

 
 
 
# Create 

## Parameters

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| host   |  Host to create container on | Yes | | 

##  Options

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| memory=\<memory\>   |  Max JVM memory for the container    |  |
| zone=\<zone\>     |  Zone where the container should be deployed   |  |
| property=\<String=String\>  | Additional System properties|  |
 
{{%tabs%}}
{{%tab "CLI"%}}

```bash
<XAP-HOME>/bin/xap container create myHost
```
{{%/tab%}}
{{%tab "REST"%}} 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "host": "string", \ 
   "memory": "string", \ 
   "zone": "string", \ 
   "vmArguments": [ \ 
     "string" \ 
   ] \ 
 }' 'http://localhost:8090/v1/containers'
```
{{%/tab%}}
{{%/tabs%}}
 
# Kill 

## Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container kill container1
```
{{%/tab%}}
{{%tab "REST"%}}
```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/container1'
```
{{%/tab%}}
{{%/tabs%}}
 
# Restart

## Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 
 
{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container restart container1
```
{{%/tab%}}
{{%tab "REST"%}}
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/container1/restart'
```
{{%/tab%}}

{{%/tabs%}}

# List 

{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container list
```
{{%/tab%}}
{{%tab "REST"%}}
```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/containers'
```
{{%/tab%}}
{{%/tabs%}}



# Info

## Parameters
 
|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| containerId   |    | Yes | | 


{{%tabs%}}
{{%tab "CLI"%}}
```bash
<XAP-HOME>/bin/xap container info container1
```
{{%/tab%}}
{{%tab "REST"%}}
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/container1'
```
{{%/tab%}}
{{%/tabs%}}
 