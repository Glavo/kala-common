package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public class MutableHashSetTest implements MutableSetTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, MutableHashSet<E>> factory() {
        return MutableHashSet.factory();
    }

    @Override
    public <E> MutableHashSet<E> of(E... elements) {
        return MutableHashSet.of(elements);
    }

    @Override
    public <E> MutableHashSet<E> from(E[] elements) {
        return MutableHashSet.from(elements);
    }

    @Override
    public <E> MutableHashSet<E> from(Iterable<? extends E> elements) {
        return MutableHashSet.from(elements);
    }
}
