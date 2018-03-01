---
type: post
title:  Terminating a Container
weight: 300
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
# To terminate a Container

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- containerId : Id of the container.
 
_Options:_<br>
 
 
*Example:*<br>

```bash
<XAP-HOME>/bin/xap container kill myContainerId
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- containerId : Id of the container. 

_Options:_<br>

 
*Example:*<br>
 
```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/containerId'
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

