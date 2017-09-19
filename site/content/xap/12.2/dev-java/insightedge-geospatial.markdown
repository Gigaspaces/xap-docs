---
type: post122
title:  Geospatial API
categories: XAP122, OSS
parent: insightedge-apis.html
weight: 950
---

This section gives a brief overview of Geospatial API and describes how to use it with Spark RDD and DataFrames.

{{%refer%}}
For a detailed description of the Geospatial queries refer to {{%exurl "Geospatial Queries""https://docs.gigaspaces.com/xap/12.1/dev-java/query-geospatial.html"%}} for more info.
{{%/refer%}}

# Shapes

Data Grid supports next shapes, all of them are located at `org.openspaces.spatial.shapes` package:

| Shape       | Description                                                                 |
|:------------|:----------------------------------------------------------------------------|
| Point       | A point, denoted by `X` and `Y` coordinates.                                |
| LineString  | A finite sequence of one or more consecutive line segments.                 |
| Circle      | A circle, denoted by a point and a radius.                                  |
| Rectangle   | A rectangle aligned with the axis (for non-aligned rectangles use Polygon). |
| Polygon     | A finite sequence of consecutive line segments which denotes a bounded area.|

To create a shape, use the `ShapeFactory` class, for example:

```scala
import org.openspaces.spatial.ShapeFactory

val userLocation = ShapeFactory.point(10, 10)
```

# Queries

Geospatial API currently supports three operations: `intersect`, `within` and `contains`.

`Intersect` returns `true` when the intersection between `shape1` and `shape2` is not empty - some or all of `shape1` overlaps some or all of `shape2`.

```scala
import org.insightedge.spark.implicits.all._
import org.openspaces.spatial.ShapeFactory._

// RDD
val rdd = sc.gridSql[SpatialData]("area spatial:intersects ?", Seq(circle(point(10,10), 10)))

// DataFrames
val df = sqlContext.read.grid[SpatialData]
val data = df.filter(df("area") geoIntersects circle(point(10,10), 10))
```

`Within` returns `true` when `shape1` is within (contained in) `shape2`, boundaries inclusive.

```scala
import org.insightedge.spark.implicits.all._
import org.openspaces.spatial.ShapeFactory._

// RDD
val rdd = sc.gridSql[SpatialData]("location spatial:within ?", Seq(circle(point(10,10), 10)))

// DataFrames
val df = sqlContext.read.grid[SpatialData]
val data = df.filter(df("location") geoWithin circle(point(10,10), 10))
```

`Contains` returns `true` when `shape1` contains `shape2`, boundaries inclusive.

```scala
import org.insightedge.spark.implicits.all._
import org.openspaces.spatial.ShapeFactory._

// RDD
val rdd = sc.gridSql[SpatialData]("area spatial:contains ?", Seq(point(10,10)))

// DataFrames
val df = sqlContext.read.grid[SpatialData]
val data = df.filter(df("area") geoContains point(10,10))
```

# Indexing

You can define Geospatial index using the `@SpaceSpatialIndex` and `@SpaceSpatialIndexes` annotations:

```scala
import org.insightedge.scala.annotation._
import org.openspaces.spatial.shapes.Point

import scala.beans.BeanProperty

case class GasStation(
    @BeanProperty @SpaceId var id: Long,
    @BeanProperty @SpaceSpatialIndex var location: Point) {
    
	def this() = this(-1, null)
}
```

{{%refer%}}
To read more about indexing fields in Data Grid, refer to {{%exurl "Geospatial Index""https://docs.gigaspaces.com/xap/12.1/dev-java/indexing-geospatial.html"%}}
{{%/refer%}}

# Zeppelin Notebook

A great place to start experimenting with the Geospatial API is the [Zeppelin notebook](notebook.html) - check out the 'InsightEdge GeoSpatial' notebook.

{{%exclamation%}} If you're using Windows, you'll need to manually copy the jars from `INSIGHTEDGE_HOME\datagrid\lib\optional\spatial` to the Spark jars folder (`INSIGHTEDGE_HOME\jars`) to use this notebook. We're working of fixing that for the next release.

