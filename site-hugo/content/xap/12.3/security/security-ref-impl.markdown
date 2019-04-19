---
type: post123
title:  Reference Implementation
categories: XAP123SEC, OSS
parent: none
weight: 600
canonical: auto
---



The reference implementation uses MongoDB as the storage for the UserDetails.
It uses Morphia to abstract the UserDetailsDAO that is stored.

## Security Properties file

Create a `security.properties file` at the default location `<XAP root>/config/security/security.properties`
It should include a SecurityManager implementation class and other properties configurable by the implementation.

```java
com.gs.security.security-manager.class = com.mycompany.MyCustomSecurityManager
com.mycompany.mongodb.host = localhost
com.mycompany.mongodb.port = 27017
com.mycompany.mongodb.databsebName = myDB
```

## Implementing SecurityManager

The implementation utilizes the properties file to configure the connection to MongoDB.
Calls to `authenticate` are triggered once per user login operation. The authentication 
is done against the user details stored in the DB.


```java
public static class MyCustomSecurityManager implements SecurityManager {

    private MongoClient mongo;
    private UserDetailsDAO userDetailsDAO;

    public void init(Properties properties) throws SecurityException {

       // Access a property form the properties file to configure this implementation
       String host = properties.getProperty("com.mycompany.mongodb.host");
       String port = properties.getProperty("com.mycompany.mongodb.port");
       String dbName = properties.getProperty("com.mycompany.mongodb.databsebName");

       // Connect to the db, use org.mongodb.morphia.Morphia for mapping
       MongoClient mongo = new MongoClient(host, Integer.parseInt(port));
       userDetailsDAO = new UserDetailsDAO(morphia, mongoClient, dbName);
       ...
    }

    public Authentication authenticate(UserDetails userDetails) 
        throws AuthenticationException {

        UserDetailsDAO user = userDetailsDAO.findOne( 
            userDetailsDAO.createIdQuery(userDetails.getUsername()));

        if (user == null) {
            throw new AuthenticationException("user not found");
        }

        if (user.getPassword().equals(userDetails.getPassword())) {

            // Populate the returned details with the authorities extracted 
            // from the DB (user.getAuthorities()) and return an Authentication 
            // object with these details.
            UserDetails populatedDetails = new User(user.getUsername(), 
                                                    user.getPassword(), 
                                                    user.getAuthorities());

            return new Authentication(populatedDetails);
        } else {
            throw new AuthenticationException("access denied");
        }
    }

    public DirectoryManager createDirectoryManager(UserDetails userDetails) 
      throws AuthenticationException, AccessDeniedException {
        return null; //ignore or throw DirectoryAccessDeniedException
    }

    public void close() {
        mongo.close();
    }
}
```


## Packaging and Classpath

The most common scenario is for all services to share the **same** custom security. This is easily accomplished by placing the custom implementation classes in the `lib/optional/security` directory.

{{% note %}}
You can use a different directory by configuring the `com.gigaspaces.lib.opt.security` system property.
{{%/note%}}


```java
<XAP root>/lib/optional/security/my-custom-security.jar
```

Processing units may share a custom security implementation. In this case, the custom security jar can be placed under `pu-common`.


```java
<XAP root>/lib/optional/pu-common/my-pu-custom-security.jar
```

If each processing unit has its **own** custom security implementation, the custom security jar can be part of the processing unit distribution.


```java
<XAP root>/deploy/hello-processor/lib/my-processor-custom-security.jar
```

{{% note %}} It is recommended that the custom security jar should only contain security-related classes. {{%/note%}}


