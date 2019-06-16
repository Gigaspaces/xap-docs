---
type: post
title:  Early Access
parent: none
categories: EARLY_ACCESS
weight: 1
---

This page contains early access information for the InsightEdge Platform and XAP data grid version 14.5.0, which is scheduled for release at the end of Q2 2019. Early access builds are intended for those who want to get involved in the development process and try out new features and functionality early on, and even affect the final outcome. If you have any feedback on early access features, we'd love to hear it!

{{%tip "Disclaimer"%}} Early access builds are provided as is, and should not be used in production. The latest stable release is version 14.2.0.<br>[Download](https://www.gigaspaces.com/download-center) | [Documentation](/xap/14.0/)</br>{{%/tip%}}

# 14.5.0 M9 (Jun-16-2019)

## Download Links

 * [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.5.0/gigaspaces-insightedge-enterprise-14.5.0-m9.zip) 

 * [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap-enterprise-14.5.0-m9.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap.net-14.5.0-m9.msi)

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13853" %}}: xxx.

# 14.5.0 M8 (Jun-10-2019)

## Download Links

 * [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.5.0/gigaspaces-insightedge-enterprise-14.5.0-m8.zip) 

 * [XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap-enterprise-14.5.0-m8.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap.net-14.5.0-m8.msi)

## Changelog

### Features and Enhancements

N/A

### Resolved Issues

- {{% jira id="GS-13812" %}}: Full recovery does not preserve the FIFO grouping order.

# 14.5.0 M7 (Jun-02-2019)

## Download Links

 * [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.5.0/gigaspaces-insightedge-enterprise-14.5.0-m7.zip) 

 * \[[XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap-enterprise-14.5.0-m7.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap.net-14.5.0-m7.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13820" %}}: Remove the XAP Maven plugin from the software package.
- {{% jira id="GS-13847" %}}: New 'maven install' CLI command for installing GigaSpaces Maven artifacts.
- {{% jira id="GS-13848" %}}: Remove bundled Apache Maven from package.

### Resolved Issues

- {{% jira id="GS-13842" %}}: Support reading DataFrame of a POJO with an enum field.

# 14.5.0 M6 (May-26-2019)

## Download Links

 * [InsightEdge Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/insightedge/14.5.0/gigaspaces-insightedge-enterprise-14.5.0-m6.zip) 

 * \[[XAP Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap-enterprise-14.5.0-m6.zip) | [XAP.NET Enterprise](https://gigaspaces-releases-eu.s3.amazonaws.com/xap/14.5.0/gigaspaces-xap.net-14.5.0-m6.msi)\]

## Changelog

### Features and Enhancements

- {{% jira id="GS-13844" %}}: Added CsvReader to simplify reading data from .CSV files.
- {{% jira id="GS-13843" %}}: Upgrade Apache Calcite to 1.13.
- {{% jira id="GS-13841" %}}: Enhanced JDBC support - the standard java.sql.Driver file is now packaged.
- {{% jira id="GS-13813" %}}: Replace Sigar with Oshi.
- {{% jira id="GS-13826" %}}: Interactive shell for command line interface.
- {{% jira id="GS-13834" %}}: Deprecate EXT_JAVA_OPTIONS - use XAP_OPTIONS_EXT instead.
- {{% jira id="GS-13830" %}}: Add XAP_LIBRARY_PATH and XAP_LIBRARY_PATH_EXT environment variables to override/extend java.library.path.
- {{% jira id="GS-13833" %}}: Add XAP_OPTIONS and XAP_OPTIONS_EXT environment variables.
- {{% jira id="GS-13832" %}}: Enhance 'pu run' CLI command to accept properties.
- {{% jira id="GS-13678" %}}: Upgrade PicoCLI to 3.9.5.
- {{% jira id="GS-13829" %}}: Rename mx-pmem file name parameter with pmem-path.
- {{% jira id="GS-13807" %}}: Reduce network traffic when the GSC connects with the GSM.


### Resolved Issues

- {{% jira id="GS-13828" %}}: Processing Unit stays in COMPROMISED state after network disruption, due to failure of the GSC to register with the GSM.
- {{% jira id="GS-13831" %}}: Command line generator does not quote the com.gs.home path if it contains space characters.
