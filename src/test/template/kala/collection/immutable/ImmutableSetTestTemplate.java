package kala.collection.immutable;

import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;

@SuppressWarnings("unchecked")
public interface ImmutableSetTestTemplate extends ImmutableCollectionTestTemplate, SetTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableSet<? extends E>> factory();

    default <E> ImmutableSet<E> of(E... elements) {
        return (ImmutableSet<E>) factory().from(elements);
    }

    default <E> ImmutableSet<E> from(E[] elements) {
        return (ImmutableSet<E>) factory().from(elements);
    }

    default <E> ImmutableSet<E> from(Iterable<? extends E> elements) {
        return (ImmutableSet<E>) factory().from(elements);
    }
}
