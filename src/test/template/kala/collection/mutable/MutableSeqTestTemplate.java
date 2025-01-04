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

import kala.collection.SeqTestTemplate;
import kala.collection.factory.CollectionFactory;
import kala.comparator.Comparators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface MutableSeqTestTemplate extends MutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSeq<E>> factory();

    @Override
    default <E> MutableSeq<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableSeq<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableSeq<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

    @Test
    default void setTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> of().set(0, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> of().set(1, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> of().set(~0, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> of().set(~1, "value"));

        {
            MutableSeq<String> seq = of("value");
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(1, "newValue"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(~0, "newValue"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(~2, "newValue"));

            seq.set(0, "newValue");
            assertIterableEquals(List.of("newValue"), seq);

            seq.set(~1, "newValue2");
            assertIterableEquals(List.of("newValue2"), seq);
        }

        {
            MutableSeq<String> seq = of("str0", "str1", "str2");
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(3, "value"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(~0, "value"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.set(~4, "value"));

            seq.set(1, "value");
            assertIterableEquals(List.of("str0", "value", "str2"), seq);

            seq.set(2, "newValue");
            assertIterableEquals(List.of("str0", "value", "newValue"), seq);

            seq.set(2, "foo");
            assertIterableEquals(List.of("str0", "value", "foo"), seq);

            seq.set(0, "bar");
            assertIterableEquals(List.of("bar", "value", "foo"), seq);

            seq.set(~2, "zzz");
            assertIterableEquals(List.of("bar", "zzz", "foo"), seq);
        }
    }

    @Test
    default void swapTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> of().swap(0, 1));

        assertIterableEquals(
                List.of("A", "B", "C"),
                MutableSeq.edit(of("A", "C", "B")).swap(1, 2).done()
        );
        assertIterableEquals(
                List.of("A", "B", "C"),
                MutableSeq.edit(of("A", "C", "B")).swap(2, 1).done()
        );
    }

    @Test
    default void sortTest() {
        List<Comparator<Integer>> comparators = Arrays.asList(
                null,
                Comparators.naturalOrder(),
                Comparators.reverseOrder()
        );

        for (Integer[] data : data1()) {
            for (Comparator<Integer> comparator : comparators) {
                MutableSeq<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                seq.sort(comparator);
                list.sort(comparator);

                assertIterableEquals(list, seq);
            }
        }
    }

    @Test
    default void shuffleTest() {
        for (Integer[] data : data1()) {
            final MutableSeq<Integer> seq = from(data);
            seq.shuffle();

            Assertions.assertEquals(data.length, seq.size());
            Assertions.assertTrue(seq.containsAll(data));
        }

    }

    @Test
    default void replaceAllTest() {
        MutableSeq<Integer> empty = of();
        empty.replaceAll(it -> it * 2);
        assertIterableEquals(List.of(), empty);

        MutableSeq<Integer> list = of(10);
        list.replaceAll(it -> it * 2);
        assertIterableEquals(List.of(20), list);

        list = of(0, 1, 2, 3, 4, 5);
        list.replaceAll(it -> it * 2);
        assertIterableEquals(List.of(0, 2, 4, 6, 8, 10), list);
    }

    @Test
    default void replaceAllIndexedTest() {
        MutableSeq<Integer> empty = of();
        empty.replaceAllIndexed(Integer::sum);
        assertIterableEquals(List.of(), empty);

        MutableSeq<Integer> list = of(10);
        list.replaceAllIndexed(Integer::sum);
        assertIterableEquals(List.of(10), list);

        list = of(0, 1, 2, 3, 4, 5);
        list.replaceAllIndexed(Integer::sum);
        assertIterableEquals(List.of(0, 2, 4, 6, 8, 10), list);
    }
}
