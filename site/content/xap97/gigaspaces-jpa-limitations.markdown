---
type: post97
title:  Limitations
categories: XAP97
parent: jpa-api.html
weight: 300
---

{{% ssummary %}}Unsupported Features Of GigaSpaces JPA{{% /ssummary %}}

# Overview

This page lists JPA features/functionalities that are unsupported by GigaSpaces JPA.

# Annotations

Here's a list of JPA annotations which are not supported by GigaSpaces JPA:

{{% section %}}
{{% column %}}
Access(AccessType.FIELD)
JoinColumn
Version
UniqueConstraint
Temporal
AssociationOverride
AttributeOverride
ColumnResult
DiscriminatorColumn
DiscriminatorValue
EntityResult
FieldResult
Inheritance
JoinTable
Lob
ManyToMany
MapKey
{{% /column %}}

{{% column %}}
NamedNativeQuery
NamedQuery
OrderBy
OrderColumn
PrimaryKeyJoinColumn
SecondaryTable
SequenceGenerator
SqlResultSetMapping
Table
TableGenerator
QueryHint
{{% /column %}}
{{% /section %}}

# JPQL

#### Unsupported JPQL keywords/expressions

- NOT.
- BETWEEN (Supported in 8.0.1).
- LIKE (Supported in 8.0.1).
- IS EMPTY.
- EXISTS.
- ANY/ALL.
- HAVING.
- IS NULL.
- String functions (CONCAT, SUBSTRING, TRIM, LENGTH, LOCATE, LOWER, UPPER)
- Arithmetic functions (ABS, SQRT, MOD, SIZE)
- Datetime functions (CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP)

#### Joins

- Outer Joins.
- Fetch Joins.
- Inner Joins are supported but the selected object must be the owning object of the relationship (this also applies to projections).

# Other/misc.

- Native queries (Supported in 8.0.1).
- Primitive types support due to inconsistency between how GigaSpaces treats primitives to how JPA treats them.
- entityManager.refresh() for classes which has properties in a relation (Supported in 8.0.1).
- orm.xml is partially supported. The recommended way to model your data is by using Annotations.
- Lazy loading is not supported and therefore entities are always Eagerly loaded.
- Optimistic locking is not supported and therefore the LockManager property should always be set to "none" in persistence.xml (Supported in 8.0.1):


```xml
  <property name="LockManager" value="none"/>
```

