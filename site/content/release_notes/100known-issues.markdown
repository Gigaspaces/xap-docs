---
type: post
title:  Known Issues and Limitations
categories: RELEASE_NOTES
parent: xap100.html
weight: 400
---


Below is a list of known issues in GigaSpaces 10.0.X.



| Key | Summary | SalesForce ID | Since Version | Workaround | Platform/s |
|:-------|:--------|:----------------|:---------------|:------------------|:----------|
| <nobr>GS-12519</nobr> | SQLQuery with composite condition might return wrong result | 9936 | 10.2.0 | Instead of writing:  a in (x,y) AND  (b < z OR A in (w,v))  do write:  (b < z OR A in (w,v)) AND a in (x,y) | Java |
| GS-12495 | Slow Memory leak in lease manager - empty cells remains after expiration | 9876 | 9.7.0 | | All |
| GS-12486 | Transaction consolidation issues with distributed transaction with durable notification and mirror when transaction contains both objects that meets the notify template and some that don't | 8935 | 9.7.0 |  | All |
| GS-11589 | Not all threads are terminated when destroying UrlSapceContainer | | 10.0.0 | | All |
| GS-11622 | Web-ui shows wrong instance count with replic | | 10.0.0 | | All |
| GS-11626 | Failed to deploy data example using secured space | | 10.0.0 | | Java |
| GS-11629 | sla.xml has side effect on spring import | 8597 | 10.0.0 | | Java |
| GS-11632 | NPE using DefaultSpaceInstance.runGc() when discoverUnmanagedSpaces used | 8587 | 10.0.0 | | All |
| GS-11634 | Redundant apostrophes in the GS_JARS variable in setenv.bat | | 10.0.0 | | Java |
| GS-11635 | Gigaspaces's jars missing from the classpath in startGroovy and startGroovy.bat | | 10.0.0 | | Java |
| GS-11636 | problem deploying JPA pet clinic example petclinic-web | | 10.0.0 | | Java |
| GS-11646 | Running a query with nested projection from Web-ui fails | | 10.0.0 | | All |
| GS-11647 | "Create" Permission wasn't added in GS-ui | 8758 | 10.0.0 | | All |
| GS-11654 | .Net localCache freeze on a clear operation in rare condition | 8740 | 10.0.0 | | .NET |
| GS-11660 | Bug in Linq - Parameters are valid only on the right side of the expression | | 10.0.0 | | .NET |
| GS-11675 | Setting spaceDataSource should not move the space to LRU cache policy | | 10.0.0 | | All |
| GS-11679 | Admin statistics history size overridden with default value | | 10.0.0 | | Java |
| GS-11682 | Web-UI: After running and terminating gs-agent, gsa is displayed for about one minute under Hosts | | 10.0.0 | | .NET |
| GS-11695 | MongoDB SynchronizationEndpoint- NPE when storing a map that contains a null value for a non null key | | 10.0.0 | | Java |
| GS-11696 | Support custom initial Load query for mongo data source                           | |10.0.0 |  | Java |
| GS-11697 | MongoDB-Incorrect return value in DataSyncOperation.getDataAsDocument() for duble | | 10.0.0 | | All |
| GS-11698 | Cannot override java.security.policy system property | | 10.0.0 | | Java |
| GS-11701 | RESTData cannot handle Array | | 10.0.0 | | All |
| GS-11711 | NPE in ESM destroy in rare condition | | 10.0.0 | | ALL |
| GS-11712 | ESM is stuck in a processing loop although the applications have been uninstalled due to unexpected exception | | 10.0.0 | | All |
| GS-11719 | gigaspace.getTypeManager().getTypeDescriptor(type) returns the outdated type descriptor from the server | | 10.0.0 | | Java |
| GS-11720 | GS-UI: Hidden progressbar in relocation window | | 10.0.0 | | Java |
| GS-11727 | com.gigaspaces.management.space.SpaceQueryDetails JavaDoc missing | | 10.0.0 | | Java |
| GS-11728 | Syntax error message in DotNetException.java class | 8927 | 10.0.0 | | Java, .NET |
| GS-11744 | Decreasing the height of the EDG window hides content and does not show scroll-bar | | 10.0.0 | | All |
| GS-11767 | Opening the Types tab under Data Grids throws an exception that is not catched in the webui | | 10.0.0 | | All |
| GS-11768 | Warning message as a result of using deprecated setAutoRenew(boolean , LeaseListener , long , long, long) method | | 10.0.0 | | Java |
| GS-11773 | ClassCastException thrown during deployment |  | 10.0.0 | | Java |
| GS-11774 | Got SQLQueryException when using rownum < ? | 8992 | 10.0.0 | | .NET |
| GS-11775 | NPE - when trying to resolve certain split brain scenario | 9032 | 10.0.0 | | All |
| GS-11776 | Fix Shutdown API to close custom thread pool and client connection | 9052 | 10.0.0 | | All |
| GS-11777 | Watchdog should stop monitoring once the server returned a result | 9031 | 10.0.0 | | All |
| GS-11780 | Severe message in the log after killing GSA in some scenarios | 9056 | 10.0.0 | | All |
| GS-11793 | jboss should have the dependency in test scope only | 9089 | 10.0.0 | | Java |
| GS-11820 | Fix pom.xml & WIKI to work with 9.7 RELEASE instead of SNAPSHOT | 9094 | 10.0.0 | | Java |
| GS-11822 | NPE in replicating change in rare condition | 9097 | 10.0.0 | | All |
| GS-11824 | Fail to shutdown GSC due to recovery thread that hang on socket trying to load class | 9081 | 10.0.0 | | All |
| GS-11825 | Got never ended SpaceMetadataException in the logs when adding index to an unregistered type | 9044 | 10.0.0 | | java |
| GS-11827 | Wrong number of instances displayed when PU with partitioned-sync2backup 2,0 and sla |  | 10.0.0 | | Java |
| GS-11829 | HTTP session bug |  | 10.0.0 | | Java |
| GS-11831 | D3 graph should properly update existing edge status |  | 10.0.0 | | Java |
| GS-11832 | NPE in getCachedBuffer in rare condition | 9128 | 10.0.0 | | All |
| GS-11833 | Missing partition after reboot of node due to ESM negative capacity exception | 9017 | 10.0.0 | | All |
| GS-11834 | GSC that is supposed to be terminated due to rebalancing does not terminate | 9074 | 10.0.0 | | All |
| GS-11836 | Extra backup and zombi instance after split brain in certain conditions | 9119 | 10.0.0 | | Java |
| GS-11839 | XAP.NET space space runtime statistics API is missing |  | 10.0.0 | | .NET |
| GS-11843 | _progressTimeout of recovery should be increased in blobstore recovery and add time to wait for lease reaper cycle to complete |  | 10.0.0 | | Java |
| GS-11844 | Recreation of FDFManager on the same jvm occurs once recovery fails - blobstore |  | 10.0.0 | | Java |
| GS-11845 | Deploying the Data example with the provided sla does not work properly |  | 10.0.0 | | Java |
| GS-11936 | Backwards compatibility issue: NPE is thrown when the client version is less then 10.0 and the server version is 10.0 | 9182 | 10.0.0 | | All |
| GS-10510 | Consistency issues with multisource replication when trinsiant entries are involved (distributed transaction consolidation) | | 9.7.0 | | All | 
