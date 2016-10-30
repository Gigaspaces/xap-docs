---
type: post121
title:  Space Inter-relations
categories: XAP121ADM, PRM
parent: cluster-view-gigaspaces-browser.html
weight: 300
---

 Viewing spaces in different cluster policy groups and how they relate to each other.



When you click a group (![Viewing Space Interrelations4.gif](/attachment_files/Viewing Space Interrelations4.gif)) in the cluster tree, the visual display area shows all the spaces in the group you clicked (or in the group of the space you clicked). If you clicked an individual space, the visual display focuses on that space, and the information panel shows detailed information about it.

# Visualizing Spaces within a Group

{{%align center%}}
![Viewing Space Interrelations in Cluster1.gif](/attachment_files/Viewing Space Interrelations in Cluster1.gif)
{{%/align%}}

Each space is shown in the visual display as an oval or a rectangle, whose fill color denotes its current status. Green spaces are currently online; gray spaces are unreachable by the server you are working on, and are probably offline. The text beneath each shape is the space's name, displayed in full.
The visual display is dynamic: You can move the spaces around, by clicking and dragging them, to get a clearer picture of the group. By default, spaces will bounce out of the way when you drag another space near them. If you want to prevent this, select **Freeze All Members* in the *Options** menu. Moving the spaces in the visual display has no effect on the cluster configuration.
When you select a space, by double-clicking it or selecting its node in the tree, the visual display focuses on it, and the space's oval (or rectangle) is given a white outline.

# Visualizing Replication Channels in a Group

If the group is a replication group, and the selected space is replicated, dotted lines appear that illustrate its replication connections. In the above example, the space Agency_Compunction replicates to five other spaces, but only the replication channel to the space Agent_Harmless is currently active.
A dotted line between the selected space (with a white outline) and another space (with a black outline) indicates that a replication connection exists, with the selected space as the source and the black-outlined space as the destination. A red dotted line indicates that the replication is inactive, generally because one or both of the replicated spaces are down; a green dotted line indicates the replication is presently active.
As mentioned above, the dotted lines indicate that a replication exists from the selected space to another space, but not whether the replication is mutual. In other words, you don't know if there is a replication back to the selected space. There are two ways to find out:

1. Select a destination space (a space to which the currently-selected space replicates) and see if the visual display draws a dotted line back to the original space. If so, the replication is mutual. If not, there is a one-way replication from the original space to the destination space.
1. From the **Options** menu, select **Show Backwards Replication**. With this option turned on, the Cluster Viewer draws a thin solid line on top of the dotted replication line, to indicate that there is backward replication from the destination space. This option may cause visual clutter, and so it is not turned on by default.

{{% tip %}}
There is no graphic representation of failover and load-balancing connections between spaces.
{{% /tip %}}

# Viewing the Replication Matrix of a Space

To view the replication matrix of a space, first select it by double-clicking it or selecting its node in the tree. In the information panel, click the **Replication** tab, then the internal tab **Table**. The Replication tab can be clicked only if the space belongs to a replication group.
The **Transmission Matrix** table lists all the spaces in the group, indicates which spaces the **Source Member** (the selected space) replicates to, and which space operations are replicated. The table contains the following columns:

- **Enable** -- if checked, the **Source Member** replicates to the **Target Member**.
- **Target Member** -- the name of the target space.
- **Take** -- if checked, all take operations on the **Source Member** are replicated to the **Target Member**.
- **Write** -  if checked, all write operations to the **Source Member** are replicated to the **Target Member**.
- **Notify** - if checked, all notifications received by the **Source Member** are replicated to the **Target Member**.
- **Sync on Commit** - if checked, Entries written under a transaction are replicated only once the transaction is committed (Sync on Commit).
