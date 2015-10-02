---
type: postsbp
title:  Embedding Tomcat as a Web Container
categories: SBP
weight: 1800
parent: production.html
---

{{% ssummary %}}This article presents embedding Tomcat 7 within the XAP data grid{{% /ssummary %}}

 {{% tip %}}
 **Author**:Ali Hodroj, Director of Solution Architecture, GigaSpaces<br/>
 **Recently tested with GigaSpaces version**: XAP 9.6<br/>
 **Last Update:** Feb 2014<br/>
{{% /tip %}}



# Overview

{{%section%}}
{{%column width="80%" %}}
GigaSpaces XAP allows for embedding a Tomcat 7 web container within the service grid by deploying it as a processing unit. The integration is achieved by utilizing the [Embedded Tomcat API](http://tomcat.apache.org/tomcat-7.0-doc/api/org/apache/catalina/startup/Embedded.html), which only requires a few lines of code to start an instance within a processing unit. This integration lets the end user enjoy all the useful features of Apache Tomcat, while leveraging the high availability and SLA monitoring of the XAP service grid.
{{%/column%}}

{{%column width="15%" %}}
![tomcat_logo.jpeg](/attachment_files/logos/tomcat_logo.jpg)
{{%/column%}}
{{%/section%}}


The key features of the embedded Tomcat are:

- Auto-Scaling: Dynamic allocation and auto-scaling of Tomcat instances (hosting a web application) as well as automatically updating your load balancer through the Admin API
- Simplified Management: Consolidated management of your web tier along with your data grid under one common administrative interface (through a web, desktop, and CLI administrative interface)
- Fault Tolerance and Isolation: SLA enforcement through the Grid Service Manager by providing JVM-level, Machine-level, and Zone-level redundancy and failover upon deployment.
- Low latency: Leverage colocation of Tomcat instances with space instances to avoid network round trips that access POJO from the data grid.


# Deployment

Embedding  Tomcat along with one or more associated web application is simply a matter of deploying a skeleton tomcat processing unit that contains both the Tomcat instantiation code as well as a your target web applications self-contained within the processing unit. This provides a clean way to deploy and undeploy the entire server instance along with its associated applications.

The tomcat processing unit hierarchy, once built with maven, will have the following structure:

Tomcat PU {{<wbr>}}
|-   com  {{<wbr>}}
|-   lib    {{<wbr>}}
|-   META-INF  {{<wbr>}}
|-   webapps


|Folder	|Purpose|
|:--------|:-----------|
|com|	Contains compiled code for instantiating Tomcat as well as utilizing server parameters|
|lib|	Additional libraries required by Tomcat and web application|
|META-INF|	Contains the PU.XML file for configuring Tomcat along with an optional SLA.XML file for configuring the processing unit SLA|
|webapps|	A collection of web applications which will be loaded by Tomcat|

In order to deploy Tomcat along with your web application within XAP, the following steps must be followed:

Step 1:	Download the tomcat-pu project from [here](/download_files/sbp/tomcat-pu.tgz)<br>
Step 2:	Configure the Tomcat instance through PU.XML to specify the container and application configuration


```xml
<bean id="tomcat7" class="com.gigaspaces.tomcat.Tomcat7">
  <property name="appBase" value="/webapps" />
  <property name="contextPath" value="/" />
  <property name="port" value="8888" />
  <property name="portRetries" value="10" />
  <property name="webapps">
   <array>
    <bean class="com.gigaspaces.tomcat.Webapp">
     <property name="name" value="examples" />
     <property name="path" value="/examples" />
    </bean>
  </array>
 </property>
</bean>
```



|Property|	Description|
|:--------|:-----------|
|appBase|	Location in the project of web applications|
|contextPath|Root context path for applications|
|port|	Starting port number when deploying multiple instances|
|portRetries|	How many ports to try before giving up|
|webapps |	The collection of web applications contained in the processing unit.|


Step 3:	Place your web applications in src/main/resources/webapps{{<wbr>}}
Step 4:	(optional) Specify the SLA parameters for fault isolation and redundancy through an SLA.XML file in META-INF/spring


|Setting|	SLA Directive|
|:--------|:-----------|
|Maximum of one Tomcat instance per JVM|`<os-sla:sla max-instances-per-vm="1" />`|
|Maximum of one Tomcat instance per physical machine|`<os-sla:sla max-instances-per-machine="1" />`|
|Maximum of one Tomcat instance per zone (sets of machines or availability zone)|`<os-sla:sla max-instances-per-zone="1" />`|


Step 5:	Build the project through mvn package{{<wbr>}}
Step 6:	Deploy processing unit in XAP, once deployed Tomcat should appear as the following


![tomcat1.png](/attachment_files/sbp/tomcat1.png)


Navigate to (default) port 8888 to test
