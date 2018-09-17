---
type: post123
title:  Viewing Event Container Details
categories: XAP123ADM,PRM
weight: 1100
parent: admin-spaces-pu.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface or REST Manager API administration tools.
{{%/note%}}  
 
**To view the event container details:**


{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}
-->

{{%tab "Web Management Console"%}}

1. In the Spaces view, highlight the Space or Space instance for which you want to see the event container details.
1. Display the Event Container pane in the lower area of the view.

	OR
1. In the Processing Units view, click the **Actions **icon for the relevant Event Containers item, and select S**how in Spaces view**.

	The following information is displayed:

* For synchronous polling containers (**Polling**):

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Name</td>
		<td>Name of the container.</td>
	</tr>
	<tr>
		<td>Transactional</td>
		<td>If checked,  this container supports transactions.</td>
	</tr>
	<tr>
		<td>Max. Concur. Consumers per Partition</td>
		<td>Maximum number of concurrent consumer threads configured for this container.</td>
	</tr>
	<tr>
		<td>Receive Timeout per Partition</td>
		<td>Timeout configured for the polling container. </td>
	</tr>
	<tr>
		<td>Receive Handler</td>
		<td>Lists the receive operation handler implementations configured for the container. </td>
	</tr>
	<tr>
		<td>Pass Array As Is</td>
		<td>If checked, the container supports batch processing.</td>
	</tr>
	<tr>
		<td>Status</td>
		<td>Current state of the container.</td>
	</tr>
	<tr>
		<td>Processed</td>
		<td>Number of events processed.</td>
	</tr>
	<tr>
		<td>Failed</td>
		<td>Number of events that were not processed.</td>
	</tr>
	<tr>
		<td>Consumers</td>
		<td>Number of consumer threads currently active.</td>
	</tr>
	</table>

* For asynchronous polling containers (**Async Polling**):

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Name</td>
		<td>Name of the container.</td>
	</tr>
	<tr>
		<td>Transactional</td>
		<td>If checked,  this container supports transactions.</td>
	</tr>
	<tr>
		<td>Concur. Consumers per Partition</td>
		<td>Maximum number of concurrent consumer threads configured for this container.</td>
	</tr>
	<tr>
		<td>Receive Timeout per Partition</td>
		<td>Timeout configured for the polling container. </td>
	</tr>
	<tr>
		<td>Status</td>
		<td>Current state of the container.</td>
	</tr>
	<tr>
		<td>Processed</td>
		<td>Number of events processed.</td>
	</tr>
	<tr>
		<td>Failed</td>
		<td>Number of events that were not processed.</td>
	</tr>
	</table>

* For notify containers (**Notify**):

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Name</td>
		<td>Name of the container.</td>
	</tr>
	<tr>
		<td>Transactional</td>
		<td>If checked,  this container supports transactions.</td>
	</tr>
	<tr>
		<td>Batch Size per Partition</td>
		<td>Number of matching objects to accumulate per batch.</td>
	</tr>
	<tr>
		<td>Batch Time per Partition</td>
		<td>Maximum wait time between sending batches.</td>
	</tr>
	<tr>
		<td>Comm. Type</td>
		<td>N/A</td>
	</tr>
	<tr>
		<td>Take On Notify</td>
		<td>N/A</td>
	</tr>
	<tr>
		<td>FIFO</td>
		<td>If checked, the container is configured to run in FIFO mode.</td>
	</tr>
	<tr>
		<td>Notify Operations</td>
		<td>List of operations that generate notifications.</td>
	</tr>
	<tr>
		<td>Pass Array As Is</td>
		<td>If checked, the container supports batch processing.</td>
	</tr>
	<tr>
		<td>Status</td>
		<td>Current state of the container.</td>
	</tr>
	<tr>
		<td>Processed </td>
		<td>Number of events processed.</td>
	</tr>
	<tr>
		<td>Failed</td>
		<td>Number of events that were not processed.</td>
	</tr>
	</table>

To view the template for a specific container, double click the required container in the table.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}

{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}

  