---
type: postsbp
title:  WAN Gateway Pass Through Replication
categories: SBP
parent: wan-based-deployment.html
weight: 300
---

{{% ssummary page %}}WAN Pass Through replication example{{% /ssummary %}}

{{% tip %}}
**Author**: Ali Hodroj, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0.7<br/>


{{% /tip %}}

# Overview

The WAN Gateway allows for the implementation of a pass-through replication topology across clusters of space instances. In this architecture, a site may act as an intermediary for delegating replication requests across two or more other sites. For instance, given three clusters in New York, London, and Hong Kong, there might be bandwidth or connectivity issues between Hong Kong and New York, thereby requiring London to be used as a pass through site. Such replication behavior may be specified through the use of the indirect delegation feature. The sample processing units and configuration provided below are intended as an example of implementing a pass through topology across three sites: New York (US), London (GB), and Hong Kong (HK) where each site has an independent cluster and a Gateway.

![WAN_passthrough.jpg](/attachment_files/sbp/WAN_passthrough.jpg)

The demo is configured to start three space instances across three clusters. While the three clusters run on your local machine, they are demarcated by zones and different lookup service ports as follows:


|Gateway/Space|Zone|Lookup Service Port|
|:------------|:--:|:-----------------:|
|wan-gateway-HK|HK|4166|
|wan-space-HK|HK|4166|
|wan-gateway-US|US|4266|
|wan-space-US|US|4266|
|wan-gateway-GB|GB|4366|
|wan-space-GB|GB|4366|

The internal architecture of the setup includes a clustered space and a Gateway, where each Gateway includes a Delegator and a Sink (click the thumbnail to enlarge):


[<img src="/attachment_files/sbp/WAN_passthrough_arch.jpg" width="140" height="100">](/attachment_files/sbp/WAN_passthrough_arch.jpg)

As a result of indirect delegation, the following scenario will take place once updates are written to the New York space:

1.	All updates performed on the New York cluster are sent to local delegator
2.	Delegator directs Hong Kong traffic to London
3.	London sink will in turn push the updates to Hong Kong sink
4.	Hong Kong sink applies the updates on the local cluster

# Configuring Indirect Delegation

The pass-through topology configuration is implemented through delegators across Hong-Kong and New York that are routed via London. This is achieved by setting the delegate-through property to London for delegation targets in Hong-Kong and New York. Notice that the in the HK and US do not need to have gateway lookups against each other in their configuration:

{{%tabs%}}

{{%tab "  New York Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

	<!-- Enable support for @Polling annotation -->
	<os-events:annotation-support />

	<os-core:embedded-space id="space" name="wanSpaceUS" gateway-targets="gatewayTargets" />
	<os-core:giga-space id="gigaSpace" space="space" />

	<os-core:giga-space-context />

	<os-gateway:targets id="gatewayTargets"		local-gateway-name="US">
		<os-gateway:target name="GB" />
		<os-gateway:target name="HK" />
	</os-gateway:targets>
</beans>
```

{{% /tab %}}

{{%tab "  New York Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/core/gateway
       http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:delegator id="delegator" local-gateway-name="US" gateway-lookups="gatewayLookups">
        <!-- <os-gateway:delegation target="DE"/> -->
        <os-gateway:delegation target="HK" delegate-through="GB" />
    </os-gateway:delegator>

    <os-gateway:sink id="sink" local-gateway-name="US" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceUS">
        <os-gateway:sources>
            <os-gateway:source name="HK"/>
            <os-gateway:source name="GB"/>
        </os-gateway:sources>
    </os-gateway:sink>

    <os-gateway:lookups id="gatewayLookups">
        <os-gateway:lookup gateway-name="US" host="localhost" discovery-port="10768" communication-port="7000"/>
        <os-gateway:lookup gateway-name="GB" host="localhost" discovery-port="10769" communication-port="8000"/>
    </os-gateway:lookups>

</beans>

```

{{% /tab %}}

{{%tab "  London Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

	<!-- Enable support for @Polling annotation -->
	<os-events:annotation-support />

	<os-core:embedded-space id="space" name="wanSpaceGB" gateway-targets="gatewayTargets" />
	<os-core:giga-space id="gigaSpace" space="space" />

	<os-core:giga-space-context />

	<os-gateway:targets id="gatewayTargets"		local-gateway-name="GB">
		<os-gateway:target name="US"/>
		<os-gateway:target name="HK"/>
	</os-gateway:targets>
</beans>
```

{{% /tab %}}

{{%tab "  London Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/core/gateway
       http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:delegator id="delegator" local-gateway-name="GB" gateway-lookups="gatewayLookups">
        <os-gateway:delegation target="US"/>
        <os-gateway:delegation target="HK"/>
    </os-gateway:delegator>

    <os-gateway:sink id="sink" local-gateway-name="GB" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceGB">
        <os-gateway:sources>
            <os-gateway:source name="US"/>
            <os-gateway:source name="HK"/>
        </os-gateway:sources>
    </os-gateway:sink>

    <os-gateway:lookups id="gatewayLookups">
        <os-gateway:lookup gateway-name="US" host="localhost" discovery-port="10768" communication-port="7000"/>
        <os-gateway:lookup gateway-name="GB" host="localhost" discovery-port="10769" communication-port="8000"/>
        <os-gateway:lookup gateway-name="HK" host="localhost" discovery-port="10770" communication-port="9000"/>
    </os-gateway:lookups>

</beans>

```

{{% /tab %}}

{{%tab "  Hong Kong Space "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

	<!-- Enable support for @Polling annotation -->
	<os-events:annotation-support />

	<os-core:embedded-space id="space" name="wanSpaceHK" gateway-targets="gatewayTargets" />
	<os-core:giga-space id="gigaSpace" space="space" />

	<os-core:giga-space-context />
	<os-remoting:annotation-support />

	<os-gateway:targets id="gatewayTargets"		local-gateway-name="HK">
		<os-gateway:target name="US" />
		<os-gateway:target name="GB" />
	</os-gateway:targets>
</beans>
```

{{% /tab %}}

{{%tab "  Hong Kong Gateway "%}}


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/core/gateway
       http://www.openspaces.org/schema/{{%latestxaprelease%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:delegator id="delegator" local-gateway-name="HK" gateway-lookups="gatewayLookups">
        <os-gateway:delegation target="US" delegate-through="GB" />
    </os-gateway:delegator>

    <os-gateway:sink id="sink" local-gateway-name="HK" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceHK">
        <os-gateway:sources>
            <os-gateway:source name="US"/>
        </os-gateway:sources>
    </os-gateway:sink>

    <os-gateway:lookups id="gatewayLookups">
        <os-gateway:lookup gateway-name="GB" host="localhost" discovery-port="10769" communication-port="8000"/>
        <os-gateway:lookup gateway-name="HK" host="localhost" discovery-port="10770" communication-port="9000"/>
    </os-gateway:lookups>

</beans>
```

{{% /tab %}}

{{% /tabs %}}

# Installing and Running the Example

1. Download the [WAN-Gateway-Examples.zip](https://github.com/Gigaspaces/wan-gateway-samples/archive/master.zip). It includes two folders: **deploy** and **scripts**. View on [GitHub](https://github.com/Gigaspaces/wan-gateway-examples/tree/master/WAN_Replication_PassThrough)
2. Please extract the file and and copy the content of the **deploy** folder into `\<GIGASPACES_HOME>\deploy` folder.
3. Extract the `scripts` folder to an arbitrary location and edit the `setExampleEnv.bat/sh` script to include correct values for `NIC_ADDR` as the machine IP and `GS_HOME` as the GigaSpaces root folder location.

The `scripts` folder contains the necessary scripts to start the [Grid Service Agent](/product_overview/service-grid.html#gsa) for each cluster, in addition to a deploy script `deployAll.bat/sh` which will be used to automate the deployment of all three gateways and space instances. This will allow you to run the entire setup on one machine to simplify testing. Here are the steps to run the example:

1. Run `startAgent-GB.bat/sh` or to start GB site.
2. Run `startAgent-HK.bat/sh` to start HK site.
3. Run `startAgent-US.bat/sh` to start US site.
4. Run `deployAll.bat/sh` file to deploy all the processing units listed above.

# Viewing the Clusters

- Start the GigaSpaces Management Center and configure the appropriate lookup groups through the "Group Management" dialog.
- Once all clusters are up and running, you will need to enable the relative groups:<br/>
![group_management_dialog.jpg](/attachment_files/sbp/group_management_dialog.jpg)<br/>
Check to enable all three advertised groups for each site:<br/>
![groups_selection_dialog.jpg](/attachment_files/sbp/groups_selection_dialog.jpg)<br/>
As a result, you should see the service grid components for each site displayed under the "Hosts" tree as follows:<br/>
[<img src="/attachment_files/sbp/service_grid.jpg" width="140" height="100">](/attachment_files/sbp/service_grid.jpg)

Once The deployAll.bat/sh script finishes running, you should be able to see all three sites deployed as follows:<br/>
[<img src="/attachment_files/sbp/pu_deployments.jpg" width="140" height="100">](/attachment_files/sbp/pu_deployments.jpg)


# Testing Pass Through Replication
You can test the setup by using the [benchmark utility]({{%latestadmurl%}}/benchmark-browser.html) comes with the GS-UI. Select one of the HK or US Benchmark icons and click Start to begin writing objects to the space:
[<img src="/attachment_files/sbp/space_write.jpg" width="140" height="100">](/attachment_files/sbp/space_write.jpg)



Click the Spaces icon on the Space Browser Tab to get a global view of all spaces. As objects are being written, you should see replication occurring across both HK and US sites until there are 5000 objects in each space. Notice that since the GB site is a pass through, the object count should remain zero:
[<img src="/attachment_files/sbp/space_object_count.jpg" width="140" height="100">](/attachment_files/sbp/space_object_count.jpg)



You can also utilize the Take operation and click Start to remove objects from either the HK or US space. As a result, you will see the object count reaching zero across both HK and US as the pass through replication is taking place:
[<img src="/attachment_files/sbp/object_count_zero.jpg" width="140" height="100">](/attachment_files/sbp/object_count_zero.jpg)



