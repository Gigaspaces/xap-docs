---
type: post
title:  Space 
weight: 300
parent: admin-tools-overview.html
---


# Deploying a Space 
(automatically deploys a PU that contains only a Space)

##  Parameters

|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name    |  The name of the Space | Yes |

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---ha       |  Should backups be used for high availability | |
| ---partitions=\<partitions\>    |  Number of partitions    |  |
| ---requires-isolation   | if each instance should be provisioned in an isolated container|  |

## Example
 
```bash
<XAP-HOME>/bin/xap space deploy mySpace
```
 
# List 

Lists all spaces

## Example

```bash
<XAP-HOME>/bin/xap space list
```
 

# List Instances

|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name    |  The name of the Space | Yes |

## Options 


## Example

```bash
<XAP-HOME>/bin/xap space list-instances mySpace
```
 

# Space Info

|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name    |  The name of the Space | Yes |

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---operation-stats | Get space operation statistics | |
| ---type-stats      | Get space types statistics | |


## Example

```bash
<XAP-HOME>/bin/xap space info --operation-stats mySpace
```
 


# Space Instance Info

|  Parameter  |  Description    | Required | Reference |
|:-----|:-----|:----------------------------|:---------|
| instanceId    |  Space instance Id | Yes | | 

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---operation-stats | Get space operation statistics | |
| ---type-stats      | Get space types statistics | |
| ---replication-stats | Get replication statistics | |


## Example

```bash
<XAP-HOME>/bin/xap space info-instance --operation-stats mySpace~1
```
 


# Run 

Under construction 

|  Parameter  |  Description    | Required |
|:-----|:-----|:----------------------------|
| name    |  The name of the Space | Yes |

## Options 

|  Option  |  Description    |  Reference |
|:-----|:-----|:----------------------------|
| ---ha | Should the space include backups for high availability | |
| ---instances=\<instances\>      | Which instances should be run (default is all) | |
| ---partitions=\<partitions\>      | Number of partitions in space | |
| ---version | DIsplay version information | |


## Example

```bash
<XAP-HOME>/bin/xap space run --partitions=2 mySpace
```
 
