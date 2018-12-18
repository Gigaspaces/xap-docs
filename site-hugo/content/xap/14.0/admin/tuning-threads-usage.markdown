---
type: post140
title:  Threads Usage
categories: XAP140ADM, OSS
parent: tuning.html
weight: 900
---




XAP uses Thread resources in an extensive manner to scale the different activities executed within the JVM process. The may happen at the client or server side. Some of the modules within the server or client side (proxy) require some tuning before moving into production. This section describes the important Thread (Thread pools) started and provides information when and how these should be tuned.

# Threads List

- Indicates the thread name includes a running number.
- SPACE_NAME - Indicates the thread name includes the associated space name using this thread.

{{% note "Note"%}}
All GigaSpaces threads running within the JVM use the **GS-** prefix as part of their name.
{{%/note %}}


| Thread Name | Description | Configuration Parameters | Client<br>Server|
|:------------|:------------|:-------------------------|:-------------|
|LRMI Selector Accept |Responsible for accepting incoming socket connections. |Single Thread|Server|
|LRMI async Selector  |Client side, handles async invocation, i.e executors, asyncRead/take. |4 Threads|Server|
|Pending Answers-pool |Responsible for sending a callback to template based waiting  operations (read/take). |space-config.engine.min_threads<br>space-config.engine.max_threads|Server|
|Notifier-pool        |Responsible to dispatch notification to client side. Used with the `Notify Container` [Java version](../dev-java/notify-container-overview.html) \|[ .NET version](../dev-dotnet/notify-container-overview.html) and [Session Based Messaging API](../dev-java/session-based-messaging-api.html). See the [Scaling Notification Delivery](../dev-java/notify-container-overview.html#scaling-notification-delivery) for details.| space-config.engine.notify_min_threads<br>space-config.engine.notify_max_threads| Server|
|Processor-pool       |Pool for space operations post processing that can be done asynchronously to user operation. Transaction cleanup, notifications etc.  |space-config.engine.min_threads<br>space-config.engine.max_threads |Server|
|Connection-pool      |Pool for regualr space operations execution.  |com.gs.transport_protocol.lrmi.max-threads <br> com.gs.transport_protocol.lrmi.min-threads |Server|
|Custom pool          |Pool for notify and task execution  |com.gs.transport_protocol.lrmi.custom.<br>threadpool.min-threads<br>com.gs.transport_protocol.lrmi.custom.<br>threadpool.max-threads |Server|
|System pool          |Pool for admin operations           |com.gs.transport_protocol.lrmi.system-priority.threadpool.min-threads<br>com.gs.transport_protocol.lrmi.system-priority.threadpool.max-threads |Server|
|SG LeaseReaper       | Used by the Service Grid | Single Thread |Server|
|Template Expiration Manager Timer | The main thread of expiration manager. Wakes up on each expiration period to find the expired templates. | Single Thread per space | Server|
|TemplateExpirationManager-pool#|Responsible for sending response to waiting client when their template expires.| 16 threads max per space |Server|
|SyncReplicationChannel SPACE_NAME|Runs per sync replication channel |Dynamically adjusted|Server|
|CapabilityChannel  |  |  |Server|
|ClassLoaderCache-<br>SelfCleaningTable |Used to clean up resources of class loaders once terminated (undeploy of processing unit) |Single Thread|Server|
|TransactionTableHolder-SelfCleaningTable | Responsible for cleaning zombie local transactions that were abandoned by the user application without committing. Single thread per GSC |Single Thread per space |Server|
|SLA Monitor Disk|Used by the Service Grid. SLA free disk space monitor|Single Thread|Server|
|Memory:writer|Used by the Service Grid. SLA Memory monitor|Single Thread|Server| 
|CPU:writer|Used by the Service Grid. SLA CPU monitor |Single Thread|Server|
|pollingEventContainer#|Used with the [Polling Container](../dev-java/polling-container-overview.html) | See the [Concurrent Consumers](../dev-java/polling-container-scaling.html)|Client|
|LeaseRenewalManager Task| Respisible to review resource lease.|One per resource|Client|
|JoinManager Task| Responsible to communicate with the lookup service|Single thread per client proxy| Client|
|Liveness-detector| See the [Proxy Connectivity](./tuning-proxy-connectivity.html) for details.|Single thread per client proxy|Client|
|Liveness-monitor| See the [Proxy Connectivity](./tuning-proxy-connectivity.html) for details.| Single thread per client proxy|Client|
|LocalTransactionManagerImpl<br>$Reaper SPACE_NAME | A thread that reaps expired transactions entries and other objects| Single thread per space | Server|
|GSPingManager| Used by the Service Grid| |Server|
|LeaseManager$Reaper SPACE_NAME |See the [Lease Manager](../dev-java/leases-automatic-expiration.html#lease-manager) for details |Single Thread per space| Server|
|Cache PersistentGC|Responsible for backup activities (cleanup indexes)|Single Thread per space|Server|
|SPACE_NAME BackgroundFifoThread3_Notify=false#|Threads that use segmentation in order to handle background events like handling waiting templates in a fifo fashion | Thread pool per space|Server|
|SPACE_NAME BackgroundFifoThread3_Notify=true#|Threads that use segmentation in order to handle background notify events  in a fifo fashion|Thread pool per space|Server|
|Statistics-Task| | | Server|
|SPACE_NAME Batch Notifier|Used when using batch notifications. See the [Batch Events](../dev-java/notify-container-overview.html#batch-events) for details.|Single Thread per space|Server|
|ActiveFailureDetector| Responsible for the active election process. See the [Failure Detection](./troubleshooting-failure-detection.html) for details.|Single Thread|Server|
|ProvisionLeaseManager|Used by the Service Grid|Single Thread|Server|
|Watchdog|See the [Communication Protocol](./tuning-communication-protocol.html) for details. |Single Thread per proxy|Client|
|Scheduled System Boot Thread|Used when the system starts|Single Thread| Server|
|SLAThresholdTaskPool#|Used by the Service Grid|Single Thread| Server|
|CapabilityChannel|Used by the Service Grid|Single Thread| Server|
|DynamicSmartStub-InvHandlerCache-SelfCleaningTable$Cleaner|Used by LRMI for background cleanup|Single Thread|Server|
|ClassLoaderCache-SelfCleaningTable$Cleaner|Used by LRMI to cleanup unused classes|Single Thread|Server|
|ProvisionFailurePool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|ProvisionPool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|ProvisionMonitorEventPool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|Reggie Comm Task-pool#|Used by the Service Grid. Monitors the running services| |Server - GSM|
|SDM Cache Task|Used by the Service Grid.| |Server - GSM|
|unicast request|Used by the Service Grid.| |Server - GSM|
|service expire|Used by the Service Grid.| |Server - GSM|
|Webster Runner|Used by the Service Grid.| |Server - GSM|
|Event Lease Manager|Used by the Service Grid.| |Server - GSM|
|Fault Detection Handler|Used by the Service Grid.| |Server - GSM|
