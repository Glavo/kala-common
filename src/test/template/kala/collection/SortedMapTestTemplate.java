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

import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface SortedMapTestTemplate extends MapTestTemplate {
    @Override
    default <K, V> MapFactory<K, V, ?, ? extends SortedMap<K, V>> factory() {
        return factory(null);
    }

    <K, V> MapFactory<K, V, ?, ? extends SortedMap<K, V>> factory(Comparator<? super K> comparator);

    @Override
    default <K, V> SortedMap<K, V> ofEntries(@NotNull Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }

    default <K, V> SortedMap<K, V> ofEntries(Comparator<? super K> comparator, @NotNull Tuple2<K, V>... tuples) {
        return this.<K, V>factory(comparator).ofEntries(tuples);
    }

    @Override
    default <K, V> SortedMap<K, V> from(Iterable<Tuple2<K, V>> entries) {
        return this.<K, V>factory().from(entries);
    }

    default <K, V> SortedMap<K, V> from(Comparator<? super K> comparator, Iterable<Tuple2<K, V>> entries) {
        return this.<K, V>factory(comparator).from(entries);
    }

    @Test
    @Override
    default void ofTest() throws Throwable {
        final Class<?> mapType = mapType();
        if (mapType == null) {
            return;
        }

        final var reverseOrder = Comparator.reverseOrder();

        final var lookup = MethodHandles.publicLookup();
        final var of0 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType));
        final var of1 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparable.class, Object.class
        ));
        final var of2 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparable.class, Object.class,
                Comparable.class, Object.class
        ));
        final var of3 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class
        ));
        final var of4 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class
        ));
        final var of5 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class,
                Comparable.class, Object.class
        ));

        final var of0c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType, Comparator.class));
        final var of1c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparator.class,
                Object.class, Object.class
        ));
        final var of2c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparator.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of3c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparator.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of4c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparator.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of5c = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Comparator.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));

        Map<Integer, String> map;

        // of0

        map = (Map<Integer, String>) of0.invoke();
        assertIterableEquals(List.of(), map.toSeq());

        map = (Map<Integer, String>) of0c.invoke(reverseOrder);
        assertIterableEquals(List.of(), map.toSeq());

        // of1

        map = (Map<Integer, String>) of1.invoke(0, "0");
        assertIterableEquals(List.of(entry(0, "0")), map.toSeq());


        map = (Map<Integer, String>) of1c.invoke(reverseOrder, 0, "0");
        assertIterableEquals(List.of(entry(0, "0")), map.toSeq());

        // of2

        map = (Map<Integer, String>) of2.invoke(0, "0", 1, "1");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1")), map.toSeq());

        map = (Map<Integer, String>) of2.invoke(1, "1", 0, "0");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1")), map.toSeq());

        map = (Map<Integer, String>) of2.invoke(0, "0", 0, "1");
        assertEquals(Map.of(0, "1"), map);


        map = (Map<Integer, String>) of2c.invoke(reverseOrder, 0, "0", 1, "1");
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of2c.invoke(reverseOrder, 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of2c.invoke(reverseOrder, 0, "0", 0, "1");
        assertIterableEquals(List.of(entry(0, "1")), map.toSeq());

        // of3

        map = (Map<Integer, String>) of3.invoke(0, "0", 1, "1", 2, "2");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2")), map.toSeq());

        map = (Map<Integer, String>) of3.invoke(2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2")), map.toSeq());

        map = (Map<Integer, String>) of3.invoke(0, "0", 1, "1", 0, "2");
        assertIterableEquals(List.of(entry(0, "2"), entry(1, "1")), map.toSeq());


        map = (Map<Integer, String>) of3c.invoke(reverseOrder, 0, "0", 1, "1", 2, "2");
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of3c.invoke(reverseOrder, 2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of3c.invoke(reverseOrder, 0, "0", 1, "1", 0, "2");
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "2")), map.toSeq());

        // of4

        map = (Map<Integer, String>) of4.invoke(0, "0", 1, "1", 2, "2", 3, "3");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());

        map = (Map<Integer, String>) of4.invoke(3, "3", 2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());

        map = (Map<Integer, String>) of4.invoke(0, "0", 1, "1", 2, "2", 0, "3");
        assertIterableEquals(List.of(entry(0, "3"), entry(1, "1"), entry(2, "2")), map.toSeq());


        map = (Map<Integer, String>) of4c.invoke(reverseOrder, 0, "0", 1, "1", 2, "2", 3, "3");
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of4c.invoke(reverseOrder, 3, "3", 2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of4c.invoke(reverseOrder, 0, "0", 1, "1", 2, "2", 0, "3");
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "3")), map.toSeq());

        // of5

        map = (Map<Integer, String>) of5.invoke(0, "0", 1, "1", 2, "2", 3, "3", 4, "4");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.toSeq());

        map = (Map<Integer, String>) of5.invoke(4, "4", 3, "3", 2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.toSeq());

        map = (Map<Integer, String>) of5.invoke(0, "0", 1, "1", 2, "2", 3, "3", 0, "4");
        assertIterableEquals(List.of(entry(0, "4"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());


        map = (Map<Integer, String>) of5c.invoke(reverseOrder, 0, "0", 1, "1", 2, "2", 3, "3", 4, "4");
        assertIterableEquals(List.of(entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of5c.invoke(reverseOrder, 4, "4", 3, "3", 2, "2", 1, "1", 0, "0");
        assertIterableEquals(List.of(entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of5c.invoke(reverseOrder, 0, "0", 1, "1", 2, "2", 3, "3", 0, "4");
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "4")), map.toSeq());
    }

    @Test
    @Override
    default void ofEntriesTest() throws Throwable {
        final Class<?> mapType = mapType();
        if (mapType == null) {
            return;
        }

        final var reverseOrder = Comparator.reverseOrder();

        final var lookup = MethodHandles.publicLookup();
        final var of0 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType));
        final var of1 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class));
        final var of2 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class));
        final var of3 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of4 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of5 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));

        final var of0c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class));
        final var of1c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class, Tuple2.class));
        final var of2c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class, Tuple2.class, Tuple2.class));
        final var of3c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of4c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of5c = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Comparator.class, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));

        Map<Integer, String> map;

        // of0

        map = (Map<Integer, String>) of0.invoke();
        assertIterableEquals(List.of(), map.toSeq());

        map = (Map<Integer, String>) of0c.invoke(reverseOrder);
        assertIterableEquals(List.of(), map.toSeq());

        // of1

        map = (Map<Integer, String>) of1.invoke(entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0")), map.toSeq());


        map = (Map<Integer, String>) of1c.invoke(reverseOrder, entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0")), map.toSeq());

        // of2

        map = (Map<Integer, String>) of2.invoke(entry(0, "0"), entry(1, "1"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1")), map.toSeq());

        map = (Map<Integer, String>) of2.invoke(entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1")), map.toSeq());

        map = (Map<Integer, String>) of2.invoke(entry(0, "0"), entry(0, "1"));
        assertEquals(Map.of(0, "1"), map);


        map = (Map<Integer, String>) of2c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"));
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of2c.invoke(reverseOrder, entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of2c.invoke(reverseOrder, entry(0, "0"), entry(0, "1"));
        assertIterableEquals(List.of(entry(0, "1")), map.toSeq());

        // of3

        map = (Map<Integer, String>) of3.invoke(entry(0, "0"), entry(1, "1"), entry(2, "2"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2")), map.toSeq());

        map = (Map<Integer, String>) of3.invoke(entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2")), map.toSeq());

        map = (Map<Integer, String>) of3.invoke(entry(0, "0"), entry(1, "1"), entry(0, "2"));
        assertIterableEquals(List.of(entry(0, "2"), entry(1, "1")), map.toSeq());


        map = (Map<Integer, String>) of3c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(2, "2"));
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of3c.invoke(reverseOrder, entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of3c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(0, "2"));
        assertIterableEquals(List.of(entry(1, "1"), entry(0, "2")), map.toSeq());

        // of4

        map = (Map<Integer, String>) of4.invoke(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());

        map = (Map<Integer, String>) of4.invoke(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());

        map = (Map<Integer, String>) of4.invoke(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(0, "3"));
        assertIterableEquals(List.of(entry(0, "3"), entry(1, "1"), entry(2, "2")), map.toSeq());


        map = (Map<Integer, String>) of4c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"));
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of4c.invoke(reverseOrder, entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of4c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(0, "3"));
        assertIterableEquals(List.of(entry(2, "2"), entry(1, "1"), entry(0, "3")), map.toSeq());

        // of5

        map = (Map<Integer, String>) of5.invoke(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.toSeq());

        map = (Map<Integer, String>) of5.invoke(entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.toSeq());

        map = (Map<Integer, String>) of5.invoke(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(0, "4"));
        assertIterableEquals(List.of(entry(0, "4"), entry(1, "1"), entry(2, "2"), entry(3, "3")), map.toSeq());


        map = (Map<Integer, String>) of5c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4"));
        assertIterableEquals(List.of(entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of5c.invoke(reverseOrder, entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0"));
        assertIterableEquals(List.of(entry(4, "4"), entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "0")), map.toSeq());

        map = (Map<Integer, String>) of5c.invoke(reverseOrder, entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(0, "4"));
        assertIterableEquals(List.of(entry(3, "3"), entry(2, "2"), entry(1, "1"), entry(0, "4")), map.toSeq());
    }
}
