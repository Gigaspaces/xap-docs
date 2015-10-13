---
type: post101
title:  Global HTTP Session Sharing
categories: XAP101TUT
parent: none
weight: 1500
---

{{% section %}}
{{% column width="10%" %}}
![counter-logo.jpg](/attachment_files/subject/httpsession.png)
{{% /column %}}
{{% column width="90%" %}}
{{%ssummary%}}{{%/ssummary%}}
{{% /column %}}
{{% /section %}}



{{%section%}}
{{%column width="70%" %}}
<br>
With XAP you can share HTTP session data across multiple data centers, multiple web server instances or different types of web servers.

<br>
{{%pdf "/attachment_files/httpsession/globalhttpsessionsharing-screencast2014.pdf"%}} Global HTTP Session Sharing Screencast.
{{%/column%}}
{{%column width="30%" %}}
{{%youtube "gRdGWMigJBI"  "Global HTTP Session sharing"%}}
{{%/column%}}
{{%/section%}}




This tutorial will show you:

1.	Run Single-Applications Session Sharing – sharing the same session between different Tomcat instances. <br>
2.	Run Multiple-Applications Session Sharing – sharing the same session between different applications running in different Web servers - Tomcat and JBoss.


# Prior running the Tutorial

Make sure you have enough disk space to install:

- XAP 10 - 150MB
- Tomcat 7.0.23 - 80MB
- JBoss 7 - 200MB


The demo application can be downloaded:

[demo-app.war](/attachment_files/httpsession/demo-app.war) <br>
[demo-app2.war](/attachment_files/httpsession/demo-app2.war)




# Single Application Session Sharing


With this session sharing model, the web user interacting with multiple web servers through a load balancer where each web server running the same web application. In most cases you will have multiple instances of the same web app running in a cluster configuration.  The load balancer rout requests based on the sticky session configuration to the relevant web server instance.

![http-session-1](/attachment_files/httpsession/http-session1.png)


In this scenario the session is shared via its **ID**. Where there is a web server failure or when a new web server is added to the cluster the session **ID** is used to retrieve the session from the IMDG. Only the updated attributes (delta) are sent to the IMDG when the session is updated. This ensure best application performance and minimal network bandwidth usage.


### Demo Flow
With this demo we will simulate session sharing between different tomcat instances by starting tomcat , running the application, terminating tomcat and later restarting tomcat without losing application HTTP Session data.

-	{{%download "http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.23/bin/apache-tomcat-7.0.23.zip"%}} Tomcat 7.0.23
-	Install Tomcat by unzipping it into `c:\` or `d:\`
-	Install XAP by unzipping it into `c:\` or `d:\`
-	Start Tomcat by running `\apache-tomcat-7.0.23\bin\startup.bat`
-	Move to the `\gigaspaces-xap-premium-10.0.1-ga\bin` folder and start GigaSpaces agent by running:


```bash
gs-agent.bat
```

-	Deploy a space named **sessionSpace**. You may have a single instance Space or deploy a clustered Space using the command line , GS-UI or the Web-UI. Here is how you can do this via the CLI


```bash
gs.bat deploy-space sessionSpace
```

-	Double check the **shiro.ini** within the `demo-app.war` file located under the `WEB-INF` folder includes the lookup service host name as part of the `connector.url` property. With the example below we are using a lookup service running locally - hence the `localhost` is used:


```bash
connector.url=jini://localhost/*/sessionSpace
```

-	Deploy the `demo-app.war` into Tomcat by placing it into \apache-tomcat-7.0.23\webapps
-	Start your web browser and access the web application via the following URL:


```bash
http://localhost:8080/demo-app
```

The URL above assumes the Tomcat is configured to use port 8080.

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session2.png)
{{%/panel%}}

-	Add some attributes using the interface provided
-	View the session updated within the space via the GS-UI or Web-UI

![http-session](/attachment_files/httpsession/http-session3.png)

-	Identify the Session ID

![http-session](/attachment_files/httpsession/http-session4.png)

-	Terminate Tomcat by terminating its process
-	Refresh the page (press the `F5` key) – you should get an error. There is no web server to serve the HTTP request.
-	Start Tomcat again using :


```bash
\apache-tomcat-7.0.23\bin\startup.bat
```

-	Refresh the page on the browser (press the `F5` key).
-	The session will be reloaded from the data grid. See tomcat console for log messages.


# Multi-Applications Session Sharing
With this session sharing model , the web user interacting with multiple web servers through a load balancer where each running a **different web application**. It may be one big web application that has been broken down into different modules each running within a different web server potentially running on a different host. Each web application may access different web application instance during the life cycle of the application.

In this scenario the session user login – the **principle** , used to identify the session to be shared across the different applications. You may have also multiple instances of each web application running to scale its activity. All will be sharing the same HTTP session.

Any new web server that will be added dynamically will be able to share the session. When the HTTP session is updated only the updated session attributes (delta) is sent to the IMDG. When the session is shared between different web applications where each might have a different version of the session - a timestamp check is performed to ensure the recent session is passed between the apps and also to be stored within the IMDG. In such a case , HTTP session attributes are merged to ensure no data is lost when navigating between the different web application modules.

### Demo Flow
With this demo we will simulate session sharing between Tomcat and JBoss web servers instances by starting Tomcat and JBoss , running the application on both , updating the HTTP session in both – where the session will be shared implicitly , terminating tomcat and JBoss and later restarting these without losing application HTTP session data.

-	{{%download "http://download.jboss.org/jbossas/7.1/jboss-as-7.1.1.Final/jboss-as-7.1.1.Final.zip"%}} JBoss 7.
-	Install JBoss 7 by unzipping it into `c:\` or `d:\`

![http-session](/attachment_files/httpsession/http-session5.png)

-	Double check the **shiro.ini** within the `demo-app2.war` file located under the `WEB-INF` folder includes the lookup service host name as part of the `connector.url` property. With the example below we are using a lookup service running locally - hence the `localhost` is used:

```bash
connector.url=jini://localhost/*/sessionSpace
```
-	Deploy `demo-app2.war` into Tomcat by placing it into `\apache-tomcat-7.0.23\webapps`
-	Deploy `demo-app2.war` into JBoss by placing it into `\jboss-as-7.1.1.Final\standalone\deployments`
-	Edit `\jboss-as-7.1.1.Final\standalone\configuration\standalone.xml`

To have:


```xml
<socket-binding name="http" port="8081"/>
```

-	Start JBoss by running :


```bash
\jboss-as-7.1.1.Final\bin\standalone.bat
```


-	Start your web browser and access the web application running in Tomcat via the following URL:


```bash
http://localhost:8080/demo-app2
```

-	Login into the application running on Tomcat using :
    -	user : root
    -	password : secret

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session6.png)
{{%/panel%}}

-	Add some attributes using the interface provided

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session7.png)
{{%/panel%}}

-	Start your browser and access the web application running in JBoss via the following URL:


```bash
http://localhost:8081/demo-app2
```

-	Login into the application running on JBoss using :
    -	user : `root`
    -	password : `secret`

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session8.png)
{{%/panel%}}

-	You should see all attributes added into the session via Tomcat are shared with the application running on JBoss.

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session9.png)
{{%/panel%}}

-	Add / Modify some attributes

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session10.png)
{{%/panel%}}

-	Switch to Tomcat:


```bash
http://localhost:8080/demo-app2
```

-	Refresh the page (press the `F5` key) – see all the attributes been updated.

{{%panel%}}
![http-session](/attachment_files/httpsession/http-session11.png)
{{%/panel%}}

-	Terminate both Tomcat and JBoss by terminating their process
-	Refresh these by press the `F5` key :


```bash
http://localhost:8080/demo-app2
http://localhost:8081/demo-app2

```

-	An error message will be displayed as both Tomcat and JBoss cannot serve the HTTP request.
-	Start Tomcat and JBoss
-	Refresh the pages served by Tomcat and JBoss by pressing the `F5` key
-	Login via user: `root`, password: `secret`
-	See session been fully recovered.

{{%panel%}}
{{%section%}}
{{%column width="50%" %}}
![http-session](/attachment_files/httpsession/http-session12.png)
{{%/column%}}
{{%column width="50%" %}}
![http-session](/attachment_files/httpsession/http-session13.png)
{{%/column%}}
{{%/section%}}
{{%/panel%}}


{{%learn "/xap101/global-http-session-sharing-overview.html"%}}
