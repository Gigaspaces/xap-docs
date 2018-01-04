---
type: post123
title:  REST Manager API
categories: XAP123ADM, PRM
weight: 250
parent: none
---
 
The [XAP Manager](xap-manager.html) provides a REST Manager API for managing the GigaSpaces application environment.

To begin, start a standalone XAP Manager on your machine using the following command:

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

After the XAP Manager has started, browse to {{%exurl "localhost:8090""http://localhost:8090"%}} and start working with the API. 

{{%info "Info"%}}
You must start a XAP Manager to access the REST Manager API. You can also start a cluster of XAP Managers to ensure high availability. To learn more about the XAP Manager, refer to [XAP Manager](xap-manager.html).
{{%/info%}}

The REST Manager API was built with {{%exurl "Swagger" "http://swagger.io/"%}}. Swagger provides typical information for each operation (parameters, responses, etc.). In addition, you can experiment with it by setting parameters and clicking the `Try it out!` button from within the documentation. This invokes the operation, and you can see the response code and body, as well as the `curl` command and request URL that were used to invoke the command. If you are familiar with the older Admin API, most of this is self explanatory. You can also download the `yaml` specification and use Swagger's `Online Editor` to generate a client in your favourite language.


![image](/attachment_files/rest-admin/rest-admin-1.png)


# Long-Running Operations

In the Admin API, long-running operations (for example, `deploy` and `undeploy`) are asynchronous with no future or callback. You can either track progress manually, or use various ‘andWait’ overloads (for example, `deployAndWait`). The REST Manager API  cannot use `andWait` so you must poll for completion, but each such operation behaves differently. 

Instead, the REST Manager API contains Request functionality. Each long-running operation creates a request and returns its ID, so you can poll on the request ID and check the status (running/successful/failed). 
The request exposes additional useful information:

- Who started the request (IP and user)
- When the request was started and completed
- If the request failed, an error message
- Additional properties on the request execution

By default, the system keeps up to 100,000 completed requests (configurable), and purges the oldest as needed.

#  Processing Unit Deployment  and Upload

Deploying a Processing Unit requires uploading a resource (JAR/WAR/ZIP) to the XAP Manager, but due to technical issues we currently can’t include both a binary file and a JSON payload in the same REST operation. 
Instead, there are two operations:

- Upload a resource.
- Deploy a Processing Unit, and provide a name of a previously uploaded resource.

This also means that when you undeploy a Processing Unit, the resource does not get deleted. If you want to delete it you must execute a separate REST operation (if you want to undeploy/redeploy with the same JAR. there’s no need to remove it).
Currently you cannot delete/replace a JAR file if a deployed Processing Unit is using it.

# Security

The REST component is part of the XAP Manager, and inherits the XAP Manager security configuration.
As the REST uses an HTTP protocol, it is best to configure SSL to allow for HTTPS (secure access). 

For more information about using the REST Manager API with security, refer to [REST Manager API - Security](../security/securing-the-REST-manager.html) section.


# Operations

You can perform a wide variety of operations on all of the environment components listed below. The available operations, request parameters and URLs are listed on each relevant page of the local XAP Manager. 

{{%info "Info"%}}
You must start a XAP Manager to access the pages using the links below.
{{%/info%}}

- Hosts - See the {{%exurl "Host Operations" "http://localhost:8090/v1/index.html#/Hosts"%}} page.
- Containers - See the {{%exurl "Container Operations" "http://localhost:8090/v1/index.html#/Containers"%}} page.
- Spaces - See the {{%exurl "Space Operations" "http://localhost:8090/v1/index.html#/Spaces"%}} page.
- Deployments - See the {{%exurl "Deployment Operations" "http://localhost:8090/v1/index.html#/Deployments"%}} page.
- Requests - See the {{%exurl "Request Operations" "http://localhost:8090/v1/index.html#/Requests"%}} page.
- Information - See the {{%exurl "Information Operations" "http://localhost:8090/v1/index.html#/Information"%}} page.
- Spark - See the {{%exurl "Spark" "http://localhost:8090/v1/index.html#/Spark"%}} page.

 
# Online Editor

You can download the `yaml` configuration file from  {{%exurl "http://localhost:8090/v1/xap-manager-api.yaml""http://localhost:8090/v1/xap-manager-api.yaml"%}} 
and import or copy and past the yaml content into the {{%exurl "Swagger Editor""http://editor.swagger.io/#!/"%}}.

![image](/attachment_files/rest-admin/swagger-ui.png)

From the Swagger user interface, you can generate client code in your preferred language:

![image](/attachment_files/rest-admin/generate-client-code.png)
 
