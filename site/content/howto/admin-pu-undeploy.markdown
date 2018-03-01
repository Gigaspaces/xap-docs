---
type: post
title:  Undeploying a PU
weight: 900
parent: admin-spaces-pu.html
---
 
 
 

 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- name : The name of the Pu to undeploy.

_Options:_<br>

- ---keep-file: Keep the undeployed file for future use.
- ---version: Display version information.

*Example:*

```bash
<XAP-HOME>/bin/xap pu undeploy myPu 
```
{{%/tab%}}

{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name : The name of the Pu to undeploy.

_Options:_<br>

- ---keep-file: Keep the undeployed file for future use.
 
 
*Example:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myPu'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}
{{%/tab%}}
{{%tab "GigaSpaces Management Console"%}}
TBD
{{%/tab%}}
{{%tab "Administration API"%}}
TBD
{{%/tab%}}
{{% /tabs %}}

  