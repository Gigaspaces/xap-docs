---
type: post122
title:  Deploy a Web App
categories: XAP122TUT, OSS
weight: 1200
parent: none
---



In this part of the tutorial we will show you how you can deploy a standard WAR file onto the Service Grid.




XAP allows you to deploy web applications (packaged as a WAR file) onto the Service Grid. The integration is built on top of the Service Grid Processing Unit Container. The web container used behind the scenes is Jetty.




The integration allows you to make use of the following Service Grid features:

- Dynamic allocation of several instances of a web application (probably fronted by a load balancer).
- Management of the instances running (if a GSC fails, the web application instances running on it will be instantiated on a different GSC).
- SLA monitor based dynamic allocation and de-allocation of web application instances.



# Deployment
The web application itself is a pure, JEE based, web application. The application can be the most generic web application, and automatically make use of the Service Grid features.
 
Here is an example how you can deploy a web application on top of the service grid. 
We can accomplish this in three ways; by command line, with the admin UI or with the REST API:

## Command line deployment
You deploy a war file just like you deploy a pu jar.

{{%tabs%}}
{{%tab "Unix CLI"%}}

```bash
GS_HOME/bin/gs.sh deploy tutorial.war
```
{{% /tab %}}
{{%tab "Windows CLI"%}}

```bash
GS_HOME\bin\gs.sh deploy tutorial.war
```
{{% /tab %}}
 
{{%tab "REST"%}}
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ 
   "name": "eventPU", 
   "resource": "...path..to\eventPU.jar"  
 }' 'http://localhost:8090/v1/deployments'
```
{{%/tab%}}
{{%/tabs%}}


## Deploy with Web Admin UI
You can deploy a war file just like a PU with the Web Administration UI:

{{%section%}}
{{%column width="20%"%}}
Host view

{{%popup "/attachment_files/qsg/Deploy-web1.png"  "Host view"%}}
{{%/column %}}

{{%column width="20%"%}}
Upload war file

{{%popup "/attachment_files/qsg/Deploy-web2.png"  "Upload war file"%}}
{{%/column%}}
 
{{%column width="20%"%}}
Application display

{{%popup "/attachment_files/qsg/Deploy-web3.png"  "Application display"%}}
{{%/column%}}

{{%column width="20%"%}}
Service display

{{%popup "/attachment_files/qsg/Deploy-web4.png"  "Service display"%}}
{{%/column%}}

{{%column width="20%"%}}
Web page display

{{%popup "/attachment_files/qsg/Deploy-web5.png"  "Web page display"%}}
{{%/column%}}
{{%/section%}}

There are fully functional examples on GitHub available. {{%tryit "https://github.com/Gigaspaces/xap-tutorial"%}}



### Using a Space
The web application can define a space either embedded or remote using Spring or direct in java code. There are several ways how a space and other components can be used and configured within a web application. Here are some scenarios:

- Creating a space using java code:

```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("xapTutorialSpace");

GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();
```


- Using PU.xml to create a space:
A web application is still just a processing unit. This means that a META-INF/spring/pu.xml can be added, where we define the standard XAP components. 

Here is the layout of the war file structure:

```bash
|----META-INF
|--------spring
|------------pu.xml
|------------pu.properties
|------------sla.xml
|--------MANIFEST.MF
|----WEB-INF
|--------lib
|--------web.xml
|----index.jsp
|---- .jsp
```

For example in the pu.xml file we can define our space that we want to be accessible from the web application.

```xml
<os-core:embedded-space id="space" space-name="xapTutorialSpace" />
<os-core:giga-space id="xapTutorialSpace" space="space"/>
```


Here is an example of a simple JSP that uses it:

```java
GigaSpace gigaSpace = (GigaSpace) getServletContext().getAttribute("xapTutorialSpace");
```


{{%refer%}}[Web Application Support]({{%currentjavaurl%}}/web-application-support.html){{%/refer%}}


# Jetty Instance
{{%section%}}
{{%column width="80%" %}}

Jetty itself is configured using Spring, and allows you to control all aspects of both the Jetty instance created, and the web application context. There are two flavors of how Jetty instances are created (by default). The first is the plain mode, where a Jetty instance is created for each web processing unit instance running within a GSC. The second is the shared mode, where a single Jetty instance is created, and shared between all the different web processing unit instances running on the same GSC. A custom Jetty instantiation and handling can also be configured.
{{%/column%}}
{{%column width="20%" %}}

{{%popup   "/attachment_files/qsg/web_app_archi.jpg" "Jetty Instance"%}}

{{%/column%}}
{{%/section%}}

{{%refer%}}[Web Application Support]({{%currentjavaurl%}}/web-application-support.html){{%/refer%}}






# HTTP Session management
The XAP-Jetty integration comes with a support for storing the javax.servlet.http.HttpSession in the Space allowing supporting session replication and failover between different web application instances deployed into GigaSpaces containers.

The GigaSpaces HTTP Session Management provides the following:

- Avoid using database as the persistent storage media - Better performance and simplify the deployment
- Allow management and control of full lifecycle of web application - From the load-balancer to the data
- Dynamic scalability - Allows the web application to scale up or down based on SLA
- Continuous High-Availability - Allows the application to survive any system failures
- Non intrusive session management or explicit Data Grid API access for fine grain control - Allows multiple web servers to share the same session in transparent manner





### Enabling HTTP Session Management
There are several ways to configure Space session based support, depending on the definition of which space and how the space was started.

For example, if the session will be stored on a remote clustered space with a local cache, the URL can be: jini://*/*/sessionSpace?useLocalCache (assuming the name of the space is sessionSpace). If the session should be stored on a space that should be started within the web application.

- Using Deploy property
Here is how you can deploy an existing WAR file without changing it (or use Spring) to use XAP's HTTP Session Management:


```bash
gs deploy -properties embed://jetty.sessions.spaceUrl=jini://*/*/sessionSpace?useLocalCache xapTutorialSpace.war
```

In this example we connect to a remote space called sessionSpace and create a local cache for the session caching.

- Using Spring
When deploying, include within the META-INF/spring/pu.properties file the jetty.sessions.spaceUrl property value. This should be set with the space url defining where the sessions will be stored.

### Deploying with the Web Admin UI:

There are fully functional examples on GitHub available.{{%tryit "https://github.com/Gigaspaces/xap-tutorial"%}}


{{%section%}}

{{%column width="25%"%}}
Deploy http Space

{{%popup "/attachment_files/qsg/Deploy-session1.png"  "Deploy http Space"%}}
{{%/column%}}

{{%column width="25%"%}}
Define deployment

{{%popup "/attachment_files/qsg/Deploy-session2.png"  "Define deployment"%}}
{{%/column%}}

{{%column width="25%" %}}
Inspect data grid

{{%popup "/attachment_files/qsg/Deploy-session3.png"  "Inspect data grid"%}}
{{%/column%}}

{{%column width="25%"%}}
Deploy war file

{{%popup "/attachment_files/qsg/Deploy-session4.png"  "Deploy war file"%}}
{{%/column%}}
{{%/section%}}

{{%section%}}
{{%column width="25%"%}}
Select web URL

{{%popup "/attachment_files/qsg/Deploy-session51.png"  "Select web URL"%}}
{{%/column%}}

{{%column width="25%"%}}
Web page

{{%popup "/attachment_files/qsg/Deploy-session6.png"  "Web page"%}}
{{%/column%}}

{{%column width="25%"%}}
Inspect data grid

{{%popup "/attachment_files/qsg/Deploy-session65.png"  "Inspect data grid"%}}
{{%/column%}}

{{%column width="25%"%}}
Inspect session

{{%popup "/attachment_files/qsg/Deploy-session7.png"  "Inspect session"%}}
{{%/column%}}
{{%/section%}}


{{%refer%}}[HTTP Session Management]({{%currentjavaurl%}}/http-session-management.html){{%/refer%}}


# Global HTTP Session Sharing
{{%section%}}
{{%column width="80%" %}}

XAP lets you share HTTP session data across multiple data centers, multiple web server instances or different types of web servers. Here are few scenarios where HTTP session sharing is required:

- Multiple different Web servers running your web application
  You may be porting your application from one web server to another and there will be a period of time when both types of servers need to be active in production.
- Web Application is broken into multiple modules
  When applications are modularized such that different functionalities are deployed across multiple server instances. For example, you may have login, basic info, check-in and shopping functionalities split into separate modules and deployed individually across different servers for manageability or scalability. In order for the user to be presented with a single, seamless, and transparent application, session data needs to be shared between all the servers.
- Reduce Web application memory footprint
  The web application storing all session within the web application process heap, consuming large amount of memory. Having the session stored within a remote process will reduce web application utilization avoiding garbage collocation and long pauses.
- Multiple Data-Center deployment
  You may need to deploy your application across multiple data centers for high-availability, scalability or flexibility, so session data will need to be replicated

{{%/column%}}
{{%column width="20%" %}}
<br>
<br>
<br>
<br>

{{%popup   "/attachment_files/qsg/httpSessionSharing1.png" "Global HTTP Session Sharing"%}}
{{%/column%}}
{{%/section%}}

{{%refer%}}[Global HTTP Session Sharing]({{%currentjavaurl%}}/global-http-session-sharing.html){{%/refer%}}


# Load Balancing
{{%section%}}
{{%column width="80%" %}}

When deploying a highly available web site, usually a load balancer is used to load balance requests between at least two instances of a web containers that run the web applications. When using XAP to deploy web applications, running more than one instance of a web application becomes very easy. XAP comes with a built-in utility allowing you to dynamically update an Apache httpd web server load-balancing configuration, based on deployed web applications.

The integration dynamically creates and updates the mod_proxy_balancer configuration, based on the state of the deployed web applications. Once changes occur (relocation / failover / changes to the number of web application instances), the utility identifies the change, updates the balancer configuration, and sends a soft restart to Apache to take the new configuration into account.


{{%/column%}}
{{%column width="20%" %}}
<br>
{{%popup   "/attachment_files/qsg/httpd_lb_agent.jpg" "Load Balancing"%}}
{{%/column%}}
{{%/section%}}


 
 
{{%refer%}}[Apache Load balancer Agent]({{%currentjavaurl%}}/apache-load-balancer-agent.html){{%/refer%}}




