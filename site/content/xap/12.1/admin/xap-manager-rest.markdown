---
type: post121
title:  Manager API
categories: XAP121ADM, PRM
parent: none
weight: 250
---
 
The [XAP Manager](xap-manager.html) provides a RESTful API for managing XAP environments.

Getting started is easy:

* Start a standalone Manager on your machine using the following command:

{{%tabs%}}
{{%tab Unix%}}
```bash
./gs-agent.sh --manager-local
```
{{%/tab%}}
{{%tab Windows%}}
```bash
gs-agent.bat --manager-local
```
{{%/tab%}}
{{%/tabs%}}

- After the Manager has started, Browse to {{%exurl "localhost:8090""http://localhost:8090"%}} and start playing with the API. 

{{%refer%}}
Starting a manager is required to access the RESTful API. You can also start a cluster of managers to ensure high availability. To learn more about the manager, see [XAP Manager](xap-manager.html).
{{%/refer%}}

The Manager’s RESTful API was built with {{%exurl "Swagger" "http://swagger.io/"%}}. Swagger provides typical information for each operation (parameters, responses, etc.). In addition, you can experiment with it by setting parameters and clicking the `Try it out!` button from within the documentation. The operation will be invoked, and you'll see the response code and body, as well as the `curl` command and request URL which were used to invoke the command. If you are familiar with the older Admin API, most of this will be self explanatory. You can also download the `yaml` specification and use Swagger's `Online Editor` to generate a client in your favourite language.


![image](/attachment_files/rest-admin/rest-admin-1.png)


# Long running operations
In the Admin API, long running operations (e.g. deploy, undeploy, etc.) are asynchronous with no future or callback. 
You can either track progress manually, or use various ‘andWait’ overloads (e.g. deployAndWait). 
In the REST API we cannot use andWait, so you need to poll for completion, but each such operation behaves differently. 
What we’ve done is added the notion of Request - each long running operation creates a request and returns its id, so you can poll on the request id and check it’s status (running/successful/failed). 
The request exposes additional useful information:

- Who started the request (ip and user)
- When was the request started and completed
- If the request failed, an error message
- Additional properties on the request execution

By default, the system keeps up to 100,000 completed requests (configurable), and purges the oldest as needed.

#  PU deployment  and upload

Deploying a PU requires uploading a resource (jar/war/zip) to the manager, but due to technical issues we currently can’t include both binary file and json payload in the same REST operation. 
Instead, there are two operations:

- Upload a resource
- Deploy a PU, and provide a name of a previously uploaded resource.

This also means that when you undeploy a PU, the resource does not get deleted - if you want to delete it you need to execute a separate REST operation to delete it (if you want to undeploy/redeploy with the same JAR - there’s no need to remove it).
Currently you cannot delete/replace a JAR file if there’s a deployed PU using it - in the future we’ll introduce support for hot-updating a PU and we’ll use that concept as the API.


# Security
Security is off by default, same as before, and can be enabled via configuration, same as before. When security is enabled, the REST API performs basic authentication and checks if the user has sufficient privileges to execute the operation. 
Note that since basic authentication does not encrypt user credentials, running a manager in a secured environment without SSL is a security hazard, and the system detects this and aborts. 

You can either:

- Disable SSL explicitly (not recommended)
- Enable SSL - the system will generate a certificate for you
- Enable SSL and provide a trusted certificate you own

An auto-generated certificate provides reasonable security, but if your enterprise security guidelines are more strict you’re welcome to provide your own certificate.

Finally, if you need to configure something which we don’t expose (we’re using Jetty under the hood to host the web app), you can provide your own jetty.xml file via a system property.


|Port |System property |Default |
|:----|:---------------|:-------|
|Enable/disable |com.gs.manager.rest.ssl.enabled| unset |
|Keystore path  |com.gs.manager.rest.ssl.keystore-path | |
|Keystore password|com.gs.manager.rest.ssl.keystore-password| |
|Custom config |com.gs.manager.rest.jetty.config|  |

# Operations

## Hosts

The following host operations are available :

![image](/attachment_files/rest-admin/hosts.png)

For details on request parameters and url see your local Manager page: {{%exurl "Host Operations" "http://localhost:8090/v1/index.html#/Hosts"%}}

## Containers

![image](/attachment_files/rest-admin/containers.png)

For details on request parameters and url see your local Manager page: {{%exurl "Container Operations" "http://localhost:8090/v1/index.html#/Containers"%}}

## Spaces

![image](/attachment_files/rest-admin/spaces.png)

For details on request parameters and url see your local Manager page: {{%exurl "Space Operations" "http://localhost:8090/v1/index.html#/Spaces"%}}

## Deployments

![image](/attachment_files/rest-admin/deployments.png)

For details on request parameters and url see your local Manager page: {{%exurl "Deployment Operations" "http://localhost:8090/v1/index.html#/Deployments"%}}


## Requests

![image](/attachment_files/rest-admin/requests.png)

For details on request parameters and url see your local Manager page: {{%exurl "Request Operations" "http://localhost:8090/v1/index.html#/Requests"%}}


## Information

![image](/attachment_files/rest-admin/information.png)

For details on request parameters and url see your local Manager page: {{%exurl "Information Operations" "http://localhost:8090/v1/index.html#/Information"%}}



 
# Online Editor

You can download the `yaml` configuration file from  {{%exurl "http://localhost:8090/v1/xap-manager-api.yaml""http://localhost:8090/v1/xap-manager-api.yaml"%}} 
and import or copy and past the yaml content into the {{%exurl "Swagger Editor""http://editor.swagger.io/#!/"%}} 

![image](/attachment_files/rest-admin/swagger-ui.png)

From the Swagger UI you generate client code in your favorite language:

![image](/attachment_files/rest-admin/generate-client-code.png)

