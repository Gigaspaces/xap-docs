---
type: post
title:  Known Issues and Limitations
categories: RELEASE_NOTES
parent: xap102.html
weight: 400
---


Below is a list of known issues in GigaSpaces 10.2.X.




| Key | Summary | SalesForce ID | Workaround | Platform/s |
|:-------|:--------|:----------------|:------------------|:----------|
| <nobr>GS-12607</nobr> | Update object lease will not update the lease manager,if update waited on a lock | 9949 | | Java |
| GS-12600 | Curator (ZK client )smart resource allocation |  |  | All |
| GS-12586 | Missing jar from our XAP as Windows Service example | 10051 |  | Java |
| GS-12570 | XAP.NET HttpSessionProvider sporadically causes 100% CPU | 10029 |  | .Net |
| GS-12569 | .Net - Fail to add indexes on document properties |  |  | .Net |
| GS-12568 | Duplicates Xmx/Xms when running gs-agent script | 9998 |  | Java |
| GS-12537 | -XX:MaxPermSize=256m option in setenv.sh is not relevant for java 8 (HOTSPOT) |  |  | All |
| GS-12534 | SimpleNotifyContainerConfigurer.create might block indefinitly | 9931 |  | All |
| GS-12532 | Memory leak using atomicos | 9917 |  | All |
| GS-12529 | First event polled by container is lost after PU was redeployed | 9934 |  | Java |
| GS-12516 | Improve logging message when index unique=true and UniqueConstraintViolationException is trown | 9930 |  | Java |
| GS-12503 | Support common functions within SQL where clause |  |  | All |
| GS-12475 | Jetty Shared Instantiation Mode is corrupted on instance relocation |  |  | Java |
| GS-12472 | .NET - failed to deploy Examples\ProcessingUnit\Feeder |  |  | .Net |
| GS-12470 | Write object with lease to LRU space when miiror is up will not result in expiration | 9801 |  | All |
| GS-12458 | Increment stateless Processing Unit instance while healing results in actual less than planned |  |  | All |
| GS-12457 | variable substitution does not work with os-core:security  |  |  | All |
| GS-12441 | GS-UI Locator Management - Error message when trying to add a valid ipv6 address  | 9747 |  | All |
| GS-12438 | Web-UI - Exception When a SpaceDocument contains Pojo as proprty and quering nested properties  | 9756  |  | All |