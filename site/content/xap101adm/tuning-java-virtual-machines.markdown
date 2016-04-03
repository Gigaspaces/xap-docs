---
type: post101
title:  Java Virtual Machines
categories: XAP101ADM
parent: tuning.html
weight: 300
---

{{% ssummary %}}  {{% /ssummary %}}

XAP, being a Java process, requires a Java virtual machine (JVM) to run. As part of configuring  XAP, you can fine-tune settings that enhance system use of the JVM. A JVM provides the runtime execution environment for Java-based applications. XAP can run on JVMs from different JVM providers. When GigaSpaces starts it writes information about the JVM, including the JVM provider information, into this  log file and the standard output.

Even though JVM tuning is dependent on the JVM provider, general tuning concepts apply to all JVMs.

# Garbage Collector

The garbage collector is the key to effectively managing the JVM memory system, which is the ultimate goal of JVM tuning. Garbage collection is the process of clearing dead objects from the heap, thus releasing that space for new objects.

## Application Throughput

The garbage collector is optimized for application throughput. This means that the garbage collector works as effectively as possible, giving as many CPU resources to the Java threads as possible. This may, however, cause non-deterministic pauses when the garbage collector stops all Java threads for garbage collection. The throughput priority should be used when non-deterministic pauses do not impact the application's behavior.

## Pause Time

The garbage collector is optimized to limit the length of each garbage collection pause where all Java threads are stopped for garbage collection. This may result in lower application throughput, as the garbage collector uses more CPU resources in total than when running with the throughput priority. The pause time priority should be used when the application depends on an even performance.

# Why and When to Perform this Task

GigaSpaces, being a Java process, requires a Java virtual machine (JVM) to run. As part of configuring  GigaSpaces, you can fine-tune settings that enhance system use of the JVM. A JVM provides the runtime execution environment for Java-based applications. GigaSpaces can run on JVMs from different JVM providers. When GigaSpaces starts it writes information about the JVM, including the JVM provider information, into this  log file and the standard output.
Even though JVM tuning is dependent on the JVM provider, general tuning concepts apply to all JVMs.
These general concepts include:

- Compiler tuning -- All JVMs use Just In Time (JIT) compilers to compile Java byte codes into native instructions during server runtime.
- Java memory or heap tuning - The JVM memory management function, or garbage collection, provides one of the greatest opportunities for improving JVM performance.
- Class loading tuning -- Loading too many classes can have a significant impact on the RAM footprint of your program. Fortunately, there are a number of techniques you can use to reduce the number of classes that are loaded.
- Garbage collection - A key feature of Java is its garbage-collected heap, which takes care of freeing dynamically allocated memory that is no longer referenced. Because the heap is garbage-collected, Java programmers don't have to explicitly free allocated memory.

## Optimize Startup Performance and Runtime Performance

In some environments, it is more important to optimize the startup performance of GigaSpaces rather than the runtime performance. In other environments, it is more important to optimize the runtime performance. By default, IBM JVMs are optimized for runtime performance while HotSpot based JVMs are optimized for startup performance.

The behavior of the Java JIT compiler has the greatest influence over whether startup or runtime performance are optimized. The initial optimization level used by the compiler influences the length of time it takes to compile a class method and the length of time it takes to start the server. For faster startups, you can reduce the initial optimization level that the compiler uses. This means that the runtime performance of your applications may be degraded because the class methods are now compiled at a lower optimization level.

It is hard to provide a specific runtime performance impact statement because the compilers might recompile class methods during runtime execution based upon the compiler's determination that recompiling might provide better performance. Ultimately, the size of the application will have a major influence on the amount of runtime degradation. Smaller applications have a higher probability of having their methods recompiled. Larger applications are less likely to have their methods recompiled.

## Set Heap Size

Generally, you may want to set the maximum heap size as high as possible, but not so high that it causes page faults for the application or for some other application on the same computer. Heap sizing is accomplished by using the `-Xms` (minimum heap size) and `-Xmx` (maximum heap size) options. The following command line parameters are useful for setting the heap size.

- -Xms  -- This setting controls the initial size of the Java heap. Properly tuning this parameter reduces the overhead of garbage collection, improving server response time and throughput. The default setting for this option is typically too low, resulting in a high number of minor garbage collections.
- -Xmx  -- This setting controls the maximum size of the Java heap. Properly tuning this parameter can reduce the overhead of garbage collection, improving server response time and throughput. The default setting for this is typically too low, resulting in a high number of minor garbage collections.
- -Xmn  -- This setting controls the size of the heap allocated for the young generation objects  it represents all the objects which have a short life of time. Young generation objects are in a specific location into the heap, where the garbage collector will pass often. All new objects are created into the young generation region (called "eden"). When an object survive is still "alive" after more than 2-3 gc cleaning, then it will be swap has an "old generation" : they are "survivor". A recommended value for the `Xmn` should be 25% of the `Xmx` value.

The size you should specify for the heap depends on your heap usage over time. In cases where the heap size changes frequently, you might improve performance if you specify the same value for the `Xms` and `Xmx` parameters.

## Garbage Collection

The recommendations in this section are for non-manager VMs only. Garbage collection, while necessary, introduces latency into your system by consuming resources that would otherwise be available to your application. If you are experiencing unacceptably high latencies in application processing, you might be able to improve performance by modifying your VM's garbage collection behavior. Garbage collection tuning options depend on the Java virtual machine you are using.

Suggestions given here apply to the Sun HotSpot VM. If you use a different JVM, check with your vendor to see if these or comparable options are available.

Modifications to garbage collection sometimes produce unexpected results. Always test your system before and after making changes to verify that the system's performance has improved. The two options suggested here are likely to expedite garbage collecting activities by introducing parallelism and by focusing on the data that is most likely to be ready for cleanup. The first parameter causes the garbage collector to run concurrent to your application processes. The second parameter causes it to run multiple, parallel threads for the "young generation" garbage collection (that is, garbage collection performed on the most recent objects in memory -- where the greatest benefits are expected):
    -XX:+UseConcMarkSweepGC -XX:+UseParNewGC

For application VMs, if you are not using shared memory and you are using remote method invocation (RMI) Java APIs, you might also be able to reduce latency by disabling explicit calls to the garbage collector. The RMI internals automatically invoke garbage collection every sixty seconds to ensure that objects introduced by RMI activities are cleaned up. Your VM may be able to handle these additional garbage collection needs. If so, your application may run faster with explicit garbage collection disabled. You can try adding the following command-line parameter to your application invocation and test to see if your garbage collector is able to keep up with demand:
    -XX:+DisableExplicitGC

{{% refer %}}
For more details, see [Moving into Production Checklist JVM Tuning](./moving-into-production-checklist.html).
{{% /refer %}}

## Remote GC

java-version
The [sun rmi dgc gcInterval](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/technotes/guides/rmi/sunrmiproperties.html)  properties are set by default to 60000 milliseconds (60 seconds).

In some cases this might cause the JVM process to slow down every 60 seconds. To reduce the performance impact of redundant GC cycles, increase the interval to be an hour (3600000 milliseconds) both for the space JVM and the client JVM.

{{% tip %}}
When starting the space in embedded mode or running it in remote mode using the `gsInstance` or `gsc` commands make sure you have the following system properties:

- `-Dsun.rmi.dgc.client.gcInterval=36000000`
- `-Dsun.rmi.dgc.server.gcInterval=36000000`
{{% /tip %}}

# Soft References Cleanup

Softly reachable objects will remain alive for some amount of time after the last time they were referenced. The default value is one second (1000) of lifetime per free megabyte in the heap. This value can be adjusted using the `-XX:SoftRefLRUPolicyMSPerMB` flag, which accepts integer values representing milliseconds per MB of free memory.

java-version
If the application using large amount of [Soft references](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/api/java/lang/ref/SoftReference.html) you may want to tune the `-XX:SoftRefLRUPolicyMSPerMB` to have a smaller value than the default.

# UseBiasedLocking JVM Option

The `UseBiasedLocking` JVM option impacts GigaSpaces performance when running synchronized replication (and also one-way mode) when used with **Solaris 10**. Please avoid using this option with clients accessing the space and space servers JVM.

{{% refer %}}
For more details, see the [Java Tuning White Paper](http://java.sun.com/performance/reference/whitepapers/tuning.html).
{{% /refer %}}

`-XX:+UseBiasedLocking` enables a technique for improving the performance of un contended synchronization. An object is "biased" toward the thread which first acquires its monitor via a monitorenter byte code or synchronized method invocation; subsequent monitor-related operations performed by that thread are relatively much faster on multiprocessor machines.

Some applications with significant amounts of un contended synchronization may attain significant speedups with this flag enabled; some applications with certain patterns of locking may see slowdowns, though attempts have been made to minimize the negative impact.

# Tuning 64-bit JVM

On 64-bit systems, the call stack for each thread is allocated with 1MB of memory space. Most threads do not use that much space. Using the `-XX:ThreadStackSize=256k` flag, you can decrease the stack size to 256k to allow more threads.

The `-Xmn<size>` flag lets you manually set the size of the "young generation" memory space for short-lived objects. If your application generates lots of new objects, you might improve GCs dramatically by increasing this value. The "young generation" size should almost never be more than 50% of heap. A recommended value for the `Xmn` should be 25% of the `Xmx` value.

The -XX:+UseCompressedOops option can improve performance of the 64-bit JRE when the Java object heap is less than 32 gigabytes in size. In this case, HotSpot compresses object references to 32 bits, reducing the amount of data that it must process. Available in Sun JVM since JDK6u14.

# References

**General:**

- [Java Performance Tuning](http://www.javaperformancetuning.com/)
- [Sun; Java Platform Performance: Stratergies  and Tactics](http://java.sun.com/docs/books/performance/)
- [Sun; Chapter 6 - Controlling Class Loading](http://java.sun.com/docs/books/performance/1st_edition/html/JPClassLoading.fm.html)
- [Scaling Enterprise Java on 64-bit Multi-Core X86-Based Servers](http://www.onjava.com/lpt/a/6781)

**Oracle Sun JVM:**

- [Sun; Java HotSpot VM Options](http://java.sun.com/docs/hotspot/VMOptions.html)
- [NetBeans; Tuning JVM switches for performance](http://performance.netbeans.org/howto/jvmswitches/)
- [Sun; Tuning Garbage Collection with the 5.0 Java Virtual Machine](http://java.sun.com/docs/hotspot/gc5.0/gc_tuning_5.html)

**IBM JVM:**

- [Tuning Java virtual machines](http://publib.boulder.ibm.com/infocenter/wasinfo/v6r0/index.jsp?topic=/com.ibm.websphere.nd.doc/info/ae/ae/tprf_tunejvm.html)
- [IBM developer kits](http://www-128.ibm.com/developerworks/java/jdk/)
- [Diagnosis documentation](http://www-128.ibm.com/developerworks/java/jdk/diagnosis/)
- [Tuning](http://publib.boulder.ibm.com/infocenter/wasinfo/v5r1/index.jsp?topic=/com.ibm.websphere.nd.doc/info/ae/ae/welc_tuning.html)

**Tuning Operating Systems:**

- [IBM; Tuning operating systems](http://publib.boulder.ibm.com/infocenter/ws60help/topic/com.ibm.websphere.nd.doc/info/ae/ae/tprf_tuneopsys.html)
