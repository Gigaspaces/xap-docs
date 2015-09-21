---
type: post110
title:  Predicate Based Queries
categories: XAP110
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
|`==` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name == "john"{{%wbr%}} } | `=` {{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{% wbr %}}  "name = ?", "john"{{% wbr %}}) |
|`!=` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name != "john"{{%wbr%}} } | `<>` {{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "name <> ?", "john"{{%wbr%}}) |
|`>`  {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.height > 10{{%wbr%}}}| `>` {{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "height > ?", 10: Integer{{%wbr%}})|
|`>=` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.height >= 10{{%wbr%}}} | `>=` {{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "height >= ?", 10: Integer{{%wbr%}}) |
|`<`  {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}} person.height < 10{{%wbr%}}}| `<` {{% wbr %}}  gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "height < ?", 10: Integer{{%wbr%}})|
|`<=` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.height <= 10{{%wbr%}} } | `<=`{{% wbr %}}  gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "height <= ?", 10: Integer{{%wbr%}}) |
|`&&` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.height > 10 && person.height < 100{{%wbr%}}} | `AND`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "( height > ? ) AND ( height < ? )", {{%wbr%}}  10: Integer, 100: Integer{{% wbr %}})|
|`||` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.height < 10 \| person.height > 100{{%wbr%}}} | `OR`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "( height < ? ) OR ( height > ? )", {{%wbr%}}  10: Integer, 100: Integer{{% wbr %}}) |
|`eq null` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name eq null{{%wbr%}} } | `is null`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "name is null", QueryResultType.OBJECT{{%wbr%}} ) |
|`ne null` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name ne null{{%wbr%}} } | `is NOT null`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{% wbr %}} "name is NOT null", QueryResultType.OBJECT{{%wbr%}}) |
|`like` {{%wbr%}}//  Implicit conversion on java.lang.String{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name like "j%"{{%wbr%}}}| `like`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "name like 'j%'", QueryResultType.OBJECT{{% wbr %}}) |
|`notLike` {{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name notLike "j%"{{%wbr%}}} | `NOT like`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "name NOT like 'j%'", QueryResultType.OBJECT{{% wbr %}}) |
|`rlike` {{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  person.name rlike "j.\*"{{%wbr%}}} | `rlike`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "name rlike 'j.\*'", QueryResultType.OBJECT{{%wbr%}}) |
|`Nested Queries` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}} person.son.name == "dave"{{%wbr%}}} |{{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "son.name = ?", "dave"{{% wbr %}}) |
|`Date` {{%wbr%}}// implicit conversion on java.util.Date{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  person.birthday < janesBirthday{{%wbr%}}} |{{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "birthday < ?", janesBirthday{{% wbr %}}) |
|`Date` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}} person.birthday <= janesBirthday{{%wbr%}}} |{{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "birthday <= ?", janesBirthday{{%wbr%}}) |
|`Date` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.birthday > janesBirthday{{%wbr%}}} |{{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "birthday > ?", janesBirthday{{%wbr%}}) |
|`Date` {{%wbr%}} predicateGigaSpace read { person: Person =>{{%wbr%}}  person.birthday >= janesBirthday{{%wbr%}}} |{{% wbr %}}gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "birthday >= ?", janesBirthday{{%wbr%}}) |
|`select` {{%wbr%}}// select is imported into scope{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}} select(person.name, person.birthday){{%wbr%}} person.id == someId{{% wbr %}}} | {{% wbr %}}setProjections gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "id = ?", someId{{%wbr%}}).setProjections("name, birthday") |
|`orderBy`{{%wbr%}}// orderBy is imported into scope{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}} orderBy(person.birthday){{%wbr%}}  person.nickName eq null{{%wbr%}}} | `ORDER BY`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "nickName is null ORDER BY birthday", {{%wbr%}}  QueryResultType.OBJECT{{% wbr %}}) |
|`orderBy().ascending`{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  orderBy(person.birthday)==.ascending{{%wbr%}} person.nickName eq null{{%wbr%}}} | `ORDER BY ... ASC`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}} "nickName is null ORDER BY birthday ASC", {{%wbr%}}  QueryResultType.OBJECT{{%wbr%}}) |
|`orderBy().descending`{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  orderBy(person.birthday).descending{{%wbr%}} person.nickName eq null{{%wbr%}}} | `ORDER BY ... DESC`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "nickName is null ORDER BY birthday DESC", {{%wbr%}}  QueryResultType.OBJECT{{%wbr%}}) |
|`groupBy`{{%wbr%}}// groupBy is imported into scope{{%wbr%}}predicateGigaSpace read { person: Person =>{{%wbr%}}  groupBy(person.birthday){{%wbr%}}  person.nickName eq null{{%wbr%}}} | `GROUP BY`{{% wbr %}} gigaSpace read new SQLQuery(classOf[Person], {{%wbr%}}  "nickName is null GROUP BY birthday", {{%wbr%}}  QueryResultType.OBJECT{{%wbr%}}) |
