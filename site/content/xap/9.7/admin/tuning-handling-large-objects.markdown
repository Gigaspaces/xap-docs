---
type: post97
title:  Handling Large Objects
categories: XAP97ADM
parent: tuning-gigaspaces-performance.html
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}




In order to store large files in memory, you should simply load the file into the relevant Data type (byte array , blob) and use the relevant API to write the data into the space. Large objects are treated like any other objects stored within the space.

{{% tip %}}
The `com.gs.transport_protocol.lrmi.maxBufferSize` system property controls the chunk size. See the [Communication Protocol](./tuning-communication-protocol.html#maxBufferSize) for details.
{{% /tip %}}

## Memory Allocation Behavior

During the data transfer activity the space leverage non-heap buffers to transfer the data (NIO direct buffer) into the target process, but due-to the serialization behavior, the entire data object maintained for a short duration within the JVM heap. These in-transit buffers will be cleared once the JVM garbage collector will request these (weak reference). To allow this activity to have enough memory headroom, you should increase the Space and client heap size to accommodate these transit buffers. The heap size headroom required would be:


```java
Maximum number of concurrent connections X Maximum Object size
```

# Large Objects Examples

## Using GigaSpace API to store large files within the space


```java
public class SpaceFile {

	SpaceFile (){}
	SpaceFile (String name , byte[] content)
	{
		this.name = name;
		this.content= content;
	}
	String name;
	byte[] content;

	@SpaceId
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}
```


```java
public static void main(String[] args) {
	GigaSpace gigaspace = new GigaSpaceConfigurer(new UrlSpaceConfigurer("jini://*/*/mySpace")).gigaSpace();
	String myfileName = "d:
  temp
  test.pdf";
	File myfile = new File(myfileName);

	//writing Space File to space
	SpaceFile spaceFile = new SpaceFile(myfile.getName(),fileToBytes(myfileName));
	gigaspace.write(spaceFile);

	//reading Space File from space
	SpaceFile spaceFile2 = gigaspace.readById(SpaceFile.class, "test.pdf");
	bytesTofile(spaceFile2.getContent(), "d:/temp/test_.pdf");
}

public static byte[] fileToBytes(String fileName) {
	FileInputStream inFile = null;
	byte[] content = null;

	try {
		System.out.println("Reading " + fileName + "...");
		inFile = new FileInputStream(fileName);
		int fileLength = (int) inFile.getChannel().size();
		content = new byte[fileLength];
		int length = 0;
		length = inFile.read(content);
		System.out.println("Read " + length + " out of " + fileLength);
		inFile.close();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (inFile != null)
			try {
				inFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	return content;
}

public static void bytesTofile(byte[] content, String fileName) {
	ByteArrayInputStream bais = null;
	FileOutputStream fos = null;
	BufferedInputStream bis = null;
	try {
		bais = new ByteArrayInputStream(content);
		System.out.println("Copying Space file Data to destination file...");
		byte[] buffer = new byte[100];
		fos = new FileOutputStream(fileName);
		bis = new BufferedInputStream(bais);

		for (int count = 0; count != -1;) {
			count = bis.read(buffer);
			fos.write(buffer);
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (bais != null)
				bais.close();
			if (fos != null)
				fos.close();
			if (bis != null)
				bis.close();
		} catch (Exception e) {
		}
	}
	System.out.println("Copying Space file Data to destination file...Done!");
}
```

## Using JDBC BLOB to store large files within the space


```java
Class.forName("com.j_spaces.jdbc.driver.GDriver").newInstance();
String url = "jdbc:gigaspaces:url:jini://*/*/mySpace";
Connection conn = DriverManager.getConnection(url);

Statement st1 = conn.createStatement();
String createTable = "CREATE TABLE MY_DATA (ID INTEGER INDEX,BLOB_COL BLOB)";
st1.executeUpdate(createTable);
st1.close();
File imgfile = new File("c:
  temp
  largeFile.pdf");
for (int i = 1; i < 5; i++) {
	FileInputStream fin = new FileInputStream(imgfile);
	PreparedStatement pre = conn.prepareStatement("insert into MY_DATA values(?,?)");
	pre.setInt(1, i);
	pre.setBinaryStream(2, fin, (int) imgfile.length());
	pre.executeUpdate();
	System.out.println("We have " + i + " files in the space");
     pre.close();
     fin.close();
}

for (int i = 1; i < 5; i++) {
	PreparedStatement pre = conn.prepareStatement("select ID,BLOB_COL from MY_DATA where ID = ?");
	pre.setInt(1, i);
	ResultSet result = pre.executeQuery();
	while (result.next()) {
		int ID = result.getInt(1);
		Blob b = result.getBlob(2);
		System.out.println("ID=" + ID + " read " + b.length()+ " bytes");
	}
	pre.close();
}
```

