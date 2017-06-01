---
type: post100
title:  Scala
categories: XAP100
parent: interoperability-overview.html
weight: 400
---

Several extensions to the XAP API have been introduced to provide a more natural way of combining Scala with XAP.


<br>

{{%fpanel%}}
[Constructor based properties](./scala-constructor-based-properties.html){{<wbr>}}
The com.gigaspaces.annotation.pojo.SpaceClassConstructor annotation on a constructor will cause the data class properties analysis to be based on properties found in the constructor (instead of getters/setters).

[Enhanced REPL](./scala-enhanced-repl.html){{<wbr>}}
Following is a short demo of what can be done with the XAP scala shell. It should be noted that this shell is a regular Scala REPL with some initial imports and initialization code.

[Predicate based queries](./scala-predicate-based-queries.html){{<wbr>}}
Support for predicate based queries on the GigaSpace proxy has been in added. This support is based on the new macros feature introduced in Scala.

[Scripting Executor](./scala-scripting-executor.html){{<wbr>}}
Dynamic Language Tasks has been extended and now supports Scala based script execution.

[Task Execution](./scala-task-execution.html){{<wbr>}}
A wrapper around the XAP API provides some sugaring on top of the GigaSpace#execute methods.
{{%/fpanel%}}

<br>

{{% info %}}
The Scala version required in order to use the Scala Openspaces extension is 2.10.x
{{% /info %}}

Assuming there is a scala installation under `$SCALA_HOME`, the jars under `$SCALA_HOME/lib` should be copied to `$GS_HOME/lib/platform/scala/lib`.
Another options is to change `setenv.{bat,sh`} so that `$SCALA_JARS` points to `$SCALA_HOME/lib`.
