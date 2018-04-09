---
type: post123
title:  Command Line Interface
categories: XAP123ADM,OSS
weight: 100
parent: admin-tools.html
---

GigaSpaces provides a Command Line Interface (CLI) tool for administering both XAP and InsightEdge. The CLI is based on the REST Manager API, enabling users to administer local, remote, and cloud-based application environments. In addition to the REST Manager API operations, the CLI supports certain administration tasks in the open source application editions.

Users that are administering a XAP environment should use the `xap` script. Users that are administering InsightEdge should use the `insightedge` CLI script, which contains all of the actions available with the `xap` script, plus additional options where relevant for administering InsightEdge components.

The image below shows the screen that is displayed when you run the `xap` script.

![image](/attachment_files/admin/xap-cli.png)

# CLI Help

Use the `--help` command (or `-h`) to see the syntax of a specified CLI command, or a list of all available commands. The list of commands may vary depending on whether you are running the XAP or the InsightEdge script. For example,
the list of commands for `insightedge host run-agent --help` contains all the actions available under `xap host run-agent --help`, plus additional options for administering the Spark master and worker components.

# CLI Configuration

The CLI connects to the REST Manager API using the name or IP address of the Manager server. Configuration is applied from one of the following:

* The first server configured in the `XAP_MANAGER_SERVERS` environment variable.
* The server configured using `--server` command line option.
* The `localhost` as the server to connect to (if none of the above was applied).

<!--
12.3.1
-->
<!--
# CLI Auto Complete Feature
-->
<!--
## Overview
<!--
Linux users can enjoy bash completion as part of our Cli. 
After activating the auto complete feature (as explained below) you can go to `<XAP-HOME>/bin` and type `xap [TAB][TAB]` or `insightedge [TAB][TAB]` this will complete the command sequance or list all completions possible in case of multiple options.
<!--
## Activating Auto Complete
<!--
The auto complete script can be found in `<XAP-HOME>/tools/cli`.
There are several ways to install auto complete, use one of the following:
<!--
* Install only for the current bash console.
  Go to `<XAP-HOME>/tools/cli` and source the completion script: `source xap-autocomplete` or `source insightedge-autocomplete`.
  When leaving the bash session the auto complete will stop working.
* Permanently install, no need to repeat for each bash console.
  Place the `xap-autocomplete` or `insightedge-autocomplete` file in a `bash_completion.d` folder. 
  The folder may appear in the following locations: `/etc/bash_completion.d` `/usr/local/etc/bash_completion.d` `~/bash_completion.d`
  After installing the script open a new bash console.
 <!--
After using one of the options go to `<XAP-HOME>/bin` and type: `xap [TAB][TAB]` or `insightedge [TAB][TAB]`.
<!--
{{%info "TIP"%}}
You can export xap or insightedge Path and it will work from anywhere, otherwise autocomplete works only from bin directory.
{{%/info%}}
<!--
{{%info "TIP"%}}
You can add an alias as well, for example: `alias xap = "cd <XAP-HOME>/bin && xap"`
{{%/info%}}
<!--
## Customizing Auto Complete
<!--
If you wish to generate a new auto complete script (for example if you have changed the name of the xap or insightedge script) you can do so in the following way.
Go to `<XAP-HOME>/tools/cli` and run the following command:
`java -cp "../../lib/required/*:*" MAIN_COMMAND ALIAS`
<!--
`MAIN_COMMAND` is different for each product, it can be one of the following:
 * XAP-Open - `org.gigaspaces.cli.commands.Autocomplete`
 * XAP-Premium - `com.gigaspaces.cli.commands.Autocomplete`
 * Insightedge-Open - `org.insightedge.cli.commands.Autocomplete`
 * Insightedge-Premium - `com.insightedge.cli.commands.Autocomplete`
`ALIAS` is optional, you can use it if you have changed the name of the command.
 <!--
 After running the following command the auto complete script will appear in `<XAP-HOME>/tools/cli`.
-->
#  Running a Local Manager Server

To run a local Manager server and a web browser client, type `xap host run-agent --auto` or `insightedge host run-agent --auto`.

To view a list of the running agents, type `xap host list` or `insightedge host list`.

{{%info "Info"%}}
If you've recently upgraded to version 12.3, note that this is a new version of the CLI tool and uses a different syntax and command set. We encourage using this new version, but if you need information about the old CLI commands, refer to the [GigaSpaces CLI](command-line-interface.html) pages in the [Legacy Tools](admin-legacy-tools.html) section.
{{%/info%}}
