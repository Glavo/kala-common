package kala.collection.immutable;

import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    default void addedTest() {
        ImmutableSet<String> set0 = of();

        var set1 = set0.added("value0");
        assertSetElements(List.of("value0"), set1);

        var set2 = set1.added("value1");
        assertSetElements(List.of("value0", "value1"), set2);

        assertSetElements(List.of("value0", "value1"), set2.added("value0"));
    }
}
