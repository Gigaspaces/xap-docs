---
type: postsbp
title:  Integration testing with scalatest
categories: SBP
parent: solutions.html
weight: 700
---

{{% ssummary  %}}We describe here a technique for implementing a XAP integration (i10n) test using a scalatest [FunSuite](http://www.scalatest.org/getting_started_with_fun_suite).{{% /ssummary %}}

{{% tip %}}
**Summary:**  A technique for implementing a XAP integration test using a scalatest <br/>
**Author**: Jason Nerothin, Senior Solutions Architect, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 9.7.2<br/>
**Last Update:** May 2014<br/>


{{% /tip %}}

# Usage

We provide an `abstract FunSuite` that is a `BeforeAndAfterAllConfigMap` in this [gist](https://gist.github.com/jasonnerothin/fd55a2c0475b5e4f2f1d#file-gsi10nsuite). Simply implemented, the subclass need only provide a small number of properties and a Spring context file. The superclass uses this information to seed a `ConfigMap`, which then configures a nested `ProcessingUnitContainerProvider`, which in turn provisions a protected `GigaSpace` proxy. 

Here's example configuration from a [working example](https://github.com/GigaSpaces/gs-executor-remoting/blob/master/src/test/scala/com/gigaspaces/sbp/WatchRepairSuite.scala) that tests against and embedded Space:


```scala

  // "defaults" is a protected member, and is 
  // the only required configuration necessary to 
  // successfully initialize the FunSuite.
  defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(numPartitions)
    , numBackupsProperty -> int2Integer(0)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> s"jini:/*/*/$spaceName" // spaceName is a class member
    , spaceModeProperty -> SpaceMode.Remote
    , configLocationProperty -> "classpath*:/META-INF/Spring/pu.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

```

In this example, the test code truly **is** an integration test - designed to run against a running XAP grid. (Note the reference to [pu.xml file](https://github.com/GigaSpaces/gs-executor-remoting/blob/master/src/main/resources/META-INF/Spring/pu.xml), which is available on the test classpath.)

We could as easily have run in-process by using ``SpaceMode.Embedded``. This makes IDE-driven debugging easier, as it does not incur the workflow overhead of setting up a remote agent or debugger. Here's the code from another [working example](https://github.com/GigaSpaces/gs-executor-remoting/tree/ide):
 

```scala
 
   defaults = Map[String, Any](
     schemaProperty -> "partitioned-sync2backup"
     , numInstancesProperty -> int2Integer(numPartitions)
     , numBackupsProperty -> int2Integer(0)
     , instanceIdProperty -> int2Integer(1) // count begins at 1
     , spaceUrlProperty -> s"/./$spaceName" // spaceName is a class member
     , spaceModeProperty -> SpaceMode.Embedded
     , configLocationProperty -> "classpath*:/META-INF/Spring/pu.xml"
     , localViewQueryListProperty -> List[SQLQuery[_]]()
   )
   
```

All `SpaceModes` supported by XAP are enumerated in the abstract FunSuite class (gisted above). For each mode, corresponding configuration can be provided through the `defaults` property.
 

```scala

  object SpaceMode extends Enumeration {
    type SpaceMode = Value
    val Embedded, Remote, LocalCache, LocalView = Value
  }

```

An end-to-end example can be forked from [here](https://github.com/GigaSpaces/gs-executor-remoting/).
 

# Considerations

- Running multiple partitions from within an i10n test cannot be run with a **XAP Lite** license.  
- The working examples presented here uses [gradle](http://www.gradle.org/) as a build tool. The code could as easily be used from [sbt](http://www.scala-sbt.org/) or the [scalatest maven plugin](http://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin).
