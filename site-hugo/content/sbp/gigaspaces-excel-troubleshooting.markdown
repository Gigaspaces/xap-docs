---
type: post
title:  GigaSpaces-Excel Troubleshooting
categories: SBP
parent: excel-that-scales-solution.html
weight: 600
---

# Overview

# Writing a Hosting Application for Easy Debugging

{{% note %}}
If it's the first time you are running UDF of RTD, try running a simple example that only returns a value, to check that the API works in your environment.
{%%endnote%}}

Writing applicative UDF/RTD:

1. Comment the space API calls inside UDF/RTD code and make it return constant values.
2. Create a console or Win-form application that hosts the UDF/RTD DLL and debug it directly to solve the function implementation problems.
