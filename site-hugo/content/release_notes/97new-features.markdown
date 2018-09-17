---
type: post
title:  New Features and Improvements
categories: RELEASE_NOTES
parent: xap97.html
weight: 200
---


Below is a list of new features and improvements in GigaSpaces 9.7.X.



|Key|Summary|Since Version|SalesForce ID|Documentation Link| Platform/s
|:--|:------|:------------|:------------|:-----------------|:----------|
|GS-23    | Added LINQ Support for XAP.Net | 9.7.0 | | | .Net |
|GS-8601  | Identifying clients that are using an old version of a POJO class | 9.7.0 | 6424 | | All |
|GS-9362  | GCS/LUS/GSM inherits COMPONENT_OPTIONS from GSA - relay on order of parameter | 9.7.0 | 6777, 8011 | | All |
|GS-9785  | Add support for escaping reserved words when queried | 9.7.0 | 7141 | | All |
|<nobr>GS-10590</nobr> | Protect from writing object to space without an ID | 9.7.0 | | | Java, .Net |
|GS-10888 | Enhance LRMI watchdog to close hanged connection when it can decide, with high guarantee the response will never come | 9.7.0 | | | All |
|GS-11208 | Projection support for nested paths | 9.7.0 | | | Java, .Net |
|GS-11213 | Add GigaSpacesRuntime.shutdown() API, which destroys GigaSpaces runtime while being hosted outside the service grid | 9.7.0 | 8151 | | Java |
|GS-11225 | Expose space remote activities in Web-ui | 9.7.0 | | | All |
|GS-11232 | Display used lookup group and locator in XAP Web-ui after login | 9.7.0 | | | All |
|GS-11242 | Server execution aspect doesn't expose the invoked Method | 9.6.1, 9.7.0 | 8102 | | Java |
|GS-11246 | Gs-ui should resolve custom security parameters | 9.6.1, 9.7.0 | 7904 | | All |
|GS-11250 | Seperate SpaceTask execution to a separate thread pool from other operations | 9.7.0 | | | Java, .Net |
|GS-11252 | Improve XAP.Net configuration to support multiple GSA configurations | 9.6.1, 9.7.0 | | | .Net |
|GS-11253 | Unique index support | 9.7.0 | | | Java, .Net |
|GS-11262 | Upgrade Spring (not including security) to 3.2.4 | 9.7.0 | | | Java |
|GS-11263 | Mongo Space Persistency implementation | 9.7.0 | | | Java |
|GS-11270 | Allow to designate a zone for primary instances and zone for backup instances | 8.0.5 patch1, 9.7.0 | | | All |
|GS-11277 | Mule: entries are written to the space without a write lease | 9.5.2 patch3, 9.6.2, 9.7.0 | 7934 | | Java |
|GS-11293 | SpaceAddedEventManager interface should have options to add listener that does not include existing spaces | 9.7.0 | | | Java |
|GS-11297 | Add create security privilege which allows only writing new objects to the space | 9.7.0 | | | All |
|GS-11298 | com.gigaspaces.license logger at level config will print where the license key was taken from | 9.7.0 | | | All |
|GS-11299 | Multiplex event session management enhancements | 9.7.0 | | | All |
|GS-11302 | Block users deploying a Mirror using a cluster schema | 9.7.0 | | | All |
|GS-11308 | Add GigaSpace.newDataEventSession() to simplify data event session creation | 9.7.0 | | | Java |
|GS-11309 | Deprecation - EventSessionFactory - use GigaSpace.newDataEventSession() instead | 9.7.0 | | | Java |
|GS-11311 | Expose PID and host in LRMIProxyMonitoringDetails | 9.7.0 | | | Java |
|GS-11324 | The ESM cannot start in secure mode | 9.5.0 patch3, 9.7.0 | 8254 | | Java |
|GS-11338 | LRMINoSuchObjectException is logged repeatedly after restart/relocation | 9.6.2 patch1, 9.7.0 | | | All |
|GS-11343 | Enhanced XAP.Net installer to include VC runtime files required by JDK7 | 9.6.1, 9.7.0 | | | .Net |
|GS-11350 | Add support for "<>" in the Cassandra space data source | 9.6.1 patch1, 9_6_2, 9.7.0 | 8298 | | Java |
|GS-11362 | Add support for Mule OpenSpaces queue connector attributes place holders | 9.5.2 patch3, 9.6.2, 9.7.0 | | | Java |
|GS-11363 | Simplify bin folder in XAP | 9.7.0 | | | Java |
|GS-11378 | Expose the change operation affect on ChangeResult if required | 9.7.0 | | | Java |
|GS-11403 | Change Extension - simplified addAndGet operation | 9.7.0 | | | Java, .Net |
|GS-11404 | Change data events default com type from unicast to multiplex | 9.7.0 | | | All |
|GS-11405 | Deprecation - Data events communication type | 9.7.0 | | | All |
|GS-11406 | Deprecation - Data event listeners with custom lease - use auto-renew instead | 9.7.0 | | | All |
|GS-11417 | Add 'version' command to CLI | 9.7.0 | | | All |
|GS-11421 | Cassandra Persistency - Simplify extending implementation | 9.6.2, 9.7.0 | | | Java |
|GS-11428 | Expose IProcessingUnit.UndeployAndWait API in .Net | 9.7.0 | | | .Net |
|GS-11453 | Allow to configure web-ui client disconnection time | 9.7.0 | | | Java |
|GS-11471 | Update XAP.Net bundled JDK to Oracle 7 update 45 | 9.7.0 | | | .Net |
|GS-11474 | Change remove handling in hibernate space data source, to use SpaceId if possible | 9.7.0 | 8461 | | All |
|GS-11477 | Add protective mode which prevents user error by querying with templates that has no null values | 9.7.0 | | | All |
|GS-11480 | Add system property to determine whether DateTime values read from the space should be converted from UTC to local time | 9.6.1 patch4, 9.7.0 | 8471 | | .Net |
|GS-11493 | Enhance EventTemplate attribute to support method with ISpaceProxy argument | 9.7.0 | | | .Net |
|GS-11494 | Add ISpaceProxy.GetServerAdmin().GetClusteredProxy() | 9.7.0 | | | .Net |
|GS-11495 | Deprecation - Customized auto-renew for data events | 9.7.0 | | | All |
|GS-11514 | Add strongly typed API for creating local cache in XAP.Net | 9.7.0 | | | .Net |
|GS-11515 | Change XAP.Net setup wizard default from both .Net 2.0 and 4.0 to 4.0 only | 9.7.0 | | | .Net |
|GS-11516 | Remove GSM and GSC windows service wrapper in XAP.Net | 9.7.0 | | | .Net |
|GS-11517 | Remove CLI startup script windows service wrapper in XAP.Net | 9.7.0 | | | .Net |
|GS-11526 | Provide capability of automatic thread dump generation upon LRMI critical resource level warning | 9.7.0 | | | All |
|GS-11539 | Web-ui should expose reconnect option | 9.7.0 | | | Java |
|GS-11544 | Modify XAP.Net assemblyVersion to Major.Minor format | 9.7.0 | | | .Net |
|GS-11596 | Log severe messages when warning persists | 9.7.1 | 8622 | | All |
|GS-11612 | Deploy process optimization | 9.7.1 | | | Java, .NET |
|GS-11621 | Bad linux scripts for the benchmark cli tool | 9.7.1 | | | Java |
|GS-11639 | Web-UI uses large amounts of memory for statistics | 9.7.0 | 8741 | | Java |
|GS-11656 | Notification event triggering is moved to a custom LRMI thread pool (together with space tasks) | 9.7.0 | | | All |
|GS-11666 | Pojo to Document Conversion should not convert Class,URI,Locale types | 9.7.1 | | | Java |
|GS-11691 | Create a setting to skip authentication for transaction commit/abort | 9.7.1 | 8765 | | All |
|GS-11693 | Enhance nHibernate EDS to support overriding initial load | 9.7.1 | | | .Net |
|GS-11696 | Support custom initial Load query for mongo data source | 9.7.1 | | | Java |
|GS-11702 | Added support for configuring a persistent space without custom code in XAP.NET | 9.7.1 | | | .Net |
|GS-11706 | Added static ProcessingUnitContainer.Current to provide cluster info to any pu component | 9.7.1 | | | .Net |
|GS-11710 | Simplify processing unit configuration in XAP.NET | 9.7.1 | | | .Net |
|GS-11731 | Simplify mirror configuration in XAP.NET | 9.7.1 | | | .Net |
|GS-11737 | Enhance XAP.NET processing unit to support properties replacement | 9.7.1 | | | .Net |
|GS-11771 | Allow to define zones during deployment in GS-UI | 9.7.1 | | | Java |
|GS-11772 | Allow to define zones during deployment in WEB-UI | 9.7.1 | | | Java |
