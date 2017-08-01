---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap122.html
weight: 100
---

This page lists the main new features in XAP 12.2.

It's not an exhaustive list of all new features. For a full change log of 12.2 please refer to the [new features](./122new-features.html) and [fixed issues](./122fixed-issues.html) pages.

# The InsightEdge Platform

Starting 12.2, InsightEdge is transforming from a Spark Distribution to a **Platform** which combines XAP, Apache Spark and the InsightEdge components, to serve both transactional and analytical use cases.

InsightEdge is released as a new package, alongside the existing XAP packages (OSS, Premium and Enterprise), and use XAP's versioning scheme. The InsightEdge package contains an Apache Spark distribution, but users may override it and use any Spark distribution, as long as its standard and compatible with the Apache Spark distribution.

# Functionality

More info coming soon...

# Administration

## Spark Standalone Management

Spark Standalone cluster is fully integrated into XAP management environment and tools. In addition to starting spark master/slave via Spark's standard scripts, you can use XAP's process manager (`gs-agent`) to start a master and/or slave.

`gs-agent --manager --spark_master` will start a spark master alongside XAP Manager. If you require high availability, simply start 3 servers like that, and the spark masters will automatically connect to the managers Zookeeper cluster and elect a leader.

`gs-agent --spark_slave` will start a spark slave (a.k.a. worker), and automatically set it's master url to the masters (assuming there's a master on each XAP manager).

Note: gs-agent support for spark is currently limited for Linux only. Windows support will be added in upcoming milestones.

# Third Party Upgrades

* [RocksDB](http://rocksdb.org/) integration has been upgraded to `5.5.1`
