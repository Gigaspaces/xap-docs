---
type: post100
title:  Your First JPA Application
categories: XAP100
weight: 600
parent: cook-books.html
---


**Summary:**  This tutorial explains how the sample Spring PetClinic application can be fine tuned to use GigaSpaces XAP [JPA API](./jpa-api.html) and deployed into the GigaSpaces XAP platform


{{% section %}}

## Before You Begin - Set Up Your Environment

{{%panel%}}
Follow these [instructions](./installation-guide.html#java-installation) to download and install the latest version of XAP.

If you also wish to utilize the GigaSpaces XAP load balancing agent, you should [download](http://httpd.apache.org/download.cgi){:target="_blank"} and install the [Apache 2.2 Http Server](http://httpd.apache.org/){:target="_blank"}.
{{%/panel%}}
{{% /section %}}

## Example Source Code and Build Scripts

You can download the example sources and build scripts [on Github](https://github.com/Gigaspaces/petclinic-jpa).
Simply [download the sources as zip](https://github.com/Gigaspaces/petclinic-jpa/archive/{{site.petclinic_jpa_branch}}.zip) or clone the repo to your machine (make sure to use the `{{site.petclinic_jpa_branch}}` branch).


## Using XAP's JPA Support to Scale the Spring PetClinic Sample Application - Step by Step

|![animals.png](/attachment_files/animals.png)|This tutorial explains how the Spring PetClinic sample application can be scaled by running on the Space, showing you how to: {{% wbr %}}{{% oksign %}} Adjust the PetClinic domain model to use the distributed XAP JPA implementation{{% wbr %}}{{% oksign %}} Utilize scalable distributed patterns such as partitioning, colocation of data and business logic and Map/Reduce to scale your JPA application{{% wbr %}}{{% oksign %}} Deploy the application on to the GigaSpaces XAP runtime environment to achieve high availability and self healing capabilities|

{{% panel   %}}

{{% section %}}


{{% column width="7" %}}
{{% align center %}}

![Step1.jpg](/attachment_files/Step1.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Adjusting Your JPA Domain Model to XAP's Distributed JPA Implementation**](./step-1-adjusting-your-jpa-domain-model-to-the-xap-jpa-implementation.html)
{{% color grey %}}~15 minutes{{% /color %}}
Shows how to adjust the PetClinic's JPA domain model to XAP's JPA implementation and deals with issues such as data partitioning and indexing
{{% /column %}}
{{% column width="40%" %}}
![object-model.png](/attachment_files/object-model.png)
{{% /column %}}
{{% /section %}}

{{%accordion%}}
{{%accord title="  **In this step you will learn...**"%}}

- The concepts basics of the GigaSpaces JPA implementation
- The required adjustments to scale the PetClinic's JPA domain model and work in with the Space's distributed JPA implementation

{{%/accord%}}
{{%/accordion%}}
{{% /panel %}}
{{% panel  %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step2.jpg](/attachment_files/Step2.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Using the Power of the Space to Scale Your Data Access Layer**](./step-2-using-the-power-of-the-space-to-scale-your-data-access-layer.html)
{{% color grey %}}~15 minutes{{% /color %}}
Shows how to implement the PetClinic's data access layer using Space based remoting and colocation of data and business logic.
{{% /column %}}

{{% column width="40%" %}}
![continuous-scaling.png](/attachment_files/continuous-scaling.png)
{{% /column %}}

{{% /section %}}

{{%accordion%}}
{{%accord title="  **In this step you will learn...**"%}}

- How to use space remoting to implement ultra-fast embedded access to your data using JPA

{{%/accord%}}
{{%/accordion%}}
{{% /panel %}}

{{% panel   %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step3.jpg](/attachment_files/Step3.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}

[**Building & Running the Application**](./step-3-building-and-running-the-application.html)
{{% color grey %}}~15 minutes{{% /color %}}
Shows how to build and deploy the application to the GigaSpaces runtime environment
{{% /column %}}

{{% column width="40%" %}}
![300px-Maven_logo.gif](/attachment_files/300px-Maven_logo.gif)
{{% /column %}}
{{% /section %}}

{{%accordion%}}
{{%accord title="  **In this step you will learn...**"%}}

- Building and deploying the application
- Scaling the web tier
- Configuring highly available HTTP session backed by the space

{{%/accord%}}
{{%/accordion%}}
{{% /panel %}}

