---
type: post101
title:  Command Line Interface
categories: XAP101SEC
parent: security-administration.html
weight: 200
---


{{% ssummary %}}{{% /ssummary %}}



The Command Line Interface can be used to manage and monitor a running system. It is mainly used for script automation, and when there is a limitation to run a Graphical User Interface. This section covers how security can be applied to the commands supported by the CLI tool.

# Login/Logout

The Command Line Interface (CLI) has two modes - an `interactive shell` mode and a `non-interactive` mode. The difference in terms of security is the **login** stage. When in `interactive` mode, you can call the `login` command, supply credentials, and perform operations using this session. But, when in a `non-interactive` mode, you can execute only one command line at a time - no session is being managed.

{{% info %}}
Note that the login is being performed against the GSM. If the Grid is not secured, you can deploy without logging in. For example, you can deploy a secured Processing Unit into a non-secured Grid.
{{%/info%}}

{{%tabs%}}
{{%tab "   Interactive "%}}
Run the `gs` script and use the command line arguments  `-user` and `-password` with the user credentials.


```bash
gs(.sh/bat) -user uuu -password ppp
```

or, run the `gs` script, and then use the  `login` command


```bash
gs(.sh/bat)
gs> login -user uuu -password ppp
gs> ...
```

{{% /tab %}}
{{%tab "   Non-Interactive "%}}
Run the `gs` script, and prefix **any** command with the command line arguments  `-user` and `-password`.


```bash
gs(.sh/.bat) -user uuu -password ppp [command]
```

{{% /tab %}}
{{% /tabs %}}

To **logout** `quit` the interactive shell.

# Deploy Command

The CLI  `deploy` command accepts a  `-secured true/false` used to deploy a secured Processing Unit with a secured Space. Common when deploying a data-grid.
In order to pass the user credentials, use  `-user` and `-password` arguments. This will implicitly deploy a secured Processing Unit, and the credentials will be propagated to the Processing Unit and its internal services.

{{% info %}}
Same syntax applies for  `deploy-space` and  `pudeploy` commands.
{{%/info%}}

Here are some examples and how they are accomplished in both CLI modes:

1. login with user uuu and password ppp - this will log onto the Grid
1. deploy a non secured data-grid
1. deploy a secured data-grid
1. deploy a secured processor PU with user xxx and password yyy

{{%tabs%}}
{{%tab "   Interactive "%}}


```bash
gs(.sh/bat)
gs> login -user uuu -password ppp
gs> deploy /templates/datagrid
gs> deploy -secured true -override-name myDataGrid /templates/datagrid
gs> deploy -user xxx -password yyy processor
```

{{% /tab %}}
{{%tab "   Non-Interactive "%}}


```bash
gs -user uuu -password ppp deploy /templates/datagrid
gs -user uuu -password ppp deploy -secured true -override-name myDataGrid /templates/datagrid
gs -user uuu -password ppp deploy -user xxx -password yyy processor
```

{{% /tab %}}
{{% /tabs %}}

# Undeploy Command

The CLI `undeploy` command of a processing unit is done on its managing GSM. If the GSM is not secured, then no credentials are needed.

{{%tabs%}}
{{%tab "   Interactive "%}}


```bash
gs(.sh/bat)
gs> login -user uuu -password ppp
gs> undeploy processor
```

{{% /tab %}}
{{%tab "   Non-Interactive "%}}


```bash
gs -user uuu -password ppp undeploy processor
```

{{% /tab %}}
{{% /tabs %}}

# Space Command

The CLI `space` commands are basically all the same. They require that the user has sufficient privileges to perform operations on the data.

{{% info %}}
Note that the login is being performed against the Space; It doesn't matter if the GSM or GSC are secured. The login credentials should reflect the operations being performed on the service.
{{%/info%}}

For example, for the  `space clean` command, the user _(uuu/ppp)_ needs  `Alter` privileges.

{{%tabs%}}
{{%tab "   Interactive "%}}


```bash
gs(.sh/bat)
gs> login -user uuu -password ppp
gs> space clean -url jini://*/*/space
```

{{% /tab %}}
{{%tab "   Non-Interactive "%}}


```bash
gs -user uuu -password ppp space clean -url jini://*/*/space
```

{{% /tab %}}
{{% /tabs %}}

For the `space copy` command, the user needs  `Read` privileges on the source space (copied from) and  `Write` privileges on the target space (copied to).

{{%tabs%}}
{{%tab "   Interactive "%}}


```bash
gs(.sh/bat)
gs> login -user uuu -password ppp
gs> space copy jini://*/*/sourceSpace jini://*/*/targetSpace
```

{{% /tab %}}
{{%tab "   Non-Interactive "%}}


```bash
gs -user uuu -password ppp space copy jini://*/*/sourceSpace jini://*/*/targetSpace
```

{{% /tab %}}
{{% /tabs %}}

