# Changelog

## 0.80.0

(In development)

### New features

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

Other changes:

* `kala.collection.base.GenericArrays.{min, minOrNull, minOption, max, maxOrNull, maxOption}` now accepts `null` as the comparator.

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
