---
type: post
title:  Data Offload - GigaSpaces-Excel Integration
categories: SBP
parent: excel-that-scales-solution.html
weight: 200
---



# Overview

In this pattern, all data is stored in the space (on the server side). Excel in turn loads only the relevant data each time and displays it in the spreadsheet. This removes the load from Excel, which is sometimes unable to cope with such large amounts of data, and, if required, updates the displayed data without delay.

Using this pattern is divided into 4 main steps:

1. [Loading all your data to the space](#1-loading-data-to-space).
2. [Loading a subset of your data to the Excel spreadsheet](#2-loading-data-subset-to-excel).
3. If required: [Defining a refresh policy](#3-defining-refresh-policy).

## 1 -- Loading Data to Space

As a first step, you need to load all your data from its current source to the space.

GigaSpaces provides [OpenSpaces](/product_overview/product-architecture.html#ProductArchitecture-OpenSpacesAPIandComponents) as its main API. However, it is also possible to load data from different types of applications transparently, using different connectors implemented by GigaSpaces:

- For messaging-based applications, refer to the [JMS]({{%latestjavaurl%}}/messaging-support.html) section.
- If your application is an external data source (like a database), refer to the [External Data Source]({{%latestjavaurl%}}/space-data-source-api.html) section.

## 2 -- Loading Data Subset to Excel

After you've loaded your data to the space, you need to load the portion you want to work with into your Excel spreadsheet. A SQL query is performed on the space, thus separating the specified data from all the data and loading it into the spreadsheet.

This can be done using the [Excel Space Viewer](./excel-space-viewer.html), which allows you to perform queries on the space and display the data in the spreadsheet.

## 3 -- Defining Refresh Policy

If you need the data displayed in your spreadsheet to be constantly updated, you can do this using the [Excel Space Viewer](./excel-space-viewer.html). Simply define the required refresh rate in milliseconds in the [New View](./excel-space-viewer.html#Creating New View) or [Configure View](./excel-space-viewer.html#Changing Existing View) window. Excel in turn loads the updated data from the space according to the specified refresh rate.

# What's Next?

{{% refer %}}[Try another pattern](./excel-that-scales-solution.html){{% /refer %}}
