---
type: post121
title:  Overview
categories: XAP121,ENT
parent: global-http-session-sharing-overview.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}



It's becoming increasingly important for organizations to share HTTP session data across multiple data centers, multiple web server instances or different types of web servers. Here are few scenarios where HTTP session sharing is required:

- **Multiple different Web servers running your web application** - You may be porting your application from one web server to another and there will be a period of time when both types of servers need to be active in production.
- **Web Application is broken into multiple modules** - When applications are modularized such that different functionalities are deployed across multiple server instances. For example, you may have login, basic info, check-in and shopping functionalities split into separate modules and deployed individually across different servers for manageability or scalability. In order for the user to be presented with a single, seamless, and transparent application, session data needs to be shared between all the servers.
- **Reduce Web application memory footprint** - The web application storing all session within the web application process heap, consuming large amount of memory. Having the session stored within a remote process will reduce web application utilization avoiding garbage collocation and long pauses.
- **Multiple Data-Center deployment** - You may need to deploy your application across multiple data centers for high-availability, scalability or flexibility, so session data will need to be replicated.



XAPs Global HTTP Session Management is designed to deliver maximum performance for the application with ZERO application code changes.

{{%vbar "Global HTTP Session Management features:"%}}
- **Reduce App/Web server memory footprint** storing the session within a remote JVM.

- **No code changes required** to share the session with other remote Web/App servers - Support **Serialized and Non-Serialized** Session attributes. Your attributes do not need to implement Serializable or Externalizable interface.

- **Transparent Session sharing** between any App/Web server - Any JEE app/web server (WebSphere , Weblogic , JBoss , Tomcat , Jetty , GlassFish...) may share their HTTP session with each other.

- **Application elasticity** - Support **session replication** across different App/Web applications located within the same or different data-centers/clouds allowing the application to scale dynamically without any downtime.

- **Unlimited number of sessions and concurrent users** support - Sub-millisecond session data access by using XAP In-Memory-Data-Grid.

- **Session replication over the WAN** support - Utilizing XAP Multi-Site Replication over the WAN technology.

- HTTP Session **data access scalability** - Session data can utilize any of the supported In-Memory-Data-Grid topologies ; replicated , partitioned , with and without local cache.

- **Transparent App/Web Failover** - Allow app server restart without any session data loss.

- Any session data type attribute support - **Primitive and Non-Primitive** (collections, user defined types) attributes supported.

- **Sticky session and Non-sticky** session support - Your requests can move across multiple instances of web application seamlessly.

- **Atomic HTTP request** session access support - multiple requests for the session attributes within the same HTTP request will be served without performing any remote calls. Master session copy will be updated when the HTTP request will be completed.

- **Delta update support** - Changes identified at the session attribute level. Minimal serialization overhead and network utilization between client and IMDG , between IMDG primary and backup instances and between remote IMDG clusters using WAN Gateway for WAN replication.

- **Compression support** - Session attributes may be compressed to support very large session storage with minimal performance impact.

- **Pluggable Serialization support** - You may use built-in serialization (Java Native, Kryo or Xstream) or implement your own serialization logic.


- **Principle based session sharing** - Allows session sharing across different applications using SSO.


{{%/vbar%}}

# Application Session Sharing

Configure your web application to use the XAP session manager, deploy the XAP in-memory data grid (IMDG) and deploy your web application. That's it!

There is no need to change the web application or plug in any custom code in order to enable session sharing between servers running in remote data centers. In addition, you don't have to add the HTTP session classes to the IMDG classpath.

## Single-Application Session Sharing

When having a single application deployed across multiple web containers that require their HTTP session to be shared the session is shared via its ID.

{{% align center%}}
![SingleApplicationSessionSharing.jpg](/attachment_files/SingleApplicationSessionSharing.jpg)
{{% /align%}}

## Multiple-Applications Session Sharing
When having multiple different web applications deployed across multiple web containers that are considered as one logical application require their HTTP session to be shared, the session is shared via the user login principle (common across all different applications). In this case a Timestamp check is performed to ensure the most recent session is passed between the different applications. In this case the Session attributes are merged when required.

{{% align center%}}
![MultiApplicationSessionSharing.jpg](/attachment_files/MultiApplicationSessionSharing.jpg)
{{% /align%}}

## The GigaSpaces Session Filter

The web application configuration should include the GigaSpaces Session Filter. This filter interacts with the GigaSpaces session manager that store the session content within the data grid. It maintains a temporary cache to reduce remote activity with the data grid.

{{% align center%}}
![HTTPSessionManagementcloserLook.jpg](/attachment_files/HTTPSessionManagementcloserLook.jpg)
{{% /align%}}



# Session Replication across different JEE Containers

The HTTP session stored within XAP in an agnostic neutral structure. This allows XAP HTTP Session Manager to share the session between different JEE Containers. For example between IBM Websphere version 7 and IBM Websphere version 8 , or between Tomcat 6 and Tomcat 7. In the same way the same HTTP session may be shared between IBM Websphere and Jboss/Tomcat/Weblogic/etc.

{{% align center%}}
![MultiJEEContainerSupport.jpg](/attachment_files/MultiJEEContainerSupport.jpg)
{{% /align%}}

# Session Replication across different Data-Centers

The following diagram depicts a common use case where there are multiple data centers connected across the WAN, and each is running a different type of web server.

{{% align center%}}
![httpSessionSharing1.jpg](/attachment_files/httpSessionSharing1.jpg)
{{% /align%}}

The XAP Global HTTP Session Sharing architecture allows users to deploy their web application across these multiple data centers where the session is shared in real-time and in a transparent manner. The HTTP session is also backed by a data grid cluster within each data center for fault tolerance and high-availability.

With this solution, there is no need to deploy a database to store the session, so you avoid the use of expensive database replication across multiple sites. Setting up XAP for session sharing between each site is simple and does not involve any code changes to the application.

The below diagram shows a more detailed view of the IMDG deployment. In this case, there are multiple partitions for high scalability, as well as backup instances for redundancy. The WAN Gateway is also deployed and shows replication between remote site.

{{% align center%}}
![httpSessionSharing2.jpg](/attachment_files/httpSessionSharing2.jpg)
{{% /align%}}

The end-to-end path between the two data center nodes includes the servlet and GigaSpaces filters, and the IMDG with local cache and WAN Gateway.

{{% align center%}}
![httpSessionSharing3.jpg](/attachment_files/httpSessionSharing3.jpg)
{{% /align%}}

