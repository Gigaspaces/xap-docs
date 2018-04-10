---
type: post123
title:  Using Docker
categories: XAP123GS
parent: none
weight: 400
---

GigaSpaces provides Docker images for both InsightEdge and XAP. The main benefits of using Docker are:

* Getting started - If you're already familiar with Docker and taking your first steps with GigaSpaces products, you can simply pull the image and start it instead of setting up the product manually.
* Automation - Docker is designed for automating environment setup; it simplifies environment setup and tear-down, so you can easily create multiple environments for development, testing, staging, and production.
* Cloud-readiness - Using Docker greatly simplifies migrating/deploying to the cloud, as all major cloud vendors support Docker. Even if you're not planning to use the cloud in the near future, using Docker will simplify future migration to the cloud, if needed.

# Available Docker Images

The following Docker images are available on [Docker Hub](https://hub.docker.com/r/gigaspaces/):

* [gigaspaces/insightedge](https://hub.docker.com/r/gigaspaces/insightedge/) - Contains the InsightEdge open source edition.
* [gigaspaces/insightedge-enterprise](https://hub.docker.com/r/gigaspaces/insightedge-enterprise/) - Contains the InsightEdge Enterprise edition (requires a license key).
* [gigaspaces/xap](https://hub.docker.com/r/gigaspaces/xap/) - Contains the XAP open source edition.
* [gigaspaces/xap-enterprise](https://hub.docker.com/r/gigaspaces/xap-enterprise/) - Contains the XAP Enterprise edition (requires a license key).

Each Docker image provides instructions on how to use it. In general, all of the GigaSpaces Docker images can be administered using the GigaSpaces [Command Line Interface (CLI)](../admin/tools-cli.html). Use the `--help` option to get started and explore the deployed product. For example, to get started with InsightEdge Enterprise, run:

```bash
docker pull gigaspaces/insightedge-enterprise
docker run gigaspaces/insightedge-enterprise --help
```
