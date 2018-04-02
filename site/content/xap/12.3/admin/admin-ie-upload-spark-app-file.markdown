---
type: post123
title: Uploading a Spark Application File
categories: XAP123ADM,PRM
weight: 600
parent: admin-insightedge.html
---
 
  

**To upload a Spark Application:** 

_Not yet available when using the **Command Line Interface**._

<br>
 
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

Upload a Spark Application resource file.
For example, you can try uploading the example application file: `<XAP-ROOT>/insightedge/examples/jars/insightedge-examples.jar`

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
