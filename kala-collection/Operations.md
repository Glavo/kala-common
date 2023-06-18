* Static Factories
    * `narrow(COLL<E>)`
    * `factory()`
    * `create()`
    * `empty()`
    * `of(E...)`
    * `from(E[])`
    * `from(Iterable<E>)`
    * `from(Iterator<E>)`
    * `from(Stream<E>)`
    * `fill(int, E)`
    * `fill(int, Supplier<E>)`
    * `fill(int, IntFunction<E>)`
* Collection Operations
    * `className()`
    * `iterableFactory()`
    * `iterator()`
    * `iterator(int)`
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
    * `elementAt(int)`
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
    * `prepend(E)`
    * `prependAll(E[])`
    * `prependAll(Iterable<E>)`
    * `append(E)`
    * `appendAll(E[])`
    * `appendAll(Iterable<E>)`
    * `clear()`
* Addition Operations
    * `prepended(E)`
    * `prependedAll(E[])`
    * `prependedAll(Iterable<E>)`
    * `added(E)`
    * `appended(E)`
    * `appendedAll(E[])`
    * `appendedAll(Iterable<E>)`
* Map Operations
    * `get(K)`
    * `getOrNull(K)`
    * `getOption(K)`
    * `getOrDefault(K, V)`
    * `getOrElse(K, Supplier<V>)`
    * `getOrPut(K, Supplier<V>)`
    * `getOrThrow(Supplier<Ex>)`
    * `getOrThrowException(Ex)`
    * `set(K, V)`
    * `put(K, V)`
    * `putIfAbsent(K, V)`
    * `putAll(...)`
    * `remove(K)`
    * `removeIfExists(K)`
    * `replace(K, V)`
    * `replaceAll(BiFunction<K, V, V>)`
    * `keysView()`
    * `valuesView()`
    * `withDefault(Function<K, V>)`
* Element Retrieval Operations
    * `find(Predicate<E>)`
    * `findFirst(Predicate<E>)`
    * `findLast(Predicate<E>)`
    * `getFirst()`
    * `getFirstOrNull()`
    * `getFirstOption()`
    * `getLast()`
    * `getLastOrNull()`
    * `getLastOption()`
* Element Conditions
    * `contains(Object)`
    * `containsAll(Object[])`
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
    * `binarySearch(E)`
    * `binarySearch(E, Comparator<E>)`
    * `binarySearch(E, int, int)`
    * `binarySearch(E, Comparator<E>, int, int)`
* Misc Operations
    * `slice(int, int)`
    * `sliceView(int, int)`
    * `drop(int)`
    * `dropLast(int)`
    * `dropWhile(Predicate<E>)`
    * `take(int)`
    * `takeLast(int)`
    * `takeWhile(Predicate<E>)`
    * `updated(int, E)`
    * `concat(SeqLike<E>)`
    * `filter(Predicate<E>)`
    * `filterNot(Predicate<E>)`
    * `filterNotNull()`
    * `map(Function<E, U>)`
    * `mapIndexed(IndexedFunction<E, U>)`
    * `mapNotNull(Function<E, U>)`
    * `mapIndexedNotNull(IndexedFunction<E, U>)`
    * `mapMulti(BiConsumer<E, Consumer<U>>)`
    * `mapIndexedMulti(BiConsumer<E, Consumer<U>>)`
    * `flatMap(Function<E, Iterable<U>>)`
    * `sorted()`
    * `sorted(Comparator<E>)`
    * `shuffled()`
    * `shuffled(Random)`
    * `zip(Iterable<U>)`
    * `zipView(SeqLike<U>)`
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
    * `toArray(Class<U>)`
    * `toArray(IntFunction)`
    * `toArray(U[])`
    * `toSeq()`
    * `toImmutableSeq()`
    * `toImmutableArray()`
    * `toImmutableLinkedSeq()`
    * `toImmutableVector()`
    * `toMutableSeq()`
    * `toMutableArray()`
    * `toMutableList()`
    * `toMutableArrayList()`
    * `toMutableLinkedList()`
    * `toMutableSinglyLinkedList()`
    * `toMap()`
    * `associate(Function)`
    * `associateBy(Function)`
    * `associateBy(Function, Function)`
* Traverse Operations
    * `forEach(Consumer<E>)`
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