---
type: post140
title: Using the InsightEdge Web Notebook
categories: XAP140GS, OSS
parent: insightedge-basics.html
weight: 200
---

This section describes how to use the interactive Apache Zeppelin Web Notebook.

# Starting the Web Notebook

The Web Notebook can be started in any of the following ways:

* Rn the `insightedge demo` command; the Web Notebook is started automatically at {{%exurl "localhost:9090""http://localhost:9090"%}}. 
* Start the Web Notebook manually at any time by running `zeppelin.sh/cmd` from the `<XAP HOME>/insightedge/zeppelin/bin` directory.

When Zeppelin is running, you can browse to {{%exurl "localhost:9090""http://localhost:9090"%}} and start exploring the pre-built notebooks:

{{%align center%}}
![image](/attachment_files/Zeppelin_examples_100.png)
{{%/align%}}

# Connecting Zeppelin to InsightEdge

## Spark Context Initialization

In order to establish the connection Zeppelin -> Spark -> Data Grid,  each notebook should start with a paragraph that injects the InsightEdge settings to the Spark context. Important settings include the following properties:

* `spaceName`
* `lookupGroups`
* `lookupLocators`

And are injected through the `InsightEdge` class

```scala
    case class InsightEdgeConfig(
                                 spaceName: String,
                                 lookupGroups: Option[String] = None,
                                 lookupLocators: Option[String] = None)
```

The Zeppelin mandatory initilization paragraph:

```scala
    %spark
    import org.insightedge.spark.implicits.all._
    import org.insightedge.spark.context.InsightEdgeConfig
    
    //spaceName is required, other two parameters are optional
    val ieConfig = new InsightEdgeConfig(spaceName = "mySpace", lookupGroups = None, lookupLocators = None)
    
    //sc is the spark context initalized by zeppelin
    sc.initializeInsightEdgeContext(ieConfig)
```
## InsightEdge JDBC Zeppelin Interpreter

Zeppelin uses interpreters to compile and run paragraphs. InsightEdge Zeppelin comes with a custom JDBC interpreter, that enables running SQL queries on the data grid in notebooks. 
The queries are executed by [InsightEdge SQL Driver](sql-query-intro.html) 

### JDBC Interpreter Configuration

The JDBC interpreter connects to the data grid via a jdbc url. To configure the url value to point to the kubernetes data grid, perform the following steps:

- In Zeppelin web UI, go to the Interpreters section
- Find the insightedge_jdbc interpreter and press edit
- Edit the `default.url` parameter to the following form `jdbc:insightedge:spaceName=<release-name>?locators=<release name>-<headless service name>`
- Save interpreter changes

### Querying the Data Grid In Notebooks

Once the JDBC interpreter is properly configured, Zeppelin paragraphs bound to the `%insightedge_jdbc`  interpreter can run SQL queries on the data grid

# Using the Web Notebook

The Web Notebook comes with sample notes. We recommend that you review them, and then use them as a template for your own notes. There are several things you should take into account.

* The data grid model can be declared in a notebook using the `%define` interpreter:

	```scala
	%define
	package model.v1

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

	```scala
	%spark
	import model.v1._
	```

* You can  load external .JARs from the Spark interpreter settings, or with the `z.load("/path/to.jar")` command:

	```scala
	%dep
	z.load("./insightedge/examples/jars/insightedge-examples.jar")
	```

	{{%refer%}}
	For more details, refer to {{%exurl "Zeppelin Dependency Management""https://zeppelin.apache.org/docs/latest/interpreter/spark.html#dependency-management"%}}.
	{{%/refer%}}

* You must load your dependencies before you start using the `SparkContext` (`sc`) command. If you want to redefine the model or load another .JAR after `SparkContext` has already started, you must reload the Spark interpreter.
