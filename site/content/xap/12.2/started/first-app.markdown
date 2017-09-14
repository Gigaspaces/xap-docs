---
type: post122
title:  Developing Your First Application
categories: XAP122GS, IEE
weight: 200
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

In this tutorial, you will learn how to develop your first InsightEdge application to read and write from/to the Data Grid. This tutorial assumes that you have basic knowledge of {{%exurl "Apache Spark""http://spark.apache.org/docs/latest/index.html"%}}.



{{%refer%}}
For installation and launching the InsightEdge cluster, refer to [Quick Start](./quick_start.html) for the minimum cluster setup.
{{%/refer%}}


# Project dependencies

InsightEdge {{%version "xap-version"%}} runs on Spark {{%version "spark-version"%}} and Scala {{%version "scala-version"%}}. These dependencies will be included when you depend on the InsightEdge artifacts.

InsightEdge jars are not published to Maven Central Repository yet. To install artifacts to your local Maven repository, make sure you have {{%exurl "Maven""https://maven.apache.org/"%}} installed and then run:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/sbin/insightedge-maven.sh
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\sbin\insightedge-maven.cmd
```
{{%/tab%}}
{{%/tabs%}}

For SBT projects include the following:

```
resolvers += Resolver.mavenLocal
resolvers += "Openspaces Maven Repository" at "http://maven-repository.openspaces.org"

libraryDependencies += "org.gigaspaces.insightedge" % "insightedge-core" % "{{%version "maven-version"%}}" % "provided" exclude("javax.jms", "jms")
```

And if you are building with Maven:

```xml
<dependency>
    <groupId>org.gigaspaces.insightedge</groupId>
    <artifactId>insightedge-core</artifactId>
    <version>{{%version "maven-version"%}}</version>
    <scope>provided</scope>
</dependency>
```

{{%note%}}
InsightEdge jars are already packed into InsightEdge distribution and are automatically loaded with your application if you submit them with `insightedge-submit` script or run the Web Notebook. Therefore you don't need to pack them into your uber jar. But if you want to run Spark in a `local[*]` mode, the dependencies should be declared with the `compile` scope.
{{%/note%}}

# Developing the Spark application

InsightEdge provides an extension to the regular Spark API.

{{%refer%}}
Please refer to {{%exurl "Self-Contained Applications""http://spark.apache.org/docs/latest/quick-start.html#self-contained-applications"%}} if you are new to Spark.
{{%/refer%}}

`InsightEdgeConfig` is the starting point in connecting Spark with the Data Grid.

Create the `InsightEdgeConfig` and the `SparkContext`:

{{%tabs%}}
{{%tab "Scala" %}}
```scala
import org.insightedge.spark.context.InsightEdgeConfig
import org.insightedge.spark.implicits.all._

val gsConfig = InsightEdgeConfig("insightedge-space", Some("insightedge"), Some("127.0.0.1:4174"))
val sparkConf = new SparkConf().setAppName("sample-app").setMaster("spark://127.0.0.1:7077").setInsightEdgeConfig(gsConfig)
val sc = new SparkContext(sparkConf)
```
{{%/tab%}}
{{%/tabs%}}

{{%note%}}
It is important to import `org.insightedge.spark.implicits.all._`, this will enable Data Grid specific API.

"insightedge-space", "insightedge" and "127.0.0.1:4174" are the default Data Grid settings

When you are running Spark applications from the Web Notebook, the `InsightEdgeConfig` is created implicitly with the properties defined in the Spark interpreter.

{{%/note%}}

# Modeling Data Grid objects

Create a case class `Product.scala` to represent a Product entity in the Data Grid:

```scala
import org.insightedge.scala.annotation._
import scala.beans.{BeanProperty, BooleanBeanProperty}

case class Product(   
   @BeanProperty @SpaceId var id: Long,
   @BeanProperty var description: String,
   @BeanProperty var quantity: Int,   
   @BooleanBeanProperty var featuredProduct: Boolean
) {
    def this() = this(-1, null, -1, false)
}
```

# Saving to Data Grid

To save Spark RDD just use `saveToGrid` method.

```scala
val rdd = sc.parallelize(1 to 1000).map(i => Product(i, "Description of product " + i, Random.nextInt(10), Random.nextBoolean()))
rdd.saveToGrid()
```

# Loading and analyzing data from Data Grid

Use the `gridRdd` method of the `SparkContext` to view Data Grid objects as Spark `RDD`

```scala
val gridRdd = sc.gridRdd[Product]()
println("total products quantity: " + gridRdd.map(_.quantity).sum())
```

# Closing context
When you are done, close the Spark context and all connections to Data Grid with

```scala
sc.stopInsightEdgeContext()
```

Under the hood it will call regular Spark's `sc.stop()`, so no need to call it manually.

# Running your Spark application
After you packaged a jar, submit the Spark job via `insightedge-submit` instead of `spark-submit`.

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge-submit --class com.insightedge.spark.example.YourMainClass --master spark://127.0.0.1:7077 path/to/jar/insightedge-examples.jar
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\bin\insightedge-submit --class com.insightedge.spark.example.YourMainClass --master spark://127.0.0.1:7077 path\to\jar\insightedge-examples.jar
```
{{%/tab%}}
{{%/tabs%}}