---
type: post120
title:  Installation
categories:  XAP120TUT
parent: none
weight: 100
---

# Download
Download the latest XAP release {{%download "http://www.gigaspaces.com/xap-download"%}}<br>

# Installation
- Unzip the distribution into a working directory; GS_HOME<br>
- Set the JAVA_HOME environment variable to point to the JDK root directory<br>
- Start your favorite Java IDE<br>
- Create a new project<br>
- Include all jar files from the GS_HOME/lib/required in the classpath<br>

#  Examples

There are two examples provided with the Open Source distribution. You can find them under the `<XAP root>/examples` directory. The examples are also available on github(see links below).


[Hello World](https://github.com/xap/xap/tree/master/xap-examples/hello-world)<br>
The Hello World example demonstrates how to perform basic Space operations - e.g. writing and reading an Object from the data-grid.
This example also covers different deployment modes: embedded, remote and partitioned.  

[Hola Mundo](https://github.com/xap/xap/tree/master/xap-examples/hola-mundo)   <br>
This example builds on the previous example, introducing the concepts of event-processing using a Processing Unit.
This example also demonstrates how to run with the IDE.  
 
# Tutorial 
You can download all examples presented here from {{%git "https://github.com/Gigaspaces/xap-tutorial"%}}. Feel free to clone, fork and contribute to the tutorial code.

