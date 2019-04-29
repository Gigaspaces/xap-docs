---
type: post
title:  WAN Gateway Master-Slave Replication
categories: SBP
parent: wan-based-deployment.html
weight: 200
---

|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Ali Hodroj| 9.6 | July 2013|    |    |



# Overview

The WAN gateway provides a simple way of creating a master-slave topology, enabling data from one site to be replicated to one or more remote sites. For instance, given three clusters in New York, London, and Hong Kong, with New York being the master and the remaining two acting as slaves, any updates to the New York Space will propagate to both London and Hong Kong asynchronously. The sample Processing Units and configuration provided below are intended as an example of implementing a single-master/multi-slave topology across three sites: New York (US), London (GB), and Hong Kong (HK) where each site has an independent cluster and a gateway.
![WAN_masterslave.png](/attachment_files/sbp/WAN_masterslave.png)

The demo is configured to start three Space instances across three clusters. While the three clusters run on your local machine, they are demarcated by zones and different lookup service ports as follows:


| Gateway/Space | Zone | Lookup Service Port |
|:--------------|:----:|:-------------------:|
| wan-gateway-HK | HK | 4166 |
| wan-space-HK | HK | 4166 |
| wan-gateway-US | US | 4266 |
| wan-space-US | US | 4266 |
| wan-gateway-GB | GB | 4366 |
| wan-space-GB | GB | 4366 |

The internal architecture of the setup includes a clustered Space and a gateway, such that the master site (US) only configures delegators while the slave sites (GB, HK) only configure sinks (click the thumbnail to enlarge):

[<img src="/attachment_files/sbp/WAN_masterslave_arch.png" width="140" height="100">](/attachment_files/sbp/WAN_masterslave_arch.png)


As a result of this topology, the following scenario will take place after updates are written to the New York Space:

1.	All updates performed on the New York cluster are sent to local delegators for London and Hong Kong.
2.	London and Hong Kong sinks receive the updates asynchronously.
3.	London and Hong Kong sinks each apply the updates on their local cluster.

# Configuring Master-Slave Replication

The master-slave topology configuration is simply implemented through delegators on the master (New York) and a sink on each slave (London, Hong Kong). In this case, New York is the active site while London and
Hong Kong are the passive sites. While the slave sites are passive, this does not necessarily mean that no work is done in these sites. However, in  terms of replication over the WAN, these sites should not replicate to the other sites and usually should not alter data replicated from other sites because it may cause conflicts:

{{%tabs%}}

{{%tab "New York Space"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

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
       http://www.openspaces.org/schema/{{%currentversion%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:delegator id="delegator" local-gateway-name="US" gateway-lookups="gatewayLookups">
        <os-gateway:delegation target="GB" />
        <os-gateway:delegation target="HK" />
    </os-gateway:delegator>

    <os-gateway:lookups id="gatewayLookups">
        <os-gateway:lookup gateway-name="US" host="localhost" discovery-port="10768" communication-port="7000"/>
        <os-gateway:lookup gateway-name="GB" host="localhost" discovery-port="10769" communication-port="8000"/>
        <os-gateway:lookup gateway-name="HK" host="localhost" discovery-port="10770" communication-port="9000"/>
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
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

	<os-events:annotation-support />

	<os-core:embedded-space id="space" name="wanSpaceGB" />
	<os-core:giga-space id="gigaSpace" space="space" />

	<os-core:giga-space-context />

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
       http://www.openspaces.org/schema/{{%currentversion%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:sink id="sink" local-gateway-name="GB" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceGB">
        <os-gateway:sources>
            <os-gateway:source name="US"/>
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
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/openspaces-core.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/events/openspaces-events.xsd
       http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/<MadCap:variable name="Versions.product-version-short" />/core/gateway/openspaces-gateway.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/remoting/openspaces-remoting.xsd">
	<context:annotation-config></context:annotation-config>
	<tx:annotation-driven transaction-manager="transactionManager" />

	<os-core:distributed-tx-manager id="transactionManager" />

	<os-events:annotation-support />

	<os-core:embedded-space id="space" name="wanSpaceHK" />
	<os-core:giga-space id="gigaSpace" space="space" />

	<os-core:giga-space-context />
	<os-remoting:annotation-support />

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
       http://www.openspaces.org/schema/{{%currentversion%}}/core/gateway/openspaces-gateway.xsd">

    <os-gateway:sink id="sink" local-gateway-name="HK" gateway-lookups="gatewayLookups"
                     local-space-url="jini://*/*/wanSpaceHK">
        <os-gateway:sources>
            <os-gateway:source name="US"/>
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

{{% /tabs %}}

# Installing and Running the Example

1. Download the [WAN-Gateway-Examples.zip](https://github.com/Gigaspaces/wan-gateway-examples/archive/master.zip) archive. It includes two folders: **deploy** and **scripts**. View on [GitHub](https://github.com/Gigaspaces/wan-gateway-examples/tree/master/WAN_Replication_MasterSlave)
2. Extract the file and and copy the contents of the **deploy** folder to the `\<GIGASPACES_HOME>\deploy` folder.
3. Extract the `scripts` folder to an arbitrary location and edit the `setExampleEnv.bat/sh` script to include the machine IP address for `NIC_ADDR` and the GigaSpaces root folder location for `GS_HOME`.

The `scripts` folder contains the necessary scripts to start the [Grid Service Agent](/product_overview/service-grid.html#gsa) for each cluster, in addition to a deploy script `deployAll.bat/sh` that is used to automate the deployment of all three gateways and sSpace instances. This allows you to run the entire setup on one machine to simplify testing. Here are the steps to run the example:

1. Run `startAgent-GB.bat/sh` to start the GB site.
2. Run `startAgent-HK.bat/sh` to start the HK site.
3. Run `startAgent-US.bat/sh` to start the US site.
4. Run `deployAll.bat/sh` file to deploy all the Processing Units listed above.

# Viewing the Clusters

- Start the GigaSpaces Management Center and configure the appropriate lookup groups through the "Group Management" dialog.
- After all clusters are up and running, you must enable the relative groups:

![group_management_dialog.jpg](/attachment_files/sbp/group_management_dialog.jpg)

Check to enable all three advertised groups for each site:

![groups_selection_dialog.jpg](/attachment_files/sbp/groups_selection_dialog.jpg)

As a result, you should see the service grid components for each site displayed under the **Hosts** tree as follows:

[<img src="/attachment_files/sbp/masterslave_hosts_view.png" width="140" height="100">](/attachment_files/sbp/masterslave_hosts_view.png)


After The `deployAll.bat/sh` script finishes running, you should be able to see all three sites deployed as follows:

[<img src="/attachment_files/sbp/pu_deployments.jpg" width="140" height="100">](/attachment_files/sbp/pu_deployments.jpg)

If you are using the Web Management Console, you can also view the site topology using the **Data Grids**&gt;**Gateways** view as follows:

[<img src="/attachment_files/sbp/webui_gw_topology.png" width="140" height="100">](/attachment_files/sbp/webui_gw_topology.png)

# Testing Master-Slave Replication

You can test the setup using the [benchmark utility](https://docs.gigaspaces.com/latest/admin/benchmark-browser.html) that comes with the GigaSpaces Management Center. Select the US Benchmark icons and click **Start** to begin writing objects to the Space:

[<img src="/attachment_files/sbp/masterslave_space_write.png" width="140" height="100">](/attachment_files/sbp/masterslave_space_write.png)

Click the **Spaces** icon on the Space Browser tab to get a global view of all Spaces. As objects are written, you should see replication occurring across both the HK and GB sites until there are 5000 objects in each Space:

[<img src="/attachment_files/sbp/masterslave_space_count.png" width="140" height="100">](/attachment_files/sbp/masterslave_space_count.png)


