---
type: post140
title:  Viewing Events
categories: XAP140ADM,PRM
parent: web-management-common-view.html
weight: 400
---

In the Events panel, you can view events for the selected component. You can filter the view based on text, and by date and time.

{{% align center%}}
![hosts1.jpg](/attachment_files/web-console/events.jpg)
{{% /align%}}

The event timeline shows the deployment life cycle of all the Processing Units.

**Life cycle success events for each instance**

- installation attempt: an attempt to provision a Processing Unit instance on an available GSC.
- instance added: a Processing Unit instance was successfully instantiated on a GSC.
- instance uninstalled: a Processing Unit instance was successfully removed.
- Container N/A: a Processing Unit instance is pending instantiation until an available GSC is discovered.

**Life cycle success events for Processing Unit**

- installation succeeded: deployment of a Processing Unit has been completed successfully (all instances instantiated).
- installation uninstalled: undeployment of a Processing Unit has been completed (all instances undeployed).

**Life cycle failure events for each instance**

- installation failed: a Processing Unit instance  failed to instantiate.
- installation unresponsive: a Processing Unit instance is unresponsive to "member-is-alive" attempts (failure is suspected).
- installation crashed: an unresponsive Processing Unit instance has timed out (failure was detected).
- installation re-detected: a Processing Unit instance was previously unresponsive but is now responsive.


