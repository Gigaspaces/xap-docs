---
type: post100adm
title:  Global vs. Local GSM
categories: XAP100ADM
parent: runtime-configuration.html
weight: 300
---

{{%ssummary%}}{{%/ssummary%}}



{{%anchor local-gsm%}}

# Local GSM

Similar to the lookup service you may run a global GSM or a local GSM. In this case a local GSM will allow you to control the host machine where the GSM and its deploy folder will be located (configured via the `com.gs.deploy` system property). The GSM deploy folder used at the deployment time when provisioning a deployed PU into exiting GSCs or once a new GSC is started (manually or dynamically via the ESM) where it is downloading from the GSM its PU configuration (pu.xml) and relevant PU files.

When performing hot deployment to support rolling upgrade or other maintenance activities the location of the GSMs is important since you must place the updated PU files in the machines running the GSM. A GSM may be running in an active or slave mode – it is recommended to place updated PU files on both GSM’s deploy folder.
If you have a large grid (over 100 GSCs) or a large PU (over 10MB) with many files you may want to choose specific machines with special network or CPU capacity to run the GSM – This is another scenario where the local GSM setup should be considered.


# Local Setup Example

With the following example we have `Machine A`, `Machine B`, `Machine C` and `Machine D` running the service grid.  We would like to start two GSMs. We have decided that `Machine A` and `Machine D` will be running a GSM.

![global-localGSM1.jpg](/attachment_files/global-localGSM1.jpg)

The agent on these machines will be started using the following:


```bash
gs-agent gsa.global.gsm 0 gsa.gsm 1
```

`Machine B` and `Machine C` will not run a GSM. The agent on these machines will be started using the following:


```bash
gs-agent gsa.global.gsm 0 gsa.gsm 0
```

Upon startup the only `Machine A` and `Machine D` agent’s that are configured to start a local GSM will have it running.  In case of `Machine A` or `Machine D` failure the system will have a single GSM. Service Grid components (LUS , GSC) will be notified for this missing GSM. Once the missing GSM will be restarted on the relevant machine Service Grid components will be notified. With a network running a DNS - you may start a new machine with the same Host name to support total machine failure while keeping number of running GSMs intact.

{{%anchor global-gsm%}}

# Global GSM

With the global GSM setup - Once a running GSM failed (as a result machine termination, or GSM process failure) , a different agent that is not running a GSM will be starting a GSM to enforce the SLA specified at the agent startup. In this case all machines are equal and may run a GSM. Two GSMs is the recommended number per service grid.

# Global Setup Example

With the following example we have `Machine A`, `Machine B`, `Machine C` and `Machine D` running the service grid.

![global-localGSM2.jpg](/attachment_files/global-localGSM2.jpg)

All agents are started with the same command instructing them to maintain two global GSMs across the entire service grid:


```bash
gs-agent gsa.global.gsm 2 gsa.gsm 0
```

Upon startup the agents will decide which ones will run a GSM and which won’t.
