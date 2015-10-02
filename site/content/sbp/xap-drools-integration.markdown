---
type: postsbp
title:  Drools Rule Engine Integration
categories: SBP
parent: processing.html
weight: 100
---


{{% ssummary page %}}This article illustrates how to integrate the Drools Rule Engine with GigaSpaces XAP {{% /ssummary %}}


{{% info %}}
{{%section%}}
{{%column width="25%" %}}
{{%align center%}}
**Author**<br>
Allen Terleto
Senior Sales Engineer
GigaSpaces
{{%/align%}}
{{%/column%}}
{{%column   width="25%" %}}
{{%align center%}}
**XAP Version** {{<wbr>}}
XAP 10.0.1
{{%/align%}}
{{%/column%}}
{{%column  width="25%" %}}
**Last updated**  <br>
September 2014
{{%/column%}}
{{%column  width="25%" %}}
{{%align center%}}
**Download example** <br>
{{%git "https://github.com/Gigaspaces/xap-drools-integration" %}}
{{%/align%}}
{{%/column%}}
{{%column  width="20%" %}}
{{%align center%}}

{{%/align%}}
{{%/column%}}
{{%/section%}}
{{% /info %}}



# Overview

Drools is a business rule management system (BRMS) with a forward and backward chaining inference-based rules engine using an enhanced implementation of the [Rete algorithm](http://en.wikipedia.org/wiki/Rete_algorithm). Reasons to use Drools include:

- Declarative Programming  <br>
- Logic and Data Separation <br>
- Speed and Scalability   <br>
- Centralization of Knowledge <br>
- Understandable and Transparent Rules <br>
- Dynamic Rule Deployment   <br>

The Drools Rule Engine matches Facts (POJO) against Rules to infer conclusions which result in actions. The process of matching the new or existing facts against Rules is called pattern matching, which is performed by the inference engine.

Drools stores its rules in Production Memory and facts in Working Memory. The Agenda manages the execution order of these rules using a Conflict Resolution strategy.

![drools1](/sbp/attachment_files/drools/drools1.png)

Drools has a "native" rule language. A rule file typically has a .drl extension. In a DRL file you can have multiple rules, queries and functions, as well as some resource declarations like imports, globals and attributes.
A package is a collection of rules and other related constructs, such as imports and globals. The package members are typically related to each other - perhaps HR rules, for instance.

A package represents a namespace, which ideally is kept unique for a given grouping of rules.


The KnowledgeBase is a repository of all the application's knowledge definitions. It will contain rules, processes, functions, and type models. The Knowledge Base itself does not contain data; instead, sessions are created from the `KnowledgeBase` into which data can be inserted and from which process instances may be started. Creating the KnowledgeBase can be heavy, whereas session creation is very light, so it is recommended that KnowledgeBases be cached where possible to allow for repeated session creation.

{{%refer%}}
Detailed documentation on Drools 5.6.0.Final can be found [here](http://docs.jboss.org/drools/release/5.6.0.Final/drools-expert-docs/html_single).
{{%/refer%}}

# Drools Integration with XAP

As stated earlier, Drools stores all rules and data types in-memory to reduce the overhead of compiling them every time before execution. This benefits enterprise applications with low-latency performance but establishes architectural gaps such as :   <br>

- Distribution <br>
- Failover    <br>
- Redundancy   <br>
- Consistency  <br>
- Scalability  <br>

Let's discuss these gaps within the context of a stateless application hosted in clustered environment and explain how an integration with XAP would benefit its use of Drools.

![drools1](/sbp/attachment_files/drools/drools2.png)

### Distribution

Drools was not created as a distributed solution. In order to maintain seamless load-balancing in a clustered environment, each instance of an enterprise application would require identical data in its `KnowledgeBase(s)`. Since XAP is inherently a distributed environment it allows the clustered enterprise applications to distribute their rules across multiple [Data Grid partitions (Space)]({{%latestadmurl%}}/data-partitioning.html). In addition, the enterprise applications would use a common reference proxy to the Space allowing them to avoid the burden on managing data locality.

### Failover

Without XAP the data in the `KnowledgeBase` would be lost, in the case of node failure within the cluster, and could not be recovered by the standard failover mechanism. XAP provides data-failover out of the box via it's [asynchronous replication]({{%latestadmurl%}}/asynchronous-replication.html) to backup cluster topology. In the case of a XAP primary partition failure, the backup partition would become the primary avoiding data loss and maintaining data integrity throughout the entire process.

### Redundancy

Storing the KnowledgeBase's data in each clustered JVM memory is redundant and costly in terms of memory footprint. Integration with XAP removes the enterprise application's need to locally store data since the `KnowledgeBase` would reside in a partition hosted on the Data Grid.

### Consistency

Similar to the concerns of load-balancing, enterprise applications would require all clustered JVMs to not only store identical data but also ensure consistency upon any change. Instead of building a custom solution to update the entire cluster, XAP could be used to ensure data consistency. By utilizing XAP's built-in [Routing Mechanism]({{%currentjavaurl%}}/routing-in-partitioned-spaces.html), all instances of the cluster can seamlessly be routed to the unique partition that is storing the relevant KnowledgeBase(s) and always receive a consistent result.

### Scalability

An enterprise application hosting multiple instances of KnowledgeBases would be limited by the memory constraints of its underlying JVM. Turning to a 64-bit JVM could extend memory resources but its benefits will eventually be offset by incurring an increased garbage collection penalty. With XAP, KnowledgeBases could be distributed across separate partitions each hosted on top of their own individual 64-bit JVM allowing Drool's production/working memory to scale into the Terabytes.


# Integration Pattern

To demonstrate XAP's integration with Drools we designed a pattern in the form of a Maven project. The project is distributed into 4 Maven-Modules each with an individual purpose: <br>


| Module | Purpose |
|:--------|:--------|
|common |Sharable library including data model and utilities |
|client |Load/analyze/execute/remove rules and facts    |
|space |Defines space, event containers and remote services    |
|web-service | Expose JSON Restful Web Services to execute rules|

The combination of these modules will provide you with a working project to deploy a Data Grid, load/compile/remove rules from DRL files, execute rules from a one-off process and/or expose them as JSON Restful Web Service.

Let's review each module to understand their code and patterns.

## Common Module

The main purpose of this module is to define the data-model for the entire Data Grid. The data-model includes a [POJO]({{%currentjavaurl%}}/pojo-overview.html) representation of Facts, Drools Rules, Knowledge Packages, KnowledgeBases, etc.

Facts are representations of business entities which will be referenced in either or both the condition and consequence of rules. In order for client applications to dynamically create rules they must first define Facts which will be loaded into a KnowledgeBase's Working Memory before Rules execution can occur. Therefore the Fact POJO must be created and deployed with the Space module before new dynamic rules can be compiled and executed using them as a reference.

The integration between Drools and XAP is represented as the [Space Class]({{%currentjavaurl%}}/pojo-support.html) called KnowledgeBaseWrapper. This object will store a KnowledgeBase in the Data Grid along with all of its compiled content (i.e. rules, definitions, globals, fact types, etc.). Therefore it is a wrapper of the Drools Production/Working Memory which is enhanced with the routing capabilities of a Space class along with additional attributes compensating for the KnowledgeBase's lack of transparency.

The remaining POJOs are meant to be informative tools for management and governance purposes. Their role is to provide the user with metadata type information about the contents of KnowledgeBase without having to execute operations against the actual objects.

## Client Module

This module implements classes encapsulating stand-alone functional processes to load, analyze, execute and remove Rules and Facts. These processes can be simply instantiated by running their main methods and will interact with the space via the Space Proxy. If you are using an Eclipse IDE you can just click on the class and choose Run As Configurations to start the process.

![drools1](/sbp/attachment_files/drools/drools3.png)

{{%note%}}
Some main methods allow for you to pass arguments
{{%/note%}}

Here is a list of the functional processes by category:<br>


|Category| Function|
|:--------|:--------|
|Analyze  | PrintOutAllKnowledgePackages <br>PrintOutSingleKnowledgePackage |
|Loader   |DroolsRuleLoader   <br>FactLoader   |
|Manage Rules |ExecuteRule <br>RemoveRule  |


### Analyze
By running these processes you can get a print out to the System.Out log of the KnowledgePackages and their corresponding Rules within the chosen KnowledgeBase.

{{%refer%}}
You can also get this information by inspecting the KnowledgePackage Space Class via the [XAP Management Center]({{%currentadmurl%}}/gigaspaces-management-center.html), which maintains metadata about each available KnowledgePackage across all loaded KnowledgeBase(s).
{{%/refer%}}

### Loader
These processes will load both the Rules and Facts into the KnowledgeBase(s) stored on one or more instances of Stateful Processing Units representing the Space.

Rules meant to reside within the same KnowledgeBase are gathered within their uniquely named `RuleSet.xml` located inside the `resources/META-INF/rules` folder. Each XML file will represent a separate `KnowledgeBase` which will be uniquely routed to its respective partition. The individual rules are encapsulated within DRL files. To add rules simply add <resource> elements to the <add> element before running the load process. To remove rules just change the <add> element to <remove> and rerun the same process. Here is an example of a `RuleSet.xml` file.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<change-set xmlns='http://drools.org/drools-5.0/change-set'
            xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'
            xs:schemaLocation='http://drools.org/drools-5.0 change-set.xsd'>
    <add>
        <resource source='classpath:META-INF/rules/drl/ValidateAgeRule.drl' type='DRL'/>
        <resource source='classpath:META-INF/rules/drl/ValidateApplicationRule.drl' type='DRL'/>
    </add>

</change-set>
```



The Drools syntax is encapsulated within a DRL file including all reserved keywords such as package, import, rule, etc. This implementation assumes that only a single rule will reside within each DRL file. As mentioned earlier, the custom facts which were defined in the common data-model will be imported into the rules for use in both the condition (when) and consequence (then).


```xml
package Application

import com.gigaspaces.droolsintegration.model.facts.*;

rule "ValidAgeRule"
when
	$applicant : Applicant( age > 18 )
	$application : Application( processed == false )
then
	System.out.println("Applicant is of valid age: " + $applicant.getAge());
	$application.setApplicantId($applicant.getId());
	$application.setApplicantName($applicant.getName());
	$application.setApplicantApproved(true);
	update($application);
end
```

Aside from loading rules this module can also load facts as Space Classes into the Space. These facts can be later queried as part of a decision web-service transaction and loaded into the KnowledgeBase's working memory before rule(s) execution.


### Manage Rules

If you wish to quickly pattern-match a set of facts against a loaded KnowledgeBase without having to create or alter a Restful Web Service you can run the ExecuteRules class as an Application. To implement your own Rules Execution process you simply have to account for the input and output parameters which will be passed to the Remote Service, which is collocated with the Space.

Since Drools only acts upon Iterable collections our integration pattern includes a utility class called IterableMapWrapper. This class simply wraps a Map and returns an iterator to its values in the iterator method. Use this map to pass all of the input facts which will be used for pattern matching before rules execution.

By default Drools returns all the input facts that have not been deleted from working memory during rules-execution along with any new facts that are added to the collection. This could be an unnecessary cost by, increasing memory footprint over the network, for client applications that do not require the entire collection of facts as a returned parameter. So to avoid the overhead we allow the client to pass a list, as an input, which will filter the result collection on the Space side and pass back only the wanted facts back over the network.


```java
   public void execute(Integer id) {
        log.info("Starting ExecuteRules Execution");

        Applicant applicant = gigaSpace.readById(Applicant.class, id);

		Application application = new Application();
		application.setProcessed(false);

        IterableMapWrapper facts = new IterableMapWrapper();
		facts.put(Applicant.class.getSimpleName(), applicant);
        facts.put(Application.class.getSimpleName(), application);

        List<String> resultKeyList = new ArrayList<String>();
        resultKeyList.add(Application.class.getSimpleName());

        try {
        	IterableMapWrapper resultFacts = (IterableMapWrapper) rulesExecutionService.executeRulesWithLimitedResults(RulesConstants.RULE_SET_APPLICANT_AGE, facts, resultKeyList);

        	Application result = (Application) resultFacts.get(Application.class.getSimpleName());
        	if(!result.getProcessed()) {
        		result.setApplicantApproved(false);
        		result.setApplicantId(applicant.getId());
        		result.setApplicantName(applicant.getName());
        	}

        	log.info("Date Approved: " + result.getDateApproved());
        	log.info("Applicant Id: " + result.getApplicantId());
        	log.info("Applicant Name: " + result.getApplicantName());
        	log.info("Application Processed: " + result.getProcessed());

        }catch(Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("End ExecuteRules Execution");
    }
```

The client module also allows the removal of an individual rule as an alternative to changing the element inside `RuleSet.xml` from <add> to <remove>. To remove a specific rule using this utility you need only to change the arguments when running the RemoveRule class as an application.



## Space Module

This module will create a deployable artifact (JAR) which will instantiate Stateful processing unit(s) representing the Data Grid (Space). By default the Space will use the SLA.xml file to deploy the predetermined amount of primary and backup partitions with the clustering topology set to `partitioned-sync2backup`.


```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:os-sla="http://www.openspaces.org/schema/sla"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
       					   http://www.openspaces.org/schema/sla http://www.openspaces.org/schema/10.0/sla/openspaces-sla.xsd">

    <os-sla:sla cluster-schema="partitioned-sync2backup" number-of-instances="2" number-of-backups="1"
                max-instances-per-vm="1">
    </os-sla:sla>

</beans>
```

In addition to hosting data this processing unit will take advantage of XAP's collocation of business logic in the form of Event Processing and Space Based Remoting.

The event processor of choice will be the Polling Container which will be listening for specific events to occur on the Space. As part of our integration pattern we have chosen to decouple the adding/removing of Rule Events to the Space from the compilation and injection of those rules to their respective KnowledgeBase. Since loading a single instance of a KnowledgeBase creates a bottleneck for a queue of 100+ rules it would make sense to allow the client (remote client proxy) to fire and forget while the server (space) does its work at its own pace. Upon notification of an event, the collocated event handler will look up the KnowledgeBase from the Data Grid and add the new rule to the KnowledgeBase assuming compilation was successful. It will also update metadata type Space Classes to keep track of the KnowledgeBase contents for transparency.


```java
@SpaceDataEvent
public DroolsRuleAddEvent addRule(DroolsRuleAddEvent droolsRuleAddEvent) {
	String ruleSet = droolsRuleAddEvent.getRuleSet();
	String knowledgePackageName = droolsRuleAddEvent.getPackageName();
	String ruleName = droolsRuleAddEvent.getRuleName();

	try {
		KnowledgeBaseWrapper knowledgeBaseWrapper = knowledgeBaseWrapperDao.read(ruleSet);
            	if(knowledgeBaseWrapper == null) {
            		knowledgeBaseWrapper = createKnowledgeBaseWrapper(ruleSet);
            	}

            	KnowledgePackage knowledgePackage = knowledgePackageDao.read(ruleSet, knowledgePackageName);
            	if(knowledgePackage == null) {
            		knowledgePackage = createKnowledgePackage(knowledgeBaseWrapper, knowledgePackageName, ruleSet);
            	}

            	if(!knowledgePackage.getRules().containsKey(ruleName)) {
            		DroolsRule droolsRule = createDroolsRule(droolsRuleAddEvent);
            		knowledgePackageDao.addRule(knowledgePackage, ruleName, droolsRule);

            		addKnowledgePackages(knowledgeBaseWrapper, droolsRuleAddEvent, ruleName);
            		knowledgeBaseWrapperDao.write(knowledgeBaseWrapper);

                	log.info(String.format("Rule '%s' compiled successfully", ruleName));
            	}else {
                	log.info(String.format("Rule '%s' already exists in knowledgePackage '%s'", ruleName, knowledgePackageName));
            	}
        }catch(Exception e) {
            log.info(String.format("Rule '%s' failed compilation for ruleset", ruleName));
            log.error(e.getMessage(), e);
        }

	droolsRuleAddEvent.setProcessed(Boolean.TRUE);
        return null;
 }
```

[Space Based Remoting]({{%currentjavaurl%}}/space-based-remoting-overview.html) is simply a combination of an interface located in the common module and an implementation of the interface which is deployed collocated with the space. The interface is shared by both the space and the client(s) via the common module. To expose the interface as a Remoting Service a simple annotation was added to the implementation class as well as a annotation scan within the [pu.xml]({{%latestjavaurl%}}/configuring-processing-unit-elements.html).

{{%tabs%}}
{{%tab " Configuration"%}}

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns="http://www.springframework.org/schema/beans"
	   xmlns:context="http://www.springframework.org/schema/context"
       xmlns:os-core="http://www.openspaces.org/schema/core"
       xmlns:os-events="http://www.openspaces.org/schema/events"
       xmlns:os-remoting="http://www.openspaces.org/schema/remoting"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
       					   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       					   http://www.openspaces.org/schema/core http://www.openspaces.org/schema/10.0/core/openspaces-core.xsd
       					   http://www.openspaces.org/schema/events http://www.openspaces.org/schema/10.0/events/openspaces-events.xsd
       					   http://www.openspaces.org/schema/remoting http://www.openspaces.org/schema/10.0/remoting/openspaces-remoting.xsd">


	<os-core:space id="space" url="/./space" />

    <!-- Defines a distributed transaction manager -->
     <os-core:distributed-tx-manager id="transactionManager"/>

    <!-- OpenSpaces simplified space API built on top of IJSpace/JavaSpace -->
    <os-core:giga-space id="gigaSpace" space="space" tx-manager="transactionManager"/>

	<!-- Enables the usage of @GigaSpaceContext annotation based injection -->
    <os-core:giga-space-context/>

    <!-- Enable scan for OpenSpaces and Spring components -->
    <context:component-scan base-package="com.mycompany.app"/>

    <!-- Enables the usage of @GigaSpaceContext annotation based injection. -->
    <os-core:giga-space-context/>

    <!--  Enables Spring Annotation configuration  -->
    <context:annotation-config/>

    <!-- Enables using @Polling and @Notify annotations -->
    <os-events:annotation-support/>

    <!-- Enables using @RemotingService as well as @ExecutorProxy (and others) annotations -->
    <os-remoting:annotation-support/>

    <!-- Enables using @PreBackup, @PostBackup and other annotations -->
    <os-core:annotation-support/>

    <!--Add service exporter for remoting services-->
    <os-remoting:service-exporter id="serviceExporter"/>

</beans>
```
{{%/tab%}}

{{%tab "Java"%}}

```java
@RemotingService
public class RulesExecutionServiceImpl implements IRulesExecutionService {

    @Autowired
    private KnowledgeBaseWrapperDao knowledgeBaseWrapperDao;

    @Transactional
    public Iterable<Object> executeRules(String ruleSet, Iterable<Object> facts,  Map<String, Object> globals) {

    	KnowledgeBaseWrapper knowledgeBaseWrapper = knowledgeBaseWrapperDao.readByRuleSet(ruleSet);
    	if(knowledgeBaseWrapper != null) {
    		StatelessKnowledgeSession session = knowledgeBaseWrapper.getKnowledgeBase().newStatelessKnowledgeSession();

	        //Session scoped globals
	        if(globals != null) {
	            for(String key : globals.keySet()) {
	                session.setGlobal(key, globals.get(key));
	            }
	        }

	        session.execute(facts);
    	}else {
    		return null;
    	}

        return facts;
    }
}
```
{{%/tab%}}
{{%/tabs%}}

As with any execution against a partitioned space a routing key is required for the [Hash-Based Routing]({{%currentadmurl%}}/data-partitioning.html) mechanism to determine which partition hosts the client's requested data. The routing key can also simply be set by adding an annotation but this time to one of the parameters inside the method signature.


```java
public interface IRulesExecutionService {

	Iterable<Object> executeRules(@Routing String resultSet, Iterable<Object> facts,  Map<String, Object> globals);

    Iterable<Object> executeRules(@Routing String resultSet, Iterable<Object> facts);

    Iterable<Object> executeRulesWithLimitedResults(@Routing String resultSet, IterableMapWrapper facts,  List<String> resultKeys, Map<String, Object> globals);

    Iterable<Object> executeRulesWithLimitedResults(@Routing String resultSet, IterableMapWrapper facts, List<String> resultKeys);

}
```


### Web-Services Module

This module will create a deployable artifact (WAR) which will instantiate Stateless processing unit(s) hosting JSON Restful Web Services on top XAP's [Jetty Processing Unit Container]({{%currentjavaurl%}}/web-jetty-processing-unit-container.html).

![drools1](/sbp/attachment_files/drools/drools4.png)

Interaction with these services is very simple since they only implement the GET REST method. A client only needs an Internet Browser to initiate a HTTP Servlet transaction on the listening Spring Controllers. There are two types of services that are deployed as part of this integration pattern - Decision Services and Generic Data Retrieval Services.

Decision services will create or lookup facts from the Space and pass them to the Remoting Service which will add them to the KnowledgeBase's working memory and pattern match them against all compiled rules. To initiate a decision service enter a URL into the Browser and in response you will receive a JSON payload printed to the screen.

![drools1](/sbp/attachment_files/drools/drools5.png)

Generic REST Data Retrieval Services come in two flavors - Read-By-ID and Read-By-Type. Read-By-ID will require the client to pass an ID along with the type of Object they are requesting. The Read-By-Type will only require the type returning multiple objects.

![drools1](/sbp/attachment_files/drools/drools6.png)

![drools1](/sbp/attachment_files/drools/drools7.png)

# Running the Demo

{{%accordion%}}

{{%accord  title="Step 1: Java Installation "%}}
Confirm Java is installed by running the java -version in your command line

![drools1](/sbp/attachment_files/drools/drools8.png)

If Java is not installed, download Java {{%download "https://java.com/en/download/index.jsp"%}} and add JAVA_HOME to your system variables
![drools1](/sbp/attachment_files/drools/drools9.png)


Add %JAVA_HOME%/bin to your PATH system variable

![drools1](/sbp/attachment_files/drools/drools10.png)
{{%/accord%}}

{{%accord title="Step 2: Maven Installation"%}}

Confirm Maven is installed by running the mvn version in your command line

![drools1](/sbp/attachment_files/drools/drools11.png)

If Maven is not installed, download Maven {{%download "http://maven.apache.org/download.cgi"%}} and add M2_HOME to your system variables

![drools1](/sbp/attachment_files/drools/drools12.png)

Add %M2_HOME%/bin to your PATH system variable

![drools1](/sbp/attachment_files/drools/drools13.png)

{{%/accord%}}

{{%accord  title="Step 3: XAP Installation"%}}
If XAP is not installed {{%download "http://www.gigaspaces.com/xap-download"%}} and add JSHOMEDIR to your system variables

![drools1](/sbp/attachment_files/drools/drools14.png)


Add %JSHOMEDIR%/bin to your PATH system variable

![drools1](/sbp/attachment_files/drools/drools15.png)
{{%/accord%}}

{{%accord title="Step 4: Download Example"%}}

Download the example {{%git "https://github.com/Gigaspaces/xap-drools-integration" %}} and extract.

![drools1](/sbp/attachment_files/drools/drools16.png)
{{%/accord%}}


{{%accord title="Step 5: Execute maven command"%}}

Navigate to the project root and execute the mvn package command

![drools1](/sbp/attachment_files/drools/drools17.png)
{{%/accord%}}


{{%accord title="Step 6: Start the XAP agent"%}}

Navigate to the XAP bin directory and double-click gs-agent.bat

![drools1](/sbp/attachment_files/drools/drools18.png)
{{%/accord%}}


{{%accord title="Step 7: Start Web management Console"%}}
Start and login to the Web Management Console by double-clicking gs-webui.bat
and then entering http://localhost:8099 into an Internet Browser (Firefox or Google Chrome is recommended)

![drools1](/sbp/attachment_files/drools/drools19.png)

![drools1](/sbp/attachment_files/drools/drools20.png)

![drools1](/sbp/attachment_files/drools/drools21.png)
{{%/accord%}}

{{%accord title="Step 8: Deploy the PU"%}}
Click on the Hosts Tab and find the Deploy drop-down. Choose Processing Unit¦

![drools1](/sbp/attachment_files/drools/drools22.png)
{{%/accord%}}

{{%accord title="Step 9: Upload file"%}}
Click on Upload File and navigate to the space/target directory inside the Distributed Drools project. Double-click the space.jar and click Deploy

![drools1](/sbp/attachment_files/drools/drools23.png)

![drools1](/sbp/attachment_files/drools/drools24.png)
{{%/accord%}}


{{%accord title="Step 10: View Data Grid"%}}
Confirm that the `Space` was created by clicking on the `Data Grid` tab.

![drools1](/sbp/attachment_files/drools/drools25.png)

{{%/accord%}}

{{%accord title="Step 11: Open Eclipse"%}}
Open the workspace in an Eclipse IDE

![drools1](/sbp/attachment_files/drools/drools26.png)
{{%/accord%}}

{{%accord title="Step 12: Import the project"%}}
Import the existing Maven Project named `xap-drools-integration`

![drools1](/sbp/attachment_files/drools/drools27.png)
{{%/accord%}}

{{%accord title="Step 13: Loading the rules"%}}
Run DroolsRuleLoader.java and FactLoader.java as a Java Application

![drools1](/sbp/attachment_files/drools/drools28.png)
{{%/accord%}}

{{%accord title="Step 14: Confirm rules are loaded"%}}
Confirm that the KnowledgeBasesWrappers and Applicant Facts were loaded into the Space

![drools1](/sbp/attachment_files/drools/drools29.png)
{{%/accord%}}

{{%accord title="Step 15: Deploy web service"%}}
Click back onto the Hosts Tab and deploy the web-services.war

![drools1](/sbp/attachment_files/drools/drools30.png)
{{%/accord%}}

{{%accord title="Step 16: Confirm the Web Services URL and PORT"%}}
Click on the Applications Tab and confirm the Web Services URL and PORT

![drools1](/sbp/attachment_files/drools/drools31.png)
{{%/accord%}}

{{%accord title="Step 17: Test the rules"%}}
Open an Internet Browser and begin testing the Decision and Generic Data Lookup Services

http://localhost:8080/web-services/mycompany/rest/com.mycompany.app.model.facts.Applicant/1
http://localhost:8080/web-services/mycompany/rest/com.mycompany.app.model.facts.Applicant
http://localhost:8080/web-services/mycompany/rest/decision/processApplicationService/1
http://localhost:8080/web-services/mycompany/rest/decision/checkHolidayService/summer/july
{{%/accord%}}

{{%/accordion%}}
