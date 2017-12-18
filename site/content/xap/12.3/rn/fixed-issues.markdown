---
type: post123
title:  Changelog
categories: XAP123RN
parent: none
weight: 300
---

This section lists the changes that were made in InsightEdge Platform 12.3.

# Features and Improvements

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