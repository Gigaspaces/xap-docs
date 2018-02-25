---
type: post123
title:  Web Management Console
categories: XAP123ADM, PRM
parent: none
weight: 200
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

## Securing the Web Dashboard

If you configured your XAP instance to run in secure mode using the `-Dcom.gs.security.enabled=true` property, you will see the following login window:

{{% align center %}}
![xap-login-inline.png](/attachment_files/web-console/login-12.3.png)
{{%/align%}}

The Lookup groups and Locators are loaded from the `setenv/setenv-overrides` script. 

{{%refer%}}
For more information on how to secure the grid services, refer to [Securing Grid Services](../security/securing-the-grid-services.html).
Lookup groups and Lookup Locators can be configured in the configuration files described in [Configure Lookup Groups and Locators](../started/common-environment-variables.html#extension).
{{%/refer%}}

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