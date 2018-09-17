---
type: post121
title:  Configuration
categories: XAP121ADM,PRM
parent: web-management-console.html
weight: 100
---


{{% ssummary %}} {{% /ssummary %}}


The management console allows for alternative locales which can be configured via XML. Currently, the supported locales
are Chinese and English (which is the default). Users wishing to change the locale can do so in two ways:


# Using a new configuration file

* Create a new file with the following contents:


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

* Replace `LOCALE` with a locale string (e.g. `zh_CN`).

* Save the file under *XAP_HOME &rarr; config*.

* Pass this file as a system property to the web UI launcher script, as follows:

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



### Update the existing configuration file

* Open the *xap-webui-[version-build].war* archive (found under `[XAP_HOME]/tools/gs-webui`) for exploring and navigate to */WEB-INF/lib*.


* Edit *web-context.xml* to add the `localeConf` bean with the desired locale string (`zh_CN` for Chinese and
`en` for English):


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

# InfluxDB configuration

* Edit the *metrics.xml* file (found under `[XAP_HOME]/config/metrics`). Change the following part according to your influxDB host(myhost) and database name(mydb) that stores metrics:


```xml

    <grafana>
        <datasources>
            <datasource name="influxdb">
                <property name="type" value="influxdb"/>
                <property name="url" value="http://myhost:8086/db/mydb"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </datasource>
            <datasource name="grafana">
                <property name="type" value="influxdb"/>
                <property name="url" value="http://myhost:8086/db/grafana"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
                <property name="grafanaDB" value="true"/>
            </datasource>
        </datasources>
    </grafana>

```
