---
type: post
title:  Quiescing a Processing Unit 
weight: 1800
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

 

#  To Quiescie a PU

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br>

- name : The name of the PU.

_Options:_<br>

- ---ha         : Should backups be used for high availability<br>
- ---partitions=\<partitions\>    : Number of partitions<br> 
- ---requires-isolation   :  Each instance should be provisioned in an isolated container 

*Example:*<br>
This example deploys a Space named **mySpace** with high availability and 5 partitions. 

```bash
<XAP-HOME>/bin/xap space deploy --ha --partitions=5 mySpace
```

{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- name : The name of the Space.

_Options:_<br>

- backups=true/false         : Should backups be used for high availability.
- partitions=\<partitions\>    : Number of partitions.
- requiresIsolation=true/false  :  Each instance should be provisioned in an isolated container.

*Example:*<br>
This example deploys a Space named **mySpace** with high availability and 3 partitions. 

```bash
  
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


#  To Un - Quiescie a PU