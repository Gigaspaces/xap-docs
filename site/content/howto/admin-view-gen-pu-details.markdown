---
type: post
title:  Viewing the Deployed Processing Units
weight: 500
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

**To view information about the deployed Processing Units:**


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

TBD

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

