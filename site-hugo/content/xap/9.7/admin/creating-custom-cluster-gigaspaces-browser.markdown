---
type: post97
title:  Creating Custom Cluster
categories: XAP97ADM
parent: working-with-clusters-gigaspaces-browser.html
weight: 200
---

{{% ssummary %}} {{% /ssummary %}}



{{% warning %}}
This section intended only for advanced users interested with a special data-grid cluster topology that is not provided with the out of the box [supported topologies](/product_overview/space-topologies.html). The supported out of the box data-grid cluster topologies are: synchronous replicated , a-synchronous replicated , partitioned , partitioned+ synchronous replicated. Any out of the box cluster topology can be deployed via the GS-UI or the [deploy CLI](./deploy-command-line-interface.html) without the need to generate cluster configuration file as described below.
{{% /warning %}}

**To build a custom cluster from a standard cluster schema:**

1. If not already open, open a Space Browser.
1. Using the mouse, select **Cluster|New** from the menu items in the Space Browser.
1. Enter the Cluster Name: `customcluster`.
1. Select the Topology: `partitioned-sync2backup`.
1. Specify the number of primary members to be 2 and the number of backup members to be 1 per primary.
1. Click **Next**.
1. The settings for the 4 space URLs are fine. Click **Next**.
1. Select the top node: `LB_FO_Backup`, click **Edit**. Note that all spaces are considered to be part of this group. This is good. Notice also that all space operations will be executed across the cluster using a Hashing algorithm (this is due to the default hash setting).
1. Click on the **Fail Over** tab. Note that container1 and container2 each have a listed backup node. (container1_1 and 2_1 respectively).
1. Click **Cancel** to exit that node view and select the next two nodes. Investigate their settings (if you like, you can change the replication to synchronous) and examine the Replication Matrix by checking the box and clicking **Edit. . .**
1. Expand the window until you can see the entire replication matrix. The relationships are established between spaces according to the type of replication in which they engage. You can make changes in the replication mode for any particular relationship.
1. Exit all node views by pressing **Cancel**, and return to the Create Groups Wizard window.
1. Click **Next** (there should be no errors).
1. Click **View File** and walk through the generated XML configuration file.
1. Close the file view.
1. Select **Finish**: a **Save File** dialogue box will appear.
1. Select **All Files** and specify a location and name for your new cluster configuration file (it should end in `.xml`) For example: `customcluster.xml`.
1. You should see a confirmation message that indicates that your file was saved successfully.
Congratulations! You now have a reusable cluster configuration file that you can use to build clusters.

{{% tip %}}
 Cluster and Space schema files located within the \gigaspaces-xap-root\lib\required\gs-runtime.jar\config\schemas folder
{{% /tip %}}

# Viewing Cluster Configuration in Space Browser

The space browser allows you to view the relationships and expected behavior of a cluster.

1. Select **Cluster\|Viewer** from the **Menu** options at the top of the Space Browser.
1. Open the `customcluster.xml` file you created and view the cluster configuration. `This cluster is not running, you are merely observing its structure.`
1. Right-click the **customcluster** node and select **Close** from the menu.

h1. Starting the customcluster and Creating a Truly Customized Cluster
When creating a cluster configuration file, only the URLs are specified for each member as it exposes itself to the Lookup Service. You will now have the opportunity to specify the space schema and any additional specifications of each instance as you create a start script and start them.
Steps to perform:

1. Create a start script (usually from an existing one).
1. Specify the space schema for each space in the start script (using a properties file helps a lot here).
1. Ensure that the classpath and arguments to the space support the necessary behavior such as: embedded workers, filters, or JDBC driver utilization.
1. Start the spaces and get to work!
1. An example cluster start script that uses the `customcluster.xml` is provided in the `clusterLab` directory. It is called `customclusterstartup.cmd`
1. Start up the `customcluster` and examine it in the Space Browser as you did with the previous clusters.

The `customclusterstartup.cmd` starts up the following script:


```java
start "customcluster1" gsInstance
   "java://localhost/customcluster_container1/customcluster?properties=customcluster&schema=default"

start "customcluster2" gsInstance
   "java://localhost/customcluster_container2/customcluster?properties=customcluster&schema=default"

start "customcluster1_back" gsInstance
   "java://localhost/customcluster_containecustomcluster?properties=customcluster&schema=default"

start "customcluster2_back" gsInstance
   "java://localhost/customcluster_container2_1/customcluster?properties=customcluster&schema=default"
```

The properties file:
The default value is 60000 milliseconds (60 seconds).


```java
-Dsun.rmi.dgc.client.gcInterval=20000000
-Dsun.rmi.dgc.server.gcInterval=20000000

com.gs.env.report=true
gs.space.url.arg.nowritelease=true
-Dcom.gs.debug=true
-Dcom.gs.clusterXML.debug=false
-Dcom.gs.client.debug=false
-Dcom.gs.cluster.cluster_enabled=true
-Dcom.gs.cluster.config-url=config/customcluster.xml
```

The cluster XML file:


```xml
<?xml version="1.0"?>
<cluster-config>
     <cluster-name>customcluster</cluster-name>
     <dist-cache>
          <config-name>DefaultConfig</config-name>
     </dist-cache>
     <cluster-members>
          <member>
               <member-name>customcluster_container1:customcluster</member-name>
               <member-url>jini://*/customcluster_container1/customcluster&lt;/member-url>
          </member>
          <member>
               <member-name>customcluster_container1_1:customcluster</member-name>
               <member-url>jini://*/customcluster_container1_1/customcluster&lt;/member-url>
          </member>
          <member>
               <member-name>customcluster_container2:customcluster</member-name>
               <member-url>jini://*/customcluster_container2/customcluster&lt;/member-url>
          </member>
          <member>
               <member-name>customcluster_container2_1:customcluster</member-name>
               <member-url>jini://*/customcluster_container2_1/customcluster&lt;/member-url>
          </member>
     </cluster-members>
     <groups>
          <group>
               <group-name>LB_FO_Backup</group-name>
               <group-members>
                    <member>
                         <member-name>customcluster_container1:customcluster</member-name>
                    </member>
                    <member>
                         <member-name>customcluster_container1_1:customcluster</member-name>
                    </member>
                    <member>
                         <member-name>customcluster_container2:customcluster</member-name>
                    </member>
                    <member>
                         <member-name>customcluster_container2_1:customcluster</member-name>
                    </member>
               </group-members>
               <load-bal-policy>
                    <load-bal-impl-class>com.gigaspaces.cluster.loadbalance.LoadBalanceImpl</load-bal-impl-class>
                    <disable-parallel-scattering>false</disable-parallel-scattering>
                    <notify>
                         <policy-type>hash-based</policy-type>
                         <broadcast-condition>broadcast-if-null-values</broadcast-condition>
                    </notify>
                    <default>
                         <policy-type>hash-based</policy-type>
                         <broadcast-condition>broadcast-if-null-values</broadcast-condition>
                    </default>
               </load-bal-policy>
               <fail-over-policy>
                    <fail-back>false</fail-back>
                    <fail-over-find-timeout>2000</fail-over-find-timeout>
                    <default>
                         <policy-type>fail-to-backup</policy-type>
                         <backup-members>
                              <member>
                                   <source-member>customcluster_container1:customcluster</source-member>
                                   <backup-member>customcluster_container1_1:customcluster</backup-member>
                              </member>
                              <member>
                                   <source-member>customcluster_container2:customcluster</source-member>
                                   <backup-member>customcluster_container2_1:customcluster</backup-member>
                              </member>
                         </backup-members>
                         <backup-members-only>
                              <backup-member-only>customcluster_container1_1:customcluster</backup-member-only>
                              <backup-member-only>customcluster_container2_1:customcluster</backup-member-only>
                         </backup-members-only>
                    </default>
               </fail-over-policy>
          </group>
          <group>
               <group-name>replGroupcustomcluster_0</group-name>
               <group-members>
                    <member>
                         <member-name>customcluster_container1:customcluster</member-name>
                    </member>
                    <member>
                         <member-name>customcluster_container1_1:customcluster</member-name>
                    </member>
               </group-members>
               <repl-policy>
                    <replication-mode>async</replication-mode>
                    <policy-type>full-replication</policy-type>
                    <recovery>true</recovery>
                    <replicate-notify-templates>true</replicate-notify-templates>
                    <trigger-notify-templates>false</trigger-notify-templates>
                    <repl-find-timeout>5000</repl-find-timeout>
                    <communication-mode>unicast</communication-mode>
                    <async-replication>
                         <repl-original-state>false</repl-original-state>
                         <sync-on-commit>false</sync-on-commit>
                         <sync-on-commit-timeout>300000</sync-on-commit-timeout>
                         <repl-chunk-size>500</repl-chunk-size>
                         <repl-interval-millis>3000</repl-interval-millis>
                         <repl-interval-opers>500</repl-interval-opers>
                    </async-replication>
                    <sync-replication>
                         <todo-queue-timeout>1500</todo-queue-timeout>
                         <unicast>
                              <min-work-threads>4</min-work-threads>
                              <max-work-threads>16</max-work-threads>
                         </unicast>
                         <multicast>
                              <ip-group>224.0.0.1</ip-group>
                              <port>28672</port>
                              <min-work-threads>4</min-work-threads>
                              <max-work-threads>16</max-work-threads>
                         </multicast>
                    </sync-replication>
               </repl-policy>
          </group>
          <group>
               <group-name>replGroupcustomcluster_1</group-name>
               <group-members>
                    <member>
                         <member-name>customcluster_container2:customcluster</member-name>
                    </member>
                    <member>
                         <member-name>customcluster_container2_1:customcluster</member-name>
                    </member>
               </group-members>
               <repl-policy>
                    <replication-mode>async</replication-mode>
                    <policy-type>full-replication</policy-type>
                    <recovery>true</recovery>
                    <replicate-notify-templates>true</replicate-notify-templates>
                    <trigger-notify-templates>false</trigger-notify-templates>
                    <repl-find-timeout>5000</repl-find-timeout>
                    <communication-mode>unicast</communication-mode>
                    <async-replication>
                         <repl-original-state>false</repl-original-state>
                         <sync-on-commit>false</sync-on-commit>
                         <sync-on-commit-timeout>300000</sync-on-commit-timeout>
                         <repl-chunk-size>500</repl-chunk-size>
                         <repl-interval-millis>3000</repl-interval-millis>
                         <repl-interval-opers>500</repl-interval-opers>
                    </async-replication>
                    <sync-replication>
                         <todo-queue-timeout>1500</todo-queue-timeout>
                         <unicast>
                              <min-work-threads>4</min-work-threads>
                              <max-work-threads>16</max-work-threads>
                         </unicast>
                         <multicast>
                              <ip-group>224.0.0.1</ip-group>
                              <port>28672</port>
                              <min-work-threads>4</min-work-threads>
                              <max-work-threads>16</max-work-threads>
                         </multicast>
                    </sync-replication>
               </repl-policy>
          </group>
     </groups>
</cluster-config>
```
