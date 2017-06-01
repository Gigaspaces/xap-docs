---
type: post100
title:  Replication Group
categories: XAP100ADM
parent: working-with-clusters-gigaspaces-browser.html
weight: 500
---

{{% ssummary %}}Common settings, sync/async replication, transmission policies, filters, recovery options.{{% /ssummary %}}



A group can define a replication policy. If it does, then no other group that contains at least one space belonging to this group can define a replication policy. If a group defines a replication policy, we call it a "replication group".(A replication group can also be a failover group, etc.).
When a replication policy is defined for a group, the members of the group are synchronized according to that policy. The policy is independent of operations and transparent to the clustered proxy. The space members themselves cooperate to achieve synchronization.

See the [Replication](./replication.html) section for details.

# Common Settings

![Replication Group1GigaSpaces Browser.jpg](/attachment_files/Replication Group1GigaSpaces Browser.jpg)

# Synchronous Replication

![Replication Group2GigaSpaces Browser.jpg](/attachment_files/Replication Group2GigaSpaces Browser.jpg)

# Asynchronous Replication

![Replication Group3GigaSpaces Browser.jpg](/attachment_files/Replication Group3GigaSpaces Browser.jpg)

# Transmission policies

Click the Edit button to display the **Replication Matrix** window

![Replication Group4GigaSpaces Browser.jpg](/attachment_files/Replication Group4GigaSpaces Browser.jpg)

The Transmission Policies tab of the Group Members Attribute window allows you to define the transmission policies for the cluster group.
The Transmission Replication Policy Matrix allows you to define the replication relationships between group cluster space members. By clicking the relevant checkbox in the table you can define which space will be the replication target for the space.
You can define transmission policy for this replication by checking take, write and notify. To activate the **Sync on Commit** operation, check the Sync on Commit box.

# Filters/recovery options

The Filters/Recovery tab of the Group Members Attribute window allows a filter class name to be defined that will be activated at the time of replication. This filter class will contain your business logic to enable/disable the replication process.

![Replication Group5GigaSpaces Browser.jpg](/attachment_files/Replication Group5GigaSpaces Browser.jpg)

Define an Input/Output filter class name for each group cluster space member by clicking its name on the list on the left-hand side and entering the appropriate class name.

You can define recovery policy by checking the Enabled box and using the Source Member drop-down box to select First Available Member or any other space in the group.
![Replication Group6GigaSpaces Browser.jpg](/attachment_files/Replication Group6GigaSpaces Browser.jpg)
