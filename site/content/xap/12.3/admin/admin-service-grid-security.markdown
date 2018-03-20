---
type: post123
title: Securing the Service Grid
categories: XAP123ADM,PRM
weight: 900
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}

***Securing the Service Grid***<br>

_Parameters:_

- ---username:<br>
- ---password:

*Example:*

```bash
<XAP-HOME>/bin/xap --uesrname=someone password=somewhere ...... additional parameters and options
```

{{%/tab%}}

{{%tab "REST Manager API"%}}

See securing the Service Grid property `-Dcom.gs.security.enabled=true`

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
