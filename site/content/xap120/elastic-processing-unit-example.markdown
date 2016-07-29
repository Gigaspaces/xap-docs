---
type: post120
title:  Example
categories: XAP120,PRM
parent: elastic-processing-unit-overview.html
weight: 600
---

{{% ssummary %}}{{% /ssummary %}}




The following demo illustrates the EPU. You may run it on your development machine or on your deployment machine.
When running it on your development machine it will scale the EPU within the machine and expand or shrink the memory capacity without any downtime.
The demo includes the following phases:

1. Initial Deployment of an EPU with maximum of 256 MB memory capacity and initial capacity of 64 MB.
2. Scaling up to 128 MB
3. Scaling up to 256 MB
4. Scaling down to 64 MB

{{% note %}}
This demo assumes you have about 300 MB available memory on your machine.
{{%/note%}}

# Running the Example

#### Step 1. Download and Install<br>
Download the example {{%download "/download_files/EPUScaleDemo.zip"%}} and extract it.

#### Step 2. Start gs-agent<br>
Start gs-agent using the following command:


```bash
gs-agent gsa.esm 1 gsa.gsc 0 gsa.lus 1 gsa.gsm 1
```

This will start an agent without any running GSCs.

#### Step 3. Run the Client Application<br>
Run the Client Application using the following command:


```bash
call C:\gigaspaces-xap-premium-8.0.0-ga\bin\setenv.bat
java -cp bin;%GS_JARS% -Djava.rmi.server.hostname=127.0.0.1 -DlocalMachineDemo=true com.test.scaledemo.ScaleDemoMain
```

- You should replace the XAP root folder and your machine IP with the relevant values.
- You will find a `run.bat` script you may use to run the client.

# Expected Instances Distribution

When running the GS-UI you will have the following displayed:




{{%tabs%}}
{{%tab "  Initial State "%}}
Initial State - agent without any GSCs running:

{{%popup   "/attachment_files/epu_demo1.JPG"%}}

{{% /tab %}}
{{%tab "  After initial Deploy "%}}
After initial Deploy:


{{%popup   "/attachment_files/epu_demo2.JPG"%}}


{{% /tab %}}
{{%tab "  After Scaling to 128 MB "%}}
After Scaling from 64.0 MB to 128 MB:


{{%popup   "/attachment_files/epu_demo3.JPG"%}}


{{% /tab %}}
{{%tab "  After Scaling to 256 MB "%}}
After Scaling from 128.0 MB to 256 MB:


{{%popup   "/attachment_files/epu_demo4.JPG"%}}


{{% /tab %}}
{{%tab "  After Scaling to 64 MB "%}}
After Scaling from 256.0 MB to 64 MB:


{{%popup   "/attachment_files/epu_demo5.JPG"%}}


{{% /tab %}}
{{% /tabs %}}

# Expected Output

The Client application will display the following output:

{{%tabs%}}
{{%tab "  Initial State "%}}


```bash
Welcome to GigaSpaces scalability Demo
Log file: C:\gigaspaces-xap-premium-X.x-ga\logs\2011-03-01~12.34-gigaspaces-service-127.0.0.1-6760.log
Created Admin - OK!
Data Grid PU not running - initial deploy
--- > Local Machine Demo - Starting initial deploy - Deploying a PU with:64MB
Tue Mar 01 12:34:53 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:0
Tue Mar 01 12:34:57 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:1
Tue Mar 01 12:35:01 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:1
Tue Mar 01 12:35:05 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:2
Tue Mar 01 12:35:09 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:2
Tue Mar 01 12:35:13 EST 2011>> Total Memory used:0.0 MB - Progress:0.0 % done - Total Containers:2
Tue Mar 01 12:35:17 EST 2011>> Total Memory used:64.0 MB - Progress:100.0 % done - Total Containers:2
Initial Deploy done! - Time to deploy system:32 seconds
```

{{% /tab %}}

{{%tab "  Scaling to 128 MB "%}}


```bash
About to start changing data-grid memory capacity from 64.0 MB to 128 MB
Hit enter to scale the data grid...

Tue Mar 01 12:37:02 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:2
Tue Mar 01 12:37:04 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:2
Tue Mar 01 12:37:06 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:3
Tue Mar 01 12:37:08 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:3
Tue Mar 01 12:37:10 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:4
Tue Mar 01 12:37:14 EST 2011>> Total Memory used:64.0 MB - Progress:50.0 % done - Total Containers:4
Tue Mar 01 12:37:17 EST 2011>> Total Memory used:96.0 MB - Progress:75.0 % done - Total Containers:4
Tue Mar 01 12:37:21 EST 2011>> Total Memory used:96.0 MB - Progress:75.0 % done - Total Containers:4
Tue Mar 01 12:37:25 EST 2011>> Total Memory used:96.0 MB - Progress:75.0 % done - Total Containers:4
Tue Mar 01 12:37:27 EST 2011>> Total Memory used:128.0 MB - Progress:100.0 % done - Total Containers:4
Data-Grid Memory capacity change done! - Time to scale system:27 seconds
```

{{% /tab %}}

{{%tab "  Scaling to 256 MB "%}}


```bash
About to start changing data-grid memory capacity from 128.0 MB to 256 MB
Hit enter to scale the data grid...

Tue Mar 01 12:38:21 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:4
Tue Mar 01 12:38:23 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:4
Tue Mar 01 12:38:25 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:5
Tue Mar 01 12:38:27 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:5
Tue Mar 01 12:38:29 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:6
Tue Mar 01 12:38:31 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:6
Tue Mar 01 12:38:33 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:7
Tue Mar 01 12:38:35 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:7
Tue Mar 01 12:38:37 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:8
Tue Mar 01 12:38:41 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:8
Tue Mar 01 12:38:43 EST 2011>> Total Memory used:160.0 MB - Progress:62.5 % done - Total Containers:8
Tue Mar 01 12:38:47 EST 2011>> Total Memory used:160.0 MB - Progress:62.5 % done - Total Containers:8
Tue Mar 01 12:38:51 EST 2011>> Total Memory used:160.0 MB - Progress:62.5 % done - Total Containers:8
Tue Mar 01 12:38:53 EST 2011>> Total Memory used:192.0 MB - Progress:75.0 % done - Total Containers:8
Tue Mar 01 12:38:57 EST 2011>> Total Memory used:192.0 MB - Progress:75.0 % done - Total Containers:8
Tue Mar 01 12:39:01 EST 2011>> Total Memory used:224.0 MB - Progress:87.5 % done - Total Containers:8
Tue Mar 01 12:39:05 EST 2011>> Total Memory used:224.0 MB - Progress:87.5 % done - Total Containers:8
Tue Mar 01 12:39:09 EST 2011>> Total Memory used:224.0 MB - Progress:87.5 % done - Total Containers:8
Tue Mar 01 12:39:11 EST 2011>> Total Memory used:256.0 MB - Progress:100.0 % done - Total Containers:8
Data-Grid Memory capacity change done! - Time to scale system:51 seconds
```

{{% /tab %}}

{{%tab "  Scaling to 64 MB "%}}


```bash
About to start changing data-grid memory capacity from 256.0 MB to 64 MB
Hit enter to scale the data grid...

Tue Mar 01 12:40:11 EST 2011>> Total Memory used:256.0 MB - Progress:25.0 % done - Total Containers:8
Tue Mar 01 12:40:14 EST 2011>> Total Memory used:224.0 MB - Progress:28.6 % done - Total Containers:7
Tue Mar 01 12:40:18 EST 2011>> Total Memory used:192.0 MB - Progress:33.3 % done - Total Containers:7
Tue Mar 01 12:40:22 EST 2011>> Total Memory used:192.0 MB - Progress:33.3 % done - Total Containers:6
Tue Mar 01 12:40:26 EST 2011>> Total Memory used:160.0 MB - Progress:40.0 % done - Total Containers:6
Tue Mar 01 12:40:28 EST 2011>> Total Memory used:160.0 MB - Progress:40.0 % done - Total Containers:5
Tue Mar 01 12:40:32 EST 2011>> Total Memory used:128.0 MB - Progress:50.0 % done - Total Containers:5
Tue Mar 01 12:40:36 EST 2011>> Total Memory used:96.0 MB - Progress:66.7 % done - Total Containers:4
Tue Mar 01 12:40:38 EST 2011>> Total Memory used:96.0 MB - Progress:66.7 % done - Total Containers:3
Tue Mar 01 12:40:42 EST 2011>> Total Memory used:64.0 MB - Progress:100.0 % done - Total Containers:3
Data-Grid Memory capacity change done! - Time to scale system:33 seconds
```

{{% /tab %}}
{{% /tabs %}}

