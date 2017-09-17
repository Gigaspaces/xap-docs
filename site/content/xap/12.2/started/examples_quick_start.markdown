---
type: post122
title:  InsightEdge Examples
categories: XAP122GS, IEE
weight: 1110
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

In this tutorial, you will learn how to start InsightEdge and run examples locally. You can download the latest InsightEdge distribution from [here](http://insightedge.io/#download) and unpack it to any location.


The examples sources are located at `insightedge/examples`.

The examples jar is located at `insightedge/examples/jars`.

# overview
What we are going to to:

- Starting local environment
- Building examples
- Running the examples
- Shutdown local environment


# Requirements For Building InsightEdge Examples
* Java 1.8
* Scala 2.10
* Maven 3.1+


# Starting local environment

Prior to executing example application, you have to start insightEdge
{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge demo
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\bin\insightedge.cmd demo
```
{{%/tab%}}
{{%/tabs%}}

This will start next components:

* Spark master at `spark://127.0.0.1:7077` and Spark slave
* XAP Manager at `http://localhost:8080`
* Spark monitoring UI at `http://localhost:8090`
* Zeppelin  UI at `http://localhost:9090`
* Data Grid manager and two containers with `1G` heap each
    - space is deployed with name `insightedge-space`


# Building examples

Prior to executing example application you need to install insightedge artifacts to your local Maven repository, make sure you have Maven installed and then run the following from InsightEdge directory:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/tools/maven/insightedge-maven.sh
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\tools\maven\insightedge-maven.cmd
```
{{%/tab%}}
{{%/tabs%}}


Building the examples, from  `insightedge/examples`, using Maven:
```bash
mvn clean package
```

This will create the examples jar at `insightedge/examples/target/insightedge-examples.jar`

# Running examples

There are several options how you can run examples:

* from Web Notebook
* from your IDE
* from a command line

## Zeppelin Notebook

Run the examples from the zeppelin notebook at `http://localhost:9090/`

## Running from IDE

You can run examples from your favourite IDE. Every example has a `main` method, so it can be executed as standard application. There two important things:

* enable `run-from-ide` maven profile (this will switch required dependencies to `compile` scope so they are available in classpath)
* Pass as arguments `local[*]` and `insightedge-space`

## Running from command line

Submit examples as Spark applications with the next command:
{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge-submit --class {main class name} --master {Spark master URL} \
    {insightedge-examples.jar location}
```
{{%/tab%}}
{{%tab Windows%}}
```bash
insightedge\bin\insightedge-submit --class {main class name} --master {Spark master URL} ^
    {insightedge-examples.jar location}
```
{{%/tab%}}
{{%/tabs%}}

For example, `SaveRDD` can be submitted with the next syntax:
{{%tabs%}}

{{%tab Linux%}}
```bash
./insightedge/bin/insightedge-submit --class org.insightedge.examples.basic.SaveRdd --master spark://127.0.0.1:7077 \
    ./insightedge/examples/jars/insightedge-examples.jar
```
{{%/tab%}}
{{%tab Windows%}}
```bash
insightedge\bin\insightedge-submit --class org.insightedge.examples.basic.SaveRdd --master spark://127.0.0.1:7077 ^
    insightedge\examples\jars\insightedge-examples.jar
```
{{%/tab%}}
{{%/tabs%}}

{{%note "Note..."%}}
Running `TwitterPopularTags` example requires you to pass {{%exurl "Twitter app tokens" "https://apps.twitter.com/"%}} as arguments.
{{%/note%}}


### Python examples

You can run Python examples with
{{%tabs%}}

{{%tab Linux%}}
```bash
./insightedge/bin/insightedge-submit --master {Spark master URL} {path to .py file}
```
{{%/tab%}}
{{%tab Windows%}}
```bash
insightedge\bin\insightedge-submit --master {Spark master URL} {path to .py file}
```
{{%/tab%}}
{{%/tabs%}}

For example,

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge-submit --master spark://127.0.0.1:7077 ./insightedge/examples/python/sf_salaries.py
```
{{%/tab%}}
{{%tab Windows%}}
```bash
insightedge\bin\insightedge-submit --master spark://127.0.0.1:7077 insightedge\examples\python\sf_salaries.py
```
{{%/tab%}}
{{%/tabs%}}

# Stopping local environment

To stop all InsightEdge components, next command can be executed:

{{%tabs%}}
{{%tab Linux%}}
```bash
./insightedge/bin/insightedge shutdown
```
{{%/tab%}}

{{%tab Windows%}}
```bash
insightedge\bin\insightedge.cmd shutdown
```
{{%/tab%}}
{{%/tabs%}}