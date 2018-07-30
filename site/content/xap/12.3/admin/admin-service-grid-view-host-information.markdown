---
type: post123
title:  Viewing Host Information
categories: XAP123ADM,PRM 
weight: 100
parent: admin-service-grid.html
---
 
  
A host is discovered by having a [Grid Service Agent](./admin-service-grid-agent.html) running on it.


**To view host information:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

**List all hosts**

*Command:*

`xap host list`  

*Description:*
 
Lists all the host machines.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host list
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-list.png)

*Parameters and Options:*

None

**List containers per host**

*Command:*

`xap host info --containers <name>`

*Description:*
 
Lists all the containers on a specific host machine.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --containers localhost
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-containers.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | name | Provide the name (or IP address) of the host for which you want to see the list of containers. |


**List host operating system information**

*Command:*

`xap host info --os <name>`

*Description:*
 
Lists all the operating system and related host machine details for a specific host machine, such as OS type, host architecture, OS version, etc.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --os localhost
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-os.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | name | Name (or IP address) of the host for which you want to see the operating system information. |


**List host operating system statistics**

*Command:*

`xap host info --os-stats <name>`

*Description:*
 
Lists several operating system statistics for a specific host machine, such as free physical memory, memory used, and free swap space.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --os-stats localhost
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-os-stats.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | name | Name (or IP address) of the host for which you want to see the operating system statistics. |

{{%/tab%}}


{{%tab "REST Manager API"%}}

**List all hosts**

*Path*

`GET /hosts`

*Description:*

This option lists the name, IP address, and containers for all the hosts in the service grid.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/hosts'
```
 
*Example Response:*

```bash
[
  {
    "name": "admin",
    "address": "172.20.3.93",
    "containers": [
      "admin~13972"
    ]
  }
]
```

*Options:*

None.


**View specific host**

*Path*

`GET /hosts{id}`

*Description:*

This option lists the name, IP address, and containers for a specific host.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/hosts/admin'
```
 
*Example Response:*

```bash
{
  "name": "admin",
  "address": "172.20.3.93",
  "containers": [
    "admin~13972"
  ]
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host name | Provide the host name for which you want to see the general details. | Yes |

**List containers per host**

*Path*

`GET /hosts{id}/containers`

*Description:*

This option lists the Host ID, Process ID, zones, and Processing Unit instances for containers on a specific host.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/hosts/admin/containers'
```
 
*Example Response:*

```bash
[
  {
    "id": "admin~13972",
    "pid": 13972,
    "zones": [],
    "instances": [
      "alertSpace~1",
      "monitorSpace~1"
    ]
  }
]
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host name | Provide the host name for which you want to see the container information. | Yes |


**List host operating system information**

*Path*

`GET /hosts{id}/details/os`

*Description:*

This option lists the container information for a specific host.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/hosts/admin/details/os'
```
 
*Example Response:*

```bash
{
  "id": "172.20.3.93",
  "name": "Win32",
  "timeDelta": 0,
  "currentTimeInMillis": 1521375407792,
  "arch": "x64",
  "version": "6.3",
  "availableProcessors": 8,
  "totalSwapSpaceSizeInBytes": 45535817728,
  "totalPhysicalMemorySizeInBytes": 34261528576,
  "hostName": "admin",
  "hostAddress": "172.20.3.93",
  "vendor": "Microsoft",
  "vendorCodeName": "Vienna",
  "vendorName": "Windows 7",
  "vendorVersion": "7"
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host name | Provide the host name for which you want to see the operating system information. | Yes |

**List host operating system statistics**

*Path*

`GET /hosts{id}/statistcics/os`

*Description:*

This option lists certain operating system statistics, such as free memory and used memory, for the specified host machine.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/hosts/admin/statistics/os'
```
 
*Example Response:*

```bash
{
  "timestamp": 1521375496115,
  "adminTimestamp": 1521375496115,
  "freeSwapSpaceSizeInBytes": 28697755648,
  "freePhysicalMemorySizeInBytes": 19815034880,
  "actualFreePhysicalMemorySizeInBytes": 20070580224,
  "physicalMemoryUsedPerc": 42.165350748885395,
  "cpuPerc": 0.06905130613591937,
  "cpuPercFormatted": "6.9%",
  "actualMemoryUsed": 14446493696
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host name | Provide the host name for which you want to see the operating system statistics. | Yes |



{{%/tab%}}


{{%tab "Web Management Console"%}}
 
The Hosts view provides a general overview of the host machines, Processing Units, and Spaces, and relevant containers.. 

{{%note "Note"%}}
Container information is explained in the [Viewing Container Information](./admin-service-grid-container-information.html) topic.
{{%/note%}}

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td colspan="2"><i>Host</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the host machine.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Indicator of how much CPU is being used, in %.</td>
  </tr>
  <tr>
    <td>Memory Utiliz. (GB)</td>
    <td>Indicator of how much of the host memory is being used, in both GB and %.</td>
  </tr>
  <tr>
    <td>Grid Services</td>
    <td>List of service grid components that are up and running (Grid Service Agent and containers).</td>
  </tr>
  <tr>
    <td>Processing Units</td>
    <td>List of Processing Units that are deployed on the host (with the number of running instances).</td>
  </tr>
  <tr>
    <td>Primaries & Backups</td>
    <td>Number of running Processing Unit instances according to primary and backup status.</td>
  </tr>
  <tr>
    <td colspan="2"><i>Grid Service Agent</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the Grid Service Agent.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Indicator of how much CPU is being used, in %.</td>
  </tr>
  <tr>
    <td>Used Heap (MB)</td>
    <td>Indicator of how much heap memory the Grid Service Agent is utilizing, in both MB and %.</td>
  </tr>
  <tr>
    <td>Threads</td>
    <td>How many threads the Grid Service Agent has open.</td>
  </tr>
  <tr>
    <td colspan="2"><i>Space</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the Space.</td>
  </tr>
  <tr>
    <td>Type</td>
    <td>Type of Space (stateful or stateless).</td>
  </tr>
  <tr>
    <td colspan="2"><i>Space Instance</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the Space instance.<br>		
	For additional information about the Space instance, you can drill through to the Spaces view via the Actions menu.</td>
  </tr>
</table>

Additionally, you can view the following information from the Hosts tab in the Processing Units view:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td colspan="2">(status icon)</td>
    <td></td>
  </tr>
  <tr>
    <td>OS</td>
    <td>Icon that indicates which operating system is running on the host machine.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Indicator of how much CPU is being used, in %.</td>
  </tr>
  <tr>
    <td>Memory (GB)</td>
    <td>Indicator of how much of the host memory is being used, in both GB and %.</td>
  </tr>
  <tr>
    <td>Core CPUs</td>
    <td>Number of cores in the CPU.</td>
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

