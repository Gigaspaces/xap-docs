---
type: post102
title:  Reloading Business Logic
categories: XAP102
parent: the-processing-unit-overview.html
weight: 350
---


{{%ssummary%}}{{%/ssummary%}}

The service reloading feature allows you to reload business logic (Spring beans) without shutting down the application or undeploying a Processing Unit. In order to do this, any reloadable business logic needs to be defined in a special Spring XML file. The Spring XML file is then referenced very similar to the [Space Mode Context Loader](./space-mode-context-loader.html) from inside the pu.xml.

{{% info %}}
Service Reloading only works when downloading the processing unit to the GSC is disabled (pu.download deploy property should be set to false). For more information on how to do it, see [this page](./deploying-onto-the-service-grid.html#distributionToGSCs).
For service reloading to work, common classes have to be copied to the <GigaSpacesRoot>/lib/platform/ext folder
{{%/info%}}

# Configuring Reloadable Business Logic

Lets assume we have business logic that we would like to reload at some point in time after our Processing Unit has been deployed. Here is an example of such business logic:


```java
public class RefreshableBean implements InitializingBean, DisposableBean {

    @GigaSpaceContext(name = "gigaSpace")
    private GigaSpace gigaSpace;

    public void afterPropertiesSet() throws Exception {
        System.out.println("BEAN LOADED, SPACE [" + gigaSpace + "]");
    }

    public void destroy() throws Exception {
        System.out.println("BEAN DESTROYED, SPACE [" + gigaSpace + "]");
    }
}
```

We then need to define it in a specific Spring XML file (lets assume it is named `refreshable-beans.xml`):


```xml
<beans ... >

    <os-core:giga-space-context />

    <bean id="refreshableBean" class="org.openspaces.example.data.processor.RefreshableBean"/>
</beans>
```

{{% tip %}}
This Spring XML file is a fully functional Spring definition and can hold several bean definitions, as well as other OpenSpaces components.
{{%/tip%}}

To enable service reloading, in our processing unit `pu.xml` file, we reference the `refreshable-beans.xml` file in the following manner:


```xml
<beans ...>
	...

	<os-core:refreshable-context-loader id="refreshableExample" location="classpath:/META-INF/spring/refreshable-beans.xml"/>
</beans>
```

{{% tip %}}
`refreshable-beans.xml` has its parent application context set to the `pu.xml`, allowing it to access any bean defined in its parent `pu.xml`. Also, the `refreshable-context-loader` only starts if the space is in primary mode (when working with a remote space, it is always in primary mode).
{{%/tip%}}

Above configuration will let you refresh the code defined in the refreshable context. Actual execution of this reloading of context can be done using OpenSpaces sync remoting, which allows you to broadcast the reload operation to all active cluster members. Here is how this is configured:


```xml
<beans ...>

        ...

	<os-remoting:service-exporter id="remotingServiceExporter">
	    <os-remoting:service ref="refreshableExample"/>
	</os-remoting:service-exporter>

</beans>
```

# Reloading Business Logic

When there is a change in business logic for any beans defined in the refreshable context and these changes are ready to be applied to the cluster, copy the new classes into the GigaSpaces deploy folder of GSM machine(s) and work folder of each cluster node (default location, <GigaSpacesRoot>/work/processing-units/<puInstance>) and execute the reload command as below:


```java
java -cp [list of jar files, including JSpace, openspaces and spring] RefreshContextLoaderExecutor jini://*/*/space
```

As a result of the above command, all the beans defined in the refreshable context are destroyed and fresh copies of these beans are re-created using the new class definitions in the work folder. Your new version of business logic is now ready without brining down the cluster.

# Other Considerations

- Refreshable context works only for business logic changes. You cannot modify the data model using this approach.
- Any beans defined in the refreshable context should be designed to properly exit without leaving any threads in unreachable state. Implementing Spring InitializingBean and DisposableBean interface (or equivalent annotation) is a good design practice and any initializing code and clean up should be invoked from the afterPropertiesSet and destroy methods.
