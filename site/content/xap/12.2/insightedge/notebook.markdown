---
type: post122
title:  Zeppelin Notebook
categories: XAP122I9E, IEE
weight: 1200
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

This section describes how to use the interactive Web Notebook.


# Starting Web Notebook

There are several options how you can start the Web Notebook:

* in a `demo` mode, Web Notebook is started automatically at {{%exurl "127.0.0.1:8090""http://127.0.0.1:8090"%}}. Refer to a [Quick Start](./quick_start.html) for `demo` mode details.
* when running a [remote cluster](./cluster_setup.html), Web Notebook is started on a master host on port `8090`

You can also start and stop Web Notebook any time manually with:
```bash
./insightedge/sbin/start-zeppelin.sh
./insightedge/sbin/stop-zeppelin.sh
```

# Web Notebook configuration

Any InsightEdge specific settings can be configured in *Interpreter* menu -> *Spark* interpreter. The important settings include details on connecting Spark with the Data Grid:

* `insightedge.group`
* `insightedge.locator`
* `insightedge.spaceName`

These properties are transparently translated into `InsightEdgeConfig` to establish a connection between Spark and the Data Grid.

{{%refer%}}
Please refer to [Connecting to Data Grid](./connecting.html) for more details on the connection properties.
{{%/refer%}}

# Using Web Notebook

The Web Notebook comes with example notes, so we recommend to start exploring them and use them as a template for your own notes. There are several things you should take into account.

Data Grid model can be declared in a notebook using `%define` interpreter:

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

You can also load external jars from the Spark interpreter settings or with `z.load("/path/to.jar")`:

```scala
%dep
z.load("./quick-start/scala/insightedge-examples.jar")
```

{{%refer%}}
For more details, please, refer to [Zeppelin Dependency Management](https://zeppelin.apache.org/docs/latest/interpreter/spark.html#dependency-management)
{{%/refer%}}

Note that you have to load your dependencies before you start using the `SparkContext` (`sc`). If you want to redefine the model or load another jar while `SparkContext` has already started, you have to reload the Spark interpreter.
