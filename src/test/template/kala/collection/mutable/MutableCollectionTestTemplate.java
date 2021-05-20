package kala.collection.mutable;

import kala.collection.CollectionTestTemplate;
import kala.collection.factory.CollectionFactory;

public interface MutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableCollection<? extends E>> factory();

    @Override
    <E> MutableCollection<E> of(E... elements);

    @Override
    <E> MutableCollection<E> from(E[] elements);

    @Override
    <E> MutableCollection<E> from(Iterable<? extends E> elements);
}
