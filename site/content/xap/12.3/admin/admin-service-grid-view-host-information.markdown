---
type: post123
title:  Viewing Host Information
categories: XAP123ADM,PRM 
weight: 100
parent: admin-service-grid.html
---
 
  

 

 
 

**To view host information:**
<br>

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**List all Hosts**

*Command:*

`xap host list`  

*Description:*
 
Lists all host machines.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host list 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-list.png)

*Parameters and Options:*

None


**List Containers per host**

*Command:*

`xap host info --containers <host name>`  

*Description:*
 
Lists all containers on a host machines.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --containers localhost 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-containers.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | host name |Name of the host |


**List Operating System Information**

*Command:*

`xap host info --os <host name>`  

*Description:*
 
Lists all Operating System details for a host machines.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --os localhost 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-os.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | host name |Name of the host |


**List Operating System Statistics**

*Command:*

`xap host info --os-stats <host name>`  

*Description:*
 
Lists all Operating System Statistics for a host machines.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host info --os-stats localhost 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-host-os-stats.png)

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | host name |Name of the host |

{{%/tab%}}


{{%tab "REST Manager API"%}}

**To view all hosts**

*Path*

`GET /hosts`

*Description:*

List all hosts.

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


**To view host specific information**

*Path*

`GET /hosts{id}`

*Description:*

List host information.

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
| host name | Provide the host name  for which you want to see the details. | Yes |

**To view host containers**

*Path*

`GET /hosts{id}/containers`

*Description:*

List host container information.

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
| host name | Provide the host name  for which you want to see the details. | Yes |


**To view host os details**

*Path*

`GET /hosts{id}/details/os`

*Description:*

List host  detail information.

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
| host name | Provide the host name  for which you want to see the details. | Yes |

**To view host statistics details**

*Path*

`GET /hosts{id}/statistcics/os`

*Description:*

List host statistics detail information.

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
| host name | Provide the host name  for which you want to see the details. | Yes |



{{%/tab%}}


{{%tab "Web Management Console"%}}
 
The Hosts view provides a general overview of the host machines, Processing Units, and Spaces, and relevant containers.. 

{{%note "Note"%}}
Container information is explained in the [Viewing Container Information](/admin-service-grid-container-information.html) topic.
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
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}

