---
type: post123
title:  Frequently Asked Questions 
categories:  XAP122GS
weight: 700
---

# Upgrading 

### If I have XAP Premium Version 12.1

**Q: What version should I download, and  where can I find it?**

A: You should download the XAP In-Memory Data Grid package for version 12.2, from {{%exurl "here""https://www.gigaspaces.com/download-center"%}}.

**Q: Do I need a new license?**

A: Yes, but there is no charge. Contact GigaSpaces Support so they can generate your updated license for version 12.2.

### If I have XAP Enterprise Version 12.1

**Q: What version should I download, and where can I find it?**

A: You should download the XAP In-Memory Data Grid package for version 12.2, from {{%exurl "here""https://www.gigaspaces.com/download-center"%}}.

**Q: Do I need a new license?**

A: Yes, but there is no charge. Contact GigaSpaces Support so they can generate your updated license for version 12.2.

**Q: Is the software backwards compatible with previous versions?**

A: Yes, within major versions. The client code from any version of 12.x can be used against any 12.x version of the server code. 


# Administration 

**Q: I already use Apache ZooKeeper in another application. Can I use it for InsightEdge/XAP?**

A: Not with the current release. However, the ability to use an existing Apache Zookeeper deployment for XAP and InsightEdge provisioning is planned for a future release. 

**Q: If I already have a different version of Apache Spark, can I use it instead of the one that comes with InsightEdge?**

A: InsightEdge supports Apache Spark versions 1.6, 2.1, and 2.2. If you have one of these Spark versions, you can use by setting the SPARK_HOME variable. 
Read this page on the GigaSpaces documentation website for details: [Common environment variables](https://docs.gigaspaces.com/xap/12.2/started/common-environment-variables.html#insightedge-environment-variables) 

**Q: If I already have a different version of Scala, can I use it instead of the one that comes with InsightEdge?**

A: You can use any version of Scala that is recommended by Apache Spark for the versions that are supported by InsightEdge (1.6, 2.1, and 2.2). Consult {{%exurl "Apache Spark""http://spark.apache.org"%}} for more information. 

**Q: What is the End of Life policy for InsightEdge?**

A: InsightEdge and  XAP products have the same End of Life (EOL) policy. 

# Application Development

**Q: Does the introduction of the InsightEdge Platform mean that GigaSpaces is scaling back efforts to fix existing XAP issues and develop new XAP features, such as repartitioning the in-memory data grid?**

A: No. XAP is the engine of GigaSpace’s InsightEdge Platform, and we will continue to support and enhance this fundamental component of GigaSpaces’ IMC product line. In every release, you can expect more than half of our product innovations to focus on pure XAP functionality. 

	

**Q: Can the XAP memory grid, together with MemoryXtend, serve as the storage layer for Apache Spark? I want to avoid using an external database and perform all of my queries on top of InsightEdge.**

A: Yes. This is one of the intended uses of the MemoryXtend feature. For "fast data lake" use cases, customers need to extend their data footprint beyond a few terabytes without having to shuffle huge amounts of data from Hadoop. Additionally, InsightEdge provides the *only* Spark distribution that lets you natively persist RDD or DataFrames from Spark on SSD/flash memory. 

**Q: Are you planning to introduce a three- tiered storage model that includes a database, SSD and heap? Will users be able to fine tune the balance between fast fetch and capacity?**

A: Yes, this functionality is planned for 2018. We will introduce a "proactive" caching capability that allows users to intelligently tier hot/warm/cold data between RAM/SSD/disk. 


**Q: Can I get time series queries in InsightEdge? What is the best way to implement it?**

A: Yes. You can store any RDD/DataFrame from Apache Spark as a Space object or document. One way to implement time series analysis is by leveraging a time series library on Spark, and saving the TimeSeries RDD/DataFrame to the XAP in-memory data grid. The following is a good library to use:{{%exurl "A New Library for Analyzing Time-Series Data with Apache Spark" "https://blog.cloudera.com/blog/2015/12/spark-ts-a-new-library-for-analyzing-time-series-data-with-apache-spark/"%}} 
