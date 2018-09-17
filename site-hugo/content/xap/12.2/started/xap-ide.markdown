---
type: post122
title: Setting up your IDE
categories: XAP122GS
parent: xap-basics.html
weight: 200
---

{{% anchor 0 %}}
Follow these steps to prepare your development environment:


- **Ensure you have a JDK installed** - you will need version 7 or higher, latest Java 8 is recommended.

#### Checking your JDK version

To check your installed Java version:

1. Open a command line window.
1. Run `set JAVA_HOME`
1. A response similar to this suggests you have a JDK installed:


```java
JAVA_HOME=C:\jdk1.6
```

To check the JDK version, run `%JAVA_HOME%\bin\java \-version`. A response like this from `Java` indicates you have a valid JDK installed:


```java
java version "1.6.0_23"
Java(TM) SE Runtime Environment (build 1.6.0_23-b05)
Java HotSpot(TM) Client VM (build 11.0-b16, mixed mode, sharing)
```

If your installed JDK version is lower then 1.5 or none is installed, see below on how to install one.

#### Installing a proper JDK (Java Development Kit)

- To install JDK 1.6, download and install [**JDK 6 Update X**](http://java.sun.com/javase/downloads/index.jsp)


{{% anchor 1 %}}


- **Download and unzip the latest XAP release** from the [downloads page](http://www.gigaspaces.com/LatestProductVersion).
{{% anchor 2 %}}


- **Install a Java IDE**: If you don't have an IDE installed, you can [download and unzip the Eclipse IDE for Java Developers](http://www.eclipse.org/downloads), or the [IntelliJ IDEA](http://www.jetbrains.com/idea/download/index.html) IDE (we recommend the Ultimate Edition because of its excellent Spring framework support).
If you're using Eclipse, it is also recommended to install the [Spring Tool Suite plugin for Eclipse](http://www.springsource.com/developer/sts).

# Steps to run the application inside Eclipse IDE

1. Start Eclipse. A **Workspace Launcher Dialog** appears.
1. Write a new workspace name or select one of your existing workspaces, and click the **OK** button.
1. To import the project, select **File > Import ...** to open the import dialog
1. Select **Existing projects into workspace* and click *Next** to open the import project dialog
1. In the **Select root directory* field click the *Browse** button to open the browse dialog
1. Select the folder `/examples/helloworld` and click **OK**
1. Make sure all 3 projects are selected: **hello-common**, **hello-processor** and **hello-feeder**
1. Click **Finish**
1. Create a new Eclipse environment variable called **GS_HOME**, and point it to your GigaSpaces installation Root folder

1. Right Click on the **hello-common** project in the **Package Explorer tab** to open the _context menu_
1. Select **Build Path > Configure Build Path...** to open the _Java Build Path dialog_
1. Select the **Libraries tab* and click the *Add Variable...** button to open the _New Variable Classpath Entry dialog_
1. Click the **Configure Variables...** button to open the _Classpath Variables dialog_
1. Click the **New...** button to open the _New Variable Entry_ dialog
1. In the **Name** field write `GS_HOME` to name the variable
1. Click the **Folder...** button and browse to **your GigaSpaces installation root folder**
1. Select your **GigaSpaces installation root folder** and click **OK**
1. Click **OK** and **OK** again
1. Click **Yes** to do full rebuild
1. Close remaining dialogs

# Classpath

XAP libraries are located under `XAP_HOME/lib`. There are three sub directories:

* `lib/required` - jar files that are required for any GigaSpaces application.
* `lib/optional` - jar files which enable additional capabilities, such as servlet api.
* `lib/platform` - jar files which are used by the XAP platform only.

## Compilation

In order to compile and run XAP applications, all the `jar` files under the `XAP_HOME/lib/required` directory should be included in your compile and run time classpaths. Additional jar files which you may need for development are located at `XAP_HOME/lib/optional`.

## Runtime - Processing Unit

When an application is deployed as processing unit, there is no need to add XAP specific jar files to the processing unit classpath. If such are added under the processing unit's `lib` directory, the system will remove those jar files and will replace those with the systems jars for compatibility.

## Runtime - Standalone

When running a standalone client which accesses XAP, please ensure that all the jars located under the `XAP_HOME/lib/required` directory are part of the JVM's classpath. This also holds true for remote Space clients which are used from another JVM (such as a standalone web container). In case your client is a JEE web application that is not running within the XAP runtime environment, or more specifically a GSC, you will have to include these jars in your application's `WEB-INF/lib` directory.

# Maven
 
XAP is Maven-friendly - it is built using maven and designed to be easily used by developers constructing XAP applications.  
    
The main dependency required to use XAP is `xap-openspaces`
 
 ```xml
 <dependency>
   <groupId>org.gigaspaces</groupId>
   <artifactId>xap-openspaces</artifactId>
   <version>{{%version "maven-version" %}}</version>
 </dependency>
 ```
 
 Since XAP artifacts are currently not published in Maven Central Repo, you'll also need to configure a repository:
 
 ```xml
 <repository>
    <id>org.openspaces</id>
    <url>http://maven-repository.openspaces.org</url>
 </repository>
 ```
