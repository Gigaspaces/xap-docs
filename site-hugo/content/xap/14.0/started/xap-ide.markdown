---
type: post140
title: Setting up your IDE
categories: XAP140GS
parent: xap-basics.html
weight: 200
---

{{% anchor 0 %}}
Follow the instructions in this tutorial to prepare your development environment.

{{% note "Note"%}}
**Ensure you have a JDK installed**. You will need Java 8 or higher, latest update is recommended.
{{% /note %}}

# Checking your JDK Version

To check your installed Java version:

1. Open a command line window.
1. Run `set JAVA_HOME`.
1. A response similar to this suggests you have a JDK installed:

	```java
	JAVA_HOME=C:\jdk1.8
	```

1. To check the JDK version, run `%JAVA_HOME%\bin\java \-version`. A response like this from `Java` indicates you have a valid JDK installed:

	```java
	java version "1.6.0_23"
	Java(TM) SE Runtime Environment (build 1.6.0_23-b05)
	Java HotSpot(TM) Client VM (build 11.0-b16, mixed mode, sharing)
	```

If your installed JDK version is lower then 1.5 or none is installed, see below on how to install one.

# Installing a Proper JDK (Java Development Kit)

1. To install JDK 1.6, download and install [**JDK 6 Update X**](http://java.sun.com/javase/downloads/index.jsp)
{{% anchor 1 %}}
1. **Download and unzip the latest XAP release** from the [downloads page](http://www.gigaspaces.com/LatestProductVersion).
{{% anchor 2 %}}
1. **Install a Java IDE**. If you don't have an IDE installed, you can [download and unzip the Eclipse IDE for Java Developers](http://www.eclipse.org/downloads), or the [IntelliJ IDEA](http://www.jetbrains.com/idea/download/index.html) IDE (we recommend the Ultimate Edition because of its excellent Spring framework support). If you're using Eclipse, it is also recommended to install the [Spring Tool Suite plugin for Eclipse](http://www.springsource.com/developer/sts).

# Running the Application inside Eclipse IDE

1. Start Eclipse. The **Workspace Launcher** window opens.
1. Write a new workspace name or select one of your existing workspaces, and click **OK**.
1. To import the project, select **File > Import** to open the Import window.
1. Select **Existing projects into workspace** and click **Next** to open the Import Project window.
1. In the **Select root directory** field, click **Browse** to open the browse window.
1. Select the folder `/examples/helloworld` and click **OK**.
1. Make sure the following 3 projects are selected: **hello-common**, **hello-processor**, and **hello-feeder**.
1. Click **Finish**.
1. Create a new Eclipse environment variable called **GS_HOME**, and point it to your GigaSpaces installation Root folder.
1. Right click the **hello-common** project in the **Package Explorer** tab to open the context menu.
1. Select **Build Path > Configure Build Path** to open the _Java Build Path window._
1. Select the **Libraries tab* and click the *Add Variable...** button to open the _New Variable Classpath Entry dialog_
1. Click **Configure Variables** to open the Classpath Variables window.
1. Click **New** to open the New Variable Entry window.
1. In the **Name** field, write `GS_HOME` to name the variable.
1. Click **Folder** and browse to **your GigaSpaces installation root folder**.
1. Select your **GigaSpaces installation root folder** and click **OK** (3 times in total).
1. Click **Yes** to do a full rebuild.
1. Close the remaining windows.

# Classpath

The XAP libraries are located under `XAP_HOME/lib`. There are three sub-directories:

* `lib/required` - JAR files that are required for any GigaSpaces application.
* `lib/optional` - JAR files that enable additional capabilities, such as servlet api.
* `lib/platform` - JAR files that are used only by the XAP platform.

## Compilation

In order to compile and run XAP applications, all the JAR files under the `XAP_HOME/lib/required` directory should be included in your compile and run time classpaths. Additional JAR files that you may need for development are located at `XAP_HOME/lib/optional`.

## Runtime - Processing Unit

When an application is deployed as a Processing Unit, there is no need to add XAP-specific JAR files to the Processing Unit classpath. If such are added under the Processing Unit's `lib` directory, the system will remove those JAR files and  replace them with the system's JARs for compatibility.

## Runtime - Standalone

When running a standalone client that accesses XAP, ensure that all the JARs located under the `XAP_HOME/lib/required` directory are part of the JVM's classpath. This also holds true for remote Space clients that are used from another JVM (such as a standalone web container). If your client is a JEE web application that is not running within the XAP runtime environment, or more specifically a GSC, you must include these JARs in your application's `WEB-INF/lib` directory.

# Maven
 
XAP is Maven-friendly. It is built using Maven, which can be easily used by developers constructing XAP applications.  
    
The main dependency required to use XAP is `xap-openspaces`.
 
 ```xml
 <dependency>
   <groupId>org.gigaspaces</groupId>
   <artifactId>xap-openspaces</artifactId>
   <version>{{%version "maven-version" %}}</version>
 </dependency>
 ```
 
 XAP artifacts are currently not published in Maven Central Repo, therefore you must configure a repository:
 
 ```xml
 <repository>
    <id>org.openspaces</id>
    <url>http://maven-repository.openspaces.org</url>
 </repository>
 ```
