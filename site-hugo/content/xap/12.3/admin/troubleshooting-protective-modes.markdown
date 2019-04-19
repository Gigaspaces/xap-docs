---
type: post123
title:  Protective Modes
categories: XAP123ADM, PRM
weight: 150
canonical: auto
parent: troubleshooting.html
---


The following guidelines are highly recommended to build robust and efficient applications as well as to avoid common mistakes. XAP was designed to be robust and to provide clear exceptions when the usage is wrong. Sometimes a plain validation is too harsh, as it might break backward-compatibility and prevent existing users from upgrading to the latest version. 

For such cases the **Protective Mode** was introduced. The validation is on by default, but can be disabled using a system property. This protects new users from repeating old mistakes, and encourages existing users to fix their code (yet allows them to disable the protection if they choose so).

## Disabling the Protective Mode
To disable a protective mode you will need to use the relevant system property. See below example how this can be applied:

{{% accordion   %}}
{{% accord title="Java"%}}
**Linux:**

```bash
export XAP_GSC_OPTIONS=-Dcom.gs.protectiveMode.typeWithoutId=false -Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false -Dcom.gs.protectiveMode.primitiveWithoutNullValue=false
```

**Windows:**

```bash
set XAP_GSC_OPTIONS=-Dcom.gs.protectiveMode.typeWithoutId=false -Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false -Dcom.gs.protectiveMode.primitiveWithoutNullValue=false
```
{{%/accord%}}
{{% accord title=".NET"%}}
The `ServiceGrid.config` should have the following

```bash
<JvmSettings>
   <JvmCustomOptions>
      <add Option="-Dcom.gs.protectiveMode.typeWithoutId=false"/>
      <add Option="-Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false"/>
      <add Option="-Dcom.gs.protectiveMode.primitiveWithoutNullValue=false"/>
   </JvmCustomOptions>
</JvmSettings>
```
{{%/accord%}}
{{%/accordion%}}

# SpaceId property Decoration Validation

The `SpaceId` property is essential for update operations. XAP also has a rich set of operations that use the `SpaceId` value to perform read/take/update very efficiently without fetching the entire object (aka `Change API`).  Since XAP 9.1 the following is enforced by the protective mode:

Writing an entry to the space without a space ID [Java version](../dev-java/query-by-id.html) \|[ .NET version](../dev-dotnet/query-by-id.html) is error-prone - it can lead to duplicate entries, bad performance and more. In case your application contains objects without an `SpaceId` value you'll get the following exception:


```bash
com.gigaspaces.client.protective.ProtectiveModeException: Cannot introduce a type named 'MyClass' without an id property defined...
```

{{% note "Note"%}}
It is highly recommended that you modify them and add a `SpaceId` decoration. If this is not feasible, it can be disabled using the following system property:

```bash
-Dcom.gs.protectiveMode.typeWithoutId=false
```
{{% /note %}}


# Space Routing Property Validation

The `Space Routing` property is used to partition the data across different partitions. It is recommended to define such property explicitly to control how data is partitioned and avoid common mistakes like writing data to the wrong partition.

See more info on `routing property` [Java version](../dev-java/routing-in-partitioned-spaces.html) \|[ .NET version](../dev-dotnet/routing-in-partitioned-spaces.html).

Starting with XAP 9.7 a new protective mode has been added to protect against writing entries with a `null` value routing. In case your application contains objects without a routing value you'll get the following exception:


```bash
com.gigaspaces.client.protective.ProtectiveModeException: Operation is rejected - no routing value provided when writing an entry of type `MyClass` in a partitioned space.
```

A Space routing value should be specified before writing the space. Missing routing value would result in a remote client not being able to locate this entry as the routing value will not match the partition the entry is located.

{{% note "Note"%}}
It is highly recommended that you modify them and add a routing value. If this is not feasible, you can disable it using the following system property:

```bash
-Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false
```
{{% /note %}}

In case your application writes directly to one of the partitions and assigns the wrong routing value you'll get the following exception:


```bash
com.gigaspaces.client.protective.ProtectiveModeException: Operation is rejected - the routing value in the written entry of type 'MyClass' does not match this space partition id. The value within the entry's routing property named 'symbol' is 100 which matches partition id 1 while current partition id is 2...
```

It is highly recommended that you modify them and set the right routing.

{{% refer %}}
The following [example](/sbp/storing-partition-information.html) demonstrates one way to handle writing entries to a partition with the wrong routing value.
{{% /refer %}}          

{{% note "Note"%}}
If this is not feasible, and you know what you're doing, it can be disabled using the following system property: 

```bash
-Dcom.gs.protectiveMode.wrongEntryRoutingUsage=false
```
{{% /note %}}


# Primitive Types Validation

If you must use primitive property types, then assign null values. This is enforced by the protective mode since 9.7.

When querying the space using template matching [Java version](../dev-java/query-template-matching.html) \|[ .NET version](../dev-dotnet/query-template-matching.html), `null` properties are ignored and `non-null` properties are matched. Since primitive properties cannot be set to `null`, a `nullValue` can be assigned to a property to indicate a value which will be treated as null when using template matching.

See primitive types matching [Java version](../dev-java/query-template-matching.html#primitive-types) \|[ .NET version](../dev-dotnet/query-template-matching.html#primitive-types).

A protective mode was added to protect against querying with a template that contains one or more primitive properties without a `nullValue`, since such templates are likely to produce unexpected results. 

If your application uses template matching with such types, you'll get the following exception:


```java
com.gigaspaces.client.protective.ProtectiveModeException: Operation is rejected - template matching on type MyClass is illegal because it has primitive properties without null value: id (int)...
```


It is highly recommended that you define `nullValue` where appropriate, or switch to SQLQuery [Java version](../dev-java/query-sql.html) \|[ .NET version](../dev-dotnet/query-sql.html) instead.

{{%note "Note"%}}
If this is not feasible, this protective mode can be disabled using the following system property: 

```bash
-Dcom.gs.protectiveMode.primitiveWithoutNullValue=false
```
{{%/note%}}


