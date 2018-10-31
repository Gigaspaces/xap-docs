---
type: post140
title:  Auditing
categories: XAP140SEC, PRM
parent: index.html
weight: 720
---



XAP provides the ability to audit the authentication requests and operations performed on a secured service. It facilitates the logging mechanism to declare the audit log file, and the level of auditing. The level can be dynamically modified using the `java.util.logging JMX Extensions`. This allows an easy extension for custom auditing.

{{% note %}}
Currently auditing of operations is limited to Space operations.
{{%/note%}}

# Configuration

The configurations should be placed in the logging configuration file `<XAP root>/config/log/xap_logging.properties`.


```bash
# xap_logging.properties

com.gigaspaces.security.audit.enabled = true
com.gigaspaces.security.audit.level = SEVERE
com.gigaspaces.security.audit.handler = com.gigaspaces.security.audit.AuditHandler
```

This configuration can also be supplied using system properties.


```bash
-Dcom.gigaspaces.security.audit.enabled=true -Dcom.gigaspaces.security.audit.level=SEVERE ...
```

The defaults of these configurations are:

|       |     |
|-------|-----|
| com.gigaspaces.security.audit.enabled | Enable/Disable security auditing; default is disabled (false) |
| com.gigaspaces.security.audit.level | Audit level of interest; default is OFF |
| com.gigaspaces.security.audit.handler | The Audit `java.util.logging.Handler` implementation accepting an `AuditLogRecord`; default is `AuditHandler` |

The `AuditHandler` is a declarable extension to the default GigaSpaces logging `Handler` (see [GigaSpaces Logging](../admin/logging-overview.html)). As such, it accepts properties that configure the handler - amongst others are the logging message **formatter** and the **filename-pattern**.


```bash
# xap_logging.properties

...
com.gigaspaces.security.audit.handler = com.gigaspaces.security.audit.AuditHandler

# Properties configuring the audit-handler:

com.gigaspaces.security.audit.AuditHandler.formatter = com.gigaspaces.logger.GSSimpleFormatter
com.gigaspaces.security.audit.AuditHandler.filename-pattern = {homedir}/logs/gigaspaces-security-audit-{service}-{host}-{pid}.log
```

# Audit Levels

|       |     |
|-------|-----|
| OFF     | Nothing is audited |
| SEVERE  | Authentication failure or invalid session |
| WARNING | Access denied due to insufficient privileges |
| INFO    | Authentication successful |
| FINE    | Access granted |




# Example

In the example below, there are two users "writer" (only privileges to write), and "reader" (only privileges to read).



```bash

FINE: Access granted; user [writer] at host [some-pc.gspaces.com/192.168.10.172] has [Write] privileges for class [com.gigaspaces.data.car.CarPojo]; session-id [827282038]
18/12/2014 12:23:50 com.gigaspaces.security.audit.SecurityAudit accessGranted

If the writer tries to read, you get a denied message:
WARNING: Access denied; user [writer] at host [some-pc.gspaces.com/192.168.10.172] lacks [Read] privileges for class [com.gigaspaces.data.car.CarPojo]; session-id [827282038]
18/12/2014 12:23:51 com.gigaspaces.security.audit.SecurityAudit accessDenied

Same goes to the reader
WARNING: Access denied; user [reader] at host [some-pc.gspaces.com/192.168.10.172] lacks [Write] privileges for class [com.gigaspaces.data.car.CarPojo]; session-id [1003653583]
18/12/2014 12:23:51 com.gigaspaces.security.audit.SecurityAudit accessDenied

And
FINE: Access granted; user [reader] at host [some-pc.gspaces.com/192.168.10.172] has [Read] privileges for class [com.gigaspaces.data.car.CarPojo]; session-id [1003653583]
18/12/2014 12:23:51 com.gigaspaces.security.audit.SecurityAudit accessGranted

```

{{%refer%}}

You can see that for each write operation an audit `FINE` log message is created with the classname. There is no data in the audit details.
If you need the data to be audited, you can apply a [filter](../security/securing-your-data.html#space-filters)  to achieve this.
{{%/refer%}}


# Custom Auditing

The `java.util.logging.Handler` accepts a `java.util.logging.LogRecord` for logging. An `AuditLogRecord` is supplied by the security layer containing the `AuditDetails`. Instead of logging into a file, a custom `Handler` can capture all the log activity for auditing. By default the `java.util.logging.LogRecord.getMessage()` of `AuditLogRecord` contains the audit message (as shown in the sample output above).
