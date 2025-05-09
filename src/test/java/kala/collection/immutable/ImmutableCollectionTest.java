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

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public final class ImmutableCollectionTest implements ImmutableSequentialCollectionTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableCollection<E>> factory() {
        return new CollectionFactory<E, ArrayList<E>, ImmutableCollection<E>>() {

            @Override
            public ArrayList<E> newBuilder() {
                return new ArrayList<>();
            }

            @Override
            public void addToBuilder(@NotNull ArrayList<E> es, E value) {
                es.add(value);
            }

            @Override
            public ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
                builder1.addAll(builder2);
                return builder1;
            }

            @Override
            public ImmutableCollection<E> build(@NotNull ArrayList<E> es) {
                return new SimpleImmutableCollection<>(es);
            }
        };
    }

    @Override
    public <E> ImmutableCollection<E> of(E... elements) {
        return ImmutableCollection.from(elements);
    }

    @Override
    public <E> ImmutableCollection<E> from(E[] elements) {
        return ImmutableCollection.from(elements);
    }

    @Override
    public <E> ImmutableCollection<E> from(Iterable<? extends E> elements) {
        return ImmutableCollection.from(elements);
    }

    @Test
    public void ofTest() {

        assertIterableEquals(List.of(), ImmutableCollection.of());
        assertIterableEquals(List.of("str1"), ImmutableCollection.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableCollection.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableCollection.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableCollection.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableCollection.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableCollection.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableCollection.of(data));
        }
    }

    @Test
    public void classNameTest() {
        assertEquals("ImmutableCollection", new SimpleImmutableCollection<>(List.of()).className());
    }

    static final class SimpleImmutableCollection<E> extends AbstractImmutableCollection<E> {
        private final Iterable<? extends E> source;

        SimpleImmutableCollection(Iterable<? extends E> source) {
            this.source = source;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return (Iterator<E>) source.iterator();
        }
    }
}
