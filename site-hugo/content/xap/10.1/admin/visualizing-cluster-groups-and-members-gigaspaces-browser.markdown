---
type: post101
title:  Groups and Members
categories: XAP101ADM
parent: cluster-view-gigaspaces-browser.html
weight: 400
canonical: auto
---

{{% ssummary %}}Viewing clustered groups, clustered spaces and URLs, and accessing information views for a clustered space.{{% /ssummary %}}



When you click the cluster node in the Cluster Tree, the visual display area shows groups as gray ovals, while the information panel shows the names and URLs of members.

# Visualizing Cluster Groups

Groups are structures within a cluster that define the relationship between spaces. A group may have one or more of the following attributes: replication, failover and load-balancing. For two spaces to maintain a replication link, for example, both must belong to a replication group. A cluster must have at least one group, and spaces may not belong to more than one group.

Each group shown in the visual display area has two lines of text. The first line is the group's name (or the beginning of the name, followed by an ellipsis). The second line shows the group's attributes, using abbreviations denoted in the legend.

You can move the groups around by clicking and dragging them; this has no effect on the cluster configuration. Double-clicking a group has the same effect as clicking its name in the **Cluster Tree**.

# Viewing Clustered Spaces and URLs

When the cluster node is selected, the information panel shows the names of all cluster members, their direct URLs and Primary/Backup indication.

# Accessing Information Views for a Clustered Space

A space has five information views, which appear under the space node in the Grid Tree. To access these views, right-click the space you want to view in the Cluster view's visual display:

{{% indent %}}
![Visualizing Cluster Groups and Members.gif](/attachment_files/Visualizing Cluster Groups and Members.gif)
{{% /indent %}}

From the context menu, select one of the options to access one of the space information views:

- [Query view](./gigaspaces-browser-query-view.html)
- [Statistics view](./gigaspaces-browser-statistics-view.html)
- [Benchmark view](./benchmark-browser.html)
- [Transactions view](./gigaspaces-browser-transaction-view.html)
- [Date Types](./gigaspaces-browser-data-types-view.html)

