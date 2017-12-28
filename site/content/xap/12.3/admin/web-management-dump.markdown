---
type: post123
title:  Generating a Dump File
categories: XAP123ADM, PRM
parent: web-management-common-view.html
weight: 500
---


{{% ssummary  %}}{{% /ssummary %}}


 
 
You can generate a dump file that includes information about the runtime GigaSpaces environment for a specific host or across the entire environment.

Start the WEB-UI, under the Generate Dump tab select the relevant option:
{{% align center %}}
![WEB-UI1.png](/attachment_files/WEB-UI1.png)
{{% /align %}}

This can be done for all hosts, all hosts but only information on GSCs, or just an overview.
For a specific host, you can click the tool-box icon.

{{% align center %}}
![WEB-UI3.png](/attachment_files/WEB-UI3.png)
{{% /align %}}

Specific GSC dump:

{{% align center %}}
![WEB-UI4.png](/attachment_files/WEB-UI4.png)
{{% /align %}}

Any of the above will display the following dialog: 

{{% align center %}}
![dump3](/attachment_files/dump/dump-3.png)
{{% /align %}}

Select the desired options and click the  **Generate**  button.

* The "JVM Heap Dump" option determines wether a JVM heap dump should be generated for the JVMs. It is recommended to choose this only on a specific host or GSC that a Java heap dump should be generated for.
* The "Only Live Services Log Dump" determines if dump should include only services which are currently running, or also services which have been terminated (useful for trouble-shooting failover scenarios)

## Generate Overview

The "Generate Overview" will provide general information on the system deployment.
This information is useful to quickly gather the system settings in production, for example when reporting a support case.

When clicking on the Generate Overview option:

{{% align center %}}
![WEB-UI5.png](/attachment_files/WEB-UI5.png)
{{% /align %}}

This will download a zip file containig an overview.txt file directly to the downloads folder.
The dump overview structure will look like this:

```bash
Overview
	Time        2016-09-18 14:05:16.101
	XAP version ENTERPRISE
	Client      License Version=12;Type=type;Customer=customer;Expiration=never;Hash=hash
	Number of Machines 1
	Grid Components GSA(1), LUS(1), GSM(1), GSC(2), ESM(0)

Processing Units (none)

Hosts
	192.168.33.159[192.168.33.159] Cores [4]
		JVM Version	Oracle Corporation Java HotSpot(TM) 64-Bit Server VM 1.8.0_71
		Utilization	cpu[7.5%] memory[11GB (66%)] 
		GSA(1), LUS(1), GSM(1), GSC(2), ESM(0)
		GSCs
			GSC[54416] Heap: Init[512MB], Max[491MB], Used[82MB], Committed[491MB]
			GSC[54419] Heap: Init[512MB], Max[491MB], Used[98MB], Committed[491MB]
...

```

 

# Dump File Structure

The dump file structure would look like this:


```bash
dump_file.zip

    gsa-10.10.10.249-23610--1284928573201
        network.txt
        summary.txt
        threads.txt
        logs
            2010-09-19~08.22-gigaspaces-gsa-10.10.10.249-23610.log

    gsc-10.10.10.249-23739--1284928573169
        network.txt
        summary.txt
        threads.txt
        logs
            2010-09-19~08.22-gigaspaces-gsc_1-10.10.10.249-23739.log
        processing-units
            space
                1
                    pu.xml
                    spaces
                        space
                            summary.txt

    gsc-10.10.10.249-23766--1284928573079
        network.txt
        summary.txt
        threads.txt
        logs
            2010-09-19~08.22-gigaspaces-gsc_2-10.10.10.249-23766.log
        processing-units
            mirror
                1
                    pu.xml
                    spaces
                        mirror
                            summary.txt
            space
                1_1
                    pu.xml
                    spaces
                        space
                            summary.txt

    gsm-10.10.10.249-24112--1284928573193
        network.txt
        summary.txt
        threads.txt
        logs
            2010-09-19~08.22-gigaspaces-gsm_3-10.10.10.249-24112.log
    lus-10.10.10.249-24127--1284928573201
        network.txt
        summary.txt
        threads.txt
        logs
            2010-09-19~08.22-gigaspaces-lus_4-10.10.10.249-24127.log
```

