---
type: post123
title: Viewing the Spark Job Status
categories: XAP123ADM,PRM
weight: 400
canonical: auto
parent: admin-insightedge.html
---

{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface, Web Management Console, GigaSpaces Management Center, or Administration API administration tools.
{{%/note%}} 
  
**To view the Spark job status:**


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

This option shows the status of the Spark jobs in the system.


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
| id | Provide the ID of the Spark driver for which you want to see the jobs.| Yes |

 
{{%/tab%}}

{{% /tabs %}}
