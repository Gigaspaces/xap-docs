---
type: post123
title:  Viewing Client-Side Cache Details
categories: XAP123ADM,PRM
weight: 1000
canonical: auto
parent: admin-spaces-pu.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface or REST Manager API administration tools.
{{%/note%}}  
 
**To view the client-side cache details:**


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

1. In the Spaces view, highlight the Space or Space instance for which you want to see the client-side cache details.
1. Display the Client-Side Caches pane in the lower area of the view.
1. You can view the following information:

* If you have one or more local cache(s) configured:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>ID</td>
    <td>ID of the local cache.</td>
  </tr>
  <tr>
    <td>PID</td>
    <td>Process ID of the master Space using the local cache instance.</td>
  </tr>
  <tr>
    <td>Host</td>
    <td>Machine hosting the local cache instance.</td>
  </tr>
  <tr>
    <td>Version</td>
    <td>Version of the data object that is stored the local cache.</td>
  </tr>
</table>


* If you have one or more local views configured:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Name</td>
    <td>Identifier of the local view.</td>
  </tr>
  <tr>
    <td>Host</td>
    <td>Machine hosting the local view.</td>
  </tr>
  <tr>
    <td>PID</td>
    <td>Process ID of the master Space using the local view.</td>
  </tr>
  <tr>
    <td>Version</td>
    <td>Version of the data object that is stored in the local view.</td>
  </tr>
  <tr>
    <td>Channel State</td>
    <td>Connection state of the network channel between the local view and the master Space:
	<ul>
		<li><b>Active</b> - The local view is connected to the master Space and the data is in sync.</li>
		<li><b>Disconnected</b> - The connection to the master Space was briefly disrupted.</li>
		<li><b>Inactive</b> - The connection to the master Space was disrupted long enough for the local view to become inactive and start throwing exceptions.</li>
		<li><b>Closed</b> - The local view is permanently closed.</li>
	</ul>
 </td>
  </tr>
  <tr>
    <td>Redo Log Retain. Size</td>
    <td>Size of the redo log (number of packets).</td>
  </tr>
  <tr>
    <td>Send Bytes TP</td>
    <td>Throughput of the sent bytes, in KB per second.</td>
  </tr>
  <tr>
    <td>Send Packets/Sec</td>
    <td>Rate of packet throughput per second.</td>
  </tr>
  <tr>
    <td>Templates</td>
    <td>How many templates are associated with this local view.</td>
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

  
