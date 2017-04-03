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
* When using MemoryXtend, last primary will automatically be stored in Zookeeper (instead of you needing to setup a shared NFS and configure the PU to use it)
* The GSM will use Zookeeper for leader election (instead of an active-active topology used today). This provides a more robust process (consistent when network partitions occur). Also, having a single leader GSM means that the general behaviour is more deterministic and logs are easier to read.
* RESTful API for managing the environment remotely from any platform.

# Getting Started

The easiest way to get started is to run a standalone manager on your machine - simply run the following command:

{{%tabs%}}
{{%tab Linux%}}
```bash
./gs-agent.sh --manager-local
```
{{%/tab%}}
{{%tab Windows%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%/tabs%}}
 
In the manager log file (`$XAP_HOME/logs`), you can see:

* The maanger has started LUS, Zookeeper, GSM and REST API have started and various other details about them.
* Zookeeper files reside in `$XAP_HOME/work/manager/zookeeper`
* REST API is started on [localhost:8090](http://localhost:8090)

# High Availability

In a production environment, you'll probably want a cluster of managers on multiple hosts, to ensure high availability. You'll need 3 machines (odd number is required to ensure quorum during network partitions). For examplem, suppose you’ve selected machines alpha, bravo and charlie to host the managers:

1. Edit the `$XAP_HOME/bin/setenv-overrides.sh/bat` script and set `XAP_MANAGER_SERVERS` to the list of hosts. For example: `export XAP_MANAGER_SERVERS=alpha,bravo,charlie`
2. Copy the modified `setenv-overrides.sh/bat` to each machine which runs a `gs-agent`.
3. Run `gs-agent --manager` on the manager machines (alpha, bravo, charlie, in this case).

# Configuration

Port configurations:

|Port   |System property |Default  |
|-------|----------------|---------|
|REST |com.gs.manager.rest.port| 8090|
|Zookeeper |com.gs.manager.zookeeper.discovery.port<br>com.gs.manager.zookeeper.leader-election.port |2888<br>3888|
|Lookup Service|com.gs.multicast.discoveryPort|4174 |

{{%note "Note:"%}}
Zookeeper requires that each manager can reach any other manager. If you are changing the Zookeeper ports, make sure you use the same port on all machines. If that is not possible for some reason, you may specify the ports via the `XAP_MANAGER_SERVERS` environment variable.  For example:

```bash
XAP_MANAGER_SERVERS=alpha;zookeeper:2000:3000;lus=4242,bravo;zookeeper:2100:3100,charlie;zookeeper:2200:3200
```
{{%/note%}}

# Backwards Compatibility

The Manager is offered side-by-side with the existing stack (GSM, LUS, etc.). We think this is a better way of working with XAP, and we want new users and customers to work solely with it. 
On the same note we understand that it requires some effort from existing users which upgrade to 12.1 (probably not too much, mostly on changing the scripts they use to start the environment), 
so if you’re upgrading for bug fixes/other features and don’t want the manager for now, you can switch from 12.0 to 12.1 and continue using the old components - it’s all still there.

# FAQ

### Q. Why do I need 3 managers? In previous versions 2 LUS + 2 GSM was enough for high availability

With an even number of managers, consistency cannot be assured in case of a network partitioning, hence the 3 managers requirement.

### Q. I want higher availability - can I use 5 managers instead of 3?

Theoretically this is possible (e.g. Zookeeper supports this), but currently this is not supported in XAP - starting 5 managers would also start 5 Lookup Services, which will lead to excessive chatinnes and performance drop. This issue is in our backlog, though - if it's important for you please contact support or your sales rep to vote it up.

### Q. Can I use a load balancer in front of the REST API?

Sure. However, make sure to use sticky sessions, as some of the operations (e.g. upload/deploy) take time to resonate to the other managers.
