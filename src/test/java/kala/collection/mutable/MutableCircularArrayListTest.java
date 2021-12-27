package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableCircularArrayListTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableCircularArrayList<E>> factory() {
        return MutableCircularArrayList.factory();
    }

    @Override
    public <E> MutableCircularArrayList<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableCircularArrayList<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableCircularArrayList<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

}
