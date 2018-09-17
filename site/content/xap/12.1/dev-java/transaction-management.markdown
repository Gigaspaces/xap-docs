---
type: post121
title:  Transaction Management
categories: XAP121
parent: transaction-overview.html
weight: 100
---

{{%imagertext "/attachment_files/tx_manager.jpg" %}}

The Spring Framework provides a transaction manager abstraction using the `PlatformTransactionManager` interface with several different built-in implementations, such as JDBC Data Source and JTA. XAP provides several implementations for Spring's `PlatformTransactionManager`, allowing you to use the XAP's local and Jini Distributed Transaction Managers.
By implementing Spring's `PlatformTransactionManager`, the XAP API allows users to utilize Spring's rich support for {{%exurl "declarative transaction management""http://static.springframework.org/spring/docs/2.5.x/reference/transaction.html#transaction-declarative"%}}. The declarative transaction support can be easily utilized with the [GigaSpace Interface](./the-gigaspace-interface.html).

Please note that when using Spring declarative transaction, a proxy is generated for the classes annotated with `@Transactional` methods. In such a case **only external method calls** coming in through the proxy will be intercepted. This means that 'self-invocation', i.e. a method within the target object calling some other method of the target object, won't lead to an actual transaction at runtime even if the invoked method is marked with `@Transactional`.


{{%/imagertext%}}
{{% note%}}
In order to make [The GigaSpace Interface ](./the-gigaspace-interface.html) transactional, the transaction manager must be provided to it when constructing the GigaSpace bean.
The following should be added to your `pu.xml` to enable the configuration of transactional behavior based on annotations:
{{%/note%}}


```xml
<beans ....
       xmlns:tx="http://www.springframework.org/schema/tx"
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version "spring"%}}.xsd">

       <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

XAP provides several transaction managers, and changing the implementation you work with is just a matter of changing the the configuration.

## Transaction Manager Types

The section below lists the different types of transaction managers supported by XAP. Each transaction manager implements Spring's `PlatformTransactionManager` interface and therefore supports the Spring transaction framework (see below).

# Constructing XAP Transaction Manager

The distributed {{%exurl "Jini Transaction Manager""https://river.apache.org/release-doc/current/specs/html/txn-spec.html"%}} starts an embedded distributed (Mahalo) **Jini Transaction Manager**, which is then wrapped with an implementation of the Spring `PlatformTransactionManager`. This transaction manager is used in order to perform distributed transactions spanning multiple space instances.

Below is an example of how it can be defined in a Spring application context:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:distributed-tx-manager id="transactionManager" />
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager" />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space"  />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager" />

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
    <property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("mySpace");
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).transactionManager(ptm).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

## Timeout Values

The Jini distributed (mahalo) transaction manager allows to set the default timeout value for transactions. A timeout value is used when a transaction is not committed/rolled back (for example due to JVM crash) to control when the transaction will be discarded. By default the timeout value is 90 and is set in seconds.

For example, to change the default timeout to 2 minutes, use the following configuration:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:distributed-tx-manager id="transactionManager" default-timeout="120"/>
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property  name="name" value="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager">
	<property name="defaultTimeout" value="120" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
	<property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{%tab "Code"%}}


```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("mySpace");
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().defaultTimeout(120).transactionManager();
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).transactionManager(ptm).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

{{% note "Time based Parameters Units" %}}
- The **default-timeout** parameter is specified in seconds
- Other parameters such as the commit and abort timeout, lookup-timeout, and others are specified in millisecond

When using Spring declarative transaction management, a transaction timeout can be set on the transaction scope. For more details, see [below](#spring-transactiondefinition-mapping-to-xap-readmodifiers).
{{%/note%}}

When using Jini based transactions, a timeout value can be set for both the commit and abort operations. This values can also be set on the transaction manager.

# Connecting to XAP Transaction Manager

The Jini Transaction Manager Lookup allows you to use the Jini lookup mechanism in order to lookup a Jini Transaction Manager that is present somewhere in the cluster (as opposed to being started locally in your application), which is then wrapped with an implementation of the Spring `PlatformTransactionManager`. This transaction manager is usually used in order to obtain a remote Jini Mahalo transaction manager for distributed transactions spanning multiple space instances.

Below is an example of how it can be defined in a Spring application context:

{{%tabs%}}
{{%tab "Namespace"%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:jini-tx-manager id="transactionManager" lookup-timeout="5000" />
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager" />
```

{{% /tab %}}
{{%tab "Plain XML"%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.LookupJiniTransactionManager">
	<property name="lookupTimeout" value="5000" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
    <property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{%tab "Code"%}}


```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("mySpace");
PlatformTransactionManager ptm = new LookupJiniTxManagerConfigurer().lookupTimeout(5000).transactionManager();
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).transactionManager(ptm).gigaSpace();
```

{{% /tab %}}

{{% /tabs %}}

 

{{% note "Spring declarative transaction management"%}}
When using Spring declarative transaction management, a transaction timeout can be set on the transaction scope. For more details, see [below](#spring-transactiondefinition-mapping-to-gigaspaces-readmodifiers).
{{%/note%}}

When using Jini based transactions, a timeout value can be set for both the commit and abort operations. This values can also be set on the transaction manager.

# Local Jini Transaction Manager - Deprecated

{{% warning "Deprecated"%}}
Local Jini Transaction Manager was deprecated in 8.0. Use [Distributed Jini Transaction Manager](#distributed-jini-transaction-manager) instead.
{{% /warning %}}

# Renewing Transactions

Jini transactions allow you to configure automatic renewing of ongoing transactions. This feature is very handy when wanting to configure a long transaction timeout, and have it expire earlier in case of a complete failure (for example, a JVM crash). Expiring the transaction is important so objects held under a transaction lock are released as soon as possible.

Here is an example of how this can be configured:

{{%tabs%}}
{{%tab "Namespace"%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<os-core:distributed-tx-manager id="transactionManager" >
    <os-core:renew pool-size="2" duration="1000" round-trip-time="500" />
</os-core:distributed-tx-manager>
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```

{{% /tab %}}
{{%tab "Plain XML"%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager">
	<property name="leaseRenewalConfig">
	    <bean class="org.openspaces.core.transaction.manager.TransactionLeaseRenewalConfig">
	        <property name="poolSize" value="2" />
	        <proeprty name="renewRTT" value="500" />
	        <property name="renewDuration" value="1000" />
        </bean>
	</property>
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
	<property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{%tab "Code"%}}


```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("mySpace");
TransactionLeaseRenewalConfig config = new TransactionLeaseRenewalConfig();
config.setPoolSize(2);
config.setRenewDuration(1000);
config.setRenewRTT(500);
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().leaseRenewalConfig(config).transactionManager();
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).transactionManager(ptm).gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

The above configuration creates a Distributed Transaction Manager with a pool of 2 transaction (lease) renewal managers (a single manager can handle multiple transactions, more managers allow for better concurrency). Each transaction is renewed every 1 second (1000 milliseconds) with an expected round trip time of 500 milliseconds. This means that a transaction with a timeout of 10 seconds is renewed 10 times (approximately) and if the JVM crashes, the transaction expires within a second (at most).

More information regarding Lease Renewal Manager can be found [here](./leases-automatic-expiration.html#LeaseRenewalManager).

# XA/JTA Support

XAP can be used within an XA transaction using JTA. The OpenSpaces API allows you to work with Spring's `JTATransactionManager` and provides support for declarative transaction management. Here is an example of how OpenSpaces JTA support can be used (using JOTM):

{{%tabs%}}
{{%tab "Namespace"%}}


```xml
<os-core:embedded-space id="space" space-name="mySpace"/>
<bean id="jotm" class="org.springframework.transaction.jta.JotmFactoryBean" />
<bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">
    <property name="userTransaction" ref="jotm" />
</bean>

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager" />
```

{{% /tab %}}
{{%tab "Plain XML"%}}


```xml
<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
</bean>

<bean id="jotm" class="org.springframework.transaction.jta.JotmFactoryBean" />

<bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">
    <property name="userTransaction" ref="jotm" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
    <property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{%tab "Code"%}}


```java
UserTransaction userTransaction = ... //get UserTransaction via JDNI / instantiation
PlatformTransactionManager ptm = new JtaTransactionManager(userTransaction);
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("mySpace");
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).transactionManager(ptm).gig
```

{{% /tab %}}
{{% /tabs %}}

{{%  note%}}
XAP JTA implementation supports both local and distributed transaction managers. That means that you can enlist multiple space partitions as a single XA resource in an XA transaction.
{{%/note%}}

{{% warning %}}
XA transactions should be carefully considered. The overhead of managing a 2PC transaction over two or more resources is often times a performance killer.
{{%/warning%}}

{{% refer %}}
See the [JTA-XA Example](/sbp/jta-xa-example.html) for fully running demonstration how to integrate XAP with an external JMS Server.
{{% /refer %}}

# Multiple Transaction Managers

Starting with Spring 3 you may specify multiple Transaction managers within the same Spring context (pu.xml) and use the `@Transactional` annotation. If you have multiple Transaction managers used, make sure you specify the name of the transaction manager your class/method should use:


```java
<os-core:distributed-tx-manager id=" txManager1" />
<os-core:giga-space id="gigaspace" space="space"  tx-manager="txManager1"/>
<tx:annotation-driven transaction-manager=" txManager1" />

<os-core:distributed-tx-manager id=" txManager2" />
<os-core:giga-space id="gigaspace2" space="space"  tx-manager="txManager2"/>
<tx:annotation-driven transaction-manager=" txManager2" />
```

```java
@Transactional(value="txManager1")
public String getFoo()
{
    return dao.find("foo");
}
```

```java
@Transactional(value="txManager2")
public String getFoo()
{
    return dao.find("foo");
}
```

# How to Demarcate Transactions in Your Code

There are two ways to demarcate the transaction boundaries in your code:

1. Annotate your methods with the Spring `@Transactional` annotation and configure Spring to process the annotation such that every call to the annotated methods will be automatically performed under a transaction. In such case the transaction starts before the method is called and commits when the method call returns. If an exception is thrown from the method the transaction is rolled back automatically. Note that you can control various aspects of the transaction by using the attributes of the `@Transactional` annotation. Please consult the [Javadoc](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/transaction/annotation/Transactional.html) of this class for more details.
1. Programmatically create the required transaction manager (see below) and a `TransactionDefinition` instance, and call the space operations you would to perform under a transaction.

# Declarative Transaction Demarcation

Here is an example how a method should be annotated to support declarative transaction management:


```java
@Transactional (propagation=Propagation.REQUIRED)
public void myTransactionalMethod(Object data) {
  gigaSpace.write(mySpaceObject);
  gigaSpace.take(mytemplate);
  ...
  }
```

To enable the declarative transaction management:

1. In your processing unit XML, your pu.xml should include the `<tx:annotation-driven>` tag. See the below example for enabling the `@Transactional` annotation processing for the pu.xml. The system will search for `@Transactional` annotations on all of the beans in the pu.xml.
1. For the transactional behavior to take effect, the calling code **must be injected with the transactional bean**. In the example below, we have the `transactionalBean` bean which annotates some of its methods with the `@Transactional` annotation. The `callingBean` is injected with it. Under the hood, Spring will wrap the `transactionalBean` with a transactional proxy that will initiate a transaction before the method is called and commit/rollback it according to the invocation result.
1. The `@Transactional` annotation is only going to be applied for proxied beans, i.e. beans that have been processed by the framework and injected to other beans which use them.
1. If you try to call an annotated method from within the same class for example (e.g. calling it on `this`), no transaction will be started since your code actually accesses direct reference and not a proxied bean.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version "spring"%}}.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
       http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version "spring"%}}.xsd
       http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd">

	<os-core:embedded-space id="space" space-name="mySpace"/>

	<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

	<!-- Defines a distributed Jini transaction manager. -->
	<os-core:distributed-tx-manager id="transactionManager"/>
        <bean id="transactionalBean" class="MyClass"/>
        <bean id="callingBean" class="MyOtherClass">
            <property name="myClass" ref="transactionalBean"/>
        </bean>
	<tx:annotation-driven transaction-manager="transactionManager" />
</beans>
```

Note that you can also annotate beans exposed via [space based remoting](./space-based-remoting.html). If you include the `<tx:annotation-driven>` element in your `pu.xml` file, it will be processed as any other bean and the remoting mechanism will use the proxied instance, thus making the remote call to the bean transactional.

# Programmatic Transaction Management

If you don't want to leverage {{%exurl "Spring's declarative transaction management""http://static.springframework.org/spring/docs/2.5.x/reference/transaction.html#transaction-declarative"%}}, or have an application that is not configured by Spring, you can start, commit and rollback transactions explicitly from within your code by using Spring's transaction API.

Here is how you should use the Transaction manager via the API:

**1.** Create a Transaction Manager using:

```java
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().transactionManager();
```

or get a reference to an exiting Transaction Manager:


```java
PlatformTransactionManager ptm = new LookupJiniTxManagerConfigurer().lookupTimeout(5000).transactionManager();
```

**2.** Create a `GigaSpace` using the relevant API and have the 'transactionManager' associated with it :

```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).transactionManager(ptm).gigaSpace();
```


Here is a full example where the `GigaSpace` used to execute space operations and rollback/commit using the `DistributedJiniTxManagerConfigurer` created without having spring involved:


```java
//get reference to a GigaSpace instance - with the example below we use a remote proxy
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer("space")).transactionManager(ptm).gigaSpace();

//get a reference to a XAP PlatformTransactionManager instance as described in step one above.
PlatformTransactionManager ptm = new DistributedJiniTxManagerConfigurer().commitTimeout(2000L).defaultTimeout(30).transactionManager();

DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
//configure the transaction definition
definition.setPropagationBehavior(Propagation.REQUIRES_NEW.ordinal());
TransactionStatus status = ptm.getTransaction(definition);
try {
    //do things with the GigaSpace instance...
    gigaSpace.write(myObject);
}
catch (MyException e) {
    ptm.rollback(status);
    throw e;
}
ptm.commit(status);
```

{{%refer%}}
You can also use Spring's {{%exurl "TransactionTemplate""http://static.springframework.org/spring/docs/2.5.x/api/org/springframework/transaction/support/TransactionTemplate.html"%}} if you prefer. This is documented in full in the {{%exurl "Spring reference guide""http://static.springframework.org/spring/docs/2.5.x/reference/transaction.html#transaction-programmatic"%}}.
{{%/refer%}}

{{% note %}}
When using Programmatic Transaction Management you should be expecting to handle the `org.springframework.transaction.TransactionException` that will have the exact reason as an instance of `net.jini.core.transaction.TransactionException`. This has the following subclasses exceptions: CannotAbortException, CannotCommitException, CannotJoinException, CannotNestException, CrashCountException, TimeoutExpiredException,UnknownTransactionException.
{{% /note %}}

# Spring TransactionDefinition Mapping to XAP ReadModifiers

The following table describes the mapping between the {{%exurl "Spring TransactionDefinition""http://static.springsource.org/spring/docs/2.0.x/api/org/springframework/transaction/TransactionDefinition.html"%}} Mapping to XAP ReadModifiers:


|Spring TransactionDefinition| XAP ReadModifiers |
|:---------------------------|:-------------------------|
|ISOLATION_READ_UNCOMMITTED| DIRTY_READ|
|ISOLATION_READ_COMMITTED|READ_COMMITTED|
|ISOLATION_REPEATABLE_READ|REPEATABLE_READ|
|ISOLATION_SERIALIZABLE | Not Supported|


# Viewing Transactions

{{%refer%}}
You can view and inspect ongoing Space transactions with the [ManagementCenter]({{%currentadmurl%}}/gigaspaces-browser-transaction-view.html).
{{%/refer%}}
