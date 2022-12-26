package kala.collection.mutable;

import kala.collection.CollectionTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface MutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableCollection<? extends E>> factory();

    @Override
    <E> MutableCollection<E> of(E... elements);

    @Override
    <E> MutableCollection<E> from(E[] elements);

    @Override
    <E> MutableCollection<E> from(Iterable<? extends E> elements);

    @Test
    default void cloneTest() {
        List<MutableCollection<Integer>> collections = List.of(of(), of(1, 2, 3));
        for (var collection : collections) {
            MutableCollection<Integer> cloned = collection.clone();
            assertIterableEquals(collection, cloned);
            if (!collection.isEmpty()) {
                assertNotSame(collection, cloned);
            }
        }
    }
}
