---
type: post122
title:  Deploy with REST
categories: XAP122ADM, PRM
parent: administration-tools.html
weight: 220
canonical: auto
---

  

# deploy resource

## Syntax

```bash
curl -X POST [name] [resource jar/war/zip file / directory location / name] [url/deployments]
```

## Description

A resource (Processing Unit) can be easily deployed onto the Service Grid. In order to deploy a resource, the Processing Unit must follow the [processing unit directory structure]({{%currentjavaurl%}}/the-processing-unit-structure-and-configuration.html).
Before deploying the processing unit you will need to jar it and then specify that jar file as the resource to the `POST` command. The deployment process will upload the jar file to all the GSMs it finds and unpack it under the `deploy` directory. It will then issue the deploy command.
 

## Options

|Option|Description|Mandetory |
|:-----|:----------|-----------|
|name              | Deployment unique name  | YES |
|resource          | Name of resource which contains binary files for deployment  jar/war/zip| YES |
|topology          | Deployment topology |NO|
|sla               | SLA definitions|NO|
|contextProperties | Properties overriding pu.properties|NO|
 
 
Examples:

The following deploys a processing unit jar file named `data-processor.jar` using the `sync_replicated` cluster schema with 2 instances.

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{
   "name": "data-processor", 
   "resource": "data-processor.jar", 
   "topology": { 
     "schema": "sync_replicated", 
     "instances": 2, 
     "partitions": 0,  
     "backupsPerPartition": 0 
   },  
   "sla": {  
     "requiresIsolation": true, 
     "zones": [ 
       "green" 
     ], 
     "maxInstancesPerVM": 1, 
     "maxInstancesPerMachine": 1 
   }, 
   "contextProperties": {}  
 }' 'http://localhost:8090/v1/deployments'
```
 

 

# deploy space

## Syntax
 
 ```bash
 curl -X POST  [url/spaces] [space name] [options]
 ```
 
## Description
 
A Space only Processing Unit can be easily deployed onto the Service Grid.
 
## Options
 
|Option|Description|Mandatory   | Default   |
|:-----|:----------|:-----------:|:----------|
|name              |  Name of the space         |  YES  | |
|partitions        |  Number of partitions         | NO    | 0 |
|backups           | true if each partition should have a backup, false otherwise          |  NO| false |
|requiresIsolation | true if each instance should be provisioned in an isolated container, i.e. without any other instances at the same container, false otherwise.         | NO | false |
  
  
Examples:
 
The following deploys a simple space named `mySpace`:
 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces?name=mySpace&partitions=0&backups=false&requiresIsolation=false'
```
 
The following deploys a space named `mySpace`, using 4 partions with each a backup and the isolation level set to true, requesting that the instances need to be deployed on different machines. 
```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v1/spaces?name=mySpace&partitions=4&backups=true&requiresIsolation=true'
```
 

 
# undeploy resource

## Syntax


```bash
curl -X DELETE [url]/deployments/resource_name
```

## Description

Undeploys an [application]({{%currentjavaurl%}}/deploying-onto-the-service-grid.html#Application Deployment and Processing Unit Dependencies) from the service grid, while respecting pu dependency order.

Example:

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v1/deployments/myPu'
```
 
 
 
