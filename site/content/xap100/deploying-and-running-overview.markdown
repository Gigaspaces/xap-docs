---
type: post100
title:  Deploying and Running
categories: XAP100
parent: the-processing-unit-overview.html
weight: 600
---

<br>

{{% section %}}
{{% column width="10%" %}}
![counter-logo.jpg](/attachment_files/subject/deploy.png)
{{% /column %}}
{{% column width="90%" %}}
This section describes the various options to debug and run your processing units.
{{% /column %}}
{{% /section %}}


<br>


{{%fpanel%}}

[Overview](./deploying-and-running-the-processing-unit.html){{%wbr%}}
Overview over the different runtime modes.

[Running in standalone mode](./running-in-standalone-mode.html){{%wbr%}}
Explains how to run your processing unit in standalone mode, which means that the processing unit is run under its own self-constructed classloader, based on the processing unit's directory structure.

[Deploying onto the service grid](./deploying-onto-the-service-grid.html){{%wbr%}}
Explains how to deploy your processing unit onto the GigaSpaces Service Grid to get automated SLA management and self-healing capabilities.

[Runtime properties](./deployment-properties.html){{%wbr%}}
The processing unit can be injected with dynamic property values at deployment time. The mechanism leverages Spring's PropertyPlaceholderConfigurer to provide powerful and simple properties-based injection.

{{%/fpanel%}}


