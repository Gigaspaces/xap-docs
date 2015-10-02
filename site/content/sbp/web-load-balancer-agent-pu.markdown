---
type: postsbp
title:  Web Load Balancer Agent PU
categories: SBP
parent: production.html
weight: 1600
---

{{% ssummary page %}}This best practice illustrates creating a Web Load Balancer Agent as a processing unit{{% /ssummary %}}

{{% tip %}}
**Author:** Jeroen Remmerswaal, Tricode<br/>
**Recently tested with GigaSpaces version**: XAP 7.1.1<br/>
**Last Update:** November 2010<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview

GigaSpaces can host JEE Web Applications in a Grid Service Container using Jetty as a Web Server. Because of GigaSpaces' virtualized infrastructure your application is supposed to be unaware of its location (its host container and machine), or topology (such as the number of instances). This can lead to problems with web applications, for example for a load-balancer to detect where the instances (and how many) are running in the GigaSpaces Containers.

GigaSpaces provides an example in the `tools` folder of the product that shows how to proactively inform an Apache load-balancer about the endpoints so that it can be dynamically refreshed upon change of the topology.

![WebLoadBalancerAgentPU.jpg](/attachment_files/sbp/WebLoadBalancerAgentPU.jpg)

This page describes how to create a Web Load Balancer Agent as a Processing Unit that utilizes the GigaSpaces [Administration and Monitoring API]({{%latestjavaurl%}}/administration-and-monitoring-api.html). This will mean the Web Load Balancer Agent will run within the GigaSpaces service grid, and thus will become fault-tolerant.

Click [here](/download_files/sbp/eAuctionWebLoadBalancer.zip) to download a working code sample that uses the Apache Load Balancer as an example. The example is simple to extend so it can incorporate other load balancers as well.

# Usage

{{%tabs%}}

{{%tab "  Spring Configuration "%}}
Here's an example Processing Unit XML configuration that demonstrates how to configure the Web Load Balancer Agent.  It consists of four parts:
a) The `webLayerLoadBalancerListener`-bean which will use a handle to the b) Admin API to monitor c) a given list of Processing Units, by means of the `monitoredProcessingUnits`-property, to see if a scale-up or scale-down event occurs on. If such an event occurs, it is handed over to a d) Load Balancer Agent for taking the right measurements. You can see this is delegated to the `ApacheLoadBalancerAgent` class by means of the `loadBalancerAgent` property of the `webLayerLoadBalancerListener`-bean.


```java
<beans>
    <bean id="webLayerLoadBalancerListener" class="com.eauction.gigaspaces.loadbalancer.WebLayerLoadBalancerListener">
        <property name="monitoredProcessingUnits" ref="monitoredProcessingUnits" />
        <property name="adminFactory" ref="adminFactory" />
        <property name="loadBalancerAgent" ref="apacheLoadBalancerAgent" />
    </bean>
    <bean id="apacheLoadBalancerAgent" class="com.eauction.gigaspaces.loadbalancer.apache.ApacheLoadBalancerAgent">
        <property name="apacheLocation" value="D:/GigaSpacesTraining/Apache2.2" />
    </bean>
    <bean id="adminFactory" class="org.openspaces.admin.AdminFactory" />
    <bean id="monitoredProcessingUnits" class="java.util.LinkedList">
        <constructor-arg>
            <list>
                <value>eauction-web-gigaspaces</value>
                <value>eauction-web-hibernate</value>
            </list>
        </constructor-arg>
    </bean>
</beans>
```

More details on the `WebLayerLoadBalancerListener` and the `ApacheLoadBalancerAgent` can be found on the next panels.
{{% /tab %}}

{{%tab "  WebLayerLoadBalancerListener "%}}
The `WebLayerLoadBalancerListener` component is capable of monitoring a list of Web Processing Units and will react upon a scale up or scale down event by communicating with a Load Balancer Agent. The main piece of code that accomplishes this can be shown as follows and uses a lifecycle-listener for the Processing Unit component of the Admin API:


```java
public class WebLayerLoadBalancerListener {
    private LoadBalancerAgent loadBalancerAgent;
    private List<String> monitoredProcessingUnits;
    private AdminFactory adminFactory;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        admin = adminFactory.createAdmin();
        admin.addEventListener(new ProcessingUnitInstanceLifecycleEventListener() {
                public void processingUnitInstanceAdded(
                    ProcessingUnitInstance puInstance) {
                    if (monitoredProcessingUnits.contains(puInstance.getName())) {
                        log.info("Processing Unit Instance Added '" +
                            puInstance.getName() + "'");
                        loadBalancer.processingUnitAdded(puInstance);
                    }
                }

                public void processingUnitInstanceRemoved(
                    ProcessingUnitInstance puInstance) {
                    if (monitoredProcessingUnits.contains(puInstance.getName())) {
                        log.info("Processing Unit Instance Removed '" +
                            puInstance.getName() + "'");
                        loadBalancer.processingUnitRemoved(puInstance);
                    }
                }
        });
    }

    @PreDestroy
    public void destroy() throws Exception {
        admin.close();
    }

    ...
}
```

A custom Load Balancer Agent needs to implement this `LoadBalancerAgent` interface:


```java
public interface LoadBalancerAgent {
    void processingUnitAdded(ProcessingUnitInstance puInstance);

    void processingUnitRemoved(ProcessingUnitInstance puInstance);
}
```

{{% /tab %}}

{{%tab "  ApacheLoadBalancerAgent "%}}
As an example an component called `ApacheLoadBalancerAgent` is provided that can communicate with the Apache Load Balancer, in a very similar fashion as the loadbalancer-agent that can be found under the `tools`-folder.

A snippet of the code is provided below for your information, containing the methods that are called when a Processing Unit instance is added or removed.


```java
public class ApacheLoadBalancerAgent implements LoadBalancerAgent, Runnable {
    private String apachectlLocation;
    private String apacheLocation;
    private String configLocation;
    private String restartCommand;
    private int updateInterval = 10;

    public void processingUnitAdded(ProcessingUnitInstance puInstance) {
        ClusterInfo clusterInfo = puInstance.getClusterInfo();
        JeeServiceDetails jeeDetails = puInstance.getJeeDetails();

        log.info("[" + clusterInfo.getName() + "] Adding [" +
            puInstance.getUid() + "] [" + jeeDetails.getHost() + ":" +
            jeeDetails.getPort() + jeeDetails.getContextPath() + "]");

        LoadBalancerInfo loadBalancersInfo = loadBalancersInfoMap.get(clusterInfo.getName());

        if (loadBalancersInfo == null) {
            loadBalancersInfo = new LoadBalancerInfo(clusterInfo.getName());
            loadBalancersInfoMap.put(clusterInfo.getName(), loadBalancersInfo);
        }

        clusterInfoMap.put(puInstance.getUid(), clusterInfo);
        jeeServiceDetailsMap.put(puInstance.getUid(), jeeDetails);
        loadBalancersInfo.putNode(new LoadBalancerNodeInfo(
                puInstance.getUid(), clusterInfo, jeeDetails));
        loadBalancersInfo.setDirty(true);
    }

    public void processingUnitRemoved(ProcessingUnitInstance puInstance) {
        ClusterInfo clusterInfo = puInstance.getClusterInfo();
        JeeServiceDetails jeeServiceDetails = puInstance.getJeeDetails();

        System.out.println("[" + clusterInfo.getName() + "] Removing [" +
            puInstance.getUid() + "] [" + jeeServiceDetails.getHost() + ":" +
            jeeServiceDetails.getPort() + jeeServiceDetails.getContextPath() +
            "]");

        LoadBalancerInfo loadBalancersInfo = loadBalancersInfoMap.get(clusterInfo.getName());

        if (loadBalancersInfo != null) {
            loadBalancersInfo.removeNode(new LoadBalancerNodeInfo(
                    puInstance.getUid(), clusterInfo, jeeServiceDetails));
            loadBalancersInfo.setDirty(true);
        }
    }
```

{{% /tab %}}

{{% /tabs %}}
