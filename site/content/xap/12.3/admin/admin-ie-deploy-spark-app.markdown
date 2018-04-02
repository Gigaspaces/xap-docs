---
type: post123
title: Deploying a Spark-Based Application
categories: XAP123ADM,PRM
weight: 300
parent: admin-insightedge.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

_Not yet available when using the **Command Line Interface**._

<br>
 
{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}
-->

{{%tab "REST Manager API"%}}

*Prerequisite*
An application resource file needs to be uploaded. Refer to [Uploading a Spark Application File](./admin-ie-upload-spark-app-file.html)
For example, you can try out the application file: <XAP-ROOT>/insightedge/examples/jars/insightedge-examples.jar

*Path*

`POST /spark/applications`

*Description:*


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
| mainClass | The main class of the Application | Yes |
| applicationJar | The application jar file name | Yes |
| name | The Spark application name | No |
| applicationArguments | The Spark application arguments | No |
| sparkProperties | The Spark context properties | No |


{{%/tab%}}

{{% /tabs %}}
