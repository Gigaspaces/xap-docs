---
type: post
title:  Custom Aggregators
categories: SBP
parent: data-access-patterns.html
weight: 20
---

{{% ssummary  %}}  {{% /ssummary %}}

Aggregators provided by the core XAP platform are extensible, allowing developers to modify existing functionality as well as add new features. This flexibility allows the framework to grow with changing requirements and new technologies.  Creating a new aggregator or extending an existing one is a small level of effort for the developer.  This document will describe different patterns to write custom aggregators and some common use cases.

# SpaceEntriesAggregator

The [SpaceEntriesAggregator](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease%}}/com/gigaspaces/query/aggregators/SpaceEntriesAggregator.html) is an abstract class that serves as the base class for all aggregators, including the ones provided by the core platform.  Extending this class will provide several methods to allow the user to easily implement a custom aggregator.  The methods are as follows:

### [aggregate](http://www.gigaspaces.com/docs/JavaDoc{{%latestxaprelease%}}/com/gigaspaces/query/aggregators/SpaceEntriesAggregator.html#aggregate-com.gigaspaces.query.aggregators.SpaceEntriesAggregatorContext-)
The aggregate method is executed for each space entity matching the SQLQuery in a space partition.  The function receives a SpaceEntriesAggregatorContext, which is a wrapper that allows the user to access members of the user entity. The members of each space entity can be accessed by the getPathValue method of SpaceEntriesAggregatorContext.

Long departmentId = (Long) context.getPathValue("depar
