---
type: post121
title:  XAP Manager
categories: XAP121ADM, PRM
parent: runtime-configuration.html
weight: 250
canonical: auto
---

The XAP Manager (or simply The Manager) is a component which stacks together the [LUS](service-grid.html#lus) and [GSM](service-grid.html#gsm) 
along with {{%exurl "Apache ZooKeeper""http://zookeeper.apache.org/"%}} and an embedded web application which hosts an admin instance with a [RESTful management API](xap-manager-rest.html) on top of it.

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

{{%note "Remote Access"%}}
The local manager is intended for local usage on the developer's machine, hence it binds to `localhost`, and is not accessible from other machines. If you wish to start a manager and access it from other hosts, follow the procedure described in **High Availability** below with a single host.
{{%/note%}}

# High Availability

In a production environment, you'll probably want a cluster of managers on multiple hosts, to ensure high availability. You'll need 3 machines (odd number is required to ensure quorum during network partitions). For examplem, suppose you’ve selected machines alpha, bravo and charlie to host the managers:

1. Edit the `$XAP_HOME/bin/setenv-overrides.sh/bat` script and set `XAP_MANAGER_SERVERS` to the list of hosts. For example: `export XAP_MANAGER_SERVERS=alpha,bravo,charlie`
2. Copy the modified `setenv-overrides.sh/bat` to each machine which runs a `gs-agent`.
3. Run `gs-agent --manager` on the manager machines (alpha, bravo, charlie, in this case).

Note that starting more than one manager on the same host is not supported.

# Configuration

## Ports

The following ports can be modified using system properties, e.g. via the `setenv-overrides` script located in `$XAP_HOME/bin`:

|Port   |System property |Default  |
|-------|----------------|---------|
|REST |`com.gs.manager.rest.port`| 8090|
|Zookeeper |`com.gs.manager.zookeeper.discovery.port`<br>`com.gs.manager.zookeeper.leader-election.port` |2888<br>3888|
|Lookup Service|`com.gs.multicast.discoveryPort`|4174 |

{{%note "Note:"%}}
Zookeeper requires that each manager can reach any other manager. If you are changing the Zookeeper ports, make sure you use the same port on all machines. If that is not possible for some reason, you may specify the ports via the `XAP_MANAGER_SERVERS` environment variable.  For example:

```bash
XAP_MANAGER_SERVERS="alpha;zookeeper=2000:3000;lus=4242,bravo;zookeeper=2100:3100,charlie;zookeeper=2200:3200"
```

When using this syntax in unix/linux systems, make sure to wrap it in quotes (as shown), because of the semi-colons.
{{%/note%}}

## Zookeeper

ZooKeeper's behavior is governed by the ZooKeeper configuration file (`zoo.cfg`). When using XAP manager, an embedded Zookeeper instance is started using a default configuration located at `$XAP_HOME/config/zookeeper/zoo.cfg`. 
If you need to override the default settings, either edit the default file, or use the `XAP_ZOOKEEPER_SERVER_CONFIG_FILE` environment variable or the `com.gs.zookeeper.config-file` system property to point to your custom configuration file.
Default port of Zookeeper is 2181.

Additional information on Zookeeper configuration can be found at {{%exurl "ZooKeeper configuration""https://zookeeper.apache.org/doc/r3.4.9/zookeeperAdmin.html#sc_configuration"%}}.

# Backwards Compatibility

The Manager is offered side-by-side with the existing stack (GSM, LUS, etc.). We think this is a better way of working with XAP, and we want new users and customers to work solely with it. 
On the same note we understand that it requires some effort from existing users which upgrade to 12.1 (probably not too much, mostly on changing the scripts they use to start the environment), 
so if you’re upgrading for bug fixes/other features and don’t want the manager for now, you can switch from 12.0 to 12.1 and continue using the old components - it’s all still there.

{{%note "Note:"%}}
The Manager uses a different selection scheme when selecting resources where to deploy a processing unit instance. The 'LeastRoundRobinSelector' chooses the container which has the least amount of instances on it. To avoid choosing the same container twice when amounts equal, it keeps the containers in a round-robin order. This scheme is different from the previous 'WeightedSelector' which assigned weights to the containers based on heuristics and state gathered while selecting the less weighted container. The reason for this is that in large deployments, the network overhead and the overall deployment time is costly and may result in an uneven resource consumption.

Notice that you may be experiencing a different instance distribution than before, in some cases non-even. To force the use of the previous selector scheme, use the following system property:
```bash
Set '-Dorg.jini.rio.monitor.serviceResourceSelector=org.jini.rio.monitor.WeightedSelector' when loading the manager (in XAP_MANAGER_OPTIONS environment variable).
```
{{%/note%}}

# FAQ

### Q. Why do I need 3 managers? In previous versions 2 LUS + 2 GSM was enough for high availability

With an even number of managers, consistency cannot be assured in case of a network partitioning, hence the 3 managers requirement.

### Q. I want higher availability - can I use 5 managers instead of 3?

Theoretically this is possible (e.g. Zookeeper supports this), but currently this is not supported in XAP - starting 5 managers would also start 5 Lookup Services, which will lead to excessive chatinnes and performance drop. This issue is in our backlog, though - if it's important for you please contact support or your sales rep to vote it up.

### Q. Can I use a load balancer in front of the REST API?

Sure. However, make sure to use sticky sessions, as some of the operations (e.g. upload/deploy) take time to resonate to the other managers.
