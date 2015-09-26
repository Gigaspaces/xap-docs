---
type: post101
title:  Installation
categories:  XAP101TUT
parent: none
weight: 100
---



{{%section%}}
{{%column width="10%" %}}
![data-access.jpg](/attachment_files/subject/data-access.png)
{{%/column%}}
{{%column width="90%" %}}
<br>
Throughout this tutorial we will create and use a simple internet payment service application to demonstrate the basic XAP features.
{{%/column%}}
{{%/section%}}


{{%section%}}
{{%column width="80%" %}}
The basic concept of our application;

- Merchants enter into a contract to handle financial transactions using the application.
- The Merchant will receive a percentage for each transaction.
- Users will make payments with the online system.
{{%/column%}}
{{%column width="20%" %}}
{{%popup "/attachment_files/qsg/class_diagram.png"  "Class Diagram Class Diagram"%}}
{{%/column%}}
{{%/section%}}


<br>

You can download all examples presented here from {{%git "https://github.com/Gigaspaces/xap-tutorial"%}}. Feel free to clone, fork and contribute to the tutorial code.

{{%vbar "Download and Install XAP"%}}
- Download and unzip the latest XAP release {{%download "http://www.gigaspaces.com/xap-download"%}}
- Unzip the distribution into a working directory; GS_HOME
- Set the JAVA_HOME environment variable to point to the JDK root directory
- Start your favorite Java IDE
- Create a new project
- Include all jar files from the GS_HOME/lib/required in the classpath
{{%/vbar%}}
