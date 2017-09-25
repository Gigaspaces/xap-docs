---
type: post122
title:  Running The Examples
categories: XAP122GS, OSS
parent: insightedge-basics.html
weight: 300
---

# Overview

This section explains how to use the sample code that has been provided with the InsightEdge package. 

{{%info "Info..."%}}
The example sources are located at `<XAP HOME>/insightedge/examples`.

The example .jar is located at `<XAP HOME>/insightedge/examples/jars`.
{{%/info%}}

# Building the examples

Building the examples is not required, since the package includes a pre-built .jar file. However, if you wish to modify the examples for learning purposes and rebuild them, follow this procedure.

Before the first time building the examples, you need to install the InsightEdge artifacts in your local Maven repository. To do that, run the following from the `$XAP_HOME/insightedge/tools/maven` directory:

```bash
insightedge-maven
```

Next, use the following command in Maven to build the examples from `$XAP_HOME/insightedge/examples`:

```bash
mvn clean package
```

The end result is an examples .jar located at `$XAP_HOME/insightedge/examples/target/insightedge-examples.jar`.

# Running the examples

In order to run the examples on your local machine you need to start a local InsightEdge environment. If you haven't done so already, refer to [Local Machine Setup](insightedge-local-setup.html) for more info.

You can run the InsightEdge example .jar from any of the following:

* Apache Zeppelin Web Notebook
* An IDE
* A command line

## Using the Web Notebook

To run the examples from the Apache Zeppelin web notebook, go to `http://localhost:9090/`. For more info see [Web notebook](insightedge-zeppelin.html).

## Using an IDE

You can run the examples from your preferred IDE. Every example has a `main` method, so it can be executed as standard application. Note these two important configuration requirements:

* Enable `run-from-ide` in the Maven profile. This will switch the required dependencies to `compile` scope, so they are available in the classpath.
* Pass the examples as `local[*]` and `insightedge-space` arguments.

## Using a Command Line


To run the examples from a command line, run the following from the `$XAP_HOME/insightedge/bin` directory:

```bash
insightedge-submit --class {main class name} --master {Spark master URL} {path/to/insightedge-examples.jar}
```

For example, `SaveRDD` can be submitted with the following syntax:

```bash
insightedge-submit --class org.insightedge.examples.basic.SaveRdd --master spark://127.0.0.1:7077 \
    ./$XAP_HOME/insightedge/examples/jars/insightedge-examples.jar
```

{{%note "Note..."%}}
Running the `TwitterPopularTags` example requires you to pass {{%exurl "Twitter app tokens" "https://apps.twitter.com/"%}} as arguments.
{{%/note%}}

### Python Examples

In addition to the above, you can run Python examples using the following command:
{{%tabs%}}

```bash
insightedge-submit --master {Spark master URL} {path/to/your-example.py}
```

For example:

```bash
insightedge-submit --master spark://127.0.0.1:7077 ./$XAP_HOME/insightedge/examples/python/sf_salaries.py
```
