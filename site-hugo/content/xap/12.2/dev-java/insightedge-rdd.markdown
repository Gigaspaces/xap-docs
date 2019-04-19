---
type: post122
title:  RDD API
categories: XAP122, OSS
parent: insightedge-apis.html
weight: 500
canonical: auto
---

This section describes how to load data from the Data Grid to Spark.


# Creating the Data Grid RDD

To load data from the Data Grid, use `SparkContext.gridRdd[R]`. The type parameter `R` is a Data Grid model class. For example,

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val products = sc.gridRdd[Product]()
```
{{%/tab%}}
{{%/tabs%}}

Once RDD is created, you can perform any generic Spark actions or transformations, e.g.

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val products = sc.gridRdd[Product]()
println(products.count())
println(products.map(_.quantity).sum())
```
{{%/tab%}}
{{%/tabs%}}

#  Saving RDD to the Data Grid

To save Spark RDD to the Data Grid, use `RDD.saveToGrid` method. It assumes that type parameter `T` of your `RDD[T]` is a Space class otherwise an exception will be thrown at runtime.

Example:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val rdd = sc.parallelize(1 to 1000).map(i => Product(i, "Description of product " + i, Random.nextInt(10), Random.nextBoolean()))
rdd.saveToGrid()
```
{{%/tab%}}
{{%/tabs%}}

# Grid-Side RDD Filters

To query a subset of data from the Data Grid, use the `SparkContext.gridSql[R](sqlQuery, args)` method. The type parameter `R` is a Data Grid model class, the `sqlQuery`parameter is a native Data Grid SQL query, `args` are arguments for the SQL query. For example, to load only products with a quantity more than 10:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val products = sc.gridSql[Product]("quantity > 10")
```
{{%/tab%}}
{{%/tabs%}}


You can also bind parameters in the SQL query:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val products = sc.gridSql[Product]("quantity > ? and featuredProduct = ?", Seq(10, true))
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
For more information about the Data Grid SQL queries, refer to [Data Grid documentation](./query-sql.html).
{{%/refer%}}

# Zip RDD with Grid SQL Data

You may want to transform the RDD by executing SQL query for each element in the RDD. This can be done using `rdd.zipWithGridSql()` method. It returns a new RDD of tuple (element, query result items).
Note, this might be an expensive operation.

For example, you have RDD of Customers and want to associate them with their orders:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val customers: RDD[Customer] = ...
val query = "customerId = ?"
val queryParamsConstructor = (c: Customer) => Seq(c.id)
val projections = None
val orders: RDD[(Customer, Seq[Order])] = customers.zipWithGridSql[Order](query, queryParamsConstructor, projections)

```
{{%/tab%}}
{{%/tabs%}}

<br>

# Controlling Spark partitions

Methods `SparkContext.gridRdd()` and `SparkContext.gridSql()` take an optional `splitCount` parameter which defines the number of Spark partitions per Data Grid partition. This feature is limited to `bucketed` grid model. 

{{%refer%}}
For more information, refer to [Data Modeling](./insightedge-modeling.html).
{{%/refer%}}
