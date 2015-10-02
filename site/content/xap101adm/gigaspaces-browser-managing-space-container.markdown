---
type: post101adm
title:  Space Containers
categories: XAP101ADM
parent: gigaspaces-management-center.html
weight: 300
---

{{% ssummary %}} {{% /ssummary %}}



{{%section%}}
{{%column width="10%" %}}
![IMG501.jpg](/attachment_files/IMG501.jpg)
{{%/column%}}
{{%column width="90%" %}}
To edit a container's configuration, select a container node in the **Container Tree** panel on the left, then select the **Container Configuration** tab at the bottom of the Configuration panel.|
{{%/column%}}
{{%/section%}}

# Lookup service options

You may view the following container attributes in the **Directory Services** tab:
**Directory Services / Jini Lookup Services** - these attributes determine where space instances are registered. They may have one of three values:

- JNDI service
- Jini Lookup Service
- Both of them

![Container Configuration Directory Services Tab.jpg](/attachment_files/Container Configuration Directory Services Tab.jpg)


# Configuration

**You may view the following container attributes in the General tab:**

- **Home Directory** -- Read only field. Displays the installation directory of the GigaSpaces Server.
- **License** -- Use this field to change the GigaSpaces Server license key.
- **Container Socket Port** -This is the port used by the container to export its stub. The default value is `0`.
- **Security Mode** -- The space container provides basic security capabilities that control whether users have full control or read-only access. **Read Only** specifies that all users should be blocked from creating and destroying spaces, shutting down containers, setting container configuration and restarting a container. **Full Control** specifies that all users are allowed to perform all operations on the container. If the configuration is set to **Read Only**, the only way to change it to **Full Control** is through the configuration file. This prevents users from changing the restrictions on a space browser.

![Container Configuration General Tab - GigaSpaces Browse.jpg](/attachment_files/Container Configuration General Tab - GigaSpaces Browse.jpg)


# JMS

**The JMS tab in the container configuration window enables viewing or modifying of the following container attributes:**

- **JMS Enabled** -- Must be checked in order to configure the server-side of the GigaSpaces JMS interface.
- **Internal JNDI Enabled** -- Must be checked if you want to use the GigaSpaces Server's lookup service for the JMS Destinations and connection factories lookup.
- **External JNDI Enabled** -- Must be checked if you want to use an external lookup service, such as the Jboss JNP naming protocol implementation.
    - If you enabled an external JNDI registry, update your JNDI provider's configuration properties inside the `jndi.properties` file and add the directory that contains the file to your classpath.

![Container Configuration JMS Tab - GigaSpaces Browser.jpg](/attachment_files/Container Configuration JMS Tab - GigaSpaces Browser.jpg)
