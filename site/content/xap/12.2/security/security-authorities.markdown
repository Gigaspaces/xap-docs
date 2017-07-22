---
type: post122
title:  Authorities and Privileges
categories: XAP122SEC, OS
parent: none
weight: 300
---

{{% ssummary %}}{{% /ssummary %}}


**Authority** refers to a grouping of privileges. A **Privilege** (permission) is a granted right to perform an operation/action on a resource. For example, a user may have a read privileges to access data.

```java
public interface Authority {

    /**
     * @return a representation of the granted authority
     */
    String getAuthority();
}
```

## Space Authority

The **Space Authority** consists of privileges for operation on space (stored) data. The operations are divided into five groups of interest.

|       |     |
|-------|-----|
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


#### Example

The `UserDetails.getAuthorities()` method lists the granted authorities of a user. An `Authority` represents a grouping of privileges. For example, a `SpaceAuthority` consists of write, read, execute privileges. Each of these can be specifically assigned to a user. These authorities are populated upon a user's authentication process.

Here is a simple user with read + write privileges construction:
```java
UserDetails user = new User("John Smith", "password", 
                        new SpaceAuthority(SpacePrivilege.READ), 
                        new SpaceAuthority(SpacePrivilege.WRITE));
```

{{% info %}}
The SecurityManager interface provides an extension to a user defined `DirectoryManager`. This allows you to connect to any storage to manage users and roles.
This method is ***not mandatory***, and implementors can throw `DirectoryAccessDeniedException` if management should be done via external user management tools.

See [Users and Roles](./security-directory-manager.html) section for more details.


```java
interface SecurityManager {
    ...
    DirectoryManager createDirectoryManager(UserDetails userDetails) throws AuthenticationException, 
                                                                               AccessDeniedException;
...
}
```
{{%/info%}}

## Role Authority

The `RoleAuthrority` represents a logical name for a role. This provides an authorities-to-role grouping. The `RoleDetails` defines the authorities belonging to a specific role. 

For example, lets define a guest role with read privileges assigned to an anonymous user:
```java
RoleDetails role = new Role("guest", new SpaceAuthority(SpacePrivilege.READ));
UserDetails user = new User("anonymous", new RoleAuthority("guest"));
```

And define a contributor, with read and write privileges:
```java
RoleDetails role = new Role("contributor", new SpaceAuthority(SpacePrivilege.READ), new SpaceAuthority(SpacePrivilege.WRITE));
UserDetails user = new User("gigaspacer", new RoleAuthority("contributor"));
```

This separation between users and roles allows to store the `RoleDetails` independent to the `UserDetails`.


## System Authority

The **System Authority** consists of two privileges and defines the distinction between a user who is allowed to define roles and a user which is only allowed to assign user's to predefined roles.
In general, one can have both management capabilities, but in some organizations this separation may be required.

|       |     |
|-------|-----|
| Manage Roles | Define roles (a set of privileges assigned to a logical role name) |
| Manage Users | Assign users to pre-defined roles, or assign user-specific privileges |


