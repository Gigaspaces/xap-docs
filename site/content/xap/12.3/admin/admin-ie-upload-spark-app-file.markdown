---
type: post123
title: Uploading a Spark Application File
categories: XAP123ADM,PRM
weight: 600
parent: admin-insightedge.html
---
 
  

**To upload a Spark Application:** 

<br>
 
{{%tabs%}}

{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}

*Path*

`POST /spark/applications`

*Description:*

Submitting a Spark Application

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "mainClass": "string", \ 
   "applicationJar": "string", \ 
   "applicationArguments": [ \ 
     "string" \ 
   ], \ 
   "name": "string", \ 
   "sparkProperties": {} \ 
 }' 'http://localhost:8090/v2/spark/applications'
```
 
*Example Response:*

```bash
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| mainClass |  |   |
| applicationJar |  | Yes |
| applicationArguments |  |   |
| name | |  |
| sparkProperties |  |   |

 
{{%/tab%}}

{{% /tabs %}}
