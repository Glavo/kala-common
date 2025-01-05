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
import kala.comparator.Comparators;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class MutableLinkedListTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableLinkedList<E>> factory() {
        return MutableLinkedList.factory();
    }

    @Override
    public <E> MutableLinkedList<E> of(E... elements) {
        return MutableLinkedList.from(elements);
    }

    @Override
    public <E> MutableLinkedList<E> from(E[] elements) {
        return MutableLinkedList.from(elements);
    }

    @Override
    public <E> MutableLinkedList<E> from(Iterable<? extends E> elements) {
        return MutableLinkedList.from(elements);
    }

    @Test
    void ensureUnaliasedTest() {
        ImmutableArray<String> values = ImmutableArray.of("value1", "value2", "value3");

        MutableLinkedList<String> seq = MutableLinkedList.from(values);
        ImmutableLinkedSeq<String> immSeq = seq.toImmutableLinkedSeq();

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
            for (Comparator<Integer> comparator : comparators) {
                MutableSeq<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                ImmutableLinkedSeq<Integer> frozen = seq.toImmutableLinkedSeq();
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
