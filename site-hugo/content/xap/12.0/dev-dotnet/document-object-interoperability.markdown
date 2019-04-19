---
type: post120
title:  Interoperability
categories: XAP120NET, PRM
weight: 300
canonical: auto
parent: document-overview.html
---

{{% ssummary %}} {{% /ssummary %}}


XAP offers interoperability between documents and concrete objects via space - it is possible to write objects and read them as documents, and vice versa. This is usually useful in scenarios requiring reading and/or manipulating objects without loading the concrete .NET classes. This page describes how to do it.

# Requirements

When working with documents the user is in charge of creating and registering the space type descriptor manually before writing/reading documents. When working with concrete objects the system implicitly generates a space type descriptor for the object's class using attributes or `gs.xml` files when the class is used for the first time. In order to inter-operate, the same type descriptor should be used for both concrete objects and documents.

If the object's class is in the application's AppDomain, or the object type is already registered in the space, there's no need to register it again - the application will retrieve it automatically when it's used for the first time. For example:


```java
// Create a document template using the POJO class name:
SpaceDocument template = new SpaceDocument(typeof(MyObject).FullName);
// Count all entries matching the template:
int count = spaceProxy.Count(template);
```

If the object's class is not available in the classpath or server, the application will throw an exception indicating there's no type descriptor registered for the specified type. In that case, it is possible to manually create a matching type descriptor using the `SpaceTypeDescriptorBuilder` and register it in the space. However, that's not recommended since it essentially requires you to duplicate all the concrete object settings and maintain them if the object class changes.

# Query Result Type

When no interoperability is involved this is a trivial matter - Querying an object type returns objects, querying a document type returns documents.
When we want to mix and match, we need semantics to determine to query result type - Object or Document.

## Template Query

Template query result types are determined by the template class - if the template is an instance of a `SpaceDocumnet` the result(s) will be document(s), otherwise it will be object(s).

## Sql Query

The `SqlQuery` class has a `QueryResultType` settings which can be set at construction. The following options are available:

- `Object` - Return .NET Object(s).
- `Document` - Return space document(s).


## ID Based Query

In order to support ID queries for documents, use the `IdQuery` class, which encapsulates the type, id, routing and a `QueryResultType` with the corresponding `ISpaceProxy` overload methods: `ReadById`, `ReadIfExistsById`, `TakeById`, `TakeIfExistsById`. The result type is determined by the `QueryResultType`, similar to `SqlQuery`.

Respectively, to support multiple ids queries, use the `IdsQuery` with the corresponding `ReadByIds` and `TakeByIds`.

# Dynamic Properties

By default, type descriptors created from concrete object classes do not support dynamic properties. If a document of such a type with a property that is not defined in the object will be written to the space, an exception will be thrown indicating the property is not defined in the type and the type does not support dynamic properties.
In order to have a concrete class support dynamic properties it should have a property decorated with the \[SpaceDynamicProperties\] and the type of that property must be either DocumentProperties, Dictionary<String, Object> or IDictionary<String, Object>.


```java
[SpaceClass]
public class MyObject
{

  private Dictionary<String, Object> _dynamicProperties

  [SpaceDynamicProperties]
  public Dictionary<String, Object> DynamicProperties
  {
     get{ return _dynamicProperties; }
     set{ _dynamicProperties = value; }
  }

}
```

The storage type of the dynamic properties can be explicitly set in the attribute \[SpaceDynamicProperties(StorageType=StorageType.Binary)\] (the default is StorageType.Object).

{{% refer %}}For more details about storage type refer to [Property Storage Type](./poco-storage-type.html){{% /refer %}}
