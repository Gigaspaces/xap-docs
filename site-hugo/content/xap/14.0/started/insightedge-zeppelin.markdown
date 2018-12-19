---
type: post140
title: Using the InsightEdge Web Notebook
categories: XAP140GS, OSS
parent: insightedge-basics.html
weight: 200
---

This section describes how to use the interactive Apache Zeppelin Web Notebook with InsightEdge.

# Starting the Web Notebook

The InsightEdge Apache Zeppelin web notebook can be started in any of the following ways:

* Run the `insightedge demo` command; the web notebook is started automatically at {{%exurl "localhost:9090""http://localhost:9090"%}}. 
* Start the web notebook manually at any time by running `zeppelin.sh/cmd` from the `<XAP HOME>/insightedge/zeppelin/bin` directory.

When Apache Zeppelin is running, you can browse to {{%exurl "localhost:9090""http://localhost:9090"%}} and start exploring the pre-built notebooks:

{{%align center%}}
![image](/attachment_files/Zeppelin_examples_100.png)
{{%/align%}}

# Connecting a New Apache Zeppelin Notebook to InsightEdge

If you want to create a new Apache Zeppelin web notebook instead of using the example notebooks that come packaged with InsightEdge, there are two ways to run a Zeppelin notebook against InsightEdge:

- Write a Spark application that reads and writes data to the data grid using the Spark context.
- Write an SQL query that is interpreted by the InsightEdge JDBC interpreter (which is run directly against the data grid).

## Initializing the Spark Context

In order to establish the connection between Apache Zeppelin through Apache Spark and then to the data grid, each notebook should start with a paragraph that injects the InsightEdge settings to the Spark context. Important settings include the following properties:

* `spaceName`
* `lookupGroups`
* `lookupLocators`

These properties are injected through the following in the notebook:

- `InsightEdge` class:

```scala
    case class InsightEdgeConfig(
                                 spaceName: String,
                                 lookupGroups: Option[String] = None,
                                 lookupLocators: Option[String] = None)
```

- Apache Zeppelin mandatory initialization paragraph:

```scala
    %spark
    import org.insightedge.spark.implicits.all._
    import org.insightedge.spark.context.InsightEdgeConfig
    
    //spaceName is required, other two parameters are optional
    val ieConfig = new InsightEdgeConfig(spaceName = "mySpace", lookupGroups = None, lookupLocators = None)
    
    //sc is the spark context initalized by zeppelin
    sc.initializeInsightEdgeContext(ieConfig)
```
## InsightEdge JDBC Interpreter

Apache Zeppelin uses interpreters to compile and run paragraphs. The Apache Zeppelin instance that is packaged with InsightEdge comes with a custom JDBC interpreter that enables running SQL queries directly on the data grid using the web notebook.  The queries are executed by the [InsightEdge SQL Driver](../dev-java/sql-query-intro.html).   

### Configuring the JDBC Interpreter

The JDBC interpreter connects to the data grid via a JDBC URL. To configure the URL value to point to the data grid, do the following:

1. In the Apache Zeppelin web interface, navigate to the Interpreters section.
1. Select the insightedge_jdbc interpreter, and click **Edit**.
1. Edit the `default.url` parameter as follows: `jdbc:insightedge:spaceName=<space-name>`
1. Save the changes you made to the interpreter.

### Querying the Data Grid in Notebooks

When the JDBC interpreter is properly configured, Zeppelin paragraphs that are bound to the `%insightedge_jdbc` interpreter can run SQL queries directly on the data grid.

### Querying Multiple JDBC Data Sources

You can configure the JDBC interpreter to query multiple JDBC data sources (in addition to the default data source). You define the additional data sources in the notebook by adding the following properties to the interpreter for each data source:

- `<data-source-name>.driver` - The class of JDBC driver applicable to the data source
- `<data-source-name>.url` - The JDBC connection string to the data source

After saving your changes, Zeppelin paragraphs starting with `%insightedge_jdbc(<data-source-name>)` can run queries on the data sources that you added.
 
For example, let's say we want to query 3 data grid sources:
 
- "grid_A" (this is the default data source)
- "grid_B" 
- "grid_C"
 
Configure Apache Zeppelin in the interpreter section to enable querying one or more of these data sources with the InsightEdge JDBC interpreter. The following key/value pairs enable querying the specified data sources: 

**grid_A** configuration: 

- Key = `default.driver`, Value = `com.gigaspaces.jdbc.Driver`
- Key = `default.url`, Value = `insightedge:jdbc:url:spaceName=grid_A`

Paragraphs starting with `%insightedge_jdbc` will query Grid A.
 
**grid_B** configuration: 

- Key = `B.driver`, Value = `com.gigaspaces.jdbc.Driver`
- Key = `B.url`, Value = `insightedge:jdbc:url:spaceName=grid_B`

Paragraphs starting with `%insightedge_jdbc` will query Grid B.

**grid_C** configuration: 

- Key = `C.driver`, Value = `com.gigaspaces.jdbc.Driver`
- Key = `C.url`, Value = `insightedge:jdbc:url:spaceName=grid_C`

Paragraphs starting with `%insightedge_jdbc` will query Grid C.
 
# Using the Web Notebook

The Apache Zeppelin web notebook comes with sample notes. We recommend that you review them, and then use them as a template for your own notes. There are several things you should take into account.

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

* You can load external .JARs from the Spark interpreter settings, or with the `z.load("/path/to.jar")` command:

	```scala
	%dep
	z.load("./insightedge/examples/jars/insightedge-examples.jar")
	```

 {{%refer%}}
 For more details, refer to {{%exurl "Zeppelin Dependency Management""https://zeppelin.apache.org/docs/latest/interpreter/spark.html#dependency-management"%}}.
 {{%/refer%}}

* You must load your dependencies before you start using the `SparkContext` (`sc`) command. If you want to redefine the model or load another .JAR after `SparkContext` has already started, you must reload the Spark interpreter.
