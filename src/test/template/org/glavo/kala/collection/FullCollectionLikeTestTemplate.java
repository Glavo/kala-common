package org.glavo.kala.collection;

public interface FullCollectionLikeTestTemplate extends CollectionLikeTestTemplate {
    @Override
    @SuppressWarnings("unchecked")
    <E> FullCollectionLike<E> of(E... elements);

    @Override
    <E> FullCollectionLike<E> from(E[] elements);

    @Override
    <E> FullCollectionLike<E> from(Iterable<? extends E> elements);
}
