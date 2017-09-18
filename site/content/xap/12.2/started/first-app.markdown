---
type: post122
title:  Developing Your First Application
categories: XAP122GS, IEE
weight: 200
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This topic explains how to create an InsightEdge application that can read and write from/to the Data Grid. You should have a basic knowledge of {{%exurl "Apache Spark""http://spark.apache.org/docs/latest/index.html"%}}.



{{%refer%}}
For instructions on how to install a minimum InsightEdge cluster setup and launch it, refer to [Starting InsightEdge](./quick_start.html).
{{%/refer%}}


# Project Dependencies

InsightEdge {{%version "xap-version"%}} runs on Spark {{%version "spark-version"%}} and Scala {{%version "scala-version"%}}. These dependencies will be included when you depend on the InsightEdge artifacts.

InsightEdge .jars are not published to Maven Central Repository yet. To install artifacts to your local Maven repository, make sure you have {{%exurl "Maven""https://maven.apache.org/"%}} installed and then run:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/tools/maven/insightedge-maven.sh
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\tools\maven\insightedge-maven.cmd
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

{{%info "Info..."%}}
InsightEdge .jars are already packed in the InsightEdge distribution, and are automatically loaded with your application if you submit them with `insightedge-submit` script or run the Web Notebook. As such, there is no need to pack them into your uber .jar. However, if you want to run Spark in `local[*]` mode, the dependencies should be declared with the `compile` scope.
{{%/info%}}

# Developing a Spark Application

InsightEdge provides an extension to the regular Spark API.

{{%refer "See also..."%}}
Read the {{%exurl "Self-Contained Applications""http://spark.apache.org/docs/latest/quick-start.html#self-contained-applications"%}} topic in the Apache Spark documentation if you are new to Spark.
{{%/refer%}}

`InsightEdgeConfig` is the starting point in connecting Spark with the Data Grid. Create the `InsightEdgeConfig` and the `SparkContext`:

{{%tabs%}}
{{%tab "Scala" %}}
```scala
import org.insightedge.spark.context.InsightEdgeConfig
import org.insightedge.spark.implicits.all._

val gsConfig = InsightEdgeConfig("insightedge-space", Some("your_lookup_groups"), Some("your_lookup_locators"))
val sparkConf = new SparkConf().setAppName("sample-app").setMaster("spark://127.0.0.1:7077").setInsightEdgeConfig(gsConfig)
val sc = new SparkContext(sparkConf)
```
{{%/tab%}}
{{%/tabs%}}

{{%info "Info..."e%}}
It is important to import `org.insightedge.spark.implicits.all._` to enable the Data Grid specific API.
<br />"insightedge-space" is the default Data Grid name that the demo mode starts automatically. The lookup groups and locators can be configured by XAP_LOOKUP_GROUPS and XAP_LOOKUP_LOCATORS environment variables. If they are not set, XAP defaults are used (groups: "xap-12.2.0", and locators: empty).

When you are running Spark applications from the Web Notebook, `InsightEdgeConfig` is created implicitly with the properties defined in the Spark interpreter.

{{%/info%}}

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
After you have packaged a .jar, submit the Spark job via `insightedge-submit` instead of `spark-submit` as follows:

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