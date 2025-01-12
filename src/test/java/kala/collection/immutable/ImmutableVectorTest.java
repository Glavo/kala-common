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
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableVectorTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableVector<E>> factory() {
        return ImmutableVector.factory();
    }

    @Test
    public void ofTest() throws Throwable {
        ImmutableSeqTestTemplate.super.ofTest();

        assertIterableEquals(List.of(),ImmutableVector.of());

        assertIterableEquals(List.of("str1"), ImmutableVector.of("str1"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1").getClass());

        assertIterableEquals(List.of("str1", "str2"), ImmutableVector.of("str1", "str2"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableVector.of("str1", "str2", "str3"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableVector.of("str1", "str2", "str3", "str4"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableVector.of("str1", "str2", "str3", "str4", "str5"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4", "str5").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableVector.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            ImmutableVector<Integer> v = ImmutableVector.of(data);
            assertIterableEquals(Arrays.asList(data), v );
            if (data.length == 0) {
                assertSame(ImmutableVector.empty(), v);
            } else if (data.length <= ImmutableVectors.WIDTH) {
                assertSame(ImmutableVectors.Vector1.class, v.getClass());
            } else {
                assertNotSame(ImmutableVectors.Vector1.class, v.getClass());
            }
        }
    }

    @Test
    @Override
    public void fillTest() throws Throwable {
        ImmutableSeqTestTemplate.super.fillTest();

        ImmutableVector<String> v;

        v = ImmutableVector.fill(ImmutableVectors.WIDTH, "str");
        assertSame(ImmutableVectors.Vector1.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH, i -> "str");
        assertSame(ImmutableVectors.Vector1.class, v.getClass());

        v = ImmutableVector.fill(ImmutableVectors.WIDTH + 1, "str");
        assertSame(ImmutableVectors.Vector2.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH + 1, i -> "str");
        assertSame(ImmutableVectors.Vector2.class, v.getClass());
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableVector.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableVector.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableVector.<E>from(elements).view();
        }
    }
}
