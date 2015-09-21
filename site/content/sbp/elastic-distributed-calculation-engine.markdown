---
type: post
title:  Elastic Distributed Calculation Engine
categories: SBP
parent: solutions.html
weight: 100
---



{{% ssummary %}} {{% /ssummary %}}

{{% tip %}}
**Summary:**  Elastic Distributed Calculation Engine implementation using Map-Reduce approach. <br/>
**Author**: Shay Hassidim, Deputy CTO, GigaSpaces<br/>
**Recently tested with GigaSpaces version**: XAP 8.0.3<br/>
**Last Update:** Sep 2011<br/>
**Contents:**<br/>


{{% /tip %}}

# Overview

{{% section %}}

{{% column width="50%"%}}

Financial services, Healthcare, Transportations,Fraud Detection, Payment systems,etc. produce reports constantly. Some of them produce reports where the data required for the report is generated over night via batch processing, and some other type of reports are produced instantly upon user request. This type of processing activity requires fast access to the raw data and the ability to utilize distributed resources available on the local environment or on the cloud.

The Elastic Calculation Engine example illustrates the following:

1. Calculating **Net present value** ([NPV](http://en.wikipedia.org/wiki/Net_present_value)) for large amounts of trades in real time using an In-Memory Data Grid.
2. Intelligent Map-Reduce directing calculations into distributed nodes, lowering the network traffic and lowering the load on each calculation node.
3. Simulating lazy data load in a batch mode optimizing database access in case of a cache miss.
4. While the calculation is going on, dynamically scaling up and down the compute/data grid. This will increase the capacity of the compute/data grid and will allow it to utilize additional CPU resources to speed up the calculation time.

{{% /column %}}

{{% column width="50%"%}}

![risk_anal.jpg](/attachment_files/sbp/risk_anal.jpg)

{{% /column %}}

{{% /section %}}

The Distributed Calcualtion Engine performs Net Present Value calculations where the Trades used for the calculation divided into several Books. These books could represent different types of Trades, different markets, different customers , etc.

# Colocated vs. Remote Calculations
Calculations can be deployed colocated with the data or separately.


|Functionality|Colocated Calculations|Remote Calculations|
|:------------|:--------------------:|:-----------------:|
|Calculation impact on data grid node CPU utilization|Yes. Controlled. |No|
|Calculation impact on Data Grid node memory utilization|Yes. Controlled. |No|
|"Bad Calculation" may cause Data-Grid instability|Yes. Each node recovers independently|No|
|Data access overhead|Low|High. May be optimized via batch operations|
|Scaling flexibility|Data-Grid and Calculations scale in 1:1 ratio|Data-Grid and Calculations scales independently|
|Transactional|Yes|Yes|
|Persistency Support  (write behind)|Yes|Yes|
|Hot deploy (refresh node code dynamically without downtime)|Yes|Yes|
|Calculation splitting support|Yes. Intelligent routing support.|Yes|
|Recommended with|Lightweight calculations. No IO|Heavyweight calculations. CPU / IO bound|

# Colocated Calculations

## The Calculating Flow

The Calculating Flow includes the following:

- A client, splitting a list of Trade IDs into multiple batches. Each Batch is sent into the calculation node (space partition) via a [AnalysisTask](#The AnalysisTask) that implements the [Task Interface]({{%latestjavaurl%}}/task-execution-over-the-space.html). Each calculation node stores a subset of the Trade data.
- [The AnalysisTask](#The AnalysisTask) is executed. Once completed, an intermediate result is sent back to the client. If the requested Trade cannot be found within the space, it is loaded from the database.
-  The client aggregating the results retrieved from all the calculations nodes and reducing it to four numbers. These four numbers represent books.

![ElasticDistributedRiskAnalysisEngine_colocated_workers.jpg](/attachment_files/sbp/ElasticDistributedRiskAnalysisEngine_colocated_workers.jpg)

{{% exclamation %}} When running the Elastic Calcualtion Engine on a single machine, scaling up and down will not affect the calculation time, but when running this on a grid with multiple machines, you will see better or worse calculation time when the grid scales up or down.

## Intelligent Map-Reduce
The Elastic Calcualtion Engine uses the [ExecutorBuilder]({{%latestjavaurl%}}/task-execution-over-the-space.html#executorbuilder-api). This allows executing multiple `AnalysisTasks` in a parallel manner where each Task includes a different Trade ID list to use for the calculation. Each List is sent to a relevant node where it is used to fetch the Trade data from the colocated space or to be loaded from the database.

## The AnalysisTask
The `AnalysisTask` include the following:

- `execute` method - invoked on each partition. It returns a sum of all of the calculated NPV values for all the trades found within the partition divided by books. The demo assumes there are four books.
- `getTradesFromDB` method  - used to load missing Trade objects from the database. Since this demo does not include a running live database the Trade  data is generated via random data.
- `calculateNPV` method - called by the execute method to calculate the Net present value for the Trade.

## The Net Present Value (NPV) Calculation
The Net Present Value calculation calculates the NPV for 6 years. It is using the following code:


```java
public void calculateNPV(double rate , Trade trade) {
    double disc = 1.0/(1.0+(double)(rate/100));
    CacheFlowData cf =  trade.getCacheFlowData();
    double NPV = (cf.getCacheFlowYear0() +
    		disc*(cf.getCacheFlowYear1() +
    		disc*(cf.getCacheFlowYear2() +
    		disc*(cf.getCacheFlowYear3() +
    		disc*(cf.getCacheFlowYear4() +
    		disc*cf.getCacheFlowYear5())))));
    trade.setNPV(NPV);
}
```

The above can be described using the following formula:
![NPV_formula.jpg](/attachment_files/sbp/NPV_formula.jpg)

## The NPVResultsReducer
The `NPVResultsReducer` receives the NPV calculation for each book from each calculation node (partition) and reduces it into a list of NPV values for each book (four values).

## The Trade
The Trade Space class stores the following items:

- id - The Trade ID.
- CacheFlowData - The cache flow data for Year 0 through Year 5.

## Elasticity
The [Elastic Processing Unit]({{%latestjavaurl%}}/elastic-processing-unit.html) is used to deploy the data/compute grid and scale it dynamically. This allows you to increase the capacity of the data grid and leverage additional CPU resources for the calculation activity. With this demo, the user changes the capacity using a scale command that instructs the data/compute grid to increase its capacity (this in turn starts additional containers and re balances the data/compute grid) or decrease its capacity (by terminating containers and re balancing).

# Remote Calculations
For long calculations that consume relatively large amount of CPU time, the recommended approach to implement distributed calculations is the [Master-Worker Pattern](./master-worker-pattern.html). The approach suggested with the Master-Worker pattern should be used when the calculation time is relativity very long where the data access time can't be considered as overhead.

![ElasticDistributedRiskAnalysisEngine_remote_workers.jpg](/attachment_files/sbp/ElasticDistributedRiskAnalysisEngine_remote_workers.jpg)

# Running the Demo

1. Download the [ElasticCalculationEngine.zip](/attachment_files/sbp/ElasticCalculationEngine.zip) and extract it into an empty folder. Move into the ElasticRiskAnalysisDemo folder and **edit** the `setExampleEnv.bat` to include correct values for the `NIC_ADDR` and the `GS_HOME` variables.
2. Start the GigaSpaces agent by running the following:

```java
startAgent.bat
```
{{% exclamation %}} You will need a machine with at least 2GB free memory to run this demo.
3. Run the Elastic Data-Grid deploy script:

```java
deployDataGrid.bat
```
This will deploy the data grid/compute grid and will later allow you to scale it. Whenever you would like to scale the data grid/compute grid just **hit Enter**. Running the deploy script again will initiate the scaling cycle again for the existing running data grid/compute grid.
4. Run the Ealstic Worker deploy script:

```java
deployWorker.bat
```
This will deploy the Worker PU into the Service Grid.
5. Run the client invoking the Colocated calculations (this will be using the Task):

```java
runClientExecutor.bat
```
6. Run the client invoking the Remote calculations (this will be using the workers):

```java
runClientMasterWorker.bat
```
The client will run the calculation repeatedly for 10,000 Trades where each cycle will use different rates (2%, 3%, 4%, 5%, 6%, 7%, 8%). To stop the client hit CTRL + C.
7. To scale the worker run the following:

```java
ScaleWorker.bat
```
 and follow the instructions.
8. To Scale the Data-Grid following Hit Enter at the command running the `deployDataGrid.bat`.

## Running within eclipse
You may run the Calcualtion Engine within eclipse by using the StartCluster main class. It will start a clustered space. You can use this to debug the `AnalysisTask` when executed at the space side.

## Expected Output


```java
\ElasticRiskAnalysisDemo>set NIC_ADDR=127.0.0.1
Log file: D:\gigaspaces-xap-premium-8.0.1-ga\logs\2011-06-23~15.05-gigaspaces-service-192.168.1.100-13980.log
2011-06-23 15:05:32,566  INFO [Deployer] - Created Admin - OK!
2011-06-23 15:05:37,906  INFO [Deployer] - --- > Local Machine Demo - Starting initial deploy - Deploying a PU with:256MB
2011-06-23 15:05:40,590  INFO [Deployer] - >> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:0
...
2011-06-23 15:05:56,597  INFO [Deployer] - >> Total Memory used:256.0 MB - Progress:100.0 % done - Total Containers:2
2011-06-23 15:05:58,597  INFO [Deployer] - Initial Deploy done! - Time to deploy system:20 seconds
2011-06-23 15:06:00,609  INFO [Deployer] -

About to scale data-grid memory capacity from 256.0 MB to 512 MB
2011-06-23 15:06:00,609  INFO [Deployer] - Hit enter to scale the data grid...

2011-06-23 15:06:37,060  INFO [Deployer] - >> Total Memory used:256.0 MB - Progress:50.0 % done - Total Containers:2
...
2011-06-23 15:06:48,824  INFO [Deployer] - >> Total Memory used:512.0 MB - Progress:100.0 % done - Total Containers:4
2011-06-23 15:06:50,825  INFO [Deployer] - Data-Grid Memory capacity change done! - Time to scale system:13 seconds
2011-06-23 15:06:52,307  INFO [Deployer] -

About to scale data-grid memory capacity from 512.0 MB to 1024 MB
2011-06-23 15:06:52,307  INFO [Deployer] - Hit enter to scale the data grid...

2011-06-23 15:06:53,565  INFO [Deployer] - >> Total Memory used:512.0 MB - Progress:50.0 % done - Total Containers:4
...
2011-06-23 15:07:17,584  INFO [Deployer] - >> Total Memory used:896.0 MB - Progress:87.5 % done - Total Containers:8
2011-06-23 15:07:21,032  INFO [Deployer] - >> Total Memory used:1024.0 MB - Progress:100.0 % done - Total Containers:8
2011-06-23 15:07:23,033  INFO [Deployer] - Data-Grid Memory capacity change done! - Time to scale system:29 seconds
2011-06-23 15:07:25,036  INFO [Deployer] -

About to scale data-grid memory capacity from 1024.0 MB to 256 MB
2011-06-23 15:07:25,036  INFO [Deployer] - Hit enter to scale the data grid...

2011-06-23 15:09:16,208  INFO [Deployer] - >> Total Memory used:1024.0 MB - Progress:25.0 % done - Total Containers:8
...
2011-06-23 15:09:57,049  INFO [Deployer] - >> Total Memory used:384.0 MB - Progress:66.7 % done - Total Containers:3
2011-06-23 15:10:00,398  INFO [Deployer] - >> Total Memory used:256.0 MB - Progress:100.0 % done - Total Containers:2
2011-06-23 15:10:02,400  INFO [Deployer] - Data-Grid Memory capacity change done! - Time to scale system:46 seconds
```


```java
Time to calculate Net present value for 10000 Trades using 2.0 % rate:41 ms
2011-06-23 15:08:15,346  INFO [Client] - Book = Book0, NPV = 2237537745.8
2011-06-23 15:08:15,346  INFO [Client] - Book = Book2, NPV = 2238433119.1
2011-06-23 15:08:15,347  INFO [Client] - Book = Book1, NPV = 2237985432.4
2011-06-23 15:08:15,347  INFO [Client] - Book = Book3, NPV = 2238880805.7
2011-06-23 15:08:16,412  INFO [Client] -
Time to calculate Net present value for 10000 Trades using 3.0 % rate:65 ms
2011-06-23 15:08:16,413  INFO [Client] - Book = Book0, NPV = 4353811571.6
2011-06-23 15:08:16,413  INFO [Client] - Book = Book2, NPV = 4355553793.1
2011-06-23 15:08:16,413  INFO [Client] - Book = Book1, NPV = 4354682682.4
2011-06-23 15:08:16,413  INFO [Client] - Book = Book3, NPV = 4356424903.9
2011-06-23 15:08:17,509  INFO [Client] -
Time to calculate Net present value for 10000 Trades using 4.0 % rate:95 ms
2011-06-23 15:08:17,510  INFO [Client] - Book = Book0, NPV = 6354633987.8
2011-06-23 15:08:17,510  INFO [Client] - Book = Book2, NPV = 6357176858.5
2011-06-23 15:08:17,510  INFO [Client] - Book = Book1, NPV = 6355905423.1
2011-06-23 15:08:17,510  INFO [Client] - Book = Book3, NPV = 6358448293.9
2011-06-23 15:08:18,656  INFO [Client] -
Time to calculate Net present value for 10000 Trades using 5.0 % rate:145 ms
2011-06-23 15:08:18,657  INFO [Client] - Book = Book0, NPV = 8245475707.5
2011-06-23 15:08:18,657  INFO [Client] - Book = Book2, NPV = 8248775217.6
2011-06-23 15:08:18,657  INFO [Client] - Book = Book1, NPV = 8247125462.6
2011-06-23 15:08:18,657  INFO [Client] - Book = Book3, NPV = 8250424972.7
2011-06-23 15:08:19,814  INFO [Client] -
Time to calculate Net present value for 10000 Trades using 6.0 % rate:156 ms
2011-06-23 15:08:19,814  INFO [Client] - Book = Book0, NPV = 10031489134.5
2011-06-23 15:08:19,814  INFO [Client] - Book = Book2, NPV = 10035503335.8
2011-06-23 15:08:19,815  INFO [Client] - Book = Book1, NPV = 10033496235.2
2011-06-23 15:08:19,815  INFO [Client] - Book = Book3, NPV = 10037510436.5
2011-06-23 15:08:21,029  INFO [Client] -
Time to calculate Net present value for 10000 Trades using 7.0 % rate:214 ms
2011-06-23 15:08:21,029  INFO [Client] - Book = Book0, NPV = 11717530016.3
2011-06-23 15:08:21,029  INFO [Client] - Book = Book2, NPV = 11722218903.8
2011-06-23 15:08:21,031  INFO [Client] - Book = Book1, NPV = 11719874460.1
2011-06-23 15:08:21,031  INFO [Client] - Book = Book3, NPV = 11724563347.6
```

