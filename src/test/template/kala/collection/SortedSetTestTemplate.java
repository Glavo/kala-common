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
package kala.collection;

import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    default void ofTest() {
        assertIterableEquals(List.of(), of());

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        assertIterableEquals(values, from(values));


        ArrayList<Integer> shuffleValues = new ArrayList<>(values);
        Collections.shuffle(shuffleValues);
        assertIterableEquals(values, from(shuffleValues));
    }
}
