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

In systems with the MemoryXtend feature, XAP can store the values of indexed fields in off-heap memory, to avoid having to fetch the complete entry from disk for queries that only need the index. This feature optimizes performance for the following operations:

- Read with projection and only indexed fields in query and projection - primary instance optimization
- Take with only indexed fields in query - backup optimization
- Clear with only indexed fields in query - primary and backup instance optimization