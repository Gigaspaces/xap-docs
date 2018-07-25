---
type: post123
title: Using the InsightEdge Web Notebook
categories: XAP123GS, OSS
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

# Configuring the Web Notebook

InsightEdge-specific settings can be configured in the *Interpreter* menu -> *Spark* interpreter. Important settings include the following properties for connecting Spark with the data grid:

* `insightedge.group`
* `insightedge.locator`
* `insightedge.spaceName`

These properties are transparently translated into `InsightEdgeConfig` to establish a connection between Spark and the data grid.

{{%refer%}}
Refer to [Connecting to the Data Grid](../dev-java/insightedge-connecting.html) for more details about the connection properties.
{{%/refer%}}

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
