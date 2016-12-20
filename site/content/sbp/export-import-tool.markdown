---
type: post
title:  Export/Import Tool
categories: SBP
parent: solutions.html
weight: 50
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Skyler Severns | 10.2.0 | December 2015|    | {{%git "https://github.com/GigaSpaces-ProfessionalServices/import-export"%}}   |


The Export/Import tool was originally created to help our engineers quickly replicate test scenarios.
Since its creation this tool has evolved to be an easy-to-use method for migrating data to new XAP deployments, capturing data snapshots,
and bootstrapping disparate environments.

The Export/Import tool leverages several XAP features that make its operations the most performant and functional for each
 use case. The fundamental feature leveraged by Export/Import is XAP's [Task Execution API]({{%latestjavaurl%}}/task-execution-overview.html).

When the Export/Import tool is started it will send either an Export or Import task to each partition, and this is where
the actual operation will be performed. To put it simply each primary instance is responsible for exporting or importing their own data.

<br />
## *When to use Export/Import?*

- Creating a snapshot of data
- Introducing a new environment
- Upgrading XAP versions
- Setting up integration-style test scenarios

You can download and build the source code from our GitHub repository {{%git "https://github.com/GigaSpaces-ProfessionalServices/import-export"%}}.
Directions on how to build the project can be found in the repository's `README` document.
 Alternatively you can contact your [GigaSpaces Technical Account Manager](http://www.gigaspaces.com/services-technical-account-management) for pre-built binaries.

<br />
# Export
During the export a remote task is sent to each primary space instance on the grid; or a subset
of the space instances if specified via the command line options.

Once the task begins executing on the grid it will acquire a list of all space classes described in that instance,
and use this list to drive the creation of export files. It is at this time that a new thread pool will be created
 to dictate how many files can be exported in parallel.

For each combination of class name and partition a query will be performed on the local space instance. If any space class
 instances match the route to the new partition it will be written to disk. If no matches were found the file will not be written.

Files Name Pattern:

    {class-name-with-package}.{originating-partition}.{target-partition}.ser.gz
    Example: com.j_spaces.examples.benchmark.messages.MessagePOJO.ser.gz

File Content Structure (Uncompressed):

    UTF: Class Name
    Obj: Specialized Type Description (Portable/Serializable Class Definition)
    Obj: Space Class Instance (x Row Count)

{{%align center%}}
![How Exporting Data Works](/attachment_files/export-import/export.png)
{{%/align%}}

<br />
## *Usage*
Due to the number of configuration options available we are unable to show all permutations of the tool, but the simplest and most
common usage is shown below.

{{%tabs%}}
{{%tab "  Linux "%}}

```bash
./setAppEnv.sh

$JAVA_HOME/bin/java -cp $GS_HOME/lib/required/*:./lib/* com.gigaspaces.tools.importexport.Program -o export -g $LOOKUPGROUPS -s myspace --jarless -d /tmp/export/output
```

{{% /tab %}}
{{%tab "  Windows "%}}

```bash
call "%~dp0\setAppEnv.bat"

%JAVA_HOME%\bin\java.exe -cp %GS_HOME%\lib\required\*;.\lib\* com.gigaspaces.tools.importexport.Program -o export -g %LOOKUPGROUPS% -s myspace --jarless -d c:\tmp\output
```

{{% /tab %}}
{{% /tabs %}}


```bash
PS C:\var\import-export> .\export.ps1
2015-12-12 23:02:15,130 CONFIG [com.gigaspaces.logger] - Log file: C:\opt\gigaspaces\xap-10.2.0-ga\logs\2015-12-12~23.02
-gigaspaces-service-riomhairenua-13864.log
2015-12-12 23:02:15,125  INFO [com.gigaspaces.tools.importexport.config.SpaceConnectionFactory] - Creating connection wi
th url:
/./myspace?total_members=2,0&schema=default&cluster_schema=partitioned-sync2backup&id=1&groups=space-test-10&state=start
ed
2015-12-12 23:02:15,199  INFO [import-export] - Started import/export operation with the following configuration:
EXPORT [Space: myspace, Lookup Groups: [space-test-10], Lookup Locators: [], Output/Input Directory: c:\var\import-expor
t\output, Operating Partitions: '[]', Export/Import Classes: '[]', XAP Read Batch Size: 1000, PU Name Override: null, Se
curity level: null, New partition count: Not specified, Threads: 20, Jarless: true, Thread sleep ms: 1000]

2015-12-12 23:02:17,203  INFO [import-export] - Partition 1 Finished ---------------------

        Partition Id: 1
        Process Id: 10048
        Hostname: 127.0.0.1
        Elapsed Process Time (ms): 1610

        Files:
                com.j_spaces.examples.benchmark.messages.MessagePOJO.1.1.ser.gz | Records: 5000 | Elapsed time (ms): 742


2015-12-12 23:02:17,204  INFO [import-export] - Partition 2 Finished ---------------------

        Partition Id: 2
        Process Id: 11592
        Hostname: 127.0.0.1
        Elapsed Process Time (ms): 1595

        Files:
                com.j_spaces.examples.benchmark.messages.MessagePOJO.2.2.ser.gz | Records: 5000 | Elapsed time (ms): 737


PS C:\var\import-export>
```

<br />
## *Options*
<table>
    <thead>
        <tr>
            <th>Short Name</th>
            <th>Long Name</th>
            <th>Optional / Required</th>
            <th>Default Value</th>
            <th>Acceptable Values</th>
            <th>Description</th>
        </tr>
        <tr>
            <th colspan="6"> Grid Connection Information</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>-s</td>
            <td>--space</td>
            <td>required</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Name of the target space to perform the operation on.</td>
        </tr>
        <tr>
            <td>-l</td>
            <td>--locators</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of XAP lookup locators for the target grid.</td>
        </tr>
        <tr>
            <td>-g</td>
            <td>--groups</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of XAP lookup groups for the target grid.</td>
        </tr>
        <tr>
            <td>-u</td>
            <td>--username</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Specifies an XAP username with read and execute privileges. Required when connecting to a secured grid.</td>
        </tr>
        <tr>
            <td>-a</td>
            <td>--password</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Specifies an XAP password corresponding to the specified XAP username. Required when connecting to a secured grid.</td>
        </tr>
        <tr>
            <td></td>
            <td>--security-level</td>
            <td>optional</td>
            <td>n/a</td>
            <td>grid, space, both</td>
            <td>Indicates the level of security for the grid.</td>
        </tr>
    </tbody>
    <thead>
            <tr>
                <th colspan="6">General Configuration</th>
            </tr>
    </thead>
    <tbody>
        <tr>
            <td>-o</td>
            <td>--operation</td>
            <td>required</td>
            <td>export</td>
            <td>export, import</td>
            <td>A flag indicating whether an export or import will be performed.</td>
        </tr>
        <tr>
            <td>-d</td>
            <td>--directory</td>
            <td>required</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A full path to the directory containing either previously exported files, or where the exported files should be placed.</td>
        </tr>
        <tr>
            <td></td>
            <td>--pu-name</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Overrides the name of the processing unit, relevant only when the processing unit is different from space name.</td>
        </tr>
        <tr>
            <td></td>
            <td>--jarless</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Indicates that the import / export will not use Java class definitions during processing.</td>
        </tr>
        <tr>
            <td>-c</td>
            <td>--classes</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of class names to operate on. The class names are case sensitive.</td>
        </tr>
        <tr>
            <td>-p</td>
            <td>--partitions</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of partitions that will be operated on.</td>
        </tr>
        <tr>
            <td>-n</td>
            <td>--number</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Relevant only when exporting data for use in a grid with a different partition count (i.e. Exporting data from a 6 partition grid to 2 partition grid or vice versa.)</td>
        </tr>
    </tbody>
    <thead>
            <tr>
                <th colspan="6">Performance Configuration</th>
            </tr>
    </thead>
    <tbody>
        <tr>
            <td>-b</td>
            <td>--batch</td>
            <td>optional</td>
            <td>1000</td>
            <td>n/a</td>
            <td>Performance option to batch records retrieved from the space.</td>
        </tr>
        <tr>
            <td></td>
            <td>--thread-sleep</td>
            <td>optional</td>
            <td>1000</td>
            <td>n/a</td>
            <td>Number of milliseconds to sleep between checks for task completion.</td>
        </tr>
        <tr>
            <td>-t</td>
            <td>--threads</td>
            <td>optional</td>
            <td>20</td>
            <td>n/a</td>
            <td>Number of threads to simultaneously process import or export files.</td>
        </tr>
    </tbody>
</table>

<br />
# Import
During the import a remote task is sent to each primary space instance. From there each primary instance will search the file system
for relevant files. Relevancy is determined by the second integer in the file name also known as the destination partition ID.

All files destine for the new partition will then be queued and processed from the export/import thread pool. The number of
files that will be processed in parallel for each partition is configurable and based on the size of the export/import thread pool.

{{%align center%}}
![How Exporting Data Works](/attachment_files/export-import/import.png)
{{%/align%}}
## *Usage*
Due to the number of configuration options available we are unable to show all permutations of the tool, but the simplest and most
common usage is shown below.

{{%tabs%}}
{{%tab "  Linux "%}}

```bash
./setAppEnv.sh

$JAVA_HOME/bin/java -cp $GS_HOME/lib/required/*:./lib/* com.gigaspaces.tools.importexport.Program -o import -g $LOOKUPGROUPS -s myspace -d /tmp/export/output
```

{{% /tab %}}
{{%tab "  Windows "%}}

```bash
call "%~dp0\setAppEnv.bat"

%JAVA_HOME%\bin\java.exe -cp %GS_HOME%\lib\required\*;.\lib\* com.gigaspaces.tools.importexport.Program -o import -g %LOOKUPGROUPS% -s myspace -d c:\tmp\output
```

{{% /tab %}}
{{% /tabs %}}


```bash
PS C:\var\import-export> .\import.ps1
2015-12-13 00:58:12,624 CONFIG [com.gigaspaces.logger] - Log file: C:\opt\gigaspaces\xap-10.2.0-ga\logs\2015-12-13~00.58-gigaspaces-service-
riomhairenua-3836.log
2015-12-13 00:58:12,618  INFO [com.gigaspaces.tools.importexport.config.SpaceConnectionFactory] - Creating connection with url:
/./myspace?total_members=2,0&schema=default&cluster_schema=partitioned-sync2backup&id=1&groups=space-test-10&state=started
2015-12-13 00:58:12,697  INFO [import-export] - Started import/export operation with the following configuration:
IMPORT [Space: myspace, Lookup Groups: [space-test-10], Lookup Locators: [], Output/Input Directory: c:\var\import-export\output, Operating
Partitions: '[]', Export/Import Classes: '[]', XAP Read Batch Size: 1000, PU Name Override: null, Security level: null, New partition count:
 Not specified, Threads: 20, Jarless: false, Thread sleep ms: 1000]

2015-12-13 00:58:14,699  INFO [import-export] - Partition 1 Finished ---------------------

        Partition Id: 1
        Process Id: 10048
        Hostname: 127.0.0.1
        Elapsed Process Time (ms): 1096

        Files:
                com.j_spaces.examples.benchmark.messages.MessagePOJO.1.1.ser.gz | Records: 5000 | Elapsed time (ms): 1095

2015-12-13 00:58:14,700  INFO [import-export] - Partition 2 Finished ---------------------

        Partition Id: 2
        Process Id: 11592
        Hostname: 127.0.0.1
        Elapsed Process Time (ms): 1099

        Files:
                com.j_spaces.examples.benchmark.messages.MessagePOJO.2.2.ser.gz | Records: 5000 | Elapsed time (ms): 1092

PS C:\var\import-export>
```

<br />
## *Options*
<table>
    <thead>
        <tr>
            <th>Short Name</th>
            <th>Long Name</th>
            <th>Optional / Required</th>
            <th>Default Value</th>
            <th>Acceptable Values</th>
            <th>Description</th>
        </tr>
        <tr>
            <th colspan="6"> Grid Connection Information</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>-s</td>
            <td>--space</td>
            <td>required</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Name of the target space to perform the operation on.</td>
        </tr>
        <tr>
            <td>-l</td>
            <td>--locators</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of XAP lookup locators for the target grid.</td>
        </tr>
        <tr>
            <td>-g</td>
            <td>--groups</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A comma separated list of XAP lookup groups for the target grid.</td>
        </tr>
        <tr>
            <td>-u</td>
            <td>--username</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Specifies an XAP username with read and execute privileges. Required when connecting to a secured grid.</td>
        </tr>
        <tr>
            <td>-a</td>
            <td>--password</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Specifies an XAP password corresponding to the specified XAP username. Required when connecting to a secured grid.</td>
        </tr>
        <tr>
            <td></td>
            <td>--security-level</td>
            <td>optional</td>
            <td>n/a</td>
            <td>grid, space, both</td>
            <td>Indicates the level of security for the grid.</td>
        </tr>
    </tbody>
    <thead>
            <tr>
                <th colspan="6">General Configuration</th>
            </tr>
    </thead>
    <tbody>
        <tr>
            <td>-o</td>
            <td>--operation</td>
            <td>required</td>
            <td>export</td>
            <td>export, import</td>
            <td>A flag indicating whether an export or import will be performed.</td>
        </tr>
        <tr>
            <td>-d</td>
            <td>--directory</td>
            <td>required</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>A full path to the directory containing either previously exported files, or where the exported files should be placed.</td>
        </tr>
        <tr>
            <td></td>
            <td>--pu-name</td>
            <td>optional</td>
            <td>n/a</td>
            <td>n/a</td>
            <td>Overrides the name of the processing unit, relevant only when the processing unit is different from space name.</td>
        </tr>
    </tbody>
    <thead>
            <tr>
                <th colspan="6">Performance Configuration</th>
            </tr>
    </thead>
    <tbody>
        <tr>
            <td>-b</td>
            <td>--batch</td>
            <td>optional</td>
            <td>1000</td>
            <td>n/a</td>
            <td>Performance option to batch records retrieved from the space.</td>
        </tr>
        <tr>
            <td></td>
            <td>--thread-sleep</td>
            <td>optional</td>
            <td>1000</td>
            <td>n/a</td>
            <td>Number of milliseconds to sleep between checks for task completion.</td>
        </tr>
        <tr>
            <td>-t</td>
            <td>--threads</td>
            <td>optional</td>
            <td>20</td>
            <td>n/a</td>
            <td>Number of threads to simultaneously process import or export files.</td>
        </tr>
    </tbody>
</table>
<br />

# Troubleshooting & Frequently Asked Questions
 
### *Can I use the Export/Import tool with a secured grid?*

Yes, the Export/Import tool will work with secured infrastructure components and/or secured spaces. The username and password provided
should have sufficient privileges to *execute* a remote task on the grid.

If using a secured space the space must be authenticated with sufficient privileges to *read, write, and update all classes, as well as monitor_pu*.
<br />
### *I'm receiving a FileNotFoundException; what does this mean?*

The most common reason for this exception is that your storage directory (the `-d` command line option) may not exist on all hosts.
Double check the directory exists before re-running the Export/Import tool.
<br />
### *Can I use this to upgrade XAP versions?*

Yes, this tool has been tested for upgrades between XAP 9.7, XAP 10, and XAP 11.
<br />
### *One or more of my files did not get imported, why is that?*

Each partition is responsible for exporting or importing its data. Because of this the files will be stored in the directory provided
on that partition's host machine.

It is recommended that all machines mount a NFS drive that will be used during the export and import operations. This will ensure all partitions
regardless of hosts will have access to the export files.
<br />
### *Why should I use the `--jarless` option during export?*

If you do not provided the `--jarless` option during export you will be required to include your application jars on the Export/Import tool's classpath
as well as the classpath used by your processing unit running on XAP. The jars would be required on both import and export operations.

When `--jarless` is provided all objects will be read as an XAP [Space Document]({{%latestjavaurl%}}/document-overview.html) this
removes the requirement to include your application jars in the classpath. Documents and Space Classes can be interoperable,
and when following XAP documentation and best practices should be interoperable.
<br />
### *Why am I receiving a NoClassDefFoundError and/or a ClassNotFoundException?*

There are several reasons this could be. If you're seeing this on one of your space classes during import it is because
the classes were exported without the `--jarless` option and you will need to include your jars in the Export/Import tools
classpath as well as the Processing Unit's classpath.

If this is occurring during export you may be missing jars on the classpath, or the class definitions in the space may not be available
to the export thread. If it is the latter the solution would be making your jars available to the processing unit instances
by placing the required jars in your pu_common folder before the processing units are deployed.