---
type: post102adm
title:  Infrastructure
categories: XAP102ADM
parent:  tuning.html
weight: 100
---

{{% ssummary %}}{{% /ssummary %}}

# Check your Infrastructure First

No matter what kind of optimization you perform, you cannot ignore your infrastructure. Therefore, you must verify that you have the following:

- Sufficient physical and virtual memory
- Sufficient disk speed
- A tuned database
- Sufficient CPU power to handle the load
- Network cards configured for speed
- A JVM with a fast JIT

# Max Processes and File Descriptors/Handlers Limit

## Linux

Linux has a Max Processes per user, as well as the limit of file descriptors allowed (which relates to processes, files, sockets and threads). This feature allows you to control the number of processes an existing user on the server may be authorized to have.

To improve performance and stability, you must set the limit of processes for the super-user root to be at least **8192**, but note that 32,000:


```bash
ulimit -u 32000
```

{{% info %}}
Before deciding about the proper values of the file descriptors, a further testing and monitoring is required on the actual environment. 8K,16K or 32K is used just an example.
{{%/info%}}

{{% note %}}
Verify that you set the ulimit using the -n option e.g. ulimit -n 8192, rather than ulimit 8192. ulimit defaults to ulimit -f. If **no parameter** is set, it sets the maximum file size in 512k blocks, which might cause a fatal process crash
{{% /note %}}

### How do I configure the File Descriptors on Linux?

In /etc/system file, the descriptors **hard limit** should be set (8192), and the file descriptors **soft limit** should be increased from 1024 to 8192 as shown below:


```bash
set rlim_fd_max=8192
set rlim_fd_cur=8192
```

Edit /etc/system with root access and reboot the server. After reboot, please, run the following in the application account:
`ulimit -n`
It should report 8192.

{{% refer %}}
See also:

- [http://www.faqs.org/docs/securing/x4733.html](http://www.faqs.org/docs/securing/x4733.html)
- [http://www.ss64.com/bash/ulimit.html](http://www.ss64.com/bash/ulimit.html)
{{% /refer %}}

To change the default value, modify the `/etc/security/limits.conf` file.

{{% tip %}}
Modify the `ulimit` value when having many concurrent users accessing the space.
{{% /tip %}}

## Windows

Windows 2003 has no parameter dealing directly with the number of **file handles**, it is not explicitly limited, but file handles allocations take part of heap shared section which is relatively small (default 512KB). Heap being exhausted might lead to the application failure.

### How do I configure the File Handlers on Windows?

To increase it run regedit - HKEY_LOCAL_MACHINE->SYSTEM->CurrentControlSet->Control->Session Manager->Subsystems:
in the key "Windows" find "SharedSection=1024,3072,512", where 512KB is the size of heap shared section for the processes running in the background. The value should be increased, the recommendation is to increase it initially to 1024KB, max value 3072. **Reboot is necessary** to enable the new setting.


{{% refer %}}
[One of reports in Sun bug database](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4189011) describes the fixed bug (fix done in JVM 1.5 RC1) which mention file handles limit 2035 per jvm - the case has the test java code attached. It could be used to check the influence of the registry reconfiguration.
{{%/refer%}}

# TCP tuning

## Linux

### TCP_KEEPALIVE_TIME

**Description**: Determines how often to send TCP keepalive packets to keep an connection alive if it is currently unused
Should be changed in order to secure fast fail-over in case of network failure (e.g. router failure).
**Set**:


```bash
echo 1  > /proc/sys/net/ipv4/tcp_keepalive_time
```

{{% info %}}
Default value: 7200 seconds (2 hours){{<wbr>}}
Recommended value: 1 seconds
{{%/info%}}

### TCP_KEEPALIVE_INTERVAL

**Description**: Determines the wait time between isAlive interval probes.
**Set**:


```bash
echo 1 > /proc/sys/net/ipv4/tcp_keepalive_intvl
```

{{% info %}}
Default value: 75 seconds{{<wbr>}}
Recommended value: 1 seconds
{{%/info%}}

### TCP_KEEPALIVE_PROBES

**Description***: Determines the number of probes before timing out.
**Set**:


```bash
echo 5  > /proc/sys/net/ipv4/tcp_keepalive_probes
```

{{% info %}}
Default value: 9 {{<wbr>}}
Recommended value: 5 {{<wbr>}}
tcp_keepalive_interval is Solaris equivalent to the Linux TCP_KEEPALIVE_TIME setting. Default value in Solaris is 2 hours
{{% /info %}}

### Connection backlog

**Description**: Determines the maximum number of packets, queued on the input side, when the interface receives packets faster than kernel can process them.
**Set**:


```bash
echo 3000 > /proc/sys/net/core/netdev_max_backlog
```

{{% info %}}
Default value: 300{{<wbr>}}
Recommended value: 3000
{{%/info%}}

**Description**: Determines the maximum number of pending connection.
Should be changed when a high rate of incoming connection requests result in connection failures.
**Set**:


```bash
echo 3000 > /proc/sys/net/core/somaxconn
```

{{% info %}}
Default value: 128 {{<wbr>}}
Recommended value: 3000{{<wbr>}}
See also: [http://tldp.org/HOWTO/TCP-Keepalive-HOWTO/usingkeepalive.html](http://tldp.org/HOWTO/TCP-Keepalive-HOWTO/usingkeepalive.html)
{{% /info %}}

## Windows

To update the TCP parameters on widows run **regedit**.
All the TCP/IP parameters are registry values that are located under the subkeys of


```bash
HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Tcpip\Parameters
```

{{% refer %}}
See also:
- [http://support.microsoft.com/kb/314053](http://support.microsoft.com/kb/314053)
{{% /refer %}}

