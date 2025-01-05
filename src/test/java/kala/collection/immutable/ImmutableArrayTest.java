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
import kala.collection.SimpleIterable;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public final class ImmutableArrayTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return ImmutableArray.factory();
    }

    @Override
    public <E> ImmutableArray<E> of(E... elements) {
        return ImmutableArray.of(elements);
    }

    @Override
    public <E> ImmutableArray<E> from(Iterable<? extends E> elements) {
        return ImmutableArray.from(elements);
    }

    @Override
    public <E> ImmutableArray<E> from(E[] elements) {
        return ImmutableArray.from(elements);
    }

    @Test
    public void ofTest() {
        ImmutableSeqTestTemplate.super.ofTest();

        assertIterableEquals(List.of(), ImmutableArray.of());
        assertIterableEquals(List.of("str1"), ImmutableArray.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableArray.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableArray.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableArray.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableArray.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableArray.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableArray.of(data));
        }
    }

    @Test
    public void fromTest() {
        ImmutableSeqTestTemplate.super.fromTest();
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableArray.from(new SimpleIterable<>(Arrays.asList(data))));
            assertIterableEquals(Arrays.asList(data), ImmutableArray.from(Arrays.stream(data)));
        }
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableArray.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableArray.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableArray.<E>from(elements).view();
        }
    }
}
