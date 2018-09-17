---
type: post100
title:  Global HTTP Session Sharing
categories: XAP100
parent: web-application-overview.html
weight: 600
---



With XAP you can share HTTP session data across multiple data centers, multiple web server instances or different types of web servers.



XAP 10.0 Global HTTP Session Sharing includes the following new features:

- Delta update support - changes identified at the session attribute level.
- Better serialization (Kryo instead of xstream)
- Compression support
- Principle / Session ID based session management. Allows session sharing across different apps with SSO
- Role based SSO Support
- Improved logging


{{% info "Licensing "%}}
This feature requires a separate license in addition to the XAP commercial license. Please contact [GigaSpaces Customer Support](http://www.gigaspaces.com/content/customer-support-services) for more details.
{{% /info %}}


<br>

{{%fpanel%}}

[Overview](./global-http-session-sharing.html){{<wbr>}}
Configure your web application to use the XAP session manager, deploy the XAP in-memory data grid (IMDG) and deploy your web application.

[Configuration & Deployment](./global-http-session-sharing-configuration.html){{<wbr>}}
Configure the web application and Shiro for deployment on the grid.

[Apache Load Balancer](./global-http-session-sharing-load-balancer.html){{<wbr>}}
Configuring Apache load balancer.
{{%/fpanel%}}



#### Additional resources

{{%youtube "gRdGWMigJBI"  "Global HTTP Session sharing"%}}


