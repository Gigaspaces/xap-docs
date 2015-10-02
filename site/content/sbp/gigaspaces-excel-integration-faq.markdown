---
type: postsbp
title:  GigaSpaces-Excel Integration FAQ
categories: SBP
parent: excel-that-scales-solution.html
weight: 400
---

{{% ssummary page%}}Frequently asked questions about the GigaSpaces-Excel integration.{{% /ssummary %}}

# Overview

The following FAQs deal with the GigaSpaces Excel Integration solutions.

# General FAQs

- [What are the **Prerequisites**?](prerequisites-gigaspaces-excel-integration.html)
- [Why does the Space Viewer toolbar not show up in Excel?](#The Space Viewer toolbar does not show up in Excel)

# The Space Viewer toolbar does not show up in Excel

### Problem

The Excel Space Viewer is installed on a laptop PC. It is working OK but after a few days, the Space Viewer toolbar does not appear in Excel.
Running the installation file (`GigaSpacesViewerSetup.msi`) and selecting **Add**/**Remove**/**Repair** works fine, but the toolbar still doesn't show up in Excel.

- **Operating system** -- Microsoft Windows XP Professional
- **Office version** -- Microsoft Office Small Business Edition 2003

### Solution

1. Open Excel
2. Click **Help** > **About Excel** > **Disabled Items**
3. Highlight the **mscorlib.dll** item and click **Enable**
