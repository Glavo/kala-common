package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableCollectionTest implements MutableCollectionTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends MutableCollection<? extends E>> factory() {
        return MutableCollection.factory();
    }

    @Override
    public <E> MutableCollection<E> of(E... elements) {
        return MutableCollection.from(elements);
    }

    @Override
    public <E> MutableCollection<E> from(E[] elements) {
        return MutableCollection.from(elements);
    }

    @Override
    public <E> MutableCollection<E> from(Iterable<? extends E> elements) {
        return MutableCollection.from(elements);
    }

    @Override
    public void classNameTest() {
    }
}
