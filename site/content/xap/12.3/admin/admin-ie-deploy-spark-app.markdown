---
type: post123
title: Deploying a Spark-Based Application
categories: XAP123ADM,PRM
weight: 300
parent: admin-insightedge.html
---
 
  

{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface, Web Management Console, GigaSpaces Management Center, or Administration API administration tools.
{{%/note%}}

**To deploy a Spark-based application:**
 
{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}
-->

{{%tab "REST Manager API"%}}

*Prerequisites*

An application resource file must be uploaded. Refer to [Uploading a Spark Application File](./admin-ie-upload-spark-app-file.html). You can use the following sample application file: `<XAP-ROOT>/insightedge/examples/jars/insightedge-examples.jar`

*Path*

`POST /spark/applications`

*Description:*

This option deploys a Spark-based application in the InsightEdge environment.


*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \
   "mainClass": "org.insightedge.examples.basic.SaveRdd", \
   "applicationJar": "insightedge-examples.jar", \
   "name": "myApp" \
 }' 'http://localhost:8090/v2/spark/applications'
```

*Example Response:*

The ID of the request to track with a status code: 202
```bash
2
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| mainClass | The main class of the application. | Yes |
| applicationJar | The application JAR file name. | Yes |
| name | The Spark application name. | No |
| applicationArguments | The Spark application arguments. | No |
| sparkProperties | The Spark context properties. | No |


{{%/tab%}}

{{% /tabs %}}
