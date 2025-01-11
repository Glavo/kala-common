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

import kala.ExtendedAssertions;
import kala.collection.SetTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        ExtendedAssertions.assertSetElements(values, s1);
    }

    @Test
    default void removeTest() {
        MutableSet<Integer> s1 = of();
        s1.remove(10);
        ExtendedAssertions.assertSetElements(List.of(), s1);

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(0));

        for (Integer value : values) {
            s1.add(value);
        }
        ExtendedAssertions.assertSetElements(values, s1);

        for (int i = values.size() - 1; i >= 0; i--) {
            if (i % 3 == 0) {
                s1.remove(values.remove(i));
            }
        }

        ExtendedAssertions.assertSetElements(values, s1);

    }
}
