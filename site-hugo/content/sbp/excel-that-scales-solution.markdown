---
type: post
title:  Excel that Scales Solution
categories: SBP
parent: data-access-patterns.html
weight: 500
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Pini Cohen| 6.6 |   |           |          |



# Overview

This section gets you started with the Excel integration by understanding its available functionality, how to develop the integration components and how to configure, deploy and run the solution.

Along with several examples that can be used as starting points for development, it includes supported platforms and a short description of testing that was performed to validate the proposed patterns.

# GigaSpaces-Excel Integration Patterns

There are two main problems that the GigaSpaces-Excel solution is relevant for:

|   |   |     |
|----|----|-----|
| -- You are working with a **very large amount of data**, which **causes Excel to slow down or freeze** <br/> - However, **you need only a portion of your data to be displayed** in Excel at a time <br/> - You need the spreadsheet to be **updated constantly** | ![blue_arrow2.jpg](/attachment_files/sbp/blue_arrow2.jpg) | [Data offload](./data-offload-gigaspaces-excel-integration.html) |
| -- You are **performing very complex calculations**, or a **large amount of calculations** in Excel <br/> - These calculations are **costly** <br/> -- they **cause Excel to slow down or freeze**; or **slow down other applications** | ![blue_arrow2.jpg](/attachment_files/sbp/blue_arrow2.jpg) | [Calculation offload](./calculation-offload-gigaspaces-excel-integration.html) |

