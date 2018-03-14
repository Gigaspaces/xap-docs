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

### Space

**Command** 

`xap space list`

**Description** 

The Spaces are listed with the name, deployment name, topology, number of instances, and InstanceId.

![image](/attachment_files/admin/CLI-xap-space-list.png)

**Parameters and Options**

None.


### Space Instance

**Command**

`xap space list-instance`

**Description**

The Space instances for the given Space are listed with ID, Mode, PartionId, BackupId, HostId, and ContainerId.

*screen cap here*

**Parameters and Options**

<table>
  <tr>
    <th>Item</th>
    <th>Name</th>
    <th>Description</th>
    <th>Comment</th>
  </tr>
   <tr>
    <td>Parameter</td>
    <td>&lt;name&gt;</td>
    <td>Provide the name of the Space instance for which you want to see the runtime information, using the InstanceId. For example, <code>mySpace-space~2_1</code>.</td>
    <td></td>
  </tr>
  <tr>
</table>


{{%/tab%}}


{{%tab "REST Manager API"%}}

### All Spaces

**Path**

`GET /spaces`

**Description**

The Spaces are listed with the name, deployment name, topology, number of instances, and InstanceId.

**Options**

None

**Example**

*give example*

### Specific Space

**Path**

`GET /spaces/{id}`

**Description**

The given Space is listed with the name, deployment name, topology, number of instances, and InstanceId.

**Options**

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Provide the name of the Space for which you want to see the runtime details. | Yes |

**Example**

*give example*

{{% bgcolor yellow %}}not sure what this REST API is for.{{% /bgcolor %}}

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.

*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces'
```

### All Space Instances

**Path**

`GET /spaces/{id}/instances~

**Description**

The Space instances for the given Space are listed with ID, Mode, PartionId, BackupId, HostId, and ContainerId.

**Options**

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Provide the name of the Space for which you want to see the list of Space instances and their runtime details. | Yes |

**Example**

*give example*

{{% bgcolor yellow %}}not sure what this REST API is for.{{% /bgcolor %}}

_Parameters:_<br> 

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the Space.
 
*Example:*
 
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces/mySpace/instances'
```

### Specific Space Instance

**Path**

`GET GET /spaces/{id}/instances/{instanceId}

**Description**

The given Space instances is listed with ID, Mode, PartionId, BackupId, HostId, and ContainerId.

**Options**

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Provide the name of the Space for which you want to see the specific Space instance. | Yes |
| instanceId | Provide the name of the Space instance for which you want to see the runtime details. | Yes |

**Example**

*give example*


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

