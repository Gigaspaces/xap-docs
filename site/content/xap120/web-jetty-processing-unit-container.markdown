---
type: post120
title:  Jetty Processing Unit Container
categories: XAP120
parent: web-application-overview.html
weight: 200
---


{{% ssummary  %}}{{%/ssummary%}}



A XAP web processing unit can use [Jetty](http://www.eclipse.org/jetty/) as the web container that will actually run the WAR file deployed into the Service Grid. Jetty itself comes built in with the GigaSpaces installation. The integration itself allows you to run both a pure WAR file (pure in a sense that it does not use a Space), as well as simplifying the introduction of Space (both embedded and remote) in a non Spring and Spring environment.

{{% align center%}}
![web_app_archi.jpg](/attachment_files/web_app_archi.jpg)
{{% /align %}}

**Dependencies**<br>
In order to use this feature, include the `${XAP_HOME}/lib/optional/jetty/xap-jetty/xap-jetty-8.jar` file on your classpath or use maven dependencies:

```xml
<dependency>
    <groupId>org.gigaspaces</groupId>
    <artifactId>xap-jetty-8</artifactId>
    <version>{{%version maven-version%}}</version>
</dependency>
```

or `${XAP_HOME}/lib/optional/jetty-9/xap-jetty/xap-jetty-9.jar`
```xml
<dependency>
    <groupId>org.gigaspaces</groupId>
    <artifactId>xap-jetty-9</artifactId>
    <version>{{%version xap-release%}}</version>
</dependency>
```

{{%refer%}}
For more information on dependencies see [Maven Artifacts](./maven-artifacts.html)
{{%/refer%}} 


{{%anchor jetty-version%}}

# Jetty version

{{%panel%}}

XAP 12.0 ships with Jetty 8.1.8.v20121106. However, it is possible to use Jetty 9.3.7.v20160115.

To install Jetty 9.3.7.v20160115 follow these steps:

1. Rename `<XAPHOME>/lib/optional/jetty` to `<XAPHOME>/lib/optional/jetty-8`
2. Rename `<XAPHOME>/lib/optional/jetty-9` to `<XAPHOME>/lib/optional/jetty`
3. Copy the Jetty 9.3.7.v20160115 jars to `<XAPHOME>/lib/optional/jetty` directory from the following folders:
   * `<Jetty>/lib/*.jar`
   * `<Jetty>/lib/annotations/*.jar`
   * `<Jetty>/lib/websocket/*.jar`
   * `<Jetty>/lib/apache-jsp/*.jar`
4. Also, Download cdi-api-2.0-EDR1.jar and place it at under the Jetty directory as well. The file could be downloaded from here: [http://central.maven.org/maven2/javax/enterprise/cdi-api/2.0-EDR1/cdi-api-2.0-EDR1.jar](http://central.maven.org/maven2/javax/enterprise/cdi-api/2.0-EDR1/cdi-api-2.0-EDR1.jar)
5. With version 9 the connector configuration has [changed](#jetty9).

{{%/panel%}}


# Jetty Instance Handling

Jetty itself is configured using Spring, and allows you to control all aspects of both the Jetty instance created, and the web application context. There are two flavors of how Jetty instances are created (by default). The first is the **plain** mode, where a Jetty instance is created for each web processing unit instance running within a GSC. The second is the **shared** mode, where a single Jetty instance is created, and shared between all the different web processing unit instances running on the same GSC. A custom Jetty instantiation and handling can also be configured.

By default, the instantiation mode is **plain**. In order to control (at deploy time) which instantiation mode is used, the deploy property **jetty.instance** can be passed with either the **plain** value (the default) or the **shared** value.

In order to configure a custom Jetty configuration, a **jetty.pu.xml** should be added to **META-INF/spring** within the WAR file. Both the **plain** and **shared** mode actually correspond to a built in "jetty.pu.xml" file that exists within the XAP JAR file (explained below).

# Plain Instantiation Mode

The plain instantiation mode (which is the simplest and the default one), creates a Jetty instance for each web processing unit instance (web application). Its actual configuration can be found within the XAP JAR file under **org/openspaces/pu/container/jee/jetty/jetty.plain.pu.xml**.

This mode is simple, mainly because the context path that is created for each web application instance is exactly the same. Only the connector (port) that it runs on, is different. Note that when working in a virtualized environment, where more than one instance of the same web application can run on the same VM, this requires some thought. Naturally, the Service Grid allows you to control whether only one instance of the web application runs on a VM, using the **max-instances-per-vm* option, or even using the *max-instances-per-machine** option. What fits best, depends on the deployment and service requirements of the application.

Even though a new Jetty instance is created for each web application instance that runs within a GSC (JVM), some resources are still shared between the different Jetty instances, making this instantiation model more lightweight than it first seemed. For example, the thread pool Jetty uses to service requests, is shared between all the different Jetty instances.

There are many features that are exposed and can control how the plain instantiation model can be used. Following is a simple explanation of all the different parts within the **jetty.plain.pu.xml**, and their nature.

## Configuration Properties

The first part of the **jetty.plain.pu.xml** is the different deploy time properties that can be used to control it, with their respective default values.


```xml
<bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="properties">
        <props>
            <prop key="web.context">/${clusterInfo.name}</prop>
            <prop key="web.port">8080</prop>
            <prop key="web.sslPort">8443</prop>
            <prop key="web.context.classLoader.parentFirst">false</prop>
            <prop key="web.context.copyWebDir">false</prop>
            <prop key="web.threadPool.minThreads">10</prop>
            <prop key="web.threadPool.maxThreads">200</prop>
            <prop key="web.threadPool.lowThreads">20</prop>
            <prop key="web.selector.maxIdleTime">300000</prop>
            <prop key="web.selector.acceptors">2</prop>
            <prop key="web.selector.lowResourcesConnections">20000</prop>
            <prop key="web.selector.lowResourcesMaxIdleTime">5000</prop>
            <prop key="web.selector.forwarded">true</prop>
        </props>
    </property>
</bean>
```

All the above properties can be controlled during deployment (or by adding a **META-INF/spring/pu.properties** file). What they actually control (though very evident from the name) is explained in the following sections.

{{% tip %}}
Controlling the size of the data a client can push to the server can be done using the `org.mortbay.jetty.Request.maxFormContentSize` property.
{{% /tip %}}

## Port Numbers


```xml
<bean id="port" class="org.openspaces.pu.container.jee.PortGenerator">
    <property name="basePort" value="${web.port}" />
    <property name="portOffset" value="${clusterInfo.runningNumber}" />
</bean>

<bean id="confidentialPort" class="org.openspaces.pu.container.jee.PortGenerator">
    <property name="basePort" value="${web.sslPort}" />
    <property name="portOffset" value="${clusterInfo.runningNumber}" />
</bean>
```

The above xml fragment from the **jetty.plain.xml** controls the port numbers that are used by the Jetty instance started. The `PortGenerator` is a utility class that does not do more than expose itself as the sum of the `basePort` property and the `portOffset` property. In our case, each instance of a web application that is deployed in plain mode, will have a unique port (that, by default, starts from 8080). For example, if a web application is deployed with 2 instances, the first instance will start on port 8080, the second instance will start on port 8081 (regardless of the host).

{{% note %}}
In this case, if another web application is deployed on the same GSC, the `web.port` property should be changed (for example, to start from 9090), so there won't be any port clashes between the two web applications. By default, if a port is taken on the same host, the subsequent port will be used with up to 20 retries. To limit the number of retries, for example to 10 (instead of the default 20), you must define a bean named **retryPortCount** of class `Integer` and the value as the number of attempts. Setting a value of 1, will only try once using the `basePort` and `portOffset`.
{{%/note%}}


```xml
<bean id="retryPortCount" class="java.lang.Integer">
    <constructor-arg value="10" />
</bean>
```

## Jetty Instance


```xml
<bean id="jettyHolder"
	class="org.openspaces.pu.container.jee.jetty.holder.SharedJettyHolder">
	<constructor-arg ref="jetty" />
</bean>

<bean id="jetty" class="org.eclipse.jetty.server.Server">
	<property name="threadPool">
		<bean class="org.eclipse.jetty.util.thread.QueuedThreadPool">
			<property name="minThreads" value="${web.threadPool.minThreads}" />
			<property name="maxThreads" value="${web.threadPool.maxThreads}" />
		</bean>

	</property>
	<property name="connectors">
		<list>
			<bean class="org.eclipse.jetty.server.nio.SelectChannelConnector">
				<property name="port" ref="port" />
				<property name="maxIdleTime" value="${web.selector.maxIdleTime}" />
				<property name="acceptors" value="${web.selector.acceptors}" />
				<property name="statsOn" value="${web.statsOn}" />
				<property name="confidentialPort" ref="confidentialPort" />
				<property name="lowResourcesConnections" value="${web.selector.lowResourcesConnections}" />
				<property name="lowResourcesMaxIdleTime" value="${web.selector.lowResourcesMaxIdleTime}" />
				<property name="forwarded" value="${web.selector.forwarded}" />
			</bean>
		</list>
	</property>
	<property name="handler">
		<bean class="org.eclipse.jetty.server.handler.HandlerCollection">
			<property name="handlers">
				<list>
					<bean class="org.eclipse.jetty.server.handler.ContextHandlerCollection" />
					<bean class="org.eclipse.jetty.server.handler.DefaultHandler" />
				</list>
			</property>
		</bean>
	</property>
</bean>
</beans>
```

The above shows how the Jetty instance is configured. The **Jetty** bean is actually the Jetty server configured. Most of the parameters can be controlled using deploy time properties.

An important aspect here is the `SharedThreadPool`, which wraps the actual Jetty thread pool used. The `SharedThreadPool` will cause the Jetty thread pool to be shared among all of the Jetty instances created on that specific GSC (JVM). Note, in this case, that the first web application that will be deployed to the GSC will control the thread pool size. Other web applications will not be able to modify the size of the thread pool.

The bean that is actually used (and expected to be defined) within the configuration is the **JettyHolder** (it must be named **JettyHolder**). In our case, the `JettyHolder` used is the `PlainJettyHolder` which creates a new instance of Jetty for each instance of the web application.

## Web Context


```xml
<<bean id="webAppContext" class="org.eclipse.jetty.webapp.WebAppContext">
 <property name="contextPath" ref="context" />
 <property name="war" value="${jee.deployPath}" />
 <property name="tempDirectory" value="${jee.deployPath}/WEB-INF/work" />
 <property name="copyWebDir" value="${web.context.copyWebDir}" />
 <property name="parentLoaderPriority" value="${web.context.classLoader.parentFirst}" />
 <property name="configurationClasses">
 	<list>
 		<value>org.eclipse.jetty.webapp.WebInfConfiguration</value>
 		<value>org.eclipse.jetty.webapp.WebXmlConfiguration</value>
 		<value>org.eclipse.jetty.webapp.MetaInfConfiguration</value>
 		<value>org.eclipse.jetty.webapp.FragmentConfiguration</value>
 		<value>org.eclipse.jetty.plus.webapp.EnvConfiguration</value>
 		<value>org.eclipse.jetty.plus.webapp.PlusConfiguration</value>
 		<value>org.eclipse.jetty.webapp.JettyWebXmlConfiguration</value>
 		<value>org.eclipse.jetty.webapp.TagLibConfiguration</value>
 	</list>
 </property>
 </bean>
```

This bean controls the actual web context that corresponds to the web application instance being deployed. Its context path is the property `web.context`, which defaults to `clusterInfo.name`. (The `clusterInfo.name` is the name of the processing unit, defaults to the war file name, but can be overridden using the override-name feature).

{{% note %}}
In the plain mode, the context path can be the same for all different instances of the web application, even if they are running on the same GSC (JVM).
{{%/note%}}

## Jetty Maven Plugin

If you are using Maven to create, compile, package and run unit tests, execute and deploy a Processing Unit which is a web application, make sure that inside your WAR file there are no Jetty jars. In your project pom.xml you should **exclude** jetty-all inside com.gigaspaces dependency.

For example:


```xml
<dependency>
	<groupId>org.gigaspaces</groupId>
	<artifactId>xap-openspaces</artifactId>
	<version>{{%version "maven-version" %}}</version>
	<exclusions>
		<exclusion>
			<groupId>org.eclipse.jetty.aggregate</groupId>
			<artifactId>jetty-all</artifactId>
		</exclusion>
	<exclusions>
</dependency>
```



## Examples

Lets take some deployment scenarios examples and see how the plain mode works. If we have a web application packaged as WAR, by the name petclinic. We have started four GSCs, two on each machine. We then deploy 4 instances of the petclinic application.

The end result of the deployment will be 4 instances, one instance per GSC. The first instance will be on GSC, using port 8080, and a web context path of `petclinic`. The second instance will run on another GSC with port 8081, and a web context path of `petclinic`. The third instance will run on another GSC, with port 8082 and a web context path of `petclinic`. The last instance will run on the last GSC, with port 8083 and a web context path of `petclinic`.

Now, lets assume that the first machine fails. This means that the first two web applications (8080 and 8081) will not be running. The end result of this failure will be that the two web application instances running on the machine that failed (each on its own GSC), will be deployed on the existing machine (assuming no SLA are defined). One of the GSCs on the machine that is still up, will run on 8080, and another will run on 8081. As you can see, the port number correlates to the instance number of the web application, and not to a specific location.

# Shared Instantiation Mode

The shared instantiation mode creates a single Jetty instance per GSC (JVM). Its actual configuration can be found within the XAP JAR file, under **org/openspaces/pu/container/jee/jetty/jetty.shared.pu.xml**. The benefits of this mode are obvious as only one instance of jetty is created per JVM (though note that the plain mode does share some resources between different Jetty instances).

The main difficulties when working with this mode come from the fact that it is possible that more than one web application instance will be running on the same GSC (JVM). In order to solve this problem, by default, when working in shared mode, the web context path is the actual web context, appended by a running number. For example, when deploying two instances of a petclinic web application, the first instance will be deployed under `petclinic_1` web app context, while the second will be deployed under `petclinic_2`.

The ServiceGrid itself allows you to configure that only a single instance of a web application will be deployed on a GSC, using the **max-instances-per-vm** parameter. In this case, the default behavior of appending a running number to the context is not needed, and it can be disabled by changing the deploy time property `web.context.unique` to `true`.

There are many features that are exposed and can control how the shared instantiation model can be used. Following is an explanation of all the different parts within the **jetty.shared.pu.xml** and their nature.

## Configuration Properties

The first part of the **jetty.shared.pu.xml** is the different deploy time properties that can be used to control it, with their respective default values.


```xml
<bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="properties">
        <props>
            <prop key="web.context">/${clusterInfo.name}</prop>
            <prop key="web.context.unique">true</prop>
            <prop key="web.context.separator">_</prop>
            <prop key="web.context.copyWebDir">false</prop>
            <prop key="web.context.classLoader.parentFirst">false</prop>
            <prop key="web.port">8080</prop>
            <prop key="web.sslPort">8443</prop>
            <prop key="web.threadPool.minThreads">10</prop>
            <prop key="web.threadPool.maxThreads">200</prop>
            <prop key="web.threadPool.lowThreads">20</prop>
            <prop key="web.selector.maxIdleTime">300000</prop>
            <prop key="web.selector.acceptors">2</prop>
            <prop key="web.selector.lowResourcesConnections">20000</prop>
            <prop key="web.selector.lowResourcesMaxIdleTime">5000</prop>
            <prop key="web.selector.forwarded">true</prop>
        </props>
    </property>
</bean>
```

All the above properties can be controlled during deployment (or by adding a **META-INF/spring/pu.properties** file). What they actually control (though very evident from the name) will be explained in the following sections.

## Port Numbers


```xml
<bean id="port" class="org.openspaces.pu.container.jee.PortGenerator">
    <property name="basePort" value="${web.port}" />
</bean>

<bean id="confidentialPort" class="org.openspaces.pu.container.jee.PortGenerator">
    <property name="basePort" value="${web.sslPort}" />
</bean>
```

Since the shared mode actually starts a single Jetty instance, the port numbers are not based on the instance of the web application, but simply on the `web.port` deployment property (which defaults to 8080). The first web application that is deployed in a GSC will control on which port number the shared Jetty instance is created. Other web applications deployed on the same GSC will have no affect on the GSC (actually, on any aspect of the Jetty server).

In case more than one GSC is running on the same machine, and a web application is deployed on both, the first will use port 8080. The second web application instance, deployed on the second GSC, will try to use port 8080, identify that it is used, and automatically try the next one, which is 8081. By default, if a port is taken on the same host, the subsequent port will be used with up to 20 retries. To limit the number of retries, for example to 10 (instead of the default 20), you must define a bean named **retryPortCount** of class `Integer` and the value as the number of attempts. Setting a value of 1, will only try once using the `basePort` and `portOffset`.


```xml
<bean id="retryPortCount" class="java.lang.Integer">
    <constructor-arg value="10" />
</bean>
```

## Jetty Instance


```xml
<bean id="jettyHolder" class="org.openspaces.pu.container.jee.jetty.SharedJettyHolder">
    <constructor-arg ref="jetty" />
</bean>

<bean id="jetty" class="org.mortbay.jetty.Server">

    <property name="threadPool">
        <bean class="org.mortbay.thread.QueuedThreadPool">
            <property name="minThreads" value="${web.threadPool.minThreads}"/>
            <property name="maxThreads" value="${web.threadPool.maxThreads}"/>
            <property name="lowThreads" value="${web.threadPool.lowThreads}"/>
        </bean>
    </property>

    <property name="connectors">
        <list>
            <bean class="org.mortbay.jetty.nio.SelectChannelConnector">
                <property name="port" ref="port"/>
                <property name="maxIdleTime" value="${web.selector.maxIdleTime}"/>
                <property name="acceptors" value="${web.selector.acceptors}"/>
                <property name="statsOn" value="false"/>
                <property name="confidentialPort" ref="confidentialPort"/>
                <property name="lowResourcesConnections" value="${web.selector.lowResourcesConnections}"/>
                <property name="lowResourcesMaxIdleTime" value="${web.selector.lowResourcesMaxIdleTime}"/>
                <property name="forwarded" value="${web.selector.forwarded}" />
            </bean>
        </list>
    </property>
    <property name="handler">
        <bean class="org.mortbay.jetty.handler.HandlerCollection">
            <property name="handlers">
                <list>
                    <bean class="org.mortbay.jetty.handler.ContextHandlerCollection"/>
                    <bean class="org.mortbay.jetty.handler.DefaultHandler"/>
                </list>
            </property>
        </bean>
    </property>
</bean>
```

The above shows how the Jetty instance is configured. The **Jetty** bean is actually the Jetty server configured. Most of the parameters can be controlled using deploy time properties.

The bean that is actually used (and expected to be defined) within the configuration is the **JettyHolder** (it must be named **JettyHolder**). In our case, the `JettyHolder` used is the `SharedJettyHolder`, which creates a single instance of Jetty on the GSC (JVM) level.

{{% note %}}
this means that the first deployed web application in a shared mode will control how the Jetty instance will be created.
{{%/note%}}

## Web Context


```xml
<bean id="context" class="org.openspaces.pu.container.jee.SharedContextFactory">
    <property name="context" value="${web.context}" />
    <property name="unique" value="${web.context.unique}" />
    <property name="separator" value="${web.context.separator}" />
</bean>

<bean id="webAppContext" class="org.mortbay.jetty.webapp.WebAppContext">
    <property name="contextPath" ref="context" />
    <property name="war" value="${jee.deployPath}" />
    <property name="tempDirectory" value="${jee.deployPath}/WEB-INF/work" />
    <property name="copyWebDir" value="${web.context.copyWebDir}" />
    <property name="parentLoaderPriority" value="${web.context.classLoader.parentFirst}" />
    <property name="configurationClasses">
        <list>
            <value>org.eclipse.jetty.webapp.WebInfConfiguration</value>
            <!-- Enable JNDI configuration -->
            <value>org.eclipse.jetty.plus.webapp.EnvConfiguration</value>
            <value>org.eclipse.jetty.webapp.JettyWebXmlConfiguration</value>
            <value>org.eclipse.jetty.webapp.TagLibConfiguration</value>
        </list>
    </property>
</bean>
```

This `webAppContext` bean controls the actual web context that corresponds to the web application instance being deployed. Its context path is controlled by the `context` bean, represented by the `SharedContextFactory` class.

The `SharedContextFactory` controls the web application context path that the web application instance will be deployed under. If the `unique` flag is set to `true`, it will use the `context` path passed, append the `separator`, and then append the web application instance number (the cluster info running number). If the `unique` flag is set to `false`, it will simply use the `context` passed.

The `context` itself passed to the `SharedContextFactory`, is the property `web.context`, which defaults to `clusterInfo.name`. (The `clusterInfo.name` is the name of the processing unit, which defaults to the WAR file name, but which can be overridden using the override-name feature).

# Load Balancing

In this section, we will show how the plain mode can be configured with Apache and `mod_proxy` load balancer (more information here [http://docs.codehaus.org/display/JETTY/Configuring+mod_proxy](http://docs.codehaus.org/display/JETTY/Configuring+mod_proxy)). The plain deployment mode will be used in the example. First, after installing Apache 2.2, the `mod_proxy`, `mod_proxy_balancer`, and `mod_proxy_http` (at least) modules need to be enabled.

The second step (as per the link to jetty documentation) is to set the workerName property of the session id manager. The jetty integration automatically sets the workerName (if it is not set explicitly) to be the clusterInfo name and the clusterInfo running number. In case one wishes to override it, it can be done by configuring the `jetty-web.xml`. The following is how the `jetty-web.xml` workerName gets set automatically (if it was configured explicitly):


```xml
<Configure class="org.mortbay.jetty.webapp.WebAppContext">
  <Get name="sessionHandler">
    <Get name="sessionManager">
      <Call name="setIdManager">
        <Arg>
          <New class="org.mortbay.jetty.servlet.HashSessionIdManager">
            <Set name="WorkerName">${clusterInfo.name}${clusterInfo.runningNumberOffset1}</Set>
          </New>
        </Arg>
      </Call>
    </Get>
  </Get>
</Configure>
```

Then, the Apache httpd.conf should be modified to enable load balancing, here is an example:


```bash
ProxyPass /balancer !
ProxyPass /petclinic balancer://petclinic_cluster/ stickysession=JSESSIONID nofailover=On

<Proxy balancer://petclinic_cluster>
    BalancerMember http://machine1:8080/petclinic route=petclinic1
    BalancerMember http://machine1:8081/petclinic route=petclinic2
    BalancerMember http://machine2:8080/petclinic route=petclinic1
    BalancerMember http://machine2:8081/petclinic route=petclinic2
</Proxy>

# Proxy Management

<Location /balancer>
SetHandler balancer-manager

Order Deny,Allow
Deny from all
Allow from all
</Location>

ProxyStatus On
<Location /status>
SetHandler server-status

Order Deny,Allow
Deny from all
Allow from all
</Location>
```

The above configuration configures the load balancer to direct traffic to `/petclinic` on `machine1` and `machine2`. On both machines, the ports used are `8080` and `8081`. This is because of the dynamic nature of the Service Grid, where web application instance number 1 (which corresponds to port `8080`) might be deployed on `machine1` or `machine2`.

The above provides an overview of how to configure apache load balancer by hand. GigaSpaces comes with a built in [Apache Load Balancer Agent](./apache-load-balancer-agent.html) that provides a dynamic update of apache, based on changes occurring in the deployment of the web application.

# Securing Jetty Container

## Configuring SSL

Web Applications running inside the Jetty container can use SSL. Here is an example that defines SSLConnector,


```xml
<property name="connectors">
     <list>
         <bean class="org.eclipse.jetty.server.ssl.SslSelectChannelConnector">
             <property name="port" value="${ssl.port}"/>
             <property name="acceptors" value="${web.selector.acceptors}"/>
             <property name="maxIdleTime" value="${web.selector.maxIdleTime}"/>
             <property name="keystore" value="${ssl.keystore}"/>
             <property name="password" value="${ssl.password}"/>
             <property name="keyPassword" value="${ssl.keyPassword}"/>
             <property name="truststore" value="${ssl.truststore}"/>
             <property name="trustPassword" value="${ssl.trustPassword}"/>
         </bean>
    </list>
</property>
```

You can find a complete Jetty container definition [here](/download_files/jetty.pu.xml) and the associated properties file [here](/download_files/pu.properties)

Jetty documentation outlines all the steps involved in configuring SSL. Please refer to this [link](http://docs.codehaus.org/display/JETTY/How+to+configure+SSL) for more information.

{{% refer %}}
Find a complete example with a keystore and certificate file included [here](/download_files/jetty-ssl.zip).
{{% /refer %}}

## Authentication and Access Control using Security Realms

Security realms allow you to secure your web applications against unauthorized access. Protection is based on authentication that identifies who is requesting access to the webapp and access control that restricts what can be accessed and how it is accessed within the webapp.

Jetty supports following realms,

- [HashLoginService](http://wiki.eclipse.org/Jetty/Tutorial/Realms#HashLoginService)
- [JDBCLoginService](http://wiki.eclipse.org/Jetty/Tutorial/Realms#JDBCLoginService)
- [JAAS](http://wiki.eclipse.org/Jetty/Tutorial/JAAS)

Jetty documentation has more information about Security Realms and how to configure these. Please refer to these links [Security Realms](http://wiki.eclipse.org/Jetty/Feature/Realms), [Security Realms Tutorial](http://wiki.eclipse.org/Jetty/Tutorial/Realms) for more information.

## Custom Jetty Security Examples

### Hash Login Example

Attached [example](/download_files/webapp-hash-security.zip) shows a web processing unit configured to use `HashLoginService`. This uses a security realm file for storing userid and passwords which are used for authenticating users. Roles authorized for the app are defined in the web.xml file.

Jetty configuration should include SecurityHandler configuration (`HashLoginService`) which will be something like below,


```xml
<property name="securityHandler">
	<bean class="org.eclipse.jetty.security.ConstraintSecurityHandler">
		<property name="loginService">
			<bean class="org.eclipse.jetty.security.HashLoginService">
				<property name="name" value="MyRealm"/>
				<property name="config" value="${jee.deployPath}/WEB-INF/realm.properties"/>
				<property name="refreshInterval" value="60"/>
			</bean>
		</property>
	</bean>
</property>
```

Web.xml file should include security-constraint, login-config and roles.

### Custom JAAS Security Example

Attached [example](/download_files/webapp-jaas.zip) shows a web processing unit configured to use `JAASLoginService` and a custom Login Module. Authentication request is passed to the login module and behavior of this can be customized to application specific needs. In this simplistic example any request with a password of "password" goes thru successfully.

Jetty configuration should include SecurityHandler configuration (`JAASLoginService`) which will be something like below,


```xml
<property name="securityHandler">
	<bean class="org.eclipse.jetty.security.ConstraintSecurityHandler">
		<property name="loginService">
			<bean class="org.eclipse.jetty.plus.jaas.JAASLoginService">
				<property name="name" value="${aas.realm.name}" />
				<property name="loginModuleName" value="AASLogin" />
				<property name="callbackHandlerClass"
                                 value="org.eclipse.jetty.plus.jaas.callback.DefaultCallbackHandler" />
			</bean>
		</property>
	</bean>
</property>
```

Login module definition is a configuration file that maps the module name to a Java class implementation and will be something like below,


```bash
AASLogin {
          com.gigaspaces.web.jaas.MySecurityModule required
          debug="true" file="/realm.properties";
      };
```

Above configuration file should be passed as a JVM system property, `java.security.auth.login.config` for all the GSC's that host the web application instances (`-Djava.security.auth.login.config=\Dev\webapp-jaas\config\login.config`)

{{%anchor jetty9%}}

# Jetty 9

To install Jetty 9.1.3.v20140225 follow these steps:

1. Rename `<XAPHOME>/lib/optional/jetty` to `<XAPHOME>/lib/optional/jetty-8`
2. Rename `<XAPHOME>/lib/optional/jetty-9` to `<XAPHOME>/lib/optional/jetty`
3. Copy the Jetty 9.1.3.v20140225 distribution jar files to the `<XAPHOME>/lib/optional/jetty` directory.

With this version the connector configuration has changed. Here is a complete example on how to configure `jetty.pu.xml` .


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">

    <bean id="propertiesConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="ignoreUnresolvablePlaceholders" value="true"/>
        <property name="properties">
            <props>
                <prop key="web.context">/${clusterInfo.name}</prop>
                <prop key="web.context.classLoader.parentFirst">false</prop>
                <prop key="web.context.copyWebDir">false</prop>
                <prop key="web.context.unique">false</prop>
                <prop key="web.context.separator">_</prop>
                <prop key="web.port">8080</prop>
                <prop key="web.sslPort">8444</prop>
                <prop key="web.threadPool.minThreads">10</prop>
                <prop key="web.threadPool.maxThreads">200</prop>
                <prop key="web.selector.maxIdleTime">300000</prop>
                <prop key="web.selector.acceptors">2</prop>
                <prop key="web.selector.selectors">-1</prop>
                <prop key="web.selector.lowResourcesConnections">20000</prop>
                <prop key="web.selector.lowResourcesMaxIdleTime">5000</prop>
                <prop key="web.selector.forwarded">true</prop>
                <prop key="web.statsOn">false</prop>
            </props>
        </property>
    </bean>

    <bean id="port" class="org.openspaces.pu.container.jee.PortGenerator">
        <property name="basePort" value="${web.port}"/>
    </bean>

    <bean id="confidentialPort" class="org.openspaces.pu.container.jee.PortGenerator">
        <property name="basePort" value="${web.sslPort}"/>
    </bean>

    <bean id="context" class="org.openspaces.pu.container.jee.SharedContextFactory">
        <property name="context" value="${web.context}"/>
        <property name="unique" value="${web.context.unique}"/>
        <property name="separator" value="${web.context.separator}"/>
    </bean>

    <bean id="webAppContext" class="org.eclipse.jetty.webapp.WebAppContext">
        <property name="contextPath" ref="context"/>
        <property name="war" value="${jee.deployPath}"/>
        <property name="tempDirectory" value="${jee.deployPath}/WEB-INF/work"/>
        <property name="copyWebDir" value="${web.context.copyWebDir}"/>
        <property name="parentLoaderPriority" value="${web.context.classLoader.parentFirst}"/>
        <property name="configurationClasses">
            <list>
                <value>org.eclipse.jetty.webapp.WebInfConfiguration</value>
                <value>org.eclipse.jetty.webapp.WebXmlConfiguration</value>
                <value>org.eclipse.jetty.webapp.MetaInfConfiguration</value>
                <value>org.eclipse.jetty.webapp.FragmentConfiguration</value>
                <value>org.eclipse.jetty.plus.webapp.EnvConfiguration</value>
                <value>org.eclipse.jetty.plus.webapp.PlusConfiguration</value>
                <value>org.eclipse.jetty.webapp.JettyWebXmlConfiguration</value>
                <value>org.eclipse.jetty.annotations.AnnotationConfiguration</value>
            </list>
        </property>
        <property name="sessionHandler" ref="secureSessionHandler"/>
    </bean>

    <bean id="secureSessionHandler" class="org.eclipse.jetty.server.session.SessionHandler">
        <property name="sessionManager" ref="sessionManager"/>
    </bean>

    <bean name="sessionManager" class="org.eclipse.jetty.server.session.HashSessionManager">
        <property name="httpOnly" value="true"/>
    </bean>

    <bean id="jettyHolder" class="org.openspaces.pu.container.jee.jetty.holder.SharedJettyHolder">
        <constructor-arg ref="jetty"/>
    </bean>


    <bean id="httpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
        <property name="secureScheme" value="https" />
        <property name="securePort" value="8444"/>
    </bean>


    <bean id="sslHttpConfig" class="org.eclipse.jetty.server.HttpConfiguration">
        <constructor-arg ref="httpConfig"/>
        <property name="customizers">
            <list>
                <bean class="org.eclipse.jetty.server.SecureRequestCustomizer"/>
            </list>
        </property>
    </bean>

    <bean id="sslContextFactory" class="org.eclipse.jetty.util.ssl.SslContextFactory">
        <property name="keyStorePath" value="/path-to/keystore"/>
        <property name="keyStorePassword" value="storepass"/>
        <property name="keyManagerPassword" value="keypass"/>
        <property name="trustStorePath"
                  value="/path-to/keystore"/>
        <property name="trustStorePassword" value="storepass"/>
    </bean>


    <bean id="jetty" class="org.eclipse.jetty.server.Server">

        <constructor-arg>
            <bean class="org.eclipse.jetty.util.thread.QueuedThreadPool">
                <property name="minThreads" value="${web.threadPool.minThreads}"/>
                <property name="maxThreads" value="${web.threadPool.maxThreads}"/>
            </bean>
        </constructor-arg>

        <property name="connectors">
            <list>
                <bean class="org.eclipse.jetty.server.ServerConnector">
                    <constructor-arg name="server" ref="jetty"/>
                    <constructor-arg name="executor">
                        <null/>
                    </constructor-arg>
                    <constructor-arg name="scheduler">
                        <null/>
                    </constructor-arg>
                    <constructor-arg name="bufferPool">
                        <null/>
                    </constructor-arg>
                    <constructor-arg name="acceptors" value="${web.selector.acceptors}"/>
                    <constructor-arg name="selectors" value="${web.selector.selectors}"/>
                    <constructor-arg name="factories">
                        <list>
                            <bean class="org.eclipse.jetty.server.HttpConnectionFactory">
                                <constructor-arg name="config" ref="httpConfig"/>
                            </bean>
                        </list>
                    </constructor-arg>
                    <property name="port" value="8080"/>
                    <property name="idleTimeout" value="${web.selector.maxIdleTime}"/>
                </bean>

                <bean class="org.eclipse.jetty.server.ServerConnector">
                    <constructor-arg name="server" ref="jetty"/>
                    <constructor-arg name="factories">
                        <list>
                            <bean class="org.eclipse.jetty.server.SslConnectionFactory">
                                <constructor-arg index="0" ref="sslContextFactory" />
                                <constructor-arg index="1" type="java.lang.String">
                                    <value>http/1.1</value>
                                </constructor-arg>
                            </bean>
                            <bean class="org.eclipse.jetty.server.HttpConnectionFactory">
                                <constructor-arg name="config" ref="sslHttpConfig"></constructor-arg>
                            </bean>
                        </list>
                    </constructor-arg>
                    <property name="port" value="8444"/>
                    <property name="idleTimeout" value="${web.selector.maxIdleTime}"/>
                </bean>
            </list>
        </property>
        <property name="handler">
            <bean class="org.eclipse.jetty.server.handler.HandlerCollection">
                <property name="handlers">
                    <list>
                        <bean class="org.eclipse.jetty.server.handler.ContextHandlerCollection"/>
                        <bean class="org.eclipse.jetty.server.handler.DefaultHandler"/>
                    </list>
                </property>
            </bean>
        </property>
    </bean>
</beans>
```
