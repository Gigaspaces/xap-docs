---
type: post100
title:  Java Security Policy File
categories: XAP100SEC
parent: security-administration.html
weight: 500
---



When accessing the space, you should have the `java.security.policy` property set correctly.
You may use a default security file bundled with the distribution. It is located at:
`<XAP Root>\policy\policy.all`:


```console
grant {
    permission java.security.AllPermission "", "";
};
```


You can augment or replace the default JVM runtime permissions using the `java.security.policy` system property to specify the path to a policy file. This System property is unique in that it can use `=` or `==` to indicate whether the policy file specified should append to, or replace the default permissions. If you use the "=", the permissions in the specified policy file are appended to the default permissions. If you use the `==`, then the permissions in the specified policy file replace the default permissions.

GigaSpaces includes default security permissions, based on the above settings. These are located in the `gs-runtime.jar` file, under `\policy\gigaspaces.policy`. If you do not need special security settings, you do not need to set up the `java.security.policy` property when accessing the space. The default setting is used. The same occurs when using the `SpaceFinder` to start a space (not using the `ServiceStarter`).

**Flat File Structure** -- the `policy.all` file can be moved under the `<XAP Root>` directory, if you want to maintain a flat file structure -- where configuration, jar, and security files can be organized under the `<XAP Root>` folder, or under their main folder without having sub-folders.

{{%refer%}}
java-version
For more details on Java security, refer to: [Sun;Default Policy Implementation and Policy File Syntax](http://docs.oracle.com/javase/{{%version "java-version"%}}/docs/technotes/guides/security/PolicyFiles.html).
{{%/refer%}}


# Security permissions required for XAP

If you want to create your own security policy file, you need to add, at least, the following security grants.


```console
grant {
  permission java.util.PropertyPermission "*", "read, write";
  permission java.lang.RuntimePermission "getProtectionDomain";
  permission java.io.FilePermission "<<ALL FILES>>", "read, write, delete, execute";
  permission java.io.SerializablePermission "enableSubstitution";
  permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
  permission java.lang.RuntimePermission "accessClassInPackage.org.apache.*";
  permission java.lang.RuntimePermission "accessClassInPackage.sun.*";
  permission java.lang.RuntimePermission "accessDeclaredMembers";
  permission java.lang.RuntimePermission "createClassLoader";
  permission java.lang.RuntimePermission "getClassLoader";
  permission java.lang.RuntimePermission "loadLibrary.libtcnative-1";
  permission java.lang.RuntimePermission "loadLibrary.management";
  permission java.lang.RuntimePermission "loadLibrary.net";
  permission java.lang.RuntimePermission "loadLibrary.tcnative-1";
  permission java.lang.RuntimePermission "modifyThread";
  permission java.lang.RuntimePermission "modifyThreadGroup";
  permission java.lang.RuntimePermission "org.jboss.security.SecurityAssociation.setServer";
  permission java.lang.RuntimePermission "setContextClassLoader";
  permission java.lang.RuntimePermission "createSecurityManager";
  permission java.lang.RuntimePermission "setFactory";
  permission java.lang.RuntimePermission "setIO";
  permission java.lang.RuntimePermission "shutdownHooks";
  permission java.net.NetPermission "specifyStreamHandler";
  permission java.net.SocketPermission "*", "listen, resolve, connect, accept";
  permission java.security.SecurityPermission "createAccessControlContext";
  permission java.security.SecurityPermission "getDomainCombiner";
  permission java.security.SecurityPermission "getPolicy";
  permission java.security.SecurityPermission "getProperty.*";
  permission java.security.SecurityPermission "insertProvider.SUN";
  permission java.security.SecurityPermission "putProviderProperty.SUN";
  permission java.security.SecurityPermission "setPolicy";
  permission java.security.SecurityPermission "setProperty.package.access";
  permission java.security.SecurityPermission "setProperty.package.definition";
  permission net.jini.security.GrantPermission "java.security.AllPermission";
  permission com.sun.jini.discovery.internal.EndpointInternalsPermission "set";
  permission com.sun.jini.discovery.internal.EndpointInternalsPermission "get";
  permission java.util.logging.LoggingPermission "control";
  permission net.jini.discovery.DiscoveryPermission "*";
  permission javax.security.auth.AuthPermission "refreshLoginConfiguration";
  permission javax.security.auth.AuthPermission "setLoginConfiguration";
  permission javax.management.MBeanServerPermission "createMBeanServer";
  permission javax.management.MBeanServerPermission "findMBeanServer";
  permission javax.management.MBeanServerPermission "newMBeanServer";
  permission javax.management.MBeanTrustPermission "register";
  permission javax.management.MBeanPermission "*", "*";
  permission java.lang.RuntimePermission "org.jboss.security.SecurityAssociation.setRunAsRole";
  permission javax.security.auth.AuthPermission "doAsPrivileged";
  permission java.lang.RuntimePermission "org.jboss.security.SecurityAssociation.getPrincipalInfo";
  permission javax.security.auth.AuthPermission "createLoginContext.HsqlDbRealm";
  permission javax.security.auth.AuthPermission "getLoginConfiguration";
  permission java.lang.RuntimePermission "org.jboss.security.SecurityAssociation.accessContextInfo";
  permission javax.security.auth.PrivateCredentialPermission "javax.resource.spi.security.PasswordCredential * \"*\"", "read";
  permission javax.security.auth.AuthPermission "*";
  permission java.lang.RuntimePermission "org.jboss.security.SecurityAssociation.setPrincipalInfo";
  permission net.jini.security.GrantPermission "java.security.AllPermission \"<all permissions>\", \"<all actions>\"";
  permission net.jini.export.ExportPermission "exportRemoteInterface.com.sun.jini.reggie.Registrar";
  permission com.sun.jini.thread.ThreadPoolPermission "getSystemThreadPool";
  permission javax.management.MBeanServerPermission "releaseMBeanServer";
  permission java.lang.RuntimePermission "exitVM";
};
```
