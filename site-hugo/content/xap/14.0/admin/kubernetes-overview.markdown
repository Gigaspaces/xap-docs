---
type: post140
title:  Kubernetes Integration
parent: orchestration.html
categories: XAP140ADM, PRM
weight: 100
---



This section describes how to deploy GigaSpaces products in a Kubernetes environment. The integration is packaged as a [Helm chart](https://docs.helm.sh/developing_charts/#charts). You can deploy the full InsightEdge platform, which includes the data grid, using the Helm chart located at `<home directory>/tools/kubernetes/charts/insightedge`, or just the data grid using the Helm chart located at `<home directory>/tools/kubernetes/charts/xap`.

{{%note%}}
The GigaSpaces Kubernetes integration is under active development and is subject to change. Additionally, you can use the `xap` Helm chart to deploy both the data grid and the InsightEdge platform.
{{%/note%}}

## Prerequisites

Before beginning to work with the data grid and InsightEdge, ensure that you have the following installed on your local machine or a VM:

* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
* [Helm](https://docs.helm.sh/using_helm/#quickstart-guide)
* Kubernetes cluster (cloud, on-premise, or local via [minikube](https://kubernetes.io/docs/setup/minikube/))

## Helm Chart Location

Helm supports installing charts in [a number of ways](https://docs.helm.sh/helm/#helm-install). A Helm chart can be used in a variety of formats and locations; packaged, unpackaged, accessed via a remote URL or even in a chart repository. The `xap` and `insightedge` Helm charts are provided as unpackaged charts in the `<home directory>/tools/kubernetes/charts` folder. All of the commands described here should be executed from that folder. If you prefer to store the chart in an alternate location, you must specify the path to the chart so that Helm can locate it.
