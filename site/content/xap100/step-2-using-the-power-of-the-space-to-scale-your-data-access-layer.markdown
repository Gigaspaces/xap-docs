---
type: post100
title:  Step 2 - Using the Power of the Space to Scale Your Data Access Layer
categories: XAP100
weight: 200
parent: your-first-jpa-application.html
---

{{% ssummary %}}This step explains how to utilize the power of the grid when implementing the data access layer of the PetClinic application (namely the `Clinic` interface), and seamlessly use distributed computing paradigms such as Map/Reduce and data and processing colocation{{% /ssummary %}}

# Before You Begin

We recommend that you do the following before starting this step of the Tutorial:

- Follow [Step 1](./step-1-adjusting-your-jpa-domain-model-to-the-xap-jpa-implementation.html) of this tutorial

#Preparing the `PetClinicService`
The `PetClinicService` is an implementation of the `Clinic` interface that the Spring MVC layer of the application is using. The easiest way to implement this service is simply define an `EntityManagerFactory` that accesses the remote space from within the web application (similar the traditional database backed implementation). But in our case we want to be able to take advantage of the built in capabilities  that GigaSpaces XAP provides for scaling your business logic:

- Data and processing colocation: instead of running your JPA access code away from the data (namely in the web application), you can actually run it with the data, such that all JPA operations are done in memory
- Map/Reduce: this means that you're able to run your business logic on multiple nodes on the space cluster, and reduce the results back at the client side to provide the same experience as invoking the business logic on the client side.
- Smart content based routing: In a distributed data store, it's very important in terms of performance and scalability to be able to route each data accessing operation to the right node, i.e. the node on which the data your operate on actually resides instead of sending the operation to all of the cluster nodes.

We can easily achieve all of the above by using the GigaSpaces XAP [Space Based Remoting](./space-based-remoting.html) support, and making a few small adaptations to the clinic interface (adding a few annotations to it).

#Adjusting the `Clinic` Interface
As mentioned above, we will use [Space Based Remoting](./space-based-remoting.html) to back the `Clinic` interface. The actual implementation will run on each of the Space nodes we will deploy. The first thing we need to do is declare for each operation (method) whether it will be sent to the entire cluster or to a specific Space partition. In case an operation is sent to the entire cluster, we also need to tell the client how to aggregate (or _reduce_) the results on from all cluster members. This is done using a reducer class which implements the [RemoteResultReducer](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/remoting/RemoteResultReducer.html) interface.

All of the above is done by applying the `@ExecutorRemotingMethod` annotation (new in 8.0.1) to each of the methods. This annotation has several attributes:

- `broadcast` - should be set to `true` in case the entire cluster must be touched or false, in case the method only touches specific partition. In case of a cluster wide task the reducer attribute must be applied. In case of the `Clinic` interface, methods that need to scan the entire data set such as `getVets()`, `findOwners()` or `loadPet()` must be invoked against all nodes and therefore will be marked as `broadcast = true`.
- `reducer` - specifies the name of the Spring bean to use for results aggregation.

The `Clinic` service has 3 reducers: the `getVets()` method uses the `GetVetsReducer` class. The `findOwners()` method uses the `FindOwnersReducer` class, and the `loadPet()` method uses the `LoadPetResultReducer` class.
In case the invoked method needs to query a specific partition, we should specify to which partition the call will be routed. We can do this using the [@Routing](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/remoting/Routing.html) annotation on a specific method parameters, or the a use an implementation of the [RemoteRoutingHandler](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/remoting/RemoteRoutingHandler.html) interface and assign it to the `remoteRoutingHandler` attribute. This gives you the required flexibility and keeps the implementation cleaner.
The `Clinic` service is using 2 handlers: the `storePet()` and the `deletePet()` methods use the `PetRoutingHandler` class. The `storeVisit()` method uses the `VisitRoutingHandler`.

Finally, we specify a [RemoteInvocationAspect](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?org/openspaces/remoting/RemoteInvocationAspect.html) implementation which intercepts the `Clinic` methods before they are actually sent to the space for execution. In our case there are two aspects:

- The `PetTypesAspect`, which is a simple aspect that returns the `PetType` enum values so that the client call will never be sent to the space (since it's not needed).
- The `IdGeneratingInvocationAspect`, which generates unique entity IDs when needed (currently XAP JPA does not support auto-generation of non-String IDs). This is essential when writing new objects to the space using JPA. The aspect simply invokes an ID generation service for generating new cluster wide unique IDs before actually sending the call to the space.
Here's a snippet of the `Clinic` interface after adding all the XAP related annotations:


```java
...
public interface Clinic {
   /**
    - Retrieve all <code>Vet</code>s from the data store.
    - @return a <code>Collection</code> of <code>Vet</code>s
    */
    @ExecutorRemotingMethod(broadcast=true, remoteResultReducer = "getVetsReducer")
    Collection<Vet> getVets() throws DataAccessException;

   /**
    - Retrieve all <code>PetType</code>s from the data store.
    - @return a <code>Collection</code> of <code>PetType</code>s
    */
    @ExecutorRemotingMethod(broadcast=false, remoteInvocationAspect="petTypesAspect")
    Collection<PetType> getPetTypes() throws DataAccessException;

   /**
    - Retrieve <code>Owner</code>s from the data store by last name,
    - returning all owners whose last name <i>starts</i> with the given name.
    - @param lastName Value to search for
    - @return a <code>Collection</code> of matching <code>Owner</code>s
    - (or an empty <code>Collection</code> if none found)
    */
    @ExecutorRemotingMethod(broadcast=true, remoteResultReducer="findOwnersReducer")
    Collection<Owner> findOwners(String lastName) throws DataAccessException;

...
```

The clinic service relies on a connection to the remote space. This connection is initialized in a Spring configuration file (or alternatively can be done within the application's code). The web application's Spring configuration file is located at `WEB-INF/spring/applicationContext-gs.xml` within the web application. See below the Spring configuration snippet.


```xml
...
 <os-core:space-proxy  id="space" name="petclinic"   lookup-timeout="20000" lookup-groups="${user.name}"/>

    <os-core:distributed-tx-manager id="jiniTransactionManager" />

    <os-core:giga-space id="petclinic" space="space" tx-manager="jiniTransactionManager"/>

    <bean id="dummyDataCreator" class="org.springframework.samples.petclinic.util.DummyDataCreator">
        <property name="dataFileResource" value="classpath:META-INF/dummyData.json"/>
    </bean>

    <bean id="idGenerator" class="org.springframework.samples.petclinic.gigaspaces.idgen.IdGeneratorImpl"/>
    <bean id="idGeneratingInvocationAspect" class="org.springframework.samples.petclinic.gigaspaces.IdGeneratingInvocationAspect"/>

    <bean id="findOwnersReducer" class="org.springframework.samples.petclinic.gigaspaces.FindOwnersReducer"/>
    <bean id="getVetsReducer" class="org.springframework.samples.petclinic.gigaspaces.GetVetsReducer"/>
    <bean id="petTypesAspect" class="org.springframework.samples.petclinic.gigaspaces.PetTypesAspect"/>
    <bean id="loadPetResultReducer" class="org.springframework.samples.petclinic.gigaspaces.LoadPetResultReducer"/>
    <bean id="petRoutingHandler" class="org.springframework.samples.petclinic.gigaspaces.PetRoutingHandler"/>
    <bean id="visitRoutingHandler" class="org.springframework.samples.petclinic.gigaspaces.VisitRoutingHandler"/>

    <os-remoting:executor-proxy id="clinicProxy" giga-space="petclinic" interface="org.springframework.samples.petclinic.Clinic"/>

    <context:annotation-config/>

    <tx:annotation-driven transaction-manager="jiniTransactionManager"/>

</beans>
```

#Changes to the Service Implementation:
The following changes had been made to the service implementation:
The `PetClinicService.loadPet(int id)` method can't use the JPA `EntityManager.find()` method since `Pet` is not a root entity in the space but rather an embedded object within the `Owner` entity. Therefore, this method must fetch the Owner using a query with the JPQL `JOIN` syntax and then iterate through the `Owner`'s `Pet`s to return the right `Pet` instance.
The same method of implementation is also applied in the `deletePet()` and `storeVisit()` methods.


```java
...
@Transactional(readOnly = true)
public Pet loadPet(int id) {
    Query query = em.createQuery("SELECT o FROM org.springframework.samples.petclinic.Owner o JOIN o.petsInternal p WHERE p.id = :id");
    query.setParameter("id", id);
    List<Owner> owners = query.getResultList();
    if (!owners.isEmpty()) {
        Owner owner = owners.get(0);
        for (Pet pet : owner.getPets()) {
           if (pet.getId() == id) {
               return pet;
           }
        }
    }
    return null;
}
...
```

#The Id Generator
The Id Generator functionality is part of the processor module. It consists of the following classes:

- The `IdGenerator` interface that exposes the `generateId()` method.
- The `IdGeneratorImpl` class that implements `IdGenerator` interface.
- The `IdCounterEntry` which is the Id counter stored in the space.
- The `IdObjectInitializer` which is the Spring initializing bean that writes the Id counter entry to the space when the application is initialized.
The Id generator implementation is quite straight forward. Since all the partitions are part of the same space, it is the natural context for storing the Id counter.
The initializer (`IdObjectInitializer`) writes the counter only to the primary node of the first partition of the space when the application starts.
Whenever the Clinic service needs a new unique Id, it invokes the `IdGeneratorImpl.generateId()` that gets a range of IDs from the space whenever it is out of IDs and returns  the current Id from the range and increment the counter by one.
You can find more details on the cluster wide Id Generator pattern [here](/sbp/global-id-generator.html).


```java
...
public class IdGeneratorImpl implements IdGenerator {
    @Resource
    private GigaSpace gigaSpace;

    private int currentId = 0;
    private int idLimit = -1;

    public IdGeneratorImpl(){}

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public synchronized Integer generateId() {
        if (currentId < 0 || currentId > idLimit) {
            getNextIdBatchFromSpace();
        }
        return currentId++;
    }

    private void getNextIdBatchFromSpace() {
        IdCounterEntry idCounterEntry = gigaSpace.readById(IdCounterEntry.class,0,0, 5000, ReadModifiers.EXCLUSIVE_READ_LOCK);
        if (idCounterEntry == null) {
            throw new RuntimeException("Could not get ID object from Space");
        }
        int[] range = idCounterEntry.getIdRange();
        currentId = range[0];
        idLimit = range[1];
        gigaSpace.write(idCounterEntry, Lease.FOREVER, 5000, UpdateModifiers.UPDATE_ONLY);
    }
}
...
```

# What's Next?

Step Three - [Building and Running the Application](./step-3-building-and-running-the-application.html) - Shows how to build, package and deploy the application while taking advantage of XAP's dynamic load balancing capabilities and the Space as a highly `HttpSession` store.
