---
type: post101
title:  Interactive API Tutorial
categories: XAP101TUT
weight: 350
parent: none
---


This tutorial lets you experience the XAP API in an interactive way.
Three options are presented. The first two options demonstrate the various API calls you can use to interact with the Space. The third option provides an interactive shell that lets you exercise API calls.

# Installation

{{%vbar "Download and Install XAP"%}}
- Download and unzip the latest XAP release {{%download  "http://www.gigaspaces.com/xap-download"%}}
- Unzip the distribution into a working directory
- Set the environment variable `GS_HOME` to `/home/user/xap-distribution` or `C:\xap-distribution` for Linux and Windows respectively.
- Set the JAVA_HOME environment variable to point to the JDK root directory
{{%/vbar%}}

{{%vbar "The Interactive Tutorial is hosted on Github"%}}
- Download the Interactive Tutorial {{%git "https://github.com/Gigaspaces/XAP-Interactive-Tutorial/releases"%}}
- Unzip it to your favorite directory. For this guide we will use /home/user/xap-tutorial and C:\xap-tutorial for Linux and Windows users respectively. 
{{%/vbar%}}


# Starting the Service Grid

A Data Grid requires a [Service Grid](/product_overview/service-grid.html) to host it. A service grid is composed of one or more machines (service grid nodes) running a [Service Grid Agent](/product_overview/service-grid.html#gsa) (or `GSA`), and provides a framework to deploy and monitor applications on those machines, in our case the Data Grid.
In this tutorial you'll launch a single node service grid on your machine. To start the service grid, simply run the `gs-agent` script from XAP `GS_HOME\bin` folder.

{{%tabs%}}
{{%tab "  Unix "%}}

```bash
<GS_HOME>/bin/gs-agent.sh
```
{{% /tab %}}
{{%tab "  Windows "%}}

```bash
<GS_HOME>\bin\gs-agent.bat
```
{{% /tab %}}
{{% /tabs %}}



# Deploying the Data Grid

The Data Grid can be deployed from command line, from the web management tool or via an Administration API. In this tutorial we'll use the command line. Start a command line, navigate to XAP `bin` folder and run the following command:

{{%tabs%}}
{{%tab "  Unix "%}}

```bash
<GS_HOME>/bin/gs.sh deploy-space -cluster total_members=2,1 myDataGrid
```
{{% /tab %}}
{{%tab "  Windows "%}}

```bash
<GS_HOME>\bin\gs.bat deploy-space -cluster total_members=2,1 myDataGrid
```
{{% /tab %}}
{{% /tabs %}}
  
This command deploys a Data Grid (Space) called **myDataGrid** with 2 partitions and 1 backup per partition (hence the `2,1` syntax).


{{%info%}}
Note that the Lite edition is limited to a single partition - if you're using it type `total_members=1,1` instead.
{{%/info%}}

# Running the Tutorial

First, we need to point to the XAP distribution directory by setting the `GS_HOME` environment variable.

{{%tabs%}}
{{%tab "  Unix "%}}

```bash
export GS_HOME="/home/user/xap-distribution/"
```
{{% /tab %}}
{{%tab "  Windows "%}}

```bash
set GS_HOME="C:\xap-distribution"
```
{{% /tab %}}
{{% /tabs %}}

To start the tutorial run:


{{%tabs%}}
{{%tab "  Unix "%}}

```bash
./start_tutorial.sh
```
{{% /tab %}}
{{%tab "  Windows "%}}

```bash
start_tutorial.bat
```
{{% /tab %}}
{{% /tabs %}}


After executing the start command you should see the following output:

![xx](/attachment_files/qsg/XAP-Interactive-Tutorial-screenshot.png)


<br>

### There are three options presented in this tutorial:

- [XAP Demo](#option1)<br>
Write / Read to/from myDataGrid Space

- [XAP 10 new API](#option2)<br>
Query Aggregation, Custom Change

- [XAP Interactive Shell](#option3)

<br>

{{%anchor option1%}}

## XAP Demo

This option demonstrates `XAP API` calls such as writing, reading and querying data from the deployed `myDataGrid`.
The program will run by itself, you just need to `press enter` to advance to the next example.

The tutorial uses simple POJOs to interact with the Space. Here is an example:


```java
package demo;

import java.io.Serializable;
import java.util.Map;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;

public class EngineerPojo implements Serializable {

	private Integer id;
	private String name;
	private String language;
	private Map<String, Object> dynamicProperties;

	public EngineerPojo(){}

	public EngineerPojo(Integer id){
		this.id = id;
	}

	public EngineerPojo(Integer id, String name, String language){
		this.id = id;
		this.name = name;
		this.language = language;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setDynamicProperties(Map<String, Object> dynamicProperties) {
		this.dynamicProperties = dynamicProperties;
	}

	@SpaceDynamicProperties
	public Map<String, Object> getDynamicProperties() {
		return dynamicProperties;
	}

	@Override
	public String toString() {
		String string =
			"engineer: id=" + getId() +
			" name=" + getName() +
			" language=" + getLanguage();

		if(dynamicProperties != null){
			string += " " + dynamicProperties;
		}
		return string;
	}
}
```


{{%anchor option2%}}

## XAP 10 new API

This option will introduce you to some unique XAP features:

{{%refer%}}
- [Query Aggregations]({{%currentjavaurl%}}/aggregators.html)<br>
- [Custom Change Operation]({{%currentjavaurl%}}/change-api-custom-operation.html)
{{%/refer%}}


{{%anchor option3%}}

## Interactive Shell

This option opens an interactive Groovy shell with `XAP` classes preloaded, that allows you to exercise the `XAP API`.
When you launch this option you should see the following screen:

![yy](/attachment_files/qsg/groovyshell.png)

Here are some code snippets that you can copy and paste into the Interactive Shell:


#### Defining required variables

```java
//import EngineerPojo class
import demo.EngineerPojo;
//define the gridname that we will be interacting with
gridname = "myDataGrid";
//connect and verify that the processing unit (gridname) is deployed and print number of instances.
admin = new AdminFactory().useDaemonThreads(true).createAdmin();
pus = admin.getProcessingUnits().waitFor(gridname, 10, TimeUnit.SECONDS);
assert (pus != null), "Unable to find ${gridname}, please make sure it is deployed."
assert pus.waitFor(1), "Unable to find ${gridname}, please make sure it is deployed."
println "Found " + pus.getInstances().length + " space instances";
```



#### Connecting to the Space

When a client connects to a Space, a proxy is created. All client interaction is performed through this proxy.


```java
gigaSpace = admin.getProcessingUnits().getProcessingUnit(gridname).getSpace().getGigaSpace();
```


{{%learn "/xap101/the-gigaspace-interface.html"%}}


#### Writing and Reading simple POJO objects


```java
//Write EngineerPojo object to the space
engineerPojo = new EngineerPojo(123456789, "EngineerPojo Pojo Object", "English");
gigaSpace.write(engineerPojo);

//Read an EngineerPojo object from space.
gigaSpace.read(new EngineerPojo());
```

{{%learn "/xap101/pojo-overview.html"%}}

#### Reading multiple POJO's from the Space with a criteria


```java
engineerPojoQuery = new EngineerPojo();
engineerPojoQuery.setLanguage("English");
writtenEngineerPojos = gigaSpace.readMultiple(engineerPojoQuery);
for (obj in writtenEngineerPojos) {
    println obj
}
```

#### Using POJO's with dynamic properties.


```java
engineerPojoWithDynamicProperties = new EngineerPojo(987654321, "EngineerPojo Pojo Object With Dynamic Properties", "English")
dynamicProperties = new DocumentProperties().setProperty("company","GigaSpaces").setProperty("age","24");
engineerPojoWithDynamicProperties.setDynamicProperties(dynamicProperties);
gigaSpace.write(engineerPojoWithDynamicProperties);
```


{{%learn "/xap101/dynamic-properties.html"%}}

#### Using the Document API

```java
engineerPojoDocument = new SpaceDocument("demo.EngineerPojo");
engineerPojoDocument.setProperty("id", 321654987);
engineerPojoDocument.setProperty("name", "EngineerPojo Document Object");
engineerPojoDocument.setProperty("language", "English");
engineerPojoDocument.setProperty("age", 21);

gigaSpace.write(engineerPojoDocument);
```


{{%learn "/xap101/document-overview.html"%}}


#### Write multiple SpaceDocuments with dynamic properties


```java
spaceTypeDescriptor = new SpaceTypeDescriptorBuilder("EngineerDocument").idProperty("id").supportsDynamicProperties(true).addFixedProperty("id", "Integer").addFixedProperty("name", "String").create();
gigaSpace.getTypeManager().registerTypeDescriptor(spaceTypeDescriptor);
spaceDocuments = new SpaceDocument[10];
for (int i=0; i<10; i++) {
    spaceDocument = new SpaceDocument("EngineerDocument");
    spaceDocument.setProperty("id", 1000+i);
    spaceDocument.setProperty("name", "EngineerDocument Document Object ("+i+")");
    spaceDocument.setProperty("company", "GigaSpaces");
    spaceDocument.setProperty("age", String.valueOf(i));
    spaceDocuments[i] = spaceDocument;
}
gigaSpace.writeMultiple(spaceDocuments);
```

{{%learn "/xap101/document-extending.html"%}}

#### Read a random POJO from the Space

```java
gigaSpace.read(new SpaceDocument("EngineerDocument"));
```

{{%learn "/xap101/querying-the-space.html"%}}



# Web Admin UI

You can start XAP's console and inspect the Data Grid components that have been started. In the XAP distribution you will find the command file to launch the console.

{{%tabs%}}
{{%tab "  Unix"%}}

```bash
<GS_HOME>/bin/gs_webui.sh
```
{{% /tab %}}
{{%tab "  Windows"%}}

```bash
<GS_HOME>\bin\gs_webui.bat
```
{{% /tab %}}
{{% /tabs %}}

After you execute the above command, open your web browser, point to `http://your_host:8099`. The login screen will be displayed. The following screenshots demonstrate some of the UI features: (no username and password needed)


{{%section%}}
{{%column%}}
{{%popup "/attachment_files/qsg/interactive-1.png"  "Login"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-2.png"  "Dashboard"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-3.png"  "Deployed Applications"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-4.png"  "Hosts (GSA,GSC,GSM,LUS)"%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column%}}
{{%popup "/attachment_files/qsg/interactive-5.png"  "Deployed Data Grids"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-6.png"  "Classes in Space"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-7.png"  "Class attributes"%}}
{{%/column%}}

{{%column%}}
{{%popup "/attachment_files/qsg/interactive-8.png"  "Statistics"%}}
{{%/column%}}

{{%/section%}}


{{%learn "/xap101adm/web-management-console.html"%}}


