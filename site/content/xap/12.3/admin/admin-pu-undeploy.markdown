---
type: post123
title:  Undeploying a Processing Unit
categories: XAP123ADM,PRM
weight: 700
parent: admin-spaces-pu.html
---
 

**Removing a Processing Unit from the Service Grid**.

<br> 
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}

*Command:*

`xap pu undeploy <name>`

*Input Example:*

```bash
xap pu undeploy myPu
```

*Parameters and Options:*

| Item | Name | Description |
|:-----|:------|:-----------|
|Parameter |name | The name of the Pu to undeploy|
|Option | keep-file | Keep the undeployed file for future use.|

{{%/tab%}}

{{%tab "REST Manager API"%}}
 
*Path*

`DELETE DELETE /pus/{id}`

*Example Request:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/pus/myPU'
```
This example undeploys a Processing Unit  named **myPU**. 


*Options:*

| Option     | Description       |   Required     |
|------|-------------------|----------------|
| name | Provide the name of the PU you are un deploying. | Yes |
 

{{%/tab%}}


{{%tab "Web Management Console"%}}

1. In the Processing Units view, highlight the Processing Unit to undeploy.
1. Click the **Actions** icon and select **Undeploy** from the menu.
1. Confirm that you want to undeploy the Processing Unit.

When the Processing Unit is undeployed successfully, it no longer appears in the Processing Units view. 

{{% note "Note"%}}
You can confirm that the Processing Unit was undeployed by viewing the list of events from the toolbar. If you redeploy the same Processing Unit, all of the deploy and undeploy events appear in the event list for the Processing Unit that is displayed in the Events tab on the side of the view.
{{% /note %}}


{{%/tab%}}

{{%tab "GigaSpaces Management Center"%}}

Refer to the [GigaSpaces Management Center](./gigaspaces-management-center.html) topics in the Administration section.

{{%/tab%}}
{{%tab "Administration API"%}}
Refer to the [Admin API](../dev-java/administration-and-monitoring-overview.html) topics in the Developers Guide.
{{%/tab%}}
{{% /tabs %}}

  