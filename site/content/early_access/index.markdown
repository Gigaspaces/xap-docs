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

## 12.2 M10 (Aug-21-2017)

**Download Links**

* XAP \[[Open Source](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.2.0/12.2.0-m10/gigaspaces-xap-open-12.2.0-m10-b18011.zip) | [Premium](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/gigaspaces-xap-premium-12.2.0-m10-b18011.zip) | [Enterprise](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/gigaspaces-xap-enterprise-12.2.0-m10-b18011.zip)\] 
* XAP.NET \[[Premium x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/GigaSpaces-XAP.NET-Premium-12.2.0.18011-M10-x64.msi) | [Premium x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/GigaSpaces-XAP.NET-Premium-12.2.0.18011-M10-x86.msi) | [Enterprise x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/GigaSpaces-XAP.NET-Enterprise-12.2.0.18011-M10-x64.msi) | [Enterprise x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m10/GigaSpaces-XAP.NET-Enterprise-12.2.0.18011-M10-x86.msi)\]
* InsightEdge Platform \[[InsightEdge](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.2.0/12.2.0-m10/gigaspaces-insightedge-12.2.0-m10-18011-premium.zip)\] 

**Summary**

- Enhancements to Pluggable XAP Manager RESTful operations
  - Response object and @CustomManagerResource annotation extracted to xap-admin.jar
  - @QueryParam support for primitive types as well as Integer, Double, String, etc. (except char - known issue)
  - Support for POST, PUT and DELETE operations
- XAP Manager RESTful API supports:
  - Monitor Spark job status 
  - Submit job spark request holds link to Spark job status and submitted driver Id of the Spark job
- Configuration and scripts now source setenv and setenv-overrides.
  - (added) /conf/insightedge-env.{sh/cmd}
  - (added) /insightedge/spark/conf/spark-env.{sh/cmd}
- Know issue: http-session non-sticky session fails after updating jax

**Resolved Issues**

|ID         | Type        | Description                                                                             |
|-----------|-------------|-----------------------------------------------------------------------------------------|
| XAP-13295 | New Feature | Move Response and CustomManagerResource annotation from xap-admin-rest-v1 to xap-admin  |
| XAP-13295 | New Feature | @QueryParam support for primitive and non-primitive                                     |
| XAP-13295 | New Feature | Add support for PUT, POST, DELETE using Query Param                                     |
| XAP-13313 | New Feature | GET spark application status                                                            |
| XAP-13313 | New Feature | Return Spark 'submit job status' URL in response                                        |


## 12.2 M9 (Aug-13-2017)

**Download Links**

* XAP \[[Open Source](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.2.0/12.2.0-m9/gigaspaces-xap-open-12.2.0-m9-b18010.zip) | [Premium](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/gigaspaces-xap-premium-12.2.0-m9-b18010.zip) | [Enterprise](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/gigaspaces-xap-enterprise-12.2.0-m9-b18010.zip)\] 
* XAP.NET \[[Premium x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/GigaSpaces-XAP.NET-Premium-12.2.0.18010-M9-x64.msi) | [Premium x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/GigaSpaces-XAP.NET-Premium-12.2.0.18010-M9-x86.msi) | [Enterprise x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/GigaSpaces-XAP.NET-Enterprise-12.2.0.18010-M9-x64.msi) | [Enterprise x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m9/GigaSpaces-XAP.NET-Enterprise-12.2.0.18010-M9-x86.msi)\]
* InsightEdge Platform \[[InsightEdge](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.2.0/12.2.0-m9/gigaspaces-insightedge-12.2.0-m9-18009-premium.zip)\] 

**Summary**

- Enhancements to Pluggable XAP Manager RESTful operations (see [What's New](/release_notes/122whats-new.html) for more info.
  - private/protected @Context
  - Response object (not just String) as a return value
- XAP Manager RESTful API supports:
  - Submitting a Spark job
- Simplified configuration for InsightEdge windows environment variables

**Resolved Issues**

|ID         | Type        | Description                                                               |
|-----------|-------------|---------------------------------------------------------------------------|
| XAP-13295 | New Feature | Allow using @Context on private/protected field (only Admin is supported) |
| XAP-13295 | New Feature | Allow to return Response object from custom REST controller               |



## 12.2 M8 (Aug-6-2017)

**Download Links**

* XAP \[[Open Source](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap-open/12.2.0/12.2.0-m8/gigaspaces-xap-open-12.2.0-m8-b18009.zip) | [Premium](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/gigaspaces-xap-premium-12.2.0-m8-b18009.zip) | [Enterprise](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/gigaspaces-xap-enterprise-12.2.0-m8-b18009.zip)\] 
* XAP.NET \[[Premium x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/GigaSpaces-XAP.NET-Premium-12.2.0.18009-M8-x64.msi) | [Premium x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/GigaSpaces-XAP.NET-Premium-12.2.0.18009-M8-x86.msi) | [Enterprise x64](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/GigaSpaces-XAP.NET-Enterprise-12.2.0.18009-M8-x64.msi) | [Enterprise x86](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/xap/12.2.0/12.2.0-m8/GigaSpaces-XAP.NET-Enterprise-12.2.0.18009-M8-x86.msi)\]
* InsightEdge Platform \[[InsightEdge](https://gigaspaces-repository-eu.s3.amazonaws.com/com/gigaspaces/insightedge/12.2.0/12.2.0-m8/gigaspaces-insightedge-12.2.0-m8-11009-premium.zip)\] 

**Summary**

- Initial support for Pluggable XAP Manager RESTful operations (see [What's New](/release_notes/122whats-new.html) for more info.
- XAP Manager RESTful API supports submitting a Spark job  
- XAP Manager RESTful API supports uploading and managing resources (jars) used for Spark job submission API
- Renamed gs-agent verb `spark_slave` to `spark_worker`
- Added Windows support for starting Spark master and worker via `gs-agent`
- Added `manager-local` support for starting Spark master and worker via `gs-agent`
- The `insightedge.sh` script now uses environment variables from `setenv.sh` (and `setenv-overrides.sh`)
- Reducing clutter in insightedge/sbin - subscripts are being merged into the main `insightedge.sh` script

**Resolved Issues**

|ID         | Type        | Description                                                  |
|-----------|-------------|--------------------------------------------------------------|
| XAP-13296 | New Feature | Start Spark Master via gs-agent |
| XAP-13297 | New Feature | Start Spark Worker (slave) via gs-agent |

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
