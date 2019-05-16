---
type: post123
title:  Dataset API
categories: XAP123, OSS
parent: insightedge-apis.html
weight: 600
canonical: auto
---

A Dataset is a distributed collection of data. Datasets provide the benefits of RDDs (strong typing, ability to use powerful lambda functions) 
with the benefits of Spark SQL’s optimized execution engine. Both the typed transformations (e.g., map, filter, and groupByKey) and 
untyped transformations (e.g., select and groupBy) are available on the Dataset class.

Datasets use a specialized Encoder to serialize the objects for processing or transmitting over the network. 
Encoders for most common types are automatically provided by importing `spark.implicits._`

To convert a `DataFrame` to a `Dataset` use the `as[U]` conversion method.

{{%refer%}}
To read more about Dataset API, please refer to {{%exurl "Spark Documentation""https://spark.apache.org/docs/latest/sql-programming-guide.html"%}}.
{{%exurl "Spark class Dataset""https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.Dataset"%}}.
{{%/refer%}}

This section describes how to use the Dataset API with the Data Grid.

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

# Creating Datasets

RDDs are stored in Data Grid simply as collections of objects of a certain type. 
You can create Dataset by such type with the next syntax:

{{%tabs%}}
{{%tab "Using SparkSession"%}}
```scala
import org.insightedge.spark.implicits.all._

val spark: SparkSession // An existing SparkSession.

val ds: Dataset[Person] = spark.read.grid[Person].as[Person]

// Displays the content of the Dataset to stdout
ds.show()

// We use the dot notation to access individual fields
// and count how many people are below 60 years old
val below60 = ds.filter( p => p.age < 60).count()
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

val ds: Dataset[Person] = spark.sql("select * from people").as[Person]

// Displays the content of the Dataset to stdout
ds.show()
```
{{%/tab%}}

{{%tab "Without imports"%}}
```scala
val spark: SparkSession // An existing SparkSession.

val ds = spark.read
              .format("org.apache.spark.sql.insightedge")
              .option("class", classOf[Person].getName)
              .load().as[Person]

// Displays the content of the Dataset to stdout
ds.show()
```
{{%/tab%}}
{{%/tabs%}}

# Persisting Datasets to Data Grid

To write a Dataset use `Dataset.write`. The content of the Dataset is saved with a specified collection name. 
The behavior of the write operation is controlled by the `SaveMode`.

Since Datasets are no longer linked to object type, the content of the Dataset is persisted by the specified collection name.
Thus, when saving a Dataset to the Data Grid, you must provide a collection name, and when loading persisted Dataset, 
the same collection name must be used instead of object type. 

To persist and load persisted Datasets you can use the following syntax:

{{%tabs%}}
{{%tab "Simplified syntax"%}}
```scala
import org.insightedge.spark.implicits.all._

val spark: SparkSession // An existing SparkSession.
val ds: Dataset[Person] // An existing Dataset of type Person

// Filter out teens (ages 13-19) and persist to the specified path "teens"
ds.filter( p => p.age >= 13 && p.age <= 19).write.mode(SaveMode.Overwrite).grid("teens")

// Load persisted data from the specified path
val persisted: Dataset[Person] = spark.read.grid("teens").as[Person]

// Displays the content of the Dataset to stdout
persisted.show()
```
{{%/tab%}}

{{%tab "Without imports"%}}
```scala
val ds: Dataset[Person] // An existing Dataset of type Person

ds.write
    .format("org.apache.spark.sql.insightedge")
    .mode(SaveMode.Overwrite)
    .save("people")

val persisted: Dataset[Person] = spark.read
    .format("org.apache.spark.sql.insightedge")
    .load("people").as[Person]

// Displays the content of the Dataset to stdout
persisted.show()
```
{{%/tab%}}
{{%/tabs%}}


{{%note%}}
Similar to DataFrames, nested properties are stored as `DocumentProperties` in Data Grid when Dataset is persisted
{{%/note%}}

Persisted Datasets are shared among multiple Spark jobs and stay alive after the jobs are complete.


# Nested properties

Let's enhance `Person` class with an additional property `Address` (that has some properties of it's own).
If you write some `Person` to a Data Grid, e.g. by persisting an RDD, and then load 
them as Dataset, you can see that Dataset schema includes nested properties:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.spark.implicits.all._

val spark: SparkSession // An existing SparkSession.

// Write some person to a Data Grid
val person: Person //Some person
val rdd = spark.sparkContext.parallelize(Seq(person))
rdd.saveToGrid()

// Load the RDD as a Dataset
val ds: Dataset[Person] = spark.read.grid[Person].as[Person]

// Displays the schema of the Dataset to stdout
ds.printSchema()
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

Nested properties can be accessed using dot-separated syntax, e.g. `address.city`. 
Here is an example of filtering by nested properties using Dataset API:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val spark: SparkSession // An existing SparkSession.

val ds: Dataset[Person] = spark.read.grid[Person].as[Person]
val countInBuffalo = ds.filter( p => p.address.city == "Buffalo").count()

// Displays number of people from Buffalo
println(countInBuffalo)
```
{{%/tab%}}
{{%/tabs%}}
