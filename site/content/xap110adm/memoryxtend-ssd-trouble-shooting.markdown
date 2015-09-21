---
type: post110
title:  Troubleshooting
categories: XAP110ADM
parent: memoryxtend-ssd.html
weight: 400
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

## Initialization error

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

## Flash device malfunction

When a single flash device is not responding or has an hardware malfunction and you wish to replace it while your applicatin is running, you will need to perform the following steps:

- In space instance machine which attached to malfunctioned device at /tmp/blobstore/devices/device-per-space.properties (default) delete the space attached to the malfunctioned device.
- Restart the GSC of the above space.

For example /dev/sdc has an HW malfunction, you should replace the flash device, delete the second row which contains /dev/scd and restart GSC which contains mySpace_container1_1-mySpace.


```console
mySpace_container1-mySpace=/dev/sdb@Consistent^Sat Jul 18 10\:47\:48 GMT+02\:00 2015
mySpace_container1_1-mySpace=/dev/sdc@Consistent^Sat Jul 18 10\:47\:56 GMT+02\:00 2015
```


## Last Primary

When you see this exception in the backup space log:


```bash
	Space recovery failure.; Caused by: com.gigaspaces.internal.server.space.recovery.direct_persistency.DirectPersistencyRecoveryException:
```

This indicats that the last primary space, which it's name is written to a shared file, is not available to the backup space.The backup space will not take the primary role since only the last primary has the most updated data. Therefor this partition will not be available. 
In order to resolve this you will need to find why the primary space is not available, it could be a network disconnection or a storage error.


