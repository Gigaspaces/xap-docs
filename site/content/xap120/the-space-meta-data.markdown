---
type: post120
title:  Data Type Meta Data
categories: XAP120
weight: 1100
parent: the-gigaspace-interface-overview.html
---




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

Run the program :

```java
public class TestSpaceMetaData {

	@Test
	public void test() throws InterruptedException {
		String spaceName = "mySpace";

		GigaSpace gigaSpaceEmb = new GigaSpaceConfigurer(new EmbeddedSpaceConfigurer(spaceName).lookupGroups("TEST"))
				.gigaSpace();

		System.out.println(
				"This test will write 2 space objects into the space , each using different classes and will use the SpaceTypeDescriptor to retrieve the space class meta data back");

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

		Bank b = new Bank();
		b.setId(UUID.randomUUID());
		gigaSpace.write(b);
		Supplier s = new Supplier();
		s.setId(UUID.randomUUID());
		gigaSpace.write(s);

		String SpaceClassNames[] = space.getRuntimeDetails().getClassNames();
		GigaSpaceTypeManager gigaSPaceTypeManager = gigaSpace.getTypeManager();

		for (int j = 0; j < SpaceClassNames.length; j++) {
			String spaceClass = SpaceClassNames[j];
			System.out.println();
			System.out.println("Meta Data       :" + spaceClass);
			SpaceTypeDescriptor typeManager = gigaSPaceTypeManager.getTypeDescriptor(spaceClass);
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
}
```

will produce the following output:

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
