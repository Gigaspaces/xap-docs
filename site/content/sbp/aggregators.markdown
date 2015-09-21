---
type: post
title:  XAP.NET Custom Aggregators
categories: SBP
parent: processing.html
weight: 400
---

{{% ssummary  %}}  {{% /ssummary %}}

{{%section%}}
{{%column width="60%" %}}
With many systems such as pricing systems, risk management, trading and other analytic and business intelligence applications you may need to perform an aggregation activity across data stored within the data grid when generating reports or when running some business process. Such activity can leverage data stored in memory and will be much faster than performing it with a database.
{{%/column%}}
{{%column width="40%" %}}
![aggreg.jpg](/attachment_files/aggreg.jpg)
{{%/column%}}
{{%/section%}}

This best practice demonstrate how to calculate Min, Max, Avg and Sum for objects stored within the space. There is no need to retrieve the entire data set from the space to the client side , iterate the result set and perform the aggregation. This would be an expensive activity as it might return a large amount of data into the client application.

Custom Aggregators allows you to perform the entire aggregation activity at the space side avoiding any data retrieval back to the client side. Only the result of each aggregation activity performed with each partition is returned back to the client side where all the results are reduced and returned to the client application. Such aggregation activity utilizes the partitioned nature of the data-grid allowing each partition to execute the aggregation with its local data in parallel, where all the partitions intermediate results are fully aggregated at the client side using the relevant reducer implementation.

# Pre-defined Aggregators

The following pre-defined aggregators are available:


|Task|Description|
|:---|:----------|
|[AverageTask](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Tasks/AverageTask.cs)|An average operation to be performed on a specific partition in the space.|
|[MaximumTask](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Tasks/MaximumTask.cs)|A maximum aggregation operation to be performed on a specific partition in the space.
|[MininimumTask](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Tasks/MinimumTask.cs)|A minimum aggregation operation to be performed on a specific partition in the space.|
|[SumSpacePropertyTask](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Tasks/SumSpacePropertyTask.cs)|A sum performed on a space property in a specific partition in the space.|
|[SumSpaceObjectTask](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Tasks/SumSpaceObjectTask.cs)|A sum performed on a space object in a specific partition in the space.|

The following pre-defined distributed aggregators are available:


|Task|Description|
|:---|:----------|
|[AverageReducer](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Reducers/AverageReducer.cs)|An average operation to be performed on the space.|
|[MaximumReducer](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Reducers/MaximumReducer.cs)|A maximum aggregation operation to be performed on the space.|
|[MinimumReducer](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Reducers/MinimumReducer.cs)|A minimum aggregation operation to be performed on the space.|
|[SumSpacePropertyReducer](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Reducers/SumSpacePropertyReducer.cs)|A sum performed on a space property in the space.|
|[SumSpaceObjectReducer](https://github.com/GigaSpaces-ProfessionalServices/dotnet-aggregators/blob/master/src/GigaSpaces.Core.Executors/Reducers/SumSpaceObjectReducer.cs)|A sum performed on a space object in the space.|

# Example

Below is an example using the `AverageReducer` to find the average salary of all employees in the space. 

{{%tabs%}}
{{%tab "  Application "%}}

```c#
ISpaceProxy spaceProxy = ...;
IDistributedSpaceTask<long,long> averageTask = new AverageReducer<Employee, int>(t => t.Salary);
var averageSalary = spaceProxy.Execute(averageTask);
```
{{% /tab%}}

{{%tab "  Space Class "%}}

```c#
[SpaceClass]
public class Employee {
	[SpaceID(AutoGenerate=false)
	public int Id { get; set; }
	
	public int Salary {get;set; }
}
```
{{% /tab%}}
{{%/tabs%}}

