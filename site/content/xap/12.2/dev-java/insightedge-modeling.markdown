---
type: post122
title:  InsightEdge Data Modeling
categories: XAP122, OSS
parent: insightedge-apis.html
weight: 400
---

This section describes how to define the Data Grid model.


# Class Annotations

The Data Grid native modeling is designed for  Java POJO classes. In order to use Scala case classes with the grid, do the following:

* Annotate each property in the case class with `scala.beans.BeanProperty` (instructs the compiler to generate a bean-compatible getter and setter).
* Mark each property as `var`.
* Add a no-args constructor with default values.

{{%refer%}}
There is partial support for immutable case classes in the data grid. For more information, refer to [Constructor Based Properties](./scala-constructor-based-properties.html).
{{%/refer%}}

In addition, you can use data-grid specific annotations from `org.insightedge.scala.annotation` to enhance your data model. `@SpaceId` is mandatory, the rest is optional.

Here is an example of `Product` class:

{{%tabs%}}
{{%tab "Scala" %}}
```scala
import org.insightedge.scala.annotation._
import scala.beans.{BeanProperty, BooleanBeanProperty}

case class Product(
   @BeanProperty @SpaceId var id: Long,
   @BeanProperty var description: String,
   @BeanProperty var quantity: Int
) {
   def this() = this(-1, null, -1)
}
```
{{%/tab%}}
{{%/tabs%}}

{{%note "Annotations and Shell"%}}
The Spark shell does not support defining annotations on your class model. Instead, pre-compile and import your model classes, or use the Zeppelin [Web Notebook](../started/insightedge-zeppelin.html) instead of the shell.
{{%/note%}}

## Autogenerate ID

If you want to increment the `id` property automatically when saving to the data grid, use `@SpaceId(autoGenerate = true)` (only for `String` fields).

## Indexing

You can improve the speed of data filtering and retrieval operations by indexing relevant fields with the `@SpaceIndex` annotation. 

{{%refer%}}
For more information, refer to [Indexing](./indexing-overview.html).
{{%/refer%}}

<br>

## Controlling Spark Partitions

By default there is a one-to-one mapping between Spark and Data Grid partitions. If you want your RDD or DataFrame to have more partitions than Data Grid, you have to mixin `org.insightedge.spark.model.BucketedGridModel` trait into your class.
The `BucketedGridModel.metaBucketId` property should be uniformly distributed between 0 and 128.
