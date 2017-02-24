# junit-dataprovider [![Build Status](https://travis-ci.org/kncept/junit-dataprovider.svg?branch=master)](https://travis-ci.org/kncept/junit-dataprovider)
Dataprovider like functionality for JUnit5.

See [ExampleUseCasesTest](src/test/java/integTest/com/kncept/junit/dataprovider/ExampleUseCasesTest.java) for an example.

This plugin uses the DynamicTest functionlaity of Junit5 to replicate the old JUnit4 @Parameterized or TestNG DataProvider style of tests.

Simply ensure that the distribution jar is on the test classpath, then use it as in the example.
N.B. This release is compiled against the JUnit 5.0.0-M3 Milestone release.

The jar is available on [Maven Central](http://search.maven.org/#artifactdetails%7Ccom.kncept.junit5.dataprovider%7Cjunit-dataprovider%7C0.9-M3%7Cjar)


    testCompile 'com.kncept.junit5.dataprovider:junit-dataprovider:0.9-M3'
    
    <dependency>
        <groupId>com.kncept.junit5.dataprovider</groupId>
        <artifactId>junit-dataprovider</artifactId>
        <version>0.9-M3</version>
    </dependency>

This plugin uses the same license as (most of) JUnit5 - EPL 1.0.

Enjoy.
