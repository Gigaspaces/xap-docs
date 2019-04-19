---
type: post123
title:  Indexing
categories: XAP123NET, PRM
parent: none
weight: 700
canonical: auto
---

When a Space is looking for a match for a read or take operation, it iterates over non-null values in the template, looking for matches in the Space. This process can be time consuming, especially when there are many potential matches. To improve performance, it is possible to index one or more properties. The Space maintains additional data for indexed properties, which shortens the time required to determine a match, thus improving performance.

# Choosing which Properties to Index

One might wonder why properties are not always indexed, or why all the properties in all the classes are not always indexed. The reason is that indexing has its downsides as well:

- An indexed property can speed up read/take operations, but might also slow down write/update operations.
- An indexed property consumes more resources, specifically memory footprint per entry.

# When to Use Indexing

Naturally the question arises of when to use indexing. Usually it is recommended to index properties that are used in common queries. However, in some scenarios one might favor less footprint, or faster performance for a specific query, and adding/removing an index should be considered.

{{% warning "Important"%}}  Keep in mind that "Premature optimization is the root of all evil". It is always recommended to benchmark your code to get better results. {{%/warning%}}

# Indexing at Runtime

Indexes can be added dynamically at run-time using the GigaSpaces Management Center.

{{% note "Note"%}}
Removing an index or changing an index type is currently not supported.
{{%/note%}}
