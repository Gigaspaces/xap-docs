---
type: postrel
title:  Backward compatibility
categories:  RELEASE_NOTES
parent: xap97.html
weight: 600
---



## Overview
Backward Compatibility in GigaSpaces
XAP 9.7 is a minor release which includes new features and improvements, including the Change API, Custom Eviction Policy and more.

We try to maintain backward compatibility wherever possible. However, in some cases, no backward compatibility is provided.

{{%note%}}
As a best practice, when upgrading to GigaSpaces 9.7, unzip the latest version of GigaSpaces 9.7 and port the necessary changes from your pre-9.7 environment into your new 9.7 environment. It is recommended to use the GigaSpaces 9.7 distribution as-is. Please refer to the  Upgrade Guide for more details.
{{%/note%}}

{{%note "The following elements affect backward compatibility:"%}}

- GigaSpaces API and Configuration
- GigaSpaces XAP binary compatibility (including GigaSpaces management tools)
- Client-Server
- Server-Server
{{%/note%}}

**Notes:**

- Please refer to the  [public GigaSpaces' product deprecation and End-of-Life[(http://www.gigaspaces.com/EOL) policy for more details.
- Disclaimer - A patch or minor release might demand a compatibility break in extreme cases. If it is required, this will be made very clear in the the release notes.


The below table describes backwards compatibility support in XAP 9.7:

## OpenSpaces


|Delivery|XAP API/Configuration|XAP Binaries|Client-Server|Server-Server|
|:----|:--------|:----------------|:---------------|:------------------|
|Patch  | YES| YES |   YES   |  YES |
|Service Pack (9.7.1, ...) | YES| YES | YES|   YES|
|Major Version (8.0, 9.0 ...)| YES(see note on deprecation policy below) | YES|YES| NO     |

* The following is supported:

- Applications built using 8.0.x or 9.0.x run without any code changes on a clean 9.7 installation.
- Mixing clients and Space servers from different XAP Major Releases:Clients running on 8.0 or 9.0 can run against 9.7 servers. 8.0, 9.0 servers cannot be part of the same cluster with 9.7 servers.




