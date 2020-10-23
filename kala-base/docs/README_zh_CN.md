## `asia.kala`

### `Tuple`

提供聚合任意多个值的不可变类型，支持序列化。

`EmptyTuple` 代表不包含元素的空元组，唯一实例为 `EmptyTuple.INSTANCE`。

非空元组继承自 `HList<H, T>` 及 `NonEmtpyTuple`。`Tuple1` ~ `Tupl9` 分别代表具有 1 ~ 9 个元素的元组，
更多元素的元组由 `TupleXXL` 表示，实现上会将所有元素打包至一个数组中。

任何由类型分别为 `T1` ~ `TN` 的 N 个元素组成的元组都可以被表示为 `HList<T1, HList<T2, ... HList<TN, EmptyTuple> ... >>`。

对于 `Tuple1` ~ `Tupl9`，可以使用 `Tuple#_N` 或 `Tuple#componentN()` 来获取第 N 个元素，
而所有元组都能使用 `Tuple#elementAt(int n)` 来类型不安全地获取第 n+1 个元素。

`Tuple` 类提供了一系列静态方法来帮助使用元组：

  * `Tuple.of(...values)` 用于构造元组。
    * `Tuple.empty()` 和 `Tuple.of()` 是 `EmptyTuple.INSTANCE` 的别名。
    * 当参数个数为 N (N >= 1 && N <= 9) 时，此方法为 `TupleN` 构造器的别名。
    * 当参数个数大于 9 时，此方法不是类型安全的，它会将元素打包至 `TupleXXL` 中，而返回结果会被自动强制转换为接受点的类型。 
  * `Tuple.componentN(HList)` (N >=1 && N <= 18) 用于类型安全地获取 `HList` 的前 18 个元素。
  
### `LazyValue`

`LazyValue<T>` 表示可被惰性初始化的一个值，支持序列化。

使用 `LazyValue.of(Supplier<? extends T> supplier)` 构造惰性的 `LazyValue` 值，当第一次获取值时使用 `supplier` 初始化。

使用 `LazyValue.ofValue(T value)` 构造提前被初始化的 `LazyValue` 值。

通过 `LazyValue#get()` 获取该 `LazyValue` 的值，当该 `LazyValue` 未被初始化的时候会使用构造时提供的 `supplier` 初始化并返回。
该方法是线程安全的。

通过 `LazyValue#isReady()` 检测该 `LazyValue` 是否已被初始化，为真时返回 `true`。

```java
var v = LazyValue.of(() -> {
    StdOut.println("do something...");
    "Foo";
});

v.isReady(); // false

var ans = v.get(); // print "do something...", and ans is "Foo"
var ans2 = v.get(); // no output, and ans2 is "Foo"

v.isReady(); // true
```

## `asia.kala.control`

### `Option`

`Option` 是可能存在一个值，或者为空的容器，支持序列化。

与 `java.util.Optional` 不同，`Option` 支持表示存在 `null` 为值的 `Option`，`Option.some(null)` != `Option.none()`。 

使用 `Option.none()` 获取不存在值的空 `Option` 实例，
使用 `Option.some(T value)` 构造值为 `value` 的 `Option` 实例。

`Option.of(T value)` 当 `value` 为 `null` 时返回 `Option.none()` 实例，其他情况下返回 `Option.some(value)`。

使用 `Option#isDefined()` 可以检查是否存在值，当存在值时可以使用 `Option#get()` 获取值。 

`Option` 支持所有 `Traversable` 和 `Transformable` 的操作。