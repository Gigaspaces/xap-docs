---
type: post123
title:  Viewing Network Details
categories: XAP123ADM,PRM
weight: 1200
canonical: auto
parent: admin-spaces-pu.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface or REST Manager API administration tools.
{{%/note%}}
 
**To view Network Details details:**

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

1. In the Spaces view, highlight the Space or Space instance for which you want to see the network  details.
1. Display the Network  pane in the lower area of the view.
1. To start receiving information about network activity in the Network pane, check the Enable the **Network Activity Monitoring** check box.
1. You can view the following network information:
	* For inbound activity:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Space Instance Name</td>
		<td>Name of the Space instance.</td>
	</tr>
	<tr>
		<td>PID</td>
		<td>Process ID of the Space instance.</td>
	</tr>
	<tr>
		<td>Host IP</td>
		<td>IP address of the Space instance’s host.</td>
	</tr>
	<tr>
		<td>Receiv. Traffic (MB)</td>
		<td>Total amount of received traffic, in MB.</td>
	</tr>
	<tr>
		<td>Generated Traffic (MB)</td>
		<td>Total amount of outgoing traffic that was generated due to invocation, in MB.</td>
	</tr>
	<tr>
		<td>Total Traffic (MB)</td>
		<td>Total amount of both received and generated traffic, in MB.</td>
	</tr>
	</table>
	* For notification activity:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>GSC</td>
		<td>Name of the container.</td>
	</tr>
	<tr>
		<td>PID</td>
		<td>Process ID of the container.</td>
	</tr>
	<tr>
		<td>Host IP</td>
		<td>IP address of the container’s host.</td>
	</tr>
	<tr>
		<td>Receiv. Traffic (MB)</td>
		<td>Total amount of received traffic, in MB.</td>
	</tr>
	<tr>
		<td>Generated Traffic (MB)</td>
		<td>Total amount of outgoing traffic that was generated due to invocation, in MB.</td>
	</tr>
	<tr>
		<td>Total Traffic (MB)</td>
		<td>Total amount of both received and generated traffic, in MB.</td>
	</tr>
	<tr>
		<td>Invoc. Count</td>
		<td>Number of invocations.</td>
	</tr>
	</table>

1. To view additional information about client connections and network traffic, double click a Space instance in the lower pane. The following information is displayed:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Name</td>
		<td>IP address and Process ID of the connected client, along with a list of the invoked methods.</td>
	</tr>
	<tr>
		<td>Version</td>
		<td>Client version.</td>
	</tr>
	<tr>
		<td>Invoc.</td>
		<td>For the client, the total number of invocations. For the methods, the individual number of invocations.</td>
	</tr>
	<tr>
		<td>Receiv. </td>
		<td>Amount of received traffic, in MB (per client, and per invocation).</td>
	</tr>
	<tr>
		<td>Generated </td>
		<td>Amount of outgoing traffic that was generated due to invocation, in MB (per client, and per invocation).</td>
	</tr>
	<tr>
		<td>Total</td>
		<td>Total amount of both received and generated traffic  for that client, as a percentage of the overall traffic.</td>
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
  
