---
type: post
title: Viewing Container Information
weight: 500
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
**To view the general Container details:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

***List all Containers***<br>

_Parameters:_

*Example:*

```bash
<XAP-HOME>/bin/xap container list
```


***List Container information***<br>

_Parameters:_

- containerId : The Id  of the Container.
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap container info  containerId
```

{{%/tab%}}


{{%tab "REST Manager API"%}}
***List all Containers***<br>
 

_Parameters:_


*Example:*

```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/containers'
```

***List Container information***<br>
 

_Parameters:_<br> 

- containerId : The Id  of the Container.
 
 
*Example:*
 
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/conatinerId'
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

