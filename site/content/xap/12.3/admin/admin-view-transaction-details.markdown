---
type: post123
title:  Viewing Transaction Details
categories: XAP123ADM,PRM
weight: 1300
parent: admin-spaces-pu.html
---
 
 
**To view the transaction details:**
<br>
 


{{%tabs%}}
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}

Transaction information is only available for Space instances that are in primary mode.

1. In the Spaces view, highlight the Space or Space instance for which you want to see the transaction details.
1. Display the Transaction pane in the lower area of the view.
1. You can view the following transaction information:

* Per Space or Space instance:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>ID</td>
		<td>ID of the transaction.</td>
	</tr>
	<tr>
		<td>Type</td>
		<td>Transaction type. May be one of the following:
	Distributed:
	??</td>
	</tr>
	<tr>
		<td>Status</td>
		<td>Transaction status. Can be either Active or ??</td>
	</tr>
	<tr>
		<td>Start Time</td>
		<td>Time the transaction started.</td>
	</tr>
	<tr>
		<td>End Time</td>
		<td>Time the transaction completed.</td>
	</tr>
	<tr>
		<td>Timeout</td>
		<td>Length of time after which the transaction expires.</td>
	</tr>
	<tr>
		<td>Locked Objects</td>
		<td>Number of locked data objects in the transaction.</td>
	</tr>
	</table>


* Per individual transaction (when drilling down), when a transaction is blocked:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>UID</td>
		<td>ID of the data entry in the Space that is blocked.</td>
	</tr>
	<tr>
		<td>Class Name</td>
		<td>Type of data entry.</td>
	</tr>
	<tr>
		<td>Operation Code</td>
		<td>Data operation that was being peformed when the transaction became blocked.</td>
	</tr>
	<tr>
		<td>Lock Type</td>
		<td>Type of lock on the transaction.</td>
	</tr>
	</table>

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}
Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
  