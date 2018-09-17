---
type: post
title:  Backward compatibility
categories:  RELEASE_NOTES
parent: xap100.html
weight: 600
---



XAP 10.0 is a major release which includes [new features and improvements.](./100whats-new.html)

We try to maintain backward compatibility wherever possible. However, in some cases, no backward compatibility is provided.

{{%note "There is a backwards compatibility issue that was found in 10.0:"%}}
Issue number is GS-11936. For more information see [known issues](./100known-issues.html).

This issue was fixed in service pack [10.0.1](http://www.gigaspaces.com/xap-download).
If your deployment requires backwards compatibility between client and server please download this service pack.
{{%/note%}}

{{%note%}}
As a best practice, when upgrading to XAP 10.0, unzip the latest version of XAP 10.0 and port the necessary changes from your pre-10.0 environment into your new 10.0 environment. It is recommended to use the XAP 10.0 distribution as-is. Please refer to the  Upgrade Guide for more details.
{{%/note%}}


{{%note "The following elements affect backward compatibility:"%}}

- XAP API and Configuration
- XAP XAP binary compatibility (including XAP management tools)
- Client-Server
- Server-Server
{{%/note%}}

**Notes:**

- Please refer to the  [public GigaSpaces' product deprecation and End-of-Life](http://www.gigaspaces.com/EOL) policy for more details.
- Disclaimer - A patch or minor release might demand a compatibility break in extreme cases. If it is required, this will be made very clear in the the release notes.


The below table describes backwards compatibility support in XAP 10.0:

## OpenSpaces


|Delivery|XAP API/Configuration|XAP Binaries|Client-Server|Server-Server|
|:----|:--------|:----------------|:---------------|:------------------|
|Patch  | YES| YES |   YES   |  YES |
|Service Pack (9.7.1, ...) | YES| YES | YES|   YES|
|Major Version (8.0, 9.0 ...)| YES(see note on deprecation policy below) | YES|YES| NO     |

* The following is supported:

- Applications built using 8.0.x or 9.0.x run without any code changes on a clean 10.0 installation.
- Mixing clients and Space servers from different XAP Major Releases:Clients running on 8.0 or 9.0 can run against 10.0 servers. 8.0, 9.0 servers cannot be part of the same cluster with 10.0 servers.




