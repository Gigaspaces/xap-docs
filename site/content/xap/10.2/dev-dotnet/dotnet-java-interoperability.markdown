---
type: post102
title:  .NET-Java Interoperability
categories: XAP102NET
parent: interoperability.html
weight: 100
---

{{% ssummary %}} {{% /ssummary %}}


# Designing Interoperable Classes



{{%panel%}}
{{%section%}}
{{%column width="50%" %}}
 C#


```csharp
using GigaSpaces.Core.Metadata;

namespace MyCompany.MyProject.Entities
{
   [SpaceClass(AliasName="com.mycompany.myproject.entities.Person")]
   public class Person
   {
    private string _name;
    [SpaceProperty(AliasName="name")]
    public string Name
    {
        get { return this._name; }
        set { this._name = value; }
    }
   }
}
```
{{%/column%}}

{{%column width="50%" %}}
 Java


```java
package com.mycompany.myproject.entities;

   public class Person
   {
    private String name;

    public String getName()
    {
    return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
   }
}
```
{{%/column%}}
{{%/section%}}
{{%/panel%}}



### Guidelines and Restrictions

The following guidelines and restrictions should be followed in order to enable platform interoperability:

- The full class name (including package\namespace) in all platforms should be identical.

{{% note %}}
Since java packages use a different naming convention than .NET namespaces, it is recommended to use the `SpaceClass(AliasName="")` feature to map a .NET class to the respective java class.
{{%/note%}}

- The properties/fields stored in the space in all platforms should be identical.


In Java, only properties are serialized into the space. In .NET, both fields and properties are serialized, so you can mix and match them.

Since java properties start with a lowercase letter, whereas .NET properties usually start with an uppercase letter, it is recommended to use the `SpaceProperty(AliasName="")` feature to map a property/field name from .NET to java.


- Only the types listed in the table below are supported. If one of your fields uses a different type, you can use the class only in a homogeneous environment.


Arrays of these types are supported as well.
You can also use .NET enumerations, which are treated as their underlying .NET type. Java enums are not supported.
If your class contains a field whose type is not in the table, you can use `SpaceExclude` to exclude it from the space.
Some of the types have different characteristics in .NET and Java (signed\unsigned, nullable\not nullable, precision, etc.) This can lead to runtime exceptions (e.g. trying to store `null` in a .NET structure) or unexpected results (e.g. copying values between signed and unsigned fields).



# Supported Types for Matching and Interoperability

The following types are supported by the space for matching and interoperability:


| CLS | C# | VB.NET | Java | Description |
|:----|:---|:-------|:-----|:------------|
| [System.Byte](http://msdn2.microsoft.com/en-us/library/system.byte.aspx) |   byte  |  Byte  | [byte](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) | 8-bit integer.**<sup>1</sup>** |
| [Nullable\<Byte\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) |  byte? |  Nullable(Of Byte)  | [java.lang.Byte](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Byte.html) | Nullable wrapper for byte.**<sup>1</sup>** |
| [System.Int16](http://msdn2.microsoft.com/en-us/library/system.int16.aspx) | short  |  Short   | [short](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) | 16-bit integer. |
| [Nullable\<Int16\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | short?  |  Nullable(Of Short)  | [java.lang.Short](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Short.html) | Nullable wrapper for short. |
| [System.Int32](http://msdn2.microsoft.com/en-us/library/system.int32.aspx) | int  |  Integer  | [int](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) | 32-bit integer. |
| [Nullable\<Int32\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | int?  |  Nullable(Of Integer)  | [java.lang.Integer](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Integer.html) | Nullable wrapper for int. |
| [System.Int64](http://msdn2.microsoft.com/en-us/library/system.int64.aspx) | long  |  Long  | [long](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) | 64-bit integer. |
| [Nullable\<Int64\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | long?  |  Nullable(Of Long)  | [java.lang.Long](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Long.html) | Nullable wrapper for long. |
| [System.Single](http://msdn2.microsoft.com/en-us/library/system.single.aspx) | float  |  Single  | [float](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) |  Single-precision floating-point number (32 bits). |
| [Nullable\<Single\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | float?  |  Nullable(Of Single)  | [java.lang.Float](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Float.html) | Nullable wrapper for float. |
| [System.Double](http://msdn2.microsoft.com/en-us/library/system.double.aspx) | double  |  Double  | [double](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html) |  Double-precision floating-point number (64 bits). |
| [Nullable\<Double\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | double?  |  Nullable(Of Double)  | [java.lang.Double](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Double.html) | Nullable wrapper for double. |
| [System.Boolean](http://msdn2.microsoft.com/en-us/library/system.boolean.aspx) | bool  |  Boolean  | [boolean](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html)   | Boolean value (true/false). |
| [Nullable\<Boolean\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | bool?  |  Nullable(Of Boolean)  | [java.lang.Boolean](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Boolean.html) | Nullable wrapper for boolean. |
| [System.Char](http://msdn2.microsoft.com/en-us/library/system.char.aspx) | char  |  Char  | [char](http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html)   | A Unicode  character (16 bits). |
| [Nullable\<Char\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) | char?  |  Nullable(Of Char)  | [java.lang.Character](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Character.html) | Nullable wrapper for char. |
| [System.String](http://msdn2.microsoft.com/en-us/library/system.string.aspx) |  string  |  String  | [java.lang.String](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/String.html) | An immutable, fixed-length string of Unicode characters. |
| [System.DateTime](http://msdn2.microsoft.com/en-us/library/system.datetime.aspx) [Nullable\<DateTime\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) |  DateTime   DateTime?  |  DateTime   Nullable(Of DateTime) | [java.util.Date](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Date.html) | An instant in time, typically expressed as a date and time of day.**<sup>2,3</sup>** |
| [System.Decimal](http://msdn2.microsoft.com/en-us/library/system.decimal.aspx) [Nullable\<Decimal\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) |  decimal   decimal?  |  Decimal   Nullable(Of Decimal)  | [java.math.BigDecimal](http://docs.oracle.com/javase/1.5.0/docs/api/java/math/BigDecimal.html) | A decimal number, used for high-precision calculations.**<sup>2,4</sup>** |
| [System.Guid](http://msdn2.microsoft.com/en-us/library/system.guid.aspx) [Nullable\<Guid\>](http://msdn.microsoft.com/en-us/library/b3h38hb0.aspx) |  Guid   Guid?  |  Guid   Nullable(Of Guid)  | [java.util.UUID](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/UUID.html) | A 128-bit integer representing a unique identifier.**<sup>2</sup>** |
| [System.Object](http://msdn2.microsoft.com/en-us/library/system.object.aspx) |  object  |  Object  | [java.lang.Object](http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Object.html) | Any object |

1. In .NET a `byte` is unsigned, whereas in java a `byte` is signed.
2. These types can be either nullable or not nullable in .NET, whereas in java they are always nullable.
3. In .NET a `DateTime` is measured in ticks (=100 nanoseconds) since 1/1/0001, whereas in java a `Date` is a measured in milliseconds since 1/1/1970.
4. The types `Decimal` (.NET) and `BigDecimal` (java) have different precision and range (see .NET and java documentation for more details). In addition, be aware that serialization/de serialization of these types is relatively slow, compared to other numeric types. As a rule of thumb these types should not be used, unless the other numeric types precision/range is not satisfactory.

{{%warning%}}
Java 8's LocalDate, LocalTime, LocalDateTime are currently not interoperable with the .NET DateTime class.
{{%/warning%}}

# Arrays and Collections support

The following collections are mapped for interoperability:


| .NET | Java | Description |
|:-----|:-----|:------------|
| T[] | E[] | Fixed-size arrays of elements. |
| [System.Collections.Generic.List\<T\>](http://msdn.microsoft.com/en-us/library/6sh2ey19.aspx) {{<wbr>}} [System.Collections.ArrayList](http://msdn2.microsoft.com/en-us/library/system.collections.arraylist.aspx){{<wbr>}}  [System.Collections.Specialized.StringCollection](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.stringcollection.aspx) | [java.util.ArrayList](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/ArrayList.html) | Ordered list of elements. |
| [System.Collections.Generic.Dictionary\<K,V\>](http://msdn.microsoft.com/en-us/library/xfhwa508.aspx){{<wbr>}}  [System.Collections.HashTable](http://msdn2.microsoft.com/en-us/library/system.collections.hashtable.aspx) {{<wbr>}} [System.Collections.Specialized.HybridDictionary](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.hybriddictionary.aspx)  [System.Collections.Specialized.ListDictionary](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.listdictionary.aspx) | [java.util.HashMap](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/HashMap.html) | Collection of key-value pairs. |
| [System.Collections.Specialized.OrderedDictionary](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.ordereddictionary.aspx) | [java.util.LinkedHashMap](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/LinkedHashMap.html) | Ordered collection of key-value pairs. |
| [System.Collections.Generic.SortedDictionary\<K,V\>](http://msdn.microsoft.com/en-us/library/f7fta44c.aspx) | [java.util.TreeMap](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/TreeMap.html) | Sorted collection of key-value pairs. |
| [System.Collections.Specialized.NameValueCollection](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.namevaluecollection.aspx) [System.Collections.Specialized.StringDictionary](http://msdn2.microsoft.com/en-us/library/system.collections.specialized.stringdictionary.aspx) | [java.util.Properties](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Properties.html) | Collection of key-value string pairs.**<sup>1</sup>** |

In java, the `Properties` type allows the user to store keys and values which are not strings.

