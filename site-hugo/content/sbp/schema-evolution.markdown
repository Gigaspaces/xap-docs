---
type: post
title:  Schema Evolution
categories: SBP
parent: data-access-patterns.html
weight: 1500
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim| 7.1 | April 2000|    |    |



# Overview

Developing software is a never ending process. There is never a final version or a last release. This means you may need to deploy new versions of the application, where the space classes model have been modified to accommodate changes required by the updates business logic of the application or changes required by the database schema. In many cases, the changes involves new space classes, or changing the model of existing space classes -- removing some fields or changing the inheritance structure.

GigaSpaces supports addition of new space classes while the system is running out of the box. New classes can be added without interrupting the space operation. New fields can be added using different techniques.

The following sections describe the recommended options to evolve the space schema.

# Schema-less Model - Document Model
A `SpaceDocument` is completely dynamic model artifact you can write into the space and read back into the application. It allows you to change or evolve your data model without ever taking down the Space. Change your application code to add additional properties or remove existing ones, and you're good to go. In addition, **old and new versions of the same Entity model can co-exist** since the space does not enforce any restriction with regards to the property set of `SpaceDocuments` belong to a certain type. `SpaceDocument` can be [mapped to a POJO]({{%latestjavaurl%}}/document-pojo-interoperability.html) during runtime allowing the application leverage POJO and evolve these as needed. You can also make the `SpaceDocument` type safe by [Extending SpaceDocument]({{%latestjavaurl%}}/document-extending.html).

![document_arch.jpg](/attachment_files/sbp/document_arch.jpg)

See the [Document Support]({{%latestjavaurl%}}/document-api.html) for details.

# Evolving Space Schema using a Map Field

With this approach you should have a "static portion" and a "dynamic portion" to the space object. The static portion will be using regular POJO primitive fields and the dynamic portion will be using a Map data type. The map keys would be indexed to allow you to query these "dynamic fields" once used. See:


```java
See example below:
public class MyClass {
	public MyClass (){}

	// static portion
	Integer num;
	String str;
	Integer id;

	// dynamic fields portion
	HashMap<String, MyData> dynamicData;

	@SpaceIndexes({@SpaceIndex(path="key1" , type = SpaceIndexType.BASIC),
			@SpaceIndex(path="key2" , type = SpaceIndexType.BASIC)})
	public HashMap<String, MyData> getMap() {
		return dynamicData;
	}
	public void setMap(HashMap<String, MyData> dynamicData) {
		this.dynamicData = dynamicData;
	}

	// getter and setter method for static portion fields
....

}
```

See more:

- [Matching Nested Maps]({{%latestjavaurl%}}/query-sql.html#MatchingNestedMaps)
- [Nested Object Indexing]({{%latestjavaurl%}}/indexing.html#Nestedpropertiesindexing)

# Evolving Space Schema using Placeholder Fields

The space model can be evolved using a few extra fields as part of the space class, which are used in future versions of the application. When using POJOs/PONOs/POCOs, these fields include getter/setter fields that include relevant facade methods once the actual business logic requests these. When dealing with Entry space classes, you can change the fields getter/setter methods. This is because these methods are not used when getting the class metadata (when the class is introduced to the space).

{{% exclamation %}} In most cases, the placeholder fields technique is the simplest one to use. It allows you to index future fields and its impact on the deployment is minimal.

# Evolving Space Schema using Externalizable Implementation

This approach is the class serialization and de-serialization methods as transformation methods. This allows you to deploy version 1 and version 2 of the class simultaneously across different application instances. The space must have version 2 as part of its classpath. This option requires the space to be restarted with the new version of the classes. The `Externalizable` implementation needs to cope with removal of fields (in this case the data stream might contain additional data which must be skipped over), and with fields being added (in this case the data stream might not contain sufficient data, so the fields must be initialized properly).

# Evolving Space Schema using Inheritance

You can evolve the space schema by downloading a codebase and using inheritance -- by adding a new Entry class type to your application. This class can extend an existing space Entry class. Clients are able to work with existing Entries, as well as the newly added Entry types.

## Example

1. Class A is written to a space by client X.
2. Class A is taken or read from the space by client Y, using a template based on A.
3. At some point, an A' class is created, which extends A.
4. Entries from type A' are written to the space by client Z.
5. Class A' metadata is acquired by the space server via a codebase from client Z.
6. Client X now receives Entries from type A', and A' metadata is acquired by client X from the client Z codebase.
7. Client X continues to typecast A' to A, and to work with Entries of type A', as if they were of type A.

The system does not have to shut down during the process and clients are not interrupted.

# Evolving Space Schema using Data Migration Tool

With this pattern, a simple program should be created in order to transform a space Entry from one schema to another.

The transformer application reads an Entry from the source space (the old class schema that was used), transforms it to its new schema, and writes it to a target space (the new class schema) that is used with the new version of the application.

As seen in the code example below, every migrated Entry preserves its Entry UID. The Entries are read from the source space using the `GSIterator`, and are written into the target space using transactions and the `writeMultiple` operation.

Below is a transformer program, that transforms a class with two attributes into a class with one attribute. To speed up the transformation process, you may implement the transformation process as `IWroker`, by running the transformation business logic in the source space or in the target space.

The migration process can also be optimized by running multiple threads that handle the migration of different space classes in parallel.


## Schema Evolution Example


```java
package com.j_spaces.transformer;

import java.util.ArrayList;
import java.util.Collection;
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.ExternalEntry;
import com.j_spaces.core.client.GSIterator;
import com.j_spaces.core.client.LocalTransactionManager;
import com.j_spaces.core.client.SpaceFinder;

public class Transformer {
	public Transformer() {
	}

	public IJSpace sourceSpace;

	public IJSpace targetSpace;

	static String namesOld[] = { "attrib1", "attrib2" }, typesOld[] = {
			String.class.getName(), String.class.getName() };

	static String namesNew[] = { "attrib1" }, typesNew[] = { String.class
			.getName() };

	static String className = "com.j_spaces.test.MyClass";

	public static void main(String[] args) {
		Transformer transformer = new Transformer();

		try {
			transformer.sourceSpace = (IJSpace) SpaceFinder
					.find("rmi://localhost/./sp1");
			transformer.targetSpace = (IJSpace) SpaceFinder
					.find("rmi://localhost/./sp2");

			ExternalEntry template1 = new ExternalEntry(className, null,
					namesOld, typesOld);
			ExternalEntry template2 = new ExternalEntry(className, null,
					namesNew, typesNew);
			transformer.sourceSpace.snapshot(template1);
			transformer.targetSpace.snapshot(template2);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		transformer.tranform();
		System.out.println("Transformation done!");
	}

	void tranform() {
		Collection templates = new ArrayList();
		try {
			ExternalEntry template = new ExternalEntry(className, null,
					namesOld, typesOld);
			templates.add(template);
			LocalTransactionManager transactionManager = (LocalTransactionManager) LocalTransactionManager
					.getInstance(targetSpace);
			GSIterator sourceIterator = new GSIterator(sourceSpace, templates,
					100, true, Lease.FOREVER);
			int count = 0;
			Transaction.Created tCreated = null;
			Transaction txn = null;
			ArrayList<Entry> entries = new ArrayList<Entry>();
			while (sourceIterator.hasNext()) {
				ExternalEntry entry = (ExternalEntry) sourceIterator.next();
				Object newValues[] = new Object[Look And Feel - ServiceGrid];
				newValues[0] = entry.getFieldValue(0);
				ExternalEntry newentry = new ExternalEntry(className,
						newValues, namesNew, typesNew);
				newentry.m_UID = entry.m_UID;
				count++;
				entries.add(newentry);
				if (count == 100) {
					while (true)
						try {
							// startTX
							tCreated = TransactionFactory.create(
									transactionManager, 1000);
							txn = tCreated.transaction;
							Entry[] ents = new Entry[entries.size()];
							entries.toArray(ents);
							targetSpace.writeMultiple(ents, txn, Lease.FOREVER);
							txn.commit();
							System.out.println("Done writeMultiple!");
							count = 0;
							entries.clear();
							break;
						} catch (Exception e) {
							txn.abort();
							e.printStackTrace();
							System.out.println("try again....");
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```
