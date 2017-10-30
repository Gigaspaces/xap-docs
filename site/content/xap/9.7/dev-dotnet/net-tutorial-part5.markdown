---
type: post97
title:  The Processing Unit
categories: XAP97NET
weight: 500
parent: net-home.html
---

{{%ssummary%}}

{{%/ssummary%}}


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


# The PU File structure

Here is an example how the directory structure looks like using Visual Studio:


```bash
|ProcessingUnit
|--common
|----Properties
|--------AssemblyInfo.cs
|----Payment.cs
|--processor
|----Properties
|--------AssemblyInfo.cs
|----PaymentEventProcessor.cs
|----pu.config
|----sla.xml
|----processor.sln
|--ProcessingUnit.sln
|
```


The file structure is composed of several key elements:

- pu.config (mandatory): This is the PU's deployment descriptor. These bindings include XAP specific components (such as the space for example). The pu.conf file typically contains definitions of XAP components (space, event containers, remote service exporters) and user defined beans.

- sla.xml (not mandatory): This file contains SLA definitions for the PU (i.e. number of instances, number of backup and deployment requirements). If its is not present, the default SLA will be applied. SLA definitions can also be specified at the deploy time via command line arguments.

- User class files: Your processing unit's classes (PaymentEventProcessor.cs)


{{%note "Building the project with Visual Studio"%}}

- Include in the References the GigaSpaceCore.dll
- Change the project properties so the output directory points to `GS_HOME\NET v...\deploy\[pu-name\]`. The Admin UI and the command line interface will find the PU's to deploy under this file structure.
- Configure the sla.xml and the pu.config file so that they are `[copy if newer]`

{{%/note%}}



# The pu.conf file
This file is an XML configuration file.

The definitions in the pu.config file are divided into 2 major categories:

- XAP specific components, such as space, event containers or remote service exporters.
- User defined beans, which define instances of user classes to be used by the PU. For example, user defined event handlers to which the event containers delegate events as those are received.


Here is an example of a pu.conf file:


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
		<ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
			<BasicContainer>
				<SpaceProxies>
					<add Name="ProcessingSpace" Url="/./eventSpace"/>
				</SpaceProxies>
			</BasicContainer>
		</ProcessingUnitContainer>
  </GigaSpaces.XAP>
</configuration>
```

{{%learn "/xap/9.7/dev-java/configuring-processing-unit-elements.html"%}}




# Service Level Agreement (SLA)

The SLA definitions can be provided as part of the PU package or during the PU's deployment process. They define the number of PU instances that should be running and deploy-time requirements such as clustering topology for PU's which contain a space. The GSM reads the SLA definition, and deploys the PU onto the available GSCs according to it.
A sample SLA definition is shown below:


```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/{{% currentversion %}}/sla/openspaces-sla.xsd">

      <os-sla:sla cluster-schema="partitioned-sync2backup"
            number-of-instances="2" number-of-backups="1"
            max-instances-per-vm="1">
       </os-sla:sla>
</beans>
```

{{%learn "/xap/9.7/dev-java/configuring-the-processing-unit-sla.html"%}}





# Deployment
When deploying the PU to the XAP Service Grid, the PU is uploaded to the XAP Manager (GSM) and extracted to the deploy directory of the local XAP installation (located by default under <XAP Root>/deploy).
Once extracted, the GSM processes the deployment descriptor and based on that provisions PU instances to the running XAP containers.

Each GSC to which a certain instance was provisioned, downloads the PU from the GSM, extracts it to its local working directory (located by default under <XAP Root>/work/deployed-processing-units) and starts the PU instance.


# Example
Our Online Payment system is expected to handle a large amount of concurrent users performing transactions. The system also needs to be highly available. This is where XAP's PU comes into play. We will create a polling container that takes a payment event as input and processes it. Then, we will deploy this code as a PU onto the IMDG. Payment events are being written into a space and the polling container will pick up the events and process them. We will use the pu.config file to define the deployment and add an SLA configuration to it to provide failover and scalability.

### Polling Container
First we define a polling container that will handle the business logic upon receiving a payment event. In our example we define a polling container that will receive events when a new payment is created:

```csharp
using System;

using GigaSpaces.Core.Events;
using GigaSpaces.XAP.Events.Polling;
using GigaSpaces.XAP.Events;

using xaptutorial.model;

[PollingEventDriven]
public class PaymentEventProcessor {

	// Define the event we are interested in
	[EventTemplate]
	Payment unprocessedData() {
		Payment template = new Payment();
		template.Status=ETransactionStatus.NEW;
		return template;
	}

	[DataEventHandler]
	public Payment eventListener(Payment ev) {
		Console.WriteLine("Payment received; processing .....");

		// set the status on the event and write it back into the space
		ev.Status=ETransactionStatus.PROCESSED;
		return ev;
	}
}
```



#### Create pu.conf
In this step will create the configuration file for the PU deployment


```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
  <configSections>
    <section name="GigaSpaces.XAP" type="GigaSpaces.XAP.Configuration.GigaSpacesXAPConfiguration, GigaSpaces.Core"/>
  </configSections>
  <GigaSpaces.XAP>
		<ProcessingUnitContainer Type="GigaSpaces.XAP.ProcessingUnit.Containers.BasicContainer.BasicProcessingUnitContainer, GigaSpaces.Core">
			<BasicContainer>
				<SpaceProxies>
					<add Name="ProcessingSpace" Url="/./eventSpace"/>
				</SpaceProxies>
			</BasicContainer>
		</ProcessingUnitContainer>
  </GigaSpaces.XAP>
</configuration>
```



#### Deployment
Now we have all the pieces that are necessary to create the deployment. After we built the project its time to deploy the PU onto the data grid. Again, you can do this in three ways; by script, c# code or via the admin UI. In our example will use the scripts to deploy the PU.

First we start the XAP Agent (GSA) that will create our IMDG on this machine:



```bash
GS_HOME\bin\gs-agent.exe
```



And now we deploy the PU onto the IMDG:

```bash
GS_HOME\bin\Gs-cli deploy  PaymentProcessor
```


If you startup the Admin UI you will be able to see that through the deployment a space called eventSpace was created and a PU named with the name `processing`.



#### Client interface

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
        // See note below !
        String url = "jini://*/*/eventSpace?groups=XAP-9.7.0-ga-NET-4.0.30319-x64";

        private ISpaceProxy proxy;

        [TestMethod]
	    public void postPayment() {


		// Create a payment
		Payment payment = new Payment();
		payment.CreatedDate=new DateTime();
		payment.MerchantId=1L;
		payment.PaymentAmount=120.70;
		payment.Status=ETransactionStatus.NEW;

		// write the payment into the space
		proxy.Write(payment);

        Thread.Sleep(10000);

        SqlQuery<Payment> query = new SqlQuery<Payment>("MerchantId=1");
        payment = proxy.Read<Payment>(query);

        Assert.AreEqual(payment.Status, ETransactionStatus.PROCESSED);
	}

       [TestInitialize]
        public void init()
        {
            proxy = GigaSpacesFactory.FindSpace(url);

        }
    }
}

```

{{%note%}}
When deploying the example with the admin UI, you need to configure the client space URL with the `groups` property default argument.
You will find the value for the property in `GS_HOME\NET v....\Config\Settings.xml`. For example:  \[<XapNet.Groups>XAP-9.7.0-ga-NET-2.0.50727-x64</XapNet.Groups>\]
{{%/note%}}

When you run this code you should see that the PU deployed onto the IMDG is processing the event, changes the status of the payment to PROCESSED and writes the event back into the space. The client then will receive an event because it has registered a listener that listens for processed payment events.

#### Deploy a PU with the WEB Admin UI
There is complete example available of  a PU on GitHub. You can download, build and deploy this example. Here is an example how you deploy a PU with the WEB admin UI:

{{%section%}}
{{%column%}}
Deploy PU

[<img src="/attachment_files/qsg/EventPU1.png" width="120" height="100">](/attachment_files/qsg/EventPU1.png)

{{%/column%}}

{{%column%}}
Applications deployed

[<img src="/attachment_files/qsg/EventPU2.png" width="120" height="100">](/attachment_files/qsg/EventPU2.png)

{{%/column%}}

{{%column%}}
Data Grid

[<img src="/attachment_files/qsg/EventPU3.png" width="120" height="100">](/attachment_files/qsg/EventPU3.png)

{{%/column%}}

{{%column%}}
Statistics

[<img src="/attachment_files/qsg/EventPU4.png" width="120" height="100">](/attachment_files/qsg/EventPU4.png)

{{%/column%}}
{{%/section%}}

{{%tryit "https://github.com/Gigaspaces/xapnet-tutorial"%}}

#### Failover and Scalability
One of our non functional requirements for our online payment system is that it is highly available and it can handle a large amount of concurrent transactions. This can be accomplish in a couple of ways. We can deploy the PU with multiple concurrent threads and or multiple PU instances on top of the grid.

#### Multi threaded PU
By default the PU is single threaded. With a simple annotation you can tell XAP how many threads the PU should run with.

```csharp
[PollingEventDriven(Name = "DataProcessor", MinConcurrentConsumers = 1, MaxConcurrentConsumers = 4)]
public class PaymentProcessor {
    //......
}
```

#### Multiple PU's
Lets assume that we have two machines available for our deployment. We want to deploy 4 instances of our PU, two on each machine.

The deployment script for this scenario looks like this:

```bash

With a statefull PU, embedded space
./Gs-cli  deploy -cluster schema=partitioned total_members=4,0 -max-instances-per-machine 2 eventProcessor

With a stateless PU
./Gs-cli  deploy -cluster total_members=4 -max-instances-per-machine 2 eventProcessor
```

{{% note %}}
Deploying with the command line options will override the sla definitions
{{%/note%}}

{{%learn "/xap/9.7/admin/deploy-command-line-interface.html"%}}


