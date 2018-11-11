---
type: post140
title:  KubeGrid
parent: orchestration.html
categories: XAP140ADM, PRM
weight: 100
---

# Overview

Kubernetes is an open-source orchestration system for automating the deployment, scaling and management of containerized applications. It can be used as an alternative to the GigaSpaces service grid for deploying and orchestrating InsightEdge and the XAP data grid in a cloud-native environment.

Kubernetes works in synergy with InsightEdge, which simplifies the operationalizing of machine learning and transactional processing at scale. InsightEdge leverages Kubernetes features such as cloud-native orchestration automation with self-healing, cooperative multi-tenancy, RBAC authorization, and auto-scaling. Auto-deployment of data services, the Apache Spark deep learning and machine learning framework, and Apache Zeppelin (providing visualization and interactive analytics), seamlessly support InsightEdge-based applications.
 
InsightEdge uses Kubernetes' anti-affinity rules to ensure that primary and backup instances are always on separate Kubernetes nodes. This high-availability design, combined with self-healing, load-balancing and fast-load mechanisms, provides zero downtime and no data loss. Additionally, rolling upgrades can be automated and implemented pod by pod using Kubernetes' Stateful Sets. This allows for a smooth upgrade process with no downtime.
 
<!--
 Automatic scaling is supported, using predefined metrics along with CPU and memory monitoring to signal to Kubernetes when to scale up or down. Customized scaling rules according to personalized SLA, based on production needs, optimize the resources required to support the application requirements.

Through the persistent volume driver, InsightEdge’s intelligent MemoryXtend multi-tier storage offering lets customers configure data prioritization according to the application’s business logic. This ensures that the most relevant data resides in the fastest data storage tier for optimized TCO.
-->

# Architecture

KubeGrid, GigaSpace’s Kubernetes-based deployment option, utilizes the key features of the Kubernetes platform mentioned above to set up and run the data grid. KubeGrid can also auto-deploy data services and frameworks, such as Spark Drivers and Spark Executors, to enable Spark machine learning and Spark SQL.

KubeGrid deploys several components that comprise the data grid. 

The Management Pod contains the data grid’s management components. A minimum of one Management Pod is deployed in a regular environment, while three are deployed in a high availability environment.

{{% align center%}}
<img src="/attachment_files/kubernetes/management-pod.png" width=235" height="278" />
{{% /align%}}

The Management Pod contains the the following:

- Lookup Service (LUS) -- A mechanism for services to discover each other. For example, the LUS can be queried for the location of active Data Pods.
- REST Manager - A RESTful API that is used to manage the data grid and InsightEdge environment remotely, from any platform.
- Apache Zookeeper - A centralized service that determines Space leader election.

The Management Pod manages the Data Pods, which are the fundamental unit of deployment in the data grid. Each Data Pod contains a single Processing Unit instance that provides cloud-native support using built-in Kubernetes controllers, such as auto scaling and self healing.

{{% align center%}}
<img src="/attachment_files/kubernetes/data-pod.png" width=235" height="278" />
{{% /align%}}

## Functionality

KubeGrid enforces SLA-driven capabilities that leverage the Kubernetes controllers and schedulers.This enables defining the following for environment stability:

- Requirements for controlling the provisioning process of available Pods.
- Hosts, zones and nodes. The SLA requirements are based on machine-level statistics, and grouping the Pod processes in zones.
- Deterministic deployment by explicitly defining the primary and backup instances.

Additional benefits include:

- High Availability in conjunction with the Kubernetes Pod anti-affinity rule, which ensures that primary and backup Pods will be allocated on - different nodes.
- Ability to scale the system up or down, and apply rolling updates with zero downtime using StatefulSets (persistent identifiers per Pod).

<!--
- Pod Disruption Budget to provide system stability by limiting voluntary disruptions, so that a homogeneous set of Pods is always up and available.
-->

## Automatic Pod Failover

KubeGrid supports automatic Pod failover behavior. The following example describes the failover behavior for a 2,1 primary/backup topology. When Pod anti-affinity is defined, the Pods are distributed over multiple nodes so that each primary/backup pair is hosted on different nodes.

{{% align center%}}
<img src="/attachment_files/kubernetes/pod-failover-before.png" width=610" height="373" />
{{% /align%}}

If the primary Data Pod B is disrupted for some reason (either voluntary for scenarios such as rolling upgrades, or involuntary for reasons such as loss of connectivity), the system fails over to the backup Data Pod B, ensuring business continuity. Backup Data Pod B switches roles, and becomes a primary Data Pod.

{{% align center%}}
<img src="/attachment_files/kubernetes/pod-failover-after.png" width=610" height="373" />
{{% /align%}}

When the original primary Data Pod B is back up and available, the system can fail back to the restored Data Pod B, which resumes its role as a primary Data Pod. 

<!--
The acting primary Data Pod B returns to backup status.
-->

{{% align center%}}
<img src="/attachment_files/kubernetes/pod-failback.png" width=610" height="373" />
{{% /align%}}

## Running Spark on KubeGrid for InsightEdge Applications

Kubernetes has native support for Apache Spark. When you submit a Spark application, the following occurs:

- A Spark driver is created that runs within a Driver Pod.
- The Spark driver creates Spark executors that run within Executor Pods
- The Spark driver connects to the Spark Executors and executes the  application code.
- When the application completes, the Executor Pods terminate and are cleaned up.
- The Driver Pod stays in “completed state”, persisting the logs until it is manually cleaned up or garbage collection is performed at some point.

The scheduling of the Driver Pod and the Executor Pods is handled by Kubernetes.

{{%note%}}
In completed state, the Driver Pod doesn’t consume any computational or memory resources.
{{% /note%}}

{{% align center%}}
![ie-kubegrid.png](/attachment_files/kubernetes/ie-kubegrid.png)
{{% /align%}}

When the Spark application runs, the Driver Pod and Executor Pods are created according to the schedule that was configured.

{{% align center%}}
![ie-kubegrid-spark.png](/attachment_files/kubernetes/ie-kubegrid-spark.png)
{{% /align%}}

The Executor Pods can access the required data objects from any of the Data Pods in the cluster, regardless of which node they reside on. When the Spark jobs are complete, the Executor Pods are cleaned up and the platform is ready for the next submit.


<!--
## Using Apache Zeppelin with InsightEdge

Apache Zeppelin is a multi-purpose web notebook that supports data ingestion and discovery, as well as data analytics, visualization, and collaboration.

Apache Zeppelin contains a built-in Apache Spark integration. This provides the following:

- Automatic SparkContext and SQLContext injection
- Runtime JAR dependency loading from the local file system or Maven repository
- Ability to view job progress and cancel jobs 

The Zeppelin notebook packaged with InsightEdge includes several InsightEdge demo applications, along with InsightEdge-specific settings that can be configured in the Spark interpreter in order to establish the connection between Spark and the data grid. 

KubeGrid runs Apache Zeppelin in a dedicated Pod, to enable ???
-->
