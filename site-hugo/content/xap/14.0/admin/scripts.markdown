---
type: post140
title:  Scripts
categories: XAP140ADM,OSS
weight: 550
parent: admin-tools.html
---

The `<XAP root>/bin` folder includes the following scripts that can be used to manage and monitor service grid components and applications in runtime. 

# General Scripts

| Script | Description | Open Source | XAP Enterprise | InsightEdge Enterprise
|:---------|:------------|:------------|:------------|:------------|
| insightedge | Starts the InsightEdge Command Line Interface (CLI). | ✔ |  | ✔ |
| setenv | Configure the settings for the [Common Environment Variables](../dev-java/common-environment-variables.html). | ✔ | ✔ | ✔ |
| setenv-overrides | Use this script to override default settings in the setenv script. | ✔ | ✔ | ✔ |
| xap | The XAP Command Line Interface (CLI). |  ✔ | ✔ | ✔ |
| gs | The legacy Gigaspaces CLI. |  | ✔ | ✔ |
| gs-agent | Starts the Grid Service Agent. |  | ✔ | ✔ |
| gs-ui | Starts the GigaSpaces Management Center. |  | ✔ | ✔ |
| gs-webui | Starts the Web Management Console. |  | ✔ | ✔ |


# Advanced Scripts

In addition to the scripts listed above, the `bin` folder (in both the XAP and InsightEdge commercial editions) contains additional scripts in the **advanced_scripts.zip** folder, which are only provided to maintain backwards compatibility with earlier versions of XAP.

| Script | Description | Open Source | Enterprise |
|:---------|:------------|:------------|:------------|
| esm | Starts the Elastic Service Manager. |  | ✔ |
| gsc | Starts a Grid Service Container.<br>Instead of this script, start a GSC as described in [Managing Containers](./admin-service-grid-container-start.html). |  | ✔ |
| gsm | Starts a Grid Service Manager.<br>Instead of this script, start a XAP Manager as described in [Managing the Grid Service Agent](./admin-service-grid-agent.html). |   | ✔ |
| gsm_nolus | Starts a Grid Service Manager without a Lookup Service. |  | ✔ |
| gs-memcached | Starts a standalone, unmanaged instance of a [Memcached API](../dev-java/memcached-api.html) listener. |  | ✔ |
| lookupbrowser | Used for special debug scenarios to inspect the Lookup Service. |  | ✔ |
| platform-info | Prints GigaSpaces version info.<br>Instead of this script, ise the `xap --version` command in the CLI. |  | ✔ |
| pu-instance | Runs a standalone Processing Unit.<br>Instead of this script, use the `xap pu run` command in the CLI. |  | ✔ |
| startJiniLUS | Starts a Lookup Service. |  | ✔ |
| startJiniTX_Mahalo | Starts an instance of the distributed transaction manager. |  | ✔ |




