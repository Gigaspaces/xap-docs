---
type: post110adm
title:  Serialization and Thread Pool
categories: XAP110ADM
parent: working-with-spaces-gigaspaces-browser.html
weight: 400
---



The Performance tab contains settings related to serialization of Entries, implicit indexing of Entries by fields, and the number of threads used by the GigaSpaces engine.

![Space SerializationGigaSpaces Browser.jpg](/attachment_files/Space SerializationGigaSpaces Browser.jpg)

**Number of Implicit Indexes** -- specifies the maximum number of indexes that are created automatically if the entry does not explicitly specify fields to be indexed.
See details about indexing in the Space Configuration section.
Extended Indexing Enabled -- When the Extended Indexing is turned on, a btree index is maintained for all space indexed class attributes. For more details see the [Indexing]({{%currentjavaurl%}}/indexing.html) section.

