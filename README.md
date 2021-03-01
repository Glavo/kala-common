# kala-common

![Gradle Check](https://github.com/Glavo/kala-common/workflows/Gradle%20Check/badge.svg?branch=main)
[![](https://jitpack.io/v/Glavo/kala-common.svg)](https://jitpack.io/#Glavo/kala-common)

## Adding Kala to your build

First, you need to add the jitpack repository to your build:

Maven:
```xml
<repositories>
  <repository>
    <id>jitpack</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Gradle:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add dependencies (replace `kala_version` with the current kala version):

Maven:
```xml
<dependency>
  <groupId>org.glavo.kala</groupId>
  <artifactId>kala-common</artifactId>
  <version>kala_version</version>
</dependency>
```

Gradle:
```groovy
implementation group: 'org.glavo.kala', name: 'kala-common', version: kala_version
```
