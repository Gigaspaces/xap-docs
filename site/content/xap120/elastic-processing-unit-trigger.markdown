---
type: post120
title:  Scale Triggers
categories: XAP120
parent: elastic-processing-unit-overview.html
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}





# Manual Capacity Scale Trigger

The system administrator may specify the memory and/or CPU core resources required for the processing unit in production. This should be specified during the deployment time, and could be specified also anytime after the deployment. The memory capacity trigger affects the number of provisioned containers. If there are not enough machines to host the provisioned containers the trigger also affects the number of provisioned machines. The number of CPUs affect directly the number of provisioned machines (even if it means that some of the machines have unused memory).

When specifying both memory and cores capacity requirements as part of the deploy and scale routines, the EPU will **be deployed successfully** only when **both memory and cores** resources can be allocated (sufficient amount of memory and cores across the available machines). If you would like to the memory capacity requirements to take precedence on the cores capacity requirements, have lower values for the cores capacity requirements than the exact existing cores count.


# Example

Here is an example how you can scale a deployed EPU memory and CPU capacity.

#### Step 1 - Deploy the PU:

We deploy the PU having 512GB as the maximum total amount of memory utilized both for primary and backup instances where the entire system should consume maximum of 32 cores. At start only 128GB and 8 cores will be utilized.


```java
ProcessingUnit pu = gsm.deploy(
        new ElasticStatefulProcessingUnitDeployment(new File("myPU.jar"))
           .memoryCapacityPerContainer(16,MemoryUnit.GIGABYTES)
           .maxMemoryCapacity(512,MemoryUnit.GIGABYTES)
           .maxNumberOfCpuCores(32)
           // set the initial memory and CPU capacity
           .scale(new ManualCapacityScaleConfigurer()
                  .memoryCapacity(128,MemoryUnit.GIGABYTES)
                  .numberOfCpuCores(8)
                  .create())
);

// Wait until the deployment is complete.
pu.waitForSpace().waitFor(pu.getTotalNumberOfInstances());
```

#### Step 2 - Increase the memory capacity from 128GB to 256GB and number of cores from 8 to 16:


```java
ProcessingUnit pu = admin.getProcessingUnits().waitFor("myPU", 5,TimeUnit.SECONDS); //get the PU
// increasing the memory capacity will start new containers
// existing machines if enough free memory is available
pu.scale(new ManualCapacityScaleConfigurer()
         .memoryCapacity(256,MemoryUnit.GIGABYTES)
         .numberOfCpuCores(16)
         .create());
```

#### Step 3 - Increase the memory capacity from 256GB to 512GB and number of cores from 16 to 32:


```java
ProcessingUnit pu = admin.getProcessingUnits().waitFor("myPU", 5,TimeUnit.SECONDS); //get the PU
// scales out to more CPU cores (existing containers are terminated on existing machines and
// new are started on new machines if not enough CPU cores are available on existing machines)
pu.scale(new ManualCapacityScaleConfigurer()
         .memoryCapacity(512,MemoryUnit.GIGABYTES)
         .numberOfCpuCores(32)
         .create());
```

#### Step 4 - Decrease the memory capacity and CPU capacity:


```java
ProcessingUnit pu = admin.getProcessingUnits().waitFor("myPU", 5,TimeUnit.SECONDS); //get the PU
pu.scale(new ManualCapacityScaleConfigurer()
         .memoryCapacity(128,MemoryUnit.GIGABYTES)
         .numberOfCpuCores(8)
         .create());
```

# Eager Scale Trigger

Eager trigger scales the EPU on all available machines and new machines joining the GigaSpaces Grid. Each new machine running a GigaSpaces agent automatically starts a new container hosting the EPU partition instance(s) relocated from some other container. To use the Eager Scale Trigger you should scale the EPU using the `EagerScaleConfigurer`:


```java
pu.scale(new EagerScaleConfigurer().create());
```

The Eager trigger has the following limitations:

- Number of maximum machines is limited to the number of partitions calculated/defined during the deployment time. This limitation does not exist for stateless processing units.
- Multiple Eager EPUs can run on the same [Lookup Service](/product_overview/service-grid.html#lus) but on different machines. Machines are marked by starting a Grid Service Agent with a specific zone (With a command line argument -Dcom.gs.zones=zone1).


```java
ProcessingUnit pu1 = gsm.deploy(
		new ElasticSpaceDeployment("eagerspace1")
		.maxMemoryCapacity(10, MemoryUnit.GIGABYTES)
		.memoryCapacityPerContainer(1, MemoryUnit.GIGABYTES)

                // discover only agents with "zone1"
		.dedicatedMachineProvisioning(
			new DiscoveredMachineProvisioningConfigurer()
                           .addGridServiceAgentZone("zone1")
                           .removeGridServiceAgentsWithoutZone()
                           .create())

                // eager scale
		.scale(new EagerScaleConfigurer()
		       .create())
);

ProcessingUnit pu2 = gsm.deploy(
		new ElasticSpaceDeployment("eagerspace2")
		.maxMemoryCapacity(10, MemoryUnit.GIGABYTES)
		.memoryCapacityPerContainer(1, MemoryUnit.GIGABYTES)

                //discover only agents with "zone2"
		.dedicatedMachineProvisioning(
			new DiscoveredMachineProvisioningConfigurer()
	                  .addGridServiceAgentZone("zone2")
                          .removeGridServiceAgentsWithoutZone()
	                  .create())

                //eager scale
		.scale(new EagerScaleConfigurer()
		       .create())
);
```

<br>

{{%info "Eager scale trigger verses Manual capacity trigger"%}}
The differences between the Eager scale trigger and a Manual capacity trigger in terms of the maximum amount of memory and CPU are:

- **Manual** capacity trigger expects the administrator to start enough available machines running the GigaSpaces agent to satisfy the specified capacity. Since it expects a new machine to be started, it does not balance the Processing Unit instances nor it does start new containers, until the machines are started. **Eager** trigger, on the other hand, redeploys as best as it can on the available machines, and scales out only when another machine is started or until the max capacity is reached.
- The **Eager** trigger spreads out thin if enough machines are available (to gain as much CPU resources as possible). **Manual** trigger spreads out to new machines, before existing machines' memory is utilized, only when the `numberOfCpu` property is high enough.

{{%/info%}}