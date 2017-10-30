---
type: post
title:  Upgrading from previous versions
categories: RELEASE_NOTES
parent: xap97.html
weight: 800
---

{{% ssummary %}}This page contains information about changes in this release which can affect upgrading from previous versions.{{% /ssummary %}}

# Changes in Behavior / Syntax 

## New Protective Modes

We at GigaSpaces occasionally pick up bad usage patterns which lead to common user errors, and try to improve the product to prevent it. Sometimes a plain validation is too harsh, as it might break backwards compatibility and prevent existing customers from upgrading to the latest version. In such cases we may create a *Protective Mode*, which means that the validation is on by default, but can be disabled using a system property. This protects new customers from repeating old mistakes, and encourages existing customers to fix their code (yet allows them to disable the protection if they choose so).

### Protective Mode: Type Without Space ID

Writing an entry to the space without a [space ID]({{%latestjavaurl%}}/query-by-id.html) is error-prone - it can lead to duplicate entries, bad performance and more. Starting 9.7 a new protective mode has been added to protect against writing such entries. If your application contains such types, it is highly recommended that you modify them and add a space ID. If this is not feasible, this protective mode can be disabled using the `com.gs.protectiveMode.typeWithoutId` system property.

### Protective Mode: Template with Primitive Properties Without Null Values

When querying the space using [template matching]({{%latestjavaurl%}}/query-template-matching.html), null properties are ignored and non-null properties are matched. Since primitive properties cannot be set to null, a `nullValue` can be assigned to a property to indicate a value which will be treated as null when using template matching. Starting 9.7 a new protective mode has been added to protect against querying with a template which contains one or more primitive properties without a `nullValue`, since such templates are likely to produce unexpected results. If your application uses template matching with such types, it is highly recommended that you define `nullValue` where appropriate, or switch to [SQLQuery]({{%latestjavaurl%}}/query-sql.html) instead. If this is not feasible, this protective mode can be disabled using the `com.gs.protectiveMode.primitiveWithoutNullValue` system property.


{{%note%}}
More information can be found in the [Admin Guide](/xap/9.7/admin/troubleshooting-protective-modes.html)
{{%/note%}}

## Data Events

### Multiplex vs. Unicast

Multiplex [data event session]({{%latestjavaurl%}}/session-based-messaging-api.html) have been enhanced, and there's currently no known reason to choose `unicast` over `multiplex`. Hence, the default communication type has been changed to `multiplex`, and `unicast` has been deprecated. This should not cause any problems for existing applications (you might even see improvements if you're using many event listeners / notify containers). If you do encounter problems, and find out that switching to unicast solves the problem, please contact support. 
 
### Auto Renew Enhancements and leased notifications

Up to now `multiplex` only affected the event listener, and event registrations renewals were done in a unicast fashion. When using this approach users experienced problems when using many event listeners / containers. Starting 9.7 improvements have been done when using multiplex data event session to perform all registrations renewals in batches, reducing both the number of threads and network traffic. These changes affect only `multiplex` (which is now the default) and not `unicast`. In addition, these changes no longer use the old auto-renew configuration - if those settings are detected multiplex will fall back to the old auto-renew mechanism. Applications using auto renew should be tested to verify there are no regressions, as this is a new mechanism. If you do encounter problems, and find out that switching to unicast solves the problem, please contact support. 

### Event Listeners with Custom Lease

Event listeners / Notify Containers with custom lease are deprecated and should not be used. When using `multiplex` (default) the lease is ignored and a warning is printed. When using `unicast` the lease is honored to allow backward compatibility fallback.   

# Simplified Structure

## XAP for Java

The `bin` folder now contains only the common scripts required to use the product: setenv, cli, ui/webui, and gs-agent. The rest of the scripts (which are rarely used, if at all) are still available in `advanced_scripts.zip` in the `bin` folder. For more information see [scripts](/xap/9.7/admin/scripts.html).

## XAP for .NET

The following obsolete tools and services have been removed from the product

### Removed Tools
A couple of obsolete tools have been removed from the `bin` folder: 

* `DistributedTransactionManager.exe` - Standalone distributed transactions manager. Embedded distributed transactions should be used instead. 
* `puinstance.exe` - Standalone processing unit host. Processing units should be deployed to the service grid or debugged using the integrated processing unit container.

### Removed Services

The Windows Service wrappers for `GSC`, `GSM` and `LUS` have been removed from the `config` folder - the `Agent` service wrapper includes these functionality and should be used instead.

In addition, the `CLI` Windows service wrapper which was used as a platform to run scripts on startup has been removed - this functionality is available as part of Windows Operating Systems (see [Assign Computer Startup Scripts](http://technet.microsoft.com/en-us/library/cc770556.aspx) on [technet](http://technet.microsoft.com)).
