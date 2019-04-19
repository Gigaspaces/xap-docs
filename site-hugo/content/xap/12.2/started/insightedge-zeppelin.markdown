---
type: post122
title: Web Notebook
categories: XAP122GS, OSS
parent: insightedge-basics.html
weight: 200
canonical: auto
---

This section describes how to use the interactive Apache Zeppelin Web Notebook.

# Starting the Web Notebook

The Web Notebook can be started in any of the following ways:

* In `demo` mode, the Web Notebook is started automatically at {{%exurl "localhost:9090""http://localhost:9090"%}}. See [InsightEdge Script](./insightedge-script.html) for information about `demo` mode.
* Start the Web Notebook manually at any time by running the following command from the `<XAP HOME>/insightedge/bin` directory:

```bash
insightedge run --zeppelin
```

Once Zeppeling is running, you can browse to {{%exurl "localhost:9090""http://localhost:9090"%}} and start playing with the pre-built notebooks:

{{%align center%}}
![image](/attachment_files/Zeppelin_examples_100.png)
{{%/align%}}

# Configuring the Web Notebook

InsightEdge-specific settings can be configured in *Interpreter* menu -> *Spark* interpreter. Important settings include the following properties for connecting Spark with the Data Grid:

* `insightedge.group`
* `insightedge.locator`
* `insightedge.spaceName`

These properties are transparently translated into `InsightEdgeConfig` to establish a connection between Spark and the Data Grid.

{{%refer%}}
Refer to [Connecting to the Data Grid](../dev-java/insightedge-connecting.html) for more details about the connection properties.
{{%/refer%}}

# Using the Web Notebook

The Web Notebook comes with example notes. We recommend that you review them, and then use them as a template for your own notes. There are several things you should take into account.

* The Data Grid model can be declared in a notebook using the `%define` interpreter:

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

* You can  load external .jars from the Spark interpreter settings, or with the `z.load("/path/to.jar")` command:

```scala
%dep
z.load("./insightedge/examples/jars/insightedge-examples.jar")
```

{{%refer%}}
For more details, please, refer to {{%exurl "Zeppelin Dependency Management""https://zeppelin.apache.org/docs/latest/interpreter/spark.html#dependency-management"%}}
{{%/refer%}}

* You must load your dependencies before you start using the `SparkContext` (`sc`) command. If you want to redefine the model or load another .jar after `SparkContext` has already started, you will have to reload the Spark interpreter.
