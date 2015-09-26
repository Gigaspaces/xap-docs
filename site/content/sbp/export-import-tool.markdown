---
type: post
title:  Export-Import Tool
categories: SBP
parent: solutions.html
weight: 50
---

{{% ssummary %}}XAP IMDG Exporting-Importing Data Tool{{% /ssummary %}}

{{% tip %}}
 **Author**:Shay Hasidim, John Burke, Christos Erototcritou, Pavlo Romanenko<br/>
 **Recently tested with GigaSpaces version**: XAP 10.1.1<br/>
 **Last Update:** July 2015<br/>
{{% /tip %}}

# Introduction

With this tool we will demonstrate how to export data from a space via serializing it to a file. We can then re-import the data back into the space. The tool executes distributed tasks in 'preprocess' mode, which reads the serialization files and returns a de-duplicated list of the classes only.

![xap-export-import.png](/attachment_files/import-export-tool.jpg)

# Getting started

### Download the Export/Import Example

You can download the example project  from [here](/sbp/download_files/ImportExportTool.zip) and unzip it into an empty folder.


### Build and Running the Tool

##### Step 1: Setup XAP maven plugin

Please [install the OpenSpaces Maven plugin]({{%latestjavaurl%}}/maven-plugin.html#MavenPlugin-Installation) before you run this example.

##### Step 2: Deploy a space and write some data

- modify `<gsVersion>` within the `ImportExportTool\pom.xml` to include the right XAP release - below example having XAP 10.1.1 (10.1.1-12800-RELEASE) as the `<gsVersion>` value:


```xml
<properties>
        <gsVersion>10.1.1-12800-RELEASE</gsVersion>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```


##### Step 3: Build the project


```bash
cd <project_root>
mvn clean install
```

##### Step 4: Deploy a space and write some data
If you do not have an already deployed space with data, you will need to deploy a new space and write some dummy data to it.
In this example you should execute [benchmark from GS-UI]({{%latestadmurl%}}/benchmark-browser.html) and write MessagePOJOs to the grid.
 
##### Step 5:	Run the tool to export the objects

For Windows:


```bash
cd <project_root>
java -classpath D:\gigaspaces-xap-premium-10.1.1-ga\lib\required\*;D:\gigaspaces-xap-premium-10.1.1-ga\lib\platform\benchmark;target\*;lib\* com.gigaspaces.tools.importexport.SpaceDataImportExportMain -e -l 127.0.0.1 -s space -d D:\gs
```

for Linux:


```console
cd <project_root>
java -classpath /home/adminuser/gigaspaces-xap-premium-10.1.1-ga/lib/required/*:/home/adminuser/gigaspaces-xap-premium-10.1.1-ga/lib/platform/benchmark/*:target/*:lib/* com.gigaspaces.tools.importexport.SpaceDataImportExportMain -e -s space -l 127.0.0.1 -d /tmp/gs
```

{{%note%}}
Make sure that classes of the POJOs are set in the classpath before running.
{{%/note%}}

{{%note%}}
Make sure that classes directory for serialized files(you specify it with -d {directory}) exists.
{{%/note%}}


```console
2015-07-14 17:47:08,432  INFO [com.gigaspaces.common] - (tid-821) : found 1 classes
2015-07-14 17:47:08,449  INFO [com.gigaspaces.common] - (tid-821) : starting export to file /tmp/gs/com.j_spaces.examples.benchmark.messages.MessagePOJO.1.1.ser.gz
2015-07-14 17:47:08,449  INFO [com.gigaspaces.common] - (tid-821) : starting export thread for com.j_spaces.examples.benchmark.messages.MessagePOJO
2015-07-14 17:47:08,449  INFO [com.gigaspaces.common] - (tid-821) : starting export to file /tmp/gs/com.j_spaces.examples.benchmark.messages.MessagePOJO.1.2.ser.gz
2015-07-14 17:47:08,450  INFO [com.gigaspaces.common] - (tid-821) : starting export thread for com.j_spaces.examples.benchmark.messages.MessagePOJO
2015-07-14 17:47:08,450  INFO [com.gigaspaces.common] - (tid-821) : waiting for 2 import operations to complete-complete
2015-07-14 17:47:08,450  INFO [com.gigaspaces.common] - (tid-938) : reading space class : com.j_spaces.examples.benchmark.messages.MessagePOJO
2015-07-14 17:47:08,451  INFO [com.gigaspaces.common] - (tid-938) : space partition contains 5000 objects
2015-07-14 17:47:08,451  INFO [com.gigaspaces.common] - (tid-938) : writing to file : /tmp/gs/com.j_spaces.examples.benchmark.messages.MessagePOJO.1.1.ser.gz
2015-07-14 17:47:08,461  INFO [com.gigaspaces.common] - (tid-938) : read 5000 objects from space partition
2015-07-14 17:47:08,461  INFO [com.gigaspaces.common] - (tid-938) : export operation took 24 millis
2015-07-14 17:47:08,462  INFO [com.gigaspaces.common] - (tid-938) : reading space class : com.j_spaces.examples.benchmark.messages.MessagePOJO
2015-07-14 17:47:08,462  INFO [com.gigaspaces.common] - (tid-938) : space partition contains 5000 objects
2015-07-14 17:47:08,462  INFO [com.gigaspaces.common] - (tid-938) : writing to file : /tmp/gs/com.j_spaces.examples.benchmark.messages.MessagePOJO.1.2.ser.gz
2015-07-14 17:47:08,463  INFO [com.gigaspaces.common] - (tid-938) : read 5000 objects from space partition
2015-07-14 17:47:08,463  INFO [com.gigaspaces.common] - (tid-938) : export operation took 22 millis
2015-07-14 17:47:08,465  INFO [com.gigaspaces.common] - (tid-821) : finished writing 1 classes
```


For each exported space class data `/tmp/gs`(or any other directory you specify) will have the `n` zip files(n - number of partitions in the target grid) with the class instances content.

##### Step 6:	Run the tool to import the objects back into a space<br/>

Once you restart the data grid you can reload your data back. This will reload the data from the zip files into the space:
For Windows:

```console
cd <project_root>
java -classpath D:\gigaspaces-xap-premium-10.1.1-ga\lib\required\*;D:\gigaspaces-xap-premium-10.1.1-ga\lib\platform\benchmark;target\*;lib\* com.gigaspaces.tools.importexport.SpaceDataImportExportMain -i -l 127.0.0.1 -s space -d D:\gs
```
for Linux:

```console
cd <project_root>
java -classpath /home/adminuser/gigaspaces-xap-premium-10.1.1-ga/lib/required/*:/home/adminuser/gigaspaces-xap-premium-10.1.1-ga/lib/platform/benchmark/*:target/*:lib/* com.gigaspaces.tools.importexport.SpaceDataImportExportMain -i -s space -l 10.23.11.212 -d /tmp/gs
```

{{%note%}}
A space read call for each class is executed before trying to perform any import.
{{%/note%}}

## Options
The tool supports the following arguments:

|                          |                    |                   |               |               |
|:-------------------------|:-------------------|:------------------|:--------------|:--------------|
| Short Name               | Long Name          | Optional/required | Default value | Description                                                                                                                                                                |
| -e                       | --export           | optional          | NA            | Performs space class export                                                                                                                                                |
| -i                       | --import           | optional          | NA            | Performs space class import                                                                                                                                                |
| -l                       | --locators         | optional          | NA            | Comma separated list of lookup locators (ex. 127.0.0.1:4174,192.168.1.100).                                                                                                |
| -g                       | --groups           | optional          | NA            | Comma separated list of lookup groups (ex. skyler,xap97).                                                                                                                  |
| -s                       | --space            | required          | NA            | The name of the space                                                                                                                                                      | 
| -c                       | --classes          | optional          | NA            | The classes whose objects to import/export - comma separated                                                                                                               |
| -b                       | --batch            | optional          | 1000          | The batch size                                                                                                                                                             |
| -p                       | --partitions       | optional          | NA            | The partition(s) to restore - comma separated                                                                                                                              |
| -n                       | --number           | optional          | NA            | Number of partitions to export. For instance: now space has 4 partitions, but you want to export all the data to space with 3 partitions, then "-n 3" has to be specified  |
| -d                       | --directory        | required          | NA            | Read-from/write-to directory                                                                                                                                               |
| -u                       | --username         | optional          | NA            | The username when connecting to a secured space.                                                                                                                           |
| -p                       | --password         | optional          | NA            | The password when connecting to a secured space.                                                                                                                           |
