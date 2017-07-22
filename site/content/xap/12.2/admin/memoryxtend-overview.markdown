---
type: post122
title:  MemoryXtend
categories: XAP122ADM, ENT
parent: none
weight: 430
---


XAP 10 introduced a new storage model called BlobStore Storage Model (commonly reffered to as MemoryXtend), which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model and its off-heap RAM and SSD implementations.

 
 <br>


{{%fpanel%}}

[Overview](./memoryxtend.html)<br>
The BlobStore storage model allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data.

[MemoryXtend for Flash/SSD](./memoryxtend-rocksdb-ssd.html)<br>
XAP provides a multi-tiered data storage architecture embedding RocksDB, which is the recommended choice for hybrid RAM-SSD clusters.

[MemoryXtend for Off-Heap RAM](./memoryxtend-ohr.html)<br>
XAP provides a MemoryXtend configuration for tiering JVM heap with direct memory buffers.

 
{{%/fpanel%}}

<br>

