---
type: post
title:  Reducing the Space Object Footprint
categories: SBP
parent: data-access-patterns.html
weight: 900
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Shay Hassidim| 7.1 | July 2009  |           |          |




# Overview
By default, when using the GigaSpaces Java API, the Space stores Space object fields as is. No data compaction or compression is done while the object is transported across the network, or when stored within the Space.

{{% note %}}

- The Compressed Storage Type is a different approach than the compact serialization, pattern, and compresses non-primitive fields using the zip utilities.
- The Binary Storage Type stores non-primitive fields within the Space in its byte array form. It does not compress or reduce the footprint of the data as the compact pattern does. This approach makes it unnecessary to introduce the nested Space object data type to the Space JVM, or to de-serialize them on the Space side. The Binary Storage Type may improve performance when the Space object stores large collections.
- C++ and .Net API data objects may undergo some compaction when sent across the network.
{{% /note %}}

With the Compact Serialization pattern, you can reduce the footprint of the data object in the Space memory when it is stored in the data grid, which allows storing more Space objects per memory unit. This pattern works very well when the Space object includes a large number of numerical values, because these objects are stored as a more optimal data type.

The compact serialization pattern enables complete control over the format of the data object while transported over the network and when stored within the Space. This technique:

- Compacts the object payload data when transported over the network and when stored in memory.
- Avoids the de-serialization involved when space object written to the space from a remote client (for non primitive fields such as user defined classes or collection field)
- Avoids the de-serialization replicated to a backup space(s)
- Avoids the serialization involved when reading an object back from the space into the client process (at the space side).

# The Basic Flow
With the compact serialization pattern:

- Before you write the object you serialize all non indexed fields (payload data) into one byte array field using the GigaSpaces serialization API (pack).
- You serialize/de-serialize all indexed fields as usual (have the `writeExternal` , `readExternal` implementation to write and read these into the stream).
- After reading the object from the space you should de-serialize the byte array data (unpack).

{{% align center %}}
![bin_ser.jpg](/attachment_files/sbp/bin_ser.jpg)
{{% /align %}}

When the object is written to the space:

- The non-indexed fields are compacted and serialized into the same field (as a byte array).
- All the indexed fields + the byte array are serialized as usual via the `writeExternal` call.
- The object with arrives in the space, de-serialized, indexed fields stored as usual and the byte array field stored as is.

When the object is read from the space:

- The read template undergoes the same actions as when writing an object to the space
- The matching object is serialized and sent to the client.
- When the matching object arrives the client side is it de-serialized, and the byte array data is de-serialized and expand (in a lazy manner).

# The Implementation
Using the compact serialization pattern can reduce the object footprint when stored within the space in drastic manner. As much as you will have more fields as part of the space object serialized using the GigaSpaces Serialization API, the memory footprint overhead will be smaller compared to the default serialization mode.

The compact serialization pattern involves creation the following methods:

1. `pack` method - Packs the object data into one field. Serialize the non-Indexed fields into the byte array.
2. `unpack` method - Unpacks the object data into one field. De-serialize the non-Indexed fields from the byte array.
3. `writeExternal` method - Serialize the object data. Required for the `Externalizable` implementation. Serialize the indexed fields and the byte array.
4. `readExternal` method - De-serialize the object data. Required for the `Externalizable` implementation. De-serialize the indexed fields and the byte array.
5. `checkNulls` method - Handles null data for the indexed and byte array fields.
6. `getnulls` method - Handles null data for non indexed fields.

# BinaryOutputStream and BinaryInputStream
The `BinaryOutputStream` contains various method to serialize all java's primitive type, their Object wrappers and arrays forms in a compacted mode. `BinaryInputStream` is its counterpart for de serialization. Your `pack` and `unpack` methods will be using an instance of those classes.

# Example
With the [attached example](/attachment_files/sbp/BinaryCompaction.zip) we have a space class with 37 fields.

- 1 Integer data type field (indexed used for queries).
- 12 String fields
- 12 Long fields
- 12 Integer Fields.

{{% info %}}
With this example - The footprint overhead of the default serialization compared to a compact format is **250%**.
{{% /info %}}

To run this example copy the example package zip into \GigaSpaces Root\examples\, extract the zip file and follow the instructions at the readme file.

## The Original Space class
Our example involves a space class that will be modified to follow the compact serialization pattern.

The original class includes:

- One Integer indexed field.
- 12 String type non indexed fields declared as space class fields
- 12 Long type non indexed fields declared as space class fields
- 12 Integer type non indexed fields declared as space class fields
- Getter and Setter methods for the above fields

The original class looks like this:


```java
@SpaceClass
public class SimpleEntry {

	public SimpleEntry() {
	}
	private Integer queryField;
	private Long _longFieldA1;

	....

	@SpaceRouting
	@SpaceIndex(type=SpaceIndexType.BASIC)
	public Integer getQueryField() {
		return queryField;
	}

	// getter and setter methods
	public void setQueryField(Integer field) {
		queryField = field;
	}

	public Long get_longFieldA1() {
		return _longFieldA1;
	}

	public void set_longFieldA1(Long fieldA1) {
		_longFieldA1 = fieldA1;
	}
```

## The BinaryFormatEntry class
The modified class that implements the compact serialization pattern includes:

- Using the `@SpaceClass(includeProperties=IncludeProperties.EXPLICIT)` decoration - this allows you to control which fields will be Space class fields explicitly.
- One Integer indexed field.
- One byte array field declared as a space class field.
- 12 String type non indexed fields. These are not space class fields.
- 12 Long type non indexed fields. These are not space class fields.
- 12 Integer type non indexed fields. These are not space class fields.
- Getter and setter methods for the above fields.
- `pack` and `unpack` method and few helper methods.
- `Externalizable` implementation - `writeExternal` and `readExternal` methods

The modified class looks like this:


```java
@SpaceClass(includeProperties=IncludeProperties.EXPLICIT)
public class BinaryFormatEntry implements Externalizable {

	public BinaryFormatEntry(){}

	private Integer    queryField;
	private byte[]     _binary;

	private Long       _longFieldA1;
	....

	@SpaceRouting
	@SpaceIndex(type=SpaceIndexType.BASIC)
		public Long getQueryField()
	{
	    return queryField;
	}

	public void setQueryField(Long _queryField)
	{
	    queryField = _queryField;
	}

	@SpaceProperty
	public byte[] getBinary() {
		return _binary;
	}

	public void setBinary(byte[] _binary) {
		this._binary = _binary;
	}

	public Long get_longFieldA1() {
		return _longFieldA1;
	}

	public void set_longFieldA1(Long fieldA1) {
		_longFieldA1 = fieldA1;
	}
	...

	public void pack(){...}
	public void unpack(){...}
	public void writeExternal(ObjectOutput out){...}
	public void readExternal(ObjectInput in) {...}
	private long getnulls(){...}
	private short checkNulls() {...}
}
```

## The pack method
The `pack` method serializes the object non indexed data. It is called explicitly before calling the space write operation. This method serialize the object data by placing the data into the byte array field. Null values fields indication stored within one field. The `BinaryOutputStream` utility class is used to write the compacted data into the byte array.


```java
public void pack() throws Exception
{
    BinaryOutputStream output = new BinaryOutputStream();
    long nulls = getNulls();
    output.writeLong(nulls);
    if (_longFieldA1 != null)
        output.writeLong(_longFieldA1);

    // ... etc. for all other compactable fields.

    _binary = output.toByteArray();
    output.close();
}
```

## The unpack method
This method de-serialize the object data by extracting the data from the byte array field and populating the fields with their corresponding values. `null` values fields are non-populated.  This method is called after calling the space read operation. The `BinaryOutputStream` utility class is used to read the compacted data and place it into the relevant field.


```java
public void unpack() throws Exception
{
    BinaryInputStream input = new BinaryInputStream(_binary);
    long nulls = input.readLong();
    int i = 0;

    if ((nulls & 1L << i) == 0)
        _longFieldA1 = input.readLong();
    i++;

    // ... etc. for all other compactable fields.

    input.close();
    _binary = null;
}
```

## The writeExternal method
The `writeExternal` method serializes the object data into the output stream.  The object data involves a field indicates which fields have `null` value, the indexed fields and a byte array field that includes all non indexed fields data (created by the `pack` method). The `writeExternal` assumes the `pack` method has been called explicitly prior the space write method call that initiated the `writeExternal` call.


```java
public void writeExternal(ObjectOutput out) throws IOException {
	short nulls = 0;
    int i=0;

    nulls = checkNulls();

    out.writeShort(nulls);
    if (queryField != null) {
        out.writeLong(queryField);
    }
    if (_binary != null) {
        out.write(_binary);
    }
}
```

## The readExternal method
The `readExternal` method essentially performs the opposite of the what the `writeExternal` method is doing. This methods populates the indexed fields data and the byte array field data. Later, the remaining fields will be populated once the `unpack` method will be called.


```java
public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
	short nulls;
    int i=0;
    nulls = in.readShort();

    if( (nulls & 1L << i) == 0 )
      queryField = in.readLong();
    i++;
    if( (nulls & 1L << i) == 0 )
    {
         byte[] data = new byte[500];
         int len = in.read(data);
         _binary = new byte[len];
         System.arraycopy(data, 0, _binary, 0, len);
    }
}
```

## The checkNulls method
This method goes through the indexed fields and the byte array field and place into a `short` data type field an indication for the ones with null value using a bit map.


```java
private short checkNulls() {
    short nulls = 0;
    int i = 0;

    nulls = (short) ((queryField == null) ? nulls | 1 << i : nulls);
    i++;
    nulls = (short) ((_binary == null) ? nulls | 1 << i : nulls);
    i++;
    return nulls;
}
```

## The getnulls method
This method goes through all class non indexed fields (the ones that their data is stored within the byte array) and place into a `long` data type field indication for the ones with null value using a bit map.


```java
private long getnulls()
{
    long nulls = 0;
    int i=0;

    nulls = ((_longFieldA1 == null)  ? nulls | 1L << i : nulls ) ;
    i++;
    nulls = ((_longFieldB1 == null)  ? nulls | 1L << i : nulls ) ;
    i++;
    ...
    return nulls;
}
```

## The Factory method
The example using a factory method called `generateBinaryFormatEntry` to create the space object. Once it has been populated , its `pack` method is called.


```java
private BinaryFormatEntry generateBinaryFormatEntry(int id){
	BinaryFormatEntry bfe = new BinaryFormatEntry(id, value1 , value2 ?)
	bfe.pack();     //  the pack method is called implicitly as part of the factory method
	return bfe;
}
```

## Writing and Reading the Object from the space
The following code snipped illustrates how the compact serialized object is written into the space and read from the space:


```java
GigaSpace _gigaspace;
BinaryFormatEntry testBFE = generateBinaryFormatEntry(500);
_gigaspace.write(testBFE, Lease.FOREVER);
BinaryFormatEntry templateBFE = new BinaryFormatEntry();
templateBFE.setQueryField (new Long(500));
BinaryFormatEntry resBFE = (BinaryFormatEntry)_gigaspace.read(templateBFE, 0);
resBFE.unpack(); // this deserialize the binary data into the object fields
```

# References
The [PackRat](http://www.openspaces.org/display/PRT/PackRat) project allows you to use the compact serialization pattern via simple annotations.
