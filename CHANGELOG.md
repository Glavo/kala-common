# Changelog

## 0.84.0

(In development)

### New features

* Add `kala.collection.SeqView::zip(SeqLike<U>)`.
* Add `kala.collection.SeqView::zip(SeqLike<U>, BiFunction<E, U, R>)`.
* Add `kala.collection.SeqView::zip3(SeqLike<U>, SeqLike<V>)`.

## 0.83.0

Release Date: 2025-08-19

### Fixes

* Add `serialVersionUID` to `kala.collection.immutable.ImmutableVector`.

### Breaking Changes

The following methods have been removed:

* `kala.control.Option::span(Predicate<T>)`
* `kala.collection.CollectionLike::toImmutableSeq()`
* `kala.collection.CollectionLike::toImmutableSet()`
* `kala.collection.CollectionLike::toImmutableMap()`
* `kala.collection.SeqView::span(Predicate<E>)`
* `kala.collection.immutable.ImmutableArray::span(Predicate<E>)`

## 0.82.0

Release Date: 2025-02-13

### New features

* Add a new class: `kala.collection.factory.MapBuilder`
* Add a new collection: `kala.collection.mutable.MutableLinkedHashSet`.
* Add `kala.collection.factory.MapFactory::newMapBuilder()`
* Add `kala.collection.factory.MapFactory::newMapBuilder(int)`
* Add `kala.collection.factory.MapFactory::newBuilder(int)`
* Add `kala.collection.CollectionLike::collect()`
* Add `kala.collection.CollectionLike::toArraySeq()`
* Add new factory method for all `Map`s: `newMapBuilder()`.

### Breaking Changes

The following methods have been removed:

* `kala.collection.CollectionLike::toImmutableLinkedSeq()`
* `kala.collection.CollectionLike::toImmutableVector()`

Other changes:

* The parameter type of `kala.collection.immutable.ImmutableArraySeq.Unsafe.wrap(Object[])` is changed to `E[]`.
* The return type of `kala.collection.CollectionLike::toSeq()` changes to `ImmutableSeq<E>`.
* The return type of `kala.collection.CollectionLike::toSet()` changes to `ImmutableSet<E>`.
* The return type of `kala.collection.CollectionLike::associateBy(Function<E, K>)` changes to `ImmutableMap<K, E>`.
* The return type of `kala.collection.CollectionLike::associateBy(Function<E, K>, Function<K, V>)` changes to `ImmutableMap<K, V>`.
* `kala.collection.CollectionLike::toImmutableSeq()` is deprecated for removal and is replaced by `toSeq()`.
* `kala.collection.CollectionLike::toImmutableSet()` is deprecated for removal and is replaced by `toSet()`.
* `kala.collection.CollectionLike::toImmutableMap()` is deprecated for removal.

## 0.81.0

Release Date: 2025-02-11

### New features

* Add `kala.collection.base.Traversable::onEach(Consumer<T>)`
* Add `kala.collection.base.Traversable::onEachChecked(CheckedConsumer<T, Ex>)`
* Add `kala.collection.base.Traversable::onEachUnchecked(CheckedConsumer<T, ?>)`
* Add `kala.collection.base.OrderedTraversable::onEachIndexed(IndexedConsumer<T>)`
* Add `kala.collection.base.OrderedTraversable::onEachIndexedChecked(CheckedIndexedConsumer<T, Ex>)`
* Add `kala.collection.base.OrderedTraversable::onEachIndexedUnchecked(CheckedIndexedConsumer<T, ?>)`
* Add `kala.collection.mutable.MutableHashMap.factory(Hasher<K>)`.
* Add new factory method for all `Seq`/`Set`: `newBuilder()`.

### Fixes

* Fix `kala.collection.mutable.MutableHashMap::{set, put}` not working with custom hasher.
* Fix `kala.collection.mutable.MutableHashSet::{add, remove}` not working with custom hasher.
* Fix `kala.function.Hashers.Identity::toString()`.

## 0.80.0

Release Date: 2025-01-19

### New features

* Add a new collection: `kala.collection.mutable.MutableSortedMap`.
* Add a new collection: `kala.collection.immutable.ImmutableChampMap`.
* Add a new collection: `kala.collection.immutable.ImutableSortedMap`.
* Add a new collection: `kala.collection.immutable.ImmutableTreeMap`.
* Add a new collection: `kala.collection.immutable.ImmutableTreeSet`.
* Add `kala.control.OptionContainer::getOrThrow()`.
* Add `kala.collection.MapLike::updated(K, V)`.
* Add `kala.collection.MapLike::removed(K)`.
* Add `kala.collection.SeqLike::getOrDefault(int, E)`.
* Add `kala.collection.SeqLike::getOrElse(int, Supplier<E>)`.
* Add `kala.collection.SeqLike::getOrThrow(int, Supplier<Ex>)`.
* Add `kala.collection.Set::added(E)`.
* Add `kala.collection.Set::addedAll(E...)`.
* Add `kala.collection.Set::addedAll(Iterable<E>)`.
* Add `kala.collection.SetLike::removed(E)`.
* Add `kala.collection.SetLike::removedAll(E...)`.
* Add `kala.collection.SetLike::removedAll(Iterable<E>)`.
* Add `kala.collection.SetLike::filterIsInstance(Class<U>)`.
* Add `kala.collection.base.OrderedTraversable::mapIndexed(CollectionFactory<U, ?, R>, IndexedFunction<T, U>)`.
* Add `kala.collection.base.OrderedTraversable::mapIndexedTo(G, IndexedFunction<T, U>)`.
* Add `kala.function.Predicates.isNotEquals(T)`.
* Add `kala.function.Predicates.isNotSame(T)`.

### Breaking Changes

The following classes/interfaces have been removed:

* `kala.comparator.Comparators`
* `kala.collection.immutable.ImmutableArraySet`
* `kala.collection.mutable.AbstractMutableListSet`
* `kala.collection.mutable.MutableArraySet`
* `kala.collection.mutable.MutableLinkedSet`

The following methods have been removed:

* `kala.collection.immutable.ImmutableSortedSet::map(Comparator<U>, Function<E, U>)`
* `kala.collection.immutable.ImmutableSortedSet::flatMap(Comparator<U>, Function<E, Iterable<U>>)`
* `kala.collection.mutable.MutableSet::removeAll(Predicate<E>)`
* `kala.collection.mutable.MutableSet::retainAll(Predicate<E>)`
* `kala.collection.mutable.MutableSet::filterInPlace(Predicate<E>)`
* `kala.collection.mutable.MutableSet::filterNotInPlace(Predicate<E>)`

Other changes:

* `kala.collection.base.GenericArrays.{min, minOrNull, minOption, max, maxOrNull, maxOption}` now accepts `null` as the comparator.
* `kala.collection.factory.CollectionBuilder` no longer inherit from `java.util.Consumer`.
* `kala.collection.Map::putted(K, V)`  is renamed to `updated`.

## 0.79.0

Release Date: 2025-01-07

### New features

* `kala.collection.mutable.MutableEnumSet` now provides factory methods.
* Add `kala.text.StringSlice::split(String)`
* Add `kala.text.StringSlice::split(String, int)`
* Add `kala.collection.base.Traversable::forEachCross(Iterable<U>, BiConsumer<T, U>)`
* Add `kala.collection.base.Traversable::forEachCrossChecked(Iterable<U>, CheckedBiConsumer<T, U, Ex>)`
* Add `kala.collection.base.Traversable::forEachCrossUnchecked(Iterable<U>, CheckedBiConsumer<T, U, ?>)`
* Add `kala.collection.mutable.MutableSeq.create(int)`
* Add `kala.collection.mutable.MutableSeq::setAll(E...)`

### Breaking Changes

The following classes/interfaces have been removed:

* `kala.annotations.UnstableName`
* `kala.function.Converter`
