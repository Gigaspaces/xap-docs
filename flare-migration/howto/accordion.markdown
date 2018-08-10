---
type: post123
Description:
title: Accordion
menu: howto
weight: 100
---

# Accordion

{{%accordion %}}

{{%accord title="accord0"%}}
```java

private int age;
private String name;
 
```
{{%/accord%}}

{{%accord title="accord1"%}}
 
 fdslfsdkfldkfdsf
 dsfsdf
 sdfsdfdsf

{{%/accord%}}


{{%accord title="accordion3" %}}
 
 fdslfsdkfldkfdsf
 dsfsdf
 sdfsdfdsf

{{%/accord%}}

{{%/accordion%}}


# Accordion 2
 
{{%accordion test1%}}

{{%accord title="accordion1"  %}}
```java

private int age;
private String name;

```
{{%/accord%}}

{{%accord title="accordion2"  %}}

Some Text goes here

{{%/accord%}}


{{%accord title="accordion3"  %}}

 Some Text goes here

{{%/accord%}}

{{%/accordion%}}


# Complexe Accordion
{{%accordion%}}
{{%accord title="Example"  %}}


The following deploys a processing unit jar file named `data-processor.jar` using the `sync_replicated` cluster schema with 2 instances (`total_members`).

```bash
gs> deploy -cluster schema=sync_replicated total_members=2 data-processor.jar
```

The following deploys a processing unit archive called `data-processor.jar` using deployment properties file called `pu.properties`.

```bash
gs> deploy -properties file://config/pu.properties data-processor.jar

```

The following deploys a processing unit archive called `data-processor.jar` direct injecting the properties.

```bash
gs> deploy -properties embed://DB_username=postgres;DB_password=pass mirror
```

Using the following pu configuration:
```xml
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="org.postgresql.Driver"/>
    <property name="url" value="jdbc:postgresql:presence"/>
    <property name="username" value="${DB_username}"/>
    <property name="password" value="${DB_password}"/>
</bean>

```

The following deploys a processing unit archive called `data-processor.jar` using an SLA element read from an external `sla.xml` file.
```bash
gs> deploy -sla file://config/sla.xml data-processor.jar
```

The following example deploys a `partitioned-sync2backup` space cluster with the name `mySpace` for both the processing unit and the Space it contains.

```bash
deploy -cluster schema=partitioned-sync2backup total_members=2,1 -override-name mySpace -properties embed://dataGridName=mySpace myPUFolder
```



{{% tip %}}
Multiple deployment properties can be injected by having ; between each property - see below example:

```java
>gs deploy -cluster schema=partitioned-sync2backup total_members=10,1
-properties "embed://dataGridName=myIMDG;space-config.proxy.router.active-server-lookup-timeout=5000;space-config.engine.max_threads=256;mypropA=aaa;mypropB=bbb" -override-name myPU /tmp/myPu.jar
```
{{% /tip %}}



{{% warning %}}
Multiple deployment properties can be injected by having ; between each property - see below example:

```java
>gs deploy -cluster schema=partitioned-sync2backup total_members=10,1
-properties "embed://dataGridName=myIMDG;space-config.proxy.router.active-server-lookup-timeout=5000;space-config.engine.max_threads=256;mypropA=aaa;mypropB=bbb" -override-name myPU /tmp/myPu.jar
```
{{% /warning %}}

{{%/accord%}}
{{%/accordion%}}


# Accordion with Table

{{%accordion %}}

{{%accord title="accord0"%}}

Keyname       | Required | Type          | Description
-----------   | -------- | ----          | -----------
type          | yes      | string        | The [node type](dsl-spec-node-types.html) of this node template.
properties    | no       | dict          | The properties of the node template matching its node type properties schema.
instances     | no       | dict          | Instances configuration.
interfaces    | no       | interfaces    | Used for mapping plugins to [interfaces](dsl-spec-interfaces.html) operation or for specifying inputs for already mapped node type operations.
relationships | no       | relationships | Used for specifying the [relationships](dsl-spec-relationships.html) this node template has with other node templates.

{{%/accord%}}

{{%/accordion%}}



# Accordion with Tabs

{{%accordion%}}
{{%accord title="Tabs in an accordion"%}}
{{%tabs%}}
{{%tab table%}}

Keyname       | Required | Type          | Description
-----------   | -------- | ----          | -----------
type          | yes      | string        | The [node type](dsl-spec-node-types.html) of this node template.
properties    | no       | dict          | The properties of the node template matching its node type properties schema.
instances     | no       | dict          | Instances configuration.
interfaces    | no       | interfaces    | Used for mapping plugins to [interfaces](dsl-spec-interfaces.html) operation or for specifying inputs for already mapped node type operations.
relationships | no       | relationships | Used for specifying the [relationships](dsl-spec-relationships.html) this node template has with other node templates.

{{%/tab%}}

{{%tab table1%}}


{{% note %}}
```
Note that this example uses the basic authentication configuration but, Shiro has various authenticator types see [realm modules](http://shiro.apache.org/static/1.2.1/apidocs/org/apache/shiro/authc/class-use/AuthenticationException.html)
```
{{% /note %}}


{{%/tab%}}
{{%/tabs%}}

{{%/accord%}}

{{%/accordion%}}

