---
type: post
title:  Dynamic Processing 
categories: SBP
weight: 200
parent: hot-deploy.html
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Shay Hassidim | 12.0 | May 2017|  [No Space restart]({{%latestjavaurl%}}/the-space-no-restart.html) |      |


#  Dynamic Processing
Data processing in real-time microservices oriented systems may handle unstructured data , data that
may be feed by very disparate systems. Data may be sent from IoT , FiS , Life science , media , Telco ,
Logistics and other mission critical systems. Such data may be sent into an edge or a central system
running on the cloud or a private data center for processing.

This incoming data may need to go through complex flow of validation , formatting , transformation ,
enrichment, aggregation etc. before it can be used by the backend system. Quick access to this data
with minimal serialization and network utilization is essential to the system scalability , availability ,
reliability, optimized &amp; effective resource utilization, and overall system agility.

The best approach to achieve such system behavior is to consume and process the data exactly where it
is hitting its data store fabric. This means there is no need to move the incoming data into another
process where the actual processing logic is executed. This approach is critical when handling vast
amount of incoming data that need to be analyzed to produce real-time decisions.

# Updating Your Processing Logic with ZERO Downtime
The challenging part with this approach is updating the processing logic without any downtime. Due-to
the dynamic nature of the incoming data, you may need to update the processing logic. As the
processing logic running within the same process and classloader as the data you may need to build a
new PU of your statefull PU that package the most recent code of your processing logic (polling
container , notify container…), undeploy the existing PU and deploy the new version. This would need
the entire data to be loaded from some external data source or from the space native persistence
storage. Another option would be to use the Hot deploy approach which delivers zero downtime , but
still requires bouncing all space cluster partitions which require data reload from primary to backup
partitions that may take some time, CPU and network resources.

The Dynamic Processing approach illustrated here allows you to push processing logic into your live data
fabric space without any predefined configuration, without the need to stop the system when new
processing code is available (new release, patch, upgrade) - while the processing code is collocated with
its associated incoming data.  This simplifies the system deployment since there is no need to package
the processing code together with the data store (Space PU). You may push new updates of your
processing logic anytime. New versions of the processing logic will replace the existing one, continuing
the processing activity without any disruption.

#  SupportCodeChange Annotation

The Dynamic Processing using the [SupportCodeChange Annotation]({{%latestjavaurl%}}/the-space-no-restart.html) available with XAP 12.1. It allows you
to update Task , Custom Change and Custom Aggregator implementations without any downtime.
With the code example below we have the DynamicTask V1 implementation that return the value “A”:

```java
import org.openspaces.core.executor.Task;
import com.gigaspaces.annotation.SupportCodeChange;

@SupportCodeChange(id="1")
public class DynamicTask implements Task<String> {

    @Override
    public String execute() throws Exception {
        return new "A";
    }
}
```

Version 2 of the same DynamicTask class returns the value “B”:

```java
import org.openspaces.core.executor.Task;
import com.gigaspaces.annotation.SupportCodeChange;

@SupportCodeChange(id="2")
public class DynamicTask implements Task<String> {

    @Override
    public String execute() throws Exception {
        return new "B";
    }
}
```

#  The Dynamic Polling Container Example

With our example below we have:
 
- Statefull PU with a space running - Note that this PU doesn’t package any processing logic once deployed. Only the Space Data class.
- Feeder – Writing Data objects into the space that should be processed
- Processor – implements SpaceDataEventListener that is collocated with the space. It consumes Data object written into the space and processing these.
- MainTaskExecutor – Responsible to execute StartPollingContainerTask against the space. The StartPollingContainerTask delivers new versions of Processor object to the space. Once the Task is executed it stops existing polling containers running (in case such exists) and starting a new one using the encapsulated Processor class.
- SpaceModeListener – Responsible to identify primary instance failure and execute the StartPollingContainerTask against the new Primary (previously backup instance) to resume the processing activity.


![pic](/attachment_files/hotdeploy/dynamic-processing-1.png)


Once a new version of the processor is available (V2), you may call the Task Executor again with a new
version ID of the StartPollingContainerTask (V2). This will replace the existing PollingContainer that is
using V1 Processor with V2.

![pic](/attachment_files/hotdeploy/dynamic-processing-2.png)


#  Running the Example



The example includes:

- Data class – A space type class.
- Feeder class – Writing Data objects into the space
- Processor class implements SpaceDataEventListener – This is the polling container DataEventListener that performs the actual processing.
- StartPollingContainerTask class – A DistributedTask implementation that start a processor object. It will stop an existing processor in case such is already running.
- StopPollingContainerTask class - A DistributedTask implementation that stop a processor object. You may use it when required.
- pu.xml - Processing unit configuration file that include the space and a singleton object that holds the existing polling container.
   Note the pu.xml includes the following: 
   
```xml
<id ="eventContainerList" class ="java.util.ArrayList" scope="singleton" />
```
- SpaceModeListener implements SpaceModeChangedEventListener – This identify primary instance failure and execute StartPollingContainerTask with latest version of the Processor.


This demo will illustrate how collocated polling containers can be swapped without any downtime.  It is using the [@SupportCodeChange]({{%latestjavaurl%}}/the-space-no-restart.html) functionality available with 12.1.

Download the example:<br>
[Client Applications](/download_files/sbp/hotdeploy/DynamicPC.zip) <br>
[Space PU](/download_files/sbp/hotdeploy/space-pu.zip)

Basic running instructions:

1. Compile the provided classes within the Client Applications package. 
2. Deploy the Space PU into a GSC or run pu.xml within your IDE using IntegratedProcessingUnitContainer. Make sure Data class be included with the pu.xml IDE project. Use a separate project for the pu.xml. You can deploy a single or clustered space. 
3. Once the space is running , Start polling containers using MainTaskExecutor class. Please do not run this using the same project as the space in case you use IDE to run this demo. Use different projects for the space PU and client.
4. Start the feeder using the Feeder class. This will write Data objects into the space. The existing running collocated Processor will take these and “process” these.
5. Change the StartPollingContainerTask version to “2” by updating the SupportCodeChange id property to have the value 2. You may change the Processor class as needed. Compile the StartPollingContainerTask and the Processor.
6. Run MainTaskExecutor again.
7. You will see the polling container running now version 2 of the Processor class.
8. Run MainSpaceDataEventListener. If you terminate primary space instance (imitating a failure) you will see the backup that turned into primary will have the polling container running within it automatically. The MainSpaceDataEventListener can be deployed as a separate stateless PU to provide it high-availability.
