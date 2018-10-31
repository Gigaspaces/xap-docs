---
type: post140
title: Deleting a Spark Application
categories: XAP140ADM,PRM
weight: 800
parent: admin-insightedge.html
---
 
{{%note "Info"%}}
This functionality is not yet available in the Command Line Interface, Web Management Console, GigaSpaces Management Center, or Administration API administration tools.
{{%/note%}} 
 
**To delete a Spark application:** 

 
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

This option deletes the spark application resource by name.

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/spark/applications/resources/app1'
```
 
*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| id | Provide the ID of the Spark driver where you want to remove the Spark application. | Yes  |
 

{{%/tab%}}

{{% /tabs %}}
