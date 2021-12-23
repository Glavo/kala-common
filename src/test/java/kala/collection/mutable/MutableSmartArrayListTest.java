package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public class MutableSmartArrayListTest implements MutableListTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, MutableSmartArrayList<E>> factory() {
        return MutableSmartArrayList.factory();
    }

    @Override
    public <E> MutableSmartArrayList<E> of(E... elements) {
        return MutableSmartArrayList.from(elements);
    }

    @Override
    public <E> MutableSmartArrayList<E> from(E[] elements) {
        return MutableSmartArrayList.from(elements);
    }

    @Override
    public <E> MutableSmartArrayList<E> from(Iterable<? extends E> elements) {
        return MutableSmartArrayList.from(elements);
    }
}
