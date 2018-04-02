---
type: post123
title:  Administering Open Source Editions
categories: XAP123ADM,OSS
weight: 80
parent: none
---
 
The Command Line Interface tool can be used to perform minimal administration tasks for open source XAP and InsightEdge editions. The available commands are described below.

{{%tabs%}}
{{%tab "XAP Open Source"%}}

## Run XAP in Demo Mode

*Command:* 

`xap demo`

*Description:* 

Runs a Space in high availability mode (2 primaries with 1 backup each).

*Parameters and Options:*

None.
 
*Input Example:*

```bash
<XAP-HOME>/bin/xap demo
``` 


## Run a Standalone Space

*Command:* 

`xap space run`

*Description:* 

Runs a standalone Space in a stateful Processing Unit.

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | name | Name of the Space. |
| Option    | ---lus| Start a lookup service. |
| Option    | ---partitions=\<partitions\> |Number of partitions to use.|
| Option    | ---ha | Run the Space with high availability (adding a backup per partition). |
| Option    | ---instances=1_1,1_2 | Specify one or more instances to run. If no instances are specified, runs all instances.|
 

*Input Example:*

This example runs a Space named **mySpace** with high availability and 2 partitions. The CLI commands start two instances for the first partition (1_1, 1_2) and two instances for the second partition (2_1, 2_2).

```bash
<XAP-HOME>/bin/xap space run --lus --partitions=2 --ha mySpace
```

To run instances separately, run each of the following commands on different hosts (note that --lus is specified for discovery):

```bash
<XAP-HOME>/bin/xap space run --lus --partitions=2 --ha --instances=1_1 mySpace
<XAP-HOME>/bin/xap space run --partitions=2 --ha --instances=1_2 mySpace
<XAP-HOME>/bin/xap space run --lus --partitions=2 --ha --instances=2_1 mySpace
<XAP-HOME>/bin/xap space run --partitions=2 --ha --instances=2_2 mySpace
```

## Run a Standalone Processing Unit

*Command:* 

`xap pu run`

*Description:* 

Runs a standalone Processing Unit.

*Parameters and Options:*


| Item | Name | Description |
|:-----|:-----|:------------|
| Parameter | path | Relative/absolute path of a Processing Unit directory or archive file.|
| Option    | ---lus| Start a lookup service. |
| Option    | ---partitions=\<partitions\> |Number of partitions to use.|
| Option    | ---ha | High availability adding a backup per partition. |
| Option    | ---instances=1_1,1_2 | Specify one or more instances to run. If no instances are specified, runs all instances.|


*Input Example:*

This example deploys a Processing Unit that contains a Space named **mySpace** with high availability and 2 partitions. 

```bash
<XAP-HOME>/bin/xap pu run --lus --ha --partitions=2 myPu.jar
```

{{%/tab%}}

{{%tab "InsightEdge Open Source"%}}

## Run InsightEdge in Demo Mode

*Command:*

`insightedge demo`

*Description:*

Run Spark in standalone mode (master, worker and Apache Zeppelin), and run a Space in high availability mode (2 primaries with 1 backup each).

*Parameters and Options:*

None.

*Input Example:*

```bash
<XAP-HOME>/bin/insightedge demo
```

{{%/tab%}}

{{% /tabs %}}