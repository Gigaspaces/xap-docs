---
type: post140
title:  Using an Apache Zeppelin Notebook in Kubernetes
parent: kubernetes-overview.html
categories: XAP140ADM, PRM
weight: 400
---

Apache Zeppelin is a multi-purpose web notebook that supports data ingestion and discovery, as well as data analytics, visualization, and collaboration.

KubeGrid runs Apache Zeppelin in a dedicated Pod. The Apache Zeppelin web notebooks can be accessed via the URL `http://<minikube ip>:30990`.

{{%note%}}
- The Zeppelin Pod port is configurable, and its value can be set when installing the `insightedge` or `insightedge-zeppelin` Helm chart by defining the following:  `--set zeppelin.service.port.api=<port>`
- To get the IP address of your minikube, type the `minikube ip` command in the command window.
{{%/note%}}

# Querying the Data Grid with the InsightEdge JDBC Interpreter

Zeppelin uses interpreters to compile and run paragraphs. InsightEdge Zeppelin comes with a custom JDBC interpreter, that enables running SQL queries on the data grid in notebooks. 
The queries are executed by the [InsightEdge SQL Driver](../dev-java/sql-query-intro.html).    

## Configuring the JDBC Interpreter

The JDBC interpreter connects to the data grid via a JDBC URL. To configure the URL value to point to the data grid in the Kubernetes environment, do the following:

1. In the Apache Zeppelin web interface, navigate to the Interpreters section.
1. Select the insightedge_jdbc interpreter, and click Edit.
1. Edit the `default.url` parameter as follows: `jdbc:insightedge:spaceName=<space-name>
1. Save the changes you made to the interpreter.

## Querying the Data Grid in Notebooks

When the JDBC interpreter is properly configured, Zeppelin paragraphs that are bound to the `%insightedge_jdbc` interpreter can run SQL queries directly on the data grid.
