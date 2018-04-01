---
type: post123
title:  Viewing Space Runtime Information
categories: XAP123ADM,PRM
weight: 200
parent: admin-spaces-pu.html
---
 
 
 
 

**To view the Space runtime information:**

<br>

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Space**

*Command:*

`xap space list`
 
*Description:*

This command lists all the Spaces  in a table with the name, deployment name, topology and InstanceId information.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space list
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-list.png)

*Parameters and Options:*

None.


**Space Instance**

*Command:*

`xap space list-instances <name>`

*Description:*

The Space instances for the given Space are listed with ID, Mode, HostId, and ContainerId.

*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```


*Output Example:*

![image](/attachment_files/admin/cli-xap-space-instances-list.png)

*Parameters and Options:*

<table>
  <tr>
    <th>Item</th>
    <th>Name</th>
    <th>Description</th>
  </tr>
   <tr>
    <td>Parameter</td>
    <td>name</td>
    <td>Provide the name of the Space for which to list the instances</td>
    <td></td>
  </tr>
  <tr>
</table>


{{%/tab%}}


{{%tab "REST Manager API"%}}

**All Spaces**

*Path*

`GET /spaces`

*Description:*

The Spaces are listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/spaces'
```


*Example Response:*

```bash
[
  {
    "name": "monitorSpace",
    "processingUnitName": "monitorSpace",
    "topology": {
      "instances": 1
    },
    "instancesIds": [
      "monitorSpace~1"
    ]
  },
  {
    "name": "alertSpace",
    "processingUnitName": "alertSpace",
    "topology": {
      "instances": 1
    },
    "instancesIds": [
      "alertSpace~1"
    ]
  },
  {
    "name": "mySpace",
    "processingUnitName": "mySpace",
    "topology": {
      "instances": 1
    },
    "instancesIds": [
      "mySpace~1"
    ]
  }
]
```

*Options:*

None

**Specific Space**

*Path*

`GET /spaces/{id}/instances`

*Description:*

The given Space is listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/spaces/alertSpace/instances'
```
 
*Example Response:*

```bash
[
  {
    "id": "alertSpace~1",
    "mode": "PRIMARY",
    "partitionId": 0,
    "backupId": 0,
    "hostId": "admin",
    "containerId": "admin~13972"
  }
] 
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| space name | Provide the name of the Space for which you want to see the runtime details. | Yes |

 
**Specific Space Instance**

*Path*

`GET /spaces/{id}/instances{instanceId}`

*Description:*

The given Space instance is listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/spaces/alertSpace/instances/alertSpace~1'
```
 
*Example Response:*

```bash
{
  "id": "alertSpace~1",
  "mode": "PRIMARY",
  "partitionId": 0,
  "backupId": 0,
  "hostId": "admin",
  "containerId": "admin~13972"
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| space name | Provide the name of the Space for which you want to see the runtime details. | Yes |
| space Id | Provide the ID of the Space for which you want to see the runtime details. | Yes |

  
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
    <td>Used Heap (MB)</td>
    <td>Amount of memory currently being used, in MB and as a percentage of the total memory allocated to this Space.</td>
  </tr>
  <tr>
    <td>Used Off-Heap Cache (MB)</td>
    <td>(RocksDB storage driver only) Amount of off-heap memory currently being used for caching, in MB and as a percentage of the total off-heap memory allocated.</td>
  </tr>
  <tr>
    <td>Used Off-Heap (MB)</td>
    <td>(Off-heap storage driver only) Amount of off-heap memory currently being used, in MB and as a percentage of the total memory allocated for the blobstore.</td>
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
    <td>Number of currently connected clients.</td>
  </tr>
  <tr>
    <td>Active Transactions</td>
    <td>Number of transactions that are now active.</td>
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
    <td>Number of currently connected clients.</td>
  </tr>
  <tr>
    <td>Active Transactions</td>
    <td>Number of transactions that are now active.</td>
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

