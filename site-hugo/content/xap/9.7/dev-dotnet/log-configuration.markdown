---
type: post97
title:  Logging
categories: XAP97NET
parent: configuration.html
weight: 600
---

{{%ssummary%}} {{%/ssummary%}}



XAP.NET components use the tracing mechanism for logging/tracing, built-in with the .NET framework. This gives the user, control over tracing behavior using the standard .NET configuration schema. Users can:

- configure the level of events which are traced
- assign one or more trace listeners which route the events to a logging facility
- implement custom trace listeners to integrate GigaSpaces events with the application events, and more.

If the user does not specify a configuration, the default configuration is assumed.

{{% refer %}}GigaSpaces XAP.NET contains some of the GigaSpaces XAP components. Its logging level needs to be configured seperately -- this is described in [GigaSpaces XAP Logging]({{% currentadmurl %}}/logging.html){{% /refer %}}.

 

# Default Configuration

The logger component loads the configuration during initialization. If it does not find a source element named `GigaSpaces.Core`, it loads a default configuration, which sets the trace level to `Information`, and configures an `EventLogTraceListener` with `source=GigaSpaces.Core`, (similar to the configuration shown in the basic example).

{{% warning %}}
If the Windows Event Log does not contain the specified source, it is automatically created. However, you need administrator permissions to create a source in the Event Log. If you don't create this source, the default configuration is not used. We recommend that you use an administrator's profile the first time you use the product on a machine, to make sure the source is created. Subsequent runs do not require high level permissions.
{{%/warning%}}

# Advanced Configuration

Here are some features/scenarios which might be useful:

- You can use any of the built-in trace listeners offered by `System.Diagnostics`:
    - `ConsoleTraceListener`
    - `TextWriterTraceListener`
    - `XmlWriterTraceListener`
    - `DelimitedListTraceListener`
    - `EventLogTraceListener`

{{% refer %}}For more details, see: [http://msdn2.microsoft.com/en-us/library/4y5y10s7.aspx](http://msdn2.microsoft.com/en-us/library/4y5y10s7.aspx).{{% /refer %}}

- You can configure a trace listener with a filter to handle specific events.

{{% refer %}}For more details, see: [http://msdn2.microsoft.com/en-us/library/system.diagnostics.eventtypefilter.aspx](http://msdn2.microsoft.com/en-us/library/system.diagnostics.eventtypefilter.aspx).{{% /refer %}}

- You can implement a custom trace listener to handle traced events in a desired manner (e-mail, SMS, custom log, etc.). If you are planning to do this, we recommend that you examine the implementation of custom trace listeners provided in Microsoft's Logging Application Block as a reference.

{{% refer %}}For more details, see:

- [http://msdn2.microsoft.com/EN-US/library/aa480464.aspx](http://msdn2.microsoft.com/EN-US/library/aa480464.aspx)
- [http://msdn2.microsoft.com/en-us/library/ms228989.aspx](http://msdn2.microsoft.com/en-us/library/ms228989.aspx){{% /refer %}}

