---
type: post110
title:  Services Manager
categories: XAP110NET
parent: installation-overview.html
weight: 500
---


When installing the Service Grid on a Microsoft Windows environment, many administrators prefer to setup the Service Grid as A *Windows Service*, which have several advantages over standard console applications:

- A service can be configured to start automatically when the machine boots, without any user logging in.
- A service can be configured to run under predefined credentials, e.g. SYSTEM.
- A service has no console/GUI which clutters up the desktop (and might be closed accidentally...).
- Windows provides standard management console, command line and API to manage services, which makes managing services a common task for system administrators.

This page explains how to install a Service Grid as a Windows Service and manage it.

# Usage

The Windows Services management console lets users start/stop installed services and modify their properties, but does not support installing new services. This task is can be done via a command line, or during installation of an application.

Instead, GigaSpaces XAP.NET provides a supplementary tool called **GigaSpaces Services Manager** (located at the `bin` folder) which simplifies common administration tasks:

- Install/uninstall Service instances of `Grid Service Agent` as you please.
- Perform common operations directly from the tool, no need to switch to the Windows Console (e.g. Start, Stop, change startup type).
- Side-by-side support for GigaSpaces Installations of different versions on the same machine.
- Automatically creates a folder for new service instances, with an XML configuration and log files.
- Create custom templates of different service grid configurations.

![ServicesManager.jpg](/attachment_files/dotnet/ServicesManager.jpg)

{{% note %}}
**Note:** Some operations require elevated permissions - make sure you run with appropriate permissions. If you're using Windows Vista or later and UAC is turned on, it is recommended to use 'Run As Administrator' (for more info see: [http://support.microsoft.com/kb/922708](http://support.microsoft.com/kb/922708))
{{%/note%}}

# Advanced

## Service Properties

To view a service properties, right-click it and select **Properties**, or simply double-click it. A dialog window with the service properties will appear.

## Configuration

The Service Properties window shows the name of the service configuration file. Either Click the configuration label to open the configuration file using your default XML viewer, or click the location label to open the service folder, then edit the configuration file using your favorite XML editor.

{{% note %}}
**Note:** In order for configuration changes to take effect the service needs to be stopped and restarted.
{{%/note%}}
