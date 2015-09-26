---
type: post110
title:  Operations
categories: XAP110NET
weight: 300
parent: the-gigaspace-interface-overview.html
---


{{% ssummary %}}{{%/ssummary%}}


XAP provides a simple space API using the [ISpaceProxy](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_ISpaceProxy.htm) interface for interacting with the space.


The interface includes the following main operations:

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Write objects into the space:"%}}
[write](#write) one object into the space{{%wbr%}}
[writeMultiple](#writeMultiple) objects into the space{{%wbr%}}
[asynchronous write](#asynchronousWrite) to the space
{{%/panel%}}
{{%/column%}}
{{%column width="45%" %}}
{{%panel  "Change objects in space:"%}}
[change](./change-api.html) one object in space{{%wbr%}}
[changeMultiple](./change-api.html) objects in space {{%wbr%}}
{{%/panel%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="50%" %}}
{{%panel  "Reading objects from the space:"%}}
[readById](#read) from the space{{%wbr%}}
[readByIds](#readMultiple) from the space{{%wbr%}}
[read](#read) object by template from the space{{%wbr%}}
[readMultiple](#readMultiple) objects from the space {{%wbr%}}
[read asynchronous](#asynchronousRead) from the space {{%wbr%}}
[read if exists](#readIfExists) {{%wbr%}}
[read if exists by id](#readIfExists)
{{%/panel%}}
{{%/column%}}
{{%column width="45%" %}}
{{%panel  "Removing objects from the space:"%}}
[take](#take) object by template from space{{%wbr%}}
[takeById](#take) object by id from space{{%wbr%}}
[takeByIds](#takeMultiple) objects by ids from space{{%wbr%}}
[takeMultiple](#takeMultiple) objects from space {{%wbr%}}
[take asynchronous](#asynchronousTake){{%wbr%}}
[take if exists](#takeIfExists){{%wbr%}}
[take if exists by id](#takeIfExists)
{{%/panel%}}
{{%/column%}}
{{%/section%}}

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Other operations:"%}}
[aggregation](#aggregators)  across the Space{{%wbr%}}
[clear](#clear) an object type from space {{%wbr%}}
[count](#count) objects in space
{{%/panel%}}
{{%/column%}}
{{%column width="45%" %}}
{{%/column%}}
{{%/section%}}


# Simpler API

The `ISpaceProxy` interface provides a simpler space API by utilizing generics, and allowing sensible defaults. Here are some examples of the space operations as defined within `ISpaceProxy`:


```csharp
public interface ISpaceProxy {
    ILeaseContext<T>  Write(T entry);
    T Take<T>(T template);
    T Take<T>(T template, long timeout);
    T Read<T>(T template);
    T ReadById<T> (object id, object routing, ITransaction tx);
    T Read<T> (IQuery<T> query);
    T Read<T> (T template, ITransaction tx, long timeout, ReadModifiers modifiers);
    // ......
}
```
In the example above, the take operation can be performed without specifying a timeout. The default take timeout is `0` (no wait), and can be overridden when configuring the `GigaSpace` factory. In a similar manner, the read timeout and write lease can be specified.




{{%anchor write%}}

# The Write Operation
{
In order to write objects to the Space, you use the write method of the GigaSpace interface. The write method is used to write objects if these are introduced for the first time, or update them if these already exist in the space. In order to override these default semantics, you can use the overloaded write methods which accept update modifiers such as WriteModifiers.UpdateOnly.



## PONO Example

The following example writes an `Employee` object into the space:


```csharp
Employee employee = new Employee ("Last Name", 32);
employee.FirstName="first name";
ILeaseContext<Employee> lc = spaceProxy.Write(employee);
Employee e = lc.Object;
```

## SpaceDocument Example

Here is an example how you create a SpaceDocument, register it with the space and then write it into the space:


```csharp
// Create the document
DocumentProperties properties = new DocumentProperties ();

properties.Add ("CatalogNumber", "av-9876");
properties.Add ("Category", "Aviation");
properties.Add ("Name", "Jet Propelled Pogo Stick");
properties.Add ("Price", 19.99f);
properties.Add ("Tags", new String[] { "New", "Cool", "Pogo", "Jet" });

DocumentProperties prop2 = new DocumentProperties ();
prop2.Add ("Manufacturer", "Acme");
prop2.Add ("RequiresAssembly", true);
prop2.Add ("NumberOfParts", 42);
properties.Add ("Features", prop2);

SpaceDocument document = new SpaceDocument ("Product", properties);

// Register the document
// Create type descriptor:
SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder("Product");
typeBuilder.SetIdProperty("CatalogNumber");
typeBuilder.SetRoutingProperty("Category");
typeBuilder.AddPropertyIndex("Name");
typeBuilder.AddPropertyIndex("Price", SpaceIndexType.Extended);
ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create();
// Register type descriptor:
spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptor);

// Write the document into the space
ILeaseContext<SpaceDocument> lc = spaceProxy.Write (document);
```



## Time To Live

To write an object into the space with a limited time to live you should specify a lease value (in millisecond). The object will expire automatically from the space.


```csharp
spaceProxy.Write(myObject, 10000)
```

{{%anchor writeMultiple%}}

## Write Multiple

When writing a batch of objects into the space, these should be placed into an array to be used by the `ISpaceProxy.WriteMultiple` operation. The returned array will include the corresponding `ILeaseContext` object.


Example


```csharp
Employee[] emps = new Employee[2];
emps [0] = new Employee ("Last Name A", 10);
emps [1] = new Employee ("Last Name B", 20);

ILeaseContext<Employee>[] leaseContexts = spaceProxy.WriteMultiple (emps);

for (int i = 0; i < leaseContexts.Length; i++) {
   Console.WriteLine ("Object UID " + leaseContexts [i].Uid + " inserted into the space");
}
```

{{%note "Here are a few important considerations when using the batch operation:"%}}
-  should be performed with transactions - this allows the client to roll back the space to its initial state prior the operation was started, in case of a failure.
-  make sure `null` values are not part of the passed array.
-  you should verify that duplicated entries (with the same ID) do not appear as part of the passed array, since the identity of the object is determined based on its `ID` and not based on its reference. This is extremely important with an embedded space, since `WriteMultiple` injects the ID value into the object after the write operation (when autogenerate=false).

- Exception handling - the operation many throw the following Exceptions.
    - [WriteMultipleException](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_Exceptions_WriteMultipleException__ctor.htm)

{{%/note%}}


## Return Previous Value

When updating an object which already exists in the space, in some scenarios it is useful to get the previous value of the object (before the update). This previous value is returned in result `ILeaseContext.Object` when using the `WriteModifiers.ReturnPrevOnUpdate` modifier.


```csharp
Employee employee = new Employee ("Last Name", 32);

ILeaseContext<Employee> lc = spaceProxy.Write(employee,WriteModifiers.ReturnPrevOnUpdate);
Employee previousValue = lc.Object;
```

{{% info %}}
Since in most scenarios the previous value is irrelevant, the default behavior is not to return it (i.e. `ILeaseContext.Object` return null). The `WriteModifiers.ReturnPrevOnUpdate` modifier is used to indicate the previous value should be returned.
{{%/info%}}

{{%anchor asynchronousWrite%}}

## Asynchronous write

Asynchronous `write` operation can be implemented using a [Task](./task-execution-over-the-space.html), where the `Task` implementation include a write operation. With this approach the `Task` is sent to the space and executed in an asynchronous manner. The write operation itself will be completed once both the primary and the backup will acknowledge the operation. This activity will be performed as a background activity from the client perspective.


Example


```csharp
Employee employee = new Employee ("Last Name", 32);
spaceProxy.Write(employee,WriteModifiers.OneWay);
```


## Modifiers

For further details on each of the available modifiers see: [WriteModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_ISpaceProxy_WriteModifiers.htm)

{{%note%}}
Writing an object into a space might generate [notifications](./notify-container.html) to registered objects.
{{%/note%}}







{{% include "/COM/xap102net/ops-write.markdown" %}}
{{% include "/COM/xap102net/ops-read.markdown" %}}
{{% include "/COM/xap102net/ops-take.markdown" %}}
{{% include "/COM/xap102net/ops-clear.markdown" %}}
{{% include "/COM/xap102net/ops-count.markdown" %}}
{{% include "/COM/xap102net/ops-aggregation.markdown" %}}
