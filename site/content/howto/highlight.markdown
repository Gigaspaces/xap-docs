---

Description:
title: Highlight
type: post
weight: 500
---
 





# Code Java Highlight


```java
// Read an entry of type MyClass whose num property is greater than 500:
MyClass result1 = gigaSpace.read(new SQLQuery<MyClass>(MyClass.class, "num > 500"));

// Take an entry of type MyClass whose num property is less than 500:
MyClass result2 = gigaSpace.take(new SQLQuery<MyClass>(MyClass.class, "num < 500"));

MyClass[] results;
// Read all entries of type MyClass whose num is between 1 and 100:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num >= 1 AND num <= 100"));

// Read all entries of type MyClass who num is between 1 and 100 using BETWEEN syntax:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num BETWEEN 1 AND 100"));

// Read all entries of type MyClass whose num is either 1, 2, or 3:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num IN (1,2,3)"));

// Read all entries of type MyClass whose num is greater than 1,
// and order the results by the name property:
results = gigaSpace.readMultiple(new SQLQuery<MyClass>(MyClass.class, "num > 1 ORDER BY name"));
```

# Csharp

```c#
namespace document
{
	public class Program
	{
		public Program ()
		{
			// Create the Space
			ISpaceProxy spaceProxy = new EmbeddedSpaceFactory ("mySpace").Create ();

			registerDocument (spaceProxy);

			PersonDoc doc1 = new PersonDoc ();
			doc1 [PersonDoc.PropertyTypeId] = "1";
			doc1 [PersonDoc.PropertyFirstName] = "John";
			doc1 [PersonDoc.PropertyLastName] = "Fellner";

			spaceProxy.Write (doc1);

			EmployeeDoc doc2 = new EmployeeDoc ();
			doc2 [PersonDoc.PropertyTypeId] = "2";
			doc2 [PersonDoc.PropertyFirstName] = "John";
			doc2 [PersonDoc.PropertyLastName] = "Walters";
			doc2 [EmployeeDoc.PropertyEmployeeNumber] = "1234";

			spaceProxy.Write (doc2);

			SqlQuery<PersonDoc> query1 = new SqlQuery<PersonDoc> (
				                             PersonDoc.DocName, "");

			PersonDoc[] result1 = spaceProxy.ReadMultiple<PersonDoc> (query1);

			// You should see two objects
			Console.WriteLine (result1.Length);

			SqlQuery<EmployeeDoc> query2 = new SqlQuery<EmployeeDoc> (
				                               EmployeeDoc.DocName, "");

			EmployeeDoc[] result2 = spaceProxy.ReadMultiple<EmployeeDoc> (query2);

			// You should see one object
			Console.WriteLine (result2.Length);
		}
}
```


# Code XML Highlight

```
<beans>
    <os-core:embedded-space id="space" name="mySpace">
        <os-core:properties>
            <props>
                <prop key="space-config.QueryProcessor.date_format">yyyy-MM-dd HH:mm:ss</prop>
                <prop key="space-config.QueryProcessor.time_format">HH:mm:ss</prop>
            </props>
        </os-core:properties>
    </os-core:embedded-space>
</beans>

<import resource="classpath*:/applicationContext-component.xml" />
<import resource="classpath*:/applicationContext-component.xml" />
<import resource="classpath*:/applicationContext-component.xml" />
```


# Property

```property
introscope.epagent.config.networkDataPort=8003
```

# YAML

```yaml
RESOURCE_SEGMENT_1|...|RESOURCE_SEGMENT_N:METRIC_NAME
```

# Bash

```bash
>cd /root
>chmod +x hello.*

```

# Sql
```sql
SELECT * FROM PERSON;

```