package kala.collection.mutable;

import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

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
        MutableSet<Integer> s1 = of();

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(0));

        for (Integer value : values) {
            s1.add(value);
        }

        for (int i = 0; i < 10; i++) {
            s1.add(values.get(i));
        }

        assertSetElements(values, s1);
    }

    @Test
    default void removeTest() {
        MutableSet<Integer> s1 = of();
        s1.remove(10);
        assertSetElements(List.of(), s1);

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(0));

        for (Integer value : values) {
            s1.add(value);
        }
        assertSetElements(values, s1);

        for (int i = values.size() - 1; i >= 0; i--) {
            if (i % 3 == 0) {
                s1.remove(values.remove(i));
            }
        }

        assertSetElements(values, s1);

    }
}
