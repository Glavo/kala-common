/*
 * Copyright 2026 Glavo
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
package kala.collection.primitive;

import kala.collection.base.MapIterator;
import kala.collection.base.primitive.IntIntMapIterator;
import kala.collection.immutable.primitive.ImmutableIntIntHashMap;
import kala.collection.immutable.primitive.ImmutableIntIntMap;
import kala.collection.mutable.primitive.MutableIntIntHashMap;
import kala.collection.mutable.primitive.MutableIntIntMap;
import kala.control.primitive.IntOption;
import kala.function.IntIntConsumer;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/// Tests primitive int map contracts and hash-table behavior.
public final class IntIntMapTest {
    /// Checks builder ownership and parallel merging through the standard map factory API.
    @Test
    public void mapFactoryTest() {
        var builder = IntIntMap.newMapBuilder(8);
        builder.plusAssign(1, 10);
        var first = builder.build();
        builder.plusAssign(1, 11);
        builder.plusAssign(2, 20);
        assertEquals(IntIntMap.of(1, 10), first);
        assertEquals(IntIntMap.of(1, 11, 2, 20), builder.build());
        assertThrows(NullPointerException.class, () -> builder.plusAssign(null, 0));
        assertThrows(NullPointerException.class, () -> builder.plusAssign(0, null));
        assertThrows(IllegalArgumentException.class, () -> IntIntMap.newMapBuilder(-1));

        var mutableBuilder = MutableIntIntMap.newMapBuilder();
        mutableBuilder.plusAssign(1, 1);
        var mutable = mutableBuilder.build();
        mutableBuilder.plusAssign(1, 2);
        assertEquals(2, mutable.get(1));
        assertSame(mutable, mutableBuilder.build());

        var collected = IntStream.range(0, 1000).boxed().parallel()
                .collect(IntIntMap.factory().collector(value -> value % 37, value -> value));
        var expected = new HashMap<Integer, Integer>();
        IntStream.range(0, 1000).forEach(value -> expected.put(value % 37, value));
        assertMappings(expected, collected);
        assertEquals(collected, IntStream.range(0, 1000).boxed().parallel()
                .collect(MutableIntIntMap.factory().collector(value -> value % 37, value -> value)));
        assertEquals(IntIntMap.of(1, 1, 2, 2, 3, 3),
                MutableIntIntHashMap.of(1, 1, 2, 2, 3, 3));
        assertEquals(4, ImmutableIntIntMap.of(1, 1, 2, 2, 3, 3, 4, 4).size());
        assertEquals(5, MutableIntIntMap.of(1, 1, 2, 2, 3, 3, 4, 4, 5, 5).size());
    }

    /// Checks direct callback invocation with primitive, boxed, and mixed arguments.
    @Test
    public void consumerTest() {
        var sum = new AtomicInteger();
        IntIntConsumer primitive = (key, value) -> sum.addAndGet(key + value);
        Integer key = 2;
        Integer value = 3;
        primitive.accept(2, 3);
        primitive.accept(key, value);
        primitive.accept(2, value);
        primitive.accept(key, 3);
        assertEquals(20, sum.get());

        BiConsumer<Integer, Integer> boxed = primitive::accept;
        boxed.accept(key, value);
        assertEquals(25, sum.get());
        assertThrows(NullPointerException.class, () -> boxed.accept(null, 1));
        assertThrows(NullPointerException.class, () -> boxed.accept(1, null));
    }

    /// Checks unambiguous primitive and boxed traversal, including partially consumed iterators.
    @Test
    public void consumerTraversalTest() {
        var sum = new AtomicInteger();
        IntIntConsumer primitive = (key, value) -> sum.addAndGet(key + value);
        BiConsumer<Integer, Integer> boxed = primitive::accept;
        var map = IntIntMap.of(1, 10);
        map.forEach((key, value) -> sum.addAndGet(key + value));
        map.forEach(primitive);
        map.iterator().forEachPrimitive((key, value) -> sum.addAndGet(key + value));
        map.iterator().forEachPrimitive((int key, int value) -> sum.addAndGet(key + value));
        map.iterator().forEachPrimitive(primitive);
        map.iterator().forEach((key, value) -> sum.addAndGet(key + value));
        map.iterator().forEach(boxed);
        assertEquals(77, sum.get());

        var iterator = IntIntMap.of(1, 10, 2, 20).iterator();
        sum.set(iterator.nextIntKey() + iterator.getIntValue());
        iterator.forEachPrimitive(primitive);
        assertEquals(33, sum.get());
        assertFalse(iterator.hasNext());
        iterator.forEachPrimitive(primitive);
        assertEquals(33, sum.get());
        assertThrows(NullPointerException.class, () -> iterator.forEachPrimitive(null));
        assertThrows(NullPointerException.class, () -> map.iterator().forEachPrimitive(null));

        var failing = map.iterator();
        var failure = new IllegalStateException();
        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> failing.forEachPrimitive((key, value) -> {
                    throw failure;
                })));
        assertEquals(10, failing.getIntValue());
        assertFalse(failing.hasNext());
    }

    /// Exercises factories, duplicate keys, missing values, and boundary keys.
    @Test
    public void factoriesAndQueriesTest() {
        assertTrue(IntIntMap.empty().isEmpty());
        assertEquals(IntIntMap.empty(), IntIntMap.of());
        assertEquals(2, IntIntMap.of(1, 1, 1, 2).get(1));
        int[] keys = {0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 0};
        int[] values = {3, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 4};
        var map = IntIntMap.from(keys, values);
        keys[0] = 42;
        values[4] = 99;
        assertEquals(4, map.size());
        assertEquals(4, map.knownSize());
        assertEquals(4, map.get(0));
        assertEquals(0, map.get(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, map.get(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, map.get(-1));
        assertTrue(map.contains(Integer.MIN_VALUE, 0));
        assertTrue(map.containsValue(Integer.MAX_VALUE));
        assertFalse(map.contains(42, 0));
        assertFalse(map.containsValue(99));
        assertFalse(map.containsKey(42));
        assertEquals(IntOption.none(), map.getOption(42));
        assertNull(map.getOrNull(42));
        assertEquals(17, map.getOrDefault(42, 17));
        assertThrows(NoSuchElementException.class, () -> map.get(42));
        assertThrows(IllegalArgumentException.class, () -> IntIntMap.from(new int[1], new int[0]));
        assertThrows(NullPointerException.class, () -> IntIntMap.from(null, new int[0]));
        assertThrows(NullPointerException.class, () -> IntIntMap.from(new int[0], null));
        assertThrows(IllegalArgumentException.class, () -> new MutableIntIntHashMap(-1));
        assertThrows(IllegalArgumentException.class, () -> new MutableIntIntHashMap(Integer.MAX_VALUE));
        var javaMap = new HashMap<Integer, Integer>();
        javaMap.put(1, null);
        assertThrows(NullPointerException.class, () -> IntIntMap.from(javaMap));
        javaMap.clear();
        javaMap.put(null, 1);
        assertThrows(NullPointerException.class, () -> IntIntMap.from(javaMap));
    }

    /// Checks mutation return values and lazy supplier invocation.
    @Test
    public void mutationsTest() {
        var map = MutableIntIntMap.create();
        assertEquals(IntOption.none(), map.put(0, 0));
        assertEquals(IntOption.some(0), map.put(0, 12));
        assertEquals(IntOption.some(12), map.putIfAbsent(0, 3));
        assertEquals(IntOption.none(), map.putIfAbsent(1, 4));
        var calls = new AtomicInteger();
        assertEquals(12, map.getOrElse(0, calls::incrementAndGet));
        assertEquals(12, map.getOrPut(0, calls::incrementAndGet));
        assertEquals(0, calls.get());
        assertEquals(1, map.getOrElse(2, calls::incrementAndGet));
        assertFalse(map.containsKey(2));
        assertEquals(2, map.getOrPut(2, calls::incrementAndGet));
        assertEquals(2, map.get(2));
        assertThrows(IllegalStateException.class, () -> map.getOrPut(3, () -> {
            throw new IllegalStateException();
        }));
        assertFalse(map.containsKey(3));
        assertThrows(NullPointerException.class, () -> map.getOrPut(0, null));
        assertThrows(NullPointerException.class, () -> map.forEach(null));
        map.putAll(map);
        assertEquals(3, map.size());
        assertEquals(IntOption.some(12), map.remove(0));
        assertEquals(IntOption.none(), map.remove(0));
        map.clear();
        map.clear();
        assertTrue(map.isEmpty());
        map.set(0, 5);
        assertEquals(5, map.get(0));
    }

    /// Verifies primitive and boxed iteration, exhaustion, and invalidation.
    @Test
    public void iteratorsTest() {
        var empty = IntIntMapIterator.empty();
        assertFalse(empty.hasNext());
        assertThrows(IllegalStateException.class, empty::getIntValue);
        assertThrows(NoSuchElementException.class, empty::nextIntKey);
        var map = MutableIntIntHashMap.of(1, 11, 2, 22);
        var it = map.iterator();
        assertThrows(IllegalStateException.class, it::getValue);
        var seen = new HashSet<Integer>();
        int last = 0;
        while (it.hasNext()) {
            assertTrue(it.hasNext());
            int key = it.nextIntKey();
            assertTrue(seen.add(key));
            last = map.get(key);
            assertEquals(last, it.getIntValue());
            assertEquals(last, it.getValue().intValue());
        }
        assertEquals(2, seen.size());
        assertThrows(NoSuchElementException.class, it::nextIntKey);
        assertEquals(last, it.getIntValue());
        assertThrows(UnsupportedOperationException.class, it::remove);
        MapIterator<Integer, Integer> boxed = map.iterator();
        var entry = boxed.next();
        assertEquals(map.get(entry.component1()), entry.component2().intValue());
        assertEquals(2, map.iterator().asKeysIterator().toArray().length);
        assertEquals(33, java.util.Arrays.stream(map.iterator().asValuesIterator().toArray()).sum());
        map.set(1, 111);
        assertThrows(ConcurrentModificationException.class, it::hasNext);
        assertThrows(ConcurrentModificationException.class, it::getIntValue);
        var beforeInsertion = map.iterator();
        map.set(3, 33);
        assertThrows(ConcurrentModificationException.class, beforeInsertion::nextIntKey);
        var beforeRemoval = map.iterator();
        map.remove(3);
        assertThrows(ConcurrentModificationException.class, beforeRemoval::hasNext);
        var beforeClear = map.iterator();
        map.clear();
        assertThrows(ConcurrentModificationException.class, beforeClear::hasNext);
    }

    /// Verifies snapshots, equality, hashing, and immutable update operations.
    @Test
    public void snapshotsAndEqualityTest() {
        var mutable = MutableIntIntHashMap.of(1, 10, 2, 20);
        var copy = mutable.clone();
        var immutable = ImmutableIntIntMap.from(mutable);
        assertEquals(mutable, immutable);
        assertEquals(immutable, mutable);
        assertEquals(mutable.hashCode(), immutable.hashCode());
        assertEquals(immutable, IntIntMap.from(java.util.Map.of(2, 20, 1, 10)));
        assertNotEquals(immutable, IntIntMap.of(1, 10, 2, 21));
        var generic = kala.collection.Map.of(1, 10, 2, 20);
        var objects = IntObjMap.of(1, 10, 2, 20);
        assertEquals(immutable, generic);
        assertEquals(generic, immutable);
        assertEquals(immutable, objects);
        assertEquals(objects, immutable);
        assertEquals(generic.hashCode(), immutable.hashCode());
        assertEquals(objects.hashCode(), immutable.hashCode());
        assertNotEquals(immutable, kala.collection.Map.of(1L, 10, 2L, 20));
        assertNotEquals(immutable, IntObjMap.of(1, 10L, 2, 20L));
        assertNotEquals(immutable, IntObjMap.of(1, null, 2, 20));
        assertNotEquals(immutable, null);
        assertNotEquals(immutable, "map");
        mutable.set(1, 100);
        copy.remove(2);
        assertEquals(10, immutable.get(1));
        assertEquals(20, immutable.get(2));
        var updated = immutable.updated(3, 30);
        assertEquals(3, updated.size());
        assertFalse(immutable.containsKey(3));
        assertFalse(updated.removed(1).containsKey(1));
        assertTrue(updated.containsKey(1));
        assertSame(immutable, immutable.toImmutableMap());
        assertSame(immutable, immutable.removed(99));
        var snapshot = mutable.updated(4, 40);
        mutable.clear();
        assertEquals(100, snapshot.get(1));
        assertEquals(40, snapshot.get(4));
        assertEquals("ImmutableIntIntHashMap{1=10}", ImmutableIntIntHashMap.of(1, 10).toString());
    }

    /// Exercises default methods using an implementation that supplies only an iterator.
    @Test
    public void defaultMethodsTest() {
        IntIntMap map = new AbstractIntIntMap() {
            @Override
            public IntIntMapIterator iterator() {
                return IntIntMap.of(7, 8).iterator();
            }
        };
        assertEquals(1, map.size());
        assertEquals(1, map.knownSize());
        assertFalse(map.isEmpty());
        assertEquals(8, map.get(7));
        assertTrue(map.containsKey(7));
        assertFalse(map.containsKey(9));
        assertEquals(IntIntMap.of(7, 8), map);
        assertEquals(IntIntMap.empty(), map.removed(7));
    }

    /// Compares mixed operations and growth against java.util.HashMap with a fixed seed.
    @Test
    public void randomizedDifferentialTest() {
        var map = new MutableIntIntHashMap();
        var expected = new HashMap<Integer, Integer>();
        var random = new Random(0x19EA72L);
        for (int step = 0; step < 30_000; step++) {
            int key = step % 3 == 0 ? random.nextInt() : random.nextInt(256) - 128;
            int value = random.nextInt();
            switch (random.nextInt(5)) {
                case 0, 1 -> assertEquals(IntOption.ofNullable(expected.put(key, value)), map.put(key, value));
                case 2 -> assertEquals(IntOption.ofNullable(expected.remove(key)), map.remove(key));
                case 3 -> {
                    expected.put(key, value);
                    map.set(key, value);
                }
                case 4 -> assertEquals(expected.getOrDefault(key, 0).intValue(), map.getOrDefault(key, 0));
            }
            if (step % 200 == 0) assertMappings(expected, map);
            if (step % 5000 == 4999) {
                expected.clear();
                map.clear();
            }
        }
        assertMappings(expected, map);
        for (int round = 0; round < 100; round++) {
            for (int key = 0; key < 200; key++) {
                map.set(key, key);
            }
            for (int key = 0; key < 200; key++) {
                assertEquals(IntOption.some(key), map.remove(key));
            }
            assertTrue(map.isEmpty());
        }
    }

    /// Checks every entry in both directions, including duplicate-free traversal.
    private static void assertMappings(HashMap<Integer, Integer> expected, IntIntMap map) {
        assertEquals(expected.size(), map.size());
        var actual = new HashMap<Integer, Integer>();
        map.forEach((key, value) -> assertNull(actual.put(key, value)));
        assertEquals(expected, actual);
        expected.forEach((key, value) -> assertEquals(value.intValue(), map.get(key)));
    }
}
