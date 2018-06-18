---
type: post123
title:  Viewing Space Statistics
categories: XAP123ADM,PRM
weight: 900
parent: admin-spaces-pu.html
---
 
You can use any of the administration tools to view operations statistics for a Space. If you have a system with MemoryXtend, you can access cache and blobstore metrics via the Admin API, and you can view these statistics in the Web Management Console and GigaSpaces Management Center.
 

<br>

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Statistics for Space operations**

*Command:*

`xap space info --operation-stats <name>`
 
*Description:*

This command lists the statistics for Space operations: object count, write/read/take/execute count, etc.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info --operation-stats mySpace
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-stats.png)

*Parameters and Options:*

|Item | Name| Description |
|:----|:----|:------------|
|Parameter | name |Provide the name of the Space for which you want to display the operation statistics. |
 
 
**Statistics for Space instance operations**

*Command:*

`xap space info-instance --operation-stats <instance ID>`
 
*Description:*

This command lists the statistics for Space instance operations: object count, write/read/take/execute count, etc.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instance --operation-stats mySpace~1_2
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-stats-instance.png)

*Parameters and Options:*

|Item | Name| Description |
|:----|:----|:------------|
|Parameter | instance ID |Provide the instance ID of the Space instance for which you want to display the operation statistics. |
 
 
 
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

 
**Statistics for Space operations**

*Path*

`GET /spaces/{id}/statistics/operations`

*Description:*

This option lists the statistics for Space operations: object count, write/read/take/execute count, etc.

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
| space name | Provide the name of the Space for which you want to see the operation statistics. | Yes |

**Statistics for Space instance operations**

*Path*

`GET /spaces/{id}/instances/{instanceId}/statistics/operations`

*Description:*

This option lists the statistics for Space instance operations: object count, write/read/take/execute count, etc.

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
| space name | Provide the name of the Space instance for which you want to see the operation statistics. | Yes |
| instanceId| Provide the instance ID of the Space instance for which you want to see the operation statistics. | Yes |
 
{{%/tab%}}


{{%tab "Web Management Console"%}}

In the Web Management Console, you can view general operation statistics for a Space. If you have a system with MemoryXtend, you can also view cache and blobstore statistics.

**To view operation statistics:**

1. In the Spaces view, highlight the Space or the Space instance you want to investigate.
1. Click **Statistics** in the upper row of options, and then click **Operations** to display the operation statistics pane in the lower area of the view. 
1. The throughput statistics timeline only shows information that is collected in real time. Click **Stop** to stop it, and **Start** to start it running again.
1. To see the exact value of a specific operation for a specific point in time, hover the mouse over the graph for that time. The information is displayed in a tooltip.
1. The Operations Count graph displays a rough estimate of the number of various operations to the Space (or Space instance). To see the exact number of a particular operation, hover the mouse over the relevant graph bar. The exact number of operations is shown in a tooltip.

 {{% note "Note"%}}
 The operation statistics function doesn’t save past events. If you stop the timeline for a length of time, you will lose the statistics that might have been gathered during that time. When you start the timeline again, it jumps to the current time.
 {{% /note %}}
 
 **To view MemoryXtend statistics:**
 
1. In the Spaces view, double-click the MemoryXtend Space instance for which you want to view statistics. This will drill through to the page that provides access to the Space instance details.
1. Click **Statistics** in the upper row of options, and then click **MemoryXtend** to display the MemoryXtend statistics for the Space instance.

The following statistics are displayed for off-heap storage:

<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Used Cache Size</td>
		<td>Amount of off-heap cache memory currently being used, in MB.</td>
	</tr>
	<tr>
		<td>Cache Miss</td>
		<td>How many times the data queried was retrieved from the off-heap cache.</td>
	</tr>
	<tr>
		<td>Cache Hit</td>
		<td>How many times the data queried was retrieved from the on-heap storage.</td>
	</tr>
	<tr>
		<td>Hot Data Cache Miss</td>
		<td>How many times the data queried was retrieved from the off-heap cache because the on-heap storage was full.</td>
	</tr>
	<tr>
		<td>Used Off-Heap Cache</td>
		<td>Amount of off-heap memory currently being used, in MB and as a percentage of the total memory allocated for the blobstore.</td>
	</tr>
	</table>
	
The following statistics are displayed for disk storage:

<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>MemTable Hits</td>
		<td>Number of MemTable hits.</td>
	</tr>
	<tr>
		<td>MemTable Misses</td>
		<td>Number of MemTable misses (data was not present in the MemTable).</td>
	</tr>
	<tr>
		<td>Get Queries Served by L0</td>
		<td>Number of Get queries that were served by the L0 layer of the RocksDB.</td>
	</tr>
	<tr>
		<td>Get Queries Served by L1</td>
		<td>Number of Get queries that were service by the L1 layer of the RocksDB.</td>
	</tr>
	<tr>
		<td>Get Queries Served by L2 and Up</td>
		<td>Number of queries that were served by the L2 layer or deeper of the RocksDB.</td>
	</tr>
	<tr>
		<td>Keys Written</td>
		<td>Number of keys that were written to the RocksDB.</td>
	</tr>
	<tr>
		<td>Keys Read</td>
		<td>Number of keys that were read from the RocksDB.</td>
	</tr>
	<tr>
		<td>Keys Updated</td>
		<td>(If in-place update is enabled) Number of keys that were updated in the RocksDB.</td>
	</tr>
	<tr>
		<td>Bytes Sent</td>
		<td>Number of uncompressed bytes issued by the following RocksDB operations: Put, Delete, Merge, Write.</td>
	</tr>
	<tr>
		<td>Bytes Received via Get</td>
		<td>Number of uncompressed bytes read from the RocksDB Get operation.</td>
	</tr>
	<tr>
		<td>Bytes Received via Iterator</td>
		<td>Number of uncompressed bytes read from an iterator (initial load).</td>
	</tr>
	<tr>
		<td>MultiGet Calls</td>
		<td>Number of MultiGet calls.</td>
	</tr>
	<tr>
		<td>Keys Read via MultiGet</td>
		<td>Number of keys read via MultiGet calls.</td>
	</tr>
	<tr>
		<td>Bytes Received via MultiGet</td>
		<td>Number of bytes received via MultiGet calls.</td>
	</tr>
	</table>

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}
{{% /tabs %}}
  