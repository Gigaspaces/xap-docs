---
type: post
title:  Upgrading from previous versions
categories:
parent: xap120.html
weight: 800
---

This page contains information about changes in this release which can affect upgrading from previous versions.


# Maven Configuration

The maven artifacts have changed.

# Using the Administration API with XAP.NET

The Administration API code was extracted to a separate JAR file, but the configuration file was not updated accordingly. To use this administration tool, modify the `DefaultApp.config` file to include the xap-admin jars.

For example, **C:\GigaSpaces\XAP.NET-12.1.1-x64\NET v4.0\Config\DefaultApp.config**. Add the following: `<add Path="$(XapNet.Runtime.Path)\lib\platform\service-grid\*.jar"/>`.

{{%refer%}}
For more information, see [Maven Artifacts](/xap/12.0/dev-java/maven-artifacts.html).
{{%/refer%}}


