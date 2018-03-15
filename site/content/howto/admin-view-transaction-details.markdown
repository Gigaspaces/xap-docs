---
type: post
title:  Viewing Transaction Details
weight: 1700
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

 


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
		<td></td>
	</tr>
	<tr>
		<td>Locked Objects</td>
		<td>Number of locked data objects in the transaction.</td>
	</tr>
	</table>


* Per individual transaction (when drilling down):

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>UID</td>
		<td></td>
	</tr>
	<tr>
		<td>Class Name</td>
		<td></td>
	</tr>
	<tr>
		<td>Operation Code</td>
		<td></td>
	</tr>
	<tr>
		<td>Lock Type</td>
		<td></td>
	</tr>
	</table>

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}
  