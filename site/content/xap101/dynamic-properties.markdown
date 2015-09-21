---
type: post101
title:  Dynamic Properties
categories: XAP101
parent: pojo-overview.html
weight: 200
---

{{% ssummary %}} {{% /ssummary %}}


POJO entries properties are bound by the schema of the POJO class definition, which means a property cannot be added without changing the class, and since changing the class requires restarting the space this can be a long and tiresome project.

Starting with version 8.0, GigaSpaces provides the [Document API](./document-api.html), which is schema free, and enables users to add properties freely without worrying about the schema. However, some users still prefer to continue working with POJO but would like to enjoy the benefits of dynamic properties.

Starting with 8.0.1, dynamic properties can be used with POJOs as well. This provides better interoperability between POJO and document entries, and provides excellent schema evolution support without switching from POJO to document.

# Enabling

To enable dynamic properties, add a `Map<String, Object>` property to the relevant class and annotate it with `@SpaceDynamicProperties`. For example, the following **Person** class has two fixed properties (name and id), and an additional **extraInfo** property used to host the dynamic properties:


```java
public class Person {
    public Person (){}
    private String name;
    private Integer id;
    private DocumentProperties extraInfo;

    public String getName() {return name}
    public void setName(String name) {this.name=name}

    @SpaceId
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}

    @SpaceDynamicProperties
    public DocumentProperties getExtraInfo() {return extraInfo;}
    public void setExtraInfo(DocumentProperties extraInfo) {this.extraInfo=extraInfo;}
}
```

{{% note %}} It is recommended to use the `DocumentProperties` class to host dynamic properties, as it provides a [fluent interface](http://en.wikipedia.org/wiki/Fluent_interface) to set properties as well as better performance and memory footprint.{{%/note%}}

# Usage

To write an entry with dynamic properties, simply populate them in the dynamic properties property. For example:


```java
Person p = new Person();
p.setId(7);
p.setName("smith");
p.setExtraInfo(new DocumentProperties()
    .setProperty("email", "smith@foo.com")
    .setProperty("age", 30));
gigaSpace.write(p);
```

When the entry is read from the space, the dynamic properties will be populated.

You can have a getter and a setter for the newly added attributes with `@SpaceExclude` annotation on the getter field. This will instruct GigaSpaces to ignore this field when assembling the POJO field meta data:


```java
public class Person {
    public Person (){}
    private String name;
    private Integer id;
    private DocumentProperties extraInfo;

    public String getName() {return name}
    public void setName(String name) {this.name=name}

    @SpaceId
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}

    @SpaceDynamicProperties
    public DocumentProperties getExtraInfo() {return extraInfo;}
    public void setExtraInfo(DocumentProperties extraInfo) {this.extraInfo=extraInfo;}

    @SpaceExclude
    public String getEmail() {return (String)extraInfo.getProperty("email")}
    public void setEmail(String email) {extraInfo.setProperty("email",email)}

    @SpaceExclude
    public Integer getAge() {return (Integer.valueOf(extraInfo.getProperty("age")))}
    public void setAge(String age) {extraInfo.setProperty("age",age)}

}
```

{{%warning%}}
Only one property per class can be annotated with `@SpaceDynamicProperties`.
{{%/warning%}}


Dynamic properties can also be used for matching. For example, suppose we want to get all persons who are not minors (age greater than 21) and email them something:


```java
Person[] people = gigaSpace.readMultiple(new SQLQuery<Person>(
    Person.class, "age > 21"), Integer.MAX_VALUE);
for (Person person : people) {
    String email = person.getExtraInfo().getProperty("email");
    if (email != null)
        sendEmail(email);
}
```

{{%note%}}
- The query expression refers to 'age', not 'extraInfo.age' - the space recognizes that the extraInfo property is annotated with @SpaceDynamicProperties and treats the dynamic properties as if they were regular properties of the Person class.
- Since 'age' and 'email' are dynamic properties, there's no guarantee they'll exist in each Person entry. The semantic for non-existent property is as if its value is null, which allows us to ignore it in the query expression and simply check for null before sending the email.
- Dynamic properties can be indexed similar to fixed properties.
{{%/note%}}

{{%refer%}}
For more info see [Indexing](./indexing.html).
{{%/refer%}}
