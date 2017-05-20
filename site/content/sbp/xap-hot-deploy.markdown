---
type: post
title:  Hot deploy
categories: SBP
weight: 100
parent: hot-deploy.html
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Anna Babich <br> Pavlo Romanenko| 10.0 | November 2014|  |  {{%git "https://github.com/fe2s/xap-hot-deploy" %}}   |


This tool allows business logic running as a PU to be refreshed (rolling PU upgrade) without any system downtime and data loss. The tool using the hot deploy approach , placing new PU code on the GSM PU deploy folder and later restart each PU instance. See XAP Hot Deploy documentation for details.

To refresh the PU code the tool restarts all processing units for a given PU. Old deployment files for the specified PU will be moved into a temporary folder to be used in case the upgrade fails. New PU files will be copied to the XAP deploy folder prior the restart phase. The tool identifies all running PU instances and restart them once by one. Once the process is completed both primary and backup PU instances will run updated logic.

# Stateful PU Restart

1. The tool discovers all processing unit instances and identifies their Space mode.
2. All backups are restarted (each instance in a separate thread).
3. All primaries are restarted. If the `double_restart` option is enabled, primaries are restarted twice to return to the original state (one by one). Without this option, primary partitions  are  restarted one time (each instance in a separate thread). Use `double_restart` if all instances should be placed in the “original” vm.

# Stateless PU Restart

The tool discover all processing unit instances and restarts them (each instance in separate thread).

# Build

Source files (`xap-hot-redeploy` folder) can be located anywhere on your machine.

Building the tool:

```bash
mvn clean install
```

Note, that tests will be skipped in this case. How to build with tests see in the [tests section](#1).

# Run

1. Copy new jar(war) files with new classes to the `xap-hot-redeploy` folder.
2. Configure options in `xap-hot-redeploy/config.properties` file.
3. Run `run.sh (run.bat)` script from xap-hot-redeploy folder.


Runtime configuration `config.properties` file.


| Option                   | Optional/required | Default value                        | Description                                                                                                                         |
|--------------------------|-------------------|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| GSM_HOSTS                | required          | -                                    | Hosts on which GSM are located.                                                                                                     |
| PU                       | required          | space=space.jar, web=web.war | Map with key value pairs, where key is processing unit name, value is name of file with new classes.                                |
| SSH_USER                 | required          | user                              | Name of user on remote machine.                                                                                                     |
| GS_HOME_DIR      | required                  | -                                    | Path to gigaspaces directory.                                                                                                       |
| LOOKUPLOCATORS      | optional               | localhost                            | Jini lookup service locators used for unicast discovery.                                                                                                                 |
| LOOKUPGROUPS             | optional          | Gigaspace default lookup group       | Jini lookup service group.                                                                                                                        |
| IDENT_PU_TIMEOUT         | required          | 60                                | Timeout to identify processing unit (in seconds).                                                                                   |
| IDENT_SPACE_MODE_TIMEOUT | required          | 60                                 | Timeout to identify space mode (in seconds).                                                                                        |
| IDENT_INSTANCES_TIMEOUT | required          | 60                                 | Timeout to identify instances (in seconds).                                                                                        |
| RESTART_TIMEOUT | required          | 60                                 | Timeout for restarting pu (in seconds).                                                                                        |
| IS_SECURED               | optional          | false                              | Set this parameter "true" if space is secured.                                                                                      |
| DOUBLE_RESTART           | optional          | false                              | Set "true" if all instances should be placed in "original" vm after redeploy. When set to "true" primary instances restarted twice. |
| LOCAL_CLUSTER_MODE       | optional          | false                              | Set "true" for local cluster mode (testing mode). |

Results

In case if there are no problems with hot-redeploy application you can see success message and details for restarting pu instances:

```bash
14:51:44,392  INFO main ConfigInitializer:init:28 - Gigaspaces location: /home/user/gigaspaces-xap-premium-10.0.0-ga
14:51:44,393  INFO main ConfigInitializer:init:29 - Pu to restart: [space, cinema, mirror]
14:51:44,393  INFO main ConfigInitializer:init:30 - Locator: null
14:51:44,393  INFO main ConfigInitializer:init:31 - Lookup group: null
14:51:44,394  INFO main ConfigInitializer:init:32 - Timeout for identify pu: 60
14:51:44,394  INFO main ConfigInitializer:init:33 - Timeout for identify instances: 60
14:51:44,394  INFO main ConfigInitializer:init:34 - Timeout for identify space mode: 60
14:51:44,395  INFO main ConfigInitializer:init:35 - Timeout for restart 60
14:51:44,395  INFO main ConfigInitializer:init:36 - Secured: false
14:51:44,395  INFO main ConfigInitializer:init:37 - Double restart: false
14:51:44,395  INFO main ConfigInitializer:init:38 - GSM Hosts: [127.0.0.1]
14:51:44,395  INFO main ConfigInitializer:init:39 - User: user
14:51:44,395  INFO main ConfigInitializer:init:40 - Is local cluster: false
14:51:52,044  INFO main StatefulPuRestarter:restartAllInstances:105 - Restarting pu space with type STATEFUL
14:51:52,045  INFO pool-6-thread-1 PuInstanceRestarter:restartPUInstance:36 - restarting instance 1 on 127.0.0.1[127.0.0.1] GSC PID:9214 mode:backup...
14:52:05,085  INFO pool-6-thread-1 PuInstanceRestarter:restartPUInstance:43 - done
14:52:06,233  INFO pool-7-thread-1 PuInstanceRestarter:restartPUInstance:36 - restarting instance 1 on 127.0.0.1[127.0.0.1] GSC PID:9213 mode:primary...
14:52:21,367  INFO pool-7-thread-1 PuInstanceRestarter:restartPUInstance:43 - done
14:52:22,433  INFO main StatelessPuRestarter:restart:23 - Restarting pu cinema with type WEB
14:52:31,107  INFO main StatelessPuRestarter:restart:25 - done
14:52:32,116  INFO main StatelessPuRestarter:restart:23 - Restarting pu mirror with type MIRROR
14:52:38,929  INFO main StatelessPuRestarter:restart:25 - done
14:52:28,945  INFO main HotRedeployMain:main:17 - Hot redeploy completed successfully
``

If there are any problems during the hot-redeploy, you will see an error message and description of the problem:

```bash
20:11:27,861  INFO main HotRedeployMain:checkFiles:76 - Please place new files on all GSM machines and try again.
20:11:27,864  INFO main HotRedeployMain:checkFiles:77 - Hot redeploy failed
```

All details about hot-redeploy process you can see in `hot-redeploy.log` file.

{{%anchor 1%}}

# Tests

If you want to build tool with running tests use:

```bash
mvn clean install -DskipTests=false
```

#### PREREQUISITES for running tests:

 * run gs-agent.sh/bat
 * lookup group and locator should be set to default values
 * properties should be set in `/tool/src/test/resources/config.properties` file
 * make sure that there is no pu with name `space` deployed already


# Rollback

Rollback functionality helps to avoid the loss of data, if errors occurred during the redeploy (for example - broken pu file).
When errors occur, the tool searches for backup GSM's. If there are more than one GSM in the system, they will be restarted one by one. If there is only one GSM in system, the tool will look for empty GSC and restart it.
In  case the rollback finished successfully, all pu's for redeploy return to its original  version.

If no backup GSM and no empty container are found, the rollback will fail and the system state is unstable.

Rollback working example:

```bash

17:03:48,679  INFO main StatefulPuRestarter:restartAllInstances:105 - Restarting pu space with type STATEFUL
17:03:48,681  INFO pool-6-thread-1 PuInstanceRestarter:restartPUInstance:36 - restarting instance 1 on 127.0.0.1[127.0.0.1] GSC PID:7612 mode:backup...
17:04:49,294  INFO pool-6-thread-1 PuInstanceRestarter:restartPUInstance:43 - done
17:10:35,739  INFO main RollbackChecker:doRollback:100 - Do rollback..
17:10:35,739  INFO main RollbackChecker:doRollback:106 - There is one GSM in system. Try to find empty GSC
17:10:35,740  INFO main RollbackChecker:doRollback:109 - Restarting GSC with id 2
17:10:53,683  INFO main RollbackChecker:doRollback:119 - Rollback completed successfully
17:10:53,684  WARN main HotRedeployMain:redeploy:44 - Hot redeploy failed. Rollback successfully completed
```

### Minimal configuration for rollback:

In order for the rollback to work, the following minimal topology needs to be available:

* At least one backup GSM should be deployed.

or

* If n = count of primary pu instances, you should have n + 1 GSC deployed.