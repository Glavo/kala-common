package org.glavo.kala.collection;

public interface ViewTestTemplate extends FullCollectionLikeTestTemplate{
    @Override
    <E> View<E> of(E... elements);

    @Override
    <E> View<E> from(E[] elements);

    @Override
    <E> View<E> from(Iterable<? extends E> elements);
}
