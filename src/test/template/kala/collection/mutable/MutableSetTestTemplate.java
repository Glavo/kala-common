package kala.collection.mutable;

import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

public interface MutableSetTestTemplate extends MutableCollectionTestTemplate, SetTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSet<E>> factory();

    @Override
    <E> MutableSet<E> of(E... elements);

    @Override
    <E> MutableSet<E> from(E[] elements);

    @Override
    <E> MutableSet<E> from(Iterable<? extends E> elements);

    @Test
    default void addTest() {
        
    }
}
