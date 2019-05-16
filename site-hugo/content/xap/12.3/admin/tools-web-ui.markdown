---
type: post123
title:  Web Management Console
categories: XAP123ADM,PRM
weight: 300
canonical: auto
parent: admin-tools.html
---

The Web Management Console is a web application that enables users to quickly understand the state of a running data grid cluster and monitor the running components, such as the physical hosts, JVMs and deployed Processing Units.

This user interface complements the GigaSpaces Management Center and provides a lightweight alternative for monitoring a running cluster, without having to install the GigaSpaces XAP runtime and run the standalone Java-based user interface.

The Web Management Console application is located under `XAP root/tools/gs-webui`. This directory contains the web application itself (in the form of a standard JEE .WAR file), and a launcher library and shell scripts used to start it in standalone mode (see below).

# Starting the Web Management Console on a Local Machine

When you first start using InsightEdge or XAP, you may prefer to run the Web Management Console on your local machine instead of on a web server. Simply click the `gs-webui.sh(bat)` script to start a Jetty web container with the Web Management Console application running within it. After the user interface starts, there's no need to deploy or perform any additional steps.

{{%note "Note"%}}
In standalone mode, the web container listens by default on port 8099. To view the Web Management Console point your browser to `http://<standalone server host>:8099`, where `<standalone server host>` is the host on which you launched the `gs-webui` script. In this mode, the default context path for the Web Management Console is the root context path ("/").
{{% /note %}}

# Security

## Enabling Security

The Web Management Console (Web-UI) allows for a secured access when the security `enabled` property is set. This property should be configured using `EXT_JAVA_OPTIONS` in `setenv` script and is applied to all XAP [Grid Components](../security/securing-the-grid-services.html).

The property:
```java
-Dcom.gs.security.enabled=true
```

{{%note "Notes"%}}
The Lookup groups and Locators are loaded from the `setenv/setenv-overrides` script. ץ Lookup groups and Lookup Locators can be configured in the configuration files described in [Configure Lookup Groups and Locators](../started/common-environment-variables.html#extension).
{{%/note%}}

## Security Properties File

The security properties file is used to configure the `SecurityManager`, which is responsible for the authentication and authorization process. The `security.properties` file is common to all components and is usually located under `<XAP root>/config/security/security.properties`. To only affect the Web Management Console, use `webui-security.properties` instead.

The configuration file can be located anywhere in the classpath or in the classpath under `config/security`. Alternatively, a system property can be set to indicate the location of the properties file: 

```java
-Dcom.gs.security.properties-file = my-security.properties
```

By setting `-Dcom.gs.security.properties-file` the property file will be located as a direct path (e.g. `~/home/user/my-security.properties`), 
a resource (e.g. "my-security.properties") in the classpath or in the classpath under `config/security`.

## Custom Credentials

An authentication mechanism might require a different set of actions to be taken on the provided credentials (username/password). A custom extension can be provided for this, as described in the [Authentication](../security/custom-authentication.html) page of the [Security](../security/index.html) section.

This credentials provider is configured as arguments to the command line of the Web Managment Console. Use `-user-details-provider` for the provider class name. Use `-user-details-properties` to provide additional properties. This argument is optional.

Run the `gs-webui` script with these parameters:

```bash
gs-webui(.sh/bat) -user-details-provider com.demo.CustomCredentialsProvider -user-details-properties custom-security.server-address=myServer
```

## SSL Connection 

In order to run the web-ui server with SSL ( using the HTTPS protocol instead of HTTP), the following parameters must be provided as arguments to the `gs-webui` script:

- `ssl.keyManagerPassword` - Password (if any) for the specific key within the key store.
- `ssl.keyStorePassword` - Password for the key store.
- `ssl.keyStorePath` - File or URL of the SSL Key store.
- `ssl.trustStorePath` - File name or URL of the trust store location.
- `ssl.trustStorePassword` - Password for the trust store.

{{%tabs%}}
{{%tab "  Linux "%}}


```bash
#Specify SSL via a command line argument
./gs-webui.sh -ssl.keyManagerPassword <passw> -ssl.keyStorePassword <passw> -ssl.keyStorePath <key-store-path> -ssl.trustStorePath <trust-store-path> -ssl.trustStorePassword <passw>

```

{{% /tab %}}
{{%tab "  Windows "%}}


```bash
#Specify SSL via a command line argument
gs-webui.bat  -ssl.keyManagerPassword <passw> -ssl.keyStorePassword <passw> -ssl.keyStorePath <key-store-path> -ssl.trustStorePath <trust-store-path> -ssl.trustStorePassword <passw>

```

{{% /tab %}}
{{% /tabs %}}

You can also use the `WEBUI_JAVA_OPTIONS` environment variable to set any JVM parameter, such as heap size (defaults to `-Xmx512m`) and other JVM settings.

In order to disable anonymous login, use `com.gigaspaces.webui.username.mandatory` system property. The following example shows how to do this:

{{%tabs%}}
{{%tab "  Linux "%}}


```bash

#Specify user name field as mandatory
export USER_NAME_MANDATORY=true
./gs-webui.sh
```

{{% /tab %}}
{{%tab "  Windows "%}}


```bash

#Specify user name field as mandatory
set USER_NAME_MANDATORY=true
gs-webui.bat
```

{{% /tab %}}
{{% /tabs %}}


# Deploying the Web Application

When you reach the stage of a proper deployment, or are preparing to move to production, you can deploy the Web Management Console as described below.

## XAP Runtime Environment

To deploy the Web Management Console to the XAP [Runtime Environment](./the-runtime-environment.html), point your deployment tool of choice (CLI, Admin API or the GigaSpaces Management Center) to the `<XAP root>/tools/gs-webui/gs-webui.war` file and deploy it.

In this case, the Web Management Console actually monitors the runtime environment on which it runs.

The following example shows how to do this using the XAP CLI:


```bash
<XAP root>/bin/gs.sh(bat} deploy -properties embed://web.port=80;web.context=/
<XAP root>/tools/gs-webui/gs-webui.war
```

The above command deploys the Web Management Console to the XAP runtime environment, listening on port 80 with the root context path.

{{%refer%}}
For more information about XAP's web application support, refer to the [Web Application Support](../dev-java/web-application-overview.html) section in the Developer Guide.
{{%/refer%}}

## Third-Party JEE Servlet Container

You can deploy the Web Management Console to a third-party servlet container, for example, Apache Tomcat (must be Tomcat 8 and above in order to support Java 8). Consult your web container documentation for deployment instructions.

{{% note "Note"%}}
When deploying to a third-party web container like Apache Tomcat, you must repackage the `xap-webui-[version-build].war` file and add the following .JAR files to the `WEB-INF/lib` directory of the `xap-webui-[version-build].war` file:

1. `.jar` files located under `<XAP root>/lib/required` <br>
1. `<XAP root>/lib/required/optional/spring/spring-web-x.x.x.RELEASE.jar`<br>
1. `<XAP root>/lib/platform/commons/commons-collections-x.x.x.jar`<br>
1. `<XAP root>/lib/optional/spring/spring-webmvc-4.1.1.RELEASE.jar`<br>

By default, these are not part of the the `gs-webui.war` file because they are automatically included in the classpath of both the standalone container and the XAP [Runtime Environment](./the-runtime-environment.html).
{{% /note %}}

After the web application starts, point your browser to the proper location. For example, if you start the web application using a standalone web container, the default URL is `http://<standalone server host>:8099`.

You will see the following screen:
 
![xap-login-inline.png](/attachment_files/web-console/main-page-12.3.png)


# Supported Web Browsers

The Web Management Console supports the following web browsers:

- {{%exurl "Moziila Firefox 11""http://www.mozilla.com/firefox/"%}} or higher
- {{%exurl "Google Chrome 18""http://www.google.com/chrome"%}} or higher
- Microsoft Internet Explorer 10 or higher

# Runtime Considerations

## Deployment Location of the Web Application

The web application communicates with the runtime components of the XAP cluster, and receives notification from the XAP [lookup service](../overview/the-runtime-environment.html#lus). Therefore, it is highly recommended to run the web application in the same network segment of the other cluster components. This does not affect the web browser client, which communicates with the web application using standard HTTP and can be located anywhere, provided that it has access to the web application.

## Running via a Reverse Proxy

YOu can set up a reverse proxy for the Web Management Console. This may be preferable in certain scenarios, such as when access to the Web Management Console is done via a gateway. Reverse proxy setups are currently available and tested only on the Apache web server.

## Configuring the Proxy

Enable the relevant modules, by uncommenting (or adding) the following lines inside `httpd.conf`:

```bash
LoadModule  proxy_module         modules/mod_proxy.so
LoadModule  proxy_http_module    modules/mod_proxy_http.so
```

Add your server's name, and define a virtual host for the configuration, where `gs.webui.com` represents your server's DNS:


```xml
NameVirtualHost *:80
<VirtualHost *:80>
        ServerName gs.webui.com
</VirtualHost>
```

Create mappings to channel proxy paths (`webui-endpoint` being the proxied path obscuring the address of the Web Management Console):


```xml
<VirtualHost *:80>
        ...
        ProxyPass /webui-endpoint/ http://127.0.0.1:8099/
        ProxyPassReverse /webui-endpoint/ http://127.0.0.1:8099/
        ProxyPreserveHost on
</VirtualHost>
```

Set a redirection rule for serving the login page:


```xml
<VirtualHost *:80>
        ...
        RedirectMatch /Gs_webui.html /webui-endpoint/Gs_webui.html
</VirtualHost>
```

## Debugging Your Proxy

You can dump logging information to custom files on the Apache server by adding the following rules:


```xml
<VirtualHost *:80>
        ...
        ErrorLog "logs/webui-error_log"
        CustomLog "logs/webui-access_log" common
</VirtualHost>
```

For more information on Apache's reverse proxy configuration, refer to {{%exurl "the corresponding entry""http://www.apachetutor.org/admin/reverseproxies"%}} on the Apache Tutor website.

# Customizing the Configuration Options

The Web Management Console has an out-of-the-box default configuration that can be used with any standard environment. However, when preparing for deployment, you may want to customize certain configuration options to suit your specific organization needs. 

## Localizing the User Interface

In addition to the default English, the Web Management Console supports localization to Chinese. You can define the language in which the user interface is displayed by either creating a new configuration file, or modifying the existing configuration file.


### Creating a New Configuration File

1. Create a new XML file with the following contents:

	```xml
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">

		<import resource="classpath:xap-webui-context.xml" />

		<bean id="localeConf" class="com.gigaspaces.admin.webui.shared.beans.LocaleConf">
			<property name="name" value="LOCALE"/>
		</bean>

	</beans>
	```

1. Replace the `LOCALE` value with the required language code (`zh_CN`).
1. Save the file under **XAP_HOME &rarr; config**.
1. Pass this file as a system property to the Web Management Console launcher script, as follows:

{{%tabs%}}
{{%tab "  Linux "%}}


```bash
# specify the locale context location

export WEBUI_JAVA_OPTIONS=-Dcom.gs.webui.context=classpath:config/locale.xml

# launch the web UI
bin/gs-webui.sh
```

{{% /tab %}}
{{%tab "  Windows "%}}


```bash
:: specify the locale context location
set WEBUI_JAVA_OPTIONS=-Dcom.gs.webui.context=classpath:config/locale.xml

:: launch the web UI
bin\gs-webui.bat
```

{{% /tab %}}
{{% /tabs %}}

### Updating the Existing Configuration File

1. Open the **xap-webui-[version-build].war** archive (located under `[XAP_HOME]/tools/gs-webui`) for exploring and navigate to `/WEB-INF/lib`.
1. Edit the **web-context.xml** file to add the `localeConf` bean with the desired language (`zh_CN` for Chinese or `en` for English).

	```xml
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">
	
		<import resource="classpath:webui-context.xml" />

		...

		<bean id="localeConf" class="com.gigaspaces.admin.webui.shared.beans.LocaleConf">
			<property name="name" value="zh_CN"/>
		</bean>

	</beans>
	```

## Specifying a Custom Port

To override the default port, you can either use the `org.openspaces.launcher.port` system property (by defining WEBUI_PORT variable), or specify `-port <listen port>` as an argument to the `gs-webui` script. Here is an example on how it's done (starting to listen on port 80):

{{%tabs%}}
{{%tab "  Linux "%}}


```bash
#Specify port via a command line argument
./gs-webui.sh -port 80

#Specify port with a system property
export WEBUI_PORT=80
./gs-webui.sh
```

{{% /tab %}}
{{%tab "  Windows "%}}


```bash
#Specify port via a command line argument
gs-webui.bat -port 80

#Specify port with a system property
set WEBUI_PORT=80
gs-webui.bat
```

{{% /tab %}}
{{% /tabs %}}

## Binding to a Specific Host

To bind to specific host, you can either use the `org.openspaces.launcher.bind-address` system property (by defining BIND_ADDRESS variable), or specify `-bind-address <myhost>` as an argument to the `gs-webui` script.
Default used bind address is 0.0.0.0 .
Here is an example on how it's done (starting on host 192.168.10.1):

{{%tabs%}}
{{%tab "  Linux "%}}


```bash
#Specify bind address via a command line argument
./gs-webui.sh -bind-address 192.168.10.1

#Specify bind address with a system property
export BIND_ADDRESS=192.168.10.1
./gs-webui.sh
```

{{% /tab %}}
{{%tab "  Windows "%}}


```bash
#Specify bind address via a command line argument
gs-webui.bat -bind-address 192.168.10.1

#Specify bind address with a system property
set BIND_ADDRESS=192.168.10.1
gs-webui.bat
```

{{% /tab %}}
{{% /tabs %}}

