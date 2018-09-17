---
type: post
title:  Managing XAP with init.d
categories: SBP
weight: 1900
parent: production.html
---


|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Patrick May| 10.0 | Sept 2014|    |    |




# Introduction
The standard for managing long running daemons and daemon-like processes on Unix
systems is via a script residing in the /etc/init.d directory. Typically these scripts provide
four capabilities:

- start<br>
- stop <br>
- restart<br>
- status

This basic set of features can manage a simple   XAP configuration. More complex configurations require correspondingly more functionality.

# An init.d Script Shell
A typical init.d script follows this pattern:

```bash
#!/bin/sh

# Script description
start(){
}

stop(){
}

status(]{
}

case "$1" in start)
    start
    ;;

    stop)
        stop
    ;;

    restart)
     stop
     start
    ;;

    status)
     status
    ;;
*)
echo "Usage: $0 {start|stop|restart|status}"
exit 1
esac

```

For XAP, this script will be named simply /etc/init.d/xap.

# Configuring the   XAP Infrastructure

Managing a simple XAP distributed space requires a certain amount of configuration information:

- The location where Java is installed<br>
- The location of the GigaSpaces XAP installation directory<br>
- The lookup group, if used<br>
- The IP addresses of the machines on which to run lookup services<br>
- The IP addresses of the machines on which to run GSMs<br>
- The number of GSCs per machine<br>

Several of these options are set at the top of the script:<br>

```bash

# Start and stop XAP infrastructure

#!/bin/sh

if [ -z "$JSHOMEDIR" ]; then
    echo "Please set JSHOMEDIR."
exit 1

fi

export GS_HOME=${JSHOMEDIR}
export PATH=${JSHOMEDIR}/bin:${PATH}
export LOOKUPLOCATORS=“server1:4174,server2:4174”

${JSHOMEDIR}/bin/setenv.sh
```

#### Starting the   XAP Infrastructure

The  XAP Agent is the easiest way to deploy the infrastructure components:

```bash

start() {
    ${JSHOMEDIR}/bin/gs-agent.sh gsa.gsc 4 gsa.lus 1 gsa.gsm 1 & >/dev/null
}
```

The lookup service (LUS) and GSM should only be started on the machines specified in the LOOKUPLOCATORS environment variable. This can be accomplished either by having different scripts on each machine or by checking for the machine name in the script and passing the appropriate values to gs-agent.sh.

Multiple agents can be started to manage different zones.

#### Stopping the   XAP Infrastructure

Shutting down the XAP infrastructure components is not as straightforward as starting them, at least in versions 9.x and 10.0. Improvements are planned for later releases. In the meantime, it is necessary to use the Admin API to cleanly shutdown.  The code to shutdown all Processing Units for a zone, undeploy the GSCs, and stop the Agent is:

```java
/**
 * ZoneShutdown shuts down all GigaSpaces infrastructure components and
 * processing units associated with a zone.
 *
 * @author Patrick May (patrick.may@gigaspaces.com)
 * @author &copy; 2014 GigaSpaces Technologies Inc.  All rights reserved.
 * @version 1
 */

package com.gigaspaces.utils.admin;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.openspaces.admin.gsa.GridServiceAgent;
import org.openspaces.admin.gsc.GridServiceContainer;
import org.openspaces.admin.gsc.events.GridServiceContainerAddedEventListener;
import org.openspaces.admin.pu.ProcessingUnit;
import org.openspaces.admin.pu.ProcessingUnitInstance;
import org.openspaces.admin.pu.events.ProcessingUnitAddedEventListener;
import org.openspaces.admin.zone.Zone;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.logging.Logger;

public class ZoneShutdown
  implements Runnable,
             GridServiceContainerAddedEventListener,
             ProcessingUnitAddedEventListener
{
  private static final long SHUTDOWN_ATTEMPT_INTERVAL = 2000;  // 2 seconds
  private static final long UPDATE_SETTLE_INTERVAL = 3000;  // 3 seconds

  private static Logger logger_
    = Logger.getLogger(ZoneShutdown.class.getName());

  private String zoneName_ = null;
  private boolean done_ = false;
  private Admin admin_ = null;
  private long lastUpdate_ = 0;
  private HashSet<GridServiceAgent> gsas_ = new HashSet<GridServiceAgent>();
  private ArrayList<GridServiceContainer> gscs_
    = new ArrayList<GridServiceContainer>();
  private ArrayList<ProcessingUnit> pus_ = new ArrayList<ProcessingUnit>();

  /**
   * The full constructor for the ZoneShutdown class.
   */
  public ZoneShutdown(String zoneName)
    {
    zoneName_ = zoneName;

    String lookupGroups = System.getenv("LOOKUPGROUPS");
    String lookupLocators = System.getenv("LOOKUPLOCATORS");

    AdminFactory factory = new AdminFactory();
    if (lookupGroups != null)
      factory.addGroups(lookupGroups);
    if (lookupLocators != null)
      factory.addLocators(lookupLocators);

    admin_ = factory.createAdmin();
    }


  public boolean isDone() { return done_; }
  public void done() { done_ = true; }


  /**
   * Update the last update timestamp.
   */
  private void updated()
    {
    lastUpdate_ = System.currentTimeMillis();
    }


  /**
   * Determine if there is enough information to shutdown the zone.
   */
  private boolean readyToShutdown()
    {
    return ((lastUpdate_ != 0)
            && ((System.currentTimeMillis() - lastUpdate_)
                > UPDATE_SETTLE_INTERVAL)
            && (gscs_.size() > 0)
            && (pus_.size() > 0));
    }


  /**
   * Get the set of unique processing units associated with the GSCs in
   * the zone.
   */
  private HashSet<ProcessingUnit> uniqueProcessingUnits()
    {
    HashSet<ProcessingUnit> pus = new HashSet<ProcessingUnit>();
    int totalInstances = 0;

    synchronized(gscs_)
      {
      for (GridServiceContainer gsc : gscs_)
        for (ProcessingUnitInstance instance : gsc.getProcessingUnitInstances())
          {
          pus.add(instance.getProcessingUnit());
          ++totalInstances;
          }
      }

    logger_.info("ZoneShutdown.uniqueProcessingUnits() found "
                 + pus.size()
                 + " unique PUs from "
                 + totalInstances
                 + " instances.");

    return pus;
    }


  /**
   * Undeploy all processing units running in GSCs in the specified zone.
   */
  private void undeployProcessingUnits()
    {
    logger_.info("ZoneShutdown.undeployProcessingUnits() called.");

    for (ProcessingUnit pu : uniqueProcessingUnits())
      {
      logger_.info("Undeploying ProcessingUnit " + pu.getName());
      pu.undeployAndWait();
      logger_.info("ProcessingUnit " + pu.getName() + " undeployed.");
      }

    logger_.info("ZoneShutdown.undeployProcessingUnits() returning.");
    }


  /**
   * Kill any GSCs that have no remaining processing units.
   */
  private void killGSCs()
    {
    logger_.info("ZoneShutdown.killGSCs() called.");
    synchronized(gscs_)
      {
      for (GridServiceContainer gsc : gscs_)
        {
        gsas_.add(gsc.getGridServiceAgent());
        if (gsc.getProcessingUnitInstances().length == 0)
          {
          logger_.info("ZoneShutdown.killGSCs(): killing GSC.");
          gsc.kill();
          logger_.info("ZoneShutdown.killGSCs(): killed GSC.");
          }
        else
          logger_.warning("ZoneShutdown.killGSCs(): GSC still has PUs.");
        }
      }
    logger_.info("ZoneShutdown.killGSCs() returning.");
    }


  /**
   * Shutdown any GSAs that have no remaining GSCs.
   */
  private void shutdownGSAs()
    {
    logger_.info("ZoneShutdown.shutdownGSAs() called.");
    synchronized(gsas_)
      {
      for (GridServiceAgent gsa : gsas_)
        {
        logger_.info("ZoneShutdown.shutdownGSAs(): shutting down GSA.");
        gsa.shutdown();
        logger_.info("ZoneShutdown.shutdownGSAs(): GSA shut down.");
        }
      }
    logger_.info("ZoneShutdown.shutdownGSAs() returning.");
    }


  /**
   * If ready, shutdown all components associated with the zone.
   */
  private boolean attemptShutdown()
    {
    logger_.info("ZoneShutdown.attemptShutdown() called.");
    if (readyToShutdown())
      {
      undeployProcessingUnits();
      killGSCs();
      shutdownGSAs();
      done();
      }

    logger_.info("ZoneShutdown.attemptShutdown() returning.");
    return isDone();
    }


  /**
   * Method called when a GSC is added.  This method is inherited from
   * the GridServiceContainerAddedEventListener interface.
   */
  public void gridServiceContainerAdded(GridServiceContainer gsc)
    {
    logger_.info("GridServiceContainer found.");
    for (Zone zone : gsc.getZones().values())
      {
      logger_.info("  " + zone.getName());
      if (zoneName_.equals(zone.getName()))
        {
        synchronized(gscs_) { gscs_.add(gsc); }
        logger_.info("GridServiceContainer added.");
        }
      }

    updated();
    }


  /**
   * Method called when a PU is added.  This method is inherited from
   * the ProcessingUnitAddedEventListener interface.
   */
  public void processingUnitAdded(ProcessingUnit pu)
    {
    logger_.info("ProcessingUnit " + pu.getName() + " added.");
    synchronized(pus_) { pus_.add(pu); }

    updated();
    }


  /**
   * Run until done.
   */
  public void run()
    {
    admin_.addEventListener(this);

    while (!isDone())
      {
      try
        {
        Thread.sleep(SHUTDOWN_ATTEMPT_INTERVAL);
        if (attemptShutdown())
          {
          logger_.info("ZoneShutdown.run():  removing event listener.");
          admin_.removeEventListener(this);
          logger_.info("ZoneShutdown.run():  closing admin.");
          admin_.close();
          logger_.info("ZoneShutdown.run():  done.");
          break;
          }
        }
      catch(InterruptedException e) { }
      }

    logger_.info("ZoneShutdown.run() returning.");
    }


  /**
   * The command line harness for the ZoneShutdown class.
   *
   * @param args The command line arguments passed in.
   */
  public static void main(String args[])
    {
    if (args.length == 1)
      {
      ZoneShutdown shutdown = new ZoneShutdown(args[0]);
      shutdown.run();
      }
    else
      logger_.info("Usage:  java "
                   + ZoneShutdown.class.getName()
                   + " <zone-name>");
    }
}  // end ZoneShutdown
```

This should be packaged in a jar file so it can be called like this:

```bash
stop() {
    java -jar zone-shutdown.jar ${ZONE_NAME}
}
```


# Monitoring the XAP Infrastructure

As with stopping the XAP infrastructure, getting the status currently requires using the Admin API. The code to determine how many GSCs are available for a particular zone is:

```java
/**
 * AbstractGridComponentMonitor provides the basic behavior required by
 * all concrete grid component monitors.
 *
 * @author Patrick May (patrick.may@gigaspaces.com)
 * @author &copy; 2014 GigaSpaces Technologies Inc.  All rights reserved.
 * @version 1
 */

package com.gigaspaces.utils.admin;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminEventListener;

import java.util.logging.Logger;

public abstract class AbstractGridComponentMonitor implements Runnable
{
  private static Logger logger_
    = Logger.getLogger(AbstractGridComponentMonitor.class.getName());
  private static Object threadMonitor_ = new Object();

  private boolean done_ = false;
  private Admin admin_ = null;

  /**
   * The full constructor for the AbstractGridComponentMonitor class.
   */
  public AbstractGridComponentMonitor(Admin admin)
    {
    admin_ = admin;
    }


  public boolean isDone() { return done_; }

  protected abstract AdminEventListener eventListener();

  /**
   * Run until done.
   */
  public void run()
    {
    AdminEventListener eventListener = eventListener();

    admin_.addEventListener(eventListener);

    while (!isDone())
      {
      synchronized(threadMonitor_)
        {
        try { threadMonitor_.wait(); }
        catch(InterruptedException e) { }
        }
      }

    synchronized(eventListener) { admin_.removeEventListener(eventListener); }
    try { Thread.sleep(2000); }
    catch(InterruptedException ignore) { }
    }


  /**
   * Stop listening.
   */
  public void done()
    {
    done_ = true;
    synchronized(threadMonitor_) { threadMonitor_.notifyAll(); }
    }
}  // end AbstractGridComponentMonitor
```

and

```java
/**
 * ZoneGSCMonitor monitors the Admin API for changes to
 * GSCs in the GigaSpaces XAP infrastructure.
 *
 * @author Patrick May (patrick.may@gigaspaces.com)
 * @author &copy; 2014 GigaSpaces Technologies Inc.  All rights reserved.
 * @version 1
 */

package com.gigaspaces.utils.admin;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminEventListener;
import org.openspaces.admin.gsc.GridServiceContainer;
import org.openspaces.admin.gsc.events.GridServiceContainerAddedEventListener;
import org.openspaces.admin.zone.Zone;

import java.util.ArrayList;

import java.util.logging.Logger;

public class ZoneGSCMonitor
  extends AbstractGridComponentMonitor
  implements GridServiceContainerAddedEventListener
{
  private static final long MONITOR_INTERVAL = 2000;  // 2 seconds

  private static Logger logger_
    = Logger.getLogger(ZoneGSCMonitor.class.getName());

  private String zoneName_ = null;
  private int gscCount_ = 0;
  private ArrayList<GridServiceContainer> gscs_
    = new ArrayList<GridServiceContainer>();

  /**
   * Inherited from AbstractGridComponentMonitor.
   */
  protected AdminEventListener eventListener() { return this; }

  /**
   * The full constructor for the ZoneGSCMonitor class.
   */
  public ZoneGSCMonitor(Admin admin,String zoneName,int gscCount)
    {
    super(admin);
    zoneName_ = zoneName;
    gscCount_ = gscCount;
    }


  /**
   * Method called when a GSC is added.  This method is inherited from
   * the GridServiceContainerAddedEventListener interface.
   */
  public void gridServiceContainerAdded(GridServiceContainer gsc)
    {
    logger_.info("GridServiceContainer found.");
    for (Zone zone : gsc.getZones().values())
      {
      logger_.info("  " + zone.getName());
      if (zoneName_.equals(zone.getName()))
        {
        synchronized(gscs_) { gscs_.add(gsc); }
        logger_.info("GridServiceContainer added.");
        }
      }

    if (gscs_.size() >= gscCount_)
      done();
    }
}  // end ZoneGSCMonitor
```

This should be called from a wrapper that prints the status to standard output:

```java
/**
 * ZoneMonitor monitors the Admin API for changes to the GigaSpaces XAP
 * infrastructure related to a particular zone.
 *
 * @author Patrick May (patrick.may@gigaspaces.com)
 * @author &copy; 2014 GigaSpaces Technologies Inc.  All rights reserved.
 * @version 1
 */

package com.gigaspaces.utils.admin;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;

import java.util.ArrayList;

import java.util.logging.Logger;

public class ZoneMonitor implements Runnable
{
  private static Logger logger_ = Logger.getLogger(ZoneMonitor.class.getName());

  private Admin admin_ = null;
  private ZoneGSCMonitor zoneGSCMonitor_ = null;

  private Admin admin()
    {
    if (admin_ == null)
      {
      String lookupGroups = System.getenv("LOOKUPGROUPS");
      String lookupLocators = System.getenv("LOOKUPLOCATORS");

      AdminFactory factory = new AdminFactory();
      if (lookupGroups != null)
        factory.addGroups(lookupGroups);
      if (lookupLocators != null)
        factory.addLocators(lookupLocators);

      admin_ = factory.createAdmin();
      }

    return admin_;
    }


  /**
   * The full constructor for the ZoneMonitor class.
   */
  public ZoneMonitor(String zoneName,int gscCount)
    {
    zoneGSCMonitor_ = new ZoneGSCMonitor(admin(),zoneName,gscCount);
    }


  /**
   * Run until done.
   */
  public void run()
    {
    Thread zoneGSCThread = new Thread(zoneGSCMonitor_);
    zoneGSCThread.start();

    while (!zoneGSCMonitor_.isDone())
      {
      try { zoneGSCThread.join(); }
      catch(InterruptedException ignore) { }
      }

    admin_.close();
    }


  /**
   * The command line harness for the ZoneMonitor class.
   *
   * @param args The command line arguments passed in.
   */
  public static void main(String args[])
    {
    if (args.length == 2)
      {
      ZoneMonitor monitor = new ZoneMonitor(args[0],Integer.parseInt(args[1]));
      monitor.run();
      logger_.info("At least "
                   + args[1]
                   + " GSCs are available in zone "
                   + args[0]);
      }
    else
      logger_.info("Usage:  java "
                   + ZoneMonitor.class.getName()
                   + " <zone-name> <gsc-count>");

    System.exit(0);
    }
}  // end ZoneMonitor
```

This should be packaged in a jar file so it can be called like this:

```bash
stop() {
  java -jar zone-status.jar ${ZONE_NAME}
}
```


### The Full init.d/xap Script

The full script looks like this:

```bash
#!/bin/sh

# Start and stop the   XAP infrastructure

if [ -z "$JSHOMEDIR" ]; then
  echo "Please set JSHOMEDIR."
exit 1

fi

export GS_HOME=${JSHOMEDIR}
export PATH=${JSHOMEDIR}/bin:${PATH}
export LOOKUPLOCATORS=“server1:4174,server2:4174”

${JSHOMEDIR}/bin/setenv.sh

start()
{
    ${JSHOMEDIR}/bin/gs-agent.sh gsa.gsc 4 gsa.lus 1 gsa.gsm 1 & >/dev/null
}

stop()
{
    java -jar zone-shutdown.jar ${ZONE_NAME}
}

status()
{
    java -jar zone-status.jar ${ZONE_NAME}
}

case "$1" in start)
    start
    ;;
    stop)
     stop
    ;;
    restart)
     stop
     start
    ;;
    status)
     status
    ;;
*)

echo "Usage: $0 {start|stop|restart|status}"
exit 1
esac

```

# Deploying the Space
Once the XAP infrastructure is running on all provisioned machines, the space can be deployed. This can either be done as part of the /etc/init.d/xap script or a separate script. In either case, two conditions must be met:

1. All infrastructure components across all machines must be provisioned

2. The space should be deployed once and only once

The second condition means that only one machine should call the script to deploy the space.  The first means that the deployment script must use the Admin API to confirm that all components are available.  The code to determine how many GSCs are available for a particular zone is in ZoneMonitor.java above.
