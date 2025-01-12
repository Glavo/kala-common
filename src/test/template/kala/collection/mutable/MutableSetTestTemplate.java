/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.mutable;

import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;
import kala.function.Predicates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static kala.ExtendedAssertions.assertSetElements;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface MutableSetTestTemplate extends MutableCollectionTestTemplate, SetTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSet<E>> factory();

    @Override
    default <E> MutableSet<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableSet<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableSet<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

    @Test
    default void addTest() {
        MutableSet<Integer> set = of();

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(0));

        for (Integer value : values) {
            set.add(value);
        }

        for (int i = 0; i < 10; i++) {
            set.add(values.get(i));
        }

        assertSetElements(values, set);
    }

    @Test
    default void addAllTest() {
        MutableSet<Integer> set = of();

        set.addAll();
        assertSetElements(List.of(), set);

        set.addAll(1, 2, 3);
        assertSetElements(List.of(1, 2, 3), set);

        set.addAll(List.of(3, 4, 5));
        assertSetElements(List.of(1, 2, 3, 4, 5), set);

        assertThrows(NullPointerException.class, () -> set.addAll((Integer[]) null));
        assertThrows(NullPointerException.class, () -> set.addAll((Iterable<Integer>) null));
    }

    @Test
    default void removeTest() {
        MutableSet<Integer> set = of();
        set.remove(10);
        assertSetElements(List.of(), set);

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(0));

        for (Integer value : values) {
            set.add(value);
        }
        assertSetElements(values, set);

        for (int i = values.size() - 1; i >= 0; i--) {
            if (i % 3 == 0) {
                set.remove(values.remove(i));
            }
        }

        assertSetElements(values, set);
    }

    @Test
    default void removeAllTest() {
        MutableSet<Integer> set = of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertFalse(set.removeAll());
        assertFalse(set.removeAll(List.of()));
        assertSetElements(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), set);

        assertFalse(set.removeAll(10, 11, 12));
        assertFalse(set.removeAll(List.of(10, 11, 12)));
        assertSetElements(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), set);

        assertTrue(set.removeAll(0, 1, 10));
        assertSetElements(List.of(2, 3, 4, 5, 6, 7, 8, 9), set);

        assertTrue(set.removeAll(List.of(0, 2, 9)));
        assertSetElements(List.of(3, 4, 5, 6, 7, 8), set);
    }

    @Test
    default void removeIfTest() {
        MutableSet<Integer> set = of(0, 1, 2, 3, 4, 5);
        assertFalse(set.removeIf(Predicates.alwaysFalse()));
        assertSetElements(List.of(0, 1, 2, 3, 4, 5), set);

        assertTrue(set.removeIf(it -> it % 2 == 0));
        assertSetElements(List.of(1, 3, 5), set);

        assertFalse(set.removeIf(it -> it % 2 == 0));
        assertSetElements(List.of(1, 3, 5), set);

        assertTrue(set.removeIf(Predicates.alwaysTrue()));
        assertSetElements(List.of(), set);

        assertFalse(set.removeIf(Predicates.alwaysTrue()));
        assertSetElements(List.of(), set);
    }
}
