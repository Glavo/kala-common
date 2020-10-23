## Kala Base

[![Build Status](https://travis-ci.com/Glavo/kala-base.svg?branch=main)](https://travis-ci.com/Glavo/kala-base)
[ ![Download](https://api.bintray.com/packages/glavo/maven/kala-base/images/download.svg) ](https://bintray.com/glavo/maven/kala-base/_latestVersion)


## Adding Kala Base to your build

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
  <artifactId>kala-base</artifactId>
  <version>${kala_base_version}</version>
</dependency>
```

Gradle:
```groovy
implementation group: 'asia.kala', name: 'kala-base', version: kala_base_version
```

## Documents

[中文](docs/README_zh_CN.md)