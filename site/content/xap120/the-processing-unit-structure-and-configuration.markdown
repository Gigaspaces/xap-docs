---
type: post120
title:  Directory Structure
categories: XAP120
parent: the-processing-unit-overview.html
weight: 100
---


{{% ssummary   %}}{{% /ssummary %}}

# The Processing Unit Jar File

Much like a JEE web application or an OSGi bundle, The Processing Unit is packaged as a .jar file and follows a certain directory structure which enables the GigaSpaces runtime environment to easily locate the deployment descriptor and load its classes and the libraries it depends on. A typical processing unit looks as follows:


```bash
|----META-INF
|--------spring
|------------pu.xml
|------------pu.properties
|------------sla.xml
|--------MANIFEST.MF
|----com
|--------mycompany
|------------myproject
|----------------MyClass1.class
|----------------MyClass2.class
|----lib
|--------hibernate3.jar
|--------....
|--------commons-math.jar
```

The processing unit jar file is composed of several key elements:

- `META-INF/spring/pu.xml` (mandatory): This is the processing unit's deployment descriptor, which is in fact a [Spring](http://www.springframework.org) context XML configuration with a number of GigaSpaces-specific namespace bindings. These bindings include GigaSpaces specific components (such as the space for example). The `pu.xml` file typically contains definitions of GigaSpaces components ([space](/product_overview/the-in-memory-data-grid.html), [event containers](./event-processing.html), [remote service exporters](./space-based-remoting.html)) and user defined beans which would typically interact with those components (e.g. an event handler to which the event containers delegate the events, or a service beans which is exposed to remote clients by a remote service exporter).

- `META-INF/spring/sla.xml` (not mandatory): This file contains SLA definitions for the processing unit (i.e. number of instances, number of backup and deployment requirements). Note that this is optional, and can be replaced with an `<os:sla>` definition in the `pu.xml` file. If neither is present, the [default SLA]({{%currentadmurl%}}/the-sla-overview.html)  will be applied. Note, the `sla.xml` can also be placed at the root of the processing unit. SLA definitions can be also specified at the deploy time via the [deploy CLI]({{%currentadmurl%}}/deploy-command-line-interface.html) or [deploy API](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/admin/gsm/GridServiceManagers.html).

{{% note %}}
SLA definitions are only enforced when deploying the processing unit to the GigaSpaces service grid, since this environment actively manages and controls the deployment using the [GSM](/product_overview/service-grid.html#gsm). When [running within your IDE](./installation-eclipse-debug.html) or in [standalone mode](./running-in-standalone-mode.html) these definitions are ignored.
{{% /note %}}

- `META-INF/spring/pu.properties` (not mandatory): Enables you to externalize properties included in the `pu.xml` file (e.g. database connection username and password), and also set system-level deployment properties and overrides, such as JEE related deployment properties (see [this page](./web-application-support.html) for more details) or space properties (when defining a space inside your processing unit). Note, the `pu.properties` can also be placed at the root of the processing unit.

- **User class files**: Your processing unit's classes (here under the com.mycompany.myproject package)

- `lib`: Other jars on which your processing unit depends, e.g. commons-math.jar or jars that contain common classes across many processing units.

- `META-INF/MANIFEST.MF` (not mandatory): This file could be used for adding additional jars to the processing unit classpath, using the standard `MANIFEST.MF` `Class-Path` property. (see [Manifest Based Classpath](./the-processing-unit-structure-and-configuration.html#Manifest Based Classpath) for more details)

{{% tip %}}
You may add your own jars into the runtime (GSC) classpath by using the `PRE_CLASSPATH` and `POST_CLASSPATH` variables. These should point to your application jars.
{{% /tip %}}



# Sharing Libraries Between Multiple Processing Units

In some cases, multiple Processing Units use the same JAR files. In such cases it makes sense to place these JAR files in a central location accessible by all the Processing Units rather than packaging them individually with each of the Processing Units. Note that this is also useful for decreasing the deployment time in case your Processing Units contain a lot of 3rd party jars files, since it saves a lot of the network overhead associated with downloading these JARs to each of the GSCs.
There are three options to achieve this:

## lib/optional/pu-common directory
JAR files placed in the `<XAP root>/lib/optional/pu-common` directory will be loaded by each Processing Unit instance in its own separate classloader (called the Service Classloader, [see the](#ClassLoaders) section below).

This means they are not shared between Processing Units on the same JVM, which provides an isolation quality often required for JARs containing the application's proprietary business-logic. On the other hand this option consumes more PermGen memory (due to potentially multiple instances per JVM).

You can place these JARs in each XAP installation in your network, but it is more common to share this folder on your network and point the `pu-common` directory to the shared location by specifying this location in the `com.gs.pu-common` system property in each of the GSCs on your network.

When a new JAR needs to be loaded, just place the new JAR in `pu-common` directory and restart the Processing Unit.

Note: if different Processing Units use different versions of the same JAR (under same JAR file name) then `pu-common` should not be used.

## META-INF/MANIFEST.MF descriptor
JAR files specified in the Processing Unit's `META-INF/MANIFEST.MF` descriptor file will be loaded by each Processing Unit instance in its own separate classloader (called the Service Classloader, see the [Class Loaders](#classloaders) section below.

This option achieves similar behavior to the `lib/optional/pu-common` option above, but allows a more fine-grained control by enabling to specify specific JAR files (each in its own location) rather than an entire folder (and only a single folder).

For more information see [Manifest Based Classpath](#ManifestBasedClasspath) section below.

## lib/platform/ext directory
JAR files placed in the `<XAP root>/lib/platform/ext` directory will be loaded once by the GSC-wide classloader and not separately by each Processing Unit instance (this classloader is called the Common Classloader, see the [Class Loaders](#classloaders) section below).

This means they are shared between Processing Units on the same JVM and thereby offer no isolation. On the other hand this option consumes less PermGen memory (one instance per JVM).

This method is recommended for 3rd party libraries that have no requirement for isolation or different versions for different Processing Units, and are upgraded rather infrequently, such as JDBC driver.

You can place these jars in each XAP installation in your network, but it is more common to share this folder on your network and point the `platform/lib/ext` directory to the shared location on your network by specifying this location in the `com.gigaspaces.lib.platform.ext` system property in each of the GSCs on your network.

When a new JAR needs to be loaded, place the new JAR in `lib/platform/ext` directory and restart the relevant GSCs (on which an instance of the PU was running).

## Considerations
When it comes to choosing the right option for your system, the following should be considered: <br>

- Size of loaded classes in memory (PermGen)  <br>
- Size of Processing Unit JAR file and Processing Unit deployment time <br>
- Isolation (sharing classes between Processing Units) <br>
- Frequency of updating the library JAR  <br>
- In addition special attention is required to xml parsing related jars that have parllels in jdk itself,If your pu requires use of one of those jar,you should place ALL related jars in lib/platform/ext
starting with 10.1 version the product dosn't include xml parsing jars under lib/platform/xml and use default jdk jars.


# Runtime Modes

The processing unit can [run](./deploying-and-running-the-processing-unit.html) in multiple modes.

When deployed on to the [GigaSpaces runtime environment]({{%currentadmurl%}}/the-runtime-environment.html) or when running in [standalone mode](./running-in-standalone-mode.html), all the jars under the `lib` directory of your processing unit jar, will be automatically added to the processing unit's classpath.

When [running within your IDE](./installation-eclipse-debug.html), it is similar to any other Java application, i.e. you should make sure all the dependent jars are part of your project classpath.

# Deploying the Processing Unit to the Service Grid

When deploying the processing unit to [GigaSpaces Service Grid]({{%currentadmurl%}}/the-runtime-environment.html), the processing unit jar file is uploaded to the [XAP Manager (GSM)](/product_overview/service-grid.html#gsm) and extracted to the `deploy` directory of the local GigaSpaces installation (located by default under `<XAP Root>/deploy`).

Once extracted, the [GSM](/product_overview/service-grid.html#gsm) processes the deployment descriptor and based on that provisions processing unit instances to the running [XAP containers](/product_overview/service-grid.html#gsc).

Each GSC to which a certain instance was provisioned, downloads the processing unit jar file from the GSM, extracts it to its local work directory (located by default under `<XAP Root>/work/deployed-processing-units`) and starts the processing unit instance.

{{% anchor dataOnlyPUs %}}

# Deploying Data Only Processing Units

In some cases, your processing unit contains only a [Space](./the-space-configuration.html#proxy) and no custom code.

One way to package such processing unit is to use the standard processing unit packaging described above, and create a processing unit jar file which only includes a [deployment descriptor](./configuring-processing-unit-elements.html) with the required space definitions and SLA.

GigaSpaces also provides a simpler option via its built-in data-only processing unit templates (located under `<XAP Root>/deploy/templates/datagrid`. Using these templates you can deploy and run data only processing unit without creating a dedicated jar for them.

For more information please refer to [Deploying and running the processing unit](./deploying-and-running-the-processing-unit.html)

{{%anchor classloaders %}}

# Class Loaders

In general, classloaders are created dynamically when deploying a PU into a GSC. You **should not add your classes** into the GSC CLASSPATH. Classes are loaded dynamically into the generated classloader in the following cases:

- When the GSM sending classes into the GSC when the application deployed and when GSC is restarted.
- When the GSM sending classes into the GSC when the application scales.
- When a Task class or Distributed Task class and its dependencies are executed (space execute operation).
- When space domain classes and their dependencies (Data model) are used (space write/read operations)

Here is the structure of the class loaders when several processing units are deployed on the Service Grid (GSC):


```bash
Bootstrap (Java)
                  |
               System (Java)
                  |
               Common (Service Grid)
             /        \
    Service CL1     Service CL2
```

The following table shows which user controlled locations end up in which class loader, and the important JAR files that exist within each one:


| Class Loader | User Locations | Built in Jar Files |
|:-------------|:---------------|:-------------------|
| Common | \[GSRoot\]/lib/platform/ext/\*.jar | gs-runtime.jar |
| Processing Unit Instance (Service Class Loader) | \[PU\], \[PU\]/lib/\*.jar, \[PU\]/META-INF/MANIFEST.MF Class-Path Entry, \[GSRoot\]/lib/optional/pu-common/\*.jar | gs-openspaces.jar, org.springframework\*.jar |

In terms of class loader delegation model, the service (PU instance) class loader uses a **parent last delegation mode**. This means that the processing unit instance class loader will first try and load classes from its own class loader, and only if they are not found, will delegate up to the parent class loader.

## Native Library Usage

When deploying applications using native libraries you should place the Java libraries (jar files) loading the native libraries under the `GSRoot/lib/platform/ext` folder. This will load the native libraries once into the common class loader.

## Permanent Generation Space

For applications that are using relatively large amount of third party libraries (PU using large amount of jars) the default permanent generation space size may not be adequate. In such a case, you should increase the permanent generation space size. Here are suggested values:


```bash
-XX:PermSize=512m -XX:MaxPermSize=512m
```

# Manifest Based Classpath

You may add additional jars to the processing unit classpath by having a manifest file located at `META-INF/MANIFEST.MF` and defining the property `Class-Path` as shown in the following example (using a simple `MANIFEST.MF` file):


```bash
Manifest-Version: 1.0
Class-Path: /home/user1/java/libs/user-lib.jar
 lib/platform/jdbc/hsqldb.jar
 ${MY_LIBS_DIRECTORY}/user-lib2.jar
 file:/home/user2/libs/lib.jar

[REQUIRED EMPTY NEW LINE AT EOF]
```

In the previous example, the `Class-Path` property contains 4 different entries:

1. `/home/user1/java/libs/user-lib.jar` - This entry uses an absolute path and will be resolved as such.
1. `lib/platform/jdbc/hsqldb.jar` - This entry uses a relative path and as such its path is resolved in relative to the gigaspaces home directory.
1. `${MY_LIBS_DIRECTORY}/user-lib2.jar` - In this entry the `${MY_LIBS_DIRECTORY}` will be resolved if an environment variable named `MY_LIBS_DIRECTORY` exists, and will be expanded appropriately.
1. `file:/home/user2/libs/lib.jar` - This entry uses URL syntax

## The `pu-common` Directory

The `pu-common` directory may contain a jar file with a manifest file as described above located at `META-INF/MANIFEST.MF`. The classpath defined in this manifest will be shared by all processing units as described in [Sharing libraries](#SharingLibrariesBetweenMultipleProcessingUnits).

## Further details

1. If an entry points to a non existing location, it will be ignored.
1. If an entry included the `${SOME_ENV_VAL}` placeholder and there is no environment variable named `SOME_ENV_VAL`, it will be ignored.
1. Only file URLs are supported. (i.e http, etc... will be ignored)

{{%refer%}}
Further details about the manifest file can be found [here](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/technotes/guides/jar/jar.html#JAR%20Manifest).
{{%/refer%}}
