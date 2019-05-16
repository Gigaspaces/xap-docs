---
type: post123
title:  Viewing Alerts
categories: XAP123ADM,PRM
parent: web-management-common-view.html
weight: 300
canonical: auto
---

You can view alerts in the Web Management Console Alerts panel, displayed by XAP Alert groups (alerts are grouped by correlation key), and also generate an alert dump for specific grid components. 

{{% note "Info"%}}
The Web-UI server utilizes the `<XAP Root>/config/alerts/alerts.xml` configuration file. These configurations apply to any client connecting to the Web-UI at the specified host and port.
{{% /note%}}

Alerts are grouped together by type, such as CPU, Memory, etc. When an alert is raised, it is aggregated with other consecutive alerts of the same type. Previous alerts from the aggregation get "pushed" down (circled in red). A resolved alert "closes" the aggregation (circled in green). A new alert of the same type will trigger a new aggregation.

Sort the 'status' column in ascending order to show the latest unresolved alerts.

{{% align center%}}
![hosts1.jpg](/attachment_files/web-console/alerts.jpg)
{{% /align%}}

# Generating an Alert Dump

A logs dump can be retrieved for the alert's related grid components. This can be done by opening the Alert Actions menu and clicking "Generate dump", after which, a window will appear providing configuration of the dump:


{{% align center%}}
![generate_dump.png](/attachment_files/web-console/generate_dump.png)
{{% /align%}}

Select the desired components for the dump and click "Generate", and a zip file will be downloaded containing log files for each of the selected components.

