---
type: post
title:  Running a Spark Job in Kubernetes
parent: kubernetes-overview.html
categories: EARLY_ACCESS
weight: 200
---

The InsightEdge platform provides a first-class integration between Apache Spark and the XAP core data grid capability. This allows hybrid/transactional analytics processing by co-locating Spark jobs in place with low-latency data grid applications. InsightEdge includes a full Spark distribution. 

Apache Spark 2.3.x has native Kubernetes support. Users can run Spark workloads in an existing Kubernetes 1.7+ cluster and take advantage of Apache Spark’s ability to manage distributed data processing tasks.

The Spark submission mechanism creates a Spark driver running within a Kubernetes pod. The driver creates executors running within Kubernetes pods, connects to them, and executes application code. Using InsightEdge, application code can connect to a Data Pod and interact with the distributed data grid.


## Kubernetes Container Image

InsightEdge Docker image is built to be run in a container runtime environment that Kubernetes supports. This Docker image is used in the examples below to demonstrate how to submit the `SparkPi` example and the `SaveRDD` basic InsightEdge example.


The Spark configuration property `spark.kubernetes.container.image` is required when submitting Spark jobs. This configuration has been applied to the examples.

```
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:dev-latest
```


### Kubernetes master URL

The Kubernetes master URL can be obtain by using the Kubernetes command-line tool. This will print out the URL to be used in the following examples when submitting Spark jobs to Kubernetes scheduler.

```
kubectl cluster-info
```
E.g. output: `Kubernetes master is running at https://192.168.99.100:8443`

### Kubernetes Service Accounts

In Kubernetes clusters with RBAC enabled, users can configure Kubernetes RBAC roles and service accounts used by the various Spark on Kubernetes components to access the Kubernetes API server.

Spark on Kubernetes supports specifying a custom service account to be used by the driver pod through the configuration property passed as part of the submit command.

```
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark
```

To create a custom service account, run the following command:
```
kubectl create serviceaccount spark
```

To grant a service account role, use the kubectl to create a role in the `default` namespace and grant it to the `spark` service account created above. For the examples that follow, run the command:

```
kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
```

## Submit Spark Jobs with InsightEdge Submit

The `insightedge-submit` script is located under the extracted location of InsightEdge and inside the `insightedge/bin` directory. This script is similar to the `spark-submit` command used by Spark users to submit Spark jobs. We will demonstrate the usage of submitting a pure Spark example and an InsightEdge example by calling this script.

### Submit the Spark Pi example in Kubernetes

```
./insightedge-submit \
--master k8s://https://192.168.99.100:8443 \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:dev-latest \
local:///opt/gigaspaces/insightedge/spark/examples/jars/spark-examples_2.11-2.3.1.jar

```

Notice that in the above example we specify a jar with a specific URI with a scheme of `local://`. This URI is the location of the example jar that is already in the Docker image. If your application’s dependencies are all hosted in remote locations like HDFS or HTTP servers, they may be referred to by their appropriate remote URIs. E.g. `https://path/to/examples.jar`

See Spark’s documentation for more configurations that are specific to Spark on Kubernetes. For example, to specify the driver pod name add the following configuration option to the submit command:

```
--conf spark.kubernetes.driver.pod.name=spark-pi-driver
```


### Submit the SaveRDD example in Kubernetes

```
./insightedge-submit \
--master k8s://https://192.168.99.100:8443 \
--deploy-mode cluster \
--name i9e-saveRdd \
--class org.insightedge.examples.basic.SaveRdd \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:dev-latest \
--conf spark.insightedge.space.lookup.locator=insightedge-space-xap-manager-hs \
local:///opt/gigaspaces/insightedge/examples/jars/insightedge-examples.jar

```

To query the number of Objects in the `insightedge-space` use the insightedge CLI tool. This should show that there are `100,000` objects of type `org.insightedge.examples.basic.Product`. The port `8090` is exposed as the internal endpoint `insightedge-space-xap-manager-service:30890 TCP` and should be specified as part of the `--server` option:

```
../../bin/insightedge --server 192.168.99.100:30890 space info --type-stats insightedge-space
```

### InsightEdge Space Lookup Locator

For the application to connect to the `insightedge-space`, the Space lookup locator needs to be set with the headless service name. This is required when running on a Kubernetes cluster (not a minikube).

```
--conf spark.insightedge.space.lookup.locator=insightedge-space-xap-manager-hs
```
