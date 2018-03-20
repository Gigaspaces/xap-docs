---
type: post123
title:  Viewing Space Statistics
categories: XAP123ADM,PRM
weight: 900
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

 
**To view Space operations:**

*Path*

`GET /spaces/{id}/statistics/operations`

*Description:*

Display the Space operations statistics.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/spaces/alertSpace/statistics/operations'
```
 
*Example Response:*

```bash
{
  "size": 1,
  "timestamp": 1521368755460,
  "writeCount": 0,
  "writePerSecond": 0.00026335180482892404,
  "readCount": 0,
  "readPerSecond": 0.0005267036096578481,
  "takeCount": 0,
  "takePerSecond": 0.0005267036096578481,
  "updateCount": 0,
  "updatePerSecond": 0.00026335180482892404,
  "changeCount": 0,
  "changePerSecond": 0,
  "notifyRegistrationCount": 0,
  "notifyRegistrationPerSecond": 0.00026335180482892404,
  "notifyTriggerCount": 0,
  "notifyTriggerPerSecond": 0.00026335180482892404,
  "notifyAckCount": 0,
  "notifyAckPerSecond": 0.00026335180482892404,
  "executeCount": 0,
  "executesPerSecond": 0.00026335180482892404,
  "objectCount": 0,
  "notifyTemplateCount": 0,
  "activeConnectionCount": 0,
  "activeTransactionCount": 0
}
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| space name | Provide the name of the Space for which you want to see the details. | Yes |

**To view Space Instance data types:**

*Path*

`GET /spaces/{id}/instances/{instanceId}/statistics/operations`

*Description:*

Display the Space Instance operations statistics.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/spaces/alertSpace/instances/alertSpace~1/statistics/operations'
```
 
*Example Response:*

```bash
{
  "adminTimestamp": 1521368888174,
  "timestamp": 1521368888174,
  "writeCount": 0,
  "writePerSecond": 0,
  "readCount": 0,
  "readPerSecond": 0,
  "takeCount": 0,
  "takePerSecond": 0,
  "updateCount": 0,
  "updatePerSecond": 0,
  "changeCount": 0,
  "changePerSecond": 0,
  "notifyRegistrationCount": 0,
  "notifyRegistrationPerSecond": 0,
  "notifyTriggerCount": 0,
  "notifyTriggerPerSecond": 0,
  "notifyAckCount": 0,
  "notifyAckPerSecond": 0,
  "executeCount": 0,
  "executesPerSecond": 0,
  "objectCount": 0,
  "notifyTemplateCount": 0,
  "activeConnectionCount": 0,
  "activeTransactionCount": 0
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| space name | Provide the name of the Space for which you want to see the runtime details. | Yes |
| instanceId| Provide the instance Id of the Space for which you want to see the runtime details. | Yes |
 
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
  