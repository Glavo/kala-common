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

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSortedArraySet;
import kala.collection.immutable.ImmutableSortedSet;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked", "SpellCheckingInspection"})
public interface SortedSetTestTemplate extends SetTestTemplate {
    @Override
    default <E> CollectionFactory<E, ?, ? extends SortedSet<? extends E>> factory() {
        return factory(null);
    }

    <E> CollectionFactory<E, ?, ? extends SortedSet<? extends E>> factory(Comparator<? super E> comparator);

    @Override
    default <E> SortedSet<E> of(E... elements) {
        return of(null, elements);
    }

    @Override
    default <E> SortedSet<E> from(E[] elements) {
        return from(null, elements);
    }

    @Override
    default <E> SortedSet<E> from(Iterable<? extends E> elements) {
        return from(null, elements);
    }

    default <E> SortedSet<E> of(Comparator<? super E> comparator, E... elements) {
        return SortedSet.narrow(this.<E>factory(comparator).from(elements));
    }

    default <E> SortedSet<E> from(Comparator<? super E> comparator, E[] elements) {
        return SortedSet.narrow(this.<E>factory(comparator).from(elements));
    }

    default <E> SortedSet<E> from(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        return SortedSet.narrow(this.<E>factory(comparator).from(elements));
    }

    @Test
    @Override
    default void ofTest() throws Throwable {
        final Class<?> klass = collectionType();
        if (klass == null) {
            return;
        }

        Comparator<String> reverseOrder = Comparator.reverseOrder();

        final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        final MethodHandle of0 = lookup.findStatic(klass, "of", methodType(klass));
        final MethodHandle of1 = lookup.findStatic(klass, "of", methodType(klass, Comparable.class));
        final MethodHandle of2 = lookup.findStatic(klass, "of", methodType(klass, Comparable.class, Comparable.class));
        final MethodHandle of3 = lookup.findStatic(klass, "of", methodType(klass, Comparable.class, Comparable.class, Comparable.class));
        final MethodHandle of4 = lookup.findStatic(klass, "of", methodType(klass, Comparable.class, Comparable.class, Comparable.class, Comparable.class));
        final MethodHandle of5 = lookup.findStatic(klass, "of", methodType(klass, Comparable.class, Comparable.class, Comparable.class, Comparable.class, Comparable.class));
        final MethodHandle ofAll = lookup.findStatic(klass, "of", methodType(klass, Comparable[].class));

        final MethodHandle of0c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class));
        final MethodHandle of1c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object.class));
        final MethodHandle of2c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object.class, Object.class));
        final MethodHandle of3c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object.class, Object.class, Object.class));
        final MethodHandle of4c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object.class, Object.class, Object.class, Object.class));
        final MethodHandle of5c = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object.class, Object.class, Object.class, Object.class, Object.class));
        final MethodHandle ofAllc = lookup.findStatic(klass, "of", methodType(klass, Comparator.class, Object[].class));

        Set<String> set;

        // of0

        set = (Set<String>) of0.invoke();
        assertIterableEquals(List.of(), set);

        set = (Set<String>) of0c.invoke(reverseOrder);
        assertIterableEquals(List.of(), set);

        // of1

        set = (Set<String>) of1.invoke("str0");
        assertIterableEquals(List.of("str0"), set);

        set = (Set<String>) of1c.invoke(reverseOrder, "str0");
        assertIterableEquals(List.of("str0"), set);

        // of2

        set = (Set<String>) of2.invoke("str0", "str1");
        assertIterableEquals(List.of("str0", "str1"), set);

        set = (Set<String>) of2.invoke("str1", "str0");
        assertIterableEquals(List.of("str0", "str1"), set);

        set = (Set<String>) of2.invoke("str0", "str0");
        assertIterableEquals(List.of("str0"), set);


        set = (Set<String>) of2c.invoke(reverseOrder, "str0", "str1");
        assertIterableEquals(List.of("str1", "str0"), set);

        set = (Set<String>) of2c.invoke(reverseOrder, "str1", "str0");
        assertIterableEquals(List.of("str1", "str0"), set);

        set = (Set<String>) of2c.invoke(reverseOrder, "str0", "str0");
        assertIterableEquals(List.of("str0"), set);

        // of3

        set = (Set<String>) of3.invoke("str0", "str1", "str2");
        assertIterableEquals(List.of("str0", "str1", "str2"), set);

        set = (Set<String>) of3.invoke("str2", "str0", "str1");
        assertIterableEquals(List.of("str0", "str1", "str2"), set);

        set = (Set<String>) of3.invoke("str0", "str1", "str0");
        assertIterableEquals(List.of("str0", "str1"), set);

        set = (Set<String>) of3.invoke("str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);


        set = (Set<String>) of3c.invoke(reverseOrder, "str0", "str1", "str2");
        assertIterableEquals(List.of("str2", "str1", "str0"), set);

        set = (Set<String>) of3c.invoke(reverseOrder, "str2", "str0", "str1");
        assertIterableEquals(List.of("str2", "str1", "str0"), set);

        set = (Set<String>) of3c.invoke(reverseOrder, "str0", "str1", "str0");
        assertIterableEquals(List.of("str1", "str0"), set);

        set = (Set<String>) of3c.invoke(reverseOrder, "str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);

        // of4

        set = (Set<String>) of4.invoke("str0", "str1", "str2", "str3");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), set);

        set = (Set<String>) of4.invoke("str3", "str0", "str2", "str1");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), set);

        set = (Set<String>) of4.invoke("str0", "str1", "str1", "str0");
        assertIterableEquals(List.of("str0", "str1"), set);

        set = (Set<String>) of4.invoke("str0", "str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);


        set = (Set<String>) of4c.invoke(reverseOrder, "str0", "str1", "str2", "str3");
        assertIterableEquals(List.of("str3", "str2", "str1", "str0"), set);

        set = (Set<String>) of4c.invoke(reverseOrder, "str3", "str0", "str2", "str1");
        assertIterableEquals(List.of("str3", "str2", "str1", "str0"), set);

        set = (Set<String>) of4c.invoke(reverseOrder, "str0", "str1", "str1", "str0");
        assertIterableEquals(List.of("str1", "str0"), set);

        set = (Set<String>) of4c.invoke(reverseOrder, "str0", "str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);

        // of5

        set = (Set<String>) of5.invoke("str0", "str1", "str2", "str3", "str4");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), set);

        set = (Set<String>) of5.invoke("str1", "str3", "str2", "str4", "str0");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), set);

        set = (Set<String>) of5.invoke("str1", "str3", "str2", "str0", "str0");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), set);

        set = (Set<String>) of5.invoke("str0", "str0", "str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);


        set = (Set<String>) of5c.invoke(reverseOrder, "str0", "str1", "str2", "str3", "str4");
        assertIterableEquals(List.of("str4", "str3", "str2", "str1", "str0"), set);

        set = (Set<String>) of5c.invoke(reverseOrder, "str1", "str3", "str2", "str4", "str0");
        assertIterableEquals(List.of("str4", "str3", "str2", "str1", "str0"), set);

        set = (Set<String>) of5c.invoke(reverseOrder, "str1", "str3", "str2", "str0", "str0");
        assertIterableEquals(List.of("str3", "str2", "str1", "str0"), set);

        set = (Set<String>) of5c.invoke(reverseOrder, "str0", "str0", "str0", "str0", "str0");
        assertIterableEquals(List.of("str0"), set);


        for (String[] data : data1s()) {
            final List<String> sortedValues = Arrays.stream(data).sorted().toList();

            //noinspection ConfusingArgumentToVarargsMethod
            set = (Set<String>) ofAll.invoke(data);
            assertIterableEquals(sortedValues, set);

            set = (Set<String>) ofAllc.invoke(reverseOrder, data);
            assertIterableEquals(sortedValues.reversed(), set);
        }

    }

    @Test
    @Override
    default void fromTest() throws Throwable {
        final Class<?> collectionType = collectionType();
        if (collectionType != null) {
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            final MethodHandle fromArray = lookup.findStatic(collectionType, "from", methodType(collectionType, Comparable[].class));
            final MethodHandle fromIterable = lookup.findStatic(collectionType, "from", methodType(collectionType, Iterable.class));
            final MethodHandle fromIterator = lookup.findStatic(collectionType, "from", methodType(collectionType, Iterator.class));
            final MethodHandle fromStream = lookup.findStatic(collectionType, "from", methodType(collectionType, Stream.class));

            final MethodHandle fromArrayC = lookup.findStatic(collectionType, "from",
                    methodType(collectionType, Comparator.class, Object[].class));
            final MethodHandle fromIterableC = lookup.findStatic(collectionType, "from",
                    methodType(collectionType, Comparator.class, Iterable.class));
            final MethodHandle fromIteratorC = lookup.findStatic(collectionType, "from",
                    methodType(collectionType, Comparator.class, Iterator.class));
            final MethodHandle fromStreamC = lookup.findStatic(collectionType, "from",
                    methodType(collectionType, Comparator.class, Stream.class));

//            final MethodHandle fromSortedSet = lookup.findStatic(collectionType, "from",
//                    methodType(collectionType, SortedSet.class));
//            final MethodHandle fromJavaSortedSet = lookup.findStatic(collectionType, "from",
//                    methodType(collectionType, java.util.SortedSet.class));

            var naturalOrder = Comparator.<String>naturalOrder();
            var reverseOrder = Comparator.<String>reverseOrder();

            List<String[]> allData = new ArrayList<>();
            allData.add(new String[0]);
            Collections.addAll(allData, data1s());

            for (String[] data : allData) {
                final var dataList = Arrays.asList(data);
                final var sorted = dataList.stream().sorted().toList();
                final var reversed = sorted.reversed();

//                final var sortedSet = ImmutableSortedArraySet.from(sorted);
//                final var javaSortedSet = new java.util.TreeSet<>(dataList);
//
//                final var reversedSortedSet = ImmutableSortedArraySet.from(reverseOrder, reversed);
//                final var reversedJavaSortedSet = new java.util.TreeSet<>(reverseOrder);
//                reversedJavaSortedSet.addAll(dataList);

                assertIterableEquals(sorted, (Set<String>) fromArray.invoke((Object[]) data));
                assertIterableEquals(sorted, (Set<String>) fromIterable.invoke(dataList));
                assertIterableEquals(sorted, (Set<String>) fromIterator.invoke(dataList.iterator()));
                assertIterableEquals(sorted, (Set<String>) fromStream.invoke(dataList.stream()));

                assertIterableEquals(sorted, (Set<String>) fromArrayC.invoke(null, (Object[]) data));
                assertIterableEquals(sorted, (Set<String>) fromIterableC.invoke(null, dataList));
                assertIterableEquals(sorted, (Set<String>) fromIteratorC.invoke(null, dataList.iterator()));
                assertIterableEquals(sorted, (Set<String>) fromStreamC.invoke(null, dataList.stream()));

                assertIterableEquals(sorted, (Set<String>) fromArrayC.invoke(naturalOrder, (Object[]) data));
                assertIterableEquals(sorted, (Set<String>) fromIterableC.invoke(naturalOrder, dataList));
                assertIterableEquals(sorted, (Set<String>) fromIteratorC.invoke(naturalOrder, dataList.iterator()));
                assertIterableEquals(sorted, (Set<String>) fromStreamC.invoke(naturalOrder, dataList.stream()));

//                assertIterableEquals(sorted, (Set<String>) fromSortedSet.invoke(sortedSet));
//                assertIterableEquals(sorted, (Set<String>) fromJavaSortedSet.invoke(javaSortedSet));

                assertIterableEquals(reversed, (Set<String>) fromArrayC.invoke(reverseOrder, (Object[]) data));
                assertIterableEquals(reversed, (Set<String>) fromIterableC.invoke(reverseOrder, dataList));
                assertIterableEquals(reversed, (Set<String>) fromIteratorC.invoke(reverseOrder, dataList.iterator()));
                assertIterableEquals(reversed, (Set<String>) fromStreamC.invoke(reverseOrder, dataList.stream()));

//                assertIterableEquals(reversed, (Set<String>) fromSortedSet.invoke(reversedSortedSet));
//                assertIterableEquals(reversed, (Set<String>) fromJavaSortedSet.invoke(reversedJavaSortedSet));
            }
        }
    }
}
