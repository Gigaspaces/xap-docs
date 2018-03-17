---
type: post
title:  Managing Containers
weight: 300
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
# Starting a Container

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

**To start a container:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap container create <host>`  

*Description:*
 
Starting a container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container start myHost 
```

 
*Parameters and Options:*

| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Parameter | host | Host where to create the container| |
|Option    |  ---memory:\<memory\> | Container's JVM max memory.|
|Option    | ---zone=\<zone name\> | Container's zone.|
|Option    |---property=\<String=String\> | Additional System properties.|
 
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- host: Name of the host unit to create the container.
 

_Options:_<br>

- memory : Container's JVM max memory.<br>
- zone:Container's zone.<br>
- vmArguments: Additional System properties.
 
 
*Example:*<br>
 

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "host": "string", \ 
   "memory": "string", \ 
   "zone": "string", \ 
   "vmArguments": [ \ 
     "string" \ 
   ] \ 
 }' 'http://localhost:8090/v1/containers'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}

Not supported in this tool.
 
{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}

 
# Terminating a Container

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To terminate a container:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap container kill <containerId>`  

*Description:*
 
Stopping a container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container kill containerId
```

 
*Parameters and Options:*

| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Parameter | containerId | Container ID to be stopped| |
 
 
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- containerId : Id of the container. 

_Options:_<br>

 
*Example:*<br>
 
```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/containerId'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Hosts view, expand the tree.
1. In the row with the required container, click the **Actions** icon and select **Terminate** from the menu. 
1. Click **Yes** in the confirmation message.
1. Click **OK**.

The container is terminated. The Host view and the status bar at the bottom of the window are updated.

{{%note "Note"%}}
If you terminated a global container, it is started again with a new name.
{{%/note%}}

{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}



# Restarting a Container

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To restart a container:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap container restart <containerId>`  

*Description:*
 
Restarting a container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container restart containerId
```

 
*Parameters and Options:*

| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Parameter | containerId | Container ID to restart| |
 
{{%/tab%}}


{{%tab "REST Manager API"%}}

_Parameters:_<br>

- containerId : Id of the container. 

_Options:_<br>

 
*Example:*<br>
 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/containers/containerId/restart'
```
{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Hosts view, expand the tree.
1. In the row with the required container, click the **Actions** icon and select **Restart** from the menu. 
1. Click **Yes** in the confirmation message.
1. Click **OK**. 

The container is terminated and restarted with the same name. The Host view and the status bar at the bottom of the window are updated during the restart progress.

{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}