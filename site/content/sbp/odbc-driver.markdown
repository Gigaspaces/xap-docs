---
type: postsbp
title:  ODBC Driver
categories: SBP
parent: data-access-patterns.html
weight: 1200
---



{{% tip %}}
**Summary:**  Access GgaSpaces Data-Grid via ODBC Driver <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 7.1<br/>
**Last Update:** April 2010<br/>


{{% /tip %}}

# Overview
GigaSpaces Data-Grid support standard data access API. One of them is the [JDBC Driver]({{%latestjavaurl%}}/jdbc-driver.html). Using the JDBC API and ODBC-JDBC bridge such as the one comes from [Open Link Software](http://uda.openlinksw.com), you can use standard ODBC API to access the GigaSpaces Data-Grid.

# GigaSpaces Installation

- You can download Gigaspaces java version from http://www.gigaspaces.com/LatestProductVersion.
- Unzip the downloaded file into a directory on your local machine. (Note: make sure the directory path does not have any spaces). This directory will be referred as <GigaSpaces root> in further instructions.
- Download and install the latest version of [Java JDK](http://java.sun.com/javase/downloads/widget/jdk6.jsp).
- Set a `JAVA_HOME` variable to the path where you have installed the JDK.

# GigaSpaces Configuration

- Start the GigaSpaces Agent by running <GigaSpaces root>\bin\gs-agent.bat(bat).
- Start the management UI by running <GigaSpaces root>\bin\gs-ui.sh(bat).

# Create a new user
Create a new user "user" and password "password" and grant it all access privileges.
See [GigaSpaces Management Center Security]({{%latestsecurl%}}/gigaspaces-management-center-ui-security.html) for instructions.

# Deploy a Secured Space

- Click  the  "Deploy In-Memory Data Grid" button (top left, second button) in the UI.
- In the dialog  select the following properties:


```java
DataGrid Name : mySpace
Space Schema : default
Secured Space checkbox enabled
User name : user, password: password
Cluster Schema <None>
```

And deploy the space.

# ODBC-JDBC bridge

- Download and install the ODBC-JDBC bridge from http://uda.openlinksw.com/odbc/st/odbc-jdbc-bridge-st.
- Set the following environment variables in your system:


```java
PATH=<Your Java Install directory>\jre\bin\server;%PATH%
CLASSPATH=<GigaSpaces root>lib\required\commons-logging.jar;
<GigaSpaces root>\lib\required\gs-openspaces.jar;<GigaSpaces root>\lib\required\gs-runtime.jar;
<GigaSpaces root>\lib\required\spring.jar
```

- Create a new OpenLink ODBC datasource with the following properties:


```java
JDBC Driver : com.j_spaces.jdbc.driver.GDriver
URL String: jdbc:gigaspaces:url:jini://*/*/mySpace
User name  User name : user, Password: password
```

You can test the connection using the openlink sample application at `Program Files\OpenLink Software\UDA\Samples\Odbc`
