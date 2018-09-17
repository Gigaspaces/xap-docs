---
type: post97
title:  Monitoring Client Side Cache
categories: XAP97
parent: client-side-caching.html
weight: 400
---




The Local View/Cache JMX Monitor monitors the number of objects stored within the client side cache and the activities performed with it. You can use [JConsole](http://docs.oracle.com/javase/1.5.0/docs/guide/management/jconsole.html) to graph the number of objects within the local view/cache and other exposed statistics while the application is running. The Local View/Cache JMX Monitor exposing the following statistics:

- Object Count
- Read Count, Take Count, Write Count, Update Count
- Read TP, Take TP, Write TP, Update TP

# Using the Local View/Cache JMX Monitor

To use the Local View/Cache JMX Monitor:

1. Download the [GSClientCacheJMXMonitorXAP9.jar](/download_files/GSClientCacheJMXMonitorXAP9.jar) and add it into your application classpath.
2. Add into your local view/cache configuration the `space-config.filters.Statistics.enabled` parameter as demonstrated below.
3. Add into your application Spring file the `gsClientCacheMonitor` bean. The following example assumes you have an application using two master spaces, each has its own client local cache proxy. See how the `GigaSpacesClientCacheJMXMonitor` is configured to specify the different proxies used by the application:


```java
<os-core:space id="space" url="jini://*/*/mySpace" />

<os-core:local-cache id="localCacheSpace" space="space">
    <os-core:properties>
        <props>
            <prop key="space-config.filters.Statistics.enabled">true</prop>
        </props>
    </os-core:properties>
</os-core:local-cache>

<os-core:giga-space id="gigaSpace1" space="localCacheSpace"/>

<os-core:space id="space2" url="jini://*/*/mySpace2" />

<os-core:local-cache id="localCacheSpace2" space="space2">
    <os-core:properties>
        <props>
            <prop key="space-config.filters.Statistics.enabled">true</prop>
        </props>
    </os-core:properties>
</os-core:local-cache>
<os-core:giga-space id="gigaSpace2" space="localCacheSpace2"/>

<bean id="gsClientCacheMonitor"
	class="com.gigaspaces.clientcachemonitor.GigaSpacesClientCacheJMXMonitor">
	<property name="gigaSpaceList" >
    	 <list>
           <ref bean="gigaSpace1"/>
           <ref bean="gigaSpace2"/>
         </list>
	</property>
</bean>
```

You can specify up to 5 local View/Cache `GigaSpace` beans as part of the `gigaSpaceList`.

4. Start JConsole for your application JVM, move into the MBean Tab and select the `GigaSpacesClientCacheJMXMonitor` under the `com.gigaspaces.clientcachemonitor`.
5. Click the relevant statistics you would like to monitor, and Double click the cell on the right side panel. you may monitor the entire statistics by clicking on the Attributes tree icon and double clicking on each value at the left side panel.
6. A graph (or multiple graphs) should be presented illustrating the value of the monitored statistic(s). The graph(s) will be updated periodically.
See below example how you can monitor multiple local cache instances running within the same application:

{{% indent %}}
{{%popup   "/attachment_files/clientCacheJMXMonitor.jpg"%}}
{{% /indent %}}

{{% note %}}
This version of the local view/cache monitor does not reflect statistics for operations using the `readByID` operation from the local view/cache.
{{%/note%}}


