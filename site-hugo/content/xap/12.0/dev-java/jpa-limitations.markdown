---
type: post120
title:  Limitations
categories: XAP120
parent: jpa-api-overview.html
weight: 400
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}



Functionalities that are not supported by the XAP JPA API.

# Annotations

#### Here's a list of JPA annotations which are not supported by XAP JPA

- Access(AccessType.FIELD)
- JoinColumn
- Version
- UniqueConstraint
- Temporal
- AssociationOverride
- AttributeOverride
- ColumnResult
- DiscriminatorColumn
- DiscriminatorValue
- EntityResult
- FieldResult
- Inheritance
- JoinTable
- Lob
- ManyToMany
- MapKey
- NamedNativeQuery
- NamedQuery
- OrderBy
- OrderColumn
- PrimaryKeyJoinColumn
- SecondaryTable
- SequenceGenerator
- SqlResultSetMapping
- Table
- TableGenerator
- QueryHint


# JPQL

#### Unsupported JPQL keywords/expressions

- NOT.
- BETWEEN 
- LIKE 
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

# Other/misc

- Native queries. 
- Primitive types support due to inconsistency between how GigaSpaces treats primitives to how JPA treats them.
- entityManager.refresh() for classes which has properties in a relation.
- orm.xml is partially supported. The recommended way to model your data is by using Annotations.
- Lazy loading is not supported and therefore entities are always Eagerly loaded.
- Optimistic locking is not supported and therefore the LockManager property should always be set to "none" in persistence.xml:


```xml
  <property name="LockManager" value="none"/>
```

