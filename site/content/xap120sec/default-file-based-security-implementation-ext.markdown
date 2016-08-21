---
type: post120
title:  Default File-Based Security
categories: XAP120SEC, PRM
parent: security-ext.html
weight: 900
---

{{% ssummary %}} {{% /ssummary %}}



XAP provides a default security implementation. The implementation uses a local file to store the users and roles. This file, needs to be shared or copied between the secured services. The default implementation has configurations for most of its defaults and some handy extension points (e.g. HTTP file-service).

To provide other custom security implementations see the [Custom Security](./custom-security.html) section.
One such custom security implementation is the [Spring Security Bridge](./spring-security-bridge.html).

# Getting Started

The default security file-based directory is first created when you try to manage the security directory (either using the UI or API).

The default file is created under `<XAP root>/security/gs-directory.fsm`.
The `.fsm` is an abbreviation of File Security Manager.

When the file doesn't exist, we create a new file and an `admin/admin` user is added with both **Manage Users** and **Manage Roles** privileges. With this `admin` user you can start to manage the roles and users. The `admin` user has no privileges to perform any other operation. Of course, it can be deleted, and replaced with your own administrator.

The file can be changed while services are up and running. Changes to the file are monitored (see `FileService.lastModified()` API), which triggers a refresh for fetching the updated file contents. The refresh rate is platform dependent. Changes don't affect open sessions, only new established sessions will be aware of the change.

When a secured component is started, it looks for the security configuration properties in order to instantiate the security implementation. The default implementation does not require a properties file to exist, but it is essential if you would want to provide overrides to any of the extensions. The security configuration properties file is implementation specific. Here we describe the properties related to the file-based security manager implementation.

# Configuration

Any configurations that are applied can be seen by setting the logging level to CONFIG (see `gs_logging.properties`):


```java
com.gigaspaces.security.level = CONFIG
```

## Directory file location Configuration

The location of the directory file and its name are configurable using the `com.gs.security.fs.file-service.file-path` property key. The `file-path` is either a direct path to a file, relative path (to `bin`) or a resource in the classpath.


```java
com.gs.security.fs.file-service.file-path = /opt/head/security/my-directory.fsm
```

{{% note %}}
Note that file separators in a properties file are `'/'`.
The file extension doesn't have to be `.fsm`.
If the file doesn't exist, only a warning is displayed
You should specify in the UI (manage security) the path to security configuration file containing this property ,otherwise the default fsm file will be used.
{{%/note%}}

{{%align center%}}
![security_namagement_config_file.jpg](/attachment_files/security_namagement_config_file.jpg)
{{%/align  %}}

# Custom Extensions

There are some handy extension points which allow you to modify some of the defaults we have considered, and replace them with your suitable requirements. These are extensions which are relevant to this specific File-based implementation. It might not be relevant for other security implementations.

## Encodings

The Encoding mechanism is separated into two - password encoding and file-content encoding. Both of these can be changed.

### Password Encoding

The password encoder is used to encode the passwords stored in the file-based directory.
The default password encoding algorithm is MD5. This is a one-way hash function that is used to encrypt the passwords when they are stored in the directory, and encrypt the passwords for authentication validation.

The `PasswordEncoder` interface exposes two methods:


```java
public interface PasswordEncoder {
    String encodePassword(String rawPass) throws EncodingException;
    boolean isPasswordValid(String encPass, String rawPass) throws EncodingException;
}
```

To set your own password encoder:


```java
com.gs.security.fs.password-encoder.class = eg.MyPasswordEncoder
```

### Content Encoding

The content encoder is used to encode/decode the user details, roles and authorities stored in the file-based directory.
The default content encoding algorithm is AES 128-bit. This is a two-way function that requires a private key to encrypt and decrypt.

An AES compliant private key (of type `javax.crypto.SecretKey`) named `gs-private.key` can be placed in the classpath to replace our default private key. Similar to a local keystore in SSL, to prevent clients the ability to connect if they only hold the password.

The `ContentEncoder` interface exposes two methods:


```java
public interface ContentEncoder {
    public byte[] encode(Object obj) throws EncodingException;
    public Object decode(byte[] bytes) throws EncodingException;
}
```

To set your own content encoder:


```java
com.gs.security.fs.content-encoder.class = eg.MyContentEncoder
```

## URL File Service

By default, we load a file from the local file system. We also provide a means to "download" a file from a URL. If you want to further customize, you can implement the `FileService` interface.

To configure the URL file service, you need to switch the default file-service implementation class and provide the parameters parsed by the `URLFileService` implementation - in this case a URL.


```java
com.gs.security.fs.file-service.class = com.gigaspaces.security.fs.URLFileService
com.gs.security.fs.file-service.url = http://www.gigaspaces.com/download/attachments/gs-directory.fsm
```

{{% info %}}
The default file-service implementation class is: `com.gigaspaces.security.fs.LocalFileService`
{{%/info%}}

{{% note%}}
It might be obvious to note that you will need to manage the security directory as a local-file, upload the file to an HTTP server, and only then configure your services with the above properties. If your HTTP server allows write-access, then the `URLFileService` can also be used for managing your directory; the `writeToFile` method (see interface) will use the output stream to write through this connection.
{{%/note%}}

## Custom File Service

The `FileService` interface defines access to the security directory file:


```java
public interface FileService {
    public void init(Properties properties) throws IOException;
    public boolean fileExists();
    public byte[] readFromFile() throws IOException;
    public void writeToFile(byte[] bytes) throws IOException;
    public long lastModified();
}
```

To set your own file service:


```java
com.gs.security.fs.file-service.class = eg.MyFileService
```
