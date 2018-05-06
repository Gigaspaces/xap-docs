---
type: post123
title: Managing the Grid Service Agent
categories: XAP123ADM,PRM 
weight: 800
parent: admin-service-grid.html
---
 
  

 
**To start a Grid Service Agent:**
<br>
 
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}


*Command:*

`xap host run-agent`  

*Description:*
 
This command starts a Grid Service Agent on the current host.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host run-agent
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
| - | - | If no additional options are configured, run a Grid Service Agent without starting any additional services. |
|Option | ---auto | Automatically resolve which service to run on the current host (local manager, Web Management Console, etc.). |
|Option | ---gsc  | Start a container on the host. |
|Option | ---containers=\<number\>  | Specify the number of containers to start (default is 0). |
|Option | ---manager  | Run one instance of the Manager (from the cluster of Managers). |
|Option | ---webui  | Start the Web Management Console. If you don't want to start the Web Management Console, set this option to `--webui=false`. |
|Option | <nobr>---custom=\<String, Integer\><nobr>| Specify which custom service The Grid Service Agent should run (for example: `--custom=global.lus=1`). |

{{%note "Notes"%}}
The Manager configuration is applied from the XAP_MANAGER_SERVERS environment variable. To configure a cluster of Managers on 1 or 3 machines (including this one), edit the `setenv-overrides` script and set the XAP_MANAGER_SERVERS environment variable to list the designated Manager servers.

To run a standalone Manager on your machine, use the `--auto` command without configuring the XAP_MANAGER_SERVERS environment variable. 
{{%/note%}}


{{%/tab%}}

{{%tab "REST Manager API"%}}

{{%note "Info"%}}
This functionality is not available in the REST Manager API administration tool.
{{%/note%}}

{{%/tab%}}


{{%tab "Web Management Console"%}}

{{%note "Info"%}}
This functionality is not available in the Web Management Console administration tool.
{{%/note%}}

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}


**To stop a Grid Service Agent:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap host kill-agent`

*Description:*

This command terminates the Grid Service Agent that is running on the current host.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host kill-agent
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
| - | - | Terminate the single running Grid Service Agent on the current host. |
|Parameter | [pids] | Display a whitespace delimited list of process IDs (for example, 8361 1123 4270)|
|Option | ---all  | Terminates all Grid Service Agents on the current host. |

{{%/tab%}}

{{%tab "REST Manager API"%}}

{{%note "Info"%}}
This functionality is not available in the REST Manaqer API administration tool.
{{%/note%}}

{{%/tab%}}


{{%tab "Web Management Console"%}}

You can stop a Grid Service Agent via the Web Management Console.

1. In the Hosts view, click the **Action** icon for the Grid Service Agent that you want to stop.
1. Click **Shutdown**.
1. Click **Yes** in the confirmation message. The associated containers will shut down along with the Grid Service Agent.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}