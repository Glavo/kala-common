package kala.collection;

import kala.collection.factory.CollectionFactory;

import java.util.Comparator;

public interface SortedSetTestTemplate extends SetTestTemplate {
    @Override
    default <E> CollectionFactory<E, ?, ? extends SortedSet<? extends E>> factory() {
        return factory(null);
    }

    @Override
    default <E> SortedSet<E> of(E... elements) {
        return of(null, elements);
    }

    @Override
    default <E> SortedSet<E> from(E[] elements) {
        return from(null, elements);
    }

    @Override
    default <E> SortedSet<E> from(Iterable<? extends E> elements) {
        return from(null, elements);
    }

    <E> CollectionFactory<E, ?, ? extends SortedSet<? extends E>> factory(Comparator<? super E> comparator);

    <E> SortedSet<E> of(Comparator<? super E> comparator, E... elements);

    <E> SortedSet<E> from(Comparator<? super E> comparator, E[] elements);

    <E> SortedSet<E> from(Comparator<? super E> comparator, Iterable<? extends E> elements);
}
