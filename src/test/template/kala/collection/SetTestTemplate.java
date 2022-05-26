package kala.collection;

import kala.collection.factory.CollectionFactory;

public interface SetTestTemplate extends SetLikeTestTemplate, CollectionTestTemplate {

    @Override
    <E> CollectionFactory<E, ?, ? extends Set<? extends E>> factory();

    @Override
    <E> Set<E> of(E... elements);

    @Override
    <E> Set<E> from(E[] elements);

    @Override
    <E> Set<E> from(Iterable<? extends E> elements);

    @Override
    default void factoryTest() {

    }
}
