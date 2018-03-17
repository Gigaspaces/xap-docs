---
type: post
title: Viewing Container Information
weight: 200
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
**To view the general Container details:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

***List all Containers***<br>


*Command:*

`xap container list`  

*Description:*
 
Lists all containers.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container list 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-container-list.png)

*Parameters and Options:*

None

**List Containers Information**


*Command:*

`xap container info <containerId>`  

*Description:*
 
Display container information.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container info admin~22200 
```

*Output Example:*

![image](/attachment_files/admin/cli-xap-container-info.png)

*Parameters and Options:*

| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Parameter | containerId | The Id of the container||
 
 


{{%/tab%}}


{{%tab "REST Manager API"%}}
***List all Containers***<br>
 

_Parameters:_


*Example:*

```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/containers'
```

***List Container information***<br>
 

_Parameters:_<br> 

- containerId : The Id  of the Container.
 
 
*Example:*
 
```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/conatinerId'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}

The Hosts view provides general overview of the containers that are running in the XAP environment. 

{{%note "Note"%}}
For a description of the other information displayed in the Hosts view, see the [Viewing Host Information](/admin-service-grid-view-host-information.html) topic.
{{%/note%}}

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td colspan="2"><i>Grid Service Container</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the GSC.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Indicator of how much CPU is being used, in %.</td>
  </tr>
  <tr>
    <td>Used Heap (MB)</td>
    <td>Indicator of how much heap memory the GSC is utilizing, in both MB and %.</td>
  </tr>
  <tr>
    <td>Threads</td>
    <td>How many threads the GSC has open.</td>
  </tr>
  <tr>
    <td>Processing Units</td>
    <td>Processing Units and number of Processing Unit instances being hosted by the GSC.</td>
  </tr>
  <tr>
    <td>Primaries & Backups</td>
    <td>Processing Unit instances being hosted by the GSC, according to primary and backup status.</td>
  </tr>
  <tr>
    <td colspan="2"><i>Grid Service Manager/Lookup Service</i></td>
    <td></td>
  </tr>
  <tr>
    <td>Name</td>
    <td>Name of the container.</td>
  </tr>
  <tr>
    <td>CPU</td>
    <td>Indicator of how much CPU is being used, in %.</td>
  </tr>
  <tr>
    <td>Used Heap (MB)</td>
    <td>Indicator of how much heap memory the container is utilizing, in both MB and %.</td>
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

