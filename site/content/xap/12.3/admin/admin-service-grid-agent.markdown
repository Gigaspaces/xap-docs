---
type: post123
title: Managing the Grid Service Agent
categories: XAP123ADM,PRM 
weight: 700
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

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

| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Option | --auto | Start the Manager on the localhost o||
|Option | --gsc  |  ||
|Option | --containers  |  ||
|Option | --manager  |  ||
|Option | --webui  |  ||
 
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}
TBD
{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
