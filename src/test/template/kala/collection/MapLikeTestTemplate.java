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
import kala.collection.immutable.ImmutableSeq;
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.value.primitive.BooleanVar;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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

    default <K, V> MapLike<K, V> ofEntries(Tuple2<K, V>... entries) {
        return from(Arrays.asList(entries));
    }

    <K, V> MapLike<K, V> from(Iterable<Tuple2<K, V>> entries);

    private Seq<Tuple2<Integer, String>> testData(int n) {
        return ImmutableSeq.fill(n, i -> Tuple.of(i, String.valueOf(i)));
    }

    private Seq<Tuple2<HashCollisionsValue, Integer>> testHashCollisionsData(int n) {
        return ImmutableSeq.fill(n, i -> Tuple.of(new HashCollisionsValue(i), i));
    }

    @Test
    default void getTest() {
        assertThrows(NoSuchElementException.class, () -> this.<Integer, String>ofEntries().get(0));

        {
            final int n = 10;
            final var map = from(testData(n));

            for (int i = 0; i < n; i++) {
                assertEquals(String.valueOf(i), map.get(i));
            }
            assertThrows(NoSuchElementException.class, () -> map.get(-1));
            assertThrows(NoSuchElementException.class, () -> map.get(n));
        }

        {
            final int n = 100;
            final var map = from(testHashCollisionsData(n));

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
            final int n = 10;
            final var map = from(testData(n));

            for (int i = 0; i < n; i++) {
                assertEquals(String.valueOf(i), map.getOrNull(i));
            }
            assertNull(map.getOrNull(-1));
            assertNull(map.getOrNull(n));
        }

        {
            final int n = 100;
            final var map = from(testHashCollisionsData(n));

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
            final int n = 10;
            final var map = from(testData(n));

            assertEquals(Option.none(), map.getOption(-1));
            assertEquals(Option.none(), map.getOption(n));

            for (int i = 0; i < n; i++) {
                assertEquals(Option.some(String.valueOf(i)), map.getOption(i));
            }
        }

        {
            final int n = 100;
            final var map = from(testHashCollisionsData(n));

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

        {
            final int n = 10;
            final var map = from(testData(n));

            for (int i = 0; i < n; i++) {
                assertEquals(String.valueOf(i), map.getOrDefault(i, "default"));
            }

            assertEquals("default", map.getOrDefault(-1, "default"));
            assertEquals("default", map.getOrDefault(n, "default"));
            assertNull(map.getOrDefault(-1, null));
            assertNull(map.getOrDefault(n, null));
        }
    }

    @Test
    default void getOrThrowTest() throws Throwable {
        class MyException extends Throwable {
        }

        {
            assertThrows(MyException.class, () -> this.<Integer, String>ofEntries().getOrThrow(0, MyException::new));
        }

        {
            final int n = 10;
            final var map = from(testData(n));

            for (int i = 0; i < n; i++) {
                assertEquals(String.valueOf(i), map.getOrThrow(i, MyException::new));
            }
            assertThrows(MyException.class, () -> map.getOrThrow(-1, MyException::new));
            assertThrows(MyException.class, () -> map.getOrThrow(n, MyException::new));
        }
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
            final int n = 10;
            var testMap = from(testData(n));

            assertFalse(testMap.containsKey(-1));
            assertFalse(testMap.containsKey(n));
            for (int i = 0; i < n; i++) {
                assertTrue(testMap.containsKey(i));
            }
        }

        {
            int n = 100;
            var map = from(testHashCollisionsData(n));

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
            final int n = 10;
            final var data = testData(n);
            final var map = from(data);

            for (int i = 0; i < n; i++) {
                assertMapEntries(data.updated(i, Tuple.of(i, "---")), map.updated(i, "---"));
            }

            assertMapEntries(data.prepended(Tuple.of(-1, "---")), map.updated(-1, "---"));
            assertMapEntries(data.appended(Tuple.of(n, "---")), map.updated(n, "---"));
        }

        {
            final int n = 100;

            var data = testHashCollisionsData(n);
            var map = from(data);

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
        {
            var empty = this.<Integer, String>ofEntries();
            assertMapEntries(List.of(), empty.removed(0));
            assertMapEntries(List.of(), empty.removed(1));
            assertMapEntries(List.of(), empty.removed(-1));
        }

        {
            final int n = 10;
            final var data = testData(n);
            final var map = from(data);

            for (int i = 0; i < n; i++) {
                assertMapEntries(data.removedAt(i), map.removed(i));
            }

            assertMapEntries(data, map.removed(-1));
            assertMapEntries(data, map.removed(n));
        }

        {
            final int n = 100;

            final var data = testHashCollisionsData(n);
            final var map = from(data);

            for (int i = 0; i < n; i++) {
                assertMapEntries(data.removedAt(i), map.removed(new HashCollisionsValue(i)));
            }

            assertMapEntries(data, map.removed(new HashCollisionsValue(-1)));
            assertMapEntries(data, map.removed(new HashCollisionsValue(n)));
        }
    }

    @Test
    default void forEachTest() {
        {
            final var empty = this.<Integer, String>ofEntries();
            final var holder = new BooleanVar(false);
            empty.forEach((k, v) -> holder.value = true);
            assertFalse(holder.value);
        }

        forEachTest(testData(10));
        forEachTest(testHashCollisionsData(100));
    }

    private <K, V> void forEachTest(Seq<Tuple2<K, V>> data) {
        final java.util.HashMap<K, V> values = new java.util.HashMap<>();
        for (var entry : data) {
            values.put(entry.getKey(), entry.getValue());
        }

        from(data).forEach((k, v) ->
                assertTrue(values.remove(k, v), () -> "Value not removed: key=" + k + ", value=" + v));

        assertTrue(values.isEmpty(), () -> "The remaining values: " + values);
    }
}
