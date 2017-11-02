---
type: post123
title:  Extended Document
categories: XAP123, OSS
parent: document-overview.html
weight: 200
---


{{% ssummary %}}{{% /ssummary %}}


While documents provide us with a dynamic schema, they force us to give up Java's type-safety for working with type less key-value pairs. GigaSpaces supports extending the SpaceDocument class to provide a type-safe wrapper for documents which is much easier to code with, while maintaining the dynamic schema.



# Definition

Let's create a type-safe document wrapper for the **Product** type described in the [Document Support](./document-api.html) page. The extensions are:

- Provide a parameter less constructor, since the type name is fixed.
- Provide type-safe properties, but instead of using private fields to store/retrieve the values, use the getProperty/setProperty methods from the SpaceDocument class.

Here's an example (only parts of the properties have been implemented to keep the example short):


```java
public class ProductDocument extends SpaceDocument {
    private static final String TYPE_NAME = "Product";
    private static final String PROPERTY_CATALOG_NUMBER = "CatalogNumber";
    private static final String PROPERTY_NAME = "Name";
    private static final String PROPERTY_PRICE = "Price";

    public ProductDocument() {
        super(TYPE_NAME);
    }

    public String getCatalogNumber() {
        return super.getProperty(PROPERTY_CATALOG_NUMBER);
    }
    public ProductDocument setCatalogNumber(String catalogNumber) {
        super.setProperty(PROPERTY_CATALOG_NUMBER, catalogNumber);
        return this;
    }

    public String getName() {
        return super.getProperty(PROPERTY_NAME);
    }
    public ProductDocument setName(String name) {
        super.setProperty(PROPERTY_NAME, name);
        return this;
    }

    public float getPrice() {
        return super.getProperty(PROPERTY_PRICE);
    }
    public ProductDocument setPrice(float price) {
        super.setProperty(PROPERTY_PRICE, price);
        return this;
    }
}
```

# Registration

If your only intention is to write/update document entries, creating the extension class is sufficient - from the space's perspective it is equivalent to a `SpaceDocument` instance. However, if you attempt to read/take entries from the space, the results will be `SpaceDocument` instances, and the cast to `ProductDocument` will throw an exception.
To overcome that, we need to include the document wrapper class in the type introduction:


```java
public void registerProductType(GigaSpace gigaspace) {
    // Create type descriptor:
    SpaceTypeDescriptor typeDescriptor =
        new SpaceTypeDescriptorBuilder("Product")
        // ... Other type settings
        .documentWrapperClass(ProductDocument.class)
        .create();
    // Register type:
    gigaspace.getTypeManager().registerTypeDescriptor(typeDescriptor);
}
```

This wrapper type-registration is kept in the proxy and not propagated to the server, so that from the server's perspective this is still a virtual document type with no affiliated POJO class.

# Usage

The following code snippet demonstrate usage of the `ProductDocument` extensions we've created to write and read documents from the space.


```java
public void example(GigaSpace gigaSpace) {
    // Create a product document:
    ProductDocument product = new ProductDocument()
        .setCatalogNumber("hw-1234")
        .setName("Anvil")
        .setPrice(9.99f);
    // Write a product document:
    gigaSpace.write(product);

    // Read product document using a template:
    ProductDocument template = new ProductDocument()
        .setName("Anvil");
    ProductDocument result1 = gigaSpace.read(template);
    // Read product document using a SQL query:
    SQLQuery<ProductDocument> query = new SQLQuery<ProductDocument>("Product", "Price > ?")
        .setParameter(1, 5.5f);
    ProductDocument result2 = gigaSpace.read(query);
    // Read product document by ID:
    ProductDocument result3 = gigaSpace.readById(new IdQuery<ProductDocument>("Product", "hw-1234"));
}
```


# Inheritance  Support

SpaceDocument query supports inheritance relationships so that entries of a sub-class are visible in the context of the super class, but not the other way around. For example, suppose class EmployeeDoc extends class PersonDoc and PersonDoc extends from `SpaceDocument`, you can register
the the sub classes in the following way:


```java
SpaceTypeDescriptor employeeDescriptor = new SpaceTypeDescriptorBuilder(
				"Subclass Document Type Name", parentSpaceTypeDescriptor).create();
```

Here is an example:

{{%tabs%}}
{{%tab " PersonDoc"%}}

```java
public class PersonDoc extends SpaceDocument {

	public static final String TYPE_ID = "ID";
	public static final String TYPE_NAME = "PersonDoc";
	public static final String FIRST_NAME = "FirstName";
	public static final String LAST_NAME = "LastName";

	public PersonDoc() {
		super(TYPE_NAME);
	}

	public PersonDoc(String type) {
		super(type);
	}
	public void setId(String id) {
		super.setProperty(TYPE_ID, id);
	}

	public String getId() {
		return super.getProperty(TYPE_ID);
	}

	public String getFirstName() {
		return super.getProperty(FIRST_NAME);
	}

	public String getLastName() {
		return super.getProperty(FIRST_NAME);
	}

	public void setFirstNme(String name) {
		super.setProperty(FIRST_NAME, name);
	}

	public void setLastNme(String name) {
		super.setProperty(LAST_NAME, name);
	}
}
```
{{%/tab%}}

{{%tab " EmployeeDoc"%}}

```java
public class EmployeeDoc extends PersonDoc {

	public static final String TYPE_NAME = "EmployeeDoc";
	public static final String EMPLOYE_NUMBER = "EMPLOYE_NUMBER";

	public EmployeeDoc() {
		super(TYPE_NAME);
	}

	public String getEmployeNumber() {
		return super.getProperty(EMPLOYE_NUMBER);
	}

	public void setEmployeNumber(String number)
	{
		super.setProperty(EMPLOYE_NUMBER, number);
	}
}
```
{{%/tab%}}

{{%tab " RegisterDocument"%}}

```java
	static public void registerDocument(GigaSpace space) {
		SpaceTypeDescriptor personDescriptor = new SpaceTypeDescriptorBuilder(
				PersonDoc.TYPE_NAME).documentWrapperClass(PersonDoc.class).
				idProperty(PersonDoc.TYPE_ID).create();
		// Register type:
		space.getTypeManager().registerTypeDescriptor(personDescriptor);

		SpaceTypeDescriptor employeeDescriptor = new SpaceTypeDescriptorBuilder(
				EmployeeDoc.TYPE_NAME, personDescriptor).documentWrapperClass(EmployeeDoc.class).create();
		// Register type:
		space.getTypeManager().registerTypeDescriptor(employeeDescriptor);
	}
```
{{%/tab%}}

{{%tab " Program"%}}

```java
public static void main(String[] args) {

		// Create the Space
		GigaSpace space = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer(
				"mySpace")).gigaSpace();


		registerDocument(space);

		PersonDoc doc1 = new PersonDoc();
		doc1.setId("1");
		doc1.setFirstNme("John");
		doc1.setLastNme("Fellner");

		space.write(doc1);

		EmployeeDoc doc2 = new EmployeeDoc();
		doc2.setId("2");
		doc2.setFirstNme("John");
		doc2.setLastNme("Fellner");
		doc2.setEmployeNumber("1234");

		space.write(doc2);

		SQLQuery<SpaceDocument> query1 = new SQLQuery<SpaceDocument>(
				PersonDoc.TYPE_NAME, "");

		PersonDoc[] result = (PersonDoc[]) space.readMultiple(query1);

        // You should see two objects
		System.out.println(result.length);

		SQLQuery<SpaceDocument> query2 = new SQLQuery<SpaceDocument>(
				EmployeeDoc.TYPE_NAME, "");

		EmployeeDoc[] result2 = (EmployeeDoc[]) space.readMultiple(query2);

        // You should see one object
		System.out.println(result2.length);
}

```
{{%/tab%}}
{{%/tabs%}}
