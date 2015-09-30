
---
title: Tabs test
type: howto
weight: 110
---

# Tabs

{{%tabs%}}
{{%tab Java%}}

```java
public class hello1{

}
```
{{%/tab%}}


{{%tab CPP%}}
```cpp
public class hello2{

}
```
{{%/tab%}}
{{%/tabs%}}




{{%tabs%}}
{{%tab table%}}

Keyname       | Required | Type          | Description
-----------   | -------- | ----          | -----------
type          | yes      | string        | The [node type](dsl-spec-node-types.html) of this node template.
properties    | no       | dict          | The properties of the node template matching its node type properties schema.
instances     | no       | dict          | Instances configuration.
interfaces    | no       | interfaces    | Used for mapping plugins to [interfaces](dsl-spec-interfaces.html) operation or for specifying inputs for already mapped node type operations.
relationships | no       | relationships | Used for specifying the [relationships](dsl-spec-relationships.html) this node template has with other node templates.
{{%/tab%}}


{{%tab CPP%}}
```
public class hello2{

}
```
{{%/tab%}}
{{%/tabs%}}
