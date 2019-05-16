---
type: post121
title:  Notifications
categories: XAP121
weight: 700
canonical: auto
parent: the-gigaspace-interface-overview.html
---

{{% ssummary %}} {{% /ssummary %}}

Some of the space operations can generate notifications when they are executed. Notifications are also generated when working in clustered mode (schema) that includes a primary/backup schema. A listener can be defined to receive these notifications.

The following space operations may trigger notifications:

- write(), writeMultiple()
- asyncTake() , take() , takeById(), takeByIds() , takeIfExists() ,takeIfExistsById(), takeMultiple() , clear()
- AsyncChange , change()

{{%note%}}
Space operations under transactions will trigger notifications when the transaction commits.
{{%/note%}}

# Notify Example

In the following example we register a listener to receive notifications when an Employee instance is written or update in the space.
The client registering the listener will receive a copy of the object written in the space as notification. The object in the space will continue to exist.


```java
@EventDriven
@Notify
@NotifyType(write = true, update = true)
@TransactionalEvent
public class EmployeeListener {
	@EventTemplate
	Employee unprocessedData() {
		Employee template = new Employee();
		template.setStatus("new");
		return template;
	}

	@SpaceDataEvent
	public Employee eventListener(Empoyee event) {
		// process Employee
		System.out.println("Notifier Received an Employee");
		return null;
	}
}
// Register the listener
SimpleNotifyEventListenerContainer eventListener  = new SimpleNotifyContainerConfigurer(
		space).eventListenerAnnotation(new EmployeeListener())
		.notifyContainer();
eventListener.start();

//.......
eventListener.destroy();

```


{{%refer%}}
[Notify Container](./notify-container.html)
{{%/refer%}}



# Polling Example

This example works just like the notification example above, except that the object is removed from the space:


```java

  @EventDriven
  @Polling
  @NotifyType(write = true, update = true)
  @TransactionalEvent
  public class EmployeeListener {
	    @EventTemplate
	    Employee unprocessedData() {
	    	Employee template = new Employee();
	    	template.setStatus("new");
		    return template;
	    }

	  @SpaceDataEvent
	  public Employee eventListener(Empoyee event) {
		// process Employee
		System.out.println("Notifier Received an Employee");
		return null;
	 }
  }
  // Register the listener
  SimplePollingEventListenerContainer pollingListener = new SimplePollingContainerConfigurer(
			space).template(new Employee())
			.eventListenerAnnotation(new Object() {
				@SpaceDataEvent
				public void eventHappened(Object event) {
					System.out.println("onEvent called Got" + event);
				}
			}).pollingContainer();

  pollingListener.start();

  //.......
  pollingListener.destroy();
```


{{%refer%}}
[Polling Container](./polling-container.html)
{{%/refer%}}



# Primary/Backup

{{%section%}}
{{%column width="60%" %}}
When working in clustered mode (schema) that includes a primary/backup schema, several components within the Processing Unit need to be aware of the current space mode and any changes made to it (such as event containers). Using Spring support for application events, two events are defined within OpenSpaces: `BeforeSpaceModeChangeEvent` and `AfterSpaceModeChangeEvent`. Both are raised when a space changes its mode from primary to backup or versa, and holds the current space mode.
{{%/column%}}
{{%column width="35%" %}}
![OS_PrimaryBackupCluster.jpg](/attachment_files/OS_PrimaryBackupCluster.jpg)
{{%/column%}}
{{%/section%}}

Custom beans that need to be aware of the space mode (for example, working directly against a cluster member, i.e. not using a clustered proxy of the space, and performing operations against the space only when it is in primary mode) can implement the Spring `ApplicationListener` and check for the mentioned events.

{{% info %}}
OpenSpaces also provides the [Space Mode Context Loader](./space-mode-context-loader.html), which can load the Spring application context when it has become primary, and unload it when it moves to backup.
{{%/info%}}

In embedded mode, the space factory bean registers with the space for space mode changes. The registration is performed on the actual space instance (and not a clustered proxy of it), and any events raised are translated to the equivalent OpenSpaces space mode change events. In remote mode, a single primary event is raised.

Space mode registration can be overridden and explicitly set within the space factory configuration. Here is an example of how it can be set (it cannot register for notifications even though it is an embedded space):

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:embedded-space id="space" space-name="space" register-for-space-mode-notifications="false" />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="space" class="org.openspaces.core.space.EmbeddedSpaceFactoryBean">
    <property name="name" value="space" />
    <property name="registerForSpaceModeNotifications" value="false" />
</bean>
```

{{% /tab %}}
{{%tab "  Code "%}}


```java

EmbeddedSpaceConfigurer spaceConfigurer =
              new EmbeddedSpaceConfigurer("space").registerForSpaceModeNotifications(false);
IJSpace space = spaceConfigurer.space();

// ...

// shutting down / closing the Space
spaceConfigurer.destroy();
```

{{% /tab %}}
{{% /tabs %}}

### Primary Backup Notifications

A bean can implement the following interfaces to get notified about space mode changes:


| Interface | Implemented Method | When Invoked |
|:----------|:-------------------|:-------------|
| _SpaceBeforeBackupListener_ | void onBeforeBackup(BeforeSpaceModeChangeEvent event) | Before a space becomes backup |
| _SpaceBeforePrimaryListener_ | void onBeforePrimary(BeforeSpaceModeChangeEvent event) | Before a space becomes primary|
| _SpaceAfterBackupListener_ | void onAfterBackup(AfterSpaceModeChangeEvent event) | After a space becomes backup |
| _SpaceAfterPrimaryListener_ | void onAfterPrimary(AfterSpaceModeChangeEvent event) | After a space becomes primary |




```java
class MyBean implements SpaceBeforeBackupListener, SpaceAfterPrimaryListener {

    // invoked before a space becomes backup
    public void onBeforeBackup(BeforeSpaceModeChangeEvent event) {
        // Do something
    }

    // invoked after a space becomes primary
    public void onAfterPrimary(AfterSpaceModeChangeEvent event) {
        // Do something
    }

}
```




If the bean does not implement any of the interfaces above, another option is to annotate the bean's methods that need to be invoked when a space mode changes.


| Annotation | Method Parameter | When Invoked |
|:-----------|:-----------------|:-------------|
| @PreBackup | _none_ or _BeforeSpaceModeChangeEvent_ | Before a space becomes backup |
| @PrePrimary | _none_ or _BeforeSpaceModeChangeEvent_ | Before a space becomes primary |
| @PostBackup | _none_ or _AfterSpaceModeChangeEvent_ | After a space becomes backup |
| @PostPrimary | _none_ or _AfterSpaceModeChangeEvent_ | After a space becomes primary |


```java
class MyBean {

    // invoked before a space becomes backup; gets the BeforeSpaceModeChangeEvent as a parameter
    @PreBackup
    public void myBeforeBackupMethod(BeforeSpaceModeChangeEvent event) {
        // Do something
    }

    // invoked after a space becomes primary; doesn't get any parameter.
    @PostPrimary
    public void myAfterPrimaryMethod() {
        // Do something
    }

}
```

In order to enable this feature, the following should be placed within the application context configuration:

{{%tabs%}}
{{%tab "  Namespace "%}}


```xml

<os-core:annotation-support />
```

{{% /tab %}}
{{%tab "  Plain XML "%}}


```xml

<bean id="coreAnntoationSupport" class="org.openspaces.core.config.AnnotationSupportBeanDefinitionParser" />
```

{{% /tab %}}
{{% /tabs %}}


When there is more than one Proxy (e.g: embedded, remote, ...), the following should be done in order to be sure that the Primary Backup Notifications arrived from the current Space instance:


```java
class MyBean {

    @Resource(name="gigaSpace")
    private GigaSpace gigaSpace;

    @Resource(name="gigaSpace2")
    private GigaSpace gigaSpace2;

	boolean isPrimary;

	@PostPrimary
	public void afterChangeModeToPrimary(AfterSpaceModeChangeEvent event) {
		if (SpaceUtils.isSameSpace(gigaSpace.getSpace(), event.getSpace()))
			isPrimary = true;
	}

	@PostBackup
	public void afterChangeModeToBackup(AfterSpaceModeChangeEvent event) {
		if (SpaceUtils.isSameSpace(gigaSpace.getSpace(), event.getSpace()))
			isPrimary = false;
	}

}
```


The method compareAndSet() allows you to compare the current value of the AtomicBoolean to an expected value.
If the current value is equal to the expected value, a new value can be set on the AtomicBoolean.
The compareAndSet() method is atomic, so only a single thread can execute it at the same time.
Thus, the compareAndSet() method can be used to implement a simple synchronization like lock.


```java
class MyBean {

    @Resource(name="gigaSpace")
    private GigaSpace gigaSpace;

    @Resource(name="gigaSpace2")
    private GigaSpace gigaSpace2;

	static AtomicBoolean isPostPrimaryCalled = new AtomicBoolean(false);

	@PostPrimary
	public void afterChangeModeToPrimary(AfterSpaceModeChangeEvent event) {
		if (isPostPrimaryCalled.compareAndSet(false, true)
        {
            initialize();
        }
	}

	@PostBackup
	public void afterChangeModeToBackup(AfterSpaceModeChangeEvent event) {
		if (SpaceUtils.isSameSpace(gigaSpace.getSpace(), event.getSpace()))
			isPrimary = false;
	}

}
```



# Listening for Space Mode Changed Events

When a remote client is interested to receive events when a space instance changing its runtime mode (from primary to backup or vise versa), it should implement the `SpaceModeChangedEventListener`. See below how:

Registering for the event using the [Administration API](./administration-and-monitoring-api.html):


```java
Admin admin = new AdminFactory().createAdmin();
Space space = admin.getSpaces().waitFor(spaceName, 10, TimeUnit.SECONDS);
SpaceModeChangedEventManager modeManager =  space.getSpaceModeChanged();
MySpaceModeListener spaceModeListener = new MySpaceModeListener (space);
modeManager.add(spaceModeListener);
```

The `MySpaceModeListener` should implement the `SpaceModeChangedEventListener` - see below simple example:


```java
public class MySpaceModeListener implements SpaceModeChangedEventListener{

	Space space ;
	public MySpaceModeListener (Space space)
	{
		this.space=space;
	}

	public void spaceModeChanged(SpaceModeChangedEvent event) {
		String partition_member = event.getSpaceInstance().getInstanceId()+"";
		if (event.getSpaceInstance().getBackupId() != 0)
		{
			partition_member = partition_member+ "_" + event.getSpaceInstance().getBackupId();
		}
	System.out.println("SpaceModeChangedEvent:  Space " + space.getName() +" - Instance " + partition_member +
			" moved into " + event.getNewMode());
	}
}
```


