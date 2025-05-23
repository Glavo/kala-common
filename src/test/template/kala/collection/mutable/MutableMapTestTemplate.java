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
package kala.collection.mutable;

import kala.collection.MapTestTemplate;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.function.Supplier;

import static kala.ExtendedAssertions.assertMapEntries;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SuppressWarnings("unchecked")
public interface MutableMapTestTemplate extends MapTestTemplate {
    @Override
    <K, V> MapFactory<K, V, ?, ? extends MutableMap<K, V>> factory();

    @Override
    @SuppressWarnings("unchecked")
    default <K, V> MutableMap<K, V> ofEntries(Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }

    @Override
    default <K, V> MutableMap<K, V> from(Iterable<Tuple2<K, V>> entries) {
        return this.<K, V>factory().from(entries);
    }

    @TestFactory
    default DynamicNode getOrPutTestFactory() {
        return DynamicContainer.dynamicContainer("getOrPutTest", List.of(
                dynamicTest("mutationInCallback", () -> {
                    MutableMap<String, String> hm = ofEntries();

                    Supplier<String> add = () -> {
                        // add enough elements to resize the hash table in the callback
                        for (int i = 1; i <= 100000; i++) {
                            hm.set(String.valueOf(i), "callback");
                        }
                        return "str";
                    };

                    hm.getOrPut("0", add);
                    assertEquals(100001, hm.size());
                    assertEquals("str", hm.get("0"));
                }),
                dynamicTest("evalOnce", () -> {
                    MutableMap<Integer, Integer> hm = ofEntries();
                    int[] i = new int[]{0};
                    hm.getOrPut(0, () -> {
                        i[0] += 1;
                        return i[0];
                    });
                    assertEquals(1, hm.get(0));
                }),
                dynamicTest("noEval", () -> {
                    MutableMap<Integer, Integer> hm = ofEntries();
                    hm.put(0, 0);
                    assertEquals(0, hm.getOrPut(0, Assertions::fail));
                }),
                dynamicTest("keyIdempotence", () -> {
                    MutableMap<String, String> hm = ofEntries();
                    String key = "key";
                    hm.getOrPut(key, () -> {
                        hm.getOrPut(key, () -> "value1");
                        return "value2";
                    });

                    assertEquals(1, hm.size());
                    assertEquals("value2", hm.get(key));
                })
        ));
    }

    @Test
    default void clearTest() {
        var map = this.<String, Integer>ofEntries();
        map.clear();
        assertMapEntries(List.of(), map);

        map = this.ofEntries(Tuple.of("A", 1));
        map.clear();
        assertMapEntries(List.of(), map);

        map = this.ofEntries(Tuple.of("A", 1), Tuple.of("B", 2), Tuple.of("C", 3));
        map.clear();
        assertMapEntries(List.of(), map);
    }
}
