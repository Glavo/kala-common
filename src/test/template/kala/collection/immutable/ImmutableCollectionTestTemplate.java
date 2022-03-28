package kala.collection.immutable;

import kala.collection.CollectionTestTemplate;
import kala.collection.factory.CollectionFactory;

public interface ImmutableCollectionTestTemplate extends CollectionTestTemplate { // TODO
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableCollection<? extends E>> factory();

    @Override
    <E> ImmutableCollection<E> of(E... elements);

    @Override
    <E> ImmutableCollection<E> from(E[] elements);

    @Override
    <E> ImmutableCollection<E> from(Iterable<? extends E> elements);
}
