---
type: post97
title:  Extending
categories: XAP97NET
weight: 200
canonical: auto
parent: document-overview.html
---

{{% ssummary %}} {{% /ssummary %}}


While documents provide us with a dynamic schema, they force us to give up .NET type-safety for working with type less key-value pairs. GigaSpaces supports extending the SpaceDocument class to provide a type-safe wrapper for documents which is much easier to code with, while maintaining the dynamic schema.

{{% note %}}
Do not confuse this with [Document-Object interoperability](./document-object-interoperability.html), which is a different feature.
{{%/note%}}

# Creating the Extension Class

Let's create a type-safe document wrapper for the **Product** type described in the [Document Support](./document-api.html) page. The extensions are:

- Provide a parameter less constructor, since the type name is fixed.
- Provide type-safe properties, but instead of using private fields to store/retrieve the values, use the index operator of the SpaceDocument class.

Here's an example (only parts of the properties have been implemented to keep the example short):


```csharp
public class ProductDocument : SpaceDocument
{
    private const String TypeName = "Product";
    private const String PropertyCatalogNumber = "CatalogNumber";
    private const String PropertyName = "Name";
    private const String PropertyPrice = "Price";

    public ProductDocument() : base(TypeName)
    {
    }

    public String CatalogNumber
    {
       get { return this[PropertyCatalogNumber]; }
       set { this[PropertyCatalogNumber] = value; }
    }

    public String Name
    {
       get { return this[PropertyName]; }
       set { this[PropertyName] = value; }
    }

    public float Price
    {
       get { return this[PropertyPrice]; }
       set { this[PropertyPrice] = value; }
    }
}
```

# Registering the Extension Class

If your only intention is to write/update document entries, creating the extension class is sufficient - from the space's perspective it is equivalent to a `SpaceDocument` instance. However, if you attempt to read/take entries from the space, the results will be `SpaceDocument` instances, and the cast to `ProductDocument` will throw an exception.
To overcome that, we need to include the document wrapper type in the type introduction:


```csharp
public void RegisterProductType(ISpaceProxy spaceProxy)
{
    // Create type descriptor:
    SpaceTypeDescriptorBuilder typeDescriptorBuilder = new SpaceTypeDescriptorBuilder("Product");
    // ... Other type settings
    typeDescriptorBuilder.DocumentWrapperType = typeof(ProductDocument);
    // Register type:
    spaceProxy.TypeManager.RegisterTypeDescriptor(typeDescriptorBuilder.Create());
}
```

This wrapper type-registration is kept in the proxy and not propagated to the server, so that from the server's perspective this is still a virtual document type with no affiliated Concrete object class.

# Using the Extension Class

The following code snippet demonstrate usage of the `ProductDocument` extensions we've created to write and read documents from the space.


```csharp
public void example(ISpaceProxy spaceProxy)
{
    // Create a product document:
    ProductDocument product = new ProductDocument({CatalogNumber = "hw-1234", Name = "Anvil", Price = 9.99F});
    // Write a product document:
    spaceProxy.Write(product);

    // Read product document using a template:
    ProductDocument template = new ProductDocument({Name = "Anvil"});
    ProductDocument result1 = spaceProxy.Read(template);
    // Read product document using a SQL query:
    SqlQuery<ProductDocument> query = new SqlQuery<ProductDocument>("Product", "Price > ?");
    query.SetParameter(1, 5.5f);
    ProductDocument result2 = spaceProxy.Read(query);
    // Read product document by ID:
    ProductDocument result3 = spaceProxy.ReadById(new IdQuery<ProductDocument>("Product", "hw-1234"));
}
```
