---
type: post120
title:  Overview
categories: XAP120,PRM
parent: xap-cpp.html
weight: 100
---

{{% ssummary %}}{{%/ssummary%}}

{{%section %}}
{{%column width="80%" %}}
GigaSpaces C++ applications may use the [CPP Processing Unit](./cpp-processing-unit.html), which utilize the concept of Space-Based Architecture (SBA) by allowing simple development, packaging and deployment of application services together with middleware services.
The C++ Processing Unit runs as an independent service, allowing you to distribute and parallelize your processing on several machines, thus achieving higher performance, lower latency, and scalability.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/cpp-SBA-system-archi.jpg"%}}
{{%/column%}}
{{%/section%}}

The GigaSpaces c++ API has been designed to address the following:

{{%vbar%}}
- **Being c++ friendly** -- allowing c++ engineers to use standard tools.
- **Ease of use** -- the ability to build C++-based applications in a matter of minutes.
- **Platform Support** -- to run on 32 and 64-bit platforms.
- **Better performance** -- to have similar or better performance than the Java-based clients.
- **Better Interoperability** -- to cope with space classes that contain nested classes.
- **Common development flow**, **configuration** and **deployment based on OpenSpaces** -- **one** common space runtime for all applications: Java, .NET and C++.
- **Using an alternative approach** for **internal JNI calls** -- based on lightweight command pattern protocol.
{{%/vbar%}}

## Features

GigaSpaces c++ API offers a very straightforward API that directly accepts the developers c++ objects. No additional serialization or marshalling code needs to be developed and maintained.


{{%vbar "The GigaSpaces c++ API provides the following:"%}}

- **Single space operations and batch space operations**.
- **Space transactions support**.
- **Space notifications support**.
- **Reduced Space lease support** (without the ability to renew or cancel).
- **Interoperability support** -- POJO and PONO data-sharing and exchanging of data with C++ IEntry objects via the space.
- **Support for portable serialization of non-primitive C++ IEntry attributes** -- passing Java objects to c++ business logic having objects with nested classes and complex graphs(without loops and duplicated references) as C++ IEntry attributes.
- **Space statistics support**.
- **SQL query support** for read, take, and notify operations(SQL blocking take not supported).
- **Space administration API support (count, clean, start, stop)**.
- **Space c++ Worker/bean support** -- allows a c++ developer to embed c++ business logic, so it can run in the same memory address as the SLA container and the space. This allows c++ business logic to have in and out of process scalability capabilities.
- **Collection API** (`stl::vector`).
- **gs.xml** support and `gsxml2cpp` code generator** -- generate C++ IEntry, POJO, PONO using one XML file enabling fast Interoperability process.
- **Dynamic casting** -- no need to cast returned objects to relevant types.
- **Optimized and efficient JNI calls**.
- **Out of the box examples**.
- **Boost Smart pointer interface - Alternative interface for C++ developers that uses only boost smart pointers**.

{{%/vbar%}}


## How it Works

c++ business logic can run as a standalone application or can be deployed into the Grid accessing the space in any of the supported runtime topologies: single space, clustered space, remote, or embedded.

When the c++ application interacts with the space, all relevant space components: space filter, replication filter, space data provider (known also as [Space Persistency](./space-persistency.html) or read/write through), and the mirror service (known also as write-behind) are active and available. In order to access space data originated by CPP business logic and POCOs, these interfaces should be implemented using Java code and relevant POJO classes.

Data written into the space can be monitored and viewed using the standard GUI and CLI tools. These same tools can be used to monitor and view data stored in the space with Java or .NET applications.

Running c++ classes in a SBA (Space Based Architecture) environment is just as easy as it is in a pure Java environment -- the developer provides the Processing Unit configuration with the relevant SLA, and deploys it into the Grid.

The POCO API is a micro version of the [GigaSpaces OpenSpaces API](./the-gigaspace-interface.html); it supports the read, write, take and notify paradigm of SBA.

In order to allow interoperability of POCO objects with both POJO (Java) and PONO (.Net) counterparts error-free, you should generate matching Java and .Net classes used to interact with the space. All transformations between these objects will be done transparently using the lightweight GigaSpaces PBS protocol.

# Architecture

#### Standalone c++ Application

{{%section %}}
{{%column width="80%" %}}
When the c++ application is running as a standalone application, the c++ business logic interacts with the space via remote calls. All space operations are conducted using c++ objects, where the actual interaction is performed via native PBS objects. The c++ objects are transformed to PBS objects in runtime, and sent to the space via the C+/\+ and Java runtime layers.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/cpp_image004.jpg"%}}
{{%/column%}}
{{%/section%}}

#### c++ Worker
{{%section %}}
{{%column width="80%" %}}
When the c++ business logic runs as a worker, collocated with the space; no remote calls are involved when interacting with the space. Interactions with the space are done using c++ objects, similar to the standalone c++ application configuration.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/attachment_files/cpp_image006.jpg"%}}
{{%/column%}}
{{%/section%}}

# POCO

The POCO is a wordplay on the famous POJO acronym and it stands for Plain Old c++ Object.

It is essentially a c++ class that includes attributes and business logic, where its instances (their data) can be manipulated by the space. With such c++ objects, data can be stored within the space, updated, removed or can trigger notifications. There is no need to re-write your domain classes or to build marshaling/de-marshaling code (which was required in previous versions of GigaSpaces c++ API) so that they can communicate with the space.

In general, you need to simply introduce your c++ class to the space using a standard XML file (that can be created manually or automatically in some cases), and call the relevant API explicitly or cal your business logic implicitly (this will be further explained below).

# Space Class Metadata

On top of the c++ class itself, which includes the attribute names and types, additional information is required for the space class. This information can include the indexed fields, FIFO mode, versioned mode, replicable mode, etc.

To introduce this additional metadata to the space, the c++ engineer provides a simple XML-based configuration file (the `gs.xml`). This file is parsed at pre-compile time and allows a code generator facility to create a piece of code that "glues" between the c++ runtime and the space runtime (the marshaling code).

{{% refer %}}For more details, refer to the [CPP API Code Generator](./cpp-api-code-generator.html) section.{{% /refer %}}

Here is a simple example of the `gs.xml` content:


```xml
<?xml version="1.0" encoding="UTF-8"?>
<gigaspaces-mapping>
  <class name="Message" persist="false" replicate="false" fifo="false" >
    <property name="id" type="int" null-value="-999" index="true"/>
    <routing  name="id"/>
    <property name="uid" type="string" null-value="" index="false"/>
    <id name="uid" auto-generate="true" />
    <property name="content" type="string" null-value="" index="false"/>
  </class>
</gigaspaces-mapping>
```

The same metadata decoration file is used across all languages -- the same `gs.xml` file can be used to introduce your language's independent class to the space.

Here is the c++ `h` file that is generated or should be used with the above `gs.xml` file:


```cpp
class Message: public IEntry
{
public:
    Message() {
        content = "";
        id = -999;

        uid = "";
    }
                std::string                              content;
                int                                           id;
                std::string                              uid;
                virtual const char* GetSpaceClassName() const
                {
                   return "Message";
                  }
};
typedef boost::shared_ptr<Message>    Message_ptr;
```

Here is the c++ code using the above class:


```cpp
SpaceProxyPtr space ( finder.find("jini://*/*/mySpace") );
Message_ptr msg( new Message() );
msg->id = 1;
msg->content = "Hello World";
space->write(msg, NULL_TX, 5000000);

Message messageTemplate;
Message_ptr result (space->take(&messageTemplate, NULL_TX, 0 ));

The SpaceProxyPtr includes all the familiar Space operations such as
write , read , take , update for single and batch operations , SQL Query , Event Notifications, Transactions etc.
```

As you can see, the example above uses `boost::shared_ptr`. If you are not familiar with boost, see: [http://www.boost.org/](http://www.boost.org/).

# Using Existing c++ Classes with the Space

To use existing c++ classes with the space, you need to perform minor changes to your existing c++ classes (such as inheriting from the `IEntry` base class). This allows you to implement your own serialization and data transport protocol to gain total control of the byte stream content sent through the wire when the client interacts with the space process.

{{% refer %}}For more details, see the [Writing Existing C++ Class to Space](./cpp-writing-existing-class-to-space.html) section.{{% /refer %}}

# Interoperability -- Sharing Data across Java, .NET and c++ Applications

The most complex part of interoperability is the ability for every class structure to digest each development language, and to include one common denominator that is understood by the space runtime.

There are several ways to achieve this, for example, using XML to store data and convert it to native language objects. However, we have found this option irrelevant for real-time SBA applications our users are building. They need fast data conversion and transportation services that are able to handle objects from all supported languages on all platforms in an optimized and efficient manner -- meaning, micro-second latency. GigaSpaces has achieved this goal and has named it Portable Binary Serialization (PBS).

The PBS mechanism efficiently scans, reduces, and serializes complex objects from different languages; and translates them into a format known by all supported runtimes: .NET, C++, and Java.

With the PBS approach, a POCO class (that is a graph object) holds references to other objects or arrays of objects. This object can be stored in the space in a way that a Java application using a matching POJO can read the data and materialize it into a pure Java object. The same is true with .NET.

# Dynamic Scalability

The main problem with Grid-based applications and such heterogeneous, complex environments is the inability to forecast how many machines need to store the data or run the business logic. Without a smart container for the data and business logic that can handle the application objects (and not only network processes), it's almost impossible to scale your application efficiently. You need a nervous system with sensors that instruct data and business logic instances to started, shut down, or move to another machine to scale dynamically. In a sense, you have 2 dimensions to scale: in-proc and out-of-proc. In-proc means multiple threads of your business logic running within the same process. Out-of-proc means starting new instances of your business logic across multiple Grid nodes (on the same machine or different machines). In both cases, you need the ability to increase/decrease the amount of business logic instances in runtime, based on some SLA of dynamic external business logic.

The space has already introduced Java based-business logic -- scaling your application by adding more containers on-the-fly to increase space cluster capacity. Java-based in/out-of-proc scalability is supported via Jini service beans and expanded also to Spring beans. This is one of the main capabilities of the [OpenSpaces](/product_overview/product-architecture.html#ProductArchitecture-OpenSpacesAPIandComponents) framework. The new c++ framework allows you to do the same with c++ business logic.

{{% refer %}}
See the [Elastic Processing Unit](./elastic-processing-unit.html) section for details about Dynamic Scalability.
{{% /refer %}}

# Deployment -- Standalone and Grid-Based

Deploying a c++ SBA application involves the standard c++ runtime libraries, the `gs.xml` files, and the GigaSpaces runtime libraries (C++, Java, and JDK libraries).

These can be packaged using an installer that simplifies the deployment routine. You can place the runtime libraries in a shared folder, so you won't need to distribute the files to every machine running an application that accesses the space. Another option is to can encapsulate the business logic as a [Processing Unit](./cpp-processing-unit.html) to be managed, deployed and executed across the Grid nodes by the SLA-driven container. This allows you to control the different c++ business logic instances to access remote spaces or collocated spaces (c++ business logic running in the same memory address as the space, allowing you to avoid remote calls).

# Configuration

Beyond the `gs.xml` file creation and the serializer library compilation (compile the generated code as a custom build event as part of your Visual Studio project), there is very little to do in order to run your c++ application and start working with the space.

You can run the c++ application in remote mode -- access a clustered space running across the Grid, or start the space instance within your c++ process.

# Compatibility

GigaSpaces c++ API is compatible with other important GigaSpaces components and tools:

- The GigaSpaces Management Center, which is being significantly improved to support large clusters, displays your c++ classes metadata, and allows you to query c++ objects.
- Write/read-through and write-behind (Mirror Service) are supported. POCO objects can be delegated into your database using your existing database schema.
- Relevant Java-based interceptors such as replication and space filters can be used for events generated by c++ API activities.

# Packaging

The GigaSpaces c++ package includes the following components:

- **Examples** -- this is in fact most of the package. It includes a simple hello world example, c++ benchmark, and a full c++ SBA example. For more details, see the [c++ homepage](./xap-cpp.html).
- **Documentation** -- Doxygen ([http://www.stack.nl/~dimitri/doxygen](http://www.stack.nl/~dimitri/doxygen)) and CHM-based API reference documentation.
- Open-source and GigaSpaces API `h` files.
- **Runtime libraries** -- core c++ API, memory allocator library, and a dynamic loader library used when deploying your c++ application into the Grid.
- **Compile scripts and source code** -- the GigaSpaces c++ package is provided out-of-the-box for Windows and Linux 32 and 64-bit platforms. If you are using other platforms, such as MaxOS, HP, AIX, etc., it is possible to build the c++ libraries on your own. Full source code and compile scripts can be provided.
- **Regression tests framework** -- open-source code of GigaSpaces c++ framework tests, based on [CUnit ](http://cunit.sourceforge.net/), allows you to run regression tests to make sure the libraries provided work as expected.

## Libraries

GigaSpaces c++ API uses [ACE](http://www.cs.wustl.edu/~schmidt/ACE-overview.html) and [Boost](http://www.boost.org/) platform independent libraries.

These provide out of the box smart pointers, threading, singletons, collections, and many more capabilities provided for all platforms.

## Internal Library Architecture

The GigaSpaces c++ libraries are constructed in several layers:

- **Space API access layer** -- The c++ proxy implementation.
- **c++ object materialization layer** -- Involves generation of PBS objects and the ingoing and outgoing activity with the space runtime.
- **Runtime layer** -- The client's c++ proxy handles client-side activities, such as transaction handlers, space proxy handlers, etc.
