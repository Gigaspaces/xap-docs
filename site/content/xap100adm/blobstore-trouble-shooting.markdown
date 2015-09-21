---
type: post100
title:  Troubleshooting
categories: XAP100ADM
parent: blobstore-overview.html
weight: 300
---

{{% ssummary %}}  {{% /ssummary %}}


#	OS-level problems

There have been initialization errors with certain devices, notably Fusion-io ioDrive2. This error is simply due to the sector configuration. The device must be low-level formatted with a 512-byte sector. This can be done by entering the BIOS at start time and reformatting the device.

#	ZetaScale issues

If a missing library is reported, it is usually reported by the library that is linked to it. In our case this would be one of the `libfdf_jni.so` files, or another `*.so` file.

The command below will return a list of the dependencies and indicate where they were found


```bash
ldd lib*.so
```

If a dependency can be found, try determining if it is on the system at all by using the following command:


```bash
find / -name lib<>.so
```

If it is present somewhere on the system, it is often enough to simply create a symlink to the location where the system expects to find it. Recently, libevent has been causing problems of this nature. The solution is as follows:

Run the following command:


```bash
find /usr -name libevent*
```

then run, as root (and it might be libevent.so.54)


```bash
ln -s /usr/lib-x86_64/libevent.so.53 /usr/lib-x86_64/libevent.so

or

ln -s /usr/lib/libevent.so.53 /usr/lib/libevent.so

or

or whatever was reported by find

```

#	Blobstore

When you receive the following error:


```console
Aug 12 10:24:00 2014 3cfcb700 fatal mcd_rec.c:638 read_label Invalid signature '' read from fd 0
```

this indicates that the device has not been properly initialized. This will probably be unnecessary in the future, but at the current time you should add the following section to the blob-store:sandisk-blob-store declaration:


```xml
 <blob-store:properties>
  <props>
    <prop key="FDF_REFORMAT">1</prop>
  </props>
 </blob-store:properties>
```

{{%warning%}}
You can also delete the blobstore contents by setting this value. Do not forget to comment it out for subsequent deployments
{{%/warning%}}

