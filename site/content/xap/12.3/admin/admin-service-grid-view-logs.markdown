---
type: post123
title: Viewing Logs
categories: XAP123ADM,PRM
weight: 400
parent: admin-service-grid.html
---
 
{{%info "Info"%}}
This functionality is not yet available in the Command Line Interface or REST Manager API administration tools.
{{%/info%}}  


**To view logs:**

 
{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}
-->

{{%tab "Web Management Console"%}}

The Logs tab is available in the Hosts and Processing Units views. You can display the logs for any of the XAP components (host machine, Processing Units, Spaces, and containers). You can also turn logging on and off for the components.

1. In the relevant view, highlight the component you want to investigate.
1. Click the Logs tab on the right side of the page.
1. To turn logging off click the **Pause** icon, and to turn logging on click the **Play** icon.
1. To filter the logs, use the **Log Level** box to select the desired log level and/or type specific text in the **Find** box, and then click the **Search** icon.
1. To expand the log window, click the **Expand** icon in the upper right corner.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}
