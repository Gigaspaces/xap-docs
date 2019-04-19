---
type: post97
title:  Interoperability
categories: XAP97
parent: document-overview.html
weight: 300
canonical: auto
---

{{% ssummary %}}{{% /ssummary %}}


XAP offers interoperability between documents and POJOs via the space - it is possible to write POJOs and read them back as documents, and vice versa. This is usually useful in scenarios requiring reading and/or manipulating POJO objects without loading the concrete java classes.

![document_arch2.jpg](/attachment_files/document_arch2.jpg)

{{% tip %}}
In previous releases the `ExternalEntry` class was used to achieve this functionality. Starting with 8.0, the `SpaceDocument` class should be used to accomplish these needs in a simpler and safer manner, whereas `ExternalEntry` has been deprecated and should no longer be used.
{{%/tip%}}

# Requirements

When working with documents, the user is in charge of creating and registering the space type descriptor manually before interacting with the document types. When working with POJOs, the system implicitly generates a space type descriptor for the POJO's class using annotations or `gs.xml` files when the class is used for the first time. In order to inter-operate, the same type descriptor should be used for both POJOs and documents.

If the POJO's class is in the application's classpath, or the POJO is already registered in the space, there's no need to register it again - the application will retrieve it automatically when it's used for the first time. For example:


```java
// Create a document template using the POJO class name:
SpaceDocument template = new SpaceDocument(MyPojo.class.getName());
// Count all entries matching the template:
int count = gigaSpace.count(template);
```

If the POJO's class is not available in the classpath or in the data grid, the application will throw an exception indicating that there is no type descriptor registered for the specified type. In that case, it is possible to manually create a matching type descriptor using the `SpaceTypeDescriptorBuilder` and register it in the space. However, that's not recommended since it essentially requires you to duplicate all the POJO settings and maintain them if the POJO changes.

# Query Result Type

When no interoperability is involved this is a trivial matter - Querying a POJO type returns POJOs, querying a document type returns documents.

When we want to mix and match, we need semantics to determine to query result type - POJO or document.

## Template Query

Template query result types are determined by the template class - if the template is an instance of a `SpaceDocument`, the results will be documents, otherwise they will be POJOs.

For example:


```java
// Read all product entries as POJOs:
Product[] objects = gigaSpace.readMultiple(new Product(), Integer.MAX_VALUE);
// Read all product entries as Documents:
SpaceDocument[] documents = gigaSpace.readMultiple(
    new SpaceDocument(Product.class.getName()), Integer.MAX_VALUE);
```

#### SQL Query

The `SQLQuery` class has been enhanced with a `QueryResultType` parameter. The following options are available:

- `OBJECT` - Return java Object(s) (POJO).
- `DOCUMENT` - Return space document(s).
- `DEFAULT` - If the type is registered with a concrete java class, return an Object. Otherwise, return a document. This is the default behavior.

For example:


```java
// Read a POJO using an SQL query - same as always:
Product pojo = gigaSpace.read(
    new SQLQuery<Product>(Product.class, "name='Dynamite'"));

// Read a document using an SQLQuery when there's no
// compatible POJO - no need to specify query result type:
SpaceDocument document = gigaSpace.read(
    new SQLQuery<SpaceDocument>("Product", "name='Dynamite'"));

// Read a documnet using an SQLQuery when there is a
// compatible POJO - explicitly specify query result type:
SpaceDocument document = gigaSpace.read(
    new SQLQuery<SpaceDocument>(Product.class.getName(),
        "name='Dynamite'", QueryResultType.DOCUMENT));
```

This strategy both preserves backwards compatibility and simplifies non-interoperability scenarios, which are more common than interoperability scenarios.

#### ID Based Query

In order to support ID queries for documents, the `IdQuery` class has been introduced, which encapsulates the type, id, routing and a `QueryResultType`. New `GigaSpace` signatures have been added for `readById`, `readIfExistsById`, `takeById`, `takeIfExistsById`. The result type is determined by the `QueryResultType`, similar to `SQLQuery`.

For example:


```java
// Read a POJO by id - same as always:
Product pojo = gigaSpace.readById(new IdQuery<Product>(Product.class, 7));

// Read a document by id when there's no
// compatible POJO - no need to specify query result type:
SpaceDocument document = gigaSpace.readById(
    new IdQuery<SpaceDocument>("Product", 7));

// Read a document by id when there is
// a compatible POJO - explicitly specify query result type:
SpaceDocument document = gigaSpace.readById(
    new IdQuery<SpaceDocument>(Product.class.getName(), 7,
        QueryResultType.DOCUMENT));
```

Respectively, to support multiple ids queries, `IdsQuery` was also introduced, with new signatures for `readByIds` and `takeByIds`. For example:


```java
Object[] ids = new Object[] {7, 8, 9};
// Read POJOs by ids - same as always:
Product[] pojos = gigaSpace.readByIds(
    new IdsQuery<Product>(Product.class, ids)).getResultsArray();

// Read documents by ids when there's no
// compatible POJO - no need to specify query result type:
SpaceDocument[] documents = gigaSpace.readByIds(
    new IdsQuery<SpaceDocument>("Product", ids)).getResultsArray();

// Read documents by ids when there is a
//  compatible POJO - explicitly specify query result type:
SpaceDocument[] documents = gigaSpace.readByIds(
    new IdsQuery<SpaceDocument>(Product.class.getName(),
        ids, QueryResultType.DOCUMENT)).getResultsArray();
```

{{% info %}}
The original `readById` (and related methods) signatures are not suited for document types, since they require a concrete java class. They always return POJO(s).
{{%/info%}}

# Dynamic Properties


When a type descriptor is created from a POJO class, the type descriptor builder checks if the POJO class supports [Dynamic Properties](./dynamic-properties.html) (new in 8.0.1). If it doesn't, the type descriptor will also not support dynamic properties. If a space document will be created using the same type with a property that is not defined in the POJO and written to the space, an exception will be thrown indicating the property is not defined in the type and the type does not support dynamic properties.

It is possible to manually create a `SpaceTypeDescriptor` of the POJO using the `SpaceTypeDescriptorBuilder` and enable dynamic properties. Note, however, that in that case if client A writes a document with a dynamic property and client B reads it as a POJO, the dynamic property will be ignored, and if client B will proceed to update the entry the dynamic property will be deleted from the space.

# Deep Interoperability


If the POJO contains properties which are POJO themselves, the space will implicitly convert these properties to space documents as needed.
For example:


```java
// Create a POJO entry with a POJO property and write it to space:
Person personPojo = new Person()
    .setName("smith")
    .setAddress(new Address()
        .setCity("New York")
        .setStreet("Main"));
gigaSpace.write(personPojo);
// Read POJO entry as a document:
SpaceDocument template = new SpaceDocument(Person.class.getName())
    .setProperty("name", "smith");
SpaceDocument personDoc = gigaSpace.read(template);
// Get address document from person document:
SpaceDocument addressDoc = personDoc.getProperty("address");
```

This works the other way around as well - if a space document is created with a nested space document property, it will be converted to a POJO with a nested POJO property when read as a POJO.

If you prefer to disable this implicit conversion and preserve the nested POJO instance within document entries, use the `@SpaceProperty` annotation and set `documentSupport` to `COPY`:


```java
public class Person {
    ...
    @SpaceProperty(documentSupport = SpaceDocumentSupport.COPY)
    public Address getAddress() {...}
    public Person setAddress(Address address) {...}
    ...
}
```

In that case the result would be:


```java
// Write POJO entry same as before
...
// Read POJO entry as a document:
SpaceDocument template = new SpaceDocument(Person.class.getName())
    .setProperty("name", "smith");
SpaceDocument personDoc = gigaSpace.read(template);
// Get address POJO from person document:
Address addressPojo = personDoc.getProperty("address");
```

The `SpaceDocumentSupport` can be one of the following:

- `CONVERT` -- Value is converted to/from space document, according to the operation's context.
- `COPY` -- Value reference is copied as-is - no conversion is performed.
- `DEFAULT` -- Behavior will be determined automatically according to the object's class.

This behavior applies to arrays and collections as well (for example, if `Person` would have `List<Address> getAddresses()`, it would be converted to a list of address documents).

{{% note %}}
This feature is new 8.0.1. In previous releases, there's no implicit conversion of nested properties.
{{%/note%}}

# Local View / Cache

[Local View](./local-view.html) and [Local Cache](./local-cache.html) supports both POJOs and Documents. Unlike an embedded space, the entry is stored in the cache as a user object (either POJO or document), which speeds up query performance since the result entries do not have to be transformed.

When working with POJOs only or Documents only, this is not an issue. However, when working in a mixed POJO-document environment it is important to understand how the objects are stored in cache to assure optimal performance.

Local view is defined by one or more views, which are essentially SQL queries, so the query result type discussed above actually determines if the objects are stored locally as POJOs or documents.

Local cache stores its object locally according to the master space: If a POJO entry was written to the master space, it will be kept in the local cache as a POJO as well, and if a document entry is written to the master it will be kept as document in the local cache. If a user asks the local cache for a document result but the entry is stored as a POJO it will be converted, and vice versa.

# Space Filters and Space Replication Filters

Space Filters are supported for space documents. If the space type descriptor that is registered in the space contains the POJO class, the entry will be passed to the filter as a POJO. Otherwise, it will be passed to the filter as a document.
