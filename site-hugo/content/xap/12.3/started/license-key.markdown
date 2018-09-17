---
type: post123
title:  Product License
categories: XAP123GS
parent: none
weight: 200
---

 

If you are using the XAP or InsightEdge open source editions, you don't need a product license. Simply download and unzip the software, and begin by exploring the examples or building your own application.

The XAP Premium, XAP Enterprise, and InsightEdge Enterprise editions each require a valid license to start. This section explains how to set up the initial 30-day evaluation license, and also provides additional related information.

# Applying the License

After you download an InsightEdge or XAP commercial edition from the {{%exurl "Download Center" "http://www.gigaspaces.com/LatestProductVersion"%}}, you will receive an email with a license key for the evaluation period. To start the evaluation period, open the license file located in `<XAP_HOME>/xap-license.txt` with any text editor, and copy the license key from the email to a blank line in the file. For example:

```bash
# License can also be set via the XAP_LICENSE environment variable or com.gs.licensekey system property
Product=InsightEdge;Version={{%version xap-version%}};Type=ENTERPRISE;Customer=yourname@yourcompany.com;Expiration=2017-Sep-30;Hash=PNXrPIPANOOddPNQFdQQ
```

If you're using [Docker images](https://hub.docker.com/u/gigaspaces/) for evaluation, add the `-e XAP_LICENSE=...` option to the Docker run command, using the license key in the email.

The same evaluation license is used for both InsightEdge and XAP downloaders, enabling you to evaluate the full InsightEdge Enterprise edition, which contains all of the available features for all commercial editions.

{{% note "Note"%}}
If you are using XAP.NET, the `xap-license.txt` file is located at `<XAP_HOME>\Runtime\xap-license.txt`.<br>
If you are using more than one machine for your evaluation, you must apply the license to each machine.
{{%/note%}}

After you have applied the license, you can access all of the available features for the duration of the 30-day evaluation period without any limitations. When the evaluation license expires, the next time you restart the data grid you will only have access to the open-source functionality. Contact GigaSpaces support to purchase a license for the edition that meets your needs. After the new license is applied, the relevant functionality will once again be available.

{{% note "Note"%}}
The license key can be viewed in the application logs. When the system starts, it writes an information summary to the logs and console, including the license key. You can use the logs to verify that you are using the correct license key.
{{% /note %}}

# License Validation on Start-Up

The license is validated whenever the service grid is started. If the license is invalid for some reason (for example, if it is expired), the system will report a problem with the license and terminate. If the license expires while the system is up and running, it will continue to run. However, if a system components fails and tries to restart, it will fail because the license is not valid.

# License Lookup Order

The system looks for the license key in the following locations, in the following order:

1. The `com.gs.licensekey` system property.
1. The `XAP_LICENSE` environment variable.
1. A `xap-license.txt` file in the Classpath.
1. A `xap-license.txt` file in XAP installation folder (can be explicitly set using `com.gs.home` system property)
1. A `xap-license.txt` file in the current directory.
