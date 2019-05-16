---
type: post120
title:  Customizing
categories: XAP120ADM, PRM
parent: cluster-view-gigaspaces-browser.html
weight: 100
canonical: auto

---

{{% ssummary %}}{{% /ssummary %}}

Changing cluster visualization behavior, refresh speed, and colors.

**Cluster View offers several ways of customizing or manipulating the way it displays clusters:**

- Changing how spaces look -- Select **Shape Mode** from the **View** menu, and select either **Oval** or **Rectangle** to specify how spaces should appear in the visual display area. Spaces appear as ovals by default. An advantage of rectangular space shapes is that space names are easier to read, particularly when there are many spaces in the group.
- Changing how spaces behave when you move them -- Check **Freeze All Members** in the **Options** menu to stop spaces from bouncing away when you drag a space near them. Uncheck this option to return to the default behavior.
- Showing backwards as well as forward replication -- Check **Show Backwards Replication* in the **Options** menu if you want to see a graphic indication of two-way replication. When this option is turned on, a thin solid line is drawn on top of regular dotted replication lines, if the destination space replicates back to the selected space. This option is turned off by default, as it may cause visual clutter.
- The **Update Speed* command in the *View** menu allows you to change the frequency by which Cluster View updates the information it displays.
    - **High** refreshes the viewer every second.
    - **Medium** refreshes every five seconds.
    - **Low** refreshes every ten seconds.
    - **Paused** stops the automatic refresh.

- Custom opens an input dialog that allows you to enter a refresh frequency in seconds.
- You may also refresh the display at any time, without waiting for the automatic update, by selecting **Refresh Now** from the **View** menu.
- Changing colors in the visual display -- To change any of the colors used in the visual display, click the color's box in the legend area on the right. The Set Color dialog opens, allowing you to select a color using predefined swatches, an HSB plane or RGB indicators.

![Customizing Cluster View - GigaSpaces Browser.gif](/attachment_files/Customizing Cluster View - GigaSpaces Browser.gif)
