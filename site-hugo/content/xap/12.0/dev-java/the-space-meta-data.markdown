---
type: post120
title:  Data Type Meta Data
categories: XAP120
weight: 1100
canonical: auto
parent: the-gigaspace-interface-overview.html
---


XAP provides the ability to obtain and modify class meta data of objects stored in the Space during runtime.


# Meta data discovery

The actual class names are discovered with the **Admin interface**  through the `Space` interface.
 

```java
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.space.Space;
...

Admin admin = new AdminFactory().discoverUnmanagedSpaces().addGroup("TEST").createAdmin();
...
Space space = admin.getSpaces().waitFor(spaceName  , 10 , TimeUnit.SECONDS);
....
String SpaceClassNames[] = space.getRuntimeDetails().getClassNames();
...
```

{{%refer%}}
For  detailed information on the `AdminFactory` consult [AdminFactory](./administration-and-monitoring-api.html)
{{%/refer%}}

The `GigaSpaceTypeManager` is retrieved from the Space instance and will give us the detailed information for each class:


```java
GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName).lookupGroups("TEST")).gigaSpace();
....
GigaSpaceTypeManager gigaSpaceTypeManager = gigaSpace.getTypeManager();
```

We can iterate over all classes that are present in the Space and obtain their meta data:

```java
for (int j = 0; j < SpaceClassNames.length; j++) {
    String spaceClass = SpaceClassNames[j];

    SpaceTypeDescriptor typeManager = gigaSpaceTypeManager.getTypeDescriptor(spaceClass);
    System.out.println("Super Type Name:" + typeManager.getSuperTypeName());
    System.out.println("Id Property Name:"+ typeManager.getIdPropertyName());	
    .......
}
```



#### Example

Lets assume that we have an abstract super class **Entity** and two sub classes, **Bank** and **Supplier**.
 

{{%tabs%}}
{{%tab  Entity%}}
```java
import java.util.UUID;

import com.gigaspaces.annotation.pojo.SpaceId;

public abstract class Entity {
	private UUID id;
	private String emailAddress;
	private String contact;

	@SpaceId(autoGenerate=false)
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
}

```
{{%/tab%}}

{{%tab  Bank%}}
```java
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceIndex;

@SpaceClass
public class Bank extends Entity {
	private String swift;
	private String iban;
	private Collection<String> currencyRates;

	@SpaceIndex
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Collection<String> getCurrencyRates() {
		return currencyRates;
	}
	public void setCurrencyRates(Collection<String> currencyRates) {
		this.currencyRates = currencyRates;
	}
}

```
{{%/tab%}}

{{%tab  Supplier%}}
```java
import com.gigaspaces.annotation.pojo.SpaceClass;

@SpaceClass
public class Supplier extends Entity {
	private Integer number;
	private ESupplierType category;

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public ESupplierType getCategory() {
		return category;
	}
	public void setCategory(ESupplierType category) {
		this.category = category;
	}
}

```
{{%/tab%}}

{{%tab  ESupplierType%}}
```java
public enum ESupplierType {
	NATIONAL, INTERNATIONAL
}
```
{{%/tab%}}

{{%/tabs%}}

Here is an example that uses the `GigaSpaceTypeManager` to explore the Space for meta data:

```java
@Test
public void test() throws InterruptedException {
	String spaceName = "mySpace";

	GigaSpace gigaSpaceEmb = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer(spaceName).lookupGroups("TEST"))
				.gigaSpace();

	System.out.println(
			"This test will write 2 space objects into the space , each using different classes and will use the SpaceTypeDescriptor to retrieve the space class meta data back");

    // wait for the admin to synchronize
    Thread.sleep(1000);
    Admin admin = new AdminFactory().discoverUnmanagedSpaces().addGroup("TEST").createAdmin();

	if (admin == null) {
		System.out.println("Can't find space " + spaceName + " Exit!");
		System.exit(0);
	}

	Space space = admin.getSpaces().waitFor(spaceName, 20, TimeUnit.SECONDS);

	if (space == null) {
		System.out.println("Can't find space " + spaceName + " Exit!");
		System.exit(0);
	}

	GigaSpace gigaSpace = new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName).lookupGroups("TEST"))
				.gigaSpace();

    // Create two new objects and write them to the Space
    Bank b = new Bank();
    b.setId(UUID.randomUUID());
    gigaSpace.write(b);
    Supplier s = new Supplier();
    s.setId(UUID.randomUUID());
    gigaSpace.write(s);

    String SpaceClassNames[] = space.getRuntimeDetails().getClassNames();
    GigaSpaceTypeManager gigaSpaceTypeManager = gigaSpace.getTypeManager();

    // Explore the space classes
    for (int j = 0; j < SpaceClassNames.length; j++) {
        String spaceClass = SpaceClassNames[j];
        System.out.println();
        System.out.println("Meta Data       :" + spaceClass);
        SpaceTypeDescriptor typeManager = gigaSpaceTypeManager.getTypeDescriptor(spaceClass);
        System.out.println("Super Type Name :" + typeManager.getSuperTypeName());
        System.out.println("Id Property Name:" + typeManager.getIdPropertyName());
        System.out.println("Indexes         :" + typeManager.getIndexes());
        String[] typeNames = typeManager.getPropertiesNames();
        System.out.println("Properties:");
        for (int i = 0; i < typeNames.length; i++) {
		    String prop = typeNames[i];
		    SpacePropertyDescriptor propdesc = typeManager.getFixedProperty(prop);

		    System.out.println("  Name:" + propdesc.getName() + " Type:" + propdesc.getTypeName() + " Storage Type:"
						+ propdesc.getStorageType());
		}
	}
	admin.close();

	System.exit(0);
}
```

The above program will produce the following output when executed: 

```bash
This test will write 2 space objects into the space , each using different classes and will use the SpaceTypeDescriptor to retrieve the space class meta data back

Meta Data       :java.lang.Object
Super Type Name :java.lang.Object
Id Property Name:null
Indexes         :{}
Properties:

Meta Data       :xap.sandbox.type.Bank
Super Type Name :xap.sandbox.type.Entity
Id Property Name:id
Indexes         :{id=SpaceIndex[name=id, type=BASIC, unique=true], swift=SpaceIndex[name=swift, type=BASIC, unique=false]}
Properties:
  Name:contact Type:java.lang.String Storage Type:OBJECT
  Name:emailAddress Type:java.lang.String Storage Type:OBJECT
  Name:id Type:java.util.UUID Storage Type:OBJECT
  Name:currencyRates Type:java.util.Collection Storage Type:OBJECT
  Name:iban Type:java.lang.String Storage Type:OBJECT
  Name:swift Type:java.lang.String Storage Type:OBJECT

Meta Data       :xap.sandbox.type.Entity
Super Type Name :java.lang.Object
Id Property Name:id
Indexes         :{id=SpaceIndex[name=id, type=BASIC, unique=true]}
Properties:
  Name:contact Type:java.lang.String Storage Type:OBJECT
  Name:emailAddress Type:java.lang.String Storage Type:OBJECT
  Name:id Type:java.util.UUID Storage Type:OBJECT

Meta Data       :xap.sandbox.type.Supplier
Super Type Name :xap.sandbox.type.Entity
Id Property Name:id
Indexes         :{id=SpaceIndex[name=id, type=BASIC, unique=true]}
Properties:
  Name:contact Type:java.lang.String Storage Type:OBJECT
  Name:emailAddress Type:java.lang.String Storage Type:OBJECT
  Name:id Type:java.util.UUID Storage Type:OBJECT
  Name:category Type:xap.sandbox.type.ESupplierType Storage Type:OBJECT
  Name:number Type:java.lang.Integer Storage Type:OBJECT
```

All information per class (super, sub and embedded class), properties, indexes, ID's etc is displayed.


# Modifying existing classes

It is possible to modify existing classes in the Space. For example, we can add indexes during runtime.

Lets take our example **Bank**, we want to add an additional index for the columns **iban** :


{{%tabs%}}
{{%tab "Basic Index"%}}
```java
AsyncFuture<AddTypeIndexesResult> asyncAddIndex = gigaSpace.getTypeManager().asyncAddIndex("xap.sandbox.type.Bank",
				   SpaceIndexFactory.createPropertyIndex("iban", SpaceIndexType.BASIC));
```
{{%/tab%}}

{{%tab "Output"%}}

```bash
Meta Data :xap.sandbox.type.Bank
Super Type Name :xap.sandbox.type.Entity
Id Property Name:id
Indexes :{id=SpaceIndex[name=id, type=BASIC, unique=true], swift=SpaceIndex[name=swift, type=BASIC, unique=false], iban=SpaceIndex[name=iban, type=BASIC, unique=false]}
Properties:
 Name:contact Type:java.lang.String Storage Type:OBJECT
 Name:emailAddress Type:java.lang.String Storage Type:OBJECT
 Name:id Type:java.util.UUID Storage Type:OBJECT
 Name:currencyRates Type:java.util.Collection Storage Type:OBJECT
 Name:iban Type:java.lang.String Storage Type:OBJECT
 Name:swift Type:java.lang.String Storage Type:OBJECT
```
{{%/tab%}}
{{%/tabs%}}

It is also possible to add a **CompoundIndex**.  Lets take our **Supplier** class, we want to add a compound index for the columns **number** and **category** :

{{%tabs%}}
{{%tab "Compound Index"%}}
```java
AsyncFuture<AddTypeIndexesResult> indexesResultAsyncFuture = gigaSpace.getTypeManager()
			.asyncAddIndex("xap.sandbox.type.Supplier", new CompoundIndex(new String[] { "number", "category" }));
```
{{%/tab%}}

{{%tab "Output"%}}
```bash
Meta Data :xap.sandbox.type.Supplier
Super Type Name :xap.sandbox.type.Entity
Id Property Name:id
Indexes :{number+category=SpaceIndex[name=number+category, type=BASIC, unique=false], id=SpaceIndex[name=id, type=BASIC, unique=true]}
Properties:
 Name:contact Type:java.lang.String Storage Type:OBJECT
 Name:emailAddress Type:java.lang.String Storage Type:OBJECT
 Name:id Type:java.util.UUID Storage Type:OBJECT
 Name:category Type:xap.sandbox.type.ESupplierType Storage Type:OBJECT
 Name:number Type:java.lang.Integer Storage Type:OBJECT
```
{{%/tab%}}
{{%/tabs%}}

{{%note%}}
Removing an index or changing an index type is currently not supported.
{{%/note%}}



# Adding Space Document

New `SpaceDocuments` can be introduced during runtime:

{{%tabs%}}
{{%tab "Space Document"%}}
```java
// Create type descriptor:
SpaceTypeDescriptor typeDescriptor = new SpaceTypeDescriptorBuilder("Product")
		.addPropertyIndex("name", SpaceIndexType.BASIC)
// ... Other type settings
		.documentWrapperClass(Document.class).create();

gigaSpace.getTypeManager().registerTypeDescriptor(typeDescriptor);
```
{{%/tab%}}

{{%tab "Output"%}}
```bash
Meta Data :Product
Super Type Name :java.lang.Object
Id Property Name:_spaceId
Indexes :{name=SpaceIndex[name=name, type=BASIC, unique=false]}
Document Wrapper :class xap.sandbox.type.Document
Properties:
 Name:_spaceId Type:java.lang.Object Storage Type:OBJECT

Meta Data :java.lang.Object
Super Type Name :java.lang.Object
Id Property Name:null
Indexes :{}
Document Wrapper :class com.gigaspaces.document.SpaceDocument
Properties:
```
{{%/tab%}}
{{%/tabs%}}

{{%refer%}}
See [Space Document](./document-overview.html) for more information.
{{%/refer%}}
