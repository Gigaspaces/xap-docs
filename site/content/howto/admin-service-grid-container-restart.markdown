---
type: post
title:  Restarting a Container
weight: 400
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
# To restart a Container

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- containerId : Id of the container.
 
_Options:_<br>
 
 
*Example:*<br>

```bash
<XAP-HOME>/bin/xap container restart myContainerId
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- containerId : Id of the container. 

_Options:_<br>

 
*Example:*<br>
 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/containerId/restart'
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

