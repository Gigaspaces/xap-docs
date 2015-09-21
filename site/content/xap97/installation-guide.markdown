---
type: post97
title:  Installation Guide
categories: XAP97
weight: 150
parent: cook-books.html
---

{{%ssummary%}}{{%/ssummary%}}

We will show you how to download and install the different versions of the  XAP platform.

{{%note%}}
For a list of the supported platforms and programming language versions please consult the [release notes](/release_notes).
Please note that the XAP Lite edition is limited to a single partition.
{{%/note%}}



{{%anchor java-installation %}}

# Java Installation
Download the latest version of XAP from the [downloads page](http://www.gigaspaces.com/xap-download). Unzip it into a directory of your choice:

gshome-directory
* On Windows, you might unzip it into `c:\tools\`, which will create `c:\tools\{{%version "gshome-directory"%}}`.

gshome-directory
* On Unix, you might unzip it into `/usr/local/`, which will create `/usr/local/{{%version "gshome-directory"%}}`. You'll also need to grant execution permissions to the scripts in the bin folder.



#### JAVA_HOME
Set the JAVA_HOME environment variable to point to the JDK root directory

#### Installing a Java IDE
If you don't have an IDE installed, you can [download and unzip the Eclipse IDE for Java Developers](http://www.eclipse.org/downloads), or the [IntelliJ IDEA](http://www.jetbrains.com/idea/download/index.html) IDE (we recommend the Ultimate Edition because of its excellent Spring framework support).
If you're using Eclipse, it is also recommended to install the [Spring Tool Suite plugin for Eclipse](http://www.springsource.com/developer/sts).

#### Setting up your IDE

gshome-directory
Open your favorite java IDE (Eclipse, IntelliJ IDEA, etc), Create a new project, and add all the jars from `{{%version "gshome-directory"%}}/lib/required` to the project's classpath.



{{%anchor dotnet-installation %}}

# .NET installation

GigaSpaces XAP.NET is packaged as a standard Windows Installer package (.msi file). After you have downloaded the latest version of XAP from the [downloads page](http://www.gigaspaces.com/xap-download), start the installation by double-click the msi file, and the installation wizard will pop up and guide you through the installation process.

Once you accept the licence agreement, you will be asked to choose a setup type. Select 'Complete' to install all the features in the default path (C:\GigaSpaces\XAP.NET X.x.x). Selecting 'Custom' will allow you to customize the installation path, which features will be installed, and more.



{{%vbar title="How to create a XAP.NET Project in Visual Studio "%}}

1. Open Microsoft Visual Studio. From the **File** menu select **New > Project**. The **New Project** dialog appears.
2. In the **Project types** tree, select **Visual C#**, then select **Console Application** in the **Templates** list.
3. In the **Name** test box, type **XapDemo**. If you wish, change the default location to a path you prefer.
4. Select **OK** to continue. Visual Studio creates the project and opens the automatically generated `program.cs` file.
5. From the **Project** menu, select **Add Reference**. The **Add Reference** dialog appears.
gshome-net-directory
6. Select the **Browse** tab, navigate to the XAP.NET installation folder (e.g. **C:\GigaSpaces\{{%version "gshome-net-directory"%}}\NET v4.0.30319**). Go into the **Bin** folder, select **GigaSpaces.Core.dll**, and click **OK**.
7. In the **Solution Explorer**, make sure you see **GigaSpaces.Core** in the project references. There's no need to reference any other assembly.
{{%/vbar%}}


{{%warning%}}
If you're using XAP.NET x64, note that the [default platform for Console Applications is x86](http://connect.microsoft.com/VisualStudio/feedback/details/455103/new-c-console-application-targets-x86-by-default), and you must [change it to x64](http://msdn.microsoft.com/en-us/library/ms185328.aspx).
{{%/warning%}}


### Other Installation Options

GigaSpaces XAP.NET offers more installation scenarios and customizations. For example:

- Command-line installation.
- Packaging XAP.NET in another installation package.
- Side-by-side installations.
- Using a different jvm.

For more information see [Advanced Installation Scenarios]({{%latestneturl%}}/advanced-installation-scenarios.html).


# License installation

GigaSpaces will send you a license to the email address you provided when you downloaded XAP. Enter this information in the `gslicense.xml` file
that is located under the GS_HOME directory.

The activation license key is in the following form:


```console
"Nov 16, 2020~user@XXXXXXXXXXXXXXXXXXXXX#PREMIUM^9.7XAPPremium%UNBOUND+UNLIMITED"
```

# Installing new License

To install the license, insert the license string between the license key tags:


```console
<com>
  <j_spaces>
        <kernel>
          <licensekey>Nov 16, 2020~user@XXXXXXX#PREMIUM^9.7XAPPremium%UNBOUND+UNLIMITED</licensekey>
       </kernel>
  </j_spaces>
</com>
```
