# 字符串插值器

Kala 提供了一个灵活且高性能的字符串格式化器。

## 用法

```java
StringFormat.format("{0} is {1} years old","Glavo","5"); // -> "Glavo is 5 years old"
```

`StringFormat` 在字符串模板中使用 `{序号}` 作为占位符，将其替换为指定序号的参数。

以上代码可以简化成这样：

```java
StringFormat.format("{} is {} years old","Glavo","5");
```

用户可以省略占位符中的序号。`StringFormat` 拥有一个初始值为零的计数器，当遇到省略了序号的占位符时，将使用该计数器的值作为实际参数序号并递增计数器。

由于 `StringFormat` 使用大括号作为占位符，因此无法直接在字符串模板中包含 `{` 字符。
用户可以使用 `{'` 和 `'}` 括起包含 `{` 的部分。

```java
StringFormat.format("Hello{' {} '}!","Glavo"); // -> "Hello {}!"
```

如果有更复杂的格式需求，用户可以在占位符中指定格式：

```java
StringFormat.format("Hello {:upper}!","Glavo") // -> "Hello GLAVO!"

        StringFormat.format("{:printf:%08X}",114514)   // -> "0001BF52"
```

占位符内分为三部分：序号、格式化器、格式化器参数。三部分之间使用 `:` 分隔。用户可以在 `StringFormatFactory` 中注册自己的格式化器。

默认的 `StringFormatFactory` 预置了以下格式化器：

| 名称              | 功能                                                            | 参数    |
|-----------------|---------------------------------------------------------------|-------|
| `printf`        | 使用 `String.format` 风格的格式说明符对参数进行格式化                           | 格式说明符 |
| `lower`/`upper` | 对对象的字符串表示进行大小写转换（对于 `null` 不作处理，即使使用 `upper` 格式化器也会原样插入其小写形式） |       |
| `array`         | 使用 `Arrays.toString` 和 `Arrays.deepToString` 格式化数组            |       |

## 性能

相较于 Java 标准库中的 `String.format` 以及 `MessageFormat`， `StringFormat` 有着更出色的效率。

[StringFormatBenchmark](benchmark/src/main/java/kala/benchmark/StringFormatBenchmark.java) 测量了几种方式进行字符串插值的效率，
该测试中 `StringFormat` 的性能达到了 `String.format` 的两倍以上，最快的情况下是 `MessageFormat` 五倍的性能。 

