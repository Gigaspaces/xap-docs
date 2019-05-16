---
type: post97
title:  Transactions
categories: XAP97
weight: 500
canonical: auto
parent: the-gigaspace-interface-overview.html
---



`GigaSpace` with the different OpenSpaces [transaction managers](./transaction-management.html) and Spring allow simple declarative definition of transactions. This boils down to the fact that if there is an ongoing transaction running, any operation performed using the `GigaSpace` interface joins it, using Spring's rich transaction support.

{{% note %}}
In order to have GigaSpace transactional, the transaction manager must be provided as a reference when constructing the GigaSpace bean.
For example (using the distributed transaction manager):
{{%/note%}}

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

<os-core:distributed-tx-manager id="transactionManager"/>

<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>

<bean id="transactionManager" class="org.openspaces.core.transaction.manager.DistributedJiniTransactionManager">
	<property name="space" ref="space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
    <property name="space" ref="space" />
	<property name="transactionManager" ref="transactionManager" />
</bean>
```

{{% /tab %}}
{{% /tabs %}}

{{% note %}}
It is highly recommended to read the [transaction management chapter](http://static.springframework.org/spring/docs/3.0.x/reference/transaction.html) in the Spring reference documentation.
{{%/note%}}

#### Transaction Provider

OpenSpaces provides a pluggable transaction provider using the following interface:


```java
public interface TransactionProvider {
    Transaction getCurrentTransaction(Object transactionalContext, IJSpace space);
    int getCurrentTransactionIsolationLevel(Object transactionalContext);
}
```

OpenSpaces comes with a default transaction provider implementation, which uses Spring and its transaction manager in order to obtain the currently running transactions and automatically use them under transactional operations.

`GigaSpace` allows access to the current running transaction using the transaction provider. The following code example shows how the take operation can be performed using `IJspace` (users normally won't be required to do so):


```java
gigaSpace.getSpace().take(obj, gigaSpace.getCurrentTransaction(), 1000);
```

# Isolation Level

GigaSpaces supports three isolation levels: `READ_UNCOMMITTED`, `READ_COMMITTED` and `REPEATABLE_READ` (default). When using `GigaSpace`, the default isolation level that it will perform under can be defined in the following manner:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:space id="space" url="/./space" />

<os-core:giga-space id="gigaSpace" space="space" default-isolation="READ_COMMITTED"/>
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.UrlSpaceFactoryBean">
    <property name="url" value="/./space" />
</bean>

<bean id="gigaSpace" class="org.openspaces.core.GigaSpaceFactoryBean">
	<property name="space" ref="space" />
    <property name="defaultIsolationLevelName" value="READ_COMMITTED" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

IJSpace space = // get Space either by injection or code creation

GigaSpace gigaSpace = new GigaSpaceConfigurer(space)
                          .defaultIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED)
                          .gigaSpace();
```

{{% /tab %}}
{{% /tabs %}}

In addition, Spring allows you to define the isolation level on the transaction definition itself:


```java
@Transactional(readOnly = true)
public class DefaultFooService implements FooService {

    private GigaSpace gigaSpace;

    public void setGigaSpace(GigaSpace gigaSpace) {
    	this.gigaSpace = gigaSpace;
    }

    public Foo getFoo(String fooName) {
        // do something
    }

    // these settings have precedence for this method
    @Transactional(readOnly = false,
                   propagation = Propagation.REQUIRES_NEW,
                   isolation  = Isolation.READ_COMMITTED)
    public void updateFoo(Foo foo) {
        // do something
    }
}
```

In the above example, any operation performed using `GigaSpace` in the `updateFoo` method automatically works under the `READ_COMMITTED` isolation level.

{{%learn "./transaction-management.html"%}}
