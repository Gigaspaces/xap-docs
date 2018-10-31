---
type: post140
title:  Changelog
categories: XAP140RN
parent: none
weight: 300
---

This section lists the changes that were made in InsightEdge Platform 12.3.x.

# 12.3.1

This service pack was released in July 2018.

## Improvements

- {{% jira id="GS-13518" %}}: Sort results for REST operations related to Spaces and Processing Units.
- {{% jira id="GS-13511" %}}: CLI Auto Complete.
- {{% jira id="GS-13510" %}}: Simplified the CLI command "pu decrement" to receive only one parameter: pui_id.
- {{% jira id="GS-13513" %}}: Enhance CLI output to display "N/A" instead of "-1" where relevant.
- {{% jira id="GS-13515" %}}: Upgrade Apache Curator to 4.0.1.
- {{% jira id="GS-13485" %}}: Improve memory allocation by pre-allocating ArrayList instances in the SQL execution engine.
- {{% jira id="GS-13519" %}}: Upgrade RocksDB to 5.11.3.
- {{% jira id="GS-13524" %}}: Display blobstore operation statistics in the Web Management Console.
- {{% jira id="GS-13532" %}}: Enable configuring the MINIMAL_BUFFER_DIFF_TO_ALLOCATE property of the  OffHeapMemoryPool.
- {{% jira id="GS-13552" %}}: Share a single instance of Curator Framework client to support large-scale deployments.
- {{% jira id="GS-13551" %}}: Handle Apache Curator leader election events in a single threaded queue.
- {{% jira id="GS-13557" %}}: Configure Spark to include InsightEdge dependencies on startup.
- {{% jira id="GS-13564" %}}: Upgrade Spring to 4.3.17.
- {{% jira id="GS-13562" %}}: Upgrade Apache Zookeeper to 3.4.12 and Netty to 3.10.6.Final.
- {{% jira id="GS-13569" %}}: Log fills up with "unmarshalling failure" messages when a cluster partition member is unreachable.
- {{% jira id="GS-13572" %}}: XAP and InsightEdge demo command should fail if XAP_MANAGER_SERVERS is configured.
- {{% jira id="GS-13571" %}}: Enable building Python notebook in build process for InsightEdge Zeppelin tutorial.
- {{% jira id="GS-13581" %}}: Upgrade Apache Spark to version 2.3.1.
- {{% jira id="GS-13428" %}}: Enhance REST API and CLI to support primary zones.
- {{% jira id="GS-13542" %}}: Display redo log information in the Web Management Console.
- {{% jira id="GS-13579" %}}: Remove dashes from new CLI output to simplify output parsing.
- {{% jira id="GS-13582" %}}: Support public and private IP addresses for Docker deployment.
- {{% jira id="GS-13563" %}}: Decouple JPA integration from OpenSpaces module.


## Resolved Issues

- {{% jira id="GS-13520" %}}: Could not display the off-heap columns in the Web Management Console (in systems with MemoryXtend).
- {{% jira id="GS-13505" %}}: Running the demo command in the CLI using off-heap generates a NoClassDefFoundError.
- {{% jira id="GS-13502" %}}: Provisioning a Space instance fails on "Identified another participant with the same name for Space".
- {{% jira id="GS-13517" %}}: Index intersection optimization did not work as expected.
- {{% jira id="GS-13512" %}}: Insightedge CLI command fails to start Spark master and Spark worker when XAP_MANAGER_SERVERS is not configured.
- {{% jira id="GS-13509" %}}: Unhealthy Space remains in Stopped state indefinitely after a network failure.
- {{% jira id="GS-13508" %}}: Apache ZooKeeper sometimes fails to create a data directory on startup.
- {{% jira id="GS-13521" %}}: Space instance recovery fails on "Failed while getting participants from zookeeper server".
- {{% jira id="GS-13486" %}}: IllegalArgumentException: A metric named process_cpu_time-total already exists.
- {{% jira id="GS-13521" %}}: Space instance recovery fails on "Failed while getting participants from zookeeper server".
- {{% jira id="GS-13486" %}}: IllegalArgumentException: A metric named process_cpu_time-total already exists.
- {{% jira id="GS-13451" %}}: The SQL query "SELECT COUNT (...) GROUP BY" doesn't work on  nested properties in the Web Management Console.
- {{% jira id="GS-13537" %}}: The SQL query "SELECT COUNT (...) GROUP BY" doesn't work  in the Web Management Console when it includes enums.
- {{% jira id="GS-13469" %}}: Remoting service routes incorrectly when the routing key is long, and if the value is higher than the max integer.
- {{% jira id="GS-13544" %}}: The XAP Manager mishandles pending requests when its leadership is relinquished.
- {{% jira id="GS-13548" %}}: Storing the blobStoreVersion not implemented in MemoryXtend off-heap driver.
- {{% jira id="GS-13539" %}}: The Java 8 LocalDateTime is not shown in the Web Management Console object inspector.
- {{% jira id="GS-13496" %}}: The Replication Channels metrics for partition clusters are not being reported.
- {{% jira id="GS-13547" %}}: ServerTypeDesc tree was not cloned correctly.
- {{% jira id="GS-13559" %}}: Display blobstore operation statistics in the GigaSpaces Management Center.
- {{% jira id="GS-13531" %}}: SpaceTypeDescriptorBuilder fails when explicitly indexing the routing key using deprecated values.
- {{% jira id="GS-13541" %}}: Manager class path does not include cert file located in 'com.gigaspaces.lib.opt.security' directory.
- {{% jira id="GS-13565" %}}: Space instance becomes backup although it is offered leadership by Apache Zookeeper.
- {{% jira id="GS-13478" %}}: Change operation replicated to mirror can cause NPE in certain failover scenarios.
- {{% jira id="GS-13546" %}}: The `xap space run` CLI command fails when XAP_MANAGER_SERVERS is defined.
- {{% jira id="GS-13543" %}}: Queries using the DISTINCT keyword do not work in the Web Management Console.
- {{% jira id="GS-13538" %}}: Web Management Console displayed different values in the Monitoring tab and the Space statistics view for the number of data objects written to a Space.
- {{% jira id="GS-13577" %}}: The 'pu undeploy' CLI command returns an error when the resource JAR file is not found in the resources directory.
- {{% jira id="GS-13584" %}}: The 'xap pu run' CLI command might leave orphan sub-processes in some scenarios.

# 12.3.0

The main version was released in March 2018.


# Features and Enhancements

- {{% jira id="GS-13438" %}}: Unified Command Line Interface.
- {{% jira id="GS-13488" %}}: New MemoryXtend storage driver for off-heap memory.
- {{% jira id="GS-13500" %}}: When killing an external process, avoid SIGKILL on systems which do not support it (Windows).
- {{% jira id="GS-13474" %}}: Docker Images for InsightEdge and XAP.
- {{% jira id="GS-13490" %}}: Revise Space index types to provide option for reduced memory footprint.
- {{% jira id="GS-13489" %}}: Reduce @SpaceId(autogenerate=false) memory footprint.
- {{% jira id="GS-13456" %}}: Upgrade Apache Zookeeper to 3.4.10.
- {{% jira id="GS-13400" %}}: Replication redolog compaction for mirror target.
- {{% jira id="GS-13477" %}}: Created v2 of XAP Manager REST API.
- {{% jira id="GS-13491" %}}: Renamed /deployments to /pus in v2 of XAP Manager REST API.
- {{% jira id="GS-13480" %}}: Limit Lookup Locator Discovery interval to 1 minute instead of 1 hour.
- {{% jira id="GS-13482" %}}: Change default of org.jini.rio.monitor.pendingRequestDelay to 1 minute instead of 10 minutes.
- {{% jira id="GS-13470" %}}: Upgrade packaged Jetty server to 9.2.24.
- {{% jira id="GS-13458" %}}: Upgrade Spring Framework to 4.3.13.
- {{% jira id="GS-13457" %}}: Upgrade Spring Security to 4.2.3.
- {{% jira id="GS-13464" %}}: Enhance gs-agent to Support running Web Management Console (--webui).
- {{% jira id="GS-13473" %}}: Enhance CachePolicy configuration to use fluent syntax.
- {{% jira id="GS-13463" %}}: Remove obsolete security algorithm to align with current standards.
- {{% jira id="GS-13102" %}}: Added ability to monitor connection status for local views.
- {{% jira id="GS-12800" %}}: Improved AbstractEventListenerContainer monitoring via public getter methods.
- {{% jira id="GS-13224" %}}: Improve log message to include table name when initial load fails due to wrong column name.
- {{% jira id="GS-13452" %}}: Improve responsiveness of remote statistics gathering in Admin API.
- {{% jira id="GS-13136" %}}: Make instance variables in DefaultHibernateSpaceDataSourceConfigurer protected.
- {{% jira id="GS-13411" %}}: User-defined cache criteria for hot data in MemoryXtend.
- {{% jira id="GS-13436" %}}: Added more GSM state information to application dump.
- {{% jira id="GS-13442" %}}: Reduce the footprint of the Lookup Service template cache.
- {{% jira id="GS-13302" %}}: When the query result set includes only indexes, fetch results from off-heap memory instead of disk.
- {{% jira id="GS-12365" %}}: Asynchronous execution should always return immediately.
- {{% jira id="GS-13417" %}}: Add metrics to Lookup Service to facilitate troubleshooting of performance issues.
- {{% jira id="GS-13389" %}}: Lookup Service (LUS) enhancements to improve stability in environments with large clusters.
- {{% jira id="GS-13433" %}}: Enhance LRMI network filter buffer allocation strategy by adding support for large objects (>10MB).
- {{% jira id="GS-13418" %}}: Skip login page if Web UI is in non-secured mode.

# Resolved Issues

- {{% jira id="GS-12946" %}}: Loop of restarts of PU instance in same container cause memory leak.
- {{% jira id="GS-13484" %}}: Missing partition instance not provisioned after network disconnected and reconnected.
- {{% jira id="GS-13483" %}}: Relocation listener is not called during failure to destroy instance.
- {{% jira id="GS-13454" %}}: Elastic Processing Unit failed to fully deploy with service-limit=1 even when there were empty GSCs running.
- {{% jira id="GS-13300" %}}: Eager Scale Strategy did not evenly redistribute partitions after a cluster node disconnected and reconnected.
- {{% jira id="GS-13465" %}}: Use XapNetworkInfo.getHost() instead of InetAddress.getLocalHost().
- {{% jira id="GS-13462" %}}: XAP Manager doesn't support com.gigaspaces.lib.opt.security system property for providing an alternative path for custom security JARs.
- {{% jira id="GS-13461" %}}: Add missing xap-admin JAR for default XAP.NET application.
- {{% jira id="GS-13453" %}}: Inefficient use of underlying data structure slows down iterator performance.
- {{% jira id="GS-13284" %}}: In rare scenarios, a transactional Polling container may drop events.
- {{% jira id="GS-13236" %}}: The Admin API blocks itself, becoming unresponsive and exploding memory usage.
- {{% jira id="GS-13440" %}}: Incomplete recovery of processing units after healing of GSM (if there are 3 or more).
- {{% jira id="GS-13281" %}}: Querying a type with enum on a re-deployed space returns incorrect results.
- {{% jira id="GS-13383" %}}: Insightedge shutdown command on Mac systems doesn't terminate the gs-agent.
- {{% jira id="GS-13416" %}}: Lookup service improvements resulted in loss of client notifications in rare scenarios.
- {{% jira id="GS-13371" %}}: REST calls to Spark failed on machine failover.
- {{% jira id="GS-13317" %}}: No proactive verification of user credentials when logging into secured web-ui.
- {{% jira id="GS-13427" %}}: The REST manager specification contained the wrong request status values.
- {{% jira id="GS-13430" %}}: Inconsistent Request ID type in Manager REST API (string vs. numeric).
- {{% jira id="GS-13204" %}}: The ProcessingUnitInstance.relocationAndWait() may not abort after timeout, causing a permanent blocking state.

{{% refer%}}
The complete list of changes is also available in {{%exurl "JIRA" "https://insightedge.atlassian.net/issues/?filter=17726" %}}.
{{%/refer%}}
