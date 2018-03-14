---
type: post
title:  Viewing Space Configuration Information
weight: 300
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}<br>

{{% bgcolor orange %}}**it seems that for the CLI and REST API, this is partly the --info command, and partly information that the user can see via jconsole (which isn't supported in the new CLI and should be run directly by the user).<br>If the updated CLI doesn't contain parallel commands, you can say as much in the topic. Need to make sure the information is paralleled as much as possible. Where information is NOT parallel, explain in the intro text why you would prefer one tool over another to perform this particular task (and give an example).**{{% /bgcolor %}}

**To view Space information:**

  
{{%tabs%}}
{{%tab "Command Line Interface"%}}

***Display Space Information***

_Parameters:_<br> 

- name : The name of the Space.

_Options:_<br>

- ---operation-stats : Displays Space operations statistics, (read, write, take etc)  <br>
- ---type-stats      : Displays Space object information.
 

*Example:*

```bash
<XAP-HOME>/bin/xap space info --type-stats mySpace
```
 
***Display Space instance information***

_Parameters:_<br> 
instanceId : The id of the Space instance to use.

_Options:_<br>

- ---operation-stats : Displays Space instance operations statistics, (read, write, take etc)  <br>
- ---type-stats      : Displays Space instance object information.<br>
- ---replication-stats: Display Space instance replication information.
 
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instances --replication-stats mySpace~1
```
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

***Display Space Information***

_Parameters:_<br>

- host URL: Host URL   where the REST Manager is running.<br>
- name : The name of the Space.

 
Displays Space operations statistics, (read, write, take etc)  <br>
  

*Example:*

```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/statistics/operations'
```
 
***Display Space instance information***

_Parameters:_<br> 

- host URL: Host URL where the REST Manager is running.<br>
- name : The name of the Space.<br>
- instanceId : The id of the Space instance to use.

_Options:_<br>

- operations : Displays Space instance operations statistics, (read, write, take etc)  <br>
- types     : Displays Space instance object information.<br>
- replication: Display Space instance replication information.
 
 
*Examples:*
 
```bash
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/operations'
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/replication'
curl -X GET --header 'Accept: application/json' 'http://localhost:8090/v1/spaces/mySpace/instances/mySpace~1/statistics/types'
```
 
{{%/tab%}}


{{%tab "Web Management Console"%}}
 
You can see the following details about Spaces in the lower pane of the Spaces view when the Configuration option is selected (default). The information is displayed for the Space instance that is highlighted in the table above.

{{%note "Note"%}}
This information is also available in the Config tab of the associated Processing Unit.
{{%/note%}}

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


If you have a system with Memory Xtend, you can also see the following information about the blobstore:

??? 

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

