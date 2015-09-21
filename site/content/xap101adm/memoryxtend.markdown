---
type: post101
title:  MemoryXtend
categories: XAP101ADM
parent: none
weight: 430
---

<br>

{{% section %}}
{{% column  width="10%" %}}
![flash-imdg.png](/attachment_files/subject/flash-imdg.png)
{{% /column %}}
{{%column width="90%" %}}
XAP 10 introduced a new storage model called BlobStore Storage Model (commonly reffered to as MemoryXtend), which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model that and its off-heap RAM and SSD implementations.
{{% /column %}}
{{% /section %}}

<br>

{{% info title="Licensing "%}}
MemoryXtend requires a separate license in addition to the GigaSpaces commercial license. Please contact [GigaSpaces Customer Support](http://www.gigaspaces.com/content/customer-support-services) for more details.
{{% /info %}}

<br>


{{%fpanel%}}

[Overview](./memoryxtend-overview.html)<br>
The BlobStore Storage Model allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data.

[Solid State Drive](./memoryxtend-ssd.html)<br>
All Enterprise flash drives are supported. SanDisk, Fusion-IO, IntelÂ® SSD , etc are supported with the IMDG storage technology.

[Off Heap RAM](./memoryxtend-ohr.html)<br>
XAP is using [MapDB](http://www.mapdb.org/), an embedded database engine which provides concurrent Maps, Sets and Queues backed by disk storage or off-heap memory.

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

{{%download "http://www.gigaspaces.com/xap-memoryxtend-flash-performance-big-data"%}}  Download MemoryXtend


{{%/column%}}

{{%/section%}}

