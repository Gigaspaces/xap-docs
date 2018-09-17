---
type: post123
title:  Monitoring
categories: XAP123PROD
parent: none
weight: 1100
---


# Monitoring

Implementing reliable monitoring functionality to track InsightEdge, XAP, and the environment where they are deployed is an important task that should be completed **before** moving into production. Correctly monitoring the InsightEdge/XAP environment enables creating a proactive action plan (manually or automatically) that can be triggered before any system failure . This helps to avoid bad user experience, data loss, and abnormal sudden system shutdown. For example, effective monitoring can identify increased use of system resources so you can allocate additional CPU or memory, or  malfunctioning components that must be corrected before they impact system health or correct system behavior.

Monitoring functionality should track the following:

- Service Grid statistics 
- Data Grid statistics
- Event Containers (Polling/Notify/Archiver) statistics
- Remote Service statistics
- Remote communication statistics
- Client Local cache/view statistics
- Web application statistics
- Mirror Service statistics
- Replication statistics
- Admin Alerts (CPU Utilization, Garbage Collection, Replication Channel Disconnection, etc.)
- Log files

For all service grid components (`GSA`, `LUS`, `GSM`, `GSC`) you should monitor thread count, CPU utilization, file descriptor count, memory utilization, and network utilization.

You can publish this information in real time to any enterprise monitoring system (CA Wily introscope, HP Operations Manager, IBM Tivoli, etc.) to be correlated with your existing application monitoring. You can also generate daily/hourly reports of this information to be shared with relevant entities within your organization, which can be processed offline to estimate required system capacity and size upon system growth.

{{% note "Note"%}}
Consult the [GigaSpaces support](mailto:support@gigaspaces.com?subject=XAP Monitoring tools) team for information about monitoring tools that are provided as part of GigaSpaces' professional services. These can be adapted to fit your exact requirements. 
{{%/note%}}

