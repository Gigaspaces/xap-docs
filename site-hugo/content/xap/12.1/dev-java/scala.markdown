---
type: post121
title:  Scala
categories: XAP121
parent: interoperability-overview.html
weight: 400
canonical: auto
---


Several extensions to the XAP API have been introduced to provide a more natural way of combining Scala with XAP.

<br>

{{%fpanel%}}
[Constructor based properties](./scala-constructor-based-properties.html)<br>
The com.gigaspaces.annotation.pojo.SpaceClassConstructor annotation on a constructor will cause the data class properties analysis to be based on properties found in the constructor (instead of getters/setters).

[Task Execution](./scala-task-execution.html)<br>
A wrapper around the XAP API provides some sugaring on top of the GigaSpace#execute methods.

[Predicate based queries](./scala-predicate-based-queries.html)<br>
Support for predicate based queries on the GigaSpace proxy has been added in. This support is based on the new macros feature introduced in Scala.

[Scripting Executor](./scala-scripting-executor.html)<br>
Dynamic Language Tasks have been extended and now support Scala based script execution.

[Enhanced REPL](./scala-enhanced-repl.html)<br>
Following is a short demo of what can be done with the XAP scala shell. It should be noted that this shell is a regular Scala REPL with some initial imports and initialization code.

[Example Project](./scala-exemplary-project.html)<br>
Example project that shows how XAP Scala can be used in a real project and how Scala and Java code might be integrated.
{{%/fpanel%}}
<br>

{{% note %}}
The Scala version required in order to use the Scala XAP extension is {{%version "scala"%}}.
{{% /note %}}
 

Assuming there is a scala installation under `$SCALA_HOME`, the jars under `$SCALA_HOME/lib` should be copied to `$GS_HOME/lib/optional/scala/lib`.
 
