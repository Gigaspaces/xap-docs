---
type: post140
title:  Apache Zeppelin Kubernetes Pod
parent: kubernetes-overview.html
categories: XAP140ADM, PRM
weight: 400
---

# Overview

Apache Zeppelin is a multi-purpose web notebook that supports data ingestion and discovery, as well as data analytics, visualization, and collaboration.

KubeGrid runs Apache Zeppelin in a dedicated Pod, and can be accessed via the url `http://<minikube ip>:30990`

Note: 

- The Zeppelin Pod port is configurable and its value can be set during KubeGrid deployment with `--set zeppelin.service.port.api=<port>`
- To get the IP address of your minikube, type the `minikube ip` command in the command window.

# Querying The Data Grid With InsightEdge JDBC Zeppelin Interpreter

Zeppelin uses interpreters to compile and run paragraphs. InsightEdge Zeppelin comes with a custom JDBC interpreter, that enables running SQL queries on the data grid in notebooks. 
The queries are executed by [InsightEdge SQL Driver](sql-query-intro.html)      

## JDBC Interpreter Configuration

The JDBC interpreter connects to the data grid via a jdbc url. To configure the url value to point to the kubernetes data grid, perform the following steps:

- In Zeppelin web UI, go to the Interpreters section
- Find the insightedge_jdbc interpreter and press edit
- Edit the `default.url` parameter to the following form `jdbc:insightedge:spaceName=<release-name>?locators=<release name>-<headless service name>`
- Save interpreter changes

## Querying the Data Grid In Notebooks

Once the JDBC interpreter is properly configured, Zeppelin paragraphs bound to the `%insightedge_jdbc`  interpreter can run SQL queries on the data grid
