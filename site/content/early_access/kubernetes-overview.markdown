---
type: post
title:  InsightEdge Kubernetes Integration
parent: none
categories: EARLY_ACCESS
weight: 100
---



This section describes how to deploy the GigaSpaces data grid and InsightEdge in a Kubernetes environment. The integration is packaged as a [Helm chart](https://docs.helm.sh/developing_charts/#charts), and is located at `$XAP_HOME/tools/kubernetes/charts/xap`.

**Note:** The GigaSpaces Kubernetes integration is under active development and is subject to change. You can use the XAP Helm chart to deploy both the data grid and the InsightEdge platform.

## Prerequisites

Before beginning to work with the data grid and InsightEdge, ensure that you have the following installed on your local machine or a VM:

* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
* [Helm](https://docs.helm.sh/using_helm/#quickstart-guide)
* Kubernetes cluster (cloud, on-premise, or local via [minikube](https://kubernetes.io/docs/setup/minikube/))

## Helm Chart Location

Helm supports installing charts in [various forms](https://docs.helm.sh/helm/#helm-install). A Helm chart can be used in a variety of formats and locations; packaged, unpackaged, accessed via a remote URL or even in a chart repository. The `xap` Helm chart is provided as an unpackaged chart in the `$XAP_HOME/tools/kubernetes/charts` folder. All of the commands described here should be executed from that folder. If you prefer to store the chart in an alternate location, you must specify the path to the chart so that Helm can locate it.
