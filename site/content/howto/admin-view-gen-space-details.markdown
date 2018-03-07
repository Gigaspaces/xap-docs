---
type: post
title:  Viewing Space Runtime Information
weight: 200
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic. Include a short description of what info is available per admin tool, i.e. which tool to use depending on what you want to achieve. Also explain the difference between the info that you see for this task, vs. the info that you see in the next task (Viewing Space Information).{{% /bgcolor %}}

<br>

**To view the Space runtime information:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Space**

Type `ie space list` or `xap space list`. The Spaces are listed with the name, deployment name, topology, and InstanceId.

*Example:*

{{% bgcolor yellow %}}Not sure we need the syntax example. Add a text capture of the list of Spaces.{{% /bgcolor %}}

```bash
<XAP-HOME>/bin/xap space list
```


**Space instance**

Type `ie space list-instance` or `xap space list-instance`. The following parameter is displayed: 

_Parameters:_<br> 

- name : The name of the Space.
 
Type `ie space list-instance <name>` or `xap space list-instance <name>`  The Space instances for the given Space are listed with ID, Mode, PartionId, BackupId, HostId, and ContainerId.
 
 *Example:*
 
 {{% bgcolor yellow %}}Not sure we need the syntax example. Add a text capture of the list of Space instances.{{% /bgcolor %}}
 
```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```

{{%/tab%}}


{{%tab "REST Manager API"%}}
***List all Spaces***<br>
Lists all Spaces with the Name, Deployment name, Topology and InstanceId

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.

*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces'
```

***List all Space instances***<br>
Lists all Space instances for a given Space with Id, Mode, PartionId, BackupId, HostId and ContainerId.

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the Space.
 
*Example:*
 
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces/mySpace/instances'
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
    <td>Name of the Space.</td>
  </tr>
  <tr>
    <td>Processing Unit</td>
    <td>Name of the associated Processing Unit.</td>
  </tr>
  <tr>
    <td>Application</td>
    <td>Name of the client application using the Space.</td>
  </tr>
  <tr>
    <td>Actual Instances</td>
    <td>Number of Space instances in the cluster.</td>
  </tr>
  <tr>
    <td>SLA</td>
    <td>Space cluster topology (number of instances, number of backups).</td>
  </tr>
  <tr>
    <td>Total Memory (MB)</td>
    <td>Amount of memory currently being used, in MB and as a percentage of the total memory allocated to this Space.</td>
  </tr>
  <tr>
    <td>Entries</td>
    <td>Number of data entries to the Space.</td>
  </tr>
  <tr>
    <td>Notify Templates</td>
    <td>Number of Notify templates for this Space.</td>
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
    <td>Name of the Space instance in the cluster.</td>
  </tr>
  <tr>
    <td>PU Instance Name</td>
    <td>Name of the Processing Unit instance where the Space instance is located.</td>
  </tr>
  <tr>
    <td>PID</td>
    <td>Process ID of the Space instance.</td>
  </tr>
  <tr>
    <td>Host IP</td>
    <td>IP address of the host machine.</td>
  </tr>
  <tr>
    <td>Total Memory (MB)</td>
    <td>Amount of memory currently being used, in MB and as a percentage of the total memory allocated to this Space instance.</td>
  </tr>
  <tr>
    <td>Entries</td>
    <td>Number of data entries to the Space instance.</td>
  </tr>
  <tr>
    <td>Notify Templates</td>
    <td>Number of Notify templates for this Space instance.</td>
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


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

