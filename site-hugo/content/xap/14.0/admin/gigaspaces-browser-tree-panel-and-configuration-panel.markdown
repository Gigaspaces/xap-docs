---
type: post140
title:  Grid Tree & Configuration
categories: XAP140ADM, PRM
parent: gigaspaces-management-center.html
weight: 200
---
 

# Grid Tree Panel

The left hand panel of the Space Browser is the **Grid Tree**. This panel allows you to view and manage all registered space containers, their spaces on the network and clusters.

{{% align center %}}
![IMG212.gif](/attachment_files/IMG212.gif)
{{% /align %}}

The **Grid Tree** shows the following main types of nodes.

|Icon|Description|
|:-----|:----------|
| ![space_network_view_icon.gif](/attachment_files/space_network_view_icon.gif) | Root of the Spaces grid tree. |
| ![container.gif](/attachment_files/container.gif) | Space Container node. |
| ![spaceTreeIcon.gif](/attachment_files/spaceTreeIcon.gif) | Space node. |
| ![cluster_node.gif](/attachment_files/cluster_node.gif) | Cluster node. |

# Spaces Network View

Click the main node in the tree, labeled **Spaces**, to view all the spaces currently running in the network.

{{% align center %}}
![GMC_space_SpaceNetworkView_6.5_RC2.jpg](/attachment_files/GMC_space_SpaceNetworkView_6.5_RC2.jpg)
{{% /align %}}

The table shows various information regarding each space. Each column represents a different type of information. You can select the columns you want to appear in the table by right-clicking one of the column names and clicking **Select Columns**:

{{% align center %}}
![space_network_view_selectColumns_6.1.jpg](/attachment_files/space_network_view_selectColumns_6.1.jpg)
{{% /align %}}

In the Select Columns window, check the checkboxes of the columns you want to see, or **Select All** (automatically selects all checkboxes):

{{% align center %}}
![space_network_view_selectColumns_window_6.5.jpg](/attachment_files/space_network_view_selectColumns_window_6.5.jpg)
{{% /align %}}

{{% note %}}
The **Name** (space name) and **Space Container Name** are default options and cannot be unchecked.
{{%/note%}}

The columns to choose from are as follows:

- **Objects Count** -- the number of objects in the space.

{{% warning %}}
The **Objects Count** column does not display the number of objects for persistent spaces running in LRU mode (this might result in very high CPU usage). To change this, see the [Menus and Buttons](./gigaspaces-browser-menus-and-buttons.html#display-persistent-lru-space-metrics) section.
{{%/warning%}}

- **Object Count Delta** -- shows an ascending or descending arrow, if the number of objects in the space is currently growing or being reduced (when objects are either written to or taken from the space). For example, if you are currently taking objects from a certain space, during the take operation, an arrow pointing down appears in this column (see screenshot above).

{{% warning %}}
For this option to work, the **Objects Count** checkbox must also be selected.
{{%/warning%}}

- **Templates Count** -- the number of templates in the space.
- **Host Name** -- the name of the machine hosting the space container.
- **Jini Group** -- the Jini lookup group used when starting the space.
- **Locators** -- the Jini locators used when starting the space.
- **Schema name** -- the space's current schema.
- **Persistent** -- whether the space is persistent or not.
- **Clustered** -- whether the space participates in a cluster.
- **Primary** -- whether the space is primary (**Yes**) or a backup space (**No**).
- **Used Memory (MB)** -- the amount of used memory in this space, in MB.
This column is highlighted in orange if the space memory usage is more than the minimum (low) watermark percentage and lower than the write only block percentage (defined in the [memory usage tag](../dev-java/memory-management-overview.html)), for example:

{{% align center %}}
![GMC_space_SpaceNetworkView_UsedMoemory_Column_Orange_6.5_RC2.jpg](/attachment_files/GMC_space_SpaceNetworkView_UsedMoemory_Column_Orange_6.5_RC2.jpg)
{{% /align %}}

This column is highlighted in red if the space memory exceeds the write only block percentage (defined in the [memory usage tag](../dev-java/memory-management-overview.html)), for example:

{{% align center %}}
![space_network_view_memoryUsage_red_6.5.jpg](/attachment_files/space_network_view_memoryUsage_red_6.5.jpg)
{{% /align %}}

{{% refer %}}For more details on memory usage, refer to the [Memory Management](../dev-java/memory-management-overview.html) section.{{% /refer %}}

- **Write**, **Update**, **Read**, **Take**, **Notify Sent**, **Notify Ack.**, **Notify Registration** -- shows the number of objects the operation has been performed on. For example, 60000 in the **Write** column means that 60000 objects have been written to this space.

You can right-click a row in the table to view options for a space. These are the same options available under each space node (see [Space Node](#spacenode) below). Clicking one of the options in the context menu switches to the clicked view, and selects the space in the tree on the left. This is an easy way to access a space or any its sub-nodes directly, without expanding the **Grid Tree**.

{{% align center %}}
![space_network_view_spaceNodeSelected_6.5.jpg](/attachment_files/space_network_view_spaceNodeSelected_6.5.jpg)
{{% /align %}}

 

You can choose to refresh the Space Network view periodically. Select the desired refresh rate from the **Refresh Rate** drop-down menu. To stop automatically refreshing the Space Network View, click the **Stop** button. When auto-refresh is running, a green blinking dot is displayed on the right side of the screen.

# Space Container Node

Right-clicking a Space container node invokes a context menu containing the following options:

{{% align center %}}
![grid_tree_containerNodeSelected_6.5.jpg](/attachment_files/grid_tree_containerNodeSelected_6.5.jpg)
{{% /align %}}

- **Refresh** -- refreshes the tree panel display.
- **Runtime Configuration Report** -- displays the current configuration of the selected space container (see [Configuration](./gigaspaces-browser-managing-space-container.html#configuration)).
- **Launch JConsole** -- launches a [JConsole](./space-jmx-management.html) for the selected space container.

When the space container node is selected, its configuration data is displayed in the configuration panel on the right side.

Selecting the space container node also makes available the three leftmost buttons on the button toolbar at the top of the GigaSpaces Browser screen. These allow you to create a space and perform several maintenance operations on the space container.

# Space Node

{{% anchor spacenode %}}

Space nodes represent a single space in the system.

**The space node has five possible views:**

- [Classes view](./gigaspaces-browser-data-types-view.html)
- [Query view](./gigaspaces-browser-query-view.html)
- [Statistics view](./gigaspaces-browser-statistics-view.html)
- [Benchmark view](./benchmark-browser.html)
- [Transactions view](./gigaspaces-browser-transaction-view.html)
- [Connections view](./gigaspaces-browser-connection-view.html)

Right-clicking a space node invokes a context menu containing the following options:

- **Clean** -- cleans the space.
- **Ping** -- pings the space.

{{% align center %}}
![IMG214.gif](/attachment_files/IMG214.gif)
{{% /align %}}

When a space node is selected, its configuration data is displayed in the **Service View** panel on the right side:

{{% align center %}}
![network_view_config.gif](/attachment_files/network_view_config.gif)
{{% /align %}}

# Cluster Node

A cluster is a collection of spaces from one or more space containers. The cluster node in the **Grid Tree** represents a cluster of spaces.

{{% refer %}}
For more details, refer to the [Cluster View](./cluster-view-gigaspaces-browser.html) section.
{{%/refer%}}

# Service View Panel

The **Service View** panel takes up most of the Space Browser screen; it is located on the right of the **Grid Tree** panel. This panel shows configuration details for the node selected in the tree on the left, allowing you to edit some of them.

