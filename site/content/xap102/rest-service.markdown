---
type: post102
title:  Overview
categories: XAP102
parent: rest-service-overview.html
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}

The REST service is a Processing Unit that once it is deployed it starts an embedded jetty server along with a REST service allowing interactions with the Space via the [REST API](./rest-service-api.html).

The Space's name and a port number must be specified. In case of multiple instances of the REST Processing Unit, the port will be the specified port plus a running number starting with zero.

{{% note %}}
The REST service is not supported with embedded space thus we recommend using it as a separate processing unit.
{{%/note%}}


# Deploment Options

<br/>

### Deploy via CLI
{{%refer%}} See full instructions [here]({{%currentadmurl%}}/rest-deploy-command-line-interface.html) {{%/refer%}}

<br/>

### Deploy using a template
A pre-configured processing unit template is provided and can be found at `{XAP_HOME}/deploy/templates/rest`

<br/>

### Deploy REST Service as part of a custom Processing Unit
A REST Service can be started as part of a custom processing unit by specifying the `<os-core:rest >` annotation as following:


```xml
<os-core:space-proxy id="theSpace" name="theGigaSpace">
<os-core:rest id="mySpaceRestService" giga-space="theGigaSpace" port="8081" />
```

or


```xml
<os-core:rest id="mySpaceRestService" space-name="mySpace" port="8081" lookup-groups="myGroups" />
```


#### \<os-core:rest\> attributes


| Attribute name | Use | Description |
|:-----|:----------|----|
| port | required | The port which the rest service will be available on. {{<wbr>}}If there are multiple instances, the port for each instance will be port+runningNumber (starting from 0) |
| giga-space | required* | Reference to GigaSpace |
| space-name |  required* | Name of the Space that the rest should connect to. |
| lookup-groups | optional | The lookup groups to be used when looking for the specified space. {{<wbr>}} A comma separated list of group names. {{<wbr>}}Use with space-name attribute only. |
| lookup-locators | optional | The lookup locators to be used when looking for the specified space. {{<wbr>}} A comma separated list of host:port. {{<wbr>}}Use with space-name attribute only. |

*giga-space and space-name attributes can not be used together.


# Date Support

Date string is parsed with a different parser from XAP's parser. Therefore, a date format must be defined.

We provide a default format `yyyy-MM-dd HH:mm:ss` but you can override it by providing the `rest.datetime_format` property in the deployment step or by specify it in the pu.xml:


```xml
<os-core:rest id="mySpaceRestService" space-name="mySpace" port="8081" lookup-groups="myGroups">
  <os-core:properties>
    <props>
      <prop key="datetime_format">yyyy-MM-dd HH:mm:ss</prop>
    </props>
  </os-core:properties>
</os-core:rest>
```

# Nested SpaceDocument

The REST API supports writing nested SpaceDocument.

The JSON representation is:

```json
{
  "typeName": "theTypeOfTheNestedObject-SpaceDocument",
  "version": 0, //optional
  "transient": true/false, //optional
  "properties": {
    "prop1": "val1",
    "prop2": "val2"
  }
}
```


# API and Playground

Once the REST Processing Unit is deployed, you can browse the API and test its methods by accessing the main page at: `http://<rest-instance-host-address>:<rest-port>/`.

The page should look like:

![rest-api-jsondoc.png](/attachment_files/rest-api-jsondoc.png)

{{%refer%}} Browse the offline API documentation [here](./rest-service-api.html) {{%/refer%}}

# Limitations

*   In case that the ports were in use, the deployment will fail.
*   Not supported with embedded space thus we recommend using it as a separate processing unit.
*   The API support writing for Document objects only.
*   Supported collections: Arrays and Lists of SpaceDocument.
*   Currently there is no support for connecting to a secured space.

# Removed APIs

The REST service was a dependant project until version 10.1.0.
Since then, it is an official part of the product.

Tasks API were removed, Pojo and Document API were merged with no support for Pojo writing.


# WAR Deployment and Customization

Another option is to deploy the REST service as [Web Processing Unit](./web-application-overview.html).

In order to do so, you will need to do the following:

1. Clone the project from Github: `git clone https://github.com/GigaSpaces/RESTData.git`

2. Edit the file that is located under src/main/webapp/WEB-INF/config.properties to include your space's properties

3. Package the project using maven: `mvn package -P standalone-war` {{<wbr>}}This will run the unit tests and package the project to a war file located at target/RESTData.war

4. Deploy the war file as a Web Processing Unit

For example:

```bash
#Specify the space parameters using the following properties:
spaceName=mySpace
lookupGroups=myGroups
#lookupLocators=
#datetime_format=
```
