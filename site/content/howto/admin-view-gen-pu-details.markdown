---
type: post
title:  Viewing General PU Details
weight: 500
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

**To view the general PU details:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

***List all PU's***<br>
Lists all Pu's with the Name, Deployment name, Topology and InstanceId

*Example:*

```bash
<XAP-HOME>/bin/xap pu list
```


***List all PU instances***<br>
Lists all PU instances for a given PU with Id, Mode, PartionId, BackupId, HostId and ContainerId.

_Parameters:_<br> 

- name : The name of the PU.
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```
{{%/tab%}}


{{%tab "REST Manager API"%}}
***List all PU's***<br>
Lists all PU's with the Name, Deployment name, Topology and InstanceId

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running, require.

*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/deployments'
```
***List all PU instances***<br>
Lists all PU instances for a given Space with Id, Mode, PartionId, BackupId, HostId and ContainerId.

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the PU.
 
*Example:*
 
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myPu'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}
 
You can see the following high-level Space details in the main Spaces view:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Space</td>
    <td>Name of the Space</td>
  </tr>
  <tr>
    <td>Processing Unit</td>
    <td>Name of the associated Processing Unit</td>
  </tr>
  <tr>
    <td>Application</td>
    <td>Name of the client application using the Space</td>
  </tr>
  <tr>
    <td>Actual Instances</td>
    <td>Number of Space instances in the cluster</td>
  </tr>
  <tr>
    <td>SLA</td>
    <td>Space cluster topology</td>
  </tr>
  <tr>
    <td>Total Memory (MB)</td>
    <td>Total amount of memory allocated to this Space</td>
  </tr>
  <tr>
    <td>Entries</td>
    <td>Number of data entries to the Space</td>
  </tr>
  <tr>
    <td>Notify Templates</td>
    <td>Number of Notify templates for this Space</td>
  </tr>
  <tr>
    <td>Connections</td>
    <td></td>
  </tr>
  <tr>
    <td>Active Transactions</td>
    <td></td>
  </tr>
</table>


Click the arrow in the right-hand column of the space entry to drill through to the following additional details:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Space Instance Name</td>
    <td>Name of the Space instance in the cluster</td>
  </tr>
  <tr>
    <td>PU Instance Name</td>
    <td>Name of the Processing Unit instance where the Space instance is located</td>
  </tr>
  <tr>
    <td>PID</td>
    <td>Process ID of the Space instance</td>
  </tr>
  <tr>
    <td>Host IP</td>
    <td>IP address of the host machine</td>
  </tr>
  <tr>
    <td>Total Memory (MB)</td>
    <td>Total amount of memory allocated to this Space instance</td>
  </tr>
  <tr>
    <td>Entries</td>
    <td>Number of data entries to the Space instance</td>
  </tr>
  <tr>
    <td>Notify Templates</td>
    <td>Number of Notify templates for this Space instance</td>
  </tr>
  <tr>
    <td>Connections</td>
    <td></td>
  </tr>
  <tr>
    <td>Active Transactions</td>
    <td></td>
  </tr>
</table> 

{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}

TBD

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

