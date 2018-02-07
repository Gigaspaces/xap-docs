---
type: post123
title:  Deploying from Docker Hub
categories: XAP123GS
parent: none
weight: 50
---

To run XAP using a Docker container, you need to pull and start a Docker image. The XAP Docker image utilizes GigaSpaces' XAP [command line interface (CLI)](https://docs.gigaspaces.com/xap/12.3/admin/command-line-interface.html "XAP CLI documentation").

To test the XAP Docker image, run the following in your command line to display a help screen with all the available commands: 

```
docker run gigaspaces/xap --help
```

 For example, the `version` command prints version information:

```
docker run gigaspaces/xap version
```

# Running Your First Container

The simplest and fastest way to start working with XAP is to get a single instance up and running on your local machine.  After the instance is initiated, you can start to explore the various features and capabilities.

To run a single host on your machine:

```
docker run --name test -e XAP_LICENSE=tryme -p 8090:8090 -p 8099:8099 gigaspaces/xap
```

This command includes the  XAP `tryme` license, which enables you to use the full XAP product for 24 hours (you can  get a longer evaluation license from the main [GigaSpaces](http://gigaspaces.com) website).

When running the XAP Docker image without arguments, a host is automatically started with the following components:

* [XAP Manager](https://docs.gigaspaces.com/xap/12.3/admin/xap-manager.html) (mapped to port `8090`) 
* [Web Management Console](https://docs.gigaspaces.com/xap/12.3/admin/web-management-console.html) (mapped to port `8099`)

{{% note "Note"%}}
These ports are mapped to your host, so you can access them.
{{% /note %}}


# Running a Test Cluster on Your Host

If you want to test high availability or other distributed features on a single host, you can start multiple instances of XAP. All you need to do is assign identities so they can communicate with each other, and specify which containers will run the XAP Manager service. For example:

```
XAP_MANAGER_SERVERS=host1,host2,host3
XAP_LICENSE=tryme
docker run --name test1 -h=host1 -d -e XAP_LICENSE -e XAP_MANAGER_SERVERS -p 8090:8090 -p 8099:8099 gigaspaces/xap
docker run --name test2 -h=host2 -d -e XAP_LICENSE -e XAP_MANAGER_SERVERS -p 8091:8090 -p 8100:8099 gigaspaces/xap
docker run --name test3 -h=host3 -d -e XAP_LICENSE -e XAP_MANAGER_SERVERS gigaspaces/xap
docker run --name test4 -h=host4 -d -e XAP_LICENSE -e XAP_MANAGER_SERVERS gigaspaces/xap
docker run --name test5 -h=host5 -d -e XAP_LICENSE -e XAP_MANAGER_SERVERS gigaspaces/xap
```

Due to starting multiple management containers on the same host, the ports of the 2nd and 3rd containers must be mapped to different host ports, in order to avoid conflicts with the 1st container. The rest of the containers don't expose any ports; they are connected to the management nodes via Docker's default bridge network, and are managed through the Web Management Console or [REST Manager API](https://docs.gigaspaces.com/xap/12.3/admin/xap-manager-rest.html).




