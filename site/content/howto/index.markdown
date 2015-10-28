---
type: post
title:  XAP Documentation Markdown
categories: HOWTO
weight:  700
parent:  none
---

 Cheat sheet for GigaSpaces Documentation

# Text


| **Example** | **Markdown** |
|-------------|--------------|
| This is an *emphasised* text. | This is an \*emphasised\* text. |
| This is a **bold** text. | This is a \*\*bold\*\* text.  |
| This is a `monospaced` text | This is a \`monospaced\` text |

# Links


| **Description** | **Markdown** | **Output** |
|-----------------|--------------|------------|
| Link to external site | \[GigaSpaces\]\(http://www.gigaspaces.com\) | [GigaSpaces](http://www.gigaspaces.com) |
| Link to page in same folder | \[Directory Structure\](./file-structure.html\) | [Directory Structure](file-structure.html) |
| Link to page in `xap` folder | \[SQL Query\]({{%/*currentjavaurl*/%}}/sqlquery.html) | [SQL Query]({{%currentjavaurl%}}/sqlquery.html) |
| Link to page in `xapnet` folder | \[SQL Query\]({{%/*currentneturl*/%}}/sqlquery.html\) | [SQL Query]({{%currentneturl%}}/sqlquery.html) |
| Link to page in latest `xap` folder | \[SQL Query\]({{%/*latestjavaurl*/%}}/sqlquery.html\) | [SQL Query]({{%latestjavaurl%}}/sqlquery.html) |
| Link to page in latest `xapnet` folder | \[SQL Query\]({{%/*latestneturl*/%}}/sqlquery.html\) | [SQL Query]({{%latestneturl%}}/sqlquery.html) |
| Link to latest Java Tutorial page | \[Getting started\]({{%/*latestjavatuturl*/%}}/java-tutorial-part1.html) | [Getting Started]({{%latestjavatuturl%}}/java-tutorial-part1.html) |
| Link to latest .NET Tutorial page | \[Getting started\]({{%/*latestnettuturl*/%}}/net-tutorial-part1.html) | [Getting Started]({{%latestnettuturl%}}/net-tutorial-part1.html) |


# Icons


| **Example** | **Markdown** |
|--------------|-------------|
| {{%oksign%}} This is an OK icon               | {{%/*oksign*/%}} This is an OK icon   |
| {{%infosign%}} This is an Info icon           |   {{%/*infosign*/%}} This is an Info icon  } |
| {{%exclamation%}} This is an Exclamation icon |   {{%/*exclamation*/%}} This is an Exclamation icon   |
| {{%question%}} This is a Question icon        |   {{%/*question*/%}} This is a Question icon   |
| {{%plus%}} This is a Plus icon                |  {{%/*plus*/%}} This is a Plus icon   |
| {{%remove%}} This is a Remove icon            |   {{%/*remove*/%}} This is a Remove icon  |
| {{%star%}} This is a Star icon                |   {{%/*star*/%}} This is a Star icon   |
| {{%lampon%}} This is a Lamp On icon           |   {{%/*lampon*/%}} This is a Lamp On icon   |
| {{%lampoff%}} This is a Lamp Off icon         |   {{%/*lampoff*/%}} This is a Lamp Off icon   |
| {{%download  This is a download   icon %}}     |   {{%/*download  This is a download   icon */%}}  |
| {{%pdf%}} This is a pdf icon                  |   {{%/*pdf*/%}} This is a pdf icon     |
| {{%zip%}}  This is a zip icon               |   {{%/*zip*/%}}  This is a zip icon     |
| {{%folderopen%}} This is an open folder icon  |  {{%/*folderopen*/%}} This is an open folder icon    |



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
| Latest XAP Release  | {{%/*latestxaprelease*/%}} | {{%latestxaprelease%}} |
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
| This is a line with a {{%/*wbr*/%}} word break |  This is a line with a {{%wbr%}} word break  |

# Custom Text Size


| **Markdown** | **Example** |
|-------------|--------------|
|{{%/* fontsize 10 */%}}This is font 10{{%/* /fontsize */%}}| {{%fontsize 10%}}This is font 10{{%/fontsize%}} |
|{{%/* fontsize 20 */%}}This is font 20{{%/* /fontsize */%}}| {{%fontsize 20%}}This is font 20{{%/fontsize%}} |
|{{%/* fontsize 30 */%}}This is font 30{{%/* /fontsize */%}}| {{%fontsize 30%}}This is font 30{{%/fontsize%}} |



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

# Image text

## Left

{{%imageltext "/attachment_files/subject/index.png" %}}
Lorem ipsum dolor sit amet, tellus magna tempus ultrices donec nisi, lorem varius. Lectus pharetra leo sit. Pede luctus leo, at ligula tortor ultricies aliquam, lorem hendrerit fringilla, posuere egestas potenti mauris viverra curabitur molestie, praesent nonummy porttitor malesuada cras. Venenatis vel, sed malesuada in posuere magna, ac ullamcorper tempus, lorem vitae sem nullam, porta litora laoreet. Pellentesque in mauris litora euismod sed sociis, quis phasellus habitant vestibulum nec at, senectus in id, id vitae suspendisse curabitur et sit. Donec vel ligula et aliquam commodo in, pede euismod vitae dictum nulla ornare suscipit. Vestibulum potenti vivamus ut nec praesent pede, ultricies ut eros vitae fermentum. Accumsan tempus interdum sociis, pellentesque ut tellus vel. Neque felis ullamcorper posuere luctus hendrerit amet. Arcu nulla et urna. Metus pellentesque erat interdum massa arcu vel, curabitur nibh tellus vitae augue. Nonummy duis quis nunc mauris, a phasellus veritatis tortor amet aliquam.
{{%/imageltext  %}}

#### Markdown

```
{{%/* imageltext "/attachment_files/subject/index.png" */%}}
Some text content
{{%/* /imageltext  */%}}
```

## Right

{{%imagertext "/attachment_files/subject/index.png" %}}
Lorem ipsum dolor sit amet, tellus magna tempus ultrices donec nisi, lorem varius. Lectus pharetra leo sit. Pede luctus leo, at ligula tortor ultricies aliquam, lorem hendrerit fringilla, posuere egestas potenti mauris viverra curabitur molestie, praesent nonummy porttitor malesuada cras. Venenatis vel, sed malesuada in posuere magna, ac ullamcorper tempus, lorem vitae sem nullam, porta litora laoreet. Pellentesque in mauris litora euismod sed sociis, quis phasellus habitant vestibulum nec at, senectus in id, id vitae suspendisse curabitur et sit. Donec vel ligula et aliquam commodo in, pede euismod vitae dictum nulla ornare suscipit. Vestibulum potenti vivamus ut nec praesent pede, ultricies ut eros vitae fermentum. Accumsan tempus interdum sociis, pellentesque ut tellus vel. Neque felis ullamcorper posuere luctus hendrerit amet. Arcu nulla et urna. Metus pellentesque erat interdum massa arcu vel, curabitur nibh tellus vitae augue. Nonummy duis quis nunc mauris, a phasellus veritatis tortor amet aliquam.
{{%/imagertext  %}}


#### Markdown
```
{{%/* imagertext "/attachment_files/subject/index.png" */%}}
Some text content
{{%/* /imagertext  */%}}
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
- [Overview](/xap110/change-api.html)<br>
Change API overview.

{{%/fpanel%}}


#### Markdown

```
{{%/* fpanel */%}}
- [Overview](/xap110/change-api.html)<br>
Change API overview.

{{%/* /fpanel */%}}
```
