---
type: post
title:  Deploying a Space
weight: 4
parent: admin-tools-overview.html
---
 
 

As part of evaluating XAP or InsightEdge, or as part of working with the products in a lab environment, you’ll want to deploy a data grid, also known as a Space. After you’ve deployed the Space, you can perform Space-related activities, such as adding data objects, viewing information about the Space configuration, querying the data in the Space, and viewing logs and alerts. 



#  To deploy a Space

{{%note "Note"%}}
In order to deploy a Space, you must first have a service grid up and running. Deploying a Space using an administration tool (such as the Web Management Console) creates a Processing Unit that contains only a Space, without any application components.
{{%/note%}}

{{%tabs%}}
{{%tab "Command Line Interface"%}}

_Parameters:_<br>
name : The name of the Space , required

_Options:_<br>
---ha         : Should backups be used for high availability<br>
---partitions=\<partitions\>    : Number of partitions<br> 
---requires-isolation   :  Each instance should be provisioned in an isolated container 

*Example:*<br>
This example deploys a Space named **mySpace** with high availability and 5 partitions. 

```bash
<XAP-HOME>/bin/xap space deploy --ha --partitions=5 mySpace
```

{{%/tab%}}

{{%tab "Web Management Console"%}}

1. From the Deploy menu on the menu bar, select **Space**.
2. In the Space Deployment dialog box, do the following:

	a. In the **Space name** box, type a name for the Space.
	b. (Optional) If you want this Space to be secure, do the following In the **User Login Details** area:
	
		- Select **Secured Space**.<br>		
		- Provide the user credentials in the **User Name** and **Password** boxes.
		
	a. In the **Cluster Info** area, apply the required configuration details.
  
	b. In the **Cluster schema** box, specify the SLA definitions (cluster topology):
	
		- **None** - A standalone Space.<br>		
		- **Partitioned** - A cluster that is partitioned across the instances that are specified.	
		- **Sync_replicated** - A cluster with synchronous replication across the instances that are specified.	
		- **Async_replicated** - A cluster with asynchronous replication across the instances that are specified.
		
	a. In the **Number of Instances** box, specify the number of primary Space instances to deploy in the cluster. 
  
	b. (For partitioned clusters) In the **Number of Backups** box, define the number of backup Spaces for each primary Space.
  
	c. In the **Max Inst. per VM** box, define the maximum number of Space instances each virtual host may contain (the default is 1).
  
	d. In the **Max Inst. per VM** box, define the maximum number of Space instances each physical host may contain.
  
	e. If you have more than one host, you can specify on which host to deploy the primary Space instances.
	
1. (Optional) If you want to use a configuration file to specify the SLA definitions, or if you want to override your defined SLA definitions in specific scenarios, click **Next**. 
1. Click the **Browse** button next to the **SLA override** box and select the sla.xml file that you want to use.
1. If your environment contains zones, you can do one of the following:

	a. Select the **Select Zone** option and: 
	
		- From the list on the left, select which zone to use for the -zone deployment parameter.<br>		
		- In the **Max. Instances (partitions) number** area on the right, define the maximum instances per zone (`-max-instances-per-zone` deployment parameter).
		
	b. If you don’t want to specify a zone, select **Any Zone**.
	
1. Click **Deploy**.

{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}

TBD

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}


# Viewing General Space Details

{{%tabs%}}
{{%tab "Command Line Interface"%}}

***List all Spaces***<br>
Lists all Spaces with the Name, Deployment name, Topology and InstanceId

*Example:*

```bash
<XAP-HOME>/bin/xap space list
```


***List all Space instances***<br>
Lists all Space instances for a given Space with Id, Mode, PartionId, BackupId, HostId and ContainerId.

_Parameters:_<br> 
name : The name of the Space , required
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```


{{%/tab%}}

{{%tab "Web Management Console"%}}
 

{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}

TBD

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

# Viewing Space Information
  
{{%tabs%}}
{{%tab "Command Line Interface"%}}

***Display Space Information***

_Parameters:_<br> 
name : The name of the Space , required

_Options:_<br>
---operation-stats : Displays Space operations statistics, (read, write, take etc)  <br>
---type-stats      : Displays Space object information.
 

*Example:*

```bash
<XAP-HOME>/bin/xap space info --type-stats mySpace
```
 
***Display Space instance information***

_Parameters:_<br> 
instanceId : The id of the Space instance to use.

_Options:_<br>
---operation-stats : Displays Space instance operations statistics, (read, write, take etc)  <br>
---type-stats      : Displays Space instance object information.<br>
---replication-stats: Display Space instance replication information.
 
 
*Example:*
 
```bash
<XAP-HOME>/bin/xap space info-instances --replication-stats mySpace~1
```
 
{{%/tab%}}

{{%tab "Web Management Console"%}}
 

{{%/tab%}}


{{%tab "GigaSpaces Management Console"%}}

TBD

{{%/tab%}}


{{%tab "Administration API"%}}

TBD

{{%/tab%}}

{{% /tabs %}}

