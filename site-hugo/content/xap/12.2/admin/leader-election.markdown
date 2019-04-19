---
type: post122
title:  Leader Election
categories: XAP122ADM, PRM
parent: data-grid-clustering.html
weight: 700
canonical: auto
---

# Overview

This section describes the Space's leader election mechanism, which is based on the CAP theorem. It also explains how to configure and optimize it. 


# CAP Theorem

The CAP theorem, also known as Brewer's theorem, states that it is impossible for a distributed computer system to simultaneously provide all three of the following guarantees:

* Consistency (all nodes see the same data at the same time)

* Availability (node failures do not prevent survivors from continuing to operate)

* Partition Tolerance (the system continues to operate despite arbitrary message loss)

{{%align center%}}
![image](/attachment_files/cap.png)
{{%/align%}}

As a result, you can have partition tolernace plus consistency, or partition tolerance plus availability. Gigaspaces provides two available implementations to support the approach that is best suited to your environment and business needs:

- [Consistency biased](./leader-election-consistency-biased.html) - this implementation is based on Apache Zookeeper.

- [Availability biased](./leader-election-availability-biased.html) - this implementation is based on the native Lookup Service.
















