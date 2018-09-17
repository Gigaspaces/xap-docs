---
type: post110
title:  Spring Security
categories: XAP110SEC
parent: spring-security-bridge.html
weight: 100
---


{{% ssummary %}}{{% /ssummary %}}


Spring Security, historically also known as `Acegi` Security, provides a comprehensive security solution for handling authentication and authorization. Spring Security namespace provides directives for most common operations, allowing complete security configuration in just a few lines of XML.

Spring Security comes with authentication providers for many occasions. Most common are the `DaoAuthenticationProvider` for retrieving user information from a database; `LdapAuthenticationProvider` for authentication against a Lightweight Directory Access Protocol (LDAP) server; `JaasAuthenticationProvider` for retrieving user information from a JAAS login configuration; etc.

{{%refer%}}
For more information, please refer to the [Spring Security website](http://static.springsource.org/spring-security/site/index.html).
{{%/refer%}}

# Authenticating Users

In Spring Security, the authentication manager assumes the job of establishing a user's identity. An authentication manager is defined by the `org.springframework.security.authentication.AuthenticationManager` interface. The `authenticate` method will attempt to authenticate the user using the `org.springframework.security.core.Authentication` object (which carries the principal and credentials). If successful, the `authenticate` method returns a complete `Authentication` object, including information about the user's granted authorities. If authentication fails, an authentication exception will be thrown.

The `AuthenticationManager` interface is quite simple and you could easily implement your own `AuthenticationManager`. But Spring Security comes with `org.springframework.security.authentication.ProviderManager`, an implementation of `AuthenticationManager` that is suitable for most situations.

# Configuring a provider manager

`ProviderManager` is an authentication manager implementation that delegates responsibility for authentication to one or more authentication providers, as shown in the figure below.

The purpose of `ProviderManager` is to enable you to authenticate users against multiple identity management sources. Rather than relying on itself to perform authentication, `ProviderManager` steps one by one through a collection of authentication providers, until one of them successfully authenticates the user (or until it runs out of providers). This makes it possible for Spring Security to support multiple mechanisms for a single request.

{{%align center%}}
![SpringSecurity-ProviderManager.png](/attachment_files/SpringSecurity-ProviderManager.png)
{{%/align%}}

The following chunk of XML shows a typical configuration of `ProviderManager` in the Spring configuration file:


```java
<bean id="authenticationManager"
     class="org.springframework.security.authentication.ProviderManager">
  <property name="providers">
    <list>
      <ref local="daoAuthenticationProvider"/>
      <ref local="anonymousAuthenticationProvider"/>
      <ref local="ldapAuthenticationProvider"/>
    </list>
  </property>
</bean>
```

`ProviderManager` is given a list of authentication providers through its `providers` property. Typically, you'll only need one authentication provider, but in some cases, it may be useful to supply a list of several providers so that if authentication fails against one provider, another provider will be tried.

Spring comes with numerous authentication providers. We will concentrate on a couple of the most commonly used authentication providers, to give an example how GigaSpaces authorities are to be managed. If you can't find an authentication provider that suits your security needs, you can always create your own by implementing the `org.springframework.security.authentication.AuthenticationProvider` interface.

