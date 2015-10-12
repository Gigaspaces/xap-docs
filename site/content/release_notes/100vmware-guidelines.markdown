---
type: postrel
title:  VMWare Guidelines
categories: RELEASE_NOTES
parent: xap100.html
weight: 750
---


{{%ssummary%}}{{%/ssummary%}}

GigaSpaces XAP fully supports deployment on virtualized environments running on VMWare Type 1 Hypervisors. This includes all major product featurs and APIs, including data access, messaging, code execution, local cache, remoting and persistency.

# Supported versions

GigaSpaces supports VMWare vSphere 5+ running the following guest operating systems:

- Windows 2008 Server SP2
- Linux RHEL 5.x/6.x
- Solaris 10

{{%warning%}}
SUSE-10 sp3 is not recommended, due to the instability of its network support layer.
{{%/warning%}}

# Configuration

- Only Type 1 Hypervisor is recommended for production use.
- vCPU may be over-subscribed, if it is under-utilized (less than 50%). In environments with high CPU utilization, vCPU must be reserved (pinned).
- Hyper-threading should be enabled.
- vMEM must be reserved (pinned).

# Other considerations

- Do not over-commit virtual memory
- Reserve memory at the virtual machine level
- When using replication, use anti-affinity rules to ensure that primary and backup nodes do not share the same virtual machine and physical host.
- Reserve sufficient memory for the operating system (~2GB per VM)

# References

High-performance settings should be used, per VMWare's recommendations [here](http://www.vmware.com/pdf/Perf_Best_Practices_vSphere5.0.pdf) and [here](http://www.vmware.com/files/pdf/techpaper/VMW-Tuning-Latency-Sensitive-Workloads.pdf).


