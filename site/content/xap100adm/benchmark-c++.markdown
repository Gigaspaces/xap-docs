---
type: post100
title:  C++ Benchmark
categories: XAP100ADM
parent: benchmarking.html
weight: 400
---

{{% ssummary %}} {{% /ssummary %}}


The XAP c++ API benchmark framework provides a simple report of the average response time in different scenarios, running with different space topologies and activating different types of operations and space classes.
You can import the report to any spread sheet (e.g. Excel) for further processing and for producing graphical results.
You can plug-in your own space entry classes in case the ones provided with the benchmark do not fit your needs.

{{% tip %}}
The benchmark source code can be found under: `<XAP Root>\cpp\examples\benchmark`.
{{%/tip%}}

# Benchmark Operations

The benchmark tests the following operations:

- write
- update
- read
- take
- notify
- writeMiltiple
- readMultiple
- takeMultiple
- GSIterator
- query

# Benchmark Command Arguments

Here are the different command arguments that the c++ benchmark program accepts:

1. \<url\> -- Space url. This can be a remote clustered space or an embedded one.
2. \<warmups\> -- Number of runs before starting the actual benchmark
3. \<repeats\> -- Number of times to re-run the whole benchmark
4. \<threads\> -- Number of concurrent threads
5. \<iterations\> -- Total amount of entries in the space
6. \<payload\> -- Payload size for each entry (for Payload object type)
7. \<batch size\> -- Size for batch operations
8. \<Space Class Type\> -- Entry class/type. See [Supported Space Class Types](#Supported Space Class Types) for details.
9. \<use transactions\> -- Whether to use transactions and their type. Options are: true\|false\|jini (true -- use Local Transactions, jini - use Jini Transactions).
10. \<Test Name\> (Optional) -- Can have any of the operations listed in [Benchmark Operations](#Benchmark Operations).


# Supported Space Class Types

The benchmark can accept any of the following Class Types:

- Payload --- Object that includes a Payload `string` field type with variable size
- Mixed --- Object with two `string` fields, one `boolean` field and one `long` field
- Doubles --- Object with 15 `double` fields
- PayloadFifo --- Same as Payload, except that it uses _fifo_ flag.
- ComplexObjectWithInnerBinaryPOJO --- Aggregated object that includes an inner _Payload_ field stored as _binary_ type.

All the classes above inherit from benchmarkBase Class, which includes a `long` type uid (Indexed field) and a `string` based field storing the object UID.

# Benchmark Command Examples

- Benchmark command example communicating with a remote space:


```bash
benchmark "jini://localhost/*/mySpace" 1 1 2 10000 1000 100 Payload false
```

- Benchmark command example communicating with an embedded space (collocated with the benchmark program running in the same process):


```bash
benchmark "/./mySpace" 1 1 2 10000 1000 100 Payload false
```

The benchmark produces an output file that includes the test name, its duration and the average throughput of the tested operation.

# Benchmark Files Location

The benchmark program is located at:


```bash
<XAP Root>\cpp\examples\benchmark\
```

The benchmark classes xml declaration file is located at:


```bash
<XAP Root>\cpp\examples\benchmark\serializer\benchmark.gs.xml
```

The benchmark generated serializer code is located at:


```bash
<XAP Root>\cpp\examples\benchmark\serializer\
```
The benchmark serializer DLL is located at:


```bash
<XAP Root>\cpp\lib\platform\native\
```

The benchmark program executable is located at:


```bash
<XAP Root>\cpp\bin\$(PLATFORM)\$(COMPILER)\
```
# Adding your Space Class

To add your own Space Class into the benchmark framework you should add a class inherited from the Benchmark class and add your code.
You should add into the `<XAP Root>\cpp\examples\benchmark\serializer\benchmark.gs.xml` your class space declaration that is supported by your new benchmark implementation.
