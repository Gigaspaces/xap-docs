---
type: post123
title:  Operations
categories: XAP123, OSS
weight: 300
parent: the-gigaspace-interface-overview.html
---


 


XAP provides a simple Space API using the [GigaSpace]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) interface for interacting with the Space.


The interface includes the following main operations:

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Write objects into the Space:"%}}
[write](#write) one object into the space{{<wbr>}}
[writeMultiple](#writeMultiple) objects into the Space{{<wbr>}}
[asynchronous write](#asynchronousWrite) to the Space
{{%/panel%}}
{{%/column%}}
{{%column width="50%" %}}
{{%panel  "Change objects in Space:"%}}
[change](#change) one object in Space{{<wbr>}}
		  [changeMultiple](./change-api-overview.html) objects in Space {{<wbr>}}
[asynchronous change](./change-api-overview.html) of objects
{{%/panel%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="50%" %}}
{{%panel  "Reading objects from the Space:"%}}
[readById](#read) from the Space{{<wbr>}}
[readByIds](#readMultiple) from the Space{{<wbr>}}
[read](#read) object by template from the Space{{<wbr>}}
[readMultiple](#readMultiple) objects from the Space {{<wbr>}}
[read asynchronous](#asynchronousRead) from the Space {{<wbr>}}
[read if exists](#readIfExists) {{<wbr>}}
[read if exists by id](#readIfExists)
{{%/panel%}}
{{%/column%}}
{{%column width="50%" %}}
{{%panel  "Removing objects from the Space:"%}}
[take](#take) object by template from Space{{<wbr>}}
[takeById](#take) object by id from Space{{<wbr>}}
[takeByIds](#takeMultiple) objects by ids from Space{{<wbr>}}
[takeMultiple](#takeMultiple) objects from Space {{<wbr>}}
[take asynchronous](#asynchronousTake){{<wbr>}}
[take if exists](#takeIfExists){{<wbr>}}
[clear](#clear) objects in Space
{{%/panel%}}
{{%/column%}}
{{%/section%}}

{{%section%}}
{{%column width="50%" %}}
{{%panel  "Other operations:"%}}
[aggregation](#aggregators)  across the Space{{<wbr>}}
[count](#count) objects in Space{{<wbr>}}
[counters](#counters) increment and decrement
{{%/panel%}}
{{%column width="50%" %}}
{{%/column%}}
{{%/column%}}
{{%/section%}}


# Simpler API

The `GigaSpace` interface provides a simpler Space API by utilizing Java 5 generics, and allowing sensible defaults. Here are some examples of the Space operations as defined within `GigaSpace`:


```java
public interface GigaSpace {
    <T> LeaseContext<T> write(T entry) throws DataAccessException;
    // ....
    <T> T read(ISpaceQuery<T> query, Object id)throws DataAccessException;
    // ......
    <T> T take(T template) throws DataAccessException;
    <T> T take(T template, long timeout) throws DataAccessException;
    // ......
}
```

In the example above, the take operation can be performed without specifying a timeout. The default take timeout is `0` (no wait), and can be overridden when configuring the `GigaSpace` factory. In a similar manner, the read timeout and write lease can be specified.



{{%anchor write%}}

# The Write Operation

In order to write objects to the Space, use the write method of the GigaSpace interface. The write method is used to write objects if these are introduced for the first time, or to update them if they already exist in the Space. In order to override the default semantics, use the overloaded write methods that accept update modifiers such as WriteModifiers.UPDATE_ONLY.

## POJO Example

The following example writes an `Employee` object to the Space:


```java
    GigaSpace space = ....

    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
    LeaseContext<Employee> lc = space.write(employee);
```

## SpaceDocument Example

Here is an example how you create a SpaceDocument, register it with the Space, and then write it to the Space:


```java
     // Create the document
     DocumentProperties properties = new DocumentProperties()
       .setProperty("CatalogNumber", "av-9876")
       .setProperty("Category", "Aviation")
       .setProperty("Name", "Jet Propelled Pogo Stick")
       .setProperty("Price", 19.99f)
       .setProperty("Tags",
            new String[] { "New", "Cool", "Pogo", "Jet" })
       .setProperty("Features",
            new DocumentProperties()
              .setProperty("Manufacturer", "Acme")
              .setProperty("RequiresAssembly", true)
              .setProperty("NumberOfParts", 42));

     SpaceDocument doc = new SpaceDocument("Product", properties);

     // Register the document
     // Create type descriptor:
     SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder(
		"Product").idProperty("CatalogNumber")
		.routingProperty("Category")
		.addPropertyIndex("Name", SpaceIndexType.BASIC)
		.addPropertyIndex("Price", SpaceIndexType.EXTENDED).create();
     // Register type:
     space.getTypeManager().registerTypeDescriptor(typeDescriptor);

    // Write the document into the space
    LeaseContext<SpaceDocument> lc = space.write(document);
```


## Time To Live

To write an object to the Space with a limited time to live, specify [a lease value](./leases-automatic-expiration.html) (in milliseconds). The object will expire automatically from the Space when the lease is finished.


```java
   gigaSpace.write(myObject, 10000)
```

{{%anchor writeMultiple%}}

{{%anchor writeMultiple%}}

## Write Multiple

When writing a batch of objects to the Space, these should be placed into an array to be used by the `GigaSpace.writeMultiple` operation. The returned array will include the corresponding `LeaseContext` object.

## Example


```java
   Employee emps[] = new Employee[2];
   emps[0] = new Employee("Last Name A", new Integer(10));
   emps[1] = new Employee("Last Name B", new Integer(20));
   try {
    LeaseContext[] leaseContexts = space.writeMultiple(emps);
    for (int i = 0;i<leaseContexts.length ; i++) {
        System.out.println ("Object UID " + leaseContexts[i].getUID() + " inserted into the space");
    }
   } catch (WriteMultipleException e) {
    IWriteResult[] writeResult = e.getResults();
    for (int i = 0;i< writeResult.length ; i++) {
        System.out.println ("Problem with Object UID " + writeResult ");
    }
  }
```

**Here are a few important considerations when using the batch operation:**

-  Should be performed with transactions - this allows the client to roll back the Space to its initial state prior to when the operation was started, in case of a failure.
-  Make sure `null` values are not part of the passed array.
-  Verify that duplicated entries (with the same ID) do not appear as part of the passed array, because the identity of the object is determined based on its `ID` and not based on its reference. This is extremely important with an embedded Space, because `writeMultiple` injects the ID value into the object after the write operation (when autogenerate=false).

- Exception handling - the operation many throw the following exceptions:
    - [WriteMultiplePartialFailureException]({{% api-javadoc %}}/org/openspaces/core/WriteMultiplePartialFailureException.html)
    - [WriteMultipleException]({{% api-javadoc %}}/org/openspaces/core/WriteMultipleException.html)

 
## Return Previous Value

When updating an object that already exists in the Space, in some scenarios it is useful to get the previous value of the object (before the update). This previous value is returned in result `LeaseContext.getObject()` when using the `RETURN_PREV_ON_UPDATE` modifier.



```java
  LeaseContext<MyData> lc = space.write(myobject,WriteModifiers.RETURN_PREV_ON_UPDATE.add(WriteModifiers.UPDATE_OR_WRITE));
  MyData previousValue = lc.getObject();
```

{{% note "Note" %}}
In most scenarios the previous value is irrelevant, therefore the default behavior is not to return it (i.e. `LeaseContext.getObject()` return null). The `RETURN_PREV_ON_UPDATE` modifier is used to indicate that the previous value should be returned.
{{%/note%}}

{{%anchor asynchronousWrite%}}

## Asynchronous Write

An asynchronous `write` operation can be implemented using a [Task](./task-execution-overview.html), where the `Task` implementation include a write operation. With this approach the `Task` is sent to the space and executed in an asynchronous manner. The write operation itself will be completed when both the primary and the backup acknowledge the operation. This activity is performed as a background activity from the client perspective.


### Example


```java
  GigaSpace space = new GigaSpaceConfigurer (new SpaceProxyConfigurer("space"))).gigaSpace();
  MyClass obj = new MyClass(1,"AAA");
  space.write(obj,WriteModifiers.ONE_WAY);
```

{{%note "Note"%}}
Writing an object to a Space may generate [notifications](./notify-container-overview.html) to registered objects.
{{%/note%}}

 
**Method summary**

Writes a new object to the Space, returning its LeaseContext.  


```java
<T> LeaseContext<T> write(T entry) throws DataAccessException
<T> LeaseContext<T> write(T entry, long lease, long timeout, WriteModifiers modifiers) throws DataAccessException
......

```

Writes new objects to the Space, returning its LeaseContexts.[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#writeMultiple(T[]))

```java
<T> LeaseContext<T>[] writeMultiple(T[] entries) throws DataAccessException
<T> LeaseContext<T>[] writeMultiple(T[] entries, long[] leases, WriteModifiers modifiers) throws DataAccessException
......
```

## Modifiers

The write operations can be configured with different modifiers:

| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|lease       |Time to live | Lease.FOREVER|milliseconds|
|timeout     | The timeout of an update operation, in milliseconds. If the entry is locked by another transaction, wait for the specified number of milliseconds for it to be released. | 0  |
|[WriteModifiers]({{% api-javadoc %}}/com/gigaspaces/client/WriteModifiers.html)|Provides modifiers to customize the behavior of write operations | UPDATE_OR_WRITE  |
|[LeaseContext]({{% api-javadoc %}}/com/j_spaces/core/LeaseContext.html) |LeaseContext is a return-value encapsulation of a write operation.| |

   


{{%anchor change%}}

# The Change Operation

The [GigaSpace.change]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) and the [ChangeSet]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeSet.html) allows updating existing objects in a Space, by specifying only the required change instead of passing the entire updated object.
This reduces the required network traffic between the client and the Space, and the network traffic generated from replicating the changes between the Space instances (e.g between the primary Space instance and its backup).

**Example**

The following example demonstrates how to update the property 'firstName' of an object of type 'Person' with id 'myID'.


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<Person>(Person.class, id, routing);
space.change(idQuery, new ChangeSet().set("firstName", "John"));
```

{{%refer%}}
[The Change API](./change-api-overview.html)
{{%/refer%}}


{{%anchor read%}}

# The Read Operation


Read operations query the Space for an object that matches the criteria provided. If a match is found, a copy of the matching object is returned. If no match is found, null is returned. Passing a null reference as the template will match any object.

Any matching object can be returned. Successive read requests with the same template may or may not return equivalent objects, even if no intervening modifications have been made to the Space.

Each invocation of `read` may return a new object even if the same object is matched in the Space.

If you want to read objects in the same order that they were written to the Space, perform the read objects in [FIFO mode](./fifo-overview.html).

{{% note "Note"%}}
The `read` operation default timeout is `JavaSpace.NO_WAIT`.
{{% /note %}}

The read operation can be performed with the following options:

- Template matching
- By ID
- By IdQuery
- By SQLQuery

To learn more about the different options, refer to [Querying the Space](./querying-the-space.html)

**Example**

The following example writes an `Employee` object to the Space and reads it back from the Space:


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
    space.write(employee);

    // Read by template
    Employee template = new Employee(new Integer(32));
    Employee e = space.read(template);

    // Read by id
    Employee e = space.readById(Employee.class, new Integer(32));

    // Read by IdQuery
    IdQuery<Employee> query = new IdQuery<Employee>(Employee.class,
    				new Integer(32));
    Employee e = space.read(query);

    // Read by SQLQuery
	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
	Employee e = space.read(query);
```


{{%anchor readMultiple%}}

## Read Multiple

The GigaSpace interface provides simple way to perform bulk read operations. You can read a large amount of objects in one call.

**Example**


```java
   Employee emps[] = new Employee[2];
   emps[0] = new Employee("Last Name A", new Integer(31));
   emps[1] = new Employee("Last Name B", new Integer(32));

   space.writeMultiple(emps);

   // Read multiple by template
   Employee[] employees = space.readMultiple(new Employee());

   // Read multiple by SQLQuery
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,"lastName ='Last Name B'");
   Employee[] e = space.readMultiple(query);

   // Read by Ids
   Integer[] ids = new Integer[] { 31, 32 };
   ReadByIdsResult<Employee> result = space.readByIds(Employee.class,ids);
   Employee[] employees = result.getResultsArray();

   // Read by IdsQuery
   Integer[] ids = new Integer[] { 31, 32 };
   IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
   ReadByIdsResult<Employee> result = space.readByIds(query);
   Employee[] employees = result.getResultsArray();

```

**Here are a few important considerations when using the batch operation:**

- Boosts the performance, because it perform multiple operations using one call. These methods return the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, these batch operations can be up to 10 times faster than multiple single-based operations.
- Should be handled with care, because they can return a large data set (potentially all the Space data). This can cause an out of memory error in the Space and client process. Use the [GSIterator](#space-iterator) to return the result in batches (paging) in such cases.
- **Does not support timeout** operations. The simple way to achieve this is by calling the `read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.
- Exception handling - operation may throw the following exceptions: [ReadMultipleException]({{% api-javadoc %}}/org/openspaces/core/ReadMultipleException.html)
 

{{%anchor readIfExists%}}

## ReadIfExists

A readIfExists operation will return a matching object, or a null if there is currently no matching object in the Space.

If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value.
If at the end of that time no value can be returned that would not interfere with transactional state, null is returned. Due to the remote nature of the Space, read and readIfExists may throw a RemoteException if the network or server fails prior to the timeout expiration.

**Example**


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
    space.write(employee);

    SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,"firstName='first name'");
    Employee e = space.readIfExists(query);
```

{{%anchor asynchronousRead%}}

## Asynchronous Read

The GigaSpace interface supports asynchronous (non-blocking) read operations through the GigaSpace interface. It returns a [Future\<T\>](http://download.oracle.com/javase/6/docs/api/java/util/concurrent/Future.html) object, where T is the type of the object the request returns. Future<T>.get() can be used to query the object to see if a result has been returned or not.

Alternatively, asyncRead also accept an implementation of [AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html), which will have its `AsyncFutureListener.onResult` method called when the result has been populated. This does not affect the return type of the `Future<T>`, but provides an additional mechanism for handling the asynchronous response.


**Example**


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
 	space.write(employee);

 	Integer[] ids = new Integer[] { 31, 32 };
 	IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
 	AsyncFuture<Employee> result = space.asyncRead(query);

    // This part of the code could be executed in a different Thread
 	try {
 	    Employee e = result.get();
 	} catch (InterruptedException e) {
 		e.printStackTrace();
 	} catch (ExecutionException e) {
 		e.printStackTrace();
 	}
```

## Passing an AsyncFutureListener with Java 8 Lambda Syntax


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
 	space.write(employee);

 	Integer[] ids = new Integer[] { 31, 32 };
 	IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
 	AsyncFuture<Employee> result = space.asyncRead(query, (result) -> {
 	    System.out.println("Got a result: " + result.getResult());
 	});

    // This part of the code could be executed in a different Thread
 	try {
 	    Employee e = result.get();
 	} catch (InterruptedException e) {
 		e.printStackTrace();
 	} catch (ExecutionException e) {
 		e.printStackTrace();
 	}
```


## Modifiers

The read operations can be configured with different modifiers.

**Example**

```java
    Employee template = new Employee();

    // Read objects in a FIFO mode
    Employee e = space.read(template, 0, ReadModifiers.FIFO);

    // Dirty read
    Employee e = space.read(template, 0, ReadModifiers.DIRTY_READ);
```

| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | POJO, SpaceDocument|| |
|timeout     | Time to wait for the response.| 0  |  milliseconds |
|query| [ISpaceQuery]({{% api-javadoc %}}/com/gigaspaces/query/ISpaceQuery.html)|      | |
|[AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html) |Allows registering for a callback on an AsyncFuture, to be notified when a result arrives.||
|[ReadModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ReadModifiers.html)|Provides modifiers to customize the behavior of read operations. | NONE  |  |


 
{{%accordion%}}
{{%accord title="Method summary..."%}}

Read by template: 

```java
<T> T read(T template) throws DataAccessException
<T> T read(T template, long timeout, ReadModifiers modifiers)throws DataAccessException
.....
```

Read by ID: 

```java
<T> T readById(Class<T> clazz, Object id) throws DataAccessException
<T> T readById(Class<T> clazz, Object id, Object routing, long timeout, ReadModifiers modifiers)throws DataAccessException
.....
```

Read by ISpaceQuery: 

```java
<T> T read(ISpaceQuery<T> query, Object id)throws DataAccessException
<T> T read(ISpaceQuery<T> query, Object routing, long timeout, ReadModifiers modifiers)throws DataAccessException
....
```

Read multiple:

```java
<T> T[] readMultiple(ISpaceQuery<T> query) throws DataAccessException
<T> T[] readMultiple(ISpaceQuery<T> query, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T[] readMultiple(T template) throws DataAccessException
<T> T[] readMultiple(T template, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T[] readMultiple(ISpaceQuery<T> template, int maxEntries, ReadModifiers modifiers) throws DataAccessException
...
```

Asynchronous read: 

```java
<T> AsyncFuture<T> asyncRead(T template) throws DataAccessException
<T> AsyncFuture<T> asyncRead(T template, long timeout, ReadModifiers modifiers, AsyncFutureListener<T> listener) throws DataAccessException
<T> AsyncFuture<T> asyncRead(ISpaceQuery<T> query)throws DataAccessException
<T> AsyncFuture<T> asyncRead(ISpaceQuery<T> query, long timeout, ReadModifiers modifiers, AsyncFutureListener<T> listener)throws DataAccessException
.....
```

Read if exists: 

```java
<T> T readIfExists(T template)throws DataAccessException
<T> T readIfExistsById(Class<T> clazz, Object id)throws DataAccessException
<T> T readIfExistsById(Class<T> clazz, Object id, Object routing, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T readIfExistsById(IdQuery<T> query, long timeout, ReadModifiers modifiers) throws DataAccessException
....
```
{{%/accord%}}
{{%/accordion%}}


{{%anchor take%}}

# The Take Operation


The `take` operations behave exactly like the corresponding `read` operations, except that the matching object is removed from the Space on one atomic operation. Two `take` operations will never return copies of the same object, although if two equivalent objects were in the Space, the two `take` operations could return equivalent objects.

If a `take` returns a non-null value, then the object has been removed from the Space, possibly within a transaction. This modifies the claims to once-only retrieval; a take is considered to be successful only if all enclosing transactions commit successfully.

If a `RemoteException` is thrown, the take may or may not have been successful.

If an `UnusableEntryException` is thrown, the take `removed` the unusable object from the Space.

If any other exception is thrown, the take did not occur, and no object was removed from the Space.


{{%note "Note"%}}
If you want to take objects from the Space in the same order they were written to the Space, perform the take objects in [FIFO mode](./fifo-support.html).

Taking an object from the space may generate [notifications](./notify-container-overview.html) to registered objects/queries.
{{%/note%}}

The take operation can be performed with the following options:

- Template matching
- By ID
- By IdQuery
- By SQLQuery

{{%refer%}}
To learn more about the different options, refer to [Querying the Space](./querying-the-space.html).
{{%/refer%}}

**Example**

The following example writes an `Employee` object to the Space and removes it from the Space:


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
    space.write(employee);

    // Take by template
    Employee template = new Employee(new Integer(32));
    Employee e = space.take(template);

    // Take by id
    Employee e = space.takeById(Employee.class, new Integer(32));

    // Take by IdQuery
    IdQuery<Employee> query = new IdQuery<Employee>(Employee.class,
    				new Integer(32));
    Employee e = space.take(query);

    // Take by SQLQuery
	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
	Employee e = space.take(query);
```


{{%anchor takeMultiple%}}

## Take Multiple


The GigaSpace interface provides simple way to perform bulk take operations. You may take large amount of objects in one call.


{{% note "Note"%}}
To remove a batch of objects without returning them back into the client, use `GigaSpace.clear(SQLQuery)`;.
{{%/note%}}

**Example**


```java
   Employee emps[] = new Employee[2];
   emps[0] = new Employee("Last Name A", new Integer(31));
   emps[1] = new Employee("Last Name B", new Integer(32));

   space.writeMultiple(emps);

   // Take multiple by template
   Employee[] employees = space.takeMultiple(Employee.class);

   // Take multiple by SQLQuery
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
   				"lastName ='Last Name B'");
   Employee[] e = space.takeMultiple(query);

   // Take by Ids
   Integer[] ids = new Integer[] { 31, 32 };
   TakeByIdsResult<Employee> result = space.takeByIds(Employee.class,ids);
   Employee[] employees = result.getResultsArray();

   // Take by IdsQuery
   Integer[] ids = new Integer[] { 31, 32 };
   IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
   TakeByIdsResult<Employee> result = space.takeByIds(query);
   Employee[] employees = result.getResultsArray();

```

**Here are a few important considerations when using the batch operation:**

-  Boosts the performance, because it performs multiple operations using one call. This method returns the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, this batch operation can be up to 10 times faster than multiple single-based operations.
-  Should be handled with care, because it can return a large data set (potentially all the Space data). This can cause an out of memory error in the Space and client processes. Use the [GSIterator](#space-iterator) to return the result in batches (paging) in such cases.
-  Should be performed with transactions. This allows the client to roll back the Space to its initial state prior to when the operation was started, in case of a failure.
-  Operation **dos not support timeout** operations. The simple way to achieve this is by calling the `read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.
-  In the event of a take error, DataAccessException will wrap a TakeMultipleException, accessible via DataAccessException.getRootCause().
 

{{%anchor takeIfExists%}}

## TakeIfExists

A takeIfExists operation will return a matching object, or a null if there is currently no matching object in the Space.

If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value. If at the end of that time no value can be returned that would not interfere with transactional state, null is returned.

**Example**


```java
    Employee employee = new Employee("Last Name", new Integer(32));
	employee.setFirstName("first name");
	space.write(employee);

	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
	Employee e = space.takeIfExists(query);
```

{{%anchor asynchronousTake%}}

## Asynchronous Take

The GigaSpace interface supports asynchronous (non-blocking) take operations through the GigaSpace interface. It returns a [Future\<T\>](http://download.oracle.com/javase/6/docs/api/java/util/concurrent/Future.html) object, where T is the type of the object the request returns. Future<T>.get() can be used to query the object to see if a result has been returned or not.

Alternatively, asyncTake also accept an implementation of [AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html), which will have its `AsyncFutureListener.onResult` method called when the result has been populated. This does not affect the return type of the `Future<T>`, but provides an additional mechanism for handling the asynchronous response.


**Example**


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
 	space.write(employee);

 	Integer[] ids = new Integer[] { 31, 32 };
 	IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
 	AsyncFuture<Employee> result = space.asyncTake(query);

 	try {
 	    Employee e = result.get();
 	} catch (InterruptedException e) {
 		e.printStackTrace();
 	} catch (ExecutionException e) {
 		e.printStackTrace();
 	}
```

## Passing an AsyncFutureListener with Java 8 Lambda Syntax


```java
    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
 	space.write(employee);

 	Integer[] ids = new Integer[] { 31, 32 };
 	IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
 	AsyncFuture<Employee> result = space.asyncTake(query, (result) -> {
 	    System.out.println("Got result: " + result.getResult());
 	});

 	try {
 	    Employee e = result.get();
 	} catch (InterruptedException e) {
 		e.printStackTrace();
 	} catch (ExecutionException e) {
 		e.printStackTrace();
 	}
```


## Modifiers

The take operations can be configured with different modifiers.

**Examples**

```java
	Employee template = new Employee();

    // Takes objects in a FIFO mode
 	Employee e = space.take(template, 0, TakeModifiers.FIFO);

    // Takes objects according to FIFO group
	Employee e = space.take(template, 0, TakeModifiers.FIFO_GROUPING_POLL);
```



| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | POJO, SpaceDocument|| |
|timeout     | Time to wait for the response.| 0  |  milliseconds |
|query| [ISpaceQuery]({{% api-javadoc %}}/com/gigaspaces/query/ISpaceQuery.html)|      | |
|[AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html) |Allows registering for a callback on an AsyncFuture to be notified when a result arrives.||
|[TakeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/TakeModifiers.html)|Provides modifiers to customize the behavior of take operations. | NONE  |  |

 
For further details about each of the available modifiers, see [TakeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/TakeModifiers.html).
 

{{%accordion%}}
{{%accord title="Method summary..."%}}

Take by template:

```java
<T> T take(T template) throws DataAccessException
<T> T take(T template, long timeout, TakeModifiers modifiers)throws DataAccessException
.....
```

Take by ID: 

```java
<T> T takeById(Class<T> clazz, Object id) throws DataAccessException
<T> T takeById(Class<T> clazz, Object id, Object routing, long timeout, TakeModifiers modifiers)throws DataAccessException
.....
```

Take by ISpaceQuery: 

```java
<T> T take(ISpaceQuery<T> query, Object id)throws DataAccessException
<T> T take(ISpaceQuery<T> query, Object routing, long timeout, TakeModifiers modifiers)throws DataAccessException
....
```

Take multiple: 

```java
<T> T[] takeMultiple(ISpaceQuery<T> query) throws DataAccessException
<T> T[] takeMultiple(ISpaceQuery<T> query, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T[] takeMultiple(T template) throws DataAccessException
<T> T[] takeMultiple(T template, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T[] takeMultiple(ISpaceQuery<T> template, int maxEntries, TakeModifiers modifiers) throws DataAccessException
...
```

Asynchronous take: 

```java
<T> AsyncFuture<T> asyncTake(T template) throws DataAccessException
<T> AsyncFuture<T> asyncTake(T template, long timeout, TakeModifiers modifiers, AsyncFutureListener<T> listener) throws DataAccessException
<T> AsyncFuture<T> asyncTake(ISpaceQuery<T> query)throws DataAccessException
<T> AsyncFuture<T> asyncTake(ISpaceQuery<T> query, long timeout, TakeModifiers modifiers, AsyncFutureListener<T> listener)throws DataAccessException
.....
```

Take if exists: 

```java
<T> T takeIfExists(T template)throws DataAccessException
<T> T takeIfExistsById(Class<T> clazz, Object id)throws DataAccessException
<T> T takeIfExistsById(Class<T> clazz, Object id, Object routing, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T takeIfExistsById(IdQuery<T> query, long timeout, TakeModifiers modifiers) throws DataAccessException
....
```
{{%/accord%}}
{{%/accordion%}}



{{%anchor clear%}}

# The Clear Operation


You can use `GigaSpace.clear` to remove objects from the Space. When using the clear operation, no object/objects are returned.


**Examples**


```java
   GigaSpace space;

   // Clear by Template
   Employee employee = new Employee("Last Name", new Integer(32));
   space.clear(employee);

   // Clear by SQLQuery
   String querystr	= "age > 30";
   SQLQuery query = new SQLQuery(Employee.class, querystr);
   space.clear(query);

   // Clear by IdQuery
   IdQuery<Employee> query = new IdQuery<Employee>(Employee.class,new Integer(32));
   space.clear(query);

   // Clear with Modifier
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,"firstName='first name'");
   space.clear(query, ClearModifiers.EVICT_ONLY);
```



{{%accordion%}}
{{%accord title="Method summary..."%}}

Clears objects from the Space. 


```java
void clear(T entry) throws DataAccessException
void clear(T entry, ClearModifiers modifiers) throws DataAccessException
void clear(ISpaceQuery<T> query) throws DataAccessException
......
```
{{%/accord%}}
{{%/accordion%}}

**Modifiers**

| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|query         | SQLQuery, IdQuery||
|[ClearModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ClearModifiers.html)|Provides modifiers to customize the behavior of the clear operations. | NONE  |


{{%anchor count%}}

# The Count Operation

You can use `GigaSpace.count` to count objects in a Space.

**Examples**


```java
   GigaSpace space;

   // Count with Template
   Employee employee = new Employee("Last Name");
   int count = space.count(employee);

   // Count with SQLQuery
   String querystr	= "age > 30";
   SQLQuery query = new SQLQuery(Employee.class, querystr);
   int count = space.count(query);

   // Count with IdsQuery
   Integer[] ids = new Integer[] { 32, 33, 34 };
   IdsQuery<Employee> query = new IdsQuery<Employee>(Employee.class, ids);
   int count = space.count(query);

   // Count with Modifier
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,"firstName='first name'");
   int count = space.count(query, CountModifiers.EXCLUSIVE_READ_LOCK);
```


{{%accordion%}}
{{%accord title="Method summary..."%}}

Count objects in a Space. 


```java
int count(T entry) throws DataAccessException
int count(T entry, ClearModifiers modifiers) throws DataAccessException
int count(ISpaceQuery<T> query) throws DataAccessException
......
```
{{%/accord%}}
{{%/accordion%}}

**Modifiers**

| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|query         | SQLQuery, IdQuery||
|[CountModifiers]({{% api-javadoc %}}/com/gigaspaces/client/CountModifiers.html)|Provides modifiers to customize the behavior of the count operations. | NONE  |



{{%anchor counters%}}

# Counters

The `ISpaceProxy.Change` API allows you to increment or decrement an Numerical field within your Space object or Document. This change may operate on a numeric property only (byte, short, int, long, float, double) or their corresponding Boxed variation. To maintain a counter, use the Change operation with the `ChangeSet` increment/decrement method that adds/subtract the provided numeric value to the existing counter.


**Example**

Incrementing a Counter done using the `ChangeSet().Increment` call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
space.change(idQuery, new ChangeSet().increment("mycounter", 1));
```


Getting the Counter value via the read call:


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<WordCount>(WordCount.class, id);
WordCount wordount = space.readById(WordCount.class , idQuery);
int counterValue = wordount.getMycounter();
```

{{%refer%}}
[The Space Counters](./the-space-counters.html)
{{%/refer%}}


{{%anchor aggregators%}}

# Aggregators

There is no need to retrieve the entire data set from the Space to the client side, iterate the result set and perform the aggregation. This would be an expensive activity as it might return large amounts of data to the client application. The Aggregators allow you to perform the entire aggregation activity on the Space side, avoiding any data retrieval back to the client side.


**Example**

{{%tabs%}}
{{%tab "  Application "%}}

```java
import static org.openspaces.extensions.QueryExtension.*;
...
SQLQuery<Person> personSQLQuery = new SQLQuery<Person>();
// retrieve the maximum value stored in the field "age"
Number maxAgeInSpace = maxValue(space, personSQLQuery, "age");
/// retrieve the minimum value stored in the field "age"
Number minAgeInSpace = minValue(space, personSQLQuery, "age");
// Sum the "age" field on all space objects.
Number combinedAgeInSpace = sum(space, personSQLQuery, "age");
// Sum's the "age" field on all space objects then divides by the number of space objects.
Double averageAge = average(space, personSQLQuery, "age");
// Retrieve the space object with the highest value for the field "age".
Person oldestPersonInSpace = maxEntry(space, personSQLQuery, "age");
/// Retrieve the space object with the lowest value for the field "age".
Person youngestPersonInSpace = minEntry(space, personSQLQuery, "age");
```
{{% /tab%}}
{{%tab "  Space Class "%}}

```java
@SpaceClass
public class Person {
    private Long id;
    private Long age;
    private String country;

    @SpaceId(autoGenerate=false)
    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getAge() {
        return age;
    }

    public Person setAge(Long age) {
        this.age = age;
        return this;
    }

    @SpaceIndex
    public String getCountry() {
        return country;
    }

    public Person setCountry(String country) {
        this.country = country;
        return this;
    }
}
```
{{% /tab%}}
{{%/tabs%}}



{{%refer%}}
[Aggregators](./aggregators.html)
{{%/refer%}}


# Async Extension

When running with Java 8, it is possible to have an even simpler Space API with the `AsyncExtension`. AsyncExtension substituts the Space `AsyncFuture` with the Java8 `CompletableFuture` and thus can make the code more fluent.

There is no need to retrieve the entire data set from the Space to the client side  iterate the result set and perform the aggregation. This would be an expensive activity as it might return a large amount of data to the client application. The `Aggregators` allow you to perform the entire aggregation activity on the Space side, avoiding any data retrieval back to the client side.


**Example**


```java
import static org.openspaces.extensions.AsyncExtension.asyncRead;

asyncRead(gigaSpace, template).thenAccept(value -> {
    System.out.println("got " + value);
})

```
 