package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableArrayListTest implements MutableListTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, MutableArrayList<E>> factory() {
        return MutableArrayList.factory();
    }

    @Override
    public <E> MutableArrayList<E> of(E... elements) {
        return MutableArrayList.from(elements);
    }

    @Override
    public <E> MutableArrayList<E> from(E[] elements) {
        return MutableArrayList.from(elements);
    }

    @Override
    public <E> MutableArrayList<E> from(Iterable<? extends E> elements) {
        return MutableArrayList.from(elements);
    }

}
