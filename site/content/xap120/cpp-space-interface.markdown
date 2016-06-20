---
type: post120
title:  The CPP Space Interface
categories: XAP120
parent: xap-cpp.html
weight: 200
---

{{% ssummary   %}}{{% /ssummary %}}


The `SpaceProxy` is the primary C++ interface that allows you to connect to the space and perform space operations. The basic space operations include the following:


- **write** -- writes an object into the space.
- **read** -- reads an object from the space that matches a given template.
- **take** -- reads an object from the space that matches the given template and removing it from the space in one atomic operation.
- **update** -- updates an entry in the space.
- **notify** -- notifies a specified listener object, when an object that matches a given template is written/taken/updated/expires in the space.

The `SpaceProxy` is used with the different space topologies and runtime modes in a transparent manner. You can use the `SpaceProxy` object with an embedded, remote, single, or clustered replicated or partitioned space.

The `SpaceProxy` supports single and batch space operations. Batch operations are used to optimize space operations with multiple objects and can boost the application performance when interacting with the space.

{{% refer %}}
Please refer to the [GigaSpaces c++ Documentation](http://www.gigaspaces.com/docs/cppdocs{{% currentversion %}}/annotated.html) for the API Documentation.
{{%/refer%}}

## Writing and Reading from Space

A C++ client may write and read objects from the space.

{{% indent %}}
![POJO_write.jpg](/attachment_files/POJO_write.jpg)
{{% /indent %}}

{{% indent %}}
![POJO_read.jpg](/attachment_files/POJO_read.jpg)
{{% /indent %}}

The following code example constructs a space proxy by passing a [space URL](./the-space-configuration.html) string into the `SpaceFinder.find()` method.
The returned object is the `SpaceProxy` object. This allows performing all space operations such as write, read, take, notify registration, etc.


```cpp
SpaceFinder		finder;
// Getting space proxy using the standard Space URL
SpaceProxyPtr spaceProxy = finder.find( "jini://lookup-host/container-name/space-name" );

// Prepare an object
Person person;
person.name = "Jack";
person.city = "New York";	// Routing field (must be specified if space is a cluster)
person.age = 40;

// Write the object to the space giving it lease time of 10 seconds
Lease lease = spaceProxy->write(&person, NULL_TX, 10000, 0, WRITE_ONLY);

// Prepare the template
Person personTemplate;
personTemplate.name = "Jack";
personTemplate.city = "New York";	// Routing field (must be specified if space is a cluster and timeout > 0)

// Read the person "Jack" from the space (use smart pointer). Wait 10 seconds at most.
PersonPtr personResponse;
personResponse.reset ( spaceProxy->read(&personTemplate, NULL_TX, 10000) );
if (personResponse == NULL) {
	std::cout << "ERROR: Failed to read person 'Jack'" << std::endl;
}

// Take the person "Jack" from the space. Wait at most 1 second.
personResponse.reset((Person*)spaceProxy->take( &personTemplate, NULL_TX, 1000));
if (personResponse == NULL) {
	std::cout << "ERROR: Failed to take person 'Jack'" << std::endl;
}
```

# Writing Batch of Objects to Space

In some cases, you might want to write a batch of objects into the space using one space call.

{{% indent %}}
![POJO_update_multi.jpg](/attachment_files/POJO_update_multi.jpg)
{{% /indent %}}

This can be used when the c++ application accesses a remote space. In this case, batch write operations is more efficient than multiple separate single write operations. This is because the communication protocol transforms the c++ objects to PBS, and their transportation to the space is performed in one single operations rather than multiple separate calls.

Below is an example for a `writeMultiple` call:


```cpp
// Prepare a vector (batch) of Person objects
std::vector<IEntry*> myBatch(100);
for (int i = 0; i < 100; i++) {
	Person* pPerson = new Person;
	pPerson->name = "Jack";
	pPerson->city = i % 2 ? "New York" : "London";	// Routing field (must be specified if space is a cluster)
	pPerson->age = i;
	myBatch[i] = pPerson;
}

// Write the batch
try {
	std::vector<Lease> results = spaceProxy->writeMultiple(myBatch,NULL_TX,Lease::FOREVER);
}
catch(XAPException &e){
	std::cout << "ERROR - writeMultiple failed" << std::endl;
}

// Memory cleanup
for (int i = 0; i < 100; i++) {
	delete myBatch[i];
}
```

# Reading Batch of Objects from Space

You can read a single object or multiple matching objects from the space.

{{% indent %}}
![POJO_read_multi.jpg](/attachment_files/POJO_read_multi.jpg)
{{% /indent %}}

When using the `readMultiple` operation, the returned object includes all of the matching objects found in the space. This operation should be used carefully, since it might return a large amount of objects; causing the space and the client to use a large amount of memory, and cause a load on the network.

Below is an example of performing the `readMultiple` operation using `SQLQuery`:


```cpp
Person personQueryTemplate;
personQueryTemplate.age = 30;
// Match all Person entries where 'city' begins with 'A' and 'age' is older than 30
SqlQuery personQuery(&personQueryTemplate, "city like 'N%' AND age > ?");
// Read all matching Person entries (at most 1000)
responseBatch = spaceProxy->readMultiple(personQuery,NULL_TX,1000);
std::cout << "Number of persons that live in New York aged 30+ is: " << responseBatch.size() << std::endl;

// Memory cleanup
for (int i = 0; i < responseBatch.size(); i++) {
	delete responseBatch[i];
}
```

# Receiving Notifications on Space Operations

The space allows c++ client applications to receive notifications when a matching event occurs in the space. To allow the c++ application to receive notifications, the following should be conducted:

- The `IRemoteEventListener` listener class should implemented
- Registering the listener via the `DataEventSession`

Once the listener is registered with a relevant template and operation type, and a matching event occurs in the space (as a result of write, take, update, or lease expiration) the registered client `IRemoteEventListener.notify()` implementation is called, passing the event object originator.

{{% indent %}}
![POJO_notify-api.jpg](/attachment_files/POJO_notify-api.jpg)
{{% /indent %}}

Below is an example for an `IRemoteEventListener` listener implementation:


```cpp
// The Listener class for a Person entry
 class PersonNotifyListener : public IRemoteEventListener
 {
 public:

 	// Implement IRemoteEventListener method
 	virtual bool notify( const RemoteEvent& remoteEvent )
 	{
 		IEntryPtr temp(remoteEvent.getIEntry());
 		return true;
 	}

 };
```

Below is an example for a registered listener with a template and a `WRITE` operation type:


```cpp
// Prepare the session
  EventSessionFactoryPtr factory( EventSessionFactory::getFactory(spaceProxy));

  // create a session configuration
  EventSessionConfigPtr config( EventSessionConfig::CreateSessionConfig(spaceProxy));
  config->setComType(ComType::UNICAST); // or MULTIPLEX,MULTICAST

  // creating a new DataEventSession
  DataEventSessionPtr session (factory->newDataEventSession(config) );

  // Create the listener using the class we defined
  PersonNotifyListener listener;

  // Register for notifications by adding the listener to the space with the Person template

  Person personTemplate;
  EventRegistrationPtr reg( session->addListener( &personTemplate, &listener, Lease::FOREVER, NotifyModifiers::NOTIFY_WRITE));
```

# Using Transactions

Space operations can be conducted using transactions. Transactions provide ACID: Atomicity, Consistency, Isolation, and Durability. For more information on using JavaSpaces transactions, see [Transaction Management](./transaction-management.html).

When using transactions, the following basic operations should be conducted:

1. Constructing the transaction manager
1. Constructing the transaction object
1. Performing the space operation(s)
1. Committing/rolling back the transaction

There are two types of transaction managers and their respective types of transactions:

- **Local** - for operations over a single space instance
- **Distributed** - for operations that span over multiple spaces

When working with only a single space instance, the Local Transaction (Manager) should be used. When working with a clustered topology, and performing operations that span over several cluster members, the Distributed Transaction (Manager) should be used.
Note, this can be avoided when working with the SBA model, where each cluster member works only with its local space instance.

Here is an example for creating a Local Transaction:


```cpp
SpaceFinder spaceFinder;
SpaceProxyPtr spaceProxy = spaceFinder.find("jini://lookup-host/container-name/space-name");
// Get the Local Transaction Manager
ITransactionManagerPtr localTxnMgr = spaceProxy->getLocalTransactionManager();
// Create a transaction
TransactionPtr txn = localTxnMgr->create();
A Local Transaction can also be created directly using:
TransactionPtr txn = spaceProxy->getLocalTransaction();
```

Here is an example of creating a Distributed Transaction based on Jini Mahalo:


```cpp
#define LOOKUP_GROUPS "MY-LOOKUP-GROUPS"	// Lookup groups for Jini Mahalo
SpaceFinder spaceFinder;
// The space must use the same lookup groups as the Jini Mahalo
SpaceProxyPtr spaceProxy = spaceFinder.find("jini://lookup-host/container-name/space-name");
// Define a Jini Mahalo config object
IConfigPtr config(new JiniConfig("", "", LOOKUP_GROUPS));
// Get a Distributed Transaction Manager based on Jini Mahalo
ITransactionManagerPtr distTxnMgr = spaceProxy-getDistributedTransactionManager(config);
// Create a transaction
TransactionPtr txn = distTxnMgr->create();
```

Once a transaction is created it can be used with any of the space operations until it is either committed or aborted.

Here is an example of using a transaction:


```cpp
Person person;
Person personTemplate;
PersonPtr personResponse;

// Create a local transaction with lease time of 10 seconds
TransactionPtr txn = spaceProxy->getLocalTransaction(10000);

// Prepare the entry
person.name = "Jack";
person.city = "New York";	// Routing field (must be specified if space is a cluster)
person.age = 40;

// Write the entry to the space giving it lease time forever. Use the Transaction.
Lease lease = spaceProxy->write(&person, txn, Lease::FOREVER);

// Update the entry inside the transaction (returns the old value)
person.age = 42;
personResponse.reset ( spaceProxy->update(&person, txn, Lease::FOREVER, 10000, UPDATE_ONLY));
if (personResponse == NULL || personResponse->age != 40) {
	std::cout << "ERROR: Failed to update person age" << std::endl;
}

// Commit the Transaction
txn->commit();
```

# Iterating Large Amounts of Data

The GSIterator provides the ability to exhaustively read through all of the Entries from a space that match one or more templates.
.

A GSIterator is created by providing one or more matching templates. Iterating through the matching entries is done by calling the `getNext` method.

The following example demonstrates how to create and use a GSIterator that matches `Person` entries:


```cpp
// Create a GSIterator using an empty Person template to match all Person entries
Person personTemplate;
GSIteratorPtr gsIterator(new GSIterator(spaceProxy, &personTemplate, 100, ExistingAndFutureEntries, Lease::FOREVER));

// Iterate over the entries
PersonPtr nextPerson;
while (gsIterator->hasNext())
{
	nextPerson.reset((Person*)gsIterator->getNext());
	if (nextPerson == NULL) {
		std::cout << "ERROR: Failed to get next person" << std::endl;
	}
	else {
		std::cout << nextPerson->name << " | " << nextPerson->city << " | " << nextPerson->age << std::endl;
	}
}
```

Note the use of `ExistingAndFutureEntries` argument when creating the GSIterator. It enables matching both entries that are already in space at the time of creation, as well as entries that will be written afterwards.

Here is another example, this time using _blocking iteration_:


```cpp
// Create a GSIterator
Person personTemplate;
GSIteratorPtr gsIteratorBlocking(new GSIterator(spaceProxy, &personTemplate, 100, ExistingAndFutureEntries, Lease::FOREVER));
IEntryPtr nextValue;
const long NEXT_TIMEOUT = 2000; // 2 seconds
count = 0;
while (true)
{
	nextValue.reset(gsIteratorBlocking->getNext(NEXT_TIMEOUT));
	if (nextValue == NULL)
		break;
	count++;
}
```

# Parellilazing and Scaling Using the c++ Processing Unit

The POCO c++ worker API allows a c++ developer to embed native code within the same process space as the SBA container; and can also be used to call other languages such as C, running the business logic as part of the same memory address as the space.

The process of building a c++ Worker is very straightforward: the developer implements a shared object or dynamically loaded library that derives from the abstract GigaSpaces interface, exports a couple of well-known entry points and he is all set to go.

The c++ worker implementation must derive from the `ICppWorker` class, so that the GigaSpaces space worker can load it dynamically during runtime.

The `ICppWorker` includes the following methods:


| Return value | Method |
|:-------------|:-------|
| `virtual` | `\~ICppWorker()` -- destructor |
| `virtual const char *` | `cppType()=0` -- well-known entry point `cppType()` |
| `virtual const char *` | `className()=0` -- well-known entry point `className()` |
| `virtual bool` | `Initialize(IWorkerPeer \*Host)=0` -- initialize |
| `virtual CommandObjectPtr` | ` run(CommandObjectPtr );` |
| `virtual bool` | `Destroy()=0` -- destroy. This entry point is called before the worker becomes eligible for unload. |

## Architecture

The basic architecture consists of the following components:

- **c++ application process** -- this process accesses the space via a remote call.
- **Space** -- runs in standalone mode or as part of a Processing Unit.
- **Java worker wrapper** running in the space. This worker is injected with the space proxy.
- **c++ worker wrapper** -- this worker might pass objects to the `ICppWorker` implementation.
- **`ICppWorker` implementation** -- the c++ implementation can access the space directly, or receive data from the space via the Java worker wrapper and its c++ worker wrapper counterpart.

{{% indent %}}
![cpp-arch-image012.jpg](/attachment_files/cpp-arch-image012.jpg)
{{% /indent %}}
