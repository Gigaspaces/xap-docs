---
type: post
title:  Resolved Issues
categories: RELEASE_NOTES
parent: xap102.html
weight: 300
---


Below is a list of issues that have been fixed in GigaSpaces 10.2.X.




| Key | Summary | Fix Version/s | Sales Force ID | Platform/s |
|:---------|:--------|:----------------|:---------------|:------------------|
| <nobr>GS-12550</nobr> | Remote service might rout to wrong partition in IntegratedProcessingUnitContainer (from 10.2) | 10.2.1, 11.0 | 9971 | Java |
| GS-12552 | ClassCastExeption is thrown when the LeaseManager is trying to clean an entry annotated with blobstoreEnabled = false | 10.2.1, 11.0 | 10004 | java |
| GS-12422 | After GSC restart an instance was missing, until managing GSM was killed | 10.2.1, 11.0 | 9704 | All |
| GS-12486 | Distributed transaction timeout when using durable notifications or WAN | 10.2.1, 11.0 | 8935 | All |
| GS-12523 | Blobstore persistent - wrong value is injected | 10.2.1, 11.0 |  | All |
| GS-12519 | SQLQuery with composite condition might return wrong result | 10.2.1, 11.0 | 9936 | Java |
| GS-12476 | llegalArgumentException after connection between mirror and mongodb is re-established | 10.2.1, 11.0 | 9832 | Java |
| GS-12495 | Slow Memory leak in lease manager - empty cells remains after expiration | 10.2.1, 11.0 | 9876 | All |
| GS-12507 | Can't start grid due to:metric named os_network_rx-bytes already exists (Sigar reporting same nw card twice) | 10.2.1, 11.0 | 9888, 9766 | All |
| GS-12465 | Remote service execution fails sporadically on Failure to find definition within the application context on rare condition | 10.2 | 9826 | All |
| GS-12492 | Blob store fill the internal cache when initial load from mirror | 10.2 |  | Java |
| GS-11632 | NPE using DefaultSpaceInstance.runGc() when discoverUnmanagedSpaces used | 10.2 | 8587 | All |
| GS-12446 | Possible mem-leak in blobstore bulk ops | 10.2 |  | Java |
| GS-12466 | Local Views view table in web-ui displayed as empty | 10.2 | | Java |
| GS-12453 | Backward issue using projection in old client against 10.1 Server cause InvalidClassException | 10.2 | 9789 | All |
| GS-12440 | Add protection against exceptions/ throwables in FIFO processing | 10.2 | 9720 | All |
| GS-12397 | On login mode with non-sticky session, session is not logged-in at all the web servers | 10.2 |  | All |
| GS-12448 | DefaultOperatingSystem.getStatistics() failed with NegativeArraysSizeException | 10.2 | 9777 | All |
| GS-12327 | In 'Spaces' view, 'types' shows 'Loading data types...' when no space is selected | 10.2 |  | All |
| GS-12445 | 10.1 Web-UI in Spaces tab - when clicking on Reconnect icon new line is added | 10.2 | 9771 | All |
| GS-12396 | Distinguish between processing unit instance failures and manual decrements| 10.2 | 8707 | All |
| GS-12364 | Failed to launch Web-UI due to NoClassDefFoundError: org/apache/commons/codec/binary/Base64 | 10.2 |  | Java |
| GS-12419 | Expose blobstore add,get,replace,remove metrics | 10.2 |  | All |
| GS-12378 | Space partition split-brain alert in Admin API and web-ui | 10.2 |  | Java |
| GS-12383 | Deploying an Elastic PU via CLI results in authentication error | 10.2 | 9708 | All |
| GS-12242 | Multithreaded execution of tasks on a secured space occasionally fails on "No credentials were provided" | 10.2 | 9426 | Java |
| GS-11937 | Force kill service by GSA in similar manner to to kill -9  | 10.2 | 9158,9338 | All |
| GS-12357 | Deployment of a PU containing an abstract bean fails | 10.2 | 9680 | All |
| GS-12403 | Generate on the fly self signed certificate does not work with IBM JDK | 10.2 | 9712 | All |
| GS-12353 | Service details of REST PU shows invalid URL on Windows | 10.2 |  | Java |
| GS-12315 | Transaction consolidation of transient only transactions causes timeout | 10.2 | 9640,9417 | All |
| GS-12324 | First row in Spaces view should be selected by default | 10.2 |  | Java |
| GS-12376 | Handle default selection in Spaces view | 10.2 |  | Java |
| GS-12358 | PU metrics are not reported in XAP.NET | 10.2 |  | .NET |
| GS-12372 | In some grids meter bars displayed as cutted  | 10.2 |  | Java |
| GS-12366 | Events grid in new Processing Unit view is always empty | 10.2 |  | Java |
| GS-12361 | When running with more than one gsm, as result Monitoring view is unavailable | 10.2 |  | All |
| GS-12356 | RLIKE/LIKE fails (DIV0) when executed against a SpaceIndex property belonging to an abstract parent class  | 10.2 | 9654 | All |
| GS-12360 | spring.schemas files were not updated for version 10.1.0 | 10.2 |  | Java |