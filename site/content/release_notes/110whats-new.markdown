---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap110.html
weight: 100
---

This page lists the main new features in XAP 11.0 (Java and .Net editions).


It's not an exhaustive list of all new features. For a full change log for 11.0 please refer to the [new features](./110new-features.html) and [fixed issues](./110fixed-issues.html) pages.

{{%panel%}}

- [GeoSpatial Queries](#geo)

- [RocksDB](#rocks)

{{%/panel%}}

<br>


{{%anchor geo%}}

# GeoSpatial Queries

GeoSpatial queries make use of geometry data types such as points, circles and polygons and these queries consider the spatial relationship between these geometries.

{{%refer%}}
[GeoSpatial Queries](/xap110/geospatial.html)
{{%/refer%}}


{{%anchor rocks%}}

# RocksDB

XAP is using [RocksDB](http://rocksdb.org/) an embeddable persistent key-value store for fast storage. Keys and values are arbitrary byte streams.

{{%refer%}}
[RocksDB](/xap110adm/memoryxtend-rocksdb-ssd.html)
{{%/refer%}}