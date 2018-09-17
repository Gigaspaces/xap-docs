---
type: post122
title:  JMS Messages in XAP
categories: XAP122, OSS
parent: messaging-support.html
weight: 200
---

JMS messages implementation; supported and unsupported message types; message compression; accessing JMS messages via space API.


- JMS messages are implemented as externalizable `MetaDataEntries`, indexed, and routed to the space by the destination name.
- The messages contain the standard JMS headers, a body, and properties.
- Messages are created by calling the `Session.createXXXMessage()` methods.
- A producer can use a message more than once. Each call to the `MessageProducer.send()` method with the same `Message` instance writes a new message to the space.

### Supported JMS Message Types

The following message types are supported by GigaSpaces:

- `Message` -- a simple message that doesn't contain any payload (body).
- `BytesMessage` -- transports a stream of un interpreted bytes, typically used to duplicate the body of one of the other message types. `BytesMessages` are useful when reading raw data from a disk file, and transferring it 'as is' to another machine and/or location.
- `TextMessage` -- contains a `java.lang.string` for transporting string objects, especially XML documents.
- `ObjectMessage` -- transports any serializable Java object.
- `MapMessage` -- transports a self-defining set of name-value pairs, where names equal strings and values equal Java primitive types.

### Unsupported JMS Message Types

`StreamMessage` is not supported. `StreamMessage` transports a stream of Java primitive values, to be filled and read sequentially. Unlike a `BytesMessage`, a `StreamMessage` is aware of the type of primitives that are stored in it, and throws an exception if an attempt is made to read bytes and convert them to the wrong primitive.

### Message Compression

Compression for JMS text-message body-data has been supported since GigaSpaces version 5.1. The message's body is compressed only if its size is larger than a configured value (threshold).

The message's body is compressed when sent, and decompressed when received.

The compression threshold property is configured via the `com.gs.jms.compressionMinSize` system property. The value assigned should be in bytes. The default value is `500000` (0.5 MB).
