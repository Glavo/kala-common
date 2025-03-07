/*
 * Copyright 2024 Glavo
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

import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableArray;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface SequentialCollectionTestTemplate extends CollectionTestTemplate {

    @Test
    @Override
    default void factoryTest() {
        CollectionFactory<Object, Object, Collection<?>> factory =
                (CollectionFactory<Object, Object, Collection<?>>) factory();
        assertIterableEquals(List.of(), factory.empty());
        assertIterableEquals(List.of(), factory.from(GenericArrays.EMPTY_OBJECT_ARRAY));
        assertIterableEquals(List.of(), factory.from(java.util.List.of()));
        assertIterableEquals(List.of(), factory.from(new Object[]{}));
        assertIterableEquals(List.of(), factory.from(Iterators.empty()));

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), factory.from(data));
            assertIterableEquals(Arrays.asList(data), factory.from(ImmutableArray.from(Arrays.asList(data))));
            assertIterableEquals(Arrays.asList(data), factory.from(Arrays.asList(data)));
            assertIterableEquals(Arrays.asList(data), factory.from(Arrays.asList(data).iterator()));
        }

        assertIterableEquals(List.of(), factory.build(factory.newBuilder()));

        for (Integer[] data : data1()) {
            Object builder = factory.newBuilder();
            for (Integer i : data) {
                factory.addToBuilder(builder, i);
            }
            assertIterableEquals(Arrays.asList(data), factory.build(builder));
        }

        assertIterableEquals(List.of(), factory.fill(0, "foo"));
        assertIterableEquals(List.of(), factory.fill(-1, "foo"));
        assertIterableEquals(List.of(), factory.fill(Integer.MIN_VALUE, "foo"));

        assertIterableEquals(List.of(), factory.fill(0, i -> "foo"));
        assertIterableEquals(List.of(), factory.fill(-1, i -> "foo"));
        assertIterableEquals(List.of(), factory.fill(Integer.MIN_VALUE, i -> "foo"));

        assertIterableEquals(List.of("foo"), factory.fill(1, "foo"));
        assertIterableEquals(List.of("foo", "foo"), factory.fill(2, "foo"));
        assertIterableEquals(List.of("foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo"), factory.fill(10, "foo"));


        assertIterableEquals(List.of("foo"), factory.fill(1, i -> "foo"));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                factory.fill(10, new IntFunction<>() {
                    int i = 0;

                    @Override
                    public Object apply(int value) {
                        return i++;
                    }
                })
        );

        assertIterableEquals(List.of("foo: 0"), factory.fill(1, i -> "foo: " + i));
        assertIterableEquals(List.of("foo: 0", "foo: 1"), factory.fill(2, i -> "foo: " + i));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), factory.fill(10, i -> i));

        Object b1;
        Object b2;

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            assertIterableEquals(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b2, List.of("foo", "bar"));
            assertIterableEquals(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            factory.addAllToBuilder(b2, List.of("A", "B"));
            assertIterableEquals(List.of("foo", "bar", "A", "B"), factory.build(factory.mergeBuilder(b1, b2)));
        }
    }

    @Test
    default void classNameTest() {
        Class<?> type = collectionType();
        if (type != null) {
            assertEquals(type.getSimpleName(), of().className());
        }
    }

    @Test
    default void serializationTest() throws IOException, ClassNotFoundException {
    }
}
