---
type: post122
title:  Data Modeling
categories: XAP122I9E
weight: 400
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This section describes how to define the Data Grid model.


# Class annotations

The Data Grid native modeling is designed for  Java POJO classes. In order to use Scala case classes with the grid, do the following:

* Annotate each property in the case class with `scala.beans.BeanProperty` (instructs the compiler to generate a bean-compatible getter and setter)
* Mark each property as `var`
* Add a no-args constructor with default values

{{%infosign%}} There's partial support for immutable case classes in the data grid. For more information see [Constructor Based Properties](https://docs.gigaspaces.com/xap/12.1/dev-java/scala-constructor-based-properties.html).

In addition, you can use Data grid specific annotations from `org.insightedge.scala.annotation` to enhance your data model. `@SpaceId` is mandatory, the rest is optional.

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
Unfortunately spark shell does not support defining annotations on your class model. Instead, pre-compile and import your model classes, or use [Zeppelin notebooks](notebook.html) instead of the shell.
{{%/note%}}

## Autogenerate ID

If you want to increment the `id` property automatically when saving to the data grid, use `@SpaceId(autoGenerate = true)` (only for `String` fields).

## Indexing

You can improve the speed of data filtering and retrieval operations by indexing relevant fields with the `@SpaceIndex` annotation. For more information see [Indexing](https://docs.gigaspaces.com/xap/12.1/dev-java/indexing-overview.html) at the data grid documentation.

## Controlling Spark partitions

By default there is a one-to-one mapping between Spark and Data Grid partitions. If you want your RDD or DataFrame to have more partitions than Data Grid, you have to mixin `org.insightedge.spark.model.BucketedGridModel` trait into your class.
The `BucketedGridModel.metaBucketId` property should be uniformly distributed between 0 and 128.
