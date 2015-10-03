---
type: post110tut
title:  Security
categories: XAP110TUT
weight: 1400
parent: none
---


{{%bannerleft "/attachment_files/qsg/security.png" %}}
In this part of the tutorial we will introduce you to XAP security, where it fits in the XAP architecture, which components can be secured, and how to configure and customize the security depending on your application security requirements.
XAP Security provides comprehensive support for securing your data and services.
{{%/bannerleft%}}






#  Security in XAP
Security is made up of *Authentication* and *Authorization*.

### Authentication
Authentication is the process of establishing and confirming the authenticity of a principal. A principal in GigaSpaces terms, means a user (human or software) performing an action in your application. A principal in XAP terms means a user (human or software) performing an action in your application. XAP Security is equipped with standard encryption algorithms (such as AES and MD5), which can be easily configured and replaced. The authentication layer is provided with a default implementation, which can be customized to integrate with other security standards. This layer is also known as the authentication manager. You can integrated the authentication layer through Spring Security to use LDAP or Data Base authentication.


### Authorization
Authorization refers to the process of deciding whether a principal is allowed to perform a certain action. The authorization decision layer is totally independent from the authentication layer. The authorization decision manager is internal to XAP components and is used to intercept unauthorized access/operations to data and services. This layer uses roles that are made up of authorities which contain a set of permissions.




# Role Based Security
XAP's authorization implementation is based on roles. A role is comprised of a collection of *authorities* where an authority has a set of *permissions*. There are four categories of user authorities; System, Grid, Space and Monitoring.


### System Authority
The System Authority consists of two privileges:



|     |      |
|------|------|
|Manage Roles|Define roles (a set of privileges assigned to a logical role name)|
|Manage Users|Assign users to pre-defined roles, or assign user-specific privileges|



### Grid Authority
The Grid Authority consists of privileges for managing the Grid and its Services (GSMs, GSCs, Processing Units).

{{%indent%}}

|   |    |
|----|----|
|Provision PU	 |Deploy, Un-deploy of processing units|
|Manage PU	 |Scale up/down, Relocate, Restart PU instance, Destroy PU instance|
|Manage Grid    |Start, Terminate, Restart of GSC/GSM/LUS via GSA|
{{%/indent%}}


### Space Authority
The Space Authority consists of privileges for operations on space data.

{{%indent%}}

|   |    |
|----|----|
|Write	  |Write and Update operations|
|Create  |Write only (no Update) operations|
|Read	  |Read, Count and Notify operations|
|Take	  |Take and Clear operations|
|Alter	  |Register type descriptor, Clean and Drop-Class operations|
|Execute |Execute tasks|
{{%/indent%}}

### Monitoring Authority
The Monitor Authority consists of privileges for monitoring the Grid and its Processing Units.
Note that the monitoring is secured only by the 'tooling' (CLI/UI).

{{%indent%}}

|   |    |
|----|----|
|Monitor JVM	 |Monitoring of JVM statistics|
|Monitor PU	 |Monitoring of Processing Units (classes, connections, statistics, etc.)|
{{%/indent%}}

{{%refer%}}[Security Authorities]({{%currentsecurl%}}/security-authorities.html){{%/refer%}}


# Managing Roles
You can create new roles and users with a configuration file or with the Admin UI. Here is an example how you use the Admin UI to create/update roles. Lets create a role called "training" that can access and interact with our xapTutorialSpace, but does not have monitoring authority.
Start the Admin UI:

```bash
GS_HOME\bin\gs-ui.sh
```
The default username and password for the security are admin/admin.

{{%section%}}
{{%column width="20%"%}}
{{%popup "/attachment_files/qsg/Admin0.png"  "Start UI Admin"%}}
{{%/column%}}

{{%column width="20%"%}}
{{%popup "/attachment_files/qsg/Security-select.png"  "Select Security options"%}}
{{%/column%}}

{{%column width="20%"%}}
{{%popup "/attachment_files/qsg/SecurityLogin.png"  "Login admin/admin default"%}}
{{%/column%}}
{{%column width="20%"%}}
{{%popup "/attachment_files/qsg/CreateRole.png"  "Create a Role"%}}
{{%/column%}}

{{%/section%}}
{{%section%}}
{{%column width="20%"%}}
{{%popup "/attachment_files/qsg/CreateRole2.png"  "Display Roles"%}}
{{%/column%}}
{{%/section%}}

You may have noticed that you can assign fine grained access control for space operations (read/write/etc) per space class(slide 4).

{{%refer%}}[Web Management Console]({{%currentsecurl%}}/gigaspaces-management-center-ui-security.html){{%/refer%}}


# Managing Users
A user is assigned roles. You can create new roles and users with a configuration file or with the Admin UI. Here is an example how you use the Admin UI to create/update users. Lets create a user called student and assign the role training we have just created in the step above. A user can have multiple roles.

{{%section%}}
{{%column width="30%"%}}
{{%popup "/attachment_files/qsg/SecurityLogin.png"  "Login"%}}
{{%/column%}}

{{%column width="30%"%}}
{{%popup "/attachment_files/qsg/CreateUser1.png"  "Create User"%}}
{{%/column%}}

{{%column width="30%"%}}
{{%popup "/attachment_files/qsg/CreateUser2.png"  "Display Users"%}}
{{%/column%}}
{{%/section%}}

You can create Users and Roles via the API. Here is an example:

{{%tabs%}}
{{%tab "  User "%}}

```java
public void createUser() {
    Properties securityProperties = new Properties();
    SecurityManager securityManager = SecurityFactory
		.createSecurityManager(securityProperties);

    directoryManager = securityManager.createDirectoryManager(new User(
			"admin", "admin"));
    // Create a new Role
    this.createRole();
    // Create the User
    User user = new User("student", "student123", new RoleAuthority("training"));
    // Register the new User
    UserManager userManager = directoryManager.getUserManager();
    userManager.createUser(user);
}
```
{{% /tab  %}}
{{%tab "  Role "%}}

```java
private Role createRole() {
   RoleManager roleManager = directoryManager.getRoleManager();

   Role role = new Role("training",
	new SpaceAuthority(SpacePrivilege.READ),
        new SpaceAuthority(SpacePrivilege.WRITE),
        new SpaceAuthority(SpacePrivilege.TAKE));

  roleManager.createRole(role);
  return role;
}
```
{{% /tab  %}}
{{% /tabs %}}

{{%refer%}}[Web Management Console]({{%currentsecurl%}}/gigaspaces-management-center-ui-security.html){{%/refer%}}


# Securing XAP Components
{{%section%}}
{{%column width="70%" %}}
XAP has security built over the major component, GSA, GSM, GSC and Processing Unit with Space data. Each one of these components can be configured according to your application security requirements.
 {{%/column%}}
{{%column width="20%" %}}

{{%popup   "/attachment_files/qsg/security_ovreview.jpg"%}}

{{%/column%}}
{{%/section%}}

### Data Security
A secured embedded Space protects access (to data) which is granted only to users with sufficient privileges. Here is an example how to create a secure space with no internal services:.

{{%tabs%}}
{{%tab "  Java "%}}

```java
EmbeddedSpaceConfigurer configurer = new EmbeddedSpaceConfigurer("xapTutorialSpace").secured(true);
GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();
```
{{% /tab  %}}

{{%tab "  Spring "%}}

```xml
<os-core:embedded-space id="space" name="xapTutorialSpace">
    <os-core:security secured="true" />
</os-core:space>
```
{{% /tab  %}}
{{% /tabs %}}


When a remote Space proxy connects to a secured Space, it must provide security credentials (username and password).

{{%tabs%}}
{{%tab "  Java "%}}

```java
public void setupSpace()
{
   SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("xapTutorialSpace").credentials("student", "password");
   GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).gigaSpace();
}
```
{{% /tab  %}}
{{%tab "  Spring "%}}

```xml
<os-core:space-proxy id="space" name="xapTutorialSpace">
    <os-core:security username="student" password="student123" />
</os-core:space-proxy>
```
{{% /tab  %}}
{{% /tabs %}}



{{%refer%}}[Securing Your Data]({{%currentsecurl%}}/securing-your-data.html){{%/refer%}}





### Grid Security
Grid Security is enabled in XAP by setting a global system property. This system property can be set when using the deployment scripts, or it can be appended in the setenv.sh/bat script in the GS_HOME/bin directory. Once the Grid Security is enabled, you can use the predefined roles and user names to protect and control the grid access.


```bash
-Dcom.gs.security.enabled=true
```


This property affects the GSA, GSM, GSC and standalone PU instances with a space.

{{%refer%}}[Securing Grid Services]({{%currentsecurl%}}/securing-the-grid-services.html){{%/refer%}}



### Transport Security
The transport layer can be secured using an SSL communication filter.

{{%refer%}}[Securing the Transport Layer]({{%currentsecurl%}}/securing-the-transport-layer-using-ssl.html){{%/refer%}}


{{%info%}}This concludes this tutorial. For more detailed information of the XAP platform and its API please consult the [Programmers Guide]({{%currentjavaurl%}}) {{%/info%}}


