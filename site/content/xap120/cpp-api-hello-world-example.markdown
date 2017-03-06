---
type: post120
title:  Hello World
categories: XAP120,PRM
parent: cpp-api-examples.html
weight: 100
---


{{% ssummary%}}{{% /ssummary %}}



The example below demonstrates interaction between a sample application and a space.

{{% note %}}
The example source code can be found under: `<XAP Root>\cpp\examples\HelloWorld`.
{{%/note%}}

{{% refer %}}To learn about GigaSpaces C++ API, refer to the [XAP C++ API](./cpp-space-interface.html) section.{{% /refer %}}



## Prerequisites

### JAVA_HOME

The `JAVA_HOME` environment variable must be set to point to the appropriate JDK version.

{{% note %}}
The JDK version must match the GigaSpaces version. GigaSpaces support JDK 6/7.
{{%/note%}}

### GigaSpaces Installation

XAP needs to be installed in your local directory. Make sure that this version matches the JDK version installed on your machine.

To verify the JVM version, run the following command: `>java -version`

{{% refer %}}For more details, refer to the [Installing GigaSpaces](./installation.html) section.{{% /refer %}}

### C++ API installation

{{% refer %}} See the [Installing CPP API Package](./installing-cpp-api-package.html) section.{{% /refer %}}

## Building and Running

You can build and run the application using the provided scripts or using the Visual Studio IDE.

{{%tabs%}}
{{%tab "  Using Scripts "%}}

{{% info %}}
`<Example Root>` below refers to the path: `<XAP Root>\cpp\examples\HelloWorld`.
{{%/info%}}

Step 1. Compile the example by running: `<Example Root>\bin\compile.sh/bat`. You should run this script from the `<Example Root>\bin` directory.
Step 2. Execute `<Example Root>\bin\startAll.sh/bat`. This starts a light version of the server with one space instance, and the Jini Lookup Service.

{{% anchor step2 %}}


If you want to start an **embedded space**, there is no need to perform this step.

Open `<Example Root>\bin\run_HelloWorld.bat/.sh` in a text editor, and replace `jini://` with `java://`.

For example, in Windows:-

Replace:

    "%XAP_HOME%\cpp\bin\%PLATFORM%\%COMPILER%\HelloWorld" jini://localhost/*/mySpace

with:

    "%XAP_HOME%\cpp\bin\%PLATFORM%\%COMPILER%\HelloWorld" java://localhost/./mySpace

Changing the space URL protocol to Java causes the application to launch a space collocated in the same process, instead of connecting to a remote one.

Another way to activate the example with an embedded space is:

    "%XAP_HOME%\cpp\bin\%PLATFORM%\%COMPILER%\HelloWorld" /./mySpace



Step 3. If you want to view the space in the [XAP Browser]({{%currentadmurl%}}/gigaspaces-management-center.html), execute `<XAP Root>\bin\gs-ui.sh/bat`.
Step 4. To start the sample application, execute `<Example Root>\bin\run_HelloWorld.sh/bat`.
Step 5. This is what you should see:

{{% indent %}}
![GigaSpacesCPP_BuildingandRunning_UsingScripts.jpg](/attachment_files/GigaSpacesCPP_BuildingandRunning_UsingScripts.jpg)
{{% /indent %}}

Here is an example for a linux makefile:


```bash
CXXFLAGS = -fPIC -rdynamic -c -w -shared

OBJS = HelloWorldMessage.o

DEF = -DACE_AS_STATIC_LIBS -D__RENTRANT -DHAVE_CONFIG_H

LIBS = "-L$(XAP_HOME)/cpp/lib/$(PLATFORM)/$(COMPILER)" \
 "-L$(XAP_HOME)/cpp/open-source/platform-libs/$(PLATFORM)/$(COMPILER)" \
 "-L$(JAVA_HOME)/jre/lib/amd64/server/" \
 -lgscpplib -lACE -lxerces-c \
 -lpthread \
 -ldl \
 -lnsl

INCL = "-I$(XAP_HOME)/cpp/include"\
 "-I$(XAP_HOME)/cpp/open-source/platform-includes/$(PLATFORM)"\
 "-I$(XAP_HOME)/cpp/open-source/platform-independant-includes"\
 "-I$(XAP_HOME)/cpp/examples/HelloWorld/serializer"\

%.o:%.cpp
	$(CC) $(CXXFLAGS) $(INCL) -c $<

TARGET = "$(XAP_HOME)/lib/platform/native/libHelloWorldMessage.so"

$(TARGET):	$(OBJS)
	g++ -shared -Wl $(INCL) -o $(TARGET) $(OBJS) $(LIBS)

all: $(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
```

{{% note "Debugging in Linux "%}}
When debugging the code in Linux, you may receive the signal SIGSEGV (segmentation fault). It is recommended to instruct the debugger to ignore these signals (`handle SIGSEGV nostop noprint` in gdb, `ignore SIGSEGV` in most versions of dbx).
{{% /note %}}
{{% /tab %}}
{{%tab "  Using Visual Studio "%}}


Step 6. Start Visual Studio using the `<XAP Root>\cpp\GigaVisualStudio.bat` script.
{{% exclamation %}} You might need to edit this file (`GigaVisualStudio.bat`) and the file it calls (`env.cmd`) to include the correct values for the `JAVA_HOME` and `XAP_HOME` environment variables and the correct location of Visual Studio.

Step 7. Once Visual Studio is started, open the examples solution `<XAP Root>\cpp\examples\examples.sln`:

{{% indent %}}
![hello_CPP1.jpg](/attachment_files/hello_CPP1.jpg)
{{% /indent %}}

Step 8. Make sure the Hello World project appears - make it the startup project:

{{% indent %}}
![hello_CPP2.jpg](/attachment_files/hello_CPP2.jpg)
{{% /indent %}}

Step 9. Modify the Hello World project to have the following as the Command Arguments (`jini://localhost/*/mySpace`):

{{% indent %}}
![hello_CPP3.jpg](/attachment_files/hello_CPP3.jpg)
{{% /indent %}}

1. Execute `<Example Root>\bin\startAll.sh/bat`. This starts a light version of the server with one space instance, and the Jini Lookup Service.

{{% info %}}
If you want to start an **embedded space**, there is no need to perform the last 2 steps. Running the Hello World example without any Command Arguments automatically launches a space collocated in the same process.

{{% indent %}}
![hello_CPP4.jpg](/attachment_files/hello_CPP4.jpg)
{{% /indent %}}
{{% /info %}}

Step 10. Make sure that Hello World is the Startup Project:

{{% indent %}}
![hello_CPP5.jpg](/attachment_files/hello_CPP5.jpg)
{{% /indent %}}

Step 11. Make sure that Hello World is configured to run in the `Release` solution configuration mode.
Step 12. **Rebuild** the Hello World project.
Step 13. Run the Hello World Application:

{{% indent %}}
![hello_CPP6.jpg](/attachment_files/hello_CPP6.jpg)
{{% /indent %}}

Step 14. This is what you should see:

{{% indent %}}
![hello_CPP7.jpg](/attachment_files/hello_CPP7.jpg)
{{% /indent %}}

{{% tip %}}
To reduce the compilation time of the examples you may remove the **TestRead1** and **TestRead1Classes** projects from the examples project.
{{% /tip %}}

{{% /tab %}}
{{% /tabs %}}

## Environment Settings

The above scripts and the Visual Studio solution define several environment settings that are necessary to build and run the example. This section lists these settings. Choose the tab below that matches your platform.

{{%tabs%}}
{{%tab "  Windows "%}}

## Environment Variables

The following environment variables need to be defined:

- `XAP_HOME` -- the `<XAP Root>` directory.
- `PLATFORM` -- the build platform, in this case win32 or win64.
-  `COMPILER` -- the compiler used for building, for example: VS9.0.

For example:

    set XAP_HOME=C:\gigaspaces-xap-8
    set PLATFORM=win32
    set COMPILER=VS9.0

In addition, the variable `PATH` should be updated to include:

    %XAP_HOME%\cpp\lib\%PLATFORM%\%COMPILER%;%XAP_HOME%\cpp\bin\%PLATFORM%\%COMPILER%;%JAVA_HOME%\jre\bin\client

{{% note %}}
You can also run the `<XAP Root>\cpp\env.cmd` file to define these variables to match your platform.
{{%/note%}}

## Additional Include Directories

(Properties window -> Configuration Properties -> C/C++ -> General -> Additional Include Directories)

    "$(XAP_HOME)\cpp\include";
    "$(XAP_HOME)\cpp\open-source\platform-independant-includes";
    "$(XAP_HOME)\cpp\open-source\platform-includes\$(PLATFORM)";
    "$(XAP_HOME)\cpp\examples\helloWorld\serializer"

## Additional Dependencies

(Properties window -> Configuration Properties -> Linker -> Input -> Additional Dependencies)

    gscpplib.lib

{{% /tab %}}
{{%tab "  Linux "%}}

## Environment Variables

The following environment variables need to be defined:

- `XAP_HOME` -- the `<XAP Root>` directory.
- `PLATFORM` -- the build platform, in this case linux-amd64 or linux32.
-  `COMPILER` -- the compiler used for building, for example: gcc-4.1.2.

Example:

    XAP_HOME=../../..
    PLATFORM=linux-amd64
    COMPILER=gcc-4.1.2

{{% note %}}
You can also run the `<XAP Root>\cpp\setenv.sh` file to define these variables to match your platform.
{{%/note%}}

## Additional Include Paths

    -I$(XAP_HOME)/cpp/include\
    -I$(XAP_HOME)/cpp/open-source/platform-includes/$(PLATFORM)\
    -I$(XAP_HOME)/cpp/open-source/platform-independant-includes\
    -I$(XAP_HOME)/cpp/examples/HelloWorld\
    -I$(XAP_HOME)/cpp/examples/HelloWorld/serializer\

## Additional Libraries

Paths:

    -L$(XAP_HOME)/cpp/lib/$(PLATFORM)/$(COMPILER) \
    -L$(XAP_HOME)/cpp/open-source/platform-libs/$(PLATFORM)/$(COMPILER) \
    -L$(JAVA_HOME)/jre/lib/amd64/server/

{{% info%}}
The last path might differ from the example above, depending on your Java installation.
{{%/info%}}

Libraries:


```bash
-lgscpplib -lACE -lxerces-c -ljvm
```

{{% /tab %}}
{{% /tabs %}}

# Code Walkthrough



{{% note %}}
`<Example Root>` in the text below refers to `<XAP Root>/cpp/examples/HelloWorld`.
{{%/note%}}

This application is a simple client that connects to a remote space, writes a data object into that space and then reads it back. We start by defining the properties of this data object which we refer to as a `Message`.

## Generating Message Class from gs.xml

The `Message` class used in the example contains only three fields: `id` (int), `uid` (string) and `content` (string). The `uid` field is used as a primary key, the `id` field is used for routing in a partitioned space, and the `content` field contains the message text.

The following XML code represents this `Message` object (located at `<Example Root>\serializer\HelloWorld.gs.xml`):


```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE gigaspaces-mapping SYSTEM "../../../config/cpp.dtd">
<gigaspaces-mapping>
  <class name="Message" persist="false" replicate="false" fifo="false" >
    <property name="id" type="int" null-value="-999" index="true"/>
    <routing  name="id"/>

    <property name="uid" type="string" null-value="" index="true"/>
    <id name="uid" auto-generate="true" />
    <property name="content" type="string" null-value="" index="false"/>
  </class>
</gigaspaces-mapping>
```

{{% info %}}
The `id` tag defines the `uid` field as the primary key, while the `routing` tag defines the `id` field used for routing. For more details, refer to the [gs.xml reference](./cpp-api-mapping-file.html) section.
{{%/info%}}

This XML file is used by the `gsxml2cpp` command that produces the `HelloWorldMessage.h` and `HelloworldMessage.cpp` files. This is performed by the following command:


```bash
gsxml2cpp ../serializer/helloWorld.gs.xml HelloWorld ../serializer/HelloWorldMessage.cpp ../serializer/HelloWorldMessage.h
```

`HelloWorldMessage.h` contains the class declaration and `HelloworldMessage.cpp` contains serialization code that shouldn't be edited. You can use an existing class instead of having it generated by `gsxml2cpp`. For more details, refer to the [Writing Existing CPP Class to Space](./cpp-writing-existing-class-to-space.html) section for more details.

A shared library (DLL or SO file) is created from `HelloWorldMessage.h/cpp` files. This shared library is copied into the `<XAP Root>\lib\platform\native` directory, so that it can be loaded at runtime by the space.

{{% note %}}
Each entry class name must be unique over XAP installation.
{{%/note%}}

## Connecting to Space

In order to connect to the remote space, we call the `find()` method (which is a member of `OpenSpaces::SpaceFinder`), and pass a space URL:


```java
SpaceProxyPtr space ( finder.find(spaceUrl) );
```

(The above is copied from the file: `<Example Root>\HelloWorld.cpp`.)

{{% note %}}
The space URL in this example is: `jini://localhost/*/mySpace?groups=javaspaces-hellospace`. It is passed to `main` in `run_HelloWorld.sh/.bat)` (see [Space URL](./the-space-configuration.html)).
{{%/note%}}

{{% info %}}
This space URL specifies the network address of the Jini Lookup Service (localhost here), and queries it for **a space belonging to the lookup group** `javaspaces-hellospace`. There is only one space in this lookup group: the space instance started by `startAll.sh`. The space is purposely started with a switch that registers it to this lookup group.
The **asterisk after localhost** represents the space container (a logical unit inside the server); this URL does not refer to a specific container. Note that the protocol used is Jini (other protocols are RMI and Java. Java is used for embedded spaces and to launch spaces on the server side).
{{%/info%}}

## Registering C++ Class to Space

The space must know about the data class that we declared previously. Thus, we need to register it to the space in the following way:

    Message messageTemplate;
    space->snapshot(&messageTemplate);

## Writing Object to Space

The application constructs an object called `msg` and **writes it to the space** by passing it as the first argument in the `SpaceProxy::write()` method. The second argument refers to the transaction object -- none in this example, the third defines the lease time.


```cpp
Message_ptr msg( new Message() );
msg->id = 1;
msg->content = "Hello World";
space->write(msg.get(), NULL_TX, 5000000);
```

(The above is copied from the file: `<Example Root>\HelloWorld.cpp`.)

The `msg` object is of type `Message`. This is a class that satisfies the requirements of the space: it implements `OpenSpaces::IEntry`, has a default constructor, and a virtual function that returns the class space name.


```cpp
class Message: public IEntry
{
public:
    Message() {
        content = "";
        id = -999;
        uid = "";
    }
    std::string			content;
    int			id;
    std::string			uid;
    virtual const char* GetSpaceClassName() const
    {
        return "Message";
    }

};
```

(The above is copied from the file: `<Example Root>\serializer\HelloWorldMessage.h` -- the header generated by `gsxml2cpp`.)

## Creating Template

The application creates an object, referred to as a template. This object is essentially the filter criteria used to query the space for matching objects. The template must be of a registered type, in this case, an object of type `Message`. This template has all attributes as `null`, which tells the space to return an object of type `Message`, regardless of its attribute values (if there are multiple objects matching the template, the space returns one of them).


```cpp
Message messageTemplate;
```

(The above is copied from the file: `<Example Root>\HelloWorld.cpp`.)

If you want to perform more specific template matching, assign specific values to the template object's attributes.

If you set `template.attribute1="value"`, the space returns an object of this class for which `attribute` equals to the value. When using a regular template, you can perform equality queries with `AND` conditions. For bigger/less-than queries or ranges, or more complex queries using `OR` conditions; use the `SQLQuery` class.

{{% refer %}}For more details on SQL queries, refer to the [SQLQuery](./query-sql.html) section.{{% /refer %}}

You can use the inheritance mechanism to match derived object-enabling queries in different level of abstraction.

## Passing Template to Space

The template is passed to the space in the first argument of the `SpaceProxy::read()` method. The second argument defines a transaction.

The third argument defines the wait time (block time) for read operations. This argument is set to `0`, meaning no wait. In this example, since we have written a matching object to the space prior the read operation, the application returns immediately with the desired object data, so the wait time is unimportant. But in real life, applications often read data that was supposed to be written or updated by other applications -- in which case the block time represents the period during which the data is expected to enter the space. If the block time is long, the application might block for a long time, waiting for the object to appear.


```cpp
Message_ptr result ( space->read(&messageTemplate, NULL_TX, 0) );
```

(The above is copied from the file: `<Example Root>\HelloWorld.cpp`.)

## Receiving Returned Message Object

In this example, the `read()` method returns the requested `Message` object (because it is written previously by the same application), and the application displays its content. If the object does not exist, the application returns immediately with a `NULL` value.

Alternatively, you can perform the read operation with a timeout. The application returns `NULL` in case a matching object has not been written to the space during the specified timeout.

