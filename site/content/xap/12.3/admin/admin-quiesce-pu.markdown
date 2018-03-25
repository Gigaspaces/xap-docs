---
type: post123
title:  Advanced Maintenance Activities
categories: XAP123ADM,PRM
weight: 1400
parent: admin-spaces-pu.html
---

# Interacting with Space Data

{{% note "Note"%}}
This functionality is not available in the Command Line Interface, REST Manager API, or Administration API.
{{% /note %}}

## Querying a Space

The Web Management Console and GigaSpaces Management Center support SQL queries, enabling users to perform actions on the data objects in the Space. For example, to query a specific class:

```
SELECT * FROM my.company.com.MyPojo WHERE rownum < 1000
```

**To query a Space:**

{{%tabs%}}
{{%tab "Web Management Console"%}}

1. In the Spaces view, highlight the Space or the Space instance you want to query.
1. Display the Queries pane in the lower area of the view.
1. Type a query, or click a data type from the Types pane.
1. Click <b>Execute Query</b>.

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

{{% /tabs %}}

## Deleting Data from a Space

You can clear data from a Space using SQL queries, or via the user interface.

**To delete (clear) data from a Space:**

{{%tabs%}}
{{%tab "Web Management Console"%}}

1. In the Spaces view, highlight the Space where you want to delete the data.
1. Display the Types pane in the lower area of the view.
1. Click the **Actions** icon and select **Clear** from the menu.

After you delete the data, you can verify it was removed by looking at the value of the Instances Count column for that data type. 

{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}

{{% /tabs %}}



# Quiescing a Processing Unit 

 

**To quiesce a Processing Unit:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu quiesce <pu name>`

*Description:*
 
Putting a Processing Unit into maintenance  mode.

*Input Example:*

```bash
<XAP-HOME>/bin/xap pu quiesce  myPu startingUpdate
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter |name | The name of the Pu to quiesce|
|Option | comment | Quiesce description .|

{{%/tab%}}

{{%tab "REST Manager API"%}}

*Path*

`POST /pus/{id}/quiesce`

*Description:*

Putting a Processing Unit into maintenance mode.

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/myPu/quiesce'
```
 

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit you want to quiesce. | Yes |
| pdescription | Provide a description why you are quiescing. | No|

{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Processing Units view, highlight the Processing Unit you want to quiesce.
1. Click the **Actions** icon, and select **Quiesce** from the menu.
1. (Optional) If you want helpful information to appear in the log, type some descriptive text in the **Description** box (for example: "offline for maintenance").
1. Click **OK**.

The Quiesce Progress window displays the result of the Quiesce process (for example, "Quiesce completed successfully"), and a token that the administrator can use to perform actions on the Processing Unit while it is in quiesced state. It is recommended to copy this token from the window before clicking **OK**.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}


<br>

# Unquiescing a Processing Unit

 

**To unquiesce a Processing Unit:**

{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu unquiesce <pu name>`

*Description:*
 
Unquiesce a Processing Unit.

*Input Example:*

```bash
<XAP-HOME>/bin/xap pu unquiesce  myPu  
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter |name | The name of the Pu to unquiesce|
 
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

*Path*

`POST /pus/{id}/unquiesce`

*Description:*

Unquiesce a Processing Unit

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/myPu/unquiesce'
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit you want to unquiesce. | Yes |
 


{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Processing Units view, highlight the Processing Unit you want to unquiesce.
1. Click the **Actions** icon, and select **Unquiesce** from the menu.
1. Click **Yes** in the confirmation message.

When the Processing Unit is available again, the status in the Processing Unit view changes to Intact.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}


# Restarting a Processing Unit

 

 

**To restart a Processing Unit:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}
N/A
{{%/tab%}}

{{%tab "REST Manager API"%}}
N/A
{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Processing Units view, highlight the Processing Unit instance you want to restart.
1. Click the **Actions** icon, and select **Restart** from the menu.
1. Click **Yes** in the confirmation message.

The Processing Unit instance is not visible in the Processing Unit view until it has finished restarting. This can take several seconds.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}

#  Relocating a Processing Unit

 

**To relocate a Processing Unit:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu relocate <pu instance ID> [<container ID>]`

*Description:*
 
Relocating a Processing Unit Instance to another container.

*Input Example:*

```bash
<XAP-HOME>/bin/xap pu relocate  myPu~1 container~1 
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | \<instance ID\> | ID of Processing unit instance to relocate|
|Parameter | [\<container ID\>] | ID of  target container for relocation| Any available container if target is not specified |
 
{{%/tab%}}

{{%tab "REST Manager API"%}}
*Path*

`POST /pus/{id}/instances/{instanceId}/relocate`

*Description:*

Relocate a Processing Unit

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/alertSpace/instances/instance1/relocate?containerId=container2' 
```


*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit you want to relocate. | Yes |
| pu instanceId | Provide the instanceId of the Processing Unit you want to relocate. | Yes |
| targetContainerId  | Id of  Target container to relocate to | Yes|

{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Processing Units view, highlight the Processing Unit instance you want to relocate.
1. Click the **Actions** icon, and select **Relocate** from the menu.
1. Highlight the host where you want to move the instance, and click **Select**.
1. Click **Yes** in the confirmation message.

The Processing Unit state is Scheduled in the Processing Unit view until the instance is relocated and initiated. This can take several seconds.

{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}


#  Incrementing a Processing Unit

 

**To increment a Processing Unit:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu increment <name>` 

*Description:*
 
Increment a Processing Unit Instances.

*Input Example:*

```bash
<XAP-HOME>/bin/xap pu increment  myPu 
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | name | The name of the stateless Processing unit to increment|
 
 
{{%/tab%}}

{{%tab "REST Manager API"%}}

*Path*

`POST /pus/{id}/instances`

*Description:*

Increment the processing unit's instances

*Example Request:*

```bash
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/myPu/instances' 
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit to increment. | Yes |

{{%/tab%}}


{{%tab "Web Management Console"%}}

N/A
 
{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}

#  Decrement a Processing Units instances

 

**To decrement a Processing Unit:**


{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu decrement <name> <instance ID>`

*Description:*
 
Decrement a stateless Processing Unit Instances.

*Input Example:*

```bash
<XAP-HOME>/bin/xap pu decrement myPu myPu~3 
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:------------|
|Parameter | \<name\> | The name of the stateless Processing unit to decrement|
|Parameter | \<instance ID\> | The ID of Processing unit instance to decrement|
 
 
{{%/tab%}}

{{%tab "REST Manager API"%}}
*Path*

`DELETE /pus/{id}/instances/{instanceId}`

*Description:*

Delete the processing unit instance.

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/myPu/instances/myPu~1'
```

*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| pu name | Provide the name of the Processing Unit to delete. | Yes |
| instanceId | Provide the instance Id of the Processing Unit to delete. | Yes |
{{%/tab%}}


{{%tab "Web Management Console"%}}

N/A
 
{{%/tab%}}


{{%tab "GigaSpaces Management Center"%}}
Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.
{{%/tab%}}


{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}

{{% /tabs %}}
