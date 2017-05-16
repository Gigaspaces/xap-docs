---
type: post121
title:  Availability Biased
categories: XAP121ADM, PRM
parent: leader-election.html
weight: 300
---

# Overview

{{% imagertext "/attachment_files/lus4.jpg" %}}
Space instances register themselves with the lookup service. Space instances go through an active-election process, discovering current instances and electing a primary
Active-election is 3-phase procedure: pending, prepare, active
{{% /imagertext%}}
