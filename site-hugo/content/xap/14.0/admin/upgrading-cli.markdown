---
type: post140
title:  Upgrading the CLI
categories: XAP140ADM,PRM
weight: 600
parent: admin-legacy-tools.html
---

A new [Command Line Interface](tools-cli.html) was introduced in the InsightEdge Platform and XAP release 12.3. This CLI provides a simpler syntax and is cloud-friendly (based on the [REST Manager API](admin-rest-manager-api.html)). 

The [legacy CLI](command-line-interface.html) is still supported, but has been deprecated as of InsightEdge Platform and XAP release 14.0 and will be removed in a future version. Customers upgrading from older product versions are encouraged to upgrade to the new CLI.

To get started with the new CLI, simply run `xap --help` from the product's `bin` folder.
   
# Commonly Used Options

The following table lists common tasks and the commands that are used to execute them using both the old and new Command Line Interfaces. This is not an exhaustive list, but it should help you get started with migration quickly and estimate the required effort.

| Task | Old Command | New Command |
|------|-------------|-------------|
| Get help                            | `gs help`                                    | `xap help` or `xap --help`                    |
| Get version                         | `gs version`                                 | `xap version`                                 |
| Get information                     | `gs stats`                                   | `xap info`                                    |
| Start agent components              | `gs gsa start-gsm | start-gsc | start-lookup`| `xap host run-agent [options]`                |
| Stop agent components               | `gs gsa shutdown [options]`                  | `xap host kill-agent [options]`               |
| List agent components               | `gs list [options]`                          | `xap host list [options]` and `xap container list [options]`|
| Deploy a space                      | `gs deploy-space [options] <space-name>`     | `xap space deploy [options] <space-name>`     |
| List spaces                         | `gs space list [options]`                    | `xap space list [options]` and `xap space info [options] <space-name>`
| Query a Space                       | `gs space sql [options]`                     | `xap space query [options] <space-name> <type>` |
| Deploy a Processing Unit            | `gs deploy [options] <pu-name-or-file>`      | `xap pu deploy [options] <pu-name> <pu-file>` |
| Undeploy a Processing Unit          | `gs undeploy [options] <pu-name>`            | `xap pu undeploy [options] <pu-name>`         |
| Quiesce a Processing Unit           | `gs quiesce [options] <pu-name>`             | `xap pu quiesce [options] <pu-name>`          |
| Unquiesce a Processing Unit         | `gs unquiesce [options] <pu-name>`           | `xap pu unquiesce [options] <pu-name>`        |

# Obsolete Commands

This section lists commands that have not been ported to the new Command Line Interface for various reasons.

## Interactive Shell Commands

The old Command Line Interface supports an interactive shell mode, and as such includes a set of shell-related commands, which are currently not supported in the new CLI as it is not interactive.

* `cd`
* `dir`
* `pwd`
* `ls`
* `set`
* `login`

## Elastic Processing Unit Commands

The Elastic Processing Unit was deprecated in version 12.1, therefore its commands were not ported to the new Command Line Interface.

* `deploy-elastic-space`
* `deploy-elastic-pu`
* `scale`

## Miscellaneous

* `pudeploy` - alias for the `deploy` command.
* `deploy-memcached` - Can be done via the `deploy` command.
* `deploy-rest` - Can be done via the `deploy` command.
* `deploy-application` and `undeploy-application` - Low usage rate, can be done via the `deploy`/`undeploy` commands and scripting.
* `jconsole` - The new CLI is based on the REST protocol, so this command is misleading. Users may access the standard JConsole directly.
* `admin multicastTest` - The new CLI is based on the XAP Manager, which uses unicast.
