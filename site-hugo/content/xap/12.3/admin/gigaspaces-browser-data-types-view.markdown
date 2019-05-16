---
type: post123
title:  Data Types View
categories: XAP123ADM, PRM
parent: gigaspaces-browser-tree-panel-and-configuration-panel.html
weight: 200
canonical: auto
---


The Data Types view displays a list of space data type (space classes) stored in the space, and their instance count. You can view the Data Type instances content using the [Query view](./gigaspaces-browser-query-view.html). Clear the Data Type instances or completely delete the Data Type and all its instances from the space.


{{%section%}}
{{%column width="80%" %}}
Details are displayed for each Data Type -- i.e., attributes and their properties (names, types, indexed), whether the Data Type is FIFO enabled, whether the Data Type is partial-replication enabled, a list of Super Data Types (inherited classes), etc. These properties appear at the bottom of the panel when the Data Type is selected.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/datatypeView1.jpg"%}}

{{%/column%}}
{{%/section%}}

# Data Type List

The **Data Type List** is located at the top of the panel. It displays the Data Type stored in the space, and their current instance and template count (how many objects and how many templates currently exist in the space). Data Type meta data is introduced to the space after the first write operation of one of its instances or after calling the snapshot method.

{{% note %}}
The **Data Type List* doesn't display any Data Types or objects for persistent spaces working when running in `LRU` mode. If you are working with such a space and want to turn on this option, from the top menu, select *View* > *Display Persistent LRU Space Metrics** (toggle option).
{{%/note%}}

The following buttons appear at the bottom of the **Data Type List** (right clicking a Data Type row also displays these options):

- **Query** -- opens the [Query view](./gigaspaces-browser-query-view.html) with the selected Data Type content in the result table, where each Entry is displayed in a separate row.
- **Delete** -- removes a Data Type and all its instances from the space. This option removes all existing templates and Data Type meta data from the space.
- **Clear** -- clears Data Type instances from the space, while keeping the Data Type meta data in the space.

# Data Types Info

The **Data Type Info** panel at the bottom of the panel displays the following:

- Public Data Type attributes, their types and indexes
- The name of the routing field
- List of super Data Types (inherited classes)
- Indication whether the Data Type is FIFO enabled.
- Indication whether the Data Type is partial-replication enabled

# Templates Info

The **Templates Info* tab at the bottom of the *Service View** displays the number of notification templates that exist in the space; and a table of all the notification templates, specifying when the template is supposed to expire, the UID of the object the template is registered for, and if the template indicates FIFO mode.

{{% align center %}}
![Pending Notify Templates Information.gif](/attachment_files/Pending Notify Templates Information.gif)
{{% /align %}}

# Refresh Options

You can choose to refresh the **Data Type List* periodically. Select the desired refresh rate from the drop-down menu. To stop automatically refreshing the *Data Type List*, click the *Stop* button. The refresh activity impacts server performance, so when running benchmarks you might want to stop auto-refresh activity (by clicking the *Stop** button). When auto-refresh is running, a green blinking dot is displayed on the right side of the screen.
