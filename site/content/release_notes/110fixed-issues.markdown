---
type: post
title:  Resolved Issues
categories: RELEASE_NOTES
parent: xap110.html
weight: 300
---


Below is a list of issues that have been fixed in GigaSpaces 11.0.X.




| Key | Summary | Fix Version/s | Sales Force ID | Platform/s |
|:---------|:--------|:----------------|:---------------|:------------------|
| <nobr>GS-12779</nobr> | Not expose context parameters values in the log for per-defined keys | 11.0.0 | | All |
| GS-12155 | Objects will stay locked under transaction if Mahalo is down between the prepare and commit | 11.0.0 | | Java |
| GS-12778 | org.openspaces.remoting.EventDrivenRemoteFuture is not thread-safe | 11.0.0 | | All |
| GS-12761 | Deploying an invalid PU might corrupt the GSC | 11.0.0 | | All |
| GS-12691 | Failure handling generic collection fields with Enum as key in .NET | 11.0.0 |	10208 | .Net |
|	GS-12687 | Repetitive quiesce + unquiesce can cause quiesce/unquiesce request to get stuck | 11.0.0 |	 | Java |
|	GS-12670 | Upgrade commons-collections-3.2.1 to version 3.2.2 | 11.0.0 | 10160  | All |
|	GS-12665 | Out of memory in server while reading the first message when client not using LRMI protocol | 11.0.0 | 10074, 10092 | All |
|	GS-12660 | ProcessingUnit status is reported as SCHEDULED instead of COMPROMISED when instance of second partition is pending to be provisioned | 11.0.0 | | All |
|	GS-12656 | XAP influxDB reporter does not report timestamp correctly when using UDP protocol  | 11.0.0 | | All |
|	GS-12607 | Update object lease will not update the lease manager,if update waited on a lock  | 11.0.0 | 9949 | Java |
|	GS-12570 | XAP .NET HttpSessionProvider sporadically causes 100% CPU and memory leak | 11.0.0 | 10029 | .Net |
|	GS-12569 | .Net - Fail to add indexes on document properties | 11.0.0 | | .Net |
|	GS-12568 | Duplicates Xmx/Xms when running gs-agent script | 11.0.0 | 9998 | Java |
|	GS-12537 | -XX:MaxPermSize=256m option in setenv.sh is not relevant for java 8 (HOTSPOT) | 11.0.0 | | All |
|	GS-12532 | Memory leak when using atomicos | 11.0.0 | 9917 | All |
|	GS-12507 | Can't start grid due to:metric named os_network_rx-bytes already exists (Sigar reporting same nw card twice) | 11.0.0, 10.2.1  | 9888, 9766 | All |
|	GS-12552 | ClassCastExeption is thrown when the LeaseManager is trying to clean an entry annotated with blobstoreEnabled = false | 11.0.0, 10.2.1  | 10004 | Java |
|	GS-12550 | Remote service might rout to wrong partition in IntegratedProcessingUnitContainer (from 10.2) | 11.0.0, 10.2.1  | 9971 | Java |
|	GS-12588 | Resource leak in Conversation class when remote host name could not be resolved | 11.0.0, 10.2.1 | 10060 | All|
|	GS-12519 | SQLQuery with composite condition might return wrong result | 11.0.0, 10.2.1 | 9936  | Java | 
|	GS-12495 | Slow Memory leak in lease manager - empty cells remains after expiration | 11.0.0, 10.2.1 | 9876  | All | 
|	GS-12486 | Distributed transaction timeout when using durable notifications or WAN | 11.0.0, 10.2.1 | 8935  | All | 
|	GS-12476 | llegalArgumentException after connection between mirror and mongodb is re-established  | 11.0.0, 10.2.1 | 9832   | Java | 
|	GS-12422 | After GSC restart an instance was missing, until managing GSM was killed  | 11.0.0, 10.2.1 | 9704  | All |
|	GS-12586 | Missing jar from "XAP as Windows Service" example | 11.0.0, 10.2.0 | 10051 | Java |
