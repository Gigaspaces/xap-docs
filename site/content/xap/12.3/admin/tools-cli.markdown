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
# CLI Autocomplete Feature
<!--
## Overview
<!--
Bash completion in the CLI is available for Linux and MacOS X users. After activating the autocomplete feature (as explained below), navigate to `<XAP-HOME>/bin` and type `xap [TAB][TAB]` or `insightedge [TAB][TAB]`. This will complete the command sequence, or list all the available completions if there are multiple options.
<!--
### MacOS X Users Only
<!--
If you are using the CLI in a MacOS X environment, the autocomplete script will only work if bash version 4 is installed on your machine. After installing the required bash version, add the shell to the allowed shells and make it the default shell.
<!--
When using autocomplete, type `./xap [TAB][TAB]` or `./insightedge [TAB][TAB]`. 
<!--
{{%note "Note"%}}
You can add xap as an alias to the `.bash_profile`, in order to simplify using autocomplete.
{{%/note%}}
<!--
## Activating the Autocomplete Feature
<!--
The autocomplete script is located in `<XAP-HOME>/tools/cli`. There are two ways to install autocomplete.
<!--
### Method 1 - Install Only for the Current Bash Console
<!--
1. Go to `<XAP-HOME>/tools/cli` and source the completion script: `source xap-autocomplete` or `source insightedge-autocomplete`.
<!--
1. Go to `<XAP-HOME>/bin` and type: `xap [TAB][TAB]` or `insightedge [TAB][TAB]`.
<!--
{{%note "Note"%}}
1. When you leave the bash session, autocomplete stops working.
{{%/note%}}
<!--  
### Method 2 - Permanent Installation
<!--
1. Place the `xap-autocomplete` or `insightedge-autocomplete` file in a `bash_completion.d` folder. The folder may appear in the following locations: `/etc/bash_completion.d` `/usr/local/etc/bash_completion.d` `~/bash_completion.d` (create one if absent).
<!--
1. (MacOS X users only) Edit the `~/.bash_profile` and add the following code: `source /usr/local/etc/bash_completion.d/xap-autocomplete`
<!--
1. (MacOS X users only) Reload the bash shell or open a new terminal `source ~/.bash_profile`.
<!--
1. After installing the script, open a new bash console.
<!--
1. Go to `<XAP-HOME>/bin` and type: `xap [TAB][TAB]` or `insightedge [TAB][TAB]`.
<!--
{{%tip "Tips"%}}
1. If you export the xap or insightedge classpath, autocomplete will work from any directory. If you don't export the classpath, autocomplete works only from the bin directory.
1. You can add a bash alias, for example: `alias xap = "cd <XAP-HOME>/bin && xap"`
{{%/tip%}}
<!--
## Customizing the Autocomplete Feature
<!--
If you want to generate a new autocomplete script (for example, if you changed the name of the xap or insightedge script) do so as follows:
<!--
Go to `<XAP-HOME>/tools/cli` and run the following command:
<!--
`java -cp "../../lib/required/*:*" MAIN_COMMAND ALIAS`
<!--
The `MAIN_COMMAND` value is different for each product and edition. Use one of the following:
 * XAP open source - `org.gigaspaces.cli.commands.Autocomplete`
 * XAP Enterprise - `com.gigaspaces.cli.commands.Autocomplete`
 * Insightedge open source - `org.insightedge.cli.commands.Autocomplete`
 * Insightedge Enterprise - `com.insightedge.cli.commands.Autocomplete`
<!--
`ALIAS` is optional; use it if you changed the name of the command.
 <!--
After running `java -cp "../../lib/required/*:*" MAIN_COMMAND ALIAS`, the autocomplete script will appear in `<XAP-HOME>/tools/cli`.
-->
#  Running a Local Manager Server

To run a local Manager server and a web browser client, type `xap host run-agent --auto` or `insightedge host run-agent --auto`.

To view a list of the running agents, type `xap host list` or `insightedge host list`.

{{%note "Info"%}}
If you've recently upgraded to version 12.3, note that this is a new version of the CLI tool and uses a different syntax and command set. We encourage using this new version, but if you need information about the old CLI commands, refer to the [GigaSpaces CLI](command-line-interface.html) pages in the [Legacy Tools](admin-legacy-tools.html) section.
{{%/note%}}
