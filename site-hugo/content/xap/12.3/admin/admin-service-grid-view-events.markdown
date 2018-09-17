---
type: post123
title: Viewing Events
categories: XAP123ADM,PRM
weight: 500
parent: admin-service-grid.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface or REST Manager API administration tools.
{{%/note%}}  
 

**To view events:**

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

The event timeline shows the deployment life cycle of all the Processing Units.

**Life cycle success events for each instance**

- installation attempt: an attempt to provision a Processing Unit instance on an available GSC.
- instance added: a Processing Unit instance was successfully instantiated on a GSC.
- instance uninstalled: a Processing Unit instance was successfully removed.
- Container N/A: a Processing Unit instance is pending instantiation until an available GSC is discovered.

**Life cycle success events for each Processing Unit**

- installation succeeded: deployment of a Processing Unit has been completed successfully (all instances instantiated).
- installation uninstalled: undeployment of a Processing Unit has been completed (all instances undeployed).

**Life cycle failure events for each instance**

- installation failed: a Processing Unit instance failed to instantiate.
- installation unresponsive: a Processing Unit instance is unresponsive to “member-is-alive” attempts (failure is suspected).
- installation crashed: an unresponsive Processing Unit instance has timed out (failure was detected).
- installation re-detected: a Processing Unit instance was previously unresponsive but is now responsive.

From the menu bar, click **Events**. The list of events in the system is displayed with the following information:

<table>
  <tr>
    <th>Item</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Component</td>
    <td>Service grid component where the event occurred.</td>
  </tr>
  <tr>
    <td>Message</td>
    <td>Type of event that occurred.</td>
  </tr>
  <tr>
    <td>Description</td>
    <td>Detailed description of the event.</td>
  </tr>
  <tr>
    <td>Time</td>
    <td>Complete timestamp of the event (month, day, year and time of day).</td>
  </tr>
  <tr>
    <td>Status</td>
    <td>Current status of the event (Success, In progress, Failed).</td>
  </tr> 
</table>  

The Event number displayed in the menu bar indicates the amount of events that have occurred since the last time you viewed events.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developer Guide.
{{%/tab%}}

{{% /tabs %}}
