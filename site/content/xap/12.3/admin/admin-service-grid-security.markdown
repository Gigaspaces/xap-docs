---
type: post123
title: Accessing a Secured Service Grid
categories: XAP123ADM,PRM
weight: 900
parent: admin-service-grid.html
---
 
  
**To access a secured service grid:**
 

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

See the [Security](./tools-web-ui.html#security) paragraph of the [Web Management Console](./tools-web-ui.html) topic for information on how to configure the tool to access a secured service grid.

{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}
TBD
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
