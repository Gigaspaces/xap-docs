---
type: post123
title:  Domain Service Host
categories: XAP123NET, PRM
parent: space-based-remoting-overview.html
weight: 100
---



 


The domain service host is used to host services within the hosting processing unit domain which are exposed for remote invocation. A service is an implementation of one or more interfaces which acts upon the service contract. Each service can be hosted by publishing it through the domain service host later to be invoked by a remote client.



# Defining the Contract

In order to support remoting, the first step is to define the contract between the client and the server. In our case, the contract is a simple interface. Here is an example:


```java
public interface IDataProcessor
{
    // Process a given Data object, returning the processed Data object.
    Data ProcessData(Data data);
}
```

{{% note %}}
The `Data` object should be `Serializable`
{{%/note%}}

# Implementing the Contract

Next, an implementation of this contract needs to be provided. This implementation will "live" on the server side. Here is a sample implementation:


```csharp
[SpaceRemotingService]
public class DataProcessor : IDataProcessor
{
    public Data ProcessData(Data data)
    {
    	data.Processed = true;
    	return data;
    }
}
```

# Hosting the Service in the Grid

The next step is hosting the service in the grid. Hosting the service is done on the server side using a processing unit, all types which has the \[SpaceRemotingService\] attribute, will automatically be created and hosted:


```csharp
[SpaceRemotingService]
public class DataProcessor : IDataProcessor
{
...
}
```

Alternatively, or when using a custom processing unit container, a service can be directly hosted using the `DomainServiceHost.Host.Publish`


```java
public ServiceHostProcessingUnitContainer : AbstractProcessingUnitContainer
{
    ...

    public void Initialize()
    {
        ...

        DomainServiceHost.Host.Publish(new DataProcessor());
    }
}
```

# Service Lookup Name

By default, the service will be published under the interfaces it implements, its lookup names will be the full name of the interfaces types it implements. In some scenarios it may be needed to specify a different lookup name, for instance, when there are two hosted services that implement the same interface. There are a few options to specify a different lookup name. When choosing one of the following options for alternative lookup name, the service will only be hosted under the alternative lookup names overriding the default behavior of investigating which interfaces the provided service implements.

## Service Attribute

A different lookup name can be specified by the \[SpaceRemotingService\] `LookupName` property:


```csharp
[SpaceRemotingService(LookupName="MyDataProcessor")]
public class DataProcessor : IDataProcessor
{
...
}
```

## Publish Lookup Names

When publishing a service it is possible to specify a list of lookup names to publish it under as part of the `Publish` method arguments:


```csharp
DomainServiceHost.Host.Publish(new DataProcessor(), "MyDataProcessor", "MySpecialDataProcessor");
```

## Publish For Specific Types

Alternatively, a service can be hosted under specific types instead of querying all the interfaces it implements, This can be achieved with the `Publish` method as well:


```csharp
DomainServiceHost.Host.Publish(new DataProcessor(), typeof(IDataProcessor), typeof(IMyService));
```

# Un publishing a Service

Once the processing unit that hosts the service is unloaded, all the services within that pu are also removed.
However, it is possible to explicitly un publish a service during the processing unit life cycle if needed, this is done by the `Unpublish` method, with the specific registration of the service that we want to un publish.


```csharp
IServiceRegistration registration = DomainServiceHost.Host.Publish(new DataProcessor());
...
DomainServiceHost.Host.Un publish(registration);
```

# Execution Aspects

Space based remoting allows you to inject different "aspects" that can wrap the invocation of a remote method on the client side, as well as wrapping the execution of an invocation on the server side. The different aspect can add custom logic to the execution, for instance, logging or security.

The server side invocation aspect interface is shown below. You should implement this interface and wire it to the `DomainServiceHost` (this is the component that is responsible for hosting and exposing your service to remote clients):


```csharp
public interface IServiceExecutionAspect
{
    /// <summary>
    /// Executed each time a service remote invocation is requested allowing a pluggable behavior
    /// for service execution. The aspect can decide whether to return the execution result value by
    /// setting the <see cref="IInvocationInterception.ResultValue"/> or
    /// to proceed with the execution process pipeline by using <see cref="IInvocationInterception.Proceed()"/>
    /// </summary>
    /// <param name="invocation">Object representing the invocation interception.</param>
    /// <param name="service">The service the invocations refers to.</param>
    void Intercept(IInvocationInterception invocation, Object service);
}
```

Here is an example of a security aspect implementation


```csharp
public class SecurityExecutionAspect : IServiceExecutionAspect
{
    void Intercept(IInvocationInterception invocation, Object service)
    {
        Object[] metaArguments = invocation.SpaceRemotingInvocation.MetaArgument;
        if (!ValidateCredentials(metaArguments, service))
            throw new SecurityException("Unauthorized Execution");
        invocation.Proceed();
    }

    ...
}
```

An implementation of such an aspect can be wired as follows:


```csharp
DomainServiceHost.Initialize(new ExecutionLoggingAspect(), new SecurityExecutionAspect());
```

The different execution aspects can be wired only once, and that is when the DomainServiceHost is initialized, which means before publishing any service in it.

The execution of the aspects follows a pattern of pipeline execution of all the aspects followed by the order in which they were set. Each aspect can decide whether to continue with the pipeline execution using the `invocation.Proceed()` method. It can either alter a return value of the next aspect in line by setting the `invocation.ReturnValue` or it can immediately return the execution result without continuing to the next aspect by setting the return value using the `invocation.ReturnValue` property and not calling the `invocation.Proceed()` method. The final service execution it self is an aspect which is the last one to be executed. Plugging custom aspects can decide according to the aspect implementation whether to execute the actual operation on the service or not.
