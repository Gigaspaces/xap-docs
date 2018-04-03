---
type: post123
title: Accessing a Secured Service Grid
categories: XAP123ADM,PRM
weight: 900
parent: admin-service-grid.html
---
 
  
When security is in place, the administration tools require credentials to securely access and operate the secured components. Access is granted to authenticated users, while operations are restricted based on the granted permission.
For more information, refer to the [Security](../started/xap-tutorial-part10.html) page in the [Getting Started](../started/index.html) guide (under the [XAP Basics](../started/xap-basics.html) section).

**To access a secured service grid:**
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}


*Command:*

`xap --username=<user name> --password=<password> <command>`


*Example:*

```bash
<XAP-HOME>/bin/xap --username=user --password=pass  host list
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:-----------|
|Option |username | The user name. |
|Option | password | The password.|

{{%/tab%}}

{{%tab "REST Manager API"%}}

See section on [REST Manager API security](../security/securing-the-REST-manager.html)

{{%/tab%}}


{{%tab "Web Management Console"%}}

See the [Security](./tools-web-ui.html#security) paragraph of the [Web Management Console](./tools-web-ui.html) topic for information on how to configure the tool to access a secured service grid.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}
Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
