---
type: post100
title:  Overview
categories: XAP100
parent: elastic-processing-unit-overview.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}


# Overview

An Elastic Processing Unit (EPU) is a Processing Unit with additional capabilities that simplify its deployment across multiple machines. Containers and machine resources such as Memory and CPU are automatically provisioned based on Memory and CPU requirements. When a machine failure occurs, or when scale requirements change, new machines are provisioned and the Processing Unit deployment distribution is balanced automatically. The PU scale is triggered by modifying the requirements through an API call. From that point in time the EPU continuously maintains the specified capacity (indefinitely, or until the next scale trigger).


The EPU has following features:


- SLA based deployment where required memory and cores can be specified.
- Automatic container lifecycle management.
- Automatic re-balancing (repartitioning).
- Automatic partition count calculation.
- Scale up/down or in/out without system downtime.
- Eager and manual scale strategies.
- Automatic machine provisioning plug-in.



Basic steps when using the EPU:

- Start XAP agents
- Deploy PU
    - Specify maximum PU capacity
    - Specify container memory capacity
    - Specify initial PU capacity

- Scale up/down or in/out
- Undeploy

Here is a simple example scaling a running EPU. With the following illustration the system using initially 2 machines, 20 partitions , 20 instances per machine (40 instances total), 4 instances per GSC, GSC capacity is **8GB**. Total memory capacity **80** GB.:

![epu1.jpg](/attachment_files/epu1.jpg)

After scaling it to leverage 10 machines, we will have 4 instances per machine, 1 instance per GSC. Total memory capacity **400** GB.

![epu2.jpg](/attachment_files/epu2.jpg)


{{% refer %}}
For a quick start follow the [Elastic Processing Unit deployment example](./deploying-onto-the-service-grid.html#ElasticProcessingUnitDeploymentusingtheAdminAPI).
When using the EPU, GigaSpaces manage the **entire** life cycle of the container. Once the EPU is deployed containers are started and the EPU instances are provisioned into these containers. When the EPU scales up, additional containers are started and instances are relocated into these containers. When the EPU is un-deployed, all the containers associated with the EPU are automatically terminated.
{{% /refer %}}

This guide will cover the following topics:

- The **EPU Deployment** section describes the required deployment parameters and capacity planning considerations.
- The **Scale Triggers** section describes how to scale a PU after it has been deployed.
- The **Machine Provisioning**, describes how to start the GigaSpaces agent on each machine, plug-in development for different cloud providers and the algorithm that re-balances the PU across the machines.



# Considerations

- When deploying an existing EPU (redeploy), the system does not ignore the scale parameters. A `ProcessingUnitAlreadyDeployedException` will be thrown, but the scale call will be executed. Future releases will throw `ProcessingUnitAlreadyDeployedException` without executing the scale call.
- The speed up the deployment process you should increase the `maxConcurrentRelocationsPerMachine` parameter to have a larger number than 1 (default value). Having a value of 2 or 3 might speed up the deploy time when having multiple machines.
- Scaling EPU should be done using multipliers that match the amount of the initial capacity. This will allow the system to allocate the exact memory/cores requested.
- When deploying on a Single Machine (using the `singleMachineDeployment` mode) you should make sure the machine has enough memory/cores resources for the entire EPU instances. The `reservedMemoryCapacityPerMachine` should be used to ensure relevant resources. Without having these set, the deploy process will fail.
- In case of a failure with the deploy process due to insufficient resources or other reasons, the ESM will retry to deploy the EPU. To stop this activity, you should explicitly undeploy the EPU. You may use the UI, CLI or API to undeploy it.
- To verify a successful deployment, you should check the PU status (see the `org.openspaces.admin.pu.ProcessingUnit.getStatus()` and the `org.openspaces.admin.pu.ProcessingUnit.getProcessingUnitStatusChanged()`). You may also check the total amount of memory/cores utilized by the EPU by iterating the PU instances.
- To monitor the EPU deployment you should monitor the ESM. A simple way to do that is to review its log file. You may use the GS-UI to have direct access to the ESM log files.
