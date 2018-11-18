---
type: post140
title:  Upgrading CLI
categories: XAP140ADM,PRM
weight: 600
parent: admin-legacy-tools.html
---

The 12.3 release has introduced a new [Command Line Interface](tools-cli.html), which provides a simpler syntax and is cloud-friendly (based on the [REST Manager API](admin-rest-manager-api.html)). 

The [legacy CLI](command-line-interface.html) is still supported, but starting v14.0 it is deprecated and will be removed in the future. Customers upgrading from older versions are encouraged to upgrade to the new CLI.

To get started with the new CLI, simply run `xap --help` from the product's `bin` folder.
   
# Commonly used options

The following table lists common tasks and the commands which are used to execute them on both the old and new Command Line Interface. This is not an exhaustive list, but it should help you get started with migration quickly and estimate the required effort.

| Task | Old command | New command |
|------|-------------|-------------|
| Get help                            | `gs help`                                    | `xap help` or `xap --help`                    |
| Get version                         | `gs version`                                 | `xap version`                                 |
| Get information                     | `gs stats`                                   | `xap info`                                    |
| Start agent components              | `gs gsa start-gsm | start-gsc | start-lookup`| `xap host run-agent [options]`                |
| Stop agent components               | `gs gsa shutdown [options]`                  | `xap host kill-agent [options]`               |
| List agent components               | `gs list [options]`                          | `xap host list [options]` and `xap container list [options]`|
| Deploy a space                      | `gs deploy-space [options] <space-name>`     | `xap space deploy [options] <space-name>`     |
| List spaces                         | `gs space list [options]`                    | `xap space list [options]` and `xap space info [options] <space-name>`
| Query a space                       | `gs space sql [options]`                     | `xap space query [options] <space-name> <type>` |
| Deploy a processing unit            | `gs deploy [options] <pu-name-or-file>`      | `xap pu deploy [options] <pu-name> <pu-file>` |
| Undeploy a processing unit          | `gs undeploy [options] <pu-name>`            | `xap pu undeploy [options] <pu-name>`         |
| Quiesce a processing unit           | `gs quiesce [options] <pu-name>`             | `xap pu quiesce [options] <pu-name>`          |
| Unquiesce a processing unit         | `gs unquiesce [options] <pu-name>`           | `xap pu unquiesce [options] <pu-name>`        |

# Obsolete Commands

This section lists commands which have not been ported to the new Command Line Interface for various reasons

## Interactive Shell Commands

The old Command Line Interface supports an interactive shell mode, and as such includes a set of shell-related commands, which are currently not supported in the new CLI as it is not interactive.

* `cd`
* `dir`
* `pwd`
* `ls`
* `set`
* `login`

## Elastic Processing Unit Commands

Since Elastic Processing Unit is deprecated, its commands have not been ported to the new Command Line Interface

* `deploy-elastic-space`
* `deploy-elastic-pu`
* `scale`

## Miscellaneous

* `pudeploy` - alias for `deploy` command
* `deploy-memcached` - Can be done via `deploy` command
* `deploy-rest` - Can be done via `deploy` command
* `deploy-application` and `undeploy-application` - Low usage rate, can be done via `deploy`/`undeploy` command and scripting
* `jconsole` - The new CLI is based on REST protocol, so this command is misleading. Users may use the standard JConsole directly.
* `admin multicastTest` - The new CLI is based on XAP Manager which uses unicast.
