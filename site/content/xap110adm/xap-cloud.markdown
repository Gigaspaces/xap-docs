---
type: post110
title:  XAP Cloud
categories: XAP110ADM
parent: none
weight: 1400
---

As the complexity of deployment environments grows, smarter automation is needed. **xap-cloud** simplifies the installation and configuration of XAP on a set of machines, on a cloud or on-premises.

<br>

# Requirements

The requirements can be divided into two sections:

#### Client

The host that will run the tool:

- CentOS 7
- A sudoer user for installing the required libraries

#### Servers

Hosts that will run XAP components:

- Python 2.7 or higher
- CentOS or Ubuntu
- A sudoer user that does not require password when running a command as superuser.

<br>

# Configurations

Using the XAP Cloud tool requires two configurations: Hosts that XAP will be installed on and XAP configurations.

### Hosts configuration

In the root directory of the tool you can find the `xap-hosts.yaml` file. This file contains the hosts that XAP will be installed on.

There are two types of hosts: **management** and **container**.

* `management` hosts will host and manage GSA, GSM and LUS
* `container` hosts will host and manage GSA and GSCs.

Here is an example of the hosts file:

```yaml
#####################################################
# An example hosts configuration file.
#
# Rewrite it to suit your needs.
#####################################################

default:
  port: 22
  auth:
    username: root
    password: root
#    key_filename: /tmp/id_rsa.pem

hosts:
  - host: 172.17.0.3
    tags: [management]
  - host: 172.17.0.4
    tags: [container]
  - ip_range: 172.17.0.10 - 172.17.0.15
    tags: [container]
```

This file consists of a list of host machines where each host has the following attributes:

- host/ip_range - a single or range of IPs of the host. The host should be accessible from the client host via this IP.
- port - authentication port
- auth - authentication details: username and password. If you have a private key you can pass it via the **key_filename** attribute.
- tags - defines the type of the host: *management* or *container* host. Note that only one tag can be provided to the same host.

In case you have one or more properties that should be applied to all hosts, e.g. auth and port attributes, you can write them in the **default** section.

<br>

### XAP configuration

Next, we need to configure the installation process as well as XAP. This is done via the `xap-cloud.yaml` file.

Bellow is the configuration file with the default values:

```bash
# mandatory properties
## URL or local location of the zip file
xap_location: "/root/demo/gigaspaces-xap-premium-11.0.0-ga-b14800.zip"
## OpenJDK Java version to install. Options are: 6, 7 and 8. 
## If you already have Java installed you need to set JAVA_HOME and leave this property empty.
java_version: "8"

# optional properties
## The remote directory location of the XAP installation. Default is user's home directory.
#xap_root: "" 

## License key, if not provided in the zip file.
#xap_license_key: "" 

## sets XAP_LOOKUP_GROUPS
#xap_lookup_groups: "" 

## sets XAP_NIC_ADDRESS
#xap_nic_address: "#eth0#" 

## The port that the webui will be available on.
#xap_webui_port: 9099 

## management hosts configuration
## Number of GSCs to start on management hosts
#management_xap_gsc_instances: 0
## The bellow properties will be passed to XAP_GSA_OPTIONS, XAP_LUS_OPTIONS, XAP_GSM_OPTIONS, 
## XAP_GSC_OPTIONS and XAP_EXT_OPTIONS
## If value is left empty, the product's default will be used.
#management_xap_gsa_options: ""
#management_xap_lus_options: ""
#management_xap_gsm_options: "-Xms128m -Xmx128m"
#management_xap_gsc_options: ""
#management_xap_ext_options: ""

## container hosts configuration
## Number of GSCs to start on container hosts
#container_xap_gsc_instances: 2
## The bellow properties will be passed to XAP_GSA_OPTIONS,XAP_GSC_OPTIONS and XAP_EXT_OPTIONS
## If value is left empty, the product's default will be used.
#container_xap_gsa_options: ""
#container_xap_gsc_options: "-Xms128m -Xmx128m"
#container_xap_ext_options: ""
```

<br>

# Installing XAP

{{%warning "Warning"%}}
Running the install twice might lead to errors in the uninstall step, therefore it is not recommended.
{{%/warning%}}

Once the configuration step is completed, you can simply run the following command from within the tool directory:

```bash
./xap-cloud-centos7.sh install
```

This command will start the installation process. First it will install Java if *java_version* property is set. Then it will download/copy the packaged zip file of XAP to the hosts, unzip it into the *xap_root* directory.

After that it will run the gs-agent.sh script with the right parameters for each *management* and *container* host.

Next, it will start a WebUI on one of the *management* hosts and will print its address.

Output sample:

![xap-cloud-install.png](/attachment_files/xap-cloud-install.png)


<br>

# Uninstalling XAP

To uninstall XAP run the following command:

```bash
./xap-cloud-centos7.sh uninstall
```

The command will stop all of the GS components as well as the WebUI and delete XAP directory (configured via *xap_root* property).

Output sample:
 
![xap-cloud-uninstall.png](/attachment_files/xap-cloud-uninstall.png)

<br>

# Advanced

XAP Cloud tool provides the ability to make modifications to the hosts and to the XAP installation. In order to do so, you need to create the relevant script file and put it in the tool root directory:

- pre-install.sh - will run prior to the install step
- pre-start.sh - will run prior to the start step

For example, if you wish to configure the `ulimit` then you should use the **pre-install.sh** script.

{{%note "Note"%}}
The tool comes with default scripts that are empty. If the files exist in the tool directory, they will replace the default ones. Therefore, deleting them afterwards will not return the defaults. In order to have the defaults back these files should be empty and placed in the tool's root folder.
{{%/note%}}
