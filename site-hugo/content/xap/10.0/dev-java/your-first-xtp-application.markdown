---
type: post100
title:  Your First XAP Application
categories: XAP100
weight: 500
parent: cook-books.html
---






**Summary:**  This tutorial explains how to build your first GigaSpaces Application in 4 easy steps, from basic API usage to scaling your application and making it highly available



{{% anchor Step1 %}}

{{% section %}}

## Before You Begin - Set Up Your Environment

{{%panel%}}
Follow these [instructions](./installation-guide.html#java-installation) to download and install the latest version of XAP.
{{%/panel%}}

The sample application used in all steps is located in {{%folderopen%}}<XAP root>\examples\helloworld


{{% anchor Step2 %}}

## Quick Start Guide Steps

We recommend that you follow these 4 basic tutorials in the specified order:
{{% /section %}}

{{% panel   %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step1.jpg](/attachment_files/Step1.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Using Processing Units for Scaling**](./step-one-using-processing-units-for-scaling.html)
{{% color grey %}}~5 minutes{{% /color %}}
A short introduction that shows what a Processing Unit is, and how it is used for scaling your applications

{{% /column %}}
{{% column width="50%" %}}
{{% align center %}}

[![What is a Processing Unit.jpg](/attachment_files/What is a Processing Unit.jpg)](./step-one-using-processing-units-for-scaling.html)
{{% /align %}}

{{% /column %}}
{{% /section %}}

{{% /panel %}}
{{% panel  %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step2.jpg](/attachment_files/Step2.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Creating the Hello World Application**](./step-two-creating-the-hello-world-application.html)
{{% color grey %}}~10 minutes{{% /color %}}
How to create, deploy, run and monitor your Processing Unit

{{%accordion%}}
{{%accord title="  "%}}

**.** {{% sub %}}How to Create a scalable application using Processing Units{{% /sub %}}
**.** {{% sub %}}How to run a Processing Unit within your IDE{{% /sub %}}

{{%/accord%}}
{{%/accordion%}}
{{% /column %}}
{{% column width="50%" %}}
{{% align center %}}

![qsg_helloworld_processing_unit.gif](/attachment_files/qsg_helloworld_processing_unit.gif)
{{% /align %}}

{{% /column %}}
{{% /section %}}

{{% /panel %}}

{{% panel   %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step3.jpg](/attachment_files/Step3.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Deploying the Hello World Application onto the ServiceGrid**](./step-three-deploying-onto-the-service-grid.html)
{{% color grey %}}~10 minutes{{% /color %}}
How to deploy the _Hello World Processing Unit_ onto the grid enabled infrastructure (the ServiceGrid) to enable instant fail-over, recovery, SLA management and runtime monitoring capabilities for your application

{{%accordion%}}
{{%accord title="  "%}}

**.** {{% sub %}}What the Service Grid is{{% /sub %}}
**.** {{% sub %}}How to Start the Service Grid{{% /sub %}}
**.** ~How to Use the Grid Manager (GSM) and the Grid Container(GSC)~
**.** {{% sub %}}How to Deploy an application onto the Service Grid with the Management UI{{% /sub %}}
**.** {{% sub %}}How to Monitor the Service Grid and deployed applications during runtime{{% /sub %}}

{{%/accord%}}
{{%/accordion%}}
{{% /column %}}
{{% column width="50%" %}}
{{% align center %}}

![qsg_service_grid.gif](/attachment_files/qsg_service_grid.gif)
{{% /align %}}

{{% /column %}}
{{% /section %}}

{{% /panel %}}
{{% panel   %}}

{{% section %}}


{{% column width="7%" %}}
{{% align center %}}

![Step4.jpg](/attachment_files/Step4.jpg)
{{% /align %}}

{{% /column %}}
{{% column width="35%" %}}
[**Scaling the Hello World Application**](./step-four-scaling-the-hello-world-application.html)
{{% color grey %}}~10 minutes{{% /color %}}
How to scale the _Hello World Processing Unit_ application

{{%accordion%}}
{{%accord title="  "%}}

**.** {{% sub %}}How to use GigaSpaces' clustering and partitioning capabilities to scale the _Hello World Processing Unit_ application{{% /sub %}}

{{%/accord%}}
{{%/accordion%}}

{{% /column %}}
{{% column width="50%" %}}
{{% align left %}}

![qsg_helloworld_scaling.gif](/attachment_files/qsg_helloworld_scaling.gif)
{{% /align %}}

{{% /column %}}
{{% /section %}}

{{% /panel %}}


{{% section %}}
## Go Beyond the Basics

After you have learned the basics, you can go to the [more advanced tutorials](./beyond-the-basics.html) to dive into the details, and learn how to implement real world scenarios.
{{% refer %}}[Back to Quick Start Guide Home](./index.html){{% /refer %}}
{{% /section %}}

