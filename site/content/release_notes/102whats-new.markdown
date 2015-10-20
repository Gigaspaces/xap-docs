---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap102.html
weight: 100
---

This page lists the main new features in XAP 10.2 (Java and .Net editions).


It's not an exhaustive list of all new features. For a full change log for 10.2 please refer to the [new features](./102new-features.html) and [fixed issues](./102fixed-issues.html) pages.

{{%anchor ssd%}}

# Enhanced SSD driver

When using MemoryXtend with a primary-backup topology, the backup can now recover from is local SSD instead of pulling all the data from the primary over the network, which boosts performance tremendously.

{{<infosign>}} For more information see [MemoryXtend](/xap102adm/memoryxtend-ssd-overview.html)

{{%anchor interscope%}}

# APM Introscope Metrics Reporter

The XAP metrics framework was designed to support custom reporter implementations. In addition to the built-in InfluxDB reporter, we've now added a new reporter to integrate with [CA APM](http://www.ca.com/us/products/application-performance-management.aspx), which is a powerful product widely used in large companies to monitor applications and react quickly when certain performance issues may occur. 

{{<infosign>}} For more information see [CA APM Introscope Reporter](/xap102adm/ca-apm-introscope-reporter.html)

{{%anchor encryption%}}

# Configure the Transport Layer Encryption Protocol

When securing the transport layer, it is now possible to configure which encryption protocol (`TLSv1.1`, `TLSv1.2`, etc.) will be used.

{{<infosign>}} For more information see [Choosing the encryption protocol](/xap102sec/securing-the-transport-layer-using-ssl.html#choosing-the-encryption-protocol.html)
