---
type: postsbp
title:  Scaling Agent
categories: SBP
parent: production.html
weight: 1300
---



{{% ssummary %}}Implementing a dynamic scalable web application with GigaSpaces XAP.{{% /ssummary %}}

**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
Using XAP:**7.0GA**<br/>
JDK:**Sun JDK 1.6**<br/>
Date: September 2009<br/>

# Overview
The [Administration and Monitoring API]({{%latestjavaurl%}}/administration-and-monitoring-api.html) allows you to monitor the application health and its resources to enforce a specific pre-defined configurable SLA that will scale the application while it is running. This ensures deterministic response time when there is increasing amount of users accessing the system and the high-availability and robustness of the application.
![scaling_agent.jpg](/attachment_files/sbp/scaling_agent.jpg)
The following example illustrates how you can construct a simple processing unit (**The Scaling Agent**) to monitor a web application deployed into GigaSpaces and track the web requests routed to the web application. Once the total average amount of the HTTP requests served by the current running web application instances breach a pre-defined upper or lower limit, the scaling agent will react and perform the necessary activities to scale the web application tier (add or remove instances).

The activities to scale up the application (add more instances) could be starting a new GSC on remote machines and starting additional web application instances. In the same manner the scaling agent can scale down the application to terminate running GSCs and reduce the amount of web the application instances.

{{% tip %}}
See the [Mule ESB Example](./mule-esb-example.html#Scale Dynamically) for an advanced usage of the Administration and Monitoring API
{{% /tip %}}

# How the Scaling Agent works?
The scaling agent sample periodically the amount of HTTP requests served by the running web application instances and compares the total amount of recent requests with the current ones (`getAverageRequests`). If the average amount of requests is larger than the maximum amount of Requests Per Instance threshold (which has been pre-defined as part of the processing unit configuration) the scaling agent scales up the application by starting a new GSC and increasing the amount of instances (see the `scaleUp()` method).

{{% tip %}}
The [WebScale.zip](/attachment_files/sbp/WebScale.zip) includes the source and configuration described below.
{{% /tip %}}

## The Scaling Agent Implementation


```java
package com.gigaspaces.examples.webscale;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsa.GridServiceContainerOptions;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebMonitor implements InitializingBean, DisposableBean, Runnable {

    private String processingUnitName;
    private long maxRequestPerInstance;
    private ScheduledExecutorService executorService;
    private ProcessingUnit pu;
    private Admin admin;
    private long scaleTimestamp = System.currentTimeMillis();

    public void setProcessingUnitName(String processingUnitName) {
        this.processingUnitName = processingUnitName;
    }

    public void setMaxRequestPerInstance(long maxRequestPerInstance) {
        this.maxRequestPerInstance = maxRequestPerInstance;
    }

    public void afterPropertiesSet() throws Exception {
        admin = new AdminFactory().createAdmin();
        admin.getLookupServices().waitFor(1,10, TimeUnit.SECONDS);
        System.out.println(admin.getLookupServices().getLookupServices().length);
        pu = admin.getProcessingUnits().waitFor(processingUnitName, 30000L, TimeUnit.SECONDS);
        System.out.println("PU is " + pu);
        if (pu != null) {
            executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleWithFixedDelay(this, 1, 1, TimeUnit.SECONDS);
        }
    }

    public void run() {
        long averageRequests = getAverageRequests();
        if (averageRequests > maxRequestPerInstance) {
            scaleUp();
        }

        // if there is nothing going on since it scale up/down for a minute - scale down
        if (System.currentTimeMillis() - scaleTimestamp > 60000)
        {
	        if (averageRequests==0) {
	            scaleDown();
	            scaleTimestamp = System.currentTimeMillis();
	        }
        }
    }

    private void scaleUp() {
        System.out.println("Scaling up...");
        pu.incrementInstance();
        try {
            Thread.sleep(10000L);
            scaleTimestamp = System.currentTimeMillis();
        } catch (InterruptedException e) {}

    }

    private void scaleDown() {

    	if (pu.getInstances().length == 1) return;

        System.out.println("Scaling down...");
        pu.decrementInstance();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {}
    }

    public void destroy() throws Exception {
        executorService.shutdownNow();
        admin.close();
    }

    public long getAverageRequests() {
        long totalRequests = 0;
        long previousTotalRequests = 0;
        for (ProcessingUnitInstance instance : pu) {
            instance.getStatistics().getPrevious();
        }
        for (ProcessingUnitInstance instance : pu) {
            if (instance.getStatistics().getPrevious() == null) {
                return 0;
            }
            totalRequests += instance.getStatistics().getWebRequests().getTotal();
            previousTotalRequests += instance.getStatistics().getPrevious().getWebRequests().getTotal();
        }
        long averageRequests = ((totalRequests - previousTotalRequests)/ pu.getTotalNumberOfInstances());
        System.out.println("Average [" + averageRequests + "],
Total [" + totalRequests + "] Previous Total [" + previousTotalRequests + "]");
        return averageRequests;
    }
}
```

## The Scaling Agent PU Configuration


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:lang="http://www.springframework.org/schema/lang"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang.xsd">

   <bean id="scalingAgent" class="com.gigaspaces.examples.webscale.WebMonitor">
       <property name="processingUnitName" value="myWebApplication"/>
       <property name="maxRequestPerInstance" value="300"/>
   </bean>

</beans>
```

