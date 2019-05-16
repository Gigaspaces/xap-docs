---
type: post123
title:  VMWare Guidelines
categories: XAP123RN
parent: none
weight: 500
canonical: auto
---


 

GigaSpaces applications fully support the following:

- Using virtual machines to host InsightEdge and XAP applications and components.
- Deployment on virtualized environments running on VMWare Type 1 Hypervisors. This includes all major product features and APIs, including data access, messaging, code execution, local cache, remoting and persistency.

{{%note "Note"%}}
There are certain guidelines regarding virtual machines that should be followed when setting up the production environment, in order to ensure stability and optimal performance. For more information, refer to [JVM Tuning](../production/production-jvm-tuning.html).
{{%/note%}}

# Supported Versions

GigaSpaces applications support VMWare vSphere 5+ running the following guest operating systems:

- Microsoft Windows  Server 2008 SP2
- Linux RHEL 5.x/6.x
- Solaris 10

{{%warning "Important"%}}
SUSE-10 Linux Enterprise Service Pack 3 is not recommended, due to the instability of its network support layer.
{{%/warning%}}

# Configuration

- Only Type 1 Hypervisor is recommended for production use.
- vCPU may be over-subscribed, if it is under-utilized (less than 50%). In environments with high CPU utilization, vCPU must be reserved (pinned).
- Hyper-threading should be enabled.
- vMEM must be reserved (pinned).

# Other Considerations

- Do not over-commit virtual memory.
- Reserve memory at the virtual machine level.
- When using replication, use anti-affinity rules to ensure that primary and backup nodes do not share the same virtual machine and physical host.
- Reserve sufficient memory for the operating system (~2GB per virtual machine).

# References

High-performance settings should be used, per VMWare's recommendations [here](http://www.vmware.com/pdf/Perf_Best_Practices_vSphere5.0.pdf) and [here](http://www.vmware.com/files/pdf/techpaper/VMW-Tuning-Latency-Sensitive-Workloads.pdf).



