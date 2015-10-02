---
type: post100sec
title:  User-Role Management
categories: XAP100SEC
parent: custom-security.html
weight: 200
---

Customize user/role management to meet your application requirements

The `DirectoryManager` interface provides an API for managing users and roles. Implementing this interface is **optional** - and is usually the case if you have an external tool that manages it for you.

From the `DirectoryManager` you can gain a `UserManager` interface and a `RoleManager` interface. Use the `UserManager` interface to create, delete, update `UserDetails` and the `RoleManager` interface to create, delete, update `RoleDetails`. This is just a logical separation - in reality you can choose how you want your users to be stored, and how you structure the one-to-many role to user relationship.

GigaSpaces security is not aware of the directory at all. As long as the authentication process manages to access the user storage, authenticate the user and return all its authorities.

#### Example usage of the `DirectoryManager` API

In the [Hello World example](./securing-the-helloworld-example.html), we presented a way to declare the users using the UI. It can also be done using the `DirectoryManager` API.

Using the API we would like to declare the following:

{{% note %}}
The "helloProcessor" user will be granted **`Take`** access for `HelloObject` and **`Write`** access for `ProcessedHelloObject`.
The "helloFeeder" user will be granted **`Write`** access for `HelloObject` and **`Read`** access for `ProcessedHelloObject`.
{{% /note %}}

The GigaSpaces `User` is the default implementation of `UserDetails`. It accepts an array or a sequence of `Authority`-ies (`varargs`). Here we added the `ClassFilter` to restrict access to this specific class.


```java
Properties securityProperties = new Properties();
SecurityManager securityManager = SecurityFactory.createSecurityManager(securityProperties);

DirectoryManager directoryManager = securityManager.createDirectoryManager(new User("admin", "admin"));
UserManager userManager = directoryManager.getUserManager();

userManager.createUser(new User("helloProcessor", "helloWorld",
        new SpaceAuthority(SpacePrivilege.TAKE, new ClassFilter("org.openspaces.example.helloworld.common.HelloObject")),
        new SpaceAuthority(SpacePrivilege.WRITE, new ClassFilter("org.openspaces.example.helloworld.common.HelloObject"))
));

userManager.createUser(new User("helloFeeder", "feedTheWorld",
        new SpaceAuthority(SpacePrivilege.WRITE, new ClassFilter("org.openspaces.example.helloworld.common.HelloObject")),
        new SpaceAuthority(SpacePrivilege.READ, new ClassFilter("org.openspaces.example.helloworld.common.HelloObject"))
));

directoryManager.close();
securityManager.close();
```
