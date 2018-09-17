---
type: post
title:  Product Life Cycle and End-of-Life Policy
categories: RELEASE_NOTES
parent: none
weight: 100000
---

This page explains the life cycle and End-of-Life (EOL) policy for GigaSpaces' products and related third-party products, along with information about extended support, upgrading, and [available assistance](http://www.gigaspaces.com/services-offering-overview). The page also provides a product support roadmap to assist customers with future planning. All customers should refer to the specific service list they purchased to determine the specific level of support to which they are entitled. 

# GigaSpaces Product Life Cycle Policy

The GigaSpaces product life cycle policy is based on the following guidelines (unless stated otherwise in your service contract):

- New policy change: For all versions, GigaSpaces provides technical product support for 2 years, starting from the next upcoming public GA of the release of the relevant product version.
- Effective as of the official Support Discontinuance Date (2 years starting from next upcoming public GA) these versions will no longer be supported under a standard support agreement. Extended Support may be available for an additional limited period, for an extra fee. Please contact GigaSpaces' Customer Service for more information.
- Maintenance releases (service packs) are scheduled to be publicly available every 6-8 weeks and are driven by customer feedback. The above, is the planned schedule however does not constitute a firm commitment to produce a release at every such interval.
 
## Backwards Compatibility 

GigaSpaces guarantees backwards compatibility under the following conditions:

- APIs - For two major versions going forward, and/or two years after a change in functionality.
- Client-server compatibility - For two major versions (clients are guaranteed to work with servers running the next major version).
- Server-server compatibility - For service packs on the same version only.

For example, if a new feature is introduced in version 10.0 that is meant to replace an existing feature, the existing feature will be deprecated as of version 10.0, and supported through 11.x. Starting from 12.x, backwards compatibility will no longer be guaranteed for the deprecated feature.

{{%warning "Important"%}}
Backwards compatibility is *not* guaranteed between server-client, where the server is running an older version and the client is running a newer version of the InsightEdge Platform or XAP.
{{%/warning%}}

 
##  End-of-Life Policy
 
GigaSpaces products may reach End of Life for a number of reasons, including product evolution due to market requirements, or replacement by more advanced versions with richer functionality.

GigaSpaces recognizes that end-of-life milestones can have a signification impact on the IT environment of our customers. With this in mind, we have put out a long-term end-of-life policy that is published in advance, to assist customers in  managing the transition to a newer version, and to help customers understand how GigaSpaces' Customer Service can help with the migrating affected applications.
 

## Extended Support 
 
- Extended Support (beyond the Support Discontinuance Date) enables you to upgrade your systems to the most recent version after the Support Discontinuance Date. Extended Support may be available for a limited period for an extra fee. It includes telephone support and access to a web support portal.
- During the Extended Support period, temporary patches are available but there are no planned service packs.
- Extended Support and maintenance services for GigaSpaces products are provided on the condition that the customer has already paid all the standard support and maintenance service fees owed up to the time Extended Support is required.
- To extend your support beyond a release’s Support Discontinuance Date, contact GigaSpaces [Customer Support](mailto:support@gigaspaces.com).

# Upgrade Path and Assistance

- The recommended upgrade path is to the latest GigaSpaces {{%latestxaprelease%}} series of editions. Please refer to this Upgrade Guide for more details.
- Customers on annual subscriptions can also upgrade to the latest release.
- Upgrading to the latest stable version provides access to all of the current bug fixes and improvements, and ensures our ability to properly support you should you encounter any issues. Furthermore, GigaSpaces has made tremendous improvements in scalability, stability, performance, user experience, and functionality in version {{%latestxaprelease%}}. Upgrading your application to the latest {{%latestxaprelease%}} version and migrating to our latest best practices will ensure that you leverage the product's technology to its fullest potential, minimizing deployment risks while shortening project cycle times. GigaPro are modular consultancy and professional services designed to ensure smooth and effective deployment of GigaSpaces solutions.

Upon request, GigaSpaces [Consulting Services](mailto:ps@gigaspaces.com) experts can offer you [GigaPro™ Upgrade Support Package](http://www.gigaspaces.com/services-offering-overview), which include the following services tailored to your needs:

{{%vbar%}}
- Supporting the new version rollout process, tuning, and stability

- Upgrade assessment

- System architecture design

- XAP delta core training (educational program as part of GigaSpaces University)

- Assisted development services during upgrade – on demand

- Code and system review

- Upgraded production environment setup

- Extended rollout support
{{%/vbar%}}



# Product Release Plan and End-of-Life Retirement Calendar


|Product Release [1] |	GA Date | Last Maintenance Service Pack Release|Planned Next Maintenance Service Pack Release [2]|Support Discontinuance ("End of Life") [3]|
|--------------------|----------|--------------------------------------|-------------------------------------------------|------------------------------------------|
|4.x	         |April 2005    |4.1 b1185                             |No Plans|	April 2008 |
|5.1	         |Sept. 2006	|5.1 b1603	                           |No Plans|	September 2008 |
|5.2 b1708	     |Dec. 2006     |5.2.3 b1780 in June 2007	           |No Plans|June 2009 |
|6.0 b1855	     |Aug. 2007	    |6.0.5 b2150 in May 2007               |No Plans|August 2009 [4]|
|6.6 b2601 (or R6.5) |Sep 2008	|6.6.5 b3320 in June 2009	           |No Plans|September 2010 [4]|
|7.0 b3500	     |July 2009	    |7.0.4 b4100 in July 2010	           |No Plans|July 2011[4] ||
|7.1 b4300	     |April 2010	|7.1.4 b4750 in Feb. 2011	           |No Plans|April 2012 [4]|
|8.0 b5000	     |January 2011	|8.0.8 b6380 in March 2013	           |No Plans|January 2013|
|9.0 b6500	     |May 2012 	    |9.0.2 b6900 in Aug. 2012	           |No Plans|October 2014|
|9.1 b7500	     |Oct. 2012 	|9.1.2 b7920 in Feb. 2013	           |No Plans|April 2015|
|9.5 b8500	     |April 2013	|9.5.2 b8900 in Aug. 2013	           |No Plans|July 2015|
|9.6 b9500	     |July 2013	    |9.6.2 b9900 in Oct. 2013	           |No Plans|January 2016|
|9.7 b10496      |Jan. 2014	    |9.7.2 b11000 in Feb. 2015	           |No Plans|July 2016|
|10.0 	         |July 2014     |10.0.1 b11800 in Aug. 2014            |10.0.2	|March 2017 |
|10.1 b12600     |March 2015	|10.1.1 b12800 in April 2015	       |No Plans|July 2017	 |
|10.2 b13800     |July 2015	    |10.2.1 b14000 in Nov. 2015	           |No Plans|April 2018 |	 
|11.0 b14800     |April 2016	| 	                                   |No Plans|January 2019 |     
|12.0 b16000     |<nobr>January 2017<nobr> |12.0.1 b16600 in Sep. 2016 |No Plans|March 2019  |            
|12.1 b17100     |March 2017	|12.1.1 b17100 in June 2017            |No Plans| September 2019 |
|12.2 b18000     |Sept. 2017	|12.2.1 b18100 in Nov. 2017            |No Plans| March 2020 |
|12.3 b19000     |March 2018	|12.3.1 b19300 in July 2018            |No Plans|  |
                         
        
**[1] Product Releases** – GigaSpaces R4.x and R5.x refer to all editions including the Enterprise (EE), Caching (EDG) and Community editions. R6.x refers to all editions including the XAP, Caching (EDG) and Community editions.<br>
**[2] Planned Maintenance Service Pack Release** – Planned dates for release of additional service packs for the specific product version. These dates are provided for planning purposes only, are subject to change at any time and are not to be considered commitments.<br>
**[3] Release End-of-Life Retirement Date** – effective date of the standard support and service discontinuance. Extended Support might be available beyond this date. <br>
**[4] Extended Support currently available**

#  JVM and Third-Party End-Of-Life Policy
 
- Java SE 1.4 EOL – Based on information made publicly available by Sun Microsystems, as of October 30th 2008 Java SE 1.4 SDK has reached its End of Service Life (EOSL).
- Java SE 1.5 EOL – Based on information made publicly available by Sun Microsystems, as of October 30th 2009 Java SE 1.5 SDK has reached its End of Service Life (EOSL).
- Sun also ceased to support the J2SE 1.4 and 1.5 JVMs. In addition, the two other major JVM vendors, namely IBM and Oracle, announced their limited ability to support these JVMs in light of Sun's announcement. This in turn will limit GigaSpaces’ ability to provide support for applications running on these JVMs. However, we will continue to support XAP editions up to version 6.6 (for Java 1.4) and up to version 7.1 (for Java 1.5) in these environments on a best-effort basis. Customers concerned about this EOL announcement are encouraged to contact their JDK vendor to explore the extended support entitlements beyond the stated EOL period.
- GigaSpaces recommends that customers upgrade to a fully-supported environment, such as the latest version of InsightEdge or XAP {{%latestxaprelease%}} and the latest Java 1.8 SDK.
- From version 7.0 onwards, XAP no longer supports the Java 1.4 SDK, and requires Java 1.5 SDK or higher.
- From version 8.0 onwards, XAP no longer supports the Java 1.5 SDK, and requires Java 1.6 SDK or higher. For details about Java 1.4 and 1.5 SDK EOL, refer to the following page: http://java.sun.com/products/archive/eol.policy.html
- For more details, refer to the page that discusses the third-party products shipped with InsightEdge and XAP.
