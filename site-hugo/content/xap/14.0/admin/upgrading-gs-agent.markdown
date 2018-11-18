---
type: post140
title:  Upgrading the GS-Agent Command
categories: XAP140ADM,PRM
weight: 700
parent: admin-legacy-tools.html
---

When the new [Command Line Interface](tools-cli.html) was introduced in InsightEdge Platform and XAP release 12.3, it included the [host run-agent](admin-service-grid-agent.html) command for starting a GigaSpaces agent on the current host. 

While the [legacy gs-agent](the-runtime-environment.html) script is still supported, it has been deprecated as of InsightEdge Platform and XAP release 14.0 and will be removed in a future version. Customers upgrading from older versions are encouraged to upgrade to the new CLI command.

To get started with the new CLI command, simply run `xap host run-agent --help` from the product's `bin` folder.

# Commonly Used Options

The following commonly-used options have the same functionality, except for the command name:

| Task | Old Command | New Command |
|------|-----------|---------------|
| Run XAP Manager                      | `gs-agent --manager` | `xap host run-agent --manager` |
| Run 'N' Grid Service Containers (GSCs) | `gs-agent --gsc=N`   | `xap host run-agent --gsc=N`   |
| Run the Web Management Console (web-ui)  | `gs-agent --webui`   | `xap host run-agent --webui`   |

# Starting a Local Manager

The `--manager-local` option was used in the previous version to start a XAP Manager on a local environment without configuration. This is superseded by the `--auto` option in the new version, which has the following improved functionality:

* If XAP Manager is not configured, the command starts a local manager.
* If XAP Manager is configured, the  configuration is inspected to check whether or not a manager should be started on the current host, and the command is executed accordingly.

| Task | Old Command | New Command |
|------|-----------|---------------|
| Run a local XAP Manager | `gs-agent --manager-local` | `xap host run-agent --auto` |

# Custom Component

The gs-agent is extensible, and allows users to create custom options using `.xml` files that describe what those options do. This customization is still supported in the new syntax, but now requires a `--custom` prefix, to instruct the command line interpreter to skip syntax validation for the custom option.

| Task | Old Command | New Command |
|------|-----------|---------------|
| Start 'N' instances of `foo.xml` | `gs-agent --foo=N` | `xap host run-agent --custom foo=N` |

# Starting Management Components Manually

Before the XAP Manager was introduced in release 12.1, users had to manually start the GSM and Lookup Service management components. This option is not available in the new command because the XAP Manager is now the recommended option, and the ability to start a management environment without it has been deprecated.

{{%note%}}
You can still start GSM and LUS using the `--custom` option, but we recommend doing this only as an interim step within the migration process, because support for non-manager environments will be removed in a future version.
{{%/note%}}

In addition to the above, the `global` modifier is not available in the new command because it is only relevant for multicast discovery, and the XAP Manager uses unicast discovery. You can use the `--custom` option as a workaround for this.

| Task | Old Command | New Command |
|------|-----------|---------------|
| Run Grid Service Manager (GSM) | `gs-agent --gsm=1` | `xap host run-agent --custom gsm=1` |
| Run Lookup Service (LUS)       | `gs-agent --lus=1` | `xap host run-agent --custom lus=1` |
| Run Global Grid Service Manager (GSM) | `gs-agent --global.gsm=1` | `xap host run-agent --custom global.gsm=1` |
| Run Global Lookup Service (LUS)       | `gs-agent --global.lus=1` | `xap host run-agent --custom global.lus=1` |
