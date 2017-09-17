---
type: post122
title:  Classpath
categories: XAP122GS
parent: installation-eclipse-overview.html
weight: 150
---

XAP libraries are located under `XAP_HOME/lib`. There are three sub directories:

* `lib/required` - jar files that are required for any GigaSpaces application.
* `lib/optional` - jar files which enable additional capabilities, such as servlet api.
* `lib/platform` - jar files which are used by the XAP platform only.

# Compilation

In order to compile and run XAP applications, all the `jar` files under the `XAP_HOME/lib/required` directory should be included in your compile and run time classpaths. Additional jar files which you may need for development are located at `XAP_HOME/lib/optional`.

# Runtime - Processing Unit

When an application is deployed as processing unit, there is no need to add XAP specific jar files to the processing unit classpath. If such are added under the processing unit's `lib` directory, the system will remove those jar files and will replace those with the systems jars for compatibility.

# Runtime - Standalone

When running a standalone client which accesses XAP, please ensure that all the jars located under the `XAP_HOME/lib/required` directory are part of the JVM's classpath. This also holds true for remote Space clients which are used from another JVM (such as a standalone web container). In case your client is a JEE web application that is not running within the XAP runtime environment, or more specifically a GSC, you will have to include these jars in your application's `WEB-INF/lib` directory.
