# kala-base

## `kala.control`

### `Try`

`kala.control.Try` 类型表示可能成功或因为异常失败的结果值。同时，它也提供了一系列用于处理异常相关问题的静态工具方法。

## `kala.function`

对 `java.util.function` 包的补充，以一致的命名提供了一些额外的函数式接口：

* 现有接口的原始类型特化（`BooleanConsumer`、`BooleanFunction`、`BooleanPredicate`、`BooleanUnaryOperator` 等）。
* 三参数版本的 `Function`：`TriFunction`。
* 带索引版本的函数式接口，名称为原始版本加上 `Indexed` 前缀。它在原始版本的参数列表之前额外接受一个 `int` 类型的索引参数。
* 受检异常友好的 `Checked` 函数式接口，名称为原始版本加上 `Checked` 前缀。

### 受检异常友好的函数式接口

`java.util.function` 中的函数式接口都没有 `throws` 声明，所以无法正常地抛出受检异常。

Kala 提供了一套名称以 `Checked` 开头的函数式接口，函数签名与原始版本一致，但是接口接受一个额外的泛型参数，为函数式接口的方法提供 `throws` 声明，
这将允许用户在 lambda 表达式或方法引用中抛出受检异常：

```java
@FunctionalInterface
public interface CheckedFunction<T, R, Ex extends Throwable> extends Function<T, R> {

    static <T, R, Ex extends Throwable> CheckedFunction<T, R, Ex> of(CheckedFunction<? super T, ? extends R, ? extends Ex> function) {
        return (CheckedFunction<T, R, Ex>) function;
    }

    R applyChecked(T t) throws Ex;

    @Override
    default R apply(T t) {
        try {
            return applyChecked(t);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }

    default Try<R> tryApply(T t) {
        try {
            return Try.success(applyChecked(t));
        } catch (Throwable ex) {
            return Try.failure(ex);
        }
    }
}
```

值得注意的是，`Checked` 系列的函数式接口继承自原始版本的接口，并在原始的方法中用 `Try.sneakyThrow` 通过不安全的非受检方式抛出异常。
所以您可以这样使用它：

```java
// Cannot be compiled
// var dirs = Files.list(dir).flatMap(Files::list).toList()
        
//ok
var dirs = Files.list(dir).flatMap(CheckedFunction.of(Files::list)).toList()
```