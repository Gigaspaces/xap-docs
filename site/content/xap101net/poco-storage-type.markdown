---
type: post101
title:  Property Storage Type
categories: XAP101NET
parent: modeling-your-data.html
weight: 400
---

{{% ssummary   %}} {{% /ssummary %}}



XAP's PBS (Portable Binary Serialization) is the underlying technology used to serialize and transport non Java objects from the client side to the space side. It is highly optimized serialization technology allowing a .Net or C++ client to communicate with the space in very efficiency manner.

To control Space Class field serialization you should use the [StorageType](http://www.gigaspaces.com/docs/dotnetdocs{{%currentversion%}}/html/T_GigaSpaces_Core_Metadata_StorageType.htm) attribute.
Example:


```csharp
public class Message
{
	private PayloadObject _payload;
	private SortedList<int, PayloadObject> _payloadList;
	[SpaceProperty (StorageType=StorageType.Binary)]
	public PayloadObject PayLoad
	{
	    get { return this._payload; }
	    set { this._payload = value; }
	}
	[SpaceProperty(StorageType = StorageType.Binary)]
	public SortedList<int, PayloadObject> Payloadlist
	{
	    get { return this._payloadList; }
	    set { this._payloadList = value; }
	}
...
}
```

Here are the `StorageType` supported options:

- **Object** - The space proxy will serialize the property value using `PBS` and the space will deserialize it into its java counterpart, and store it in the space as such. When the entry is read/taken from the space the property will be serialized back in the same manner.
- **Binary** - The space proxy will serialize the property value using `PBS`, but the space will not deserialize it, just keep the bytes in a special Binary container in the space. When the entry is read/taken the binary content will be passed as-is back to the .NET proxy, which will deserialize it back to a .NET object.
- **BinaryCustom** - Same as Binary, except that .NET serialization is used instead of `PBS` serialization.
- **Document** - The space proxy will serialize the property value in `PBS` and the space will deserialize it as a `SpaceDocument`, and store it in the space as such. This allows matching on nested properties inside the property and to index these nested properties as well.
- **Compressed** - Same as BinaryCustom, except that the value is compressed as well.

{{% note %}}
Indexing a property with a `Binary` / `BinaryCustom` / `Compressed` storage type is not supported. For more information about indexing see [Indexing](./indexing.html).
{{%/note%}}

# Interoperability

StorageType can be defined on .NET classes and C++ classes, but it currently cannot be defined on POJOs (planned for future releases).

Effectively this means a POJO property is always considered as StorageType.Object.

- `Object` and `Document` are fully interoperable across all proxy types (Java, .NET, c++).
- `Binary` is fully interoperable between .NET and C++, and partial interop with java (a java proxy can read the pojo, and access/modify all properties which are StorageType.Object, leaving the rest unchanged).
- `BinaryCustom` and `Compressed` are currently only supported for .NET proxies, with partial interop with java and C++ as described above.

# Defaults

There is a list of types which have 'premium citizenship' in the `PBS` protocol, which means all platforms know how to read/write them. These are: all the primitives (int, DateTime, string, etc,), nullables (Nullable<int>, Nullable<DateTime>, etc), arrays/collections/lists/maps/dictionaries of such. All of these types default to Object storage types.

Any other type is assumed to be a user defined type, and its default storage type will be BinaryCustom, to make sure a novice user will have no trouble working with it. Setting it to Object would required the user to provide the space with an equivalent POJO class, so the space would be able to store it, and this is something most .NET users don't care about. Setting it to Binary might provide faster result, but it have some limitations (e.g. complex object graphs which contains the same object twice, or cycles).

# Performance Considerations

- `Binary` is supposed to be the fastest (almost) always, because the server side doesn't need to deserialize/serialize on write/read.
- `BinaryCustom` is somewhat slower in most cases than Binary, because `PBS` serialization is customized for XAP whereas .NET serialization is more generic. However, on the space side, the data is still stored as a blob.
- `Object` and `Document` serialization are usually the slower because the space have to fully deserialize the blob and reconstruct the pojo or the document.
- `Compressed` is slower on one hand because compression takes time, but faster on the other because network traffic is reduced and the data is stored in the space as a blob and is not deserialized.

Note that for common primitive types (int, string, DateTime, etc.) these performance diffs are negligible (if at all), and there's no sense in changing the default from object. As the property's payload gets bigger, it makes more sense to start tweaking it's storage type.
