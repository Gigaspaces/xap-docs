---
type: post123
title:  Directory Management
categories: XAP123SEC, OSS
parent: none
weight: 400
---



The users `UserDetails` are to be stored in some kind of storage. Usually this storage provides tooling for managing the user data. The `SecurityManager.authenticate(UserDetails userDetails)` method tries to authenticate the user by accessing this storage, populating the `Authentication` result with the user authorities. This is specific to how the user's details and authority privileges are stored.

XAP provides an abstraction (`DirectoryManager`) which may be used to ease implementation. However, this is ***not mandatory** and is up to the implementor of the `SecurityManager` to decide.

## Directory Manager interface

The users (`UserDetails`) and roles (`RoleDetails`) can be managed by acquiring a `DirectoryManager`, through which you may administer the users and roles. It is a simple API for creating, deleting, updating and mapping of users and roles.

We provide two distinct privileges for managing the directory when assigned with the `SystemAuthority` - one for user management `SystemPrivilege.MANAGE_USERS` and one for role management `SystemPrivilege.MANAGE_ROLES`.

## User Manager interface

The [DirectoryManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/DirectoryManager.html) interface provides a means to managing users using the [UserManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/UserManager.html) API. Access should be granted only to users with `MANAGE_USERS` authority.

The [UserManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/UserManager.html) interface has simple methods for declaring users:


```java
public interface UserManager {
    ...
    void createUser(UserDetails)
    UserDetails getUser(String)
    void deleteUser(String)
    void updateUser(UserDetails)
    ...
}
```

## Role Manager interface

The [DirectoryManage]({{% api-javadoc %}}/com/gigaspaces/security/directory/DirectoryManager.html) interface provides a means to managing roles using the `RoleManager` API. Access should be granted only to users with **MANAGE_ROLES** authority.

The [RoleManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/RoleManager.html) interface has simple methods for declaring roles:


```java
public interface RoleManager {
    ...
    createRole(RoleDetails)
    RoleDetails getRole(String)
    deleteRole(String)
    updateRole(RoleDetails)
    ...
}
```

## Using the API

The directory manager is accessible via the [SecurityManager]({{% api-javadoc %}}/com/gigaspaces/security/SecurityManager.html). In the example below, the user `admin/admin` has both `MANAGE_USERS` and `MANAGE_ROLES`. We will use this user to gain access to the directory manager. Also, we have two default implementations [User]({{% api-javadoc %}}/com/gigaspaces/security/directory/User.html) and [Role]({{% api-javadoc %}}/com/gigaspaces/security/directory/Role.html) implementing [UserDetails]({{% api-javadoc %}}/com/gigaspaces/security/directory/UserDetails.html) and [RoleDetails]({{% api-javadoc %}}/com/gigaspaces/security/directory/RoleDetails.html) respectively.


```java
Properties securityProperties = new Properties();
SecurityManager securityManager = SecurityFactory.createSecurityManager(securityProperties);

DirectoryManager directoryManager = securityManager.createDirectoryManager(new User("admin", "admin"));
```

We can now add a new user using the [UserManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/UserManager.html) API. "Alice" will be added with `READ` authority for class "eg.ClassA".


```java
UserManager userManager = directoryManager.getUserManager();

userManager.createUser(new User("alice", "password",
        new SpaceAuthority(SpacePrivilege.READ, new ClassFilter("eg.ClassA"))
    ));
```

A role can be declared using the [RoleManager]({{% api-javadoc %}}/com/gigaspaces/security/directory/RoleManager.html) API. The "author" role will be added with `READ, WRITE, TAKE` authorities for class "eg.ClassA".


```java
RoleManager roleManager = directoryManager.getRoleManager();

roleManager.createRole(new Role("author",
        new SpaceAuthority(SpacePrivilege.READ, new ClassFilter("eg.ClassA")),
        new SpaceAuthority(SpacePrivilege.WRITE, new ClassFilter("eg.ClassA")),
        new SpaceAuthority(SpacePrivilege.TAKE, new ClassFilter("eg.ClassA"))
    ));
```

We can assign this role to user "bob".


```java
userManager.createUser(new User("bob", "password",
        new RoleAuthority("author")
    ));
```

The `ClassFilter` is one of the restrictive filters available. There is also a `RegexFilter`, `PackageFilter`, etc. A handy utility is the wildcard expression converter which converts a string expression to a filter. This is used by the UI dialog when creating filters.


```java
userManager.createUser(new User("carol", "password",
        new SpaceAuthority(SpacePrivilege.READ,
				WildcardExpressionToFilterConverter.convert(
						"eg.Class*", true))
    ));
```
