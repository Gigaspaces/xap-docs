---
type: post97
title:  Maven Plugin
categories: XAP97
parent: installation.html
weight: 500
---

{{% ssummary%}}{{% /ssummary %}}



Maven is a tool used to automate and simplify the development cycle of any Java-based project. The OpenSpaces plugin for Maven utilizes Maven to simplify the development cycle of OpenSpaces-based applications. You can use this plugin to easily create, compile, package, run unit tests, execute and deploy Processing Units.

You don't need to be an experienced Maven user to start working with this plugin. This section provides you with everything you need to know in order to start developing Processing Units with the Maven plugin. Experienced Maven users can use the Maven plugin to embed Processing Unit development with their existing development environment.

{{% info %}} The OpenSpaces Maven plugin has been tested with Maven 3.0. For further information about maven see: [apache.org; What is Maven?](http://maven.apache.org/what-is-maven.html)
{{%/info%}}


# Prior to Installation

In order to use the OpenSpaces Maven plugin, Maven needs to be installed on the machine. If a Maven installation already exists on the machine, it can be used. If not, GigaSpaces comes with a ready-to-use distribution of Maven 3.0, located under: `<JSHOMEDIR>/tools/maven/apache-maven-3.0`.

All you need to do is add the Maven `bin` directory to the system `PATH` variable, and you are ready to go. To test whether the Maven command is accessible, open a command line window, type `mvn \-version`, and press Enter.
The following message should be displayed:


```bash
>mvn -version

Apache Maven 3.0 (r1004208; 2010-10-04 13:50:56+0200)
Java version: 1.6.0_23
OS name: "windows 7" version: "6.1" arch: "x86" Family: "windows"
```

{{% note %}} **First uses of Maven require internet connection** in order for the local repository to be populated with required libraries. Once all required libraries are in the local repository the internet connection is not mandatory. {{%/note%}}

{{% info %}} **Dependency Download by Maven**
Maven uses repositories: a local repository where all required dependencies (artifacts) are stored for Maven's use, and remote repositories from which Maven downloads required dependencies that are missing in the local repository. If a dependency is missing from the local repository during execution, Maven automatically downloads the missing dependencies from the remote repositories. The download might take a few minutes (progress messages are printed to the console). When the download is finished, Maven returns to its original tasks.
{{%/info%}}

# Installation

To install the OpenSpaces Maven plugin:

Run the `installmavenrep` script from the `<GigaSpaces Root>\tools\maven` directory:


```bash
D:\gigaspaces-xap-premium-8.0.0-ga\tools\maven>installmavenrep.bat
```

This installs the GigaSpaces libraries and the OpenSpaces Maven plugin into the local Maven repository. Once the installation is finished, the Maven plugin is ready to be used.

{{% info %}} The OpenSpaces Maven plugin is installed under: `<maven-repository-dir>\org\apache\maven\plugins\maven-openspaces-plugin`{{%/info%}}

## Location of Libraries and Local Repository

**Library Location**:

- XAP libraries are installed under: `<maven-repository-dir>/com/gigaspaces`

{{%anchor dependencies%}}

### Dependencies


```xml
<dependency>
  <artifactId>gs-runtime</artifactId>
  <groupId>com.gigaspaces</groupId>
maven-version
  <version>{{%version "maven-version"%}}</version>
</dependency>
<dependency>
  <artifactId>gs-openspaces</artifactId>
  <groupId>com.gigaspaces</groupId>
maven-version
  <version>{{%version "maven-version"%}}</version>
</dependency>
```

### Local Repository Location

By default, Maven creates the local repository under the your home directory: `<USER_HOME>\.m2\repository`. For example, on Windows XP, the local repository is created in `C:\Documents and Settings\<username>\.m2\repository`. However, the location of the repository can be changed by editing the `settings.xml` file under `<Maven Root>\conf`.

### public repository Location

You can install the OpenSpaces artifacts using a public repository:


```xml
<repository>
   <id>org.openspaces</id>
   <url>http://maven-repository.openspaces.org</url>
</repository>
```

# Using Available Project Templates

You may view list of available project templates and their description using the following command:


```bash
mvn os:create
```

The result is a list of available template names and descriptions:


|Template Name | Description|
|:-------------|:-----------|
|basic|Creates a basic SBA application with two processing units. The Feeder processing unit sends Data objects through the Space to a Processor. The Space and the Processor are collocated in the same processing unit.|
|basic-async-persistency| Creates a basic SBA application with three processing units. The Feeder processing unit sends Data objects through the Space to a Processor. The Space and the Processor are collocated in the same processing unit.The Processor is connected to a Mirror and provides a reliable async replication and persistency to the Database using Hibernate. |
|basic-xml| Creates a basic SBA application with two processing units. The Feeder processing unit sends Data objects through the Space to a Processor. The Space and the Processor are collocated in the same processing unit.|
|basic-async-persistency-xml|Creates a basic SBA application with three processing units. The Feeder processing unit sends Data objects through the Space to a Processor. The Space and the Processor are collocated in the same processing unit. The Processor is connected to a Mirror and provides a reliable asynchronous replication and persistency to the Database using Hibernate.|
|mule|Creates a SBA application with two processing units that use mule as an ESB. The Feeder processing unit writes Data objects to the Space. The Processor processing unit uses the extended SEDA model to defines 3 services. A Verifier service that verifies unprocessed Data objects, an Approver service that approves verified Data objects and a Processor service that processes approved Data objects. The Space and the Processor are collocated in the same processing unit.|

Use the -Dtemplate=<template> argument to specify a project template. Example:


```bash
mvn os:create -Dtemplate=basic-async-persistency
```

# Creating Processing Unit Project

The OpenSpaces Maven plugin can create Processing Unit projects. It generates the resources and the appropriate directory structure, making it easy to immediately start working on the Processing Units. Projects can be created in any directory. Before creating the project change to the directory where the project should be created. To create a Processing Unit project, use the following command-line:


```bash
mvn os:create
    -DgroupId=<group-id>
    -DartifactId=<artifact-id>
    -Dtemplate=<project-template>
```


| Argument | Description | Required | Default |
|:---------|:------------|:---------|:--------|
| `groupId` | The project package name | No | `com.mycompany.app` |
| `artifactId` | The project name | No | `my-app` |
| `template` | The project template | Yes | |

The project is generated in the current directory (`my-app` directory).

{{% info %}} Executing `os:create` without specifying a template shows a list of available templates and their description.

To start working with the project (compiling, packaging etc...) you should change directory to the directory of the project.
{{%/info%}}

# Processing Unit Project Structure

Basically, a Processing Unit project structure is what Maven users call a multi-module project. It consists of a main (top-level) project that contains sub-projects called modules. A Processing Unit is implemented as a module of the main project, thus a main project might consist of many Processing Units.

The project, created by the `default` template, consists of a main project and three modules (sub-projects):

- **feeder** -- a Processing Unit that writes data into the space.
- **processor** -- a Processing Unit that takes data from the space, processes it and writes the results back to the space.
- **common** -- a module that contains resources shared by both the feeder and the processor.

{{% info %}} The archives generated by the common module and its dependencies are added to the `lib` directory of the feeder's and processor's distributables. {{%/info%}}

The main project and each of the modules contain a project-descriptor file called `pom.xml`; which contains information about the project's properties, dependencies, build configuration, and so on. A module is considered a Processing Unit module if its `pom.xml` file contains the property `gsType=PU`. In this case, only the feeder and the processor are considered Processing Unit modules.


# Compiling the Processing Unit Project

In order to compile the Processing Unit project, use the following command line from the main project's directory.


```bash
mvn compile
```

This compiles each module and puts the output files under the modules' _target_ directory.

# Running Processing Unit Modules

Sometimes, during development, the developer might want to run the Processing Unit module to check its functionality. The OpenSpaces Maven plugin allows you to run Processing Unit modules without the need to package them as Processing Unit distributables first. This feature saves time, while evading build phases that are not required for this task.

{{% info %}} To run modules, they need to be compiled first.{{%/info%}}

Make sure you are in the directory of the project.
To run Processing Unit modules, use the following command-line (found in the `artifactId` folder):


```bash
mvn os:run
    -Dcluster=<"cluster-properties">
    -Dgroups=<groups>
    -Dlocators=<locators>
    -Dproperties=<"context-level-properties-location">
    -Dmodule=<module-name>
```


| Argument | Description | Required | Properties | Example |
|:---------|:------------|:---------|:-----------|:--------|
| `cluster` | Cluster properties | No | * `schema` -- the cluster schema name{{<wbr>}}- `total_members` -- a list of the cluster members, separated by a comma{{<wbr>}}- `id` -- the cluster ID{{<wbr>}}- `backup_id` -- the backup ID | * `schema=partitioned`{{<wbr>}}- `total_members=1,1`{{<wbr>}}- `id=1`{{<wbr>}}- `backup_id=1` |
| `groups` | Comma-delimited list of lookup group names | No | | group1,group2 |
| `locators` | Comma-delimited list of Jini locators hosts | No | | jini://<hostname1>, jini://<hostname2> |
| `properties` | Location of context-level properties | No| * `file` -- the properties file{{<wbr>}}- `embed` -- property definition | file://config/context.properties{{<wbr>}}    embed://prop1=value1 |
| `module` | The name of the Processing Unit module to run | No| | `feeder` |

**Example:**


```bash
mvn compile os:run -Dcluster="schema=partitioned total_members=1,1
id=1" -Dproperties="embed://prop1=value1" -Dmodule=feeder
```

## Determining Module Execution

- If the current directory is a the base directory of a module, only this module is executed.
- If the current directory is the main project directory and the `module` argument is not set, all modules are executed one by one.
- If the current directory is the main project directory and the `module` argument is set, only the specified module is executed.

{{% anchor overriding %}}

## Overriding Space/Cluster Configuration

If you need to override the configuration of the space or cluster when running the processing units through the OpenSpaces plugin and you want to do it by replacing the original configuration files, you can do it by placing the required file in the project's root directory.

Examples:
To change the logging configuration place the new _gs_logging.properties_ file in the _config_ directory (you may need to create this directory) under the project's root directory.

To change the security permissions place the new _policy.all_ file in the _policy_ directory (you may need to create this directory) under the project's root directory.

{{% anchor packaging %}}

# Packaging Processing Units

In order to deploy Processing Units, you need to package them in a distributable form. The OpenSpaces Maven plugin allows you to package two types of distributables supported by GigaSpaces: a single JAR archive and an open directory structure.

Make sure you are in the directory of the project.
To package the Processing Units, use the following command-line from the main project directory:


```bash
mvn package
```

The Processing Units' distributable bundles are generated for each module, under the directory `target`. For example, the distributables of a module named `feeder` are generated under `<proj-dir>\feeder\target`.

The single JAR distributable is `feeder.jar`; the open directory structure distributable is created under the directory `feeder`.

## Suppressing Unit Test Execution While Packaging

If not specified explicitly, unit tests are executed when packaging the Processing Units.

To suppress the execution of unit tests, add one of the following arguments to the command line: `skipTests` or `maven.test.skip`:


| Argument | Description |
|:---------|:------------|
| `skipTests` | Skips the unit test execution, but still performs unit test compilation |
| `maven.test.skip` | Skips the unit testing phase entirely, including the test compilation |

For example:


```bash
>mvn package -DskipTests

 .. or ..

>mvn package -Dmaven.test.skip
```

# Running Processing Units

After packaging the Processing Units, you might want to test the validity of the assemblies. The OpenSpaces Maven plugin makes it possible to run the Processing Units as standalone modules. The Maven plugin includes all the assembly dependencies in the execution classpath, making sure that the Processing Unit finds all the required resources. Managing to run the Processing Unit as a module while failing to run it as a standalone module might imply that a problem exists with the assembly definitions.

Make sure you are in the directory of the project.
To run Processing Units as standalone modules, use the following command-line:


```bash
mvn os:run-standalone
    -Dcluster=<"cluster-properties">
    -Dgroups=<groups>
    -Dlocators=<locators>
    -Dproperties=<"context-level-properties-location">
    -Dmodule=<module-name>
```


| Argument | Description | Required | Properties | Example |
|:---------|:------------|:---------|:-----------|:--------|
| `cluster` | Cluster properties | No | * `schema` -- the cluster schema name{{<wbr>}}- `total_members` -- a list of the cluster members, separated by a comma{{<wbr>}}- `id` -- the cluster ID{{<wbr>}}- `backup_id` -- the backup ID | * `schema=partitioned`{{<wbr>}}- `total_members=1,1`{{<wbr>}}- `id=1`{{<wbr>}}- `backup_id=1` |
| `groups` | Comma-delimited list of lookup group names | No | | group1,group2 |
| `locators` | Comma-delimited list of Jini locators hosts | No | | jini://<hostname1>, jini://<hostname2> |
| `properties` | Context-level properties location | No | * `file` -- properties file{{<wbr>}}- `embed` -- properties definition | {{<wbr>}}    file://config/context.properties{{<wbr>}}    embed://prop1=value1 |
| `module` | The name of the Processing Unit module to run | No | | `feeder` |

**Example:**


```bash
mvn os:run-standalone -Dcluster="schema=partitioned total_members=1,1
id=1" -Dproperties="embed://prop1=value1" -Dmodule=feeder
```

## Determining Processing Unit Execution

- If the current directory is a Processing Unit module's base directory, only this Processing Unit is executed.
- If the current directory is the main project directory and the `pu-name` argument is not set, all Processing Units are executed one by one.
- If the current directory is the main project directory and the `pu-name` argument is set, only the specified Processing Unit is executed.

## Overriding Space/Cluster Configuration

Overriding the space and cluster configuration is explained in [Running Processing Unit Modules](#overriding).

# Deploying Processing Units

Processing Units usually run in the Service Grid. In order to deploy a Processing Unit, you first need to package it (see [Packaging Processing Units](#packaging)).

GigaSpaces supports two forms of Processing Unit distributables: A single JAR archive and an open directory structure. The OpenSpaces Maven plugin allows you to deploy Processing Units simply -- packaged as JAR archives -- into the Service Grid.

{{% note %}} When deploying Processing Units, make sure that the Grid Service Manager (GSM) and the Grid Service Container (GSC) are running.{{%/note%}}

Make sure you are in the directory of the project.
Once your Processing Units are packaged, use the following command-line to deploy them to the Service Grid:


```bash
mvn os:deploy
    -Dsla=<sla>
    -Dcluster=<cluster>
    -Dgroups=<groups>
    -Dlocators=<locators>
    -Dtimeout=<timeout>
    -Dproperties=<"prop1=val1 prop2=val2...">
    -Doverride-name=<override-name>
    -Dmax-instances-per-vm=<max-instances-per-vm>
    -Dmax-instances-per-machine=<max-instances-per-machine>
    -Dmodule=<module-name>
```


| Argument | Description | Required | Default |
|:---------|:------------|:---------|:--------|
| `sla` | The SLA policy | No | |
| `cluster` | The name of the cluster | No | |
| `groups` | Comma-delimited list of lookup group names | No | gigaspaces-\<VERSION\> |
| `locators` | Comma-delimited list of Jini locators hosts | No | |
| `timeout` | Timeout | No | 10000 |
| `properties` | The properties file name or key-value pairs | No | |
| `override-name` | Override name | No | |
| `max-instances-per-vm` | The maximum instances per virtual machine | No | |
| `max-instances-per-machine` | The maximum instances per machine (host) | No | |
| `module` | The name of the Processing Unit module to deploy | No | |

If the current directory is a Processing Unit module's base directory, only this processing unit is deployed.

If the current directory is the main project directory and the `pu-name` argument is not set, Maven deploys the Processing Unit in the order described [below](#order).

If the current directory is the main project directory and the `pu-name` argument is set, only the specified Processing Unit is deployed.

# Undeploying Processing Units

The OpenSpaces Maven plugin makes it simple to undeploy Processing Units from the Service Grid. Make sure you are in the directory of the project. To undeploy a Processing Unit from the Service Grid, use the following command-line:


```bash
mvn os:undeploy
    -Dgroups=<groups>
    -Dlocators=<locators>
    -Dtimeout=<timeout>
    -Dmodule=<module-name>
```


| Argument | Description | Required | Default |
|:---------|:------------|:---------|:--------|
| `groups` | Comma-delimited list of lookup group names | No | gigaspaces-\<VERSION\> |
| `locators` | Comma-delimited list of Jini locators hosts | No | |
| `timeout` | Timeout | No | 10000 |
| `module` | The name of the Processing Unit module to undeploy | No | |

- If the current directory is a Processing Unit module's base directory, only this Processing Unit is undeployed.
- If the current directory is the main project directory and the `pu-name` argument is not set, Maven undeploys the Processing Unit the order described [below](#order).
- If the current directory is the main project directory and the `pu-name` argument is set, only the specified Processing Unit is undeployed.

{{% anchor order %}}

# Controlling Order of Deployment/Undeployment

## Deployment

A Processing Unit might have a dependency on another Processing Unit (this dependency is defined in the Processing Unit `pom.xml` file). It is important to deploy these Processing Units in the right order to prevent errors.

- The independent Processing Unit should be deployed first, and the the dependent Processing Unit should be deployed second.
- The Maven plugin identifies these dependencies and deploys the Processing Units in the right order.
- If there is no dependency between the Processing Units, they are deployed in the same order in which the modules are declared in the main project `pom.xml` file.

## Undeployment

Undeployment of Processing Units takes place in a reverse order: the dependent Processing Unit is undeployed first and the independent second.

# Adding Dependencies to Modules

A dependency is a library (usually a JAR archive containing class libraries) required by the Processing Unit for compilation, execution, etc.
For example, if the Processing Unit's code uses a class from an external archive, this archive needs to be added as a dependency of the Processing Unit.
Adding dependencies is done a Maven-typical way, which is editing the module's `pom.xml` file.
For example, to add `commons-logging` version 1.1.1 as a dependency to the processor Processing Unit, add the following XML snippet to the `<dependencies>` section of the `pom.xml` file:

{{% anchor xml %}}


```xml
<project>
    ...
    <dependencies>
        ...
        <!--The added snippet-->
        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1.1</version>
            <scope>compile</scope>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```

## Private Dependencies

Private dependencies are Processing Unit dependencies that are not shared with other Processing Units. Processing Unit distributions hold private dependencies in the `lib` directory. To add private dependency, add it to the Processing Unit module `pom.xml` file. For example, to add the `commons-logging` version 1.1.1 as a private dependency of the processor Processing Unit, add the XML snippet [above](#xml) to the **processor** module's `pom.xml` file. When the Processing Unit is packaged, the `commons-logging` archive is located under the `lib` directory of the processor distributable.

## Shared Dependencies

Shared dependencies are Processing Unit dependencies that are shared with other Processing Units. To add shared dependencies, add the dependencies to the common module `pom.xml` file. For example, to add the `commons-logging` version 1.1.1 as a shared dependency of the processor and the feeder Processing Units, add the XML snippet [above](#xml) to the **common** module's `pom.xml` file. When the Processing Units are packaged, the `commons-logging` archive is located under the `lib` directory of the processor and the feeder distributables.

# Importing Processing Unit Projects to Eclipse IDE

It is possible to import a Processing Unit project into the Eclipse environment. Imported projects have built-in launch targets, allowing you to run the processor and the feeder using Eclipse run (or debug) targets.

## 1. Generate Eclipse Project

Execute the following command from the project root directory:


```bash
mvn eclipse:eclipse
```

This generates a `.project` file under each module's base directory.

## 2. Import Generated Projects to Eclipse Environment

1. Select **File** > **Import** > **Existing Projects into Workspace**.
1. In the **Import** dialog, keep the **Select root directory** option selected, and click **Browse**.
1. Select the base directory of the project you want to import and click **Finish**.

This imports the three modules to Eclipse, each as a separate project.

## 3. Define M2_REPO Variable

Imported projects use a variable called `M2_REPO` to point to the location of Maven's local repository. If this is a fresh Eclipse installation, the `M2_REPO` variable needs to be defined:

1. Select **Window** > **Preferences**.
1. In the **Preferences** dialog, select **Java** > **Build Path** > **Classpath Variables**, and click **New**.
1. In the **New Variable Entry** dialog, type `M2_REPO` in the **Name** field.
1. Press **Folder** and select the directory of Maven's local repository.
1. Click **OK** to close all dialogs.

## 4. Convert Generated Projects To Maven Projects

Do the following for each project:

1. Right click on the project.
1. Select **Configure** > **Convert to Maven Project**.

# Viewing Persistent Data

When running a Processing Unit that uses persistency, e.g when using the _basic-async-persistency_ template, one would like to view the persisted data. OpenSpaces Maven Plugin makes it easy to start the HSQLDB viewer to immediately view persisted data.

{{% note %}} The HSQLDB viewer is for monitoring HSQLDB databases only. {{%/note%}}

To start the HSQLDB viewer use the following command-line:


```bash
mvn os:hsql-ui
    -Ddriver=<driver-class>
    -Durl=<url>
    -Duser=<user>
    -Dpassword=<password>
    -Dhelp
```


| Argument | Description | Required | Default |
|:---------|:------------|:---------|:--------|
| `driver` | JDBC driver class | No | org.hsqldb.jdbcDriver |
| `url` | JDBC url | No | jdbc:hsqldb:hsql://localhost/testDB |
| `user` | User name used for the connection | No | |
| `password` | Password used for this user | No | |
| `help` | Prints the usage options | No | |

{{% info %}} The default values are sufficient when using the data source values generated by the plugin. {{%/info%}}


