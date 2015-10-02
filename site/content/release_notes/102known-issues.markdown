---
type: postrel
title:  Known Issues and Limitations
categories: RELEASE_NOTES
parent: xap102.html
weight: 400
---


Below is a list of known issues in GigaSpaces 10.2.X.




| Key | Summary | SalesForce ID | Workaround | Platform/s |
|:-------|:--------|:----------------|:------------------|:----------|
| <nobr>GS-12519</nobr> | SQLQuery with composite condition might return wrong result | 9936 | Instead of writing:  a in (x,y) AND (b < z OR A in (w,v)) do write: (b < z OR A in (w,v)) AND a in (x,y) | Java |
| GS-12504 | Rebalance API |  |  | All |
| GS-12503 | Support common functions within SQL where clause |  |  | All |
| GS-12502 | Geospatial Queries |  |  | Java |
| GS-12495 | Slow Memory leak in lease manager - empty cells remains after expiration | 9876 |  | All |
| GS-12486 | Transaction consolidation issues with distributed transaction with durable notification and mirror when transaction contains both objects that meets the notify template and some that don't | 8935 |  | All |
| GS-12476 | llegalArgumentException after connection between mirror and mongodb reestablished  | 9832 |  | Java |
| GS-12475 | Jetty Shared Instantiation Mode is corrupted on instance relocation |  |  | Java |
| GS-12470 | Write object with lease to LRU space when miiror is up will not result in expiration | 9801 |  | All |
| GS-12465 | Remote service execution fails sporadically on Failure to find definition within the application context on rare condition | 9826 |  | All |
| GS-12458 | Increment stateless Processing Unit instance while healing results in actual less than planned |  |  | All |
| GS-12457 | variable substitution does not work with os-core:security  |  |  | All |
| GS-12441 | GS-UI Locator Management - Error message when trying to add a valid ipv6 address  | 9747 |  | All |
| GS-12438 | Web-UI - Exception When a SpaceDocument contains Pojo as proprty and quering nested properties  | 9756  |  | All |
| GS-12422 | Instance missing after GSC restart till killing managing GSM | 9704 |  | All |
