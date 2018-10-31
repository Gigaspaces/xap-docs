---
type: post140
title: Uploading a Spark Application File
categories: XAP140ADM,PRM
weight: 600
parent: admin-insightedge.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface, Web Management Console, GigaSpaces Management Center, or Administration API administration tools.
{{%/note%}}  

**To upload a Spark Application:** 

 
{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}
-->

{{%tab "REST Manager API"%}}

*Path*

`POST /spark/applications/resources`

*Description:*

This option uploads a Spark application resource file. YOu can use the example application file provided in the following directory: `<XAP-ROOT>/insightedge/examples/jars/insightedge-examples.jar`

*Example Request:*

```bash
curl -X PUT --form file=@<XAP-HOME>/insightedge/examples/jars/insightedge-examples.jar http://localhost:8090/v2/spark/applications/resources
```
 
*Example Response:*

A URL to the uploaded application resource file

```bash
http://localhost:8090/v2/spark-applications/insightedge-examples.jar
```

{{%/tab%}}

{{% /tabs %}}
