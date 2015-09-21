---
type: post110
title:  Operations
categories: XAP110
weight: 300
parent: the-gigaspace-interface-overview.html
---


{{% ssummary %}}{{%/ssummary%}}


XAP provides a simple space API using the [GigaSpace](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/core/GigaSpace.html) interface for interacting with the space.


The interface includes the following main operations:

{{%section%}}
{{%column width="50%" %}}
{{%panel bgColor=white | title="Write objects into the Space:"%}}
[write](#write) one object into the space{{%wbr%}}
[writeMultiple](#writeMultiple) objects into the Space{{%wbr%}}
[asynchronous write](#asynchronousWrite) to the Space
{{%/panel%}}
{{%/column%}}
{{%column width="50%" %}}
{{%panel bgColor=white | title="Change objects in Space:"%}}
[change](#change) one object in Space{{%wbr%}}
		  [changeMultiple](./change-api.html) objects in Space {{%wbr%}}
[asynchronous change](./change-api.html) of objects
{{%/panel%}}
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="50%" %}}
{{%panel bgColor=white |  title="Reading objects from the Space:"%}}
[readById](#read) from the Space{{%wbr%}}
[readByIds](#readMultiple) from the Space{{%wbr%}}
[read](#read) object by template from the Space{{%wbr%}}
[readMultiple](#readMultiple) objects from the Space {{%wbr%}}
[read asynchronous](#asynchronousRead) from the Space {{%wbr%}}
[read if exists](#readIfExists) {{%wbr%}}
[read if exists by id](#readIfExists)
{{%/panel%}}
{{%/column%}}
{{%column width="50%" %}}
{{%panel bgColor=white |  title="Removing objects from the Space:"%}}
[take](#take) object by template from Space{{%wbr%}}
[takeById](#take) object by id from Space{{%wbr%}}
[takeByIds](#takeMultiple) objects by ids from Space{{%wbr%}}
[takeMultiple](#takeMultiple) objects from Space {{%wbr%}}
[take asynchronous](#asynchronousTake){{%wbr%}}
[take if exists](#takeIfExists){{%wbr%}}
[clear](#clear) objects in Space
{{%/panel%}}
{{%/column%}}
{{%/section%}}

{{%section%}}
{{%column width="50%" %}}
{{%panel bgColor=white |  title="Other operations:"%}}
[aggregation](#aggregators)  across the Space{{%wbr%}}
[count](#count) objects in Space{{%wbr%}}
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




{{% include "/COM/xap102/ops-write.markdown" %}}
{{% include "/COM/xap102/ops-change.markdown" %}}
{{% include "/COM/xap102/ops-read.markdown" %}}
{{% include "/COM/xap102/ops-take.markdown" %}}
{{% include "/COM/xap102/ops-clear.markdown" %}}
{{% include "/COM/xap102/ops-count.markdown" %}}
{{% include "/COM/xap102/ops-counters.markdown" %}}
{{% include "/COM/xap102/ops-aggregation.markdown" %}}
{{% include "/COM/xap102/ops-async-extension.markdown" %}}
