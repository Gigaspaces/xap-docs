---
type: post97net
title:  User-Defined Objects
categories: XAP97NET
parent: interoperability.html
weight: 200
---

{{% ssummary %}} {{% /ssummary %}}



A deep class is defined as a class in which, at least one of its fields is a user defined class.

{{% vbar %}}
Creating a deep class to be interoperability ready, requires defining all its deep fields as fields that will be stored to the space, using its matching Java objects types. Use the property, `StorageType = StorageType.Object` on the `SpaceProperty()` attribute, defined to all the fields that are user defined objects.

Class name mapping: when defining interoperability ready classes, class names of the corresponding .NET and Java classes have to match exactly. In order to keep the .NET and Java namespace style conventions, and still create matching classes, we use the `AliasName` property of the `SpaceClass()` attribute to map the .NET class name and namespace to the matching Java class name and namespace.

Properties mapping: when defining interoperability of properties the names of the properties of the Java and .NET classes have to match exactly. In order to keep .NET and Java coding conventions and still create matching classes we use `AliasName` property of the `SpaceProperty()` attribute to map properties between .NET and Java.
{{%/vbar%}}

# Designing Interoperable Classes

For the purpose of explaining the subject we'll look at a Person class (a deep class)


| C# | Java |
|----------|-------------|
|`using GigaSpaces.Core.Metadata;`<br/><br/>`namespace MyCompany.MyProject.Entities`<br/>`{`<br/>`    [SpaceClass(AliasName = "com.mycompany.myproject.entities.Person")]`<br/>`    public class Person`<br/>`    {`<br/>`        private string _name;`<br/>`        private Address _address;`<br/><br/>`        [SpaceProperty(AliasName = "name")]`<br/>`        public string Name`<br/>`        {`<br/>`            get { return _name; }`<br/>`            set { _name = value; }`<br/>`        }`<br/><br/>`        [SpaceProperty(AliasName = "address", StorageType = StorageType.Object)]`<br/>`        public Address Address`<br/>`        {`<br/>`            get { return _address; }`<br/>`            set { _address = value; }`<br/>`        }`<br/>`    }`<br/><br/>`    [SpaceClass(AliasName = "com.mycompany.myproject.entities.Address")]`<br/>`    public class Address`<br/>`    {`<br/>`        [SpaceProperty(AliasName = "houseNumber")]`<br/>`        public int HouseNumber;`<br/><br/>`        [SpaceProperty(AliasName = "street")]`<br/>`        public string Street;`<br/><br/>`        [SpaceProperty(AliasName = "country")]`<br/>`        public string Country;`<br/>`    }`<br/>`}`|`package com.mycompany.myproject.entities;`<br/><br/>`public class Person`<br/>`{`<br/>`    private String _name;`<br/>`    public String getName() { return _name; }`<br/>`    public void setName(String value) { _name = value; }`<br/><br/>`    private Address _address;`<br/>`    public Address getAddress() { return _address; }`<br/>`    public void setAddress(Address value) { _address = value; }`<br/>`}`<br/><br/>`package com.mycompany.myproject.entities;`<br/><br/>`public class Address`<br/>`{`<br/>`    public String HouseNumber;`<br/>`    public String Street;`<br/>`    public String Country;`<br/>`}`|
