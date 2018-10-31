---
type: post140
title:  Customizing the Configuration Options
categories: XAP140ADM,PRM
parent: web-management-console.html
weight: 100
---


 

The Web Management Console has an out-of-the-box default configuration that can be used with any standard environment. However, when preparing for deployment, you may want to customize certain configuration options to suit your specific organization needs. 

# Localizing the User Interface

In addition to the default English, the Web Management Console supports localization to Chinese. You can define the language in which the user interface is displayed by either creating a new configuration file, or modifying the existing configuration file.


## Creating a New Configuration File

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

## Updating the Existing Configuration File

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

# Specifying a Custom Port

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

# Binding to a Specific Host

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


