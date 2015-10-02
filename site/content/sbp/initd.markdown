---
type: postsbp
title:  Managing XAP with init.d
categories: SBP
weight: 1900
parent: production.html
---


{{% ssummary page %}}{{%/ssummary %}}
{{% tip %}}
**Summary**: This pattern explains how to manage XAP with init.d.  <br>
**Author**:Patrick May<br/>
**Recently tested with GigaSpaces version**: XAP 10.0<br/>
**Last Update:** Sept 2014<br/>
{{% /tip %}}


# Introduction
The standard for managing long running daemons and daemon-like processes on Unix
systems is via a script residing in the /etc/init.d directory. Typically these scripts provide
four capabilities:

- start<br>
- stop <br>
- restart<br>
- status

This basic set of features can manage a simple   XAP configuration. More complex configurations require correspondingly more functionality.

# An init.d Script Shell
A typical init.d script follows this pattern:


```bash
#!/bin/sh

# Script description
start(){
}

stop(){
}

status(]{
}

case "$1" in start)
    start
    ;;

    stop)
        stop
    ;;

    restart)
     stop
     start
   
