package kala.collection.immutable;

import kala.collection.factory.CollectionFactory;

public class ImmutableHashSetTest implements ImmutableSetTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableHashSet<E>> factory() {
        return ImmutableHashSet.factory();
    }
}
