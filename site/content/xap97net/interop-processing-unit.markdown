---
type: post97net
title:  Interoperability PU
categories: XAP97NET
parent: processing-units.html
weight: 400
---

{{% ssummary %}} {{% /ssummary %}}



It is possible to create a processing unit which is part .NET part Java and part C++.
This page assumes knowledge of creating and using [OpenSpaces Processing Unit]({{% currentjavaurl %}}/the-processing-unit-structure-and-configuration.html) and creating and using [.NET Processing Unit Container](./processing-unit-container.html). It will cover how to properly create deployment files and directory structure for a mixed processing unit.

{{% refer %}}C++ processing unit is based on [OpenSpaces Processing Unit]({{% currentjavaurl %}}/the-processing-unit-structure-and-configuration.html) and therefore treated the same, see [CPP Processing Unit]({{% currentjavaurl %}}/cpp-processing-unit.html) for related information.{{% /refer %}}

# Creating Mixed Processing Unit Deployment Structure

A Mixed processing unit is basically two processing unit that should be deployed as one. Each one of the .NET and OpenSpaces processing unit part are developed independently but compiled into one deployment directory. The mixed processing unit should have the directory structure of both [.NET Processing Unit](./processing-unit-container.html) and [OpenSpaces Processing Unit]({{% currentjavaurl %}}/the-processing-unit-structure-and-configuration.html) under the same root directory. It should be created using a `pu.xml` file under `<PU deployment dir>\META-INF\spring` that configures the OpenSpaces processing unit and have a `pu.interop.config` file under the `<PU deployment dir>` root dir that configured the .NET Processing Unit Container. The `pu.interop.config` file should be configured exactly like the [pu.config](./processing-unit-container.html#pu.config) file (Except for the fact that is should be named pu.interop.config).

The following should be added into the `<PU deployment dir>\META-INF\spring\pu.xml` file as the last tag in order to support mixed processing unit:


```xml
<bean id="dotnetProcessingUnitContainer" class="org.openspaces.interop.DotnetProcessingUnitBean">
</bean>
```

# Sharing The Same Space

In some mixed processing unit scenarios there might be a need for a shared embedded space, for instance two polling containers, one in Java and one in .NET, that should poll objects from the same space. This should be done by starting an embedded space, in both the Java and the .NET processing unit parts, which has the same Url.

The pu.xml and .NET code should resemble the following:

## OpenSpaces pu.xml space construction related part:


```xml
<os-core:space id="space" url="/./interopSpace">
</os-core:space>
```

## .NET Processing Unit Container code space construction related part


```java
class MyInteropProcessingUnit : AbstractProcessingUnitContainer
{

  public override void Initialize()
  {
    SpaceConfig spaceConfig = new SpaceConfig();
    spaceConfig.ClusterInfo = _clusterInfo;
    ISpaceProxy proxy = GigaSpacesFactory.FindSpace("/./interopSpace", spaceConfig);
  }

  ...
}
```
