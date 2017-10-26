---
type: post97
title:  Metadata
categories: XAP97
parent: task-execution-overview.html
weight: 400
---

{{%ssummary%}}{{%/ssummary%}}


# Task Execution

|                   |                       |
|-------------------|-----------------------|
|Attribute Annotation|@TaskGigaSpace   |
|Description         | This annotation injects a clustered proxy into the Task implementation. This is useful when the Task should access other partitions.   |


{{%accordion%}}
{{%accord title="Example"%}}

```java
public class MyTask implements Task<Integer>  {

    @TaskGigaSpace
    private transient GigaSpace colocatedSpace;
    private transient GigaSpace clusteredSpace;

    public MyTask() {
    }
....
}
```
{{%/accord%}}
{{%/accordion%}}

{{%learn "./task-execution-overview.html"%}}


# Task Routing

|                   |                       |
|-------------------|-----------------------|
|Method Annotation|@SpaceRouting  |
|Description         | This annotation selects the routing value. In case it is a POJO defined with a @SpaceRouting on one of its properties, the value of that property will be used as the routing information when passed as a parameter.   |


{{%accordion%}}
{{%accord title="Example"%}}

```java
public class MyTask implements Task<Integer>  {

    @SpaceRouting
    public Integer routing() {
       return this.value;
    }
....
}
```
{{%/accord%}}
{{%/accordion%}}
{{%learn "./task-execution-overview.html"%}}
