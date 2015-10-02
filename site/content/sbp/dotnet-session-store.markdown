---
type: post
title:  XAP.NET ASP.NET Session State Store
categories: SBP
parent: solutions.html
weight: 250
---
{{% ssummary page %}} {{% /ssummary %}}

{{% tip %}}
**Summary:**  This pattern presents the HTTP Session sharing blueprint for IIS servers and .NET apps <br/>
**Author**: Ronnie Guha<br/>
**Recently tested with GigaSpaces version**: XAP.NET 9.7.0 x86<br/>
**Last Update:** May 2014<br/>

{{% /tip %}}


# XAP.NET ASP.NET Session State Store

It's becoming increasingly important for organizations to share HTTP session data across multiple data centers, multiple web server instances or different types of web servers. Here are few scenarios where HTTP session sharing is required: {{<wbr>}}

{{%vbar%}}
-  Multiple different Web servers running your web application - You may be porting your application from one web server to another and there will be a period of time when both types of servers need to be active in production.{{<wbr>}}

-  Web Application is broken into multiple modules - When applications are modularized such that different functionalities are deployed across multiple server instances. For example, you may have login, basic info, check-in and shopping functionalities split into separate modules and deployed individually across different servers for manageability or scalability. In order for the user to be presented with a single, seamless, and transparent application, session data needs to be shared between all the servers.{{<wbr>}}

-  Reduce Web application memory footprint - The web application storing all session within the web application process heap, consuming large amount of memory. Having the session stored within a remote process will reduce web application utilization avoiding garbage collocation and long pauses.{{<wbr>}}

-  Multiple Data-Center deployment - You may need to deploy your application across multiple data centers for high-availability, scalability or flexibility, so session data will need to be replicated.{{<wbr>}}
{{%/vbar%}}

{{%section%}}
{{%column width="70%" %}}
The GigaSpaces XAP.NET ASP.NET Session State Store architecture allows users to deploy their web application across multiple data centers where the session is shared in real-time and in a transparent manner. The HTTP session is also backed by a data grid cluster within each data center for fault tolerance and high-availability.
With this solution, there is no need to deploy a database to store the session, so you avoid the use of expensive database replication across multiple sites. Setting up GigaSpaces for session sharing between each site is simple and does not involve any code changes to the application.
{{%/column%}}
{{%column width="30%" %}}
{{%youtube "fgXdQhPkXBQ"  "XAP.NET ASP.NET Session State Store"%}}
{{%/column%}}
{{%/section%}}

# Overview

GigaSpaces XAP.NET ASP.NET Session State Store Management for .NET is designed to deliver the application maximum performance with **ZERO** application code changes. It features the following: {{<wbr>}}

{{%vbar%}}

-  Reduced IIS memory footprint storing the session within a remote Data Grid.{{<wbr>}}

-  No code changes required to share the session with other remote IIS servers.{{<wbr>}}

-  Application elasticity - Support session replication across different IIS applications located within the same or different data-centers/clouds allowing the application to scale dynamically without any downtime.{{<wbr>}}

-  Unlimited number of sessions and concurrent users support - Sub-millisecond session data access by using GigaSpaces In-Memory-Data-Grid as the session state store. {{<wbr>}}

-  Session replication over the WAN support - Utilizing GigaSpaces Multi-Site Replication over the WAN technology. Support data compression and encryption.{{<wbr>}}

-  HTTP Session data access scalability - Session data can utilize any of the supported In-Memory-Data-Grid topologies ; replicated or partitioned.{{<wbr>}}

-  Transparent IIS Failover - Allow IIS restart without any session data loss.{{<wbr>}}

-  Sticky session and Non-sticky session support - Your requests can move across multiple IIS seamlessly.{{<wbr>}}

{{%/vbar%}}

# Session State Options

Before proceeding with the following sections, please find some more information on various session state modes [here](http://msdn.microsoft.com/en-us/library/vstudio/ms178586(v=vs.100).aspx). There are pros and cons of these various approaches.

1)	Inproc mode has the best performance metrics but it only works best when you have a single server hosting a web application and web applications in which the end user is guaranteed to be re-directed to the one and only, and therefore, correct server. This mode is used when session data is not very critical as it can be in some other ways reconstructed.

2)	Out of process/StateServer mode is the only way that allows you to really distribute your applications inside and outside your data centers. This is the mode that one chooses when they cannot guarantee which web server would be serving the end user's request. Herewith you can get the performance of reading from memory, reliability (and associated concerns) of having sessions managed by a separate process for all the web servers involved.

3)	SQL Server mode is used when very high levels of reliability are required. In other words, a session and its attributes have to be guaranteed re-construction on the fly and the application absolutely requires this kind of data reliability. Keep in mind that this mode has the worst performance of all (as quite easily understood) and trades off reliability for performance. This also allows you to globally distribute your applications across datacenters but relies heavily on clustering SQL Server databases across these data centers globally.

We will be concentrating our discussions on using an out of process, custom storage provider for achieving true global, lightweight distribution of your application that provides better reliability than other modes discussed above.

# Various Scenarios

Below are various ASP.NET HTTP Session state sharing scenarios supported with XAP.NET. All of the scenarios are configured the same with Scenario 4 being the exception, and discussed later on in the article.
The application in all instances is configured to use a custom session state mode with the GigaSpaces session state provider.

## Scenario 1

{{%section%}}
{{%column width="80%" %}}
A single web application persisting and reading from XAP.NET to provide resilient session state.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/sbp/pics/iis-pic1.png"%}}
{{%/column%}}
{{%/section%}}

## Scenario 2
{{%section%}}
{{%column width="80%" %}}
Multiple instances of the same application distributed across a cluster of load balanced or distributed servers.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/sbp/pics/iis-pic2.png"%}}
{{%/column%}}
{{%/section%}}

## Scenario 3
{{%section%}}
{{%column width="80%" %}}
Multiple applications persisting and consuming session data from the same IMDG, in either a single server or clustered topology.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/sbp/pics/iis-pic3.png"%}}
{{%/column%}}
{{%/section%}}

## Scenario 4
{{%section%}}
{{%column width="80%" %}}
Any number of applications and servers communicating with the IMDG, deployed using GigaSpaces WAN Gateway technology. 
The difference in configuration for applications in this scenario depends on the GiagSpaces WAN Gateway deployment. 
The application would be configured to communicate with the local/near local active space instance.
{{%/column%}}
{{%column width="20%" %}}
{{%popup   "/sbp/pics/iis-pic4.png"%}}
{{%/column%}}
{{%/section%}}

# Prerequisites
The following instructions assume that you have downloaded/installed the prerequisites:

- [Download the sample ASP.NET web application](/sbp/download_files/GigaSpacesSessionStateProvider.zip)

- [Download XAP.NET latest version](http://www.gigaspaces.com/xap-download)

- [Download Microsoft .NET 4.5](http://www.microsoft.com/en-us/download/details.aspx?id=30653)

{{% note type="Application Platform" %}}
XAP.NET is platform dependent. Ensure your application, version of .NET, and server configuration use the same platform as the XAP.NET installation.{{% /note %}}

## The Sample Application
{{% warning %}}
If needed correct the reference to `Gigaspaces.Core.dll`, by adding a reference to the library shipped with XAP.NET.
The default installation of GigaSpaces will put the library in a path similar to `C:\GigaSpaces\XAP.NET 9.7.0 x64\NET v4.0.30319\Bin`
{{% /warning %}}

The HTTP Session Provider sample is a simple UI for inserting, updating, and deleting session items. 
The application can be run from Visual Studio, but the IMDG must be deployed before the application is ran or an 
exception will be encountered.

The sample application can be deployed into IIS and ran from Visual Studio at the same time, emulating a light weight 
clustered server environment. All applications expected to read and write data that will be consumed by other applications 
must share the same [machine key](http://msdn.microsoft.com/en-us/library/w8h3skw9(v=vs.100).aspx).

#### Configuration


```xml
<configuration>
  <system.web>
    <compilation debug="true" targetFramework="4.5" />
    <httpRuntime targetFramework="4.5" />
    <machineKey validationKey="E83700E53CC20595182307B502E45F0AE4C2ADBEB6CE5BBD2E88B2EE5AB8A6FDC633FBF69309A6D6D06B4B6DBF22E2DA96F0315ED97F65136DDCEFDB4779C3E8" 
                decryptionKey="06C735053A39D1A9BEA101A58BAFE8FDFF7FA59C51F50673" validation="SHA1" decryption="AES "/>
    <sessionState mode="Custom" customProvider="GigaSpaceSessionProvider" cookieless="false" timeout="5" regenerateExpiredSessionId="true">
      <providers>
        <!--
        the name type and connectionStringName should be set as follows,
        writeExceptionToEventLog states whether unexpected exception should be written to a log or thrown back to the client.
        supportSessionOnEndEvents states whether the provider should support triggering Session_End events when sessions expires.-->
        <add name="GigaSpaceSessionProvider" type="GigaSpaces.Practices.HttpSessionProvider.GigaSpaceSessionProvider" connectionStringName="SessionProviderSpaceUrl" 
             writeExceptionsToEventLog="false" supportSessionOnEndEvents="true"/>
      </providers>
    </sessionState>
  </system.web>
  <connectionStrings>
    <add name="SessionProviderSpaceUrl" connectionString="jini://*/*/sessionSpace" />
  </connectionStrings>
</configuration>
```

#### Deployment
Deploy an [IMDG](./dotnet-your-first-xap-application.html) with the name of `sessionSpace`.

Deploy the application using [Microsoft's publish web application](http://msdn.microsoft.com/en-us/library/dd465337(v=vs.110).aspx) feature built into Visual Studio. We've already defined a publish profile
to publish the application into the standard web directory of the local machine (`c:\inetpub\wwwroot\`). 

{{% note %}}
Publishing to `c:\inetpub\wwwroot\` requires running Visual Studio as an administrator.
{{% /note %}}

#### Interacting with the space
{{% info %}}There is nothing unique in how client applications interface with the session API.{{% /info %}}

```c#
public partial class Default : Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
            DisplaySessionData();
    }

    protected void SubmitForm_Click(object sender, EventArgs e)
    {
        Session[SessionKey.Text] = SessionValue.Text;
        DisplaySessionData();
    }

    protected void RemoveKey_Click(object sender, EventArgs e)
    {
        Session.Remove(SessionKey.Text);
        DisplaySessionData();
    }
    
    private void DisplaySessionData()
    {
        foreach (string sessionKey in Session.Keys)
        {
            WriteSessionDataToPage(sessionKey);
        }
    }
    
    private void WriteSessionDataToPage(string sessionKey)
    {
        var row = new TableRow();

        row.Cells.Add(new TableCell { Text = sessionKey });
        row.Cells.Add(new TableCell { Text = Convert.ToString(Session[sessionKey]) });

        sessionData.Controls.Add(row);
    }
}
```
