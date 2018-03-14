---
type: post
title:  Undeploying a Processing Unit
weight: 900
parent: admin-spaces-pu.html
---
 
 
 

 
{{% bgcolor yellow %}}write intro for this topic{{% /bgcolor %}}

<br>

**To undeploy a Processing Unit:**
 
{{%tabs%}}
{{%tab "Command Line Interface"%}}

**Command**

`xap pu undeploy` 

**Description**


| Item | Name | Description | Comment |
|:-----|:------|:------------|:--------|
|Parameter |name | The name of the Pu to undeploy||
|Option | keep-file | Keep the undeployed file for future use.||
|version | Display version information. ||

**Example:**

```bash
<XAP-HOME>/bin/xap pu undeploy myPu 
```
{{%/tab%}}

{{%tab "REST Manager API"%}}
 
**Path**

DELETE / deployments

**Description**

**Options**

|  Option | Description | Required |
|:------|:------------|:--------|
|name | The name of the Pu to undeploy| Yes|
|keep-file | Keep the undeployed file for future use.|No|
 
 
*Example:*

```bash
curl -X DELETE --header 'Accept: text/plain' 'http://localhost:8090/v2/deployments/myPu'
```
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
TBD
{{%/tab%}}
{{% /tabs %}}

  