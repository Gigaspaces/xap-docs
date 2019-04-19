---
type: post123
title:  XAP Over a Firewall
categories: XAP123ADM, PRM
parent: network.html
weight: 600
canonical: auto
---
 

In many scenarios, you need to set up XAP in environments which have a firewall running. This section&nbsp;provides&nbsp;XAPs configuration recommendations for several firewall topologies:

- Basic configuration: XAP cluster (GSM and GSCs) running behind the firewall, with clients connecting through the firewall. Multicast traffic is allowed behind the firewall and unicast-only traffic comes from the clients through the firewall.

 {{% note %}}
 Basic configuration, suitable for most scenarios, requires mandatory setting of the following properties (these system property settings are described below):
 
 - `com.gs.multicast.discoveryPort`
 - `com.gigaspaces.start.httpPort`
 - `com.gs.transport_protocol.lrmi.bind-port`
 - `com.sun.jini.reggie.initialUnicastDiscoveryPort`
 
 {{%/note%}}

- Same topology as above: All cluster components and clients communicate over unicast only. **Multicast traffic is prohibited**.
- The firewall divides the XAP cluster into zones. Some components (GSCs, GSM) run in one firewall zone, while the rest of the components run in another firewall zone(s). Only unicast traffic is allowed between firewall zones.

To learn more about XAP port usage, refer to [How to Control the Used Ports](./network-ports.html). 

# XAP Firewall Settings

**To enable all XAP components to work over a firewall and control all network activity through explicitly defined static ports:**

Step 1: All communications traversing the firewall should be switched to Unicast TCP (use XAP Jini unicast lookup locators and set the `-Dcom.gs.multicast.discoveryPort` as described in the next section). Jini Multicast discovery should be disabled.<br>

Step 2: Specific listener ports of system components should be **statically set**.   <br>

Step 3: Necessary listener **port ranges** should be defined per each IP address, where the XAP server components reside.

 
Components such as GSM/Lookup Service, GSC, and Mahalo use a single Webster (HTTPD service) and a single LRMI transport port per component. Accordingly, the same quantity of Webster and LRMI ports should be planned for each IP address where those components reside.

Port ranges should be chosen continuously, as Webster and LRMI port bindings are performed sequentially, beginning from the low port number. Each additional component started on the *same machine* opens a sequentially higher Webster and LRMI port, beginning from the low port in the defined port range.
 

Step 4: **Firewall rules for incoming traffic** should include opening a TCP port for each statically defined XAP component listener, for each IP address where a XAP component is running (excluding the JMX MBean server).<br>

Step 5: **JMX listener ports** that are presented in XAP components and assigned by the RMIRegistry mechanism (the default port range begins at 10098; each component opens the next available port) can remain dynamically assigned and should not be opened in the firewall. JMX connections are dedicated to administrative purposes and can be accessed by monitoring tools behind the firewall. The MBeanServer and the RMI lookup are not available outside the firewall. <br>

Step 6: Each static XAP listener port behind the firewall should be mapped by NAT to the static IP address outside of the firewall. XAP clients/servers residing outside of the firewall should be set to work versus statically mapped by NAT listeners outside IP addresses.<br>

Step 7: **Mandatory** -- the range of port numbers (just free unassigned ports allowed) should be above `1024` and below `65536`.

{{% note "Recommended Port Ranges"%}}
As per IANA standards, port numbers are based on three ranges: System Ports (0-1023), User Ports (1024-49151), and the Dynamic and/or Private Ports (49152-65535). Select your port range from the available user ports or dynamic ports. 
{{%/note%}}

## Listener Ports per XAP Component


| Component | Listeners |
|:----------|:----------|
| GSM | Lookup Service<br>LRMI Transport<br>Webster<br>JMX |
| GSC | LRMI Transport <br>Webster <br>JMX |
| Transaction Manager (Mahalo) | LRMI Transport <br>Webster <br>JMX |

## Required Configuration Modifications

### bin/gs.sh Script Modifications

Add the following system properties to the command line:

- **Optional** -- For unicast-only solutions, use the following system property to **disable the Jini multicast activity**:


```bash
-Dcom.gs.multicast.enabled=false
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.gs.multicast.discoveryPort`, the port used during discovery for both unicast and multicast requests. The default value is `4174`.


```bash
-Dcom.gs.multicast.discoveryPort=
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.sun.jini.reggie.initialUnicastDiscoveryPort`, the port used during unicast discovery. The default value is `0` (undefined).

{{% note %}}
If you leave the unicast discovery port property `com.sun.jini.reggie.initialUnicastDiscoveryPort` unconfigured (with its default value of 0), then it will use the port value in the `com.gs.multicast.discoveryPort` property. 
{{%/note%}}

```bash
-Dcom.sun.jini.reggie.initialUnicastDiscoveryPort=
```

- **Optional** -- `RMIRegistry` port (used for RMI lookup and for JMX MBean server). The default value is `10098`.


```bash
-Dcom.gigaspaces.system.registryPort=
```

- **Mandatory** -- Webster HTTPD service port.


```bash
-Dcom.gigaspaces.start.httpPort=
```

Sample port settings:


```bash
-Dcom.gs.multicast.enabled=false
-Dcom.gs.multicast.discoveryPort=7102
-Dcom.gigaspaces.system.registryPort=7103
-Dcom.gigaspaces.start.httpPort=7104
```

### bin/gs-ui.sh GUI Script Modifications (Optional)

These modifications are only needed when a GUI client should connect through the firewall. Add the following system properties to the command line:

- **Mandatory** -- use the following system property to **disable the Jini multicast activity**. For unicast-only solutions:


```bash
-Dcom.gs.multicast.enabled=false
```

- **Mandatory** -- Reggie Lookup Service: in this context, modify `com.gs.multicast.discoveryPort`, the port used during discovery for both unicast and multicast requests. The default value is `4174`.


```bash
-Dcom.gs.multicast.discoveryPort=
```

- **Mandatory** -- `RMIRegistry` port (used for RMI lookup and for JMX MBean server). The default value is `10098`.


```bash
-Dcom.gigaspaces.system.registryPort=
```

- **Optional** -- Webster HTTPD service port.


```bash
-Dcom.gigaspaces.start.httpPort=
```

Sample port settings:


```bash
-Dcom.gs.multicast.enabled=false
-Dcom.gs.multicast.discoveryPort=7102
-Dcom.gigaspaces.system.registryPort=7103
-Dcom.gigaspaces.start.httpPort=7104
```

### bin/setenv.sh Script Modifications (Mandatory)

Lookup locators unicast discovery port should be the same as defined in `gs.sh`, for example:


```bash
XAP_LOOKUP_LOCATORS=server111:7102; export XAP_LOOKUP_LOCATORS
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



