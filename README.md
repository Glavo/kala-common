# kala-common

[![Gradle Check](https://github.com/Glavo/kala-common/actions/workflows/check.yml/badge.svg)](https://github.com/Glavo/kala-common/actions/workflows/check.yml)
[![codecov](https://codecov.io/github/Glavo/kala-common/graph/badge.svg?token=IHM4ZK7K7A)](https://codecov.io/github/Glavo/kala-common)
![Code Lines](https://tokei.rs/b1/github/Glavo/kala-common?category=code)

This is a powerful set of Java base libraries that provide a series of easy-to-use abstractions and collections.
If you have used Scala, it should be easy for you to get started with this collection library.

It's not production ready and the API may change significantly in the future,
but we already use it in Aya language:
[aya-prover/aya-dev](https://github.com/aya-prover/aya-dev)

Its documentation is not ready, please contact me if you have any questions.

This project is hosted on GitHub: [Glavo/kala-common](https://github.com/Glavo/kala-common). 

## Adding Kala to your build

Please replace `$kala_version` with the current kala version.

Maven:
```xml
<dependency>
  <groupId>org.glavo.kala</groupId>
  <artifactId>kala-common</artifactId>
  <version>$kala_version</version>
</dependency>
```

Gradle:
```kotlin
implementation("org.glavo.kala:kala-common:$kala_version")
```

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
