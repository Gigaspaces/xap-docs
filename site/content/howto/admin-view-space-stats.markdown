---
type: post
title:  Viewing Space Statistics
weight: 1300
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}<br>

<br>

**To view Space statistics:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Display Space Statistics**

*Command:*

`xap space info --operation-stats <space name>`
 
*Description:*

This command lists all the Spaces  in a table with the name, deployment name, topology and InstanceId information.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info --operation-stats alertSpace
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-stats.png)

*Parameters and Options:*

|Item | Name| Description | Comment |
|:----|:----|:------------|:--------|
|Parameter | \<name\> |Name of the Space to display the statistics for ||
 
 

**Display Space Instance Statistics**

*Command:*

`xap space info-instance --operation-stats <space ID>`
 
*Description:*

This command lists all the Spaces  in a table with the name, deployment name, topology and InstanceId information.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instance --operation-stats alertSpace~1
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-stats-instance.png)

*Parameters and Options:*

|Item | Name| Description | Comment |
|:----|:----|:------------|:--------|
|Parameter | \<spaceId\> |Id of the Space to display the statistics for ||
 
 
 
 
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

1. In the Spaces view, highlight the Space or the Space instance you want to investigate.
1. Display the Statistics pane in the lower area of the view.
1. The throughput statistics timeline only shows information that is collected in real time. Click **Stop** to stop it, and **Start** to start it running again.
1. To see the exact value of a specific operation for a specific point in time, hover the mouse over the graph for that time. The information is displayed in a tooltip.
1. The Operations Count graph displays a rough estimate of the number of various operations to the Space (or Space instance). To see the exact number of a particular operation, hover the mouse over the relevant graph bar. The exact number of operations is shown in a tooltip.

 {{% note "Note"%}}
 The statistics function doesn’t save past events. If you stop the timeline for a length of time, you will essentially lose the statistics that might have been gathered during that time. When you start the timeline again, it jumps to the current time.
 {{% /note %}}

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}
{{% /tabs %}}
  