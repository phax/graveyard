cipa-start-jmssender
====================

*This project is deprecated and will not be developed any further since the support for the START protocol is being phased out by OpenPEPPOL!*

This project is a [CIPA e-Delivery](https://joinup.ec.europa.eu/software/cipaedelivery/home)
START sender using [Apache ActiveMQ](http://activemq.apache.org/) as JMS service.
It is a standalone web application (WAR) that takes messages from
a JMS queue, dispatches them to PEPPOL, and puts the response in a structured
XML back into another JMS queue.
It requires **at least CIPA 1.1.2** - it won't work with any previous version, as the
sending interface was modified in 1.1.2!

This project makes no sense if you are not using PEPPOL. To find out more about PEPPOL
and OpenPEPPOL please visit the [official website](http://www.peppol.eu).

The XML data formats used for exchange are defined in the
[CIPA START JMS API project](https://github.com/phax/cipa-start-jms-api) for
common use.

Latest release: *1.0.1*

Download location: [MavenRepo](http://repo2.maven.org/maven2/com/helger/cipa-start-jmssender/1.0.1/). Since version 1.0.1 on Maven Central.

Usage via Maven: *makes no sense as this is a WAR project* (except for an overlay project)
 ```
<project...>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>com.helger</groupId>
      <artifactId>cipa-start-jmssender</artifactId>
      <version>1.0.1</version>
    </dependency>
    ...
  </dependencies>
  ...
</project>  
 ```


## Configuration

All JMS specific configuration of this application is inside `config-start-jms-sender.xml`.
When building from source, it resides in `src/main/resources`. When using the binary
version, the file resides in `WEB-INF/classes`.
The following configuration files items are supported:
* `connectionstring` - the connection string to the JMS server. The default
  value `tcp://localhost:61616` connects to a local ActiveMQ server running
  on the default port (without any clustering).
* `persistent-messaging` - defines whether the exchanged messages should be
  persistent or not. Persistent messaging is more secure but a bit slower. Please
  consult the ActiveMQ-/JMS-documentation for details. Default value is `false`.
* `to-peppol-inbox-queue-name` - defines the name of the JMS queue to which this
  application should listen for incoming messages of type WrappedPeppol-XML (see
  [CIPA START JMS API project](https://github.com/phax/cipa-start-jms-api) for
  details). Default value is `TO_PEPPOL_INBOX`. 
* `to-peppol-response-queue-name` - defines the name of the JMS queue to which
  the sending result of type PeppolSenderResponse-XML should be sent. Default value
  is `TO_PEPPOL_RESPONSE`.
  
Additionally to `config-start-jms-sender.xml` the default CIPA START AP client
configuration files `configSAML.properties` and `configServer.properties`
must be filled with the your PEPPOL/OpenPEPPOL certificate information. By default
the "old" PEPPOL truststore is configured - for my personal convenience only.

## Installation

Download the WAR file and unzip it. Go to the `WEB-INF/classes` directory and modify
the three configuration files as outlined above. Copy the extracted and modified content
of your WAR file into your application server (like Tomcat or Jetty). After deployment
the startup page should show the text **CIPA START JMS Sender**.

*Important*: make sure that the ActiveMQ server is running before this application is
started! 

## Dependencies

This project depends on CIPA components as well as on ActiveMQ components. Please
use either the prebuild WAR file, or build via [Apache Maven](http://maven.apache.org/).

## License

Licensed under Apache 2.0 license.
  
---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
