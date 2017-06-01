---
type: post102
title:  Lookup Service Configuration
categories: XAP102ADM
parent: network.html
weight: 400
---

{{% ssummary %}}{{% /ssummary %}}

Defining group names and unicast Locators for Jini Services; searching for available lookup services in the network.

# Essential Guidelines

{{% vbar %}}
**It is essential to set the `groups` and `locators` system properties in the Java process which starts the Lookup Service or Mahalo services**. This is required in order to "tell" the Jini service which groups and locators it should join, and which to serve. If these properties are not set, the Lookup Service/Mahalo uses the Jini fallback values and that impact the SLA these services serve with.



**Do not have more than two Jini Lookup Services across one site** - having the `gsInstance` running a Jini Lookup Service embedded by default can cause problems. As a result of not have more than two Jini Lookup Services across one site, spaces do not have to deal with registering themselves into too many Jini Lookup Services, and the Space Browser's freezing behavior is reduced.



**Jini groups vs. locators** - Jini groups are irrelevant when using unicast lookup discovery - they are relevant only when using multicast lookup discovery. If you have multiple spaces with the same name and you are using unicast lookup discovery, you might end up getting the wrong proxy.
{{%/vbar%}}

In such a case, make sure you have a different lookup group for each space, where each space is configured to use a specific lookup. A good practice is to have different space/service names.

{{% refer %}}
When using multicast please review the following section [How to Determine Whether Multicast is Available](./network-multicast-is-available.html) and [How to Configure Multicast](./network-multicast.html).
{{%/refer%}}

# Setting up the Lookup Service For Multicast Discovery (Using Lookup Group)

Each of the Jini Services (such as the Reggie lookup service or the Mahalo Transaction Manager) registers and advertises using a group name which is defined by the following system property (set in the `/bin/setenv.bat file`):

    set LOOKUPGROUPS="gigaspaces-%USERNAME%"
    set LOOKUP_GROUPS_PROP=-Dcom.gs.jini_lus.groups=%LOOKUPGROUPS%

While the `%USERNAME%` will be replaced after installation with the version number e.g.
set `LOOKUPGROUPS="gigaspaces-7.0XAPga"`.

The command that loads a space needs to have this system property set. Another way of doing it instead of setting the `com.gs.jini_lus.groups` system property, is to set the Space URL groups attribute, e.g.:


```java
/./mySpace?schema=cache&groups=myPrivateGroupName
```

{{% refer %}}
If you are looking for a way to use a unicast discovery, please refer to the [How to Configure Unicast Discovery](./network-unicast-discovery.html) section for more details.
{{% /refer %}}

# Multicast Settings

{{% note %}}
To support co-existence of different XAP versions, the defaults below may change between releases.
{{%/note%}}

Adjusting the lookup services multicast settings can be done using the following system properties:


{{% include "/COM/xap100/config-multicast.markdown" %}}

| Property name | Description | Default |
|-----|------|------|
|com.gs.multicast.enabled|Global property allowing you to completely enable or disable multicast in the system.| true|
|com.gs.multicast.announcement|the multicast address that controls the lookup service announcement. The lookup service uses this address to periodically announce its existence. |224.0.1.188|
|com.gs.multicast.request|the multicast address that controls the request of clients (when started) to available lookup services. | 224.0.1.187|
|com.gs.multicast.discoveryPort|the port used during discovery for multicast requests. Defaults to **4174**. Note that in case the property **com.sun.jini.reggie.initialUnicastDiscoveryPort** system property is not defined it is also used as the default post for unicast requests.|4174|
|com.gs.multicast.ttl|The multicast packet time to live. | 3|


{{% info %}}
The two multicast addresses allow you to completely separate two different GigaSpaces installations, so lookup services won't communicate with each other (even on the wire level, which is different than the groups, which communicate on the content level).
{{%/info%}}

# Troubleshooting the Discovery/Group Configuration

Using the [list LUS ](./command-line-interface.html) CLI option, run from `<XAP Root>\bin\gs.bat/sh`, you can search for available Jini Lookup Services in the network.
`<XAP Root>\bin\gs.bat/sh` list lus

The following result examples appear on the console:


```bash
-----------------------------------------------------------------------
-- Discovered Lookup Service at host [ 192.168.10.233 ].
-- Lookup Service registered to the following jini groups:
                 Group [ gigaspaces-gershon ]
-- Lookup Service has [3] services, lookup took [631] millis, [0] seconds:
                 Service Class: com.j_spaces.core.JSpaceContainerProxy | 018aae08-5d2a-4b61-9739-36c915f4e2d9
                 Service Class: com.j_spaces.core.client.JSpaceProxy | 6f05db92-187f-4e26-b52d-ec32d3d4723c
                 Service Class: com.sun.jini.reggie.ConstrainableRegistrarProxy | 2ff6aab3-8e1a-4a2b-a1b2-
                 4391825c2bbc

-----------------------------------------------------------------------
-- Discovered Lookup Service at host [ 192.168.10.233 ].
-- Lookup Service registered to the following jini groups:
                 Group [ gigaspaces-gershon ]
-- Lookup Service has [3] services, lookup took [50] millis, [0] seconds:
                 Service Class: com.j_spaces.core.JSpaceContainerProxy | 018aae08-5d2a-4b61-9739-36c915f4e2d9
                 Service Class: com.j_spaces.core.client.JSpaceProxy | 6f05db92-187f-4e26-b52d-ec32d3d4723c
                 Service Class: com.sun.jini.reggie.ConstrainableRegistrarProxy | d3e7e224-9b46-4782-a902-
                 e4e64c11ca95
```

# Multi Network Card Configuration

{{% refer %}}
 For details on multi-network card configuration, refer to: [How to Configure an Environment With Multiple Network-Cards (Multi-NIC)](./network-multi-nic.html).
{{% /refer %}}

