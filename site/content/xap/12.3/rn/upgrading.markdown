---
type: post123
title:  Upgrading from a Previous Version
categories: XAP123RN
parent: none
weight: 600
---

If you are currently using a previous version of XAP or InsightEdge, contact <support@gigaspaces.com> for information on how to upgrade to the most recent [version](https://www.gigaspaces.com/download-center).

As a best practice, when upgrading to the latest version, unzip the downloaded software package and port the necessary changes from your previous environment into your new environment. It is recommended to use the new distribution as-is. 
 
# Backwards Compatibility

This version of the InsightEdge Platform is backwards compatible to XAP version 11.0 (APIs and client-server). For more information about GigaSpaces' backwards compatibility policy, refer to the [Product Life Cycle and End-of-Life Policy](/release_notes/lifecycle.html) page.

For information about product changes that may affect your upgrade process, refer to the sections on Upgrading from a Previous Version for XAP [12.1](/release_notes/121upgrading.html), [12.0](/release_notes/120upgrading.html), and [11.0](/release_notes/110upgrading.html). 

# Windows Environments

If you are upgrading to version 12.2 or later in a Windows environment, you must add the following environment variables to the wrapper.conf file:

- `wrapper.java.additional.27 = -DXAP_GSA_OPTIONS=${xap_gsa_options}` 
- `wrapper.java.additional.28 = -DXAP_GSC_OPTIONS=${xap_gsc_options}` 
- `wrapper.java.additional.29 = -DXAP_GSM_OPTIONS=${xap_gsm_options}` 
- `wrapper.java.additional.30 = -DXAP_LUS_OPTIONS=${xap_lus_options}`

# New Location for xap.datagrid.jar File

As of version 12.2, the xap.datagrid.jar file was moved to **<XAP install dir>/lib/required**. When upgrading from an earlier version, verify that there isn't an additional copy of this file in the old location **<XAP install dir>/lib/optional/pu-common**. Having multiple copies of this file will result in a failure to instantiate data when the data grid is started, and an exception will be thrown. 

# Maven Artifacts

If you are upgrading from an earlier version to any 12.x version, review the page on [Maven artifacts](../started/maven-artifacts.html) in the Getting Started section to ensure that the client application will continue to behave as ex