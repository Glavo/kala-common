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

import kala.collection.base.primitive.IntObjMapIterator;
import kala.collection.immutable.primitive.ImmutableIntObjHashMap;
import kala.collection.immutable.primitive.ImmutableIntObjMap;
import kala.collection.mutable.primitive.MutableIntObjHashMap;
import kala.collection.mutable.primitive.MutableIntObjMap;
import kala.control.Option;
import kala.function.IntObjConsumer;
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

/// Tests object-valued int maps, including null and covariant copying.
public final class IntObjMapTest {
    /// Checks snapshot builders, nullable values, and merging through map factories.
    @Test
    public void mapFactoryTest() {
        var builder = IntObjMap.<String>newMapBuilder(8);
        builder.plusAssign(1, null);
        var first = builder.build();
        builder.plusAssign(1, "one");
        builder.plusAssign(2, "two");
        assertEquals(Option.some(null), first.getOption(1));
        assertFalse(first.containsKey(2));
        assertEquals(IntObjMap.of(1, "one", 2, "two"), builder.build());
        assertThrows(NullPointerException.class, () -> builder.plusAssign(null, "invalid"));
        assertThrows(IllegalArgumentException.class, () -> IntObjMap.newMapBuilder(-1));

        var mutableBuilder = MutableIntObjMap.<String>newMapBuilder();
        mutableBuilder.plusAssign(1, "one");
        var mutable = mutableBuilder.build();
        mutableBuilder.plusAssign(1, null);
        assertNull(mutable.get(1));
        assertSame(mutable, mutableBuilder.build());

        var collected = IntStream.range(0, 1000).boxed().parallel()
                .collect(IntObjMap.<String>factory().collector(value -> value % 37,
                        value -> value % 2 == 0 ? null : value.toString()));
        var expected = new HashMap<Integer, String>();
        IntStream.range(0, 1000).forEach(value ->
                expected.put(value % 37, value % 2 == 0 ? null : Integer.toString(value)));
        assertMappings(expected, collected);
        assertEquals(collected, IntStream.range(0, 1000).boxed().parallel()
                .collect(MutableIntObjMap.<String>factory().collector(value -> value % 37,
                        value -> value % 2 == 0 ? null : value.toString())));
        assertEquals(IntObjMap.of(1, "a", 2, "b", 3, "c"),
                MutableIntObjHashMap.of(1, "a", 2, "b", 3, "c"));
        assertEquals(4, ImmutableIntObjMap.of(1, "a", 2, "b", 3, "c", 4, null).size());
        assertEquals(5, MutableIntObjMap.of(1, "a", 2, "b", 3, "c", 4, null, 5, "e").size());
    }

    /// Checks callbacks with boxed value types, mixed arguments, and null values.
    @Test
    public void consumerTest() {
        var seen = new HashMap<Integer, String>();
        IntObjConsumer<String> primitive = seen::put;
        primitive.accept(1, null);
        assertTrue(seen.containsKey(1));
        BiConsumer<Integer, String> boxed = primitive::accept;
        boxed.accept(2, "two");
        assertEquals("two", seen.get(2));
        assertThrows(NullPointerException.class, () -> boxed.accept(null, "invalid"));

        var numbers = new HashMap<Integer, Integer>();
        IntObjConsumer<Integer> integers = numbers::put;
        Integer key = 2;
        Integer value = 20;
        integers.accept(1, 10);
        integers.accept(key, value);
        integers.accept(3, value);
        integers.accept(key, 21);
        integers.accept(4, null);
        assertEquals(10, numbers.get(1).intValue());
        assertEquals(21, numbers.get(2).intValue());
        assertEquals(20, numbers.get(3).intValue());
        assertTrue(numbers.containsKey(4));
        assertNull(numbers.get(4));

        var flags = new HashMap<Integer, Boolean>();
        IntObjConsumer<Boolean> booleans = flags::put;
        booleans.accept(1, true);
        booleans.accept(key, Boolean.FALSE);
        assertEquals(java.util.Map.of(1, true, 2, false), flags);
    }

    /// Checks primitive and boxed traversal with inferred lambdas and contravariant callbacks.
    @Test
    public void consumerTraversalTest() {
        var seen = new HashMap<Integer, Object>();
        IntObjConsumer<Object> primitive = seen::put;
        BiConsumer<Integer, Object> boxed = primitive::accept;
        IntObjMap.<String>of(1, null).iterator().forEachPrimitive(primitive);
        IntObjMap.of(2, "two").iterator().forEach((key, value) -> seen.put(key, value));
        IntObjMap.of(3, "three").iterator().forEachPrimitive((int key, String value) -> seen.put(key, value));
        IntObjMap.of(4, "four").iterator().forEach(boxed);
        IntObjMap.of(5, "five").forEach(primitive);
        IntObjMap.of(6, "six").forEach((key, value) -> seen.put(key, value));
        IntObjMap.of(7, "seven").iterator().forEachPrimitive((key, value) -> seen.put(key, value));
        assertEquals(7, seen.size());
        assertTrue(seen.containsKey(1));
        assertNull(seen.get(1));
        assertEquals("two", seen.get(2));
        assertEquals("three", seen.get(3));
        assertEquals("four", seen.get(4));
        assertEquals("five", seen.get(5));
        assertEquals("six", seen.get(6));
        assertEquals("seven", seen.get(7));

        var iterator = IntObjMap.of(1, "one", 2, "two").iterator();
        int consumedKey = iterator.nextIntKey();
        seen.clear();
        iterator.forEachPrimitive(primitive);
        assertEquals(1, seen.size());
        assertFalse(seen.containsKey(consumedKey));
        assertFalse(iterator.hasNext());
        iterator.forEachPrimitive((key, value) -> fail("Iterator should be exhausted"));
        assertThrows(NullPointerException.class, () -> iterator.forEachPrimitive(null));
        assertThrows(NullPointerException.class,
                () -> IntObjMap.of(1, "one").iterator().forEachPrimitive(null));

        var failing = IntObjMap.of(1, "one").iterator();
        var failure = new IllegalStateException();
        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> failing.forEachPrimitive((key, value) -> {
                    throw failure;
                })));
        assertEquals("one", failing.getValue());
        assertFalse(failing.hasNext());
    }

    /// Distinguishes absent mappings from present null values across query and mutation APIs.
    @Test
    public void nullValuesAndFactoriesTest() {
        var map = MutableIntObjMap.<String>create();
        assertTrue(map.put(0, null).isEmpty());
        assertTrue(map.getOption(0).isDefined());
        assertNull(map.get(0));
        assertNull(map.getOrDefault(0, "default"));
        assertNull(map.getOrNull(0));
        assertTrue(map.containsKey(0));
        assertTrue(map.contains(0, null));
        assertFalse(map.contains(1, null));
        assertTrue(map.containsValue(null));
        assertEquals(Option.some(null), map.putIfAbsent(0, "replacement"));
        var calls = new AtomicInteger();
        assertNull(map.getOrPut(0, () -> {
            calls.incrementAndGet();
            return "replacement";
        }));
        assertNull(map.getOrElse(0, () -> {
            calls.incrementAndGet();
            return "replacement";
        }));
        assertEquals(0, calls.get());
        assertEquals(Option.some(null), map.put(0, "zero"));
        assertEquals(Option.some("zero"), map.remove(0));
        assertEquals(Option.none(), map.remove(0));
        assertThrows(NoSuchElementException.class, () -> map.get(0));
        assertEquals("fallback", map.getOrElse(0, () -> "fallback"));
        assertFalse(map.containsKey(0));
        assertThrows(IllegalStateException.class, () -> map.getOrPut(2, () -> {
            throw new IllegalStateException();
        }));
        assertFalse(map.containsKey(2));
        assertNull(map.getOrPut(1, () -> null));
        assertEquals(Option.some(null), map.remove(1));
        int[] keys = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0};
        String[] values = {"min", "max", "zero", null};
        var immutable = IntObjMap.from(keys, values);
        keys[0] = 2;
        values[3] = "changed";
        assertEquals(3, immutable.size());
        assertEquals("min", immutable.get(Integer.MIN_VALUE));
        assertEquals("max", immutable.get(Integer.MAX_VALUE));
        assertNull(immutable.get(0));
        assertEquals(IntObjMap.of(1, "second"), IntObjMap.of(1, "first", 1, "second"));
        assertEquals(IntObjMap.empty(), IntObjMap.of());
        assertThrows(IllegalArgumentException.class, () -> IntObjMap.from(new int[1], new String[0]));
        assertThrows(NullPointerException.class, () -> IntObjMap.from(null, new String[0]));
        assertThrows(NullPointerException.class, () -> IntObjMap.from(new int[0], null));
        var source = new HashMap<Integer, String>();
        source.put(0, null);
        assertEquals(Option.some(null), IntObjMap.from(source).getOption(0));
        source.put(null, "invalid");
        assertThrows(NullPointerException.class, () -> IntObjMap.from(source));
    }

    /// Checks shallow immutable snapshots, clone independence, and covariance.
    @Test
    public void snapshotsAndEqualityTest() {
        var mutable = MutableIntObjHashMap.of(0, (String) null, 1, "one");
        var immutable = ImmutableIntObjMap.from(mutable);
        var clone = mutable.clone();
        assertEquals(mutable, immutable);
        assertEquals(immutable, mutable);
        assertEquals(mutable.hashCode(), immutable.hashCode());
        assertNotEquals(immutable, IntObjMap.of(0, "zero", 1, "one"));
        assertNotEquals(immutable, IntObjMap.of(2, (String) null, 1, "one"));
        var generic = kala.collection.Map.of(0, null, 1, "one");
        assertEquals(immutable, generic);
        assertEquals(generic, immutable);
        assertEquals(generic.hashCode(), immutable.hashCode());
        assertNotEquals(immutable, kala.collection.Map.of(0L, null, 1L, "one"));
        mutable.set(0, "zero");
        clone.clear();
        assertNull(immutable.get(0));
        assertEquals(2, immutable.size());
        assertEquals("new", immutable.updated(0, "new").get(0));
        assertNull(immutable.get(0));
        assertEquals(1, immutable.removed(0).size());
        assertSame(immutable, immutable.removed(9));
        assertSame(immutable, immutable.toImmutableMap());
        var snapshot = mutable.updated(2, "two");
        mutable.clear();
        assertEquals("zero", snapshot.get(0));
        assertEquals("two", snapshot.get(2));
        IntObjMap<Number> numbers = IntObjMap.from(IntObjMap.of(1, 2));
        assertEquals(2, numbers.get(1));
        MutableIntObjMap<Number> mutableNumbers = MutableIntObjMap.from(numbers);
        mutableNumbers.putAll(IntObjMap.of(2, 3.5));
        assertEquals(3.5, mutableNumbers.get(2));
        mutableNumbers.putAll(mutableNumbers);
        assertEquals(2, mutableNumbers.size());
        var value = new StringBuilder("value");
        assertSame(value, IntObjMap.of(1, value).get(1));
        assertEquals("ImmutableIntObjHashMap{0=null}", ImmutableIntObjHashMap.of(0, null).toString());
    }

    /// Verifies null iteration, primitive keys, boxed entries, and iterator invalidation.
    @Test
    public void iteratorsTest() {
        var empty = IntObjMapIterator.empty();
        assertFalse(empty.hasNext());
        assertThrows(IllegalStateException.class, empty::getValue);
        assertThrows(NoSuchElementException.class, empty::nextIntKey);
        var map = MutableIntObjHashMap.of(0, (String) null, 1, "one");
        var it = map.iterator();
        assertThrows(IllegalStateException.class, it::getValue);
        var seen = new HashSet<Integer>();
        String last = null;
        while (it.hasNext()) {
            var entry = it.next();
            assertTrue(seen.add(entry.component1()));
            last = map.get(entry.component1());
            assertEquals(last, entry.component2());
            assertEquals(last, it.getValue());
        }
        assertEquals(2, seen.size());
        assertThrows(NoSuchElementException.class, it::nextIntKey);
        assertEquals(last, it.getValue());
        assertThrows(UnsupportedOperationException.class, it::remove);
        assertEquals(2, map.iterator().asKeysIterator().toArray().length);
        var valueIterator = map.iterator().asValuesIterator();
        assertTrue(valueIterator.hasNext());
        valueIterator.next();
        valueIterator.next();
        assertFalse(valueIterator.hasNext());
        assertThrows(NullPointerException.class, () -> map.forEach(null));
        map.remove(0);
        assertThrows(ConcurrentModificationException.class, it::hasNext);
        assertThrows(ConcurrentModificationException.class, it::getValue);
        var replacement = map.iterator();
        map.set(1, "changed");
        assertThrows(ConcurrentModificationException.class, replacement::nextIntKey);
    }

    /// Exercises inherited query and copy behavior on an iterator-only implementation.
    @Test
    public void defaultMethodsTest() {
        IntObjMap<String> map = new AbstractIntObjMap<>() {
            @Override
            public IntObjMapIterator<String> iterator() {
                return IntObjMap.<String>of(0, null).iterator();
            }
        };
        assertEquals(1, map.size());
        assertEquals(1, map.knownSize());
        assertFalse(map.isEmpty());
        assertNull(map.get(0));
        assertTrue(map.containsKey(0));
        assertFalse(map.containsKey(1));
        assertTrue(map.containsValue(null));
        assertFalse(map.containsValue("missing"));
        assertEquals(IntObjMap.of(0, null), map);
        assertEquals(IntObjMap.empty(), map.removed(0));
    }

    /// Compares mixed nullable-value operations against java.util.HashMap with a fixed seed.
    @Test
    public void randomizedDifferentialTest() {
        var map = new MutableIntObjHashMap<String>();
        var expected = new HashMap<Integer, String>();
        var random = new Random(0xF317B2L);
        for (int step = 0; step < 30_000; step++) {
            int key = step % 3 == 0 ? random.nextInt() : random.nextInt(256) - 128;
            String value = random.nextBoolean() ? null : Integer.toString(random.nextInt());
            boolean present = expected.containsKey(key);
            switch (random.nextInt(5)) {
                case 0, 1 -> {
                    String old = expected.put(key, value);
                    assertEquals(present ? Option.some(old) : Option.none(), map.put(key, value));
                }
                case 2 -> {
                    String old = expected.remove(key);
                    assertEquals(present ? Option.some(old) : Option.none(), map.remove(key));
                }
                case 3 -> {
                    expected.put(key, value);
                    map.set(key, value);
                }
                case 4 -> assertEquals(expected.getOrDefault(key, "missing"), map.getOrDefault(key, "missing"));
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
                map.set(key, null);
            }
            for (int key = 0; key < 200; key++) {
                assertEquals(Option.some(null), map.remove(key));
            }
            assertTrue(map.isEmpty());
        }
    }

    /// Checks size, unique keys, lookup results, and null-valued mappings.
    private static void assertMappings(HashMap<Integer, String> expected, IntObjMap<String> map) {
        assertEquals(expected.size(), map.size());
        var actual = new HashMap<Integer, String>();
        map.forEach((key, value) -> {
            assertFalse(actual.containsKey(key));
            actual.put(key, value);
        });
        assertEquals(expected, actual);
        expected.forEach((key, value) -> {
            assertTrue(map.containsKey(key));
            assertEquals(value, map.get(key));
        });
    }
}
