---
type: postsbp
title: Appendix
categories: SBP
weight: 600
parent: spring-data.html
---


{{%ssummary%}}{{%/ssummary%}}



{{%anchor appendix-a%}}

# Supported Query Keywords

The following table lists the supported keywords by the Spring Data XAP repository query derivation mechanism:


|Logical keyword | Keyword expressions|
----- | -----|
AND | And
OR | Or
AFTER | After, IsAfter
BEFORE | Before, IsBefore
CONTAINING | Containing, IsContaining, Contains
BETWEEN | Between, IsBetween
ENDING_WITH | EndingWith, IsEndingWith, EndsWith
FALSE | False, IsFalse
GREATER_THAN | GreaterThan, IsGreaterThan
GREATER_THAN_EQUALS | GreaterThanEqual, IsGreaterThanEqual
IN | In, IsIn
IS | Is, Equals, (or no keyword)
IS_NOT_NULL | NotNull, IsNotNull
IS_NULL | Null, IsNull
LESS_THAN | LessThan, IsLessThan
LESS_THAN_EQUAL | LessThanEqual, IsLessThanEqual
LIKE | Like, IsLike
NOT | Not, IsNot
NOT_IN | NotIn, IsNotIn
NOT_LIKE | NotLike, IsNotLike
REGEX | Regex, MatchesRegex, Matches
STARTING_WITH | StartingWith, IsStartingWith, StartsWith
TRUE | True, IsTrue

Keywords are not supported in XAP Repositories:


Logical keyword | Keyword expressions|
-_-- | ----|
EXISTS | Exists
NEAR | Near, IsNear
WITHIN | Within, IsWithin

{{%anchor appendix-b%}}

# Supported Querydsl Methods

`Predicate` methods are supported by Spring Data XAP to build up Querydsl queries:<br>

* number comparison: `eq`, `ne`, `lt`, `loe`, `goe`, `between`, `notBetween`    <br>
* string comparison: `like`, `matches`, `isEmpty`, `isNotEmpty`, `contains`, `containsIgnoreCase`, `endsWith`, `endsWithIgnoreCase`, `startsWith`, `startsWithIgnoreCase`  <br>
* other comparison: `isNull`, `isNotNull`, `in`, `notIn` <br>
* complex queries: `and`, `or`  <br>
* embedded fields

{{%note%}}
Note that `contains`, `startsWith`, `endWith` and their `...IgnoreCase` equivalents use the `Regular Expression` matches
{{%/note%}}

{{%anchor appendix-c%}}

# Supported Change API methods

`Change API` methods are available while using Querydsl syntax (`QChangeSet` class):

* field: `set`, `unset`
* numeric: `increment`, `decrement`
* collections and maps: `addToCollection`, `addAllToCollection`, `removeFromCollection`, `putInMap`, `removeFromMap`
* lease: `lease`
* custom change: `custom`

{{%anchor appendix-d%}}

# Unsupported operations

Although we try to support each and every Spring Data feature, sometimes native implementation is not possible using Space as a data source. Instead of providing workarounds, which are often slow, we decided to mark some features as unsupported, among them are:

#### Using `IgnoreCase`, `Exists`, `IsNear` and `IsWithin` keywords


```java
public interface PersonRepository extends XapRepository<Person, String> {

    // these methods throw an UnsupportedOperationException when called

    List<Person> findByNameIgnoreCase(String name);

    List<Person> findByNameExists(boolean exists);

    List<Person> findByAgeIsNear(Integer nearAge);

    List<Person> findByAgeIsWithin(Integer minAge, Integer maxAge);

}
```

#### Setting `Sort` to `ignoreCase`


```java
public interface PersonRepository extends XapRepository<Person, String> {

    // these methods throw an UnsupportedOperationException when called

    List<Person> findByNameIgnoreCase(String name);

    List<Person> findByNameExists(boolean exists);

    List<Person> findByAgeIsNear(Integer nearAge);

    List<Person> findByAgeIsWithin(Integer minAge, Integer maxAge);

}
```

#### Setting any `NullHandling` in `Sort` other than `NATIVE`


```java
// NullHandling other than NATIVE is not supported
Sort sorting = new Sort(new Order(ASC, "id", NullHandling.NULLS_FIRST));
// will throw an UnsupportedOperationException
personRepository.findByNameEquals("paul", new PageRequest(1, 2, sorting));
```

#### Using query derivation in `XapDocumentRepository`


```java
@SpaceDocumentName("Person")
public interface DocumentQueries
        extends XapDocumentRepository<SpaceDocument, String> {

    @Query("name = ?")
    List<SpaceDocument> findByName(String name);

    // this declaration without @Query annotation
    // or named query from external resource
    // will throw UnsupportedOperationException during configuration
    List<SpaceDocument> findByAge(Integer age);

}
```
