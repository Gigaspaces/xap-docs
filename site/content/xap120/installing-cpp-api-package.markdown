---
type: post120
title:  Installation
categories: XAP120,PRM
parent: xap-cpp.html
weight: 150
---

{{% ssummary %}}{{% /ssummary %}}



XAP's c++ API supported on Linux and Windows 64bit machines.
The current supported platforms and compilers are:

- Linux
    -  gcc.4.1.2
- Windows
    - 64bit -- VS 10.0 (Visual Studio 2010)


# Installation

1. Download the c++ API file that suits your platform.
1. Unzip the file into your `<XAP Root>` directory (download GigaSpaces [here](http://www.gigaspaces.com/LatestProductVersion)), using your favorite unzip tool (.e.g WinZip). For example - On linux you should run the following to install the C++ libraries:


```bash
tar -xzvf gigaspaces-cpp-{{%version "xap-version" %}}-ga-linux-amd64-gcc-4.1.2.tar.gz
```

Where the `gigaspaces-cpp-9.X.X-ga-linux-amd64-gcc-4.1.2.tar.gz` should be located at the `/gigaspaces-xap-premium-9.X.X-ga` folder.

1. After unzipping the ZIP file, you should have the following files and folders under your `<XAP Root>\cpp` folder:

{{% indent %}}
![CppTree.PNG](/attachment_files/CppTree.PNG)
{{% /indent %}}

{{%anchor setting-the-environment %}}

# Setting the Environment

There are several environment settings that are necessary to build and run the examples that come with this package. This section lists these settings. Choose the tab below that matches your platform.

{{%tabs%}}
{{%tab "  Windows "%}}

## Environment Variables

The following environment variables need to be defined:

- **`JSHOMEDIR`** -- the `<XAP Root>` directory.
- **`PLATFORM`** -- the build platform, in this case win32 or win64.
- **`COMPILER`** -- the compiler used for building, for example: VS9.0.
- **`PATH`** -- This should include the compiler folder , GigaSpaces gsxml2cpp location and the jvm.dll location

For example:


```bash
set JSHOMEDIR=C:\{{%version "gshome-directory" %}}
set PLATFORM=win32
set COMPILER=VS9.0
```

In addition, for windows 32 bit the **`PATH`** variable should be updated to include:

```bash
%JSHOMEDIR%\cpp\lib\%PLATFORM%\%COMPILER%;%JSHOMEDIR%\cpp\bin\%PLATFORM%\%COMPILER%;%JAVA_HOME%\jre\bin\client
```

For windows 64 bit the **`PATH`** variable should be updated to include:


```bash
%JSHOMEDIR%\cpp\lib\%PLATFORM%\%COMPILER%;%JSHOMEDIR%\cpp\bin\%PLATFORM%\%COMPILER%;%JAVA_HOME%\jre\bin\server
```

## Environment Script Files

If you don't want to set these variables globally (by defining System Variables) then the GigaSpaces C++ package includes the following script files that help set the environment:

- **`<XAP Root>\cpp\env.cmd`** -- Running this file defines these variables to match your platform.
- **`<XAP Root>\cpp\GigaVisualStudio.bat`** -- Running this file starts **Visual Studio** and automatically sets the environment.

{{% tip %}} You might need to edit these files to include the correct values for the `PATH` , `JAVA_HOME` and `JSHOMEDIR` environment variables and the correct location of Visual Studio and the jvm.dll.{{%/tip%}}

{{% /tab %}}
{{%tab "  Linux "%}}

## Environment Variables

The following environment variables need to be defined:

- **`JSHOMEDIR`** -- the `<XAP Root>` directory.
- **`PLATFORM`** -- the build platform, in this case linux-amd64 or linux32.
- **`COMPILER`** -- the compiler used for building, for example: gcc-4.1.2.

Example:

    JSHOMEDIR=../../..
    PLATFORM=linux-amd64
    COMPILER=gcc-4.1.2

## Environment Script Files

If you don't want to set these variables globally the GigaSpaces C++ package includes the following script file that help set the environment:

- **`<XAP Root>/cpp/setenv.sh`** -- Running this file defines these variables to match your platform.
{{% /tab %}}
{{% /tabs %}}

# Testing the Installation

The package provides the following scripts (placed in `<XAP Root>\cpp`):

- **sanity** -- Run sanity tests on embedded, remote and partitioned space
- **runBenchmark** -- Run benchmark tests on embedded space
- **runTest** -- Run all functional tests on embedded space
- **testJiniTransactions** -- Run benchmark and functional tests on partitioned space using Jini Mahalo transactions

Result files can be found in `<XAP Root>\cpp`:

`benchmarkResult*.txt`  -- Benchmark tests results
`testResult*.xml` -- Functional tests results


# Server installation

When installing XAP on a server the following  package needs to be installed according to your version:

[Microsoft Visual C++ 2008 SP1 Redistributable Package (x86)](http://www.microsoft.com/en-us/download/details.aspx?id=5582) <br>
[Microsoft Visual C++ 2008 SP1 Redistributable Package (x64)](http://www.microsoft.com/en-us/download/details.aspx?id=2092)

[Microsoft Visual C++ 2010 SP1 Redistributable Package (x86)](http://www.microsoft.com/en-us/download/details.aspx?id=8328)<br>
[Microsoft Visual C++ 2010 SP1 Redistributable Package (x64)](http://www.microsoft.com/en-us/download/details.aspx?id=13523)

