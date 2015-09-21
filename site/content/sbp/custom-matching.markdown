---
type: post
title:  Custom Matching
categories: SBP
parent: data-access-patterns.html
weight: 100
---

{{% ssummary page %}}This article illustrates custom Matching Implementation.{{% /ssummary %}}
{{% tip %}}
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.0<br/>
**Last Update:** January 2010<br/>
{{% /tip %}}

# Overview

Usually you index and execute queries using primitive fields (long, float, string, etc). The fields may be within the root level of the space object, or embedded within [nested objects]({{%latestjavaurl%}}/query-sql.html#nested-0bject-query) within the space object. You may construct a query using a template object or SQL to specify the criteria you would like to use when the matching phase is performed within the space when looking for the relevant objects.

In some cases you might want to use a custom data type with a custom business logic to find matching objects within the space, rather the usual [primitive data type](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) comparison. To allow the space to invoke your business logic when the matching process is conducted, the [Comparable](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Comparable.html) interface should be implemented for a class that stores the data you would like to use with your custom business logic.

{{% tip %}}
The [Comparable](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Comparable.html) implementation should not be done for the space class itself, but for one of its fields.
{{% /tip %}}

Such custom business logic might be useful when comparing vector data (2 dimensional Cartesian space). These may represent sound, maps, pictures or any other 2 or 3 dimensional artifacts. You may use this technique to query data based on any other mathematical or financial related formulas such as [Time value of money](http://en.wikipedia.org/wiki/Time_value_of_money) like Present Value of a Cash Flow Series, Future Value of a Cash Flow Series, etc. Other areas where such custom matching is relevant, are Pattern recognition, Sequence analysis, Surveillance, Forensic, Social network behavior etc.

{{% exclamation %}} In some cases, you may index the data to speed up the custom matching process. To enable this, you should index the field so that its class implements the `Comparable` interface using the `EXTENDED` index type as part of the space class. See the [Indexing]({{%latestjavaurl%}}/indexing.html) page for additional information about how to enable the `EXTENDED` index. Indexing the custom type field **should be used carefully since it does not support** a `Comparable.compareTo` implementation that performs relative-based matching, as demonstrated by the following example.

# Vector Compare Example
The following example illustrates a business logic implementation used to query for vector data (an array of Integer values), using the [Euclidean distance](http://en.wikipedia.org/wiki/Euclidean_distance) formula:

{{% panel %}}![EuclideanDistance.jpg](/attachment_files/sbp/EuclideanDistance.jpg){{% /panel %}}

The object that holds the array implements the [Comparable](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Comparable.html) interface. The Space class has a getter method for this field indexing, using the `EXTENDED` index. The actual query involves indexed fields for several sample data points within the vector, together with the custom field:


```java
SQLQuery<Vector> query= new SQLQuery<Vector>(Vector.class ,
		"(x0 > ? AND x0 < ? ) " +
		"AND (x1 > ? AND x1 < ? ) " +
		"AND (x2 > ? AND x2 < ? ) " +
		"AND (x3 > ? AND x3 < ? ) " +
		"AND (x4 > ? AND x4 < ? ) " +
		"AND (x5 > ? AND x5 < ? ) " +
		"AND (x6 > ? AND x6 < ? ) " +
		"AND (x7 > ? AND x7 < ? ) " +
		"AND (x8 > ? AND x8 < ? ) " +
		"AND (x9 > ? AND x9 < ? ) " +
		"AND (x10 > ? AND x10 < ? ) " +
		"AND vectordata <= ?");
```

Here is an example of a target vector, and a matching vector found using the custom matching implementation illustrated below:

{{% panel %}}![custommatching.jpg](/attachment_files/sbp/custommatching.jpg){{% /panel %}}

{{% tip %}}
To scale the system you should deploy the space using the [partitioned cluster schema](/product_overview/terminology.html). This will allow queries (i.e. matching) to be executed across all the partitions in parallel, speeding up the query execution time.
{{% /tip %}}

See **The Application** tab for the full query usage. This allows the `Comparable.compareTo` implementation to be performed on a smaller candidate subset of objects.

{{%tabs%}}

{{%tab "  The Comparable implementation "%}}


```java
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class VectorData implements Serializable, Comparable <VectorData>{

	VectorData (int[] data)
	{
		this.data = data;
	}

	private static final double THRESHOLD = 200;
	private static final int LARGERTHAN = 1;
	private static final int EQUALS = 0;
	private static final int LESSTHAN = -1;
	int data[];

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public int compareTo(VectorData candidate) throws ClassCastException {
		int result = LARGERTHAN;
		double sum = 0;
		double dist = 0;
		int[] candidateData = candidate.getData();

		for (int j = 0; j < Vector.VECTOR_SIZE; j++) {
			sum += Math.pow(candidateData[j] - this.data[j], 2);
		}
		dist = Math.pow(sum, 0.5);

		if (dist <= THRESHOLD){
			result = EQUALS;
		}

		return result ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VectorData other = (VectorData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}
}
```

{{% /tab %}}

{{%tab "  The Space Class "%}}


```java
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.annotation.pojo.SpaceProperty.IndexType;

@SpaceClass
public class Vector {
	public Vector (){}

	static public int VECTOR_SIZE = 101;

	String name;
	VectorData vectordata;

	@SpaceRouting
	@SpaceId
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public VectorData getVectordata() {
		return vectordata;
	}
	public void setVectordata(VectorData vectordata) {
		this.vectordata = vectordata;
	}

	@SpaceProperty ( index = IndexType.EXTENDED)
	Integer getX0()
	{
		if (vectordata !=null)
			return vectordata.data[0] ;
		else
			return null;
	}
	....
	@SpaceProperty ( index = IndexType.EXTENDED)
	Integer getX10()
	{
		if (vectordata !=null)
			return vectordata.data[100] ;
		else
			return null;
	}

	void setX0(Integer x){};
	....
	void setX10(Integer x){};
}
```

{{% /tab %}}

{{%tab "  The Application "%}}


```java
static Random rand = new Random();
static int[] baseVector =null;
public static void main(String[] args) throws Exception{
	GigaSpace gigaspace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("/./space")).gigaSpace();
	System.out.println("Writing Vector objects into the space");
	int max = 1000;
	Vector[] vs = new Vector[max];
	for (int i=0;i<max;i++)
	{
		Vector v = new Vector();
		v.setName(i+"");
		v.setVectordata(new VectorData(getRandomVector()));
		vs[i] = v;
	}
	gigaspace.writeMultiple(vs);

	double MIN_THRESHOLD = 0.85;
	double MAX_THRESHOLD = 1.15;

	SQLQuery<Vector> query= new SQLQuery<Vector>(Vector.class ,
		"(x0 > ? AND x0 < ? ) " +
		"AND (x1 > ? AND x1 < ? ) " +
		"AND (x2 > ? AND x2 < ? ) " +
		"AND (x3 > ? AND x3 < ? ) " +
		"AND (x4 > ? AND x4 < ? ) " +
		"AND (x5 > ? AND x5 < ? ) " +
		"AND (x6 > ? AND x6 < ? ) " +
		"AND (x7 > ? AND x7 < ? ) " +
		"AND (x8 > ? AND x8 < ? ) " +
		"AND (x9 > ? AND x9 < ? ) " +
		"AND (x10 > ? AND x10 < ? ) " +
		"AND vectordata <= ?");

	VectorData vec = new VectorData(getRandomVector());

	query.setParameter(1,(int)(MIN_THRESHOLD * vec.getData()[0]));
	query.setParameter(2,(int)(MAX_THRESHOLD * vec.getData()[0]));
	...
	query.setParameter(21,(int)(MIN_THRESHOLD * vec.getData()[100]));
	query.setParameter(22,(int)(MAX_THRESHOLD * vec.getData()[100]));

	query.setParameter(23,vec);

	Vector matchingVectors[] = gigaspace.readMultiple(query ,Integer.MAX_VALUE);
}

static int[] getRandomVector()
{
	int data[] = new int[Vector.VECTOR_SIZE];
	for (int j=0;j<Vector.VECTOR_SIZE;j++)
	{
		if (baseVector != null)
			data[j] = baseVector [j] + rand.nextInt(50);
		else
			data[j] = rand.nextInt(100);

	}
	return data;
}
```

{{% /tab %}}

{{% /tabs %}}
