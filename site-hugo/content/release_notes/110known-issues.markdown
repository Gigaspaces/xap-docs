---
type: post
title:  Known Issues and Limitations
categories: RELEASE_NOTES
parent: xap110.html
weight: 400
---


Below is a list of known issues in GigaSpaces 11.0




| Key | Summary | SalesForce ID | Workaround | Platform/s |
|:-------|:--------|:----------------|:------------------|:----------|
| GS-12817 | When the Compound Index contain more than 2 properties, the order might affect the Write throughput | 10356 | | Java |
| <nobr>GS-12760</nobr> | Using @SpaceClassConstructor in order not to define setter result in exception querying the type from both web-ui and gs-ui | 10286 | | Java |
| GS-12758 | SQLQuery.equals() do not check projections | 10285 | | Java | 
| GS-12750 | Try to revert blobstore ops' in backup after blobstoreexception | | | Java | 
| GS-12719 | Failed to deploy a valid PU after undeploy non valid PU with the same name | 10246 | | Java | 
| GS-12709 | Webui displays wrong screen when pressing config without marking a PU | | | All | 
| GS-12704 | .Net - NULL object reference exception in ProcessingUnitStatusChangedEventArgs.PreviousState on first event | 10220 | | .Net | 
| GS-12701 | GSA shutdown hook called when outbound "all-traffic" rule is removed | | | All |
| GS-12686 | Both GUI and web GUI shows negative memory utilization on Solaris | 10209 | | Java | 
| GS-12674 | Split-brain might cause displaying of redundant machine in web-ui | | | Java | 
| GS-12534 | SimpleNotifyContainerConfigurer.create might block indefinitly if server is not responsive | 9931 | | All | 
