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
|Persistency|JPA|[Hibernate]({{% latestjavaurl%}}/hibernate-space-persistency.html), [JPA]({{% latestjavaurl%}}/jpa-api.html)|
|Messaging|JMS,MDB|[JMS]({{%latestjavaurl%}}/messaging-support.html){{% wbr%}}Polling Container {{%latestjavanet%}} {{% wbr%}}Notify Container {{% latestjavanet %}} {{% wbr%}} [Native Messaging API]({{% latestjavaurl%}}/session-based-messaging-api.html), MDB {{%  star %}} |
|Security|JAAS, SSL|[Spring Security]({{%latestsecurl%}}/spring-security-bridge.html), [SSL]({{% latestsecurl%}}/securing-the-transport-layer-using-ssl.html)|
|Web Session Management|HttpSession|[Global HttpSession sharing]({{% latestjavaurl%}}/global-http-session-sharing-overview.html)|
|Transaction Management|JTA|[Spring Transaction via Jini Transaction Manager]({{% latestjavaurl%}}/transaction-management.html)|
|Data Access|JDBC , Session Bean (Stateless or Stateful), Entity Bean|[JDBC]({{% latestjavaurl%}}/jdbc-driver.html), [Space]({{% latestjavaurl%}}/the-gigaspace-interface.html), [JPA]({{% latestjavaurl%}}/jpa-api.html) , Session Bean(Stateless or Stateful) {{%  star %}}, Entity Bean {{%  star %}}|
|Remoting|EJB, IIOP, RMI|[Spring remoting over LRMI]({{% latestjavaurl%}}/executor-based-remoting.html) , EJB {{%  star %}}|
|Web|Servlet, JSP | [Servlet, JSP via Jetty]({{% latestjavaurl%}}/web-application-support.html)|
|Packaging and deployment|EAR , war|[jar]({{% latestjavaurl%}}/the-processing-unit-structure-and-configuration.html), [war]({{% latestjavaurl%}}/web-application-support.html) , EAR {{%  star %}}|
|Contexts and Dependency Injection|JSR 299|Spring IOC|
|System Management|JMX|[JMX]({{% latestjavaurl%}}/snmp-connectivity-via-alert-logging-gateway.html) , [SNMP]({{% latestjavaurl%}}/snmp-connectivity-via-alert-logging-gateway.html), [Native Admin API]({{% latestjavaurl%}}/administration-and-monitoring-api.html)|
|Java Naming and Directory Service|JNDI|[Jini Lookup Service](./about-jini.html)|

{{%  star %}} Available via [EasyBeans](http://www.easybeans.net/xwiki/bin/view/Main/WebHome), [openejb](http://openejb.apache.org), [embedded jboss](http://docs.jboss.org/ejb3/embedded/embedded.html) or [embedded-glassfish](http://embedded-glassfish.java.net).

# Messaging Concepts & Patterns


|Functionality|JEE|XAP|
|:------------|:---|:--|
|Queue|JMS Queue|Polling Container {{%latestjavanet "polling-container.html"%}}|
|Topic|JMS Topic|Notify Container  {{%latestjavanet "notify-container.html"%}}|
|Unit of Order|JMS UOO|[XAP Unit Of Work](/sbp/unit-of-work.html)|
|Queue Partitioning|JMS Service Activator Aggregator Strategy|[XAP Parallel Queue](/sbp/parallel-queue-pattern.html)|
|Distributed Priority Queue|JMS Quality of Service|[XAP Priority Based Queue](/sbp/priority-based-queue.html)|
|Compute Grid|MDB + Custom code|[XAP Master-Worker](/sbp/master-worker-pattern.html)|
