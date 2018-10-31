---
type: post140
title:  Extending the REST Manager API
categories: XAP140ADM, PRM
parent: xap-manager-rest.html
weight: 200
---
 
The REST Manager API is extensible so that custom methods can be added. Developers can implement a plain Java class with {{%exurl "JAX-RS" "https://github.com/jax-rs"%}} annotations.

# Sample Implementation

Follow the instructions below to create a sample extension for the REST Manager API:

1. Create a class and annotate it with `com.gigaspaces.manager.rest.CustomManagerResource`.
1. Create a method for each path you wish to intercept, and annotate it with an HTTP operation (e.g. `@GET`) and a `@Path` annotation with the relevant path.
1. Use JAX-RS parameter annotations (e.g. `@QueryParam`) to map HTTP request parameters to your method.
1. If you wish to use `Admin`, create an appropiate field and annotate it with JAX-RS `@Context` annotation.

For example:

```java
@CustomManagerResource
@Path("/demo")
public class BasicPluggableOperationTest {
    @Context Admin admin;

    @GET
    @Path("/report")
    public String report(@QueryParam("hostname") String hostname) {
        Machine machine = admin.getMachines().getMachineByHostName(hostname);
        return "Custom report: host=" + hostname + 
                ", containers=" + machine.getGridServiceContainers() + 
                ", PU instances=" + machine.getProcessingUnitInstances();
    }
}
```

This class maps an HTTP `GET` operation in the `/demo/report` path to a `report` method. It accepts a query parameter, and uses an injected `Admin` instance to perform user-defined code (in this case, a custom report).  For example, `http://localhost:8090/v2/demo/report?hostname=mypc`.

To run the example, compile it and package it into a .jar file, then copy the .jar to `$XAP_HOME/lib/platform/manager/plugins` and start the XAP Manager.

Note that some JAX-RS features are not supported - see [JAX-RS Support](#jax-rs-support) below for detailed information.

# Configuration

When the XAP Manager starts, it scans the `$XAP_HOME/lib/platform/manager/plugins` for classes in the jar files with the JAX-RS annotations and registers them.
You can override the location using the following system property:

```bash
com.gs.manager.rest.plugins.path="pathToJar"
```

# Response

In the example above the method returns a String, and in addition it implicitly returns an HTTP code 200 (OK). If you need to explicitly specify the HTTP result code, use `org.openspaces.admin.rest.Response` instead of a `String`. 

For example:

```java
import org.openspaces.admin.rest.Response

@GET
@Path("/report")
public Response report(@QueryParam("hostname") String hostname) {
    Machine machine = admin.getMachines().getMachineByHostName(hostname);
	if (machine == null)
        return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity("Host not found").build();	
    String result = "Custom report: host=" + hostname + 
            ", containers=" + machine.getGridServiceContainers() + 
            ", PU instances=" + machine.getProcessingUnitInstances();
	return Response.ok().entity(result).build();
}
```

{{%note "Note"%}}
Make sure you use `org.openspaces.admin.rest.Response` and not JAX-RS Response.
{{%/note%}}

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
    @Context Admin admin;

    @PrivilegeRequired(RestPrivileges.MANAGE_GRID)
    @GET
    @Path("/getBase")
    public String getBase() {
        return "hello";
    }
}
```

# JAX-RS Support

The `JAX-RS` API is used for extension support because it is a well-known standard and commonly used by developers. The sections below list the annotations that are supported, and those that are not supported.

## Supported Annotations

The following JAX-RS annotations are supported:

* HTTP operations: `@GET`, `@PUT`, `@POST`, `@DELETE`
* Parameters: `@QueryParam`, `@PathParam`, `@DefaultValue` 
   * Supported types: Java primitive types ('int', 'long', etc.) and `String`
* Other: `@Context`
   * Fields only (No support for constructors or method arguments)
   * Supported types: `Admin`

## Unsupported Annotations

The following JAX-RS annotations are not supported:

* HTTP operations: `@OPTIONS`, `@HEAD`
* Parameters: `@FormParam`, `@HeaderParam`, `@CookieParam`, `@MatrixParam`
* Other: `@Consumes`, `@Produces`

