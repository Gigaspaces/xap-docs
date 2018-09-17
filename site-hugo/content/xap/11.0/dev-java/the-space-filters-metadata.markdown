---
type: post110
title:  Interceptors
categories: XAP110
parent: the-space-filters.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}

The Space Filter implementation may use the following interceptors. The relevant method should be annotated with the annotation listed below. The signature of these methods may include the following parameters:


| Parameter |Description|Type|
|:----------|:----------|:---|
|First Parameter |Space Template for before read or before take / Space object for after read/after take/write/update|Object|
|Second Parameter |Operation Type|int|
|Third Parameter |Security Context. Passed with a secured space.|[SpaceContext]({{% api-javadoc %}}/index.html?com/j_spaces/core/SpaceContext.html) |

Annotation List:


| Filter Annotation| Description |Operation Type|
|:-----------------|:------------|:-------------|
|AfterAllNotifyTrigger|Filter callback after all notify trigger.|18|
|AfterNotifyTrigger|Filter callback after notify trigger.|16|
|AfterReadMultiple|Filter callback after read multiple operation. Called for each matching object.|12|
|AfterRemoveByLease|Filter callback after an entry was removed due to lease expression or lease cancel.|53|
|AfterTakeMultiple|Filter callback after take multiple operation. Called for each matching object.|14|
|AfterUpdate|Filter callback after update operation.|10|
|AfterWrite|Filter callback after write operation.|1|
|BeforeAllNotifyTrigger|Filter callback before all notify trigger.|17|
|BeforeAuthentication|Filter callback before authentication.|6|
|BeforeCleanSpace|Filter callback after clean space operation.|8|
|BeforeExecute|Filter callback before execute operation.|20|
|AfterExecute|Filter callback after execute operation.|23|
|BeforeNotify|Filter callback before notify operation.|4|
|BeforeNotifyTrigger|Filter callback before notify trigger.|15|
|BeforeRead|Filter callback before read operation.|2|
|AfterRead|Filter callback after read operation.|21|
|BeforeReadMultiple|Filter callback before read multiple operation.|11|
|BeforeRemoveByLease|Filter callback before an entry was removed due to lease expression or lease cancel.|52|
|BeforeTake|Filter callback before take operation.|3|
|AfterTake|Filter callback after take operation.|22|
|BeforeTakeMultiple|Filter callback before take multiple operation.|13|
|BeforeUpdate|Filter callback before update operation.|9|
|BeforeWrite|Filter callback before write operation.|0|
|BeforeChange|Filter callback before change operation.|24|
|AfterChange|Filter callback after change operation. Called for each matching object.|25|
|OnFilterClose|A callback method when the filter is closed.| |
|OnFilterInit|A callback method when the filter is initialized.|51|

The space filter business logic impacts the space responsiveness to client requests - please make sure your filter implementation will not involve heavy business logic. With concurrent access into the space, all clients share the **same filter instance** object where the space engine access the same filter object on behalf of each client. Every non-thread safe attributes you use as part of the filter implementation will be thread-safe protected.

# Example

The following example illustrates a space Filter POJO and a Bean that performs some space operations:

{{%tabs%}}
{{%tab "  The Space Filter POJO "%}}


```java
package com.test;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceLateContext;
import org.openspaces.core.space.filter.AfterAllNotifyTrigger;
import org.openspaces.core.space.filter.AfterNotifyTrigger;
import org.openspaces.core.space.filter.AfterReadMultiple;
import org.openspaces.core.space.filter.AfterRemoveByLease;
import org.openspaces.core.space.filter.AfterTakeMultiple;
import org.openspaces.core.space.filter.AfterWrite;
import org.openspaces.core.space.filter.BeforeAllNotifyTrigger;
import org.openspaces.core.space.filter.BeforeExecute;
import org.openspaces.core.space.filter.BeforeNotify;
import org.openspaces.core.space.filter.BeforeNotifyTrigger;
import org.openspaces.core.space.filter.BeforeRead;
import org.openspaces.core.space.filter.BeforeReadMultiple;
import org.openspaces.core.space.filter.BeforeTake;
import org.openspaces.core.space.filter.BeforeTakeMultiple;
import org.openspaces.core.space.filter.BeforeWrite;
import org.openspaces.core.space.filter.OnFilterClose;
import org.openspaces.core.space.filter.OnFilterInit;
import org.springframework.beans.factory.InitializingBean;

public class MySpaceFilter implements InitializingBean{

    //late context is needed since there's a circular dependency between the filter bean and the space itself
	@GigaSpaceLateContext
	GigaSpace space;

    @OnFilterInit
    void init() {
    	System.out.println("MySpaceFilter Space Filter initialized");
    }

    @OnFilterClose
    void close() {
    	System.out.println("Closing Space Filter");
    }

    @BeforeWrite
    public void beforeWrite(Object spaceObject) {
    	System.out.println("beforeWrite called" + spaceObject);
    }

    @AfterWrite
    public void afterWrite(Object echo) {
    	System.out.println("afterWrite called" + echo);
    }

    @BeforeRead
    public void beforeRead(Object spaceTemplate) {
    	System.out.println("beforeRead called" + spaceTemplate);
    }

    @AfterRemoveByLease
    public void afterRemoveByLease(Object spaceObject) {
    	System.out.println("afterRemoveByLease called" + spaceObject);
    }

    @BeforeTake
    public void beforeTake(Object spaceTemplate) {
    	System.out.println("beforeTake called " + spaceTemplate);
    }

    @BeforeReadMultiple
    public void beforeReadMultiple(Object spaceTemplate) {
    	System.out.println("beforeReadMultiple called - Template:" + spaceTemplate);
    }

    @AfterReadMultiple
    public void afterReadMultiple(Object spaceObject) {
    	System.out.println("afterReadMultiple called - Space Object:" + spaceObject);
    }

    @BeforeTakeMultiple
    public void beforeTakeMultiple(Object spaceTemplate) {
    	System.out.println("beforeTakeMultiple called - Template:" + spaceTemplate);
    }

    @AfterTakeMultiple
    public void afterTakeMultiple(Object spaceObject) {
    	System.out.println("afterTakeMultiple called - Space Object:" + spaceObject);
    }

    @BeforeExecute
    public void beforeExecute(Object task) {
    	System.out.println("beforeExecute called " + task);
    }

    @BeforeNotify
    public void beforeNotify(Object spaceObject) {
    	System.out.println("beforeNotify called " + spaceObject);
    }

    @AfterAllNotifyTrigger
    public void afterAllNotifyTrigger(Object spaceObject) {
    	System.out.println("afterAllNotifyTrigger called " + spaceObject);
    }

    @BeforeAllNotifyTrigger
    public void beforeAllNotifyTrigger(Object spaceObject) {
    	System.out.println("beforeAllNotifyTrigger called " + spaceObject);
    }

    @AfterNotifyTrigger
    public void AfterNotifyTrigger(Object spaceTemplate) {
    	System.out.println("afterNotifyTrigger called " + spaceTemplate);
    }

    @BeforeNotifyTrigger
    public void beforeNotifyTrigger(Object spaceTemplate) {
    	System.out.println("beforeNotifyTrigger called " + spaceTemplate);
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("space "+space);
	}
}
```

{{% /tab %}}
{{%tab "  The Bean "%}}


```java
package com.test;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;
import org.springframework.beans.factory.InitializingBean;
import com.j_spaces.core.LeaseContext;

public class MyBean implements InitializingBean{
	@GigaSpaceContext
	GigaSpace space;

	@Override
	public void afterPropertiesSet() throws Exception {
		Message m = new Message();
		m.setId("1");
		m.setData("AAAA");

		System.out.println("Calling write");
		LeaseContext<Message> lc = space.write(m);
		line();
		System.out.println("Calling read");
		Message m1 = space.read(new Message());
		line();
		System.out.println("Calling lease cancel");
		lc.cancel();
		line();
		System.out.println("Calling write");
		space.write(m);
		line();
		System.out.println("Calling readMultiple");
		Message m1Array[] =space.readMultiple(new Message(), Integer.MAX_VALUE);
		line();
		System.out.println("Calling take");
		Message m2 = space.take(new Message());
		line();
		System.out.println("Calling write");
		space.write(m);
		line();
		System.out.println("Calling takeMultiple");
		Message m2Array[] =space.takeMultiple(new Message(), Integer.MAX_VALUE);
		line();
		System.out.println("Calling execute");
		space.execute(new MyTask());

		Thread.sleep(1000);

		line();
		System.out.println("Calling Notify Registration");
		SimpleNotifyEventListenerContainer notifyEventListenerContainer =
			new SimpleNotifyContainerConfigurer(space)
	        .template(new Message())
	        .notifyWrite(true)
	        .eventListenerAnnotation(new Object() {
	            @SpaceDataEvent
	            public void eventHappened() {
	                System.out.println("SpaceDataEvent event called");
	            }
	        }).notifyContainer();

		notifyEventListenerContainer.start();
		Thread.sleep(1000);
		line();
		System.out.println("Calling write");
		space.write(m);
		Thread.sleep(2000);

	}

	static void line()
	{
		System.out.println("-------------------------");
	}

}
```

{{% /tab %}}
{{%tab "  The Beans Definitions "%}}


```xml
<bean id="mySpaceFilter" class="com.test.MySpaceFilter" />
<bean id="myBean" class="com.test.MyBean" />

<os-core:embedded-space id="space" name="space" >
	<os-core:annotation-adapter-filter priority="2">
		<os-core:filter ref="mySpaceFilter" />
	</os-core:annotation-adapter-filter>
</os-core:embedded-space>

<os-core:giga-space id="gigaSpace" space="space"/>
<os-core:giga-space-context/>
<os-core:giga-space-late-context />
```

{{% /tab %}}
{{%tab "  The Space Class "%}}


```java
package com.test;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class Message {
	String id;
	String data;

	public Message (){}

	@SpaceId
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String toString() {
		return " Class Message - Object ID:" + id + " data:" + data;
	}
}
```

{{% /tab %}}
{{%tab "  The Space Task "%}}


```java
package com.test;

import java.io.Serializable;
import org.openspaces.core.executor.Task;

public class MyTask implements Task<Serializable>{

	@Override
	public Serializable execute() throws Exception {
		return null;
	}
}
```

{{% /tab %}}
{{% /tabs %}}


{{%accordion%}}
{{%accord title=" Here is how the expected output should look like:"%}}


```java

MySpaceFilter Space Filter initialized
INFO [com.gigaspaces.core.common] - Space [space_container:space] with url
 [/./space?schema=default&groups=gigaspaces-7.1.0-XAPPremium-rc&state=started] started successfully
space space_container:space
Calling write
beforeWrite called Class Message - Object ID:1 data:AAAA
afterWrite called Class Message - Object ID:1 data:AAAA
-------------------------
Calling read
beforeRead called Class Message - Object ID:null data:null
-------------------------
Calling lease cancel
afterRemoveByLease called Class Message - Object ID:1 data:AAAA
-------------------------
Calling write
beforeWrite called Class Message - Object ID:1 data:AAAA
afterWrite called Class Message - Object ID:1 data:AAAA
-------------------------
Calling readMultiple
beforeReadMultiple called - Template: Class Message - Object ID:null data:null
afterReadMultiple called - Space Object: Class Message - Object ID:1 data:AAAA
-------------------------
Calling take
beforeTake called Class Message - Object ID:null data:null
afterRemoveByLease called Class Message - Object ID:1 data:AAAA
-------------------------
Calling write
beforeWrite called Class Message - Object ID:1 data:AAAA
afterWrite called Class Message - Object ID:1 data:AAAA
-------------------------
Calling takeMultiple
beforeTakeMultiple called Template: Class Message - Object ID:null data:null
afterRemoveByLease called Class Message - Object ID:1 data:AAAA
afterTakeMultiple called Space Object:Class Message - Object ID:1 data:AAAA
-------------------------
Calling execute
beforeExecute called com.test.MyTask@9e4585
-------------------------
Calling Notify Registration
beforeNotify called  Class Message - Object ID:null data:null
-------------------------
Calling write
beforeWrite called Class Message - Object ID:1 data:AAAA
afterWrite called Class Message - Object ID:1 data:AAAA
beforeAllNotifyTrigger called  Class Message - Object ID:1 data:AAAA
beforeNotifyTrigger called  Class Message - Object ID:1 data:AAAA
SpaceDataEvent event called
afterNotifyTrigger called  Class Message - Object ID:1 data:AAAA
afterAllNotifyTrigger called  Class Message - Object ID:1 data:AAAA
```

{{%/accord%}}
{{%/accordion%}}


# Advanced Options

A filter is an instance of a class that implements the `ISpaceFilter` interface (`com.j_spaces.core.filters.ISpaceFilter`; see [Javadoc]({{% api-javadoc %}}/com/j_spaces/core/filters/ISpaceFilter.html)).

{{% tip %}}
When performing operations using transactions (write, update, or take with a transaction object), the space filter callbacks are called when the operation is called, and not when the transaction commit/rollback is called.
{{% /tip %}}

## ISpaceFilter Interface


| Return value | method |
|:-------------|:-------|
| `void` | `close()`{{<wbr>}}when the space is closed gracefully, all active filters close method is called to allow cleaning any open resources. |
| `void` | `init` ([IJSpace]({{% api-javadoc %}}/index.html?com/j_spaces/core/IJSpace.html) `space`, [String](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/lang/String.html) `filterId`, [String](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/lang/String.html) `url, int priority`){{<wbr>}}When a space initializes, it instantiates all the filters defined as part of its schema file and calls the `init` method on each one. The `init` method provides an opportunity for the filter to initialize itself. The arguments of the `init` method are:{{<wbr>}}- `space` -- a reference to a collocated space proxy. This reference can be used later to operate on the space, although this should be done with extreme care.<br>- `filterId` -- The name of this filter {{<wbr>}}- `url` -- The free string defined as part of the filter definition at the space schema file. This allows passing any data into the filter from external source.{{<wbr>}}`priority` -- a number between 0 and 4. Filters with higher priorities will be executed first, i.e., the filters will be executed in ascending order of priority. For example, a `BEFORE_WRITE` filter with priority 4 will be executed before an `AFTER_WRITE` filter with priority 3. If a `RuntimeException` is thrown from the into method during filter initialization, it is discarded for the lifetime of the current engine run. |
| `void` | process ([SpaceContext]({{% api-javadoc %}}/index.html?com/j_spaces/core/SpaceContext.html) context, [ISpaceFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/entry/ISpaceFilterEntry.html) Entry, `int operationCode`){{<wbr>}}This method is called by the engine when an event matching this filter's operation code occurs in the engine. When an operation inside the space triggers a filter, the filter's process method is called with the following arguments:{{<wbr>}}- `context` is passed as the first parameter to process. It may be null for a non-secured space. `SpaceContext` has a `SecurityContext` object as one of its public fields. The proxy passes the `SpaceContext` object to a space method in each call, (except for replication and recovery) . For operation codes `ON_INIT`, `BEFORE_REMOVE` and `AFTER_REMOVE`, the `SpaceContext` is always `null`.{{<wbr>}}- `entry` -- a representation of the Entry that caused the space event is passed to the process method as an argument. The Entry is the object or the template object used at the relevant space operation, represented by the{{<wbr>}}[ISpaceFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/entry/ISpaceFilterEntry.html). This argument may be `null` (e.g., in `ON_INIT` filters). The `ISpaceFilterEntry` contains Entry class name, field types and values. It is important to note that if a `RuntimeException` occurs in `BEFORE` operations, the space engine aborts the operation. For example, if a `BEFORE_WRITE` filter throws a `RuntimeException`, the Entry will not be inserted to the space, and the client will receive this exception. Therefore, the filter developer is responsible for catching any exceptions that will not abort the operation.{{<wbr>}}- `operationCode` -- any one of the{{<wbr>}}[FilterOperationCodes]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/FilterOperationCodes.html){{<wbr>}}or [FilterOperationCodes]({{% api-javadoc %}}/constant-values.html#com.j_spaces.core.filters.FilterOperationCodes.AFTER_ALL_NOTIFY_TRIGGER) that triggers the process method. The optional values are the ones defined as part of the filter definition as part of the space schema file.{{<wbr>}}`BEFORE_WRITE/AFTER_WRITE` and `BEFORE_UPDATE/AFTER_UPDATE` codes are used when calling the `writeMultiple()/updateMultiple()` respectively. The `readMultiple` and `takeMultiple` have their own set of codes.{{<wbr>}}`FilterOperationCodes` with the prefix `AFTER` (which indicate operations to be performed after a certain space operation) do work for space operations that use timeouts - if the space operation was successfully performed.{{<wbr>}}The `ON_INIT`, `BEFORE_REMOVE` and `AFTER_REMOVE` operation codes can be used only in non-security filters, i.e., they receive a `null` context. For example, a filter with operation code `BEFORE_WRITE` will be called (its process method will be called) on every Entry that enters the space immediately before it is inserted into the space engine. A filter can be defined to handle several operation codes, for example `<operation-code>1,3<operation-code>`, if the filter definition activates it in `AFTER_WRITE (1) and BEFORE_TAKE (3)` trigger-points. |
| `void` | process ([SpaceContext]({{% api-javadoc %}}/index.html?com/j_spaces/core/SpaceContext.html) context, [ISpaceFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/entry/ISpaceFilterEntry.html)\[\] entries, `int operationCode`).{{<wbr>}}This method is called when the `IJSpace.replace()` is called for the `BEFORE_UPDATE` and `AFTER_UPDATE`. The passed Entries array size is 2 and includes the template Entry at the first position at the array and the new entry at the second position at the array. This method is also called for the `AFTER_UPDATE` event of the `IJSpace.update()`. The passed Entries array size is 2 and includes the original Entry at the first position at the array and the updated Entry at the second position at the array. |

## FilterOperationCodes

The [FilterOperationCodes]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/FilterOperationCodes.html) class includes the all the filter code you may use.

Use the `init()` method to initialize the filter. This method is called before the space fully starts. The `init()` method guarantees that no one else can access the space before `init()` is finished.

`FilterOperationCodes` with the prefix `AFTER` (which indicate operations to be performed after a certain space operation) are not called for space operations that use timeouts. For example, if you use the code `AFTER_READ`, and a specific read operation is performed with a timeout, the operation that was supposed to happen after the read will not be executed.

The `BEFORE_NOTIFY_TRIGGER`, `AFTER_NOTIFY_TRIGGER`, `BEFORE_ALL_NOTIFY_TRIGGER`, and `AFTER_ALL_NOTIFY_TRIGGER` `FilterOperationCodes` events provide:

- Notification statistics infrastructure
- Durable notifications infrastructure
- Notification recovery infrastructure

- `BEFORE_NOTIFY_TRIGGER` -- before a notify trigger operation, indicates that a matched notify template was found for the current Entry event.
- `AFTER_NOTIFY_TRIGGER` -- after a notify trigger operation, indicates that a notify trigger was successful.
- `BEFORE_ALL_NOTIFY_TRIGGER` -- before all notify trigger operations, indicates that a notify trigger was successful.
- `AFTER_ALL_NOTIFY_TRIGGER` -- after all notify trigger operations, indicates that all notify templates that are matched to the current Entry event were triggered, and returned or failed.

## Space Filter invocation with backup/replica Space

The space filter context is `null` for a filter called as a result of a replication event. For secured space, you can use this as indication to know if this operation originated by a client call or replication event.

## ISpaceFilterEntry Interface

The [ISpaceFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/entry/ISpaceFilterEntry.html) represents an Entry instance passed to the `ISpaceFilter` process method implementation. The `ISpaceFilterEntry` class extends the [IFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/filters/entry/IFilterEntry.html) that extends the [IGSEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/IGSEntry.html) which includes methods that allow you to access the Entry values, class name and other Entry object properties. The `ISpaceFilterEntry` class does not include the `Entry Class` methods. To convert the `ISpaceFilterEntry` class to your Entry object, you should call the `ISpaceFilterEntry.getEntry(IJSpace space)` method or the `ISpaceFilterEntry.getExternalEntry(IJSpace space)` method.

## Modifying Written Space Object or Template

You may modify the written Entry field values before the Entry is stored in the space, using the `BEFORE_WRITE` event, or you can modify the template field values used when performing a search for a matching Entry, using the `BEFORE_READ` event. This allows you to truncate an unnecessary Entry field value, or replace it with some enriched data.

When modifying the template field values, you can construct a different search than the original one constructed by the client. For example, you can aggregate data from multiple Entries as part of a `BEFORE_READ` event, write the aggregated data into the space, and modify the template to return the aggregated data. This will return the aggregated Entry data to the client, and not the Entry that matches the original template constructed by the client. To modify the written Entry or the template, you should use the `IFilterEntry.setFieldValue` method.
