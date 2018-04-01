---
type: post123
title: Generating Dump Files
categories: XAP123ADM,PRM
weight: 700
parent: admin-service-grid.html
---
 
**To generate dump files:**
<br>

_Not yet available when using the **Command Line Interface** or the **REST Manager API** administration tools._

{{%tabs%}}

<!--
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}
-->

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
		<li><b>Generate Overview</b> - Generates only the overview file, which provides system and application information. This information is useful to quickly gather the system settings in production, for example when reporting a support case.</li>
	</ol>

	OR

1. In one one the console views, click the **Actions** icon for the required component and select **Generate dump**.
1. If you selected **Generate Overview**, either open or save the zip file that was generated.
1. If you selected any other dump file type, do the following:

	<ol type="a">
		<li>In the <b>Cause for dump retrieval</b> box, type a reason for generating the dump file. This text will appear in the summary file for each XAP component.</li>
		<li>In the <b>Select Dump Types</b> area, check the options for the information you want in the dump file, which provides a summary file plus the following:
		<ul>
			<li><b>JVM Thread Dump</b> - container state and thread information per container. It is recommended to choose this only on a specific host or GSC that a Java heap dump should be generated for.</li>
			<li><b>Network Dump</b> - network statistics per container.</li>
			<li><b>Log Dump</b> - log files for all services, including those that have been terminated, or only services that are currently running (if the <b>Only Live Services Log Dump</b> option is checked). This type of dump file is useful for troubleshooting failover scenarios.</li>
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
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
