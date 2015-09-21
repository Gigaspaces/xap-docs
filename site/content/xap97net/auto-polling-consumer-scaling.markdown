---
type: post97
title:  Auto Scaling
categories: XAP97NET
parent: polling-container.html
weight: 100
---



By default, the [polling event container](./polling-container.html) starts a single thread that performs the receive operations, and invokes the event listener. It can be configured to start several concurrent consumer threads, and have an upper limit to the concurrent consumer threads. The container manages the scaling up and down of concurrent consumers automatically, according to the load.
There are 5 parameters that control the scaling behavior:


|Parameter Name| Description |Default |
|:-------------|:------------|:------:|
| MinConcurrentConsumers  | Minimum number of consumers that wait and process events. | 1 |
| MaxConcurrentConsumers  | Maximum number of consumers that wait and process events. | 1 |
| DynamicScaleSampleRate  | After how many iterations the scaling detection mechanism should work. | 10 |
| IdleIterationsThreshold | After how many idle iterations a consumer is considered idle. | 1 |
| BusyIterationsThreshold | After how many un idle iterations a consumer is considered busy. | 10 |

The scaling is managed by a single consumer, which is considered as the main consumer. There is always at least one active consumer, therefore the main consumer is always alive. After the `DynamicScaleSampleRate` number of iterations, the main consumer checks if all the other consumers are busy. A consumer is considered busy if it processed an event in the last `BusyIterationsThreshold` number of iterations. If all the consumers are busy, and the active number of consumers is less than the `MaxConcurrentConsumers`, a new consumer is spawned. If there is a consumer which is not busy, the main consumer checks if there is a need to scale down the number of consumers. It does so by iterating over all the active consumers, and checking if one of them is idle. A consumer is considered idle if for the last `IdleIterationsThreshold` number of iterations, it did not process a single event. If there is at least one consumer which is idle, then one (and only one) of the currently idle consumers is stopped.
Here is an example of how these properties can be configured:

{{%tabs%}}

{{%tab "  Using EventListenerContainerFactory "%}}


```csharp
[PollingEventDriven(MinConcurrentConsumers = 1, MaxConcurrentConsumers = 5, DynamicScaleSampleRate = 1000, IdleIterationsThreshold = 10, BusyIterationThreshold = 50)]
public class SimpleListener
{
    [EventTemplate]
    public Data UnprocessedData
    {
        get
        {
            Data template = new Data();
            template.Processed = false;
            return template;
        }
    }

    [DataEventHandler]
    public Data ProcessData(Data event)
    {
        //process Data here and return processed data
    }
}
```

{{% /tab %}}

{{%tab "  PollingEventListenerContainer Code Construction "%}}


```csharp
PollingEventListenerContainer<Data> pollingEventListenerContainer = // create or obtain a reference to a polling container

pollingEventListenerContainer.MinConcurrentConsumers = 1;
pollingEventListenerContainer.MaxConcurrentConsumers = 5;
pollingEventListenerContainer.DynamicScaleSampleRate = 1000;
pollingEventListenerContainer.IdleIterationsThreshold = 10;
pollingEventListenerContainer.BusyIterationsThreshold = 50;
```

{{% /tab %}}

{{% /tabs %}}

Since there is no asynchronous process that monitors the load, but one of the consumers does so, when altering the default parameters a few things should be kept in mind. The amount of time that it takes to detect that a spawning of an additional consumer is needed is the average event processing time ** the `DynamicScaleSampleRate` . If the processing time is very long, you might want to reduce the `DynamicScaleSampleRate`. On the other hand, it takes `ReceiveTimeout` ** `IdleIterationsThreshold` amount of time to detect an idle consumer. If the `ReceiveTimeout` is long, obviously it increases the idle consumer detection time. As a rule of thumb, it is more important to scale up quickly rather than to scale down quickly, so altering the parameters should be planned towards faster scaling up.
