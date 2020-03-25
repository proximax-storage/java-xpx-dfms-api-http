<p align="center"><a href="https://github.com/proximax-storage" target="_blank"><img width="150" src="https://github.com/proximax-storage/java-xpx-dfms-api-http/raw/master/docs/images/logo.jpg" alt="ProximaX DFMS Storage Java SDK"></a></p>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Coverage Status](https://coveralls.io/repos/github/proximax-storage/java-xpx-dfms-api-http/badge.png?branch=master)](https://coveralls.io/github/proximax-storage/java-xpx-dfms-api-http?branch=master)
[![Build Status](https://travis-ci.com/proximax-storage/java-xpx-dfms-api-http.svg?branch=master)](https://travis-ci.com/proximax-storage/java-xpx-dfms-api-http)

# ProximaX DFMS Storage Java SDK #

Official ProximaX DFMS Storage Java SDK Library in Java.

The ProximaX DFMS Storage Java SDK is a Java library for interacting with the DFMS storage. It provides a complete library set coverage, and supports synchronous and asynchronous requests. This SDK can be used with JVM-based languages like Java, Kotlin, Scala.

## Use the library ##

See [wiki](https://github.com/proximax-storage/java-xpx-dfms-api-http/wiki) for latest version of the project.

This library requires use of Java8. Releases of library are published to [Maven Central](https://search.maven.org/search?q=a:java-xpx-dfms-api-http) and [snapshots](https://oss.sonatype.org/content/repositories/snapshots/io/proximax/java-xpx-dfms-api-http/) are available in [Sonatype snapshot repository](https://oss.sonatype.org/content/repositories/snapshots/). To include library and its dependencies, add following to your build script:

### Maven ###

```xml
<dependency>
    <groupId>io.proximax</groupId>
    <artifactId>java-xpx-dfms-api-http</artifactId>
    <version>${javaXpxDfmsVersion}</version>
</dependency>
```

### Gradle ###

```gradle
compile "io.proximax:java-xpx-dfms-api-http:${javaXpxDfmsVersion}"
```

## Documentation ##

For SDK documentation and examples see [wiki](https://github.com/proximax-storage/java-xpx-dfms-api-http/wiki) and [JavaDoc](https://proximax-storage.github.io/java-xpx-dfms-api-http/javadoc/).

Check [integration tests](https://github.com/proximax-storage/java-xpx-dfms-api-http/tree/master/src/e2e/java/io/proximax/sdk) for working examples how to use the library.

## Contribution ##

We'd love to get more people involved in the project. Please feel free to [raise issue](https://github.com/proximax-storage/java-xpx-dfms-api-http/issues/new) or PR and we'll review your contribution.
    
## License ##

Project is licensed under Apache License Version 2.0. See [LICENSE](https://github.com/proximax-storage/java-xpx-dfms-api-http/blob/master/LICENSE) file for details.

<p align="right"><i><sub>Copyright (c) 2019 ProximaX Limited</sub></i></p>