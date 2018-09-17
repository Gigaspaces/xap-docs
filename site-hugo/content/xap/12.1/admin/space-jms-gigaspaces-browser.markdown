---
type: post121
title:  Space JMS
categories: XAP121ADM, PRM
parent: working-with-spaces-gigaspaces-browser.html
weight: 100
---


Using the **JMS** tab of the advanced space configuration dialog.


{{%align center%}}
![Space JMS - GigaSpaces Browser.jpg](/attachment_files/Space JMS - GigaSpaces Browser.jpg)
{{%/align%}}

The **JMS** tab in the **Advanced Space Configuration** window contains the following options:

- **RMI Port** - The name of the space URL of the JMS connection factory. Default 10098 port is used.
These JMS destinations are considered persistent only when the Space itself is persistent.

- **Administrated Topic Names** - The name of the list of persistent topics. There may be additional (transient temporary) JMS topics, but they will not survive a system restart and must be recreated as part of the application.
- **Administrated Queue Names** - The name of the list of persistent queues. There may be additional (transient temporary) JMS queues, but they will not survive a system restart.
