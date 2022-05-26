package kala.collection;

public interface SetLikeTestTemplate  extends CollectionLikeTestTemplate {
    @Override
    @SuppressWarnings("unchecked")
    <E> SetLike<E> of(E... elements);

    @Override
    <E> SetLike<E> from(E[] elements);

    @Override
    <E> SetLike<E> from(Iterable<? extends E> elements);
}
