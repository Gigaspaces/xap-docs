---
type: post121
title:  Type Discovery
categories: XAP121NET, PRM
parent: modeling-your-data.html
weight: 500
canonical: auto
---

{{% ssummary %}} {{% /ssummary %}}

When querying the space (read/take/notify), the space returns the results to the space proxy in an internal type-neutral format, which the space proxy automatically converts back to the relevant user objects. In general the result type is the same as the query type (e.g reading `SqlQuery<Person>` returns `Person` instances). However, since the space supports **type polymorphism**, the result may also be of a different subtype which extends the query type (e.g. reading `SqlQuery<Person>` can return a `Student` instance  which extends the `Person` type). Since the result is type-neutral, it contains the type name instead of the actual type, and it is up to the space proxy to resolve the concrete type from the type name. The space proxy first scans the loaded assemblies looking for the type by its name, and if the type is not found it searches the assemblies files in the current location for a matching type. The assemblies scan can be customized via configuration, as explained below.

# Customizing Assembly scanning

By default all the `.dll` files in the current location are scanned. This can be customized using the following configuration:


```xml
<configuration>
    <configSections>
        <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
    </configSections>
    <GigaSpaces>
        <DataTypes>
            <ScanAssemblies>
                <add AssemblyName="File1.dll"/>
                <add AssemblyName="MyCompany.*.dll" NameSpace="MyCompany.MyProject."/>
            </ScanAssemblies>
        </DataTypes>
    </GigaSpaces>
</configuration>
```

In this example the space proxy will scan `File1.dll` and all the files which match the `MyCompany.\*.dll` pattern. In addition, only types whose namespace starts with "MyCompany.MyProject." will be scanned in the `MyCompany.\*.dll` files.

## Disabling assemblies scan

To disable the assemblies scan altogether, use the following configuration:


```xml
<configuration>
    <configSections>
        <section name="GigaSpaces" type="GigaSpaces.Core.Configuration.GigaSpacesCoreConfiguration, GigaSpaces.Core"/>
    </configSections>
    <GigaSpaces>
        <DataTypes>
            <ScanAssemblies Disabled="true"/>
        </DataTypes>
    </GigaSpaces>
</configuration>
```

## Configuring at Runtime

Configuration can also be set at runtime, for example:


```csharp
var element = new ScanAssemblyConfigurationElement { AssemblyName = "MyCompany.*.dll", NameSpace = "MyCompany.MyProject." };
GigaSpacesFactory.Configuration.DataTypes.ScanAssemblies.Add(element);
```
