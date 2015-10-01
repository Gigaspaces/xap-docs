---
type: post110net
title:  License Key
categories: XAP110NET
parent: installation-overview.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}


When downloading GigaSpaces XAP from the [GigaSpaces website](http://www.gigaspaces.com/LatestProductVersion) the installation already has a bundled license key for a limited period of time, therefore for the evaluation period, **there is no need to install any license key**.

# License Key File Validation

The GigaSpaces license key is loaded once the GigaSpaces Container or a space instance is started. To allow the GigaSpaces runtime to load a new license key, you should recycle the process (JVM) to allow it to load the new license key. If a license key expires, GigaSpaces will throw license key validation exception the next time the GigaSpaces Container or a space instance will be started and abort the space or container initialization. It will not throw license key validation exception while the system is running.

# Extending the License

If you need to extend your license key or change it, please edit the `<GigaSpaces Root>\gslicense.xml` file and copy the license key provided to the `<licensekey>` tag value.

{{% tip %}}
The license key file should be placed on each machine running a GSA/GSM/GSC.
{{% /tip %}}

The activation license key is in the following form:

    "Nov 16, 2020~user@XXXXXXXXXXXXXXXXXXXXX#PREMIUM^9.7XAPPremium%UNBOUND+UNLIMITED"

# Installing new License

To install the license, insert the license string between the license key tags:


```xml
<com>
  <j_spaces>
        <kernel>
          <licensekey>Nov 16, 2020~user@XXXXXXX#PREMIUM^9.7XAPPremium%UNBOUND+UNLIMITED</licensekey>
       </kernel>
  </j_spaces>
</com>
```



