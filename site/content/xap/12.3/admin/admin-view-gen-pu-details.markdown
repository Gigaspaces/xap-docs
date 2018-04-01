---
type: post123
title:  Viewing the Deployed Processing Units
categories: XAP123ADM,PRM
weight: 500
parent: admin-spaces-pu.html
---
 
 
**To view information about the deployed Processing Units:**
<br>

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Processing Unit**

*Command:*

`xap pu list`

*Description:*

This command lists all the Processing Units in a table with the name, deployment name, topology, status, instance count and additional information.

*Input Example:*

```bash
xap pu list
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-pu-list.png)

*Parameters and Options:*

None.

**Processing Unit instance**

*Command:*

`xap pu list-instances <name>`
 
*Description:*

This command lists all of the instances for a given Processing Unit, along with the Instance ID, Host ID, and Container ID information.

 
*Input Example:*
 
```bash
xap pu list-instances data-feeder
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-pu-list-instances.png)


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
    <td>Name of Processing Unit to list instances for.</td>
    <td></td>
  </tr>
  <tr>
</table>


{{%/tab%}}


{{%tab "REST Manager API"%}}

**All Processing Units**

*Path*

`GET /pus`

*Description:*

The Processing Units are listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/pus'
```


*Example Response:*

```bash
[
  {
    "name": "monitorSpace",
    "processingUnitType": "stateful",
    "resource": "datagrid",
    "topology": {
      "instances": 1
    },
    "sla": {
      "requiresIsolation": false,
      "zones": [],
      "maxInstancesPerVM": 0,
      "maxInstancesPerMachine": 0
    },
    "spaces": [
      "monitorSpace"
    ],
    "scalable": false,
    "status": "intact",
    "quiesceDetails": {
      "quiesced": false,
      "description": "initial"
    },
    "instances": [
      "monitorSpace~1"
    ]
  },
  {
    "name": "alertSpace",
    "processingUnitType": "stateful",
    "resource": "datagrid",
    "topology": {
      "instances": 1
    },
    "sla": {
      "requiresIsolation": false,
      "zones": [],
      "maxInstancesPerVM": 0,
      "maxInstancesPerMachine": 0
    },
    "spaces": [
      "alertSpace"
    ],
    "scalable": false,
    "status": "intact",
    "quiesceDetails": {
      "quiesced": false,
      "description": "initial"
    },
    "instances": [
      "alertSpace~1"
    ]
  }
]
```

*Options:*

None

**Specific Processing Unit**

*Path*

`GET /pus/{id}`

*Description:*

The given Processing Unit is listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/pus/alertSpace'
```
 
*Example Response:*

```bash
{
  "name": "alertSpace",
  "processingUnitType": "stateful",
  "resource": "datagrid",
  "topology": {
    "instances": 1
  },
  "sla": {
    "requiresIsolation": false,
    "zones": [],
    "maxInstancesPerVM": 0,
    "maxInstancesPerMachine": 0
  },
  "spaces": [
    "alertSpace"
  ],
  "scalable": false,
  "status": "intact",
  "quiesceDetails": {
    "quiesced": false,
    "description": "initial"
  },
  "instances": [
    "alertSpace~1"
  ]
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit for which you want to see the runtime details. | Yes |


**Processing Unit Instance**

*Path*

`GET /pus/{id}/instances/{instanceId`

*Description:*

The given Processing Unit Instance is listed with the name, deployment name, topology, number of instances, and InstanceId.

*Example Request:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/pus/alertPu/instances/alertPu~1'
```
 
*Example Response:*

```bash
{
  "id": "alertPu~1",
  "processingUnitName": "alertPu",
  "hostId": "admin",
  "containerId": "admin~13972",
  "partitionId": 0,
  "backupId": 0
}
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit for which you want to see the runtime details. | Yes |
| instanceId | Provide the instanceId of the Processing Unit for which you want to see the runtime details. | Yes |

  
 
{{%/tab%}}


{{%tab "Web Management Console"%}}
 
You can see the following Processing Unit details in the main Processing Units view:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the Processing Unit. Expand the Processing Unit node to view the name of the Processing Unit instance, and below that the Space name.</td>
  </tr>
  <tr>
    <td>Status</td>
    <td>Current status of the Processing Unit:
	<ul>
			<li><b>Intact</b> - the Processing Unit was successfully deployed and is operating normally.</li>
			<li><b>Scheduled</b> - the Processing Unit was not yet successfully deployed, but an additional attempt will be made.</li>
			<li><b>Compromised</b> - the Processing Unit is no longer intact, but has not yet completed the undeploy process.</li>
			<li><b>Broken</b> - the Processing Unit could not be deployed.</li>
		</ul>
	</td>
  </tr>
  <tr>
    <td>Type</td>
    <td>Indicates whether the Processing Unit is stateful or stateless.</td>
  </tr>
  <tr>
    <td>Host Name</td>
    <td>Name of the machine hosting the Processing Unit instance.</td>
  </tr>
  <tr>
    <td>PID</td>
    <td>Process ID of the Processing Unit instance.</td>
  </tr>
  <tr>
    <td>Application</td>
    <td>Client application that is running, if applicable.</td>
  </tr>
  <tr>
    <td>Zones</td>
    <td>If configured, the zone where the Processing Unit is located.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Amount of CPU resources being used by the Processing Unit instance, in percent.</td>
  </tr>
  <tr>
    <td>Used Heap</td>
    <td>Amount of JVM heap being used by the Processing Unit instance, in MB (% of allocated heap).</td>
  </tr>
</table>


If the Space is highlighted, you can click the **Actions** icon and drill through to the Spaces view to see the Space details.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}

Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.

{{%/tab%}}

{{% /tabs %}}

