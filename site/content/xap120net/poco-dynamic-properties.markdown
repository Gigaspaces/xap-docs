---
type: post120
title:  Dynamic Properties
categories: XAP120NET, PRM
parent: poco-overview.html
weight: 200
---



Object entries' properties are bound by the schema of the Object class, which means a property cannot be added without changing the class, and since changing the class requires restarting the space, this can be a long and tiresome project.

XAP provides the [Document API](./document-api.html), which is schema free, and thus enables users to add properties freely without worrying about schema changes. However, some users still prefer to continue working with standard objects (with fixed attributes) but would like to enjoy the benefits of dynamic properties.

Dynamic properties can be used with Object as well. This provides better interoperability between Object and document entries, and provides excellent schema evolution support without switching from Objects to documents.

{{%warning%}}
Only one property per class can be annotated with `[SpaceDynamicProperties]`.
{{%/warning%}}

# Enabling Dynamic Properties

To enable dynamic properties add a `Dictionary<String, Object>` property to the relevant class and decorate it with `[SpaceDynamicProperties]`. For example, the following **Person** class has two fixed properties (Name and Id), and an additional **ExtraInfo** property used to host the dynamic properties:


```csharp
public class Person
{
    public String Name { get; set; }
    [SpaceID]
    public int? Id { get; set; }
    [SpaceDynamicProperties]
    public DocumentProperties ExtraInfo { get; set; }
}
```

{{% info %}}
It is recommended to use the `DocumentProperties` class to host dynamic properties.
{{%/info%}}

# Using Dynamic Properties

To write an entry with dynamic properties, simply populate them in the dynamic properties property. For example:


```csharp
Person p = new Person();
p.Id = 7;
p.Name = "smith";
p.ExtraInfo = new DocumentProperties();
p.ExtraInfo["email"] = "smith@foo.com";
p.ExtraInfo["age"] = 30;
space.Write(p);
```

When the entry is read from the space the dynamic properties will be stored in the DocumentProperties reference annotated with `SpaceDynamicProperties`.

Dynamic properties can also be used for matching. For example, suppose we want to get all persons who are not minors (defined here as those less than twenty-one years of age) and email them something:


```csharp
Person[] people = space.ReadMultiple<Person>(new SqlQuery<Person>("age > 21"));
foreach (var person in people)
{
    String email = (String)person.ExtraInfo["email"];
    if (email != null)
        SendEmail(email);
}
```


{{%note "Note:"%}}

- The query expression refers to 'age', not 'ExtraInfo.age' - the space recognizes that the ExtraInfo property is decorated with \[SpaceDynamicProperties\] and treats the dynamic properties as if they were regular properties of the Person class.
- Since 'age' and 'email' are dynamic properties, there's no guarantee that they will exist in a given Person entry. The semantic for non-existent property is as if its value is null, which allows us to ignore it in the query expression and simply check for null before sending the email.
{{%/note%}}




# Indexing

Dynamic properties can be indexed similar to fixed properties.

{{%refer%}}
For more info see [Indexing](./indexing.html).
{{%/refer%}}