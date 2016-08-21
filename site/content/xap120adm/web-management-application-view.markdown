---
type: post120
title:  Application View
categories: XAP120ADM,PRM
parent: web-management-console.html
weight: 400
---

{{% ssummary %}}{{% /ssummary %}}


The Applications Module allow users to manage and monitor XAP applications. It offers a wide set of functionality from deployment to verification, monitoring and even log tracing.

The picture below explains the different parts composing the Applications module

{{% align center%}}
![apps_explained_9_6.png](/attachment_files/apps_explained_9_6.png)
{{% /align%}}

# Application Map

The application map is a graphical representation of the deployment plan (processing units, their SLA and their dependencies)
It allow the user to compare the plan with the actual deployment in any given moment. the next sections give detailed explanation of the application map functionality

#### Overview

{{% align center%}}
![app_map_explained.png](/attachment_files/app_map_explained.png)
{{% /align%}}

#### Understanding the processing unit display

The application map depicts a shape per each processing unit

{{% align center%}}
![pu.jpg](/attachment_files/pu.jpg)
{{% /align%}}

It shows the deployment and dependencies between each processing unit, belonging to the chosen "application" from the drop-down menu.
Processing units that were not deployed in the context of an application, will be shown under "Unassigned Services".

{{% align center%}}
![unassigned_selection_9_6.jpg](/attachment_files/unassigned_selection_9_6.jpg)
{{% /align%}}

#### Processing Unit dependencies

Dependencies between processing units are depicted by an arrow flowing in the direction of "depends on".
For example, in the screenshot below, the feeder depends on the Space to be alive.

{{% info %}}
For more information on processing unit dependencies, see [Application deployment and processing unit dependencies]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html#Application Deployment and Processing Unit Dependencies)
{{% /info %}}

{{% align center%}}
![application_dependency_9_6.png](/attachment_files/application_dependency_9_6.png)
{{% /align%}}

The following table explains these icons

|      |     |
|----|-----|
|![segment.png](/attachment_files/segment.png)|Space Partition|
|![segments.png](/attachment_files/segments.png)|Space Partition with backup|
|![data.png](/attachment_files/data.png)|Space Replica|
|![process.png](/attachment_files/process.png)|Processing (Business Logic)|
|![pipe.png](/attachment_files/pipe.png)|Event Container (Messaging)|
|![world.png](/attachment_files/world.png)|Web application|

#### Contextual Actions

{{% align center%}}
![actions_explained.png](/attachment_files/actions_explained.png)
{{% /align%}}

### The Monitoring view

The monitoring view, allow the user to monitor the performance of a the selected processing unit. The displayed statistics are at the cluster level.

#### Understanding the widgets

{{% align center%}}
![metrics_explained.png](/attachment_files/metrics_explained.png)
{{% /align%}}

#### Stateful Processing Unit metrics:
In the case of backup spaces OS and VM metrics can be selected either for all instances of Processing Unit( Cluster ) or all instances without backups.
The same with Processing Unit that has replication.


#### OS metrics
- Memory
- CPU

#### JVM metrics
- Memory
- CPU
- GC

#### Space replication
- Replication TP(bytes/sec)
- Redo Log Size
- Redo Log Memory Packets Count
- Redo Log External Storage Packet Count
- Redo Log External Storage Space Used

Total and average Space operation metrics are available for selection as well.

#### Space Troughput
- Write per sec.
- Read per sec.
- Take per sec.
- Update per sec.
- Execute per sec.
- Notify Ack per sec.
- Notify Reg per sec.
- Notify Trigger per sec.
- Change per sec.

#### Space operations
- Write
- Read
- Take
- Update
- Execute
- Notify Ack
- Notify Reg
- Notify Trigger
- Change

{{% align center%}}
![space_vm_metrics.jpg](/attachment_files/space_vm_metrics.jpg)
{{% /align%}}

{{% align center%}}
![space_metrics.jpg](/attachment_files/space_metrics.jpg)
{{% /align%}}

# Stateless Processing Unit metrics:

{{% align center%}}
![stateless_vm_metrics.jpg](/attachment_files/stateless_vm_metrics.jpg)
{{% /align%}}

# Web Processing Unit metrics:

{{% align center%}}
![web_pu_metrics.jpg](/attachment_files/web_pu_metrics.jpg)
{{% /align%}}

# Mirror metrics:

### OS metrics
- Memory
- CPU

### JVM metrics
- Memory
- CPU
- GC

### Mirror
- Failed operations count
- Mirror Write per sec.
- Mirror Update per sec.
- Mirror Remove per sec.

# The Infrastructure view

The infrastructure view allow the user to verify the application's topology. It maps the processing unit instances to hosts, providing some basic information about each host.

{{% align center%}}
![infra_explained.png](/attachment_files/infra_explained.png)
{{% /align%}}

### The Services view

The services view allow the user to get information at the processing unit instance level and to correlate performance of several selected instances.

### Comparing Instances

{{% align center%}}
![services_explained.png](/attachment_files/services_explained.png)
{{% /align%}}

### Service Instance Details

{{% align center%}}
![details_explained.png](/attachment_files/details_explained.png)
{{% /align%}}

### Contextual Actions

### The Logs view

The logs view allow the user to browse the application logs, filter or search them.

{{% align center%}}
![logs_explained.png](/attachment_files/logs_explained.png)
{{% /align%}}

### Events time-line (per application)

The events time line is filtered per application chosen from the application drop-down menu.
The events time-line shows the deployment life cycle of all the processing units belonging to this application.

{{% info %}}
For more information on the events displayed, see [Events time-line tab in dashboard view](./web-management-dashboard-view.html#Events time-line)
{{% /info %}}

{{% align center%}}
![events_timeline_in_application.png](/attachment_files/events_timeline_in_application.png)
{{% /align%}}

### Events table (per application)

The events table is filtered per application chosen from the application drop-down menu.
The events table shows the deployment life cycle of all the processing units belonging to this application.

{{% align center%}}
![events_grid_in_application_9_6.png](/attachment_files/events_grid_in_application_9_6.png)
{{% /align%}}

# SSH terminal

There is an option to open SSH terminal for specific machine

{{% align center%}}
![options_to_open_ssh_9_6.png](/attachment_files/options_to_open_ssh_9_6.png)
{{% /align%}}

{{% align center%}}
![permit_ssh_applet_9_6.png](/attachment_files/permit_ssh_applet_9_6.png)
{{% /align%}}

{{% align center%}}
![user_name_ssh_applet.png](/attachment_files/user_name_ssh_applet.png)
{{% /align%}}

{{% align center%}}
![authentication_warning_ssh_applet.png](/attachment_files/authentication_warning_ssh_applet.png)
{{% /align%}}

{{% align center%}}
![provide_password_ssh_applet.png](/attachment_files/provide_password_ssh_applet.png)
{{% /align%}}

{{% align center%}}
![ssh_applet_screen.png](/attachment_files/ssh_applet_screen.png)
{{% /align%}}