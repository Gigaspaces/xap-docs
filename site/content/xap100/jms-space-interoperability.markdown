---
type: post100
title:  JMS-Space Interoperability
categories: XAP100
parent: messaging-support.html
weight: 300
---

{{% ssummary%}}  {{% /ssummary %}}


JMS-space interoperability allows JMS applications to communicate with non-JMS applications using the space, without having to know or deal with the space API.

XAP introduced the ability for JMS applications to write messages to the space by implementing them as Externalizable `MetaDataEntries`, thus allowing non-JMS applications (usually space API applications) to read these `MetaDataEntries` using a template.

Furthermore, since XAP, an application using the space API can write JMS messages of any type to the space, using the space API (without knowing JMS). Therefore, it is possible to handle JMS messages the same way as any other Entry type.

With XAP, it is possible for the JMS application to control and decide exactly which type of object is written to the space, as long as the written object is valid for the receiving/reading application. This is done using the new [MessageConverter](#Writing POJOs/Entries to Space using JMS API -- Message Converter) feature. A common use-case is writing a JMS message to the space, where the message is "stripped" on the space side, leaving only the message body, usually a POJO. The space application can then read the POJO using a template that includes only the POJO type.

To summarize, the table below shows which operations are supported, allowing interoperability between the JMS and space API.


| Operation | JMS Application | Space Application |
|:----------|:---------------:|:-----------------:|
| Write JMS messages to space | {{<oksign>}} | {{<oksign>}} |
| Write Entries to space | {{<oksign>}}  | {{<oksign>}} |
| Write POJOs (or objects of any type) to space | {{<oksign>}} | {{<oksign>}} |
| Read JMS messages from space | {{<oksign>}} | {{<oksign>}} |
| Read Entries/POJOs from space | {{% remove %}} | {{<oksign>}} |

{{% note %}}
As shown above, a JMS application **cannot read POJOs/Entries** from the space, unless they are part of a JMS message.
{{%/note%}}

The following sections will show you how to use the `MessageConverter` to write objects to the space using the JMS API; and how to write JMS messages and read/take those messages using the space API.

{{% refer %}}To see how the `MessageConverter` is used with OpenSpaces, refer to the [The OpenSpaces Data Example](./the-openspaces-data-example.html).{{% /refer %}}

# Writing POJOs/Entries to Space using JMS API -- MessageConverter


This feature allows clients to define the outcome of JMS writes. By implementing a `MessageConverter`, you can convert JMS messages to a POJO before they are written to the space. The result of using a converter is that what is written to the space is not necessarily the JMS message (with all the headers, properties etc.), but the result of the message conversion.

A basic use-case of this feature might be writing POJOs to the space using standard JMS API, and not the space API.

A simple implementation of this conversion is the _`ObjectMessage2ObjectConverter`_ class. When the `MessageConverter` is invoked by passing an `ObjectMessage` to its `toObject` method, it returns the contained POJO (message's body). When using the `MessageConverter` to send `ObjectMessages`, what is actually written to the space is only the JMS message body, which contains the POJO. The `ObjectMessage` wrapper is not written.

You can create a custom implementation of the `MessageConverter`. The returned object can be an Entry, an object, an array of Entries, or an array of objects. The returned object can be the same JMS message passed as an argument, or a different one. Generally, the possibility of what can be returned from the conversion method are endless, however, the returned objects should be Entries or POJOs, valid for working with the space.

## IMessageConverter Interface

This interface defines an API for message conversion:


```java
interface IMessageConverter {
	Object toObject(javax.jms.Message m);
}
```

Implement the `IMessageConverter` interface to return the object required to be written to the space.

Following is the implementation code of `ObjectMessage2ObjectConverter`:


```java
class ObjectMessage2ObjectConverter implements IMessageConverter {
	Object toObject(javax.jms.Message msg) {
		if (msg != null && msg instanceof javax.jms.ObjectMessage) {
			return ((javax.jms.ObjectMessage)msg).getObject();
		{
		return msg;
	}
}
```

When passing an `ObjectMessage` to this converter, it returns the message body, which is actually the POJO.

## Setting MessageConverter for ConnectionFactory

You can configure a `ConnectionFactory` to use a `MessageConverter`.

Any `MessageProducer` created under this `ConnectionFactory` uses the converter automatically when sending messages.

### Offline Configuration

You can configure a `ConnectionFactory` to use a `MessageConverter` in the Open Spaces Spring configuration:


```xml
<bean id="messageConverter" class="com.j_spaces.jms.utils.ObjectMessage2ObjectConverter" />

<os-jms:connection-factory id="connectionFactory" giga-space="gigaSpace" message-converter="messageConverter" />
```

In this example, a `ConnectionFactory` is configured, and an instance of `ObjectMessage2ObjectConverter` is injected into it.

### Configuring MessageConverters During Runtime

You can use the `GSJMSAdmin` helper class to get a `ConnectionFactory` with a `MessageConverter`, by using one its methods. For the list of methods, see [Javadoc]({{% api-javadoc %}}/index.html?com/j_spaces/jms/utils/GSJMSAdmin.html).

Passing `null` as a `MessageConverter` means that the `ConnectionFactory` does not use a `MessageConverter`.

The following code uses the `ObjectMessage2ObjectConverter` to send instances of the `MyPOJO` class to the space:


```java
ObjectMessage2ObjectConverter converter = new ObjectMessage2ObjectConverter();
ConnectionFactory connectionFactory = GSJMSAdmin.getInstance().getConnectionFactory(space, converter);
...
ObjectMessage msg = session.createObjectMessage(new MyPOJO());
producer.send(msg);
```

## Setting MessageConverter per Message

It is possible to set a `MessageConverter` per sent message.

This is done by setting the `JMS_GSMessageConverter` property before calling `MessageProducer.send()`.

For example, to use the `ObjectMessage2ObjectConvertor` on a specific message:


```java
ObjectMessage2ObjectConverter converter = new ObjectMessage2ObjectConverter();
ObjectMessage msg = ...
msg.setObjectProperty("JMS_GSMessageConverter", converter);
producer.send(msg);
```

{{% note %}}
In this case, the `MessageConverter` is used even if another `MessageConverter` is set in the `ConnectionFactory`.
{{%/note%}}

## MessageConverter Resolution

In case a `MessageConverter` is set for a `ConnectionFactory`, and another `MessageConverter` is set in a message, GigaSpaces resolves this in the following order:

1. The `MessageConverter` is set in the message's `JMS_GSMessageConverter` property.
1. The `MessageConverter` is set in the `ConnectionFactory`.

{{% note %}}
If no `MessageConverter` is set, the JMS message is written as-is.

Only one `MessageConverter` is used each time.
{{%/note%}}

## Considerations

- Message conversion is performed before the space write action. When using transactions, the messages are written to the space only after the transaction is committed. Therefore, changing a `MessageConverter` during a transaction affects all conversions performed by this converter in this specific transaction.
For example, a converter that returns an object of type `A` -- during a transaction, a few messages are sent, and then the properties of the converter are changed to return an object of type `B`. When committing the transaction, all messages using this converter are converted to objects of type `B`.

- Setting the `JMS_GSMessageConverter` property to `null` is the same as disabling the `MessageConverter` set in the `ConnectionFactory`.
No conversion is performed and the JMS messages are written as-is.

- `TextMessage` compression is still performed if the returned object is a `TextMessage`.
- Setting the `JMS_GSMessageConverter` property with an object that doesn't implement `IMessageConverter` throws a `MessageFormatException`.
- After calling `MessageProducer.send()`, the message instance behaves as if no conversion occurred. The `JMS_GSMessageConverter` property in the returned object is unset.
- A destination has to be created, even if it is not used in the converted object.


# Writing and Reading JMS Messages using Space API


## Writing JMS messages

### 1. Create JMS Message Instance

To create a JMS message, use the new operator as follows:


```java
GSSimpleMessageImpl simpleMessage = new GSSimpleMessageImpl(null);
GSTextMessageImpl   textMessage   = new GSTextMessageImpl  (null);
GSObjectMessageImpl objectMessage = new GSObjectMessageImpl(null);
GSMapMessageImpl    mapMessage    = new GSMapMessageImpl   (null);
GSBytesMessageImpl  bytesMessage  = new GSBytesMessageImpl (null);
```

- Use `null` for the session argument.
- It is preferred not to use the default constructors, because they are meant to create `null` templates of the JMS messages.

### 2. Set Required Headers

Use the Message API to set the body, the header values, and the properties:


```java
// create the message
GSTextMessage textMessage = new GSTextMessage(null);

// set the message's body
textMessage.setText("This is my message.");

// set the message's headers
textMessage.setJMSMessageID("message1");

// set the message's properties
textMessage.setBooleanProperty("processed", false);
```

### 3. Write Message to Space

Use the space API to write the message in an ordinary way:


```java
spaceProxy.write(textMessage, null, Lease.Forever);
```

{{% info %}}
The `SpaceWriter` example that resides in `<XAP Root>\examples\Basic\helloJMS` uses this technique to write JMS messages to the space.
{{%/info%}}

## Reading/Taking JMS Messages

Like with any Entry/POJO type, to receive a JMS message from the space you need to create a template. The template is matched with the objects in the space, and matching objects are retrieved. All public members of the message class participate in the match process, including the properties map. Therefore, if you don't know the exact content of a message properties map, it is better to set it to `null` in the JMS message template.

### 1. Create Template Based on JMS Message Classes


```java
GSSimpleMessageImpl simpleMessageTemplate = new GSSimpleMessageImpl();
GSTextMessageImpl   textMessageTemplate   = new GSTextMessageImpl  ();
GSObjectMessageImpl objectMessageTemplate = new GSObjectMessageImpl();
GSMapMessageImpl    mapMessageTemplate    = new GSMapMessageImpl   ();
GSBytesMessageImpl  bytesMessageTemplate  = new GSBytesMessageImpl ();
```

To create a generic template for all kinds of JMS messages, create an instance of `GSMessageImpl` as follows:


```java
GSMessageImpl genericMessageTemplate = new GSMessageImpl();
```

Using the default constructors creates a `null` template of the JMS message. This means that the properties map is also `null`. If you do not use the default constructor, you should set the properties map to `null` by invoking:


```java
jsmMessageTemplate.setProperties(null);
```

### 2. Read/Take from the Space

You can use the template to read or take messages from the space. The following example takes a text message from the space:


```java
GSTextMessageImpl msg = (GSTextMessageImpl ) spaceProxy.take(textMessageTemplate, null, 1000L);
```

