---
type: post110
title:  Metadata Summary
categories:
parent: space-based-remoting-overview.html
weight: 400
---

{{% ssummary %}}{{% /ssummary %}}


# RemotingService

| | |
|----|----|
|Class Annotation    | @RemotingService|
|Description         | Spring provides support for various remoting technologies. GigaSpaces uses the same concepts to provide remoting, using the space as the underlying protocol |
|Attribute Annotation| @ExecutorProxy  |
|Method argument     | @Routing |


{{%accordion%}}
{{%accord title="Example"%}}

```java
// Service Implementation
@RemotingService
public class DataProcessor implements IDataProcessor {

    public Data processData(Data data) {
    	data.setProcessor(true);
    	return data;
    }
}

// Client remoting proxy
public class DataRemoting {

    @ExecutorProxy
    private IDataProcessor dataProcessor;

    // ...
}

// remote method with routing parameter
public interface MyService {

    void doSomething(@Routing int param1, int param2);
}
```
{{%/accord%}}
{{%/accordion%}}

{{%refer%}}
[Space based remoting](./space-based-remoting.html)
{{%/refer%}}


# ExecutorProxy

| | |
|----|----|
|Attribute Annotation| @ExecutorProxy  |
|Description         | Spring provides support for various remoting technologies. GigaSpaces uses the same concepts to provide remoting, using the space as the underlying protocol |


{{%accordion%}}
{{%accord title="Example"%}}

```java
// Client remoting proxy
public class DataRemoting {

    @ExecutorProxy
    private IDataProcessor dataProcessor;

    // ...
}
```
{{%/accord%}}
{{%/accordion%}}

{{%refer%}}
[Space based remoting](./space-based-remoting.html)
{{%/refer%}}




# Routing

| | |
|----|----|
|Method argument     | @Routing |
|Description         | Spring provides support for various remoting technologies. GigaSpaces uses the same concepts to provide remoting, using the space as the underlying protocol |



{{%accordion%}}
{{%accord title="Example"%}}

```java
// Service Implementation
@RemotingService

   // remote method with routing parameter
   public interface MyService {

    void doSomething(@Routing int param1, int param2);
}
```
{{%/accord%}}
{{%/accordion%}}

{{%refer%}}
[Space based remoting](./space-based-remoting.html)
{{%/refer%}}

