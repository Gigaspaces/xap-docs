---
type: post102
title:  Menu Bar
categories: XAP102ADM
parent: web-management-console.html
weight: 510
canonical: auto
---

{{%ssummary%}}{{%/ssummary%}}

![hosts1.jpg](/attachment_files/web-console/menu-bar.png)

The menu bar for the web management UI is common across all tabs. It lets you deploy Processing Units and Spaces, view Alerts and Events.


# Deploy
The Web UI supports the deployment of Processing Units and Spaces  as demonstrated below.


## Space

Deployment options:

![hosts1.jpg](/attachment_files/web-console/space-deploy.png)

<br>

## Processing Unit

Deployment options:

![hosts1.jpg](/attachment_files/web-console/pu-deploy.png)

<br>

## Define SLA

![hosts1.jpg](/attachment_files/web-console/sla-deploy.png)

<br>

# Alerts

The Alerts panel displays XAP Alert groups (Alerts are grouped by correlation key) for more details see [Administrative Alerts]({{%currentjavaurl%}}/administrative-alerts.html)

![hosts1.jpg](/attachment_files/web-console/alerts.jpg)

### Alerts dump

{{%section%}}
{{%column width="5%" %}}
![segment.png](/attachment_files/web-console/icons/setting.png)
{{%/column%}}
{{%column width="90%" %}}
A logs dump can be retrieved for the alert's related grid components.
This can be done by opening the Alert Actions menu and clicking "Generate dump...", after which, a window will appear providing configuration of the dump:
{{%/column%}}
{{%/section%}}

![generate_dump.png](/attachment_files/web-console/generate_dump.png)

Select the desired components for the dump and click "Generate", and a zip file will be downloaded containing log files for each of the selected components.


# Events

In the events panel you can see events for the selected component. It lets you filter the events and you can also choose the narrow down the
events by date and time.

![hosts1.jpg](/attachment_files/web-console/events.jpg)

The events time-line shows the deployment life cycle of all the processing units.

#### Life-cycle success events for each instance:

- installation attempt : an attempt to provision a processing unit instance on an available GSC
- instance added: a processing unit instance has successfully been instantiated on a GSC
- instance uninstalled: a processing unit instance has been successfully removed
- Container N/A: a processing unit instance is pending instantiation until an available GSC is discovered

#### Life-cycle success events for processing unit:

- installation succeeded: deployment of processing unit has been completed successfully (all instances instantiated)
- installation uninstalled: undeployment of processing unit has been completed (all instances undeployed)

#### Life-cycle failure events for each instance:

- installation failed: processing unit instance has failed to instantiate
- installation unresponsive: processing unit instance is unresponsive to "member-is-alive" attempts (suspecting failure)
- installation crashed: processing unit instance unresponsiveness has timed-out (detected failure)
- installation re-detected: processing unit instance was previously unresponsive but is now responsive.


