---
type: post110
title:  GeoSpatial Support - API and Usage
categories: XAP110
parent: none
weight: 1200
---

# Configuration

### Lucene Root Directory

XAP uses Lucene Spatial to index shapes and execute fast geospatial queries. It uses MMapDirectory to store index files in the file system. 

The root directory that it uses can be configured with the system property `com.gs.foreignindex.lucene.work`. By default it will use the `user.home` directory.

We create a directory for each class that is written to the space. A sub directory called entries will contain the indexed shapes. The full path looks like: *LuceneRoot/space_containerx:space/entries/*

{{<wbr>}}

# GeoSpatial API


### GeoSpatial Shapes

The shapes classes are located under the package `com.gigaspaces.spatial.shapes`. 

The supported shapes are:

* Point
* Circle
* Rectangle
* Polygon

{{<wbr>}}

### GeoSpatial Operations

GeoSpatial operations are available using the SQLQuery syntax:

```java
Shape polygon = new Polygon(new Point(0.0d, 0.0d), new Point(4.0d, 0d), new Point(4, 4), new Point(0, 4));
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "indexedShape <geospatial operation> ? ")
	.setParameter(1, polygon);         
```

There are three types of geospatial operations that can be used in the query:

* geospatial:contains
* geospatial:within
* geospatial:intersects 

{{<wbr>}}

### GeoSpatial Annotation

In order to index a shape field the field should be annotated with the `@SpaceSpatialIndex` annotation.

When the field in query is not annotated with the `@SpaceSpatialIndex` annotation a full scan will be used to serve the geospatial query.

Given the pojo:

```java
public class Pojo {
    ...
    @SpaceSpatialIndex
    private Polygon _indexedShape;
    
    private Circle _notIndexedShape
    ...
}
```

Both queries will work:

```java
Shape point = new Point(4, 4);
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "indexedShape geospatial:contains ? ")
	.setParameter(1, point);         
```

```java
Shape point = new Point(4, 4);
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "notIndexedShape geospatial:contains ? ")
	.setParameter(1, point);         
```

The first query will use lucene spatial index to perform the query while the second one will iterate over all Pojo objects in space and filter out the matching objects.

{{<wbr>}}

# Usage and Examples

Given the pojo:

```java
public class Pojo {
    ...
    private String _author
    
    @SpaceSpatialIndex
    private Polygon _indexedShape;
    
    private Circle _notIndexedShape
    ...
}
```

{{<wbr>}}

### GeoSpatial Query

```java
Shape circle = new Circle(new Point(0.0d, 0.0d), 5.0d);
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "indexedShape geospatial:within ?")
    .setParameter(1, circle); 

gigaSpace.readMultiple(query);
```

{{<wbr>}}

### Combining geospatial with space query

```java
Shape circle = new Circle(new Point(0.0d, 0.0d), 5.0d);
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "indexedShape geospatial:within ? and author = ?")
    .setParameter(1, circle)
    .setParameter(2, "John");
    
gigaSpace.readMultiple(query);
```

{{<wbr>}}

### Notify Container

```java
Shape circle = new Circle(new Point(0.0d, 0.0d), 5.0d);
SQLQuery<Pojo> query = new SQLQuery<Pojo>(Pojo.class, "indexedShape geospatial:within ?")
    .setParameter(1, circle); 
    
SimpleNotifyEventListenerContainer simpleNotifyEventListenerContainer = new SimpleNotifyContainerConfigurer(gigaSpace)
    .template(query)
    .eventListenerAnnotation(new Object() {
        @SpaceDataEvent
        public void eventHappened(Pojo data) {
            System.out.println("Got "+data);
        }
    }).notifyContainer();
```
