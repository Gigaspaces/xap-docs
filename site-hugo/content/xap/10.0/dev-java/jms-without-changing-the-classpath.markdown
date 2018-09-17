---
type: post100
title:  Using GigaSpaces JMS Without Changing the Classpath
categories: XAP100
parent: jms-advanced.html
weight: 600
---



A JMS application may use the XAP RMI registry to a acquire its JMS resources. In this case the application administrator may prefer not to include the GigaSpaces JAR files in the application's classpath. The GigaSpaces RMI registry specifies a Code Base where the application receives GigaSpaces's class information automatically.

In order for an application to use XAP JMS without having to include XAP JAR files in the classpath the application administrator has to make sure that:

1. The security policy contains:


```java
grant {
    permission java.security.AllPermission "", "";
};
```

For example, the security policy may be set to the policy file of GigaSpaces:


```java
-Djava.security.policy=<JSHOMEDIR>\policy\policy.all
```

1. The security manager is set to be the RMISecurityManager:


```java
-Djava.security.manager=java.rmi.RMISecurityManager
```
