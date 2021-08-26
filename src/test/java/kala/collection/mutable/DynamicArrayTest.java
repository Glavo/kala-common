package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class DynamicArrayTest implements DynamicSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, DynamicArray<E>> factory() {
        return DynamicArray.factory();
    }

    @Override
    public <E> DynamicArray<E> of(E... elements) {
        return DynamicArray.from(elements);
    }

    @Override
    public <E> DynamicArray<E> from(E[] elements) {
        return DynamicArray.from(elements);
    }

    @Override
    public <E> DynamicArray<E> from(Iterable<? extends E> elements) {
        return DynamicArray.from(elements);
    }

}
