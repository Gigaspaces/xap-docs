---
type: post123
title: Terminating a Spark Job
categories: XAP123ADM,PRM
weight: 700
parent: admin-insightedge.html
---
 

**To terminate a Spark Job:** 

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

Terminating a Spark Job.

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/spark/applications/application1'
```
 
*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Spark driver id | Yes  |
 

 
{{%/tab%}}

{{% /tabs %}}
