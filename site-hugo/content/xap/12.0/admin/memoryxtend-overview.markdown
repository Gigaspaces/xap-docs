---
type: post120
title:  MemoryXtend
categories: XAP120ADM, ENT
parent: none
weight: 430
canonical: auto
---


XAP 10 introduced a new storage model called BlobStore Storage Model (commonly reffered to as MemoryXtend), which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model that and its off-heap RAM and SSD implementations.

 
 <br>


{{%fpanel%}}

[Overview](./memoryxtend.html)<br>
The BlobStore Storage Model allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data.

[RocksDB Add-On](./memoryxtend-rocksdb-ssd.html)<br>
XAP provides a MemoryXtend add-on based on RocksDB, which is the recommended choice for hybrid RAM-SSD clusters.

 
{{%/fpanel%}}

<br>

#### Additional Resources

{{%section%}}
{{%column width="30%"  %}}
{{%youtube "kAe-ZxFnIYc" "XAP MemoryXtend" %}}
{{%/column%}}

{{%column width="30%"  %}}
{{%pdf "/download_files/White-Paper-ssd-V2.pdf" %}}
This MemoryXtend white paper provides a high level overview of the technology and motivation behind MemoryXtend.
{{%/column%}}

{{%column width="30%"  %}}

{{%download "http://www.gigaspaces.com/xap-memoryxtend-flash-performance-big-data"%}}  Download MemoryXtend


{{%/column%}}

{{%/section%}}

