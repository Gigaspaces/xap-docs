---
type: post
title:  Quiescing a Processing Unit 
weight: 1800
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

 

#  To Quiesce a PU


{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- name : The name of the Pu.
- description: Quiesce description.
  

*Example:*

```bash
<XAP-HOME>/bin/xap pu quiesce  myPu startingUpdate
```
{{%/tab%}}

{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name : The name of the Pu.
- description: Quiesce description.

 
*Example:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myPu/quiesce?description=ready%20for%20update'
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


<br>

#  To Un - Quiesce a PU

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- name : The name of the Pu.
  

*Example:*

```bash
<XAP-HOME>/bin/xap pu unquiesce  myPu 
```
{{%/tab%}}

{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name : The name of the Pu.

 
*Example:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myPu/unquiesce'
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

