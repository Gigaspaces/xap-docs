---
type: post102
title:  API Mapping File
categories: XAP102
parent: xap-cpp.html
weight: 300
---

{{% ssummary%}}{{% /ssummary %}}



This section describes the elements that are available to be used in your `gs.xml` file.

{{% note %}}
The `type` property is mandatory in case the `property` element is defined.
{{%/note%}}

The gs.xml file allows you to define c++ classes in the space. To learn how to do this, see the [CPP API Code Generator](./cpp-api-code-generator.html) section.

{{%refer%}}
For the latest supported configurations please consult the [api documentation](/api_documentation/xap-{{%currentversion%}}.html)
{{%/refer%}}

The `*.gs.xml` configuration needs to reside in a `<Root Folder>\config\mapping` folder where the `<Root Folder>` should be part of the application classpath.

XML mapping can be defined in the same package as the class (using the class name as the file prefix).



# class

**Usage**: `<class name="myClass" >`


| XML Attribute Name | Type | Description | Default Value |
|:-------------------|:-----|:------------|:--------------|
| `name` | string | The name of the class. | |
| `cpp-name` | string | The C++ name of the class.{{% wbr %}}If not specified then 'name' is used. | |
| `dotnet-name` | string | The .NET name of the class, including the namespace.{{% wbr %}}If not specified then 'name' is used. | |
| `replicate` | boolean | When running in partial replication mode, a `true` value for this field replicates all objects of this type to a target space or spaces. | `true` |
| `persist` | boolean | When a space is defined as persistent, a `true` value for this annotation persists objects of this type.{{% wbr %}} For more details, refer to the [Persistency](./space-persistency.html) section. {{% wbr %}}| `true` |
| `fifo` | boolean | To enable FIFO-based notifications and take operations, this annotation should be `true`.{{% wbr %}} For more details, refer to the [FIFO operations](./fifo-support.html) section. {{% wbr %}}| `false` |

{{% note %}}
The default values for `replicate`, `persist`, and `fifo` should only be considered as +recommended+ default values. Actual values should be specified in the `gs.xml` file.
{{% /note %}}

# superclass

Declares the parent class. If it inherits from another class, only one class is allowed.

The superclass must also be a c++ class.

**Usage**: `<superclass name="myBaseClass" />`


| XML Attribute Name | Type | Description |
|:-------------------|:-----|:------------|
| `name` | string | The name of the inherited class |

# include-header

This attribute is used to add include file declaration to your generated c++ code.


| XML Attribute Name | Type | Description |
|:-------------------|:-----|:------------|
| `file` | string | The name of the include file. |

For example:


```cpp
<include-header file="UserMessage.h"/>
```

# property

The `property` element defines a field in this class.

**Usage**: `<property name="m_Age" type="int" null-value="-1" />`


| XML Attribute Name | Type | Description | Default Value |
|:-------------------|:-----|:------------|:--------------|
| `name` | string | The property name.{{% wbr %}}{{% infosign %}} It is recommended that property names start with a lowercase letter to avoid conflicts in Java. If POJO classes are generated too, then any property that starts with an uppercase letter will be excluded from its POJO class.  | `NONE` |
| `index` | string of `IndexType` | Defines if this field data is indexed. Querying indexed fields speeds up read and take operations. Possible values are `false` and `true`. | `false` |
| `null-value` | String | Specifies that a value be treated as `null`.{{% wbr %}}For example: `<property name="m_Age" type="int" null-value="4711" />`, where `4711` functions as a `null` value. | |
| `type` | String | Defines the type of the property. **This is required.** See possible values below. | |

{{% anchor 1 %}}

{{% anchor type--Supported-Types %}}

# type

The table below shows the **supported types that can be used in the space**, and how the different types are mapped in each language.


| XML Type | c++ Type | Java Type | .NET Type |
|:---------|:---------|:----------|:----------|
| `bool` | `bool` | `boolean` | `boolean` |
| `byte` | `char` | `byte` | `byte` |
| `char` | `char` | `char` | `char` |
| `double` | `double` | `double` | `double` |
| `float` | `float` | `float` | `float` |
| `int` | `int` | `int` | `int` |
| `long` | `long` | `int` | `int` |
| `long long` | `long long` | `long` | `long` |
| `short` | `short` | `short` | `short` |
| `string` | `std::string` | `java.lang.String` | `string` |
| `bool[]` | `std::vector<bool>` | `boolean[]` | `bool[]` |
| `byte[]` | `std::vector<char>` | `byte[]` | `byte[]` |
| `char[]` | `std::vector<char>` | `char[]` | `char[]` |
| `double[]` | `std::vector<double>` | `double[]` | `double[]` |
| `float[]` | `std::vector<float>` | `float[]` | `float[]` |
| `int[]` | `std::vector<int>` | `int[]` | `int[]` |
| `long[]` | `std::vector<long>` | `int[]` | `int[]` |
| `long long[]` | `std::vector<long long>` | `long[]` | `long[]` |
| `short[]` | `std::vector<short>` | `short[]` | `short[]` |
| `string[]` | `std::vector<std::string>` | `java.lang.String[]` | `string[]` |
| `blob` | `Blob` | `byte[]` | `byte[]` |

{{% tip %}}
When having a Java class and a C++ class sharing data you should use int/long/float/double/short data types with your Java space class POJO instead of java.lang.Integer/Long/Float/Double/Short
{{% /tip %}}

# ref-property


| XML Attribute Name | Type | Description | Default Value |
|:-------------------|:-----|:------------|:--------------|
| `name` | string | The property name | `NONE` |
| `class-ref` | string | The class name | `NONE` |
| `type` | string | Determines if the object is an array or a single object | `NONE` |
| `storage-type` | string | Determines how this field value is stored in the space.{{% wbr %}}options are:{{% wbr %}} `object` - The value of this property is stored explicitly, so entries can be matched by specific value. In this case you should generate a matching POJO for the embedded C++ class.{{% wbr %}} `binary` - The value is stored as a blob. In this case there is no need to generate a matching POJO for the embedded C++ class. It is more efficient option, but would not allow you to perform matching based on this field. See below example how you should use this option: {{% wbr %}}\<class name="complexPayloadAsBinary" persist="true" replicate="false" fifo="false"\>{{% wbr %}}   \<superclass name="benchmarkBase" /\>{{% wbr %}}   \<ref-property class-ref="payloadAsString" name="innerPayload" type="boost::shared_ptr" storage-type="binary"/\>{{% wbr %}} </class>{{% wbr %}}{{% wbr %}} `object` |



Types that can be used with the `ref-property` attribute:


| XML type | CPP Type | Java Type | .NET Type |
|:---------|:---------|:----------|:----------|
| `array` | `std::vector < boost::shared_ptr < POCO > >` | `POJO[]` | `PONO[]` |
| `boost::shared_ptr` | `boost::shared_ptr< POCO >` | `POJO` | `PONO` |

For example:


```xml
<class name="com.gigaspaces.tests.test_refNode" persist="true" replicate="true" fifo="false" >
    <property name="intIndex"  type="int" null-value="0" index="true"/>
    <ref-property class-ref="com.gigaspaces.tests.test_refChildNode" name="children" type="array"></ref-property>
    <ref-property class-ref="com.gigaspaces.tests.test_refChildNode" name="leftChild" type="boost::shared_ptr"></ref-property>
    <ref-property class-ref="com.gigaspaces.tests.test_refChildNode" name="rightChild" type="boost::shared_ptr"></ref-property>
</class>
```

# id

**Usage**:


```xml
<property name="idField" type="string" null-value="" />
<id name="idField" auto-generate="true" />
```

Defines whether this field value is used when generating the Entry's UID. The field value should be unique - i.e. multiple objects with the same value cannot be written into the space. Each object should have a different field value. When writing an object into the space with an existing `id` field value, an `EntryAlreadyInSpaceException` is thrown. The Entry's UID is created based on the `id` field value.


| XML Attribute Name | Type | Description | Default Value |
|:-------------------|:-----|:------------|:--------------|
| `name` | string | Specifies the name of the property for holding the UID. | |
| `auto-generate` | String | Specifies if the Entry's UID is generated automatically by the space when written into the space. If `false`, the field is indexed automatically, and if `true`, the field isn't indexed | `false` |

{{% note %}}
The `id` element cannot be used with multiple fields.

The `id` element type must be string.
{{%/note%}}

If `auto-generate` is declared as `false`, the field is indexed automatically. If `auto-generate` is declared as `true`, the field isn't indexed.

{{% anchor 3 %}}

# version


| XML Attribute Name | Type | Description |
|:-------------------|:-----|:------------|
| `name` | string | Specifies the name of the property holding the version's ID. |


# routing

The `routing` element routes the field value under this element to the relevant space. This is done using hash-based load-balancing.


| XML Attribute Name | Type | Description |
|:-------------------|:-----|:------------|
| `name` | string | Specifies the property that allows identification of the `routing` element in the space. |

{{% info %}}
When working with a partitioned persistent space that persists into a central data-source, make sure that a property mapped for `routing` is also mapped with `id`.
{{%/info%}}

# Example


```xml
<class name="com.gigaspaces.tests.completeType" persist="true" replicate="true" fifo="false" >
    <property name="idField" type="string" null-value="" />
    <id name="idField" auto-generate="true" />

    <property name="stringField" index="true" type="string" null-value="" />
    <routing  name="stringField"/>

    <property name="versionField"     type="long" null-value="0" />
    <version name="versionField"/>

    <!--<property name="byteField"     type="byte" null-value="0" />-->
    <property name="booleanField"  type="bool" null-value="false" />
    <property name="shortField"    type="short" null-value="0" />
    <property name="charField"     type="char" null-value="0" />
    <property name="intField"      type="int" null-value="0" />
    <property name="longField"     type="long" null-value="0" />
    <property name="longlongField"     type="long long" null-value="0" />
    <property name="floatField"    type="float" null-value="0" />
    <property name="doubleField"   type="double" null-value="0" />
</class>
```


