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
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableVector;
import kala.collection.mutable.MutableArray;
import kala.collection.factory.CollectionFactory;
import kala.value.primitive.IntVar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface SeqTestTemplate extends CollectionTestTemplate, SeqLikeTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();

    @Override
    default <E> Seq<E> of(E... elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Override
    default <E> Seq<E> from(E[] elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Override
    default <E> Seq<E> from(Iterable<? extends E> elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Test
    default void ofTest() {
        final Class<?> klass = collectionType();
        if (klass == null) {
            return;
        }

        try {
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            final MethodHandle of0 = lookup.findStatic(klass, "of", MethodType.methodType(klass));
            final MethodHandle of1 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class));
            final MethodHandle of2 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class));
            final MethodHandle of3 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class));
            final MethodHandle of4 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class, Object.class));
            final MethodHandle of5 = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object.class, Object.class, Object.class, Object.class, Object.class));
            final MethodHandle ofAll = lookup.findStatic(klass, "of", MethodType.methodType(klass, Object[].class));


            Seq<Object> seq0 = (Seq<Object>) of0.invoke();
            assertTrue(seq0.isEmpty());

            Seq<Object> seq1 = (Seq<Object>) of1.invoke("str0");
            assertEquals(1, seq1.size());
            assertIterableEquals(List.of("str0"), seq1);

            Seq<Object> seq2 = (Seq<Object>) of2.invoke("str0", "str1");
            assertEquals(2, seq2.size());
            assertIterableEquals(List.of("str0", "str1"), seq2);

            Seq<Object> seq3 = (Seq<Object>) of3.invoke("str0", "str1", "str2");
            assertEquals(3, seq3.size());
            assertIterableEquals(List.of("str0", "str1", "str2"), seq3);

            Seq<Object> seq4 = (Seq<Object>) of4.invoke("str0", "str1", "str2", "str3");
            assertEquals(4, seq4.size());
            assertIterableEquals(List.of("str0", "str1", "str2", "str3"), seq4);

            Seq<Object> seq5 = (Seq<Object>) of5.invoke("str0", "str1", "str2", "str3", "str4");
            assertEquals(5, seq5.size());
            assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq5);

            for (Integer[] data : data1()) {
                Seq<Integer> size = (Seq<Integer>) ofAll.invoke((Object[]) data);
                assertEquals(data.length, size.size());
                assertIterableEquals(Arrays.asList(data), size);
            }

        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    default void fromTest() {
        final Class<?> klass = collectionType();
        if (klass != null) {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

                final MethodHandle fromArray = lookup.findStatic(klass, "from", MethodType.methodType(klass, Object[].class));
                final MethodHandle fromIterable = lookup.findStatic(klass, "from", MethodType.methodType(klass, Iterable.class));
                final MethodHandle fromIterator = lookup.findStatic(klass, "from", MethodType.methodType(klass, Iterator.class));
                final MethodHandle fromStream = lookup.findStatic(klass, "from", MethodType.methodType(klass, Stream.class));


                for (Integer[] data : data1()) {
                    final List<Integer> dataList = Arrays.asList(data);

                    assertIterableEquals(dataList, (Seq<Integer>) fromArray.invoke((Object[]) data));
                    assertIterableEquals(dataList, (Seq<Integer>) fromIterable.invoke(dataList));
                    assertIterableEquals(dataList, (Seq<Integer>) fromIterator.invoke(dataList.iterator()));
                    assertIterableEquals(dataList, (Seq<Integer>) fromStream.invoke(dataList.stream()));
                }

            } catch (Throwable e) {
                fail(e);
            }
        }
    }

    @Test
    default void fillTest() {
        final Class<?> klass = collectionType();
        if (klass != null) {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

                final MethodHandle fillValue = lookup.findStatic(klass, "fill", MethodType.methodType(klass, int.class, Object.class));
                final MethodHandle fillIntFunction = lookup.findStatic(klass, "fill", MethodType.methodType(klass, int.class, IntFunction.class));

                assertIterableEquals(List.of(), (Seq<String>) fillValue.invoke(0, "value"));
                assertIterableEquals(List.of("value"), (Seq<String>) fillValue.invoke(1, "value"));
                assertIterableEquals(List.of("value", "value", "value"), (Seq<String>) fillValue.invoke(3, "value"));

                assertIterableEquals(List.of("value0"), (Seq<String>) fillIntFunction.invoke(1, (IntFunction<String>) i -> "value" + i));

                List<Integer> expected = List.of(0, 1, 2, 3, 4, 5);

                IntVar intVar = new IntVar();
                IntFunction<Integer> function = i -> i;

                assertIterableEquals(List.of(), (Seq<Integer>) fillIntFunction.invoke(0, function));
                assertIterableEquals(expected, (Seq<Integer>) fillIntFunction.invoke(6, function));
            } catch (Throwable e) {
                fail(e);
            }
        }
    }

    @Test
    default void generateUntilTest() {
        final Class<?> klass = collectionType();
        if (klass != null) {
            try {
                final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

                final MethodHandle generateUntil = lookup.findStatic(klass, "generateUntil", MethodType.methodType(klass, Supplier.class, Predicate.class));
                final MethodHandle generateUntilNull = lookup.findStatic(klass, "generateUntilNull", MethodType.methodType(klass, Supplier.class));

                List<Integer> expected = List.of(0, 1, 2, 3, 4, 5);

                {
                    IntVar var = new IntVar();
                    Supplier<Integer> supplier = () -> var.value++;
                    Predicate<Integer> predicate = it -> it > 5;

                    assertIterableEquals(expected, (Seq<Integer>) generateUntil.invoke(supplier, predicate));
                }
                {
                    IntVar var = new IntVar();
                    Supplier<Integer> supplier = () -> {
                        int res = var.value++;
                        return res <= 5 ? res : null;
                    };

                    assertIterableEquals(expected, (Seq<Integer>) generateUntilNull.invoke(supplier));
                }

            } catch (Throwable e) {
                fail(e);
            }
        }
    }

    @Test
    default void collectTest() {
        assertEquals("", this.<String>of().collect(Collectors.joining()));
        assertEquals("str0str1str2", this.of("str0", "str1", "str2").collect(Collectors.joining()));

        assertIterableEquals(List.of(), this.<String>of().collect(Seq.factory()));
        assertIterableEquals(List.of("str0", "str1", "str2"), this.of("str0", "str1", "str2").collect(Seq.factory()));
    }

    @Test
    default void equalsTest() {
        Assertions.assertEquals(ImmutableArray.empty(), factory().empty());
        assertEquals(ImmutableVector.empty(), factory().empty());
        assertEquals(MutableArray.empty(), factory().empty());

        Seq<?> foo = factory().from(List.of("foo"));
        assertNotEquals(ImmutableArray.empty(), foo);
        assertNotEquals(ImmutableVector.empty(), foo);
        assertNotEquals(MutableArray.empty(), foo);
        assertEquals(ImmutableArray.of("foo"), foo);
        assertEquals(ImmutableVector.of("foo"), foo);
        assertEquals(MutableArray.of("foo"), foo);

        for (Integer[] data : data1()) {
            assertEquals(ImmutableArray.from(data), factory().from(data));
            assertEquals(ImmutableVector.from(data), factory().from(data));
            assertEquals(MutableArray.from(data), factory().from(data));
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
                Object obj = new ObjectInputStream(in).readObject();
                assertIterableEquals(c, (Iterable<?>) obj);
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
