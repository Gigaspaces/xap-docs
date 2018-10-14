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

This topic explains how to run the Apache Spark `SparkPi` example, and the InsightEdge `SaveRDD` example, which is one of the basic Scala examples provided in the InsightEdge software package. Do the following to run these examples in Kubernetes:

1. Set the Spark configuration property for the InsightEdge Docker image.
1. Provide a URL for submitting the Spark jobs to Kubernetes.
1. Configure the Kubernetes service account so it can be used by the Driver Pod.
1. Deploy a data grid with a headless service (Lookup locator).
1. Submit the Spark jobs for the examples.

## Kubernetes Container Image

InsightEdge provides a Docker image designed to be used in a container runtime environment, such as Kubernetes. This Docker image is used in the examples below to demonstrate how to submit the Apache Spark `SparkPi` example and the InsightEdge `SaveRDD` example.


The following Spark configuration property `spark.kubernetes.container.image` is required when submitting Spark jobs for an InsightEdge application. Note how this configuration is applied to the examples in the Spark submit:

```
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:14.0-m11
```

## Getting the Kubernetes Master URL

You can get the the Kubernetes master URL using kubectl. Type the following command to print out the URL that will be used in the Spark and InsightEdge examples when submitting Spark jobs to the Kubernetes scheduler.

```
kubectl cluster-info
```

Sample output: `Kubernetes master is running at https://192.168.99.100:8443`

## Configuring the Kubernetes Service Accounts

In Kubernetes clusters with RBAC enabled, users can configure Kubernetes RBAC roles and service accounts used by the various Spark jobs on Kubernetes components to access the Kubernetes API server. 

Spark on Kubernetes supports specifying a custom service account for use by the Driver Pod via the configuration property that is passed as part of the submit command. To create a custom service account, run the following kubectl command:

```
kubectl create serviceaccount spark
```

After the custom service account is created, you need to grant a service account role. To grant a service account a Role, a RoleBinding is needed. To create a RoleBinding or ClusterRoleBinding, use the kubectl `create rolebinding` (or `clusterrolebinding` for ClusterRoleBinding) command. For example, the following command creates an `edit` ClusterRole in the `default` namespace and grants it to the `spark` service account you created above.

```
kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
```

After the service account has been created and configured, you can apply it in the Spark submit:

```
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark
```

## Deploying the Data Grid on Kubernetes

Run the following Helm command in the command window to start a basic data grid called `insightedge-space`: 

```
helm install insightedge --name insightedge-space
```

For the application to connect to the `insightedge-space` data grid, the Space lookup locator needs to be set with the headless service name. This is required when running on a Kubernetes cluster (not a minikube).

The headless service is set as the Space lookup locator as shown below in the Spark submit:

```
--conf spark.insightedge.space.lookup.locator=insightedge-space-insightedge-manager-hs
```

## Submitting Spark Jobs with InsightEdge Submit

The `insightedge-submit` script is located in the InsightEdge home directory, in `insightedge/bin`. This script is similar to the `spark-submit` command used by Spark users to submit Spark jobs. The following examples run both a pure Spark example and an InsightEdge example by calling this script.

### SparkPi Example

Run the following InsightEdge submit script for the SparkPi example. This example specifies a JAR file with a specific URI that uses the `local://` scheme. This URI is the location of the example JAR that is already available in the Docker image. If your application’s dependencies are all hosted in remote locations (like HDFS or HTTP servers), you can use the appropriate remote URIs, such as `https://path/to/examples.jar`.

```bash
./insightedge-submit \
--master k8s://https://192.168.99.100:8443 \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:14.0-m11 \
local:///opt/gigaspaces/insightedge/spark/examples/jars/spark-examples_2.11-2.3.1.jar

```

Refer to the Apache Spark documentation for more configurations that are specific to Spark on Kubernetes. For example, to specify the Driver Pod name, add the following configuration option to the submit command:

```
--conf spark.kubernetes.driver.pod.name=spark-pi-driver
```

### SaveRDD Example

Run the following InsightEdge submit script for the SaveRDD example, which generates "N" products, converts them to RDD, and saves them to the data grid. This example has the following configuration:

* The --master has the prefix `k8s://<Kubernetes Master URL>:<port>`.
* The `spark.insightedge.space.lookup.locator` is set with the headless service of the Manager Pod (&lt;release name&gt;-insightedge-manager-hs).
* The example lookup is the default Space called `insightedge-space`.
* In Kubernetes clusters with RBAC enabled, the service account must be set (e.g. `serviceAccountName=spark`).
* The `spark.kubernetes.container.image` is set with the desired Docker image (This is usually of the form `gigaspaces/insightedge-enterprise:1.0.0`).


```bash
./insightedge-submit \
--master k8s://https://192.168.99.100:8443 \
--deploy-mode cluster \
--name i9e-saveRdd \
--class org.insightedge.examples.basic.SaveRdd \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:14.0-m11 \
--conf spark.insightedge.space.lookup.locator=insightedge-space-insightedge-manager-hs \
local:///opt/gigaspaces/insightedge/examples/jars/insightedge-examples.jar

```

Use the GigaSpaces CLI to query the number of objects in the `insightedge-space` data grid. The output should show `100,000` objects of type `org.insightedge.examples.basic.Product`.

Port `8090` is exposed as the internal endpoint `insightedge-space-insightedge-manager-service:30890 TCP`, and should be specified as part of the `--server` option. Type the following CLI command:

```
../../bin/insightedge --server 192.168.99.100:30890 space info --type-stats insightedge-space
```

#### Improved Configuration Options 

{{%note%}}
These improvements are available starting from version 14.0.0 M13.
{{%/note%}}

The following simplified configuration options can be used with the `insightedge-submit` script.

##### Lookup Locator

The full notation for providing the Space lookup locator is `--conf spark.insightedge.space.lookup.locator=<release name>-<headless service name>, for example `--conf spark.insightedge.space.lookup.locator=testmanager-insightedge-manager-hs`.

You can define this information for the insightEdge submit script using the simplified sytax `--conf spark.insightedge.space.manager=<platform manager name>` that adds the configuration property `, for example `--conf spark.insightedge.space.manager=testmanager`

##### Space Name

To provide a different Space name to operate on, add the configuration property: `--conf spark.insightedge.space.name=testspace`.

For example, the following helm commands will install the following stateful sets:
testmanager-insightedge-manager, testmanager-insightedge-zeppelin, testspace-insightedge-space-*\[i\]*.

The insightedge submit command will submit the SaveRdd example with the `testspace` and `testmanager` configuration parameters.

```bash
$ helm install insightedge/ --name testmanager --set space.enabled=false

$ helm install insightedge/ --name testspace --set space.manager=testmanager --set zeppelin.enabled=false

$ ./insightedge-submit \
--master k8s://https://192.168.99.100:8443 \
--deploy-mode cluster \
--name i9e-saveRdd \
--class org.insightedge.examples.basic.SaveRdd \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
--conf spark.kubernetes.container.image=gigaspaces/insightedge-enterprise:14.0.0-m13 \
--conf spark.insightedge.space.name=testspace \
--conf spark.insightedge.space.manager=testmanager \
local:///opt/gigaspaces/insightedge/examples/jars/insightedge-examples.jar
```