# 字符串插值器

`StringFormat` 是一个灵活且高性能的字符串格式化器。

## 用法

```java
StringFormat.format("{0} is {1} years old", "Glavo", 5); // -> "Glavo is 5 years old"
```

`StringFormat` 在字符串模板中使用 `{序号}` 作为占位符，将其替换为指定序号的参数。

用户可以省略占位符中的序号。`StringFormat` 拥有一个初始值为零的计数器，当遇到省略了序号的占位符时，将使用该计数器的值作为实际参数序号并递增计数器:

```java
StringFormat.format("{} is {} years old", "Glavo", 5);
```

由于 `StringFormat` 使用大括号作为占位符，因此无法直接在字符串模板中包含 `{` 字符，用户需要使用 `{'` 和 `'}` 将包含 `{` 的部分括起，之间的内容都会被原样保留。

```java
StringFormat.format("Hello{' {} '}!", "Glavo"); // -> "Hello {}!"
```

如果有更复杂的格式需求，用户可以在占位符中指定格式：

```java
StringFormat.format("Hello {:upper}!", "Glavo") // -> "Hello GLAVO!"

StringFormat.format("{:printf:%08X}", 114514)   // -> "0001BF52"
```

占位符内分为三部分：序号、格式化器、格式化器参数。三部分之间使用 `:` 分隔。用户可以在 `StringFormatFactory` 中注册自己的格式化器。

默认的 `StringFormatFactory` 预置了以下格式化器（所有预置格式化器对于 `null` 不作处理，会原样插入小写形式的 `null`）：

| 名称              | 功能                                                 | 参数              |
|-----------------|----------------------------------------------------|-----------------|
| `array`         | 使用 `Arrays.toString` 和 `Arrays.deepToString` 格式化数组 |                 |
| `printf`        | 使用 `String.format` 风格的格式说明符对参数进行格式化                | 格式说明符           |
| `lower`/`upper` | 对参数的字符串表示大小写转换                                     |                 |
| `substring`     | 截取参数的字符串表示的一部分                                     | \[起始索引]\[,结束索引] |
| `trim`          | 去除参数的字符串两端的空白                                      |                 |

## 性能

相较于 Java 标准库中的 `String.format` 以及 `MessageFormat`， `StringFormat` 有着更出色的效率。

[StringFormatBenchmark](benchmark/src/main/java/kala/benchmark/StringFormatBenchmark.java) 测量了几种方式进行字符串插值的效率。
该测试中 `StringFormat` 表现出色，性能约为 `String.format` 的 2\~2.3 倍，`MessageFormat` 的 2.7\~5 倍。
