---
type: post100
title:  Configuration & Deployment
categories: XAP100
parent: global-http-session-sharing-overview.html
weight: 200
---

{{%ssummary%}}{{%/ssummary%}}



# The Web Application Configuration

The web application requires a couple of configuration changes to the `web.xml` file in order to enabled XAP Session sharing:


```xml
<web-app>
		....
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

shiro.ini settings 


|Section|Property|Description|Required|Optional Values|Default Values|
|:------|:-------|:----------|:-------|:--------------|:-------------|
|main|connector| wrap SpaceProxy and perform operation aginst space|Yes|`com.gigaspaces.httpsession.SpaceConnector`|
|main|connector.url| Space url|Yes|`jini://*/*/<space_name>`|
|main|connector.username| Space username|No|`<space username>`|
|main|connector.password| Space password|No|`<space password>`|
|main|connector.sessionLease|Lease timeout in milliseconds|No|Any positive integer. Millisecond time unit| 1800000 |
|main|connector.readTimeout|Read timeout in milliseconds|No|Any positive interger. Millisecond time unit| 300000 |
|main|sessionManager|XAP Session Manager Implementation|Yes|com.gigaspaces.httpsession.GigaSpacesWebSessionManager|
|main|sessionManager.sessionDAO||Yes|$sessionDAO|
|main|sessionDAO|Provides a transparent caching layer between the components that use it and the underlying EIS (Enterprise Information System) session backing store |Yes|org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO|
|main|cacheManager|XAP extension of org.apache.shiro.cache.CacheManager Provides and maintains the lifecycles of `com.gigaspaces.httpsession.sessions.GigaSpacesCache` instances|Yes|com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager|
|main|cacheManager.compressor|Set the compressor instance to be used|No|$compressor|`com.gigaspaces.httpsession.serialize.NonCompressCompressor`
|main|cacheManager.serializer|instance of the serializer implementation|Yes|$serializer|
|main|cacheManager.policy|instance of session policy implementation|Yes|$policy|
|main|cacheManager.connector|instance of space connector implementation|Yes|$connector|
|main|compressor|Provides compress functionality|No| Provides your own `com.gigaspaces.httpsession.serialize.Compressor` implementation or use one of the out of the box option:<br> 1.`com.gigaspaces.httpsession.serialize.CompressorImpl`<br>2.`com.gigaspaces.httpsession.serialize.NonCompressCompressor`|
|main|storeMode|Provide functionality of how to save changes to the space. there is tow sessions store mode full and delta.|Yes| use on of two options:<br> 1.`com.gigaspaces.httpsession.sessions.FullStoreMode` 2.`com.gigaspaces.httpsession.sessions.DeltaStoreMode`|
|main|storeMode.connector| Space connector to be used|Yes|$connector|
|main|storeMode.listener|Provides changes notification functionality. it must extends `com.gigaspaces.httpsession.policies.GigaspacesNotifyListener`|No| `listener`|
|main|storeMode.changeStrategy|define strategy of comparison and conflict detection response.|Yes|DeltaStoreMode:   1.`com.gigaspaces.httpsession.policies.LWWChangeStrategy` 2.`com.gigaspaces.httpsession.policies.FailFastChangeStrategy` 3.`com.gigaspaces.httpsession.policies.PartialChangeStrategy`|FullStoreMode: `com.gigaspaces.httpsession.policies.FullModeStrategy`    DeltaStoreMode: `com.gigaspaces.httpsession.policies.LWWChangeStrategy`
|main|listener|Fully qualified class name implementing `com.gigaspaces.httpsession.policies.GigaspacesNotifyListener`|No|`com.gigaspaces.httpsession.policies.TraceListener`|
|main|serializer|Provides serialization functionality|Yes| use one of the following options: 	1.`com.gigaspaces.httpsession.serialize.KryoSerializerImpl` (recomended)	2.`com.gigaspaces.httpsession.serialize.XStreamSerializerImpl` <br> 3. Custom - an implementation of the `com.gigaspaces.httpsession.serialize.Serializer` interface
|main|serializer.logLevel|internal kryo logging level|No| 1. `NONE = 6` disables all logging.<br> 2. `ERROR = 5` is for critical errors. The application may no longer work correctly.<br> 3. `WARN = 4` is for important warnings. The application will continue to work correctly.<br> 4.`INFO = 3` is for informative messages. Typically used for deployment.<br> 5. `DEBUG = 2` is for debug messages. This level is useful during development.<br> 6. `TRACE = 1` is for trace messages. A lot of information is logged, so this level is usually only needed when debugging a problem. | `LEVEL_INFO = 3`
|main|serializer.classes|comma separate list full qualified class names to be loaded at the initialization of the Kryo Serializer|No||
|main|policy|Provides functionality of session policy to apply e.g. with and without authentication|Yes| Options:<br>1.`com.gigaspaces.httpsession.policies.SessionPolicyWithLogin` for sharing session cross multiple aplplications<br>2.`com.gigaspaces.httpsession.policies.SessionPolicyWithoutLogin` for single application session store|
|main|policy.connector = $connector|instance of space connector implementation|Yes||
|main|policy.storeMode = $storeMode|instance of space storeMode implementation|Yes||


The `shiro.ini` file should to be placed within the `WEB-INF` folder. See below examples for the `shiro.ini` file:

{{%accordion%}}


{{%accord title="Single Application Session Sharing Configuration Example..."%}}


```bash

	[main]
	# space proxy wraper
	connector = com.gigaspaces.httpsession.SpaceConnector
	connector.url = jini://*/*/sessionSpace
	# When using secured GigaSpace cluster, pass the credentials here
	# connector.username = <username>
	# connector.password = <password>
	# Default lease is 30 minutes - 30 * 60 * 1000 = 1800000
	connector.sessionLease = 1800000
	# Default read timeout is 5 minutes = 5 * 60 * 1000 = 300000
	connector.readTimeout = 300000
	
	sessionManager = com.gigaspaces.httpsession.GigaSpacesWebSessionManager
	
	#set the sessionManager to use an enterprise cache for backing storage:
	sessionDAO = org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
	sessionManager.sessionDAO = $sessionDAO
	
	# ensure the securityManager uses our native SessionManager:
	securityManager.sessionManager = $sessionManager
	
	# whatever your CacheManager implementation is, for example:
	cacheManager = com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager
	
	# Model Manager service
	storeMode = com.gigaspaces.httpsession.sessions.DeltaStoreMode
	storeMode.connector = $connector
	#storeMode.changeStrategy = com.gigaspaces.httpsession.policies.FailFastChangeStrategy
	listener1 = com.gigaspaces.httpsession.policies.TraceListener
	storeMode.listener = $listener1
	
	cacheManager.storeMode = $storeMode
	# Serialization Service
	serializer = com.gigaspaces.httpsession.serialize.KryoSerializerImpl
	serializer.logLevel = 1
	#### classes registation: class1, class2, ...,classN
	#serializer.classes = com.pak1.myClass1 , com.pak2.myClass2
	cacheManager.serializer = $serializer
	# Session Policy Service
	policy = com.gigaspaces.httpsession.policies.SessionPolicyWithoutLogin
	policy.connector = $connector
	policy.storeMode = $storeMode
	
	cacheManager.policy = $policy
	# space proxy setter
	cacheManager.connector= $connector
	
	# This will use GigaSpaces for _all_ of Shiro's caching needs (realms, etc), not just for Session storage.
	securityManager.cacheManager = $cacheManager

```
{{%/accord%}}

{{%accord title="Multiple Applications Session Sharing Configuration Example..."%}}
{{% note %}}Note that this example uses the basic authentication configuration but, Shiro has various authenticator types see [realm modules](http://shiro.apache.org/static/1.2.1/apidocs/org/apache/shiro/authc/class-use/AuthenticationException.html) {{% /note %}}

```bash

	[main]
	authc.loginUrl = /login.jsp
	
	# space proxy wraper
	connector = com.gigaspaces.httpsession.SpaceConnector
	connector.url = jini://*/*/sessionSpace
	# When using secured GigaSpace cluster, pass the credentials here
	# connector.username = <username>
	# connector.password = <password>
	# Default lease is 30 minutes - 30 * 60 * 1000 = 1800000
	connector.sessionLease = 1800000
	
	#sessionManager = org.apache.shiro.web.session.mgt.StandardWebSessionManager
	#sessionManager = org.apache.shiro.web.session.mgt.DefaultWebSessionManager
	sessionManager = com.gigaspaces.httpsession.GigaSpacesWebSessionManager
	
	#set the sessionManager to use an enterprise cache for backing storage:
	sessionDAO = org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
	sessionManager.sessionDAO = $sessionDAO
	
	# ensure the securityManager uses our native SessionManager:
	securityManager.sessionManager = $sessionManager
	
	# whatever your CacheManager implementation is, for example:
	cacheManager = com.gigaspaces.httpsession.sessions.GigaSpacesCacheManager
	
	# Compression service
	#compressor = com.gigaspaces.httpsession.serialize.CompressorImpl
	#cacheManager.compressor = $compressor
	# Model Manager service
	#changeStrategy = com.gigaspaces.httpsession.policies.LastWriteWinsPolicy
	storeMode = com.gigaspaces.httpsession.models.DeltaStoreMode
	storeMode.connector = $connector
	#storeMode.changeStrategy = $changeStrategy
	
	cacheManager.storeMode = $storeMode
	# Serialization Service
	serializer = com.gigaspaces.httpsession.serialize.KryoSerializerImpl
	#serializer.classes = com.gigaspaces.httpsession.sessions.NestSerial , com.gigaspaces.httpsession.sessions.NestNonSerial
	cacheManager.serializer = $serializer
	# Session Policy Service
	policy = com.gigaspaces.httpsession.policies.SessionPolicyWithLogin
	policy.connector = $connector
	policy.storeMode = $storeMode
	
	cacheManager.policy = $policy
	# space proxy setter
	cacheManager.connector= $connector
	
	# This will use GigaSpaces for _all_ of Shiro's caching needs (realms, etc), not just for Session 	storage.
	securityManager.cacheManager = $cacheManager
	
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
	/remoting/** = authc, roles[b2bClient], perms["remote:invoke:lan,wan"
```
{{%/accord%}}
{{%/accordion%}}

<br>

### Web Application Libraries

The web application should include the following libraries within its `\WEB-INF\lib` folder:
 
* gs-session-manager-xxx.jar - located within the `XAP ROOT\lib\optional\httpsession` folder.
* gs-runtime.jar  - located within the `XAP ROOT\lib\required` folder.

{{% note %}}
The `gs-runtime.jar` should be replaced with the relevant XAP `gs-runtime.jar` matching your XAP data grid release.
{{% /note %}}

# Deployment

The XAP IMDG should be deployed using one of the [topologies](/product_overview/space-topologies.html). You may also use the WAN Gateway with multi data grid deployment.


```bash
# To deploy the IMDG called `sessionSpace` start the XAP agent using:
<XAP-HOME>/bin/gs-agent

# and run the following command to deploy the session Space:
<XAP-HOME>/bin/gs deploy-space sessionSpace

```


{{% refer %}}See the [deploy-space]({{%currentadmurl%}}/deploy-command-line-interface.html) command for details.
{{% /refer %}}

### Deploying the WAN Gateway

The [WAN Gateway]({{%currentjavaurl%}}/multi-site-replication-over-the-wan.html) should be deployed using your preferred replication topography, such as multi-master or master-slave. See the [WAN Replication Gateway](/sbp/wan-replication-gateway.html) best practice for an example of how a multi-master Gateway architecture can be deployed.

### Securing the XAP IMDG

When using a [Secure XAP cluster]({{%currentsecurl%}}/securing-your-data.html) you can pass security credentials using following parameters in the `shiro.ini` file:


```bash
connector.username = user
connector.password = pass
```


# Example

The example can be deployed into any web server (Tomcat, JBoss, Websphere, Weblogic, Jetty, GlassFish). It demostrates Single Application Session Sharing configuration.

1. Download the demo web application {{%download "/download_files/demo-app.war"%}}.
2. Deploy a space named **sessionSpace**. You may have a single instance Space or deploy a clustered Space using the command line , GS-UI or the Web-UI.
3. Deploy the `demo-app.war` into Tomcat (or any other app server).
4. Start your browser and access the web application via the following URL: `http://localhost:8080/demo-app`

{{% note %}}
The URL above assumes the Web Server is configured to use port 8080.
{{% /note %}}


{{%panel "Set some attributes with their name and value and click the **Update Session** button."%}}
<br>

![httpSessionSharing4.jpg](/attachment_files/http-session-sharing-single-1.png)

{{%/panel%}}


{{%panel "View the session updated within the space via the GS-UI or Web-UI."%}}
<br>
![httpSessionSharing4.jpg](/attachment_files/http-session-sharing-single-2.png)
{{%/panel%}}

{{%panel "Identify the Session ID"%}}
<br>
![httpSessionSharing4.jpg](/attachment_files/http-session-sharing-single-3.png)
{{%/panel%}}

{{%tip%}}
Restart your web application and refresh the page. The session will be reloaded from the data grid.
{{%/tip%}}



# Considerations

## Web Application Context

Global HTTP session sharing works only when your application is deployed as a non-root context. It is relying on browser cookies for identifying user session, specifically `JSESSIONID` cookie. Cookies are generated at a context name per host level. This way all the links on the page are referring to the same cookie/user session.

## WebSphere Application Server HttpSessionIdReuse Custom Property

When using XAP Global HTTP session sharing with applications deployed into WebSphere Application Server, please enable the [HttpSessionIdReuse](http://pic.dhe.ibm.com/infocenter/wasinfo/v7r0/index.jsp?topic=%2Fcom.ibm.websphere.express.doc%2Finfo%2Fexp%2Fae%2Frprs_custom_properties.html) custom property. In a multi-JVM environment that is not configured for session persistence setting this property to true enables the session manager to use the same session information for all of a user's requests even if the Web applications that are handling these requests are governed by different JVMs.

## Transient Attribute

An attribute specified as *transient* would not be shared and its content will not be stored within the IMDG. Your code should be modified to have this as a regular attribute that can be serialized.


