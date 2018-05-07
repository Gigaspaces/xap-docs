---
type: post123
title: Migrating from Java EE to XAP
categories: XAP123OVW
parent: none
weight: 1000
---


From a design and implementation perspective, XAP shares a lot of patterns with Java Enterprise Edition ecosystem. This section provides a mapping to help migrate your existing JavaEE application to GigaSpaces XAP. 

# Java EE versus XAP

|Component|JEE|XAP|
|:--------|:---|:--|
|Persistency|JPA|[Hibernate](../dev-java/hibernate-space-persistency.html), [JPA](../dev-java/jpa-api-overview.html)|
|Messaging|JMS,MDB|[JMS](../dev-java/messaging-support.html)<br>Polling Container [Java version](../dev-java/polling-container-overview.html) \|[ .NET version](../dev-dotnet/polling-container-overview.html)<br>Notify Container [Java version](../dev-java/notify-container-overview.html) \|[ .NET version](../dev-dotnet/notify-container-overview.html)<br> [Native Messaging API](../dev-java/session-based-messaging-api.html), MDB {{% star %}} |
|Security|JAAS, SSL|[Spring Security](../security/spring-security-bridge.html), [SSL](../security/securing-the-transport-layer-using-ssl.html)|
|Web Session Management|HttpSession|[HTTP Session Management](../dev-java/http-session-management.html)|
|Transaction Management|JTA|[Spring Transaction via Jini Transaction Manager](../dev-java/transaction-overview.html)|
|Data Access|JDBC , Session Bean (Stateless or Stateful), Entity Bean|[JDBC](../dev-java/jdbc-driver.html), [Space](../dev-java/the-gigaspace-interface-overview.html), [JPA](../dev-java/jpa-api-overview.html) , Session Bean (Stateless or Stateful) {{% star %}}, Entity Bean {{% star %}}|
|Remoting|EJB, IIOP, RMI|[Spring remoting over LRMI](../dev-java/executor-based-remoting.html), EJB {{% star %}}|
|Web|Servlet, JSP | [Servlet, JSP via Jetty](../dev-java/web-application-overview.html)|
|Packaging and deployment|EAR, WAR|[JAR](../dev-java/the-processing-unit-structure-and-configuration.html), [WAR](../dev-java/web-application-overview.html), EAR {{%  star %}}|
|Contexts and Dependency Injection|JSR 299|Spring IOC|
|System Management|JMX|[JMX](../dev-java/snmp-connectivity-via-alert-logging-gateway.html) , [SNMP](../dev-java/snmp-connectivity-via-alert-logging-gateway.html), [Native Admin API](../dev-java/administration-and-monitoring-overview.html)|
|Java Naming and Directory Service|JNDI|[Jini Lookup Service](./about-jini.html)|

{{%  star %}} Available via [EasyBeans](https://forge.ow2.org/projects/easybeans), [openejb](http://openejb.apache.org), [embedded jboss](http://docs.jboss.org/ejb3/embedded/embedded.html) or [embedded-glassfish](http://embedded-glassfish.java.net).

# Messaging Concepts & Patterns


|Functionality|JEE|XAP|
|:------------|:---|:--|
|Queue|JMS Queue|Polling Container [Java version](../dev-java/polling-container-overview.html) \|[ .NET version](../dev-dotnet/polling-container-overview.html)|
|Topic|JMS Topic|Notify Container  [Java version](../dev-java/notify-container-overview.html) \|[ .NET version](../dev-dotnet/notify-container-overview.html)|
|Unit of Order|JMS UOO|[XAP Unit Of Work](/sbp/unit-of-work.html)|
|Queue Partitioning|JMS Service Activator Aggregator Strategy|[XAP Parallel Queue](/sbp/parallel-queue-pattern.html)|
|Distributed Priority Queue|JMS Quality of Service|[XAP Priority Based Queue](/sbp/priority-based-queue.html)|
|Compute Grid|MDB + Custom code|[XAP Master-Worker](/sbp/master-worker-pattern.html)|
