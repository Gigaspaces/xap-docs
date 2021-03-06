---
type: post123
title:  Viewing Processing Unit Configuration Information
categories: XAP123ADM,PRM
weight: 600
canonical: auto
parent: admin-spaces-pu.html
---
 
 
**To view the Processing Unit configuration information:**
 

{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Processing Unit**

*Command:*

`xap pu info <name>`

*Description:*

Display detailed information for the given Processing Unit.
  
*Input Example:*

```bash
<XAP-HOME>/bin/xap pu info  myPu
```
 
*Output Example:*

![image](/attachment_files/admin/cli-xap-pu-info.png)
 
 
*Parameters and Options:*

|Item | Name| Description |
|:----|:----|:------------|
|Parameter | name |Name of the Processing Unit for which you want to view the details.|
 


**Processing Unit instance**

*Command:*

`xap pu info-instance <instance ID>`

*Description:*

Display detailed information for the given Processing Unit instance.
  
*Input Example:*

```bash
<XAP-HOME>/bin/xap pu info-instance mySpace~1_1
```
 
*Output Example:*

![image](/attachment_files/admin/cli-xap-pu-info-instance.png)
 
 
*Parameters and Options:*

|Item | Name| Description |
|:----|:----|:------------|
|Parameter | &lt;instance ID&gt; |Instance ID of the Processing Unit for which you want to view details.|
 


{{%/tab%}}

{{%tab "REST Manager API"%}}

**Specific Processing Unit**

*Path*

`GET /pus/{id}/instances`

*Description*

The Instance ID, Processing Unit name, Host ID, Container ID, Partition ID, and Backup ID are displayed for the specified Processing Unit.

*Example Request*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/pus/myPu/instances'
```
 
*Example Response*

```bash
{
  "id": "myPu~1",
  "processingUnitName": "myPu",
  "hostId": "gigaspaces.com",
  "containerId": "gigaspaces.com~16674",
  "partitionId": 0,
  "backupId": 0
}
```
 
*Options*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host URL | Provide the Host URL where the REST Manager is running. | Yes | 
| name | Provide the name of the Processing Unit for which you want to see the configuration details. | Yes |  
 
**Specific Processing Unit instance**

*Path*

`GET /pus/{id}/instances/{instanceId}`

*Description*

The Instance ID, deployment name, Host ID, Container ID, Partition ID, and Backup ID are displayed for the specified Processing Unit instance.

*Example Request*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v2/pus/myPu/instances/myPu~1'
```
 
*Example Response*

```bash
{
  "id": "myPu~1",
  "deploymentName": "myPu",
  "hostId": "gigaspaces.com",
  "containerId": "gigaspaces.com~16674",
  "partitionId": 0,
  "backupId": 0
}
```
 
*Options*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| host URL | Provide the Host URL where the REST Manager is running. | Yes | 
| name | Provide the name of the Processing Unit for which you want to see the configuration details. | Yes |  
| instanceId | Provide the Instance ID of the Processing Unit instance for which you want to see the configuration details. | Yes |

{{%/tab%}}


{{%tab "Web Management Console"%}}

To see specific details about a Processing Unit, Processing Unit instance, or Space in the Processing Unit view, highlight the required component and click the Config tab on the right.

## Processing Unit

The Config tab displays the general Processing Unit parameters as they were defined in the sla.xml file, and how they were actually deployed in runtime.

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Type</td>
    <td>Indicates whether the Processing Unit is stateful (contains a Space) or stateless (does not contain a Space).</td>
  </tr>
  <tr>
    <td>Application</td>
    <td>Client application that is running, if applicable.</td>
  </tr>
  <tr>
    <td>Number of Instances</td>
    <td>How many Processing Unit instances have been defined in the sla.xml.</td>
  </tr>
  <tr>
    <td>Number of Backups</td>
    <td>(For partitioned Processing units) How many backup Processing Unit instances have been defined in the sla.xml.</td>
  </tr>
  <tr>
    <td>Instances per VM</td>
    <td>Maximum number of Processing Unit instances that may be deployed per VM (if the hosts are virtual).</td>
  </tr>
  <tr>
    <td>Instances per Machine</td>
    <td>Maximum number of Processing Unit instances that may be deployed per machine (if the hosts are physical).</td>
  </tr>
  <tr>
    <td>Deployment Status</td>
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
    <td>Planned Instances</td>
    <td>How many Processing Unit instances should be deployed in total, based on the number of instances and number of backups configured in the sla.xml file.</td>
  </tr>
  <tr>
    <td>Running Instances</td>
    <td>How many Processing Unit instances have been deployed in runtime.</td>
  </tr>
  <tr>
    <td>Managing GSM</td>
    <td>Name, host name, and IP address of the managing Grid Service Manager that deployed the Processing Unit.</td>
  </tr>
</table>


## Processing Unit Instance

The Config tab displays each component that was configured in the pu.xml and its properties. For example, embedded Spaces, event containers (notify and/or polling), remote services, etc.

## Space

The Config tab displays general information about the Space configuration, along with network and environment information, and the configured memory management properties.

{{% note "Note"%}}
This information is also available in the Configuration pane of the Spaces view when you highlight a Space.
{{% /note %}}

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td colspan="2"><i>General</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Space Schema</td>
    <td>Type of schema (default, mirror, or persistent).</td>
  </tr>
  <tr>
    <td>Secured</td>
    <td>Whether or not this Space is secured.</td>
  </tr>
  <tr>
    <td>Persistent</td>
    <td>Whether or not this Space is persistent.</td>
  </tr>
  <tr>
    <td>Clustered</td>
    <td>Whether or not this Space is part of a cluster.</td>
  </tr>
  <tr>
    <td>Clustered Schema</td>
    <td>(If applicable) The cluster topology (partitioned or replicated).</td>
  </tr>
  <tr>
    <td colspan="2"><i>Network & Environment</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Home Directory</td>
    <td>Path where the deployment files are located.</td>
  </tr>
  <tr>
    <td>Host Name</td>
    <td>Name or IP address of the host machine.</td>
  </tr>
  <tr>
    <td>RMI Registry Port</td>
    <td>Port number for the RMI registry.</td>
  </tr>
  <tr>
    <td>JMX Service URL</td>
    <td>Full URL for hte JMX service.</td>
  </tr>
  <tr>
    <td colspan="2"><i>Memory Management</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Cache Policy</td>
    <td>Defined cache policy (all in cache or LRU)</td>
  </tr>
  <tr>
    <td>LRU Eviction Batch Size</td>
    <td>(LRU only) How many of the oldest data objects to remove if eviction is based on available memory. </td>
  </tr>
  <tr>
    <td>Cache Size</td>
    <td>Amount of memory allocated to the cache.</td>
  </tr>
  <tr>
    <td>Memory Management State</td>
    <td>Whether the memory manager is enabled or disabled.</td>
  </tr>
  <tr>
    <td>High Watermark</td>
    <td>When the amount of used cache memory reaches this value (in %), the memory manager must take action (throw an exception or evict objects).</td>
  </tr>
  <tr>
    <td>Low Watermark</td>
    <td>When the amount of used cache memory reaches this value (in %),  the memory manager begins to monitor how much cache memory is left.</td>
  </tr>
  <tr>
    <td>Write Operation Rejection</td>
    <td>When the amount of used cache memory reaches this value (in %), the memory manager begins to block write operations. </td>
  </tr>
  <tr>
    <td>Write Operation Inspection</td>
    <td>When the amount of used cache memory reaches this value (in %), the memory manager begins to monitor write operations. </td>
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

  
