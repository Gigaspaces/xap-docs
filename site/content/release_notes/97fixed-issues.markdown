---
type: post
title:  Resolved Issues
categories: RELEASE_NOTES
parent: xap97.html
weight: 300
---


Below is a list of issues that have been fixed in GigaSpaces 9.7.X.




| Key | Summary | Fix Version/s | SalesForce ID | Platform/s |
|:---------|:--------|:----------------|:---------------|:------------------|
| <nobr>GS-12141</nobr> | Transaction consolidation fails when one of the participants has only transient objects  | 9.7.2, 10.1.0 | 9432 | All |
| GS-12137 | Out of memory during lrmi client handshake | 9.7.2, 10.1.0 |  | Java |
| GS-12122 | notify update bug may result getting wrong value in rapid updates of entry | 9.7.2, 10.1.0 |  | Java |
| GS-12104 | After split brain resolution a .NET backup Space may remain inactive |  9.7.2, 10.1.0 | 9368 | Java |
| GS-12101 | XA transaction with suspend resume fails | 9.7.2, 10.1.0 | 9373 | Java |
| GS-12085 | Instance missing after GSM failover due to "clustered space member must be defined in at least in one partition" | 9.7.2, 10.1.0 | 9018 | Java |
| GS-12077 | LUS OOM in rare condition of plenty registration and unregistration of services due to disconnections | 9.7.2, 10.1.0 | 9316 | All |
| GS-12074 | Deploy Application with dependencies hang in case of more then 2 instances per service | 9.7.2, 10.1.0 | 9279 | All |
| GS-12054 | Pending provision of processing unit instance after failover of machine with GSM and ESM due to GSM not in sync | 9.7.2, 10.1.0 | 9274 | Java |
| GS-11978 | Memory Leak in FIfo Groups in certain condition | 9.7.2, 10.1.0 | 9218 | All |
| GS-12047 | Limit the query results by a system property to avoid crashing the space with large queries | 9.7.2, 10.1.0 | 9198 | All |
| GS-12045 | .Net - IndexOutOfBoundsException when iterating over IOutgoingReplication | 9.7.2, 10.1.0 |  | .Net |
| GS-12043 | Impossible to override NIC_ADDR at environment without dns like openstack | 9.7.2, 10.1.0 |  | Java |
| GS-11991 | Add support of lrmi port range and wan gateway | 9.7.2, 10.1.0 |  | Java |
| GS-11979 | IllegalArgumentException When Using SQLQuery and one of properties is compressed or binary | 9.7.2, 10.1.0 | 9220,9275,9325,9376 | Java |
| GS-11978 | Memory Leak in FIfo Groups in certain condition | 9.7.2, 10.1.0 | 9218 | All |
| GS-11972 | Client failed to connect to space when the disconnected machine appears first in jini url | 9.7.2, 10.1.0 | 9200, 9529 | Java |
| GS-11834 | GSC that is supposed to be terminated due to rebalancing does not terminate | 9.7.2, 10.1.0 | 9047 | All |
| GS-11833 | Missing parttition after reboot of node due to ESM negative capacity exception | 9.7.2, 10.1.0 | 9017 | All |
| GS-11832 | NPE in getCachedBuffer in rare condition | 9.7.2, 10.1.0 | 9128 | All |
| GS-11825 | SpaceMetadataException logged In loop when adding index to a type that doesn't exists in the mirror | 9.7.2, 10.1.0 | 9044 | Java |
| GS-11775 | NPE - when trying to resolve certain split brain scenario  | 9.7.2, 10.1.0 | 9032 | All |
| GS-11748 | LRU space with no EDS partitioned-sync2backup might face version conflict that will lead to replication error and increase redolog  | 9.7.2, 10.0.0 | 8962 | All |
| GS-11740 | Duplicate lease renewal in MapCache | 9.7.2, 10.1.0 | 8815 | Java |
| GS-11736 | Can't see space in gs-ui when running XAP 9.7.1 gsInstance.bat when using jdk 7u55 | 9.7.2, 10.0.0 |  | Java |
| GS-11732 | Disributed transaction over multiple clusters might cause consolidations problems | 9.7.2, 10.0.0 | 8935 | All |
| GS-11641 | Default Notifications may not consume all concurrent resources when it could have | 9.7.2, 10.0.0 | 8635 | All |
| GS-11665 | Ability to add GS services JAVA_OPTIONS within setenv.sh | 9.7.2, 10.1.0 | 8768 | Java |
| GS-10903 | Repeatable SQLQuery with a slight difference per each SQL (changing the value is enough) for long time causes memory leak in client | 9.7.2, 10.1.0 | 7791 | All |
| GS-10510 | Consistency issues with multisource replication when transient entries are involved (distributed transaction consolidation) | 9.7.2 |  | All |
| GS-11599 | Limit number of LookupKeepaliveTask's per lookup service | 9.7.2, 10.0.0 | 9204 | Java |
| GS-11739 | Benchmark Write using batches writes actually only part of expected objects | 9.7.1, 10.0.0 |  | Java |
| GS-11717 | XAP benchmark fails on primitiveWithoutNullValue protective mode | 9.7.1, 10.0.0 |  | All |
| GS-11704 | Queries with empty ranges IndexOutOfBoundsException | 9.7.1, 10.0.0 | 8894 | All |
| GS-11690 | Linq expressions without where cause throw exception | 9.7.1, 10.0.0 |  | .Net |
| GS-11689 | Protective mode primitiveWithoutNullValue is thrown from replication | 9.7.1, 10.0.0 | 8808 | All |
| GS-11681 | Ping cli command does not work | 9.7.1, 10.0.0 |  | Java |
| GS-11661 | Using enums with Linq throws an exception | 9.7.1, 10.0.0 |  | .Net |
| GS-11652 | Calendar instance is not formatted nicely in GS-ui query results | 9.7.1, 10.0.0 |  | Java |
| GS-11651 | Web-ui: NumberFormatException thrown while parsing cpu values for specific Local values | 9.7.1, 10.0.0 |  | Java |
| GS-11640 | Installmavenrep.bat fails because POMGenerator doesn't generate mongo-datasource pom | 9.7.1, 10.0.0 |  | Java |
| GS-11633 | Add Multi thread support to XAResourceImpl | 9.7.1, 10.0.0 | 8698 | Java |
| GS-11619 | Fix  installmavenrep.bat script in windows | 9.7.1, 10.0.0 |  | All |
| GS-11616 | ReadModifiers  missing default constructor  | 9.7.1, 10.0.0 | 8657 | Java |
| GS-11615 | SpaceDataSourceSplitter#initialMetadataLoad might causes NPE | 9.7.1, 10.0.0 | 8655 | All |
| GS-11606 | Deterministic Deployment is not working on secured grid | 9.7.1, 10.0.0 | 8642 | All |
| GS-11561 | Reading a POJO which contains a document using mongo EDS fails | 9.7.1, 10.0.0 |  | Java |
| GS-11536 | LRMI threads cause JVM-wide slowdown with parallel reads | 9.7.1, 10.0.0 | 8527 | Java |
| GS-11505 | StandaloneProcessingUnitContainerProvider#close() raise Interruption that cause problems in unit tests | 9.7.1, 10.0.0 | 8518 | Java |
| GS-11559 | ESM - Scale undeployment is in progress, stuck in a loop caused by ExpectedMachineWithMoreMemoryException | 9.7.0 | 8586 | Java |
| GS-11532 | NullPointerException in LeaseManager | 9.7.0 | 8555 | All |
| GS-11527 | 'DiscoveredMachineProvisioningConfigurer' is missing the 'reservedMemoryCapacityPerManagementMachine' method | 9.7.0 |  | Java |
| GS-11522 | Durable notification do not arrive when there is a gateway configured | 9.7.0 |  | Java, .Net|
| GS-11491 | Space shutdown may hang while closing Background fifo thread on rare scenarios | 9.7.0 | 8451 | All |
| GS-11490 | GSC failed to create a JMX server and listen on a port | 9.7.0 | 8496 | All |
| GS-11489 | Wrong display of Machine SLA Enforcment event | 9.7.0 |  | All |
| GS-11487 | Local View which fail to initialize due to MSE can cause replication backlog accumulation in the space | 9.7.0 | 8475 | All |
| GS-11486 | Fix GigaMap id registration | 9.7.0 |  | Java |
| GS-11469 | Change operation and central data base, causes a problem when sending a durable notifications | 9.7.0 | | All |
| GS-11462 | Restart the ESM if it cannot detect any lookup service | 9.7.0 | 8448 | Java |
| GS-11452 | NPE replicating change operation in rare conditions | 9.7.0 | 8439 | Java, .Net|
| GS-11451 | ESM mistakenly kills containers not registered with LUS, when it failed to start a container on that machine | 9.7.0 | | Java |
| GS-11445 | ESM failing to shutdown after the GSA has been stopped | 9.7.0 | 8406 | Java |
| GS-11442 | ClassCastException may occur in reliable async target if replication backlog is over flown | 9.7.0 | | All |
| GS-11440 | Notify container leaseListener attribute of the @NotifyLease annotation, causes compilation errors | 9.7.0 | | Java |
| GS-11435 | Add missing LRMI monitoring admin API in .Net | 9.7.0 | | .Net |
| GS-11433 | "useLocalCache" in url definition, result in ClassCastException after reconnecting to master space | 9.7.0 | 8385 | All |
| GS-11431 | NPE when overriding a setter method and no getter is available | 9.7.0 | 8349 | Java |
| GS-11419 | Cassandra Space Synchronization Endpoint - writing space documents with id autogenerate=true might cause data corruption | 9.7.0 | | Java |
| GS-11415 | Hide news feed button if not available | 9.7.0 | | All |
| GS-11414 | ESM may not start if failed communicating with management GSA | 9.7.0 | | Java |
| GS-11411 | XSD 9.6 schemas are not provided - require internet  | 9.7.0 | 8342 | All |
| GS-11402 | When a cancel/renew multiple leases operation fails, the exceptions are ignored | 9.7.0 | | All |
| GS-11397 | Misleading error when specifying empty locator in url | 9.7.0 | 8333 | All |
| GS-11395 | Local view properties ignored | 9.7.0 | | All |
| GS-11394 | Creating a compound index allowed from SpaceTypeDescriptorBuilder with type=extended | 9.7.0 | | Java |
| GS-11391 | ESM service is missing an icon in Gs-ui | 9.7.0 | | All |
| GS-11388 | NPE appears in logs when performing change operation with SQLQuery on local cache | 9.7.0 | | Java, .Net |
| GS-11383 | Upgrade Spring Security 3.1.4 | 9.7.0 | | Java |
| GS-11382 | System parameter com.gs.security.fs.file-service.file-path ignored by ui | 9.7.0 | 8309 | All |
| GS-11380 | @EventTemplate annotation not respected when using @Archive container annotation | 9.7.0 | | Java |
| GS-11375 | High memory consumption when using large objects and light serialization | 9.7.0 | 8280 | Java |
| GS-11374 | A space with EDS and mirror invokes a redundant, call to the EDS iterator/enumerator with java.lang.Object/System.Object upon creation. | 9.7.0 | 8259 | Java, .Net |
| GS-11368 | Serialization error occurring when writing to swap replication redolog, could cause deadlock in the space | 9.7.0 | 8304 | All |
| GS-11365 | Updating entry twice under the same transaction when object was written with lease, might cause to memory leak | 9.7.0 | 8294 | All |
| GS-11357 | Increment by zero in change operation, causes to UnMarshallingException | 9.7.0 | | Java, .Net |
| GS-11355 | "Insufficient Data In Class" might appear in log while starting local view after failovers | 9.7.0 | 8282 | All |
| GS-11345 | XAP pu.xml should use current xml schema | 9.7.0 | | Java |
| GS-11339 | Blocked update may throw wrong kind of exception or stay blocked in case of an internal error | 9.7.0 | | All |
| GS-11331 | NPE is thrown when trying to perform admin operation with non-secured admin on secured grid | 9.7.0 | | Java, .Net |
| GS-11317 | Typo in class name UniqueConstraintViolationException | 9.7.0 | | Java |
| GS-11315 | FifoGroups is not working with inheritance when the fifo groups annotation is configured on the son class | 9.7.0 | | Java, .Net |
| GS-11312 | Last replicated packet is kept on thread local cache which could hold up significant memory | 9.7.0 | 8237 | All |
| GS-11305 | ArrayIndexOutOfBoundsException may be thrown when working with more than one class defining FIFO Group in rare condition | 9.7.0 | 8243 | Java, .Net |
| GS-11303 | Projections is not working with a space iterator | 9.7.0 | 8247 | Java, .Net |
| GS-11280 | Problem presenting log in ui and admin logging API, serialization error appears when log line is too long | 9.7.0 | 8223 | All |
| GS-11266 | NHibernate practice binaries should not be packaged in the msi | 9.7.0 | | .Net |
| GS-11260 | Durable notification and local view may receive notification which does not match its template of operations, under distributed transaction | 9.7.0 | 8159 | Java, .Net |
| GS-11254 | On some scenarios, GSM may go down when GSC gets OutOfMemoryError | 9.7.0 | | All |
| GS-11251 | ESM fails to start GSC when one out of two LUS is down | 9.7.0 | | Java |
| GS-11239 | Eager Elastic PU does not self-heal when it is deployed right before second GSM starts | 9.7.0 | 8189 | Java |
| GS-11233 | Projection on entry with auto generate id true doesn't work | 9.7.0 | | Java, .Net |
| GS-11231 | Space mode change annotations are not working when a space proxy is created in the ContainerInitializing method | 9.7.0 | | .Net |
| GS-11227 | XAP.Net CLI Windows Service is broken - missing gs_cli.config file | 9.7.0 | | .Net |
| GS-11226 | When adding a dynamic gateway target, the backup space will throw an exception in the logs rejecting this addition | 9.7.0 | | Java, .Net |
| GS-11223 | Data replication throughput at Dashboard is not displayed as whole number | 9.7.0 | | All |
| GS-11207 | Exception is thrown when query projected result does not include primitive properties without null values | 9.7.0 | | Java,.Net |
| GS-11191 | .Net ReadMultiple with bad query result in class cast exception | 9.7.0 | 8122 | .Net |
| GS-11186 | NPE is thrown when performing SQLQuery with template and projection on a primitive field and this filed is not part of the projection | 9.7.0 | | Java, .Net |
| GS-11175 | ESM stops working when it cannot discover itself | 9.7.0 | | Java |
| GS-11148 | TargetException:"Object does not match target type" when using Execution based remoting with two interfaces with the same method definition | 9.7.0 | 8056 | Java |
| GS-11142 | ChangeException in .Net is not marked as serializable | 9.7.0 | 8046 | .Net |
| GS-11139 | GigaSpaces was holding file handlers to deleted files | 9.7.0 | 7944 | All |
| GS-11108 | Error in startGroovy.bat file when trying to use Java 6 style wildcard for jars to be added to classpath | 9.7.0 | 8007 | Java |
| GS-11069 | Add the ability to set OpenSpacesMuleMessageReceiver max batch size on os-queue:connector | 9.7.0 | 7943 | Java |
