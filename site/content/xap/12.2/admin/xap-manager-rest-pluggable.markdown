---
type: post122
title:  Pluggable Manager Operations
categories: XAP122ADM, PRM
parent: xap-manager-rest.html
weight: 200
---
 
The XAP Manager RESTful API extensible: Developers can implement a plain Java class with {{%exurl "JAX-RS" "https://github.com/jax-rs"%}} annotations.

**NOTE**: The `JAX-RS` API is being used because it's a well-known standard, but it's not fully supported - see list of limitations below.

# Usage

1. Create a class and annotate it with `com.gigaspaces.manager.rest.CustomManagerResource`. When the XAP Manager starts, it scans the `$XAP_HOME/lib/platform/manager/plugins` for classes with that annotations and registers them.
2. For each path you wish to register to, create a method annotated with an HTTP operation (e.g. `@GET`) and a `@Path` annotation with the relevant path.
3. Each method parameter must have an annotation (e.g. `@QueryParam`) so its value can be returned at runtime.
4. Some services (e.g. `Admin`) can be injected via the `@Context` annotation.

For example:

```java
@CustomManagerResource
@Path("/demo")
public class BasicPluggableOperationTest {
    @Context public Admin admin;

    @GET
    @Path("/report")
    public String report(@QueryParam("hostname") String hostname) {
        final Machine machine = admin.getMachines().getMachineByHostName(hostname);
        return "Custom report: host=" + hostname + 
                ", containers=" + machine.getGridServiceContainers() + 
                ", PU instances=" + machine.getProcessingUnitInstances();
    }
}
```

This class maps an HTTP `GET` operation on path `/demo/report` to a `report` method. It accepts a query parameter, and uses an injected `Admin` instance to perform some user-define code (in this case, a custom report).

# Response

To use Response you will need to import 'org.openspaces.admin.rest.Response', currently the Response support only String as a body.

For example:
```java
@CustomManagerResource
@Path("/response")
public class ResponsePluggableOperationTest {

    @GET
    @Path("/fullOkResponse")
    public Response jaxresponse() {
        return Response.ok().entity("good").header("headername","headervalue").build();
    }

}
```
# Security

Security and privileges are supported.To use it import 'org.openspaces.admin.rest.PrivilegeRequired' and 'org.openspaces.admin.rest.RestPrivileges' and use '@PrivilegeRequired'.
The '@PrivilegeRequired' annotation can contain only 'RestPrivileges' enum.


For example:
```java
@CustomManagerResource
@Path("/secured/")
public class PluggableSecuredContoller {
    @Context
    Admin admin;

    @PrivilegeRequired(RestPrivileges.MANAGE_GRID)
    @GET
    @Path("/getBase")
    public String getBase() {
        return "hello";
    }

}
```




# Configuration

By default, the XAP manager scans `$XAP_HOME/lib/platform/manager/plugins` for pluggable operations classes. You can override this using the `com.gs.manager.rest.plugins.path` system property.

# Limitations

This feature is under active development, with new functionality added each sprint. This limitations list is updated with each sprint release.

* Supported operations: `GET` `@PUT`, `@POST`, `@DELETE` .
* Parameters: Currently `@QueryParam` support String and primitive types (e.g. 'int').
* `@Context` is currently supported only for fields (No support for constructors or method args).
* `@Context` is currently supported only for fields of type `Admin`.
* Operations always return 200 (Support for specifying http response code and other response settings will be added in upcoming sprints).
* The following JAX-RS are not supported: '@Consumes' , '@Produces' , '@FormParam' , '@HeaderParam' , '@CookieParam', '@MatrixParam' , '@OPTIONS' , '@HEAD' , '@Context (parameter, constructor)'.
