---
type: post97
title:  Custom Serialization
categories: XAP97
parent: modeling-your-data.html
weight: 550
---



To solve the performance problems associated with class serialization, the serialization mechanism allows you to declare an embedded class is Externalizable. When the ObjectOutputStream writeObject() method is called, it performs the following sequence of actions:

- Tests to see if the object is an instance of Externalizable. If so, it uses externalization to marshall the object.
- If the object isn't an instance of Externalizable, it tests to see whether the object is an instance of Serializable. If so, it uses serialization to marshall the object. If neither of these two cases apply, an exception is thrown.

Externalizable is an interface that consists of two methods:


```java
public void readExternal(ObjectInput in);
public void writeExternal(ObjectOutput out);
```

The Externalization mechanism writes out the identity of the class (which boils down to the name of the class and the appropriate serialVersionUID). It also stores the superclass structure and all the information about the class hierarchy. But instead of visiting each superclass and using it to store some of the state information, it simply calls writeExternal() on the local class definition.
The Externalization mechanism stores all the metadata, but writes out only the local instance information.

When a POJO class with embedded properties uses the Space API, you may implement the `Externalizable` mechanism for the embedded property. This can be done to control serialization and deserialization when the Object is sent to the space (e.g. write, update and execute Operations) and when it is sent back to the client (e.g. read and take operations). This will optimize the remote call when using Remote Space configuration for single, partitioned, and replicated space topologies.

![storage-type-object.jpg](/attachment_files/serialization2.jpg)

<br>

Here is an example; the Person class has an Address property that is being externalized.




```java
@SpaceClass
public class Person {

	private Long id;

	private String lastName;

	private String firstName;

	private Address address;

	@SpaceId(autoGenerate=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
```



```java
public class Address implements Externalizable {

	private String street;

	private String city;

	private String country;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {

		street = (String) in.readObject();
		city = (String) in.readObject();
		country = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		out.writeObject(street);
		out.writeObject(city);
		out.writeObject(country);
	}
}
```

