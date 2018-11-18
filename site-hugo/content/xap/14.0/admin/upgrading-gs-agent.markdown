---
type: post140
title:  Upgrading gs-agent
categories: XAP140ADM,PRM
weight: 700
parent: admin-legacy-tools.html
---

The 12.3 release has introduced a new [Command Line Interface](tools-cli.html), which provides the [host run-agent](admin-service-grid-agent.html) command for starting a GigaSpaces agent on the current host. 

The [legacy gs-agent](the-runtime-environment.html) script is still supported, but starting v14.0 it is deprecated and will be removed in the future. Customers upgrading from older versions are encouraged to upgrade to the new CLI command.

To get started with the new CLI command, simply run `xap host run-agent --help` from the product's `bin` folder.

# Commonly used options

Options which are commonly used have are basically the same, except for the command name:

| Task | Old command | New command |
|------|-----------|---------------|
| Run XAP Manager                      | `gs-agent --manager` | `xap host run-agent --manager` |
| Run N Grid Service Containers (GSCs) | `gs-agent --gsc=N`   | `xap host run-agent --gsc=N`   |
| Run Web Management Console (web-ui)  | `gs-agent --webui`   | `xap host run-agent --webui`   |

# Starting a Local manager

The `--manager-local` option was used to start a XAP Manager on a local environment without configuration. This has been superseded by the `--auto` option, which is actually smarter:

* If XAP Manager is not configured, it starts a local manager.
* If XAP Manager is configured, it inspects the configuration to check wether or not a manager should be started on the current host, and acts accordingly.

| Task | Old command | New command |
|------|-----------|---------------|
| Run a local XAP Manager | `gs-agent --manager-local` | `xap host run-agent --auto` |

# Custom component

The gs-agent is extensible, and allows users to create custom options thru `.xml` files which describe what those options do. This customization is still supported in the new syntax, except that it requires a `--custom` prefix, to instruct the command line interpreter to skip syntax validation for the custom option.

| Task | Old command | New command |
|------|-----------|---------------|
| Start N instances of `foo.xml` | `gs-agent --foo=N` | `xap host run-agent --custom foo=N` |

# Starting Management Components Manually

Before the XAP Manager was introduced in 12.1, users had to manually start the GSM and Lookup Service management components. This option is not available in the new command since the XAP Manager is now the recommended option, and starting a management environment without it is deprecated.

Note that you can still start GSM and LUS thru the `--custom` option, but we recommend using this option only as an interim step within the migration process, as in the future we'll remove support for non-manager environment.

Likewise, the `global` modifier is not available in the new command since it's only relevant for multicast discovery, whereas the XAP Manager uses unicast discovery. Again, you can use the `--custom` option to overcome this.

| Task | Old command | New command |
|------|-----------|---------------|
| Run Grid Service Manager (GSM) | `gs-agent --gsm=1` | `xap host run-agent --custom gsm=1` |
| Run Lookup Service (LUS)       | `gs-agent --lus=1` | `xap host run-agent --custom lus=1` |
| Run Global Grid Service Manager (GSM) | `gs-agent --global.gsm=1` | `xap host run-agent --custom global.gsm=1` |
| Run Global Lookup Service (LUS)       | `gs-agent --global.lus=1` | `xap host run-agent --custom global.lus=1` |
