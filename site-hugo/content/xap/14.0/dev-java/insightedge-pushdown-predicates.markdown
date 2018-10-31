---
type: post140
title:  Pushdown Predicates
categories: XAP140, OSS
parent: insightedge-apis.html
weight: 450
---

InsightEdge enhances the Agile Spark pushdown predicate capability by leveraging our native advanced indexing mechanism, to retrieve only relevant data entries when running a query (filter). This ability to filter directly on the source (instead of on the target as is done in the vanilla Apache Spark architecture) dramatically improves performance and reduces network overhead.

# Indexing

Pushdown predicates are dependent on indexing the relevant fields. The indexing can be adjusted to improve performance further, as described in the [Indexing](./indexing-overview.html) section. You can also refer to the [Data Modeling](./insightedge-modeling.html) topic.

# Pushing Predicates to the Data Grid

When executing a query in InsightEdge, the following predicates are pushed down to the grid level.

Basic equality comparisons:

- `EqualTo`, `GreaterThan`, `GreaterThanOrEqual`, `LessThan`, `LessThanOrEqual`
- `IsNull`, `IsNotNull`
- `In`

Geospatial predicates: 

- `GeoIntersects`
- `GeoWithin`
- `GeoContains`

For more information about the geospatial predicates, refer to the [GeoSpatial API](./insightedge-geospatial.html) topic.


# RDD API

To use the pushdown predicates with RDD, you can use the gridSql API as shown in the following example. With this API, the queries are performed in the data grid.

```scala
val rdd1 = sc.gridSql[Product]("quantity > 0")...

val rdd2 = sc.gridSql[Product]("quantity > ? and featuredProduct = ?", Seq(10, true))
```


## Geospatial API Support

When using the geospatial API with the RDD API, the predicate is pushed down to the grid and the filtering is done on the data grid side. The gridSql API receives a Data Grid SQL query.


```scala
val rdd = sc.gridSql[SpatialData]("location spatial:within ?", Seq(circle(point(10,10), 10)))
```

For more information about these types of queries, refer to the [RDD API](./insightedge-rdd.html) topic.


# DataFrame/DataSet APIs

When using the DataFrame or DataSet APIs, filters are performed in the client side (executors). If you choose to work with these APIs, use the regular syntax of the **filter** and **where** APIs. InsightEdge pushes down the supported predicates to the grid level.

```scala
//DataFrame
val df = spark.read.grid[Product].where("quantity > 0")

//Dataset
val ds = spark.read.grid[Product].as[Product].where("quantity > 0")
```


{{%note%}}
When using the custom function syntax for filtering, the filtering itself is performed on the client side, not in the data grid.


```scala
//This does not use pushdown predicates and is performed in the client side.
spark.read.grid[Product].as[Product].filter(p => p.quantity > 0).count()
```
{{%/note%}}


## Geospatial API Support

Although this API is supported, the predicates are not pushed down to the data grid and therefore filtering is done on the client side.

```scala
val df = sqlContext.read.grid[SpatialData]
val data = df.filter(df("location") geoWithin circle(point(10,10), 10))
```

For more information about using geospatial predicates with these APIs, refer to the [Dataset API](./insightedge-datasets.html) and [DataFrame API](./insightedge-dataframes.html) topics.

## Aggregations

Currently, Apache Spark does not push down aggregation queries (`groupBy`, `count`, etc.) to the data source. When this functionality becomes available in Apache Spark, it can also be used in InsightEdge. 
