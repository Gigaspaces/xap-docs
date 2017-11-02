---
type: post
title:  Observable WAN
categories: SBP
parent: wan-based-deployment.html
weight: 500
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| DeWayne Filppi| 8.0.4 | March 2012|    |    |



# Overview
With the introduction of the Gigaspaces multi-site replication feature, keeping different Gigaspaces deployments in sync is easy.  However, detecting and anticipating communication problems between sites where it counts (at the application level), isn't so easy.  This solution/pattern uses some simple concepts and components to enable easy, flexible monitoring of the replication performance from multiple sites.

The basic idea is to write timestamps into spaces that are being replicated using the multi-site mechanism. The timestamps then flow from site to site, intermingled with application domain objects.  Each site then polls it's site for timestamps arriving from remote sites.  From information in the timestamp, as well as noticing when the timestamp arrives, a picture of replication performance can be deduced.  Each site then maintains a global replication status object with 3 states: up, down, and degraded.  Interested parties can attach event listeners to state changes on the replication status object.  
Refer to my recent blog [post](https://blog.gigaspaces.com/assured-wan-replication-with-latency-measures/) for more detail.

{{% align center %}}![observablewan.png](/attachment_files/sbp/observablewan.png){{% /align %}}

# Getting the project
The mirror project is held on github in the [patterns](https://github.com/Gigaspaces/bestpractices) project. This is an umbrella repository; the specific project is in the observable-wan directory under the root directory.

## Building the project

The project has a Maven2 pom structure.  A "mvn install" at the root directory will build everything.  Currently, the project depends on XAP 8.0.4.  You'll have to install XAP 8.0.4 gs-openspaces.jar and gs-runtime.jar into your local repo.  Versions more recent than 8.0.4 should work as well, but haven't been tested.  The space and gateway projects are currently configured to use "localhost" in a 2 site, multi-master configuration.  For a more realistic run, install the site1 and site2 components on grids on two different hosts (with different lookup groups).

## Running the project

Deployment should occur in the following order:

1. trans-space-site1-pu
2. trans-space-site2-pu
3. trans-gateway-site1
4. trans-gateway-site2

At this point you should see the replication channels linking up.  You'll also see logging indicating that there are no TimeRecords.  This is normal.  Next deploy the time writers:

1. site1-time-pu
2. site2-time-pu

Timestamps will now flow into both "sites". You will notice logging indicating that the remote site is "DOWN", until 10 samples arrive.  Then it will change the status to "UP".  After everything is "UP", you can experiment by undeploying the gateways and watching the status degrade to "DOWN" through "DEGRADED".  If running on a multi-site setup, you can try interfering with the network (unplugging it) to simulate connectivity problems.
