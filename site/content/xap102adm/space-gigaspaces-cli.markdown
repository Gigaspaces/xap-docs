---
type: post102adm
title:  Space Command Line Interface
categories: XAP102ADM
parent: administration-tools.html
weight: 300
---

{{%ssummary%}}{{%/ssummary%}}

{{%info%}}
Interacting with a secured gridIn order to interact with a secured grid you need to login first. See [(CLI) Security]({{%currentsecurl%}}/command-line-interface-cli-security.html).
{{%/info%}}

# clean

#### Syntax

    gs> space clean [variable [value]]

#### Description

Removes all Entries and templates from the space.

The `space clean` command is relevant only for spaces in a started state.

{{% tip %}}
When using a persistent space, the clean operation might take some time because it needs to erase data and close database connections. It should not be called when other clients are using the space.
{{% /tip %}}

Specifying a container URL prints a numbered list of all the spaces in the container. You can then choose which space to clear, or `all` (clears all spaces).

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to clear by its number, or `all` (lists all spaces).

{{% tip %}}
Using `-c` (or `-cluster`) with a URL of a clustered space clears **all** cluster members.
{{% /tip %}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to clear, or of the container -- clears the spaces under that container. | Container URL: `jini://localhost/my_container` {{<wbr>}}Space URL: `jini://localhost/my_container/mySpace`  |
| `cluster` / `c` | Clears all spaces in the cluster. | |
| `template` | The template for the class of objects you want to remove from the space. | `com.j_spaces.examples.benchmark.messages.MessageSerializable` |
| `help` / `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}

Tbe following prints a numbered list of spaces, and you can choose a space to clear by its number, or `all` (clears all spaces).

    space clean

The following clears all objects in the `MessageSerializable` class, from a space named `mySpace`.

    space clean -url jini://localhost/my_container/mySpace -template com.j_spaces.examples.benchmark.messages.MessageSerializable
{{%/accord%}}
{{%/accordion%}}


# clear

#### Syntax

    gs> space clear [variable [value]]

#### Description

Removes all Entries and templates from the space.

The `space clear` command is relevant only for spaces in a started state.

{{% tip %}}
When using a persistent space, the clear operation might take some time because it needs to erase data and close database connections. It should not be called when other clients are using the space.
{{% /tip %}}

Specifying a container URL prints a numbered list of all the spaces in the container. You can then choose which space to clear, or `all` (clears all spaces).

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to clear by its number, or `all` (lists all spaces).

{{% tip %}}
Using `-c` (or `-cluster`) with a URL of a clustered space clears **all** cluster members.
{{% /tip %}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to clear, or of the container -- clears the spaces under that container. | Container URL: `jini://localhost/my_container`{{<wbr>}} Space URL: `jini://localhost/my_container/mySpace`  |
| `cluster` / `c` | Clears all spaces in the cluster. | |
| `template` | The template for the class of objects you want to remove from the space. | `com.j_spaces.examples.benchmark.messages.MessageSerializable` |
| `help` / `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}
Tbe following prints a numbered list of spaces, and you can choose a space to clear by its number, or `all` (clears all spaces).

    space clear

The following clears all objects in the `MessageSerializable` class, from a space named `mySpace`.

    space clear -url jini://localhost/my_container/mySpace -template com.j_spaces.examples.benchmark.messages.MessageSerializable

{{%/accord%}}
{{%/accordion%}}

# cluster-members

#### Syntax

    gs> space cluster-members [variable [value]]

#### Description

This command prints a list of all cluster members (spaces belonging to the specified cluster), including members that are not "alive". The list includes each member's name, whether it is alive or not (`is alive`), and its URL. Additionally, the command prints out the total number of members (including members that aren't alive), and the total number of live members.

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `-url` | Specify one of the following:{{<wbr>}}* The space URL of any member belonging to the cluster{{<wbr>}}* A URL containing the cluster name{{<wbr>}}* A URL containing the Jini group and the cluster name


{{%accordion%}}
{{%accord title="**Example**"%}}

The following prints a list of all members in a cluster named `test`:

    space cluster-members -url jini://*/*/*?groups=gigaspaces-{{%currentversion%}}XAP&clustername=test

The following prints a list of all members in the same cluster as the `gigaspaces-{{%currentversion%}}XAP` member:

    space cluster-members -url jini://*/trade_cache_container1/trade_cache?groups=gigaspaces-{{%currentversion%}}XAP

The same functionality can be achieved by running the following command, using only the cluster name:

    space cluster-members -clustername myClusterName
{{%/accord%}}
{{%/accordion%}}


# config

#### Syntax

    gs> space config [variable [value]]

#### Description

Displays the specified spaces's configuration details: space configuration, container configuration, cluster configuration, system properties, system environment and variables, Java properties, network interfaces, GigaSpaces build or version.

You can view the configuration details of spaces in a specific container (see the options below) -- specifying a container URL prints a numbered list of all the spaces in that container, and you can choose a space to view by its number, or `all` (view all spaces).

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to view by its number, or `all` (view all spaces).

{{% tip %}}
Using `-c` (or `-cluster`) with a URL of a clustered space displays configuration details of **all** cluster members.
{{% /tip %}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to view, or of the container -- shows the configuration details of spaces under that container. | Container URL: `jini://localhost/my_container`{{<wbr>}}{{<wbr>}}  Space URL: `jini://localhost/my_container/mySpace`  |
| `cluster` \ `c` | Displays the configuration details of all cluster members. | |
| `help` \ `h` | Prints help -- the command's usage and options. | |


# connections

#### Syntax

    gs> space connections [variable [value]]

#### Description

Displays all live connections to the specified space.

{{% refer %}}
It is also possible to retrieve space connections using the GigaSpaces UI **[Connections view]({{%currentadmurl%}}/gigaspaces-browser-connection-view.html)**.
{{%/refer%}}

The information that is shown:

- The space and container name
- The server IP address
- The client(s) IP address and port
- Connection time -- when the server connected to the space

You can view the connections of spaces in a specific container (see the options below) -- specifying a container URL prints a numbered list of all the spaces in that container, and you can choose a space to view by its number, or all spaces (restart all spaces).

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to view by its number, or `all` (displays all spaces).

{{% info %}}
Using `-c` (or `-cluster`) with a URL of a clustered space displays live connections of **all** cluster members.
{{%/info%}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to view, or of the container -- displays live connections of the spaces under that container. | Container URL: `jini://localhost/my_container`{{<wbr>}} Space URL: `jini://localhost/my_container/mySpace`  |
| `c` \ `cluster` | Displays the live connections of all cluster members. | |
| `help` \ `h` | Prints help -- the command's usage and options. | |


# copy

#### Syntax

    gs> space copy [source-space-url] [destination-space-url] [options]

#### Description

Copies all objects from the specified source space to the specified destination space. Specify the source and destination space URL by simply typing these after the `copy` command -- first the source space, and then the destination space.

The `space copy` command is relevant only for spaces that are in a started mode.

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `-move` | clears the source space after copy | `-move` |
| `help` \ `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}

The following prints a list of all members in a cluster named `test`:

    space cluster-members -url jini://*/*/*?groups=gigaspaces-{{%currentversion%}}XAP&clustername=test

The following prints a list of all members in the same cluster as the `gigaspaces-{{%currentversion%}}XAP` member:

    space cluster-members -url jini://*/trade_cache_container1/trade_cache?groups=gigaspaces-{{%currentversion%}}XAP

The same functionality can be achieved by running the following command, using only the cluster name:

    space cluster-members -clustername myClusterName
{{%/accord%}}
{{%/accordion%}}


# list

#### Syntax

    gs> space list [variable [value]]

#### Description

Lists spaces in the network. Displays the space URL and if it includes a cluster schema, displays the type of the schema.

You can list spaces in a specific container (see the options below) -- specifying a container URL lists all the spaces in that container.

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to see by its number, or `all` (lists all spaces).

{{% tip %}}
Using `\-c` (or `\-cluster`) with a URL of a clustered space lists **all** cluster members.
{{%/tip%}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to see, or of the container -- lists the spaces under that container. | Container URL: `jini://localhost/my_container`{{<wbr>}} Space URL: `jini://localhost/my_container/mySpace`  |
| `cluster` / `c` | Lists all spaces in the cluster. | |
| `stats` | Displays the number of times different operations are performed in the space. | |
| `noRTI` / `noCount` | Instructs the system not to display the number of objects and templates in the space as part of the list. | |
| `help` / `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}

The following lists all spaces in the network:

    space list

The following lists a space named `mySpace`, displays the operation statistics for this space, and does not display the number of objects and templates in the space.

    space list -url jini://localhost/my_container/mySpace -stats -noRTI

{{%/accord%}}
{{%/accordion%}}


# ping

#### Syntax

    gs> space ping [variable [value]]

#### Description

Provides a convenient way to test a space. This option does the following:

- Writes a set of message objects (each with a specific ID and configurable length).
- Reads or takes them.

The average time of write/take is calculated and printed to the console.

This is useful for verifying that a space exists and is running correctly as a basic performance-testing tool.

The `space ping` command is relevant only for spaces in a started state.

You can ping spaces in a specific container (see the options below) -- specifying a container URL prints a numbered list of all the spaces in that container, and you can choose a space to ping by its number, or `all` (ping all spaces).

Specifying a URL of a clustered space prints a list of all spaces in the cluster, and you can choose a space to ping by its number, or `all` (pings all spaces).

{{% tip %}}
Using `-c` (or `-cluster`) with a URL of a clustered space pings **all** cluster members.
{{% /tip %}}

#### Options


| Option | Description | Value Format |
|:-------|:------------|:-------------|
| `url` | The URL of the space you want to restart, or of the container -- pings the spaces under that container. | Container URL: `jini://localhost/my_container`{{<wbr>}}  Space URL: `jini://localhost/my_container/mySpace`  |
| `cluster` / `c` | Pings all cluster members. | |
| `t` |  Sets the message objects' lease timeout (in `[ms]`). Default is `FOREVER`. | `1000` |
| `ft` | Sets a a timeout (in `[ms]`) for the Jini protocol. | `1000` |
| `s` | Sets the byte size of the object. | `128` |
| `i` |  Sets the iteration number. Default is `5`. | `10` |
| `read` | Reads from the space. Cannot be performed together with take -- either read or take is performed. If you do not specify read nor take, read is performed by default. | |
| `take` | Takes from the space. Cannot be performed together with read -- either take or read is performed. If you do not specify read nor take, read is performed by default. | |
| `x` | All operations performed as part of `space ping` are under a transaction. | |
| `help` \ `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}

The following, writes a set of message objects to the space, and reads them back:

    space ping -url jini://host:port/mySpace_container1/mySpace -read

The following, writes a set of message objects to the space, and takes them from the space:
{{%/accord%}}
{{%/accordion%}}


# sql

#### Syntax

    gs> space sql [variable [value]]

#### Description

Queries the space you specified -- according to the space URL and SQL query supplied.

You can query spaces in a specific container (see the options below) -- specifying a container URL prints a numbered list of all the spaces in that container, and you can choose a space to query by its number, or all spaces (queries all spaces).

#### Options


| Option | Description | Value Format |
|:-------|:------------|--------------|
| `url` | The URL of the space you want to restart, or of the container -- restarts the spaces under that container -- **mandatory option**. | Container URL: `jini://localhost/my_container`{{<wbr>}} Space URL: `jini://localhost/my_container/mySpace`  |
| `query` | The query that is run on the space -- **mandatory option**. | `select uid,* from com.j_spaces.examples.benchmark.messages.Message  `{{<wbr>}}` WHERE rownum<10` |
| `multispace` | Indicates if this query is multi space, default is single space. | |
| `help` \ `h` | Prints help -- the command's usage and options. | |

{{%accordion%}}
{{%accord title="**Example**"%}}

    space sql -url jini://localhost/mySpace_container/mySpace -query select uid,* from com.j_spaces.examples.benchmark.messages.Message
    WHERE rownum<10
{{%/accord%}}
{{%/accordion%}}

