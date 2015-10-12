---
type: post110adm
title:  MemoryXtend
categories: XAP110ADM
parent: none
weight: 430
---



{{% bannerleft "/attachment_files/subject/flash-imdg.png" %}}

XAP 10 introduced a new storage model called BlobStore Storage Model (commonly reffered to as MemoryXtend), which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model that and its off-heap RAM and SSD implementations.
{{% /bannerleft %}}


<br>

{{% info title="Licensing "%}}
MemoryXtend requires a separate license in addition to the GigaSpaces commercial license. Please contact [GigaSpaces Customer Support](http://www.gigaspaces.com/content/customer-support-services) for more details.
{{% /info %}}

<br>


{{%fpanel%}}

[Overview](./memoryxtend-overview.html)<br>
The BlobStore Storage Model allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data.

[RocksDB](./memoryxtend-rocksdb-ssd.html)<br>
XAP is using [RocksDB](http://rocksdb.org/), an embeddable persistent key-value store for fast storage.

[ZetaScale](./memoryxtend-ssd.html)<br>
All Enterprise flash drives are supported. SanDisk, Fusion-IO, IntelÂ® SSD , etc are supported with the IMDG storage technology.

[MapDB - Off Heap RAM](./memoryxtend-ohr.html)<br>
XAP is using [MapDB](http://www.mapdb.org/), an embedded database engine which provides concurrent Maps, Sets and Queues backed by disk storage or off-heap memory.

{{%/fpanel%}}

<br>

#### Additional Resources

{{%section%}}
{{%column width="30%"  %}}
{{%youtube "kAe-ZxFnIYc"  "XAP MemoryXtend"%}}
{{%/column%}}

{{%column width="30%"  %}}
{{%pdf "/download_files/White-Paper-ssd-V2.pdf" %}}
This MemoryXtend white paper provides a high level overview of the technology and motivation behind MemoryXtend.
{{%/column%}}

{{%column width="30%"  %}}

{{%/column%}}

{{%/section%}}

