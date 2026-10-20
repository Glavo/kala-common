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

import kala.collection.base.primitive.IntIntMapIterator;
import kala.collection.base.primitive.IntObjMapIterator;
import kala.collection.immutable.primitive.ImmutableIntIntTreeMap;
import kala.collection.immutable.primitive.ImmutableIntObjTreeMap;
import kala.collection.immutable.primitive.ImmutableSortedIntIntMap;
import kala.collection.immutable.primitive.ImmutableSortedIntObjMap;
import kala.collection.mutable.primitive.MutableIntIntTreeMap;
import kala.collection.mutable.primitive.MutableIntObjTreeMap;
import kala.collection.mutable.primitive.MutableSortedIntIntMap;
import kala.collection.mutable.primitive.MutableSortedIntObjMap;
import kala.comparator.primitive.IntComparator;
import kala.control.Option;
import kala.control.primitive.IntOption;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/// Tests ordering, mutation, snapshots, and factory contracts of primitive tree maps.
public final class IntTreeMapTest {
    /// Checks natural and reversed ordering, extreme keys, and empty-map boundaries.
    @Test
    public void orderingTest() {
        int[] keys = {0, Integer.MAX_VALUE, -1, Integer.MIN_VALUE, 0};
        int[] values = {1, 2, 3, 4, 5};
        String[] objects = {"one", "two", "three", "four", null};
        for (IntComparator comparator : Arrays.asList(null, IntComparator.reverseOrder())) {
            var ints = MutableIntIntTreeMap.from(comparator, keys, values);
            var refs = MutableIntObjTreeMap.from(comparator, keys, objects);
            var expectedInts = new TreeMap<Integer, Integer>(comparator);
            var expectedRefs = new TreeMap<Integer, String>(comparator);
            for (int i = 0; i < keys.length; i++) {
                expectedInts.put(keys[i], values[i]);
                expectedRefs.put(keys[i], objects[i]);
            }
            assertMappings(expectedInts, ints);
            assertMappings(expectedRefs, refs);
            assertSame(comparator, ints.comparator());
            assertSame(comparator, refs.comparator());
            assertEquals(5, ints.get(0));
            assertEquals(Option.some(null), refs.getOption(0));
            ints.clear();
            refs.clear();
            assertThrows(NoSuchElementException.class, ints::firstKey);
            assertThrows(NoSuchElementException.class, ints::lastKey);
            assertThrows(NoSuchElementException.class, refs::firstKey);
            assertThrows(NoSuchElementException.class, refs::lastKey);
            assertThrows(NoSuchElementException.class, () -> ints.get(0));
            assertThrows(NoSuchElementException.class, () -> refs.get(0));
            assertEquals(17, ints.getOrDefault(0, 17));
            assertEquals("missing", refs.getOrDefault(0, "missing"));
            assertEquals(IntOption.none(), ints.getOption(0));
            assertEquals(Option.none(), refs.getOption(0));
        }
    }

    /// Exercises deletion of two-child nodes and replacement of both successor components.
    @Test
    public void successorDeletionTest() {
        var ints = new MutableIntIntTreeMap();
        var refs = new MutableIntObjTreeMap<String>();
        var expectedInts = new TreeMap<Integer, Integer>();
        var expectedRefs = new TreeMap<Integer, String>();
        for (int key : new int[]{4, 2, 6, 1, 3, 5, 7}) {
            ints.set(key, key * 10);
            refs.set(key, key == 5 ? null : Integer.toString(key));
            expectedInts.put(key, key * 10);
            expectedRefs.put(key, key == 5 ? null : Integer.toString(key));
        }
        for (int key : new int[]{4, 6, 2, 1, 7, 5, 3}) {
            assertEquals(IntOption.some(expectedInts.remove(key)), ints.remove(key));
            assertEquals(Option.some(expectedRefs.remove(key)), refs.remove(key));
            assertMappings(expectedInts, ints);
            assertMappings(expectedRefs, refs);
        }
        assertEquals(IntOption.none(), ints.remove(0));
        assertEquals(Option.none(), refs.remove(0));
    }

    /// Compares mixed operations against Java tree maps under three total orderings.
    @Test
    public void randomizedDifferentialTest() {
        IntComparator magnitudeOrder = (left, right) -> {
            int comparison = Long.compare(Math.abs((long) left), Math.abs((long) right));
            return comparison == 0 ? Integer.compare(left, right) : comparison;
        };
        for (IntComparator comparator : Arrays.asList(null, IntComparator.reverseOrder(), magnitudeOrder)) {
            var ints = new MutableIntIntTreeMap(comparator);
            var refs = new MutableIntObjTreeMap<String>(comparator);
            var expectedInts = new TreeMap<Integer, Integer>(comparator);
            var expectedRefs = new TreeMap<Integer, String>(comparator);
            var random = new Random(0x7EE4A9L);
            for (int step = 0; step < 30_000; step++) {
                int key = step % 7 == 0 ? random.nextInt() : random.nextInt(512) - 256;
                int value = random.nextInt();
                String ref = step % 3 == 0 ? null : Integer.toString(value);
                switch (random.nextInt(5)) {
                    case 0 -> {
                        boolean present = expectedRefs.containsKey(key);
                        String previous = expectedRefs.put(key, ref);
                        assertEquals(present ? Option.some(previous) : Option.none(), refs.put(key, ref));
                        assertEquals(IntOption.ofNullable(expectedInts.put(key, value)), ints.put(key, value));
                    }
                    case 1 -> {
                        boolean present = expectedRefs.containsKey(key);
                        String previous = expectedRefs.remove(key);
                        assertEquals(present ? Option.some(previous) : Option.none(), refs.remove(key));
                        assertEquals(IntOption.ofNullable(expectedInts.remove(key)), ints.remove(key));
                    }
                    case 2 -> {
                        ints.set(key, value);
                        refs.set(key, ref);
                        expectedInts.put(key, value);
                        expectedRefs.put(key, ref);
                    }
                    case 3 -> {
                        assertEquals(expectedInts.getOrDefault(key, 0).intValue(), ints.getOrDefault(key, 0));
                        assertEquals(expectedRefs.getOrDefault(key, "absent"), refs.getOrDefault(key, "absent"));
                    }
                    case 4 -> {
                        assertEquals(IntOption.ofNullable(expectedInts.putIfAbsent(key, value)),
                                ints.putIfAbsent(key, value));
                        if (expectedRefs.containsKey(key)) {
                            assertEquals(Option.some(expectedRefs.get(key)), refs.putIfAbsent(key, ref));
                        } else {
                            assertEquals(Option.none(), refs.putIfAbsent(key, ref));
                            expectedRefs.put(key, ref);
                        }
                    }
                }
                if (step % 250 == 0) {
                    assertMappings(expectedInts, ints);
                    assertMappings(expectedRefs, refs);
                    assertMappings(expectedInts, ints.clone());
                    assertMappings(expectedRefs, refs.clone());
                }
                if (step % 5000 == 4999) {
                    ints.clear();
                    refs.clear();
                    expectedInts.clear();
                    expectedRefs.clear();
                }
            }
            assertMappings(expectedInts, ints);
            assertMappings(expectedRefs, refs);
        }
    }

    /// Checks independent snapshots, comparator preservation, updates, and map equality.
    @Test
    public void snapshotsAndEqualityTest() {
        var comparator = IntComparator.reverseOrder();
        var ints = MutableIntIntTreeMap.of(comparator, 1, 10, 2, 20, 3, 30);
        var refs = MutableIntObjTreeMap.of(comparator, 1, "one", 2, null, 3, "three");
        var intSnapshot = ImmutableIntIntTreeMap.from(ints);
        var refSnapshot = ImmutableIntObjTreeMap.from(refs);
        var intClone = ints.clone();
        var refClone = refs.clone();
        ints.clear();
        refs.clear();
        intClone.remove(2);
        refClone.set(2, "two");
        assertEquals(IntIntMap.of(1, 10, 2, 20, 3, 30), intSnapshot);
        assertEquals(intSnapshot, IntIntMap.of(1, 10, 2, 20, 3, 30));
        assertEquals(IntObjMap.of(1, "one", 2, null, 3, "three"), refSnapshot);
        assertEquals(refSnapshot, IntObjMap.of(1, "one", 2, null, 3, "three"));
        var generic = kala.collection.Map.of(1, 10, 2, 20, 3, 30);
        assertEquals(generic, intSnapshot);
        assertEquals(intSnapshot, generic);
        assertEquals(generic.hashCode(), intSnapshot.hashCode());
        assertSame(comparator, intSnapshot.comparator());
        assertSame(comparator, refSnapshot.comparator());
        assertSame(comparator, intClone.comparator());
        assertSame(comparator, refClone.comparator());
        assertSame(intSnapshot, intSnapshot.toImmutableMap());
        assertSame(refSnapshot, refSnapshot.toImmutableMap());
        assertSame(intSnapshot, intSnapshot.removed(99));
        assertSame(refSnapshot, refSnapshot.removed(99));
        assertEquals(Option.some(null), refSnapshot.getOption(2));
        var updatedInts = intSnapshot.updated(4, 40).removed(1);
        var updatedRefs = refSnapshot.updated(4, "four").removed(1);
        assertSame(comparator, updatedInts.comparator());
        assertSame(comparator, updatedRefs.comparator());
        assertEquals(4, updatedInts.firstKey());
        assertEquals(4, updatedRefs.firstKey());
        assertFalse(intSnapshot.containsKey(4));
        assertFalse(refSnapshot.containsKey(4));
        assertSame(comparator, ImmutableIntIntTreeMap.of(comparator, 1, 1).removed(1).comparator());
        assertSame(comparator, ImmutableIntObjTreeMap.of(comparator, 1, "one").removed(1).comparator());
        assertSame(comparator, intClone.toImmutableMap().comparator());
        assertSame(comparator, refClone.toImmutableMap().comparator());
        assertSame(comparator, intClone.updated(7, 70).comparator());
        assertSame(comparator, refClone.removed(1).comparator());
        assertEquals("ImmutableIntIntTreeMap{3=30, 2=20, 1=10}", intSnapshot.toString());
        assertEquals("ImmutableIntObjTreeMap{3=three, 2=null, 1=one}", refSnapshot.toString());
    }

    /// Checks builder ownership, comparator-aware factories, and parallel collector merging.
    @Test
    public void factoriesTest() {
        var comparator = IntComparator.reverseOrder();
        var builder = ImmutableIntIntTreeMap.newMapBuilder(comparator);
        builder.plusAssign(1, 10);
        var first = builder.build();
        builder.plusAssign(1, 11);
        builder.plusAssign(2, 20);
        assertEquals(10, first.get(1));
        assertFalse(first.containsKey(2));
        assertEquals(11, builder.build().get(1));
        assertSame(comparator, first.comparator());
        assertThrows(NullPointerException.class, () -> builder.plusAssign(null, 0));
        assertThrows(NullPointerException.class, () -> builder.plusAssign(0, null));

        var refBuilder = ImmutableSortedIntObjMap.<String>newMapBuilder(comparator);
        refBuilder.plusAssign(1, null);
        var refFirst = refBuilder.build();
        refBuilder.plusAssign(1, "one");
        assertEquals(Option.some(null), refFirst.getOption(1));
        assertEquals("one", refBuilder.build().get(1));
        assertSame(comparator, refFirst.comparator());
        assertThrows(NullPointerException.class, () -> refBuilder.plusAssign(null, "invalid"));

        var mutableBuilder = MutableSortedIntIntMap.newMapBuilder(comparator);
        mutableBuilder.plusAssign(1, 1);
        var mutable = mutableBuilder.build();
        mutableBuilder.plusAssign(2, 2);
        assertSame(mutable, mutableBuilder.build());
        assertEquals(2, mutable.firstKey());
        var mutableRefBuilder = MutableIntObjTreeMap.<String>newMapBuilder(comparator);
        mutableRefBuilder.plusAssign(1, null);
        var mutableRef = mutableRefBuilder.build();
        mutableRefBuilder.plusAssign(2, "two");
        assertSame(mutableRef, mutableRefBuilder.build());
        assertEquals(2, mutableRef.firstKey());

        var collectedInts = IntStream.range(0, 1000).boxed().parallel()
                .collect(ImmutableIntIntTreeMap.factory(comparator).collector(i -> i % 37, i -> i));
        var collectedRefs = IntStream.range(0, 1000).boxed().parallel()
                .collect(MutableIntObjTreeMap.<String>factory(comparator)
                        .collector(i -> i % 37, i -> i % 2 == 0 ? null : i.toString()));
        var expectedInts = new TreeMap<Integer, Integer>(comparator);
        var expectedRefs = new TreeMap<Integer, String>(comparator);
        IntStream.range(0, 1000).forEach(i -> {
            expectedInts.put(i % 37, i);
            expectedRefs.put(i % 37, i % 2 == 0 ? null : Integer.toString(i));
        });
        assertMappings(expectedInts, collectedInts);
        assertMappings(expectedRefs, collectedRefs);
        assertSame(comparator, collectedInts.comparator());
        assertSame(comparator, collectedRefs.comparator());
        assertEquals(36, collectedInts.sortedMapFactory().newMapBuilder().build()
                .updated(36, 1).firstKey());
        var refFactoryBuilder = collectedRefs.<Number>sortedMapFactory(comparator).newMapBuilder();
        refFactoryBuilder.plusAssign(1, 1.5);
        assertSame(comparator, refFactoryBuilder.build().comparator());
        assertEquals(1.5, refFactoryBuilder.build().get(1));

        assertEquals(5, SortedIntIntMap.of(1, 1, 2, 2, 3, 3, 4, 4, 5, 5).size());
        assertEquals(5, MutableSortedIntObjMap.of(comparator, 1, "a", 2, "b", 3, "c", 4, "d", 5, "e").size());
        assertEquals(4, ImmutableSortedIntIntMap.of(comparator, 1, 1, 2, 2, 3, 3, 4, 4).firstKey());
        assertEquals(3, SortedIntObjMap.of(1, "a", 2, "b", 3, "c").size());
        assertTrue(SortedIntIntMap.empty().isEmpty());
        assertTrue(SortedIntObjMap.empty(comparator).isEmpty());
        assertTrue(MutableSortedIntIntMap.create().isEmpty());
        assertSame(comparator, MutableSortedIntObjMap.create(comparator).comparator());
    }

    /// Checks array validation, null entries, and Java sorted-map comparator preservation.
    @Test
    public void copyingTest() {
        assertThrows(IllegalArgumentException.class,
                () -> MutableIntIntTreeMap.from(new int[1], new int[0]));
        assertThrows(IllegalArgumentException.class,
                () -> ImmutableIntObjTreeMap.from(new int[0], new String[1]));
        assertThrows(NullPointerException.class,
                () -> MutableIntIntTreeMap.from(null, new int[0]));
        assertThrows(NullPointerException.class,
                () -> ImmutableIntObjTreeMap.from(new int[0], null));
        assertThrows(NullPointerException.class,
                () -> MutableIntObjTreeMap.from((IntObjMapLike<String>) null));
        var badInts = new HashMap<Integer, Integer>();
        badInts.put(1, null);
        assertThrows(NullPointerException.class, () -> ImmutableIntIntTreeMap.from(badInts));
        badInts.clear();
        badInts.put(null, 1);
        assertThrows(NullPointerException.class, () -> MutableIntIntTreeMap.from(badInts));
        var badRefs = new HashMap<Integer, String>();
        badRefs.put(null, "invalid");
        assertThrows(NullPointerException.class, () -> ImmutableIntObjTreeMap.from(badRefs));

        var javaInts = new TreeMap<Integer, Integer>(java.util.Comparator.reverseOrder());
        var javaRefs = new TreeMap<Integer, String>(java.util.Comparator.reverseOrder());
        javaInts.put(1, 10);
        javaInts.put(2, 20);
        javaRefs.put(1, null);
        javaRefs.put(2, "two");
        assertMappings(javaInts, MutableIntIntTreeMap.from(javaInts));
        assertMappings(javaInts, ImmutableSortedIntIntMap.from(javaInts));
        assertMappings(javaRefs, ImmutableIntObjTreeMap.from(javaRefs));
        assertMappings(javaRefs, SortedIntObjMap.from(javaRefs));
        var natural = MutableIntIntTreeMap.from(null, javaInts);
        assertNull(natural.comparator());
        assertEquals(1, natural.firstKey());
        assertSame(IntComparator.reverseOrder(),
                MutableIntObjTreeMap.from(MutableIntObjTreeMap.of(IntComparator.reverseOrder(), 1, 2)).comparator());

        int[] keys = {2, 1};
        String[] values = {"two", null};
        var copied = ImmutableIntObjTreeMap.from(keys, values);
        keys[0] = 3;
        values[0] = "changed";
        assertEquals("two", copied.get(2));
        assertEquals(Option.some(null), copied.getOption(1));
        MutableIntObjTreeMap<Number> numbers = MutableIntObjTreeMap.from(IntObjMap.of(1, 2));
        numbers.set(2, 3.5);
        assertEquals(3.5, numbers.get(2));
    }

    /// Verifies iterator state, fail-fast checks, and no-op mutation behavior.
    @Test
    public void iteratorsTest() {
        var ints = MutableIntIntTreeMap.of(2, 20, 1, 10);
        var refs = MutableIntObjTreeMap.of(2, "two", 1, null);
        var intIterator = ints.iterator();
        var refIterator = refs.iterator();
        assertThrows(IllegalStateException.class, intIterator::getIntValue);
        assertThrows(IllegalStateException.class, refIterator::getValue);
        assertEquals(1, intIterator.nextIntKey());
        assertEquals(10, intIterator.getIntValue());
        assertEquals(1, refIterator.nextIntKey());
        assertNull(refIterator.getValue());
        assertEquals(2, intIterator.nextKey());
        assertEquals(20, intIterator.getValue());
        assertEquals(2, refIterator.nextKey());
        assertEquals("two", refIterator.getValue());
        assertFalse(intIterator.hasNext());
        assertFalse(refIterator.hasNext());
        assertThrows(NoSuchElementException.class, intIterator::nextIntKey);
        assertThrows(NoSuchElementException.class, refIterator::nextIntKey);
        assertEquals(20, intIterator.getIntValue());
        assertEquals("two", refIterator.getValue());
        assertThrows(UnsupportedOperationException.class, intIterator::remove);
        assertThrows(UnsupportedOperationException.class, refIterator::remove);
        ints.remove(99);
        refs.remove(99);
        assertFalse(intIterator.hasNext());
        assertFalse(refIterator.hasNext());
        ints.set(1, 11);
        refs.set(1, "one");
        assertThrows(ConcurrentModificationException.class, intIterator::hasNext);
        assertThrows(ConcurrentModificationException.class, refIterator::getValue);
        var beforeInsert = ints.iterator();
        ints.set(3, 30);
        assertThrows(ConcurrentModificationException.class, beforeInsert::nextIntKey);
        var beforeRemove = refs.iterator();
        refs.remove(2);
        assertThrows(ConcurrentModificationException.class, beforeRemove::hasNext);
        var beforeClear = ints.iterator();
        ints.clear();
        assertThrows(ConcurrentModificationException.class, beforeClear::hasNext);
        var empty = ints.iterator();
        ints.clear();
        assertFalse(empty.hasNext());
        ints.set(1, 1);
        assertThrows(ConcurrentModificationException.class, empty::hasNext);
    }

    /// Checks comparator-equivalent keys and rollback when comparison fails.
    @Test
    public void comparatorTest() {
        IntComparator decades = (left, right) -> Integer.compare(left / 10, right / 10);
        var ints = MutableIntIntTreeMap.of(decades, 11, 1, 19, 2);
        var refs = MutableIntObjTreeMap.of(decades, 11, "first", 19, null);
        assertEquals(1, ints.size());
        assertEquals(11, ints.firstKey());
        assertEquals(2, ints.get(15));
        assertEquals(11, refs.firstKey());
        assertEquals(Option.some(null), refs.getOption(15));
        assertEquals(IntOption.some(2), ints.remove(18));
        assertEquals(Option.some(null), refs.remove(18));
        assertTrue(ints.isEmpty());
        assertTrue(refs.isEmpty());

        var failure = new IllegalArgumentException("Rejected key");
        IntComparator rejecting = (left, right) -> {
            if (left == 9 || right == 9) {
                throw failure;
            }
            return Integer.compare(left, right);
        };
        var map = new MutableIntIntTreeMap(rejecting);
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> map.set(9, 9)));
        assertTrue(map.isEmpty());
        map.set(1, 1);
        var iterator = map.iterator();
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> map.put(9, 9)));
        assertEquals(1, map.size());
        assertEquals(1, iterator.nextIntKey());
        assertEquals(1, iterator.getIntValue());
    }

    /// Checks that monotonic insertions and deletions retain logarithmic search depth.
    @Test
    public void balancedTreeTest() {
        var comparisons = new AtomicInteger();
        IntComparator comparator = (left, right) -> {
            comparisons.incrementAndGet();
            return Integer.compare(left, right);
        };
        var ints = new MutableIntIntTreeMap(comparator);
        var refs = new MutableIntObjTreeMap<Integer>(comparator);
        for (int key = 0; key < 4096; key++) {
            ints.set(key, key);
            refs.set(key, key);
        }
        for (int key = 0; key < 4096; key += 2) {
            assertEquals(IntOption.some(key), ints.remove(key));
            assertEquals(Option.some(key), refs.remove(key));
        }
        for (int key = 1; key < 4096; key += 2) {
            comparisons.set(0);
            assertEquals(key, ints.get(key));
            assertTrue(comparisons.get() <= 24);
            comparisons.set(0);
            assertEquals(key, refs.get(key).intValue());
            assertTrue(comparisons.get() <= 24);
        }
        var clone = ints.clone();
        for (int key = 4095; key >= 1; key -= 2) {
            assertEquals(IntOption.some(key), clone.remove(key));
        }
        assertTrue(clone.isEmpty());
        assertEquals(2048, ints.size());
    }

    /// Checks sorted-interface defaults on implementations that only supply ordered access.
    @Test
    public void sortedDefaultsTest() {
        var comparator = IntComparator.reverseOrder();
        SortedIntIntMap ints = new SortedIntIntMap() {
            @Override
            public IntComparator comparator() {
                return comparator;
            }

            @Override
            public int firstKey() {
                return 2;
            }

            @Override
            public int lastKey() {
                return 1;
            }

            @Override
            public IntIntMapIterator iterator() {
                return MutableIntIntTreeMap.of(comparator, 1, 10, 2, 20).iterator();
            }
        };
        SortedIntObjMap<String> refs = new SortedIntObjMap<>() {
            @Override
            public IntComparator comparator() {
                return comparator;
            }

            @Override
            public int firstKey() {
                return 2;
            }

            @Override
            public int lastKey() {
                return 1;
            }

            @Override
            public IntObjMapIterator<String> iterator() {
                return MutableIntObjTreeMap.of(comparator, 1, "one", 2, null).iterator();
            }
        };
        assertSame(comparator, ints.toImmutableMap().comparator());
        assertSame(comparator, refs.toImmutableMap().comparator());
        assertSame(comparator, ints.updated(3, 30).comparator());
        assertSame(comparator, refs.updated(3, "three").comparator());
        assertSame(comparator, ints.removed(2).comparator());
        assertSame(comparator, refs.removed(2).comparator());
        assertEquals(3, ints.updated(3, 30).firstKey());
        assertEquals(3, refs.updated(3, "three").firstKey());
        assertEquals(2, ints.size());
        assertEquals(2, refs.size());
    }

    /// Compares all mappings, key order, and endpoints with a Java reference map.
    private static void assertMappings(TreeMap<Integer, Integer> expected, SortedIntIntMap actual) {
        assertEquals(expected.size(), actual.size());
        var keys = new ArrayList<Integer>();
        actual.forEach((key, value) -> {
            keys.add(key);
            assertEquals(expected.get(key).intValue(), value);
        });
        assertEquals(new ArrayList<>(expected.keySet()), keys);
        expected.forEach((key, value) -> assertEquals(value.intValue(), actual.get(key)));
        if (!expected.isEmpty()) {
            assertEquals(expected.firstKey().intValue(), actual.firstKey());
            assertEquals(expected.lastKey().intValue(), actual.lastKey());
        }
    }

    /// Compares nullable mappings, key order, and endpoints with a Java reference map.
    private static void assertMappings(TreeMap<Integer, String> expected, SortedIntObjMap<String> actual) {
        assertEquals(expected.size(), actual.size());
        var keys = new ArrayList<Integer>();
        actual.forEach((key, value) -> {
            keys.add(key);
            assertTrue(expected.containsKey(key));
            assertEquals(expected.get(key), value);
        });
        assertEquals(new ArrayList<>(expected.keySet()), keys);
        expected.forEach((key, value) -> assertEquals(Option.some(value), actual.getOption(key)));
        if (!expected.isEmpty()) {
            assertEquals(expected.firstKey().intValue(), actual.firstKey());
            assertEquals(expected.lastKey().intValue(), actual.lastKey());
        }
    }
}
