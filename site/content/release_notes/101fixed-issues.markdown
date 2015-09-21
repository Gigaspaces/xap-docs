---
type: post
title:  Resolved Issues
categories: RELEASE_NOTES
parent: xap101.html
weight: 300
---


Below is a list of issues that have been fixed in GigaSpaces 10.1.X.




| Key | Summary | Fix Version/s | Sales Force ID | Platform/s |
|:---------|:--------|:----------------|:---------------|:------------------|
|  <nobr>GS-12435</nobr> | Web PU spring context deployment failure after upgrade from 10.01 to 10.1 | 10.1.1 | 9750 | All |
| GS-12376 | Handle default selection in Spaces view | 10.1.1, 10.2.0 | | Java |
| GS-12372 | In some grids meter bars displayed as cutted | 10.1.1, 10.2.0 | | Java |
| GS-12366 | Events grid in new Processing Unit view is always empty | 10.1.1, 10.2.0 | | Java |
| GS-12361 | When running with more than one gsm, as result Monitoring view is unavailable | 10.1.1, 10.2.0 |  | All |
| GS-12360 | spring.schemas files were not updated for version 10.1.0 | 10.1.1, 10.2.0 |  | Java |
| GS-12358 | PU metrics are not reported in XAP.NET | 10.1.1, 10.2.0 |  | .NET |
| GS-12357 | Deployment of a PU containing an abstract bean fails | 10.1.1, 10.2.0 |  9680 | All |
| GS-12356 | RLIKE/LIKE fails (DIV0) when executed against a SpaceIndex property belonging to an abstract parent class | 10.1.1, 10.2.0 | 9654 | All |
| GS-12353 | Service details of REST PU shows invalid URL on Windows | 10.1.1, 10.2.0 |  | Java |
| GS-12335 | Web-ui might fails on OOM due to statistics for gateway replication that keep large exception traces (in version previous to 10.1 ) | 10.1.1 | 9656 | Java |
| GS-12327 | In 'Spaces' view, 'types' shows 'Loading data types...' when no space is selected | 10.1.1, 10.2.0 | | All |
| GS-12324 | First row in Spaces view should be selected by default | 10.1.1, 10.2.0 |  | Java |
| GS-12315 | Transaction consolidation of transient only transactions causes timeout | 10.1.1, 10.2.0 | 9640,9417 | All |
| GS-12320 | NullPointerException in webui server when pressing on "Types" tab without selecting any instance | 10.1.0 |  | All |
| GS-12293 | Alerts for CPU, heap, physical memory are sent before measurement-period is over | 10.1.0 |  | Java |
| GS-12291 | Inconsistent pom.xml files when using installmavenrep.sh vs using maven dependencies | 10.1.0 |    9596 | Java |
| GS-12282 | Undeploy takes a long time when there are a lot of notify containers registered | 10.1.0 |  | All |
| GS-12279 | Second and all next attempts to navigate from specific space instance in Hosts view to Data Grid failed | 10.1.0 |  | Java |
| GS-12279 | Second and all next attempts to navigate from specific space instance in Hosts view to Data Grid failed | 10.1.0 |  | Java |
| GS-12266 | After either host or gs node in Hosts view expanded meter bars displayed as cutted | 10.1.0 |  | Java |
| GS-11965 | Allow Un-even deployment with ESM | 10.1.0 |  | All |
| GS-12264 | Deployment status on simultaneous relocation of two instances | 10.1.0 | 8707 | Java |
| GS-12249 | Simultaneous restart of two instances on the same GSC | 10.1.0 | 8707 | All |
| GS-12241 | Destroy one SimpleNotifyEventListenerContainer cause all SimpleNotifyEventListenerContainers of same client to stop | 10.1.0 | 9557 | All |
| GS-12201 | Undeploy hang in rare condition waiting to all contexts to be inactive | 10.1.0 | 9498 | All |
| GS-12175 | Unable to retrieve beans based on os-core:embedded-space | 10.1.0 | 9512 | Java |
| GS-12157 | GigasSpace.getClustered() doesn't return cluster proxy when proxy is injected in task | 10.1.0 | 9457 | Java |
| GS-12141 | Transaction consolidation fails when one of the participants has only transient objects | 10.1.0 | 9432 | All |
| GS-12139 | Queries with "order by" and id autoGenerate=true don't work | 10.1.0 |  | All |
| GS-12137 | Out of memory during lrmi client handshake | 10.1.0 |  | Java |
| GS-12104 | After split brain resolution a .NET backup Space may remain inactive | 10.1.0  | 9368 | Java |
| GS-12101 | XA transaction with suspend resume fails | 10.1.0 | 9373 | Java |
| GS-12085 | Instance missing after GSM failover due to "clustered space member must be defined in at least in one partition"  | 10.1.0 | 9018 | Java |
| GS-12077 | LUS OOM in rare condition when plenty of registration and unregistration of services due to disconnections | 10.1.0 | 9316 | All |
| GS-12074 | Deploy Application with dependencies hang in case of more then 2 instances per service | 10.1.0 | 9279 | All |
| GS-12055 | Space proxy lookup after a failover sometimes ignores the timeout | 10.1.0 |  | All |
| GS-12054 | Pending provision of processing unit instance after failover of machine with GSM and ESM due to GSM not in sync  | 10.1.0 | 9274 | Java |
| GS-12047 | Limit the query results by a system property to avoid crashing the space with large queries. | 10.1.0 | 9198 | All |
| GS-12045 | .Net - IndexOutOfBoundsException when iterating over IOutgoingReplication | 10.1.0 |  | .Net |
| GS-12043 | Impossible to override NIC_ADDR at environment without dns like openstack | 10.1.0 |  | Java |
| GS-12041 | Querying indexed field with multiple OR'd predicates breaks FIFO ordering | 10.1.0 |    9291 | Java |
| GS-12031 | Running aggregated functions from web-ui returns empty row as result | 10.1.0 |  | Java |
| GS-11987 | WAN Gateway throws NumberFormatException when using LRMI port ranges | 10.1.0 | 9250 | Java |
| GS-11979 | IllegalArgumentException is thrown when using SQLQuery and one of the properties is compressed or binary | 10.1.0 | 9220,<br>9275,<br>9325,<br>9376,9538 | Java |
| GS-11978 | Memory Leak in FIFO Groups in certain condition | 10.1.0 | 9218 | All |
| GS-11975 | JPA transaction commit timeout configure according LockTimeout (JPA property) | 10.1.0 |  | Java |
| GS-11973 | When user is created from API and use RegexFilter, AccessDeniedException is thrown when registering a type | 10.1.0 |  8982 | Java |
| GS-11972 | Client failed to connect to space when the disconnected machine appears first in jini url | 10.1.0 | 9200, 9529 | Java |
| GS-11965 | Allow Un-even deployment with ESM | 10.1.0 |  | All |
| GS-11962 | .NET 10.0 version- when BasicContainer element is missing from pu.config we got System.NullReferenceException | 10.1.0 | 9201 | .Net |
| GS-11856 | XAP 10 blobstore RPM hangs when installing | 10.1.0 |  | All |
| GS-11855 | XAP 10 blobstore RPM gs-agent-blobstore.sh GSC_JAVA_OPTIONS | 10.1.0 |  | All |
| GS-11850 | Rare Deadlock on SynchronizeReplicaDataProducer lock when ReplicationNode thread and LeaseManager$Reaper thread call close method- during failover using Blobstore | 10.1.0 |  | All |
| GS-11847 | UI - ElectionInProcessException thrown when deploying processing unit with a backup | 10.1.0 |  | Java |
| GS-11836 | Extra backup-Space after network disconnection | 10.1.0 |  9119,<br>9185,<br>9186,<br>9184 | Java |
| GS-11834 | GSC that is supposed to be terminated due to rebalancing does not terminate | 10.1.0 | 9074 | All |
| GS-11833 | Missing partition after reboot of node due to ESM negative capacity exception | 10.1.0 | 9017 | All |
| GS-11825 | SpaceMetadataException logged In loop when adding index to a type that doesn't exists in the mirror | 10.1.0 | 9044 | Java |
| GS-11824 | Fail to shutdown GSC due to recovery thread that hang on socket trying to load class | 10.1.0 | 9081 | All |
| GS-11728 | Syntax error message in DotNetException.java class | 10.1.0 | 8927 | Java, .Net |
| GS-11381 | ESM caught in a re-balancing loop after addition of new machine in rare condition(cpu reported as 0) | 10.1.0 | | All |
| GS-11675 | Change default Setting of cache policy in case of spaceDataSource defined from LRU to ALL IN CACHE | 10.1.0 | 8242 | All |
| GS-11665 | Ability to add GS services JAVA_OPTIONS within setenv.sh | 10.1.0 | 8768 | Java |
| GS-11647 | "Create" Space operation permission is missing in GS-UI | 10.1.0 | 8758 | All |
| GS-11381 | ESM caught in a re-balancing loop after addition of new machine in rare condition(cpu reported as 0) | 10.1.0 | 8242 | All |
| GS-10903 | Repeatable SQLQuery with a slight difference per each SQL (changing the value is enough) for long time causes memory leak in client | 10.1.0 | 7791 | All |
| GS-10131 | In rare scenarios blocked locks for SpaceProxyTypeManager- readById blocked waiting to type descriptor | 10.1.0 | 7272 | All |
| GS-8165 | Read with SQL query on transient object in LRU topology is unnecessary delegated to the EDS | 10.1.0 | 6176 | All |
