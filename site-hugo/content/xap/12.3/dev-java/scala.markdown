---
type: post123
title:  Scala
categories: XAP123, OSS
parent: interoperability-overview.html
weight: 400
---


GigaSpaces offer several extensions to the XAP API, to more easily combine Scala with XAP. This includes support for [constructor-based properties](./scala-constructor-based-properties.html), a wrapper around the XAP API for [task execution](./scala-task-execution.html), [predicate-based queries](./scala-predicate-based-queries.html), and a [scripting executor](./scala-scripting-executor.html) (part of the [Dynamic Language Tasks](./task-dynamic-language.html) feature).

This section also provides an [enhanced REPL](./scala-enhanced-repl.html) demo of what can be done with the XAP scala shell, along with a [sample project](./scala-exemplary-project.html) that shows how XAP Scala can be used in a real project, and how Scala and Java code can be integrated.


{{% note "Note"%}}
The minimum Scala version supported by the Scala XAP extension is {{%version "scala"%}}.
{{% /note %}}
 
Assuming there is a scala installation under `$SCALA_HOME`, the jars under `$SCALA_HOME/lib` should be copied to `$GS_HOME/lib/optional/scala/lib`.
 
