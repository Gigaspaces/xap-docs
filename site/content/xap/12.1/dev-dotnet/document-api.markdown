---
type: post121
title:  Document API
categories: XAP121NET, PRM
weight: 100
parent: document-overview.html
---

{{%ssummary%}}{{%/ssummary%}}

The XAP document API exposes the space as {{%exurl "Document Store" "http://en.wikipedia.org/wiki/Document-oriented_database"%}}. A document, which is represented by the class `SpaceDocument`, is essentially a collection of key-value pairs, where the keys are strings and the values are primitives, `String`, `Date`, other documents, or collections thereof. Most importantly, the Space is aware of the internal structure of a document, and thus can index document properties at any nesting level and expose rich query semantics for retrieving documents.

Unlike concrete objects, which force users to design a fixed data schema (in the form of a class definition) and adhere to it, a document is much more dynamic - users can add and remove properties at runtime as necessary. A Document always belongs to a certain type, represented by the interface `ISpaceTypeDescriptor`.

Before a certain Document instance is written to the space, its type should be introduced to the data grid. The type has a name and controls metadata such as an identifier property, a routing property and which properties are initially indexed (naturally, you can also index new properties at runtime after adding them to your documents).

{{% tip %}}
The Type controls **metadata** - so only the metadata is part of the type. A document can introduce new properties at will.
{{% /tip %}}

Note that the Document type does not describe the properties themselves (except for the names of the ID and Routing properties). These are completely dynamic and each instance can have a different set of properties (although in most cases Document instances of the same type are likely to have identical or similar set of properties).

# Schema Evolution with Space Documents

Since a `SpaceDocument` is completely dynamic by nature, it is very easy to change or evolve your data model without ever taking down the Space. Simply change your application code to add additional properties or remove existing ones, and you're good to go. Even better, old and new versions can co-exist since the space does not enforce any restriction with regards to the property set of documents that belong to a certain type. This is a much more lightweight model in comparison to the classic concrete object model, where a recompilation and in many cases a full space restart is required to change the data schema.

# Type Definition

Before we start writing and reading `SpaceDocument` from the space, we need an **initial** schema definition of the document type.

For example, suppose we're implementing an electronic commerce system, and decided we need a type called **Product** with the following properties:

- CatalogNumber : String
- Category : String
- Name : String
- Description : String
- Price : float
- Features : Nested document (for example: Manufacturer=Acme, RequiresAssembly=false, weight=7.5)
- Tags : Collection of Strings
- Reviews : Collection of nested documents

We also decide that **CatalogNumber** will be a primary key, partitioning will be done by the **Category** property, and the properties **Name**, **Price** should be indexed since they participate in most of the queries executed. Remember, the type definition is for metadata only, so we're not concerned about **Description** and other such fields in the type definition, because Description isn't used for indexing or any other metadata.

The following is an example of how to introduce a new document type:


```csharp
ISpaceProxy spaceProxy = ... //Obtain a space proxy
// Create type descriptor:
SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder("Product");
typeBuilder.SetIdProperty("CatalogNumber");
typeBuilder.SetRoutingProperty("Category");
typeBuilder.AddPropertyIndex("Name");
typeBuilder.AddPropertyIndex("Price", SpaceIndexType.Extended);
ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create();
// Register type descriptor:
spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptor);
```

Note that this code does not reflect the complete model - most of the properties do not need to be introduced to the schema. Only properties with special roles (ID, Routing) are part of the schema definition. These meta model **settings cannot be changed** without restarting the space or dropping the type, clearing all its instances and reintroducing it again.

# Creating and Writing Documents

To create a document in the space, create a `SpaceDocument` object using the type name, and set the desired properties. Then write it to the space using the regular `ISpaceProxy` write method:


```csharp
public void WriteProduct1(ISpaceProxy spaceProxy)
{
    // 1. Create the document using the type name
    SpaceDocument document = new SpaceDocument("Product");
    // 2. Create the properties:
    document["CatalogNumber"] = "hw-1234";
    document["Category"] = "Hardware";
    document["Name"] = "Anvil";
    document["Price"] = 9.99f;
    document["Tags"] = new String[] {"heavy", "anvil"};

    IDictionary<String, Object> features = new Dictionary<String, Object>();
    features["Manufacturer"] = "Acme";
    features["RequiresAssembly"] = false;
    features["Weight"] = 100;
    document["Features"] = features;

    IDictionary<String, Object> review1 = new Dictionary<String, Object>();
    review1["Name"] = "Wile E. Coyote";
    review1["Rate"] = 1;
    review1["Comments"] "Don't drop this on your toe, it will hurt.";
    IDictionary<String, Object>> review2 = new Dictionary<String, Object>();
    review2["Name"] = "Road Runner";
    review2["Rate"] = 5;
    review2["Comments"] = "Beep Beep!";
    document["Reviews"] = new IDictionary<String, Object>[] {review1, review2});

    // 3. Write the document to the space:
    spaceProxy.Write(document);
}
```

Using generic types as nested properties (i.e IDictionary<String, Object> features) will be read as IDictionary<Object, Object> when the above document is read from the space.

There are two ways to receive back a dictionary of the same generic type. One is using the `DocumentProperties` class instead, and the other is to use a BinaryCustom storage type for the dynamic properties.

The `DocumentProperties` class extends IDictionary<String, Object> and besides being strongly typed to String keys and Object values, it provides better performance when used.


```csharp
public void WriteProduct2(ISpaceProxy spaceProxy)
{
    // 1. Create the document using the type name
    SpaceDocument document = new SpaceDocument("Product");
    // 2. Create the properties:
    document["CatalogNumber"] = "hw-1234";
    document["Category"] = "Hardware";
    document["Name"] = "Anvil";
    document["Price"] = 9.99f;
    document["Tags"] = new String[] {"heavy", "anvil"};

    DocumentProperties features = new DocumentProperties();
    features["Manufacturer"] = "Acme";
    features["RequiresAssembly"] = false;
    features["Weight"] = 100;
    document["Features"] = features;

    DocumentProperties review1 = new DocumentProperties();
    review1["Name"] = "Wile E. Coyote";
    review1["Rate"] = 1;
    review1["Comments"] "Don't drop this on your toe, it will hurt.";
    DocumentProperties review2 = new DocumentProperties();
    review2["Name"] = "Road Runner";
    review2["Rate"] = 5;
    review2["Comments"] = "Beep Beep!";
    document["Reviews"] = new DocumentProperties[] {review1, review2});

    // 3. Write the document to the space:
    spaceProxy.Write(document);
}
```

{{% note %}} The `ISpaceProxy.WriteMultiple` method can be used to write a batch of documents.{{%/note%}}

{{% warning %}}
Update semantics are the same as for concrete objects, except **partial update**, which is not currently supported.
Use only alphanumeric characters (a-z, A-Z, 0-9) and the underscore ('_') to construct properties keys. Other characters might have special behaviors in GigaSpaces (for example: the dot ('.') is used to distinguish nested paths).
{{%/warning%}}

# Reading and Removing Documents

There are three types of document queries:

## Template Query

This type of query uses a SpaceDocument with _type_ and any other set of properties values as a template for the query
For example: Read a document of type **Product** whose **Name** is **Anvil**:


```csharp
public SpaceDocument ReadProductByTemplate(ISpaceProxy spaceProxy)
{
    // Create template:
    SpaceDocument template = new SpaceDocument("Product");
    template["Name"] = "Anvil";
    // Read:
    SpaceDocument result = spaceProxy.Read(template);
    return result;
}
```

## Sql Query

You can use the [SqlQuery](./query-sql.html) to search for matching `SpaceDocument` entries.

For example: to read a document of type **Product** whose **Price** is greater than 15:


```csharp
public SpaceDocument ReadProductBySql(ISpaceProxy spaceProxy)
{
    // Create query:
    SqlQuery<SpaceDocument> query = new SqlQuery<SpaceDocument>("Product", "Price > ?");
    query.SetParameter(1, 15f);
    // Read:
    SpaceDocument result = spaceProxy.Read(query);
    return result;
}
```

{{% tip %}}
Consider indexing properties used in queries to boost performance.
{{% /tip %}}

Queries on nested properties are supported. For example, to read products manufactured by **Acme**:


```csharp
public SpaceDocument[] ReadProductBySqlNested(ISpaceProxy spaceProxy)
{
    // Create query:
    SqlQuery<SpaceDocument> query = new SqlQuery<SpaceDocument>(
        "Product", "Features.Manufacturer = ?");
    query.SetParameter(1, "Acme");
    // Read:
    SpaceDocument[] result = spaceProxy.ReadMultiple(query, 10);
    return result;
}
```

## ID Based Query

For example: Read a document of type **Product** whose ID is **hw-1234**:


```csharp
public SpaceDocument ReadProductById(ISpaceProxy spaceProxy)
{
    return spaceProxy.ReadById(new IdQuery<SpaceDocument>("Product", "hw-1234"));
}
```

Queries by multiple Ids are supported. For example:


```csharp
public SpaceDocument[] ReadProductByMultipleIds(ISpaceProxy spaceProxy)
{
    Object[] ids = new Object[] {"hw-1234", "av-9876"};
    IReadByIdsResult<SpaceDocument> result =
        spaceProxy.ReadByIds(new IdsQuery<SpaceDocument>("Product", ids));
    return result.ResultsArray;
}
```

{{% note %}}
All other `ISpaceProxy` query operations (ReadIfExists, ReadMultiple, Take, TakeIfExists, TakeMultiple, Count, Clear) are supported for documents entries as well.
All other Id based operations (ReadIfExists, TakeById, TakeIfExistsById, TakeByIds) are supported for documents as well.
All overloads of those operations with timeout, transactions, modifiers etc. are supported for documents. The semantics is similar to concrete objects.
{{%/note%}}

# Nested Properties

The `Document` properties' values can be either scalars (integers, strings, enumerations, etc), collections (arrays, lists), or nested properties (dictionary or an extension of dictionary, such as `DocumentProperties`). Values must adhere to the same restrictions as in the concrete object model (e.g. be serializable). Nested properties can be queried by using the dot ('.') notation to describe paths, as shown above.

{{% note%}}
It's highly recommended to use `DocumentProperties` for nested documents since it contains performance and memory footprint optimizations which are tailored for GigaSpaces usage.
{{%/note%}}


{{% warning %}}
While it's possible to use  `SpaceDocument` as a property, it is probably a mistake, since it contains extra information which is not relevant for nested properties (type name, version, etc.).
{{%/warning%}}


# Document Hierarchy

SpaceDocument query supports hierarchical relationships so that entries of a child are visible in the context of the parent document, but not the other way around. For example, a document with name Employee can register its parent document Person in the following way:


```csharp
SpaceTypeDescriptor employeeDescriptor = new SpaceTypeDescriptorBuilder(
				"Child Document Type Name", parentSpaceTypeDescriptor).Create();
```

Here is an example:

{{%tabs%}}
{{%tab " Program"%}}

```csharp
public SimpleDocumentQuery ()
{
	// Create the Space
	ISpaceProxy spaceProxy = new EmbeddedSpaceFactory ("mySpace").Create ();

	registerDocument (spaceProxy);

	SpaceDocument document1 = new SpaceDocument ("Person");
	document1 ["ID"] = "1234";
	document1 ["FirstName"] = "John";
	document1 ["LastName"] = "Fellner";

	spaceProxy.Write (document1);

	SpaceDocument document2 = new SpaceDocument ("Employee");
	document2 ["ID"] = "12345";
	document2 ["FirstName"] = "John";
	document2 ["LastName"] = "Walters";
	document2 ["EmployeeId"] = "12345";

	spaceProxy.Write (document2);

	SqlQuery<SpaceDocument> query1 = new SqlQuery<SpaceDocument> (
			                                  "Person", "");

	SpaceDocument[] result1 = spaceProxy.ReadMultiple<SpaceDocument> (query1);

	// You should see two documents
	Console.WriteLine (result1.Length);

	SqlQuery<SpaceDocument> query2 = new SqlQuery<SpaceDocument> (
			                                  "Employee", "");

	SpaceDocument[] result2 = spaceProxy.ReadMultiple<SpaceDocument> (query2);

	// You should see one document
	Console.WriteLine (result2.Length);
}

```
{{%/tab%}}

{{%tab " RegisterDocument"%}}

```csharp
public  void  registerDocument (ISpaceProxy spaceProxy)
{
	// Create the Person descriptor
	SpaceTypeDescriptorBuilder personDescriptor = new SpaceTypeDescriptorBuilder ("Person");
	personDescriptor.SetIdProperty ("ID");
	ISpaceTypeDescriptor personTypeDescriptor = personDescriptor.Create ();

	// Register type:
	spaceProxy.TypeManager.RegisterTypeDescriptor (personTypeDescriptor);

	// Create the Employee descriptor
	SpaceTypeDescriptorBuilder employeeDescriptor = new SpaceTypeDescriptorBuilder ("Employee",
			                                                 personTypeDescriptor);
	ISpaceTypeDescriptor employeeTypeDescriptor = employeeDescriptor.Create ();

	// Register type:
	spaceProxy.TypeManager.RegisterTypeDescriptor (employeeTypeDescriptor);
}
```
{{%/tab%}}
{{%/tabs%}}

# Indexing

Properties and nested paths can be [indexed](./indexing.html) to boost queries performance. In the type registration sample above, the **Name** and **Price** properties are indexed.

Since the schema is flexible and new properties might be added after the type has been registered, it is possible to add indexes dynamically as well.

{{% refer %}}For more information about indexing, see the [Indexing](./indexing.html) page.{{% /refer %}}

# Events

Event containers (both [polling container](./polling-container.html) and [notify container](./notify-container.html)) support `SpaceDocument` entries.

Here is a simple example of a polling event container configuration using a `SpaceDocument`:


```csharp
[PollingEventDriven]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            SpaceDocument template = new SpaceDocument("Product");
            template["Name"] = "Anvil";
            return template;
        }
    }

    [DataEventHandler]
    public SpaceDocument EventListener(SpaceDocument event)
    {
        //process Data here
    }
}
```

# FIFO

FIFO support is off by default with `SpaceDocument` entries (as with concrete object entries). To enable FIFO support, modify the type introduction code and set the desired FIFO support mode. For example:


```csharp
// Create type descriptor:
ISpaceProxy spaceProxy = ... //Obtain a space proxy
// Create type descriptor:
SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder("Product");
// Other type descriptor settings.
typeBuilder.FifoSupport = FifoSupport.Operation;
ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create();
// Register type descriptor:
spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptor);
```

{{% info %}} Changing FIFO support after a type has been registered is not supported. {{%/info%}}

# Transactions and Optimistic Locking

Transactions and isolation modifier semantics is identical to the concrete object semantics.

Optimistic locking is disabled by default with `SpaceDocument` entries (same as with concrete object). To enable it, modify the type introduction code and set the optimistic locking support. For example:


```csharp
// Create type descriptor:
ISpaceProxy spaceProxy = ... //Obtain a space proxy
// Create type descriptor:
SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder("Product");
// Other type descriptor settings.
typeBuilder.SupportsOptimisticLocking = true;
ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create();
// Register type descriptor:
spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptor);
```

{{% warning %}}
Changing optimistic locking after a type has been registered is not supported.
{{%/warning%}}

# Dynamic Properties Storage Type

By default, the dynamic properties of the document will be deserialized fully and kept in space as an instance of their corresponding space type class. This allows indexing and nested matching against these properties. However, in some scenarios, the data kept inside the dynamic properties is not used for matching. In that case it is possible to keep these properties serialized in their binary format in the space, increasing performance and reducing memory consumption.

However, the data stored in binary format will be not be visible via the management center or readable from an interoperable csharp/C++ client, it will appear as binary large object to these clients and will only deserialized when a .NET client reads it. The way to specify the storage type is when constructing the type using the `SpaceTypeDescriptorBuilder`:


```csharp
// Create type descriptor:
ISpaceProxy spaceProxy = ... //Obtain a space proxy
// Create type descriptor:
SpaceTypeDescriptorBuilder typeBuilder = new SpaceTypeDescriptorBuilder("Product");
// Other type descriptor settings.
typeBuilder.DynamicPropertiesSupport(true, StorageType.Binary);
ISpaceTypeDescriptor typeDescriptor = typeBuilder.Create();
// Register type descriptor:
spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptor);
```

{{% refer %}}For more info on storage types, please refer to [Property Storage Type](./poco-storage-type.html){{% /refer %}}

# Persistency

The External Data Source is supported for space documents, however, if the EDS is also used to initially load the space with data, the space needs to be aware of the type descriptors of the documents. Therefore, the space needs to be started with known type descriptors.

Example on how to implement an EDS that persists SpaceDocuments of type "Trade" and start a space in a basic processing unit container with the EDS and known Trade type descriptors:

{{%tabs%}}

{{%tab "  Starting the Space "%}}


```csharp
[ContainerInitializing]
public void Initialize(BasicProcessingUnitContainer container)
{
  //Create a new space configuration object that is used to start a space
  SpaceConfig spaceConfig = new SpaceConfig();
  //Set a new ExternalDataSource config object
  spaceConfig.ExternalDataSourceConfig = new ExternalDataSourceConfig();
  //Configure EDS related stuff
  ...
  spaceConfig.ExternalDataSourceConfig.Instance = new DocumentEDS();
  //Set the EDS to work with documents
  spaceConfig.ExternalDataSourceConfig.EntryType = EntryType.Document;

  SpaceTypeDescriptor tradeTypeDescBuilder = new SpaceTypeDescriptor("Trade");
  //Set id property with auto generate true
  tradeTypeDescBuilder.SetIdProperty("Uid", true);
  tradeTypeDescBuilder.SetRoutingProperty("SymbolLabel");
  //Adding type descriptor to the space known type descriptors
  spaceConfig.SpaceTypeDescriptors.Add(tradeTypeDescBuilder.Create());
  //Create the space
  ISpaceProxy spaceProxy = container.CreateSpaceProxy(
    "MySpace", "/./MySpace", spaceConfig);
}
```

{{% /tab %}}

{{%tab "  The EDS Implementation "%}}


```csharp
public class DocumentEDS : ISqlDataSource
{
    public void Init(Dictionary<String, String> properties)
    {
        // initialize persistency layer
    }

    IDataEnumerator InitialLoad()
    {
        // load all the data from persistency
        // build and return an iterator of documents
    }

    public void ExecuteBulk(IList<BulkItem> bulk)
    {
        foreach (BulkItem bulkItem in bulk)
        {
            SpaceDocument document = (SpaceDocument) bulkItem.Item;

            switch (bulkItem.Operation)
            {
                case BulkItem.WRITE:

                   // WriteDocument(document);
                    break;
                case BulkItem.UPDATE:

                   // UpdateDocument(document);
                    break;
                case BulkItem.REMOVE:
                    //RemoveDocument(document);
                    break;

                default:
                    break;
            }
        }
    }

    public void Shutdown()
    {
        //cleanup resources and close the persistency
    }
}
```

{{% /tab %}}

{{% /tabs %}}

Concrete objects can be persisted via document EDS as well, in the same way.

{{% warning %}}
In order to support InitialLoad of documents the relevant types must be declared in the space config, so that they are registered in the space before InitialLoad is invoked.
Document persistence is currently not provided by default - If needed, the External Data Source should be implemented to fit the required solution.
{{%/warning%}}

