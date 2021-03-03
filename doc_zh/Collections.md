## Kala Collection

## 将 Kala Collection 加入构建

首先将 jcenter 仓库添加至仓库配置中：

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

然后将 kala-collection 加入依赖（将`kala_version`替换为当前的 kala 版本）：

Maven:
```xml
<dependency>
  <groupId>org.glavo</groupId>
  <artifactId>kala-collection</artifactId>
  <version>kala_version</version>
</dependency>
```

Gradle:
```groovy
implementation group: 'org.glavo', name: 'kala-collection', version: kala_version
```

