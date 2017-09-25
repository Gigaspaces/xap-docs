---
type: post122
title:  Logging API
categories: XAP122ADM, OSS
parent: logging-overview.html
weight: 350
---

 
Part of the Administration API is an extensive logging extraction and filtering from different runtime components of XAP. Here is a very simple example:


```java
import static com.gigaspaces.log.LogEntryMatchers.*;

Admin admin = new AdminFactory().addGroup("myGroup").createAdmin();
// ...
admin.getGridServiceContainers().waitFor(1);
GridServiceContainer container = admin.getGridServiceContainers().getContainers()[0];

// get all the logs of the chosen container between 8 AM and 9 AM on November 15.
LogEntryMatcher matcher = afterTime("2009-11-15 8:00:00", beforeTime("2009-11-15 9:00:00"));
LogEntries logEntries = container.logEntries(matcher);
for (LogEntry logEntry : logEntries.logEntries()) {
    System.out.println(logEntry.getText());
}
```

The above example uses the Admin API in order to get the GridServiceContainer we wish to extract the logs from, and then extracts all the log entries ("log lines") between 8 AM and 9 AM on November 15.

# On Log Entry, Log Entries, and Matchers

At the base of the logging support is the `LogEntry` class. `LogEntry` represents an entry or a "row" in the results that are returned as part of the logging API call.

The `LogEntry` comes in two types, the first, is a `LOG` type. This type represents a single "log line" within the traversed log file(s) (A log line holder all the information in logger.log(...), including the exception). The `LogEntry` allows to access the actual text of the log line, and the timestamp of when it was taken.

The second type of a `LogEntry` is a `FILE_MARKER`. This represents the file at which the following `LogEntry` were extracted from. Most times, this should not care API users.

`LogEntries` represent a collection of `LogEntry` with additional meta data. `LogEntries` provide the host address and name, the process id, and process type (GSA, GSC, LUS, GSM) that the `LogEntries` were extracted from. Since most times an iteration over just the `LOG` type entries is required, a simple `LogEntries#logEntries()` method is provided that returns a list of only the `LOG` type entries.

`CompoundLogEntries` are a list of `LogEntries`. They exists in order to support extraction of logging information from several runtime components with a single API call. We will see an example of that later on when we see how we can get logging information from the GridServiceAgent.

The extraction of logging information is performed on the server side and filtering which logging information to extract is done using `LogEntryMatcher`. There are several built in `LogEntryMatcher`, all accessible through the static factory method class `LogEntryMatchers`.

`LogEntryMatcher` is responsible for both accumulating the log entries to return, as well as filtering them. The accumulating type matchers are very simple, with the `LogEntryMatchers.all` which is widely used which simply accumulates all the log entries that end up at it.

A `LogEntryMatcherFilter` is a special type (base class) of `LogEntryMatcher` which accepts a delegating `LogEntryMatcher` and controls if `LogEntry` will be passed to the delegating matcher or not (thus, filtering out certain log entries). Some examples of filtering log entry matchers include `AfterTime`, `BeforeTime`, `Regex` and so on. By default, most filtering matchers default to the `all` matcher as the delegating matchers if not explicitly defined.

A `StreamLogEntryMatcher` is a special type of matchers that can be reused between invocations and contains state that is stored on the calling side. They are very handy to perform operations such a "tail -f" of log files or iterating over large log files using chunking.

Here is an example of iterating over all the log file(s) of a specific container in chunks of 100 log entries with each call:


```java
LogEntryMatcher matcher = forwardChunk(size(100));
while (true) {
	LogEntries logEntries = container.logEntries(matcher);
	for (LogEntry logEntry : logEntries.logEntries()) {
	    System.out.println(logEntry.getText());
	}
}
```

And here is an example of implementing a "tail -f" type logic over the container logging (start with latest 100 log entries, and print new ones as they come):


```java
new Thread(new Runnable() {

    private LogEntryMatcher matcher = continuous(lastN(100));

    public void run() {
        while (true) {
            final LogEntries logs = container.logEntries(matcher);
            for (LogEntry log : logs.logEntries()) {
                System.out.println(log.getText());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}).start();
```

# Logging Extraction Process

Logging are extracted by running the provided matchers on the server side on the machine where the process is running. This can, potentially, be a heavy operation. For that reason, when using the Admin API, logging for runtime components will be automatically extracted by the Grid Service Agent if there is a Grid Service Agent that started the runtime component. If there isn't a Grid Service Agent that started the runtime component, then the logging extraction will be executed on the actual runtime component.

All logging extraction enabled runtime components implement the `LogProviderGridComponent` interface. The default `logEntries` method automatically uses the above mentioned logic. The `logEntriesDirect` method will always execute on the runtime component, without trying to go to the agent if the runtime component was started by one.

# Grid Service Agent

{{% note "Note"%}}
The Grid Service Agent (GSA) is a XAP Premium feature, and is not available in the open source edition.
{{%/note%}}

The Agent is just another component that its log files can be extracted by the above mentioned ways. It also provide additional ways to extract logs for runtime components it is managing. For example, extracting all the log information of all the different runtime components that are currently executed by the agent. Here is an example:


```java
new Thread(new Runnable() {

    private LogEntryMatcher matcher = continuous(regex(".*INFO.*", lastN(100)), regex(".*INFO.*"));

    public void run() {
        while (true) {
            CompoundLogEntries entries = agent.liveLogEntries(matcher);
            if (entries.isEmpty()) {
                System.out.println("**** EXIT");
                break;
            }
            for (LogEntries logEntries : entries.getSafeEntries()) {
                for (LogEntry log : logEntries.logEntries()) {
                    System.out.println(logEntries.getProcessType() + "/" + logEntries.getPid() + ": " + log.getText());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}).start();
```

The above example will continuously (tail like) extract logs from all the "live" (existing running runtime components) that this agent manages. The first regex matcher is the one that the continuous matcher will use for the initial matching (the first call), and the second parameter is a regex matcher that will be applied to new log entries.

The `CompoundLogEntries` allows to iterate over all the `LogEntries` of each process sampled.
