---
type: post101
title:  Flash drive IMDG Storage
categories: XAP101TUT
parent: none
weight: 1600
---

XAP 10 introduces a new storage model called BlobStore Storage Model, which allows an external storage medium (one that does not reside on the JVM heap) to store the IMDG data. This guide describes the general architecture and functionality of this storage model that is leveraging both on-heap, off-heap and SSD implementation, called `MemoryXtend`.

<br>


{{%section%}}
{{%column width="30%"  %}}
{{%youtube "kAe-ZxFnIYc"  "XAP MemoryXtend"%}}
{{%/column%}}

{{%column width="30%"  %}}
{{%pdf "/download_files/White-Paper-ssd-V2.pdf"%}}
This MemoryXtend white paper provides a high level overview of the technology and motivation behind MemoryXtend.
{{%/column%}}

{{%column width="30%"  %}}
{{%pdf "/download_files/xap10–memoryXtend-tutorial.pdf"%}}
The MemoryXtend Tutorial describes how to experiment with MemoryXtend and comparing RAM based Data Grid with SSD based Data Grid.
{{%/column%}}
{{%/section%}}

<br>
{{%learn "/xap101adm/memoryxtend.html"%}}

