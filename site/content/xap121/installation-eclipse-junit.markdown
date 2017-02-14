---
type: post121
title:  JUnit Testing
categories: XAP121
parent: installation-eclipse-overview.html
weight: 300
---

{{% ssummary %}} {{% /ssummary %}}

When creating unit tests with XAP you will use an `Embedded Space` that is internally represented by the `IntegratedProcessingUnitContainer` class and a reference to this Space will be available through a class implementing the `GigaSpace` interface. This allows you to read / write objects to that Space just as you normally would in any XAP application.



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

Testing partitioning and routing can be accomplished by using the `IntegratedProcessingUnitContainerProvider`. You can start multiple partitions by defining with the `ClusterInfo` the number of members of a cluster.



In the example below 4 partitions are started.

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
            IntegratedProcessingUnitContainerProvider provider = new IntegratedProcessingUnitContainerProvider();
            provider.addConfigLocation("classpath:META-INF/application-context.xml");
            ClusterInfo clusterInfo = new ClusterInfo();
            clusterInfo.setSchema("partitioned");
            clusterInfo.setNumberOfInstances(4);
            provider.setClusterInfo(clusterInfo);

            ProcessingUnitContainer c = provider.createContainer();

            space = new GigaSpaceConfigurer(new SpaceProxyConfigurer("processingSpace")).gigaSpace();
	}
}
```

<br>

You can verify that the 4 partitions exists by starting the [Management Center]({{%currentadmurl%}}/gigaspaces-management-center.html)


#  Lookup Service

The StandaloneProcessingUnitContainer automatically starts an embedded Lookup service. If you intend to use a separate Lookup service you can disable the embedded Lookup service by passing the setting the `com.j_spaces.core.container.directory_services.jini_lus.enabled` system property to false. This property can also be set within the Space definition:

```xml
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
      <props>
        <prop key="com.j_spaces.core.container.directory_services.jini_lus.start-embedded-lus">false</prop>
      </props>
    </os-core:properties>
</os-core:embedded-space>
```