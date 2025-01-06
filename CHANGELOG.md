# Changelog

## 0.79.0 (In development)

### Added

Methods:

* `kala.text.StringSlice::split(String)`
* `kala.text.StringSlice::split(String, int)`
* `kala.collection.base.Traversable::forEachCross(Iterable<U>, BiConsumer<T, U>)`
* `kala.collection.base.Traversable::forEachCrossChecked(Iterable<U>, CheckedBiConsumer<T, U, Ex>)`
* `kala.collection.base.Traversable::forEachCrossUnchecked(Iterable<U>, CheckedBiConsumer<T, U, ?>)`
* `kala.collection.mutable.MutableSeq.create(int)`
* `kala.collection.mutable.MutableSeq::setAll(E...)`

### Removed

Classes/Interfaces:

* `kala.annotations.UnstableName`
* `kala.function.Converter`
