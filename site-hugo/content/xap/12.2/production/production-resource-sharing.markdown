---
type: post122
title:  Resource Sharing
categories: XAP122PROD
parent: none
weight: 500
canonical: auto
---

# Grid Management Service Infrastructure

There are numerous ways for different systems/applications/groups to share the same pool of servers (in a development or production environment) on the network.  A non-exhaustive list of some of the options is:

1. Outside of XAP:  Dedicated hardware for each group, where each set of servers runs an independent XAP runtime environment (Service Grid) without sharing the same server between different groups.  This naive approach is good for simple or temporary scenarios. When using this option, each XAP runtime environment is isolated from the others using [different XAP_LOOKUP_LOCATORS](./production-xap-considerations.html#running-multiple-locators) or [different XAP_LOOKUP_GROUPS](./production-xap-considerations.html#running-multiple-groups) value.

2. [Using Multiple Zones](./production-xap-considerations.html#running-multiple-zones):  A single XAP runtime environment spans all servers, where each group of XAP containers (across several machines) is labeled with a specific Zone.  Multiple Zones can used by different containers on the same server. For example,  server A has two containers labeled zoneX and four containers labeled zoneY, and server B has two containers labeled zoneX and four containers labeled zoneY.
At deployment time, application services (Processing Units) are deployed using a specific Zone. This instructs the system to provision the services into the corresponding containers.  Using multiple Zones breaks the runtime environment into different logical segments.

3. [Using Multiple Lookup Groups (multicast lookup discovery)](./production-xap-considerations.html#running-multiple-groups):  All servers run multiple XAP runtime environments, where each XAP container uses a specific lookup group when registering with the Lookup Service.  At deployment time, application services (Processing Units) are deployed using a specific lookup group. Using multiple lookup group breaks the infrastructure into different logical segments. The Lookup Group value is controlled via the `XAP_LOOKUP_GROUPS` environment variable. When using this option, ensure that multicast is enabled on all machines.

4. [Using Multiple Lookup Locators (unicast lookup discovery)](./production-xap-considerations.html#running-multiple-locators): All servers run multiple XAP runtime environments, where each XAP container uses a specific lookup locator when registering with the Lookup Service.  At deployment time, application services (Processing Units) are deployed using a specific lookup locator. Using multiple lookup locators breaks the infrastructure into different logical segments. If you have multiple lookup services running on the same server, each will use a different listening port. You can control this port using the `com.sun.jini.reggie.initialUnicastDiscoveryPort` system property. The Lookup Locators value is controlled via the `XAP_LOOKUP_LOCATORS` environment variable.

5. Using a shared XAP runtime environment: A single XAP runtime environment spans all servers, with no use of Zones or Lookup Groups/Locators.   Application services share the servers and allocation done in a random manner, without using any pre-defined segmentation.

For any of the above options, XAP exposes the ability to control a deployed application service in runtime, so that new application service instances can be created or existing instances can be relocated.  This tight operational control enables even more creative resource-sharing possibilities.

When devising the appropriate resource sharing strategy for your system, consider the breadth of operational requirements and application service characteristics. For example, it may be problematic to run two applications with variable load on a fixed-size shared environment if peak loads coincide.

GigaSpaces provides consultancy services for the environment-planning stage that addresses the above, as well as other considerations that affect your environment. For more information, refer to [GigaPro Services](http://www.gigaspaces.com/content/gigapro-full-services-offering-xap-customers)



