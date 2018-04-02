---
type: post123
title: Deleting a Spark Application
categories: XAP123ADM,PRM
weight: 800
parent: admin-insightedge.html
---
 
  
**To delete a Spark Application:** 
 
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

`DELETE /spark/applications/{id}`

*Description:*

Deletes the spark application resource by name.

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/spark/applications/resources/app1'
```
 
*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Spark driver id | Yes  |
 

{{%/tab%}}

{{% /tabs %}}
