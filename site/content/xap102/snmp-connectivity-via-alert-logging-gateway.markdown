---
type: post102
title:  SNMP Alerts
categories: XAP102
parent: administration-and-monitoring-overview.html
weight: 300
---

{{% section %}}
 {{% column width="10%" %}}
 ![counter-logo.jpg](/attachment_files/subject/alerts.png)
 {{% /column %}}
 {{% column width="90%" %}}
 {{% ssummary %}} {{% /ssummary %}}
 {{% /column %}}
 {{% /section %}}



The XAP `Alert` interface exposes the XAP environment and the application's health state. It allows users to register listeners on one or more alert types and receive notifications once an alert has been raised or has been resolved. You may use this framework to build a custom integration with a third party monitoring products to leverage the XAP alerting system.
A recommended approach for such integration would be to construct a listener that writes the chosen types of alerts into logger mechanism. Examples for such may be the log4j or the commons-logging frameworks.

The main advantage with this approach is the ability to use an extensive set of out-of-box log appenders that translates log messages into different protocols and APIs to be consumed by third party products.
![AlertsLoggerBridge.jpg](/attachment_files/AlertsLoggerBridge.jpg)

# Example

The `AlertLoggingGateway` example project provided with the GigaSpaces distribution using an existing `Log4J` Appender (SnmpTrapAppender) to convert log messages into SNMP traps, resulting in the alerts propagated to a third party network management solution.
![SNMP_Appender.jpg](/attachment_files/SNMP_Appender.jpg)

## AlertsLoggingGatway components

#### SnmpTrapTransmitter

The **SnmpTrapTransmitter** is a XAP PU responsible for the generic Alert-to-Log bridging. It does that by listening to all alerts in its alert filter file. Any incoming alerts are simply writing to commons logging log. Notice that, being generic in nature, the SnmpTrapTransmitter can be reused without any changes in similar projects.
SnmpTrapTransmitter exposes the following configuration parameters:

**AlertFileFilter** - the name of Alert filter xml file used to filter Alerts to be logged.<br>
**loggerName** - the name of the logger to be created.<br>
**group** - the XAP group for which the Alert listener will be configured.


```xml
  <bean id="SnmpTrapTransmitter" class="org.openspaces.example.alert.logging.snmp.SnmpTrapTransmitter" >
    <property name="alertFileFilter" value="notify-alerts.xml" />
    <property name="loggerName" value="org.openspaces.example.alert.logging.AlertLoggingGateway" />
    <property name="group" value="group-name-here" />
  </bean>
```

Note that if you implement your own variant for this class, for other types of alert interception, you will also have to override the `construct()` method to register for alerts, the `destroy()` method to cleanup the registration, and to create your own class implementing the `AlertTriggeredEventListener` interface in which you will issue the logging calls:


```java
public class SnmpTrapTransmitter {

	private Log logger;

	@PostConstruct
	public void construct() throws Exception {
		registerAlertTrapper();
	}

	@PreDestroy
	public void destroy() throws Exception {
	        alertManager.getAlertTriggered().remove(atListener);
	}

	private void registerAlertTrapper() {
		atListener = new AlertTriggeredEventListener() {
			public void alertTriggered(Alert alert) {
				String loggRecord;
				loggRecord = alert.toString();
				logger.info(loggRecord);
			}
		};

		XmlAlertConfigurationParser cparse = new XmlAlertConfigurationParser(alertFileFilter);
		alertManager.configure(cparse.parse());
		alertManager.getAlertTriggered().add(atListener);
	}
}
```

#### SnmpTrapSender

The **SnmpTrapSender** is a utility class that implements the SnmpTrapAppender's `SnmpTrapSenderFacade` interface with an implementation that queues and asynchronously transmits Alerts as SNMP traps. The SNMP transmission method - `sendTrap()` - uses snmp4j library as its underlying implementation.


```java
public class SnmpTrapSender implements SnmpTrapSenderFacade {

	public void addTrapMessageVariable(String trapOID, String trapValue) {
		trapQueue.add(trapValue);
	}


	public void initialize(SNMPTrapAppender arg0) {
		trapQueue.clear();
		loadRunParams();
	}

	public void sendTrap() {
		String trapVal = trapQueue.removeFirst();
                PDUv1 trapPdu = (PDUv1)DefaultPDUFactory.createPDU(SnmpConstants.version1);
                trapPdu.setType(PDU.V1TRAP);
                // pack trapVal into trapPdu
		snmp.send(trapPdu, target);
	}

```

#### commons-logging.properties and log4j.properties

The **Commons-logging.properties** file is a commons logging configuration file which re-directs its calls to a log4j logger. In our example this file contains redirection of commons-logging to log4j as the SNMP trapper we use is on top of log4j.
**log4j.properties** is a log4j configuration file which delegates log writes to the SNMPTrapAppender, resulting in SNMP traps.


```console
log4j.rootCategory=INFO,TRAP_LOG
log4j.appender.TRAP_LOG=org.apache.log4j.ext.SNMPTrapAppender
log4j.appender.TRAP_LOG.ImplementationClassName=org.openspaces.example.alert.logging.snmp.SnmpTrapSender
log4j.appender.TRAP_LOG.ManagementHost=127.0.0.1
log4j.appender.TRAP_LOG.ManagementHostTrapListenPort=162
log4j.appender.TRAP_LOG.CommunityString=public
log4j.appender.TRAP_LOG.Threshold=INFO
log4j.appender.TRAP_LOG.layout=org.apache.log4j.PatternLayout
log4j.appender.TRAP_LOG.layout.ConversionPattern=%d,%p,%t,%c,%m%n
```

# Running the Example

The example is located under `<XAP root>/tools/alert-integration`. To run it you should do the following:

1. Set the "group" value in the pu.xml file to your own XAP group. Optionally you may edit the function `registerAlertTrapper()` in `SnmpTrapTransmitter.java` to create your own `Admin` object in any way you see fit.
1. Optionally edit file `notify-alerts.xml` to set your own alerts and alert conditions that will be listened to by this example.
1. Optionally edit `log4j.properties` to set the IP and port used by your SNMP server software (if any).
1. If you do not have an SNMP server software, you should download one for the sake of running and testing this example. iReasoning MIB browser for example ([http://ireasoning.com/mibbrowser.shtml](http://ireasoning.com/mibbrowser.shtml)) provides good basic SNMP trap viewing capabilities with a free personal edition. Make sure you configure log4j.properties to use the same IP and port used by the server.
1. Build and pack the example project into a jar file. This can be done by executing the command "mvn" from the project's root directory or performing an equivalent action within your UI. A successful build should result in the creation of the example jar file in target/AlertLoggingGateway.jar.
1. If needed start XAP with at least one running LUS, GSM and GSC belonging to the XAP group declared in item #2.
1. Deploy the example JAR into the GSC.
1. If needed - perform XAP actions that will trigger one or more of the alerts the example is tuned to listen to. Creating a new GSCs is usually a good way for creating a multitude of different alerts.
1. Start-up your SNMP server to intercept and view incoming traps. If you use MIB browser enter the Trap Receiver (Ctrl-I) and make sure it is configured to listen on the right IP and port.

#. External Dependencies

1. log4j version >= 1.2.14
1. snmpTrapAppender version >= 1.2.9
1. snmp4j version >= 1.11.2
1. For the example build process you should have [Apache Maven](http://maven.apache.org) installed. You may find it already part of the GigaSpaces folders under `\gigaspaces-xap\tools\maven`.
