---
type: post123
title:  Replication Filters, OSS
categories: XAP123ADM
parent: replication.html
weight: 450
---


{{% ssummary %}} {{% /ssummary %}}



When constructing a replicated space topology you may need to call some business logic when data is replicated. GigaSpaces provides the `IReplicationFilter` plug-in interface [com.j_spaces.core.cluster.IReplicationFilter]({{% api-javadoc %}}/index.html?com/j_spaces/core/cluster/IReplicationFilter.html), that allows you to build business logic that is called when data is sent through the replication channel.

The `IReplicationFilter` methods are called before data is sent to the replication channel from the source space (output mode) and after coming out from the replication channel - i.e. before written to the target space (input mode). The replication filter should implement the `IReplicationFilter` interface methods.

{{%align center%}}
![replicationfilter.jpg](/attachment_files/replicationfilter.jpg)
{{%/align%}}

The replication filter can be used to monitor or alter the data passed through the replication channel. The replication channel passes `IReplicationFilterEntry` objects that store the replicated data. You should `DefaultReplicationFilterProviderFactory` and set its Replication Filter implementation when constructing the Space. You can use the same replication filter implementation class for both input and output replication modes. Here are the classes you will be using with your Replication Filter implementation:

- [IReplicationFilterEntry]({{% api-javadoc %}}/index.html?com/j_spaces/core/cluster/IReplicationFilterEntry.html) -- stores the space object data that is passed into the `IReplicationFilter`.
- [IReplicationFilter]({{% api-javadoc %}}/index.html?com/j_spaces/core/cluster/IReplicationFilter.html) -- a replication filter is an interface called when a replication event is triggered. Two types of replication filters can be defined -- an input replication and an output replication. If both of the classes specified (for input and output) are the same, only one filter object will be used for both input and output replication.
- [ReplicationFilterException]({{% api-javadoc %}}/index.html?com/j_spaces/core/cluster/ReplicationFilterException.html) -- the `ReplicationFilterException` is thrown when there are errors that occur in the replication filter.  Errors can happen in the source or target space. The error is wrapped as part of the `ReplicationFilterException` and thrown back to the client. The `ReplicationFilterException` includes methods that includes information about the origin of the error, replication mode (input/output), the implementation class and the underlying exception. The `ReplicationFilterException.getCause()` should be used to retrieve the original exception that occurred.

{{% refer %}}
 you can **control the replication at the operation level, using configuration only**. For more details, refer to the [Replication Operations](./replication-operations.html) section.
{{% /refer %}}

# Guidelines for Cluster Replication Filters

- In order to block a space object to be replicated, assign a `ReplicationOperationType.DISCARD` value as the operation type.
- Don't overwrite the `m_Key` (serial number) field of the packet.
- Object field values (`m_FieldsValues` array) may be changed, but notice that if the serialization type of the space is not 0 (that is, fields are serialized inside the space) -- then each non-native field (i.e. not from the Java.lang package) is stored in the array in a serialized format.
- For outgoing replication packets (output replication Filter), if you want to change  the values of some fields, deep-cloning of the `m_FieldsValues` array is needed, since the `m_FieldsValues` is a reference to the array stored in the space internal data structures.
- When using synchronous replication and an error has been occurs at the replication filter implementation, `ReplicationFilterException` is thrown back into the relevant thread at the client application. The ReplicationFilterException can originate at the source or target space. The ReplicationFilterException will include the relevant information to identify the origin and the underlying exception that caused the problem.
- When using asynchronous replication and an error occurs at the replication filter implementation, the space replication channel will be disabled, and an error will be logged into the space log file and displayed at the space console. The client application continues to function against its source space but there will not be any replication to the target space. In order to enable the replication, you should use the `IRemoteJSpaceAdmin.changeReplicationState()`.
- All replication packets are sent according to their replication policy. When either the Interval Milliseconds or the Interval Operations times out, a replication event is executed. `ReplicationOperationType.DISCARD` packets are sent when a sequence of operations performed on one space does not need to be performed again on the replicated members. For example, when using asynchronous replication mode, a sequence of write and take on the same object does not need to replicated. Therefore, a `ReplicationOperationType.DISCARD` packet is sent. In contrast, the take operation is always replicated to ensure data consistency.

# Statistics Example

The following Replication Filter displays replication activity statistics for outgoing activity (output filter). For each replication target (i.e. mirror , WAN gateway , backup instance), it generates an output (every 5 seconds) that includes info about each replication target , operation , space class , TP , Total operations and Delta since the last sampling. 



The replication filter:

```java
package com.test;

import java.util.concurrent.atomic.AtomicLong;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.cluster.IReplicationFilter;
import com.j_spaces.core.cluster.IReplicationFilterEntry;
import com.j_spaces.core.cluster.ReplicationPolicy;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StatsReplicationFilter implements IReplicationFilter {

	Map<String, AtomicLong> outputCounterMap = new HashMap<String, AtomicLong>();
	Map<String, Long> outputLastCounterMap = new HashMap<String, Long>();
	int samplingPeriodInSec = 5;
	ReportTP reporter;
	IJSpace space;

	@Override
	public void close() {
		System.out.println(this + " - MyReplicationFilter close");
		reporter.timer.cancel();
	}

	@Override
	public void init(IJSpace space, String arg1, ReplicationPolicy replicationPolicy) {
		System.out.println(this + " - MyReplicationFilter init " + space + " - replicationPolicy :" + replicationPolicy);
		reporter = new ReportTP();
		this.space = space;
	}

	@Override
	public void process(int direction, IReplicationFilterEntry filterEntry, String remoteSpaceMemberName) {
		switch (direction) {
		case IReplicationFilter.FILTER_DIRECTION_INPUT: {
			break;
		}
		case IReplicationFilter.FILTER_DIRECTION_OUTPUT: {
			AtomicLong tempCounter;
			StringBuffer _keyBuffer = new StringBuffer("OUTPUT - ").append(remoteSpaceMemberName).append("-").append(filterEntry.getOperationType().toString()).append("-").append(filterEntry.getClassName());
			String key = _keyBuffer.toString();
			if (outputCounterMap.containsKey(key)) {
				tempCounter = outputCounterMap.get(key);
			} else {
				tempCounter = new AtomicLong();
				outputCounterMap.put(key, tempCounter);
			}
			tempCounter.incrementAndGet();
			break;
		}
		}
	}

	public class ReportTP {
		Toolkit toolkit;
		Timer timer;

		public ReportTP() {
			toolkit = Toolkit.getDefaultToolkit();
			timer = new Timer(true);
			timer.schedule(new ReportTPTask(), 10, samplingPeriodInSec * 1000);
		}

		class ReportTPTask extends TimerTask {
			public void run() {
				List<String> stats = new ArrayList<String>();
				Iterator<String> iter = outputCounterMap.keySet().iterator();
				while (iter.hasNext()) {
					String _key = iter.next();
					AtomicLong tempCounter = outputCounterMap.get(_key);
					long recent = outputLastCounterMap.getOrDefault(_key, 0l);
					long delta = tempCounter.get() - recent;
					double TP = (double) (delta) / (double) samplingPeriodInSec;
					StringBuffer _out = new StringBuffer();
					_out.append(_key).append(" TP[Obj/sec]:").append(TP).append(" Total:").append(tempCounter.get()).append(" - Delta:").append(delta);
					stats.add(_out.toString());
					outputLastCounterMap.put(_key, tempCounter.get());
				}
				Collections.sort(stats);
				StringBuffer out = new StringBuffer ("----> ").append(new Date(System.currentTimeMillis())).append(" - ").append(space.toString()).append(":\n");
				for(String temp: stats){
					out.append(temp).append("\n");
				}
				System.out.println(out.toString());
			}
		}
	}
}
```

The output generated looks like this:

```
[gsc][2/6984]   ----> Fri Sep 30 11:52:27 EDT 2016 - wanSpaceUS_container1:wanSpaceUS:
[gsc][2/6984]   OUTPUT - gateway:GB-TAKE-com.test.Data TP[Obj/sec]:2800.0 Total:875000 - Delta:14000
[gsc][2/6984]   OUTPUT - gateway:GB-UPDATE-com.test.Data TP[Obj/sec]:3000.0 Total:328000 - Delta:15000
[gsc][2/6984]   OUTPUT - gateway:GB-WRITE-com.test.Data TP[Obj/sec]:3000.0 Total:877000 - Delta:15000
[gsc][2/6984]   OUTPUT - wanSpaceUS_container1_1:wanSpaceUS-LEASE_EXPIRATION-com.test.Data TP[Obj/sec]:0.0 Total:1000 - Delta:0
[gsc][2/6984]   OUTPUT - wanSpaceUS_container1_1:wanSpaceUS-TAKE-com.test.Data TP[Obj/sec]:2800.0 Total:875000 - Delta:14000
[gsc][2/6984]   OUTPUT - wanSpaceUS_container1_1:wanSpaceUS-UPDATE-com.test.Data TP[Obj/sec]:3000.0 Total:328000 - Delta:15000
[gsc][2/6984]   OUTPUT - wanSpaceUS_container1_1:wanSpaceUS-WRITE-com.test.Data TP[Obj/sec]:3000.0 Total:877000 - Delta:15000
```

# Blocking Replication  Example

The following example will start two spaces replicating data to each other. The replication filter will display the replicated data that is passed through the replication channel. The example displays all objects sent via the output filter. When an object with the data **Block me** is passed, it is blocking by setting the replication Operation Type to `ReplicationOperationType.DISCARD`.



{{%tabs%}}
{{%tab "  The Space Class "%}}


```java
package com.test;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;

@SpaceClass
public class MyClass {

		String id;
		String data;

		@SpaceId(autoGenerate = false)
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
}
```
{{% /tab %}}
{{%tab "  The Application "%}}


```java
package com.test;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.EmbeddedSpaceConfigurer;
import org.openspaces.core.space.filter.replication.DefaultReplicationFilterProviderFactory;

public class ReplicationFilterTestMain {

	public static void main(String[] args) throws Exception{
		DefaultReplicationFilterProviderFactory repFactory  = new DefaultReplicationFilterProviderFactory ();
		repFactory.setOutputFilter(new RepFilter());
		repFactory.afterPropertiesSet();

		GigaSpace gigaspace1 = new GigaSpaceConfigurer(
			new EmbeddedSpaceConfigurer("mySpace?cluster_schema=sync_replicated&total_members=2&id=1")
		.replicationFilterProvider(repFactory)).
		gigaSpace();

		GigaSpace gigaspace2 = new GigaSpaceConfigurer(
			new EmbeddedSpaceConfigurer("mySpace?cluster_schema=sync_replicated&total_members=2&id=2")).
		gigaSpace();

		MyClass o1 = new MyClass();
		o1.setId("1");
		o1.setData("AAA");
		gigaspace1.write(o1);

		MyClass o2 = new MyClass();
		o2.setId("2");
		o2.setData("Block me");

		gigaspace1.write(o2);

		MyClass o3 = gigaspace2.readById(MyClass.class,"1");
		if (o3 != null)
			System.out.println("Replicated Object ID 1 value is:" + o3.getData());
		MyClass o4 = gigaspace2.readById(MyClass.class,"2");
		if (o4 != null)
			System.out.println("Replicated Object ID 2 value is:" + o4.getData());
		else
			System.out.println("Object ID 2 has not been replicated");
	}
}
```

{{% /tab %}}

{{%tab "  The Replication Filter "%}}


```java
package com.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.client.ClientUIDHandler;
import com.j_spaces.core.cluster.IReplicationFilter;
import com.j_spaces.core.cluster.IReplicationFilterEntry;
import com.j_spaces.core.cluster.ReplicationOperationType;
import com.j_spaces.core.cluster.ReplicationPolicy;

public class RepFilter implements IReplicationFilter{

	@Override
	public void close() {

	}

	GigaSpace gigaspace = null;
	@Override
	public void init(IJSpace space, String paramUrl,
			ReplicationPolicy replicationPolicy) {
		// TODO Auto-generated method stub
		gigaspace  = new GigaSpaceConfigurer(space).gigaSpace();
		System.out.println("Rep Filter - Created "+gigaspace);
	}

	AtomicInteger counter = new AtomicInteger(0);
	@Override
	public void process(int direction,
		IReplicationFilterEntry replicationEntry,
		String remoteSpaceMemberName) {

	String filterDirectionStr = "";
	String operationCodeStr = "";

       switch( direction ) {
           case IReplicationFilter.FILTER_DIRECTION_INPUT:
            filterDirectionStr="INPUT";
           break;
            case IReplicationFilter.FILTER_DIRECTION_OUTPUT:
                filterDirectionStr="OUTPUT";
            break;
       }

       counter.incrementAndGet();   // increment the number of entries processed.

       System.out.println(
                "\nDefaultReplicationFilter"
                + "\n\t | Space: " + gigaspace
                + "\n\t | Packet No."+ counter
                + "\n\t | Direction: "+ filterDirectionStr
                + "\n\t | Operation code: "+ operationCodeStr
                + "\n\t | Entry packet UID: "
                + "\n\t | 2Str: "+ replicationEntry.toString()
                + replicationEntry.getUID() + "\n");

       /*
         * Lets Block the "Block me" object on its way out
         */
        if (direction == IReplicationFilter.FILTER_DIRECTION_OUTPUT
                && replicationEntry.getOperationType().equals(ReplicationOperationType.WRITE)
                && replicationEntry.getFieldsValues() != null
                && replicationEntry.getFieldValue("data").equals("Block me"))
        {
            System.out.println("\t | ==> Filter blocked outgoing object\n");
            // dismiss replication packet:
            replicationEntry.discard();
        }

	}
}
```
{{% /tab %}}
{{% /tabs %}}



