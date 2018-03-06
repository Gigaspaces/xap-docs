---
type: post
title:  Querying a Space
weight: 1000
parent: admin-spaces-pu.html
---
 
 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To query a Space:**
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}

The Query editor supports SQL queries. For example, to query a specific class:

	SELECT * FROM my.company.com.MyPojo WHERE rownum < 1000

1. In the Spaces view, highlight the Space or the Space instance you want to query.
1. Display the Queries pane in the lower area of the view.
1. Type a query, or click a data type from the Types pane.
1. Click **Execute Query**.

	The query is executed against the selected Space or Space instance. If there are too many results to display on a single web page, you can navigate using the paging controls at the bottom of the table. Paging is static (results are fetched once per execute request).
1. If you have tried to run multiple queries, you can navigate between them using the **Go Back**/**Go Forward** buttons, or select a query from the dropdown lists next to these buttons.
1. To export the results of a query:

	<ol type="a">
		<li>Click <b>Export</b>.
		<li>Select an action (<b>Open</b>, <b>Save</b>, or <b>Cancel</b>).</li>
	</ol>
	The results are exported in a zipped CSV file.
	
{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
TBD
{{%/tab%}}

{{% /tabs %}}
