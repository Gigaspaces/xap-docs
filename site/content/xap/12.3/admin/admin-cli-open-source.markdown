---
type: post123
title:  Administering Open Source Editions
categories: XAP123ADM,OSS
weight: 80
parent: none
---
 
The Command Line Interface tool can be used to perform minimal administration tasks for open source XAP and InsightEdge editions.

<br>

# XAP 

## Demo

*Command:* 

`xap demo`

*Description:* 

Run a Space in high availability mode (2 primaries with 1 backup each)

*Parameters and Options:*

None.
 
*Input Example:*

```bash
<XAP-HOME>/bin/xap demo
``` 


## Deploy a Space

*Command:* 

`xap space run`

*Description:* 

Deploys a Space in a stateful Processing Unit.

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | name | Name of the Space |
| Option    | ---ha | High availability adding a backup per partition. |
| Option    | ---instances=\<instances\> | Number of instances to run.|
| Option    | ---lus| Start a lookup service. |
|Option     | ---partitions=\<partitions\> |Number of partitions to use.|
 

*Input Example:*

This example deploys a Space named **mySpace** with high availability and 5 partitions. 

```bash
<XAP-HOME>/bin/xap space run --ha --partitions=5 mySpace
```

  


## Processing Unit


*Command:* 

`xap pu run`

*Description:* 

Deploys a Processing Unit onto the Service Grid.

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | path | 
Relative/absolute path of a Processing Unit directory or archive file.|
| Option    | ---ha | High availability adding a backup per partition. |
| Option    | ---instances=\<instances\> | Number of instances to run.|
| Option    | ---lus| Start a lookup service. |
| Option    | ---partitions=\<partitions\> |Number of partitions to use.|
 

*Input Example:*

This example deploys a Space named **mySpace** with high availability and 5 partitions. 

```bash
<XAP-HOME>/bin/xap pu run --ha --partitions=5 myPu
```


# Insightedge

