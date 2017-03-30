---
type: post121
title:  XAP Manager
categories: XAP121ADM, PRM
parent: runtime-configuration.html
weight: 250
---

The XAP Manager (or simply The Manager) is a component which stacks together the [LUS](service-grid.html#lus) and [GSM](service-grid.html#gsm) 
along with [Zookeeper](zookeeper.html) and an embedded web application which hosts an admin instance with a [RESTful management API](xap-manager-rest.html) on top of it.

In addition to simplifying setup and management, the Manager also provides the following benefits:

* Space leader election will use zookeeper instead of LUS, providing a more robust process (consistent when network partitions occur), and eliminating [split brains](./split-brain-and-primary-resolution.html).
- When using MemoryXtend, last primary will automatically be stored in Zookeeper (instead of you needing to setup a shared NFS and configure the PU to use it)
- The GSM will use Zookeeper for leader election (instead of an active-active topology used today). This provides a more robust process (consistent when network partitions occur). Also, having a single leader GSM means that the general behaviour is more deterministic and logs are easier to read.
- RESTful API for managing the environment remotely from any platform.

# Standalone Manager
In a development environment, we use a standalone manager. To start the Manager, simply run the following command:

{{%tabs%}}
{{%tab "Unix"%}}
```bash
./gs-agent.sh --manager-local
```
{{%/tab%}}
{{%tab "Windows"%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%/tabs%}}


You’ll notice:

- The manager log file In `${XAP_HOME}/logs`, which shows that LUS, Zookeeper, GSM and REST API have started and various other details about them.
- Zookeeper files reside in ${XAP_HOME}/work/manager/zookeeper (shown in log file)
- REST API is available in [localhost:8090](http://localhost:8090) (shown in log file)

# Multiple Managers on multiple hosts
In a production environment, we need to start a cluster of managers on multiple hosts. In this scenario you need at least 3 machines (odd number is required to ensure quorum during network partitions). 
Suppose you’ve decided machines alpha, bravo and charlie to host the managers:

1. Edit the setenv-overrides.sh/bat script and set `XAP_MANAGER_SERVERS` to the list of hosts. For example: `export XAP_MANAGER_SERVERS=alpha,bravo,charlie`
2. Copy the modified `setenv-overrides.sh` to each machine which runs a `gs-agent`.
3. Run `gs-agent --manager` on the manager machines (alpha, bravo, charlie, in this case).

Port configurations:

|Port   |System property |Default  |
|-------|----------------|---------|
|REST |com.gs.manager.rest.port| 8090|
|Zookeeper |com.gs.manager.zookeeper.discovery.port<br>com.gs.manager.zookeeper.leader-election.port |2888<br>3888|
|Lookup Service|com.gs.multicast.discoveryPort|4174 |

{{%note "Note:"%}}
Zookeeper requires that each manager can reach any other manager. 
If you are changing the Zookeeper ports, make sure you use the same port on all machines. 
If that is not possible for some reason, you may specify the ports via the `XAP_MANAGER_SERVERS` environment variable. 
{{%/note%}}

For example:

```bash
XAP_MANAGER_SERVERS=alpha;zookeeper:2000:3000;lus=4242,bravo;zookeeper:2100:3100,charlie;zookeeper:2200:3200
```
# Backwards Compatibility

The Manager is offered side-by-side with the existing stack (GSM, LUS, etc.). We think this is a better way of working with XAP, and we want new users and customers to work solely with it. 
On the same note we understand that it requires some effort from existing users which upgrade to 12.1 (probably not too much, mostly on changing the scripts they use to start the environment), 
so if you’re upgrading for bug fixes/other features and don’t want the manager for now, you can switch from 12.0 to 12.1 and continue using the old components - it’s all still there.

#  Limitations/Known Issues

- Customers who need high availability are used to starting 2 GSM and 2 LUS - the manager requires an odd number of instances to ensure quorum (zookeeper)
- Starting more than 3 managers (e.g. 5) is currently not supported - this will lead to 5 GSM and 5 LUS, which will probably lead to noisy network. We haven’t tested it, so we blocked this in code. We might enhance this in future releases.
- Sticky session when using REST to maintain upload/deploy dependency






