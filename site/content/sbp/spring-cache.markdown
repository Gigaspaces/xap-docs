---
type: post
title:  Spring Cache Abstraction with XAP
categories: SBP
parent: data-access-patterns.html
weight: 101
---

{{% mute %}}This article shows how to  use the Spring Cache Abstraction Provider with  XAP{{% /mute %}}

{{% panel %}}
{{%section%}}
{{%column width="15%" %}}
**XAP Version**<br>
**Last Updated**<br>
**Reference**<br>
**Example**
{{%/column%}}
{{%column  width="50%" %}}
9.7<br>
October 2014<br>
[Spring Framework](http://docs.spring.io/spring-framework/docs/4.0.x/spring-framework-reference/html/cache.html)<br>
{{%git "https://github.com/GigaSpaces-ProfessionalServices/spring-cache-abstraction" %}}
{{%/column%}}
{{%column  width="10%" %}}
{{%align right%}}
**Author**
{{%/align%}}
{{%/column%}}
{{%column  width="25%" %}}
{{%align center%}}
Ali Hodroj <br>
Director of Solution Architecture<br>
GigaSpaces
{{%/align%}}
{{%/column%}}
{{%/section%}}
{{% /panel %}}


{{%ssummary%}}{{%/ssummary%}}


# Introduction

Since version 3.1, the `Spring Framework` provides support for transparently adding caching to an existing `Spring` application through method annotations. As is the case with transaction support, the caching abstraction decouples caching implementation from the business logic. This article shows how to utilize the XAP CacheManager with Spring that implements the following distributed caching scenario:


|      |     |
|------|-----|
|**Distributed cache**|**Distributed cache + local cache** |
|![scaling_agent.jpg](/sbp/attachment_files/spring-cache1.png)|![scaling_agent.jpg](/sbp/attachment_files/spring-cache2.png)|



{{%vbar "Benefits when using XAP as a Spring caching provider: "%}}

-	**Decreased Latency** -  Ability to utilize local cache across all server instances for localized reads, greatly reducing serialization across the wire

-	**Scalability** - Horizontally scalable and partitioned cache with flexible cache entry routing

-	**High Availability** - Each parti
