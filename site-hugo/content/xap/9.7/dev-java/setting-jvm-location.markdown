---
type: post97
title:  Setting JVM Location
categories: XAP97
parent: installation-java.html
weight: 300
---



XAP has a dependency on the `JAVA_HOME` environment variable being set. Make sure you have an environment variable `JAVA_HOME` defined pointing to your JDK root directory.
In the case of `JAVA_HOME` not being set, GigaSpaces script files will print an error message.

    YOU MUST SUPPLY A JAVA_HOME environment variable or set it in "<GigaSpaces installation>\bin\setenv"
    The system cannot find the path specified.

You may also modify the `<GigaSpaces Cache Root>\bin\setenv.sh` to set the `JAVA_HOME` variable.
Unix: `setenv.sh`

    # - Set or override the JAVA_HOME variable
    # - By default, the system value is used.
    JAVA_HOME="/utils/jdk1.6.0_23"

Windows: setenv.bat

    @rem - Set or override the JAVA_HOME variable
    @rem - By default, the system value is used.
    set JAVA_HOME="D:\jdk1.6.0_23"
