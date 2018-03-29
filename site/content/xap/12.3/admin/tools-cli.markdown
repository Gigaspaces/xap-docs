---
type: post123
title:  Command Line Interface
categories: XAP123ADM,OSS
weight: 100
parent: admin-tools.html
---

GigaSpaces provides a Command Line Interface (CLI) tool for administering both XAP and InsightEdge. The CLI is based on the REST Manager API, enabling users to administer local, remote, and cloud-based application environments. In addition to the REST Manager API operations, the CLI supports certain administration tasks in the open source application editions. 

Users that are administering a XAP environment should use the `xap` script. For example:

`xap space deploy`

Users that are administering InsightEdge should use the `insightedge` CLI script, which contains all of the actions available with the `xap` script, plus additional options where relevant for administering InsightEdge components.

Use the `--help` command to see the syntax of a specified CLI command, or a list of all available commands. The list of commands may vary depending on whether you are running the XAP or the InsightEdge script. For example, 
the list of commands for `insightedge host run-agent --help` contains all the actions available under `xap host run-agent --help`, plus additional options for administering the Spark master and worker components.

{{%info "Info"%}}
If you have recently upgraded to version 12.3, note that this is a new version of the CLI tool and uses a different syntax and command set. We encourage using this new version, but if you need information about the old CLI commands, refer to the [GigaSpaces CLI](command-line-interface.html) pages in the [Legacy Tools](admin-legacy-tools.html) section. 
{{%/info%}}


#  Command Line Interface

![image](/attachment_files/admin/xap-cli.png)
