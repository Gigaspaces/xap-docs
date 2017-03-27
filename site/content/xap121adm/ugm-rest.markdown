---
type: post121
title:  REST API
categories: XAP121ADM, PRM
parent: ugm.html
weight: 200
---



{{%warning%}}
This page Is under construction
{{%/warning%}}

The XAP management REST API was built with {{%exurl "Swagger" "http://swagger.io/"%}}. 
Swagger provides the typical information for each operation (parameters, responses, etc.).
You can experiment with it by setting parameters and clicking the `Try it out!` button from within the documentation. 
The operation will be invoked, and you'll see the response code and body, as well as the `curl` command and request URL which were used to invoke the command.
You can also download the `yaml` specification and use Swagger's `Online Editor` to generate a client in your favourite language.

The Swagger interface is part of the UGM and is started with the following command:

{{%tabs%}}
{{%tab Windows%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%tab Unix%}}
```bash
gs-agent.sh --manager-local
```
{{%/tab%}}
{{%/tabs%}}


# Operations

## Hosts

The following host operations are available :

![image](/attachment_files/rest-admin/hosts.png)

For details on request parameters and url see your local UGM page: {{%exurl "Host Operations" "http://localhost:8090/v1/index.html#/Hosts"%}}

## Containers

![image](/attachment_files/rest-admin/containers.png)

For details on request parameters and url see your local UGM page: {{%exurl "Container Operations" "http://localhost:8090/v1/index.html#/Containers"%}}

## Spaces

![image](/attachment_files/rest-admin/containers.png)

For details on request parameters and url see your local UGM page: {{%exurl "Space Operations" "http://localhost:8090/v1/index.html#/Spaces"%}}

## Deployments

![image](/attachment_files/rest-admin/deployments.png)

For details on request parameters and url see your local UGM page: {{%exurl "Deployment Operations" "http://localhost:8090/v1/index.html#/Deployments"%}}


## Requests

![image](/attachment_files/rest-admin/requests.png)

For details on request parameters and url see your local UGM page: {{%exurl "Request Operations" "http://localhost:8090/v1/index.html#/Requests"%}}


## Information

![image](/attachment_files/rest-admin/information.png)

For details on request parameters and url see your local UGM page: {{%exurl "Information Operations" "http://localhost:8090/v1/index.html#/Information"%}}



# Specification

(http://localhost:8090/v1/xap-manager-api.yaml)


# Online Editor

(http://editor.swagger.io/#!/)




