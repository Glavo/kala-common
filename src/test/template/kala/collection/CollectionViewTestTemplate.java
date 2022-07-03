package kala.collection;

public interface CollectionViewTestTemplate extends CollectionLikeTestTemplate {
    @Override
    <E> CollectionView<E> of(E... elements);

    @Override
    <E> CollectionView<E> from(E[] elements);

    @Override
    <E> CollectionView<E> from(Iterable<? extends E> elements);
}
