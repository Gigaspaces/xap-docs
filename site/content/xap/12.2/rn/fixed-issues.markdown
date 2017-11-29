---
type: post122
title:  Changelog
categories: XAP122RN
parent: none
weight: 300
---

This section lists the changes that were made in InsightEdge Platform 12.2.X.

# 12.2.1

This service pack was released in November 2017.

## Improvements

- {{% jira id="GS-13389" %}}: Lookup Service (LUS) enhancements to improve stability in environments with large clusters.
- {{% jira id="GS-13417" %}}: Add metrics to Lookup Service to facilitate troubleshooting of performance issues

## Resolved Issues

- {{% jira id="GS-13430" %}}: Inconsistent Request ID type in Manager REST API (string vs. numeric).
- {{% jira id="GS-13427" %}}: The REST manager specification contained the wrong request status values.
- {{% jira id="GS-13416" %}}: Lookup service improvements resulted in loss of client notifications in rare scenarios.
- {{% jira id="GS-13281" %}}: Querying a type with enum on a re-deployed space returns incorrect results.
- {{% jira id="GS-13371" %}}: REST calls to Spark failed on machine failover.
- {{% jira id="GS-13383" %}}: Insightedge shutdown command on Mac systems doesn't terminate the gs-agent.


# 12.2

The main version was released in September 2017.

## Features and Improvements

- {{% jira id="GS-13295" %}}: Pluggable XAP Manager RESTful operations.
- {{% jira id="GS-13355" %}}: XAP SQL-99 and JDBC implementation.
- {{% jira id="GS-13358" %}}: Enhance gs-agent to support verbs containing `-`.
- {{% jira id="GS-13313" %}}: Enhance REST over Spark.
- {{% jira id="GS-13319" %}}: Upgrade to spark 2.2.0.
- {{% jira id="GS-13320" %}}: Package InsightEdge example sources.
- {{% jira id="GS-13294" %}}: On MemoryXtend, Read operation with projection including only indexed fields executes without unpacking non-indexed fields.
- {{% jira id="GS-13296" %}}: Start Spark Master via gs-agent.
- {{% jira id="GS-13292" %}}: Upgrade RocksDB version to 5.5.1.
- {{% jira id="GS-13278" %}}: Take optimization form Memory Extend in backup .

## Resolved Issues

- {{% jira id="GS-13359" %}}: An asynchronous connection generated a null pointer, causing a connection/socket leak.
- {{% jira id="GS-13318" %}}: When the secured XAP Manager is initialized, it does not start and requests the default security.properties file.
- {{% jira id="GS-13346" %}}: The XAP_GSA_OPTIONS environment variable is not applied to the gs-agent.sh script.
- {{% jira id="GS-13315" %}}: The XAP Manager unevenly deploys instances when there are extra containers.
- {{% jira id="GS-13305" %}}: Cannot deploy a clustered Space with MemoryXtend and blob-store-cache-query configuration tag.
- {{% jira id="GS-13299" %}}: Customer client application stops getting notification events when all 20 connections are in use during high load.
- {{% jira id="GS-13357" %}}: Recovery attempts continued after a Space was marked as unstable.
- {{% jira id="GS-13245" %}}: Wrong access permissions for xap-common.jar in the installation package.


{{% refer%}}
The complete list of changes is also available in {{%exurl "JIRA" "https://insightedge.atlassian.net/issues/?filter=17723" %}}.
{{%/refer%}}