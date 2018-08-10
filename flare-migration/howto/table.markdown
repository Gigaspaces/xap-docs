---

type: post123
title: Table
weight: 1200
---

 

# Simple markdown table

Keyname       | Required | Type          | Description
-----------   | -------- | ----          | -----------
type          | yes      | string        | The [node type](dsl-spec-node-types.html) of this node template.
properties    | no       | dict          | The properties of the node template matching its node type properties schema.
instances     | no       | dict          | Instances configuration.
interfaces    | no       | interfaces    | Used for mapping plugins to [interfaces](dsl-spec-interfaces.html) operation or for specifying inputs for already mapped node type operations.
relationships | no       | relationships | Used for specifying the [relationships](dsl-spec-relationships.html) this node template has with other node templates.


# Without header

|      |  |           | |
|--------|-------|----|----------|
|type          | yes      | string        | The [node type](dsl-spec-node-types.html) of this node template.
|properties    | no       | dict          | The properties of the node template matching its node type properties schema.
|instances     | no       | dict          | Instances configuration.
|interfaces    | no       | interfaces    | Used for mapping plugins to [interfaces](dsl-spec-interfaces.html) operation or for specifying inputs for already mapped node type operations.
|relationships | no       | relationships | Used for specifying the [relationships](dsl-spec-relationships.html) this node template has with other node templates.

 

| Modifier and Type | Description | Default | Unit|
|:---------------|:------------|:--------|:----|
| T              | POJO, SpaceDocument|| |
|timeout         | Time to wait for the response| 0  |  milliseconds |
|query           | [ISpaceQuery]({{% api-javadoc %}}/com/gigaspaces/query/ISpaceQuery.html) |      | |
| [AsyncFutureListener]({{% api-javadoc %}}/com/gigaspaces/async/AsyncFutureListener.html) | Allows to register for a callback on an AsyncFuture to be notified when a result arrives||
| [ReadModifiers]({{% api-javadoc %}}/com/gigaspaces/client/ReadModifiers.html)| Provides modifiers to customize the behavior of read operations | NONE  |  |

 