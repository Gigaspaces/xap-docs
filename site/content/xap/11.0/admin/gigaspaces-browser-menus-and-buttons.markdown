---
type: post110
title:  Menus and Buttons
categories: XAP110ADM
parent: gigaspaces-management-center.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}

# File Menu

The **Exit** option closes the XAP Management Center.

{{%align center%}}
![GMC_space_FileMenuOption_6.5.jpg](/attachment_files/GMC_space_FileMenuOption_6.5.jpg)
{{%/align%}}

# Launch Menu

The **Launch** menu allows you to perform operations in the Deployments tab:

{{%align center%}}
![GMC_space_LaunchMenuOption_6.5.jpg](/attachment_files/GMC_space_LaunchMenuOption_6.5.jpg)
{{%/align%}}

|Syntax|Description|
|:-----|:----------|
| <nobr>SBA Application - Processing Unit</nobr> |deploys a Processing Unit that includes spaces and other application services to the Service Grid. |
| **Enterprise Data Grid** | deploys a Data Grid in a specific space topology.|
| **Single Space** | deploys a single space in a local GSC. |
| **Grid Service Manager** | launches a local Grid Service Manager in the GMC.|
| **Grid Service Container** | launches a local Grid Service Container in the GMC.|
| **Service Launcher** | allows you to launch GSCs and GSMs and to define their properties.|


# Space Menu

The **Space** menu provides options relevant for a specific space.

{{%align center%}}
![GMC_space_SpaceMenuOption_6.5.jpg](/attachment_files/GMC_space_SpaceMenuOption_6.5.jpg)
{{%/align%}}

The **Space** menu is enabled only when pressing a space node  in the tree on the left.

The **Space** menu allows you to:

|Syntax|Description|
|:-----|:----------|
|Clean a space | remove all entries from the space.
|Ping a space | verifies that the space exists and is running correctly.
|Restart** a space.||
|<nobr>Destroy a space (Destroy Space).</nobr>||
|Start or stop a preexisting space |the **Stop** option is enabled when the selected space is in a started state, and the **Start** option is enabled when the selected space is in stopped state. |
|Display Persistent LRU Space Metrics | see the following section.   |

## Display Persistent LRU Space Metrics

When working with persistent spaces in LRU mode, this option turns on the instance count (a list or total of instances) in the relevant Spaces Network view row; and in the Classes view and Cluster view under the relevant space/cluster node.

{{% note %}}
This option is turned off by default, since displaying the instance count for such spaces might result in very high CPU usage.
{{%/note%}}

Clicking **Space** > **Display Persistent LRU Space Metrics** turns on the instance count for all persistent LRU-mode spaces you are using. Clicking this option again turns off the instance count for these spaces.

# View Menu

The **View** menu provides the following options:

{{%align center%}}
![GMC_space_ViewMenuOption_6.5.jpg](/attachment_files/GMC_space_ViewMenuOption_6.5.jpg)
{{%/align%}}

|Syntax|Description|
|:-----|:----------|
|Refresh Now ||
|Update Speed|choose the desired speed or provide a custom speed. |
|Shape Mode |allows you to define the shape of the graphical representation for clusters -- oval or rectangle. |
|Zoom |allows you to zoom in or zoom out of the cluster view. |
|Options |the options below are related to the shapes representing the cluster members in the Cluster view. These options can be useful when wanting to reduce CPU usage.|
|Freeze all Members |stops updating all cluster member shapes.|
|<nobr>Backward Replication</nobr> |relevant for replicated cluster members, shows the direction of replication -- an arrow is drawn from the replicating space to the replicated space.|
|Show Objects Count |stops showing the number of objects in each cluster member. If this options is used and one of the members is a persistent space in LRU mode, the [Display Persistent LRU Space Metrics](#display-persistent-lru-space-metrics) will not take affect.|

# Settings Menu

The **Settings** menu provides the following options:

{{%align center%}}
![GMC_space_SettingsMenuOption_6.5.jpg](/attachment_files/admin_ui/GMC_space_SettingsMenuOption_11.0.png)
{{%/align%}}

- **Discovery**

{{%align center%}}
![GMC_space_SettingsMenuOption_Discovery_6.5.jpg](/attachment_files/admin_ui/GMC_space_SettingsMenuOption_Discovery_11.0.png)
{{%/align%}}

- **Group Management** -- opens the Discovery Group Selection window, allowing you to manage different Jini groups:

{{%align center%}}
![GMC_space_SettingsMenuOption_Discovery_DiscovGroupSelection_Window_6.5.jpg](/attachment_files/GMC_space_SettingsMenuOption_Discovery_DiscovGroupSelection_Window_6.5.jpg)
{{%/align%}}

Selecting a Jini group defines which services (containers, spaces, GSCs, GSMs) are displayed in the GigaSpaces Management Center according to their Jini group. After updating, press **Apply** and then **OK**, or **Close** without saving.

- **Locator Discovery** -- allows you to find new locators using a port number. Type a port number in the first text box, and click *Add*. The port is then added to the **managed set of locators** at the bottom.

{{%align center%}}
![GMC_space_SettingsMenuOption_Discovery_LocatorDiscov_Window_6.5.jpg](/attachment_files/GMC_space_SettingsMenuOption_Discovery_LocatorDiscov_Window_6.5.jpg)
{{%/align%}}

- **Discovery Stats** -- opens the Lookup Service Discovery Times window, which displays the locator IP address, its Jini group, and the time in \[ms\]:

{{%align center%}}
![GMC_space_SettingsMenuOption_Discovery_LookupServiceDiscovTimes_Window_6.5.jpg](/attachment_files/GMC_space_SettingsMenuOption_Discovery_LookupServiceDiscovTimes_Window_6.5.jpg)
{{%/align%}}



## Configured System Properties Window

The **System Properties** option opens a window listing all configured [system properties](./runtime-configuration.html). The Configured System Properties window allows you to edit existing system properties, and add new properties.

{{%align center%}}
![GMC_space_SettingsMenuOption_Settings_ConfiguredSystemProp_Window_6.5.jpg](/attachment_files/GMC_space_SettingsMenuOption_Settings_ConfiguredSystemProp_Window_6.5.jpg)
{{%/align%}}

To edit an existing value, double click it, and type in your changes:

{{%align center%}}
![GMC_space_SettingsMenuOption_Settings_ConfiguredSystemProp_Window_Editing_6.5.jpg](/attachment_files/GMC_space_SettingsMenuOption_Settings_ConfiguredSystemProp_Window_Editing_6.5.jpg)
{{%/align%}}

{{%align center%}}
|![IMG365.gif](/attachment_files/IMG365.gif)| To add a new value, click the + button, which adds a new row. Type the property on the left side, and its value on the right side.|
{{%/align%}}

Press **OK** to accept the changes and exit the window, or **Cancel** to exit without saving the changes.

# Help Menu

The **Help** menu provides the following options

{{%align center%}}
![GMC_space_HelpMenuOption_6.5.jpg](/attachment_files/GMC_space_HelpMenuOption_6.5.jpg)
{{%/align%}}


|Syntax|Description|
|:-----|:----------|
|About|provides product version information.|
|Online Help|opens the [GigaSpaces Online Help homepage]({{%currentjavaurl%}}).|

## Button Toolbar

The button toolbar allows you to perform maintenance operations on containers and spaces.

|Syntax|Description|
|:-----|:----------|
| ![ping.gif](/attachment_files/ping.gif) | Pings the selected space |
| ![clean_space.gif](/attachment_files/clean_space.gif) | Cleans the selected space |
| ![destroy_space.gif](/attachment_files/destroy_space.gif) | Destroys the selected space |
| ![GMC_space_groupManagementToolbarIcon_6.5.jpg](/attachment_files/GMC_space_groupManagementToolbarIcon_6.5.jpg) | Opens the [Discovery Group Selection](#settings-menu) window |
| ![GMC_space_ResetPerspectiveToolbarIcon_6.5.jpg](/attachment_files/GMC_space_ResetPerspectiveToolbarIcon_6.5.jpg) | Resets all main window tabs, returns docking windows to their initial size and location. |
| ![help.gif](/attachment_files/help.gif) | Opens the [GigaSpaces Online Help homepage]({{% currentjavaurl %}}) |

# Address Bar

The **Address** bar allows you to specify either a space, container, or cluster address to view and manage. You can use a Jini or RMI address type. Typing your address and pressing Enter generates the relevant space or cluster node in the Grid Tree on the left.
