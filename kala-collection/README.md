## Kala Collection

[![Build Status](https://travis-ci.com/Glavo/kala-collection.svg?branch=main)](https://travis-ci.com/Glavo/kala-collection)
[ ![Download](https://api.bintray.com/packages/glavo/maven/kala-collection/images/download.svg) ](https://bintray.com/glavo/maven/kala-collection/_latestVersion)


## Adding Kala Collection to your build

First, you need to add the jcenter repository to your build:

Maven: 
```xml
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com</url>
  </repository>
</repositories>
```

Gradle:
```groovy
repositories {
    jcenter()
}
```

Then add dependencies:

Maven:
```xml
<dependency>
  <groupId>asia.kala</groupId>
  <artifactId>kala-collection</artifactId>
  <version>${kala_collection_version}</version>
</dependency>
```

Gradle:
```groovy
implementation group: 'asia.kala', name: 'kala-collection', version: kala-collection_version
```
