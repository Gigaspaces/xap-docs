---
type: post
title:  Viewing Space Statistics
weight: 1300
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To view Space Statistics:**

  
{{%tabs%}}
{{%tab "Command Line Interface"%}}

***Display Space Statistics***

_Parameters:_<br> 

- name : The name of the Space.

_Options:_<br>

- ---operation-stats : Displays Space operations statistics, (read, write, take etc)  <br>
- ---type-stats      : Displays Space object information.
 

*Example:*

```bash
<XAP-HOME>/bin/xap space info --type-stats mySpace
```
 
***Display Space Instance Statistics***

_Parameters:_<br> 
instanceId : The id of the Space instance to use.

_Options:_<br>

- ---operation-stats : Displays Space instance operations statistics, (read, write, take etc)  <br>
- ---type-stats      : Displays Space instance object information.<br>
- ---replication-stats: Display Space instance replication information.
 
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instances --replication-stats mySpace~1
```
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

***Display Space Statistics***

_Parameters:_<br>

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the Space.

 
Displays Space operations statistics, (read, write, take etc)  <br>
  

*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/statistics/operations'
```
 
***Display Space Instance Statistics***

_Parameters:_<br> 

- host URL: Host URL where the REST Manager is running.<br>
- name : The name of the Space.<br>
- instanceId : The id of the Space instance to use.

_Options:_<br>

- operations : Displays Space instance operations statistics, (read, write, take etc)  <br>
- types     : Displays Space instance object information.<br>
- replication: Display Space instance replication information.
 
 
*Examples:*
 
```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/operations'
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/replication'
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/types'
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
  