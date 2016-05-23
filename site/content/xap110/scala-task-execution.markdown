---
type: post110
title:  Scala Task Execution
categories: XAP110
parent: scala.html
weight: 500
---






A wrapper around the `XAP` API provides some sugaring on top of the `GigaSpace#execute` methods.

# Usage

{{%vbar%}}
Import the following into scope to use the methods demonstrated below.

import com.gigaspaces.async.AsyncResult
import org.openspaces.scala.core.ScalaGigaSpacesImplicits.ScalaEnhancedGigaSpaceWrapper 
{{%/vbar%}}

Some examples:


```scala
/** Import GigaSpace implicits into scope */
import com.gigaspaces.async.AsyncResult
import org.openspaces.scala.core.ScalaGigaSpacesImplicits._

...
val gsm = admin.getGridServiceManagers.waitForAtLeastOne
gsm.deploy(new org.openspaces.admin.space.SpaceDeployment("mySpace"))
val Some(gigaSpace) = getGigaSpace("mySpace")
case class Data @SpaceClassConstructor() ( @BeanProperty @SpaceId @SpaceProperty(nullValue = "-1") id: Long = -1, @BeanProperty @SpaceRouting @SpaceProperty(nullValue = "-1") routing: Long = -1, @BeanProperty data: String = null )
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
