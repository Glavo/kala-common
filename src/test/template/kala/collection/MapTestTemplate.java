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
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("unchecked")
public interface MapTestTemplate extends MapLikeTestTemplate {
    <K, V> MapFactory<K, V, ?, ? extends Map<K, V>> factory();

    @Override
    default <K, V> Map<K, V> ofEntries(Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }

    @Override
    default <K, V> Map<K, V> from(Iterable<Tuple2<K, V>> entries) {
        return this.<K, V>factory().from(entries);
    }

    default Class<?> mapType() {
        final String testClassName = this.getClass().getName();
        assertTrue(testClassName.endsWith("Test"));

        final String className = testClassName.substring(0, testClassName.length() - 4);

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail(e);
            return null;
        }
    }

    @Test
    default void ofTest() throws Throwable {
        final Class<?> mapType = mapType();
        if (mapType == null) {
            return;
        }

        final var lookup = MethodHandles.publicLookup();
        final var of0 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType));
        final var of1 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Object.class, Object.class
        ));
        final var of2 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of3 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of4 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));
        final var of5 = lookup.findStatic(mapType, "of", MethodType.methodType(mapType,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class,
                Object.class, Object.class
        ));

        Map<Integer, String> map;

        // of0

        map = (Map<Integer, String>) of0.invoke();
        assertTrue(map.isEmpty());

        // of1

        map = (Map<Integer, String>) of1.invoke(0, "0");
        assertEquals(Map.of(0, "0"), map);

        // of2

        map = (Map<Integer, String>) of2.invoke(0, "0", 1, "1");
        assertEquals(Map.of(0, "0", 1, "1"), map);

        map = (Map<Integer, String>) of2.invoke(0, "0", 0, "1");
        assertEquals(Map.of(0, "1"), map);

        // of3

        map = (Map<Integer, String>) of3.invoke(0, "0", 1, "1", 2, "2");
        assertEquals(Map.of(0, "0", 1, "1", 2, "2"), map);

        map = (Map<Integer, String>) of3.invoke(0, "0", 1, "1", 0, "2");
        assertEquals(Map.of(0, "2", 1, "1"), map);

        // of4

        map = (Map<Integer, String>) of4.invoke(0, "0", 1, "1", 2, "2", 3, "3");
        assertEquals(Map.of(0, "0", 1, "1", 2, "2", 3, "3"), map);

        map = (Map<Integer, String>) of4.invoke(0, "0", 1, "1", 2, "2", 0, "3");
        assertEquals(Map.of(0, "3", 1, "1", 2, "2"), map);

        // of5

        map = (Map<Integer, String>) of5.invoke(0, "0", 1, "1", 2, "2", 3, "3", 4, "4");
        assertEquals(Map.of(0, "0", 1, "1", 2, "2", 3, "3", 4, "4"), map);

        map = (Map<Integer, String>) of5.invoke(0, "0", 1, "1", 2, "2", 3, "3", 0, "4");
        assertEquals(Map.of(0, "4", 1, "1", 2, "2", 3, "3"), map);
    }

    @Test
    default void ofEntriesTest() throws Throwable {
        final Class<?> mapType = mapType();
        if (mapType == null) {
            return;
        }

        final var lookup = MethodHandles.publicLookup();
        final var of0 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType));
        final var of1 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class));
        final var of2 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class));
        final var of3 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of4 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));
        final var of5 = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class, Tuple2.class));
        final var ofAll = lookup.findStatic(mapType, "ofEntries", MethodType.methodType(mapType, Tuple2[].class));

        Map<Integer, String> map;

        // of0

        map = (Map<Integer, String>) of0.invoke();
        assertTrue(map.isEmpty());

        // of1

        map = (Map<Integer, String>) of1.invoke(Tuple.of(0, "0"));
        assertEquals(Map.of(0, "0"), map);

        // of2

        map = (Map<Integer, String>) of2.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"));
        assertEquals(Map.of(0, "0", 1, "1"), map);

        map = (Map<Integer, String>) of2.invoke(Tuple.of(0, "0"), Tuple.of(0, "1"));
        assertEquals(Map.of(0, "1"), map);

        // of3

        map = (Map<Integer, String>) of3.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(2, "2"));
        assertEquals(Map.of(0, "0", 1, "1", 2, "2"), map);

        map = (Map<Integer, String>) of3.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(0, "2"));
        assertEquals(Map.of(0, "2", 1, "1"), map);

        // of4

        map = (Map<Integer, String>) of4.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(3, "3"));
        assertEquals(Map.of(0, "0", 1, "1", 2, "2", 3, "3"), map);

        map = (Map<Integer, String>) of4.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(0, "3"));
        assertEquals(Map.of(0, "3", 1, "1", 2, "2"), map);

        // of5

        map = (Map<Integer, String>) of5.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(3, "3"), Tuple.of(4, "4"));
        assertEquals(Map.of(0, "0", 1, "1", 2, "2", 3, "3", 4, "4"), map);

        map = (Map<Integer, String>) of5.invoke(Tuple.of(0, "0"), Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(3, "3"), Tuple.of(0, "4"));
        assertEquals(Map.of(0, "4", 1, "1", 2, "2", 3, "3"), map);

        for (Integer[] data : data1()) {
            Tuple2<Integer, String>[] array = (Tuple2<Integer, String>[])
                    Stream.of(data).map(it -> Tuple.of(it, String.valueOf(it))).toArray(Tuple2<?, ?>[]::new);

            //noinspection ConfusingArgumentToVarargsMethod
            map = (Map<Integer, String>) ofAll.invoke(array);

            assertEquals(data.length, map.size());
            assertEquals(Map.ofEntries(array), map);
        }
    }
}
