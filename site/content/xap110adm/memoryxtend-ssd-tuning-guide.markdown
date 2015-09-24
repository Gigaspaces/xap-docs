---
type: post110adm
title:  Advanced Tuning Guide
categories: XAP110ADM
parent: memoryxtend-ssd.html
weight: 300
---


{{% ssummary %}}  {{% /ssummary %}}



# Configuration


In order to sustain read/write speeds anywhere near RAM-based performance more than a single SSD is required. Current drives can at best yield ~500MB write/~550 MB read at 50k IOPs/sec.


In order to determine what level of speed is required in any particular project, the first value should be the number of reads and/or write required per second. Secondly, the percentage of writes versus reads is also very important, as when the device writes and reads at the same time the overall performance tends to degrade, to the point where reads can only be performed at the write rate. There are exceptions here, specifically with regards to SAS/SATA SSDs, which can come in different flavors, some tuned for reads, some balanced, and others write-enhanced.

Here is an example of a machine, which has two SSDs in RAID0:


```bash
sudo hdparm -tT /dev/mapper/isw_bhjbdcgibg_Volume0:

 Timing cached reads:   12760 MB in  1.99 seconds = 6396.92 MB/sec
 Timing buffered disk reads: 2630 MB in  3.00 seconds = 876.09 MB/sec
```

This means the machine can read one CD from the volume in one second. If you have a magnetic disk you should probably see something like ~100 MB/sec.


### Aggregation

Most installations will require greater read/write performance than that offered by a single drive. This necessarily involves RAID technology. There are only two RAID designations we will be considering, RAID0 and RAID10. RAID0 means that data being stored is separated, or 'striped', ie. partitioned, to all the drives. RAID10 is the same, with the exception that every stripe has a backup.



#	Operating System

##	Linux

Currently this only works on Linux. In the future it should be possible to run on *BSD and Solaris, but Windows is not definite. The Blobstore installer should install all of its dependencies and configure itself.


# Tools and Utilities

##	Installing Linux Tools

Firstly, keep in mind that these commands may or may not exist on the machine(s) you are using and you will need to install them if they don't. The basics are as follows (and yes, most of these commands need to be 'sudo'):


```bash
sudo yum search 'command'  - this command helps you find the name of a package that contains certain things, ie. libraries or executables

sudo yum install 'package' - this command installs the specified package
```

##	Required Utilities

The following tools are necessary:


```bash
sudo parted <device> - provides the same functionality as fdisk but knows about GPT

sudo mdadm <> - creates, modifies, or destroys a kernel-based raid device

sudo hdparm -tT <device> - gives 'ballpark' drive performance rating

sudo iostat -h -x -d /dev/sdb 1 - gives partition level statistics for a device, updated every second
```

##	Creating MDADM RAID0/RAID10 Devices

The instructions below refer to the following commands: parted/gdisk, hdparm, iostat, mdadm

These are the actual instructions you should issue to create an array using mdadm; the example uses 8 drives. Ideally you will use 4kb per drive to determine the stripe size, so in this case 8x4 = 32, OR 4x4 = 16.

#### this is for a non-replicated array - it is call 8,0

```bash
mdadm --create /dev/blobstore --level=raid0 --chunk=32KB --raid-devices=8 /dev/sda /dev/sdb /dev/sdc /dev/sdd /dev/sde /dev/sdf /dev/sdg /dev/sdh
```

#### this is for a replicated (backup) array - it is call 4,1

```bash
mdadm --create /dev/blobstore --level=raid10 --chunk=16KB --raid-devices=8 /dev/sda /dev/sdb /dev/sdc /dev/sdd /dev/sde /dev/sdf /dev/sdg /dev/sdh
```

##	Partitioning

Here are the specific instructions for using the Fusion-io cards:


```bash
1) 'fparted /dev/fioa' - then 'mklabel gpt' - this only needs to be done once

2) 'gdisk /dev/fioa' - please leave 1mb free at the beginning and create four primary partitions - i have not installed gdisk yet but willl do so later.

if for some reason you reboot the machine you will need to re-insert the module:

'insmod /lib/modules/2.6.32-431.el6.x86_64/extra/fio/iomemory-vsl.ko'

```

if you do double-check that '/dev/fioa' exists after the command returns. if it is partitioned there should be /dev/fioa1-4 as well.


#	Monitoring

Issuing the following command will return drive and partition statistics for a given device updated every second


```bash
iostat -h -x -p /dev/sdb 1
```

#	Performance

These are the recommended BIOS settings. Please note that they are somewhat specific to their devices.

1) Disable all C-states - no low power states

2) Set Power to Max Performance

3) Virtualization - disable all

4) Hyperthreading - disable

These settings, or a slightly different version, can be found in the following document [{{%pdf%}}](http://www.vmware.com/a/assets/vmmark/pdf/2014-02-18-HP-ProLiantDL580G8.pdf)


