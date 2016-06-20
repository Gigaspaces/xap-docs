---
type: post120
title:  Enhanced Scala REPL
categories: XAP120
parent: scala.html
weight: 200
---


Following is a short demo of what can be done with the XAP scala shell. It should be noted that this shell is a regular Scala REPL with some initial imports and initialization code.

# Demo Setup

- Run `$GS_HOME/bin/gs-agent.sh` (or `.bat`)
- Start the shell `$GS_HOME/tools/scala/shell.sh` (or `.bat`)

# The Demo


```scala
java version "1.8.0_45"
Java(TM) SE Runtime Environment (build 1.8.0_45-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.45-b02, mixed mode)

Initializing... This may take a few seconds.
Welcome to Scala version 2.11.6 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_45).
Type in expressions to have them evaluated.
Type :help for more information.
Please enjoy the predefined 'admin' val.

xap>
```

We'll start by deploying a single space to the service grid, notice we already have an admin instance in scope to simplify this process:


```scala
xap> val gsm = admin.getGridServiceManagers.waitForAtLeastOne
gsm: org.openspaces.admin.gsm.GridServiceManager = org.openspaces.admin.internal.gsm.DefaultGridServiceManager@ca43aa97

xap> gsm.deploy(new org.openspaces.admin.space.SpaceDeployment("mySpace"))
res0: org.openspaces.admin.pu.ProcessingUnit = org.openspaces.admin.internal.pu.DefaultProcessingUnit@59479eba
```

We'll use some helper method that is imported into the session scope (from `org.openspaces.scala.repl.GigaSpacesScalaReplUtils`) to get a `GigaSpace` proxy:


```scala
xap> val Some(gigaSpace) = getGigaSpace("mySpace")
gigaSpace: org.openspaces.core.GigaSpace = mySpace_container:mySpace
```

Now we'll execute a task using another helper method:


```scala
xap> execute(gigaSpace) { holder => holder.clusterInfo.getNumberOfInstances}

res1: com.gigaspaces.async.AsyncFuture[Integer] = org.openspaces.core.transaction.internal.InternalAsyncFuture@f1423ba

xap> val numberOfInstances = res1.get
numberOfInstances: Integer = 1
```

Let's define a new case class and write an entry to the space:


```scala
xap> case class Data @SpaceClassConstructor() (@BeanProperty @SpaceId id: String = null, @BeanProperty content: String = null)
defined class Data

xap> gigaSpace.write(Data(id = "id1", content = "my data content"))
res2: com.j_spaces.core.LeaseContext[Data] = SpaceEntryLease[uid=-792314720^58^id1^0^0,typeName=Data,routingValue=id1,expirationTime=9223372036854775807]
```

Now execute a task that reads this entry and returns is `content` property:


```scala
xap> execute(gigaSpace) { holder =>holder.gigaSpace.read(Data()).content}

res3: com.gigaspaces.async.AsyncFuture[String] = org.openspaces.core.transaction.internal.InternalAsyncFuture@7c767c0d

xap> val dataContent = res3.get
dataContent: String = my data content
```

# Configuration

It is possible to customize the initialization code, the shutdown code and the initial imports.
XAP 10.2 introduced alternative method to customize initialization code along with initial imports.

## Standard method

XAP REPL during initialization phase will load imports from initial imports file and then execute init code. The end of shell session
will be preceded by execution of shutdown code.

#### Initial imports

By default the initial imports will be loaded from `$GS_HOME/tools/scala/conf/repl-imports.conf`. This location can be overridden by the system property: `org.os.scala.repl.imports`. Each import should be in its own line. (empty lines and lines beginning with `#` are ignored)

#### Init code

By default the initialization code will be loaded from `$GS_HOME/tools/scala/conf/init-code.scala`. This location can be overridden by the system property: `org.os.scala.repl.initcode`.

#### Shutdown code

By default the shutdown code will be loaded from `$GS_HOME/tools/scala/conf/shutdown-code.scala`. This location can be overridden by the system property: `org.os.scala.repl.shutdowncode`.

## New method

The difference between the two methods is that the new one does not use a file with initial imports. Those imports
should be placed in initialization code script (with import prefix added). Alternative method is used if system property `org.os.scala.repl.newinitstyle` is set as 'yes', 'on' or 'true'.

#### Init code

By default the initialization code will be loaded from `$GS_HOME/tools/scala/conf/new-init-code.scala`. This location can be overridden by the system property: `org.os.scala.repl.initcode`.
Please note that new init code script should also contain necessary imports.

#### Shutdown code

Please refer to `Shutdown code` described above in the standard method.
