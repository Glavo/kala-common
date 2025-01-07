# Changelog

## 0.79.0 (In development)

### New APIs

* `kala.collection.mutable.MutableEnumSet` now provides factory methods.

Other new methods:

* `kala.text.StringSlice::split(String)`
* `kala.text.StringSlice::split(String, int)`
* `kala.collection.base.Traversable::forEachCross(Iterable<U>, BiConsumer<T, U>)`
* `kala.collection.base.Traversable::forEachCrossChecked(Iterable<U>, CheckedBiConsumer<T, U, Ex>)`
* `kala.collection.base.Traversable::forEachCrossUnchecked(Iterable<U>, CheckedBiConsumer<T, U, ?>)`
* `kala.collection.mutable.MutableSeq.create(int)`
* `kala.collection.mutable.MutableSeq::setAll(E...)`


### Removed APIs

The following classes/interfaces have been removed:

* `kala.annotations.UnstableName`
* `kala.function.Converter`
