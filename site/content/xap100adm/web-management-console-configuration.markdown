---
type: post100
title:  Configuration
categories: XAP100ADM
parent: web-management-console.html
weight: 100
---





The management console allows for alternative locales which can be configured via XML. Currently, the supported locales
are Chinese and English (which is the default). Users wishing to change the locale can do so in two ways:


### Using a new configuration file

* Create a new file with the following contents:


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
spring
    http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">

    <import resource="classpath:xap-webui-context.xml" />

    <bean id="localeConf" class="com.gigaspaces.admin.webui.shared.beans.LocaleConf">
        <property name="name" value="LOCALE"/>
    </bean>

</beans>
```

* Replace `LOCALE` with a locale string (e.g. `zh_CN`).

* Save the file under *XAP_HOME &rarr; config*.

* Pass this file as a system property to the web UI launcher script, as follows:

{{%tabs%}}
{{%tab "  Linux "%}}


```console
# specify the locale context location
export WEBUI_JAVA_OPTIONS=-Dcom.gs.webui.context=classpath:config/locale.xml

# launch the web UI
bin/gs-webui.sh
```

{{% /tab %}}
{{%tab "  Windows "%}}


```console
:: specify the locale context location
set WEBUI_JAVA_OPTIONS=-Dcom.gs.webui.context=classpath:config/locale.xml

:: launch the web UI
bin\gs-webui.bat
```

{{% /tab %}}
{{% /tabs %}}



### Update the existing configuration file

* Open the *gs-webui.war* archive (found under `[XAP_HOME]/tools/gs-webui`) for exploring and navigate to */WEB-INF/lib*.
Open the *gs-webui-[version-build].jar* archive for exploring.

* Edit *xap-webui-context.xml* to add the `localeConf` bean with the desired locale string (`zh_CN` for Chinese and
`en` for English):


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
spring
    http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd">

    <import resource="classpath:webui-context.xml" />

    ...

    <bean id="localeConf" class="com.gigaspaces.admin.webui.shared.beans.LocaleConf">
        <property name="name" value="zh_CN"/>
    </bean>

</beans>
```
