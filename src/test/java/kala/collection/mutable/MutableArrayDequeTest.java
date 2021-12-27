package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableArrayDequeTest implements MutableSeqTestTemplate {

    @Override
    public Class<?> collectionType() {
        return null;
    }

    @Override
    public <E> CollectionFactory<E, ?, MutableArrayDeque<E>> factory() {
        return MutableArrayDeque.factory();
    }

    @Override
    public <E> MutableArrayDeque<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableArrayDeque<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    public <E> MutableArrayDeque<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

}
