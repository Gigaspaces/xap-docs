---
type: post122
title: InsightEdge
categories: XAP122I9E
weight: 50
---

{{%note "Maintenance Notice"%}}
InsightEdge is being transformed from a Spark distribution to a Unified transactional/analytics platform. This documentation was imported from the previous release as-is, and may contain some inaccuracies. We're currently reviewing and fixing it, and will remove this notice once we're done.
{{%/note%}}

# Introduction

InsightEdge is a high performance Spark distribution for low latency workloads, high availability, and hybrid transactional/analytical processing in one unified solution.

{{%align center%}}
![image](/attachment_files/insightedge_arch.png)
{{%/align%}}


# Features

{{%fpanel%}}
* Exposes Data Grid as Spark RDDs
* Saves Spark RDDs to Data Grid
* Full DataFrames and Dataset API support with persistence
* Transparent integration with SparkContext using Scala implicits
* Ability to select and filter data from Data Grid with arbitrary SQL and leverage Data Grid indexes
* Running SQL queries in Spark over Data Grid
* Data locality between Spark and Data Grid nodes
* Storing MLlib models in Data Grid
* Saving Spark Streaming computation results in Data Grid
* Geospatial API, full geospatial queries support in DataFrames and RDDs
* Interactive Web Notebook
* Python support
* Windows support
{{%/fpanel%}}
