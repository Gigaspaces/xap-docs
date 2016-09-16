---
type: post97
title:  Operations
categories: XAP97NET
weight: 300
parent: the-gigaspace-interface-overview.html
---


{{% ssummary %}}{{%/ssummary%}}


XAP provides a simple space API using the [ISpaceProxy](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_ISpaceProxy.htm) interface for interacting with the space.


The interface includes the following main operations:

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Write objects into the space:"%}}
[write](#write) one object into the space{{<wbr>}}
[writeMultiple](#writeMultiple) objects into the space{{<wbr>}}
[asynchronous write](#asynchronousWrite) to the space
{{%/panel%}}
{{%/column%}}
{{%column width="45%" %}}
{{%panel  "Change objects in space:"%}}
[change](./change-api.html) one object in space{{<wbr>}}
[changeMultiple](./change-api.html) objects in space {{<wbr>}}
{{%/panel%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="50%" %}}
{{%panel  "Reading objects from the space:"%}}
[readById](#read) from the space{{<wbr>}}
[readByIds](#readMultiple) from the space{{<wbr>}}
[read](#read) object by template from the space{{<wbr>}}
[readMultiple](#readMultiple) objects from the space {{<wbr>}}
[read asynchronous](#asynchronousRead) from the space {{<wbr>}}
[read if exists](#readIfExists) {{<wbr>}}
[read if exists by id](#readIfExists)
{{%/panel%}}
{{%/column%}}
{{%column width="45%" %}}
{{%panel  "Removing objects from the space:"%}}
[take](#take) object by template from space{{<wbr>}}
[takeById](#take) object by id from space{{<wbr>}}
[takeByIds](#takeMultiple) objects by ids from space{{<wbr>}}
[takeMultiple](#takeMultiple) objects from space {{<wbr>}}
[take asynchronous](#asynchronousTake){{<wbr>}}
[take if exists](#takeIfExists){{<wbr>}}
[take if exists by id](#takeIfExists)
{{%/panel%}}
{{%/column%}}
{{%/section%}}

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Other operations:"%}}
[clear](#clear) an object type from space {{<wbr>}}
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


{{%anchor read%}}

# The Read Operation

The  read operations query the space for an object that matches the criteria provided.
If a match is found, a copy of the matching object is returned.
If no match is found, null is returned. Passing a null reference as the template will match any object.


Any matching object can be returned. Successive read requests with the same template may or may not return equivalent objects, even if no intervening modifications have been made to the space.
Each invocation of `read` may return a new object even if the same object is matched in the space.
If you would like to read objects in the same order they have been written into the space you should perform the read objects in a [FIFO mode](./fifo-support.html).

{{% note %}}
The `Read` operation default timeout is `0`.
{{% /note %}}

The read operation can be performed with the following options:

- Template matching
- By Id
- By IdQuery
- By SQLQuery

To learn more about the different options refer to [Querying the Space](./querying-the-space.html)

Examples:

The following example writes an `Employee` object into the space and reads it back from the space :


```csharp
Employee employee = new Employee("Last Name", 32);
employee.FirstName="first name";
spaceProxy.Write(employee);

// Read by template
Employee template = new Employee(32);
Employee e = spaceProxy.Read(template);

// Read by id
Employee e1 = spaceProxy.ReadById<Employee>(32);

// Read by IdQuery
IdQuery<Employee> query1 = new IdQuery<Employee>( 32);
Employee e2 = spaceProxy.Read<Employee>(query1);

// Read by SQLQuery
SqlQuery<Employee> query2 = new SqlQuery<Employee>("FirstName='first name'");
Employee e3 = spaceProxy.Read<Employee>(query2);

```


{{%anchor readMultiple%}}

## Read multiple


The GigaSpace interface provides simple way to perform bulk read operations. You may read a large amount of objects in one call.


## Examples:


```csharp
Employee[] emps = new Employee[2];
emps[0] = new Employee("Last Name A", 31);
emps[1] = new Employee("Last Name B", 32);

spaceProxy.WriteMultiple(emps);

// Read multiple by template
Employee[] employees = spaceProxy.ReadMultiple<Employee>(new Employee());

// Read multiple by SQLQuery
SqlQuery<Employee> query = new SqlQuery<Employee>("LastName ='Last Name B'");
Employee[] e = spaceProxy.ReadMultiple<Employee>(query);

// Read by Ids
Object[] ids = new Object[] { 31, 32 };
IReadByIdsResult<Employee> result = spaceProxy.ReadByIds<Employee>(ids);
Employee[] e1 = result.ResultsArray;

// Read by IdsQuery
Object[] ids1 = new Object[] { 31, 32 };
IdsQuery<Employee> query2 = new IdsQuery<Employee>(ids1);
IReadByIdsResult<Employee> result2 = spaceProxy.ReadByIds<Employee>(query2);
Employee[] employees2 = result.ResultsArray;
```

{{%note "Here are few important considerations when using the batch operation: "%}}
- boosts the performance, since it perform multiple operations using one call. These methods returns the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, these batch operations can be up to 10 times faster than multiple single based operations.
- should be handled with care, since they can return a large data set (potentially all the space data). This might cause an out of memory error in the space and client process. You should use the [GSIterator](#space-Iterator) to return the result in batches (paging) in such cases.
- **dos not support timeout** operations. The simple way to achieve this is by calling the `Read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.
- Exception handling - operation many throw the following Exceptions. [ReadMultipleException](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_Exceptions_ReadMultipleException__ctor.htm)
{{%/note%}}

{{%anchor readIfExists%}}

## Read if exists
A readIfExists operation will return a matching object, or a null if there is currently no matching object in the space.
If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value.
If at the end of that time no value can be returned that would not interfere with transactional state, null is returned. Note that, due to the remote nature of the space, read and readIfExists may throw a RemoteException if the network or server fails prior to the timeout expiration.

Example:


```csharp
Employee employee = new Employee("Last Name", 32);
employee.FirstName="first name";
spaceProxy.Write(employee);

SqlQuery<Employee> query = new SqlQuery<Employee>("FirstName='first name'");
Employee e = spaceProxy.ReadIfExists<Employee>(query);
```




{{%anchor asynchronousRead%}}

## Asynchronous Read

The GigaSpace interface supports asynchronous (non-blocking) read operations through the ISpaceProxy interface. It is implemented via a call back listener.

Example:


```csharp

private void ReadListener (IAsyncResult<Employee> asyncResult)
{
    Employee result = spaceProxy.EndRead (asyncResult);
}

public void asyncRead ()
{
    spaceProxy.BeginRead<Employee> (new SqlQuery<Employee> ("Id=1"), ReadListener, null);
}
```




## Modifiers

The read operations can be configured with different modifiers.

Examples:

```csharp
Employee template = new Employee();

// Read objects in a FIFO mode
Employee e = spaceProxy.Read<Employee>(template, null, 0, ReadModifiers.Fifo);

// Dirty read
Employee e1 = spaceProxy.Read<Employee>(template, null, 0, ReadModifiers.DirtyRead);
```


For further details on each of the available modifiers see: [ReadModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_IReadOnlySpaceProxy_ReadModifiers.htm)


{{%accordion%}}
{{%accord title="Method summary"%}}

Read by template:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_Read.htm)

```csharp
T Read(T template);
T Read(T template, long timeout, ReadModifiers modifiers);
.....
```

Read by Id:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_ReadById.htm)

```csharp
T ReadById<T>(Object id);
T ReadById<T>(Object id,Object routing,ITransaction tx,long timeout);
.....
```

Read by ISpaceQuery:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_Read.htm)

```csharp
T Read(ISpaceQuery<T> query, Object id);
T Read(ISpaceQuery<T> query, Object routing, long timeout, ReadModifiers modifiers);
....
```

Read multiple:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_ReadMultiple.htm)

```csharp
T[] ReadMultiple<T>(T template);
T[] ReadMultiple<T>(T template,ITransaction tx,int maxItems);
T[] ReadMultiple<T>(IQuery<T> query,ITransaction tx,int maxItems,ReadModifiers modifiers);
...
```


Asynchronous Read:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_BeginRead.htm)

```csharp
IAsyncResult<T> BeginRead<T>(T template,AsyncCallback<T> userCallback, Object stateObject);
IAsyncResult<T> BeginRead<T>(T template,long timeout,AsyncCallback<T> userCallback,Object stateObject)
.....
```


Read if exists:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_ReadIfExists.htm)

```csharp
T ReadIfExists<T>(T template);
T ReadIfExists<T>(T template,ITransaction tx);
T ReadIfExists<T>(T template,ITransaction tx,long timeout,ReadModifiers modifiers);
T ReadIfExists<T>(IQuery<T> query,ITransaction tx,long timeout);
....
```




| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | PONO, SpaceDocument|| |
|timeout     | Time to wait for the response| 0  |  milliseconds |
|query| [IQuery](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_IQuery_1.htm)|      | |
|[ReadModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_IReadOnlySpaceProxy_ReadModifiers.htm)|Provides modifiers to customize the behavior of read operations | NONE  |  |

{{%/accord%}}
{{%/accordion%}}



{{%anchor take%}}

# The Take Operation

The `take` operations behave exactly like the corresponding `read` operations, except that the matching object is **removed from the space on one atomic operation. Two `take` operations will never return** copies of the same object, although if two equivalent objects were in the space the two `take` operations could return equivalent objects.


If a `take` returns a non-null value, the object has been removed from the space, possibly within a transaction. This modifies the claims to once-only retrieval: A take is considered to be successful only if all enclosing transactions commit successfully.
If an `UnusableEntryException` is thrown, the take `removed` the unusable object from the space.
If any other exception is thrown, the take did not occur, and no object was removed from the space.

With a `RemoteException`, an object can be removed from a space and yet never returned to the client that performed the take, thus losing the object in between.
In circumstances in which this is unacceptable, the take can be wrapped inside a transaction that is committed by the client when it has the requested object in hand.


{{%note%}}
If you would like to take objects from the space in the same order they have been written into the space you should perform the take objects in a [FIFO mode](./fifo-support.html).

Taking an object from the space might generate [notifications](./notify-container.html) to registered objects/queries.
{{%/note%}}

The take operation can be performed with the following options:

- Template matching
- By Id
- By IdQuery
- By SQLQuery

To learn more about the different options refer to [Querying the Space](./querying-the-space.html)

Examples:

The following example writes an `Employee` object into the space and removes it from the space :


```csharp
Employee employee = new Employee("Last Name", 32);
employee.FirstName="first name";
spaceProxy.Write(employee);

// Take by template
Employee template = new Employee(32);
Employee e = spaceProxy.Take<Employee>(template);

// Take by id
Employee e1 = spaceProxy.TakeById<Employee>(32);

// Take by IdQuery
IdQuery<Employee> query = new IdQuery<Employee>(32);
Employee e2 = spaceProxy.Take<Employee>(query);

// Take by SQLQuery
SqlQuery<Employee> query1 = new SqlQuery<Employee>("FirstName='first name'");
Employee e3 = spaceProxy.Take<Employee>(query);

```


{{%anchor takeMultiple%}}

## Take multiple
The GigaSpace interface provides simple way to perform bulk take operations. You may take large amount of objects in one call.


{{% info %}}
To remove a batch of objects without returning these back into the client use `ISPaceProxy.Clear(SqlQuery)`;
{{%/info%}}

Examples:


```csharp
Employee[] emps = new Employee[2];
emps[0] = new Employee("Last Name A",  31);
emps[1] = new Employee("Last Name B",  32);

spaceProxy.WriteMultiple(emps);

// Take multiple by template
Employee[] employees = spaceProxy.TakeMultiple<Employee>(new Employee());

// Take multiple by SQLQuery
SqlQuery<Employee> query = new SqlQuery<Employee>("LastName ='Last Name B'");
Employee[] e = spaceProxy.TakeMultiple<Employee>(query);

// Take by Ids
Object[] ids = new Object[] { 31, 32 };
ITakeByIdsResult<Employee> result = spaceProxy.TakeByIds<Employee>(ids);
Employee[] e1 = result.ResultsArray;

// Take by IdsQuery
Object[] ids1 = new Object[] { 31, 32 };
IdsQuery<Employee> query1 = new IdsQuery<Employee>(ids1);
ITakeByIdsResult<Employee> result1 = spaceProxy.TakeByIds(query1);
Employee[] employees1 = result1.ResultsArray;
```

{{%note "Here are few important considerations when using the batch operation: "%}}
-  boosts the performance, since it performs multiple operations using one call. This method returns the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, this batch operation can be up to 10 times faster than multiple single based operations.
-  should be handled with care, since it can return a large data set (potentially all the space data). This might cause an out of memory error in the space and client process. You should use the [GSIterator](#space-iterator) to return the result in batches (paging) in such cases.
-  should be performed with transactions - this allows the client to roll back the space to its initial state prior the operation was started, in case of a failure.
-  operation **dos not support timeout** operations. The simple way to achieve this is by calling the `Read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.


{{%/note%}}

{{%anchor takeIfExists%}}

## Take if exists
A takeIfExists operation will return a matching object, or a null if there is currently no matching object in the space.
If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value.
If at the end of that time no value can be returned that would not interfere with transactional state, null is returned.

Example:


```csharp
Employee employee = new Employee("Last Name",  32);
employee.FirstName="first name";
spaceProxy.Write(employee);

SqlQuery<Employee> query = new SqlQuery<Employee>("FirstName='first name'");
Employee e = spaceProxy.TakeIfExists<Employee>(query);
```



{{%anchor asynchronousTake%}}

## Asynchronous Take

The GigaSpace interface supports asynchronous (non-blocking) take operations through the ISpaceProxy interface. It is implemented via a call back listener.

Example:


```csharp

private void TakeListener (IAsyncResult<Employee> asyncResult)
{
    Employee result = spaceProxy.EndTake (asyncResult);
}

public void asyncTake ()
{
    spaceProxy.BeginTake<Employee> (new SqlQuery<Employee> ("Id=1"), TakeListener, null);
}
```



## Modifiers

The take operations can be configured with different modifiers.

Examples:

```csharp
Employee template = new Employee();

// Takes objects in a FIFO mode
Employee e = spaceProxy.Take<Employee>(template, null, 0, TakeModifiers.Fifo);

// Takes objects according to FIFO group without transactions
Employee e1 = spaceProxy.Take<Employee>(template, null, 0, TakeModifiers.FifoGroupingPoll);
```


For further details on each of the available modifiers see: [TakeModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_ISpaceProxy_TakeModifiers.htm)


{{%accordion%}}
{{%accord title="Method summary"%}}

Take by template:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_Take.htm)

```csharp
T take<T>(T template);
T take<T>(T template, long timeout, TakeModifiers modifiers);
.....
```

Take by Id:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_TakeById.htm)

```csharp
T TakeById<T>(Object id);
T TakeById<T>(Object id, Object routing, long timeout, TakeModifiers modifiers);
.....
```

Take by Id's:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_TakeByIds.htm)

```csharp
ITakeByIdsResult<T> TakeByIds<T>(IdsQuery<T> idsQuery,ITransaction tx);
ITakeByIdsResult<T> TakeByIds<T>(Object[] ids,Object routingKey,ITransaction tx,TakeModifiers modifiers);
.....
```


Take multiple:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_TakeMultiple.htm)

```csharp
T[] TakeMultiple<T>(T template);
T[] TakeMultiple<T>(IQuery<T> query,ITransaction tx,int maxItems,TakeModifiers modifiers);
T[] TakeMultiple<T>(T template,int maxItems);
...
```


Asynchronous take:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_BeginTake.htm)

```csharp
IAsyncResult<T> BeginTake<T>(IQuery<T> query,AsyncCallback<T> userCallback,Object stateObject);
IAsyncResult<T> BeginTake<T>(T template,long timeout,AsyncCallback<T> userCallback,Object stateObject);
)
.....
```


Take if exists:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_TakeIfExists.htm)

```csharp
T TakeIfExists<T>(T template);
T TakeIfExists<T>(IQuery<T> query,ITransaction tx,long timeout,TakeModifiers modifiers);
.....

```

Take by id if exists:[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_ISpaceProxy_TakeIfExistsById.htm)

```csharp
Object TakeIfExistsById(Type type,Object id);
T TakeIfExistsById<T>(IdQuery<T> idQuery,ITransaction tx,long timeout,TakeModifiers modifiers);
....
```


| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | PONO, SpaceDocument|| |
|timeout     | Time to wait for the response| 0  |  milliseconds |
|query| [IQuery](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_IQuery_1.htm)|      | |
|[TakeModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_ISpaceProxy_TakeModifiers.htm)|Provides modifiers to customize the behavior of take operations | NONE  |  |
|[ITakeByIdsResult](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_IQuery_1.htm)|ResultSet||
{{%/accord%}}
{{%/accordion%}}




{{%anchor count%}}

# The Count Operation


You can use `ISpaceProxy.Count` to count objects in a space.


Examples:


```csharp
    ISpaceProxy spaceProxy = GigaSpacesFactory.FindSpace (url);

    // Count with Template
    Employee employee = new Employee ("Last Name");
    int counter = spaceProxy.Count(employee);

    // Count with SQLQuery
    String querystr	= "Age > 30";
    SqlQuery<Employee> query = new SqlQuery<Employee>( querystr);
    int count1 = spaceProxy.Count(query);

    // Count with IdsQuery
    Object[] ids = new object[] { 32, 33, 34 };
    IdsQuery<Employee> query1 = new IdsQuery<Employee>(ids);
    int count2 = spaceProxy.Count(query1);
```



{{%accordion%}}
{{%accord title="Method summary"%}}

Count objects in space.[.NetAPI]http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/Overload_GigaSpaces_Core_IReadOnlySpaceProxy_Count.htm)


```java
int Count(T entry);
int Count(T entry, ClearModifiers modifiers);
int Count(ISpaceQuery<T> query);
......

```


| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | PONO, SpaceDocument||
|query         | SqlQuery, IdQuery||
|[ReadModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/P_GigaSpaces_Core_IReadOnlySpaceProxy_ReadModifiers.htm)|Provides modifiers to customize the behavior of the count operations | NONE  |
{{%/accord%}}
{{%/accordion%}}





{{%anchor clear%}}

# The Clear Operation

You can use `ISpaceProxy.Clear` to remove objects from the space. When using the clear operation no object/objects are returned.


Examples:


```csharp
    ISpaceProxy spaceProxy = GigaSpacesFactory.FindSpace(url);

    // Clear by Template
    Employee employee = new Employee ("Last Name", 32L);
    spaceProxy.Clear(employee);

    // Clear by SQLQuery
    String querystr	= "Age > 30";
    SqlQuery<Employee> query = new SqlQuery<Employee> (querystr);
    spaceProxy.Clear(query);

    // Clear by IdQuery
    IdQuery<Employee> query1 = new IdQuery<Employee> (32L);
    spaceProxy.Clear(query1);

    // Clear with Modifier
    SqlQuery<Employee> query2 = new SqlQuery<Employee> ("FirstName='first name'");
    spaceProxy.Clear(query2, null,TakeModifiers.EvictOnly);
```



{{%accordion%}}
{{%accord title="Method summary"%}}

Clears objects from space.[.NetAPI](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_ISpaceProxy.htm)



```csharp
void Clear(T entry)
void Clear(T entry, ITransaction tx, TakeModifiers modifiers)
void Clear(ISpaceQuery<T> query)
......

```


| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | PONO, SpaceDocument||
|query         | SqlQuery, IdQuery||
|[TakeModifiers](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_TakeModifiers.htm)|Provides modifiers to customize the behavior of the clear operations | NONE  |
{{%/accord%}}
{{%/accordion%}}






{{% include "/COM/xap102net/ops-write.markdown" %}}
{{% include "/COM/xap102net/ops-read.markdown" %}}
{{% include "/COM/xap102net/ops-take.markdown" %}}
{{% include "/COM/xap102net/ops-clear.markdown" %}}
{{% include "/COM/xap102net/ops-count.markdown" %}}
{{% include "/COM/xap102net/ops-aggregation.markdown" %}}
