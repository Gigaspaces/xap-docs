---
type: post123
title:  Starting Embedded Mahalo
categories: XAP123ADM, OSS
parent: admin-legacy-tools.html
weight: 50
---

You can enable this option in one of the following ways:

- Set the following option to `true` in your container schema:


```xml
<embedded-services>
...
<mahalo>
	<!-- If true, will start an embedded Mahalo Jini Transaction Manager. Default value: false -->
    <start-embedded-mahalo>${com.gs.start-embedded-mahalo}</start-embedded-mahalo>
</mahalo>
```

- Set the following option as a JVM argument:

    -Dcom.gs.start-embedded-mahalo=true

- Set XPath in the `<XAP Root>\config\gs.properties` file:

    com.j_spaces.core.container.embedded-services.mahalo.start-embedded-mahalo=true

