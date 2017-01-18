---
type: post121
title:  Maven Artifacts
categories: XAP121
parent: installation-maven-overview.html
weight: 50
---

{{% ssummary%}}{{% /ssummary %}}

XAP is Maven-friendly - it is built using maven and designed to be easily used by developers constructing XAP applications. In this page we'll explain how to use XAP with Maven.

# XAP Maven Repository

Since XAP artifacts are currently not published in Maven Central Repo, you'll first need to configure a repository:

```xml
<repository>
   <id>org.openspaces</id>
   <url>http://maven-repository.openspaces.org</url>
</repository>
```

# XAP Artifacts

The main dependency required to use XAP is `xap-openspaces`

```xml
<dependency>
  <groupId>org.gigaspaces</groupId>
  <artifactId>xap-openspaces</artifactId>
  <version>{{%version "maven-version" %}}</version>
</dependency>
```


## Core Artifacts

This section lists core artifacts. Note that you only need to add `xap-openspaces`, since all other core artifacts are its dependencies.

The group Id for the these artifacts is `org.gigaspaces`

| Artifact Id	   | Location in product |
|------------------|---------------------|
| xap-openspaces | ${XAP_HOME}/lib/required/xap-openspaces.jar	|
| xap-datagrid   | ${XAP_HOME}/lib/required/xap-datagrid.jar	|
| xap-common	   | ${XAP_HOME}/lib/required/xap-common.jar		|
| xap-trove	   | ${XAP_HOME}/lib/required/xap-trove.jar		|
| xap-asm		   | ${XAP_HOME}/lib/required/xap-asm.jar			|

## Extension artifacts

This section lists extension artifacts. The group Id for the these artifacts is `org.gigaspaces`

| Artifact Id	   | Location in product | Required For |
|------------------|---------------------|---|
| xap-map 			| ${XAP_HOME}/lib/optional/map/xap-map.jar | [Map API](./map-api.html) |
| xap-map-spring		| ${XAP_HOME}/lib/optional/map/xap-map-spring.jar | [Map API](./map-api.html) |
| xap-jms				| ${XAP_HOME}/lib/optional/jms/xap-jms.jar | [JMS](./messaging-support.html) |
| xap-jetty-8			| ${XAP_HOME}/lib/optional/jetty/xap-jetty/xap-jetty-8.jar | [Jetty PU Container](./web-jetty-processing-unit-container.html) |
| xap-jetty-9			| ${XAP_HOME}/lib/optional/jetty-9/xap-jetty/xap-jetty-9.jar | [Jetty PU Container](./web-jetty-processing-unit-container.html) |
| xap-mongodb			| ${XAP_HOME}/lib/optional/mongodb/xap-mongodb.jar | [MongoDB integration](./mongodb.html) |
| xap-spatial			| ${XAP_HOME}/lib/optional/spatial/xap-spatial.jar | [Geospatial Queries](./query-geospatial.html) |
| xap-full-text-search| ${XAP_HOME}/lib/optional/full-text-search/xap-full-text-search.jar | Full Text Search |

## Premium Extensions

This section lists premium artifacts. The group Id for the these artifacts is `com.gigaspaces`

| Artifact Id	   | Location in product | Required For |
|------------------|---------------------|---|
| xap-near-cache-spring | ${XAP_HOME}/lib/optional/near-cache/xap-near-cache-spring.jar | [Client Side Caching](./client-side-caching.html) |
| xap-near-cache 		  | ${XAP_HOME}/lib/optional/near-cache/xap-near-cache.jar | [Client Side Caching](./client-side-caching.html) |
| xap-wan-gateway-spring| ${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway-spring.jar | [WAN Replication](./multi-site-replication-overview.html) |
| xap-wan-gateway		  | ${XAP_HOME}/lib/optional/wan-gateway/xap-wan-gateway.jar | [WAN Replication](./multi-site-replication-overview.html) |
| xap-security		  | ${XAP_HOME}/lib/optional/security/xap-security.jar | [Security]({{%currentsecurl%}}/security.html) |
| xap-zookeeper		  | ${XAP_HOME}/lib/platform/zookeeper/xap-zookeeper.jar | [Consistency-biased leader election]({{%currentadmurl%}}/leader-election-consistency-biased.html) |
| xap-admin 			  | ${XAP_HOME}/lib/platform/service-grid/xap-admin.jar | [Admin API](./administration-and-monitoring-overview.html)|

