---
type: post
title:  SSL and Self Signed Certificates
categories: SBP
parent: production.html
weight: 2300
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
| Dixson Huie| 12.0| December 2016|    |    |



This page describes the steps needed to secure the transport layer using SSL and self signed certificates.


# Scenario

On my local computer, I am running XAP. On a remote computer, I have a client that will connect to the XAP server and writes an object to the space. In order to protect the communication with SSL, we will enable the `SSLFilterFactory` and supply the `keystore`. We will then deploy a space and run a client. This example builds upon the [documentation]({{%latestsecurl%}}/securing-the-transport-layer-using-ssl.html)
 
|    | XAP Server (local computer, running Windows)|
|------|------|
|Name: |my-pc.gspaces.com|
|IP address: |10.10.10.131|

|    | Client (remote linux host)|
|---|------|
|Name: |blob.gspaces.com|
|IP address: |10.10.10.21|

# Private key and certificate

## Generate the private key and certificates on my-pc:

```bash
cd <XAP_root>\bin

# generate the private key
keytool -genkeypair -alias server -keyalg RSA -keypass changeit -storepass changeit -keystore keystore.jks 

# export the certificate
keytool -exportcert -alias server -storepass changeit -file server.cer -keystore keystore.jks 
```

## Generate the private key and certificates on blob:

```bash
cd <XAP_root>/bin

# generate the private key
keytool -genkeypair -alias client -keyalg RSA -keypass changeit -storepass changeit -keystore client_keystore.jks 

# export the certificate
keytool -exportcert -alias client -storepass changeit -file client.cer -keystore client_keystore.jks 
```

## Upload and exchange the certs.
Place the server.cer in ```blob:<XAP_root>/bin```


Place the client.cer in ```my-pc:<XAP_root>/bin```


## Import the cert into the keystore.
On many web sites, the recommendation is to put the certificate in the truststore. But here we import directly into the keystore.


On ```my-pc:<XAP_root>/bin```, run 

```bash
keytool -importcert -v -trustcacerts -alias client -file client.cer -keystore keystore.jks -keypass changeit -storepass changeit 
```

On ```blob:<XAP_root>/bin```, run

```bash
keytool -importcert -v -trustcacerts -alias server -file server.cer -keystore client_keystore.jks -keypass changeit -storepass changeit 
```

# XAP server setup
Below are the contents of the setenv-overrides.bat

```bash
set XAP_LOOKUP_LOCATORS=my-pc.gspaces.com:4174
set XAP_NIC_ADDRESS=10.10.10.131

set EXT_JAVA_OPTIONS=-Dcom.gs.lrmi.filter.factory=com.gigaspaces.lrmi.nio.filters.SSLFilterFactory -Dcom.gs.lrmi.filter.security.keystore=C:/Users/Dixson/work/xap/gigaspaces-xap-premium-12.0.0-ga-b16000/bin/keystore.jks -Dcom.gs.lrmi.filter.security.password=changeit -Djava.net.preferIPv4Stack=true -Djavax.net.debug=ssl
```

Start XAP by running gs-agent

Create a partitioned 2,1 space named SSLSpace. You may use the gs-ui or web-ui to do this. See ({{%latestjavatuturl%}}/java-tutorial-part2.html) for more information.

# Client setup

Compile the example SSLClient.java program provided in the [documentation]({{%latestsecurl%}}/securing-the-transport-layer-using-ssl.html). You may need to change the Groups and Locators values as needed.

Below is the bash script I used to run the client:

```bash
#!/bin/bash

export JAVA_HOME=/opt/jdk/jdk8u66

export XAP_LOOKUP_LOCATORS=my-pc.gspaces.com:4174
export XAP_LOOKUP_GROUPS=dixson_1
export XAP_NIC_ADDRESS=10.10.10.21

CLASSPATH=/home/user/sslclient
CLASSPATH=/opt/gspaces/gigaspaces-xap-premium-12.0.0-ga-b16000/bin:${CLASSPATH}
CLASSPATH=/opt/gspaces/gigaspaces-xap-premium-12.0.0-ga-b16000/lib/required/*:${CLASSPATH}


echo $CLASSPATH
${JAVA_HOME}/bin/java -cp ${CLASSPATH} \
-Dcom.gs.jini_lus.locators=${XAP_LOOKUP_LOCATORS} \
-Dcom.gs.jini_lus.groups=${XAP_LOOKUP_GROUPS} \
-Dcom.gs.lrmi.filter.factory=com.gigaspaces.lrmi.nio.filters.SSLFilterFactory \
-Dcom.gs.lrmi.filter.security.keystore=/opt/gspaces/gigaspaces-xap-premium-12.0.0-ga-b16000/bin/client_keystore.jks \
-Dcom.gs.lrmi.filter.security.password=changeit \
-Djava.net.preferIPv4Stack=true \
-Djavax.net.debug=ssl \
SSLClient
```

# Verification

The SSLSpace will have one object. By setting `-Djavax.net.debug=ssl` you will see the ssl handshake methods. If this is not enabled, you may get an error: **General SSLEngine problem**, but it will be missing details.

After enabling the debug option, you should see the following output in the logs:

```bash
2016-12-15 14:19:35,661  INFO [com.gigaspaces.lrmi.filters] - Created LRMI filter factory: com.gigaspaces.lrmi.nio.filters.SSLFilterFactory
adding as trusted cert:
adding as trusted cert:
 Subject: CN=blob.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Subject: CN=blob.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Issuer:  CN=blob.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Issuer:  CN=blob.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Algorithm: RSA; Serial number: 0x6aebeb13
 Algorithm: RSA; Serial number: 0x6aebeb13
 Valid from Thu Dec 15 10:35:01 EST 2016 until Wed Mar 15 11:35:01 EDT 2017
 Valid from Thu Dec 15 10:35:01 EST 2016 until Wed Mar 15 11:35:01 EDT 2017

adding as trusted cert:

adding as trusted cert:
 Subject: CN=my-pc.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Subject: CN=my-pc.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Issuer:  CN=my-pc.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Issuer:  CN=my-pc.gspaces.com, OU=Unknown, O=Gigaspaces, L=New York, ST=New York, C=US
 Algorithm: RSA; Serial number: 0x2f54a1d6
 Algorithm: RSA; Serial number: 0x2f54a1d6
 Valid from Wed Dec 14 16:57:53 EST 2016 until Tue Mar 14 17:57:53 EDT 2017

 Valid from Wed Dec 14 16:57:53 EST 2016 until Tue Mar 14 17:57:53 EDT 2017
```

If the certificate of the other computer is not listed, you will get an error:

```bash
***
main, fatal error: 46: General SSLEngine problem
sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```


