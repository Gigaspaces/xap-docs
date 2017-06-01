---
type: post102
title:  Advanced
categories: XAP102NET
parent: change-api-overview.html
weight: 200
---


# Obtaining Change detailed results

By default, the change result will only contain the number of entries which were changed during the operation. In order to get more details (requires more network traffic) the ChangeModifiers.ReturnDetailedResults should be used. When using this modifier the result will contain the list of entries which were changed including the change affect that took place on each entry.
You can use this in order to know what was the affect, for instance what is the value of a numeric property after the increment operation was applied on it.


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;
IdQuery<Account> idQuery = new IdQuery<Account>(id, routing);
IChangeResult<Account> changeResult = space.Change(idQuery, new ChangeSet().Increment("balance.euro", 5.2D), ChangeModifiers.ReturnDetailedResults);

foreach(IChangedEntryDetails<Account> changedEntryDetails in changeResult.Results) {
  //Will get the first change which was applied to an entry, in our case we did only single increment so we will have only one change operation.
  //The order is corresponding to the order of operation applied on the `ChangeSet`.
  IChangeOperationResult operationResult = changedEntryDetails.ChangeOperationsResults[0];
  double newValue = IncrementOperation.GetNewValue(operationResult);
  ...
}
```

Here is the full list of change operations:


|ChangeSet operation|ChangeOperation class|Comment|
|:------------------|:--------------------|:------|
|**ChangeSet.Set**|SetOperation| |
|**ChangeSet.Unset**|UnsetOperation| |
|**ChangeSet.Increment**|IncrementOperation| |
|**ChangeSet.Decrement**|IncrementOperation|will be increment with negative value|
|**ChangeSet.AddToCollection**|AddToCollectionOperation| |
|**ChangeSet.AddRangeToCollection**|AddRangeToCollectionOperation| |
|**ChangeSet.RemoveFromCollection**|RemoveFromCollectionOperation| |
|**ChangeSet.SetInDictionary**|SetInDictionaryOperation| |
|**ChangeSet.RemoveFromDictionary**|RemoveFromDictionaryOperation| |



# Add and Get operation

A common usage pattern is to increment a numeric property of a specific entry and needing the updated value after the increment was applied.
Using the `AddAndGet` operation you can do that using one method call and get an atomic add and get operation semantics.
Following is an example of incrementing a property named `Counter` inside an entry of type `WordCount`:


```csharp
ISpaceProxy space = // ... obtain a space reference
Guid id = ...;

IdQuery<WordCount> idQuery = new IdQuery<WordCount>(id, routing);
int? newCounter = ISpaceProxy.AddAndGet(idQuery, "Counter", 1);
```

{{% note %}}
You should use the primitive wrapper types as the operation semantic is to return null if there is no object matching the provided id query
{{%/note%}}

{{% info %}}
Add `using ISpaceProxy.Core.Change.Extensions;` in order to have the extension methods available.
{{% /info %}}

