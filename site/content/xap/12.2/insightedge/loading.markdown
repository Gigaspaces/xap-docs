---
type: post122
title:  RDD API
categories: XAP122I9E
weight: 500
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

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

# Grid-side RDD filters

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
val products = sc.gridSql("quantity > ? and featuredProduct = ?", Seq(10, true))
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
For more details on Data Grid SQL queries please refer to [Data Grid documentation](https://docs.gigaspaces.com/xap/12.1/dev-java/query-sql.html).
{{%/refer%}}

# Zip RDD with Grid SQL data

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



# Controlling Spark partitions

Methods `SparkContext.gridRdd()` and `SparkContext.gridSql()` take an optional `splitCount` parameter which defines the number of Spark partitions per Data Grid partition. This feature is limited to `bucketed` grid model. 

{{%refer%}}
Refer to [Data Modeling](modeling.html) for more details.
{{%/refer%}}