---
type: post121
title:  Overview
categories: XAP121ADM, PRM
parent: ugm.html
weight: 100
---



{{%warning%}}
This page Is under construction
{{%/warning%}}


The XAP Unified Grid Manager (UGM) is a component which stacks together the [LUS](/product_overview/service-grid.html#the-lookup-service-lus) and [GSM](/product_overview/service-grid.html#grid-service-manager-gsm) 
along with [Zookeeper]({{%currentadmurl%}}/zookeeper.html) and an embedded web application which hosts an admin instance with a REST API on top of it. 
It simplifies setup and management of the XAP environment.

In addition to simplicity, the UGM also provides the following benefits:

- Space leader election will automatically use zookeeper instead of LUS, providing a more robust process (consistent when network partitions occur)
- When using MemoryXtend, Zookeeper will implicitly be used to store the last primary (instead of you needing to setup a shared NFS and configure the PU to use it)
- The GSM will use Zookeeper for leader election (instead of an active-active topology used today). This provides a more robust process (consistent when network partitions occur). Also, having a single leader GSM means that the general behaviour is more deterministic and logs are easier to read. This eliminates the [split brain problem](./split-brain-and-primary-resolution.html).
- REST API for managing the environment remotely.


# Standalone Manager
In a development environment, we use a standalone manager. To start the UGM, simply run the following command:

{{%tabs%}}
{{%tab "Windows"%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%tab "Unix"%}}
```bash
./gs-agent.sh --manager-local
```
{{%/tab%}}
{{%/tabs%}}

You’ll notice:<br>
- The manager log file In `${XAP_HOME}/logs`, which shows that LUS, Zookeeper, GSM and REST API have started and various other details about them.<br>
- Zookeeper files reside in ${XAP_HOME}/work/manager/zookeeper (shown in log file)<br>
- REST API is available in `http://localhost:8090/v1`. (shown in log file)


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

# Help with gs-agent syntax

Run the following command to get a complete list of options: 

```bash
>gs-agent.sh -h
```

{{%note%}}
The previous syntax is fully supported for backwards compatibility.
{{%/note%}}


# REST API

The Manager’s RESTful API was built with {{%exurl "Swagger" "http://swagger.io/"%}}. You can simply browse to `http://localhost:8090/v1` and start playing with the API. 
Swagger provides user-friendly documentation that lets you execute actual operations from within the api docs and view the respective `CURL` command you can run from a client/command line.
If you are familiar with the older Admin API, most of this will be self explanatory.

Run the following command to start the UGM:
 
{{%tabs%}}
{{%tab "Windows"%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%tab "Unix"%}}
```bash
./gs-agent.sh --manager-local
```
{{%/tab%}}
{{%/tabs%}}

After the UGM has started, you can view the Swagger UI at the following url `http://localhost:8090/v1`

![image](/attachment_files/rest-admin/rest-admin-1.png)


# Long running operations
In the Admin API, long running operations (e.g. deploy, undeploy, etc.) are asynchronous with no future or callback. 
You can either track progress manually, or use various ‘andWait’ overloads (e.g. deployAndWait). 
In the REST API we cannot use andWait, so you need to poll for completion, but each such operation behaves differently. 
What we’ve done is added the notion of Request - each long running operation creates a request and returns its id, so you can poll on the request id and check it’s status (running/successful/failed). 
The request exposes additional useful information:

- Who started the request (ip and user)
- When was the request started and completed
- If the request failed, an error message
- Additional properties on the request execution

By default, the system keeps up to 100,000 completed requests (configurable), and purges the oldest as needed.

#  PU deployment  and upload

Deploying a PU requires uploading a resource (jar/war/zip) to the manager, but due to technical issues we currently can’t include both binary file and json payload in the same REST operation. 
Instead, there are two operations:

- Upload a resource
- Deploy a PU, and provide a name of a previously uploaded resource.

This also means that when you undeploy a PU, the resource does not get deleted - if you want to delete it you need to execute a separate REST operation to delete it (if you want to undeploy/redeploy with the same JAR - there’s no need to remove it).
Currently you cannot delete/replace a JAR file if there’s a deployed PU using it - in the future we’ll introduce support for hot-updating a PU and we’ll use that concept as the API.


# Security
Security is off by default, same as before, and can be enabled via configuration, same as before. When security is enabled, the REST API performs basic authentication and checks if the user has sufficient privileges to execute the operation. 
Note that since basic authentication does not encrypt user credentials, running a manager in a secured environment without SSL is a security hazard, and the system detects this and aborts. 

You can either:

- Disable SSL explicitly (not recommended)
- Enable SSL - the system will generate a certificate for you
- Enable SSL and provide a trusted certificate you own

An auto-generated certificate provides reasonable security, but if your enterprise security guidelines are more strict you’re welcome to provide your own certificate.

Finally, if you need to configure something which we don’t expose (we’re using Jetty under the hood to host the web app), you can provide your own jetty.xml file via a system property.


|Port |System property |Default |
|:----|:---------------|:-------|
|Enable/disable |com.gs.manager.rest.ssl.enabled| false |
|Keystore path  |com.gs.manager.rest.ssl.keystore-path | |
|Keystore password|com.gs.manager.rest.ssl.keystore-password| |
|Custom config |com.gs.manager.rest.jetty.config|  |


# Backwards Compatibility

The Manager is offered side-by-side with the existing stack (GSM, LUS, etc.). We think this is a better way of working with XAP, and we want new users and customers to work solely with it. 
On the same note we understand that it requires some effort from existing users which upgrade to 12.1 (probably not too much, mostly on changing the scripts they use to start the environment), 
so if you’re upgrading for bug fixes/other features and don’t want the manager for now, you can switch from 12.0 to 12.1 and continue using the old components - it’s all still there.

#  Limitations/Known Issues

- Customers who need high availability are used to starting 2 GSM and 2 LUS - the manager requires an odd number of instances to ensure quorum (zookeeper)
- Starting more than 3 managers (e.g. 5) is currently not supported - this will lead to 5 GSM and 5 LUS, which will probably lead to noisy network. We haven’t tested it, so we blocked this in code. We might enhance this in future releases.
- Sticky session when using REST to maintain upload/deploy dependency






