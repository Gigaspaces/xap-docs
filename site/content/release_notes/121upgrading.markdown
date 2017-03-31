---
type: post
title:  Upgrading from previous versions
categories:
parent: xap121.html
weight: 800
---

This page contains information about changes in this release which can affect upgrading from previous versions. Note that it includes information about changes from the previous version (12.0) - if you're upgrading from an older version, we recommend you read the respective "Upgrading from previous versions" pages of those versions.

## Minimum Java 7

Oracle has stopped supporting Java 6 back in Feb 2013 ([Oracle Java SE Support Roadmap](http://www.oracle.com/technetwork/java/eol-135779.html)). Starting 12.1, Java 6 is no longer supported with XAP as well, and the minimum requirement is Java 7.

(Note that Java 7 is no longer supported by Oracle as well - we strongly recommend upgrading to the latest Java 8 update)

## GS-Agent New Syntax

GS-Agent supports a new, user-friendly syntax and semantics, side-by-side with the previous behaviour. In a nutshell:

A) You can use `--` instead of `gsa.` prefix. For example, `--gsc=2` will start 2 GSCs, equivalent to `gsa.gsc 2`

B) GS-Agent defaults to 2 GSCs, 2 global GSMs and 2 global LUSs. This forces you to explicitly disable each of those. For example, if you want to start 2 GSCc without any global LUS or GSM, you'd specify: `gsa.gsc 2 gsa.global.gsm 0 gsa.global.lus 0`. However, using the new syntax automatically disables defaults, so you can simply specify `--gsc=2`.

Note that the previous syntax is still fully supported and unchanged - if you've written scripts based on it in previous versions, they will behave the same in 12.1. We do recommend upgrading to the new syntax as soon as possible - it will simplify your scripts.

For more information see [The Runtime Environment](/xap121adm/the-runtime-environment.html).

## Replication Configuration Semantics

We've made some changes in replication configuration semantics. Most users upgrading from earlier version will either not notice this change, or will experience more predictable behaviour. However, if you've modified the default configuration settings, we recommend you read the relevant section (Weight Policy) before upgrading, and verify how this change affects your system in a test environment. If needed, this change can be disabled to restore the previous semantics.

For more information see [Weight Policy](/xap121adm/controlling-the-replication-redo-log.html#weight-policy)

## New XAP Manager

XAP 12.1 offers a simplified management experience using the XAP Manager, which unifies GSM and LUS components, as well as an embedded Zookeeper (including more resilient leader election algorithm) and a RESTful management API. 

This is not a requirement for upgrading - you can perform a simple upgrade and continue using the service grid as-is. However, we recommend you learn about this and make the necessary changes to leverage it, as future improvements will be built on top of it.

For more information see [XAP Manager](/xap121adm/xap-manager.html).

## Third Party Upgrades

* [Jetty](http://www.eclipse.org/jetty/) integration has been upgraded to `9.2.21`
* [Curator](http://curator.apache.org/) integration has been upgraded to `12.2.0`
* [Lucene](https://lucene.apache.org/) integration has been upgraded to `6.4.2` (affects geospatial and full text search)
* [Spatial4J](https://github.com/locationtech/spatial4j) integration has been upgraded to `0.6` (affects geospatial)

In addition, the jvm bundled with XAP.NET hase been upgraded to Java 8 update 121.

## Deprecations and Removed APIs

### Deprecations

* [Elastic Processing Unit](/xap120/elastic-processing-unit-overview.html) is deprecated, and will be removed in future releases. 

### Removals

* `SpaceConfigurer.destroy()` has been removed - use `close()` instead.
* `GigaSpace` operations with `int` modifiers have been removed - use explicit type modifiers instead (e.g. `WriteModifiers`, `ReadModifiers`).
* Event subscription with `NotifyComType` has been removed - there's no longer any benefit in changing the default notify communication type.
