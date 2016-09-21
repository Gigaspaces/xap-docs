---
type: post
title:  Resolved Issues
categories: RELEASE_NOTES
parent: xap120.html
weight: 300
---


Below is a list of issues that have been fixed in XAP 12.0.X.




| Key                   | Summary                            | Fix Version/s    | Platform/s        |
|:----------------------|:-----------------------------------|:----------------|:---------------| 
|<nobr>GS-12767<nobr>   |Fixed handling of BlobstoreException in blobstore sync-replication     | 12.0  | Java|
|<nobr>XAP-11836<nobr> |Cancel discovery of extra backup-Space after network disconnection after max retries| 12.0.1| Java|
|XAP-12028 |Redo-log keeps growing when connection between server and local-view client can’t be established from server| 12.0.1|  Java|
|XAP-12582 |bypass active failure detector until recovery completes or fails| 12.0.1| Java|
|XAP-12793 |Consumer threads are never released by SimplePollingEventListenerContainer| 12.0.1| Java|
|XAP-12814 |Maintain instance SLA after network partition (for mirror & stateless)| 12.0.1|  Java|
|XAP-12825 |Web-ui shows aggregated statistics of both primary and backup Spaces| 12.0.1| Java|
|XAP-12837 |Redo-log metrics stops after restarting the primary| 12.0.1| Java|
|XAP-12853 |ProtectiveModeException is thrown when com.gs.protectiveMode.queryWithoutIndex property is set to true when querying an empty space on an indexed field| 12.0.1|  Java|
|XAP-12954 |Unreachable lookup locator using unicast discovery causes a 90s delay before next lookup retry| 12.0.1|  Java|
|XAP-12967 |Don't elect backup as primary if recovery failed for direct persistency| 12.0.1| Java|
|XAP-12970 |Shutdown might be pending endlessly for replication to finish| 12.0.1|  Java|
|<nobr>XAP-12972<nobr> |gs-ui takes lookup groups and locators setting from deprecated adminui.config| 12.0.1|  Java|