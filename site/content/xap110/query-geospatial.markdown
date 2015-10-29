---
type: post110
title:  Geospatial Queries
categories: XAP110
parent: querying-the-space.html
weight: 340
---

Geospatial queries make use of geometry data types such as points, circles and polygons and these queries consider the spatial relationship between these geometries.

{{%note "Technical Preview"%}}
This feature is new in 11.0 and is currently a technical preview, i.e. it is subject to breaking changes until 11.0 is released.
{{%/note%}}

# Getting Started

Suppose we want to write an application to locate nearby gas stations. 

First, we should create a `GasStation` class which includes the location of the gas station:

```java
import com.gigaspaces.spatial.shapes.Point;

public class GasStation {
	private Point location;
	
	public Point getLocation() {
	    return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
}
```

Next, assuming we've written some gas station entries to the space, we can query for a gas station within a certain radius of our location:

```java
public GasStation findNearbyGasStation(Point location, int radius) {
	SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location geospatial:within ?")
		.setParameter(1, new Circle(location, radius));
	return gigaSpace.read(query);
}
```

# Model and Query API

The supported shapes are `Point`, `Circle`, `Rectangle` and `Polygon`, which are all located in the `com.gigaspaces.spatial.shapes` package. 

Geospatial queries are available through the `geospatial:` extension to the SQL query syntax. The following operations are supported:

* `shape1 geospatial:contains shape2` - shape1 contains shape2, boundaries inclusive.
* `shape1 geospatial:within shape2` - shape1 is within (contained in) shape2, boundaries inclusive.
* `shape1 geospatial:intersects shape2` - The intersection between shape1 and shape 2 is not empty (i.e. some or all of shape1 overlaps some or all of shape2).

Geospatial queries can be used with any space operation which supports SQL queries (`read`, `readMultiple`, `take`, etc.)

# Indexing

Our current implementation is valid, but not very efficient - if the space contains lots of `GasStation` entries and our query is only relevant to a small subset of them, the space is likely to scan lots of entries before finding a match. In order to improve that, we can index the location property using the `@SpaceSpatialIndex` annotation:

```java
public class GasStation {
	private Point location;

	@SpaceSpatialIndex
	public Point getLocation() {
	    return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
}
```

# Combining Geospatial and Standard Predicates

Suppose our `GasStation` class contains a `price` property as well, and we want to enhance our query and find nearby gas stations whose price is lower than a certain threshold. We can simply add the relevant predicate to the query's criteria:

```java
public GasStation findNearbyGasStation(Point location, int radius, double maxPrice) {
	SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location geospatial:within ? AND price < ?")
		.setParameter(1, new Circle(location, radius))
		.setParameter(2, maxPrice);
	return gigaSpace.read(query);
}
```

{{<plus>}} If both `location` and `price` are indexed, the index which appears first in the query is the one that will be used. This may significantly effect the performance of your query, so it's recommended to think which index is most efficient for each query and put it first.

# Geo-fencing

Suppose we want something done when an event occurs within a certain area, for example - notify me when there's a new `GasStation` within a certain radius of my location. Event Containers can be used in conjunction with Geospatial queries to do just that. For example:

```java
Point location = new Point(0.0d, 0.0d);
double radius = 5.0d;
SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location geospatial:within ?")
		.setParameter(1, new Circle(location, radius));
   
SimpleNotifyEventListenerContainer eventContainer = new SimpleNotifyContainerConfigurer(gigaSpace)
    .template(query)
    .eventListenerAnnotation(new Object() {
        @SpaceDataEvent
        public void eventHappened(GasStation gasStation) {
            System.out.println("Got " + gasStation);
        }
    }).notifyContainer();
```

# Configuration

## Lucene 

XAP uses [Lucene](https://lucene.apache.org/) to index spatial shapes. XAP maintains Lucene's best practices by default so there's no need to learn Lucene for using XAP's Geospatial API. Howwever, if you're familiar with Lucene and wish to better understand how it's used and what can be modified, this section is for you.

### Store Directory

Lucene indexing is stored in a **Store Directory**. Lucene supports different Store Directory implementations, but recommends using the [MMapDirectory](https://lucene.apache.org/core/4_1_0/core/org/apache/lucene/store/MMapDirectory.html), which is what XAP uses by default.

### File System

Lucene needs to store some files to maintain its indexes. The location of these files can be set using the `com.gs.foreignindex.lucene.work` system property (if not set, defaults to `user.home`).
Within this directory, a sub directory is automatically created for each space (for example, `LuceneRoot/space_container1:space/entries/`

{{<infosign>}} This default will be changed in one of the upcoming milestones - if the space is deployed in a processing unit, it will use its working directory instead, so data will be automatically deleted when the processing unit is undeployed.

# Limitations

The following limitations and open issues apply to 11.0.0 m4:

* Transactions are not supported (expected to be supported in m5)
* Disjoint queries are not supported (expected to be supported in m5)
* The property itself must be a `Shape` - nested properties, collections, dynamic properties and space documents are not supported.
* There are some issues with inheritance which have not been isolated yet.
