---
type: post
title:  Hyperic integration
categories: SBP
parent: production.html
weight: 500
---

{{%ssummary%}}{{%/ssummary%}}

# Overview

[Hyperic](http://www.hyperic.com/) is a global monitoring system and this page describes a quick GigaSpaces integration that I made during a consulting session.
The plugin uses the GigaSpaces Admin API to perform the monitoring of a global system.
The plugin is offered as open source as part of OpenSpaces community.

# Installation

As the GigaSpaces plugin uses the [Administration and Monitoring API]({{%latestjavaurl%}}/administration-and-monitoring-api.html) (a.k.a. Admin API) a single Hyperic agent will be used to monitor the whole cluster. Indeed GigaSpaces is a very dynamic platform and a classical Hyperic installation based on an agent monitoring the processes on his physical (or virtual) machine won't work well, a given processing unit could be unavailable on a machine because the GSM decided to deploy it somewhere else.

In order to use the plugin on an hyperic agent you have to prepare this agent to be 'GigaSpaces ready'.

1. Copy the GigaSpaces required jar files (_JSHOMEDIR/lib/required_) to the following agent folder: _agent-4.6/bundles/agent-4.6/pdk/lib/gs_ (you'll have to create the gs folder).
2. Upgrade the agent JVM to a 1.6 version
3. Restart the agent
4. Connect to the Hyperic server console with admin account and go to the plugin manager. Install the GigaSpaces plugin ([hyperic-gs-plugin.jar](/attachment_files/sbp/hyperic-gs-plugin.jar))

# Usage

### Create a platform

In order to use the plugin you'll have to configure a GigaSpaces platform. Indeed as a correct defined GigaSpaces cluster discovery relies on lookup groups and/or lookup locators the plugin won't be able to auto-discover your infrastructure without these parameters.

![hyperic_new_platform.png](/attachment_files/sbp/hyperic_new_platform.png)

On the hyperic new platform page select

1. The platform type to be GigaSpaces XAP Grid.
2. In Agent Connection select the agent that you prepared for GigaSpaces monitoring (Cf. Installation).
3. IP Address is not used by the plugin, Hyperic requires a valid one however so you can put any address (the one of the hyperic server if you like).

#### Configure the platform

Once the platform is created you should be able to configure it:

![hyperic_platform_conf.png](/attachment_files/sbp/hyperic_platform_conf.png)

Click on the _Edit_ button in the _Configuration Properties_ panel.

Configure your locators, groups and monitoring user details (if GigaSpaces security is enabled).

Click on OK, the plugin should auto discover the GigaSpaces services (GSAs, GSMs, GSCs, Pus and Spaces).

_Note that Hyperic Autodiscovery of services etc run daily only so if you deploy a new pu and want it to appear you should repeat the configuration step (without changing values) to trigger a new auto discovery._

_The plugin is currently delivering some very basic features and has to be improved in the future to cover more monitoring information.

# Building the plugin

If you wish to enhance the plugin, fix bugs or just review the source code, you can [download the source code](https://github.com/Gigaspaces/bestpractices) from _GigaSpaces Solutions and Patterns GitHub repository_ and build it yourself.
Plugin package uses maven but requires a few Hyperic artifacts that are not available in public repositories. In order to build the plugin you'll first have to add them to your repository:

- Run the following commands:
    - mvn install:install-file -Dfile=$PATH_TO_AGENT_HOME_DIR/bundles/agent-4.6/lib/hq-agent-core-4.6.jar -DgroupId=org.hyperic.hq.agent -DartifactId=hq-agent-core -Dversion=4.6 -Dpackaging=jar
    - mvn install:install-file -Dfile=$PATH_TO_AGENT_HOME_DIR/bundles/agent-4.6/pdk/lib/hq-pdk-4.6.jar -DgroupId=org.hyperic.hq -DartifactId=hq-pdk -Dversion=4.6 -Dpackaging=jar
    - mvn install:install-file -Dfile=$PATH_TO_AGENT_HOME_DIR/bundles/agent-4.6/pdk/lib/hq-util-4.6.jar -DgroupId=org.hyperic.hq -DartifactId=hq-util -Dversion=4.6 -Dpackaging=jar
    - mvn install:install-file -Dfile=$PATH_TO_AGENT_HOME_DIR/bundles/agent-4.6/pdk/lib/hq-common-4.6.jar -DgroupId=org.hyperic.hq -DartifactId=hq-common -Dversion=4.6 -Dpackaging=jar

Now you should be able to build the plugin using maven (mvn install).
