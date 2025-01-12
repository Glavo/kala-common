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

import kala.collection.SeqView;
import kala.collection.SeqViewTestTemplate;
import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.MutableSinglyLinkedList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableLinkedSeqTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableLinkedSeq<E>> factory() {
        return ImmutableLinkedSeq.factory();
    }

    @Test
    public void mergeBuilderTest() {
        MutableSinglyLinkedList<String> b1 = new MutableSinglyLinkedList<>();
        MutableSinglyLinkedList<String> b2 = new MutableSinglyLinkedList<>();

        assertTrue(ImmutableLinkedSeq.Builder.merge(b1, b2).isEmpty());

        b1 = MutableSinglyLinkedList.of("str1", "str2");
        b2 = MutableSinglyLinkedList.of("str3", "str4");

        var m = ImmutableLinkedSeq.Builder.merge(b1, b2);
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), m);
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableLinkedSeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableLinkedSeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableLinkedSeq.<E>from(elements).view();
        }
    }
}
