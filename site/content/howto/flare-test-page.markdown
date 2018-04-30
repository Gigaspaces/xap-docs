---
type: post
title:  Flare Test Page
categories: howto
weight: 50
---

 Test page for Flare migration


# Heading 1

## Heading 2

### Heading 3

#### Heading 4

##### Heading 5


# Text

## Formatting

This is an *emphasized* text. 

This is a **bold** text. 

This is a `monospaced` text.
 

## Lists

* Unordered list item
* Unordered list item
* Unordered list item


1. Ordered list item
1. Ordered list item
1. Ordered list item
1. Ordered list item



## Spans


This text has {{%color blue%}}blue{{%/color%}}   
This text has {{% bgcolor orange %}}orange background{{% /bgcolor %}}

# Links

## Macros

| **Description** | **Macro** | **Output** |
|-----------------|-----------|------------|
| Latest XAP Release file name |  {{%/*version "build-filename"*/%}}    | {{%version build-filename %}} |
| Latest XAP Release home dir |   {{%/*version "gshome-directory"*/%}}    | {{%version gshome-directory %}} |
| Latest Default Lookup Group |   {{%/*version "default-lookup-group"*/%}}    | {{%version default-lookup-group %}} |
| Latest XAP Maven |  {{%/*version "maven-version"*/%}} {{%version maven-version %}}   |{{%version maven-version %}} |


## JAR versions

| **Description** | **Macro** | **Output** |
|-----------------|-----------|------------|
| spring  |  {{%/*version "spring"*/%}}   |{{%version spring %}} |
| antlr4-runtime  |   {{%/*version "antlr4-runtime"*/%}}  |{{%version antlr4-runtime %}} |
| mongo-java-driver  |   {{%/*version "mongo-java-driver"*/%}}  |{{%version mongo-java-driver %}} |
| mongo-datasource  |   {{%/*version "mongo-datasource"*/%}}   |{{%version mongo-datasource %}} |
| openjpa  |   {{%/*version "openjpa"*/%}}     |{{%version openjpa %}} |


## Anchor

Navigate to the anchor [point](#hello)

Should inherently convert to html links.

## Cross References

| **Description** | **Markdown** | **Output** |
|-----------------|--------------|------------|
| Link to external site | \[GigaSpaces\]\(http://www.gigaspaces.com\) | [GigaSpaces](http://www.gigaspaces.com) |
| Link to page in same folder | \[Anchor\](./anchor.html\) | [Anchor](./anchor.html) |
| Link to page in `xap` folder | \[SQL Query\]({{%/*currentjavaurl*/%}}/sqlquery.html) | [SQL Query]({{%currentjavaurl%}}/query-sql.html) |
| Link to page in `xapnet` folder | \[SQL Query\]({{%/*currentneturl*/%}}/sqlquery.html\) | [SQL Query]({{%currentneturl%}}/query-sql.html) |
| Link to page in latest `xap` folder | \[SQL Query\]({{%/*latestjavaurl*/%}}/sqlquery.html\) | [SQL Query]({{%latestjavaurl%}}/query-sql.html) |
| Link to page in latest `xapnet` folder | \[SQL Query\]({{%/*latestneturl*/%}}/sqlquery.html\) | [SQL Query]({{%latestneturl%}}/query-sql.html) |
| Link to latest Java Tutorial page | \[Getting started\]({{%/*lateststartedurl*/%}}) | [Getting Started]({{%lateststartedurl%}}) |
| Link to latest .NET Tutorial page | \[Getting started\]({{%/*latestnettuturl*/%}}) | [Getting Started]({{%latestnettuturl%}}) |
| Link to latest Admin page | \[Administration\]({{%/*latestadmurl*/%}}) | [Admin]({{%latestadmurl%}}) |


# Icons


{{%infosign%}} This is an Info icon. 

{{%exclamation%}} This is an Exclamation icon. 

{{%zip%}}  This is a zip icon. Should appear as regular link to a file, w/o image. For example, Download the example {{%zip "/attachment_files/sbp/PollingEvictor.zip"%}} that uses a polling container for eviction of Orders.


# Layouts

## Table


| Header1 | Header2 | Header3 |
|:--------|:--------|:--------|
| column1 | column2 | column3 |


## Tabbed Pane

{{%tabs%}}
{{%tab "Foo  This is tab Foo"%}}

Contents of the first tab.

{{%/tab%}}
{{%tab "Bar  This is tab Bar"%}}

Contents of the second tab.

{{%/tab%}}
{{% /tabs %}}



## Accordion

{{%accordion%}}

{{%accord title="accord0" %}}
```java
private int age;
private String name;
```
{{% /accord %}}

{{%accord title="accord1" %}}
 Some Text
{{% /accord %}}

{{% accord title="accordion3"%}}
More Text
{{% /accord%}}

{{% /accordion%}}


## Section with Two Columns

{{%section%}}
{{%column width="20%" %}}
![fifo-groups.png](/attachment_files/subject/index.png)
{{%/column%}}

{{%column width="80%" %}}
 The polling event container is an implementation of the polling consumer pattern which uses the Space to receive events.
 A polling event operation is mainly used when simulating Queue semantics or when using the master-worker design pattern.
{{%/column%}}
{{%/section%}}


# Image Popup - Convert to Thumbnail

{{%popup "/attachment_files/qsg/class_diagram.png"  %}}


# Youtube Video

{{%youtube "V7rbbmWo3JU"  "Multi Site Deployment" %}}

Video links should be hidden.

# Panels


Warning   {{%warning "Warning"%}}... {{%/warning%}}
 
Note  {{%note "Note"%}}... {{%/note%}}

Tip  {{%tip "Tip"%}}... {{%/tip%}}


## Landing page panel

{{%fpanel%}}
- [Overview](/xap/11.0/dev-java/change-api.html)<br>
Change API overview.

{{%/fpanel%}}

Convert to Flare mini-TOCs


# Links

Testing docs link [Event Processing](http://www.mulesoft.org) to see how this works.<br>

Testing external link {{%exurl "Mule Site" "http://www.mulesoft.org/" %}} to see how this works.<br>

# Code Samples

## SQL

```sql
create table users(
    username varchar_ignorecase(50) not null primary key,
    password varchar_ignorecase(50) not null,
    enabled boolean not null);

create table authorities (
    username varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username));
    create unique index ix_auth_username on authorities (username,authority);

---
Group Authorities (if enabled)

create table groups (
    id bigint generated by default as identity(start with 0) primary key,
    group_name varchar_ignorecase(50) not null);

create table group_authorities (
    group_id bigint not null,
    authority varchar(50) not null,
    constraint fk_group_authorities_group foreign key(group_id) references groups(id));

create table group_members (
    id bigint generated by default as identity(start with 0) primary key,
    username varchar(50) not null,
    group_id bigint not null,
    constraint fk_group_members_group foreign key(group_id) references groups(id));
```

## Java

```java

public void pu() {
	Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
	// wait till things get discovered (you can also use specific waitFor)
	for (ProcessingUnit processingUnit : admin.getProcessingUnits()) {
		System.out.println("Processing Unit: " + processingUnit.getName()
					+ " status: " + processingUnit.getStatus());
		if (processingUnit.isManaged()) {
				System.out.println("   -> Managing GSM: "
						+ processingUnit.getManagingGridServiceManager()
								.getUid());
		} else {
				System.out.println("   -> Managing GSM: NA");
		}

		for (GridServiceManager backupGSM : processingUnit
					.getBackupGridServiceManagers()) {
				System.out.println("   -> Backup GSM: " + backupGSM.getUid());
		}

		for (ProcessingUnitInstance processingUnitInstance : processingUnit) {
				System.out.println("   ["
						+ processingUnitInstance.getClusterInfo()
						+ "] on GSC ["
						+ processingUnitInstance.getGridServiceContainer()
								.getUid() + "]");
				if (processingUnitInstance.isEmbeddedSpaces()) {
					System.out.println("      -> Embedded Space ["
							+ processingUnitInstance.getSpaceInstance()
									.getUid() + "]");
				}

				Map<String, ServiceDetails> services = processingUnitInstance.getServiceDetailsByServiceId();

				for (ServiceDetails details : services.values()) {
					System.out.println("      -> Service " + details);
				}
		}
	}
}
```

## XML

```xml
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
        <props>
            <prop key="cluster-config.groups.group.async-replication.repl-chunk-size">1000</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

```xml
<os-core:embedded-space id="space" space-name="mySpace">
    <os-core:properties>
        <props>
            <prop key="cluster-config.groups.group.repl-policy.replication-mode">async</prop>
        </props>
    </os-core:properties>
</os-core:embedded-space>
```

## Bash/Shell


```bash
-Dcom.gs.lrmi.filter.factory=com.gigaspaces.lrmi.nio.filters.SSLFilterFactory
-Dcom.gs.lrmi.filter.security.keystore=keystore.ks
-Dcom.gs.lrmi.filter.security.password=password
```

```bash
2018-04-15 09:45:54,832 [main] INFO  - Initializing connection to space jini://*/*/space
2018-04-15 09:45:54,872 [main] INFO  - Connection to space has been initialized
2018-04-15 09:45:54,902 [main] DEBUG - Creating prepared statement for query: SELECT e.firstName, e.age FROM Person e WHERE e.age = ?
2018-04-15 09:45:55,439 [main] DEBUG - Looking for XAP tables ...
2018-04-15 09:45:55,464 [main] DEBUG - Found registered types in the space [java.lang.Object, Person]
2018-04-15 09:45:55,465 [main] TRACE - Found [2] space types
2018-04-15 09:45:55,465 [main] TRACE - Registering table: Person
2018-04-15 09:45:55,466 [main] DEBUG - Looking for 'Person' table row type
2018-04-15 09:45:55,493 [main] DEBUG - 'Person' table row type is RecordType(JavaType(class java.lang.Integer) age, VARCHAR(65535) firstName, JavaType(class java.util.UUID) id)
2018-04-15 09:45:55,493 [main] DEBUG - 'Person' table routing field is 'id'
2018-04-15 09:45:56,150 [main] DEBUG - Executing XAP query: SELECT * FROM Person WHERE age =  ?  Projection: [firstName, age] Parameters: [20]
| John  | 20  |
| Eric  | 20  |
```

## C Sharp (.NET)


```csharp
void MyEventHandler(object sender, BatchDataEventArgs<Data> e)
{
    Data[] batch = e.Data;
    // handle event
}
```

```csharp
public class SimpleListener
{
    ...

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```