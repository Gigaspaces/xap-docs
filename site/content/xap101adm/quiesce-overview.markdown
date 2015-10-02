---
type: post101adm
title:  Overview
categories: XAP101ADM
parent: quiescemode.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

# Introduction

The purpose of this feature is to add support for "Quiesce state" (a.k.a maintenance mode) for a processing unit.<br> If a space is **Quiesced**, it rejects all data-related operations with an exception, allowing the user to perform maintenance operations. <br>
Regardless to the space, XAP [Polling Container]({{%currentjavaurl%}}/polling-container.html) and [Notify Container]({{%currentjavaurl%}}/notify-container.html) are also aware of quiesce state and will handle quiesce state changed events by implementing [QuiesceStateChangedListener](./quiesce-pu-api.html#quiesce-state-changed-listener). <br>
As well as XAP event containers mentioned above, a user defined bean can also handle quiesce state changed events by implementing [QuiesceStateChangedListener](./quiesce-pu-api.html#quiesce-state-changed-listener). <br>
Quiesce is mostly triggered using ProcessingUnit API and propagated to all space instances and quiesce state changed listeners in the processing unit. <br>

{{%refer %}}
Quiesce mode can be also triggered using CLI (see [Quiesce CLI](./quiesce-command-line-interface.html))
{{%/refer%}}



# Motivation

There are several user stories which can benefit from quiesce state such as:

### Safe shutdown

Shutdown an application which contains a data grid and a mirror in a safe manner - make sure all operations completed on the grid are flushed to the mirror before undeploy:
  
1. Enter quiesce mode.
1. Wait for replication between the space and the mirror to complete (by monitoring the redolog).
1. Undeploy the space and mirror.

### Rolling system upgrade

Perform a rolling system upgrade on a live system (typically a processing unit code upgrade) and avoid inconsistencies related to processing of requests during the upgrade process:

1. Enter quiesce mode  <br>
2. Perform the hot-upgrade pattern: <br>
2.1.   Update the pu in the GSM<br>
2.2.  Restart the backups<br>
2.3. Restart the primaries<br>
3. Exit quiesce mode - the upgraded system resumes and works as expected

{{% refer %}}
Here you can find [use cases examples](./quiesce-pu-api.html#use-cases-examples)
{{%/refer%}}

# Space Behavior During Quiesce Mode
- The space will reject all user data-related new operations by throwing QuiesceException back to the user, replication of previous on going operations continues.
- Commit and abort are allowed in case of two phase commit transactions.
- Commit and prepare of a single phase transactions will be rejected.
- Lease manager will be paused except extend lease operation in order to prevent losing notify listener templates.

# Security
- On a secured system, one needs a `Manage PU` privileges to quiesce/unquiesce a processing unit.

# Configuration
- This feature can be disabled by setting `com.gs.engine.disableQuiesceMode` to true.
