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
package kala.collection;

import kala.SerializationUtils;
import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableArray;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static kala.ExtendedAssertions.assertSetElements;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface SetTestTemplate extends SetLikeTestTemplate, CollectionTestTemplate {

    @Override
    <E> CollectionFactory<E, ?, ? extends Set<? extends E>> factory();

    @Override
    default <E> Set<E> of(E... elements) {
        return Set.narrow(this.<E>factory().from(elements));
    }

    @Override
    default <E> Set<E> from(E[] elements) {
        return Set.narrow(this.<E>factory().from(elements));
    }

    @Override
    default <E> Set<E> from(Iterable<? extends E> elements) {
        return Set.narrow(this.<E>factory().from(elements));
    }

    @Test
    default void ofTest() throws Throwable {
        final Class<?> klass = collectionType();
        if (klass == null) {
            return;
        }

        final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        final MethodHandle of0 = lookup.findStatic(klass, "of", MethodType.methodType(klass));
        final MethodHandle of1 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class));
        final MethodHandle of2 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class));
        final MethodHandle of3 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class));
        final MethodHandle of4 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class, Object.class));
        final MethodHandle of5 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class, Object.class, Object.class));
        final MethodHandle ofAll = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class.arrayType()));

        Set<String> set;

        // of0

        set = (Set<String>) of0.invoke();
        assertTrue(set.isEmpty());

        // of1

        set = (Set<String>) of1.invoke("str0");
        assertSetElements(List.of("str0"), set);

        // of2

        set = (Set<String>) of2.invoke("str0", "str1");
        assertSetElements(List.of("str0", "str1"), set);

        set = (Set<String>) of2.invoke("str0", "str0");
        assertSetElements(List.of("str0"), set);

        // of3

        set = (Set<String>) of3.invoke("str0", "str1", "str2");
        assertSetElements(List.of("str0", "str1", "str2"), set);

        set = (Set<String>) of3.invoke("str0", "str1", "str0");
        assertSetElements(List.of("str0", "str1"), set);

        // of4

        set = (Set<String>) of4.invoke("str0", "str1", "str2", "str3");
        assertSetElements(List.of("str0", "str1", "str2", "str3"), set);

        set = (Set<String>) of4.invoke("str0", "str1", "str2", "str0");
        assertSetElements(List.of("str0", "str1", "str2"), set);

        // of5

        set = (Set<String>) of5.invoke("str0", "str1", "str2", "str3", "str4");
        assertSetElements(List.of("str0", "str1", "str2", "str3", "str4"), set);

        set = (Set<String>) of5.invoke("str0", "str1", "str2", "str3", "str0");
        assertSetElements(List.of("str0", "str1", "str2", "str3"), set);

        for (String[] data : data1s()) {
            Set<String> size = (Set<String>) ofAll.invoke(data);
            assertEquals(data.length, size.size());
            assertSetElements(Arrays.asList(data), size);
        }

    }

    @Test
    default void fromTest() throws Throwable {
        final Class<?> collectionType = collectionType();
        if (collectionType != null) {
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            final MethodHandle fromArray = lookup.findStatic(collectionType, "from", MethodType.methodType(collectionType, Object[].class));
            final MethodHandle fromIterable = lookup.findStatic(collectionType, "from", MethodType.methodType(collectionType, Iterable.class));
            final MethodHandle fromIterator = lookup.findStatic(collectionType, "from", MethodType.methodType(collectionType, Iterator.class));
            final MethodHandle fromStream = lookup.findStatic(collectionType, "from", MethodType.methodType(collectionType, Stream.class));

            for (String[] data : data1s()) {
                final var dataList = Arrays.asList(data);

                assertSetElements(dataList, (Set<String>) fromArray.invoke((Object[]) data));
                assertSetElements(dataList, (Set<String>) fromIterable.invoke(dataList));
                assertSetElements(dataList, (Set<String>) fromIterator.invoke(dataList.iterator()));
                assertSetElements(dataList, (Set<String>) fromStream.invoke(dataList.stream()));
            }
        }
    }

    @Test
    @Override
    default void factoryTest() {
        CollectionFactory<Object, Object, Set<?>> factory =
                (CollectionFactory<Object, Object, Set<?>>) factory();
        assertSetElements(List.of(), factory.empty());
        assertSetElements(List.of(), factory.from(GenericArrays.EMPTY_OBJECT_ARRAY));
        assertSetElements(List.of(), factory.from(java.util.List.of()));
        assertSetElements(List.of(), factory.from(new Object[]{}));
        assertSetElements(List.of(), factory.from(Iterators.empty()));

        for (Integer[] data : data1()) {
            var dataSet = java.util.Set.of(data);

            assertSetElements(dataSet, factory.from(data));
            assertSetElements(dataSet, factory.from(ImmutableArray.from(Arrays.asList(data))));
            assertSetElements(dataSet, factory.from(Arrays.asList(data)));
            assertSetElements(dataSet, factory.from(Arrays.asList(data).iterator()));
        }

        assertSetElements(List.of(), factory.build(factory.newBuilder()));

        for (Integer[] data : data1()) {
            Object builder = factory.newBuilder();
            for (Integer i : data) {
                factory.addToBuilder(builder, i);
            }
            assertSetElements(Arrays.asList(data), factory.build(builder));
        }

        Object b1;
        Object b2;

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            assertSetElements(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b2, List.of("foo", "bar"));
            assertSetElements(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            factory.addAllToBuilder(b2, List.of("A", "B"));
            assertSetElements(List.of("foo", "bar", "A", "B"), factory.build(factory.mergeBuilder(b1, b2)));
        }
    }

    @Test
    default void serializationTest() throws IOException, ClassNotFoundException {
        try {
            for (Integer[] data : data1()) {
                Collection<?> c = factory().from(data);
                ByteArrayOutputStream out = new ByteArrayOutputStream(4 * 128);
                new ObjectOutputStream(out).writeObject(c);
                byte[] buffer = out.toByteArray();
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                var obj = (Set<Integer>) new ObjectInputStream(in).readObject();

                assertEquals(c, obj);
            }
        } catch (NotSerializableException ignored) {
        }

        assertEquals(of(), SerializationUtils.writeAndRead(of()));
        assertEquals(of(0), SerializationUtils.writeAndRead(of(0)));
        assertEquals(of(0, 1, 2), SerializationUtils.writeAndRead(of(0, 1, 2)));

        for (String[] data : data1s()) {
            assertEquals(from(data), SerializationUtils.writeAndRead(from(data)));
        }
    }
}
