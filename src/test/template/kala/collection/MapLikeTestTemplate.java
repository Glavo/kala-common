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
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

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

    <K, V> MapFactory<K, V, ?, ? extends MapLike<K, V>> factory();

    default <K, V> MapLike<K, V> ofEntries(Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }

    private MapLike<Integer, String> testMap() {
        return ofEntries(
                Tuple.of(0, "0"),
                Tuple.of(1, "1"),
                Tuple.of(2, "2"),
                Tuple.of(3, "3"),
                Tuple.of(4, "4")
        );
    }

    @Test
    default void getTest() {
        assertThrows(NoSuchElementException.class, () -> this.<Integer, String>ofEntries().get(0));

        var map = this.testMap();
        assertThrows(NoSuchElementException.class, () -> map.get(-1));
        assertEquals("0", map.get(0));
        assertEquals("1", map.get(1));
        assertEquals("2", map.get(2));
        assertEquals("3", map.get(3));
        assertEquals("4", map.get(4));
        assertThrows(NoSuchElementException.class, () -> map.get(5));
    }

    @Test
    default void getOrNullTest() {
        assertNull(this.<Integer, String>ofEntries().getOrNull(0));

        var map = this.testMap();
        assertNull(map.getOrNull(-1));
        assertEquals("0", map.getOrNull(0));
        assertEquals("1", map.getOrNull(1));
        assertEquals("2", map.getOrNull(2));
        assertEquals("3", map.getOrNull(3));
        assertEquals("4", map.getOrNull(4));
        assertNull(map.getOrNull(5));
    }

    @Test
    default void getOptionTest() {
        assertEquals(Option.none(), this.<Integer, String>ofEntries().getOption(0));

        var map = this.testMap();
        assertEquals(Option.none(), map.getOption(-1));
        assertEquals(Option.some("0"), map.getOption(0));
        assertEquals(Option.some("1"), map.getOption(1));
        assertEquals(Option.some("2"), map.getOption(2));
        assertEquals(Option.some("3"), map.getOption(3));
        assertEquals(Option.some("4"), map.getOption(4));
        assertEquals(Option.none(), map.getOption(5));
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
}
