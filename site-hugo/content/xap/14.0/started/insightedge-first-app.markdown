---
type: post140
title:  Developing Your First Application
categories: XAP140GS, OSS
parent: insightedge-basics.html
weight: 400
---

This topic explains how to create an InsightEdge application that can read and write from/to the Data Grid. You should have a basic knowledge of {{%exurl "Apache Spark""http://spark.apache.org/docs/latest/index.html"%}}.

{{%refer%}}
For instructions on how to install a minimum InsightEdge cluster setup and launch it, refer to [Starting InsightEdge](./insightedge-local-setup.html).
{{%/refer%}}

# Project Dependencies

InsightEdge {{%version "xap-version"%}} runs on Spark {{%version "spark-version"%}} and Scala {{%version "scala-version"%}}. These dependencies will be included when you depend on the InsightEdge artifacts.

InsightEdge .jars are not published to Maven Central Repository yet. To install Maven artifacts run the following command from the '&lt;XAP HOME&gt;/insightedge/tools/maven' directory:

```bash
insightedge-maven
```

For SBT projects include the following:

```bash
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

{{%note "Info"%}}
InsightEdge .jars are already packed in the InsightEdge distribution, and are automatically loaded with your application if you submit them with `insightedge-submit` script or run the Web Notebook. As such, there is no need to pack them into your uber .jar. However, if you want to run Spark in `local[*]` mode, the dependencies should be declared with the `compile` scope.
{{%/note%}}

# Developing a Spark Application

InsightEdge provides an extension to the regular Spark API.

{{%refer%}}
Read the {{%exurl "Self-Contained Applications""http://spark.apache.org/docs/latest/quick-start.html#self-contained-applications"%}} topic in the Apache Spark documentation if you are new to Spark.
{{%/refer%}}

`InsightEdgeConfig` is the starting point in connecting Spark with the Data Grid. Create the `InsightEdgeConfig` and the `SparkContext`:

{{%tabs%}}
{{%tab "Scala" %}}
```scala
import org.insightedge.spark.context.InsightEdgeConfig
import org.insightedge.spark.implicits.all._

val sparkConf = new SparkConf()
    .setAppName("sample-app")
	.setMaster("spark://127.0.0.1:7077")
	.setInsightEdgeConfig(InsightEdgeConfig("insightedge-space"))
val sc = new SparkContext(sparkConf)
```
{{%/tab%}}
{{%/tabs%}}

{{%note "Info"e%}}
It is important to import `org.insightedge.spark.implicits.all._` to enable the Data Grid specific API.

`insightedge-space` is the default Data Grid name that the demo mode starts automatically.

When you are running Spark applications from the Web Notebook, `InsightEdgeConfig` is created implicitly with the properties defined in the Spark interpreter.

{{%/note%}}

# Modeling Data Grid Objects

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

# Saving to the Data Grid

To save a Spark RDD,  use the `saveToGrid` method.

```scala
val rdd = sc.parallelize(1 to 1000).map(i => Product(i, "Description of product " + i, Random.nextInt(10), Random.nextBoolean()))
rdd.saveToGrid()
```

# Loading and Analyzing Data from the Data Grid

Use the `gridRdd` method of the `SparkContext` to view Data Grid objects as Spark RDDs.

```scala
val gridRdd = sc.gridRdd[Product]()
println("total products quantity: " + gridRdd.map(_.quantity).sum())
```

# Closing the Spark Context
When you are done, close the Spark context and all connections to Data Grid with the following command:

```scala
sc.stopInsightEdgeContext()
```

Under the hood, this will call the regular Spark `sc.stop()` command, so there is no need to call it manually.

# Running your Spark Application

After you have packaged a .jar, submit the Spark job via `insightedge-submit` located in `<XAP Home>/insightedge/bin` instead of `spark-submit` as follows:

```bash
insightedge-submit --class com.insightedge.spark.example.YourMainClass --master spark://127.0.0.1:7077 path/to/jar/insightedge-examples.jar
```
