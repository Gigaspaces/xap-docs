---
type: post
title:  Real Time Big-Data Analytics
categories: PRODUCT_OVERVIEW
weight: 3000
parent: none
menu: product
---

{{%  ssummary %}} {{%  /ssummary %}}


Real-time Big-Data Analytics or Real-time business intelligence (RTBI) is the process of delivering information about business operations as they occur. Real time means near to zero latency and access to information whenever it is required.

{{% section%}}
{{% column width="80%" %}}
Real-time Analytics may be used with the following applications:  Algorithmic trading , Fraud detection , Systems monitoring , Application performance monitoring , Customer Relationship Management , Demand sensing , Dynamic pricing and yield management , Data validation , Operational intelligence and risk management , Payments & cash monitoring , Data security monitoring , Supply chain optimization , RFID/sensor network data analysis , Workstreaming , Call center optimization , Enterprise Mashups and Mashup Dashboards and the Transportation industry.
{{% /column%}}
{{% column width="20%" %}}
{{% youtube "ioHwEsARPWI""Real-time Analytics"%}}
{{% /column%}}
{{% /section%}}


The Transportation industry for example, leveraging real-time analytics for the railroad network management. Depending on the results provided by the real-time analytics, dispatcher can make a decision on what kind of train he can dispatch on the track depending on the train traffic and commodities shipped.

# Today's Real-time Processing Systems
The speed of today's processing systems has moved classical data warehousing into the realm of real-time. The result is real-time analytics. Business transactions as they occur are fed into a real-time business intelligence system that maintains the current state of the enterprise - mostly **in memory**. The RTBI system not only supports the classic strategic functions of data warehousing for deriving information and knowledge from past enterprise activity, but it also provides real-time tactical support to drive enterprise actions that react **immediately** to events as they occur. In some cases it may replace both the classic data warehouse and the enterprise application integration (EAI) functions. Such event-driven processing is a basic tenet of real-time business intelligence.

In this context, real-time means a range from few seconds to a few milliseconds after the business event has occurred. While traditional business intelligence presents historical data for manual analysis, real-time business intelligence compares current business events with historical patterns to detect problems or opportunities automatically. This automated analysis capability enables corrective actions to be initiated and/or business rules to be adjusted to optimize business processes.

# GigaSpaces Real Time Analytics
With GigaSpaces , RTBI is an approach in which up-to-a-minute data is analyzed, either directly from operational sources or feeding business transactions into in-memory data grid while the processing logic is collocated with the data in-memory. RTBI logic using the IMDG to analyzes these feeds in real time. 

GigaSpaces provides different type of analytics and processing APIs and integration with stream processing libraries such as STORM and SPARK that simplify the implementation of workflow based real time analytics scenarios.

Real-time business intelligence makes sense for some applications but not for others – a fact that organizations need to take into account as they consider investments in real-time BI tools. Key to deciding whether a real-time BI strategy would pay dividends is understanding the needs of the business and determining whether end users require immediate access to data for analytical purposes, or if something less than real time is fast enough.

# The Internet of Things

Real Time big-data analytics plays a big role with **The Internet of Things** (IoT) systems. The IoT is the interconnection of uniquely identifiable embedded computing devices within the existing Internet infrastructure. Typically, IoT offer connectivity of devices, systems, and services that goes beyond machine-to-machine communications (M2M) and covers a variety of protocols, domains, and applications.

Things, in the IoT, can refer to a wide variety of devices such as heart monitoring implants, biochip transponders on farm animals, electric clams in coastal waters, automobiles with built-in sensors, or field operation devices that assist fire-fighters in search and rescue. Current market examples include smart thermostat systems and washer/dryers that utilize wifi for remote monitoring.

IoT based systems can be seen in Media , Environmental monitoring , Infrastructure management, Manufacturing ,Energy management , Medical and healthcare systems , Building and home automation , Transportation, Large scale deployments and more.

# Evolution of RTBI
In today’s competitive environment with high consumer expectation, decisions that are based on the most current data available to improve customer relationships, increase revenue, maximize operational efficiencies, and yes – even save lives.

This technology is real-time business intelligence. Real-time business intelligence systems provide the information necessary to strategically improve an enterprise’s processes as well as to take tactical advantage of events as they occur.

# Latency
All real-time business intelligence systems have some latency, but the goal is to minimize the time from the business event happening to a corrective action or notification being initiated. Analyst Richard Hackathorn describes three types of latency:
- Data latency; the time taken to collect and store the data
- Analysis latency; the time taken to analyze the data and turn it into actionable information
- Action latency; the time taken to react to the information and take action

Real-time business intelligence technologies are designed to reduce all three latencies to as close to zero as possible, whereas traditional business intelligence only seeks to reduce data latency and does not address analysis latency or action latency since both are governed by manual processes.

# RT Analytics Architecture Foundation Artifacts

## Event-based
Real-time Business Intelligence systems are event driven, and may use Event Stream Processing and Mashup (web application hybrid) techniques to enable events to be analyzed without being first transformed and stored in a database. These in- memory techniques have the advantage that high rates of events can be monitored, and since data does not have to be written into databases data latency can be reduced to milliseconds.

## Server-less Technology
The latest alternative innovation to "real-time" event driven and/or "real-time" data warehouse architectures is MSSO Technology (Multiple Source Simple Output) which removes the need for the data warehouse and intermediary servers altogether since it is able to access live data directly from the source (even from multiple, disparate sources). Because live data is accessed directly by server-less means, it provides the potential for zero-latency, real-time data in the truest sense.

## Process-aware
This is sometimes considered a subset of Operational intelligence and is also identified with Business Activity Monitoring. It allows entire processes (transactions, steps) to be monitored, metrics (latency, completion/failed ratios, etc.) to be viewed, compared with warehoused historic data, and trended in real-time. Advanced implementations allow threshold detection, alerting and providing feedback to the process execution systems themselves, thereby 'closing the loop'.

{{% refer%}}
Related Topics

- [Aggregators API]( {{%latestjavaurl%}}/aggregators.html)
- Counters aAPI {{%latestjavanet "the-space-counters.html" %}}
- [Map reduce API]({{%latestjavaurl%}}/task-execution-over-the-space.html)
- Document-Schemaless API {{%latestjavanet "document-api.html" %}}
- Query API {{%latestjavanet "querying-the-space.html" %}}
- Processing API {{%latestjavanet "event-processing.html"%}}
- BlobStore Storage Model [MemoryXtend]({{%latestadmurl%}}/memoryxtend.html)
- [Time Series](/sbp/time-series.html)
- [Storm Integration](/sbp/storm-integration.html)
- [Kafka Integration](/sbp/kafka-integration.html)
- [Spark Integration](/sbp/spark-integration.html)
- [Drools Rule Engine Integration](/sbp/xap-drools-integration.html)

{{% /refer%}}




