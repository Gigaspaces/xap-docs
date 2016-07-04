---
type: post120
title:  Geospatial Index
categories: XAP120
parent: indexing-overview.html
weight: 500
---

Geospatial indexes can be defined by using the `@SpaceSpatialIndex` and `@SpaceSpatialIndexes` annotations.

Lets assume we have a class called `GasStation` that has a `Point` property that describes its location and we want to execute geospatial queries 
against this property:

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

Here is a query that will trigger the usage of this index:

```java
SQLQuery<GasStation> query = new SQLQuery<GasStation>(GasStation.class, "location spatial:within ?")
				.setParameter(1, ShapeFactory.circle(p, 4.5d));
```

{{%refer%}}
See [Geospatial queries](./query-geospatial.html)  for more information on how geospatial queries work. 
{{%/refer%}}


### Nested Index

An index can be defined on a nested property to improve performance of nested queries. Nested properties indexing uses an additional attribute - **`path()`**.
This attribute represents the path of the property within the nested object.

In the example below, the `Point` is a property of the class `Location`  which is a property of `GasStation`:


{{%tabs%}}

{{%tab "Single Index Annotation" %}}
```java
import org.openspaces.spatial.SpaceSpatialIndex;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class GasStation {

	private Long id;
	private Location location;
    ...

	@SpaceSpatialIndex(path = "point")
	public Location getLocation() {
		return location;
	}
}
 
 
import org.openspaces.spatial.shapes.Point;

public class Location {

	private String address;
	private Point point;
}
```
{{%/tab%}}
{{%tab "Multiple Index Annotation" %}}
```java
import org.openspaces.spatial.SpaceSpatialIndex;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class GasStation {

	private Long id;
	private Location location;
    ...

	@SpaceSpatialIndexes({ @SpaceSpatialIndex(path = "point"), @SpaceSpatialIndex(path = "circle")})
	public Location getLocation() {
		return location;
	}
}
 
 
import org.openspaces.spatial.shapes.Point;

public class Location {

	private String address;
	private Point  point;
	private Circle circle;
}
```
{{%/tab%}}

{{%/tabs%}}

The following is an example of a query that triggers this index:

```java
SQLQuery<GasStation> query = new SQLQuery<GasStation>(GasStation.class, "location.point spatial:within ?")
				.setParameter(1, ShapeFactory.circle(p, 4.5d));
		GasStation station = gigaSpace.read(query);
```



### Combining Spatial and Standard Predicates

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

