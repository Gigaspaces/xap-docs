---
type: post120
title:  License Key
categories: XAP120,PRM
parent: installation.html
weight: 110
---

{{% ssummary %}}{{% /ssummary %}}

GigaSpaces XAP Premium and Enterprise editions require a valid license to start. This page explains how to setup the initial evaluation license and provides additional related information.

# Getting started

When downloading XAP Premium or Enterprise from the [XAP Download Center](http://www.gigaspaces.com/LatestProductVersion), you'll get an email with a license key for the evaluation period. To start your evaluation, simply edit the license file located in `<XAP_HOME>/xap-license.txt` with any text editor, and copy the license key from the email to a blank line in the file. For example:

```xml
# License can also be set via the system property com.gs.licensekey
Version=12;Type=EVALUATION;Customer=yourname@yourcompany.com;Expiration=2016-Dec-31;Hash=NP6SQOOdOZrPRcN9OTYQ
```

{{% info %}}
If you're using more than one machine for the evaluation, make sure you copy the license to each of those machines.
{{%/info%}}

That's it! You're good to go!

{{% tip "Viewing the license in the logs" %}}
When the system starts, it writes an information summary to the logs and console, including the license key. You can use that to verify that the system is using the correct license key.
{{% /tip %}}

# License Validation

XAP validates the license whenever the Service Grid is started. If the license is invalid (e.g. expired), the system will report there's a license problem and terminate. If the license expires while the system is running, it is not terminated, but if one of the system's components fails and tries to restart, it will fail because the license has expired.

# License Lookup Order

The system looks for the license key in the following order:

1. The `com.gs.licensekey` system property.
1. A `xap-license.txt` file in the Classpath.
1. A `xap-license.txt` file in XAP installation folder (can be explicitly set using `com.gs.home` system property)
1. A `xap-license.txt` file in the current directory.
