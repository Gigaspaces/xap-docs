---
type: post101
title:  Change API
categories: XAP101
weight: 400
parent: the-gigaspace-interface-overview.html
---

<br>

{{%section%}}
{{%column width="10%" %}}
![cassandra.png](/attachment_files/subject/change-api.png)
{{%/column%}}
{{%column width="90%" %}}
The [GigaSpace.change](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/org/openspaces/core/GigaSpace.html) and the [ChangeSet](http://www.gigaspaces.com/docs/JavaDoc{{% currentversion %}}/index.html?com/gigaspaces/client/ChangeSet.html) allows updating existing objects in Space, by specifying only the required change instead of passing the entire updated object.
{{%/column%}}
{{%/section%}}



<br>

{{%fpanel%}}
[Overview](./change-api.html){{<wbr>}}
Change API overview.

[Custom Change](./change-api-custom-operation.html){{<wbr>}}
A custom change operation lets the user implement his own change operation.

[Advanced Change Options](./change-api-advanced.html){{<wbr>}}
A SpaceSynchronizationEndpoint implementation can make use of the Change API and support change operation.
{{%/fpanel%}}

<br>

#### Additional Resources

{{%youtube "k2zGdUi_ntU"  "Custom Change API"%}}
