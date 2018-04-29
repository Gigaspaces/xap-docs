---
type: post
title:  Flare Test Page
categories: howto
parent:  index.html
weight: 50
---

 Test page for Flare migration


# Text


| **Example** | **Markdown** |
|-------------|--------------|
| This is an *emphasized* text. | This is an \*emphasized\* text. |
| This is a **bold** text. | This is a \*\*bold\*\* text.  |
| This is a `monospaced` text | This is a \`monospaced\` text |

# Links


| **Description** | **Markdown** | **Output** |
|-----------------|--------------|------------|
| Link to external site | \[GigaSpaces\]\(http://www.gigaspaces.com\) | [GigaSpaces](http://www.gigaspaces.com) |
| Link to page in same folder | \[Anchor\](./anchor.html\) | [Anchor](./anchor.html) |
| Link to page in `xap` folder | \[SQL Query\]({{%/*currentjavaurl*/%}}/sqlquery.html) | [SQL Query]({{%currentjavaurl%}}/query-sql.html) |
| Link to page in `xapnet` folder | \[SQL Query\]({{%/*currentneturl*/%}}/sqlquery.html\) | [SQL Query]({{%currentneturl%}}/query-sql.html) |
| Link to page in latest `xap` folder | \[SQL Query\]({{%/*latestjavaurl*/%}}/sqlquery.html\) | [SQL Query]({{%latestjavaurl%}}/query-sql.html) |
| Link to page in latest `xapnet` folder | \[SQL Query\]({{%/*latestneturl*/%}}/sqlquery.html\) | [SQL Query]({{%latestneturl%}}/query-sql.html) |
| Link to latest Java Tutorial page | \[Getting started\]({{%/*lateststartedurl*/%}}) | [Getting Started]({{%lateststartedurl%}}) |
| Link to latest .NET Tutorial page | \[Getting started\]({{%/*latestnettuturl*/%}}) | [Getting Started]({{%latestnettuturl%}}) |
| Link to latest Admin page | \[Administration\]({{%/*latestadmurl*/%}}) | [Admin]({{%latestadmurl%}}) |


# Icons


| **Example** | **Markdown** |
|--------------|-------------|
| {{%infosign%}} This is an Info icon           |   {{%/*infosign*/%}} This is an Info icon. Example: {{%infosign%}} If this is your first time working with Easysoft, first follow the instructions to register and create your Easysoft user account.   |
| {{%exclamation%}} This is an Exclamation icon |   {{%/*exclamation*/%}} This is an Exclamation icon. Example: {{%exclamation%}} When running in `./gs-agent --manager` configuration, this environment variable is ignored.    |
| {{%zip%}}  This is a zip icon               |   {{%/*zip*/%}}  This is a zip icon. Should appear as regular link to a file, w/o image. For example, Download the example {{%zip "/attachment_files/sbp/PollingEvictor.zip"%}} that uses a polling container for eviction of Orders.     |


# Layouts

## Table


| Header1 | Header2 | Header3 |
|:--------|:--------|:--------|
| column1 | column2 | column3 |

```
 | Header1 | Header2 | Header3 |
 |:--------|:--------|:--------|
 | column1 | column2 | column3 |
```

## Tabbed Pane

{{%tabs%}}
{{%tab "Foo  This is tab Foo"%}}
{{%/tab%}}
{{%tab "Bar  This is tab Bar"%}}
{{%/tab%}}
{{% /tabs %}}


```
{{%/* tabs */%}}
{{%/* tab "Foo  This is tab Foo" */%}}
{{%/* /tab */%}}
{{%/* tab "Bar  This is tab Bar" */%}}
{{%/* /tab */%}}
{{%/* /tabs */%}}
```




## Accordion

{{%accordion%}}

{{%accord title="accord0" %}}
```java
private int age;
private String name;
```
{{% /accord %}}

{{%accord title="accord1" %}}
 Some Text
{{% /accord %}}

{{% accord title="accordion3"%}}
More Text
{{% /accord%}}

{{% /accordion%}}


```
{{%/* accordion */%}}

{{%/* accord title="accord0" */%}}
Some Text 0
{{%/* /accord */%}}

{{%/* accord title="accord1" */%}}
 Some Text 1
{{%/* /accord */%}}

{{%/* accord title="accordion3" */%}}
More Text 3
{{%/* /accord */%}}

{{%/* /accordion */%}}
```


## Section

{{%section%}}
{{%column width="20%" %}}
![fifo-groups.png](/attachment_files/subject/index.png)
{{%/column%}}

{{%column width="80%" %}}
 The polling event container is an implementation of the polling consumer pattern which uses the Space to receive events.
 A polling event operation is mainly used when simulating Queue semantics or when using the master-worker design pattern.
{{%/column%}}
{{%/section%}}

```
{{%/*section%/%}}
{{%/* column width="20%" */%}}
![fifo-groups.png](/attachment_files/subject/index.png)
{{%/* /column */%}}

{{%/*column width="80%" */%}}
The polling event container is an implementation of the polling  ........
{{%/* /column*/%}}
{{%/* /section*/%}}
```


# Macros


| **Description** | **Macro** | **Output** |
|-----------------|-----------|------------|
| Latest XAP Release file name |  {{%/*version "build-filename"*/%}}    | {{%version build-filename %}} |
| Latest XAP Release home dir |   {{%/*version "gshome-directory"*/%}}    | {{%version gshome-directory %}} |
| Latest Default Lookup Group |   {{%/*version "default-lookup-group"*/%}}    | {{%version default-lookup-group %}} |
| Latest XAP Maven |  {{%/*version "maven-version"*/%}} {{%version maven-version %}}   |{{%version maven-version %}} |


# Jar versions


| **Description** | **Macro** | **Output** |
|-----------------|-----------|------------|
| spring  |  {{%/*version "spring"*/%}}   |{{%version spring %}} |
| antlr4-runtime  |   {{%/*version "antlr4-runtime"*/%}}  |{{%version antlr4-runtime %}} |
| mongo-java-driver  |   {{%/*version "mongo-java-driver"*/%}}  |{{%version mongo-java-driver %}} |
| mongo-datasource  |   {{%/*version "mongo-datasource"*/%}}   |{{%version mongo-datasource %}} |
| openjpa  |   {{%/*version "openjpa"*/%}}     |{{%version openjpa %}} |


# Anchor

Navigate to the anchor [point](#hello)



```
Navigate to the anchor [point](#hello)

{{</*anchor hello*/>}}
```



# Custom Colors


| **Markdown** | **Example** |
|-------------|--------------|
| This text has {{%/* color blue*/%}}blue{{%/* /color */%}}  |  This text has {{%color blue%}}blue{{%/color%}}    |
| This text has {{%/* bgcolor orange */%}}orange background{{%/* /bgcolor */%}} | This text has {{% bgcolor orange %}}orange background{{% /bgcolor %}} |

# Image Popup

{{%popup "/attachment_files/qsg/class_diagram.png"  %}}

#### Markdown
```
{{%/* popup "/attachment_files/qsg/class_diagram.png"  */%}}
```

# Youtube Video



{{%youtube "V7rbbmWo3JU"  "Multi Site Deployment" %}}

#### Markdown
```
{{%/*youtube "V7rbbmWo3JU"  "Multi Site Deployment" */%}}
```

# Panels



| Description    | Markdown     |  Output   |
|----------------|--------------|-----------|
| Simple panel|{{%/*panel "hello there"*/%}}This is a simple panel{{%/* /panel */%}}|{{%panel "hello there"%}}This is a simple panel{{%/panel%}}|
| Warning     |{{%/*warning "Warning....."*/%}}... {{%/* /warning*/%}}|{{%warning "Warning....."%}}... {{%/warning%}}|
| Info     |{{%/*info "Info....."*/%}}... {{%/* /info*/%}}|{{%info "Info....."%}}... {{%/info%}}|
| Note     |{{%/*note "Note....."*/%}}... {{%/* /note*/%}}|{{%note "Note....."%}}... {{%/note%}}|
| Tip     |{{%/*tip "Tip....."*/%}}... {{%/* /tip*/%}}|{{%tip "Tip....."%}}... {{%/tip%}}|
| Refer     |{{%/*refer*/%}}... {{%/* /refer*/%}}|{{%refer%}}... {{%/refer%}}|
| VBar     |{{%/*vbar "VBar....."*/%}}... {{%/* /vbar*/%}}|{{%vbar "VBar....."%}}... {{%/vbar%}}|


### Landing page panel

{{%fpanel%}}
- [Overview](/xap/11.0/dev-java/change-api.html)<br>
Change API overview.

{{%/fpanel%}}


#### Markdown

```
{{%/* fpanel */%}}
- [Overview](/xap/11.0/dev-java/change-api.html)<br>
Change API overview.

{{%/* /fpanel */%}}
```


# Links

Testing docs link [Event Processing](http://www.mulesoft.org) to see how this works.<br>

Testing external link {{%exurl "Mule Site" "http://www.mulesoft.org/" %}} to see how this works.<br>



