---
type: postsbp
title:  Hot deploy
categories: SBP
weight: 2100
parent: production.html
---

{{% mute %}}This tool allows business logic to be refreshed without any system downtime and data loss.{{% /mute %}}

{{% panel %}}
{{%section%}}
{{%column width="15%" %}}
**XAP Version**<br>
**Last Updated**<br>
**Reference**<br>
**Example**
{{%/column%}}
{{%column  width="50%" %}}
10.0<br>
November 2014<br>
<br>
{{%git "https://github.com/fe2s/xap-hot-deploy" %}}
{{%/column%}}
{{%column  width="10%" %}}
{{%align right%}}
**Author**
{{%/align%}}
{{%/column%}}
{{%column  width="25%" %}}
{{%align center%}}
Anna Babich - Anna_Babich@epam.com <br>
Pavlo Romanenko - Pavlo_Romanenko@epam.com <br>
{{%/align%}}
{{%/column%}}
{{%/section%}}
{{% /panel %}}


{{%ssummary%}}{{%/ssummary%}}

This tool allows business logic running as a PU to be refreshed (rolling PU upgrade) without any system downtime and data loss. The tool using the hot deploy approach , placing new PU code on the GSM PU deploy folder and later restart each PU instance. See XAP Hot Deploy documentation for details.

To refresh the PU code the tool restarts all processing units for a given PU. Old deployment files for the specified PU will be moved into a temporary folder to be used in case the upgrade fails. New PU files will be copied to the XAP deploy folder prior the restart phase. The tool identifies all running PU instances and restart them once by one. Once the process is completed both primary and backup PU instances will run updated logic.

# Stateful PU Restart

1. The tool discovers all processing unit instances and identifies their Space mode.
2. All backups are restarted (each instance in a separate thread).
3. All primaries are restarted. If the `double_restart` option is enabled, primaries are restarted twice to 
