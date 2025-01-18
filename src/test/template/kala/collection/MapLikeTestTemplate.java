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

import kala.HashCollisionsValue;
import kala.TestValue;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.mutable.MutableArrayList;
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.value.primitive.BooleanVar;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

import static kala.ExtendedAssertions.assertMapEntries;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface MapLikeTestTemplate {

    default Integer[][] data1() {
        return TestData.data1;
    }

    default String[][] data1s() {
        return TestData.data1s;
    }

    default <K, V> @NotNull Tuple2<K, V> entry(K key, V value) {
        return Tuple.of(key, value);
    }

    <K, V> MapLike<K, V> ofEntries(Tuple2<K, V>... entries);

    /// Data:
    /// ```
    ///{
    ///   0: "0",
    ///   1: "1",
    ///   2: "2",
    ///   3: "3",
    ///   4: "4",
    ///   5: "5"
    ///}
    ///```
    private MapLike<Integer, String> testMap() {
        return testMap(5);
    }

    private Seq<Tuple2<Integer, String>> testMapData(int n) {
        return ImmutableSeq.fill(n, i -> Tuple.of(i, String.valueOf(i)));
    }

    private MapLike<Integer, String> testMap(int n) {
        return ofEntries(testMapData(n).toArray(new Tuple2[0]));
    }

    private Seq<Tuple2<HashCollisionsValue, Integer>> hashCollisions(int n) {
        return ImmutableSeq.fill(n, i -> Tuple.of(new HashCollisionsValue(i), i));
    }

    private MapLike<HashCollisionsValue, Integer> testMapHashCollisions(int n) {
        return ofEntries(hashCollisions(n).toArray(new Tuple2[0]));
    }

    @Test
    default void getTest() {
        assertThrows(NoSuchElementException.class, () -> this.<Integer, String>ofEntries().get(0));

        {
            var map = this.testMap();
            assertThrows(NoSuchElementException.class, () -> map.get(-1));
            assertEquals("0", map.get(0));
            assertEquals("1", map.get(1));
            assertEquals("2", map.get(2));
            assertEquals("3", map.get(3));
            assertEquals("4", map.get(4));
            assertThrows(NoSuchElementException.class, () -> map.get(5));
        }

        {
            int n = 100;
            var map = testMapHashCollisions(n);

            assertThrows(NoSuchElementException.class, () -> map.get(new HashCollisionsValue(-1)));
            assertThrows(NoSuchElementException.class, () -> map.get(new HashCollisionsValue(n)));

            for (int i = 0; i < n; i++) {
                assertEquals(i, map.get(new HashCollisionsValue(i)));
            }
        }
    }

    @Test
    default void getOrNullTest() {
        assertNull(this.<Integer, String>ofEntries().getOrNull(0));

        {
            var map = this.testMap();
            assertNull(map.getOrNull(-1));
            assertEquals("0", map.getOrNull(0));
            assertEquals("1", map.getOrNull(1));
            assertEquals("2", map.getOrNull(2));
            assertEquals("3", map.getOrNull(3));
            assertEquals("4", map.getOrNull(4));
            assertNull(map.getOrNull(5));
        }

        {
            int n = 100;
            var map = testMapHashCollisions(n);

            assertNull(map.getOrNull(new HashCollisionsValue(-1)));
            assertNull(map.getOrNull(new HashCollisionsValue(n)));

            for (int i = 0; i < n; i++) {
                assertEquals(i, map.getOrNull(new HashCollisionsValue(i)));
            }
        }
    }

    @Test
    default void getOptionTest() {
        assertEquals(Option.none(), this.<Integer, String>ofEntries().getOption(0));

        {
            var map = this.testMap();
            assertEquals(Option.none(), map.getOption(-1));
            assertEquals(Option.some("0"), map.getOption(0));
            assertEquals(Option.some("1"), map.getOption(1));
            assertEquals(Option.some("2"), map.getOption(2));
            assertEquals(Option.some("3"), map.getOption(3));
            assertEquals(Option.some("4"), map.getOption(4));
            assertEquals(Option.none(), map.getOption(5));
        }

        {
            int n = 100;
            var map = testMapHashCollisions(n);

            assertEquals(Option.none(), map.getOption(new HashCollisionsValue(-1)));
            assertEquals(Option.none(), map.getOption(new HashCollisionsValue(n)));

            for (int i = 0; i < n; i++) {
                assertEquals(Option.some(i), map.getOption(new HashCollisionsValue(i)));
            }
        }
    }

    @Test
    default void getOrDefaultTest() {
        assertNull(this.<Integer, String>ofEntries().getOrDefault(0, null));
        assertEquals("default", this.<Integer, String>ofEntries().getOrDefault(0, "default"));

        var map = this.testMap();
        assertEquals("default", map.getOrDefault(-1, "default"));
        assertEquals("0", map.getOrDefault(0, "default"));
        assertEquals("1", map.getOrDefault(1, "default"));
        assertEquals("2", map.getOrDefault(2, "default"));
        assertEquals("3", map.getOrDefault(3, "default"));
        assertEquals("4", map.getOrDefault(4, "default"));
        assertEquals("default", map.getOrDefault(5, "default"));

        assertNull(map.getOrDefault(-1, null));
        assertEquals("0", map.getOrDefault(0, null));
        assertEquals("1", map.getOrDefault(1, null));
        assertEquals("2", map.getOrDefault(2, null));
        assertEquals("3", map.getOrDefault(3, null));
        assertEquals("4", map.getOrDefault(4, null));
        assertNull(map.getOrDefault(5, null));
    }

    @Test
    default void getOrThrowTest() throws Throwable {
        class MyException extends Throwable {
        }

        assertThrows(MyException.class, () -> this.<Integer, String>ofEntries().getOrThrow(0, MyException::new));

        var map = this.testMap();
        assertThrows(MyException.class, () -> map.getOrThrow(-1, MyException::new));
        assertEquals("0", map.getOrThrow(0, MyException::new));
        assertEquals("1", map.getOrThrow(1, MyException::new));
        assertEquals("2", map.getOrThrow(2, MyException::new));
        assertEquals("3", map.getOrThrow(3, MyException::new));
        assertEquals("4", map.getOrThrow(4, MyException::new));
        assertThrows(MyException.class, () -> map.getOrThrow(5, MyException::new));
    }

    @Test
    default void containsKeyTest() {
        {
            var empty = this.<Integer, String>ofEntries();
            assertFalse(empty.containsKey(-1));
            assertFalse(empty.containsKey(0));
            assertFalse(empty.containsKey(1));
            assertFalse(empty.containsKey(Integer.MIN_VALUE));
            assertFalse(empty.containsKey(Integer.MAX_VALUE));
        }

        {
            final int n = 5;
            var testMap = testMap(n);

            assertFalse(testMap.containsKey(-1));
            assertFalse(testMap.containsKey(n));
            for (int i = 0; i < n; i++) {
                assertTrue(testMap.containsKey(i));
            }
        }

        {
            int n = 100;
            var map = testMapHashCollisions(n);

            assertFalse(map.containsKey(new HashCollisionsValue(-1)));
            assertFalse(map.containsKey(new HashCollisionsValue(n)));

            for (int i = 0; i < n; i++) {
                assertTrue(map.containsKey(new HashCollisionsValue(i)));
            }
        }
    }

    @Test
    default void updatedTest() {
        {
            var empty = this.<Integer, String>ofEntries();
            assertMapEntries(List.of(entry(0, "0")), empty.updated(0, "0"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "1")), empty.updated(0, "0").updated(1, "1"));
            assertMapEntries(List.of(entry(0, "1")), empty.updated(0, "0").updated(0, "1"));
        }

        {
            final int n = 5;
            var map = this.testMap(n);

            assertMapEntries(List.of(entry(-1, "-1"), entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.updated(-1, "-1"));
            assertMapEntries(List.of(entry(0, "---"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.updated(0, "---"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "---"), entry(2, "2"), entry(3, "3"), entry(4, "4")), map.updated(1, "---"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "1"), entry(2, "---"), entry(3, "3"), entry(4, "4")), map.updated(2, "---"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "---"), entry(4, "4")), map.updated(3, "---"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "---")), map.updated(4, "---"));
            assertMapEntries(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4"), entry(5, "---")), map.updated(5, "---"));
        }

        {
            final int n = 100;

            var data = hashCollisions(n);
            var map = testMapHashCollisions(n);

            assertMapEntries(data, map);

            assertMapEntries(data.prepended(Tuple.of(new HashCollisionsValue(-1), Integer.MAX_VALUE)),
                    map.updated(new HashCollisionsValue(-1), Integer.MAX_VALUE));
            assertMapEntries(data.appended(Tuple.of(new HashCollisionsValue(n), Integer.MAX_VALUE)),
                    map.updated(new HashCollisionsValue(n), Integer.MAX_VALUE));

            for (int i = 0; i < n; i++) {
                assertMapEntries(data.updated(i, Tuple.of(new HashCollisionsValue(i), Integer.MAX_VALUE)),
                        map.updated(new HashCollisionsValue(i), Integer.MAX_VALUE));
            }
        }
    }

    @Test
    default void removedTest() {
        var empty = this.<Integer, String>ofEntries();
        assertMapEntries(List.of(), empty.removed(0));

        var testMap = testMap();
        assertMapEntries(List.of(entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), testMap.removed(0));
        assertMapEntries(List.of(entry(0, "0"), entry(2, "2"), entry(3, "3"), entry(4, "4")), testMap.removed(1));
        assertMapEntries(List.of(entry(0, "0"), entry(1, "1"), entry(2, "2"), entry(3, "3"), entry(4, "4")), testMap.removed(5));
    }

    @Test
    default void forEachTest() {
        {
            final var empty = this.<Integer, String>ofEntries();
            final var holder = new BooleanVar(false);
            empty.forEach((k, v) -> holder.value = true);
            assertFalse(holder.value);
        }

        class Helper<K, V> implements BiConsumer<K, V>, AutoCloseable {
            final java.util.HashMap<K, V> values = new java.util.HashMap<>();

            @Override
            public void accept(K k, V v) {
                assertTrue(values.remove(k, v), () -> "Value not removed: key=" + k + ", value=" + v);
            }

            public void close() {
                assertTrue(values.isEmpty(), () -> "The remaining values: " + values);
            }
        }

        try (var helper = new Helper<Integer, String>()) {
            final int n = 5;
            for (int i = 0; i < n; i++) {
                helper.values.put(i, Integer.toString(i));
            }

            testMap(n).forEach(helper);
        }

        try (var helper = new Helper<HashCollisionsValue, Integer>()) {
            final int n = 100;
            for (int i = 0; i < n; i++) {
                helper.values.put(new HashCollisionsValue(i), i);
            }

            testMapHashCollisions(n).forEach(helper);
        }
    }
}
