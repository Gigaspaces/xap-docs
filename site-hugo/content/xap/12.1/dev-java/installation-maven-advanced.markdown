---
type: post121
title:  Advanced Configuration
categories: XAP121,PRM
parent: installation-maven.html
weight: 200
---


This page contains information beyond the basics. It was created for those who want to know a little bit more about how XAP and Maven integrate. Basically, the information given in [XAP Maven Plugin](./installation-maven.html) should be sufficient for the common user.

# Processing Unit Assembly Definition

The XAP maven plugin uses the maven assembly plugin to generate the processing unit directory structure. Each PU module has the file <module-name>/src/main/assembly/assembly.xml that defines the assembly structure. One of its tasks is to determine which of the dependency JARS go into the lib directory and which go into the shared-lib directory.

**assembly.xml**


```xml
<assembly>
  <id>assemble-pu</id>
  <formats>
    <format>jar</format>
    <format>dir</format>
  </formats>
  <includeBaseDirectory>false</includeBaseDirectory>
  <fileSets>
    <fileSet>
      <directory>target/classes</directory>
      <lineEnding>keep</lineEnding>
      <outputDirectory>/</outputDirectory>
      <includes>
        <include>**/**</include>
      </includes>
    </fileSet>
  </fileSets>
  <dependencySets>
    <dependencySet>
      <useProjectArtifact>false</useProjectArtifact>
      <useTransitiveDependencies>false</useTransitiveDependencies>
      <outputDirectory>lib</outputDirectory>
      <excludes>
        <exclude>com.mycompany.app:common</exclude>
      </excludes>
    </dependencySet>
    <dependencySet>
      <useProjectArtifact>false</useProjectArtifact>
      <useTransitiveDependencies>true</useTransitiveDependencies>
      <useTransitiveFiltering>true</useTransitiveFiltering>
      <outputDirectory>shared-lib</outputDirectory>
      <includes>
        <include>com.mycompany.app:common</include>
      </includes>
    </dependencySet>
  </dependencySets>
</assembly>
```

## Dependencies Target Locations

Inside assembly.xml we have a dependencySets element that contains two dependencySet elements.
The first dependencySet (where its outputDirectory=lib) defines which JARS go into the lib directory in the PU structure. It excludes the dependency in the common module and its transitive dependencies. In other words, all dependencies that are not related to the commons module go into the lib directory.

The second dependencySet (where its outputDirectory=shared-lib) defines which JARS go into the shared-lib directory in the PU structure. It includes the dependency in common module and its transitive dependencies, leaving other dependencies out. In other words, all dependencies that are related to the commons module go into the shared-lib directory.

## Dependency Scopes

When defining a dependency for an artifact (module), you can specify the dependency's scope. Scope determines which dependencies participate in the build lifecycle.

Maven defines the following scopes:


| Scope | Description |
|:------|:------------|
| **compile** | Indicates that the dependency is available for the classpaths of all phases. **This is the default scope**. |
| **provided** | Indicates that the dependency is available on the compile classpath, but in the production environment you expect the JDK or a container to provide it. It is not transitive. |
| **test** | Indicates that the dependency is only available for the test compilation and execution phases. |
| **runtime** | Indicates that the dependency is available for the runtime and test classpaths, but not for the compile classpath. |
| **system** | This scope is similar to provided, except that you have to provide the JAR location explicitly. The artifact is not looked up in a repository. Using this scope is not advised due to portability issues. |

{{% info %}}
During processing unit packaging only dependencies declared with the scopes **compile* and *runtime** are added to the PU distributable.
{{%/info%}}

{{%refer%}}
For more information go to [Introduction to the Dependency Mechanism](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)
{{%/refer%}}

## Configuring Maven to use the Assembly Descriptor

To instruct Maven to use this assembly descriptor to generate the processing units, we configure the assembly plugin by adding the following xml snippet to the module's pom.xml file.

**pom.xml**


```xml
<build>
  ...
  <finalName>testing-feeder</finalName>
  <plugins>
    <plugin>
      <artifactId>maven-assembly-plugin</artifactId>
      <configuration>
        <appendAssemblyId>false</appendAssemblyId>
        <attach>false</attach>
        <ignoreDirFormatExtensions>true</ignoreDirFormatExtensions>
        <descriptors>
          <descriptor>src/main/assembly/assembly.xml</descriptor>
        </descriptors>
      </configuration>
      <executions>
        <execution>
          <id>assembly</id>
          <phase>package</phase>
          <goals>
            <goal>single</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
  ...
</build>
```

## Further Information

{{%refer%}}
For further information about the assembly plugin options, go to:

- [Assembly plugin documentation](http://maven.apache.org/plugins/maven-assembly-plugin/)
- [Assembly descriptor reference](http://maven.apache.org/plugins/maven-assembly-plugin/assembly.html)
- [Assembly plugin 'single' mojo documentation](http://maven.apache.org/plugins/maven-assembly-plugin/single-mojo.html)

{{%/refer%}}
