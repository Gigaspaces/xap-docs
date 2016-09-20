---
type: post120
title:  Maven Artifacts
categories: XAP120
parent: installation-maven-overview.html
weight: 50
---

{{% ssummary%}}{{% /ssummary %}}

XAP is Maven-friendly - it is built using maven and designed to be easily used by developers constructing XAP applications. In this page we'll explain how to use XAP with Maven.

# XAP Artifacts

The main dependency required to use XAP is `xap-openspaces`

```xml
<dependency>
  <groupId>org.gigaspaces</groupId>
  <artifactId>xap-openspaces</artifactId>
  <version>{{%version "maven-version" %}}</version>
</dependency>
```

Since XAP artifacts are currently not published in Maven Central Repo, you'll also need to configure a repository:

```xml
<repository>
   <id>org.openspaces</id>
   <url>http://maven-repository.openspaces.org</url>
</repository>
```
