---
type: post97
title:  XAP Over a Firewall
categories: XAP97ADM
parent: network.html
weight: 600
---

{{% ssummary   %}} {{% /ssummary %}}

In many scenarios, you need to set up GigaSpaces in environments which have a firewall running. This section&nbsp;provides&nbsp;GigaSpaces configuration recommendations for several firewall topologies:

- Basic configuration: GigaSpaces cluster (GSM and GSCs) running behind the firewall, with clients connecting through the firewall. Multicast traffic is allowed behind the firewall and unicast-only traffic comes from the clients through the firewall.

{{% note %}}
**Basic configuration, suitable for most scenarios, requires mandatory setting of the following properties** (these system property settings are described below):
    - `com.gs.multicast.discoveryPort`
    - `com.gigaspaces.start.httpPort`
    - `com.gs.transport_protocol.lrmi.bind-port`
    - `com.sun.jini.reggie.initialUnicastDiscoveryPort`.
{{%/note%}}

- Same topology as above: All cluster components and clients communicate over **unicast only. Multicast traffic is prohibited**.
- The firewall divides GigaSpaces cluster into zones. Some components (GSCs, GSM) are running in one firewall zone, while the rest of the components are running in another firewall zone/s. Only unicast traffic is allowed between firewall zones.

{{% refer %}}To learn more about GigaSpaces port usage, refer to [How to Control the Used Ports](./network-ports.html).{{% /refer %}}

# GigaSpaces Firewall Settings


**To enable all GigaSpaces components to work over a firewall and control all network activity through explicitly defined static ports, perform the following configuration steps**:

1. All communications traversing the firewall should be switched to Unicast TCP (use GigaSpaces Jini unicast lookup locators and set the -Dcom.gs.multicast.discoveryPort as described in the next section), Jini Multicast discovery should be disabled.
1. Specific listener ports of system components should be **statically set**.
1. Necessary listener **port ranges** should be defined per each IP address, where the GigaSpaces server components reside.

{{% warning %}}
Components such as GSM/Lookup Service, GSC, Mahalo use a single Webster (HTTPD service) and a single LRMI transport port per each component. Accordingly, the same quantity of Webster and LRMI ports should be planned per each IP address where those components reside.
{{%/warning%}}

{{% info%}}
Port ranges should be chosen continuously, as Webster and LRMI port bindings are performed **sequentially*, beginning from the low port number -- each additional component started on the *same machine** opens sequentially higher Webster and LRMI ports, beginning from the low port in the defined port range.
{{%/info%}}

1. **Firewall rules for incoming traffic** should include opening TCP port per each statically defined GigaSpaces component listener, for each IP address where GigaSpaces component is running (excluding JMX MBean server).
1. **JMX listener ports** presented in GigaSpaces components and assigned by RMIRegistry mechanism (default port range beginning - 10098; each component opens next available port) can remain dynamically assigned and should not be opened in the firewall. JMX connections are dedicated to administrative purposes and can be accessed by monitoring tools behind the firewall. Of course, MBeanServer and the RMI lookup will not be available outside firewall
1. Each static GigaSpaces listener port behind the firewall should be mapped by NAT to the static IP address outside of the firewall. GigaSpaces clients/servers residing outside of the firewall should be set to work versus statically mapped by NAT listeners outside IP addresses.
1. **Mandatory** -- the range of port numbers (just free unassigned ports allowed) should be above `1024` and below `65536`.

{{% note %}}
**Recommended port ranges** -- above `7100` in free unassigned IANA ranges (`7102`\-`7120`, `7130`\-`7160`, `7167`\-`7173`, `7175`\-`7199`, `7228`\-`7271`, `7282`\-`7299`, `7366`\-`7390`..., `47558`\-`47623`, `47625`\-`47805`, `47809`\-`47999`, `48004`\-`48127`, `48620`\-`49150`)
{{%/note%}}

## Listener Ports per GigaSpaces Component


| Component | Listeners |
|:----------|:----------|
| GSM | Lookup Service{{<wbr>}}LRMI Transport{{<wbr>}}Webster{{<wbr>}}JMX |
| GSC | LRMI Transport {{<wbr>}}Webster {{<wbr>}}JMX |
| Transaction Manager (Mahalo) | LRMI Transport {{<wbr>}}Webster {{<wbr>}}JMX |

## Required Configuration Modifications



### bin/gs.sh Script Modifications

Add the following system properties to the command line:

- **Optional** -- use the following system property to **disable the Jini multicast activity**. For unicast-only solutions:


```bash
-Dcom.gs.multicast.enabled=false
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.gs.multicast.discoveryPort`, the port used during discovery for both unicast and multicast requests. Default value is `4174`.


```bash
-Dcom.gs.multicast.discoveryPort=
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.sun.jini.reggie.initialUnicastDiscoveryPort`, the port used during unicast discovery. Default value is `0` - any available port.


```bash
-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=
```

- **Optional** -- `RMIRegistry` port (used for RMI lookup and for JMX MBean server). Default value is `10098`.


```bash
-Dcom.gigaspaces.system.registryPort=
```

- **Mandatory** -- Webster HTTPD service port.


```bash
-Dcom.gigaspaces.start.httpPort=
```

Settings example:


```bash
-Dcom.gs.multicast.enabled=false
-Dcom.gs.multicast.discoveryPort=7102
-Dcom.gigaspaces.system.registryPort=7103
-Dcom.gigaspaces.start.httpPort=7104
```

### bin/gs-ui.sh GUI Script Modifications -- Optional; needed when GUI client should connect through the firewall

Add the following system properties to the command line:

- **Mandatory** -- use the following system property to **disable the Jini multicast activity**. For unicast-only solutions:


```bash
-Dcom.gs.multicast.enabled=false
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.gs.multicast.discoveryPort`, the port used during discovery for both unicast and multicast requests. Default value is `4174`.


```bash
-Dcom.gs.multicast.discoveryPort=
```

- **Mandatory** -- `RMIRegistry` port (used for RMI lookup and for JMX MBean server). Default value is `10098`.


```bash
-Dcom.gigaspaces.system.registryPort=
```

- **Optional** -- Webster HTTPD service port.


```bash
-Dcom.gigaspaces.start.httpPort=
```

Settings example:


```bash
-Dcom.gs.multicast.enabled=false
-Dcom.gs.multicast.discoveryPort=7102
-Dcom.gigaspaces.system.registryPort=7103
-Dcom.gigaspaces.start.httpPort=7104
```

### bin/setenv.sh Script Modifications -- Mandatory

Lookup locators unicast discovery port should be the same as defined in `gs.sh`, for example:


```bash
LOOKUPLOCATORS=server111:7102; export LOOKUPLOCATORS
```

### LRMI Communication Protocol Port Range Setting Modifications

The `com.gigaspaces.start.httpPort` Webster port number property can be defined by overriding as shown below, or using a system property:


```bash
gsm.sh webster.xml
gsc.sh webster.xml
startJiniTX_Mahalo.sh webster.xml
```

Content of override file for Webster port definitions:


```xml
<overrides>
    <Component Name="com.gigaspaces.start">
        <Parameter Name="httpPort" Value="9099"/>
    </Component>
</overrides>
```



# Blocking Reliable Take

The blocking Take operation (`timeout >0`) with a remote client opens a network connection between the client and the space and also a **reverse** network connection between the space and the client before the object is removed from the space to ensure that the client that initiated the blocking take operation is still active. This ensures that the removed object will be actually consumed by the remote client.

There might be cases where the network would not allow the space to perform network operations from the space process back into the client process - for example send SYN_SENT TCP packets over the network. This could happen when the space running within a non-secured network zone (`DMZ`) that sits behind a firewall and the remote client running within a secured network zone. This would cause the take operation to react very slowly since the space network activity ([LRMI](./tuning-communication-protocol.html)) will timeout after 5 seconds (this is the default settings) without the ability to complete the take operation correctly.

In such a case you could disable the `Blocking Reliable Take` activity by using the `space-config.engine.reliable_take` property. You can control the Take operation to restore the object in case the client was terminated during the time it was in blocking mode by using transactions - this will ensure that the removed object will reappear within the space in case the client was terminated abnormally or failed.

Here is how you can configure the space to disable the Blocking Reliable Take activity:


```xml
<os-core:space id="space" url="/./space">
    <os-core:properties>
        <props>
            <prop key="space-config.engine.reliable_take">false</prop>
        </props>
    </os-core:properties>
</os-core:space>
```



