---
type: post100
title:  Gateway Components
categories: XAP100
parent: multi-site-replication-overview.html
weight: 200
canonical: auto
---

{{% ssummary %}} {{% /ssummary %}}



{{%imagertext "/attachment_files/wan_gatway_archi.jpg" %}}
A replication gateway is used in order to send replication events from one data grid to another data grid by acting as the delegator of outgoing replication from one data grid to another, and by receiving incoming replication from a remote gateway and dispatching it to the local data grid. The gateway is composed of two components that handle this operation, a delegator and a sink. These components are configured in a standard `pu.xml` and usually the gateway should be deployed as a single processing unit (with one instance) into the service grid on each site configured with the local site relevant properties.

{{%/imagertext%}}


## Gateway Delegator

The gateway's delegator main purpose is to delegate outgoing replication from one site to another. The need for the delegator arises from the common gateway usage topology, which is replicating data between different data grids across the WAN. In this case, usually each data grid space instance machine cannot open a direct socket to the remote site, therefor it uses the local delegator which is deployed on the local network in order to create a replication connection to the remote site. The machine on which the delegator is deployed, should have access to the remote site(s) over the WAN and the ability to open sockets with the ports for discovery and communication which are configured in the gateway. Because the delegator is an outgoing communication point for replication, it is also used in the [Bootstrap](./replication-gateway-bootstrapping-process.html) process to delegate the bootstrap related communication from the bootstrapped target site to the bootstrap source site.

The delegator configuration block in the gateway `pu.xml` looks as follows:


```xml
<os-gateway:delegator id="delegator" local-gateway-name="NEWYORK" gateway-lookups="gatewayLookups">
  <os-gateway:delegations>
    <os-gateway:delegation target="LONDON"/>
    <os-gateway:delegation target="HONGKONG"/>
  </os-gateway:delegations>
</os-gateway:delegator>
```

In this example, we have New-York's delegator gateway which acts as a delegation point for two targets, London and Hong-Kong. This will be the mediation point for the two replication channels to London and Hong-Kong data grids. It is important to understand that the delegator is not in charge of broadcasting each packet to all the targets but it receives the relevant packet from each source channel and delegate it to the correct target according to the channel endpoint.

## Gateway Sink

The gateway sink main purpose is to handle incoming replication activity, which is received by a remote gateway delegator from a remote site, and dispatches it into the local data grid. The sink has a special proxy to the local data grid that it uses in order to dispatch the replication into the data grid. The sink is also used the [Bootstrap](./replication-gateway-bootstrapping-process.html) process in both ends, it is used to initiate the bootstrap process and to fill in the local data grid with the data. It is also used at the bootstrap source site as the communication mediator between the remote sink and the local data grid which is used as the source for bootstrapping.

The sink configuration block in the gateway `pu.xml` looks as follows:


```xml
<os-gateway:sink id="sink"
  local-gateway-name="NEWYORK" gateway-lookups="gatewayLookups"
  local-space-url="jini://*/*/myNYSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
</os-gateway:sink>
```

In this example, this is the sink of New-York gateway that is open for incoming replication from London and Hong-Kong and will dispatch this incoming replication to the local data grid named "myNYSpace".

# Gateway Lookup Mechanism

In the above configuration samples, there is a `gateway-lookup` attribute, this attribute points to the lookup related configuration parameters.

The gateway components should locate each other in order to communicate with each other. This lookup is done both across sites and in some cases locally. Additionally the local data grid needs to locate the gateway delegator it needs to use for delegation, and the gateway sink needs to locate the local data grid in order to dispatch incoming replication to it.

We will split the lookup described here into two categories:

1. Local site lookup - locating components which reside in the local site (network)
1. Cross site lookup - locating components between sites (different networks), this lookup is only relevant for gateway components locating each other.

## Local Site Lookup

This lookup is done using the locally deployed lookup services which are already used by the service grid components and data grids in order to locate each other. The gateway components register in the local lookup services and therefor can be located by all the local services and components. e.g a data grid locates the local delegator to the remote target via the local lookup service.

## Cross Site Lookup

In most common scenarios, each site resides on a different LAN, and there is no direct network connection between the machines on different sites. However, the machines hosting the gateways of each site, needs to have some network connectivity available in order for the gateway to connect to each other and send the replication data between the sites. For the gateway to be able to communicate and locate each other, they need to be able to use two available ports, one used for the lookup discovery process and the other for the actual communication activity between the gateways. These are known as the discovery ports (Lookup service port) and the communication port (LRMI port). By default, each gateway will start an embedded lookup service which is used for the discovery process, that lookup service will be started with the specified discovery port for that gateway. This is not mandatory, and the gateway can use an **already existing lookup service** which will be explained later on. Each component, delegator and sink, will publish themselves in the lookup services in order for the other gateway components to be able to locate them between the sites.

The configuration which specifies the discovery and communication ports, along with the lookup service machine host (which by default should be the gateway machine it self as it starts the embedded lookup service) is described in each gateway `pu.xml` as follows:


```xml
<os-gateway:lookups id="gatewayLookups">
  <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
    discovery-port="10001" communication-port="7000" />
  <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
    discovery-port="10002" communication-port="8000" />
  <os-gateway:lookup gateway-name="HONGKONG" host="hk-gateway-host-machine"
    discovery-port="10003" communication-port="9000" />
</os-gateway:lookups>
<!-- The above ports and host names are randomly selected and have no
     meaning, all sites could designate the same ports as well-->
```

The host and discovery port are used for the lookup process only, the communication port is used for the actual replication process after the relevant gateway component have discovered its target (e.g a delegator locates the target sink). The discovery and communication port should be permitted in the firewalls between the lookup services machines and between the gateway, when using the default, the lookup services are started inside the gateways, so this ports needs to be permitted between the gateway machines only.

When using the default configuration, in which the gateway starts the lookup service as embedded, the gateway should be deployed on the machine specified in its lookup entry as the `host` attribute, this can be achieved by having a [GigaSpaces Agent](/product_overview/service-grid.html#gsa) running on that machine with a dedicated zone, and configuring the gateway to be deployed to that specific zone (see [Configuring the Processing Unit SLA]({{%currentadmurl%}}/the-sla-overview.html)). Once deployed, the gateway will check if the [Container](/product_overview/service-grid.html#gsc) it was deployed into is started with the proper ports, if not it will relocate itself to a proper container, and it may instantiate a new container managed by the agent on that machine if needed.

The communication port is optional, if there is no firewall between the gateways or all ports are permitted, this is not needed. Its purpose is to ease the gateway deployment by having the gateway automatically detect whether it is instantiated a GSC which was started with the specified communication port (LRMI), and if not it will spawn a new GSC with the correct LRMI port and relocate itself into that GSC thus saving you the need to pre-start a GSC with this specific port. If the GSC that is supposed to host the gateway is already configured with the right LRMI port, or alternatively, the communication port is not defined, the gateway will not spawn a new GSC and will not relocate itself.

If there is no firewall, and all ports are available, there is no need to specify a communication port as any communication port specified by the OS will work.

This process of spawning a new GSC and gateway relocation done as well when the gateway is configured to start an embedded lookup service (default) and the hosting GSC is not started with the matching discovery (lookup) port. This will not happen if the gateway is not configured to start an embedded gateway and is using an already existing one that is determined by the `host` and `discovery-port` attributes. In order to disable the creation of an embedded lookup service all the gateway components in the gateway processing unit (Both delegator and sink) should be configured with the following:


```xml
<os-gateway:delegator id="delegator" local-gateway-name=... gateway-lookups=... start-embedded-lus="false">
  ...
</os-gateway:delegator>
```


```xml
<os-gateway:sink id="sink"
  local-gateway-name=... gateway-lookups=... start-embedded-lus="false" local-space-url=...>
  ...
</os-gateway:sink>
```

In some scenarios, for example when there are many sites, it is more reasonable to use pre-started limited number of lookup services to avoid the creation of lookup services as the number of sites.
In this case, the `start-embedded-lus="false"` should be used and the `host` and `discovery-port` of the lookup attributes should point to the existing lookup services location. For example:


```xml
<os-gateway:lookups id="gatewayLookups">
  <os-gateway:lookup gateway-name="NEWYORK" host="lookup1-host-machine"
    discovery-port="10001" communication-port=... />
  <os-gateway:lookup gateway-name="LONDON" host="lookup2-host-machine"
    discovery-port="10002" communication-port=... />
  <os-gateway:lookup gateway-name="HONGKONG" host="lookup1-host-machine"
    discovery-port="10001" communication-port=... />
</os-gateway:lookups>
<!-- The above ports and host names are randomly selected and have no
     meaning, all sites could designate the same ports as well-->
```

In this case it is not that important which `host` and `discovery-port` is specified in which site, but it is recommended to use the "nearest" available lookup service configuration per site. However, each existing lookup service should appear at least once under one of the `os-gateway:lookup` entries in order for it to be used in the lookup process.

# Full Gateway Configuration Example File

Below is an example `pu.xml` file for the gateway of New-York


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:os-core="http://www.openspaces.org/schema/core"
  xmlns:os-events="http://www.openspaces.org/schema/events"
  xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
  xmlns:os-sla="http://www.openspaces.org/schema/sla"
  xmlns:os-gateway="http://www.openspaces.org/schema/core/gateway"
spring
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
spring
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
    http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{% currentversion %}}/core/openspaces-core.xsd
    http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{% currentversion %}}/events/openspaces-events.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{% currentversion %}}/remoting/openspaces-remoting.xsd
    http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd
    http://www.openspaces.org/schema/core/gateway http://www.openspaces.org/schema/{{% currentversion %}}/core/gateway/openspaces-gateway.xsd">

  <os-gateway:delegator id="delegator" local-gateway-name="NEWYORK"
    gateway-lookups="gatewayLookups">
    <os-gateway:delegation target="LONDON"/>
    <os-gateway:delegation target="HONGKONG"/>
  </os-gateway:delegator>

  <os-gateway:sink id="sink" local-gateway-name="NEWYORK"
    gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myNYSpace">
    <os-gateway:sources>
      <os-gateway:source name="LONDON" />
      <os-gateway:source name="HONGKONG" />
    </os-gateway:sources>
  </os-gateway:sink>

  <os-gateway:lookups id="gatewayLookups">
    <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
      discovery-port="10001" communication-port="7000" />
    <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
      discovery-port="10002" communication-port="8000" />
    <os-gateway:lookup gateway-name="HONGKONG" host="hk-gateway-host-machine"
      discovery-port="10003" communication-port="9000" />
  </os-gateway:lookups>
  <!-- The above ports and host names are randomly selected
       and have no meaning, all sites could designate the same ports as well-->
</beans>
```

# Indirect Delegation (Delegation via other gateways)

Some topologies may require in direct delegation, for example in the above topology, there bandwidth between London and Hong-Kong be very poor or maybe there is no direct connection between this two sites but they should still replicate to each other. This replication can be delegated from London to Hong-Kong via New-York and the other way around as well. This is accomplished by chaining delegators together, in this case the delegator of London to Hong-Kong will be actually connected to the delegator of New-York to Hong-Kong thus delegating the replication communication via New-York. In this case the gateway of London should be configured as follows:


```xml
<os-gateway:delegator id="delegator"
  local-gateway-name="LONDON" gateway-lookups="gatewayLookups">
  <os-gateway:delegations>
    <os-gateway:delegation target="NEWYORK"/>
    <os-gateway:delegation target="HONGKONG" delegate-through="NEWYORK"/>
  </os-gateway:delegations>
</os-gateway:delegator>

<os-gateway:sink id="sink" local-gateway-name="LONDON"
  gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myNYSpace">
  <os-gateway:sources>
    <os-gateway:source name="LONDON" />
    <os-gateway:source name="HONGKONG" />
  </os-gateway:sources>
</os-gateway:sink>

<os-gateway:lookups id="gatewayLookups">
  <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
    discovery-port="10001" communication-port="7000" />
  <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
    discovery-port="10002" communication-port="8000" />
</os-gateway:lookups>
<!-- The above ports and host names are randomly selected and
     have no meaning, all sites could designate the same ports as well-->
</beans>
```

In this configuration we have specifies that the delegator to Hong Kong should be routed via New York. This chaining can contain one or more delegators, i.e New York delegator could have been connected to Hong Kong via some other 4th site. The Hong Kong gateway should be configured in the same way. Another important thing to notice is that the lookup entry for Hong Kong is removed in London's gateway since it should never lookup Hong Kong directly.

In Direct delegation can be used by a [Bootstrap](./replication-gateway-bootstrapping-process.html) in the same manner, since the gateway sink in the bootstrapping site is using the local delegator in order to locate the bootstrap source site sink and therefor it will go through the same delegation path.

![wan-delegation.jpg](/attachment_files/wan-delegation.jpg)

# Seperating or Bundling The Gateway Components

The delegator and sink components are actually isolated and can even be deployed in separate processing units but the most simple deployment would be to bundle theses two together. However, in some cases you might want to separate this into two or more machines due to system loads or other reasons. This is done by simple creating two different processing units for the gateway, for example one can run the delegator and the other can run the sink.

Delegator pu.xml:


```xml
<?xml version="1.0" encoding="UTF-8"?>
  ...

  <os-gateway:delegator id="delegator" local-gateway-name="NEWYORK"
    gateway-lookups="gatewayLookups">
    <os-gateway:delegation target="LONDON"/>
    <os-gateway:delegation target="HONGKONG"/>
  </os-gateway:delegator>

  <os-gateway:lookups id="gatewayLookups">
    <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
      discovery-port="10001" communication-port="7000" />
    <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
      discovery-port="10002" communication-port="8000" />
    <os-gateway:lookup gateway-name="HONGKONG" host="hk-gateway-host-machine"
      discovery-port="10003" communication-port="9000" />
  </os-gateway:lookups>
  <!-- The above ports and host names are randomly selected
       and have no meaning, all sites could designate the same ports as well-->
</beans>
```

Sink pu.xml:


```xml
<?xml version="1.0" encoding="UTF-8"?>
  ...

  <os-gateway:sink id="sink" local-gateway-name="NEWYORK"
    gateway-lookups="gatewayLookups" local-space-url="jini://*/*/myNYSpace">
    <os-gateway:sources>
      <os-gateway:source name="LONDON" />
      <os-gateway:source name="HONGKONG" />
    </os-gateway:sources>
  </os-gateway:sink>

  <os-gateway:lookups id="gatewayLookups">
    <os-gateway:lookup gateway-name="NEWYORK" host="ny-gateway-host-machine"
      discovery-port="10001" communication-port="7000" />
    <os-gateway:lookup gateway-name="LONDON" host="london-gateway-host-machine"
      discovery-port="10002" communication-port="8000" />
    <os-gateway:lookup gateway-name="HONGKONG" host="hk-gateway-host-machine"
      discovery-port="10003" communication-port="9000" />
  </os-gateway:lookups>
  <!-- The above ports and host names are randomly selected
       and have no meaning, all sites could designate the same ports as well-->
</beans>
```

It is also possible to bundle more than one sink and/or delegator in one processing unit, thus having one processing unit acting as the gateway of multiple data grids.
It is important to understand that a gateway is a logical and not a physical term which relates to all of the deployed processing units that contains at least one of the gateway components with the same name (sink or delegator).
Additionally, one can bundle two different gateways (i.e gateway components, sinks or delegators, with different names) in the same processing unit.

# NAT Configuration

When having Network Address Translation (NAT) data transit across different routing devices over the WAN, you should use the [NAT mapping configuration]({{%currentadmurl%}}/network-over-nat.html) with your WAN Gateway. Each site gateway should have a NAT mapping file that maps the remote site local IP to the public IP, therefore each site should have a different mapping file because it should not map its own local IP to the public IP. Additionally, in each site you should place the public IP in the Gateway lookup element of the remote sites Gateway, and the local IP in the lookup element of the local site Gateway, once again, in this case the gateways lookup element in the pu.xml of the Gateways will not be symmetric since the local gateway lookup element should contain the local IP at each site. After the lookup process is done, both Gateways connect directly as usual.

# Security


{{%refer%}}
**Space PU Security**<br>
Before addressing the gateway security,  make sure your Space PU is deployed as secured.  Fore more details see [Security]({{%currentsecurl%}}/securing-your-data.html#processing-unit)
{{%/refer%}}


On Multiple Site topologies, securing grid components and data grids is done as described in XAP [Security]({{%currentsecurl%}}/index.html) page. When using a secured environment it is required to provide security credentials for the Gateway components (Sink & Delegator).
The security credentials are used for accessing a secured data grid and for performing administrative operations such as creating a new GSC for the gateway components if necessary.

Providing the security credentials can be done in two ways:

- On deployment (bean level properties)
- In the gateway component's pu.xml.

### Providing security credentials on deployment

The following bean level properties should be set:

- "security.username"
- "security.password"

### Providing security credentials in gateway component's pu.xml

Setting the security credentials is done using the "security" element in the "os-gateway" namespace:


```xml
<os-gateway:delegator id="delegator" local-gateway-name="USA" gateway-lookups="gatewayLookups">
  <os-gateway:delegations>
    <os-gateway:delegation target="FRANCE" />
  </os-gateway:delegations>
  <os-gateway:security username="${security.username}" password="${security.password}" />
</os-gateway:delegator>

<os-gateway:sink id="sink" local-gateway-name="USA" gateway-lookups="gatewayLookups"
         local-space-url="jini://*/*/usaSpace">
  <os-gateway:sources>
    <os-gateway:source name="FRANCE" />
  </os-gateway:sources>
  <os-gateway:security username="gs" password="1234" />
</os-gateway:sink>
```

It is also possible to set a "com.gigaspaces.security.directory.UserDetails" implementation instead of using the username and password attributes:


```xml
<bean id="userDetailsBean" class="com.gigaspaces.security.directory.User">
  <constructor-arg>
    <value>${username}</value>
  </constructor-arg>
  <constructor-arg>
    <value>${password}</value>
  </constructor-arg>
</bean>

<os-gateway:delegator id="delegator" local-gateway-name="USA" gateway-lookups="gatewayLookups">
  <os-gateway:delegations>
    <os-gateway:delegation target="FRANCE" />
  </os-gateway:delegations>
  <os-gateway:security user-details="userDetailsBean" />
</os-gateway:delegator>

<os-gateway:sink id="sink" local-gateway-name="USA" gateway-lookups="gatewayLookups"
        local-space-url="jini://*/*/usaSpace">
  <os-gateway:sources>
    <os-gateway:source name="FRANCE" />
  </os-gateway:sources>
  <os-gateway:security user-details="userDetailsBean" />
</os-gateway:sink>
```
