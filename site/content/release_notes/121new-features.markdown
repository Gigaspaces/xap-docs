---
type: post
title:  New Features
categories: RELEASE_NOTES
parent: xap121.html
weight: 200
---

List of new features and improvements in XAP 12.1.

## Open Source

New features and improvements in 12.1 Open Source edition:

| Key                         | Summary                                                 | Fix Version(s) | Platform(s) |
|:----------------------------|:------------------------------------------------------------------|:-----|:-----| 
| {{% jira id="XAP-12352" %}} | Reload modified space task without restarting the space           | 12.1 | Java | 
| {{% jira id="XAP-12508" %}} | Enhance replication configuration to improve transaction handling | 12.1 | All  |
| {{% jira id="XAP-12846" %}} | Get context details (as ports) from within Web PU                 | 12.1 | Java |
| {{% jira id="XAP-13009" %}} | SQL query support for full text search                            | 12.1 | All  |
| {{% jira id="XAP-13047" %}} | End Of Life - SpaceConfigurer destroy()                           | 12.1 | Java |
| {{% jira id="XAP-13048" %}} | SpaceConfigurer now extends standard Closable interface           | 12.1 | Java |
| {{% jira id="XAP-13098" %}} | Reload modified custom change without restarting the space        | 12.1 | Java |
| {{% jira id="XAP-13100" %}} | Protective mode for ambiguous query routing                       | 12.1 | All  |
| {{% jira id="XAP-13101" %}} | Reload modified custom aggregate without restarting the space     | 12.1 | Java |
| {{% jira id="XAP-13105" %}} | Upgrade Spatial4J integration to v0.6                             | 12.1 | All  |
| {{% jira id="XAP-13106" %}} | Upgrade Lucene integration to v6.4.2                              | 12.1 | All  |
| {{% jira id="XAP-13116" %}} | async-supported for web PUs using Jetty 9                         | 12.1 | All
| {{% jira id="XAP-13170" %}} | Upgrade Jetty integration to v9.2.21                              | 12.1 | All  |
| {{% jira id="XAP-13178" %}} | End Of Life - GigaSpace operations with int modifiers             | 12.1 | Java |
| {{% jira id="XAP-13180" %}} | End Of Life - NotifyComType                                       | 12.1 | Java |

## Premium and Enterprise

The Premium and enterprise editions are built on top of the Open Source edition, so naturally they include all its new features and improvements, in addition to the following:

| Key       | Summary                                                 | Fix Version(s) | Platform(s) |
|:----------|:------------------------------------------------------------------|:-----|:-----| 
| XAP-11214 | Web UI Custom Security                                            | 12.1 | All  |
| XAP-12988 | Dump can optionally collect terminated processes information      | 12.1 | All  |
| XAP-13018 | XAP Manager - unified management server                           | 12.1 | All  |
| XAP-13019 | Network Partition Protection for Service Grid                     | 12.1 | All  |
| XAP-13020 | Automatically use Consistent Active Election When Available       | 12.1 | All  |
| XAP-13021 | REST API for Grid Management                                      | 12.1 | All  |
| XAP-13023 | User-defined query for initial Load in MemoryXtend                | 12.1 | All  |
| XAP-13097 | Web-UI supports SSL                                               | 12.1 | All  |
| XAP-13111 | Use --zero-defaults or -z with gs-agent to start empty            | 12.1 | All  |
| XAP-13132 | Startup system report is verbose when log level is increased      | 12.1 | All  |
| XAP-13144 | Gs-agent supports '--' syntax in addition to old 'gsa.' prefix    | 12.1 | All  |
| XAP-13157 | Upgrade XAP.NET bundled java to 8u121                             | 12.1 | .NET |
| XAP-13179 | Display XAP Manager servers in web-ui and gs-ui                   | 12.1 | All  |
| XAP-13181 | Configurable RocksDB WriteOptions for MemoryXtend space           | 12.1 | All  |
| XAP-13199 | Deprecation - Elastic Processing Unit                             | 12.1 | All  |
