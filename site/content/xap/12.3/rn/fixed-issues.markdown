---
type: post123
title:  Changelog
categories: XAP123RN
parent: none
weight: 300
---

This section lists the changes that were made in InsightEdge Platform 12.3.

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