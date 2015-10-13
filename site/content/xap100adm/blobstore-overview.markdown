---
type: post100
title:  Flash drive IMDG Storage - MemoryXtend for SSD
categories: XAP100ADM
parent: memory-management-overview.html
weight: 400
---

<br>

{{% section %}}
{{% column  width="10%" %}}
![flash-imdg.png](/attachment_files/subject/flash-imdg.png)
{{% /column %}}
{{%column width="90%" %}}
XAP 10 introduces a new storage model called BlobStore Storage Model, which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model that is leveraging both on-heap, off-heap and SSD implementation, called `MemoryXtend`.
{{% /column %}}
{{% /section %}}

<br>

{{%fpanel%}}

[BlobStore Storage Model Overview](./blobstore-cache-policy.html)<br>
Overview and introduction to MemoryXtend.

[Advanced Tuning Guide](./blobstore-tuning-guide.html)<br>
Tuning options for MemoryXtend.

[Troubleshooting](./blobstore-trouble-shooting.html)<br>
How to troubleshoot common problems.
{{%/fpanel%}}

<br>

#### Additional Resources

{{%section%}}
{{%column width="30%"  %}}
{{%youtube "kAe-ZxFnIYc"  "XAP MemoryXtend"%}}
{{%/column%}}

{{%column width="30%"  %}}
[{{%pdf "/download_files/White-Paper-ssd-V2.pdf"%}}
This MemoryXtend white paper provides a high level overview of the technology and motivation behind MemoryXtend.
{{%/column%}}

{{%column width="30%"  %}}
[{{%pdf "/download_files/XAP10–MemoryXtend Tutorial.pdf"%}}
The MemoryXtend Tutorial describes how to experiment with MemoryXtend and comparing RAM based Data Grid with SSD based Data Grid.
{{%/column%}}
{{%/section%}}

