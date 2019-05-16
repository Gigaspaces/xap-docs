---
type: post123
title:  Creating a Processing Unit
categories: XAP123NET, PRM
parent: xapnet-basics.html
weight: 600
canonical: auto
---



The PU is the fundamental unit of deployment in XAP. The PU itself runs within a Processing Unit Container and is deployed onto the Service Grid. Once a PU is deployed, a PU instance is the actual runtime entity.

There are two types of Processing Unit Containers:

- Integrated Processing Unit Container
A container that runs the PU inside VisualStudio. The integrated processing unit container enables to run the PU inside VisualStudio for testing and debugging purposes.

- Service Grid Processing Unit Container
A Processing Unit Container which runs within a Grid Service Container. It enables running the PU within a service grid, which provides self-healing and SLA capabilities to components deployed on it.

# Processing Unit (PU)
The PU is a deployable, independent, scalable unit, which is the building block for the Space Based Architecture (SBA). The PU is a combination of service beans and/or an embedded space instance.

There are several types of PU's; data only, business-logic only, mixed PU's (which contain both data and business logic) and special purpose PU's.

#### Data only PU
This type of PU does not include any business logic, only a Space. The PU simply defines the runtime characteristics of the space, i.e. its runtime topology, the number of space replicas/partitions, etc.

#### Business-logic only PU
The Business-logic Only PU implements your application code, and does not include any data. Typically, your code interacts with a remote Space which is defined by another PU. By defining the PU as business logic only, you create an application server which is hosted and monitored by the XAP Service Grid.

#### Mixed PU
This type of PU's includes both business logic and a space. Typically, the business logic interacts with a local space instance (i.e. a data grid instance running within the same PU instance) to achieve lowest possible latency and best performance.

# Creating a Processing Unit

A processing unit is essentially a .NET class library with a deployment descriptor called `pu.config`. Creating a processing unit is simple:

1. In Visual Studio, Create a new `Class Library` project.
2. Add a reference to `GigaSpaces.Core.dll` from the product's `bin` folder.
3. Add an xml file called `pu.config` to the project.
4. Right-click `pu.config`, select **Properties**, and modify the {{%exurl "Copy to Output Directory" "http://msdn.microsoft.com/en-us/library/0c6xyb66%28v=vs.100%29.aspx"%}} to **Copy Always** (or **Copy If Newer**).
5. Copy the following configuration into `pu.config`:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="ProcessingUnit" type="GigaSpaces.XAP.Configuration.ProcessingUnitConfigurationSection, GigaSpaces.Core"/>
  </configSections>
  <ProcessingUnit>
    <!-- Processing unit configuration goes here -->
  </ProcessingUnit>
</configuration>
```

{{%note%}}
 PU Configuration SnippetsFrom here on processing unit configuration snippets will usually be shortened to focus on the `<ProcessingUnit>` tag.
 {{%/note%}}



# Service Level Agreement (SLA)

The SLA definitions can be provided as part of the PU package or during the PU's deployment process. They define the number of PU instances that should be running and deploy-time requirements such as clustering topology for PU's which contain a space. The GSM reads the SLA definition, and deploys the PU onto the available GSCs according to it.

To include the SLA in the processing unit, add an xml file called `sla.xml` and modify its **Copy To Output Directory** setting (same as `pu.config`).

A sample SLA definition is shown below:


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd">

      <os-sla:sla cluster-schema="partitioned"
            number-of-instances="2" number-of-backups="1"
            max-instances-per-vm="1">
       </os-sla:sla>
</beans>
```

{{%refer%}}
[SLA Overview](../admin/the-sla-overview.html)
{{%/refer%}}


# Deployment
When deploying the PU to the XAP Service Grid, the PU is uploaded to the XAP Manager (GSM) and extracted to the deploy directory of the local XAP installation (located by default under &lt;XAP Root&gt;\deploy).
Once extracted, the GSM processes the deployment descriptor and based on that provisions PU instances to the running XAP containers.

Each GSC to which a certain instance was provisioned, downloads the PU from the GSM, extracts it to its local working directory (located by default under &lt;XAP Root&gt;\work\deployed-processing-units) and starts the PU instance.

{{%tip "Building directly to the deploy folder"%}}
A common practice is to change the PU project output directory to `GS_HOME\deploy\[pu-name\]`, since this is the default path used by the GUI and the command line interface will find the PU's to deploy under this file structure.
{{%/tip%}}

# Example
Our Online Payment system is expected to handle a large amount of concurrent users performing transactions. The system also needs to be highly available. This is where XAP's PU comes into play. We will create a polling container that takes a payment event as input and processes it. Then, we will deploy this code as a PU onto the IMDG. Payment events are being written into a space and the polling container will pick up the events and process them. We will use the `pu.config` file to define the deployment and add an SLA configuration to it to provide failover and scalability.

### Polling Container
First we define a polling container that will handle the business logic upon receiving a payment event. In our example we define a polling container that will receive events when a new payment is created:

```csharp
using System;

using GigaSpaces.Core.Events;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events;

using xaptutorial.model;

[PollingEventDriven]
public class PaymentEventProcessor 
{
	// Define the event we are interested in
	[EventTemplate]
	Payment unprocessedData()
	{
		return new Payment { Status=ETransactionStatus.NEW };
	}

	[DataEventHandler]
	public Payment eventListener(Payment ev)
	{
		Console.WriteLine("Payment received; processing .....");
    	// set the status on the event and write it back into the space
		ev.Status=ETransactionStatus.PROCESSED;
		return ev;
	}
}
```

### Processing Unit

Next, we'll configure `pu.config` to create an embedded space for the polling container:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="ProcessingUnit" type="GigaSpaces.XAP.Configuration.ProcessingUnitConfigurationSection, GigaSpaces.Core"/>
  </configSections>
  <ProcessingUnit>
    <EmbeddedSpaces>
      <add Name="eventSpace"/>
    </EmbeddedSpaces>
  </ProcessingUnit>
</configuration>
```

### Deployment
Now we have all the pieces that are necessary to create the deployment. After we built the project its time to deploy the PU onto the data grid. Again, you can do this in three ways; by script, c# code or via the admin UI. In our example will use the scripts to deploy the PU.

First let's launch `gs-agent.exe` from the product's `bin` folder - this is the XAP Agent (GSA) that will host our IMDG on this machine.

Next we deploy the PU onto the IMDG:

```bash
GS_HOME\bin\gs-cli deploy PaymentProcessor
```

If you startup the Admin UI you will be able to see that through the deployment a space called eventSpace was created and a PU named with the name `processing`.

### Client interface

Now its time to create a client that creates events and writes them into the space.


```csharp
using System;
using System.Threading;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GigaSpaces.Core;
using common;

namespace UnitTest
{
    [TestClass]
    public class UnitTest
    {
        private ISpaceProxy proxy;

	    [TestInitialize]
		public void Initialize()
		{
			proxy = new SpaceProxyFactory("eventSpace").Create();
		}

        [TestMethod]
	    public void PostPayment()
		{
			// Create a payment and write it to the space:
			proxy.Write(new Payment { CreatedDate=new DateTime(), MerchantId=1L, PaymentAmount=120.70, Status=ETransactionStatus.NEW });
			// Wait for the payment to be processed by the event container
			Thread.Sleep(10000);
			// Read the payment from the space
			payment = proxy.Read<Payment>(new SqlQuery<Payment>("MerchantId=1"));
			// Test the payment
			Assert.AreEqual(payment.Status, ETransactionStatus.PROCESSED);
		}
	}
}
```

When you run this code you should see that the PU deployed onto the IMDG is processing the event, changes the status of the payment to PROCESSED and writes the event back into the space. The client then will receive an event because it has registered a listener that listens for processed payment events.

## Deploy a PU with the WEB Admin UI
There is complete example available of  a PU on GitHub. You can download, build and deploy this example. Here is an example how you deploy a PU with the WEB admin UI:

{{%section%}}
{{%column width="25%"%}}
Deploy PU
{{%popup   "/attachment_files/qsg/EventPU1.png" "Deploy PU"%}}
{{%/column%}}

{{%column width="25%"%}}
Applications deployed
{{%popup   "/attachment_files/qsg/EventPU2.png" "Applications deployed"%}}
{{%/column%}}

{{%column width="25%"%}}
Data Grid
{{%popup   "/attachment_files/qsg/EventPU3.png" "Data Grid"%}}
{{%/column%}}

{{%column width="25%"%}}
Statistics
{{%popup   "/attachment_files/qsg/EventPU4.png" "Statistics"%}}
{{%/column%}}
{{%/section%}}

 

### Failover and Scalability
One of our non functional requirements for our online payment system is that it is highly available and it can handle a large amount of concurrent transactions. This can be accomplish in a couple of ways. We can deploy the PU with multiple concurrent threads and or multiple PU instances on top of the grid.

### Multi threaded Event Container
By default the event container is single threaded. With a simple annotation you can tell XAP how many threads the event container should run with.

```csharp
[PollingEventDriven(Name = "DataProcessor", MinConcurrentConsumers = 1, MaxConcurrentConsumers = 4)]
public class PaymentProcessor
{
    //......
}
```

#### Multiple PU's
Lets assume that we have two machines available for our deployment. We want to deploy 4 instances of our PU, two on each machine.

The deployment script for this scenario looks like this:

```bash
With a stateful PU, embedded space
gs-cli deploy -cluster schema=partitioned total_members=4,0 -max-instances-per-machine 2 eventProcessor
With a stateless PU
gs-cli deploy -cluster total_members=4 -max-instances-per-machine 2 eventProcessor
```

{{% note %}}
Deploying with the command line options will override the sla definitions
{{%/note%}}


{{%refer%}}
[Deploy Command Line Interface](../admin/deploy-command-line-interface.html)
{{%/refer%}}





