---
type: post123
title: Viewing the Spark Job Status
categories: XAP123ADM,PRM
weight: 400
parent: admin-insightedge.html
---
 
  
**To view the Spark Job Status:**

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

`GET /spark/applications/{id}`

*Description:*


*Example Request:*

```bash
curl -X GET --header 'Accept: text/plain' 'http://localhost:8090/v2/spark/applications/application1'
```
 
*Example Response:*

```bash
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | The id of the Spark driver.| Yes |

 
{{%/tab%}}

{{% /tabs %}}
