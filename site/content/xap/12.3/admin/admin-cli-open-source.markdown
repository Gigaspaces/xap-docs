---
type: post123
title:  Administering Open Source Editions
categories: XAP123ADM,OSS
weight: 80
parent: none
---
 
The Command Line Interface tool can be used to perform minimal administration tasks for open source XAP and InsightEdge editions.

<br>

# XAP Open Source Edition

## Demo

*Command:* 

`xap demo`

*Description:* 

Run a Space in high availability mode (2 primaries with 1 backup each)

*Parameters and Options:*

None.
 
*Input Example:*

```bash
xap demo
``` 


## Run a standalone Space

*Command:* 

`xap space run`

*Description:* 

Run a standalone Space in a stateful Processing Unit.

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | name | Name of the Space |
| Option    | ---lus| Start a lookup service. |
| Option    | ---partitions=\<partitions\> |Number of partitions to use.|
| Option    | ---ha | High availability adding a backup per partition. |
| Option    | ---instances=1_1,1_2 | Specify one or more instances to run. If no instances are specified, runs all instances.|
 

*Input Example:*

This example runs a Space named **mySpace** with high availability and 2 partitions.
This will start two instances for the first partition (1_1, 1_2) and two instances for the second partition (2_1, 2_2).

```bash
xap space run --lus --partitions=2 --ha mySpace
```

To run instances separately, run each of the following commands on different hosts (note that --lus is specified for discovery):
```bash
xap space run --lus --partitions=2 --ha --instances=1_1 mySpace
xap space run --partitions=2 --ha --instances=1_2 mySpace
xap space run --lus --partitions=2 --ha --instances=2_1 mySpace
xap space run --partitions=2 --ha --instances=2_2 mySpace
```

## Processing Unit

*Command:* 

`xap pu run`

*Description:* 

Run a standalone Processing Unit

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | path | Relative/absolute path of a Processing Unit directory or archive file.|
| Option    | ---lus| Start a lookup service. |
| Option    | ---partitions=\<partitions\> |Number of partitions to use.|
| Option    | ---ha | High availability adding a backup per partition. |
| Option    | ---instances=1_1,1_2 | Specify one or more instances to run. If no instances are specified, runs all instances.|


*Input Example:*

This example deploys a Space named **mySpace** with high availability and 5 partitions. 

```bash
xap pu run --lus --ha --partitions=2 myPu.jar
```


# InsightEdge Open Source Edition

## Demo

*Command:*

`insightedge demo`

*Description:*

Run Spark in standalone mode (Master, Worker and Zeppelin) and run a Space in high availability mode (2 primaries with backup each).

*Parameters and Options:*

None.

*Input Example:*

```bash
insightedge demo
```
