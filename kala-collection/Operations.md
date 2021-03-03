* Static Factories
  * `narrow(COLL<E>)`
  * `factory()`
  * `create()`
  * `empty()`
  * `of(E...)`
  * `from(E[])`
  * `from(Iterable<E>)`
  * `from(Iterator<E>)`
  * `fill(int, E)`
  * `fill(int, Supplier<E>)`
  * `fill(int, IntFunction<E>)`
* Collection Operations
  * `className()`
  * `iterableFactory()`
  * `iterator()`
  * `spliterator()`
  * `view()`
  * `stream()`
  * `parallelStream()`
  * `edit()`
  * `asJava()`
  * `clone()`
* Size Info
  * `isEmpty()`
  * `size()`
  * `knownSize()`
* Size Compare Operations
  * `sizeCompare(int)`
  * `sizeCompare(Iterable)`
  * `sizeIs(int)`
  * `sizeIs(Iterable)`
  * `sizeEquals(int)`
  * `sizeEquals(Iterable)`
  * `sizeLessThan(int)`
  * `sizeLessThan(Iterable)`
  * `sizeLessThanOrEquals(int)`
  * `sizeLessThanOrEquals(Iterable)`
  * `sizeGreaterThan(int)`
  * `sizeGreaterThan(Iterable)`
  * `sizeGreaterThanOrEquals(int)`
  * `sizeGreaterThanOrEquals(Iterable)`
* Positional Access Operations
  * `isDefinedAt(int)`
  * `get(int)`
  * `getOrNull(int)`
  * `getOption(int)`
  * `set(int, E)`
  * `insert(int, E)`
  * `insertAll(int, E[])`
  * `insertAll(int, Iterable<E>)`
  * `insertAll(int, Iterator<E>)`
  * `removeAt(int)`
  * `removeAt(int, int)`
* Reversal Operations
  * `reversed()`
  * `reverseIterator()`
* Modification Operations
  * `append(E)`
  * `appendAll(E[])`
  * `appendAll(Iterable<E>)`
  * `prepend(E)`
  * `prependAll(E[])`
  * `prependAll(Iterable<E>)`
  * `clear()`
* Addition Operations
  * `added(E)`
  * `appended(E)`
  * `appendedAll(E[])`
  * `appendedAll(Iterable<E>)`
  * `prepended(E)`
  * `prependedAll(E[])`
  * `prependedAll(Iterable<E>)`
* Map Operations
  * `get(K)`
  * `getOrNull(K)`
  * `getOption(K)`
  * `getOrDefault(K, V)`
  * `getOrElseGet(K, Supplier<V>)`
  * `put(K, V)`
  * `set(K, V)`
  * `remove(K)`
  * `removeIfExists(K)`
* Element Retrieval Operations
  * `find(Predicate<E>)`
  * `first()`
  * `first(Predicate<E>)`
  * `firstOrNull()`
  * `firstOrNull(Predicate<E>)`
  * `firstOption()`
  * `firstOption(Predicate<E>)`
  * `last()`
  * `last(Predicate<E>)`
  * `lastOrNull()`
  * `lastOrNull(Predicate<E>)`
  * `lastOption()`
  * `lastOption(Predicate<E>)`
* Element Conditions
  * `contains(Object)`
  * `containsAll(E[])`
  * `containsAll(Iterable<?>)`
  * `sameElements(Iterable<?>)`
  * `sameElements(Iterable<?>, boolean)`
  * `anyMatch(Predicate<E>)`
  * `allMatch(Predicate<E>)`
  * `noneMatch(Predicate<E>)`
* Search Operations
  * `indexOf(Object)`
  * `indexOf(Object, int)`
  * `indexWhere(Predicate<E>)`
  * `indexWhere(Predicate<E>, int)`
  * `lastIndexOf(Object)`
  * `lastIndexOf(Object, int)`
  * `lastIndexWhere(Predicate<E>)`
  * `lastIndexWhere(Predicate<E>, int)`
* Misc Operations
  * `slice(int, int)`
  * `drop(int)`
  * `dropWhile(Predicate<E>)`
  * `take(int)`
  * `takeWhile(Predicate<E>)`
  * `updated(int, E)`
  * `filter(Predicate<E>)`
  * `filterNot(Predicate<E>)`
  * `filterNotNull()`
  * `map(Function<E, U>)`
  * `mapIndexed(IndexedFunction<E, U>)`
  * `flatMap(Function<E, Iterable<U>>)`
  * `zip(Iterable<U>)`
  * `span(Predicate<E>)`
  * `chunked(int)`
  * `windowed(int)`
  * `windowed(int, int)`
  * `windowed(int, int, boolean)`
* Aggregate Operations
  * `count(Predicate<E>)`
  * `max()`
  * `max(Comparator<E>)`
  * `maxOrNull()`
  * `maxOrNull(Comparator<E>)`
  * `maxOption()`
  * `maxOption(Comparator<E>)`
  * `min()`
  * `min(Comparator<E>)`
  * `minOrNull()`
  * `minOrNull(Comparator<E>)`
  * `minOption()`
  * `minOption(Comparator<E>)`
  * `fold(E, BiFunction<E, E, E>)`
  * `foldLeft(U, BiFunction<U, E, U>)`
  * `foldRight(U, BiFunction<E, U, U>)`
  * `foldIndexed(E, IndexedBiFunction<E, E, E>)`
  * `foldLeftIndexed(U, IndexedBiFunction<U, E, U>)`
  * `foldRightIndexed(U, IndexedBiFunction<E, U, U>)`
  * `reduce(BiFunction<E, E>)`
  * `reduceOrNull(BiFunction<E, E>)`
  * `reduceOption(BiFunction<E, E>)`
  * `reduceLeft(BiFunction<E, E>)`
  * `reduceLeftOrNull(BiFunction<E, E>)`
  * `reduceLeftOption(BiFunction<E, E>)`
  * `reduceRight(BiFunction<E, E>)`
  * `reduceRightOrNull(BiFunction<E, E>)`
  * `reduceRightOption(BiFunction<E, E>)`
* Copy Operations
  * `copyToArray(Object[])`
  * `copyToArray(Object[], int)`
  * `copyToArray(Object[], int, int)`
* Conversion Operations
  * `collect(Collector)`
  * `collect(CollectionFactory)`
  * `toArray()`
  * `toArray(IntFunction)`
  * `toArray(U[])`
  * `toArray(Class<U>)`
  * `toSeq()`
  * `toImmutableSeq()`
  * `toImmutableArray()`
  * `toImmutableList()`
  * `toImmutableVector()`
  * `toMutableSeq()`
  * `toMutableArray()`
  * `toBuffer()`
  * `toArrayBuffer()`
  * `toLinkedBuffer()`
  * `toDoubleLinkedBuffer()`
* Traverse Operations
  * `forEach(Consumer<E>)`
  * `forEachPrimitive(T_CONSUMER)`
  * `forEachChecked(CheckedConsumer<E>)`
  * `forEachUnchecked(CheckedConsumer<E>)`
  * `forEachIndexed(IndexedConsumer<E>)`
  * `forEachIndexedChecked(CheckedIndexedConsumer<E>)`
  * `forEachIndexedUnchecked(CheckedIndexedConsumer<E>)`
* Comparison and Hashing
  * `canEqual(Object)`
  * `equals(Object)`
  * `hashCode()`
* String Representation
  * `joinTo(A)`
  * `joinTo(A, CharSequence)`
  * `joinTo(A, CharSequence, CharSequence, CharSequence)`
  * `joinToString()`
  * `joinToString(CharSequence)`
  * `joinToString(CharSequence, CharSequence, CharSequence)`
* Serialization Operations
  * `readResolve()`
  * `writeReplace()`
  * `readObject(ObjectInputStream)`
  * `writeObject(ObjectOutputStream)`