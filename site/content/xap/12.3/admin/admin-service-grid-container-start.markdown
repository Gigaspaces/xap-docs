---
type: post123
title:  Managing Containers
categories: XAP123ADM,PRM
weight: 300
parent: admin-service-grid.html
---
 
  

 

 
 
# Starting a Container

 

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

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | host | Host where to create the container|
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

1. In the Hosts view, click the **Actions** icon for the Grid Service Agent where you want to start a container.
1. Select the relevant option:
	- **StartGSM** - to start a Grid Service Manager
	- **StartGSC** - to start a Grid Service Container
	- **Start Lookup** - to start a Lookup Service (LUS)
	
	The container is started.

{{%note "Note"%}}
If you try to start a global container that is already running under this Grid Service Agent, you get a notification message and the container isn't started.
{{%/note%}}
 
{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}

 
# Terminating a Container

 

 

**To terminate a container:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap container kill <container ID>`

*Description:*
 
Stopping a container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container kill admin~22200
```

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | \<container\> ID | ID of the container to kill|
 
 
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
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}



# Restarting a Container

**To restart a container:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap container restart <container ID>`

*Description:*
 
Restarting a container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap container restart containerId
```

 
*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | \<container ID\> | ID of the container to restart|
 
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
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}