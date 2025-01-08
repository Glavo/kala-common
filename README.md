# Kala Common

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/Glavo/kala-common/blob/main/LICENSE)
[![Gradle Check](https://github.com/Glavo/kala-common/actions/workflows/check.yml/badge.svg)](https://github.com/Glavo/kala-common/actions/workflows/check.yml)
[![codecov](https://codecov.io/github/Glavo/kala-common/graph/badge.svg?token=IHM4ZK7K7A)](https://codecov.io/github/Glavo/kala-common)
![Code Lines](https://tokei.rs/b1/github/Glavo/kala-common?category=code)
[![](https://img.shields.io/maven-central/v/org.glavo.kala/kala-common?label=Maven%20Central)](https://search.maven.org/artifact/org.glavo.kala/kala-common)

This is a powerful set of Java base libraries that provide a series of easy-to-use abstractions and collections.
If you have used Scala, it should be easy for you to get started with this collection library.

> [!NOTE]
> This library is not production ready.
> When [JEP 218 (Generics over Primitive Types)](https://openjdk.org/jeps/218) and [JEP 401 (Value Classes and Objects)](https://openjdk.org/jeps/401) were delivered, 
> we needed to redesign many APIs. (See [#76](https://github.com/Glavo/kala-common/issues/76) for more details)
> 
> Until Kala Common is production-ready, we do not recommend that you use this library for production.

Its documentation is not ready, please contact me if you have any questions.

This project is hosted on GitHub: [Glavo/kala-common](https://github.com/Glavo/kala-common). 

## Add Kala Common to your project

Maven:

```xml
<dependency>
  <groupId>org.glavo.kala</groupId>
  <artifactId>kala-common</artifactId>
  <version>0.79.0</version>
</dependency>
```

Gradle:
```kotlin
implementation("org.glavo.kala:kala-common:0.79.0")
```

## Learn about Kala Common

* [Our Tutorial](docs/Tutorial.md)

## Note

The last version compatible with Java 8 was 0.70.0. Building and running kala common now require Java 21.

## Donate

If you like this library, donating to me is my greatest support!

Due to payment method restrictions, donations are currently only supported through payment channels in Chinese mainland (微信，支付宝，爱发电等).

Here are the ways to donate: [捐赠支持 Glavo](https://donate.glavo.site/)

## License

All Java code is released under the [Apache 2.0](./LICENSE) license.

This README file is released under the [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/) license.

## Especially thanks

<img alt="PLCT Logo" src="./PLCT.svg" width="200" height="200">

Thanks to [PLCT Lab](https://plctlab.org) for supporting me.

![IntelliJ IDEA logo](https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg)


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.
