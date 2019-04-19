---
type: post122
title:  Virtualized OS
categories: XAP122PROD
parent: none
weight: 200
canonical: auto
---


For information on the platforms supported by InsightEdge and XAP, refer to [Supported Platforms](../rn/supported-platforms.html) in the version release notes.

# Configuration

When setting up a virtualized operating system, ensure that the following is configured:

- Only Type 1 Hypervisor is recommended for production use.
- The vCPU may be over-subscribed, if it is under-utilized (less than 50%). In environments with high CPU utilization, vCPU must be reserved (pinned).
- Hyper-threading should be enabled.
- vMEM must be reserved (pinned).

# Other Considerations

The following additional planning issues should be taken into account:

- Do not over-commit virtual memory.
- Reserve memory at the virtual machine level.
- When using replication, implement [anti-affinity](https://pubs.vmware.com/vsphere-51/index.jsp#com.vmware.vsphere.resmgmt.doc/GUID-94FCC204-115A-4918-9533-BFC588338ECB.html?resultof=%2522%2541%256e%2574%2569%252d%2541%2566%2566%2569%256e%2569%2574%2579%2522%2520%2522%2552%2575%256c%2565%2573%2522%2520%2522%2572%2575%256c%2565%2522%2520) rules to ensure that the primary and backup nodes do not share the same virtual and machine physical host. 
- Reserve sufficient memory for the operating system (~2GB per VM).

