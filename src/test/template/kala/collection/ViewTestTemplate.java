package kala.collection;

public interface ViewTestTemplate extends CollectionLikeTestTemplate {
    @Override
    <E> View<E> of(E... elements);

    @Override
    <E> View<E> from(E[] elements);

    @Override
    <E> View<E> from(Iterable<? extends E> elements);
}
