---
type: post100net
title:  Space Document
categories: XAP100NET
weight: 200
parent: programmers-guide.html
---



{{% section %}}
{{% column  width="10%" %}}
![space-document.png](/attachment_files/subject/space-document.png)
{{% /column %}}

{{% column width="90%" %}}
The XAP document API exposes the space as [Document Store](http://en.wikipedia.org/wiki/Document-oriented_database). A document, which is represented by the class `SpaceDocument`, is essentially collection of key-value pairs, where the keys are strings and the values are primitives, `String`, `Date`, other documents, or collections thereof. Most importantly, the Space is aware of the internal structure of a document, and thus can index document properties at any nesting level and expose rich query semantics for retrieving documents.
{{% /column %}}
{{% /section %}}

<br>

{{%fpanel%}}

[Document API](./document-api.html){{<wbr>}}
Basic Document API

[Extending the Document](./document-extending.html){{<wbr>}}
XAP supports extending the SpaceDocument class to provide a type-safe wrapper for documents which is much easier to code with, while maintaining the dynamic schema.

[Interoperability](./document-object-interoperability.html){{<wbr>}}
Interoperability between Concrete Object and Document space entries.

{{%/fpanel%}}

