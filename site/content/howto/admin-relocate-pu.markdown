---
type: post
title:  Relocating a Processing Unit 
weight: 2000
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

#  To Relocate a PU


{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- id :Id of Processing unit instance to relocate.
- targetContainerId:  Target container for relocation.
 
_Options:_<br>

- ---version:
- ---help:


*Example:*

```bash
<XAP-HOME>/bin/xap pu replace myPu~1 myContainer~1
```
{{%/tab%}}

{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name : The name of the Pu.
- description: Quiesce description.

 
*Example:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/mySpace~%601/instances/myContainer1/relocate'
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


 
  