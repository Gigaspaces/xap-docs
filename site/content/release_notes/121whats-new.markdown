---
type: post
title:  What's New
categories: RELEASE_NOTES
parent: xap121.html
weight: 100
---

This page lists the main new features in XAP 12.1 (Java and .NET editions).

It's not an exhaustive list of all new features. For a full change log for 12.1 please refer to the [new features](./121new-features.html) and [fixed issues](./121fixed-issues.html) pages.

# Functionality

## Change code without restarts

When executing user code on the space (e.g. space tasks), the space automatically loads the code from the remote client and caches it for future executions. Since the code is cached, modifications are ignored, and users are forced to restart the space whenever they modify their code. 

Starting 12.1, you can use the `@SupportCodeChange` annotation to tell the space your code has changed. For example, start with annotating your task with `@SupportCodeChange(id="1")`, and when the code changes, set the annotation to `@SupportCodeChange(id="2")`, and the space will load the new task.

The space can store multiple versions of the same task side-by-side (ideal for supporting clients using different versions). This feature also applies for other features which execute user-defined code at the space, namely custom change and custom aggregate.

{{<infosign>}} For more information see [Change code without restarts](/xap121/the-space-no-restart.html)

## Customize MemoryXtend cache warmup

MemoryXtend includes a cache to store recently used entries in memory. You can now control this cache warmup using one or more queries to determine which entries should be cached.

{{<infosign>}} For more information see [MemoryXtend Initial Load](/xap121adm/memoryxtend.html#user-defined-query-for-initial-load)

## Full text search

Querying text properties made a great step forward, with full text search capabilities. For example, to find `Email` entries which contain a word similar to `salary` in the title property, use `new SQLQuery(Email.class, "title text:match ?", "salary~1")`.

Full text search is using {{%exurl "Lucene" "https://lucene.apache.org/"%}} under the hood, so you're getting all the power of lucene within your XAP application.

{{<infosign>}} For more information see [Full text search](/xap121/query-full-text-search.html)

## Get Jetty port within your application

Web applications which need to know which port their running on can now get it directly from the context properties.

{{<infosign>}} More information coming soon.

# Administration

## GS-Agent enhancements

GS-Agent has received the following enhancements:

* Help screen: run gs-agent with `--help` or `-h` to get a help screen explaining how to use it.
* Zero Defaults: run gs-agent with `--zero-defaults` or `-z` to disable the default services. e.g. `gs-agent -z gsa.gsc 1' starts a single container, without explicitly disabling the global lus and gsm.

{{<infosign>}} Documentation is under construction - more information coming soon.

## Simplified Replication Configuration for Transactions

Replication configuration is affected by the size of the redolog, measured in replication packets, and various thresholds and policies which dictate system behavior. Since transactions are replicated in a single packet (to ensure atomicity), redolog size was not granular enough, as transactions can include few or many sub-operations. Starting 12.1, replication packets have weight, where simple operations weigh 1 and transactions weigh as the sum of their sub-operations, which makes it easier to configure replication.

{{<infosign>}} Documentation is under construction - more information coming soon.

## Custom Security Support for Web Management Console

The Web Management Console (web-ui) now supports custom security, same as the gs-ui.

{{<infosign>}} Documentation is under construction - more information coming soon.

## SSL Support for Web Management Console

The Web Management Console (web-ui) can now be configured to use SSL.

{{<infosign>}} For more information see [SSL Connection](/xap121adm/web-management-console-starting.html#ssl-connection).

# Troubleshooting

A couple of improvements which can come in handy for troubleshooting:

## Include terminated processes in dump

The dump functionality is useful for gathering log files from system components. However, up to now it only included components which are currently running - if a process has terminated and restarted, only its current log would be collected. Starting 12.1 you can specify if you want to collect terminated processes as well when you perform a dump.

{{<infosign>}} Documentation is under construction - more information coming soon.

## Verbose System Report

Whenever a service grid component is started, it prints a system report in its log file. Starting 12.1, if you increase the log level from `INFO` to `CONFIG` or beyond, this report will be more verbose and include all system properties and environment variables, which can come in handy for troubleshooting some problems.

{{<infosign>}} Documentation is under construction - more information coming soon.
