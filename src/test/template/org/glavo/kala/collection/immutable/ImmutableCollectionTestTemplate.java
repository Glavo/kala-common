package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.FullCollectionLikeTestTemplate;
import org.glavo.kala.collection.CollectionTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;

public interface ImmutableCollectionTestTemplate extends CollectionTestTemplate, FullCollectionLikeTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableCollection<? extends E>> factory();

    @Override
    <E> ImmutableCollection<E> of(E... elements);

    @Override
    <E> ImmutableCollection<E> from(E[] elements);

    @Override
    <E> ImmutableCollection<E> from(Iterable<? extends E> elements);
}
