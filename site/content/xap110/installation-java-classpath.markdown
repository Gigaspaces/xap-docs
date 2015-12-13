---
type: post110
title:  Classpath
categories: XAP110
parent: installation-java-overview.html
weight: 300
---



XAP libraries are located under `<XAP Root>\lib`

You'll find three sub directories:
**lib/required** - jar files that are required for any GigaSpaces application.
**lib/optional** - jar files which enable additional capabilities, such as servlet api and Spring CGLib support.
**lib/platform** - This directory contains jar files which are used by the XAP platform only.

# Compilation

In order to compile and run XAP applications, all the `jar` files under the `<XAP Root>/lib/required` directory should be included in your compile and run time classpaths. These include:

- `gs-runtime.jar`
- `gs-openspaces.jar`
- `commons-logging.jar`
- Spring framework jars (all start with `org.springframework.\*` or `com.springsource.\*`)

Additional jar files which you may need for development are located at:

- `<XAP Root>\lib\optional`

# Runtime - Processing Unit

When application is deployed as processing unit, there is no need to add XAP specific jar files to the processing unit classpath. If such are added under the processing unit's `lib` directory, the system will remove those jar files and will replace those with the systems jars for compatibility.

{{% tip %}}
You may add your own jars into the runtime (GSC) classpath by using the `PRE_CLASSPATH` and `POST_CLASSPATH` variables. These should point to your application jars.
{{% /tip %}}

# Runtime - Standalone

When running a standalone client which accesses   XAP, please ensure that all the jars located under the `<XAP root>/lib/required` directory are part of the JVM's classpath. These include the following jars:

- `gs-runtime.jar`
- `gs-openspaces.jar`
- `commons-logging.jar`
- Spring framework jars (all start with `com.spring*`)

This also holds true for remote Space clients which are used from another JVM (such as a standalone web container). In case your client is a JEE web application that is not running within the XAP runtime environment, or more specifically a GSC, you will have to include these jars in your application's `WEB-INF/lib` directory.
