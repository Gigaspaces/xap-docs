---
type: post123
title: Securing the Service Grid
categories: XAP123ADM,PRM
weight: 900
parent: admin-service-grid.html
---
 
  
**Accessing a secured Service Grid:**
 

<br>
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}


*Command:*

`xap undeploy <pu name> --username=user --password=password` 


*Example:*

```bash
<XAP-HOME>/bin/xap --uesrname=user password=password ...... additional parameters and options
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:-----------|
|Option |username | The user name. |
|Option | password | The password.|

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
