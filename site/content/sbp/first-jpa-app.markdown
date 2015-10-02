---
type: postsbp
title:  Your First JPA Application
categories: SBP
weight: 300
parent: examples.html
---

<br>


This tutorial explains how the sample Spring PetClinic application can be fine tuned to use GigaSpaces XAP [JPA API]({{%latestjavaurl%}}/jpa-api.html) and deployed into the GigaSpaces XAP platform.

Will show you how to:

- Adjust the PetClinic domain model to use the distributed XAP JPA implementation <br>
- Utilize scalable distributed patterns such as partitioning, colocation of data and business logic and Map/Reduce to scale your JPA application  <br>
- Deploy the application on to the   XAP runtime environment to achieve high availability and self healing capabilities  <br>


<br>

{{%fpanel%}}

[Adjusting Your JPA Domain Model to XAP's Distributed JPA Implementation](./first-jpa-app-step-1.html)<br>
Shows how to adjust the PetClinic's JPA domain model to XAP's JPA implementation and deals with issues such as data partitioning and indexing

[Using the Power of the Space to Scale Your Data Access Layer](./first-jpa-app-step-2.html)<br>
Shows how to implement the PetClinic's data access layer using Space based remoting and colocation of data and business logic.

[Building & Running the Application](./first-jpa-app-step-3.html)<br>
Shows how to build and deploy the application to the GigaSpaces runtime environment

{{%/fpanel%}}
