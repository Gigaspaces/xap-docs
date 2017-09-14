---
type: post122
title: Concepts
categories: XAP122OVW
parent: none
weight: 200
---

{{%  ssummary %}}{{%  /ssummary %}}

GigaSpaces XAP (pronounced zap) is a scale-out application server. Developers and architects who are looking to develop web-based, transactional applications, with high-throughput, low-latency and scalability, may want to use it to build their next generation class of applications.

Enterprise-level application servers (also known as JEE - formerly J2EE), were last truly designed and deployed in the late 1990's, and their prime focus was to solve the problems encountered in deploying applications for the web. Prior application servers relied on rich application clients for interactivity, which made maintenance and management almost impossible, unless restrictions on platform were used - and if you know of an enterprise that limits its web access to, say, IE6, then you know those restrictions haven't altogether been avoided.

For this reason, these application servers focused mainly on resource management, and provided the means for web application deployment, such as web-containers, EJB containers for business logic processing, and a data-access layer for interaction with external data sources, mainly relational databases.

# Space-Based Architecture

A detailed description of the model and the theory that led to the inception of GigaSpaces XAP as a next-generation development and runtime platform, as the means to achieve scalability in a high-throughput, stateful environment, is provided in the following white paper: [The Scalability Revolution: From Dead End to Open Road](http://www.gigaspaces.com/os_papers.html#scalrev).

In "The Scalability Revolution," we define scalability as the ability to increase an application's working capacity while retaining data and logic consistency, latency, and the application code - and show that inherent scalability barriers represent a dead end for today's tier-based business-critical applications. In order to survive, today's applications must achieve linear scalability, and the only valid way to do so, is to switch from a tier-based model to a new architectural approach - partitioned applications.

We suggest an approach in which applications are partitioned into self-sufficient Processing Units, and present Space-Based Architecture (SBA) as a practical implementation of this approach. We demonstrate that SBA guarantees both linear scalability and simplicity for designers, developers and administrators - transforming scalability from dead end to open road.

The [Space-Based Architecture and the End of Tier-based Computing](http://www.gigaspaces.com/os_papers.html#a1) white paper describes how changes in the IT resource landscape, such as memory capacity, network speed and the emergence of powerful and new multi-core commodity hardware, and the introduction of SOA/Grid architectures, tout the promise of achieving true linearly-scalable systems at a lower cost. It introduces how a Space-Based Architecture (SBA) approach can be used as a means to transform existing tier-based applications into linearly and dynamically scalable services.

GigaSpaces XAP was built to be an implementation of the theory behind these concepts, and to make the development of applications based on this model, **simple.**

# Overcoming Limitations of First-Generation Servers



GigaSpaces XAP and Space-Based Architecture overcome the major limitations found in first-generation application servers. These limitations can be divided into three main categories:

- Complexity
- Built on top of non-scalable architecture that creates bottlenecks
- Not suited for many types of applications

GigaSpaces XAP provides a unique advantage in terms of scalability and performance, in addition to supporting many types of applications. The complexity is solved by providing a holistic view of the application requirements, and by not focusing only on a single aspect of the system.

## Overcoming Complexity

JEE was, and continues to be, highly criticized mainly because of its complexity. This complexity came into play in the areas of complex programming models, complex configuration that relies heavily on XML, and bloated specifications which resulted in heavy-weight and complex application servers.

Recent versions of JEE (Java EE 5 and Java EE 6, which hasn't been finalized yet) are taking the first steps to address this problem through a POJO-based programming model and the use of profiles. However, the development community has not waited for the Java Community Process to keep up with current trends and needs, and alternate programming models and development frameworks have become the standard. The most dominant one is the Spring Framework, which promotes dependency injection, a POJO-based programming model and aspect-oriented programming.

GigaSpaces XAP adopts this style of programming as part of its core OpenSpaces APIs, as this is the path that the community is following - including the new revisions of the Java EE specification, which has added dependency injection and a POJO-based model already, and can be imagined to add aspect-oriented programming soon as well.

Another aspect of complexity, is that JEE and other application servers were designed to solve a set of very similar problems - business logic processing, in addition to web capabilities. Data storage/management and messaging were always defined as a problem of other types of servers. This assumption moves the burden of putting it all together onto the development teams.

GigaSpaces XAP takes a different approach. Space-Based Architecture views the problem from end-to-end, , and provides a complete solution without the need for complex integration within the boundaries of applications and systems.

## Built for Scalability and Performance

GigaSpaces XAP is a memory-based technology at its core. The sheer capability of data storage and computation within the boundaries of a single process (i.e. collocation), without a need for expensive inter-process communications and file-system access, performs far better than alternatives, such as integration between application servers and relational databases.

In addition, built-in data partitioning on top of grid deployment and management capabilities within a single clustering model, enables true linear scalability.

In alternative tier-based architecture the need to use specific server implementation, such as database, application server and messaging server and integrating those into a single system, results in complexity and inability to scale the entire system linearly. The fact that each server has a different scaling and high-availability (clustering) policies, makes it difficult and in many cases impossible to support scaling beyond a certain size.

## A Wider Range of Applications

Prior generations of application servers were built for the web in the days when HTTP was purely request/response. This limited their usage in the cases of:

- Event driven applications, where an event can be state changes or elapsed time.
- Batch Processing and batched real-time processing
- Parallel Processing of tasks, or more complex data-oriented tasks such as Map/Reduce.
- Scalable stateful applications
- Scalable AJAX web applications
- Compute Grid

GigaSpaces XAP supports web-enabled request/response applications, in addition to all the modern computational styles described above, and the combination of all of the above.



# Space - Concepts and Capabilities


The space is the 'secret sauce' behind GigaSpaces XAP architecture, and it is important to understand its capabilities in order to better understand the entire Space-Based Architecture model.

{{%  anchor tuple %}}

## Tuple Space

Tuple Space is a different paradigm for inter-process communication, based on sharing information tuples instead of the alternate message passing paradigm.

Linda language, developed by David Gelernter and Nicholas Carriero at Yale University, was the first implementation of Tuple Spaces. Later on, the [JavaSpaces specification](http://www.sun.com/software/jini/specs/js2_0.pdf) was defined as part of [Sun's Jini specification](http://www.sun.com/software/jini/specs/).

GigaSpaces initial product version was a commercial implementation to the JavaSpaces specification, and the kernel API of the product still implements the JavaSpaces specification.

Over the life-cycle of the product, with additional capabilities added into the space implementation (such as clustering, fail-over and replication), we realized that the distributed shared-memory model is capable of providing many other middleware capabilities, such as data access, messaging and processing and at the same time keeping a single underlying implementation model - the space.

This section describes the capabilities of the space, and how a combination of these capabilities results in a simple and sound runtime model for the GigaSpaces XAP application server.

## Space

A Space is a logical in-memory service, which can store entries of information. `Entry` is a domain object. In Java, entry can be as simple as a Java POJO.

## Space - Basic Concepts

The space is accessed via a programmatic interface which supports the following main verbs:

- **Write** -- the semantics of writing a new entry of information into the space.
- **Read** -- read the contents of a stored entry into the client side.
- **Take** -- get the value from the space and delete its content.
- **Notify** -- alert when the contents of an entry of interest have registered changes.

The power of the space model is that the combination of the various basic APIs creates a very powerful set of interaction semantics:

- **Write + Read** -- This combination creates caching semantics. As entries are stored within the application, memory storing and retrieving of entries is done rapidly, making space technology perfect for caching solutions.
- **Write + Take** -- This combination is perfect for parallel processing paradigms. By having a single (or few) writer/s who write tasks into a shared space, and multiple consumers who take entries for execution from the shared location, it is very easy to create a parallel processing application. In fact, the Master/Worker pattern was invented originally in the context of Tuple Spaces, the origins behind JavaSpaces.
- **Write/Take + Notify** -- This is a messaging paradigm. Clients are informed of information changes asynchronously once these occur, and in fact, the JMS implementation on top of the Space API, which is provided as part of GigaSpaces XAP, does exactly that.

These capabilities are at the core of GigaSpaces XAP. This combination, plus the additional capabilities  of the basic APIs, allow synchronous and asynchronous applications to be built as the models for which first generation application servers apply. In addition, these models can be used in a wider range of applications, such as: real-time analytics, real-time batch processing, complex event-driven applications, caching scenarios, parallel processing and more.

{{%  tip "Template Matching" %}}
Because the space stores entries in the form of objects, retrieval of information is done using template matching. The application creates a template object -- the class of the template object defines which type of objects the application wants to read (for example, a template of class `Message` will return `Message` objects), and the template object's properties are used as retrieval criteria. If a template property has a value, objects are only retrieved if they have the same value for that property. If properties in the template are null, they are ignored.
GigaSpaces XAP extends template matching by providing semantics to query ranges on information as well. For additional information, please refer to the [SQLQuery API]({{% latestjavaurl%}}/query-sql.html) in the Programmer's Guide.
{{%  /tip %}}



# Clustering and Topologies

The space as defined previously, is a logical concept - a memory space which can contain entries of information. The actual space implementation can vary. Multiple space instances connected via a defined relationship (clustering topology), form a **cluster**, and for external clients, a cluster can be seen as a single "large" space. GigaSpaces XAP provides multiple clustering topologies, and XAP users define the cluster topology during system design and deployment.

The three main cluster topologies are:

- [Replication](#replication)
- [Partitioned](#1)
- [Resilient Partitioning](#resilient-partitioning): a combination of Replication and Partitioned.

{{% anchor  Replication %}}

## Replication

When two or more spaces share their entire content with one another, these spaces are fully replicated. When an entry is added into one of them, it is immediately replicated to its replica. Replication is useful to preserve high-availability of the data instances. In case one of the space instances dies, another one contains the entire information set.

Full replication, where all the spaces of a cluster are a replica of one another, provides the highest availability of information. However there is a limit to the amount of information which can be stored in the cluster. The sum of information is limited according to the memory capacity of a single node.

#### Synchronous Replication

GigaSpaces XAP provides various levels of replication QoS. The highest quality is synchronous replication (sync replication). In sync replication, any client writing into the space is blocked until the information has been replicated into its replicated space(s).

#### Asynchronous Replication

The asynchronous replication (async replication) strategy returns the call back to the client, and at the same time replicates the information into the other replica. Async replication may lose information in the case of a failure within the source space while the information has not yet been delivered to its replica. However, it does provide better performance for clients.

{{%  info %}}
The replication QoS should be considered, based on the application requirements. The default replication policy is synchronous replication.
{{% /info%}}

{{% anchor  1 %}}

## Partitioned

Spaces form a 'partitioned cluster' when there is a need to store, in-memory, larger amounts of data than can be stored in a single space process memory. Data is divided into partitions, and each space contains a subset of the information. A combination of all the spaces within the same cluster, creates the full set of information.

For a remote client using a proxy to the cluster (clustered proxy), the division into partitions is transparent.

Information is divided into partitions when it is accessed into the cluster, using a hash-based routing policy.

For example, when writing one million orders into a space, the orders are (and should be) divided across the partitions in the cluster. The routing that specifies which order instance should be stored in which partition, is decided based on a property of the order itself. The application developer provides hints to the clustered proxy by suggesting a particular property within the entry. The clustered proxy decides, based on the hash-code of the property module, the size of the cluster.

{{%  refer %}}
For details about scaling a running space cluster **in runtime** see the [Elastic Processing Unit]({{% latestjavaurl%}}/elastic-processing-unit.html) section.
{{%  /refer %}}

 

## Resilient Partitioning

Since each partition contains only a subset of the data, losing a partition can result in information being lost if that partition is the only place that the data exists. For this reason, a combination of partitioning and replication provides both scalability and resiliency.

The common topology is of a partitioned cluster, where each partition member has one or more replicas. With this topology, there is no scalability bottleneck in the amount of data that can be stored in the cluster, and each partition is fault-tolerant as it has a backup replica.

{{%  info %}}
This topology is also known in GigaSpaces XAP as Partitioned-Sync2Backup.
{{% /info%}}



