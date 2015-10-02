---
type: post101adm
title:  Timeout, Filters and Lease Manager
categories: XAP101ADM
parent: working-with-spaces-gigaspaces-browser.html
weight: 500
---



The **General* tab contains settings related to client and server timeout, and the *Lease Manager**.

![Space TimeoutFiltersLease Manager - GigaSpaces Browser.jpg](/attachment_files/Space TimeoutFiltersLease Manager - GigaSpaces Browser.jpg)

{{% tip %}}
If your client application loses its connection to the server, you can follow a simple procedure to check whether the server erased any of your notify templates in the interim. For each notify template, write an Entry to the space that matches the template, and see if you receive a notification. If not, this means two things: First, while you were disconnected, new Entries matching the notify template entered the space. You can try to find them, and if you like depending on their lease time, they may still exist. Second, as a result, your notify template was erased.
{{% /tip %}}

**Space Filters** -- this check-box specifies whether space filters should be enabled or disabled for this space. Filters are hooks inside the GigaSpaces engine that enable integration with external systems and/or implementation of user-defined logic inside the space. One filter, `DefaultSecurityFilter`, comes with every space by default, and controls user permissions for the space.
**Edit** button -- launches the **Filter Management** dialog, which allows you to add filters and manage the behavior of the default filter and custom filters you have added.

