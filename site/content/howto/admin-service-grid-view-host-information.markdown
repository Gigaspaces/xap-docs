---
type: post
title:  Viewing Host Information 
weight: 100
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}


{{%warning%}}
This section was under construction while documenting
{{%/warning%}}

<br>

**To view host information:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br> 

 
 
_Options:_<br>
 
 
*Example:*<br>

```bash
 
```
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

 

_Options:_<br>

 
*Example:*<br>
 
```bash
```
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
TBD
{{%/tab%}}

{{% /tabs %}}

