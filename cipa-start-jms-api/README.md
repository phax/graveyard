cipa-start-jms-api
==========================

*This project is deprecated and will not be developed any further since the support for the START protocol is being phased out by OpenPEPPOL!*

The API project for the [CIPA START JMS Receiver project](https://github.com/phax/cipa-start-jmsreceiver)
and the [CIPA START JMS Sender project](https://github.com/phax/cipa-start-jmssender)
for usage on both sides of the JMS queue.
It defines the exchanged data structures and can be used for both sending via PEPPOL (using the
START client) as well as receiving via PEPPOL (using the START server).

Latest release: *1.5.0*

Download location: [MavenCentral](http://repo2.maven.org/maven2/com/helger/cipa-start-jms-api/1.5.0/). Since version 1.4.0 on Maven Central.

Usage via Maven:
 ```
<project...>
  ...
  <dependencies>
    ...
    <dependency>
      <groupId>com.helger</groupId>
      <artifactId>cipa-start-jms-api</artifactId>
      <version>1.5.0</version>
    </dependency>
    ...
  </dependencies>
  ...
</project>  
 ```

## Document types

This project contains the following document types:

* PeppolReceiverResponse
  * file: /schemas/peppol-receiver-response.xsd
  * purpose: contains the response of a START AP server when handling an incoming document
* PeppolSenderResponse
 * file: /schemas/peppol-sender-response.xsd
 * purpose: contains the response of a START AP client when sending a document
* WrappedPeppol
 * file: /schemas/wrapped-peppol.xsd
 * purpose: contains an arbitrary PEPPOL document + the default document headers (message ID, 
     channel ID, sender ID, recipient ID, document type ID and process ID) and a list of
     arbitrary key-value-pairs for exchanging anything else. 

## Dependencies

This project is independent of CIPA or JMS and is easily be usable on the other side of the JMS queue.
It depends on:
* ph-commons-5.2.0.jar (generic utility library - http://repo2.maven.org/maven2/com/helger/ph-commons/5.2.0/)
* slf4j-api-1.7.7.jar (Logging facade library - http://repo2.maven.org/maven2/org/slf4j/slf4j-api/1.7.7/)
* annotations-2.0.3.jar (FindBugs annotations - http://repo2.maven.org/maven2/com/google/code/findbugs/annotations/2.0.3/)
* validation-api-1.1.0.Final.jar (Java Bean Validation API - http://repo2.maven.org/maven2/javax/validation/validation-api/1.1.0.Final/)

## License

Licensed under Apache 2.0 license.

---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
