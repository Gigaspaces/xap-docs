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
 
Run a Grid Service Agent on the current host.

*Input Example:*

```bash
<XAP-HOME>/bin/xap host run-agent
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Option | ---auto |  Start the Manager on the localhost (or on a host from the cluster of Managers) and start the Web Management Console|
|Option | ---gsc  |  |
|Option | ---containers=\<number\>  | Specify the number of containers to start(default is 0) |
|Option | ---manager  | Run one instance of the Manager from the cluster of Managers |
|Option | ---webui  | Start the Web Management Console |
|Option | <nobr>---custome=\<String, Integer\><nobr>| Specify which custom service to run by the agent (for example: --custom=global.lus=1)|
 
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}

N/A

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

TBD
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

N/A

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