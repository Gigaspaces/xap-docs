---

type: post
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


