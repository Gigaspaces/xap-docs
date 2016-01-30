---
type: faq
title:  Processing Unit
categories: FAQ
weight: 400
parent: none

---






{{%panel%}}

- [How do I package my application ?](#1)

- [Can I change a service within a Processing Unit without shutting down the Processing Unit?](#2)




{{%/panel%}}


{{%anchor 1%}}

### How do I package my application ?

One of the main goals of XAP is simplifying lifecycle management of an application. A typical application starts within the developer IDE and then progresses to a test environment, pre-production and finally production. OpenSpaces allows to run and debug a Processing Unit within the IDE in a simple manner, package it and then simply provide different deployment scenarios.

A Processing Unit is a simple directory structure. It includes a Spring XML configuration file (under META-INF/spring/pu.xml), the business logic class files, and third-party module jar files. A Processing Unit, under the mentioned structure, can then run within the IDE, locally, and deployed on the SLA-driven container without any changes.

{{%refer%}}
[The Processing Unit]({{%latestjavaurl%}}/the-processing-unit-overview.html)
{{%/refer%}}


{{%anchor 2%}}

### Can I change a service within a Processing Unit without shutting down the Processing Unit?

XAP supports dynamic reloading of selected service beans (business logic) without bringing down the processing unit.

This mainly applies when wanting to change business logic of a Processing Unit that also starts a space, without shutting down the space. Any Processing Unit or other services that connect to the space remotely can be replaced easily with the current version.

{{%refer%}}
[Reloading Business Logic]({{%latestjavaurl%}}/reloading-business-logic.html)
{{%/refer%}}

{{%anchor 3%}}



