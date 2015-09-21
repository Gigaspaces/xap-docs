---
type: post102
title:  User Defined Metrics
categories: XAP102ADM
parent: metrics-overview.html
weight: 400
---

In addition to the metrics shipped with the product, users are free to define additional metrics for application-specific data using the `@ServiceMetric` annotation. For example, suppose we have a `FooService` class which processes some application-specific requests, and we want to measure the number of processed requests:


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

To activate the metric the service needs to be acknowledged as a bean, for example, using the `pu.xml`:


```java
<beans xmlns="http://www.springframework.org/schema/beans">
                                          
	<bean id="FooService" class="com.gigaspaces.demo.FooService" />

</beans>
```

The metric name provided in the annotation is automatically prefixed with `pu_` and reported with the processing unit tags, as explained [here](./metrics-bundled.html#processing-unit).
