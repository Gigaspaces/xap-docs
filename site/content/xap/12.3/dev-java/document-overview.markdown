---
type: post123
title:  Space Document
categories: XAP123, OSS
parent: none
weight: 500
---


The XAP document API exposes the space as {{%exurl "Document Store" "http://en.wikipedia.org/wiki/Document-oriented_database"%}}. A document, which is represented by the class `SpaceDocument`, is essentially collection of key-value pairs, where the keys are strings and the values are primitives, `String`, `Date`, other documents, or collections thereof. Most importantly, the Space is aware of the internal structure of a document, and thus can index document properties at any nesting level and expose rich query semantics for retrieving documents.

<br>

{{%fpanel%}}
[Document API](./document-api.html){{<wbr>}}
Basic Document API

[Extending the Document](./document-extending.html){{<wbr>}}
XAP supports extending the SpaceDocument class to provide a type-safe wrapper for documents which is much easier to code with, while maintaining the dynamic schema.

[Interoperability](./document-pojo-interoperability.html){{<wbr>}}
XAP offers interoperability between documents and POJOs via the space
{{%/fpanel%}}

<br>

#### Additional Resources
{{%youtube "nBljYC3ciYc"  "Document API"%}}
