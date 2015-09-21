---
type: post97
title:  REST API
categories: XAP97
parent: interoperability-overview.html
weight: 300
---


{{%wbr%}}

{{%section%}}
{{%column width="10%" %}}
![data-access.jpg](/attachment_files/web-services.jpg)
{{%/column%}}
{{%column width="90%" %}}
XAP REST interface.
{{%/column%}}
{{%/section%}}

<hr/>

The REST API exposing HTTP based interface Space. It is leveraging the [GigaSpace API](./the-gigaspace-interface.html). It support the following methods:

1. GET - can be used to perform an introduceType, readByID or a readMultiple action by a space query.
1. POST - can be used to perform a write / writeMultiple action.
1. PUT - can be used to perform a single or multiple write or update actions.
1. DELETE - can be used to perform take / takeMultiple actions either by ID or by a space query.

{{% info %}}
POST is mapped to a WriteOnly action. An exception will be thrown when trying to write an object which already exists in space.
{{% /info %}}



## Examples

{{% info %}}
Note that `space` and `locators` parameters can be passed to each request. If not, `defaultSpaceName` that is defined in config.properties will be used as `space` and `localhost` as `locators`.
In the following examples we are passing `space=myDataGrid`. `locators=localhost` will be used as it is not passed.
{{% /info %}}


- introduceType


```java
http://localhost:8080/RESTData/rest/data/Item/_introduce_type?space=myDataGrid&spaceid=id
```

- writeMultiple


```java
curl -XPOST -H "Content-Type: application/json" -d '[{"id":"1", "data":"testdata", "data2":"common", "nestedData" : {"nestedKey1":"nestedValue1"}},
{"id":"2", "data":"testdata2", "data2":"common", "nestedData" : {"nestedKey2":"nestedValue2"}},
{"id":"3", "data":"testdata3", "data2":"common", "nestedData" : {"nestedKey3":"nestedValue3"}}]'
http://localhost:8080/RESTData/rest/data/Item?space=myDataGrid
```

- count


```java
http://localhost:8080/RESTData/rest/data/Item/count?space=myDataGrid
```

- readMultiple


```java
http://localhost:8080/RESTData/rest/data/Item/_criteria?q=data2='common'
http://localhost:8080/RESTData/rest/data/Item/_criteria?q=id=%271%27%20or%20id=%272%27%20or%20id=%273%27&space=myDataGrid
```

The url is encoded, the query is "id='1' or id='2' or id='3'".

- readById


```java
http://localhost:8080/RESTData/rest/data/Item/1?space=myDataGrid
http://localhost:8080/RESTData/rest/data/Item/2?space=myDataGrid
http://localhost:8080/RESTData/rest/data/Item/3?space=myDataGrid
```

- updateMultiple


```java
curl -XPUT -H "Content-Type: application/json" -d '[{"id":"1", "data":"testdata", "data2":"commonUpdated", "nestedData" : {"nestedKey1":"nestedValue1"}},
{"id":"2", "data":"testdata2", "data2":"commonUpdated", "nestedData" : {"nestedKey2":"nestedValue2"}},
{"id":"3", "data":"testdata3", "data2":"commonUpdated", "nestedData" : {"nestedKey3":"nestedValue3"}}]' 
http://localhost:8080/RESTData/rest/data/Item?space=myDataGrid
```

See that data2 field is updated:


```java
http://localhost:8080/RESTData/rest/data/Item/_criteria?q=data2='commonUpdated'&space=myDataGrid
```

Single nested update:


```java
curl -XPUT -H "Content-Type: application/json" -d '{"id":"1", "data":"testdata", "data2":"commonUpdated", "nestedData" : {"nestedKey1":"nestedValue1Updated"}}' http://localhost:8080/RESTData/rest/data/Item?space=myDataGrid
```

See that Item1 nested field is updated:


```java
http://localhost:8080/RESTData/rest/data/Item/1?space=myDataGrid
```

- takeMultiple (url is encoded, the query is "id=1 or id=2")


```java
curl -XDELETE http://localhost:8080/RESTData/rest/data/Item/_criteria?q=id=%271%27%20or%20id=%272%27&space=myDataGrid
```

See that only Item3 remains:


```java
http://localhost:8080/RESTData/rest/data/Item/_criteria?q=id=%271%27%20or%20id=%272%27%20or%20id=%273%27&space=myDataGrid
```

- takeById


```java
curl -XDELETE "http://localhost:8080/RESTData/rest/data/Item/3?space=myDataGrid"
```

See that Item3 does not exists:


```java
http://localhost:8080/RESTData/rest/data/Item/_criteria?q=id=%271%27%20or%20id=%272%27%20or%20id=%273%27&space=myDataGrid
```

# Setup Instructions

1.Download the project from the [github repository](https://github.com/OpenSpaces/RESTData)

2.Edit "/RESTData/src/main/webapp/WEB-INF/config.properties" to include your space name, for example: `defaultSpaceName=myDataGrid`

Note: This is a default space name. You can override it by passing space parameter to the request.

3.Package the project using maven: "mvn package". This will run the unit tests and package the project to a war file located at /target/RESTData.war

4.[Deploy]({{%currentadmurl%}}/deploy-command-line-interface.html) the war file.
