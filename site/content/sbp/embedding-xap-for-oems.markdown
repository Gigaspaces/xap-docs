---
type: post
title:  Embedding XAP for OEMs
categories: SBP
parent: production.html
weight: 100
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim|  | Feb 2014|    |    |


{{% ssummary page %}}Explains how to deploy and manage a [Data Grid](/product_overview/the-in-memory-data-grid.html), catering to quick OEM installation.{{% /ssummary %}}

GigaSpaces XAP can be used as a self contained application platform in which case your application would be contained (embedded) within the GigaSpaces application container. GigaSpaces XAP can also be embedded within external application processes. This section describe the steps required to embed GigaSpaces within external application processes.

# General Description

Embedding GigaSpaces cluster can be as simple as starting the [GigaSpaces Agent](/product_overview/service-grid.html#gsa) in each machine.
The agent is responsible for bootstrapping the GigaSpaces cluster environment implicitly. The agent uses a peer to peer communication between themselves to ensure that the environment is up and running also in an  event of a failure.

Once the agent get started you can start using the [GigaSpaces Elastic middleware]({{%latestjavaurl%}}/elastic-processing-unit.html) directly from your application.
The Elastic Middleware automatically provision itself on the GigaSpaces cluster based on the capacity and other SLA requirements.

The following example shows how to embed GigaSpaces Data Grid using this model.

# Example - Embedding GigaSpaces Data Grid

## Acquiring and Installing XAP

Acquiring XAP is simple: download an archive from the [Current Releases](http://www.gigaspaces.com/LatestProductVersion) page.

Installation of XAP is just as easy as getting XAP is: since it's a simple archive, unzip it into a directory of your choice.

On Windows, for example, one might install it into `C:\tools\`, leading to an installation directory of `C:\tools\gigaspaces-xap-premium-8.0.5\`.

In a UNIX environment, you might install it into `/usr/local/`, which would result in a final installation directory of `/usr/local/gigaspaces-xap-premium-8.0.5/`.

## Running the GigaSpaces Agent

A GigaSpaces node is best facilitated through the use of a service called the "[Grid Service Agent](/product_overview/service-grid.html#gsa)," or GSA.

The simplest way to start a node with GigaSpaces is just to invoke the GSA in the GigaSpaces home directory, preferably in its own command shell (although you can easily start a background process with `start` or `nohup` if desired):

{{%tabs%}}

{{%tab "  Windows "%}}


```java
.\gs-agent.bat
```

{{% /tab %}}

{{%tab "  Linux "%}}


```java
./gs-agent.sh
```

{{% /tab %}}

{{% /tabs %}}

## Connecting to a Data Grid

It's actually fairly easy to write some code that can connect to an existing datagrid, and deploy a new one if the datagrid doesn't exist.

First, make sure the [classpath]({{%latestjavaurl%}}/setting-classpath.html) includes the GigaSpaces runtime. Then, connect to the datagrid. The following snippets shows how to create and deploy an Elastic Data Grid and how to find an existing data Data Grid service.

Creating and deploying an Elastic Data Grid


```java
        Admin admin = new AdminFactory().createAdmin();
        GridServiceManager esm = admin.getGridServiceManagers().waitForAtLeastOne();
        ProcessingUnit pu = esm.deploy(new SpaceDeployment(spaceName)
          .partitioned(2, 1));
        admin.close();
```

Getting a reference to an existing DataGrid instance


```java
     UrlSpaceConfigurer configurer =
        new UrlSpaceConfigurer("jini:/*/*/" + spaceName);
      IJSpace space = configurer.space();
```

You can also use a simple helper utility (DataGridConnectionUtility) that combines the two. It first look for a DataGrid instance and if one doesn't exist it will create a new one; it's trivial to alter the `getSpace()` method to increase the number of nodes or even scale dynamically as required.

{{% tip %}}
A The DataGridConnectionUtility class [is available on Github](https://github.com/Gigaspaces/bestpractices/blob/master/plains/src/main/java/org/openspaces/plains/datagrid/DataGridConnectionUtility.java), in the "plains" project.
{{% /tip %}}

With this class in the classpath, getting a datagrid reference is as simple as:


```java
GigaSpace space=DataGridConnectionUtility.getSpace("myGrid");
```

{{%refer%}}

- [Modeling and Accessing Your Data]({{%latestjavaurl%}}/modeling-your-data.html)
- [Deploying and Interacting with the Space](./deploying-and-interacting-with-the-space.html)

{{%/refer%}}
