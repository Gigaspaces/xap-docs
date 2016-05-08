---
type: post110
title:  Geospatial Queries
categories: XAP110
parent: querying-the-space.html
weight: 340
---


Spatial queries make use of geometry data types such as points, circles and polygons and these queries consider the spatial relationship between these geometries. 




To take advantage of XAP's spatial capabilites, simply add the `xap-spatial` maven dependency to your project:


```xml
<dependency>
    <groupId>com.gigaspaces</groupId>
    <artifactId>xap-spatial</artifactId>
    <version>{{%version spatial%}}</version>
</dependency>
```

If you're not using maven, add `XAP_HOME/lib/optional/spatial` to your classpath.



# Getting Started

Suppose we want to write an application to locate nearby gas stations.  First, we create a `GasStation` class which includes the location and address of the gas station:


{{%align center%}}
![image](/attachment_files/geo/geo-coordinates.png)
{{%/align%}}
 

And here is the corresponding java class:

```java
import org.openspaces.spatial.shapes.Point;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class GasStation {

	private Long id;

	private Point location;

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	@SpaceId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

```

Let's write some gas stations into the Space:

```java
    GigaSpace gigaSpace = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer("geoSpace")).gigaSpace();

    GasStation gs = new GasStation();
    gs.setId(new Long(1));
    gs.setLocation(ShapeFactory.point(10.0d, 10.0d));

    gigaSpace.write(gs);		
```

Next, we can query for a gas station within a certain radius of our location:

```java
    Point p = ShapeFactory.point(7.5d, 7.5d);

    SQLQuery<GasStation> query = new SQLQuery<GasStation>(GasStation.class, "location spatial:within ?")
				.setParameter(1, ShapeFactory.circle(p, 4.5d));
    GasStation station = gigaSpace.read(query);

    if (station != null) {
        System.out.println("Found a GasStation :" + station);
    }
}
```


# Shapes

XAP supports the following shapes:

{{%align center%}}
![image](/attachment_files/geo/geo-data-types.png)
{{%/align%}}


All shapes are located in the `org.openspaces.spatial.shapes` package. 

|   Shape    | Description    |
|:-----------|:---------------|
|Point       |A point, denoted by `X` and `Y` coordinates.|
|Circle      |A circle, denoted by a point and a radius.|
|Rectangle   |A rectangle aligned with the axis (for non-aligned rectangles use Polygon).|
|LineString  |A finite sequence of one or more consecutive line segments.|
|Polygon     | A finite sequence of consecutive line segments which denotes a bounded area.|

To create a shape, use the `ShapeFactory` class. For example:

```java
Point point = ShapeFactory.point(1, 2);
```

It's recommended to use static import to simplify shape creation. For example:

```java
import static org.openspaces.spatial.ShapeFactory.*;
...
Polygon polygon = polygon(point(0,0), point(1,1), point(2,2));
```



# Queries

Spatial queries are available through the `spatial:` extension to the [SQL query syntax](./query-sql.html). The following operations are supported:

|  Query   |  Description   |
|:-----|:-------|
|shape1 spatial:contains shape2   | shape1 contains shape2, boundaries inclusive.|
|shape1 spatial:within shape2     | shape1 is within (contained in) shape2, boundaries inclusive.|
|shape1 spatial:intersects shape2 | The intersection between shape1 and shape 2 is not empty (i.e. some or all of shape1 overlaps some or all of shape2).|

Spatial queries can be used with any space operation which supports SQL queries (`read`, `readMultiple`, `take`, etc.)


# Geofencing
 
Geofencing allows automatic alerts to be generated based on the defined coordinates of a geographic area.  

{{%align center%}}
![image](/attachment_files/geo/geo-fencing.png)
{{%/align%}}


XAP uses Event Containers with Spatial queries to do just that. For example, we want to be notified when a new `GasStation` appears within a certain radius of my location.

 



```java
Point location = ShapeFactory.point(0, 0);
double radius = 5.0d;
SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location spatial:within ?")
		.setParameter(1, ShapeFactory.circle(location, radius));
   
SimpleNotifyEventListenerContainer eventContainer = new SimpleNotifyContainerConfigurer(gigaSpace)
    .template(query)
    .eventListenerAnnotation(new Object() {
        @SpaceDataEvent
        public void eventHappened(GasStation gasStation) {
            System.out.println("Got " + gasStation);
        }
    }).notifyContainer();
```


# Indexing

If the space contains lots of `GasStation` entries and our query is only relevant to a small subset of them, the space is likely to scan lots of entries before finding a match. In order to improve that, we can index the location property using the `@SpaceSpatialIndex` annotation:

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

# Combining Spatial and Standard Predicates

Suppose our `GasStation` class contains a `price` property as well, and we want to enhance our query and find nearby gas stations whose price is lower than a certain threshold. We can simply add the relevant predicate to the query's criteria:

```java
public GasStation findNearbyGasStation(Point location, int radius, double maxPrice) {
	SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location spatial:within ? AND price < ?")
		.setParameter(1, ShapeFactory.circle(location, radius))
		.setParameter(2, maxPrice);
	return gigaSpace.read(query);
}
```

{{%note "Choosing the optimal index"%}}
If both `location` and `price` are indexed, the index which appears first in the query is the one that will be used. This may significantly effect the performance of your query, so it's recommended to estimate which index is most efficient for each query and put it first.
{{%/note%}}


# WKT Support

The `ShapeFactory` also supports parsing [WKT](https://en.wikipedia.org/wiki/Well-known_text)

|   Shape  | Description    |
|:----|:-------|
|Point       | POINT (0 0)|
|Rectangle   | LINESTRING(30 10, 40 10, 40 20, 30 20, 30 10) |
|LineString  | LINESTRING (0 0, 1 1, 5 5)|



For example:

```java
Shape shape = ShapeFactory.parse("LINESTRING (0 0, 1 1, 5 5)", ShapeFormat.WKT);
```


# GeoJson Support
The `ShapeFactory` also supports parsing  [GeoJson](http://geojson.org/) strings into shapes. For example:



# Configuration

## Lucene 

XAP uses [Lucene](https://lucene.apache.org/) to index spatial shapes. XAP maintains Lucene's best practices by default so there's no need to learn Lucene for using XAP's Spatial API. However, if you're familiar with Lucene and wish to better understand how it's used and what can be modified, this section is for you.

### Store Directory

Lucene indexing is stored in a **Store Directory**. Lucene supports different Store Directory implementations, but recommends using the [MMapDirectory](https://lucene.apache.org/core/5_3_0/core/org/apache/lucene/store/MMapDirectory.html), which is what XAP uses by default.

### File System

Lucene indexes are stored in the file system. When used within a processing unit deployed on the service grid, these files are stored within the processing unit working folder, and automatically deleted if/when the processing unit is undeployed. When there's no service grid involved, the files are stored in a unique folder under `user.home`. This location can be explicitly set using the `space-config.spatial.lucene.storage.location` space property.
