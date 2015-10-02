---
type: postrel
title:  New Features and Improvements
categories: RELEASE_NOTES
parent: xap101.html
weight: 200
---



Below is a list of new features and improvements in GigaSpaces 10.1.X.



| Key | Summary | Version | Sales<br>Force ID | Platform/s|
|:-------|:------|:------------|:------------|:----------|
|GS-12370|Added default -Xms512m -Xmx512m to all GS components and to EXT_JAVA_OPTIONS .                                               |10.1.1|| Java |
|GS-7738 |Order by query with limit shouldn't fetch all the entries |10.1.0|5922, 8987| All |
|GS-8567 |Add 2 new method clearById and clearByIds(IdsQuery) that are more preformant than takeById.  |10.1.0|6416,<br>6796,9169|All |
|<nobr>GS-10837</nobr>|Upgrade to Hibernate 4.1.8                   |10.1.0|| Java |
|GS-11127|Show "CPU Starvation detected messages" in logs when running on weak platform.    |10.1.0||Java |
|GS-11579|New off heap storage implementation that use MapDB's byte buffers to store off heap data.    |10.1.0||Java |
|GS-11580|EDS implementation - SSD    |10.1.0||Java |
|GS-11581|HTTP session productization    |10.1.0||Java |
|GS-11582|Java 8 Certification    |10.1.0|Java |
|GS-11593|Web-UI: planned instances displayed in Applications view on each Processing Unit's recatngle  |10.1.0||Java|
|GS-11627|Deprecate method `gigaSpace::getModifiersForIsolationLevel` |10.1.0|8643| All|
|GS-11639|Web-UI uses large amounts of memory due to statistics retention.  | 10.1.0|8741,<br>9548|Java|
|GS-11723|Expose Blob Store configuration in GS-UI.  |10.1.0||  Java   |
|GS-11726|Support 'IN' and 'BETWEEN' in queries nested values.  |10.1.0 |8925|  Java   |
|GS-11758|Support creating/configuring a space without using Jini url (jini://*/*/)     | 10.1.0||Java |
|GS-11769|Improve error message "node belongs to a wrong SL", the new error message explain the situation and how to fix the client code.     |10.1.0|8967| All |
|GS-11779|UI - Added a 'PID' column in the Data Grid.                                                                       |10.1.0||Java |
|GS-11851|Increase initial load speed from BlobStore                                                                        |10.1.0|| Java|
|GS-11870|Instead of having one '?' for each 'IN' element, users can have a single '?' for all 'IN' elements that passed as array or a list. |10.1.0||All |
|GS-11876|Allow bulk operations in blobstore to improve performance.                                                                          |10.1.0||Java |
|GS-11890|Spring 4 upgrade                                                                                                                     |10.1.0|9312|Java |
|GS-11891|Upgrade EDS to Hibernate 4                                                                                                           |10.1.0|| Java|
|GS-11893|Space quiesce command                                                                                                                |10.1.0|| All|
|GS-11899|Support Java 8 lambda for custom change ops and custom aggregators.                                                                   |10.1.0||Java |
|GS-11912|When using consistency mode quorum or all, if sync replication to quorum-1 or all of the backup fails and the operation is put in the redo log, throw an exception to the client indicating the the data does not meet the consistency requirements.|10.1.0||All |
|GS-11914|Add ability to annotate a numeric field with a @SpaceSequence annotation. The sequence is a monotonically increasing number incremented by 1 for every new object written to the space.          |10.1.0||  All |
|GS-11926|Improving JDBC driver performance                                                                                         |10.1.0|| Java  |
|GS-12008|Save filters for Hosts view <br>(saved filters are presented in combo and saved in local storage )                            |10.1.0|| All  |
|GS-12009|Add Transpose hosts screen (details pane on the right not at the bottom)                                                  |10.1.0||All   |
|GS-12010|Implement relocation for PU instances (using dialog not drag and drop)                                                    |10.1.0|| All |
|GS-12011|Screen layout is saved in local storage                                                                                   |10.1.0|| All  |
|GS-12012|Application name column is added in the hosts view.                                                                       |10.1.0|| All  |
|GS-12013|A 'type' column was added to the hosts view.                                                                              |10.1.0|| All  |
|GS-12015|Remove dashboard                                                                                                           |10.1.0|| All  |
|GS-12016|Create an events pane, that contains - alerts, events and security events.                                                 |10.1.0||  All |
|GS-12019|Update logo.                                                                                                              |10.1.0|| Java  |
|GS-12022|Style of filter toolbar in hosts view should be identical to the toolbar in applications screen .                         |10.1.0|| Java  |
|GS-12039|Added Metrics Framework                                                                                                   |10.1.0||  All |
|GS-12069|Add current license to license validation error messages.                                                                  |10.1.0||  All |
|GS-12078|Separate Cassandra data source from Openspaces. (Created new repository Gigaspaces/xap-cassandra)                          |10.1.0|| Java  |
|GS-12079|Increase fifo-groups concurrency by creating the main list segmented.                                                     |10.1.0||  All  |
|GS-12082|Add the option for deploying elastic PU from CLI.                                                                          |10.1.0|| All  |
|GS-12083| GS-12082 Add the option for deploying elastic PU from CLI.                                                                          |10.1.0|| All  |
|GS-12087|Remove all hosts row from hosts view in UI.                                                                                |10.1.0|| Java  |
|GS-12095|Replace Applications map view: Add processing units tree similar to hosts tree.                                            |10.1.0|| Java  |
|GS-12099|Allow relocate PUI to another GSC from UI.                                                                                 |10.1.0|| Java  |
|GS-12108|Support deploying elastic application.                                                                                |10.1.0|| All  |
|GS-12111|Enable a sequence-number space property (per-partition).                                                                  |10.1.0||  Java  |
|GS-12112|Support wait with timeout for deployment to complete and undeploy-on-failure.                                             |10.1.0|| All   |
|GS-12121|Support "order by nulls first/nulls last" in space queries.                                                               |10.1.0|| All   |
|GS-12124|Develop second cross views menu bar that contains: deploy, events, alerts, application selection combo sections.           |10.1.0||  Java |
|GS-12126|Improve RESTData and add make it deployable as a processing unit                                                          |10.1.0||  All   |
|GS-12130|Do not show events timeline.                                                                                              |10.1.0|| Java   |
|GS-12142|Change the format of printed of the system information details when XAP is started.                                       |10.1.0|| All   |
|GS-12161|Removed SpaceXmlDocument support.                                                                                         |10.1.0|| Java   |
|GS-12166|Change font in gs-ui log viewer to monospace                                                                              |10.1.0|| All  |
|GS-12169|Remove lib/platform/xml jars from package.                                                                                 |10.1.0|| All |
|GS-12171|Enhanced XAP.NET to support settings file config via "XapNet.SettingsFile" env variable.                                  |10.1.0|| .Net  |
|GS-12174|Add ESM support for XAP.NET. .                                               |10.1.0||  .Net  |
|GS-12183|Show space mode in CLI 'list' command.                                       |10.1.0|| All   |
|GS-12185| GS-12126 Allow to supply a full type descriptor when introducing type using the REST API.                                |10.1.0|| All   |
|GS-12194|Expose commandLineArguments at deployment and atMostOneConcurrentRelocatio.|10.1.0||  All |
|GS-12200|Added support for java 8 java.time.|10.1.0| |  Java  |
|GS-12203|Add getters to read some default values in DefaultGigaSpace (readTimeout, takeTimeout, WriteLease and IsolationLayer). |10.1.0| 9507 |  Java  |
|GS-12205|blobstore- check rate(%) in reads/takes after write threshold reached.                                                    |10.1.0|| Java   |
|GS-12207|blob-store, don't deserialize result on remove operation.|10.1.0|| All   |
|GS-12211|End of life - UndeployingEventProcessingUnitContainer.                                                                    |10.1.0|| Java   |
|GS-12225|Simplified Space Iterator API - single template (instead of collection) , bring only current entries and expose simpler API. |10.1.0|| Java  |
|GS-12226|Allow uses of providing sharedMachineProvisioning option in CLI and Application.                                             |10.1.0|| All |
|GS-12227|Allow ssl usage without authentication (encryption only mode).                                                               |10.1.0|| All |
|GS-12240|GS-11893 Quiesce mode scenario: Safely undeploy a Space processing unit.                                                     |10.1.0|| All   |
|GS-12244|Remove memcached deployment option per pm request.|10.1.0||  Java |
|GS-12248|GS-11893 Quiesce mode scenario: Safely undeploy a space processing unit with polling container.                              |10.1.0|| Java   |
|GS-12257|Don't save statistics in Admin instance created by Web UI.|10.1.0|| Java   |
|GS-12270| GS-11893 Quiesce mode scenario: Hot deploy.                                                                                 |10.1.0|| Java   |
|GS-12284|Add metric reporter that report to a file as sample for custom reporter.|10.1.0|| Java  |
|GS-12286|Deprecated Gigaspace.snapshot() - use prepareTemplate() or getTypeManager().registerTypeDescriptor instead .            |10.1.0|| Java |
|GS-12287|Allow access to the new Quiesce/Unquiesce commands via the CLI.                                                              |10.1.0|| All |
|GS-12289|Add space-name property to space spring definition.                                                                          |10.1.0|| Java |


