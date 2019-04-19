---
type: post122
title:  Unicast Discovery
categories: XAP122ADM, PRM
parent: network.html
weight: 100
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}



There are many cases where unicast-based service discovery is preferred or even necessary. For example if you want to use unicast with multicast (using Jini Groups), which is the default, or when **you do not have multicast enabled, or you prefer not to use multicast**.

In such cases, the Jini lookup discovery enables the user to discover services (Spaces, GSC, GSM, Processing Units, etc.) using unicast protocol.

{{% refer %}}
For more information, refer to the [Lookup Service Configuration](./network-lookup-service-configuration.html) or the [Networking How Tos](./network.html) pages.
{{%/refer%}}


# Discovery Order

When using the unicast lookup service discovery option, you can specify multiple locators. This means that a client looking to bootstrap its space proxy can search for the proxy location within one or more lookup services that may be running on specific hosts.

![locators](/attachment_files/smart-proxy.png)

When multiple locators are specified, a parallel search is conducted across all the given host names (or IPs) for a matching Space name. After it is found, the proxy and its cluster information (primaries, backups) are populated. 
This means the search may not have a pre-defined deterministic order, as a different lookup service can be used each time. When there are many clients using multiple locators (for example, a large compute grid environment with many workers, or a large web application with many instances), the Space proxy bootstrap is distributed across all discovered lookup services. This allows the system to load-balance the overall lookup activity across all running lookup services. 

{{%note%}}
You must ensure that the XAP agent (GSA) and its managed processes, specifically the GCSs, have the same locator configuration (XAP_LOOKUP_LOCATORS environment variable) as the client, to allow these to register themselves within all running lookup services.
{{%/note%}}

If the first lookup service discovered does not have a matching lookup entry that matches the required Space name, the next discovered lookup is used.

For example, with the following code examples the XAP proxy or the Admin that may hold many XAP proxies, a parallel lookup discovery is conducted across Host1 and Host2 specified as the locator settings. The `mySpace` space is bootstrapped using one of the lookup services running on Host1 and Host2:


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("mySpace").lookupLocators("Host1, Host2")).gigaSpace();

GigaSpace gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/mySpace?locators=Host1,Host2")).gigaSpace();

Admin admin = new AdminFactory().addLocators(“Host1”, “Host2”).createAdmin();
```


# Configuring the Lookup Locators Property

Services use the `locators` property to locate the Jini Lookup Service, to look up other services registered on it. The `locators` property is configured using the `XAP_LOOKUP_LOCATORS` environment variable or the `-Dcom.gs.jini_lus.locators` system property. By default, it is left blank. The property accepts a comma-separated list of `host:port`. This list should include the hosts (and ports) where the Jini Lookup Service (or GSM) is running. The default port is 4174.

For example, if the GSM(+LUS) is running on linux-lab1:4174 and linux-lab2:4174 machines:


```bash
-Dcom.gs.jini_lus.locators=linux-lab1:4174,linux-lab2:4174
```

This will influence the `XAP_LOOKUP_LOCATORS` environment variable in the `setenv` script, which you may also modify directly.

# Locating Services Using Locators

When the services are started with the `locators` settings, any client should be able to find a service using unicast discovery.

To look up a Space service using the unicast protocol, add the `locators` SpaceURL attribute to the Space URL string.

For both **unicast AND multicast discovery**, use:


```java
jini://*/./mySpace?locators=linux-lab1:4174,linux-lab2:4174&groups=gigaspaces-{{%currentversion%}}-XAPPremium-ga
```

{{% warning "Important"%}}
When the `locators attribute` is used in conjunction with the jini://* prefix and `groups` attribute, the discovery will be unicast AND multicast.
{{%/warning%}}


#  Unicast Discovery Only

If you want unicast discovery only, you should disable multicast altogether using `-Dcom.gs.multicast.enabled=false` system property, and use:


```java
jini://linux-lab1:4174,linux-lab2:4174/./mySpace?locators=linux-lab1:4174,linux-lab2:4174
```

For troubleshooting purposes, verify that the services (Spaces, GSC, GSM, Processing Units, etc.) print the correct settings for the locators while they initialize. You can turn on the relevant logging if required.
 

# Unicast Port

To change the lookup service listening port, use the `com.sun.jini.reggie.initialUnicastDiscoveryPort` system property. The default value is the one assigned to the `com.gs.multicast.discoveryPort`.

Set the `XAP_LOOKUP_LOCATORS` system property in `<XAP Root>\bin\setenv.bat/sh` to match the port number you defined (in this case, `host:1234`). This is required if you specify an explicit unicast/locators port, otherwise the service uses the default port if not set explicitly.

{{%refer%}}
For more information, refer to [com.gs.multicast.discoveryPort system property](./network-lookup-service-configuration.html#Multicast Settings).
{{%/refer%}}

# Discovery Intervals

When a lookup service fails and is brought back online, a client (such as a GSC, Space, or a client with a Space proxy) needs to re-discover and federate again. In order to make this happen, Jini unicast discovery must retry connections to the remote lookup service. The default unicast retry protocol provides a graduated approach, increasing the amount of time to wait before the next discovery attempts are made upon each invocation, eventually reaching a maximum time interval over which discovery is re-tried. In this way, the network is not flooded with unicast discovery requests referencing a lookup service that may not be available for quite some time (if ever). The default time to wait between unicast retry attempts are:


```java
long[] sleepTime = {5 * 1000, 10 * 1000, 20 * 1000,
                                    30 * 1000, 60 * 1000,
                                    2 * 60 * 1000, 4 * 60 * 1000,
                                    8 * 60 * 1000, 15 * 60 * 1000};
```

The maximum time interval is 15 minutes between retries, which is a big window. The retry logic only begins when the discovered lookup service is discarded.

To adjust the unicast retry intervals, the `com.gigaspaces.unicast.interval` system property is used to control the behavior of this LookupLocatorDiscovery utility. A comma-separated list of values defines the intervals to wait between subsequent retries. Values are declared in milliseconds.

Example:


```bash
-Dcom.gigaspaces.unicast.interval=5000
```

The above will cause the LookupLocatorDiscovery utility to wait 5 seconds between retries.


```bash
-Dcom.gigaspaces.unicast.interval=5000,10000
```

The above will cause the LookupLocatorDiscovery utility to first wait 5 seconds, then 10 seconds between retries. This declaration provides a graduated approach (similar in approach to the default settings).

You must set this property to take affect for GSM and GSC startup. You should see a similar log message to "Set unicast interval to 5000".


 
