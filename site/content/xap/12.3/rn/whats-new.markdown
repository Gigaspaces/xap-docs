---
type: post123
title:  What's New
categories: XAP123RN
parent: none
weight: 100
---

This section describes product changes, along with new features and functionality for the early access version of InsightEdge Platform release 12.3.

Draft documentation is available, and may not completely describe the product updates as it is still in progress. For additional details about the available features and enhancements in this version, refer to the  [Early Access](/early_access/index.html) description.

# Performance Improvement

In systems with MemoryXtend, XAP can store the values of indexed fields in off-heap memory, to avoid having to fetch data from disk for queries that only need the index. This feature can optimize performance by up to 50% for the following operations:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization

# Tiered Storage Enhancement

The custom initial load mechanism in MemoryXtend has been extended to provide full life cycle management of the blobstore cache, so that users can prioritize data availability throughout the application lifetime. Users can define a set of queries that define what data should be cached as hot data in the in-memory data grid, while cold data continues to be stored on disk.
