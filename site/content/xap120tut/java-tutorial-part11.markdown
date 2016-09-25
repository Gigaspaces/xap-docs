---
type: post120
title:  Query Geospatial Data
categories: XAP120TUT
parent: none
weight: 550
---

Spatial queries make use of geometry data types such as points, circles and polygons and these queries consider the spatial relationship between these geometries.



# Example

Suppose we want to write an application to locate nearby gas stations. 

First, we should create a `GasStation` class which includes the location of the gas station:

```java
import org.openspaces.spatial.shapes.Point;

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
	SQLQuery<GasStation> query = new SQLQuery(GasStation.class, "location spatial:within ?")
		.setParameter(1, ShapeFactory.circle(location, radius));
	return gigaSpace.read(query);
}
```

# Model and Query API

## Shapes

All shapes are located in the `org.openspaces.spatial.shapes` package. 

* `Point` - A point, denoted by `X` and `Y` coordinates.
* `Circle` - A circle, denoted by a point and a radius.
* `Rectangle` - A rectangle aligned with the axis (for non-aligned rectangles use Polygon).
* `LineString` - A finite sequence of one or more consecutive line segments.
* `Polygon` - A finite sequence of consecutive line segments which denotes a bounded area.

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

The `ShapeFactory` also supports parsing [WKT](https://en.wikipedia.org/wiki/Well-known_text) or [GeoJson](http://geojson.org/) strings into shapes. For example:
```java
Shape shape = ShapeFactory.parse("LINESTRING (0 0, 1 1, 5 5)", ShapeFormat.WKT);
```

## Operations

Spatial queries are available through the `spatial:` extension to the SQL query syntax. The following operations are supported:

* `shape1 spatial:contains shape2` - shape1 contains shape2, boundaries inclusive.
* `shape1 spatial:within shape2` - shape1 is within (contained in) shape2, boundaries inclusive.
* `shape1 spatial:intersects shape2` - The intersection between shape1 and shape 2 is not empty (i.e. some or all of shape1 overlaps some or all of shape2).

Spatial queries can be used with any space operation which supports SQL queries (`read`, `readMultiple`, `take`, etc.)

# Indexing

If the Space contains lots of `GasStation` entries and our query is only relevant to a small subset of them, the space is likely to scan lots of entries before finding a match. In order to improve that, we can index the location property using the `@SpaceSpatialIndex` annotation:

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

# Geo-fencing

Suppose we want something done when an event occurs within a certain area, for example - notify me when there's a new `GasStation` within a certain radius of my location. Event Containers can be used in conjunction with Spatial queries to do just that. For example:

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

{{%refer%}}
[Geospatial Queries]({{%currentjavaurl%}}/query-geospatial.html)
{{%/refer%}}
