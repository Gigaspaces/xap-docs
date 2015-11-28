---
type: post102
title:  Spring Integration
categories:  XAP102TUT
parent: none
weight: 10000
---



All XAP components can be wired and configured with the application using corresponding [Spring Beans](http://spring.io/).



# The XAP Spring Integration supports:
Spring Automatic Transaction Demarcation{{<wbr>}}
Spring Data{{<wbr>}}
Spring JMS{{<wbr>}}
Spring JPA{{<wbr>}}
Spring Hibernate{{<wbr>}}
Spring Remoting{{<wbr>}}
String Batch{{<wbr>}}
Spring Security{{<wbr>}}
Mule



# Example

Lets look at a Spring configuration file that represents the creation of an embedded space:



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla"
	xsi:schemaLocation="
   http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
   http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd">

	<!-- Scan the packages for annotations -->
	<context:component-scan base-package="xap.tutorial"/>

	<!-- Enables to configure Spring beans through annotations -->
	<context:annotation-config/>

	<!-- A bean representing a space (an IJSpace implementation) -->
	<os-core:embedded-space id="space" name="tutorialSpace"/>

	<!-- Define the GigaSpace instance that the application will use to access the space -->
	<os-core:giga-space id="xapTutorialSpace" space="space"/>
</beans>
```


And here is the code to access the Spring Bean within your application:

```java
public void findSpace()  {
    FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
	"classpath:/spring/application-context.xml");

    GigaSpace space = (GigaSpace) context.getBean("xapTutorialSpace");
}
```

# XAP Spring Data

`XAP Spring Data` implements the Spring Data Framework that lets you use the concepts to the development of applications using XAP's In-Memory Data Grid (IMDG) as the data store

{{%refer%}}
You can find more information under [Services & Best Practises](/sbp/spring-data.html)
{{%/refer%}}
