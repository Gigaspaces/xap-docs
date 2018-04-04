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

When security is enabled, the REST Manager API performs basic authentication and checks if the user has sufficient privileges 
to execute the operation.

For more information about using the REST Manager API in a secured environment, refer to the [REST Manager API](./admin-rest-manager-api.html) introductory page.

{{%/tab%}}


{{%tab "Web Management Console"%}}

If you configured your XAP instance to run in secure mode using the `-Dcom.gs.security.enabled=true` property, you will see the following login window when the Web Management Console is started:

{{% align center %}}
![xap-login-inline.png](/attachment_files/web-console/login-12.3.png)
{{%/align%}}

For more information about how to configure the Web Management Console for use in a secured environment, refer to the [Web Management Console](.//tools-web-ui.html) introductory page.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}
Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}
