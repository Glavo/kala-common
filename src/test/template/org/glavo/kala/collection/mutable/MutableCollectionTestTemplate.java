package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.Collection;
import org.glavo.kala.collection.CollectionTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;

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
