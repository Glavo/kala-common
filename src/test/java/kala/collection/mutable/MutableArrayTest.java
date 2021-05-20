package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableArrayTest implements MutableSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, MutableArray<E>> factory() {
        return MutableArray.factory();
    }

    @Override
    public <E> MutableArray<E> of(E... elements) {
        return MutableArray.from(elements);
    }

    @Override
    public <E> MutableArray<E> from(E[] elements) {
        return MutableArray.from(elements);
    }

    @Override
    public <E> MutableArray<E> from(Iterable<? extends E> elements) {
        return MutableArray.from(elements);
    }

}
