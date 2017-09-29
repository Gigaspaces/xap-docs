---
type: post122
title:  Configuration & Deployment
categories: XAP122,ENT
parent: global-http-session-sharing-overview.html
weight: 200
---


# The Web Application Configuration

The web application requires a couple of configuration changes to the `web.xml` file in order to enabled XAP Session sharing:


```xml
<web-app>
    <listener>
        <listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
    </listener>
    <listener>
        <listener-class>com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager</listener-class>
    </listener>
    <filter>
        <filter-name>GigaSpacesHttpSessionFilter</filter-name>
        <filter-class>com.gigaspaces.httpsession.web.GigaSpacesHttpSessionFilter</filter-class>
        <web:init-param> 
            <web:param-name>rewriteUrl</web:param-name> 
            <web:param-value>false</web:param-value> <!-- default true -->
        </web:init-param>
    </filter>
    <filter-mapping>
        <filter-name>GigaSpacesHttpSessionFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>
</web-app>
```

{{% note %}}The **GigaSpacesHttpSessionFilter** must be the first filter defined.{{% /note %}}

# Shiro configuration

In order to enable the **GigaSpacesHttpSession** you need to set shiro.ini configuration file first.
The following should be located under **main** section.

## Space Connector - Manages connections with the space



|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|connector| wrap SpaceProxy and perform operation against space|Yes|com.gigaspaces.httpsession.SpaceConnector|
|connector.<br>url| Space url including groups and locators |Yes|jini://*/*/<space_name>?groups=myGroup|
|connector.<br>username| Space username|No|<space username>|
|connector.<br>password| Space password|No|<space password>|
|connector.<br>sessionLease|Lease timeout in milliseconds <br>Default to Lease.FOREVER so that the session won't be removed from the space |No|Any positive integer. Millisecond time unit|
|connector.<br>readTimeout|Read timeout in milliseconds <br>Default to 300000|No|Any positive interger. Millisecond time unit|
|connector.<br>sessionBaseName| Fully qualified type name that holds the session attributes in space.<br>Default is com.gigaspaces.httpsession.models.<br>DefaultSpaceSessionStore|Yes|

## Store Mode - Configure how changes are saved to the space


|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|listener|Fully qualified class name implementing com.gigaspaces.httpsession.policies.<br>GigaspacesNotifyListener|No|com.gigaspaces.httpsession.policies.TraceListener|
|storeMode|Provide functionality of how to save changes to the space. there is tow sessions store mode full and delta.|Yes| use on of two options:<br> 1.com.gigaspaces.httpsession.sessions.FullStoreMode <br>2.com.gigaspaces.httpsession.sessions.DeltaStoreMode|
|storeMode.<br>connector| Space connector to be used<br>See [Space Connector Section](#connector-manages-connections-with-the-space)|Yes|$connector|
|storeMode.<br>listener|Provides changes notification functionality. it must extends com.gigaspaces.httpsession.policies.<br>GigaspacesNotifyListener|No| $listener |

## Session Manager - XAP Session Manager Implementation


|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|sessionDAO                    |Provides a transparent caching layer between the components that use it and the underlying EIS (Enterprise Information System) session backing store |Yes|org.apache.shiro.session.<br>mgt.eis.EnterpriseCacheSessionDAO|
|sessionManager                |XAP Session Manager Implementation|Yes|com.gigaspaces.httpsession.<br>GigaSpacesWebSessionManager|
|sessionManager.sessionDAO     ||Yes|$sessionDAO|
|sessionManager.storeMode      |Configure how changes are saved to the space. See [Store Mode Section](#store-mode-configure-how-changes-are-saved-to-the-space)|Yes|$storeMode|
|securityManager.sessionManager|Ensure the securityManager uses our native SessionManager|Yes|$sessionManager|

## Session Policy - Authentication settings


|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|policy|Provides functionality of session policy to apply e.g. with and without authentication. |Yes| Options:<br>1.com.gigaspaces.httpsession.policies.SessionPolicyWithLogin <br>2.com.gigaspaces.httpsession.policies.SessionPolicyWithoutLogin |
|policy.connector|Instance of space connector implementation{{<wbr>}}See [Space Connector Section](#connector-manages-connections-with-the-space)|Yes|$connector|
|policy.storeMode|Instance of space storeMode implementation{{<wbr>}}See [Store Mode Section](#store-mode-configure-how-changes-are-saved-to-the-space)|Yes|$storeMode|

## Serializer


|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|serializer|Provides serialization functionality|Yes| use one of the following options: 	1.`com.gigaspaces.httpsession.serialize.KryoSerializerImpl` (recomended)	2.`com.gigaspaces.httpsession.serialize.XStreamSerializerImpl` <br> 3. Custom - an implementation of the `com.gigaspaces.httpsession.serialize.Serializer` interface
|serializer.logLevel|internal kryo logging level {{<wbr>}}Default to `LEVEL_INFO = 3`|No| 1. `NONE = 6` disables all logging.<br> 2. `ERROR = 5` is for critical errors. The application may no longer work correctly.<br> 3. `WARN = 4` is for important warnings. The application will continue to work correctly.<br> 4.`INFO = 3` is for informative messages. Typically used for deployment.<br> 5. `DEBUG = 2` is for debug messages. This level is useful during development.<br> 6. `TRACE = 1` is for trace messages. A lot of information is logged, so this level is usually only needed when debugging a problem. |
|serializer.classes|comma separate list full qualified class names to be loaded at the initialization of the Kryo Serializer|No||

## Cache Manager - XAP Cache Manager Implementation


|Property|Description|Required|Optional Values|
|:-------|:----------|:-------|:--------------|
|compressor|Provides compress functionality|No| Provides your own com.gigaspaces.httpsession.serialize.Compressor <br>implementation or use one of the out of the box option:<br> 1.com.gigaspaces.httpsession.serialize.<br>CompressorImpl<br>2.com.gigaspaces.httpsession.<br>serialize.NonCompressCompressor|
|cacheManager|XAP extension of org.apache.shiro.cache.CacheManager Provides and maintains the lifecycles of `com.gigaspaces.httpsession.{{<wbr>}}sessions.GigaSpacesCache` instances|Yes|com.gigaspaces.httpsession.<br>sessions.GigaSpacesCacheManager|
|cacheManager.<br>initialCapacity|Specifies the initial capacity of the LRU used for caching session in memory.|No|1000|
|cacheManager.<br>maximumCapacity|Maximum capacity of the LRU used for caching session in memory.|No|10000|
|cacheManager.<br>concurrencyLevel|Specifies the estimated number of concurrently updating threads|No|16|
|cacheManager.<br>compressor|Set the compressor instance to be used. {{<wbr>}}Default to `com.gigaspaces.httpsession.{{<wbr>}}serialize.NonCompressCompressor`|No|$compressor|
|cacheManager.<br>serializer|Instance of the serializer implementation{{<wbr>}}See [Serializer Section](#serializer)|Yes|$serializer|
|cacheManager.<br>policy|Instance of session policy implementation{{<wbr>}}See [Session Policy Section](#session-policy-authentication-settings)|Yes|$policy|
|cacheManager.<br>connector|Instance of space connector implementation{{<wbr>}}See [Space Connector Section](#connector-manages-connections-with-the-space)|Yes|$connector|


The `shiro.ini` file should to be placed within the `WEB-INF` folder. See below examples for the `shiro.ini` file:

{{%accordion%}}


{{%accord title="Session Sharing Configuration For Non-Secured Application ..."%}}


```ini
[main]
# space proxy wraper
connector=com.gigaspaces.httpsession.SpaceConnector
connector.url=jini://*/*/sessionSpace
# When using secured GigaSpace cluster, pass the credentials here
# connector.username = <username>
# connector.password = <password>
# Default lease is Lease.FOREVER so that the session won't be removed from the space
# connector.sessionLease = 9223372036854775807
# Default read timeout is 5 minutes = 5 * 60 * 1000 = 300000
connector.readTimeout = 300000

# The SpaceDocument class name that holds the session attributes in space
connector.sessionBaseName=com.gigaspaces.httpsession.models.DefaultSpaceSessionStore

# Model Manager service
storeMode=com.gigaspaces.httpsession.sessions.DeltaStoreMode
storeMode.connector=$connector
listener1 = com.gigaspaces.httpsession.policies.TraceListener
storeMode.listener=$listener1

# Set the sessionManager to use an enterprise cache for backing storage:
sessionDAO=org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
sessionManager=com.gigaspaces.httpsession.GigaSpacesWebSessionManager
sessionManager.sessionDAO=$sessionDAO
sessionManager.storeMode=$storeMode

# ensure the securityManager uses our native SessionManager:
securityManager.sessionManager=$sessionManager

# Session Policy Service
policy=com.gigaspaces.httpsession.policies.SessionPolicyWithoutLogin
policy.connector=$connector
policy.storeMode=$storeMode

# Serialization Service
serializer=com.gigaspaces.httpsession.serialize.KryoSerializerImpl
# serializer.logLevel = 1
javaSerialization=com.esotericsoftware.kryo.serializers.JavaSerializer
## classes registation: class1, class2, ...,classN
serializer.classSerializers=javax.security.auth.Subject:$javaSerialization,org.springframework.security.core.userdetails.User:$javaSerialization,org.springframework.security.core.context.SecurityContextImpl:$javaSerialization

compressor=com.gigaspaces.httpsession.serialize.CompressorImpl

# Set the cacheManager to use the GigaSpaceCacheManager
cacheManager=com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager
cacheManager.initialCapacity=1000
cacheManager.maximumCapacity=10000
cacheManager.concurrencyLevel=32
cacheManager.storeMode=$storeMode
cacheManager.serializer=$serializer
cacheManager.policy=$policy
# space proxy setter
cacheManager.connector=$connector
cacheManager.compressor=$compressor

# This will use GigaSpaces for _all_ of Shiro's caching needs (realms, etc), not just for Session storage.
securityManager.cacheManager=$cacheManager

```
{{%/accord%}}

{{%accord title="Session Sharing Configuration Example For Secured Application Using Shiro Security..."%}}
{{% note %}}Note that this example uses the basic authentication configuration but, Shiro has various authenticator types see [realm modules](http://shiro.apache.org/static/1.2.1/apidocs/org/apache/shiro/authc/class-use/AuthenticationException.html) {{% /note %}}

```ini
[main]
# space proxy wraper
connector=com.gigaspaces.httpsession.SpaceConnector
connector.url=jini://*/*/sessionSpace
# When using secured GigaSpace cluster, pass the credentials here
# connector.username = <username>
# connector.password = <password>
# Default lease is Lease.FOREVER so that the session won't be removed from the space
# connector.sessionLease = 9223372036854775807
# Default read timeout is 5 minutes = 5 * 60 * 1000 = 300000
connector.readTimeout = 300000

# The SpaceDocument class name that holds the session attributes in space
connector.sessionBaseName=com.gigaspaces.httpsession.models.DefaultSpaceSessionStore

# Model Manager service
storeMode=com.gigaspaces.httpsession.sessions.DeltaStoreMode
storeMode.connector=$connector
listener1 = com.gigaspaces.httpsession.policies.TraceListener
storeMode.listener=$listener1

# Set the sessionManager to use an enterprise cache for backing storage:
sessionDAO=org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
sessionManager=com.gigaspaces.httpsession.GigaSpacesWebSessionManager
sessionManager.sessionDAO=$sessionDAO
sessionManager.storeMode=$storeMode

# ensure the securityManager uses our native SessionManager:
securityManager.sessionManager=$sessionManager

# Session Policy Service
policy=com.gigaspaces.httpsession.policies.SessionPolicyWithLogin
policy.connector=$connector
policy.storeMode=$storeMode

# Serialization Service
serializer=com.gigaspaces.httpsession.serialize.KryoSerializerImpl
# serializer.logLevel = 1
javaSerialization=com.esotericsoftware.kryo.serializers.JavaSerializer
## classes registation: class1, class2, ...,classN
serializer.classSerializers=javax.security.auth.Subject:$javaSerialization,org.springframework.security.core.userdetails.User:$javaSerialization,org.springframework.security.core.context.SecurityContextImpl:$javaSerialization

compressor=com.gigaspaces.httpsession.serialize.CompressorImpl

# Set the cacheManager to use the GigaSpaceCacheManager
cacheManager=com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager
cacheManager.initialCapacity=1000
cacheManager.maximumCapacity=10000
cacheManager.concurrencyLevel=32
cacheManager.storeMode=$storeMode
cacheManager.serializer=$serializer
cacheManager.policy=$policy
# space proxy setter
cacheManager.connector=$connector
cacheManager.compressor=$compressor

# This will use GigaSpaces for _all_ of Shiro's caching needs (realms, etc), not just for Session storage.
securityManager.cacheManager=$cacheManager

[users]
## format: username = password, role1, role2, ..., roleN
root = secret,admin
guest = guest,guest
presidentskroob = 12345,president
darkhelmet = ludicrousspeed,darklord,schwartz
lonestarr = vespa,goodguy,schwartz
	
[roles]
## format: roleName = permission1, permission2, ..., permissionN
admin = *
schwartz = lightsaber:*
goodguy = winnebago:drive:eagle5

[urls]
## The /login.jsp is not restricted to authenticated users (otherwise no one could log in!), but
## the 'authc' filter must still be specified for it so it can process that url's
## login submissions. It is 'smart' enough to allow those requests through as specified by the
## shiro.loginUrl above.
/login.jsp = authc
/** = authc
##/logout = logout
##/account/** = authc
/remoting/** = authc, roles[b2bClient], perms["remote:invoke:lan,wan"]
```
{{%/accord%}}

{{%accord title="Session Sharing Configuration Example For Secured Application Using Spring Security..."%}}
{{% note %}}Note that in order to use Spring Security you still have to provide `shiro.ini` configuration file.
{{<wbr>}}The configuration should be similar to the one in the **Non-Secured Application** example above. {{% /note %}}

Below is an example for the spring-security.xml that should be located under `WEB-INF` folder.

```xml
<beans:beans xmlns="http://www.springframework.org/schema/security"
			 xmlns:beans="http://www.springframework.org/schema/beans"
			 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
	http://www.springframework.org/schema/security
	http://www.springframework.org/schema/security/spring-security-3.2.xsd">

	<http auto-config="true" create-session="never">
		<intercept-url pattern="/**" access="ROLE_USER"/>
		<intercept-url pattern="/UpdateSessionServlet**" access="ROLE_USER" />
		<logout logout-success-url="/" />
		<session-management session-fixation-protection="none">
			<concurrency-control/>
		</session-management>
	</http>

	<authentication-manager>
		<authentication-provider>
			<user-service>
				<user name="user1" password="user1" authorities="ROLE_USER" />
				<user name="user2" password="user2" authorities="ROLE_USER" />
			</user-service>
		</authentication-provider>
	</authentication-manager>

</beans:beans>
```

In addition, you need to add the following in your web.xml file:


```xml
<!-- Spring MVC -->
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring-security.xml</param-value>
</context-param>

<!-- Spring Security -->
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>

<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

{{%/accord%}}


{{%/accordion%}}

<br>

# Web Application Libraries

The web application should include the following libraries within its `\WEB-INF\lib` folder:
 
* gs-session-manager.jar - located within the `XAP ROOT\lib\optional\httpsession` folder.
* xap-datagrid.jar  - located within the `XAP ROOT\lib\required` folder.

{{% note %}}
The `xap-datagrid.jar` should be replaced with the relevant XAP `xap-datagrid.jar` matching your XAP data grid release.
{{% /note %}}

Another option is to use Maven:


```xml
<repositories>
	<repository>
		<id>org.openspaces</id>
		<name>OpenSpaces</name>
		<url>http://maven-repository.openspaces.org</url>
		<releases>
			<enabled>true</enabled>
			<updatePolicy>daily</updatePolicy>
			<checksumPolicy>warn</checksumPolicy>
		</releases>
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
			<checksumPolicy>warn</checksumPolicy>
		</snapshots>
	</repository>
</repositories>
<dependencies>
	<dependency>
		<groupId>org.gigaspaces</groupId>
		<artifactId>xap-datagrid</artifactId>
		<version>{{%version "maven-version" %}}</version>
	</dependency>

	<dependency>
		<groupId>com.gigaspaces.httpsession</groupId>
		<artifactId>gs-session-manager</artifactId>
		<version>{{%version "maven-version" %}}</version>
	</dependency>
</dependencies>
```


# Deployment

The XAP IMDG should be deployed using one of the [topologies](/product_overview/space-topologies.html).


{{%tabs%}}
{{%tab CLI%}}
```bash
# To deploy the IMDG called `sessionSpace` start the XAP agent using:
<XAP-HOME>/bin/gs-agent

# and run the following command to deploy the session Space:
<XAP-HOME>/bin/gs deploy-space sessionSpace
```
{{%/tab%}}
{{%tab "REST"%}}
```bash
# start the agent with the REST interface
# Windows
gs-agent.bat --manager-local --gsc=2
# Unix
./gs-agent.sh --manager-local --gsc=2

# deploy the space
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 
'http://localhost:8090/v1/spaces?name=sessionSpace&requiresIsolation=false'
```
{{%/tab%}}
{{%/tabs%}}


{{% refer %}}See the [deploy-space]({{%currentadmurl%}}/deploy-command-line-interface.html) command for details.
{{% /refer %}}

### Classpath
The `gs-session-manager.jar` located within the `\gigaspaces-xap-root\lib\optional\httpsession` folder should be copied into the `\gigaspaces-xap-root\lib\platform\ext` folder. 

### Securing the XAP IMDG

When using a [Secure XAP cluster]({{%currentsecurl%}}/securing-your-data.html) you can pass security credentials using following parameters in the `shiro.ini` file:


```ini
connector.username = user
connector.password = pass
```


# Example

{{%refer %}}
Examples of using the XAP HTTP Session can be found [here](../started/http-session-sharing.html) 
{{%/refer%}}


# Considerations

## Web Application Context

Global HTTP session sharing works only when your application is deployed as a non-root context. It is relying on browser cookies for identifying user session, specifically `JSESSIONID` cookie. Cookies are generated at a context name per host level. This way all the links on the page are referring to the same cookie/user session.

## WebSphere Application Server HttpSessionIdReuse Custom Property

When using XAP Global HTTP session sharing with applications deployed into WebSphere Application Server, please enable the [HttpSessionIdReuse](http://pic.dhe.ibm.com/infocenter/wasinfo/v7r0/index.jsp?topic=%2Fcom.ibm.websphere.express.doc%2Finfo%2Fexp%2Fae%2Frprs_custom_properties.html) custom property. In a multi-JVM environment that is not configured for session persistence setting this property to true enables the session manager to use the same session information for all of a user's requests even if the Web applications that are handling these requests are governed by different JVMs.

## Transient Attribute

An attribute specified as *transient* would not be shared and its content will not be stored within the IMDG. Your code should be modified to have this as a regular attribute that can be serialized.

## Webserver's Java version

Please note that:

- Jetty 9 does not support JDK6.
- JBoss 7 does not support JDK8.



## Modifying Session Attributes

We assume that no attributes are changed in the login phase: Changing a regular attribute in the login request will store the change in-memory but won't save it in the space.
