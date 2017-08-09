---
type: post122
title:  Data Frames API
categories: XAP122I9E
weight: 550
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

DataFrames provide an API for manipulating data within Spark. These provide a more user friendly experience than pure Scala for common queries.

{{%refer%}}
To read more about DataFrames API, please refer to {{%exurl "Spark Documentation""https://spark.apache.org/docs/latest/sql-programming-guide.html"%}}.
{{%/refer%}}

This section describes how to use the DataFrames API with the Data Grid.


# Preparing

The entry point to Dataset features is Spark `SparkSession` (replaces the old `SQLContext`).

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.apache.spark.sql.SparkSession

val spark = SparkSession.builder().getOrCreate()

// For implicit conversions like converting RDDs to DataFrames to Dataset
import spark.implicits._
// This is used to simplify calling Data Grid features
import org.insightedge.spark.implicits.all._
```
{{%/tab%}}
{{%/tabs%}}

# Person case class

For the following code snippets, we will use a simple case class named Person.
This case class can be written to (and loaded from) the Data Grid or an external persisted storage.

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.scala.annotation._
import scala.beans.BeanProperty

case class Person(
    @BeanProperty @SpaceId(autoGenerate = true) var id: String,
    @BeanProperty var name: String,
    @BeanProperty var age: Int ) {
   
    def this() = this(null, null, -1)
}
```
{{%/tab%}}
{{%/tabs%}}

# Creating DataFrames

RDDs are stored in Data Grid simply as collections of objects of a certain type. You can create DataFrame by such type with the next syntax:

{{%tabs%}}
{{%tab "Using SQLContext"%}}
```scala
val spark: SparkSession // An existing SparkSession.

import org.insightedge.spark.implicits.all._

val df = spark.read.grid[Person]

// Displays the content of the DataFrame to stdout
df.show()
```
{{%/tab%}}

{{%tab "SQL syntax"%}}
```scala
val spark: SparkSession // An existing SparkSession.

spark.sql(
  s"""
     |create temporary table people
     |using org.apache.spark.sql.insightedge
     |options (class "${classOf[Person].getName}")
  """.stripMargin)

val df = spark.sql("select * from people")

// Displays the content of the DataFrame to stdout
df.show()
```
{{%/tab%}}

{{%tab "Without imports"%}}
```scala
val spark: SparkSession // An existing SparkSession.

val df = spark.read
              .format("org.apache.spark.sql.insightedge")
              .option("class", classOf[Person].getName)
              .load()

// Displays the content of the DataFrame to stdout
df.show()
```
{{%/tab%}}
{{%/tabs%}}

{{%note%}}
If you want to load DataFrame from `SpaceDocuments` written form a third-party application, please, refer to [section below](#loading-custom-spacedocuments)
{{%/note%}}

# Persisting DataFrames to Data Grid

To write a DataFrame use `DataFrame.write`. The content of the DataFrame is saved with a specified collection name. 
The behavior of the write operation is controlled by the `SaveMode`.

Since DataFrames are no longer linked to object type, the content of the DataFrame is persisted by the specified collection name.
Thus, when saving a DataFrame to the Data Grid, you must provide a collection name, and when loading persisted DataFrame, 
the same collection name must be used instead of object type.

To persist and load persisted DataFrame you can use the following syntax:

{{%tabs%}}
{{%tab "Simplified syntax"%}}
```scala
import org.insightedge.spark.implicits.all._

val spark: SparkSession // An existing SparkSession.
val df: DataFrame // An existing DataFrame

// Persist DataFrame into a collection named "people"
df.write.mode(SaveMode.Overwrite).grid("people")

// Load the DataFrame from the collection by name
val persisted = spark.read.grid("people")

// Displays the content of the DataFrame to stdout
persisted.show()
```
{{%/tab%}}

{{%tab "Without imports"%}}
```scala
val spark: SparkSession // An existing SparkSession.
val df: DataFrame // An existing DataFrame

df.write
    .format("org.apache.spark.sql.insightedge")
    .mode(SaveMode.Overwrite)
    .save("people")

val persisted = spark.read
    .format("org.apache.spark.sql.insightedge")
    .load("people")

// Displays the content of the DataFrame to stdout
persisted.show()
```
{{%/tab%}}
{{%/tabs%}}


{{%note%}}
Nested properties are stored as `DocumentProperties` in Data Grid when DataFrame is persisted
{{%/note%}}

Persisted DataFrames are shared among multiple Spark jobs and stay alive after the jobs are complete.


# Nested properties

Let's enhance `Person` class with an additional property `Address` (that has some properties of it's own).

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.scala.annotation._
import scala.beans.BeanProperty

case class Person(
    @BeanProperty @SpaceId(autoGenerate = true) var id: String,
    @BeanProperty var name: String,
    @BeanProperty var age: Int,
    @BeanProperty var address: Address ) {
        
    def this() = this(null, null, -1, null) 
}

case class Address(city: String, state: String)
```
{{%/tab%}}
{{%/tabs%}}

If you write some `Person` to a Data Grid, e.g. by persisting an RDD, and then load them as DataFrame, 
you can see that DataFrame schema includes nested properties:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.spark.implicits.all._

val spark: SparkSession // An existing SparkSession.

val df = spark.read.grid[Person]

// Displays the schema of the DataFrame to stdout
df.printSchema()
```
{{%/tab%}}
{{%/tabs%}}

This code will print the next schema:

```
root
 |-- id: string (nullable = true)
 |-- name: string (nullable = true)
 |-- age: integer (nullable = false)
 |-- address: struct (nullable = true)
 |    |-- city: string (nullable = true)
 |    |-- state: string (nullable = true)
```

Nested properties can be accessed using dot-separated syntax, e.g. `address.city`. Here is an example of filtering by nested properties:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val spark: SparkSession // An existing SparkSession.

val df = spark.read.grid[Person]
val countInBuffalo = df.filter(df("address.city") equalTo "Buffalo").count()

// Displays number of people from Buffalo
println(countInBuffalo)
```
{{%/tab%}}
{{%/tabs%}}

You can also unwrap the nested properties, thus changing the DataFrame schema:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val spark: SparkSession // An existing SparkSession.

spark.read.grid[Person].createOrReplaceTempView("people")

val df = spark.sql("select address.city as city, address.state as state from people")
val countInBuffalo = df.filter(df("city") equalTo "Buffalo").count()

// Displays the schema of the DataFrame to stdout
df.printSchema()

// Displays number of people from Buffalo
println(countInBuffalo)
```
{{%/tab%}}
{{%/tabs%}}

This code will print the next DataFrame schema:

```
root
 |-- city: string (nullable = true)
 |-- state: string (nullable = true)
```

{{%note%}}
When DataFrame is persisted, nested properties are stored as `DocumentProperties` in Data Grid
{{%/note%}}


# Controlling Spark partitions

Similar to RDD in order to control the number of Spark partitions, set the `splitCount` option:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val spark: SparkSession // An existing SparkSession.

val df = spark.read.option("splitCount", "4").grid[Person]
```
{{%/tab%}}
{{%/tabs%}}

The `splitCount` defines the number of Spark partitions per Data Grid partition. This feature is limited to `bucketed` grid model. Please, refer to [Data Modeling](./modeling.html) for more details.


# Loading custom SpaceDocuments

If you have a third-party application writing `SpaceDocuments` into the grid, you have to manually provide a schema for the DataFrame. Nested properties must be supplied with class metadata, so that DataFrame API can properly parse the `SpaceDocuments` (see the `address` field in the example below).

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.spark.implicits.all._
import org.apache.spark.sql.types._

val addressType = StructType(Seq(
  StructField("state", StringType, nullable = true),
  StructField("city", StringType, nullable = true)
))

val schema = StructType(Seq(
  StructField("personId", StringType, nullable = false),
  StructField("name", StringType, nullable = true),
  StructField("surname", StringType, nullable = true),
  StructField("age", IntegerType, nullable = false),
  StructField("address", addressType, nullable = true, nestedClass[Address])
))

val spark: SparkSession // An existing SparkSession.

val df = spark.read.schema(schema).grid("people")
```
{{%/tab%}}
{{%/tabs%}}
