---
type: post123
title:  Predicate Based Queries
categories: XAP123, OSS
parent: scala.html
weight: 300
---


 

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
|`==` <br> predicateGigaSpace read { person: Person => <br> person.name == "john"} | `=` <br> gigaSpace read new SQLQuery(classOf[Person], <br>  "name = ?", "john") |
|`!=` <br> predicateGigaSpace read { person: Person =><br>  person.name != "john"} | `<>` <br> gigaSpace read new SQLQuery(classOf[Person], <br> "name <> ?", "john") |
|`>`  <br> predicateGigaSpace read { person: Person =><br>  person.height > 10}| `>` <br>gigaSpace read new SQLQuery(classOf[Person], <br> "height > ?", 10: Integer)|
|`>=` <br> predicateGigaSpace read { person: Person =><br>  person.height >= 10} | `>=` <br> gigaSpace read new SQLQuery(classOf[Person], <br>  "height >= ?", 10: Integer) |
|`<`  <br> predicateGigaSpace read { person: Person =><br> person.height < 10}| `<` <br>  gigaSpace read new SQLQuery(classOf[Person], <br> "height < ?", 10: Integer)|
|`<=` <br> predicateGigaSpace read { person: Person =><br>  person.height <= 10} | `<=`<br>  gigaSpace read new SQLQuery(classOf[Person], <br>  "height <= ?", 10: Integer) |
|`&&` <br> predicateGigaSpace read { person: Person =><br>  person.height > 10 && person.height < 100<br>} | `AND`<br> gigaSpace read new SQLQuery(classOf[Person], <br>  "( height > ? ) AND ( height < ? )", <br>  10: Integer, 100: Integer)|
|`||` <br> predicateGigaSpace read { person: Person =><br>  person.height < 10 \| person.height > 100<br>} | `OR`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "( height < ? ) OR ( height > ? )", <br>  10: Integer, 100: Integer) |
|`eq null` <br> predicateGigaSpace read { person: Person =><br>  person.name eq null} | `is null`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "name is null", QueryResultType.OBJECT ) |
|`ne null` <br> predicateGigaSpace read { person: Person =><br>  person.name ne null} | `is NOT null`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "name is NOT null", QueryResultType.OBJECT) |
|`like` <br>//  Implicit conversion on java.lang.String<br>predicateGigaSpace read { person: Person =><br>  person.name like "j%"}| `like`<br> gigaSpace read new SQLQuery(classOf[Person], <br>  "name like 'j%'", QueryResultType.OBJECT) |
|`notLike` <br>predicateGigaSpace read { person: Person =><br>  person.name notLike "j%"} | `NOT like`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "name NOT like 'j%'", QueryResultType.OBJECT) |
|`rlike` <br>predicateGigaSpace read { person: Person =><br>  person.name rlike "j.\*"} | `rlike`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "name rlike 'j.\*'", QueryResultType.OBJECT) |
|`Nested Queries` <br> predicateGigaSpace read { person: Person =><br> person.son.name == "dave"} |<br>gigaSpace read new SQLQuery(classOf[Person], <br> "son.name = ?", "dave") |
|`Date` <br>// implicit conversion on java.util.Date<br>predicateGigaSpace read { person: Person =><br>  person.birthday < janesBirthday} |<br>gigaSpace read new SQLQuery(classOf[Person], <br>  "birthday < ?", janesBirthday) |
|`Date` <br> predicateGigaSpace read { person: Person =><br> person.birthday <= janesBirthday} |<br>gigaSpace read new SQLQuery(classOf[Person], <br> "birthday <= ?", janesBirthday) |
|`Date` <br> predicateGigaSpace read { person: Person =><br>  person.birthday > janesBirthday} |<br>gigaSpace read new SQLQuery(classOf[Person], <br> "birthday > ?", janesBirthday) |
|`Date` <br> predicateGigaSpace read { person: Person =><br>  person.birthday >= janesBirthday} |<br>gigaSpace read new SQLQuery(classOf[Person], <br> "birthday >= ?", janesBirthday) |
|`select` <br>// select is imported into scope<br>predicateGigaSpace read { person: Person =><br> select(person.name, person.birthday)<br> person.id == someId} | <br>setProjections gigaSpace read new SQLQuery(classOf[Person], <br> "id = ?", someId<br>).setProjections("name, birthday") |
|`orderBy`<br>// orderBy is imported into scope<br>predicateGigaSpace read { person: Person =><br> orderBy(person.birthday)<br>  person.nickName eq null} | `ORDER BY`<br> gigaSpace read new SQLQuery(classOf[Person], <br>  "nickName is null ORDER BY birthday", <br>  QueryResultType.OBJECT) |
|`orderBy().ascending`<br>predicateGigaSpace read { person: Person =><br>  orderBy(person.birthday)==.ascending<br> person.nickName eq null} | `ORDER BY ... ASC`<br> gigaSpace read new SQLQuery(classOf[Person], <br> "nickName is null ORDER BY birthday ASC", <br>  QueryResultType.OBJECT) |
|`orderBy().descending`<br>predicateGigaSpace read { person: Person =><br>  orderBy(person.birthday).descending<br> person.nickName eq null} | `ORDER BY ... DESC`<br> gigaSpace read new SQLQuery(classOf[Person], <br>  "nickName is null ORDER BY birthday DESC", <br>  QueryResultType.OBJECT) |
|`groupBy`<br>// groupBy is imported into scope<br>predicateGigaSpace read { person: Person =><br>  groupBy(person.birthday)<br>  person.nickName eq null} | `GROUP BY`<br> gigaSpace read new SQLQuery(classOf[Person], <br>  "nickName is null GROUP BY birthday", <br>  QueryResultType.OBJECT) |
