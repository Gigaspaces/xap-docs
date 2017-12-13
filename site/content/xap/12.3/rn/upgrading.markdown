---
type: post123
title:  Upgrading from a Previous Version
categories: XAP123RN
parent: none
weight: 600
---

If you are currently using a previous version of XAP or InsightEdge, contact <support@gigaspaces.com> for information on how to upgrade to the most recent [version](https://www.gigaspaces.com/download-center).
 
# Backwards Compatibility

This version is a major release that includes [new features and improvements.](./whats-new.html)

As a best practice, when upgrading to the latest version, unzip the downloaded software package and port the necessary changes from your previous environment into your new environment. It is recommended to use the new distribution as-is. 

The following table describes backwards compatibility support in this version:


|Delivery| API/Configuration| Binaries|Client-Server|Server-Server|
|:-------------------------------|:-------------------------------------------|:----|:----|:----|
| Service Pack / Patch (12.3.x)  | YES                                        | YES | YES | YES |
| Major Version (12.3.0 or later) | YES (see note on deprecation policy below) | NO  | YES | NO  |

{{%exclamation%}} In extreme cases, a service pack or a patch might break backwards compatibility. In such cases, it will be noted in the the release notes.

Please refer to the [Deprecation and End-of-Life policy](http://www.gigaspaces.com/EOL) for more details.

