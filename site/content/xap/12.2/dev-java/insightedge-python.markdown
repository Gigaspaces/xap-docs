---
type: post122
title:  Python
categories: XAP122, OSS
parent: insightedge-apis.html
weight: 650
---

InsightEdge has a Python API available via PySpark. Its functionality is limited to the DataFrame API.


# Interactive Use

There are two options to analyze data interactively in Python: 

- [Zeppelin](../started/insightedge-zeppelin.html)<br>
- command line shell

# Zeppelin Notebook

To develop notebooks in Python, use the `%pyspark` interpreter in the Zeppelin web notebook. See the `InsightEdge python example` notebook as a reference example.

# Command Line Shell

To start the command line shell, run the `<XAP-HOME>/insightedge/bin/insightedge-pyspark` script.

For example, start InsightEdge in demo mode:

{{%tabs%}}
{{%tab Linux%}}
```bash
<XAP-HOME>/insightedge/bin/insightedge.sh demo
```
{{%/tab%}}

{{%tab Windows%}}
```bash
<XAP-HOME>\insightedge\bin\insightedge.cmd demo
```
{{%/tab%}}
{{%/tabs%}}

Then start the command line shell:

{{%tabs%}}
{{%tab Linux%}}
```bash
<XAP-HOME>/insightedge/bin/insightedge-pyspark --master spark://127.0.0.1:7077
```
{{%/tab%}}

{{%tab Windows%}}
```bash
<XAP-HOME>\insightedge\bin\insightedge-pyspark --master spark://127.0.0.1:7077
```
{{%/tab%}}
{{%/tabs%}}

# Saving and Loading DataFrames in Python

To operate on InsightEdge DataFrames, use the regular PySpark DataFrame API with the `org.apache.spark.sql.insightedge` format and specify Data Grid `collection` or `class` options. For example,

{{%tabs%}}
{{%tab "Python"%}}
```python
# create pyspark.sql.SparkSession
spark = SparkSession.builder.getOrCreate()

# load SF salaries dataset from file
jsonFilePath = os.path.join(os.environ["XAP_HOME"], "insightedge/data/sf_salaries_sample.json")
jsonDf = spark.read.json(jsonFilePath)

# save DataFrame to the grid
jsonDf.write.format("org.apache.spark.sql.insightedge").mode("overwrite").save("salaries")

# load DataFrame from the grid
gridDf = spark.read.format("org.apache.spark.sql.insightedge").option("collection", "salaries").load()
gridDf.show()
```
{{%/tab%}}
{{%/tabs%}}

You can also load a DataFrame backed by a DataGrid Scala class with the `class` options, for example:

{{%tabs%}}
{{%tab "Python"%}}
```python
my_class_name = ...
df = spark.read.format("org.apache.spark.sql.insightedge").option("class", my_class_name).load()
```
{{%/tab%}}
{{%/tabs%}}

# Self-Contained Applications

To develop a self-contained submittable application, use the regular PySpark and configure InsightEdge settings in `SparkConf`:

{{%tabs%}}
{{%tab "Python"%}}
```python
from pyspark.conf import SparkConf
from pyspark.sql import SparkSession

conf = SparkConf()
conf.setAppName("InsightEdge Python Example")
conf.set("spark.insightedge.space.name", "insightedge-space")
conf.set("spark.insightedge.space.lookup.group", "insightedge")
conf.set("spark.insightedge.space.lookup.locator", "127.0.0.1:4174")

spark = SparkSession.builder.config(conf=SparkConf()).getOrCreate()

```
{{%/tab%}}
{{%/tabs%}}

The complete source code is available at `<XAP-HOME>/insightedge/examples/python/sf_salaries.py`.

The application can be submitted with `insightedge-submit` script, for example:

{{%tabs%}}
{{%tab Linux%}}
```bash
<XAP-HOME>/insightedge/bin/insightedge-submit <XAP-HOME>/insightedge/examples/python/sf_salaries.py
```
{{%/tab%}}

{{%tab Windows%}}
```bash
<XAP-HOME>\insightedge\bin\insightedge-submit </XAP-HOME/insightedge\examples\python\sf_salaries.py
```
{{%/tab%}}
{{%/tabs%}}
