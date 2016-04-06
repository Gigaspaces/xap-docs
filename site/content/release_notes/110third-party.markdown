---
type: post
title:  3rd-Party License Agreements 
categories: RELEASE_NOTES
parent: xap110.html
weight: 900
---

This page list all the 3rd party libraries shipped with GigaSpaces XAP on a per version basis, and their respective license agreements.


{{%tabs%}}

{{%tab "  Core "%}}


| Product | Version | License | Required | Component |
|:--------|:--------|:--------|:---------|:----------|
| [Security](http://java.sun.com/products/jsse/) |1.6 | [Sun](http://www.opensource.org/licenses/sunpublic.php) | Yes | Core |
| [Apache Commons](http://jakarta.apache.org/commons/) | 1.x-2.x | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | Yes | Core |
| [Spring](http://www.springframework.org/about) | 4.1.1 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | Yes | Core |
| [ASM](http://asm.objectweb.org/doc/tutorial.html) | 2.2.3 | [INRIA](http://asm.objectweb.org/license.html) | No | Core |
| [Sigar](http://support.hyperic.com/display/SIGAR/Home) | 1.6.5 | [Apache2](http://support.hyperic.com/display/SIGAR/Home#Home-license) | No | Core |
| [HyperSonic SQL](http://www.hsqldb.org/) | 1.8.0 | [Hypersonic SQL](http://www.hsqldb.org/web/hsqlLicense.html) | No | Persistency |
| [H2](http://www.h2database.com/) | 1.2 | [H2](http://www.h2database.com/html/license.html) | No | Persistency |
| [Velocity](http://velocity.apache.org/) | 1.5 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | No | Utilities |
| [Maven](http://maven.apache.org/) | 3.0.2| [Apache2](http://maven.apache.org/license.html) | No | OpenSpaces Maven Integration |
| [Ant](http://ant.apache.org/) | 1.9.4 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | No | Examples |

{{% /tab %}}

{{%tab "Integrations"%}}


| Product | Version | License | Component | Documentation |
|:--------|:--------|:--------|:----------|:--------------|
|[OpenJPA](http://openjpa.apache.org/)      | 2.0.1 | [Apache2](http://openjpa.apache.org/license.html)          | Persistency | [JPA](/xap101/jpa-api-overview.html) |
|[Hibernate](http://www.hibernate.org/orm/) | 3.6.1 | [LGPL](http://hibernate.org/community/license/)            | Persistency | [Hibernate Space Persistency](/xap101/hibernate-space-persistency.html) |
|[Cassandra](http://cassandra.apache.org/)  | 1.1.6 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) |Persistency | [Cassandra Integration](/xap101/cassandra.html) |
|[MongoDB](http://www.mongodb.org/)         | 2.11.2 | [Creative Commons](http://creativecommons.org/licenses/by-nc-sa/3.0/) | Persistency | [MongoDB Integration](/xap101/mongodb.html) |
|[InfluxDB](https://influxdata.com/) | 0.9 (As of 10.2.1, 0.8 for 10.2.0) | [MIT](https://influxdb.com/docs/v0.9/about/licenses.html) | Metrics | [InfluxDB](/xap102adm/metrics-influxdb-reporter.html) |
|[Jetty](http://eclipse.org/jetty/)         | 8.1.8, 9.1.3 | [Apache2](http://www.eclipse.org/jetty/licenses.php) | Web Container | [Jetty Processing Unit Container](/xap101/web-jetty-processing-unit-container.html) |
|[JMS](http://java.sun.com/products/jms/)   | 1.1 | [Sun](http://www.opensource.org/licenses/sunpublic.php) | JMS | [JMS Messaging Support](/xap101/messaging-support.html) |
|[Mule](http://www.mulesoft.org/)           | 3.3.0 | [CPAL](http://www.mulesoft.org/licensing-mule-esb) | Mule | [Mule ESB](/xap100/mule-esb.html) |
|[Groovy](http://groovy-lang.org/)          | 1.8.6 | [Apache2](http://svn.codehaus.org/groovy/trunk/groovy/groovy-core/LICENSE.txt) | Dynamic Scripting | [Dynamic Scripting Support](/xap101/dynamic-language-tasks.html)|
|[Scala](http://www.scala-lang.org/)        | 2.10.1 | [Scala](http://www.scala-lang.org/license.html) | Scala | [Scala](/xap101/scala.html) |
|[MapDB](http://www.mapdb.org/)             | 1.0.7 | [Apache2](https://github.com/jankotek/MapDB/blob/master/license.txt) | Off Heap | [Off Heap RAM](/xap101adm/memoryxtend-ohr.html) |
|[snmp4j](http://www.snmp4j.org/)           | 1.11.2 | [Apache2](http://www.snmp4j.org/LICENSE-2_0.txt) | SNMP | [SNMP Connectivity via Alert Logging Gateway](/xap101/snmp-connectivity-via-alert-logging-gateway.html) |
|[log4j-snmp-trap-appender](http://code.google.com/p/log4j-snmp-trap-appender/) | 1.2.9 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | SNMP | [SNMP Connectivity via Alert Logging Gateway](/xap101/snmp-connectivity-via-alert-logging-gateway.html)  |

{{% /tab %}}

{{%tab "  UI "%}}


| Product | Version | License |
|:--------|:--------|:--------|
| [InfoNode](http://www.infonode.net/) | 1.6.1 | [InfoNode Software License](http://www.infonode.net/index.html?idwlicense) |
| [JChart2D](http://jchart2d.sourceforge.net/index.shtml) | 3.1.0 | [LGPL](http://www.gnu.org/copyleft/lesser.txt) |
| [BSF](http://jakarta.apache.org/bsf/) | 2.2 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) |
| [JGoodies](http://www.jgoodies.com/) | 2.3.1 | [BSD](http://www.opensource.org/licenses/bsd-license.html) |
| [JGraph](http://www.jgraph.com/) | 5.11.0.1 | [BSD](http://www.jgraph.com/license.html) |
| [JOI - Java Object Inspector](http://www.programmers-friend.org/JOI/) | 3.5.2 | [CPL](http://www.programmers-friend.org/cpl-v10.html) |
| [iSQL-Viewer](http://isql.sourceforge.net/) | 3.0.0 | [MPL 1.1](http://www.mozilla.org/MPL/) |

{{% /tab %}}

{{%tab "  Web UI "%}}


| Product | Version | License |
|:--------|:--------|:--------|
| [Google Web Toolkit](http://code.google.com/webtoolkit/) | 2.4.0 | [Apache2](http://code.google.com/webtoolkit/terms.html) |
| [FileUpload](http://commons.apache.org/fileupload/) | 1.2.2 | [Apache2](http://www.apache.org/licenses/) |
| [gwtupload](http://code.google.com/p/gwtupload/) | 0.6.3 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0) |
| [Commons Lang](http://commons.apache.org/lang/) | 2.6 | [Apache2](http://www.apache.org/licenses/) |
| [Highcharts](http://www.highcharts.com/products/highcharts) | 2.1.9 | [Commercial](http://highsoft.com/legal/Highslide-Software-License-1-3.pdf) |
| [Ext GWT](http://www.sencha.com/products/) | 2.2.5 | [Commercial](http://www.sencha.com/products/extjs/license/) |
| [jQuery](http://www.jquery.com/) | 1.8.1 | [MIT](https://github.com/jquery/jquery/blob/master/MIT-LICENSE.txt) |
| [Raphaël](http://www.raphaeljs.com/) | 2.1.0 | [MIT](http://raphaeljs.com/license.html) |
| [Dracula Graph](http://www.graphdracula.net/) | 0.0.3 | [MIT](http://www.opensource.org/licenses/mit-license.php) |
| [CCombo V2.0](http://www.christsam.blogspot.com/2011/05/ccombo-v20.html) | 2.0 | [MIT](http://www.opensource.org/licenses/mit-license.php) |
| [SexyCombo](http://vladimir-k.blogspot.com/2009/02/sexy-combo-jquery-plugin.html) | 2.1.3 | [MIT](http://www.opensource.org/licenses/mit-license.php) |
| [GwtQuery](http://code.google.com/p/gwtquery/) | 1.1.0 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0) |
| [CodeMirror](http://codemirror.net/) | 2.3.4 | [MIT](http://codemirror.net/LICENSE) |
| [Apache Commons Math v2.1](http://commons.apache.org/math) | 2.1 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0) |
| [Fugue Icons](http://p.yusukekamiyamane.com/) | 3.5.6 | [Creative Commons](http://p.yusukekamiyamane.com/) |
| [Alphanum Comparator](http://www.davekoelle.com/alphanum.html) | | [LGPL](http://www.gnu.org/licenses/lgpl.html) |
| [Jackson](http://wiki.fasterxml.com/JacksonHome) | 1.9.9 | [LGPL](http://www.gnu.org/licenses/lgpl.html) |
| [JGit](http://www.eclipse.org/jgit/) | 2.2.0 | [EDL](http://www.eclipse.org/org/documents/edl-v10.php) |
| [slf4j](http://www.slf4j.org/) | 1.7.2 | [MIT](http://www.slf4j.org/license.html) |
| [JCTerm](http://www.jcraft.com/jcterm/) | 0.0.11 | [GNU LGPL](http://www.gnu.org/licenses/lgpl.html) |
| [JSch](http://www.jcraft.com/jsch/) | 0.1.48 | [BSD](http://www.jcraft.com/jsch/LICENSE.txt) |
| [JZlib](http://www.jcraft.com/jzlib/) | 1.1.1 | [BSD](http://www.jcraft.com/jsch/LICENSE.txt) |
| [D3](http://d3js.org/) | 3.2.7 | [BSD](http://opensource.org/licenses/BSD-3-Clause) |

{{% /tab %}}

{{%tab "  XAP.NET "%}}


| Product | Version | License | Component |
|:--------|:--------|:--------|:----------|
| [Castle](http://www.castleproject.org/) | | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) | Remoting |

{{% /tab %}}

{{%tab "  XAP C++ "%}}


| Product | Version | License |
|:--------|:--------|:--------|
| [Winhoard - The Hoard Memory Allocator](http://www.cs.umass.edu/~emery/hoard/hoard-documentation.html) | | [Commercial](http://www.otc.utexas.edu) |
| [Xerces-C++](http://xerces.apache.org/xerces-c/) | | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) |
| [ACE](http://www.cs.wustl.edu/~schmidt/ACE.html) | | [ACE](http://www.cs.wustl.edu/~schmidt/ACE-copying.html) |
| [Boost](http://www.boost.org/) | | [Boost](http://www.boost.org/more/license_info.html) |

{{% /tab %}}

{{%tab "  REST "%}}


| Product | Version | License |
|:--------|:--------|:--------|
| [JSONDoc](http://jsondoc.org/) | 1.0.9 | [MIT](http://www.opensource.org/licenses/mit-license.php) |
| [Guava](https://code.google.com/p/guava-libraries/) | 15.0 | [Apache 2](http://www.opensource.org/licenses/mit-license.php) |
| [Javassist](http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/) | 3.18.2 | [Apache 2](http://www.opensource.org/licenses/mit-license.php) |
| [log4j](https://logging.apache.org/) | 1.2.17 | [Apache 2](http://www.opensource.org/licenses/mit-license.php) |
| [Reflections](https://code.google.com/p/reflections/) | 0.9.9 | [WTFPL](http://www.wtfpl.net/about/) |
| [Jackson](http://wiki.fasterxml.com/JacksonHome) | 2.3 | [Apache2](http://www.apache.org/licenses/LICENSE-2.0.html) |

{{% /tab %}}

{{% /tabs %}}
