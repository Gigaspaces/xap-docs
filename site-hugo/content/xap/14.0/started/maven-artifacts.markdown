---
type: post140
title:  Maven Artifacts
categories: XAP140GS
parent: installation-maven-overview.html
weight: 50
---



XAP is Maven-friendly. It is built using Maven and is designed to be easily used by developers constructing XAP applications. In this page we'll explain how to use XAP with Maven.

# XAP Maven Repository

XAP artifacts are not currently published in the Maven Central Repo, so you must first configure a repository:

```xml
<repository>
   <id>org.openspaces</id>
   <url>http://maven-repository.openspaces.org</url>
</repository>
```

# XAP Artifacts

The main dependency required to use XAP is `xap-openspaces`.

```xml
<dependency>
  <groupId>org.gigaspaces</groupId>
  <artifactId>xap-openspaces</artifactId>
  <version>{{%version "maven-version" %}}</version>
</dependency>
```


## Core Artifacts

This section lists the core artifacts. You only need to add `xap-openspaces`, because all the other core artifacts are its dependencies.

The group ID for these artifacts is `org.gigaspaces`.

| Artifact ID	   | Location in product |
|------------------|---------------------|
| xap-openspaces   | ${XAP_HOME}/lib/required/xap-openspaces.jar	|
| xap-datagrid     | ${XAP_HOME}/lib/required/xap-datagrid.jar	|
| xap-common	   | ${XAP_HOME}/lib/required/xap-common.jar		|
| xap-trove	       | ${XAP_HOME}/lib/required/xap-trove.jar		|
| xap-asm		   | ${XAP_HOME}/lib/required/xap-asm.jar			|

## Extension Artifacts

This section lists extension artifacts. The group ID for these artifacts is `org.gigaspaces`.

| Artifact ID	   | Location in product | Required for |
|------------------|---------------------|---|
| xap-map 			    | ${XAP_HOME}/lib/optional/map/xap-map.jar | [Map API](../dev-java/map-api.html) |
| xap-map-spring		| ${XAP_HOME}/lib/optional/map/xap-map-spring.jar | [Map API](../dev-java/map-api.html) |
| xap-jms				| ${XAP_HOME}/lib/optional/jms/xap-jms.jar | [JMS](../dev-java/messaging-support.html) |
| xap-jetty			| ${XAP_HOME}/lib/optional/jetty/xap-jetty/xap-jetty.jar | [Jetty PU Container](../dev-java/web-jetty-processing-unit-container.html) |
| xap-mongodb			| ${XAP_HOME}/lib/optional/mongodb/xap-mongodb.jar | [MongoDB integration](../dev-java/mongodb.html) |
| xap-spatial			| ${XAP_HOME}/lib/optional/spatial/xap-spatial.jar | [Geospatial Queries](../dev-java/query-geospatial.html) |
| xap-full-text-search| ${XAP_HOME}/lib/optional/full-text-search/xap-full-text-search.jar | [Full Text Search](../dev-java/query-full-text-search.html) |

## Premium Extensions

This section lists Premium artifacts. The group ID for these artifacts is `com.gigaspaces`.

| Artifact ID	   | Location in product | Required for |
|------------------|---------------------|---|
| xap-near-cache-spring | ${XAP_HOME}/lib/optional/near-cache/xap-near-cache-spring.jar | [Client Side Caching](../dev-java/client-side-caching.html) |
| xap-near-cache 		  | ${XAP_HOME}/lib/optional/near-cache/xap-near-cache.jar | [Client Side Caching](../dev-java/client-side-caching.html) |
| xap-security		  | ${XAP_HOME}/lib/optional/security/xap-security.jar | [Security](../security/) |
| xap-zookeeper		  | ${XAP_HOME}/lib/platform/zookeeper/xap-zookeeper.jar | [Consistency-biased leader election](../admin/leader-election-consistency-biased.html) |
| xap-admin 			  | ${XAP_HOME}/lib/platform/service-grid/xap-admin.jar | [Admin API](../dev-java/administration-and-monitoring-overview.html)|

## Enterprise Extensions

This section lists Enterprise artifacts. The group ID for these artifacts is `com.gigaspaces`.

| Artifact ID	   | Location in product | Required for |
|------------------|---------------------|---|
| xap-mx-rocksdb | ${XAP_HOME}/lib/optional/memoryxtend/rocksdb/xap-mx-rocksdb.jar | [MemoryXtend - SSD](../admin/memoryxtend-overview.html)
| xap-wan-gateway-spring| ${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway-spring.jar | [WAN Replication](../dev-java/multi-site-replication-overview.html) |
| xap-wan-gateway		  | ${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway.jar | [WAN Replication](../dev-java/multi-site-replication-overview.html) |
