---
type: post123
title:  Indexing
categories: XAP123, OSS
parent: none
weight: 900
---





When a Space is looking for a match for a read or take operation, it iterates over non-null values in the template, looking for matches in the Space. This process can be time consuming, especially when there are many potential matches. To improve performance, it is possible to index one or more properties. The Space maintains additional data for indexed properties, which shortens the time required to determine a match, thus improving performance.

# Choosing which Properties to Index

One might wonder why properties are not always indexed, or why all the properties in all the classes are not always indexed. The reason is that indexing has its downsides as well:

- An indexed property can speed up read/take operations, but might also slow down write/update operations.
- An indexed property consumes more resources, specifically memory footprint per entry.

# When to Use Indexing

Naturally the question arises of when to use indexing. Usually it is recommended to index properties that are used in common queries. However, in some scenarios one might favor less footprint, or faster performance for a specific query, and adding/removing an index should be considered.

{{% warning "Warning"%}} Keep in mind that "premature optimization is the root of all evil". It is always recommended to benchmark your code to get better results. {{%/warning%}}

# Indexing at Runtime


Indexes can be added dynamically with the `GigaSpaceTypeManager` interface.
 
{{%refer%}}
Refer to [Data Type Metadata](./the-space-meta-data.html#modifying-existing-classes) for more information.
{{%/refer%}}

# Performance Tips

Properties that are not indexed and not used for queries can be grouped within a user-defined class (also known as payload class). This improves the read/write performance because these properties aren't introduced to the Space class model.

# Deprecated Indexing Options

## Implicit Indexing

If no properties are indexed explicitly, the Space implicitly indexes the first **n** properties (in alphabetical order), where **n** is determined by the `number-implicit-indexes` property in the Space schema.

{{% note "Note"%}} Using this feature is not recommended, because adding/removing properties can have unexpected side effects. It is deprecated, and might be removed in future versions.{{%/note%}}

# Query Execution Flow

When a `read`, `take`, `readMultiple`, or `takeMultiple` call is performed, a template is used to locate matching Space objects. The template may have multiple field values - some may include values and some may not (i.e. `null` field values acting as wildcard). The fields that do not include values are ignored during the matching process. In addition, some class fields may be indexed and some may not be indexed.

When multiple class fields are indexed, the Space looks for the field value index that includes the smallest amount of matching Space objects with the corresponding template field value as the index key.

The smallest set of Space objects is the list of objects to perform the matching against (matching candidates). After the candidate Space object list has been constructed, it is scanned to locate Space objects that fully match the given template - i.e. all non-null template fields match the corresponding Space object fields.

{{% note "Note"%}} 
Class fields that are not indexed are not used to construct the candidate list. 
{{%/note%}}
