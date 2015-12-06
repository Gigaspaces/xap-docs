---
type: post110
title:  JUnit Testing
categories: XAP110
parent: installation.html
weight: 900
---

{{% ssummary %}} {{% /ssummary %}}

JUnit tests with XAP are very simple to setup.  When creating unit tests with XAP you will use an `Embedded Space` that is internally represented by the `IntegratedProcessingUnitContainer` class and a reference to this Space will be available through a class implementing the `GigaSpace` interface. This allows you to read / write objects to that Space just as you normally would in any XAP application.



# Simple test

Here is an example of a simple JUnit test:

{{%tabs%}}
{{%tab "Space class"%}}
 ```java
@SpaceClass
public class SpaceData {
	private UUID id;
	private Long category;
	private String name;

	@SpaceId
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	@SpaceRouting
	public Long getCategory() {
		return category;
	}
	public void setCategory(Long category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
```
{{%/tab%}}

{{%tab "Spring configuration"%}}
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:os-core="http://www.openspaces.org/schema/core" xmlns:os-jms="http://www.openspaces.org/schema/jms"
	xmlns:os-events="http://www.openspaces.org/schema/events"
	xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
	xmlns:os-sla="http://www.openspaces.org/schema/sla" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-{{%version spring%}}.xsd
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-{{%version spring%}}.xsd
	http://www.openspaces.org/schema/core http://www.openspaces.org/schema/{{%currentversion%}}/core/openspaces-core.xsd
	http://www.openspaces.org/schema/events http://www.openspaces.org/schema/{{%currentversion%}}/events/openspaces-events.xsd
	http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-{{%version spring%}}.xsd
    http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/{{%currentversion%}}/remoting/openspaces-remoting.xsd">

	<os-core:space id="aSpace" url="/./processingSpace" />

	<os-core:giga-space id="processingSpace" space="aSpace" />

</beans>
```
{{%/tab%}}
{{%/tabs%}}

And here is the JUnit test case

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/application-context.xml" })
public class TestXAP {

	@Autowired
	private GigaSpace processingSpace;

	@Test
	public void simpleUnitTest() {

		SpaceData data = new SpaceData();
		data.setId(UUID.randomUUID());
		data.setCategory(1L);
		data.setName("Test data");

		processingSpace.write(data);

		SQLQuery<SpaceData> query = new SQLQuery<SpaceData>(SpaceData.class, "");
		SpaceData[] result = processingSpace.readMultiple(query);

		Assert.assertEquals(result.length, 1);
	}

	@Before
	public void init() {
		processingSpace.clear(null);
	}
}
```

<br>

# Partition/Routing test

Testing partitioning and routing can be accomplished by manipulating the Space URL. You can start multiple partitions by defining in the Space URL the number of members and the `id` of the partition.

Example:

```java
"processorSpace?cluster_schema=partitioned&total_members=4&groups=junit&id=1"
```

In the example below 4 partitions are started by creating 4 `EmbeddedSpaceConfigurer` and then a fifth `SpaceProxyConfigurer` is constructed that points to the entire cluster.

```java
public class ClusterTest {

	private GigaSpace space;

	@Test
	public void testCluster() {

		for (int i = 0; i < 4; i++) {
			SpaceData data = new SpaceData();
			data.setId(UUID.randomUUID());
			data.setName("Space Data " + i);
			data.setCategory(new Long(i));

			space.write(data);
		}

		SQLQuery<SpaceData> query = new SQLQuery<SpaceData>(SpaceData.class, "");
        SpaceData[] result = space.readMultiple(query);

        Assert.assertEquals(result.length, 4);
	}

	@Before
	public void init() {

		new EmbeddedSpaceConfigurer("processorSpace?cluster_schema=partitioned&total_members=4&groups=junit&id=1")
		.lookupGroups("junit").space();

		new EmbeddedSpaceConfigurer("processorSpace?cluster_schema=partitioned&total_members=4&groups=junit&id=2")
		.lookupGroups("junit").space();

		new EmbeddedSpaceConfigurer("processorSpace?cluster_schema=partitioned&total_members=4&groups=junit&id=3")
		.lookupGroups("junit").space();

		new EmbeddedSpaceConfigurer("processorSpace?cluster_schema=partitioned&total_members=4&groups=junit&id=4")
		.lookupGroups("junit").space();


		space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("processorSpace").lookupGroups("junit")).gigaSpace();
	}
}
```

<br>

You can verify that the 4 partitions exists by starting the [Management Center]({{%currentadmurl%}}/gigaspaces-management-center.html)