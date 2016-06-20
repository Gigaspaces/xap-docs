---
type: post120
title:  Query View
categories: XAP120ADM
parent: gigaspaces-browser-tree-panel-and-configuration-panel.html
weight: 400
---

 Used to query the space using SQL statements and view the resulting data set in tabular format.


The Query view allows you to query the space using SQL statements and to get the full resulting data set in tabular format.

{{% align center %}}
![query_view.gif](/attachment_files/query_view.gif)
{{% /align %}}

The Query view includes the following options:

- Result set table -- the query results are displayed in a table at the lower part of the **Service View**, where each row represents Entry data, and each column represents class attribute data. Non-native types' attribute data is displayed using the `toString` returned value.

{{% info %}}
Make sure that the space includes the non-native attribute classes as part of its classpath, and that the relevant application has the correct codebase, so that the the query result set table can display non-native type attribute data correctly.
{{%/info%}}

- Exporting result set data into HTML, XML or CSV -- to export the restored result set data, click the export button (![query_export.gif](/attachment_files/query_export.gif)). This displays the export dialog, which requires you to follow 2 simple steps, and then save the file to disk.
- Entry UID is displayed for each object data -- when the SQL query includes the UID as part of the select statement (`select uid,\* from person`), the first column in the result set table displays the Entry UID.
- Space classes list query -- to query the space for the classes it stores run the following query:

    SELECT * FROM SYSTABLES

- Class meta data information -- to get the class meta data information, click the meta data button (![query_metadata.gif](/attachment_files/query_metadata.gif)). This displays a table in which each row includes information about the table column's meta data. This displays a translation of the relevant class attribute type into its SQL equivalent:

![query_metadata_test.jpg](/attachment_files/query_metadata_test.jpg)

Scroll right to see more attributes:

![query_metadata_2.jpg](/attachment_files/query_metadata_2.jpg)

![query_metadata_3.jpg](/attachment_files/query_metadata_3.jpg)

- SQL queries can be saved to a file. Right-click the top of the **Service View** panel and click **Save to file**:

{{% align center %}}
![query_options.gif](/attachment_files/query_options.gif)
{{% /align %}}

{{% anchor 1 %}}

- Inspecting objects using the [Object Inspector](./gigaspaces-browser-object-inspector.html) or the [Object Snapshot Inspector](./gigaspaces-browser-object-inspector.html).
