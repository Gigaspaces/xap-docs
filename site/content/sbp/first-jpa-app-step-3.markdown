---
type: postsbp
title:  Building and Running the Application
categories: SBP
weight: 300
parent:  first-jpa-app.html
---

{{% ssummary %}}{{% /ssummary %}}

This step shows how to build, package and deploy the application while taking advantage of XAP's dynamic load balancing capabilities and the Space as a highly HttpSession store.


# Building the Example Application

The application sources and build scripts can be downloaded {{%git "https://github.com/gigaspaces/petclinic-jpa"%}}. This application uses a Maven build script, so you need to make sure you're connected to the internet when you first run it to allow Maven to download all the dependencies.



```bash
~/gs/xap/{{%version "gshome-directory" %}}/tools/maven>installmavenrep.sh
""
""
"Installing XAP {{% currentversion%}}.0-RELEASE jars"
""
""
```

- cd to the root directory of the application
- Edit the value of the `gsVersion` property in the `pom.xml` file at the root directory to reflect the GigaSpaces build you're using (this is the build number that the Maven plugin installation script outputs to the console when invoked).
For example, if you are using GigaSpaces XAP {{%version "xap-version" %}} you should modify the `pom.xml` to have:


```xml
<gsVersion>{{%version "maven-version" %}}</gsVersion>
```

- Run the following Maven command:


```bash
mvn package
```

This will download the application's decencies, compile the sources and package the processor processing unit and the web application.

# Deploying the Example Application

To deploy the application, you should do the following:

- Start a [GigaSpaces Agent](/product_overview/service-grid.html#gsa)
- Run the following Maven command from the application's root directory:


```java
mvn os:deploy
```

# Creating Sample Data

To have an initial sample data set to work with, simply click the "Create Dummy Data" link in the welcome page of the application. This will create a number of `Owner` s, `Pet` s and `Vet` s that you can work with to experience the application's functionality.

![dummy-data.png](/attachment_files/dummy-data.png)

# Monitoring the Deployed Application

To monitor  the application, start the GigaSpaces UI using the `<XAP root>/bin/gs-ui.sh(bat)` or the GigaSpaces [Web UI]({{%currentadmurl%}}/web-management-console.html).

![web-ui-pc.png](/attachment_files/web-ui-pc.png)

# Backing the HttpSession with the Space for High Availability

Please refer to [this page](./first-web-app-step-2.html) for directions on how to enable `HttpSession` high availability for the web application.

# Configuring Dynamic Load Balancing

Please refer to [this page](./first-web-app-step-3.html) for directions on how to enable `HttpSession` high availability for the web application.

