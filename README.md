# kala-common

![Gradle Check](https://github.com/Glavo/kala-common/workflows/Gradle%20Check/badge.svg?branch=main)
[![](https://jitpack.io/v/Glavo/kala-common.svg)](https://jitpack.io/#Glavo/kala-common)

This is a powerful set of Java base libraries that provide a series of easy-to-use abstractions and collections.
If you have used Scala, it should be easy for you to get started with this collection library.

It's not production ready and the API may change significantly in the future,
but we already use it in Aya language:
[aya-prover/aya-dev](https://github.com/aya-prover/aya-dev)

Its documentation is not ready, please contact me if you have any questions.

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
  <groupId>org.glavo</groupId>
  <artifactId>kala-common</artifactId>
  <version>kala_version</version>
</dependency>
```

Gradle:
```groovy
implementation group: 'org.glavo', name: 'kala-common', version: kala_version
```

## Donate

If you like this library, donating to me is my greatest support!

Due to payment method restrictions, donations are currently only supported through payment channels in Chinese mainland (微信，支付宝，爱发电等).

Here are the ways to donate: [捐赠支持 Glavo](https://donate.glavo.site/)

## Especially thanks

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg" alt="IntelliJ IDEA logo.">


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.