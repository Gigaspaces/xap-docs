---
type: post
title: Generating Dump Files
weight: 900
parent: admin-service-grid.html
---
 
  

{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>
 
{{%tabs%}}

{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}

{{%tab "Web Management Console"%}}

The Web Management Console supports generating the following types of dump files:

* Overview (provides time of dump, XAP build and version, license information, number of host machines, number of grid components, service grid information, and host information)
* Hosts (complete information dump per host, generated per XAP grid component) 
* Containers (container information per host)
* Per component (select a specific XAP component from any of the console views)

**To generate a dump file:**

1. From the menu bar, click **Generate Dump** and select from the following options:

	<ol type="a">
		<li><b>Generate hosts dump</b> - This is the most comprehensive dump file, generated per host with an overview file, summary file, and all information defined by the user per XAP component.</li>
		<li><b>Generate GSCs dump only</b> - Partial dump file generated per host of the containers with an overview file, summary file, and all information defined by the user.</li>
		<li><b>Generate Overview</b> - Generates only the overview file, which provides system and application information.</li>
	</ol>

	OR

1. In one one the console views, click the **Actions** icon for the required component and select **Generate dump**.
1. If you selected **Generate Overview**, either open or save the zip file that was generated.
1. If you selected any other dump file type, do the following:

	<ol type="a">
		<li>In the <b>Cause for dump retrieval</b> box, type a reason for generating the dump file. This text will appear in the summary file for each XAP component.</li>
		<li>In the <b>Select Dump Types</b> area, check the options for the information you want in the dump file, which provides a summary file plus the following:
		<ul>
			<li><b>JVM Thread Dump</b> - container state and thread information per container.</li>
			<li><b>Network Dump</b> - network statistics per container.</li>
			<li><b>Log Dump</b> - log files for all services or only live services (If the <b>Only Live Services Log Dump</b> option is checked)</li>
			<li><b>Processing Unit Dump</b> - pu.xml file per Processing Unit, event container information per Processing Unit instance, and a summary file per Space.</li>
			<li><b>JVM Heap Dump</b> - binary heap dump file.</li>
		</ul>
		<li>Click <b>Generate</b>.
	</ol>
    
1. When the dump file has been generated (indicated by the status bar in the dialog box), define another dump file to generate or click **Close**.
1. Open or save the zip file that was generated.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}
