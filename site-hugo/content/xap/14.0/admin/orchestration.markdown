---
type: post140
title:  Orchestration
categories: XAP140ADM,PRM
weight: 10
parent: none
---

The InsightEdge Platform and XAP can be deployed on-premise, in cloud environments, or in a hybrid environment. GigaSpaces supports a number of deployment and orchestration options to suit every organization's specific business needs.

# The GigaSpaces Service Grid

The original GigaSpaces runtime environment is the Service Grid. This orchestration method is responsible for materializing the configuration of the Processing Units, provisioning the instances to the runtime infrastructure, and making sure they continue to run properly over time. Core components of the Service Grid include the Grid Service Manager, Grid Service Agent, and the XAP Manager.

# Docker

GigaSpaces products can be deployed on the Service Grid using Docker images that are available online through Docker Hub. Using Docker simplifies environment setup on-premise as well as moving to the cloud, because all the major cloud vendors support Docker. The following Docker images are available on [Docker Hub](https://hub.docker.com/r/gigaspaces/) (each Docker image provides instructions on how to use it):

* [gigaspaces/insightedge](https://hub.docker.com/r/gigaspaces/insightedge/) - Contains the InsightEdge open source edition.
* [gigaspaces/insightedge-enterprise](https://hub.docker.com/r/gigaspaces/insightedge-enterprise/) - Contains the InsightEdge Enterprise edition (requires a license key).
* [gigaspaces/xap](https://hub.docker.com/r/gigaspaces/xap/) - Contains the XAP open source edition.
* [gigaspaces/xap-enterprise](https://hub.docker.com/r/gigaspaces/xap-enterprise/) - Contains the XAP Enterprise edition (requires a license key).

# Kubernetes

InsightEdge and XAP both support the Kubernetes environment. Kubernetes can be used in any type of environment (on-premise, cloud, or hybrid) and is supported by every major cloud provider, including public cloud platforms like AWS, Microsoft Azure, GCP and others. Benefits of using the Kubernetes orchestration platform include: 

- The ability to deploy GigaSpaces-based applications in whatever environment best suits the business needs of the enterprise.
- Kubernetes synergizes with InsightEdge and XAP, simplifying the operationalizing of machine learning and transactional processing at scale.
- InsightEdge and XAP utilize key features of Kubernetes, such as cloud-native orchestration automation with self-healing, cooperative multi-tenancy, and RBAC authorization.
- Auto-deployment of data services and deep learning and machine learning frameworks, such as Apache Spark jobs, stateful Processing Units, as well as the Apache Zeppelin web notebook.





