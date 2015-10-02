---
type: post100adm
title:  Policy Settings
categories: XAP100ADM
parent: cluster-view-gigaspaces-browser.html
weight: 200
---

{{% ssummary %}}How to view replication, load-balancing and failover settings for a space in Cluster View. {{% /ssummary %}}



When you select a space node in the Cluster Tree, the information panel shows configuration details for that space, in the context of the attributes of its group. For example, if a space belongs to a replication and load balancing group, the information panel shows its replication and load balancing settings.

# Viewing Replication Settings

To view the replication settings of a space, first select it by clicking its node in the tree or double-clicking it in the visual display area. In the information panel, click the **Replication** tab, then the internal tab **Settings**. The **Replication** tab can be clicked only if the space belongs to a replication group.
For more information on these settings, see [Replication Options](./replication-group-gigaspaces-browser.html).

# Viewing Load-Balancing Settings

To view the load-balancing settings of a space, first select it by clicking its node in the tree or double-clicking it in the visual display area. In the information panel, click the **Load Balancing** tab (the tab can be clicked only if the space belongs to a load-balancing group).

# Viewing Failover Settings

To view the failover settings of a space, first select it by clicking its node in the tree or double-clicking it in the visual display area. In the information panel, click the **Fail Over** tab (the tab can be clicked only if the space belongs to a failover group).

# Viewing Classes View Settings

To view a space's classes view settings, select Classes View in the Grid Tree. The Classes View Information window will be displayed.
In the output generated above, the **Class Name** is the name of the instance used, while its corresponding count is the number of objects per instance. When an Entry is highlighted, the lower pane shows the name of the object selected (*Name*), its field type (**Type**) and whether or not it is indexed (**true** or **false**).
