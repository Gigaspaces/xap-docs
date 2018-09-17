---
type: post122
title:  Predicate Based Queries
categories: XAP122, OSS
parent: scala.html
weight: 300
---


{{% ssummary  %}}{{% /ssummary %}}

Support for predicate based queries on the `GigaSpace` proxy has been in added. This support is based on the new macros feature introduced in Scala 2.10.  Each predicate based query is transformed during compilation into an equivalent [SQLQuery](./query-sql.html).


# Usage

To use predicate based queries, import `import org.openspaces.scala.core.ScalaGigaSpacesImplicits._` into scope. Then call the `predicate` method on the `GigaSpace` instance as demonstrated:


```scala
/* Import GigaSpace implicits into scope */
import org.openspaces.scala.core.ScalaGigaSpacesImplicits._

/* implicit conversion on gigaspace returns a wrapper class with predicate based query methods */
val predicateGigaSpace = gigaSpace.predicate
```

# Supported Queries

## Example data class

For the purpose of demonstration, we will use the following example data class


```scala
case class Person @SpaceClassConstructor() (

  @BeanProperty
  @SpaceId
  id: String = null,

  @BeanProperty
  name: String = null,

  @BeanProperty
  @SpaceProperty(nullValue = "-1")
  @SpaceIndex(`type` = SpaceIndexType.EXTENDED)
  height: Int = -1,

  @BeanProperty
  birthday: Date = null,

  @BeanProperty
  son: Person = null

)
```

## Translations


|Predicate Query|Translated SQL Query|
|:--------------|:-------------------|
|`==` {{<wbr>}} predicateGigaSpace read { person: Person => {{<wbr>}} person.name == "john"} | `=` {{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "name = ?", "john") |
|`!=` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name != "john"} | `<>` {{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "name <> ?", "john") |
|`>`  {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.height > 10}| `>` {{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "height > ?", 10: Integer)|
|`>=` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.height >= 10} | `>=` {{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "height >= ?", 10: Integer) |
|`<`  {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}} person.height < 10}| `<` {{<wbr>}}  gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "height < ?", 10: Integer)|
|`<=` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.height <= 10} | `<=`{{<wbr>}}  gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "height <= ?", 10: Integer) |
|`&&` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.height > 10 && person.height < 100{{<wbr>}}} | `AND`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "( height > ? ) AND ( height < ? )", {{<wbr>}}  10: Integer, 100: Integer)|
|`||` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.height < 10 \| person.height > 100{{<wbr>}}} | `OR`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "( height < ? ) OR ( height > ? )", {{<wbr>}}  10: Integer, 100: Integer) |
|`eq null` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name eq null} | `is null`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "name is null", QueryResultType.OBJECT ) |
|`ne null` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name ne null} | `is NOT null`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "name is NOT null", QueryResultType.OBJECT) |
|`like` {{<wbr>}}//  Implicit conversion on java.lang.String{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name like "j%"}| `like`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "name like 'j%'", QueryResultType.OBJECT) |
|`notLike` {{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name notLike "j%"} | `NOT like`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "name NOT like 'j%'", QueryResultType.OBJECT) |
|`rlike` {{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  person.name rlike "j.\*"} | `rlike`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "name rlike 'j.\*'", QueryResultType.OBJECT) |
|`Nested Queries` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}} person.son.name == "dave"} |{{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "son.name = ?", "dave") |
|`Date` {{<wbr>}}// implicit conversion on java.util.Date{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  person.birthday < janesBirthday} |{{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "birthday < ?", janesBirthday) |
|`Date` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}} person.birthday <= janesBirthday} |{{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "birthday <= ?", janesBirthday) |
|`Date` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.birthday > janesBirthday} |{{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "birthday > ?", janesBirthday) |
|`Date` {{<wbr>}} predicateGigaSpace read { person: Person =>{{<wbr>}}  person.birthday >= janesBirthday} |{{<wbr>}}gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "birthday >= ?", janesBirthday) |
|`select` {{<wbr>}}// select is imported into scope{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}} select(person.name, person.birthday){{<wbr>}} person.id == someId} | {{<wbr>}}setProjections gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "id = ?", someId{{<wbr>}}).setProjections("name, birthday") |
|`orderBy`{{<wbr>}}// orderBy is imported into scope{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}} orderBy(person.birthday){{<wbr>}}  person.nickName eq null} | `ORDER BY`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "nickName is null ORDER BY birthday", {{<wbr>}}  QueryResultType.OBJECT) |
|`orderBy().ascending`{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  orderBy(person.birthday)==.ascending{{<wbr>}} person.nickName eq null} | `ORDER BY ... ASC`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}} "nickName is null ORDER BY birthday ASC", {{<wbr>}}  QueryResultType.OBJECT) |
|`orderBy().descending`{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  orderBy(person.birthday).descending{{<wbr>}} person.nickName eq null} | `ORDER BY ... DESC`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "nickName is null ORDER BY birthday DESC", {{<wbr>}}  QueryResultType.OBJECT) |
|`groupBy`{{<wbr>}}// groupBy is imported into scope{{<wbr>}}predicateGigaSpace read { person: Person =>{{<wbr>}}  groupBy(person.birthday){{<wbr>}}  person.nickName eq null} | `GROUP BY`{{<wbr>}} gigaSpace read new SQLQuery(classOf[Person], {{<wbr>}}  "nickName is null GROUP BY birthday", {{<wbr>}}  QueryResultType.OBJECT) |
