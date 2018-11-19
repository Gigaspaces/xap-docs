---
type: post140
title:  Elastic Processing Unit
categories: XAP140,PRM
parent: none
weight: 2100
---

{{%note%}}
The Elastic Processing Unit is deprecated as of version 12.1, as noted in the [Release Notes](http://localhost:1313/release_notes/121upgrading.html).
{{%/note%}}

An Elastic Processing Unit (EPU) is a Processing Unit with additional capabilities that simplify its deployment across multiple machines. Containers and machine resources such as Memory and CPU are automatically provisioned based on Memory and CPU requirements. When a machine failure occurs, or when scale requirements change, new machines are provisioned and the Processing Unit deployment distribution is balanced automatically. The PU scale is triggered by modifying the requirements through an API call. From that point in time, the EPU continuously maintains the specified capacity (indefinitely, or until the next scale trigger).


The EPU has following features:

- SLA-based deployment where required memory and cores can be specified.
- Automatic container lifecycle management.
- Automatic re-balancing (repartitioning).
- Automatic partition count calculation.
- Scale up/down or in/out without system downtime.
- Eager and manual scale strategies.
- Automatic machine provisioning plug-in.



Basic steps when using the EPU:

1. Start XAP agents
1. Deploy PU
    - Specify maximum PU capacity
    - Specify container memory capacity
    - Specify initial PU capacity

1. Scale up/down or in/out
1. Undeploy

The following is a simple example scaling a running EPU. In the diagraj, the system is initially using 2 machines, 20 partitions, 20 instances per machine (40 instances total), 4 instances per GSC, and the GSC capacity is **8GB**. Total memory capacity **80** GB.:

{{% align center%}}
![epu1.jpg](/attachment_files/epu1.jpg)
{{% /align%}}

After scaling it to leverage 10 machines, we have 4 instances per machine, and 1 instance per GSC. Total memory capacity is **400** GB.

{{% align center%}}
![epu2.jpg](/attachment_files/epu2.jpg)
{{% /align%}}

{{% note "Note"%}}
For a quick start, follow the [Elastic Processing Unit deployment example](./deploying-onto-the-service-grid.html#elastic-processing-unit-deployment-using-the-admin-api).
When using the EPU, XAP manages the **entire** life cycle of the container. When the EPU is deployed, containers are started and the EPU instances are provisioned into these containers. When the EPU scales up, additional containers are started and instances are relocated into these containers. When the EPU is un-deployed, all the containers associated with the EPU are automatically terminated.
{{% /note %}}

This section discusses the following topics:

- [Deployment](./elastic-processing-unit-deploy.html) - describes the required deployment parameters and capacity planning considerations.
- [Scale Triggers](./elastic-processing-unit-trigger.html) - describes how to scale a PU after it has been deployed.
- [Machine Provisioning](./elastic-processing-unit-provisioning.html) - describes how to start the XAP agent on each machine, plug-in development for different cloud providers, and the algorithm that re-balances the PU across the machines.

# Considerations

- When deploying an existing EPU (redeploy), the system does not ignore the scale parameters. A `ProcessingUnitAlreadyDeployedException` is thrown, but the scale call is executed. 
- To speed up the deployment process, increase the `maxConcurrentRelocationsPerMachine` parameter to a value larger than 1 (the default value). Assigning a value of 2 or 3 can reduce the deploy time when there are multiple machines.
- Scaling EPUs should be done using multipliers that match the amount of the initial capacity. This will allow the system to allocate the exact memory/cores requested.
- When deploying on a Single Machine (using `singleMachineDeployment` mode) ensure that the machine has enough memory/cores resources for the all the EPU instances. The `reservedMemoryCapacityPerMachine` should be used to ensure relevant resources. Without having these set, the deploy process will fail.
- If the deploy process fails due to insufficient resources or other reasons, the ESM will try to redeploy the EPU. To stop this activity, you should explicitly undeploy the EPU using the GigaSpaces Management Center, CLI, or API.
- To verify a successful deployment, check the PU status (see the `org.openspaces.admin.pu.ProcessingUnit.getStatus()` and the `org.openspaces.admin.pu.ProcessingUnit.getProcessingUnitStatusChanged()`). You can also check the total amount of memory/cores utilized by the EPU by iterating the PU instances.
- To monitor the EPU deployment you should monitor the ESM. A simple way to do this is reviewing its log file. You can use the GigaSpaces Management Center to gain direct access to the ESM log files.
