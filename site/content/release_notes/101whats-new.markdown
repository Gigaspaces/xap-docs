---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap101.html
weight: 100
---

This page lists the main new features in XAP 10.1 (Java and .Net editions). It's not an exhaustive list of all new features. For a full change log for 10.1 please refer to the [new features](./101new-features.html) and [fixed issues](./101fixed-issues.html) pages.


{{%panel%}}

- [Metrics](#metrics)

- [Sequence Number](#snumber)

- [Support for Java 8](#java8)

- [New Space Iterator](#paging)

- [Off Heap BlobStore](#blobstore)

- [Quiesce Mode](#quiesce)

- [Web Management Console](#webui)



REST

- [REST API](#rest1)

- [Deploy REST API via CLI](#rest2)


<br>

Third Party library upgrades

- [Hibernate  4.1 support](#hibernate)

- [Spring  4.1 support](#spring)

{{%/panel%}}




{{%anchor hibernate%}}


{{%anchor metric%}}

# Metrics

XAP provides a framework for collecting and reporting metrics from the distributed runtime environment into a metric repository of your choice, which can then be analysed and used to identity trends in the system behaviour.

{{%learn "/xap/10.1/admin/metrics-overview.html"%}}


{{%anchor snumber%}}

# Sequence number

A sequence number (like a data-base sequence-number/autoincrement column) is a property that is given a unique incrementing value when the entry is written to the Space

{{%learn "/xap/10.1/dev-java/pojo-attribute-annotations.html#space-sequence-number"%}}


{{%anchor rest1%}}

# REST API

The REST service is a Processing Unit that once it is deployed it starts an embedded jetty server along with a REST service allowing interactions with the Space via the REST API.

{{%learn "/xap/10.1/dev-java/rest-service-overview.html"%}}


{{%anchor rest2%}}

# Deploy REST API via CLI
XAP provides an interactive command line tool as part of the product.

{{%learn "/xap/10.1/admin/rest-deploy-command-line-interface.html"%}}


{{%anchor java8%}}

# Support for Java 8

The Space API supports the following new Java classes

- java.time.LocalDate;
- java.time.LocalDateTime;
- java.time.LocalTime;

{{%learn "/xap/10.1/dev-java/query-sql.html#java-8-dates"%}}

{{%anchor paging%}}

# New Space Iterator

The new Space iterator which is intended to replace the old GSIterator starting 10.1.

{{%learn "/xap/10.1/dev-java/query-paging-support.html"%}}

{{%anchor blobstore%}}

# Off Heap Blobstore

XAP is using [MapDB](http://www.mapdb.org/), an embedded database engine which provides concurrent Maps, Sets and Queues backed by disk storage or off-heap memory.

{{%learn "/xap/10.1/admin/memoryxtend-ohr.html"%}}


{{% anchor quiesce%}}

# Quiesce Mode

Provides the ability to set an XAP processing unit into quiesce mode (maintenance mode).

{{%learn "/xap/10.1/admin/quiescemode.html"%}}


{{% anchor webui%}}

# Web Management Console

New and improved Web Management Console

{{%learn "/xap/10.1/admin/web-management-monitoring.html"%}}


# Third Party library updates

### Hibernate 4.1 support

This release supports the Hibernate framework 4.1

{{%anchor spring%}}

### Spring 4.1 support

This release supports the Spring framework 4.1
