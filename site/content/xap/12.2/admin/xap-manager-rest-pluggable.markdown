---
type: post122
title:  Pluggable Manager Operations
categories: XAP122ADM, PRM
parent: xap-manager-rest.html
weight: 200
---
 
The XAP Manager RESTful API can be extended by implement a plain Java class with {{%exurl "JAX-RS" "https://github.com/jax-rs"%}} annotations. 

# Usage

Here is an example:

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

This class maps an HTTP `GET` operation on path `/demo/report` to a `report` method. 
It accepts a query parameter and uses an injected `Admin` instance to perform some user-define code (in this case, a custom report).

To run the example, compile it and package it into a JAR file, then copy the JAR to `$XAP_HOME/lib/platform/manager/plugins` and start the Manager.

Note that some JAX-RS features are not supported - see [JAX-RS Support](#jax-rs-support) below for detailed information.

# Deploying

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

NOTE: Make sure you use `org.openspaces.admin.rest.Response` and not JAX-RS Response.

# Security

To define security privilege for a custom method, you need to import:
 
- `org.openspaces.admin.rest.PrivilegeRequired`
- `org.openspaces.admin.rest.RestPrivileges`
- `@PrivilegeRequired`

The `@PrivilegeRequired` annotation accepts a `RestPrivileges` enum which corresponds to the Security privileges (For more information on security see [Security Guide](../security/)).

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

## Supported Annotations

The following JAX-RS annotations are supported:

* HTTP Operations: `@GET` `@PUT`, `@POST`, `@DELETE`
* Parameters: `@QueryParam`, `@PathParam`, `@DefaultValue` 
   * Supported types: Java primitive types ('int', 'long', etc.) and `String`
* `@Context`
   * Fields only (No support for constructors or method arguments)
   * Supported types: `Admin`

## Unsupported Annotations

The following JAX-RS annotations are not supported:

* HTTP Operations: `@OPTIONS`, `@HEAD`
* Parameters: `@FormParam`, `@HeaderParam`, `@CookieParam`, `@MatrixParam`
* `@Consumes`, `@Produces`
