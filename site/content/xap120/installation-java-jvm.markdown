---
type: post120
title:  JVM Location
categories: XAP120
parent: installation-java-overview.html
weight: 400
---



XAP has a dependency on the `JAVA_HOME` environment variable being set. Make sure you have an environment variable `JAVA_HOME` defined pointing to your JDK root directory.
In the case of `JAVA_HOME` not being set, GigaSpaces script files will print an error message.

    YOU MUST SUPPLY A JAVA_HOME environment variable or set it in "XAP_HOME\bin\setenv"
    The system cannot find the path specified.

You may also modify the `XAP_HOME\bin\setenv-overrides.bat` to set the `JAVA_HOME` variable.

Unix: `setenv-overrides.sh`

    # - Set or override the JAVA_HOME variable
    # - By default, the system value is used.
    JAVA_HOME="/utils/jdk1.6.0_23"

Windows: `setenv-overrides.bat`

    @rem - Set or override the JAVA_HOME variable
    @rem - By default, the system value is used.
    set JAVA_HOME="D:\jdk1.6.0_23"
