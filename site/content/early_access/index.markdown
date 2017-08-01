---
type: post
title:  Early Access
parent:
categories: EARLY_ACCESS
weight:
---

This page contains early access information for XAP and InsightEdge 12.2, which is scheduled for release in September 2017. Early access builds are intended for those who wish to get involved in the development process to try out new stuff early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

{{%plus%}} If you're just getting started with 12.2, we recommend reading [What's New](/release_notes/122whats-new.html) page.

{{%info "Disclaimer"%}}
Early Access builds are provided as-is and should not be used in production. If your're looking for the latest stable release, please refer to **12.1.1** - [Download](http://www.gigaspaces.com/xap-download) | [Docs](/xap121.html)
{{%/info%}}
<hr/>

## 12.2 M7 (Jul-30-2017)

**Download Links**

* XAP \[[Open Source](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.2.0/12.2.0-m7/gigaspaces-xap-open-12.2.0-m7-b18008.zip) | [Premium](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/gigaspaces-xap-premium-12.2.0-m7-b18008.zip) | [Enterprise](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/gigaspaces-xap-enterprise-12.2.0-m7-b18008.zip)\] 
* XAP.NET \[[Premium x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/GigaSpaces-XAP.NET-Premium-12.2.0.18008-M7-x64.msi) | [Premium x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/GigaSpaces-XAP.NET-Premium-12.2.0.18008-M7-x86.msi) | [Enterprise x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/GigaSpaces-XAP.NET-Enterprise-12.2.0.18008-M7-x64.msi) | [Enterprise x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m7/GigaSpaces-XAP.NET-Enterprise-12.2.0.18008-M7-x86.msi)\]
* InsightEdge Platform \[[InsightEdge](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.2.0/12.2.0-m7/gigaspaces-insightedge-12.2.0-m7-11008-premium.zip)\] 

**Summary**

- New InsightEdge Platform package - The InsightEdge package has been restructured to better serve both XAP and Apache Spark users. It will be built and released each milestone alongside XAP.
- InsightEdge can now use *any* standard spark distribution - use the `SPARK_HOME` environment variable to tell InsightEdge where it resides.
- Use `gs-agent --manager --spark_master` to start a Spark master alongside XAP manager. The spark master will automatically use the Manager's Zookeeper for high availability.
- Use `gs-agent --spark_slave` to start a Spark slave (worker). The slave will automatically search for Spark masters on each of XAP management servers.

**Resolved Issues**

|ID         | Type        | Description                                                  |
|-----------|-------------|--------------------------------------------------------------|
| XAP-13292 | New Feature | RocksDB has been upgraded to 5.5.1 |
