---
type: post
title:  Upgrading from previous versions
categories: RELEASE_NOTES
parent: xap100.html
weight: 800
---

{{% ssummary %}}This page contains information about changes in this release which can affect upgrading from previous versions.{{% /ssummary %}}

# Changes in Behavior / Syntax 

## Externalizable

GigaSpaces XAP uses a proprietary serialization mechanism to transfer data efficiently. In previous versions, using a POJO which implements Java's [Externalizable](http://docs.oracle.com/javase/7/docs/api/java/io/Externalizable.html) 
forced XAP to use the user-defined `Externalizable` serialization instead.

As time passed, XAP's built-in serialization has been improved and fine-tuned, and experience from support cases shows that user-defined `Externalizable` implementations are often less efficient, in comparison, or provide nearly-equivalent performance.
On the other hand, maintaining `Externalizable` support is slowing down the development of new features. As a result, we've deprecated this behaviour when we released *XAP 9.0*

It's now time to take the next step in the End-Of-Life cycle: starting XAP 10.0, the *default behaviour* is to ignore `Externalizable` and serialize `Externalizable` POJOs same as plain ones. 

If you're upgrading from a previous version and are using `Externalizable` POJOs, you'll probably want to verify that there's no performance penalty as a result of that change. If you do encounter performance problems, you can set 
the system property `com.gs.transport_protocol.lrmi.serialize-using-externalizable` to `true` to revert to the previous behaviour and test if the performance issue is resolved. Please note that this system property it only meant
to aid for investigating these issues and will be removed in future versions, when `Externalizable` support will reach end of life.

## .NET 2.0 support

In previous versions we've released XAP.NET for .NET 2.0 and 4.0. Since the vast majority of our customers have already moved to .NET 3.5 (or later), and supporting .NET 2.0 is becoming harder (e.g. complicates our LINQ support), 
we've decided to release XAP.NET 10.0 for .NET 3.5 instead of 2.0, assuming most users have installed 3.5 anyhow (the latest service pack for 2.0 is actually 3.5 SP1).

If you're upgrading from a previous XAP.NET version and cannot upgrade to .NET 4.0, we suggest you upgrade to .NET 3.5. If that's not an option, please contact support for assistance.

## Removed APIs
The following APIs have been deprecated in previous versions and removed in 10.0: 

* `IServerAdmin.GetTypeDescriptor`
* `ISpaceProxy.CreateLocalView` 
* `NoWriteLease`
* `SpaceClassAttribute.Fifo`
* `data event sessions with transactions`
* `local transaction manager in XAP.NET`
* `non-generic SQLQuery in XAP.NET`
* `Clean operation in .NET`
* `Shutting down a space from a remote proxy`
* `IRemoteJSpaceAdmin.start/stop/restart() operations`

