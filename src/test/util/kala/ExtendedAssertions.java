/*
 * Copyright 2024 Glavo
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
package kala;

import kala.collection.MapLike;
import kala.collection.SetLike;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ExtendedAssertions {
    public static void assertIteratorEquals(Iterable<?> expected, Iterator<?> actual) {
        assertIteratorEquals(expected, actual, null);
    }

    public static void assertIteratorEquals(Iterable<?> expected, Iterator<?> actual, Supplier<String> messageSupplier) {
        assertIterableEquals(expected, Iterators.collect(actual, new ArrayList<>()), messageSupplier);
    }

    public static void assertSetElements(Iterable<?> expected, SetLike<?> actual) {
        int count = 0;
        for (Object value : expected) {
            count++;
            if (!actual.contains(value)) fail(actual + " does not contain element " + value);
        }

        if (count != actual.size()) fail(actual + " contains redundant elements");
    }

    public static <K, V> void assertMapEntries(Iterable<? extends Tuple2<K, ?>> expected, MapLike<K, ?> actual) {
        var expectedMap = new java.util.LinkedHashMap<K, Object>();
        for (var pair : expected) {
            if (expectedMap.put(pair.getKey(), pair.getValue()) != null) {
                throw new AssertionError("duplicate key " + pair.getKey());
            }
        }

        Supplier<String> messageSupplier = () -> "Expected: %s\nActual: %s".formatted(expectedMap, actual.joinToString(", ", "{", "}"));

        actual.iterator().forEach((k, v) -> {
            assertTrue(expectedMap.containsKey(k), () -> "Actual Key: %s, Actual Value: %s\n".formatted(k, v) + messageSupplier.get());
            assertEquals(expectedMap.get(k), v, () -> "Actual Key: %s, Actual Value: %s\n".formatted(k, v) + messageSupplier.get());
        });

        assertEquals(expectedMap.size(), actual.size(), messageSupplier);

    }
}
