---
type: post
title:  Known Issues and Limitations
categories: RELEASE_NOTES
parent: xap97.html
weight: 400
---


Below is a list of known issues in GigaSpaces 9.7.X.



| Key | Summary | SalesForce ID | Since version | Workaround | Platform/s
|:----|:--------|:----------------|:---------------|:------------------|:----------|
| <nobr>GS-12519</nobr> | SQLQuery with composite condition might return wrong result | 9936 | 10.2.0 | Instead of writing: a in (x,y) AND (b < z OR A in (w,v)) do write: (b < z OR A in (w,v)) AND a in (x,y) | Java |
| GS-12495 | Slow Memory leak in lease manager - empty cells remains after expiration | 9876 | 9.7.0 | | All |
| GS-12486 | Transaction consolidation issues with distributed transaction with durable notification and mirror when transaction contains both objects that meets the notify template and some that don't | 8935 | 9.7.0 |  | All |
| GS-11222 | Unable to run GigaSpaces component when installed under folder with '(',')' chars | 8169 | 9.7.0 | | All |
| GS-11249 | Error while deploying a processing unit which contains '#' in its name/directory | 8209 | 9.7.0 | | Java, .Net |
| GS-11295 | Web-ui show strange log msg when trying to run custom command on gsc which contains stateful pu | | 9.7.0 | | All |
| GS-11320 | Progress is missing when performing undeploy application/service via Web-ui | | 9.7.0 | | All |
| GS-11346 | Web-ui doesn't show the correct running/planned instances of a deployed application | | 9.7.0 | | All |
| GS-11361 | Deployment of an application with dependencies on elastic pu could be slower than sequential pu deployment | | 9.7.0 | | Java |
| GS-11381 | ESM caught in a re-balancing loop after addition of new machine | 8242 | 9.7.0 | | All |
| GS-11408 | Web-ui - Mirror stats should track change operations | | 9.7.0 | | All |
| GS-11418 | Gs-ui - Remote Space View of selected remote space shows only portion of the data and not all | | 9.7.0 | | All |
| GS-11434 | WAN Gateway -  pass through site won't show on Web UI | | 9.7.0 | | All |
| GS-11443 | Discovery group selection doesn't filter out WAN Gateway Lookup Groups | | 9.7.0 | | All |
| GS-11446 | Client without credentials can invoke (deprecated) operations admin.restart() admin.stop() | 8407 | 9.7.0 | | All |
| GS-11447 | Client without credentials is able to get RuntimeDetails of secured space | 8407 | 9.7.0 | | All |
| GS-11449 | ESM log is cluttered when admin API cannot get statistics | | 9.7.0 | | Java |
| GS-11450 | ESM log is cluttered by failed attempt to kill containers | | 9.7.0 | | Java |
| GS-11463 | Need to restart the ESM if it cannot detect any lookup service during initialization | | 9.7.0 | | Java |
| GS-11484 | Space name can not contain the word "localview" | | 9.7.0 | | All |
| GS-11506 | WAN Gateway should have cascading replication | | 9.7.0 | | All |
| GS-11511 | Select count(*) query from Web-ui which should return 0 throws NullPointerException | 8535 | 9.7.0 | | All |
| GS-11518 | No backup for pu deployment, if one of the two GSM was in recovery process while deployment | 8513 | 9.7.0 | | All |
| GS-11519 | Web-ui logs - Feature Requests | | 9.7.0 | | All |
| GS-11520 | Web-ui logs - buttons are not working | | 9.7.0 | | All |
| GS-11534 | Heap Dumps not supported on IBM jdk | | 9.7.0 | | All |
| GS-11537 | @EventDriven @Polling @Notify should support inheritance | 8560 | 9.7.0 | | Java, .Net |
| GS-11542 | .Net ScalingAgent example - inverted arrows | | 9.7.0 | | .Net |
| GS-11546 | ESM stateless increment instance - timeout is too big | | 9.7.0 | | Java |
| GS-11547 | NPE in admin when primary GSM restarts | | 9.7.0 | | Java |
| GS-11550 | Unclear UpdateOperationTimeoutException | | 9.7.0 | | All |
| GS-11555 | Deploying with max-instances-per-zone 0 result in 1 instance | 8574 | 9.7.0 | | All |
| GS-11557 | .Net add validation that both client and server classes are including the same aliases | 8546 | 9.7.0 | | .Net |
| GS-11561 | Reading a POJO which contains a document using mongo EDS fails | | 9.7.0 | | Java |
| GS-11592 | Web-ui ssh terminal blocked by java 1.7u51 | | 9.7.0 | | Java |
| GS-11619 | Fix installation scripts in windows | | 9.7.0 | | All |
| GS-11621 | Bad linux scripts for the benchmark cli tool | | 9.7.0 | | Java |
| GS-11640 | Installmavenrep.bat fails because POMGenerator doesn't generate mongo-datasource pom | | 9.7.0 | | Java |
| GS-11664 | AccessDeniedException in write only operation - when using role including WRITE permission | 8757 | 9.7.0 | | All |
| GS-11679 | Admin statistics history size overridden with default value |  | 9.7.1 | | Java |
| GS-11703 | XAP.NET yields CLSCompliant warnings | 8903 | 9.7.1 | | .NET |
| GS-11736 | Can't see space in gs-ui when running gsInstance.bat when using jdk 7u55 |  | 9.7.1 | | Java |
| GS-11776 | Fix Shutdown API to close custom thread pool and client connection | 9052 | 10.0.0 | | All |
| GS-11836 | Extra backup and zombi instance after split brain in certain conditions | 9119 | 10.0.0 | | Java |
| GS-10510 | Consistency issues with multisource replication when trinsiant entries are involved (distributed transaction consolidation) | | 9.7.0 | | All | 
