---
type: post140
title:  Space Document
categories: XAP140NET, PRM
weight: 300
parent: none
---


The XAP document API exposes the Space as {{%exurl "Document Store" "http://en.wikipedia.org/wiki/Document-oriented_database"%}}. A document, which is represented by the class `SpaceDocument`, is essentially a collection of key-value pairs, where the keys are strings and the values are primitives, `String`, `Date`, other documents, or collections thereof. Most importantly, the Space is aware of the internal structure of a document, and so can index document properties at any nesting level and expose rich query semantics for retrieving documents.

This section discusses the basic [Document API](./document-api.html), and also explains XAP support for [extending the Document](./document-extending.html) class to provide a type-safe wrapper for documents that is much easier to code with, while maintaining the dynamic schema.

XAP also supports [interoperability](./document-object-interoperability.html) between Concrete Object and Document Space entries.


