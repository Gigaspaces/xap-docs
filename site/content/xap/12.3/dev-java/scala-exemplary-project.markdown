---
type: post123
title:  Example Project
categories: XAP123, OSS
parent: scala.html
weight: 600
---



# Overview

Here is an example project that shows how XAP Scala can be used in a real project and how Scala and Java code might be integrated.

#  OpenSpaces Maven plugin project

The project is based on a template project of `basic` type from the `XAP Maven plugin`. A few changes were introduced:

- `Common` module, which implements Space classes,  written in Scala and takes advantage of constructor based properties.
- A new module - `verifier` was introduced. It uses a class with constructor based properties and predicate based queries to obtain objects from Space.
- Build process of `common` and `verifier` modules was modified to handle Scala and mixed Java/Scala modules, respectively.

#  Build & run

## Requirements
1. JDK in version at least 1.6 is required to build the project.
2. The project uses `maven` build tool.
3. To run the project, Scala libraries have to be a accessible for XAP.

Please note that Scala is not required to build the project, since requried libraries will be downloaded by `maven`.

## Build and run steps
1. Download {{%giturl "xap-scala" "https://github.com/Gigaspaces/xap-scala/archive/10.2.0_ga_build13800_07_28_2015.zip"%}} and unzip it.
2. From the project's main directory `$XAP_SCALA` run command `mvn clean install`
3. From the project's main directory `$XAP_SCALA/example/gs-openspaces-scala-example` run command `mvn clean package` - necessary JAR files to deploy on a grid will be created.
4. Start XAP Grid Service by running command: `$GS_HOME/bin/gs-agent.sh/bat`
5. Run this command: `$GS_HOME/tools/maven/installmavenrep.sh/bat`
6. Deploy the project on the grid (from `$XAP_SCALA/example/gs-openspaces-scala-example`): `mvn os:deploy -Dgroups=$XAP_LOOKUP_GROUPS`.

#  XAP Scala features

## Constructor based properties

`Common` module defines Space classes used by other modules. Please note, that the classes are written in Scala and are used in other Scala and Java modules as well. All of these classes are translated to `bytecode` and therefore can be used interchangeably.

Sometimes, having immutable state is a desired feature. This requirement is covered in XAP Scala by classes that use constructor based properties, in case of the `common` module it is the `Verification` class. It is written only once to the `Space` and never changed (instances can be removed).



```scala
case class Verification @SpaceClassConstructor() (
  @BeanProperty
  @SpaceId
  id: String,

  @BeanProperty
  dataId: String) extends scala.Serializable {

  override def toString: String = s"id[$id] dataId[$dataId]"
}
```

The other class (`Data`) has been rewritten in Scala. However, its behavior has not been modified (apart from adding a new field needed by the `verifier` module):


```scala
case class Data (
  @BeanProperty @SpaceId(autoGenerate = true) var id: String = null,
  @BeanProperty @SpaceRouting @SpaceProperty(nullValue = "-1") var `type`: Long = -1,
  @BeanProperty var rawData: String = null,
  @BeanProperty var data: String = null,
  @BooleanBeanProperty var processed: Boolean = false,
  @BooleanBeanProperty var verified: Boolean = false) {

  def this(`type`: Long, rawData: String) = this(null, `type`, rawData, null, false, false)

  def this() = this(-1, null)

  override def toString: String = s"id[${id}] type[${`type`}] rawData[${rawData}] data[${data}] processed[${processed}] verified[${verified}]"
}
```

## Predicate based queries

The `verifier` module extends the pipeline presented in the baseline project (the one created by the `OpenSpaces Maven plugin`). The `Verifier` picks up processed `Data` instances and tries to verify them. The objects that pass the verification process are then modified (`verified` set to `true`) and saved along with a new, immutable `Verification` object. The objects that failed during verification process are removed from the Space. The `verifier` uses the new feature - predicate based queries - to access the Space in a more readable and natural way (especially for functional languages such as Scala):


```scala
@GigaSpaceContext private var gigaSpace: GigaSpace = _ // injected
// ...

// data instances to process further are obtained in the following way
val unverifiedData = gigaSpace.predicate.readMultiple { data: Data => data.processed == true && data.verified == false }
```

Pu.xml contains a standard description of gigaSpace:


```xml
...
<os-core:giga-space-context/>

<os-core:space-proxy  id="space" space-name="mySpace"/>

<os-core:giga-space id="gigaSpace" space="space"/>
...
```

{{%note%}}
Please note that `gigaSpace` from the code above is an instance of ScalaEnhancedGigaSpaceWrapper - a wrapper around GigaSpace introduced in XAP Scala.
{{%/note%}}

#  Building Scala and mixed Java/Scala modules

The build configuration in Scala or Java/Scala modules is almost as simple in case of pure Java modules.

## Scala module

The `common` module is a pure Scala module. The `maven-compiler-plugin` has been replaced by `scala-maven-plugin`. The build configuration from the `pom.xml` for the `common` has the following form:


```xml
<build>
    <sourceDirectory>src/main/scala</sourceDirectory>
    <plugins>
        <plugin>
            <groupId>net.alchim31.maven</groupId>
            <artifactId>scala-maven-plugin</artifactId>
            <version>3.2.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>testCompile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <scalaCompatVersion>${scalaBinaryVersion}</scalaCompatVersion>
            </configuration>
        </plugin>
    </plugins>
    <finalName>gs-openspaces-scala-example-common</finalName>
</build>
```
where `scalaBinaryVersion` is a property defined in a parent pom file (in this case it is `2.11`).

## Java-Scala module

The `verifier` module is a mixed Java-Scala module, where Scala classes call Java classes. This configuration can be used when a separate task is implemented in Java and it only needs to be called from other parts of application. In case of this project, Java module is simulated by `VerifierEngine` class and, for ease of use, it is executed by Scala `verifier`.

In such a configuration, the Scala compiler has to 'somehow' reach Java compiled classes. This is where a `build-helper-maven-plugin` is used - it adds Java classes to the source, then they are compiled and finally the Scala compiler uses them during Scala code compilation. The build configuration of the `verifier` module is as follows:


```xml
<build>
    <sourceDirectory>src/main/scala</sourceDirectory>

    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>build-helper-maven-plugin</artifactId>
            <version>1.4</version>
            <executions>
                <execution>
                    <id>add-java-source</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>add-source</goal>
                    </goals>
                    <configuration>
                        <sources>
                            <source>${basedir}/src/main/java</source>
                        </sources>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>net.alchim31.maven</groupId>
            <artifactId>scala-maven-plugin</artifactId>
            <version>3.2.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>testCompile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <scalaCompatVersion>${scalaBinaryVersion}</scalaCompatVersion>
            </configuration>
        </plugin>
    </plugins>
</build>
```
