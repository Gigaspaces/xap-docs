---
type: post122
title:  File Descriptors
categories: XAP122PROD
parent: none
weight: 800
---

The XAP LRMI communication layer opens network connections dynamically. With large-scale applications, or with clients that are running a large number of threads accessing the data grid, you might end up having a large number of file descriptors used both on the client and server side. You may also have multiple JVMs running on the machine. If so, you may have to increase the default max user processes value.

For Linux environments, if you don't have the correct file descriptor settings, there will be **Too many open files** error messages in the XAP processes log files or on the client side. Low file descriptor values will affect application stability, along with the ability of clients to connect to the XAP in-memory data grid.  Set both the **System** level File Descriptor Limit and the **Process** level File Descriptor Limit to high values to avoid these problems (see below for instructions).

# System File Descriptor Limit

To change the number of file descriptors in Linux, as the `root` user edit the following line in the `/etc/sysctl.conf` file:


```bash
fs.file-max = 300000
```
300000 will be the new file descriptor limit that you want to set.

Apply the change by running the following command:


```bash
/sbin/sysctl -p
```

Verify your settings using:

```bash
cat /proc/sys/fs/file-max 
```

OR 


```bash
sysctl fs.file-max
```


# Process File Descriptor Limit

The Linux OS by default has a relatively small number of file descriptors available and max user processes (1024). You should make sure that your standalone clients, or GSA/GSM/GSC/LUS using a user account which have its **maximum open file descriptors (open files) and max user processes** configured to a high value. A good value is 32768.

Setting the **max open file descriptors** and **max user processes** is done via the following call:


```bash
ulimit -n 32768 -u 32768
```

Alternatively, you should have the following files updated:


```bash
/etc/security/limits.conf

- soft    nofile          32768
- hard    nofile          32768

/etc/security/limits.d/90-nproc.conf

- soft nproc 32768
```

See [increase-open-files-limit](https://rtcamp.com/tutorials/linux/increase-open-files-limit/) for more details.

# Monitoring Utilized File Descriptors

You can monitor the `MaxFileDescriptorCount` and `OpenFileDescriptorCount` using the JConsole:

![jmx-file-descriptors.png](/attachment_files/jmx-file-descriptors.png)

