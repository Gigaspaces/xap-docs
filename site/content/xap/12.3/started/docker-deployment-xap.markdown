---
type: post123
title:  Docker Images
categories: XAP123GS
parent: none
weight: 400
---

GigaSpaces provides Docker images for both InsightEdge and XAP. The main benefits of using Docker are:

* Getting started - if you're already familiar with Docker and making your first steps with GigaSpaces, you can simply pull the image and start it instead of setting up the product.
* Automation - Docker is designed for automating environment setup, it can simplify environment setup and tear-down, so you can easily create multiple environments for dev, test, staging, production, etc.
* Cloud-readiness - Using docker greatly simplifies migrating/deploying on the cloud, as all major cloud vendors support Docker. Even if you're not planning to use the cloud in the near future, using Docker will simplify future migration to the cloud, if needed.

# Getting Started

The following Docker images are available on [Docker Hub](https://hub.docker.com/r/gigaspaces/):

* [gigaspaces/insightedge](https://hub.docker.com/r/gigaspaces/insightedge/) - Contains InsightEdge Open Source
* [gigaspaces/insightedge-enterprise](https://hub.docker.com/r/gigaspaces/insightedge-enterprise/) - Contains InsightEdge Enterprise edition (requires a license key)
* [gigaspaces/xap](https://hub.docker.com/r/gigaspaces/xap/) - Contains XAP Open Source
* [gigaspaces/xap-enterprise](https://hub.docker.com/r/gigaspaces/xap-enterprise/) - Contains XAP Enterprise edition (requires a license key)

Each of those images contain instructions on how to use it. In general, all images are based on the [command line interface (CLI)](../admin/tools-cli.html), so you can use the `--help` option to get started and explore. For example, to get started with InsightEdge, run:

```bash
docker pull gigaspaces/insightedge-enterprise
docker run gigaspaces/insightedge-enterprise --help
```
