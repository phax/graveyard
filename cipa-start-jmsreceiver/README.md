cipa-start-jmsreceiver
======================

*This project is deprecated and will not be developed any further since the support for the START protocol is being phased out by OpenPEPPOL!*

This project is a [CIPA e-Delivery](https://joinup.ec.europa.eu/software/cipaedelivery/description) START
receiver using [Apache ActiveMQ](http://activemq.apache.org/) JMS. 
CIPA e-Delivery is the official PEPPOL tool suite, maintained by DIGIT.
This project is currently linked against CIPA 2.1.1 and ActiveMQ 5.9.0.

This project makes no sense if you are not using PEPPOL. To find out more about PEPPOL
and OpenPEPPOL please visit the [official website](http://www.peppol.eu).
This project cannot be run standalone it is a "plugin" for a PEPPOL START AccessPoint
server.

The XML data formats used for exchange are defined in the
[CIPA START JMS API project](https://github.com/phax/cipa-start-jms-api) for
common use.

Latest release: *1.0.2*

Download location: [MavenRepo](http://repo2.maven.org/maven2/com/helger/cipa-start-jmsreceiver/1.0.1/). Since version 1.0.1 on Maven Central.

Usage via Maven:
 ```
<project...>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>com.helger</groupId>
      <artifactId>cipa-start-jmsreceiver</artifactId>
      <version>1.0.2</version>
    </dependency>
    ...
  </dependencies>
  ...
</project>  
 ```

## Setup

It is meant as a replacement for the default cipa-start-filereceiver-X.Y.jar file.
Remove the filereceiver-JAR file and add the following additional binary files to the START server:
* cipa-start-jmsreceiver.jar (this project - no binary release yet)
* cipa-start-jms-api-1.5.0.jar (generic START JMS API project - http://repo2.maven.org/maven2/com/helger/cipa-start-jms-api/1.5.0/)
* ph-jms-1.2.0.jar (generic JMS project - http://repo2.maven.org/maven2/com/helger/ph-jms/1.2.0/)
* jms-api-1.1-rev-1.jar (generic JMS 1.1 API - http://central.maven.org/maven2/javax/jms/jms-api/1.1-rev-1/)
* activemq-client-5.9.0.jar (Apache ActiveMQ client library - http://central.maven.org/maven2/org/apache/activemq/activemq-client/5.9.0/)
* hawtbuf-1.9.jar (required by ActiveMQ - http://central.maven.org/maven2/org/fusesource/hawtbuf/hawtbuf/1.9/)
* geronimo-j2ee-management_1.1_space-1.0.1.jar (required by ActiveMQ client - http://central.maven.org/maven2/org/apache/geronimo/specs/geronimo-j2ee-management_1.1_spec/1.0.1/)
* validation-api-1.1.0.Final.jar (Java Bean Validation API - http://central.maven.org/maven2/javax/validation/validation-api/1.1.0.Final/)

Ensure to start the Apache ActiveMQ server before you start the START server!

## Configuration
  
The configuration of this component is done in the configuration file called 
`config-start-jmsreceiver.properties`. 
It must reside in the root directory of the classpath (e.g. in the `WEB-INF/classes` folder of 
the START server).

This is an example configuration file with all required items:
```
# ActiveMQ connections String
connection-string = tcp://localhost:61616

# 'true' to enable persistent messaging, 'false' to disable it
persistent-messaging = false

# Name of the queue where the WrappedPeppol documents are delivered to
inbox-queue-name = FROM_PEPPOL_INBOX

# Name of the queue where the PeppolReceiverResponse document is awaited
response-queue-name = FROM_PEPPOL_RESPONSE

# The maximum number of milliseconds to wait for the response
response-timeout = 30000
```

## License

Licensed under Apache 2.0 license.

---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
