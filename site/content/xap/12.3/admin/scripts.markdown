---
type: post123
title:  Scripts
categories: XAP123ADM,OSS
weight: 550
parent: admin-tools.html
---

The `<XAP root>/bin` folder includes the following scripts that can be used to manage and monitor service grid components and applications in runtime. 

# General Scripts

| Script | Description | Open Source | XAP Enterprise | InsightEdge Enterprise
|:---------|:------------|:------------|:------------|:------------|
| insightedge | Starts the InsightEdge Command Line Interface (CLI). | {{%oksign%}} |  | {{%oksign%}} |
| setenv | Configure the settings for the [Common Environment Variables](../dev-java/common-environment-variables.html). | {{%oksign%}} | {{%oksign%}} | {{%oksign%}} |
| setenv-overrides | Use this script to override default settings in the setenv script. | {{%oksign%}} | {{%oksign%}} | {{%oksign%}} |
| xap | The XAP Command Line Interface (CLI). |  {{%oksign%}} | {{%oksign%}} | {{%oksign%}} |
| gs | The legacy Gigaspaces CLI. |  | {{%oksign%}} | {{%oksign%}} |
| gs-agent | Starts the Grid Service Agent. |  | {{%oksign%}} | {{%oksign%}} |
| gs-ui | Starts the GigaSpaces Management Center. |  | {{%oksign%}} | {{%oksign%}} |
| gs-webui | Starts the Web Management Console. |  | {{%oksign%}} | {{%oksign%}} |


# Advanced Scripts

In addition to the scripts listed above, the `bin` folder (in both the XAP and InsightEdge commercial editions) contains additional scripts in the **advanced_scripts.zip** folder, which are only provided to maintain backwards compatibility with earlier versions of XAP.

| Script | Description | Open Source | Enterprise |
|:---------|:------------|:------------|:------------|
| esm | Starts the Elastic Service Manager. |  | {{%oksign%}} |
| gsc | Starts a Grid Service Container.<br>Instead of this script, start a GSC as described in [Managing Containers](./admin-service-grid-container-start.html). |  | {{%oksign%}} |
| gsm | Starts a Grid Service Manager.<br>Instead of this script, start a XAP Manager as described in [Managing the Grid Service Agent](./admin-service-grid-agent.html). |   | {{%oksign%}} |
| gsm_nolus | Starts a Grid Service Manager without a Lookup Service. |  | {{%oksign%}} |
| gs-memcached | Starts a standalone, unmanaged instance of a [Memcached API](../dev-java/memcached-api.html) listener. |  | {{%oksign%}} |
| lookupbrowser | Used for special debug scenarios to inspect the Lookup Service. |  | {{%oksign%}} |
| platform-info | Prints GigaSpaces version info.<br>Instead of this script, ise the `xap --version` command in the CLI. |  | {{%oksign%}} |
| pu-instance | Runs a standalone Processing Unit.<br>Instead of this script, use the `xap pu run` command in the CLI. |  | {{%oksign%}} |
| startJiniLUS | Starts a Lookup Service. |  | {{%oksign%}} |
| startJiniTX_Mahalo | Starts an instance of the distributed transaction manager. |  | {{%oksign%}} |




