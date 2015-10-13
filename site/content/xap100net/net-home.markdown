---
type: post100
title:  Full Tutorial
categories: XAP100NET
parent: tutorials.html
weight: 200
---




This Tutorial provides a high-level overview of the GigaSpaces XAP platform. Hands on examples are provided to demonstrate the core concepts and API's. The primary people who can benefit from this tutorial, are architects and developers who wish to build scaled-out applications with GigaSpaces XAP.


# Class Model
Throughout this tutorial we will create and use a simple internet payment service application to demonstrate the basic XAP features. The basic concept of our application;

- Merchants enter into a contract to handle financial transactions using the application.
- The Merchant will receive a percentage for each transaction.
- Users will make payments with the online system.

{{%section%}}
{{%column%}}
Here is the simplified Class Model:
{{%/column%}}
{{%column%}}
{{%popup "/attachment_files/qsg/class_diagram.png"  "Class Diagram Class Diagram"%}}
{{%/column%}}
{{%/section%}}
{{<wbr>}}


You can download all examples presented here from {{%git "https://github.com/Gigaspaces/xapnet-tutorial"%}}. Feel free to clone, fork and contribute to the tutorial code.


{{%vbar "Download and Install XAP"%}}
GigaSpaces XAP.NET is packaged as a standard Windows Installer package (.msi file). After you have downloaded {{%download "http://www.gigaspaces.com/xap-download"%}} the latest version, start the installation by double-clicking the msi file, and the installation wizard will pop up and guide you through the installation process.

{{%/vbar%}}


# Tutorial Trail



{{% panel  %}}
{{%section%}}
{{%column width="20%" %}}

{{%align center%}}
[Part I](./net-tutorial-part1.html)

<img src="/attachment_files/qsg/data.png" width="100" height="100">

{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Interacting with the Space**{{%/align%}}
This part of the tutorial will introduce you to the space as a data store.

You will learn how to:

- create a space
- write objects into the space
- querying the space
- indexing objects in space

{{%learn "./net-tutorial-part1.html"%}}
{{%/column%}}
{{%/section%}}
{{%/panel%}}




{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part II](./net-tutorial-part2.html)

<img src="/attachment_files/subject/deploy.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Deploying a Space**{{%/align%}}
In this part of the tutorial we will show you how you can deploy an In Memory Data Grid (IMDG) that provides scalability and failover.

You will learn how to:

- start a data grid
- deploy a data grid
- interact with the data grid
- how to use the administration UI

{{%learn "./net-tutorial-part2.html"%}}
{{%/column%}}
{{%/section%}}
{{%/panel%}}

{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part III](./net-tutorial-part3.html)

<img src="/attachment_files/qsg/processing.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Processing Services**{{%/align%}}
In this part of the tutorial we will introduce you to the different processing services you can run on top of the space.

You will learn how to use:

- Executor service
- Remoting service


{{%learn "./net-tutorial-part3.html"%}}

{{%/column%}}
{{%/section%}}
{{%/panel%}}



{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part IV](./net-tutorial-part4.html)

<img src="/attachment_files/qsg/Events-Message.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Events and Messaging**{{%/align%}}
In this part of the tutorial we will introduce you to events and messaging on top of the space.

You will learn how to use:

- Notify container
- Polling container

{{%learn "./net-tutorial-part4.html"%}}

{{%/column%}}
{{%/section%}}
{{%/panel%}}


{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part V](./net-tutorial-part5.html)

<img src="/attachment_files/subject/pu.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**The Processing Unit**{{%/align%}}
In this part of the tutorial we will introduce you the Processing Unit (PU). The PU is the fundamental unit of deployment in GigaSpaces XAP

You will learn how to:

- create a PU
- configure the PU
- deploy a PU
- how to scale and provide failover


{{%learn "./net-tutorial-part5.html"%}}

{{%/column%}}
{{%/section%}}
{{%/panel%}}



{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part VI](./net-tutorial-part6.html)

<img src="/attachment_files/qsg/transaction.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Space Transactions**{{%/align%}}
In this part of the tutorial we will introduce you to the transaction processing capabilities of XAP.

You will learn about:

- Transaction managers
- Transaction processing
- Concurrency
- Locking

{{%learn "./net-tutorial-part6.html"%}}

{{%/column%}}
{{%/section%}}
{{%/panel%}}


{{%panel   %}}
{{%section%}}
{{%column width="20%" %}}
{{%align center %}}
[Part VII](./net-tutorial-part7.html)

<img src="/attachment_files/qsg/persistence.png" width="100" height="100">
{{%/align%}}
{{%/column%}}

{{%column width="70%" %}}
{{%align center%}}**Space Persistence**{{%/align%}}
In this part of the tutorial we will introduce you to space persistency.

You will learn about:

- Synchronous persistence
- Asynchronos persistence
- Persistence Adapter

{{%learn "./net-tutorial-part7.html"%}}

{{%/column%}}
{{%/section%}}
{{%/panel%}}
