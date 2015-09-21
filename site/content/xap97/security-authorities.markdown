---
type: post97
title:  Authorities
categories: XAP97
parent: security-overview.html
weight: 120
---

{{% ssummary %}}{{% /ssummary %}}


There are four categories of user authorities. These categories allow the flexibility of securing the different components of GigaSpaces.
"**Authority**" refers to an abstraction given to a set of privileges of the same category. A "**Privilege**" (permission) is a granted right to perform an operation/action on a resource. For example, a user may have a `Grid Authority` of type `Provision PU`, which allows it to deploy and un-deploy in the Grid.

# System Authority

The **System Authority** consists of two privileges and defines the distinction between a user who is allowed to define roles and a user which is only allowed to assign user's to predefined roles.
In general, one can have both management capabilities, but in some organizations this separation may be required.

|                   |                       |
|-------------------|-----------------------|
| Manage Roles | Define roles (a set of privileges assigned to a logical role name) |
| Manage Users | Assign users to pre-defined roles, or assign user-specific privileges |

# Grid Authority

The **Grid Authority** consists of privileges for managing the Grid and its Services (GSMs, GSCs, Processing Units).

|                   |                       |
|-------------------|-----------------------|
| Provision PU | Deploy, Un-deploy of processing units |
| Manage PU | Scale up/down, Relocate, Restart PU instance, Destroy PU instance |
| Manage Grid | Start, Terminate, Restart of GSC/GSM/LUS via GSA |

In distributed systems, it may be confusing as to which service is performing the authorization. The GSM is responsible for deploying, un-deploying, scaling, relocating, restarting and destroying of processing units. The GSA (if available) is responsible for starting, terminating restarting of GSC/GSM/LUS. The GSC on the other hand, mainly delegates the calls to the managing GSM (e.g. relocate).


{{% note %}}
When deploying an [elastic processing unit](./elastic-processing-unit.html), the *Provision PU* privilege is not enough - *Manage PU* and *Manage Grid* are required as well, since an elastic PU requires scaling and grid management.
{{%/note%}}

# Space Authority

The **Space Authority** consists of privileges for operation on space (stored) data. The operations are divided into five groups of interest.

|                   |                       |
|-------------------|-----------------------|
| Write | Write and Update operations |
| Create| Write only (no Updates) operations|
| Read | Read, Count and Notify operations |
| Take | Take and Clear operations |
| Alter | Register type descriptor, Clean and Drop-Class operations |
| Execute | Execute tasks |

When you grant a privilege you are allowing the user to perform the operation on the space. If you wish to restrict the operation to certain classes, a 'Class-Filter' may be defined.

#### Restricting of privileges

A 'Class-Filter' **allows** or **denies** operations on certain classes based on their fully qualified name, package name, or wild-card manipulation. For example, you may define a user with **Read** privileges for Class `com.gigaspaces.office.Employee`, **Write** privileges for Package `com.gigaspaces.vacations.\*` and **Take** privileges for a match of `com.gigaspaces.sickdays*`. You may also define a user which is **denied** of reading `com.gigaspaces.salary.*`.

#### Class-Filter matching using Hierarchy

When performing a matching operation (e.g. space read), a template is supplied. Matches can be returned also from subclasses of this template.
Take for example, the following hierarchy:

- Shape
    - Circle
    - Rectangle
        - Square
    - Triangle

When we read using a `Shape` template, a `Shape`, `Circle`, `Rectangle`, `Triangle` and even `Square` are part of the result set.
But if we read using a `Rectangle` template, only it and its subclass `Square` are part of a result set.

This should be considered when you **allow** a Class-Filter for _matching_ operations (**Read**, **Take**, **Alter**).

But, when you **deny** a Class more caution should be taken. If you **deny** a `Rectangle` Class from being read it means you can't perform read with this class. But it does not mean you cannot perform read with `Square`. Moreover, it does not mean that `Rectangle` won't be returned in the result set when performing read with `Shape`. If your packages are flexible you can deny using wildcard matching or package matching. Otherwise you need to **deny** _all_ classes in the hierarchy.

# Monitor Authority

The **Monitor Authority** consists of privileges for monitoring the Grid and its Processing Units.
Note that the monitoring is secured only by the 'tooling' (CLI/UI).

|                   |                       |
|-------------------|-----------------------|
| Monitor JVM | Monitoring of JVM statistics |
| Monitor PU | Monitoring of Processing Units (classes, connections, statistics, etc.) |
