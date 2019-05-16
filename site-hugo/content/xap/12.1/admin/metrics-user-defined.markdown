---
type: post121
title:  User Defined Metrics
categories: XAP121ADM
parent: metrics-overview.html
weight: 400
canonical: auto
---

In addition to the metrics shipped with the product, users can define additional metrics for application-specific data. 
The metric name provided in the annotation is automatically prefixed with `pu_` and reported with the processing unit tags, as explained [here](./metrics-bundled.html#processing-unit).

For example, suppose we have a `FooService` bean:

```xml
<beans xmlns="http://www.springframework.org/schema/beans">                                          
	<bean id="FooService" class="com.gigaspaces.demo.FooService" />
</beans>
```

And suppose it processes some application-specific requests: 

```java
public class FooService {

    private final AtomicInteger processedRequests = new AtomicInteger();

    public void processRequest(Object request) {
        // TODO: Process request
        processedRequests.incrementAndGet();
    }
}
```

and we want to create a metric for the number of processed requests. We can use one of the following techniques:

## Using Annotation

The simplest option is to use the `@ServiceMetric` annotation - simply create a method which returns the current value of the metric, and annotate it with `@ServiceMetric` along with the name you want the metric to be reported with. For example:

```java
public class FooService {

    private final AtomicInteger processedRequests = new AtomicInteger();

    public void processRequest(Object request) {
        // TODO: Process request
        processedRequests.incrementAndGet();
    }

    @ServiceMetric(name="processed-requests")
    public int getProcessedRequests() {
        return processedRequests.get();
    }
}
```

## Using Code

If annotations are not granular enough (for example, you wish to dynamically register and unregister metrics at runtime), you can explicitly register metrics in your code. For that end:

1. The bean should implement the `ProcessingUnitContainerContextAware` interface.
2. Use the injected `ProcessingUnitContainerContext` to create a `BeanMetricManager`. You'll need to provide a name which will prefix all the metrics registered using this `BeanMetricManager`.
3. Use the new `BeanMetricManager` to register a `Gauge` metric - Implement the `getValue()` method to return the value you wish to report.

For example:

```java
public class FooService implements ProcessingUnitContainerContextAware {

    private BeanMetricManager metricsManager;
    private final AtomicInteger processedRequests = new AtomicInteger();

    public void processRequest(Object request) {
        // TODO: Process request
        processedRequests.incrementAndGet();
    }

    @Override
    public void setProcessingUnitContainerContext(ProcessingUnitContainerContext processingUnitContainerContext) {
        this.metricsManager = processingUnitContainerContext.createBeanMetricManager("FooService");
        this.metricsManager.register("processed-requests", new Gauge<Integer>() {
            @Override
            public Integer getValue() throws Exception {
                return processedRequests.get();
            }
        });
    }
}
```

Another option is to use `LongCounter` metric instead to simplify metric registration. `LongCounter` is a built-in metric type used to simplify reporting counters.

For example:

```java
public class FooService implements ProcessingUnitContainerContextAware {

    private BeanMetricManager metricsManager;
    private final LongCounter processedRequests = new LongCounter();

    public void processRequest(Object request) {
        // TODO: Process request
        processedRequests.inc();
    }

    @Override
    public void setProcessingUnitContainerContext(ProcessingUnitContainerContext processingUnitContainerContext) {
        this.metricsManager = processingUnitContainerContext.createBeanMetricManager("custom-name");
        this.metricsManager.register("processed-requests", processedRequests);
    }
}
```

# Prefix And Tags

The specified metric name is automatically prefixed with `pu_` and reported with the processing unit tags, as explained [here](./metrics-bundled.html#processing-unit).
