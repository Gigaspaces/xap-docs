---
type: post110
title:  License Key
categories: XAP110
parent: installation.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}


When downloading   XAP from the [GigaSpaces website](http://www.gigaspaces.com/LatestProductVersion) the installation already has a bundled license key for a limited period of time, therefore for the evaluation period, **there is no need to install any license key**.

# License Key File Validation

The XAP license key is loaded once the XAP Container or a Space instance is started. To allow the XAP runtime to load a new license key, you should recycle the process (JVM) to allow it to load the new license key. If a license key expires, GigaSpaces will throw license key validation exception the next time the GigaSpaces Container or a space instance will be started and abort the space or container initialization. It will not throw license key validation exception while the system is running.

# Extending the License

If you need to extend your license key or change it, please edit the `<XAP Root>\gslicense.xml` file and copy the license key provided to the `<licensekey>` tag value.

{{% tip %}}
The license key file should be placed on each machine running a GSA/GSM/GSC.
{{% /tip %}}

The activation license key is in the following form:

{{%panel%}}
"Nov 16, 2020~user@XXXXXXXXXXXXXXXXXXXXX#PREMIUM^{{%version xap-version%}}XAPPremium%UNBOUND+UNLIMITED"
{{%/panel%}}

# Installing new License

To install the license, insert the license string between the license key tags:


```xml
<com>
  <j_spaces>
        <kernel>
          <licensekey>Nov 16, 2020~user@XXXXXXX#PREMIUM^{{%version xap-version%}}XAPPremium%UNBOUND+UNLIMITED</licensekey>
       </kernel>
  </j_spaces>
</com>
```

# License Key File Search path

XAP search for the license key using the following search path:

1. JVM Classpath.
1. Current directory
1. `com.gs.home` system property.
1. Using the `com.gs.licensekey` system property. Example:


```console
export EXT_JAVA_OPTIONS=-Dcom.gs.licensekey=Nov 16, 2020~user@XXXXXXX#PREMIUM^{{%version xap-version%}}XAPPremium%UNBOUND+UNLIMITED
./gs-agent.sh &
```

