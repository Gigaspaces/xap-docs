---
type: post120
title:  Quiesce Command Line
categories: XAP120ADM, PRM
parent: administration-tools.html
weight: 400
canonical: auto
---

 

XAP allows putting a processing unit in quiesce mode (a.k.a maintenance mode). The quiesce mode can be invoked via the CLI. This page explains the usage of the CLI commands.

{{%refer%}}
For more information please refer to the [Quiesce documentation](./quiesce-overview.html)
{{%/refer%}}

{{%info "Interacting with a Secured Grid"%}}
In order to interact with a secured grid you need to login first. See [(CLI) Security]({{%currentsecurl%}}/command-line-interface-cli-security.html).
 {{%/info%}}

# Quiesce A Processing Unit

### Syntax


```bash
gs> quiesce [options] PU_NAME
```

### Description

Sends a quiesce request to the GSM for the provided processing unit's name.

{{%accordion%}}
{{%accord title="Example"%}}

One option is to specify the processing unit's name in the command:


```bash
gs> quiesce -description some description myPU
Locating processing unit with name [myPU]
Sending quiesce request...
Waiting up to 300 seconds until the processing unit [myPU] is quiesced
Quiesce command completed successfully [token=ee16f577-92df-430b-afc7-2dd9f2c16998]
```

Another option is to run the command in interactive way; First it will look for deployed processing units and then you can choose one from the list:


```bash
gs> quiesce
Locating processing units ...
Total processing units: 1
[1]	myPU
Choose a processing unit or "c" to cancel: 1
Enter new value, or press ENTER for the default
	Quiesce description []: the description
	Timeout in seconds [300]: 
Locating processing unit with name [myPU]
Sending quiesce request...
Waiting up to 300 seconds until the processing unit [myPU] is quiesced
Quiesce command completed successfully [token=ee16f577-92df-430b-afc7-2dd9f2c16998]
```

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="Options"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| -description | The quiesce request description | `-description [description]`|
| -timeout | Timeout for quiesce operation |`-timeout [timeout in seconds]`|
| -help  | Prints help | |
{{%/accord%}}
{{%/accordion%}}




# Unquiesce A Processing Unit

### Syntax


```bash
gs> unquiesce [options] PU_NAME
```

### Description

Sends an unquiesce request to the GSM for the provided processing unit's name.

{{%accordion%}}
{{%accord title="Example"%}}

Like the quiesce command, the unquiesce command can be executed with a processing unit name;


```bash
gs> unquiesce -description some description myPU
Locating processing unit with name [myPU]
Sending unquiesce request...
Waiting up to 300 seconds until the processing unit [myPU] is unquiesced
Unquiesce command completed successfully
```

Or run it in interactive mode:


```bash
gs> unquiesce
Locating processing units ...
Total processing units: 1
[1]	myPU
Choose a processing unit or "c" to cancel: 1
Enter new value, or press ENTER for the default
	Unquiesce description []: the description
	Timeout in seconds [300]: 
Locating processing unit with name [myPU]
Sending unquiesce request...
Waiting up to 300 seconds until the processing unit [myPU] is unquiesced
Unquiesce command completed successfully
```

{{%/accord%}}
{{%/accordion%}}

{{%accordion%}}
{{%accord title="Options"%}}


|Option|Description|Value Format|
|:-----|:----------|:-----------|
| -description | The unquiesce request description | `-description [description]`|
| -timeout | Timeout for unquiesce operation |`-timeout [timeout in seconds]`|
| -help  | Prints help | |
{{%/accord%}}
{{%/accordion%}}
