---
type: post100
title:  Step One - Using Processing Units for Scaling
categories: XAP100
weight: 200
parent: your-first-xtp-application.html
---



**Summary:**  In this step we will use a car wash analogy to compare the traditional tier based approach for architecting enterprise application with GigaSpaces' Space Based approach, and explain the concept of _Processing Units_.

#### Preface

Once in a while you need to wash your car. Some people keep their car in superb condition and wash it very often, while others transform it skillfully into a mega-junk-mobile and take it to the car wash very rarely, if ever. But the fact remains that most people will bring their car to the car wash at one stage or another

#### Washing Your Car The Tier-Based Way

In a tier-based architecture, our system is composed of several disparate physical tiers - meaning the database, the messaging queue and the application server. So every time your application needs to get something done, it typically goes through all these components. Imagine using this approach to wash your car. Each tier is analogous to a phase in our washing process: Firstly, the dirty car is transported to a soaping warehouse, where it is covered with soap. The next stop is transporting the "soaped" car to a different warehouse, where it is watered to rinse off the soap. The last step is transporting the car to yet another warehouse, which specializes in drying water marks, to dry and wipe all the water off of it.

Fortunately, we don't clean cars this way, because transporting the car to each of the warehouses (which might be geographically remote from one another) would take a considerable amount of time.

{{% align center %}}![Tierbased2.jpg](/attachment_files/Tierbased2.jpg){{% /align %}}
{{% align center %}}**{{% sub %}}Diagram 1. Car wash, the tier based way: a different warehouse for each step in the process.{{% /sub %}}**{{% /align %}}

#### Washing the Car Using Space-Based Architecture

What we actually do, is send the car a single warehouse, which has all the services available in the same place (we call this colocation). That way this services can be accessed much faster. One local machine soaps it, another local washing machine waters it, and finally a third local machine uses hot air to dry it and remove all the water marks. This an all-in-one warehouse, containing a depot to hold the car and all the collocated services for washing, rinsing and drying it. In the GigaSpaces language, we call this a _Processing Unit_ - in this case a carwash processing unit.

Going back to the real world (where enterprise applications live...), imagine that your application will have all the services it needs in the same location (operating system process to be specific). So it will not go through separate physical layers just to access the data or send and receive messages. This is the concept behind GigaSpaces' Processing Unit.

{{% align center %}}![Spacebased.jpg](/attachment_files/Spacebased.jpg){{% /align %}}
{{% align center %}}**{{% sub %}}Diagram 2. An all-in-one "washing unit" containing soaping, watering and drying services.{{% /sub %}}**{{% /align %}}

#### Scaling Easily with Self Contained Processing Units

Self contained Processing Units also have the advantage that they can be easily scaled. If we have more cars to wash than a single unit can handle, the system can be scaled almost linearly, by simply adding more car washing units.

{{% align center %}}![Spacebasedscaled.jpg](/attachment_files/Spacebasedscaled.jpg){{% /align %}}
{{% align center %}}**{{% sub %}}Diagram 3. Scaling the system by adding more "car washing units".{{% /sub %}}**{{% /align %}}

#### A GigaSpaces Processing Unit


Now let's translate this to the enterprise application terminology. In the diagram below you can see a GigaSpaces Processing Unit instance, which runs within a Processing Unit Container. (This is in fact a JVM with a thin layer that knows how to run processing units.)

You can see the _Space_ (the "car depot") in the center of the processing unit. The space holds objects in the local JVM memory, making them accessible to all the local services (the "washing services"). The space can also act as a messaging layer, by notifying the relevant service when a particular object is written to it. The services can then respond to the message by processing it, and writing it back to the space. They can also use the space to retrieve additional pieces of information needed for the processing. A client application can interact with the space-side services by writing objects to the Processing Unit's space and reading them after they are processed. We can linearly scale the system by simply adding more Processing Unit instances, since the processing units are entirely independent of one another.

{{% align center %}}![Spacebasedreal.jpg](/attachment_files/Spacebasedreal.jpg){{% /align %}}
{{% align center %}}**{{% sub %}}Diagram 4. A Processing Unit with 3 services collocated with a space.{{% /sub %}}**{{% /align %}}


## What's Next?

Next Page - [Step two - Creating the Hello World Application](./step-two---creating-the-hello-world-application.html). Shows how to create your first GigaSpaces application and run it on your local machine.

Or return to the [Your First XTP Application](./your-first-xtp-application.html) page.
