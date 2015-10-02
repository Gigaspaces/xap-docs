---
type: post100net
title:  XAP.NET in 5 Minutes
categories: XAP100NET
parent: tutorials.html
weight: 100
---

{{% ssummary  %}}{{% /ssummary %}}



This tutorial explains how to deploy and use an XAP [Data Grid](/product_overview/the-in-memory-data-grid.html) from a .NET client application.



{{%vbar "Download and Install XAP"%}}
GigaSpaces XAP.NET is packaged as a standard Windows Installer package (.msi file). After you have downloaded the latest version of XAP from the [downloads page](http://www.gigaspaces.com/xap-download), start the installation by double-click the msi file, and the installation wizard will pop up and guide you through the installation process.

Once you accept the licence agreement, you will be asked to choose a setup type. Select 'Complete' to install all the features in the default path (C:\GigaSpaces\XAP.NET X.x.x). Selecting 'Custom' will allow you to customize the installation path, which features will be installed, and more.
{{%/vbar%}}

# Starting a Service Grid

A Data Grid requires a [Service Grid](/product_overview/service-grid.html) to host it. A service grid is composed of one or more machines (service grid nodes) running a [Service Grid Agent](/product_overview/service-grid.html#gsa) (or `GSA`), and provides a framework to deploy and monitor applications on those machines, in our case the Data Grid.

In this tutorial you'll launch a single node service grid on your machine. To start the service grid, simply run the `Gs-agent.exe` from the product's `bin` folder.

{{% tip title="Optional - The Web Console "%}}
XAP provides a web-based tool for monitoring and management. From the `bin` folder start `Gs-webui.exe`, then browse to `http://localhost:8099`. Click the 'Login' button and take a look at the *Dashboard* and *Hosts* tabs - you'll see the service grid components created on your machine.
{{% /tip %}}

# Deploying the Data Grid

The Data grid can be deployed from command line, from the web management tool or via an Administration API. In this tutorial we'll use the command line.

Start a command line, navigate to the product's `bin` folder and run the following command:


```bash
gs-cli deploy-space -cluster total_members=2,1 myDataGrid
```
  
This command deploys a Data Grid (aka space) called **myDataGrid** with 2 partitions and 1 backup per partition (hence the `2,1` syntax). 

If you're using the web console mentioned above to see what's going on, you'll see the data grid has been deployed.
 
{{%info%}} Note that the Lite edition is limited to a single partition - if you're using it type `total_members=1,1` instead.{{%/info%}}

# Interacting with the Data Grid


### Setting up your IDE

gshome-net-directory
Launch Visual Studio, create a new C# *Console Application* and add a reference to **GigaSpaces.Core.dll** from `C:\GigaSpaces\{{%version "gshome-net-directory"%}}\NET v4.0\Bin`. If you're new to Visual Studio and .NET, follow these instructions:

{{%accordion%}}
{{%accord title="How to create a XAP.NET Project in Visual Studio"%}}
1. Open Microsoft Visual Studio. From the **File** menu select **New > Project**. The **New Project** dialog appears.
2. In the **Project types** tree, select **Visual C#**, then select **Console Application** in the **Templates** list.
3. In the **Name** test box, type **XapDemo**. If you wish, change the default location to a path you prefer.
4. Select **OK** to continue. Visual Studio creates the project and opens the automatically generated `program.cs` file.
5. From the **Project** menu, select **Add Reference**. The **Add Reference** dialog appears.
gshome-net-directory
6. Select the **Browse** tab, navigate to the XAP.NET installation folder (e.g. **C:\GigaSpaces\{{%version "gshome-net-directory"%}}\NET v4.0**). Go into the **Bin** folder, select **GigaSpaces.Core.dll**, and click **OK**.
7. In the **Solution Explorer**, make sure you see **GigaSpaces.Core** in the project references. There's no need to reference any other assembly.

{{%/accord%}}
{{%/accordion%}}

{{%warning%}}
If you're using XAP.NET x64, note that the [default platform for Console Applications is x86](http://connect.microsoft.com/VisualStudio/feedback/details/455103/new-c-console-application-targets-x86-by-default), and you must [change it to x64](http://msdn.microsoft.com/en-us/library/ms185328.aspx).{{%/warning%}}

### Implementing a PONO

Any PONO can be stored in the space, so long as it has a default constructor and an ID property. For this tutorial let's define a `Person` class with the following properties:


```csharp
using GigaSpaces.Core.Metadata;

public class Person
{
    [SpaceID]
    public int? Ssn {get; set;}
    public String FirstName {get; set;}
    public String LastName  {get; set;}
}
```

Note that we've annotated the `Ssn` property with a custom XAP.NET attribute (`[SpaceID]`) to mark it as the entries ID.

### Interacting with the grid

First, let's establish a connection to the data grid we've deployed: 


```csharp
ISpaceProxy spaceProxy = new SpaceProxyFactory("myDataGrid").Create();
```

Now that we have a proxy connected to the data grid, we can store entries in the grid using the `Write()` method and read them using various `Read()` methods:


```csharp
Console.WriteLine("Write (store) a couple of entries in the data grid:");
spaceProxy.Write(new Person {Ssn=1, FirstName="Vincent", LastName="Chase"});
spaceProxy.Write(new Person {Ssn=2, FirstName="Johnny", LastName="Drama"});

Console.WriteLine("Read (retrieve) an entry from the grid by its id:");
Person result1 = spaceProxy.ReadById<Person>(1);

Console.WriteLine("Read an entry from the grid using LINQ:");
var query = from p in spaceProxy.Query<Person>()
            where p.FirstName == "Johnny"
            select p;
Person result2 = spaceProxy.Read<Person>(query.ToSpaceQuery());

Console.WriteLine("Read all entries of type Person from the grid:");
Person[] results = spaceProxy.ReadMultiple(new Person());
```

If you're using the web console mentioned above to see what's going on, you'll see two entries stored in the grid, one in each partition.

### {{% anchor source %}} Full Source Code

{{%accordion%}}
{{%accord title="`Program.cs`"%}}

```csharp
using System;
using System.Linq;
using GigaSpaces.Core;
using GigaSpaces.Core.Linq;

namespace XapDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Connecting to data grid...");
            ISpaceProxy spaceProxy = new SpaceProxyFactory("myDataGrid").Create();

            Console.WriteLine("Write (store) a couple of entries in the data grid:");
            spaceProxy.Write(new Person { Ssn = 1, FirstName = "Vincent", LastName = "Chase" });
            spaceProxy.Write(new Person { Ssn = 2, FirstName = "Johnny", LastName = "Drama" });

            Console.WriteLine("Read (retrieve) an entry from the grid by its id:");
            Person result1 = spaceProxy.ReadById<Person>(1);
            Console.WriteLine("Result: " + result1);

            Console.WriteLine("Read an entry from the grid using LINQ:");
            var query = from p in spaceProxy.Query<Person>()
                        where p.FirstName == "Johnny"
                        select p;
            Person result2 = spaceProxy.Read<Person>(query.ToSpaceQuery());
            Console.WriteLine("Result: " + result2);

            Console.WriteLine("Read all entries of type Person from the grid:");
            Person[] results = spaceProxy.ReadMultiple(new Person());
            Console.WriteLine("Result: " + String.Join<Person>(",", results));

            Console.WriteLine("Demo completed - press ENTER to exit");
            Console.ReadLine();
        }
    }
}
```
{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="`Person.cs`"%}}


```csharp
using System;
using GigaSpaces.Core.Metadata;

namespace XapDemo
{
    public class Person
    {
        [SpaceId(AutoGenerate=false)]
        public int? Ssn { get; set; }

        [SpaceIndex(Type=SpaceIndexType.Basic)]
        public String FirstName { get; set; }

        public String LastName { get; set; }

        public override string ToString()
        {
            return "Person #" + Ssn + ": " + FirstName + " " + LastName;
        }
    }
}
```
{{%/accord%}}
{{%/accordion%}}

