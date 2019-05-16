---
type: post123
title:  Timeout, Filters and Lease Manager
categories: XAP123ADM, PRM
parent: working-with-spaces-gigaspaces-browser.html
weight: 200
canonical: auto
---



The **General** tab contains settings related to client and server timeout, and the **Lease Manager**.

{{%align center%}}
![Space TimeoutFiltersLease Manager - GigaSpaces Browser.jpg](/attachment_files/Space TimeoutFiltersLease Manager - GigaSpaces Browser.jpg)
{{%/align%}}

{{% tip "Tip"%}}
If your client application loses its connection to the server, you can follow a simple procedure to check whether the server erased any of your notify templates in the interim:

1. For each notify template, write an Entry to the space that matches the template.
1. Verify that you receive a notification.

If there is no notification, this means two things. First, while you were disconnected, new Entries matching the notify template entered the space. (You can try to find them; depending on their lease time, they may still exist.) Second, as a result, your notify template was erased.
{{% /tip %}}

The **Space Filters** option specifies whether filters should be enabled or disabled for this Space. Filters are hooks inside the GigaSpaces engine that enable integration with external systems and/or implementation of user-defined logic inside the Space. The `DefaultSecurityFilter` is part of every Space by default, and it controls user permissions for the Space. Click **Edit** to launch the Filter Management dialog, which allows you to add custom filters and to manage the behavior of the default and custom filters.

