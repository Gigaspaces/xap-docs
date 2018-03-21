---
type: post123
title: Viewing Alerts
categories: XAP123ADM,PRM
weight: 501
parent: admin-service-grid.html
---
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To view alerts:**
 
{{%tabs%}}

{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}

{{%tab "Web Management Console"%}}

You can view alerts in the Web Management Console Alerts panel, displayed in XAP Alert groups (according to correlation key). Alerts are grouped together by type, such as CPU, Memory, etc. When an alert is triggered, it is aggregated with other consecutive alerts of the same type. Previous alerts from the aggregation get pushed down. A resolved alert closes the aggregation. A new alert of the same type triggers a new aggregation. You can sort the on **Status** column in ascending order to show the latest unresolved alerts.

From the menu bar, click **Alerts**. The list of alerts in the system is displayed with the following details: 

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Status</td>
    <td>Severity of the alert (Warning, Severe). Expand the top-level alert to see the recurrences of the alert and if/when it was resolved. A green check mark indicates an alert than has been resolved.<td>
  </tr>
  <tr>
    <td>Type</td>
    <td>Type of alert.</td>
  </tr>
  <tr>
    <td>Description</td>
    <td>Detailed description of the alert.</td>
  </tr>
  <tr>
    <td>Location</td>
    <td>Service grid component where the alert occurred.</td>
  </tr>
  <tr>
    <td>Last Update</td>
    <td>Time that the alert was generated.</td>
  </tr> 
</table>  

The Alert number displayed in the menu bar indicates the amount of currently active alerts.

{{%info "Info"%}}
The Web-UI server utilizes the `<XAP Root>/config/alerts/alerts.xml` configuration file. These configurations apply to any client connecting to the Web-UI at the specified host and port.
{{%/info%}}  

{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
