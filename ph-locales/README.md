# ph-locales
Java Locale Extensions. This workspace is spearated in two different projects:

  * ph-locales16 - Locale extensions that are only needed for Java versions upto and incl. 1.6.
  * ph-locales - Locale extensions that can be used with all Java versions

## ph-locales16
This project contains the following locales:

  * Bosnian and SerboCroation local based on https://github.com/KlausBrunner/locale-sh
    * Locales supported: "bs", "bs\_BA", "sh", "sh\_BA", "sh\_HR" and "sh\_RS".
      Note: this is only relevant for Java 1.6+. Starting with Java 1.7 the Locale "sr\_Latn\_RS" etc. are available.

This project does not depend on any other project except the runtime itself because it is a JDK extension.

## ph-locales
This project contains the following locales:
  * Galician based on http://www.javagalician.org/
    * Locales supported: "gl" and "gl\_ES"

This project does not depend on any other project except the runtime itself because it is a JDK extension.

#Building
This project is build with Apache Maven 3.x. Simply call `mvn clean install` and you will end up with a JAR file in the respective `target` directory.

#Installation
Simply adding one or both libraries to your project wont work. You need to put the needed libraries into your `jre/lib/ext` directory. After restarting the JVM, the new locales are available. Therefore no Maven configuration needed for this project.

The created JAR files have no dependencies except the runtime, so no further JAR files are needed. 

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodeingStyleguide.md) |
On Twitter: <a href="https://twitter.com/philiphelger">@philiphelger</a>
