---
type: post123
title:  Statistics View
categories: XAP123ADM, PRM
parent: gigaspaces-browser-tree-panel-and-configuration-panel.html
weight: 500
---


The Statistics view provides information on operation statistics in the Space, such as read, write, take, etc. If you have a system with MemoryXtend, you can also see statistics for the off-heap driver and the disk storage (RocksDB) driver.

# Operations Statistics

The Operations tab provides a graphical representation of Space operations performed and average throughput.

{{% align center %}}
![GMC_space_statistics_7.1.jpg](/attachment_files/GMC_space_statistics_7.1.jpg)
{{% /align %}}

## Operations Statistics Status Area

The graph in the upper area of the tab displays the total number of operations performed in the Space. The legend on the right side shows the color that represents each operation in the graph. You can select and clear the check boxes as necessary to show or hide data for the different operations in the graph.

## Throughput Statistics History Area

The graph in the lower area of the tab display the following information, depending on the selected option:

- **Display TP (Operations/Sec) History** -- displays the current throughput performance (operations per second).
- **Display Total Operations Count History** -- displays the history of the Space operations, namely the number of operations performed since you began working with the Space.


Additional options:
- **Clear** button -- clears the graph.
- The **Graph Scaling** area in the bottom right provides a check box for every operation, allowing you to remove it from the graph.

# Refresh Options

You can choose to refresh the statistics display periodically. When auto refresh is running, a green blinking dot is displayed on the right side of the screen. Select the desired refresh rate from the drop-down menu. To stop the statistics automatic refresh, click the **Stop** button.

{{% note %}}
Auto-refresh activity impacts server performance, so you might want to stop it when running benchmarks.
{{%/note%}}

{{% align center %}}
![GMC_space_statistics_RefreshRate_area_TopRight_7.1.jpg](/attachment_files/GMC_space_statistics_RefreshRate_area_TopRight_7.1.jpg)
{{% /align %}}

