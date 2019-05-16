---
type: post102
title:  FIFO Ordering
categories: XAP102
parent: event-processing.html
weight: 600
canonical: auto
---


Supporting FIFO (First In, First Out) behavior for Entries is a critical requirement when building messaging systems or implementing master-worker patterns. Users should be able to get Entries in the same order in which they were written. GigaSpaces supports both non-ordered Entries and FIFO ordered Entries when performing space operations.

<br>

{{%fpanel%}}

[FIFO Support](./fifo-support.html){{<wbr>}}
How to get entries in the same order in which they were written to the space.

[FIFO Grouping](./fifo-grouping.html){{<wbr>}}
How to read/take a group of space entries with a common property value, in FIFO order (by order of insertion), without having to maintain a FIFO order for all the entries in the space.
{{%/fpanel%}}

