---
type: post120
title:  Platform Configuration
categories: XAP120NET, PRM
parent: administrators-guide.html
weight: 400
---

{{% ssummary %}} {{% /ssummary %}}

This page explains how to configure XAP.NET Grid environment. In general, the default settings provided are good for small development or production environment. You should change these to satisfy your specific environment to increase the GSC capacity , specify lookup service location, specify zones , enable security, specify log files location, etc.

You will find the XAP .NET grid configuration within the `GS_HOME\NET v....\Config\Settings.xml`. Each machine running XAP .NET grid should have its `Settings.xml` modified. 

# Typical XAP .NET Grid Setup

A typical XAP .NET grid setup process involves:

**Step 1**

Set the `XapNet.HostName` to have the machine IP. This is required for machines with multiple networks address (multi-NIC).

**Step 2**      

Set the `XapNet.Locators` to have two machines IP comma separated that will run the lookup service (`IP1,IP2` or `MachineAddress1,MachineAddress2`). These machine XAP Agents should be started first!  The `XapNet.Locators`  should be set only with environments that do not have multicasting enabled or with environments where client applications don't have multicasting enabled between client machine and XAP .Net grid machines.

**Step 3**

Set the `XapNet.Gsc.Memory.Maximum` to have the right size.  Usually 20480 (20GB) will be a good number. For example: A machine with 64 GB RAM should have 3 GSCs running , each with 20GB GSC as Maximum size. 

**Please make sure firewall and any anti-virus SW are DISABLED on XAP .Net Grid machines!**

# Platform Configuration Properties

Below common platform configuration properties you should set when you setup your XAP .Net grid:


| Property name  | Description | Default value  |
|-----------------|------------|----------|
|XapNet.Path| XAP .NET folder location |$(XapNet.SettingsFile)\..\.. |
|XapNet.Install.Path| Location of XAP .NET Installation| $(XapNet.Path)\..|
|XapNet.Config.Path| Location of Configuration folder| $(XapNet.Path)\Config|
|XapNet.Runtime.Path| XAP Runtime files Location | $(XapNet.Install.Path)\Runtime|
|XapNet.Runtime.JavaHome| JDK home folder|$(XapNet.Runtime.Path)\Java|
|XapNet.Logs.ConfigurationFile| logging config folder|$(XapNet.Config.Path)\Logs\xap_logging.properties|
|XapNet.Logs.Path| log files folder|$(XapNet.Path)\Logs|
|XapNet.Logs.FileName| logging file name. This include log file format| {date,yyyy-MM-dd~HH.mm}-gigaspaces-{service}-{host}-{pid}.log|
|XapNet.HostName| Machine Name or IP| %COMPUTERNAME%|
|XapNet.Multicast.Enabled| Lookup multicast discovery mode|true|
|XapNet.Groups| Lookup discovery group|  XAP-x.x.x-ga-NET-x-x|
|XapNet.Locators| Lookup discovery locators. Should include list of all machines running lookup service|  |
|XapNet.Zones| Service grid container zone. |  |
|XapNet.Security.Enabled| security enabled mode| false|
|XapNet.ServiceGrid.Deploy.Path| Processing Unit Deploy folder| $(XapNet.Path)\Deploy|
|XapNet.ServiceGrid.Work.Path| work folder. Used to store temp files such deployed PU files , redo log overflow files|$(XapNet.Path)\Work|
|XapNet.GsAgent.Config.Path| Agent config folder| $(XapNet.Config.Path)\GsAgent\||
|XapNet.Gsc.Memory.Initial| GSC initial heap size in MB |16 |
|XapNet.Gsc.Memory.Maximum| GSC max heap size in MB |512 |
|XapNet.Gsm.Memory.Initial| GSM initial heap size in MB |16 |
|XapNet.Gsm.Memory.Maximum| GSC max heap size in MB |512 |
|XapNet.Lus.Memory.Initial| Lookup initial heap size in MB |16 |
|XapNet.Lus.Memory.Maximum| Lookup max heap size in MB|512 |
|DefaultLookupGroups| Default Lookup Groups - used with older versions | $(XapNet.Groups) |
|DefaultLocators| Default Locators  - used with older versions| $(XapNet.Locators) |
