---
type: post
title:  Terms and Concepts
parent: kubernetes-overview.html
categories: EARLY_ACCESS
weight: 80
---

## KubeGrid

KubeGrid is GigaSpaces’ Kubernetes-based deployment and orchestration of the data grid and InsightEdge. 

## Node

A node is a worker machine, and can be a either a VM or physical machine (depending on the cluster). Every node includes all the services that are needed to run the Pods that it hosts (including Docker, kubelet and kube-proxy), and is managed by the master components.

## Pods

### Data Pod

The Data Pod is analogous to the Processing Unit instance in the data grid. Each Data Pod contains a single Processing Unit instance that provides cloud-native support using the Kubernetes built-in controllers, such as auto scaling and self healing.

### Driver Pod

A Driver Pod contains the Spark Driver, which creates Spark Executors, connects to them, and executes the required application code. When the application completes, the Driver Pod persists the logs and shuts down, remaining in completed state.

### Executor Pod
 
An Executor Pod contains the Spark Executor, which runs the Spark job on the data in the co-located Data Pod. When the application completes and the Spark jobs are no longer required, the Executor Pod terminates.

### Management Pod

The Management Pod contains the data grid’s Manager components, namely the Lookup Service (LUS) used by services to discover each other, the REST Manager  for remotely managing the environment from any platform, and Apache Zookeeper for Space leader election.

### Zeppelin Pod

The Zeppelin Pod contains the interactive Apache Zeppelin web-based notebook. Use this notebook to perform data-driven, interactive analytics on the data grid.

## Pod Anti-Affinity

An anti-affinity rule forces a group of virtual machines to be placed across different physical hosts. This prevents all the virtual machines from failing at the same time if a single physical host fails.
Pod anti-affinity allocates the primary and backup Management and Data Pods across the available nodes in such a way that a backup Pod is always located on a different node from its primary Pod.

## Pod Disruption Budget
 
A Pod can disappear due to a voluntary disruption, meaning a person or a controller destroys it, or if there is an unavoidable hardware or system software error (an involuntary disruption).
A Pod Disruption Budget (PDB) enables limiting the number Pods that are down simultaneously due to voluntary disruptions. This protects the application by ensuring that a homogeneous set of Pods is always up and available.

## Pod Quorum

A high availability configuration requires three Management Pods. This ensures an uninterrupted workflow if a Management Pod is disrupted, because there is still a quorum (required minimum) available to continue managing the Data Pods in the system.

## Readiness

A probe that indicates whether a Data Pod is ready to service requests (the application’s processes are ready to process input). If data is still loading, then the Pod is not ready and the probe will not receive a response.

## Services

### Service

A Kubernetes service is an abstraction that defines a logical set of Pods, along with a policy for accessing them. We use services to define NodePorts that expose specific Pods to the user, such as for Apache Zeppelin (port 30990), and Swagger for the REST Manager API (port 30890).

### Headless Service

In environments where load-balancing and a single service IP are not needed, you can use headless services to reduce coupling to the Kubernetes system and do discovery independently. In this scenario, no cluster IP is allocated, kube-proxy doesn’t handle these services, and there is no load balancing or proxying done by the platform for them.

## StatefulSet

A StatefulSet manages the deployment and scaling of a set of Pods for stateful applications. The Pods are based on an identical container definition, but are not interchangeable (each Pod has a persistent identifier that it maintains across any rescheduling).
For example, a StatefulSet can manage automated rolling updates of business logic inside a Pod, and can control scaling up Data Pods one at a time.

