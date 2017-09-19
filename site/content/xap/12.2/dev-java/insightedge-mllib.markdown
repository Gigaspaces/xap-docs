---
type: post122
title:  Machine Learning
categories: XAP122, IEE
parent: insightedge-apis.html
weight: 700
---

This section describes how to reuse ML models with the Data Grid. You can train your model, save it to the Data Grid and then reuse it in different Spark contexts.

# Saving ML model to Data Grid

To save Spark ML/MLlib model to the Data Grid, use `saveToGrid` method on your model. The model is serialized with the default Java serialization mechanism, so the models stored on Spark workers (i.e. those that have a reference to RDD or DataFrame) are not currently supported.

For example:

* you can persist `org.apache.spark.mllib.clustering.KMeansModel` that holds a local array of cluster centers `clusterCenters: Array[Vector]`
* you cannot persist `org.apache.spark.mllib.recommendation.MatrixFactorizationModel` that holds references to `userFeatures: RDD[(Int, Array[Double])]` and `productFeatures: RDD[(Int, Array[Double])]`.

Code example:

{{%tabs%}}
{{%tab "Scala"%}}
```scala
val rdd = sc.parallelize(List(Vectors.dense(1.0, 1.0, 3.0), Vectors.dense(2.0, 0.0, 1.0), Vectors.dense(2.0, 1.0, 0.0)))
val k = 2
val maxIterations = 100
val model = KMeans.train(rdd, k, maxIterations)
model.saveToGrid(sc, "KMeansModel")
```
{{%/tab%}}
{{%/tabs%}}

# Loading ML model from Data Grid

To load an ML model from the Data Grid, use `SparkContext.loadMLModel[R]`. The type parameter `R` is an ML model class.

{{%tabs%}}
{{%tab "Scala"%}}
```scala
import org.insightedge.spark.implicits.all._
val model = sc.loadMLInstance[KMeansModel]("KMeansModel").get
```
{{%/tab%}}
{{%/tabs%}}
