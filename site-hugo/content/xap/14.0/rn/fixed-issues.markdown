---
type: post140
title:  Changelog
categories: XAP140RN
parent: none
weight: 300
---

This section lists the changes that were made in InsightEdge Platform 14.0.x.

# 14.0.0

# Features and Enhancements

- {{% jira id="GS-13593" %}}: Kubernetes support.
- {{% jira id="GS-13601" %}}: New MemoryXtend driver for Persistent Memory (PMEM).
- {{% jira id="GS-13679" %}}: Change InsightEdge space name in demo mode.
- {{% jira id="GS-13681" %}}: Migrate InsightEdge examples from separate repo to InsightEdge.
- {{% jira id="GS-13695" %}}: Upgrade Jetty version to 9.3 latest. 
- {{% jira id="GS-13693" %}}: Deprecate gs-agent script.
- {{% jira id="GS-13692" %}}: Deprecate legacy command line interface.
- {{% jira id="GS-13688" %}}: Support querying space data from REST Manager and CLI.
- {{% jira id="GS-13686" %}}: Enhanced CLI 'pu run' command validation to include invalid instances.
- {{% jira id="GS-13685" %}}: Improve error message for CLI command 'pu run' when file not found.
- {{% jira id="GS-13622" %}}: Support unmanaged Processing Units in Admin API.
- {{% jira id="GS-13599" %}}: Changed default rownum from 5000 to 10 in Web Management Console.
- {{% jira id="GS-13644" %}}: Upgrade Zeppelin to 0.8.
- {{% jira id="GS-13656" %}}: Add Zeppelin InsightEdge JDBC interpreter. 
- {{% jira id="GS-13650" %}}: Add autoCommit property to InsightEdge JDBC Driver (for Zeppelin).
- {{% jira id="GS-13309" %}}: New API to initialize InsightEdge from existing Spark context/session.
- {{% jira id="GS-13667" %}}: Increase ZooKeeper client session timeout setting to 15 seconds.
- {{% jira id="GS-13588" %}}: Enhanced XAP.NET to support dtd validation via Tls12 url (s3).
- {{% jira id="GS-13677" %}}: End Of Life - XAP.NET for .NET v3.5.
- {{% jira id="GS-13675" %}}: InsightEdge JDBC driver automatically creates an alias for types using short name if not ambigous.
- {{% jira id="GS-13674" %}}: InsightEdge JDBC driver automatically creates an alias for types using underscores to allow non-escaped usage.
- {{% jira id="GS-13672" %}}: End Of Life - GigaSpace.getModifiersForIsolationLevel().
- {{% jira id="GS-13671" %}}: Deprecation - GSIterator and GigaSpace.iterator().
- {{% jira id="GS-13669" %}}: Enhance Deploy command in CLI to auto-zip resource in case it is a directory.
- {{% jira id="GS-13668" %}}: Reduce Web Management Console (web-ui) memory consumption due to unused statistics.
- {{% jira id="GS-13666" %}}: Upgrade Spark to 2.3.2.
- {{% jira id="GS-13637" %}}: Upgrade to Spring Security 4.2.8 and Spring Framework 4.3.19.
- {{% jira id="GS-13643" %}}: Configure insightedge-submit script to use Platform Manager name instead of lookup locator when running on Kubernetes.
- {{% jira id="GS-13499" %}}: Reduce replication network traffic by skipping discarded packets.
- {{% jira id="GS-13618" %}}: Add ChangeSet.isEmpty() to indicate if it contains any change operations.
- {{% jira id="GS-13619" %}}: Override the IdQuery.equals() default implementation to streamline unit testing.
- {{% jira id="GS-13602" %}}: Expose Jetty ports in the Admin API (such as the SslSelectChannelConnector used for HTTPS).
- {{% jira id="GS-13615" %}}: Run InsightEdge Spark jobs on Kubernetes using the InsightEdge Docker image.
- {{% jira id="GS-13600" %}}: Add system property to show standalone Spaces in REST Manager API and CLI.
- {{% jira id="GS-13448" %}}: Enhance logging to reflect replication connection state changes.
- {{% jira id="GS-13596" %}}: Enhance command line `--server` option to support `host:port` format.
- {{% jira id="GS-13591" %}}: Add a summary to CLI output.
- {{% jira id="GS-13573" %}}: Improve  XAP and InsightEdge demo command behavior when XAP_MANAGER_SERVERS is configured.


# Resolved Issues

- {{% jira id="GS-13689" %}}: InsightEdge JDBC driver does not work with Spark SQL.
- {{% jira id="GS-13631" %}}: When a transaction aborts due to network failure, it may not release the participating entries immediately.
- {{% jira id="GS-13658" %}}: Misspelled License in "Licence data is corrupted" error message.
- {{% jira id="GS-13657" %}}: Typo in XAP Manager system property for configuring ZooKeeper session timeout.
- {{% jira id="GS-13649" %}}: Fix InsightEdge JDBC Driver initialization to return null when connection fails.
- {{% jira id="GS-13627" %}}: EXT_JAVA_OPTIONS environment variable is ignored when using OpenJDK with Microsoft Windows.
- {{% jira id="GS-13661" %}}: SPARK_MOUNTED_CLASSPATH contains incorrect semicolon on Microsoft Windows.
- {{% jira id="GS-13535" %}}: Null comparison does not work in SQL query when using a nested collection of objects.
- {{% jira id="GS-13625" %}}: SQL query with Java date/time WHERE clause fails when date/time format property is configured.
- {{% jira id="GS-13603" %}}: The Web Management Console fails to parse java.util.Date properties.
- {{% jira id="GS-13404" %}}: Can't generate dump files from user interface after first attempt.
- {{% jira id="GS-13386" %}}: "Only Live Services Log Dump" doesn't work as expected.

{{% refer%}}
The complete list of changes is also available in {{%exurl "JIRA" "https://insightedge.atlassian.net/issues/?filter=17743" %}}.
{{%/refer%}}
