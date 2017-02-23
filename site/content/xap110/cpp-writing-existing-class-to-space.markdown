---
type: post110
title:  Writing Existing Class to Space
categories: XAP110
parent: cpp-api-examples.html
weight: 400
---



This example shows you how to write your own c++ class to the space (as opposed to writing a class that is generated from a XML file). The code for this example is located at `<XAP Root>\cpp\examples\PocoFromExistingClass\`. This path will be referred to as `<Example Root>` in this page.

{{% note %}}
This example can be built and run on **Windows OS** only. If you use **Visual Studio** open the solution `PocoFromExistingClass.sln` located in `<XAP Root>\cpp\examples\PocoFromExistingClass\`. It is recommended to set your solution configuration to `Release` and do a rebuild that will generate all related files.
{{%/note%}}

{{% refer %}}It is assumed that you have read the [GigaSpaces C++ Hello World example](./cpp-api-hello-world-example.html) which serves as a starting point.{{% /refer %}}

To write your c++ class to the space, perform the following steps:

# Create a gs.xml file that contains the class definition.

For example, this is the file `<Example Root>\PocoFromExistingClass.gs.xml`:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE gigaspaces-mapping SYSTEM "../../config/cpp.dtd">
<gigaspaces-mapping>
  <include-header file="UserMessage.h"/>
  <class name="UserMessage" persist="false" replicate="false" fifo="false" >
    <property name="id" type="int" null-value="-999" index="true"/>
    <routing  name="id"/>

    <property name="uid" type="string" null-value="" index="false"/>
    <id name="uid" auto-generate="true" />
    <property name="content" type="string" null-value="" index="false"/>
  </class>
</gigaspaces-mapping>
```

Note the line that informs `gsxml2cpp` about the file of your existing class:


```cpp
<include-header file="UserMessage.h"/>
```

# Change the user class.

{{% info %}}
An example of an original class can be found at `<Example Root>\UserMessage_original.h.txt`; and an example of the same class with the changes listed below can be found at `<Example Root>\UserMessage.h`.
{{%/info%}}

Step 1. Add an include statement and namespace as shown below:


```cpp
#include "IEntry.h"
using namespace OpenSpaces;
```

Step 2. Inherit from `IEntry`:


```cpp
class UserMessage:  public IEntry /** (GigaSpaces) need to inherit for space operations **/
```

Step 3. Implement the the `IEntry` interface (one function):


```cpp
virtual const char* GetSpaceClassName() const
{
     return "UserMessage";
}
```

{{% note %}}
The name of the class as returned by `GetSpaceClassName` should match the name of the class in the `gs.xml` file.
    1. Make sure the class has a *default constructor**.
    2. Optional -- initialize with `null values` in the constructor:
{{%/note%}}


```cpp
UserMessage()
{
	// (GigaSpaces) Optional - initialize with null values in the class constructor
	content = "";
	id = -999;
	uid = "";
}
```

Note that these 3 fields are the ones specified in our `gs.xml` file.

Step 4. Optional -- add a smart pointer:


```cpp
typedef boost::shared_ptr<UserMessage>    UserMessagePtr;
```

Step 5. Handle the c++ serializer code generation, build the shared library (DLL) from this code, and place the library in the appropriate directory (`<XAP Root>\lib\platform\native`).
The following instructions show you how to do this in Visual Studio using the supplied makefile (`<Example Root>/makefileSerializer.mk`):

Step 6. Create a custom build for `PocoFromExistingClass.gs.xml`:

![cpp_exisitng_xmlPropertiesSelect.PNG](/attachment_files/cpp_exisitng_xmlPropertiesSelect.PNG)

![cpp_exisitng_xmlPropertiesCommandLine.PNG](/attachment_files/cpp_exisitng_xmlPropertiesCommandLine.PNG)

Step 7. Type the following lines in the **Command Line** text box:


```bash
"$(XAP_HOME)/cpp/bin/$(PLATFORM)\$(COMPILER)/gsxml2cpp" "$(InputPath)" NA "$(InputDir)\UserMessageSerializer.cpp" DummyHeaderFile
  nmake -f makefileSerializer.mk
```

{{% info %}}
In Win64 use `makefileSerializer_win64.mk`
{{%/info%}}

The first line is responsible for c++ code generation, and the second line is responsible for build and placement.

{{% note %}}
The makefile specifies the target name for the generated serializer `.cpp` file and the serializer DLL. If you wish to use your own name then edit the file and change the value for `PROJNAME`.
{{%/note%}}

Step 8. **Rebuild** the example.

{{% info %}}
Compiling the file `PocoFromExistingClass.gs.xml` causes the following:

- The file `UserMessageSerializer.cpp` is automatically generated in `<Example Root>`.
- The DLL `UserMessageSerializer.dll` is created and placed in `<XAP Root>\lib\platform\native\`.
{{% /info %}}

Step 9. Run the example. The output should be as follows:

![cpp_exisitng_expectedOutput.png](/attachment_files/cpp_exisitng_expectedOutput.png)
