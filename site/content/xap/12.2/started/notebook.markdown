---
type: post122
title:  Apache Zeppelin Web Notebook
categories: XAP122GS, IEE
weight: 1000
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This section describes how to use the interactive Apache Zeppelin Web Notebook.


# Starting the Web Notebook

The Web Notebook can be started in any of the following ways:


* In `demo` mode, the Web Notebook is started automatically at {{%exurl "127.0.0.1:9090""http://127.0.0.1:9090"%}}. (Refer to a [Starting InsightEdge](./quick_start.html) for information about `demo` mode.)
* When running a [remote cluster](./cluster_setup.html), the Web Notebook is started on a master host, on port `9090`.
* Start and stop the Web Notebook manually at any time using the following commands:

```bash
./insightedge/sbin/start-zeppelin.sh
./insightedge/sbin/stop-zeppelin.sh
```

# Configuring the Web Notebook

InsightEdge-specific settings can be configured in *Interpreter* menu -> *Spark* interpreter. Important settings include the following properties for connecting Spark with the Data Grid:

* `insightedge.group`
* `insightedge.locator`
* `insightedge.spaceName`

These properties are transparently translated into `InsightEdgeConfig` to establish a connection between Spark and the Data Grid.

{{%refer%}}
Refer to [Connecting to the Data Grid](./connecting.html) for more details about the connection properties.
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
z.load("./quick-start/scala/insightedge-examples.jar")
```

{{%refer%}}
For more details, please, refer to [Zeppelin Dependency Management](https://zeppelin.apache.org/docs/latest/interpreter/spark.html#dependency-management)
{{%/refer%}}

* You must load your dependencies before you start using the `SparkContext` (`sc`) command. If you want to redefine the model or load another .jar after `SparkContext` has already started, you will have to reload the Spark interpreter.
