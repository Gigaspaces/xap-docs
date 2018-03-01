---
type: post
title:  Starting a Container
weight: 200
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
# To start a Container

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

- host : Name of the host unit to create the container.
 

_Options:_<br>

- ---memory:\<memory\>:Container's JVM max memory.<br>
- ---zone=\<zone name\>:Container's zone.<br>
- ---property=\<String=String\>: Additional System properties.
 
*Example:*<br>

```bash
<XAP-HOME>/bin/xap container create myHost myContainer
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- host: Name of the host unit to create the container.
 

_Options:_<br>

- memory : Container's JVM max memory.<br>
- zone:Container's zone.<br>
- vmArguments: Additional System properties.
 
 
*Example:*<br>
 

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

