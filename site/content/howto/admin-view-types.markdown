---
type: post
title:  Viewing Data Types
weight: 1100
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

 
**To view data types in the Space:**

  
{{%tabs%}}
{{%tab "Command Line Interface"%}}

**To view Space data types:**

*Command:*

`xap space info --type-stats <space name>`
 
*Description:*

This command lists all the Spaces  in a table with the name, deployment name, topology and InstanceId information.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info --type-stats alertSpace
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-types.png)

*Parameters and Options:*

|Item | Name| Description | Comment |
|:----|:----|:------------|:--------|
|Parameter | \<name\> |Name of the Space to display the statistics for ||
 
 

**Display Space Types for Instance**

*Command:*

`xap space info-instance --type-stats <space ID>`
 
*Description:*

This command lists all the Spaces  in a table with the name, deployment name, topology and InstanceId information.
 
*Input Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instance --operation-stats alertSpace~1
```
 
*Output Example:*
  
![image](/attachment_files/admin/cli-xap-space-types-instance.png)

*Parameters and Options:*

|Item | Name| Description | Comment |
|:----|:----|:------------|:--------|
|Parameter | \<spaceId\> |Id of the Space to display the statistics for ||
 
 
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Spaces view, highlight the Space or Space instance where you want to see the data types.
1. Display the Types pane in the lower area of the view.
1. You can view the following information per data type:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Data Type Name</td>
		<td>Name of the data type.</td>
	</tr>
	<tr>
		<td>Instances Count</td>
		<td>How many instances of this data type are currently in the Space.</td>
	</tr>
	<tr>
		<td>Templates Count</td>
		<td>How many instances of this data type match a template in an event container.</td>
	</tr>
	<tr>
		<td>Space ID</td>
		<td>Pointer to the primary key of the data type in the Space.</td>
	</tr>
	<tr>
		<td>Space Routing</td>
		<td>Pointer to the field that identifies the Space partition.</td>
	</tr>
	<tr>
		<td>Indexes</td>
		<td>Displays which fields of the data type are indexed.</td>
	</tr>
	</table>


	Additionally, when you drill through a data type, you can see the following additional information:

	<table>
	<tr>
		<th>Item</th>
		<th>Description</th>
	</tr>
	<tr>
		<td>Member Name</td>
		<td>Name of the field in the data type.</td>
	</tr>
	<tr>
		<td>Member Type</td>
		<td>Type of field.</td>
	</tr>
	<tr>
		<td>Storage Type</td>
		<td>How the data type is stored (object, binary, or compressed).</td>
	</tr>
	<tr>
		<td>Indexes</td>
		<td>Index type for the member of the data type.</td>
	</tr>
	</table>

1. To filter the data type table, type an alphanumeric value in the **Filter** box. 

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}
{{% /tabs %}}
