package kala.collection.mutable;

import kala.collection.SortedSetTestTemplate;
import kala.collection.factory.CollectionFactory;

import java.util.Comparator;

public interface MutableSortedSetTestTemplate extends MutableSetTestTemplate, SortedSetTestTemplate {
    @Override
    default <E> CollectionFactory<E, ?, ? extends MutableSortedSet<E>> factory() {
        return factory(null);
    }

    @Override
    default <E> MutableSortedSet<E> of(E... elements) {
        return of(null, elements);
    }

    @Override
    default <E> MutableSortedSet<E> from(E[] elements) {
        return from(null, elements);
    }

    @Override
    default <E> MutableSortedSet<E> from(Iterable<? extends E> elements) {
        return from(null, elements);
    }

    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSortedSet<E>> factory(Comparator<? super E> comparator);

    @Override
    <E> MutableSortedSet<E> of(Comparator<? super E> comparator, E... elements);

    @Override
    <E> MutableSortedSet<E> from(Comparator<? super E> comparator, E[] elements);

    @Override
    <E> MutableSortedSet<E> from(Comparator<? super E> comparator, Iterable<? extends E> elements);
}
