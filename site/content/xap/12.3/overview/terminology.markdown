---
type: post123
title: Terminology
categories: XAP123OVW
parent: none
weight: 300
---

 


# Basic Components

{{%  anchor Space %}}

## Space

{{%  section %}}
{{%  column width="30%" %}}
![term_space.gif](/attachment_files/term_space.gif)
{{%  /column %}}
{{%  column width="70%" %}}
The GigaSpaces cache instance that holds data objects in memory.
{{%  /column %}}
{{%  /section %}}

{{%  anchor Execute-Read-Write-Take-and-Notify %}}

## Execute, Read, Write, Take and Notify

{{%  section %}}
{{%  column width="30%" %}}
![term_verbs.jpg](/attachment_files/term_verbs.jpg)
{{%  /column %}}
{{%  column width="70%" %}}
A set of methods used to read, write, take, and register for notification on objects that are stored in the space. Execute allows sending Tasks to be executed within the space. Read and Take criteria can be specified via a query or a template (an example object).
{{%  /column %}}
{{%  /section %}}

{{%  anchor Service Bean %}}

##  Service Bean

{{%  section %}}
{{%  column width="30%" %}}
![term_service_bean.gif](/attachment_files/term_service_bean.gif)
{{%  /column %}}
{{%  column width="70%" %}}
An application component that interacts with the space (using the read, write, take and notify operations), and implements a certain functionality.
{{%  /column %}}
{{%  /section %}}

{{%  anchor Processing Unit %}}

# Processing Unit

{{%  section %}}
{{%  column width="30%" %}}
![term_empty_pu.gif](/attachment_files/term_empty_pu.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A combination of service beans and/or an embedded space instance. This is the fundamental unit of deployment in GigaSpaces XAP. The Processing Unit itself runs within a [Processing Unit Container](#processing-unit-container), and is typically deployed onto the [Service Grid](#service-grid). Once a Processing Unit is deployed, a **Processing Unit instance** is the actual runtime entity.
{{%  /column %}}
{{%  /section %}}

## Processing Unit configured with an embedded space

{{%  section %}}
{{%  column width="30%" %}}
![term_pu_with_space.gif](/attachment_files/term_pu_with_space.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A deployable package which instantiates an embedded space instance, also called a data grid instance. A set of embedded space instances that run within the processing units typically form a [Data Grid](#Data Grid).
{{%  /column %}}
{{%  /section %}}


## Processing Unit configured with one or more services

{{%  section %}}
{{%  column width="30%" %}}
![term_pu_with_bean.gif](/attachment_files/term_pu_with_bean.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A deployable package containing one or more services. In the GigaSpaces context, it usually acts as a client that interacts with other Processing Units by utilizing the messaging capabilities of the space.
{{%  /column %}}
{{%  /section %}}


## Processing Unit configured with embedded space and embedded services
{{%  section %}}
{{%  column width="30%" %}}
![term_pu_with_space_and_bean.gif](/attachment_files/term_pu_with_space_and_bean.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A deployable, independent, scalable unit, which is the building block of [Space-Based Architecture](./space-based-architecture.html).
Client application (which can also be other processing units) write objects to the space, and the processing unit which contains this space consumes these objects or is notified about them and triggers a related services.
{{%  /column %}}
{{%  /section %}}


# Data Grid


{{%  anchor Data Grid %}}

## In Memory Data Grid - IMDG, or Enterprise Data Grid - EDG

{{%  section %}}
{{%  column width="30%" %}}
![term_populated_data_grid.gif](/attachment_files/term_populated_data_grid.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A set of space instances, typically running within their respective processing unit instances.
The space instances are connected to each other to form a space cluster.
The relations between the spaces define the [Data Grid Topology](#Data Grid Topology).
{{%  /column %}}
{{%  /section %}}

{{%  anchor Data Grid Topology %}}

## Data Grid Topologies

{{%  anchor Primary Backup Data Grid %}}

### Primary Backup Data Grid

{{%  section %}}
{{%  column width="30%" %}}
![term_primary_backup_text_data_grid.gif](/attachment_files/term_primary_backup_text_data_grid.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A Data Grid with a primary instance and one or more backup instances.
Destructive operations (write, update and take) are applied to the primary instance, and are replicated to the backup instance either synchronously or asynchronously.
{{% /column%}}
{{%  /section %}}



{{%  anchor Partitioned Data Grid %}}

### Partitioned Data Grid

{{%  section %}}
{{%  column width="30%" %}}
![term_partitioned_data_grid.gif](/attachment_files/term_partitioned_data_grid.gif)
{{%  /column %}}
{{%  column width="70%" %}}
Each Data Grid instance (partition) holds a different subset of the objects in the Data Grid.
When the objects are written to this Data Grid, they are routed to the proper partition, according to a predefined attribute in the object that acts as the [routing](#Routing) index.
{{%  /column %}}
{{%  /section %}}


{{%  anchor Routing %}}

### Routing

{{%  section %}}
{{%  column width="30%" %}}
![term_routing.gif](/attachment_files/term_routing.gif)
{{%  /column %}}
{{%  column width="70%" %}}
The mechanism that is in charge of routing the objects into and out of the corresponding partitions.
The routing is based on a designated attribute inside the objects that are written to the space, and is termed _Routing Index_.
{{%  /column %}}
{{%  /section %}}



{{%  anchor Primary Backup Partitioned Data Grid %}}

### Primary Backup Partitioned Data Grid

{{%  section %}}
{{%  column width="30%" %}}
![term_partitioned_primary_backup_data_grid.gif](/attachment_files/term_partitioned_primary_backup_data_grid.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A [partitioned Data Grid](#Partitioned Data Grid), with one or more [backup](#Primary Backup Data Grid) instances for each partition. Each of the Data Grid instances (partitions) holds a different subset of the objects in the Data Grid, and replicates this subset to its backup instance/s.
{{%  /column %}}
{{%  /section %}}

{{%  refer %}}
For details about scaling a running space cluster **in runtime**, refer to the [Elastic Processing Unit]({{% latestjavaurl%}}/elastic-processing-unit-overview.html) section.
{{%  /refer %}}


# Runtime Components

## Processing Unit Container

{{%  anchor Processing Unit Container %}}

{{%  section %}}
{{%  column width="30%" %}}
![term_puc.gif](/attachment_files/term_puc.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A container that hosts a [Processing Unit](#Processing Unit).
The Processing Unit can run only inside a hosting Processing Unit Container.
{{%  /column %}}
{{%  /section %}}


## Types of Processing Unit Containers

### Integrated Processing Unit Container

{{%  section %}}
{{%  column width="30%" %}}
![term_ipuc.gif](/attachment_files/term_ipuc.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A container that runs the Processing Unit inside an IDE (e.g. IntelliJ IDEA, Eclipse).
{{%  /column %}}
{{%  /section %}}


{{%  anchor SGPUC %}}

### Service Grid Processing Unit Container (SLA Driven Container)

{{%  section %}}
{{%  column width="30%" %}}
![term_gscnet.gif](/attachment_files/term_gscnet.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A Processing Unit Container which runs within a [Grid Service Container](#GSC).
It enables running the processing unit within a [service grid](#service-grid), which provides self-healing and SLA capabilities to components deployed on it.
{{% /column%}}
{{%  /section %}}



{{%  anchor Service Grid %}}

### Service Grid

{{%  section %}}
{{%  column width="30%" %}}
![term_empty_service_grid.gif](/attachment_files/term_empty_service_grid.gif)
{{%  /column %}}
{{%  column width="70%" %}}
A set of [GigaSpaces Containers (GSC)](#gsc) managed by a [GigaSpaces Manager](#gsm).
The containers host various deployments of [Processing Units](#Processing Unit) and [Data Grids](#Data Grid).
Each container can be run on a separate physical machine.
{{% /column%}}
{{%  /section %}}


{{%  anchor GSC %}}

### GigaSpaces Container (GSC)

{{%  section %}}
{{%  column width="30%" %}}
![term_gsc.jpg](/attachment_files/term_gsc.jpg)
{{%  /column %}}
{{%  column width="70%" %}}
A [Service Grid](#service-grid) component which hosts [Processing Unit](#processing-unit) instances.
A machine can run one or more [GSC](#gsc) processes. Each GSC communicates with a manager component [GSM](#gsm). The GSC receives requests to start/stop a processing unit instance, and sends information about the machine which runs it (OS, processor architecture, current memory and CPU stats), the software installed on it and the status of processing unit instances currently running on it.
{{% /column%}}
{{%  /section %}}



{{%  anchor GSM %}}

### GigaSpaces Manager (GSM)

{{%  section %}}
{{%  column width="30%" %}}
![term_gsm.gif](/attachment_files/term_gsm.gif)
{{%  /column %}}
{{%  column width="70%" %}}
The [GSM](#gsm) is a [Service Grid](#service-grid) component which manages a set of [GigaSpaces Containers (GSC)](#gsc).
A GSM has an API for deploying/un deploying processing units. When a GSM is instructed to deploy a Processing Unit, it finds an appropriate, available GSC and tells that GSC to run an instance of that processing unit. It then continuously monitors that processing unit instance to verify that it's alive, and that the SLA is not breached.
{{%  /column %}}
{{%  /section %}}



{{%  anchor Management UI %}}

# Management Component

{{%  section %}}
{{%  column width="30%" %}}
![term_management_ui.gif](/attachment_files/term_management_ui.gif)
{{%  /column %}}
{{%  column width="70%" %}}
The GigaSpaces Management Center, also known as the GigaSpaces UI or GS-UI.
A monitoring, management and deployment console.
Enables the user to view and interact with the runtime components running in the network.
{{%  /column %}}
{{%  /section %}}


