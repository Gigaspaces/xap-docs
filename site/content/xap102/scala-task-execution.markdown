---
type: post102
title:  Scala Task Execution
categories: XAP102
parent: scala.html
weight: 500
---






A wrapper around the `XAP` API provides some sugaring on top of the `GigaSpace#execute` methods.

# Usage

Import `org.openspaces.scala.core.ScalaGigaSpacesImplicits.ScalaEnhancedGigaSpaceWrapper` into scope to use the methods demonstrated below.

Some examples:


```scala
/** Import GigaSpace implicits into scope */
import org.openspaces.scala.core.ScalaGigaSpacesImplicits._

...

gigaSpace write Data(id = 1, routing = 2, data = "some data")
gigaSpace write Data(id = 2, routing = 3, data = "some other data")

val asyncFuture1 = gigaSpace.execute { gigaSpace: GigaSpace =>
  gigaSpace.readById(classOf[Data], 1l)
}

println("Execute1 result: " + asyncFuture1.get())

val asyncFuture2 = gigaSpace.execute(
  { gigaSpace: GigaSpace => gigaSpace.read(Data()).data } /* map */,
  { results: Seq[AsyncResult[String]] => results.map { _.getResult() }.mkString } /* reduce */
)

println("Map reduce result: " + asyncFuture2.get())
```
