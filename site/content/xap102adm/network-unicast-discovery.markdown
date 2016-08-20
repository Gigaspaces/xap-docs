---
type: post102
title:  Unicast Discovery
categories: XAP102ADM
parent: network.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}



There are many cases when you need to use a unicast-based services discovery. For example if you want to use unicast with multicast (using Jini Groups), which is the default, or when **you do not have multicast enabled, or you prefer not to use multicast**.

In such cases, the Jini lookup discovery enables the user to discover services (spaces, GSC, GSM, processing units etc.) using unicast protocol.

{{% refer %}}
Please refer to the [Lookup Service Configuration](./network-lookup-service-configuration.html) or the [Networking How Tos](./network.html) section for more details.
{{%/refer%}}

# Configuring the lookup locators property

Services will use the locators property to locate the Jini Lookup Service to lookup other services registered on it. The locators property is configured using the `LOOKUPLOCATORS` environment variable or the `-Dcom.gs.jini_lus.locators` system property. By default it is left blank. It accepts a comma separated list of `host:port`. This list should include the hosts (and ports) where the Jini Lookup Service (or GSM) is running. The default port is 4174.

For example, considering the GSM(+LUS) is running on linux-lab1:4174 and linux-lab2:4174 machines:


```bash
-Dcom.gs.jini_lus.locators=linux-lab1:4174,linux-lab2:4174
```

This will influence the `LOOKUPLOCATORS` environment variable in `setenv` script, which you may also modify directly.

# Locating services using locators

Once the services are started with the locators settings, then any client should be able to find a service, using unicast discovery.

To lookup a Space service using the unicast protocol, add the locators SpaceURL attribute to the space URL string.

For both **unicast AND multicast discovery**, use:


```java
jini://*/./mySpace?locators=linux-lab1:4174,linux-lab2:4174&groups=gigaspaces-{{%currentversion%}}-XAPPremium-ga
```

{{% warning %}}
When the locators attribute is used in conjunction with the jini://* prefix and groups attribute, the discovery will be unicast AND multicast.
{{%/warning%}}

If you want unicast only, you should disable multicast altogether.

{{% tip %}}
For **unicast discovery only**, you should disable multicast using **`-Dcom.gs.multicast.enabled=false`** system property, and use:


```java
jini://linux-lab1:4174,linux-lab2:4174/./mySpace?locators=linux-lab1:4174,linux-lab2:4174
```

{{% /tip %}}

{{% tip %}}
For troubleshooting purposes you should verify that the services (spaces, GSC, GSM, processing units etc.) print correct settings for the locators while they initialize. You can turn on the relevant logging if required.
{{%/tip%}}

# Configuring Jini Lookup Service Unicast Port

To change the lookup service listening port use the `com.sun.jini.reggie.initialUnicastDiscoveryPort` system property. The default value is the one assigned to the `com.gs.multicast.discoveryPort`.

- Set the `LOOKUPLOCATORS` system property in `<XAP Root>\bin\setenv.bat/sh` to match the port number you defined (in this case, `host:1234`). That is required if you specify an explicit unicast/locators port, otherwise the service will use the default port if not set explicitly.

{{%refer%}}
For more information refer to [com.gs.multicast.discoveryPort system property](./network-lookup-service-configuration.html#Multicast Settings).
{{%/refer%}}

# Configuring lookup discovery intervals

When a lookup service fails and is brought back online, a client (such as a GSC, space or a client with a space proxy) needs to re-discover and federate again. In order to make that happen, Jini unicast discovery must retry connections to the remote lookup service. The default unicast retry protocol provides a graduating approach, increasing the amount of time to wait before the next discovery attempts are made - upon each invocation, eventually reaching a maximum time interval over which discovery is re-tried. In this way, the network is not flooded with unicast discovery requests referencing a lookup service that may not be available for quite some time (if ever). The default time to wait between unicast retry attempts are:


```java
long[] sleepTime = {5 * 1000, 10 * 1000, 20 * 1000,
                                    30 * 1000, 60 * 1000,
                                    2 * 60 * 1000, 4 * 60 * 1000,
                                    8 * 60 * 1000, 15 * 60 * 1000};
```

You'll max out at 15 minutes between retries. Thats a big window.
The retry logic only begins once the discovered lookup service is discarded.

To tune the unicast retry intervals, the `com.gigaspaces.unicast.interval` system property is used to control the behavior of this LookupLocatorDiscovery utility. A comma separated list of values defining the intervals to wait between subsequent retries. Values are declared in milliseconds.

Example:


```bash
-Dcom.gigaspaces.unicast.interval=5000
```

Will cause the LookupLocatorDiscovery utility to wait 5 seconds between retries


```bash
-Dcom.gigaspaces.unicast.interval=5000,10000
```

Will cause the LookupLocatorDiscovery utility to first wait 5 seconds, then 10 seconds between retries. This declaration provides a graduating approach (similar in approach to the default settings).

You will need to set this property to take affect for GSM and GSC startup. You should see a similar log message to "Set unicast interval to 5000".
