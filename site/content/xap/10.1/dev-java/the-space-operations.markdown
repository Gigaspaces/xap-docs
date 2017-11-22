---
type: post101
title:  Operations
categories: XAP101
weight: 300
parent: the-gigaspace-interface-overview.html
---


{{% ssummary %}}{{%/ssummary%}}


XAP provides a simple space API using the [GigaSpace]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) interface for interacting with the space.


The interface includes the following main operations:

{{%section%}}
{{%column width="50%" %}}
{{%panel    "Write objects into the Space:"%}}
[write](#write) one object into the space{{<wbr>}}
[writeMultiple](#writeMultiple) objects into the Space{{<wbr>}}
[asynchronous write](#asynchronousWrite) to the Space
{{%/panel%}}
{{%/column%}}
{{%column width="50%" %}}
{{%panel    "Change objects in Space:"%}}
[change](#change) one object in Space{{<wbr>}}
		  [changeMultiple](./change-api.html) objects in Space {{<wbr>}}
[asynchronous change](./change-api.html) of objects
{{%/panel%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="50%" %}}
{{%panel     "Reading objects from the Space:"%}}
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
{{%panel   "Removing objects from the Space:"%}}
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

The `GigaSpace` interface provides a simpler space API by utilizing Java 5 generics, and allowing sensible defaults. Here are some examples of the space operations as defined within `GigaSpace`:


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

In order to write objects to the Space, you use the write method of the GigaSpace interface. The write method is used to write objects if these are introduced for the first time, or update them if these already exist in the space. In order to override these default semantics, you can use the overloaded write methods which accept update modifiers such as WriteModifiers.UPDATE_ONLY.



#### POJO Example

The following example writes an `Employee` object into the space:


```java
    GigaSpace space = ....


    Employee employee = new Employee("Last Name", new Integer(32));
    employee.setFirstName("first name");
    LeaseContext<Employee> lc = space.write(employee);
```

#### SpaceDocument Example

Here is an example how you create a SpaceDocument, register it with the space and then write it into the space:


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


#### Time To Live

To write an object into the space with a limited time to live you should specify [a lease value](./leases-automatic-expiration.html) (in millisecond). The object will expire automatically from the space.


```java
   gigaSpace.write(myObject, 10000)
```

{{%anchor writeMultiple%}}

{{%anchor writeMultiple%}}

#### Write Multiple

When writing a batch of objects into the space, these should be placed into an array to be used by the `GigaSpace.writeMultiple` operation. The returned array will include the corresponding `LeaseContext` object.



#### Example


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

-  should be performed with transactions - this allows the client to roll back the space to its initial state prior the operation was started, in case of a failure.
-  make sure `null` values are not part of the passed array.
-  you should verify that duplicated entries (with the same ID) do not appear as part of the passed array, since the identity of the object is determined based on its `ID` and not based on its reference. This is extremely important with an embedded space, since `writeMultiple` injects the ID value into the object after the write operation (when autogenerate=false).

- Exception handling - the operation many throw the following Exceptions.
    - [WriteMultiplePartialFailureException]({{% api-javadoc %}}/org/openspaces/core/WriteMultiplePartialFailureException.html)
    - [WriteMultipleException]({{% api-javadoc %}}/org/openspaces/core/WriteMultipleException.html)

 


## Return Previous Value

When updating an object which already exists in the space, in some scenarios it is useful to get the previous value of the object (before the update). This previous value is returned in result `LeaseContext.getObject()` when using the `RETURN_PREV_ON_UPDATE` modifier.



```java
  LeaseContext<MyData> lc = space.write(myobject,WriteModifiers.RETURN_PREV_ON_UPDATE.add(WriteModifiers.UPDATE_OR_WRITE));
  MyData previousValue = lc.getObject();
```

{{% info %}}
Since in most scenarios the previous value is irrelevant, the default behavior is not to return it (i.e. `LeaseContext.getObject()` return null). The `RETURN_PREV_ON_UPDATE` modifier is used to indicate the previous value should be returned.
{{%/info%}}

{{%anchor asynchronousWrite%}}

## Asynchronous write

Asynchronous `write` operation can be implemented using a [Task](./task-execution-over-the-space.html), where the `Task` implementation include a write operation. With this approach the `Task` is sent to the space and executed in an asynchronous manner. The write operation itself will be completed once both the primary and the backup will acknowledge the operation. This activity will be performed as a background activity from the client perspective.


#### Example


```java
  GigaSpace space = new GigaSpaceConfigurer (new UrlSpaceConfigurer("jini://*/*/space")).gigaSpace();
  MyClass obj = new MyClass(1,"AAA");
  space.write(obj,WriteModifiers.ONE_WAY);
```


## Modifiers

 
For further details on each of the available modifiers see: [WriteModifiers]({{% api-javadoc %}}/com/gigaspaces/client/WriteModifiers.html)
 

{{%note%}}
Writing an object into a space might generate [notifications](./notify-container.html) to registered objects.
{{%/note%}}

{{%accordion%}}
{{%accord title="Method summary..."%}}

Writes a new object to the space, returning its LeaseContext.[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#write(T))


```java
<T> LeaseContext<T> write(T entry) throws DataAccessException
<T> LeaseContext<T> write(T entry, long lease, long timeout, WriteModifiers modifiers) throws DataAccessException
......

```

Writes new objects to the space, returning its LeaseContexts.[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#writeMultiple(T[]))

```java
<T> LeaseContext<T>[] writeMultiple(T[] entries) throws DataAccessException
<T> LeaseContext<T>[] writeMultiple(T[] entries, long[] leases, WriteModifiers modifiers) throws DataAccessException
......
```


| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|lease       |Time to live | Lease.FOREVER|milliseconds|
|timeout     | The timeout of an update operation, in milliseconds. If the entry is locked by another transaction wait for the specified number of milliseconds for it to be released. | 0  |
|[WriteModifiers]({{% api-javadoc %}}/com/gigaspaces/client/WriteModifiers.html)|Provides modifiers to customize the behavior of write operations | UPDATE_OR_WRITE  |
|[LeaseContext]({{% api-javadoc %}}/com/j_spaces/core/LeaseContext.html) |LeaseContext is a return-value encapsulation of a write operation.| |
{{%/accord%}}
{{%/accordion%}}



{{%anchor change%}}

# The Change Operation

The [GigaSpace.change]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html) and the [ChangeSet]({{% api-javadoc %}}/index.html?com/gigaspaces/client/ChangeSet.html) allows updating existing objects in space, by specifying only the required change instead of passing the entire updated object.
Thus reducing required network traffic between the client and the space, and the network traffic generated from replicating the changes between the space instances (e.g between the primary space instance and its backup).



Example:

The following example demonstrates how to update the property 'firstName' of an object of type 'Person' with id 'myID'.


```java
GigaSpace space = // ... obtain a space reference
String id = "myID";
IdQuery<WordCount> idQuery = new IdQuery<Person>(Person.class, id, routing);
space.change(idQuery, new ChangeSet().set("firstName", "John"));
```

{{%refer%}}
[The Change API](./change-api.html)
{{%/refer%}}



{{%anchor read%}}

# The Read Operation


The  read operations query the space for an object that matches the criteria provided.
If a match is found, a copy of the matching object is returned.
If no match is found, null is returned. Passing a null reference as the template will match any object.

Any matching object can be returned. Successive read requests with the same template may or may not return equivalent objects, even if no intervening modifications have been made to the space.
Each invocation of `read` may return a new object even if the same object is matched in the space.
If you would like to read objects in the same order they have been written into the space you should perform the read objects in a [FIFO mode](./fifo-overview.html).

{{% note %}}
The `read` operation default timeout is `JavaSpace.NO_WAIT`.
{{% /note %}}

The read operation can be performed with the following options:

- Template matching
- By Id
- By IdQuery
- By SQLQuery

To learn more about the different options refer to [Querying the Space](./querying-the-space.html)

Examples:

The following example writes an `Employee` object into the space and reads it back from the space :


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

## Read multiple

The GigaSpace interface provides simple way to perform bulk read operations. You may read a large amount of objects in one call.

Examples:


```java
   Employee emps[] = new Employee[2];
   emps[0] = new Employee("Last Name A", new Integer(31));
   emps[1] = new Employee("Last Name B", new Integer(32));

   space.writeMultiple(emps);

   // Read multiple by template
   Employee[] employees = space.readMultiple(new Employee());

   // Read multiple by SQLQuery
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
   				"lastName ='Last Name B'");
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

- boosts the performance, since it perform multiple operations using one call. These methods returns the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, these batch operations can be up to 10 times faster than multiple single based operations.
- should be handled with care, since they can return a large data set (potentially all the space data). This might cause an out of memory error in the space and client process. You should use the [GSIterator](#Space Iterator) to return the result in batches (paging) in such cases.
- **dos not support timeout** operations. The simple way to achieve this is by calling the `read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.
- Exception handling - operation many throw the following Exceptions. [ReadMultipleException]({{% api-javadoc %}}/org/openspaces/core/ReadMultipleException.html)
 

{{%anchor readIfExists%}}

## Read if exists
A readIfExists operation will return a matching object, or a null if there is currently no matching object in the space.
If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value.
If at the end of that time no value can be returned that would not interfere with transactional state, null is returned. Note that, due to the remote nature of the space, read and readIfExists may throw a RemoteException if the network or server fails prior to the timeout expiration.

Example:


```java
    Employee employee = new Employee("Last Name", new Integer(32));
	employee.setFirstName("first name");
	space.write(employee);

	SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
	Employee e = space.readIfExists(query);
```

{{%anchor asynchronousRead%}}

## Asynchronous Read

The GigaSpace interface supports asynchronous (non-blocking) read operations through the GigaSpace interface. It returns a [Future\<T\>](http://download.oracle.com/javase/6/docs/api/java/util/concurrent/Future.html) object, where T is the type of the object the request returns. Future<T>.get() can be used to query the object to see if a result has been returned or not.

Alternatively, asyncRead also accept an implementation of [AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html), which will have its `AsyncFutureListener.onResult` method called when the result has been populated. This does not affect the return type of the `Future<T>`, but provides an additional mechanism for handling the asynchronous response.


Example:


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

## Passing an AsyncFutureListener with Java 8 lambda syntax:


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

Examples:

```java
	Employee template = new Employee();

    // Read objects in a FIFO mode
 	Employee e = space.read(template, 0, ReadModifiers.FIFO);

    // Dirty read
	Employee e = space.read(template, 0, ReadModifiers.DIRTY_READ);
```


 
For further details on each of the available modifiers see: [ReadModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ReadModifiers.html)
 

{{%accordion%}}
{{%accord title="Method summary..."%}}

Read by template:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#read(T))

```java
<T> T read(T template) throws DataAccessException
<T> T read(T template, long timeout, ReadModifiers modifiers)throws DataAccessException
.....
```

Read by Id:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#readById(java.lang.Class,%20java.lang.Object))

```java
<T> T readById(Class<T> clazz, Object id) throws DataAccessException
<T> T readById(Class<T> clazz, Object id, Object routing, long timeout, ReadModifiers modifiers)throws DataAccessException
.....
```

Read by ISpaceQuery:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#read(com.gigaspaces.query.ISpaceQuery))

```java
<T> T read(ISpaceQuery<T> query, Object id)throws DataAccessException
<T> T read(ISpaceQuery<T> query, Object routing, long timeout, ReadModifiers modifiers)throws DataAccessException
....
```

Read multiple:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#readMultiple(com.gigaspaces.query.ISpaceQuery))

```java
<T> T[] readMultiple(ISpaceQuery<T> query) throws DataAccessException
<T> T[] readMultiple(ISpaceQuery<T> query, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T[] readMultiple(T template) throws DataAccessException
<T> T[] readMultiple(T template, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T[] readMultiple(ISpaceQuery<T> template, int maxEntries, ReadModifiers modifiers) throws DataAccessException
...
```

Asynchronous Read:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#asyncRead(com.gigaspaces.query.ISpaceQuery))

```java
<T> AsyncFuture<T> asyncRead(T template) throws DataAccessException
<T> AsyncFuture<T> asyncRead(T template, long timeout, ReadModifiers modifiers, AsyncFutureListener<T> listener) throws DataAccessException
<T> AsyncFuture<T> asyncRead(ISpaceQuery<T> query)throws DataAccessException
<T> AsyncFuture<T> asyncRead(ISpaceQuery<T> query, long timeout, ReadModifiers modifiers, AsyncFutureListener<T> listener)throws DataAccessException
.....
```

Read if exists:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#readIfExists(com.gigaspaces.query.ISpaceQuery))

```java
<T> T readIfExists(T template)throws DataAccessException
<T> T readIfExistsById(Class<T> clazz, Object id)throws DataAccessException
<T> T readIfExistsById(Class<T> clazz, Object id, Object routing, long timeout, ReadModifiers modifiers) throws DataAccessException
<T> T readIfExistsById(IdQuery<T> query, long timeout, ReadModifiers modifiers) throws DataAccessException
....
```




| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | POJO, SpaceDocument|| |
|timeout     | Time to wait for the response| 0  |  milliseconds |
|query| [ISpaceQuery]({{% api-javadoc %}}/com/gigaspaces/query/ISpaceQuery.html)|      | |
|[AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html) |Allows to register for a callback on an AsyncFuture to be notified when a result arrives||
|[ReadModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ReadModifiers.html)|Provides modifiers to customize the behavior of read operations | NONE  |  |

{{%/accord%}}
{{%/accordion%}}




{{%anchor take%}}

# The Take Operation


The `take` operations behave exactly like the corresponding `read` operations, except that the matching object is **removed from the space on one atomic operation. Two `take` operations will never return** copies of the same object, although if two equivalent objects were in the space the two `take` operations could return equivalent objects.



If a `take` returns a non-null value, the object has been removed from the space, possibly within a transaction. This modifies the claims to once-only retrieval: A take is considered to be successful only if all enclosing transactions commit successfully.
If a `RemoteException` is thrown, the take may or may not have been successful.
If an `UnusableEntryException` is thrown, the take `removed` the unusable object from the space.
If any other exception is thrown, the take did not occur, and no object was removed from the space.


{{%note%}}
If you would like to take objects from the space in the same order they have been written into the space you should perform the take objects in a [FIFO mode](./fifo-support.html).

Taking an object from the space might generate [notifications](./notify-container.html) to registered objects/queries.
{{%/note%}}

The take operation can be performed with the following options:

- Template matching
- By Id
- By IdQuery
- By SQLQuery

{{%refer%}}
To learn more about the different options refer to [Querying the Space](./querying-the-space.html)
{{%/refer%}}

Examples:

The following example writes an `Employee` object into the space and removes it from the space :


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

## Take multiple


The GigaSpace interface provides simple way to perform bulk take operations. You may take large amount of objects in one call.


{{% info %}}
To remove a batch of objects without returning these back into the client use `GigaSpace.clear(SQLQuery)`;
{{%/info%}}

Examples:


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

-  boosts the performance, since it performs multiple operations using one call. This method returns the matching results in one result object back to the client. This allows the client and server to utilize the network bandwidth in an efficient manner. In some cases, this batch operation can be up to 10 times faster than multiple single based operations.
-  should be handled with care, since it can return a large data set (potentially all the space data). This might cause an out of memory error in the space and client process. You should use the [GSIterator](#Space Iterator) to return the result in batches (paging) in such cases.
-  should be performed with transactions - this allows the client to roll back the space to its initial state prior the operation was started, in case of a failure.
-  operation **dos not support timeout** operations. The simple way to achieve this is by calling the `read` operation first with the proper timeout, and if non-null values are returned, perform the batch operation.
-  in the event of a take error, DataAccessException will wrap a TakeMultipleException, accessible via DataAccessException.getRootCause().
 

{{%anchor takeIfExists%}}

## Take if exists
A takeIfExists operation will return a matching object, or a null if there is currently no matching object in the space.
If the only possible matches for the template have conflicting locks from one or more other transactions, the timeout value specifies how long the client is willing to wait for interfering transactions to settle before returning a value.
If at the end of that time no value can be returned that would not interfere with transactional state, null is returned.

Example:


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


Example:


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

## Passing an AsyncFutureListener with Java 8 lambda syntax:


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

Examples:

```java
	Employee template = new Employee();

    // Takes objects in a FIFO mode
 	Employee e = space.take(template, 0, TakeModifiers.FIFO);

    // Takes objects according to FIFO group
	Employee e = space.take(template, 0, TakeModifiers.FIFO_GROUPING_POLL);
```


 
For further details on each of the available modifiers see: [TakeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/TakeModifiers.html)
 

{{%accordion%}}
{{%accord title="Method summary..."%}}

Take by template:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#take(T))

```java
<T> T take(T template) throws DataAccessException
<T> T take(T template, long timeout, TakeModifiers modifiers)throws DataAccessException
.....
```

Take by Id:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#takeById(java.lang.Class,%20java.lang.Object))

```java
<T> T takeById(Class<T> clazz, Object id) throws DataAccessException
<T> T takeById(Class<T> clazz, Object id, Object routing, long timeout, TakeModifiers modifiers)throws DataAccessException
.....
```

Take by ISpaceQuery:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#take(com.gigaspaces.query.ISpaceQuery))

```java
<T> T take(ISpaceQuery<T> query, Object id)throws DataAccessException
<T> T take(ISpaceQuery<T> query, Object routing, long timeout, TakeModifiers modifiers)throws DataAccessException
....
```

Take multiple:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#takeMultiple(com.gigaspaces.query.ISpaceQuery))

```java
<T> T[] takeMultiple(ISpaceQuery<T> query) throws DataAccessException
<T> T[] takeMultiple(ISpaceQuery<T> query, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T[] takeMultiple(T template) throws DataAccessException
<T> T[] takeMultiple(T template, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T[] takeMultiple(ISpaceQuery<T> template, int maxEntries, TakeModifiers modifiers) throws DataAccessException
...
```

Asynchronous take:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#asyncTake(com.gigaspaces.query.ISpaceQuery))

```java
<T> AsyncFuture<T> asyncTake(T template) throws DataAccessException
<T> AsyncFuture<T> asyncTake(T template, long timeout, TakeModifiers modifiers, AsyncFutureListener<T> listener) throws DataAccessException
<T> AsyncFuture<T> asyncTake(ISpaceQuery<T> query)throws DataAccessException
<T> AsyncFuture<T> asyncTake(ISpaceQuery<T> query, long timeout, TakeModifiers modifiers, AsyncFutureListener<T> listener)throws DataAccessException
.....
```

Take if exists:[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#takeIfExists(com.gigaspaces.query.ISpaceQuery))

```java
<T> T takeIfExists(T template)throws DataAccessException
<T> T takeIfExistsById(Class<T> clazz, Object id)throws DataAccessException
<T> T takeIfExistsById(Class<T> clazz, Object id, Object routing, long timeout, TakeModifiers modifiers) throws DataAccessException
<T> T takeIfExistsById(IdQuery<T> query, long timeout, TakeModifiers modifiers) throws DataAccessException
....
```




| Modifier and Type | Description | Default | Unit|
|:-----|:------------|:--------|:----|
| T          | POJO, SpaceDocument|| |
|timeout     | Time to wait for the response| 0  |  milliseconds |
|query| [ISpaceQuery]({{% api-javadoc %}}/com/gigaspaces/query/ISpaceQuery.html)|      | |
|[AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html) |Allows to register for a callback on an AsyncFuture to be notified when a result arrives||
|[TakeModifiers]({{% api-javadoc %}}/com/gigaspaces/client/TakeModifiers.html)|Provides modifiers to customize the behavior of take operations | NONE  |  |

{{%/accord%}}
{{%/accordion%}}



{{%anchor clear%}}

# The Clear Operation


You can use `GigaSpace.clear` to remove objects from the space. When using the clear operation no object/objects are returned.


Examples:


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
   IdQuery<Employee> query = new IdQuery<Employee>(Employee.class,
   				new Integer(32));
   space.clear(query);

   // Clear with Modifier
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
   space.clear(query, ClearModifiers.EVICT_ONLY);
```



{{%accordion%}}
{{%accord title="Method summary..."%}}

Clears objects from space.[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#clear(java.lang.Object))


```java
void clear(T entry) throws DataAccessException
void clear(T entry, ClearModifiers modifiers) throws DataAccessException
void clear(ISpaceQuery<T> query) throws DataAccessException
......

```


| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|query         | SQLQuery, IdQuery||
|[ClearModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ClearModifiers.html)|Provides modifiers to customize the behavior of the clear operations | NONE  |
{{%/accord%}}
{{%/accordion%}}



{{%anchor count%}}

# The Count Operation


You can use `GigaSpace.count` to count objects in a space.


Examples:


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
   SQLQuery<Employee> query = new SQLQuery<Employee>(Employee.class,
				"firstName='first name'");
    int count = space.count(query, CountModifiers.EXCLUSIVE_READ_LOCK);
```



{{%accordion%}}
{{%accord title="Method summary..."%}}

Count objects in space.[Java API]({{% api-javadoc %}}/org/openspaces/core/GigaSpace.html#count(java.lang.Object))


```java
int count(T entry) throws DataAccessException
int count(T entry, ClearModifiers modifiers) throws DataAccessException
int count(ISpaceQuery<T> query) throws DataAccessException
......

```


| Modifier and Type | Description | default |
|:-----|:------------|:-------- |
|T          | POJO, SpaceDocument||
|query         | SQLQuery, IdQuery||
|[CountModifiers]({{% api-javadoc %}}/com/gigaspaces/client/CountModifiers.html)|Provides modifiers to customize the behavior of the count operations | NONE  |
{{%/accord%}}
{{%/accordion%}}


{{%anchor counters%}}

# Counters

The `ISpaceProxy.Change` API allows you to increment or decrement an Numerical field within your Space object or Document. This change may operate on a numeric property only (byte,short,int,long,float,double) or their corresponding Boxed variation. To maintain a counter you should use the Change operation with the `ChangeSet` increment/decrement method that adds/subtract the provided numeric value to the existing counter.


Example:

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

There is no need to retrieve the entire data set from the space to the client side , iterate the result set and perform the aggregation. This would be an expensive activity as it might return large amount of data into the client application. The Aggregators allow you to perform the entire aggregation activity at the space side avoiding any data retrieval back to the client side.


Example:

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



When running with Java8, it is possible to have an even simpler Space API with the `AsyncExtension`.
AsyncExtension substituts the Space `AsyncFuture` with the Java8 `CompletableFuture` and thus can make the code more fluent.

There is no need to retrieve the entire data set from the Space to the client side , iterate the result set and perform the aggregation. This would be an expensive activity as it might return a large amount of data into the client application. The `Aggregators` allow you to perform the entire aggregation activity at the Space side avoiding any data retrieval back to the client side.



Example:


```java
import static org.openspaces.extensions.AsyncExtension.asyncRead;

asyncRead(gigaSpace, template).thenAccept(value -> {
    System.out.println("got " + value);
})

```








{{% include "/COM/xap/10.2/dev-java/ops-write.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-change.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-read.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-take.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-clear.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-count.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-counters.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-aggregation.markdown" %}}
{{% include "/COM/xap/10.2/dev-java/ops-async-extension.markdown" %}}
