---
type: faq
title:  Space Objects
categories: FAQ
weight: 300
parent: none

---






{{%panel%}}
- [Can I implement my own custom serialization ?](#1)


{{%/panel%}}

{{%anchor 1%}}

### Can I implement my own custom serialization ?

You can implement the `Externalizable` interface on a Space class.  When the Space is accessed remotely these methods will be used to serialize the object. They are not called when an embedded Space proxy is accessed.

We do not recommend the Space class to implement `Externalizable` The internals of the XAP serialization mechanism has been optimized to serialize the Space object in a very efficient manner.
Users who want to customize this should implement Externalizable at the [Space class property level]({{%latestjavaurl%}}/storage-types-controlling-serialization.html). This is how XAP will leverage this as part of its own serialization mechanism.


You can use `kryo` and other serialization libraries. XAP is using it for example with the [global HTTP session sharing]({{%latestjavaurl%}}/global-http-session-sharing-overview.html) functionality.

{{%refer%}}
[Custom Serialization]({{%latestjavaurl%}}/custom-serialization.html)<br>
[Storage Types]({{%latestjavaurl%}}/storage-types-controlling-serialization.html)
{{%/refer%}}
