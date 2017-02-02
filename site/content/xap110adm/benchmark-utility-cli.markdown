---
type: post110
title:  Utility
categories: XAP110ADM
parent: benchmarking.html
weight: 200
---






The benchmark example provides a good tool for running performance benchmarks on the cache in various scenarios. This program performs a loop of `write/put` and `read/get` or `take/remove` operations from a space according to a different set of parameters. The result is the average time it took to perform the operations. You can define a sampling rate to allow you to track the intermediate performance while the benchmark example is running. You may also dump the results into a file to be converted into graphs and analyzed using a spreadsheet and analysis tools.

You can find the benchmark in the `<XAP Root>\tools\benchmark` directory.

Typing `run.bat -h` in the console displays:

### A detailed list of options for:

- Setup
- Performing operations
- Operation options
- Setting cluster topologies
- Viewing statistics

### A set of examples.

This print-out is displayed below:



Option arguments in square brackets [] are required;{{<wbr>}}
Option arguments in triangular brackets <> are optional.


#### Setup:


-objecttype Defines the object type that will be written to the space.
Usage: -objecttype ext

Other possible values are:


|Option | Description|
|:------|:-----|
|entry | Object implements net.jini.core.entry.Entry interface.|
|pojo | Plain Java object|
|uid | Object extends com.j_spaces.core.client.MetaDataEntry using the ClientUIDHandler.|
|fifo | Object extends com.j_spaces.core.client.MetaDataEntry and sets FIFO=true.|
|ser | Object implements net.jini.core.entry.Entry and java.io.Serializable interfaces. {{<wbr>}}Contains additional field with a complex list, therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms|
|ext | Object implements net.jini.core.entry.Entry and java.io.Externalizable interfaces.{{<wbr>}}Contains additional field with a complex list, therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms|
|pojo-ext | Plain Java object implements java.io.Externalizable interface.{{<wbr>}}Contains additional field with a complex list, therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms|
|fifo-ext | Object extends com.j_spaces.core.client.MetaDataEntry implements java.io.Externalizable interface and sets FIFO=true.{{<wbr>}}Contains additional field with a complex list, therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms|
|uid-ext | Object extends com.j_spaces.core.client.MetaDataEntry{{<wbr>}}implements java.io.Externalizable interface and using the ClientUIDHandler. Contains additional field with a complex list,{{<wbr>}}therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms|
|mde-ext | Object extends com.j_spaces.core.client.MetaDataEntry implements java.io.Externalizable interface.{{<wbr>}}Contains additional field with a complex list, therefore should NOT be compared with the following objects: entry, pojo, uid, fifo, jms |
|jms | com.j_spaces.jms.GSSimpleMessageImpl, a basic JMS message which extends com.j_spaces.core.client.MetaDataEntry {{<wbr>}}implements java.io.Externalizable and javax.jms.Message interfaces.{{<wbr>}}For basic JMS send or sync/async receive, pass -write for JMS send, -take for JMS sync receive or -notify for JMS async receive.|
|-clean  |       Clean space before benchmark starts |
|-url [url] |Connection url; If none provided the one defined as part of the script file will be used  |
|-f [FileName] |dump results into file. Works with -showrate option. Default File name is BenchMarkResult`Date`_`Time`.|
|-pause |Pauses once connection with space is established.|
|-exit | Benchmark will hang once finished. When used with embedded space, space will still be alive.|


### Operations:



|Option | Description|
|:------|:-----|
|write |perform first operation as write/put |
|read |perform second operation as read/get  |
|update |perform second operation as update (with uid)|
|take |perform second/third operation as take/remove|
|all third| perform all operations - first, second and then|
|execute [first/second ] |enables execution of only first or second operation.|
|bench | perform first-*second-second-third operation with uid|


### Options:


|Option | Description|
|:------|:-----|
|-i <number of iterations> |number of iterations; default is 1000 |
|-lease [time in ms] |lease timeout in milliseconds of the write/put operation   |
|-t [time in ms]| timeout in milliseconds of the read/take/get/remove operations |
|-s [size in bytes] |set byte size of the entry object.  |
|-content |changes the content value each iteration     |
|-tr [number of threads] |number of threads performing each operation|
|-rangefirst [from-to] |will perform first operation with ids/keys between range|
|-rangesecond [from-to]| will perform second operation with ids/keys between range |
|-repeatfirst [repeats] |repeat times of first operation|
|-repeatsecond [repeats] |repeat times of second operation  |
|-m [batch size] |specifies the batch size when performing batch operations |
|-rand <number of iterations> |will randomize second operation ids/keys in a range; default is by -i|
|-tx [nth-iteration] |tx will be committed every nth-iteration; if zero, as specified by -i|
|-delaywrite [t ms] |delays first operation every t milliseconds    |
|-delayreadtake [t ms] |delays second & third operation every t milliseconds |
|-writerate [Max Rate msg/sec] |will limit the first operation rate (msg in 1 second) for all threads |
|-notify |registers for notifications on all events |
|-parallel |parallel execution of the operations |
|-clusteredoperation |This parameter indicates if operation will be performed using a clustered proxy |
|-returnlease |This parameter indicates if write operation will return a lease object|



### Transactions:


|Option | Description|
|:------|:-----|
|tx 2000 |Perform operation under local transaction, commit every 2000 operations.|
|dtx 1000| Perform operation under distributed transaction, commit every 1000 operations.|


### Topologies:


|Option | Description|
|:------|:-----|
|-cache |Turn on local cache mode; default is false |
|-map |Map API - first/second/third operations as: put, get, remove.|
|-map -cache |Map API with local cache.  |
|-target \[space name\] |Space name to perform second operations on (in a cluster configuration)|
|-hashtable |use java.util.Hashtable API- first/second/third operations as: put,get, remove |


### Statistics:


|Option | Description|
|:------|:-----|
|-showrate <iteration cycle> |global throughput will be displayed every iteration cycle; default is 1000|
|-showthreadrate <iteration cycle> |thread throughput will be displayed every iteration cycle; default is 1000|
|-stress \[repeat times\] |runs the entire benchmark as many as 'repeat times' as stated |


#### Examples:



|Option | Example|
|:------|:-----|
|-f |-f resultsfile.xsl -showrate {{<wbr>}}dump results into resultsfile.xsl  |
|-execute |-execute first    {{<wbr>}}will perform only write/put operations   |
|-execute |-execute second    {{<wbr>}}will perform only read operations     |
|-execute |-execute second -take {{<wbr>}}will perform only take operations |
|-bench |-bench -map   {{<wbr>}}will perform put,put(update),get,remove with uid |
|-bench |-bench      {{<wbr>}}will perform write,update,read,take with uid|
|-lease |-i 1000 -lease 20000 -execute first {{<wbr>}}perform 1st operation with entry lease of 2 sec |
|-t | -i 1000 -t 20000 -execute second {{<wbr>}}perform 2nd operation with timeout of 2 sec |
|-s |-s 1000         {{<wbr>}}define 1K size entries each|
|-content |-i 1000 -content -s 1000  {{<wbr>}}defines an entry with a changing content of 1K |
|-tr |-i 1000 -tr 4   {{<wbr>}}will perform 1000 iterations by each of the 4 threads |
|-rangefirst |-rangefirst 1000-2000  {{<wbr>}}will write/put ids/keys from 1000 to 2000 |
|-rangesecond |-rangesecond 1000-2000 {{<wbr>}}will read/take/get/remove ids/keys from 1000 to 2000 |
|-repeatfirst |-i 1000 -repeatfirst 10  {{<wbr>}}will write/put 10 times 1000 entries  |
|-repeatsecond |-i 1000 -repeatsecond 10 {{<wbr>}}will read/get or take/remove 10 times 1000 entries |
|-m |-m 100 -i 1000   {{<wbr>}}will use 10 batches of 100 to perform operations on 1000 entries.|
|-rand |-i 1000 -rand  {{<wbr>}}will randomize second operation ids/keys from 0 to 1000 |
|-rand |-i 10000 -rand 40000  {{<wbr>}}will randomize between ids/keys in the range of 0-40000|
|-rand |-rangesecond 10000-25000 -rand   {{<wbr>}}will randomize between 10000-25000        |
|-writerate |-i 100000 -writerate 50000 -tr 4  {{<wbr>}}4 threads will write 100000 entries each with a maximum TP rate of 50000 msg/sec |
|-notify |-notify -i 1000 -tr 5  {{<wbr>}}will start 5 threads that will write and notify 1000 entries|
|-parallel |-parallel -i 1000 -tr 5   {{<wbr>}}will start 5 parallel threads, each performing 1000 operations|
|-clusteredoperation |-clusteredoperation -i 100000 {{<wbr>}} will write 100000 objects to entire cluster  |
|-returnlease |-returnlease -i 100000  {{<wbr>}}will write 100000 objects with returning Lease object  |
|-dtx-manager-url |-dtx 1000 dtx-manager-url thunder1:3733 {{<wbr>}}Use distributed transaction. Commit every 1000 operations. {{<wbr>}}Transaction Manager registered on LUS that is running on machine thunder1:3733 |
|-cache |-cache     {{<wbr>}}will use local cache to remote space.|
|-map |-map -all -i 1000  {{<wbr>}}will put/get/remove 1000 entries.|
|-map |-map -cache -i 1000 -repeatsecond 4 {{<wbr>}}will put 1000 entries, first get from space, successive {{<wbr>}}gets from local cache. |
|-target |-target rmi://host:port/container/space_name  {{<wbr>}}will perform second operation this target space |
|-showrate |-showrate 10000{{<wbr>}}will show TP every 10000 iterations |
|-showthreadrate |-showthreadrate 10000  {{<wbr>}}will show each thread-TP every 10000 iterations|
|-stress |-map -all -stress 10     {{<wbr>}}will run 10 cycles of map api put/get/remove |





{{%anchor 61%}}

{{%tip%}} The benchmark results are printed to an Excel file (using the `FileName` parameter), which allows you to easily sort your results. To do this, open your printed Excel file, and select *Filter* > *AutoFilter* from the *Data* menu at the top.
{{%/tip%}}

{{%info%}} The `rangefirst` and `rangesecond` parameters are supported *only with single operations* (and not with multiple/batch operations).{{%/info%}}

# More Examples

The following example arguments should be passed to the runTest script after loading a remote Space using startAll script.
e.g. ./runTest.sh -read -i 1000


The following example uses the JavaSpaces API, writes 1,000 Entries into the space, 1K each, and reads them back into the client.

```bash
-read -objecttype entry -i 1000 -s 1024
```

The following example uses the JavaSpaces API, writes 1,000 POJOs into the space, 1K each, and takes them from the space. A remote cache is accessed.


```bash
-take -objecttype pojo -i 1000 -s 1024
```

The following example uses the JavaSpaces API, writes 1,000 Entries into the space in FIFO mode {{%currentjavanet "fifo-support.html" %}}, 1K each, and reads them back into the client three times.


```bash
-read -objecttype fifo -i 1000 -s 1024 -rt 3
```

The following example uses the Map API, puts 1,000 Entries into the space, 1K each, and gets them back into the client.


```bash
-map -read -i 1000 -s 1024
```

The following example uses the Map API, puts 1,000 Entries into the space, 1K each, and removes them from the space. A remote space is accessed.


```bash
-map -take -i 1000 -s 1024
```

The following example uses the Map API, puts 1,000 Entries into the space, 1K each, and gets them back into the client three times.


```bash
-map -read -i 1000 -s 1024 -rt 3
```

The following example uses the Map API, puts 100,000 Entries into the space, gets them back, and removes them from the space. Throughput is displayed every 10,000 operations.


```bash
-map -all -i 100000 -showrate 10000
```

The following example uses the JMS API, sends 10,000 JMS messages into the space (_MessageProducer.send()_), and receives them asynchronously (_MessageListener.onMessage()_) with notifications.


```bash
-i 100000 -notify -objecttype jms
```

The following example uses the JMS API, sends 100,000 JMS messages into the space (_MessageProducer.send()_), and receives them synchronously (_MessageConsumer.receive()_). Throughput is displayed every 10,000 operations.


```bash
-i 100000 -take -objecttype jms -showrate 10000
```

