---
type: post122
title:  Extending the REST Manager API
categories: XAP122ADM, PRM
parent: xap-manager-rest.html
weight: 200
---
 
The REST Manager API is extensible so that custom methods can be added. Developers can implement a plain Java class with {{%exurl "JAX-RS" "https://github.com/jax-rs"%}} annotations.

# Limitations

{{%note "Note"%}}
This feature is under active development, with new functionality added regularly. The list of limitations is updated accordingly.
{{%/note%}}

The `JAX-RS` API is used for extension support because it is a well-known standard and commonly used by developers. However, some of its features are not yet supported. Note the following limitations:

* Supported operations: `@GET`, `@PUT`, `@POST`, `@DELETE`.
* Supported parameters: `@QueryParam` support String and primitive types (e.g. `int`).
 * `@Context` is currently supported only for fields (no support for constructors or method args).
 * `@Context` is currently supported only for fields of type `Admin`.
* The following JAX-RS are _**not**_ supported: `@Consumes`, `@Produces`, `@FormParam`, `@HeaderParam`, `@CookieParam`, `@MatrixParam`, `@OPTIONS`, `@HEAD`, `@Context` (parameter, constructor).

# Usage

Follow the instructions below to create an extension for the REST Manager API:

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

This class maps an HTTP `GET` operation in the `/demo/report` path to a `report` method. It accepts a query parameter, and uses an injected `Admin` instance to perform user-defined code (in this case, a custom report).

# Response

Currently the Response supports only String as a body. For other Response types, import `org.openspaces.admin.rest.Response`. 

For example:
```java
import org.openspaces.admin.rest.Response

@CustomManagerResource
@Path("/response")
public class ResponsePluggableOperationTest {

    @GET
    @Path("/fullOkResponse")
    public Response jaxresponse() {
        return Response.ok().entity("good").header("headername","headervalue").build();
    }

    @GET
    @Path("/badResponse")
    public Response badResponse() {
         return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).header("headername","headervalue").build();
    }
}
```
# Security

To define security privileges for a custom method, you have to import `org.openspaces.admin.rest.PrivilegeRequired` and `org.openspaces.admin.rest.RestPrivileges`, and use `@PrivilegeRequired`.
The `@PrivilegeRequired` annotation accepts a `RestPrivileges` enum that corresponds to the Security privileges. 

{{%refer%}}
For more information about security, see the [Security Guide](../security/).
{{%/refer%}}

For example:
```java
import org.openspaces.admin.rest.PrivilegeRequired
import org.openspaces.admin.rest.RestPrivileges

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

By default, the XAP Manager scans `$XAP_HOME/lib/platform/manager/plugins` for pluggable operation classes. You can override this using the `com.gs.manager.rest.plugins.path` system property.


