---
type: post110
title:  Creating the Hello World Application
categories: XAP110TUT
weight: 200
parent: first-xap-app.html
---

{{%ssummary%}}{{%/ssummary%}}

Learn how to create and run a Processing Unit - a scalable unit of deployment, inside your development environment. Learn how to use the XAP basic API, by implementing a simple processor and feeder application.




# Components

There are two components in our scenario:

{{%imagertext "/attachment_files/helloworld_processor_processing_unit.gif" %}}

**Processor Processing Unit**

Processes Message objects as they are written to the data grid (Space)
It contains 3 components: {{<wbr>}}
* `Polling Container` component that listens to new Message objects written to the `Space`<br>
* `Processor Bean` that is delegated the actual processing work by the `Polling Container`.
{{%/imagertext%}}


{{%imagertext "/attachment_files/helloworld_feeder.gif" %}}

**Feeder**

An application that feeds unprocessed Message objects to the Space, and after a certain period of time, counts and reads one of them at random.

{{%/imagertext%}}

{{%imagertext "/attachment_files/Message.gif" %}}

**Message Object**

A simple POJO with an id and info attributes.

{{%/imagertext%}}



The Processing Unit itself runs within a dedicated processing unit container in a host environment. (This can be your IDE, any Java process, or the GigaSpaces Grid Service Container - more on this in the next tutorial.)





# The Workflow

{{%imagertext "/attachment_files/Helloworld_workflow.jpg" %}}
1. The _helloFeeder_ application writes 1000 Message objects (POJOs) to the _space_ and waits.

1. Inside the Processing Unit, the _Polling Container_ continuously removes unprocessed objects from the data grid (one at a time) and hands them to its _Processor Bean_ for processing.

1. After each Message object has been processed, the _Polling Container_ writes it back to the _Space_.
(Steps 2 & 3 repeat, until there are no more unprocessed Message objects left to process in the Space.)

1. After waiting 100 milliseconds (to allow for all the objects to be processed), the feeder counts all the processed Message objects inside the _Processor Processing Unit_'s _Space_, and reads one of them at random.

{{%/imagertext%}}

{{% anchor Jwalkthrough %}}


# Code walk through

You can download the full source code from {{%git "https://github.com/Gigaspaces/xap-example-helloworld" %}}


First let's take a look at the Message object that is being written to the space by the feeder application:

{{% anchor JObject %}}

# The Message Object

This is a simple POJO containing two attributes: _id_, which represents the object id, and _info_, which represents the information that this object holds. Both have setter and getter methods.

{{% info %}}
The getter for the id attribute is annotated with the `@SpaceRouting` annotation that is used to route Message objects when they are written to the space. This is necessary for scaling the application, and will be explained in the next tutorial. For now, just remember that this annotation should decorate one of the object's properties.
{{%/info%}}


```java
private Integer id;    // object id

public void setId(Integer id) {
    this.id = id;
}

@SpaceRouting
public Integer getId() {
    return id;
}
```



```java
private String info;    // info represents the info the object holds

public String getInfo() {
    return info;
}

public void setInfo(String info) {
    this.info = info;
}
```

A necessary default empty constructor and another constructor to construct
a new Message object with a given id and info:


```java
public Message() {   // Mandatory empty constructor

}

public Message(Integer id, String info) {
    this.id = id;

    this.info = info;
}
```

Next, let's take a look at the Processor Processing Unit.

{{% anchor Processor Processing Unit %}}

#### The Processor Processing Unit (pu.xml, Processor.java)

The Processor Processing Unit contains two components: a space (cache), which holds objects in memory, and a processor bean that takes, modifies and writes objects back to this space.


**Processor Processing Unit Configuration** (META-INF/spring/pu.xml)

A Processing Unit always has an XML file called pu.xml, that resides under the _META-INF\spring_ directory.
In fact, this is a standard [Spring framework](http://www.springframework.org) XML configuration file, with a number of custom GigaSpaces specific tags. Let's take a look at this file. In our example there are 3 main components contained within the Processing Unit:

Step 1. The first component is a _space_ (cache) instance embedded inside the Processing Unit, named **processorSpace**. It has a URL property.
On the second line, we define a transaction manager, which is referencing this space, and manages its transactions.
Finally a bean called _gigaSpace_ wraps the _space_, and provides a simple client API to interact with it, as we will see later in this tutorial.


```xml
<os-core:embedded-space id="space" name="processorSpace"/>
<os-core:distributed-tx-manager id="transactionManager" />
<os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>
```

Step 2. The second component is a **helloProcessor Bean**, which contains the method that does the actual processing. This bean is defined in the Processor.java source file, which is shown in the [next section](#Processor Bean).


```xml
<bean id="helloProcessor" class="org.openspaces.example.helloworld.processor.Processor"/>
```


The third, key component in this workflow is the **Polling Container**,  which continuously removes (takes) objects matching certain criteria from the space. The criteria are expressed in the form of a template object (also known as example object). In our case, the polling container is instructed to take objects of type Message. However, it does not take all instances of the Message class, only those whose "info" property equals the string "Hello ". When a match is found, the object is taken and passed to a listener bean - here the listener is the previously defined _Processor bean_. This bean has a method annotated with the @SpaceDataEvent annotation, which is invoked with the taken object as a parameter. It returns a processed Message object, which is written back to the space by the _Polling Container_.


```xml
<os-events:polling-container id="helloProcessorPollingEventContainer" giga-space="gigaSpace">
    <os-events:tx-support tx-manager="transactionManager"/>
    <os-core:template>
        <bean class="org.openspaces.example.helloworld.common.Message">
            <property name="info" value="Hello "/>
        </bean>
    </os-core:template>
    <os-events:listener>
        <os-events:annotation-adapter>
            <os-events:delegate ref="helloProcessor"/>
        </os-events:annotation-adapter>
    </os-events:listener>
</os-events:polling-container>
```

Next we'll see the source code for the _Processor_ bean.

{{% anchor Processor Bean %}}
**Processor Bean** (Processor.java)

We saw that the pu.xml file defines a bean called Processor. Now let's look at this bean's source code.
It has one method. The primary method, processMessage(), is annotated with the @SpaceDataEvent annotation. Previously we saw that the _Processor bean_ is referenced by the _Polling Container_ and acts as its listener. When a Message object is taken from the space by the _Polling Container_, this method is invoked with the object as an argument. It returns a processed object. In this example it simply adds the "World !!" string to the object's _info_ property:


```java
public class Processor {

    @SpaceDataEvent
    public Message processMessage(Message msg) {
        System.out.println("Processor PROCESSING : " + msg);
        msg.setInfo(msg.getInfo()+"World !!");
        return msg;
    }

    public Processor(){
      System.out.println("Processor instantiated...");
    }
}
```

Now we are ready to view feeder application that feeds Message objects to the space.



{{% anchor JFeeder %}}

#### The Feeder Application (Feeder.java)

The feeder `main` method constructs a new Feeder instance, and passes the space URL to it, to connect to the space.
Each space is identified uniquely by its name. In the processor processing unit, we defined the space with the URL "/./processorSpace", which means an **embedded** space named "processorSpace". Therefore the URL the feeder uses to connect to the space, is "jini:/\*/\*/processorSpace". Without getting into the details, it means that the Jini protocol is used to discover a space named "processorSpace" in the network. It is passed to the feeder as a program argument.
We then start writing the Message object to the space and then read the results from it.


```java
public static void main(String [] args) {
    if(args.length==0){
	System.out.println("Usage: java Feeder <space URL>");
	System.exit(1);
    }

    Feeder feeder = new Feeder (args[0]);   // create the feeder and connect it to the space

    feeder.feed(1000);   // run the feeder (start feeding)

    feeder.readResults();   // read back results
    
    feeder.close();

    System.exit(0);

}
```

Here's the constructor of the Feeder connects to the Processor Processing unit _Space_ by using the input URL:


```java
public Feeder(String spaceName){

     // Wrap the space with the gigaSpace API
     this.gigaSpace = new GigaSpaceConfigurer(new UrlSpaceConfigurer(spaceName)).gigaSpace();
}
```

The `feed()` method loops and writes Message objects to the space by using the `gigaSpace.write()` method:


```java
public void feed(int numberOfMessages){
    for(int counter=0;counter<numberOfMessages;counter++){
	   Message msg = new Message(counter, "Hello ");
	   gigaSpace.write(msg);
    }
System.out.println("FEEDER WROTE " + numberOfMessages + " messages");
}
```

Here's how all processed objects are read from the space, using template matching. The number of processed objects in the space (all of them should have their _info_ property set to "Hello World \!\!") is then printed out:


```java
public void readResults(){

    Message template = new Message();          // Create a template to read a Message with info
    template.setInfo("Hello World !!");        // attribute that equals "Hello World !!"

    // Read an object matching the template
    System.out.println("Here is one of them printed out: "+gigaSpace.read(template));

    //wait 100 millis for all to be processed:
    try{
         Thread.sleep(100);
    }catch(InterruptedException ie){ /*do nothing*/}

    // Count number of objects in the space matching the template
    int numInSpace=gigaSpace.count(template);

    System.out.println("There are "+numInSpace+" processed Message objects in the space now.");
}
```

Before existing, the space proxy of the spaced needed to be closed.


```java
 private void close() throws Exception {
 
        spaceProxyConfigurer.close();
    }
    
```
Next, we compile and run the sample application



{{% anchor JRun All In IDE %}}

### Compiling and Running the Application within your IDE



**Steps to run the application inside Eclipse IDE:**

If you haven't already done so,[download GigaSpaces and set up your development environment]({{%currentjavaurl%}}/installation.html)
- This is needed for running the tutorial sample application.

{{% anchor JImporting Project to the IDE %}}

**Importing the project into Eclipse**

Step 1. Import the **hello-common**, **hello-processor* and **hello-feeder** projects located under the `<XAP Root>/examples/helloworld` folder.
(After importing, you'll see some errors since the GS_HOME path variable is not set yet)


![ide-2.jpg](/attachment_files/ide-2.jpg)

{{%accordion%}}
{{%accord title=" How do I do that..."%}}
{{% panel %}}

**Importing the sample projects into the IDE**

1. Start Eclipse. A **Workspace Launcher Dialog** appears.
1. Write a new workspace name or select one of your existing workspaces, and click the **OK** button.
1. To import the project, select **File > Import ...** to open the import dialog
1. Select **Existing projects into workspace* and click *Next** to open the import project dialog
1. In the **Select root directory* field click the *Browse** button to open the browse dialog
1. Select the folder `/examples/helloworld` and click **OK**
1. Make sure all 3 projects are selected: **hello-common**, **hello-processor** and **hello-feeder**
1. Click **Finish**

{{% /panel %}}
{{%/accord%}}
{{%/accordion%}}

Step 2. Create a new Eclipse environment variable called **GS_HOME**, and point it to your GigaSpaces installation Root folder

![ide-3.jpg](/attachment_files/ide-3.jpg)

{{%accordion%}}
{{%accord title=" How do I do that..."%}}
{{% panel  %}}

**Setting an environment variable pointing to the XAP root folder**

1. Right Click on the **hello-common* project in the *Package Explorer tab** to open the _context menu_
1. Select **Build Path > Configure Build Path...** to open the _Java Build Path dialog_
1. Select the **Libraries tab* and click the *Add Variable...** button to open the _New Variable Classpath Entry dialog_
1. Click the **Configure Variables...** button to open the _Classpath Variables dialog_
1. Click the **New...** button to open the _New Variable Entry_ dialog
1. In the **Name** field write `GS_HOME` to name the variable
1. Click the **Folder...** button and browse to **your GigaSpaces installation root folder**
1. Select your **GigaSpaces installation root folder** and click **OK**
1. Click **OK** and **OK** again
1. Click **Yes** to do full rebuild
1. Close remaining dialogs

{{% /panel %}}
{{%/accord%}}
{{%/accordion%}}

{{% tip %}}
Make sure your project includes the latest Spring libraries located at `<XAP Root>\lib\required` folder.
{{% /tip %}}

{{% anchor Run Processor in IDE %}} **Running the Processor**

![ide-run-hello-proce-config.jpg](/attachment_files/ide-run-hello-proce-config.jpg)

Step 3. From the toolbar at the top of the screen, select **Run > Run Dialog...** to open the **Run** dialog
Step 4. Click the **+** to the left of **Java Application**, to unfold it
Step 5. Select the **Hello Processor** launch configuration, and click the **Run** button

{{% anchor JRun Feeder in IDE %}}

**Waiting for the Processor to instantiate**

Step 6. Before running the feeder, you should wait for the following output to appear in the **Console tab** at the bottom of the screen:
    Processor instantiated, waiting for messages feed...
This indicates the Processor is up and running.

{{% anchor JRun Feeder in IDE2 %}}

**Running the Feeder**

![ide-run-hello-feeder-config.jpg](/attachment_files/ide-run-hello-feeder-config.jpg)

Step 7. From the toolbar at the top of the screen, select **Run > Run Dialog...** to open the **Run** dialog again
Step 8. Click the **+** left to **Java Application**, to unfold it
Step 9. Select the **Hello Feeder** launch configuration
Step 10. Click the **Run** button

{{% anchor JView Output %}}

You can use the Management Console to view the Object count and statistics for the different operations:

![ide-gs-ui-stats.jpg](/attachment_files/ide-gs-ui-stats.jpg)

#### Expected output

Running the processor and the feeder results in the following output, which can be viewed in the **Console tab** at the bottom of the screen.
Use the _Display Selected Console_ button ![display_selected_console_button_with_location_small.jpg](/attachment_files/display_selected_console_button_with_location_small.jpg) to switch between the feeder and processor output consoles

**Feeder expected output**


The feeder starts, writes 1000 message objects to the space, reads and prints one of them at random, and finally prints the number of processed messages in the space:

```bash
    Starting the Feeder (Will wait for the space to initialize first...)
    FEEDER WROTE 1000 objects
    Here is one of them printed out: id[47] info[Hello World !!]
    There are 841 processed Message objects in the space now.
    Press any key to continue . . .
```

**Processor expected output**

The processor prints the _id_ and _info_ attributes for each messages it takes for processing:


```bash
    Processor PROCESSING : id[445] info[Hello ]
    Processor PROCESSING : id[904] info[Hello ]
    Processor PROCESSING : id[896] info[Hello ]
    Processor PROCESSING : id[446] info[Hello ]
    Processor PROCESSING : id[889] info[Hello ]
       .
       .
       .
    Processor PROCESSING : id[893] info[Hello ]
    Processor PROCESSING : id[905] info[Hello ]
    Processor PROCESSING : id[897] info[Hello ]
    Processor PROCESSING : id[875] info[Hello ]
    Processor PROCESSING : id[900] info[Hello ]
```




**Steps to run the application inside IntelliJ IDE:**

If you haven't already done so,[download GigaSpaces and set up your development environment]({{%currentjavaurl%}}/installation.html)
- This is needed for running the tutorial sample application.

{{% anchor JImporting Project to the IDE %}}

**Importing the project into IntelliJ**

1. Import the hello world project with 3 modules **common**, **processor** and **feeder** located under the `<XAP Root>/examples/helloworld` folder.

{{%accordion%}}
{{%accord title=" How do I do that..."%}}
{{% panel %}}


**Importing the sample projects into the IDE**

* Select **File** > **New** > **Project from Existing Sources...**.
* Browse to `<XAP Root>/examples/helloworld` folder and choose the file `pom.xml` and click **OK**.



![intellij-ide-hello-world-1.png](/attachment_files/intellij-ide-hello-world-1.png)



* Don't change the default settings of this page and click **Next**.
&nbsp;

* Enable the **IDE** profile and disable the **Default** profile then click **Next**.



![intellij-ide-2.png](/attachment_files/intellij-ide-2.png)


* Click **Next**.
&nbsp;

* Select project SDK and click **Next**.
&nbsp;

* Enter Project name and location then click **Finish**.

{{% /panel %}}
{{%/accord%}}
{{%/accordion%}}

{{% anchor Create Run Configurations in IDE %}} **Create Run Configurations**
&nbsp;

1. Execute the following command from the project root directory `<XAP Root>/examples/helloworld`:

```bash
build.(sh/bat) intellij
```
{{% anchor Create Run Configurations in IDE %}}

{{% anchor Run Processor in IDE %}} **Running the Processor**


1. From the toolbar at the top of the screen, select **Run > Run > Processor**.

{{% anchor JRun Feeder in IDE %}}

**Waiting for the Processor to instantiate**

1. Before running the feeder, you should wait for the following output to appear in the **Run tab** at the bottom of the screen:
    Processor instantiated, waiting for messages feed...
This indicates the Processor is up and running.

{{% anchor JRun Feeder in IDE2 %}}

**Running the Feeder**


1. From the toolbar at the top of the screen, select **Run > Run > Feeder**.

{{% anchor JView Output %}}

You can use the Management Console to view the Object count and statistics for the different operations:

![ide-gs-ui-stats.jpg](/attachment_files/ide-gs-ui-stats.jpg)

#### Expected output

Running the processor and the feeder results in the following output, which can be viewed in the **Run tab** at the bottom of the screen.
Press on Processor or Feeder to switch between the output consoles.

**Feeder expected output**


The feeder starts, writes 1000 message objects to the space, reads and prints one of them at random, and finally prints the number of processed messages in the space:

```bash
    Starting the Feeder (Will wait for the space to initialize first...)
    FEEDER WROTE 1000 objects
    Here is one of them printed out: id[47] info[Hello World !!]
    There are 1000 Message objects in the space now
  .
```

**Processor expected output**

The processor prints the _id_ and _info_ attributes for each messages it takes for processing:


```bash
    Processor PROCESSING : id[445] info[Hello ]
    Processor PROCESSING : id[904] info[Hello ]
    Processor PROCESSING : id[896] info[Hello ]
    Processor PROCESSING : id[446] info[Hello ]
    Processor PROCESSING : id[889] info[Hello ]
       .
       .
       .
    Processor PROCESSING : id[893] info[Hello ]
    Processor PROCESSING : id[905] info[Hello ]
    Processor PROCESSING : id[897] info[Hello ]
    Processor PROCESSING : id[875] info[Hello ]
    Processor PROCESSING : id[900] info[Hello ]
```

