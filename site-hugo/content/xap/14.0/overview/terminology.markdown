---
type: post140
title: Terminology
categories: XAP140OVW
parent: none
weight: 300
---

 


# Basic Components

{{%  anchor Space %}}

## Space

{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/space.png" width=140" height="90" />
{{%  /column %}}
{{%  column width="70%" %}}
The GigaSpaces cache instance that holds data objects in memory.
{{%  /column %}}
{{%  /section %}}

{{%  anchor Execute-Read-Write-Take-and-Notify %}}

## Execute, Read, Write, Take and Notify

{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/operations.png"  />
{{%  /column %}}
{{%  column width="70%" %}}
A set of methods used to read, write, take, and register for notification on objects that are stored in the Space. Execute allows sending Tasks to be executed within the Space. Read and Take criteria can be specified via a query or a template (an example object).
{{%  /column %}}
{{%  /section %}}


{{%  anchor Processing Unit %}}

# Processing Unit

{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/processing_unit.png" width=154" height="103" />
{{%  /column %}}
{{%  column width="70%" %}}
A combination of clients and/or an embedded Space instance. This is the fundamental unit of deployment in GigaSpaces XAP. The Processing Unit itself is typically deployed onto the Service Grid. When a Processing Unit is deployed, a **Processing Unit instance** is the actual runtime entity.
{{%  /column %}}
{{%  /section %}}

## Processing Unit Configured with an Embedded Space

{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/PU_space.png" width=154" height="103" />
{{%  /column %}}
{{%  column width="70%" %}}
A deployable package that instantiates an embedded Space instance, also called a data grid instance. A set of embedded Space instances that run within the Processing Units typically form a data grid.
{{%  /column %}}
{{%  /section %}}


## Processing Unit Configured with One or More Services

{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/PU_services.png"  />
{{%  /column %}}
{{%  column width="70%" %}}
A deployable package containing one or more services. In the GigaSpaces context, it usually acts as a client that interacts with other Processing Units by utilizing the messaging capabilities of the Space.
{{%  /column %}}
{{%  /section %}}


## Processing Unit Configured with an Embedded Space and Embedded Services
{{%  section %}}
{{%  column width="30%" %}}
<img src="/attachment_files/overview/PU_spaces_services.png" width=154" height="103" />
{{%  /column %}}
{{%  column width="70%" %}}
A deployable, independent, scalable unit that is the building block of [Space-Based Architecture](./space-based-architecture.html). A client application (which can also be other Processing Units) write objects to the Space, and the Processing Unit that contains this Space consumes these objects or is notified about them and triggers related services.
{{%  /column %}}
{{%  /section %}}


# Data Grid


{{%  anchor Data Grid %}}

## In-Memory Data Grid (IMDG)

A set of Space instances, typically running within their respective processing unit instances. The space instances are connected to each other to form a space cluster. The relations between the spaces define the data grid topology.


{{%  anchor Data Grid Topology %}}

## Data Grid Topologies

{{%  anchor Partitioned Data Grid %}}

### Partitioned Data Grid

{{%  section %}}
{{%  column width="40%" %}}
<img src="/attachment_files/overview/partitioned_data_grid.png"  />
{{%  /column %}}
{{%  column width="60%" %}}
Each Data Grid instance (partition) holds a different subset of the objects in the data grid. When the objects are written to this data grid, they are routed to the proper partition according to a predefined attribute in the object that acts as the routing index.
{{%  /column %}}
{{%  /section %}}


{{%  anchor Routing %}}

### Routing

{{%  section %}}
{{%  column width="40%" %}}
<img src="/attachment_files/overview/routing.png"  />
{{%  /column %}}
{{%  column width="60%" %}}
The mechanism that is in charge of routing the objects into and out of the corresponding partitions. The routing is based on a designated attribute inside the objects that are written to the Space, called the _Routing Index_.
{{%  /column %}}
{{%  /section %}}



{{%  anchor Primary Backup Partitioned Data Grid %}}

### Partitioned Data Grid with High Availability

{{%  section %}}
{{%  column width="45%" %}}
<img src="/attachment_files/overview/partitioned_data_grid_HA.png"  />
{{%  /column %}}
{{%  column width="55%" %}}
A partitioned data grid, with one or more backup instances for each partition. Each data grid instance (partition) holds a different subset of the objects in the data grid, and replicates this subset to its backup instance/s.
{{%  /column %}}
{{%  /section %}}


# Runtime Components

## Processing Unit Container

{{%  anchor Processing Unit Container %}}

A container that hosts a Processing Unit. The Processing Unit can run only inside a hosting Processing Unit Container.


## Types of Processing Unit Containers

### Integrated Processing Unit Container

A container that runs the Processing Unit inside an IDE (e.g. IntelliJ IDEA, Eclipse).

{{%  anchor SGPUC %}}

### Service Grid Processing Unit Container (SLA-Driven Container)

A Processing Unit Container that runs within a Grid Service Container. It enables running the Processing Unit within a service grid, which provides self-healing and SLA capabilities to components deployed on it.


{{%  anchor Service Grid %}}

### Service Grid

A set of Grid Service Containers (GSCs) managed by a Grid Service Manager. The containers host various deployments of Processing Units and data grids.
Each container can be run on a separate physical machine.

{{%  anchor GSC %}}

### Grid Service Container (GSC)

A service grid component that hosts Processing Unit instances. A machine can run one or more GSC processes. Each GSC communicates with a manager component (GSM). The GSC receives requests to start/stop a Processing Unit instance, and sends information about the machine that runs it (operating system, processor architecture, current memory and CPU stats), the software installed on it, and the status of Processing Unit instances currently running on it.


{{%  anchor GSM %}}

### Grid Service Manager (GSM)

The GSM is a service grid component that manages a set of Grid Service Containers (GSCs). A GSM has an API for deploying/undeploying Processing Units. When a GSM is instructed to deploy a Processing Unit, it finds an appropriate, available GSC and tells that GSC to run an instance of that Processing Unit. It then continuously monitors that Processing Unit instance to verify that it is alive, and that the SLA is not breached.





