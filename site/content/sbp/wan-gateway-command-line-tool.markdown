---
type: post
title:  WAN Gateway CLI Tool
categories: SBP
parent: solutions.html
weight: 60
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Skyler Severns, Pavlo Romanenko | 10.2.1 | December 2015|    | {{%git "https://github.com/GigaSpaces-ProfessionalServices/gw-cli"%}}   |



# Introduction

Often there is a need to create and deploy a WAN gateway PU. The process is repeatable
and can be easily automated. To speed it up this tool was created and it takes care
of WAN gateway configuration and deployment.
This tool can be used with master-slave, master-master or any other topology.

# Getting started

### Download the WAN Gateway CLI

You can download the example project  from [here](/download_files/sbp/WAN_GW_CLI_Example.zip) and unzip it into an empty folder.

### Build and Running the Tool

### Step 1: Deploy space Processing Units

Deploy `space-de.jar` and `space-ru.jar` via [command line or Gigaspaces Management Center]({{%latestjavaurl%}}/deploying-onto-the-service-grid.html)

### Step 2: Build tool with maven

Modify `<gsVersion>` within the `gw-cli/pom.xml` to include the right XAP release -
example below has XAP 10.2.1 (10.2.1-14000-RELEASE) as the `<gsVersion>` value.
Then execute:

```bash
cd <project_root>
mvn clean install
```
### Step 3:	Run the tool to deploy WAN Gateways

Tool execution requires configuration file. Two configuration files (`wan-gateway-de.conf` and `wan-gateway-us.conf`)
for WAN Gateways for both spaces are provided.

For Windows:

Modify `run.ps` to point to your XAP installation path instead of `D:\gigaspaces-xap-premium-10.2.1-ga`.

```bash
cd <project_root>
run.ps wan-gateway-de.conf
run.ps wan-gateway-us.conf
```

for Linux:

Modify `run.sh` to point to your XAP installation path instead of ``/home/adminuser/gigaspaces-xap-premium-10.2.1-ga`.

```bash
cd <project_root>
./run.sh wan-gateway-de.conf
./run.sh wan-gateway-us.conf
```

Example output:

```bash
./run.sh wan-gateway-us.sh
01:02:53.055 [main] INFO  Gateway-CLI - Waiting for operations
01:02:53.167 [main] DEBUG Gateway-CLI - CONNECT: ConnectOptions{username='null', password='null', lookupLocators='null', lookupGroup='pavlo'}
01:02:54.853 [main] DEBUG Gateway-CLI - INITIALIZE: InitializeGatewayParameters{name=US}
01:02:55.901 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='null', communicationPort=null, username='null', password='null', delegator='null', target='null', sink='sink', localSpaceURL='jini://*/*/wanSpaceUS', source='null', requireBootstrap=false, gatewayLookup=false, hostName='null', discoveryPort=null}
01:02:55.978 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='null', communicationPort=null, username='null', password='null', delegator='null', target='null', sink='sink', localSpaceURL='null', source='DE', requireBootstrap=false, gatewayLookup=false, hostName='null', discoveryPort=null}
01:02:56.110 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='null', communicationPort=null, username='null', password='null', delegator='delegator', target='null', sink='null', localSpaceURL='null', source='null', requireBootstrap=false, gatewayLookup=false, hostName='null', discoveryPort=null}
01:02:56.222 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='null', communicationPort=null, username='null', password='null', delegator='delegator', target='DE', sink='null', localSpaceURL='null', source='null', requireBootstrap=false, gatewayLookup=false, hostName='null', discoveryPort=null}
01:02:56.346 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='DE', communicationPort=null, username='null', password='null', delegator='null', target='null', sink='null', localSpaceURL='null', source='null', requireBootstrap=false, gatewayLookup=true, hostName='127.0.0.1', discoveryPort=4174}
01:02:56.437 [main] DEBUG Gateway-CLI - CONFIGURE: ConfigureOptions{gatewayName=US, add=true, remove=false, modify=false, remoteGatewayName='US', communicationPort=null, username='null', password='null', delegator='null', target='null', sink='null', localSpaceURL='null', source='null', requireBootstrap=false, gatewayLookup=true, hostName='127.0.0.1', discoveryPort=4174}
01:02:56.511 [main] DEBUG Gateway-CLI - DEPLOY: DeployOptions{name='US', zone='null', bootStrapSource='null', timeout=3600, xap9=false}
01:02:58.063 [main] DEBUG Gateway-CLI: Deployer - Deploying gateway
01:02:59.693 [main] DEBUG Gateway-CLI: Deployer - Cleaning resources..
01:02:59.700 [main] DEBUG Gateway-CLI: Deployer - Cleaned
01:02:59.700 [main] DEBUG Gateway-CLI: Deployer - Deploy executed
01:02:59.700 [main] DEBUG Gateway-CLI - DISCONNECT
```
### Step 4:	Test WAN replication

Write the data to the first space and see how it's replicated to second one.


Configuration file format
---

Each line of configuration file has the following format:

    {command} {parameters}

Allowed commands: CONFIGURE, DEPLOY, DISCONNECT, INITIALIZE, LINK, BOOTSTRAP, CONNECT.
Please see allowed parameters for each command below.

### connect [*options*]
Sets the connect parameters. Required as the first command to connect to the specified grid, without it all other commands will fail.

**Options:**

| Short name 	|     Long name    	|              Description             	            | Optional/Required 	|
|:----------:	|:----------------:	|:------------------------------------:	            |:-----------------:	|
|     -u     	| --username 	    | Username to authenticate against a secured grid.  |      optional     	|
|     -p     	| --password 	    | Password to authenticate against a secured grid.  |      optional     	|
|     -l     	| --lookup-locators | Lookup locator (null for default)  	            |      optional     	|
|     -g     	| --lookup-groups 	| Lookup group (if doesn't set default is used)  	|      optional     	|

    connect -u user -p password -l localhost:4174 -g mygroup

### initialize [*options*]
Initializes a new gateway. Throws exception, if gateway with the same name was initialized or deployed already.

**Options:**

| Short name    |   Long name   |   Description         |   Optional/Required   |
|:----------:   |:-----------:  |:-------------:        |:------------------:   |
|   -n          | --name        | Gateway site name.    |   optional            |

    initialize -n SITE-1

### configure [*options*]
Configures the gateway component or throws an exception if the gateway wasn't initialized.

**Options:**

Use one of three options to add/remove/modify one of gateway configurations.

| Short name 	|     Long name    	|              Description            |
|:----------:	|:----------------:	|:-----------------------------------:|
|            	| --name 	        | Name of the gateway to configure.   |
|     -a     	| --add 	        | Add to gateway configuration 	      |
|     -r     	| --remove 	        | Remove from gateway configuration   |
|     -m     	| --modify	        | Modify gateway configuration 	      |

*Delegator options:*

To add/remove delegator use ***-D*** option:

| Short name 	|     Long name    	   |              Description            |  Optional/Required 	|
|:----------:	|:----------------:	   |:-----------------------------------:|	:-----------------:	|
|     -D     	| --delegator 	       | Delegator id	                     |  	required        |
|           	| --username           | Username                    	     |      optional        |
|           	| --password           | Password                   	     |      optional        |
|     -c     	| --communication-port | Communication port         	     |      optional        |

    configure SITE-1 -a -D delegator1 --gateway-lookups lookupsId --c 8000

To add/remove target to existing delegator.

| Short name 	|     Long name    	   |              Description            |  Optional/Required 	|
|:----------:	|:----------------:	   |:-----------------------------------:|	:-----------------:	|
|     -D     	| --delegator 	       | Delegator id	                     |  	required        |
|            	| --target	           | Target gateway name                 |      required        |

    configure SITE-1 -a -D delegator1 --target SITE-2

*Sink options:*

To add/remove sink  use ***-S*** option:

| Short name 	|     Long name    	   |              Description            |  Optional/Required 	|
|:----------:	|:----------------:	   |:-----------------------------------:|	:-----------------:	|
|     -S     	| --sink    	       | Sink id    	                     |  	required        |
|     -c     	| --communication-port | Communication port         	     |      optional        |
|           	| --username           | Username                    	     |      optional        |
|           	| --password           | Password                   	     |      optional        |
|            	| --local-space-url	   | Locasl space url                    |      required        |

        configure SITE-1 -a -S sink1 --c 8000 --local-space-url jini://*/*/wanSpace

To add/remove source to existing sink.

| Short name 	|     Long name    	   |              Description            |  Optional/Required 	|
|:----------:	|:----------------:	   |:-----------------------------------:|	:-----------------:	|
|     -S     	| --sink 	           | Sink id	                         |  	required        |
|            	| --source	           | Source gateway name                 |      required        |

    configure SITE-1 -a -S sink1 --source SITE-2

*Lookup options:*

To add/remove/modify lookup use ***-L*** option.

| Short name 	|     Long name    	       |              Description            |  Optional/Required 	|
|:----------:	|:----------------:	       |:-----------------------------------:|	:-----------------:	|
|     -n     	| --remote-gateway-name    | Name of the remote gateway	         |   required           |
|     -c     	| --communication-port     | Communication port         	     |   optional           |
|      -h      	| --host	               | Host name                           |   required           |
|      -d      	| --discovery-port	       | Discovery port                      |   required           |

    configure SITE-1 -a -L -n SITE2 -h localhost -d 4366 -c 8000

### deploy [*options*]
Deploys gateway processing unit. If --source param is specified then replication gateway bootstrapping process will be executed.

| Short name 	|     Long name         |              Description                                      | Optional/Required  |
|:----------:	|:----------------:     |:-----------------------------------:                          |:-----------------: |
|      -n      	| --name                | Name of the gateway to deploy                                 |   required         |
|      -b      	| --bootstrap-source    | Name of the bootstrap source gateway                          |   optional         |
|      -z      	| --zone                | Name of the required zone for deployment                      |   optional         |
|      -t      	| --bootstrap-timeout   | The number of seconds before a boostrap timeout occurs.       |   optional         |
|            	| --xap9                | Indicates if the resulting gateway should be XAP 9.x or 10.x  |   optional         |


    deploy SITE-1 --bootstrap-source SITE-2

### link [*options*]
Connects the specified space to a deployed gateway at runtime.

| Short name 	|     Long name                 |              Description              |Optional/Required 	|
|:----------:	|:----------------:             |:-----------------------------------:  |:-----------------:	|
|     -a      	| --add                         | Adds the gateway target.              |   required         |
|     -r       	| --remove                      | Removes the gateway target.           |   required         |
|     -n       	| --name                        | Target gateway name.                  |   required         |
|     -s       	| --space-name                  | Target space.                         |   required         |
|     -b       	| --bulk-size                   | Replication bulk size.                |   optional         |
|     -i     	| --idle-time-threshold         | Max milliseconds between replication. |   optional         |
|     -m     	| --max-redo-capacity           | Max redo log count.                   |   optional         |
|     -o     	| --on-capacity-exceeded        | Operation when redo log size exceeded.|   optional         |
|     -c     	| --replicate-change-as-update  | Replicates changes as updates.        |   optional         |

    link --add -n gateway1 -s mySpace1

### disconnect
Exits from gateway configuration CLI.

    disconnect

