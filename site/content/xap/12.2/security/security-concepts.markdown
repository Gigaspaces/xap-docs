---
type: post122
title:  Concepts
categories: XAP122SEC, OS
parent: none
weight: 200
---

{{% ssummary %}}{{% /ssummary %}}

This section provides the concepts used throught this security guide, and present the basic interfaces.

# Authentication

Authentication is the act of supplying user credentials (usually username and password) to be confirmed as authentic against a secured service. Authentication fails either due to an unknown user or an invalid password, resulting in an `AuthenticationException`. A successful authentication results in an authenticated session used for further correspondence.

```java
interface SecurityManager {
    /** 
     * Attempts to authenticate the passed user represented by 
     * UserDetails, returning a fully populated UserDetails 
     * object (including granted authorities) if successful.
     */
    Authentication authenticate(UserDetails userDetails) 
      throws AuthenticationException
    ...
}
```

The service (e.g. Space Instance) receives a request for authentication. Usually this is done by supplying a `UserDetails` object which holds the credentials of a user. This is passed along to the `SecurityManager` which is responsible for authenticating the request.

An attempt to authenticate the passed `UserDetails` object, returns a fully populated `Authentication` object (including granted authorities) if successful. On failure, an `AuthenticationException` is thrown.

Further correspondence are done via this authenticated session context. The service has security interceptors which intercept each method call to verify the granted privileges. An `AccessDeniedException` is thrown if the user lacks sufficient privileges to perform an operation.

# Encryption

A two-way encryption is used to protect the credentials passed as part of the authentication process. The `UserDetails` object is encrypted before it is sent along the stream, and decrypted at the service. Of course, it is best to use **SSL** for transport layer security, but nevertheless we ensure these details are encrypted. The two-way encryption is done using a private key available to both client and server. A generated AES compliant key can be kept in a `gs-keystore.key` file located in the classpath. See `ContentEncoder` interface for more details.

```java
interface ContentEncoder {

    /** Decode the byte array returning a decrypted object. */
    Object decode(byte[] bytes) 

    /** Encode the object returning an encrypted byte array. */
    byte[] encode(Object obj) 
}
```

The `UserDetails` password field can also be encrypted separately using a one-way hash function, e.g. an `MD5` algorithm. This one-way encryption allows to store a non-plain text password, and is an implementation detail of the `SecurityManager`. This should not be confused with the two-way encryption done at the transport layer of an authentication call. See `PasswordEncoder` interface for more details.

```java
interface PasswordEncoder {
 
    /* Encodes the specified raw password with an implementation 
     * specific algorithm. */
    String encodePassword(String rawPass) 
      
    /* Validates a specified "raw" password against an 
     * encoded password. */
    boolean isPasswordValid(String encPass, String rawPass)     
}
```

As noted, the `SecurityManager` implementation is responsible for handling an authentication request. The `UserDetails` supplied are used to extract the user by-name, and validate that the supplied password matches the stored password. Usually passwords are stored after they have been hashed with a one-way function (such as `MD5`). When validating, the supplied password is encrypted using this algorithm and compared to the stored password.

