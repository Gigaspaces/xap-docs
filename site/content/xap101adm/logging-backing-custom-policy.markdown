---
type: post101
title:  Backing-up Files With a Custom Policy
categories: XAP101ADM
parent: logging-overview.html
weight: 500
---

{{% ssummary %}}{{% /ssummary %}}

A backup-policy can be configured to backup files. By default a `NullBackupPolicy` is configured, which does nothing. It can be replaced by a `DeleteBackupPolicy` to keep a backup of files for a specified period. The `BackupPolicy` interface allows custom implementations to be plugged-in.

# Null Backup Policy

A null backup policy acts as a placeholder for a 'do-nothing' behavior. Used when an exception occurs trying to instantiate a customized backup policy, or if no policy is desired.

# Delete Backup Policy

A backup policy that deletes any file which is older than the specified period, but keeps at least as many of the specified backup files.

By default, a file is kept for a 30 day period. After 30 days, the file is deleted; Unless if there are less than 10 backup files available. In other words, maintain a history of 10 files, even if there was nothing logged for more than 30 days.

These properties can be configured either by modifying the logging configuration file:


```console
com.gigaspaces.logger.RollingFileHandler.backup-policy = com.gigaspaces.logger.DeleteBackupPolicy
com.gigaspaces.logger.DeleteBackupPolicy.period = 30
com.gigaspaces.logger.DeleteBackupPolicy.backup = 10
```

or by use of a system property override:


```console
-Dcom.gigaspaces.logger.DeleteBackupPolicy.[property-name]=[property-value]

For example:
-Dcom.gigaspaces.logger.DeleteBackupPolicy.period=30
```

# Customized Backup Policy

The `com.gigaspaces.logger.`**`BackupPolicy`** is an interface for a pluggable backup policy. For example, you may wish to write an implementation to zip files if reached a certain threshold. The interface has a single method, which is used to **track** newly created log files. A file is either created upon rollover or at initialization time. Implementation can keep track of files and decide whether to trigger the backup policy.


```console
    public void track(File file);
```

