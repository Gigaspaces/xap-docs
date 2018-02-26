---
type: post
title:  Viewing PU Information
weight: 600
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

**To view PU information:**

  
{{%tabs%}}
{{%tab "Command Line Interface"%}}

***Display PU Information***

_Parameters:_<br> 

- name : The name of the Space.
  

*Example:*

```bash
<XAP-HOME>/bin/xap pu info  myPu
```
 
  
***Display PU instance information***

_Parameters:_<br> 

- name: Name of PU
- instanceId : The id of the PU instance to use.

 
*Example:*
 
```bash
<XAP-HOME>/bin/xap pu info-instances myPu myPu~1
```
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

***Display PU Instances***

_Parameters:_<br>

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the Space.

 
*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/deployments/myPu/instances'
```
 
***Display PU instance information***

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the PU.<br>
- instanceId : The id of the PU instance to use.

_Options:_<br>
 
*Examples:*
 
```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/deployments/myPu/instances/myPu~1'
```
 
{{%/tab%}}


{{%tab "Web Management Console"%}}
TBD
{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}

