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
 
*Path*

`POST /containers`

*Description:*

Deploy a container (GSC).

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "host": "myHost", \ 
   "memory": "12g", \ 
   "zone": "green", \ 
 }' 'http://localhost:8090/v2/containers'
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| memory | Container's JVM max memory.| No |
| zone |Container's zone.| No |
|vmArguments | Additional System properties.|
 
{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Hosts view, click the Actions icon for the Grid Service Agent where you want to start a container.
1. Select the relevant container that you want to start:
	- StartGSM - to start a Grid Service Manager
	- StartGSC - to start a Grid Service Container
	- Start Lookup - to start a Lookup Service (LUS)
	
	The container is started.

{{%note "Note"%}}
If you try to start a global container that is already running under this Grid Service Agent, you get a notification message and the container isn't started.
{{%/note%}}
 
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

*Path*

`DELETE /containers/{id}`

*Description:*

Stop a container (GSC).

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/containers/admin~13972'
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| containerId | Id of the container | Yes |
 
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
*Path*

`DELETE /containers/{id}/restart`

*Description:*

restart a container (GSC).

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v2/containers/admin~13972/restart'
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| containerId | Id of the container | Yes |
 
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