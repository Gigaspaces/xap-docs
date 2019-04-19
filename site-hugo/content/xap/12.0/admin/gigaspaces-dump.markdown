---
type: post120
title:  Dump
categories: XAP120ADM, PRM
parent: gigaspaces-management-center.html
weight: 900
canonical: auto
---

 

You can generate a dump file that includes information about the runtime XAP environment for a specific GSC or across the entire environment.
Start the GS-UI , move into the Hosts tab and select the relevant option:
Full environment dump:

{{% align center %}}
![dump0.jpg](/attachment_files/dump0.jpg)
{{% /align %}}

Specific machine dump:

{{% align center %}}
![dump1.jpg](/attachment_files/dump1.jpg)
{{% /align %}}

Specific GSC dump:

{{% align center %}}
![dump3.jpg](/attachment_files/dump3.jpg)
{{% /align %}}

Any of the above will display the following dialog. Place the location of the dump file , select the desired options and click the **Generate** button.

{{% align center %}}
![dump2.jpg](/attachment_files/dump2.jpg)
{{% /align %}}

{{% note %}}
Make sure the user that started the GS-UI has write permissions to the location of the dump file.
{{% /note %}}

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

