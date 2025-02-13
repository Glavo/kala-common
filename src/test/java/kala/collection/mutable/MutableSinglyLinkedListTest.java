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

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableLinkedSeq;
import kala.collection.immutable.ImmutableSeq;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class MutableSinglyLinkedListTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableSinglyLinkedList<E>> factory() {
        return MutableSinglyLinkedList.factory();
    }

    @Override
    public <E> MutableSinglyLinkedList<E> of(E... elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Override
    public <E> MutableSinglyLinkedList<E> from(E[] elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Override
    public <E> MutableSinglyLinkedList<E> from(Iterable<? extends E> elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Test
    void ensureUnaliasedTest() {
        ImmutableArray<String> values = ImmutableArray.of("value1", "value2", "value3");

        MutableSinglyLinkedList<String> seq = MutableSinglyLinkedList.from(values);
        ImmutableLinkedSeq<String> immSeq = seq.freeze();

        assertIterableEquals(values, immSeq);

        seq.append("value4");
        assertIterableEquals(values.appended("value4"), seq);
        assertIterableEquals(values, immSeq);

        seq.prepend("value0");
        assertIterableEquals(values.appended("value4").prepended("value0"), seq);
        assertIterableEquals(values, immSeq);
    }

    @Test
    public void sortTest() {
        List<Comparator<Integer>> comparators = Arrays.asList(
                null,
                Comparator.naturalOrder(),
                Comparator.reverseOrder()
        );

        for (Integer[] data : data1()) {
            for (Comparator<Integer> comparator : comparators) {
                MutableSeq<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                seq.sort(comparator);
                list.sort(comparator);

                assertIterableEquals(list, seq);
            }
            for (Comparator<Integer> comparator : comparators) {
                MutableSinglyLinkedList<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                ImmutableSeq<Integer> frozen = seq.freeze();
                @SuppressWarnings({"unchecked", "rawtypes"})
                List<Integer> frozenValues = (List) Arrays.asList(frozen.toArray());

                seq.sort(comparator);
                list.sort(comparator);

                assertIterableEquals(list, seq);
                assertIterableEquals(frozenValues, frozen);
            }
        }

    }
}
