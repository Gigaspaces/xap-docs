---
type: postsbp
title:  Priority Based Queue
categories: SBP
parent: processing.html
weight: 800
---

{{% tip %}}
**Summary:**  Implementing Priority based Queue <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0<br/>
**Last Update**: Feb 2011<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview

To implement a Priority based Queue you should use the `Take` operation with `SQLQuery` having the priority field used as part of the query. This field should use the `EXTENDED` index type.

You can query for all objects which got their priority bigger than X (`priority > X`) or query for objects based on a range (`Y > priority > X`). In both cases the objects will be taken from the space in an orderly manner based on their field values.

See below example code:


```java
GigaSpace space = new GigaSpaceConfigurer (new UrlSpaceConfigurer(spaceURL)).gigaSpace();
String queryStr = "priority>" + priorityMin + " and priority<"+ priorityMax;
SQLQuery query = new SQLQuery(Order.class.getName(), queryStr);
Object template = space.snapshot(query);
while (true) {
	Object result = space.take (template ,100000);
	if result(!=null)
	{
		...
	}
}
```

The Order Class will have the **Priority** indexed:


```java
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.metadata.index.SpaceIndexType;

@SpaceClass
public class Order {
	public Integer priority;
	public Integer id;
	public Long timestamp;
	public Order()
	{}

	public String toString()
	{
		return "id:" + id + " priority:" + priority + " timestamp:"+timestamp;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@SpaceIndex(type=SpaceIndexType.EXTENDED)
	@SpaceRouting
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
```

# Example

This is a [Priority based queue example](/attachment_files/sbp/PriorityBasedQueue.zip) - It includes 2 parts:

-- Master - Write Orders into the space with a random priority (1-9)
-- Worker - Consume Orders from the space where the consumption order is based on their priority value.

With this example we have one Master thread and two worker threads.

![prior_q.jpg](/attachment_files/sbp/prior_q.jpg)

This demo can be modified to have:

- Different amount of queues
- Different amount of workers that deals with different queue. For example - queue with high priority Orders will have more workers than low priority Orders:
Worker 1 : priorities 1-3 - High priories orders
Worker 2 : priorities 4-6 - Mid priories orders
Worker 3 : priorities 7-9 - Low priories orders

To run:

1. Extract [the attached](/attachment_files/sbp/PriorityBasedQueue.zip).
2. Load the example project into your IDE.
3. Set the project classpath to include GigaSpaces jars located under `\gigaspaces-xap\lib\required` folder.
4. Run the `com.gigaspaces.examples.priorityq.PriorityQueueExample` main.

Expected output:


```java
Welcome to GigaSpaces Priority Based Queue Example
....
Connect to space:/./mySpace OK!
Master Started - Thread ID:99
Worker 1 Started Query:priority>0
Worker 0 Started Query:priority>0
Worker 0 priority>0 :Processing Order:id:85 priority:1 timestamp:1270582548559
Worker 1 priority>0 :Processing Order:id:82 priority:1 timestamp:1270582548499
Worker 0 priority>0 :Processing Order:id:67 priority:1 timestamp:1270582548199
Worker 1 priority>0 :Processing Order:id:56 priority:1 timestamp:1270582547978
Worker 0 priority>0 :Processing Order:id:48 priority:1 timestamp:1270582547818
Worker 1 priority>0 :Processing Order:id:44 priority:1 timestamp:1270582547738
Worker 0 priority>0 :Processing Order:id:38 priority:1 timestamp:1270582547618
Worker 1 priority>0 :Processing Order:id:35 priority:1 timestamp:1270582547558
Worker 0 priority>0 :Processing Order:id:21 priority:1 timestamp:1270582547275
Worker 1 priority>0 :Processing Order:id:15 priority:1 timestamp:1270582547155
Worker 0 priority>0 :Processing Order:id:13 priority:1 timestamp:1270582547115
Worker 1 priority>0 :Processing Order:id:8 priority:1 timestamp:1270582547015
Worker 0 priority>0 :Processing Order:id:89 priority:2 timestamp:1270582548639
Worker 1 priority>0 :Processing Order:id:87 priority:2 timestamp:1270582548599
Worker 0 priority>0 :Processing Order:id:68 priority:2 timestamp:1270582548219
Worker 1 priority>0 :Processing Order:id:53 priority:2 timestamp:1270582547918
Worker 0 priority>0 :Processing Order:id:47 priority:2 timestamp:1270582547798
Worker 1 priority>0 :Processing Order:id:36 priority:2 timestamp:1270582547578
Worker 0 priority>0 :Processing Order:id:34 priority:2 timestamp:1270582547538
Worker 0 priority>0 :Processing Order:id:84 priority:3 timestamp:1270582548539
Worker 1 priority>0 :Processing Order:id:25 priority:2 timestamp:1270582547355
Worker 0 priority>0 :Processing Order:id:80 priority:3 timestamp:1270582548459
Worker 1 priority>0 :Processing Order:id:77 priority:3 timestamp:1270582548399
Worker 0 priority>0 :Processing Order:id:58 priority:3 timestamp:1270582548019
Worker 1 priority>0 :Processing Order:id:51 priority:3 timestamp:1270582547878
Worker 0 priority>0 :Processing Order:id:46 priority:3 timestamp:1270582547778
Worker 0 priority>0 :Processing Order:id:42 priority:3 timestamp:1270582547698
Worker 0 priority>0 :Processing Order:id:37 priority:3 timestamp:1270582547598
Worker 1 priority>0 :Processing Order:id:43 priority:3 timestamp:1270582547718
Worker 1 priority>0 :Processing Order:id:7 priority:3 timestamp:1270582546995
Worker 0 priority>0 :Processing Order:id:32 priority:3 timestamp:1270582547498
Worker 1 priority>0 :Processing Order:id:2 priority:3 timestamp:1270582546889
Worker 0 priority>0 :Processing Order:id:94 priority:4 timestamp:1270582548739
Worker 1 priority>0 :Processing Order:id:90 priority:4 timestamp:1270582548659
Worker 0 priority>0 :Processing Order:id:73 priority:4 timestamp:1270582548319
Worker 1 priority>0 :Processing Order:id:62 priority:4 timestamp:1270582548099
Worker 0 priority>0 :Processing Order:id:60 priority:4 timestamp:1270582548059
Worker 1 priority>0 :Processing Order:id:55 priority:4 timestamp:1270582547958
Worker 0 priority>0 :Processing Order:id:54 priority:4 timestamp:1270582547938
Worker 1 priority>0 :Processing Order:id:30 priority:4 timestamp:1270582547455
Worker 0 priority>0 :Processing Order:id:12 priority:4 timestamp:1270582547095
Worker 1 priority>0 :Processing Order:id:9 priority:4 timestamp:1270582547035
Worker 0 priority>0 :Processing Order:id:6 priority:4 timestamp:1270582546973
Worker 1 priority>0 :Processing Order:id:81 priority:5 timestamp:1270582548479
Worker 0 priority>0 :Processing Order:id:76 priority:5 timestamp:1270582548379
Worker 1 priority>0 :Processing Order:id:72 priority:5 timestamp:1270582548299
Worker 0 priority>0 :Processing Order:id:69 priority:5 timestamp:1270582548239
Worker 1 priority>0 :Processing Order:id:66 priority:5 timestamp:1270582548179
Worker 0 priority>0 :Processing Order:id:50 priority:5 timestamp:1270582547858
Worker 1 priority>0 :Processing Order:id:45 priority:5 timestamp:1270582547758
Worker 0 priority>0 :Processing Order:id:41 priority:5 timestamp:1270582547678
Worker 0 priority>0 :Processing Order:id:18 priority:5 timestamp:1270582547215
Worker 1 priority>0 :Processing Order:id:27 priority:5 timestamp:1270582547395
Worker 0 priority>0 :Processing Order:id:17 priority:5 timestamp:1270582547195
Worker 1 priority>0 :Processing Order:id:11 priority:5 timestamp:1270582547075
Worker 0 priority>0 :Processing Order:id:93 priority:6 timestamp:1270582548719
Worker 1 priority>0 :Processing Order:id:86 priority:6 timestamp:1270582548579
Worker 0 priority>0 :Processing Order:id:83 priority:6 timestamp:1270582548519
Worker 1 priority>0 :Processing Order:id:65 priority:6 timestamp:1270582548159
Worker 0 priority>0 :Processing Order:id:63 priority:6 timestamp:1270582548119
Worker 1 priority>0 :Processing Order:id:20 priority:6 timestamp:1270582547255
Worker 0 priority>0 :Processing Order:id:3 priority:6 timestamp:1270582546909
Worker 1 priority>0 :Processing Order:id:1 priority:6 timestamp:1270582546794
Worker 0 priority>0 :Processing Order:id:91 priority:7 timestamp:1270582548679
Worker 1 priority>0 :Processing Order:id:74 priority:7 timestamp:1270582548339
Worker 0 priority>0 :Processing Order:id:71 priority:7 timestamp:1270582548279
Worker 1 priority>0 :Processing Order:id:59 priority:7 timestamp:1270582548039
Worker 0 priority>0 :Processing Order:id:57 priority:7 timestamp:1270582547999
Worker 0 priority>0 :Processing Order:id:49 priority:7 timestamp:1270582547838
Worker 1 priority>0 :Processing Order:id:52 priority:7 timestamp:1270582547898
Worker 0 priority>0 :Processing Order:id:39 priority:7 timestamp:1270582547638
Worker 1 priority>0 :Processing Order:id:31 priority:7 timestamp:1270582547475
Worker 0 priority>0 :Processing Order:id:26 priority:7 timestamp:1270582547375
Worker 1 priority>0 :Processing Order:id:22 priority:7 timestamp:1270582547295
Worker 0 priority>0 :Processing Order:id:16 priority:7 timestamp:1270582547175
Worker 1 priority>0 :Processing Order:id:14 priority:7 timestamp:1270582547135
Worker 0 priority>0 :Processing Order:id:5 priority:7 timestamp:1270582546949
Worker 1 priority>0 :Processing Order:id:4 priority:7 timestamp:1270582546929
Worker 0 priority>0 :Processing Order:id:96 priority:8 timestamp:1270582548779
Worker 1 priority>0 :Processing Order:id:78 priority:8 timestamp:1270582548419
Worker 0 priority>0 :Processing Order:id:75 priority:8 timestamp:1270582548359
Worker 1 priority>0 :Processing Order:id:64 priority:8 timestamp:1270582548139
Worker 0 priority>0 :Processing Order:id:28 priority:8 timestamp:1270582547415
Worker 1 priority>0 :Processing Order:id:24 priority:8 timestamp:1270582547335
Worker 0 priority>0 :Processing Order:id:23 priority:8 timestamp:1270582547315
Worker 1 priority>0 :Processing Order:id:19 priority:8 timestamp:1270582547235
Worker 0 priority>0 :Processing Order:id:95 priority:9 timestamp:1270582548759
Worker 1 priority>0 :Processing Order:id:92 priority:9 timestamp:1270582548699
Worker 0 priority>0 :Processing Order:id:88 priority:9 timestamp:1270582548619
Worker 1 priority>0 :Processing Order:id:79 priority:9 timestamp:1270582548439
Worker 0 priority>0 :Processing Order:id:70 priority:9 timestamp:1270582548259
Worker 1 priority>0 :Processing Order:id:61 priority:9 timestamp:1270582548079
Worker 0 priority>0 :Processing Order:id:40 priority:9 timestamp:1270582547658
Worker 1 priority>0 :Processing Order:id:33 priority:9 timestamp:1270582547518
Worker 0 priority>0 :Processing Order:id:29 priority:9 timestamp:1270582547435
Worker 1 priority>0 :Processing Order:id:10 priority:9 timestamp:1270582547055
```

