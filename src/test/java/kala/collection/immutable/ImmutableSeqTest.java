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
package kala.collection.immutable;

import kala.collection.CollectionView;
import kala.collection.SequentialCollectionViewTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class ImmutableSeqTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return ImmutableSeq.factory();
    }

    @Test
    public void ofTest() throws Throwable {
        ImmutableSeqTestTemplate.super.ofTest();

        assertIterableEquals(List.of(),ImmutableSeq.of());
        assertIterableEquals(List.of("str1"), ImmutableSeq.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableSeq.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableSeq.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableSeq.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableSeq.of(data));
        }
        of().view().appended("zero").joinToString("::");
    }

    @Override
    public void classNameTest() {
    }

    static final class ViewTest implements SequentialCollectionViewTestTemplate {
        @Override
        public <E> CollectionView<E> of(E... elements) {
            return ImmutableSeq.from(elements).view();
        }

        @Override
        public <E> CollectionView<E> from(E[] elements) {
            return ImmutableSeq.from(elements).view();
        }

        @Override
        public <E> CollectionView<E> from(Iterable<? extends E> elements) {
            return ImmutableSeq.<E>from(elements).view();
        }
    }
}
