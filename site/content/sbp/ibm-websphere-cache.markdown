---
type: post
title:  XAP Integration WebSphere Dynamic Cache
categories: SBP
parent: data-access-patterns.html
weight: 60
---

{{%ssummary%}}{{%/ssummary%}}

{{% tip %}}
**Summary:** This article illustrates how to integrate IBM's DynaCache with GigaSpaces XAP <br/>
**Author**: Allen Terleto<br/>
**Recently tested with GigaSpaces version**: XAP 9.7<br/>
**Last Update:** September 2014<br/>

{{% /tip %}}



# Introduction
IBM provides an embedded cache called Dynamic Cache (DynaCache) as a feature of their WebSphere Application Server. By utilizing a caching-strategy applications can bypass the latency-costs of processing web services, business logic, data access, network & IO overhead, etc. DynaCache is the underlying caching strategy for many of the WebSphere-based family of products such as:

- 	WebSphere Commerce Server <br>
- 	WebSphere Portal Server  <br>
- 	WebSphere Enterprise Service Bus <br>
- 	Business Process Manager    <br>
- 	WebSphere Application Server

WebSphere allows administrators to configure the Dynamic Cache Service to use GigaSpace's XAP IMDG as its alternative Cache Provider instead of the default DynaCache. GigaSpaces XAP enhances WebSphere's caching strategy by providing:

- 	Elastic Scalability <br>
- 	High availability <br>
- 	Transactional Support <br>
- 	Event Processing <br>
- 	Persistency <br>
- 	Filtering   <br>
- 	Big Data Integration  <br>
- 	Security       <br>
- 	Enhanced Monitoring Capabilities <br>
- 	Distributed and Centralized System of Record  <br>
- 	HTTP Session Sharing (across various Web Containers) <br>
- 	Replication across multiple Data Centers<br>
- 	Other XAP Features¦

GigaSpace's XAP features increase the capabilities of WebSpheres DynaCache beyond the limitations of the default dynamic cache engine and data replication service. While DynaCache can only provide caching across replicated and synchronized WebSphere application servers, GigaSpaces XAP provides a truly distributed and remote caching architecture. WebSphere administrators no longer need to worry about data loss due to cluster failures, redeployments and upgrades. Additionally, scaling the Data Cache Tier will no longer be a function of adding additional WebSphere Application Server instances.

If you are currently using DynaCache, you can simply use the administrative console or wsadmin commands to replace WebSpheres default cache provider with GigaSpaces XAP. You do not have to make any changes to the code interacting with the default dynamic cache or caching data model. In the Demo: Step-By-Step Walkthrough section we will show how to switch to GigaSpace's XAP IMDG in just a few configuration changes.

{{%refer%}}
Please read the GigaSpaces XAP Integration with IBM Dynamic Cache [{{%pdf "/sbp/download_files/DynamicCache.pdf"%}} document for a detailed walk through and additional introduction information.
{{%/refer%}}





# Demo Requirements

The following technology is required to run this demo

An Application Server from the WebSphere Family <br>
- 	WebSphere Application Server 7+    <br>
- 	WebSphere Enterprise Service Bus  <br>
- 	IBM Integration Bus  <br>
- 	IBM WebSphere Process Server

#### An IBM IDE<br>
- 	Rational Application Developer<br>
- 	IBM Integration Designer

#### Gig
